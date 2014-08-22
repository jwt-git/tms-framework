package org.tmsframework.util.key;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tmsframework.util.StringUtil;

/**
 * 生成纯数字的ID列表txt文件
 * 		需要输入参数：保存文件的路径，生成id总数，起始的位置，生成文件的个数
 * 
 * @author sam,zhang
 *
 */
public class NumberGenerator implements Generator {

	private static Log _log = LogFactory.getLog(NumberGenerator.class);
	
	public static void main(String[] args) {
		
		
			long k = 100000000L;
			FileWriter fileWriter;
			try {
				for(int i=0;i<10;i++){
					File file = new File("d:/generateNum"+i+".txt");
					if(file.exists()){
						file.delete();
					}
					file.createNewFile();
					fileWriter = new FileWriter(file);
					for(int j=0;j<100000;j++){
						try {
							fileWriter.write((k++)+"\r\n");
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					fileWriter.close();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}

	public static void generateNumberFile(String fileSavePath, int fileNumber, long startNum, int pageNum, long totalNum){
		FileWriter fileWriter;
		checkParams(fileSavePath,fileNumber,pageNum,totalNum);
		try {
			for(int i=0;i<fileNumber;i++){
				File file = new File(fileSavePath+i+".txt");
				if(file.exists()){
					file.delete();
				}
				file.createNewFile();
				fileWriter = new FileWriter(file);
				for(int j=0;j<pageNum;j++){
					try {
						fileWriter.write((startNum++)+"\r\n");
					} catch (FileNotFoundException e) {
						_log.error("写入文件不存在", e);
					} catch (IOException e) {
						_log.error("写入文件时出现IO错误", e);
					}
				}
				fileWriter.close();
			}
		} catch (IOException e1) {
			_log.error("写入文件时出现IO错误", e1);
		}
	}

	private static void checkParams(String fileSavePath, int fileNumber, int pageNum, long totalNum) {
		if(StringUtil.isEmpty(fileSavePath)){
			fileSavePath = "";
		}
		if(fileNumber<1){
			fileNumber =1;
		}
		if(totalNum<1){
			totalNum = 1;
		}
		if(pageNum<1){
			pageNum = 100000;
		}
	}
	
	@Override
	public String generate(String... strings) {
		if(strings.length==1){
			
		}
		return null;
	}
}
