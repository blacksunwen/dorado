package com.bstek.dorado.view.output;

/**
 * 视图对象输出器的通用接口。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 4, 2008
 */
public interface Outputter {
	/**
	 * 输出组件。
	 * @param object 要输出的对象。
	 * @param context 输出上下文。
	 * @throws Exception
	 */
	void output(Object object, OutputContext context) throws Exception;
}
