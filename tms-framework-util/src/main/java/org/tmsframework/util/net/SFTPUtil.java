package org.tmsframework.util.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * 
 * 功能说明:
 * <p>
 * 系统版本: v1.0<br>
 * 开发人员: wukan <br>
 * 时间: 2014-9-11 <br>
 */
public class SFTPUtil {

	private static Log logger = LogFactory.getLog(SFTPUtil.class);
	
	public static final String pathSplit = "/";

	public static int timeout = 2000;
	
	public static  ChannelSftp connect(FTPServerConfigure serverConfigure) throws Exception {
		return connect(serverConfigure.getHost(), serverConfigure.getPort(),
				serverConfigure.getUsername(), serverConfigure.getPassword());
	}

	/**
	 * 连接sftp服务器
	 * 
	 * @param host
	 *            主机
	 * @param port
	 *            端口
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return
	 * @throws Exception 
	 */
	public static ChannelSftp connect(String host, int port, String username,
			String password) throws Exception {
		ChannelSftp sftp = null;
		try {
			JSch jsch = new JSch();
			jsch.getSession(username, host, port);
			Session sshSession = jsch.getSession(username, host, port);

			if (logger.isDebugEnabled()) {
				logger.debug(host + ":" + port + ",username=" + username
						+ ".Session created.");
			}

			sshSession.setPassword(password);

			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.setTimeout(timeout); // 设置timeout时间
			sshSession.connect();// 通过sshSession建立链接

			if (logger.isDebugEnabled()) {
				logger.debug(host + ":" + port + ",username=" + username
						+ ".Session connected.");
			}

			Channel channel = sshSession.openChannel("sftp");// 打开SFTP通道
			channel.connect();
			sftp = (ChannelSftp) channel;
			if (logger.isDebugEnabled()) {
				logger.debug("Connected successfully to host = " + host
						+ ",as userName = " + username + ", returning: "
						+ channel);
			}
		} catch (Exception e) {
			logger.error("Failure to connect " + host + ":" + port
					+ ",username=" + username, e);
			throw e;
		}
		
		return sftp;
	}

	/**
	 * 断开sftp服务器
	 * 
	 */
	public static void disconnect(ChannelSftp sftp) {
		if (sftp != null) {
			if (sftp.isConnected()) {
				sftp.disconnect();
			}
		}
		
		Session _sshSession;
		try {
			_sshSession = sftp.getSession();
			if ( _sshSession != null && _sshSession.isConnected()) {
				_sshSession.disconnect();
			}
		} catch (JSchException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 上传文件
	 * 
	 * @param directory
	 *            上传的目录
	 * @param uploadFile
	 *            要上传的文件
	 * @param sftp
	 * @throws  
	 * @throws Exception 
	 */
	public static void upload(FTPServerConfigure server) throws Exception  {
		ChannelSftp sftp  = connect(server);
		try {
			sftp.cd(server.getDirectory());
		} catch (Exception e) {
			creteStpDir(sftp,server.getDirectory());
		}
		File file = new File(server.getLocalFile());
		sftp.put(new FileInputStream(file), file.getName());
		disconnect(sftp);
	}
	
	static void creteStpDir(ChannelSftp sftp ,String dir) throws SftpException {
		if(dir.startsWith(pathSplit)){
			logger.error(dir+"目录格式错误，不能以/开头。");
			throw new IllegalArgumentException("目录格式错误，不能以/开头。");
		}
		
		if(dir.contains("\\")){
			logger.error(dir+"目录格式错误，不能包含\\。");
			throw new IllegalArgumentException("目录格式错误，不能包含\\。");
		}
		
		String[] data = dir.split(pathSplit);
		
		for(int i = 0;i<data.length;i++){
			String tempDir = data[i];
			try{
				sftp.mkdir(tempDir);
				sftp.cd(tempDir);
			}catch (Exception e) {
				sftp.cd(tempDir);
			}
		}
	}

	/**
	 * 下载文件
	 * 
	 * @param directory
	 *            下载目录
	 * @param downloadFile
	 *            下载的文件
	 * @param saveFile
	 *            存在本地的路径
	 * @param sftp
	 */
	public static boolean download(FTPServerConfigure serverConfigure) {
		ChannelSftp sftp = null;
		try {
			sftp = connect(serverConfigure);
			sftp.cd(serverConfigure.getDirectory());
			File file = new File(serverConfigure.getLocalDirectory()+File.separator + serverConfigure.getServerFile());
			
			File dir = new File(serverConfigure.getLocalDirectory());
			if(!dir.exists()){
				dir.mkdirs();
			}
			
			sftp.get(serverConfigure.getServerFile(), new FileOutputStream(file));
			return true;
		} catch (Exception e) {
			logger.error("downloaded the file " +serverConfigure.getServerFile() +" failed：",e);
			return false;
		}finally{
			disconnect(sftp);
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param directory
	 *            要删除文件所在目录
	 * @param deleteFile
	 *            要删除的文件
	 * @param sftp
	 */
	public void delete(String directory, String deleteFile, ChannelSftp sftp) {
		try {
			sftp.cd(directory);
			sftp.rm(deleteFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 打开指定目录
	 * 
	 * @param sftp
	 *            ChannelSftp
	 * @param directory
	 *            directory
	 * @return 是否打开目录
	 */
	public static boolean openDirs(ChannelSftp sftp, String directory) {
		try {
			sftp.cd(directory);
			return true;
		} catch (SftpException e) {
			logger.error("openDir Exception : " + e);
			return false;
		}
	}

	/**
	 * 列出目录下的文件
	 * 
	 * @param directory
	 *            要列出的目录
	 * @param sftp
	 * @return
	 * @throws SftpException
	 */
	public Vector listFiles(String directory, ChannelSftp sftp)
			throws SftpException {
		return sftp.ls(directory);
	}

	/**
	 * 下载文件.
	 * 
	 * @param ftpDir
	 *            存放下载文件的SFTP路径
	 * @param locDir
	 *            下载的文件 SFTP上的文件名称
	 * @param ftpFileName
	 *            FTP上的文件名称
	 * @param deleteFtpFile
	 *            the delete ftp file
	 * @return 本地文件对象
	 * @throws FileNotFoundException
	 *             FileNotFoundException
	 */
	public File download(ChannelSftp sftp, String ftpDir, String locDir,
			String ftpFileName, boolean deleteFtpFile)
			throws FileNotFoundException {
			
		
		
		File file = null;
		FileOutputStream output = null;
		String localDir = "";// CommonTools.buildLocalDir(locDir).append(File.separator).append(ftpFileName).toString();
		try {
			String now = sftp.pwd();
			// if (!StringTools.isNullOrNone(ftpDir))
			// {
			sftp.cd(ftpDir);
			// }
			file = new File(localDir);
			output = new FileOutputStream(file);
			sftp.get(ftpFileName, output);
			sftp.cd(now);
			if (deleteFtpFile) {
				sftp.rm(ftpFileName);
			}
		} catch (SftpException e) {
			logger.error("Failed to download", e);
		} finally {
			if (null != output) {
				try {
					output.close();
				} catch (IOException e) {
					logger.error("create localFile failed:" + e);
				}
			}
		}
		return file;
	}

}