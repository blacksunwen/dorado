package com.bstek.dorado.view.output;


/**
 * 属性输出器。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jun 4, 2009
 */
public interface PropertyOutputter extends Outputter {

	/**
	 * 判断传入的数值是否该属性的默认值。
	 */
	public boolean isEscapeValue(Object value);
}
