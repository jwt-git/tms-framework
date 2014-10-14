
package org.tmsframework.util.net;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;

/**
 * ftp工具类
 * @author fangdw
 *
 */
public class FTPUtil {

	protected static final Log log = LogFactory.getLog(FTPUtil.class);
	
	/**
	 * 获取FtpClient
	 * @param address
	 * @param userName
	 * @param password
	 * @return 
	 * @throws IOException 
	 * @throws SocketException 
	 */
	public static FTPClient  getFtpClient(String address,String userName,String password) throws Exception {
		FTPClient ftpClient = new FTPClient();
		ftpClient.connect(address);
		ftpClient.login(userName, password); 
		return ftpClient;
	}
	
	/**
	 * 下载文件
	 * @param ftpClient
	 * @param workingDirectory
	 * @param localPath 本地路径
	 * @param fileName	带下载文件名
	 * @return
	 * @throws IOException 
	 */
	public static boolean downloadFile(FTPClient ftpClient,String localPath,String fileName,String encoding,String workingDirectory) throws Exception{
		FileOutputStream fos = null; 
		//判断文件是否存在
		//设置目录
		
		File pathFile = new File(localPath);
		//如果文件路径不存在,创建文件路径
		if(!pathFile.exists()){
			if(!pathFile.mkdirs()){
				return false;
			}
		}
		
		ftpClient.changeWorkingDirectory(workingDirectory);
		boolean judgeFileExist = false;
		String[] names = ftpClient.listNames();
        for(int i = 0 ; i<names.length ; i++){
        	if(names[i].equals(fileName)){
        		judgeFileExist = true;
        		break;
        	}
        }
        //如果是文件存在
        if(!judgeFileExist){
        	return false;
        }
		
		fos = new FileOutputStream(localPath+File.separator+fileName);
		ftpClient.setBufferSize(1024); 
        //设置文件类型（二进制） 
		
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE); 
        ftpClient.retrieveFile(fileName, fos); 
        IOUtils.closeQuietly(fos); 
		ftpClient.disconnect(); 
		return true;
	}
	
	/**
	 * 上传文件
	 * @param ftpClient
	 * @param workingDirectory
	 * @param localPath 本地文件保存地址
	 * @param fileName	
	 * @return
	 * @throws IOException 
	 */
	public static boolean uploadFile(FTPClient ftpClient,String localPath,String fileName,String encoding,String workingDirectory) throws Exception{
		File file = new File(localPath+File.separator+fileName); 
		
		FileInputStream fis = null;
		if(!file.exists()){
			return false;
		}else{
			//判断服务器是否存在该目录
			String[] paths = workingDirectory.split("\\/");
			for(int i = 0 ; i < paths.length ; i ++){
				if(!"".equals(paths[i])){
					if(!ftpClient.changeWorkingDirectory(paths[i])){
						if(!ftpClient.makeDirectory(paths[i])){
							return false;
						}else{
							ftpClient.changeWorkingDirectory(paths[i]);
						}
					}
				}
			}
			fis = new FileInputStream(file);
			ftpClient.setBufferSize(1024); 
            //设置编码格式
            ftpClient.setControlEncoding(encoding); 
            //设置文件类型（二进制） 
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE); 
            ftpClient.storeFile(fileName, fis); 
		}
		IOUtils.closeQuietly(fis); 
		ftpClient.disconnect(); 
		return true;
	}
	
	public static void main(String[] args) throws Exception {
		FTPClient ftpClient = FTPUtil.getFtpClient("127.0.0.1", "ftpUser", "@a123456");
		FTPUtil.uploadFile(ftpClient, "C:/Users/fangdw/Desktop/test.txt", "test123.txt", "utf-8", "111/123");
	}
	
}
