/**
 * 
 */
package org.tmsframework.util.key;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * AES算法密钥的生成类
 * 		用法一：直接调用generate()方法，程序自动随机生成密钥
 * 		用法二：调用generate()方法时传入项目名称和项目代码，生成唯一密钥
 * 
 * @author sam.zhang
 *
 */
public class AESKeyGenerator implements Generator {
	
	public final static String CONFIG_FILE_PATH="setting.properties";
	
	public final static String PROJECT_NAME="projectName";
	
	public final static String PROJECT_CODE="projectCode";
	
	private final static int KEY_LENGTH = 16;
	
	private Log _log = LogFactory.getLog(AESKeyGenerator.class);
	
	private final static byte[] SAND_BOX = 
					{(byte)0xF0,(byte)0x74,(byte)0x38,(byte)0x0C
					,(byte)0xF1,(byte)0x75,(byte)0x39,(byte)0x0D
					,(byte)0xF2,(byte)0x76,(byte)0x3A,(byte)0x0E
					,(byte)0xF3,(byte)0x77,(byte)0x3B,(byte)0x0F};

	/* (non-Javadoc)
	 * @see com.hundsun.network.melody.tool.Generator#generate(java.lang.String[])
	 */
	@Override
	public String generate(String... strings) {
		byte[] result = new byte[KEY_LENGTH];
		if(strings!=null && strings.length>0){
			String inputString = getInputString(strings);
			_log.debug("取到输入字符「"+inputString+"」");
			byte[] inputs = inputString.getBytes();
			_log.debug("取到的字节长度「"+inputs.length+"」");
			if(inputs.length>KEY_LENGTH){
				result = compress(inputs);
			}else{
				result = extend(inputs);
			}
			result = cleanInvisibleChar(result);
		}else{
			result = randomChar();
		}
		return byteToString(result);
	}

	private String byteToString(byte[] result) {
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<result.length;i++){
			sb.append((char)result[i]);
		}
		return sb.toString();
	}

	/**
	 * 生成随机字符串
	 * @return
	 */
	private byte[] randomChar() {
		byte[] b = new byte[KEY_LENGTH];
		boolean isEnough = false;
		int i=0;
		while(true){
			if(isEnough){
				break;
			}
			int random = (int)Math.round(Math.random()*128);
			if(random>32 && random<127){
				b[i] = (byte)random;
				i++;
			}
			if(i==16){
				isEnough=true;
			}
		}
		return b;
	}

	private byte[] cleanInvisibleChar(byte[] data) {
		for(int i=0;i<data.length;i++){
			if((data[i]^(byte)0xFF) < 0 || (int)data[i] < 32){
				data[i] = doClean(data[i]);
			}
		}
		return data;
	}

	private byte doClean(byte b) {
		byte a = b;
		if((int)b < 0){
			a = (byte)(b & (byte)0x7F);
		}
		if((int)a==0 || (int)a==127){
			a='*';
		}else{
			while(a<=32){
				a=(byte)(a<<1);
			}
		} 
		return a;
	}
	
	/**
	 * 用户输入的太短，需要扩展一下
	 * @param inputs
	 * @return
	 */
	private byte[] extend(byte[] inputs) {
		byte[] b = new byte[KEY_LENGTH];
		for(int i=0,k=0;i<KEY_LENGTH;i++,k++){
			if(k==inputs.length){
				k=0;
			}
			b[i] = (byte)(SAND_BOX[i]^inputs[k]);
		}
		return b;
	}

	/**
	 * 用户输入的太长，需要压缩一下
	 * @param inputs
	 * @return
	 */
	private byte[] compress(byte[] inputs) {
		int mod = inputs.length % KEY_LENGTH;
		int quotient = inputs.length / KEY_LENGTH;
		int i=0;
		int curPoint=0;
		byte[] result = new byte[KEY_LENGTH];
		for(int k=0;k<KEY_LENGTH;k++,i++){
			int num = quotient;
			if(i < mod){
				num = quotient+1;
			}
			byte[] dealedChar = getDealChar(inputs,curPoint,num);
			result[k] = dealWithChar(dealedChar);
			curPoint = curPoint+num;
		}
		return result;
	}

	private byte dealWithChar(byte[] dealedChar) {
		byte b = (byte)0xFF;
		for(int i=0; i<dealedChar.length; i++){
			b = (byte)(b^dealedChar[i]);
		}
		return b;
	}

	private byte[] getDealChar(byte[] inputs, int curPoint, int num) {
		byte[] temp = new byte[num];
		System.arraycopy(inputs, curPoint, temp, 0, num);
		return temp;
	}

	private String getInputString(String[] strings) {
		StringBuffer sb = new StringBuffer();
		for(String item : strings){
			if(item!=null && !"".equals(item.trim())){
				sb.append(item.trim());
			}
		}
		return sb.toString();
	}

	
	public static void main(String[] args) {
		
		File file = new File("conf/"+CONFIG_FILE_PATH);
		if(!file.exists()){
			System.out.println("没法找到配置文件「"+AESKeyGenerator.CONFIG_FILE_PATH+"」,请确认该文件是否存在！");
			return;
		}
		
		Properties p = new Properties();
		try {
			p.load(new FileReader(file));
		} catch (FileNotFoundException e) {
			System.out.println("没法找到配置文件「"+AESKeyGenerator.CONFIG_FILE_PATH+"」,请确认该文件是否存在！");
			return;
		} catch (IOException e) {
			System.out.println("读取配置文件「"+AESKeyGenerator.CONFIG_FILE_PATH+"」出错！");
			return;
		}
		
		String pName = p.getProperty(PROJECT_NAME);
		String pCode = p.getProperty(PROJECT_CODE);
		
		AESKeyGenerator ak = new AESKeyGenerator();
		long st = System.currentTimeMillis();
		System.out.println("==============================================");
		System.out.println("key="+ak.generate(pName,pCode));
		System.out.println("耗时："+ (System.currentTimeMillis()-st)+"ms");
		
	}
}
