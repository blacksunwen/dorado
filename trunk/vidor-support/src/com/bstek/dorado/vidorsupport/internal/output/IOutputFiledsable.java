package com.bstek.dorado.vidorsupport.internal.output;

import com.bstek.dorado.vidorsupport.internal.output.AbstractField;

/**
 * 以字段为单位输出的对象
 * 
 * @author TD
 *
 */
public interface IOutputFiledsable {
	
	/**
	 * 被输出的字段列表
	 * 
	 * @return
	 */
	AbstractField<?>[] getOutputFilelds();
	
}
