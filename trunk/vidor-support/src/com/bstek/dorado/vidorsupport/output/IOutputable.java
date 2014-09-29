package com.bstek.dorado.vidorsupport.output;

/**
 * 可以被输出的对象 
 * 
 * @author TD
 *
 * @param <CTX>
 */
public interface IOutputable<CTX extends OutputContext> {
	
	/**
	 * 输出
	 * @param context
	 */
	void output(CTX context);
	
}
