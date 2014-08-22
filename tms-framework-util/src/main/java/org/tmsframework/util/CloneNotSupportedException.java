package org.tmsframework.util;

/**
 * ��<code>ObjectUtil.clone</code>����������ʱ������ƵĶ���֧�ָò��������׳����쳣��
 * 
 * <p>
 * ע�⣬��<code>java.lang.CloneNotSupportedException</code>��ͬ�����쳣��<code>RuntimeException</code>����
 * </p>
 *
 */
public class CloneNotSupportedException extends RuntimeException {
    private static final long serialVersionUID = 3257281439807584562L;

    /**
     * ����һ���յ��쳣.
     */
    public CloneNotSupportedException() {
        super();
    }

    /**
     * ����һ���쳣, ָ���쳣����ϸ��Ϣ.
     *
     * @param message ��ϸ��Ϣ
     */
    public CloneNotSupportedException(String message) {
        super(message);
    }

    /**
     * ����һ���쳣, ָ����������쳣������.
     *
     * @param cause �쳣������
     */
    public CloneNotSupportedException(Throwable cause) {
        super(cause);
    }

    /**
     * ����һ���쳣, ָ����������쳣������.
     *
     * @param message ��ϸ��Ϣ
     * @param cause �쳣������
     */
    public CloneNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }
}
