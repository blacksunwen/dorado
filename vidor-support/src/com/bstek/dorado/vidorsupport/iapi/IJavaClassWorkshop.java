package com.bstek.dorado.vidorsupport.iapi;

import java.util.Collection;
import java.util.Map;

public interface IJavaClassWorkshop {

	/**
	 * 返回系统默认支持的全部JavaClass的名称列表
	 * @return
	 * @throws Exception
	 */
	Collection<String> names(Map<String, Object> parameter) throws Exception;
}
