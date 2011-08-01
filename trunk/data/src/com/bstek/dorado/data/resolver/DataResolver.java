package com.bstek.dorado.data.resolver;

import com.bstek.dorado.core.bean.Scope;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.data.DataModelObject;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apr 29, 2009
 */
public interface DataResolver extends DataModelObject {
	/**
	 * 返回DataResolver的名称。
	 */
	String getName();

	/**
	 * 返回作用范围。
	 */
	Scope getScope();

	/**
	 * 返回DataResolver归属的文件资源。<br>
	 * DataResolver归属的资源是指该DataResolver定义在哪个配置文件中。
	 * 如果DataResolver并非由配置文件产生，那么该方法将返回null。
	 * 
	 * @return 文件资源描述对象
	 */
	Resource getResource();

	/**
	 * 设置默认参数。
	 */
	Object getParameter();

	/**
	 * 设置默认参数。
	 */
	void setParameter(Object parameter);

	/**
	 * @param dataItems
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	Object resolve(DataItems dataItems) throws Exception;

	/**
	 * @param dataItems
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	Object resolve(DataItems dataItems, Object parameter) throws Exception;
}
