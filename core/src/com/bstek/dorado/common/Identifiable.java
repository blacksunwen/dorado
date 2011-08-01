package com.bstek.dorado.common;

/**
 * id可变对象的通用接口。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 4, 2008
 */
public interface Identifiable {
	void setId(String id);

	String getId();
}
