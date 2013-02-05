package com.bstek.dorado.console.security;
/**
 * Dorado Console AccessDeniedException
 * 
 * @author Alex Tong(mailto:alex.tong@bstek.com)
 * 
 */
public class AccessDeniedException extends RuntimeException {

	private static final long serialVersionUID = -7136350916222027464L;
	
	public static final String LOGIN_TIME_OUT_MESSAGE="登陆已超时！";

	/**
     * Constructs an <code>AccessDeniedException</code> with the specified
     * message.
     *
     * @param msg the detail message
     */
    public AccessDeniedException(String msg) {
        super(msg);
    }

    /**
     * Constructs an <code>AccessDeniedException</code> with the specified
     * message and root cause.
     *
     * @param msg the detail message
     * @param t root cause
     */
    public AccessDeniedException(String msg, Throwable t) {
        super(msg, t);
    }
}
