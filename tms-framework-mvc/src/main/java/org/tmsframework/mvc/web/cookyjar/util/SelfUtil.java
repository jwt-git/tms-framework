package org.tmsframework.mvc.web.cookyjar.util;

import org.apache.commons.lang.StringUtils;

/**
 * ������С�SelfDependenceת��,String[] ת���ɵ����ַ�,ת����ĸ�ʽΪ
 * ����,����,����...|�ַ�+�ַ�+�ַ�...,ÿ���ַ��ʾ��N���ַ��ƫ����,�� ��1���ַ��Ǵ�
 * �ַ�.substring(0,����1),��2���ַ��Ǵ� �ַ�.substring(����1,����2)..�Դ�����
 * 
 * @author sam.zhang
 * 
 */
public class SelfUtil {
	public static final String format(String... strings) {
		if (strings == null) {
			throw new NullPointerException("strings can't be null.");
		}
		int length = strings.length;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			String s = strings[i];
			if (s == null) {
				sb.append(0);
			} else {
				sb.append(s.length());
			}
			if (i < (length - 1)) {
				// �������һ���ַ�
				sb.append(',');
			} else {
				sb.append('|');
			}
		}
		for (int i = 0; i < length; i++) {
			String s = strings[i];
			if (s != null) {
				sb.append(s);
			}
		}
		return sb.toString();
	}

	public static final String[] recover(String s) {
		if (s == null) {
			throw new NullPointerException("string can't be null.");
		}
		int seqPos = StringUtils.indexOf(s, '|');
		if (seqPos < 0) {
			return new String[] {};
		}
		String numberString = s.substring(0, seqPos);
		String strings = s.substring(seqPos + 1);
		String[] ss = StringUtils.split(numberString, ',');
		int ssLength = ss.length;
		int beginPos = 0;
		for (int i = 0; i < ssLength; i++) {
			int endPos = beginPos + Integer.parseInt(ss[i]);
			if (endPos > strings.length()) {
				throw new IllegalArgumentException("unknow format string[" + s
						+ "] string is too short.");
			}
			String one = strings.substring(beginPos, endPos);
			if (one.length() == 0) {
				ss[i] = null;
			} else {
				ss[i] = one;
			}
			beginPos = endPos;
		}
		return ss;
	}

	public static void main(String[] args) {
		// System.out.println(format("a", "bc", "def", null, "g"));
		// System.out.println(Arrays.asList(recover("1,2,3,0,1|abcdefg")));
		// System.out.println("----------------------------");
		// System.out.println(Arrays.asList(recover("1,4,5|abcdefg")));
	}

}
