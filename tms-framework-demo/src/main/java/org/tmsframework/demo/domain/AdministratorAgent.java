package org.tmsframework.demo.domain;

import java.math.BigInteger;
import java.util.List;

import org.tmsframework.demo.enums.FunctionsEnum;
import org.tmsframework.io.cookie.SelfUtil;
import org.tmsframework.mvc.web.cookyjar.SelfSerializable;

/**
 * 后台用户的cookie持久化对象
 * 
 * @author fish
 * 
 */
public class AdministratorAgent implements SelfSerializable{

	public static final String AdministratorTag = "administratorAgent";

	private String loginId;

	private BigInteger functions;// 以2进制位纪录管理员用户的权限

	public AdministratorAgent() {
		super();
	}

	public AdministratorAgent(Administer admin) {
		super();
		this.loginId = admin.getLoginId();
	}

	/**
	 * 在指定的2进制位上是否有权限
	 * 
	 * @param index
	 * @return
	 */
	public boolean haveFunction(int index) {
		return this.functions.testBit(index);
	}

	/**
	 * 是否拥有权限
	 * 
	 * @param fe
	 * @return
	 */
	public boolean haveFunction(FunctionsEnum fe) {
		return haveFunction(fe.ordinal());
	}

	/**
	 * 设置用户的权限,实际应用可以使用自增的权限表id等唯一并且连续的正整数替换这里的enum.ordinal
	 * 
	 * @param funs
	 */
	public void setFunctions(List<FunctionsEnum> funs) {
		this.functions = new BigInteger("0");
		for (FunctionsEnum en : funs) {
			this.functions = this.functions.setBit(en.ordinal());
		}
	}

	public void setFunctions(int pos) {
		if (this.functions == null) {
			this.functions = new BigInteger("0");
		}
		this.functions = this.functions.setBit(pos);
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public BigInteger getFunctions() {
		return functions;
	}

	public void setFunctions(BigInteger functions) {
		this.functions = functions;
	}

	public String lieDown() {
		return SelfUtil.format(this.loginId, this.functions.toString(36));
	}

	public SelfSerializable riseUp(String value) {
		String[] values = SelfUtil.recover(value);
		this.loginId = values[0];
		this.functions = new BigInteger(values[1], 36);
		return this;
	}
}