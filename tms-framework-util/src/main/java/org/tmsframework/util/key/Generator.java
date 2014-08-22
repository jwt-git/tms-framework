/**
 * 生成器接口
 * 
 */

package org.tmsframework.util.key;


/**
 * @author sam.zhang
 *
 */
public interface Generator {

	/**
	 * 根据输入参数生成对应的结果
	 * @param strings
	 * @return
	 */
	public String generate(String ...strings);
	
}
