package com.bstek.dorado.data;

/**
 * 数据封装功能相关的常量。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jan 7, 2008
 */
public abstract class Constants {
	private Constants() {
	}

	/**
	 * DataType在{@link com.bstek.dorado.core.bean.ScopeManager}中进行管理时使用的名称前缀。
	 */
	public final static String SCOPE_DATA_TYPE_PREFIX = "DataType:";

	/**
	 * DataProvider在{@link com.bstek.dorado.core.bean.ScopeManager}
	 * 中进行管理时使用的名称前缀。
	 */
	public final static String SCOPE_DATA_PROVIDER_PREFIX = "DataProvider:";

	/**
	 * DataResolver在{@link com.bstek.dorado.core.bean.ScopeManager}
	 * 中进行管理时使用的名称前缀。
	 */
	public final static String SCOPE_DATA_RESOLVER_PREFIX = "DataResolver:";

	/**
	 * 私有对象的名称前缀，dorado引擎将名称符合此前缀的对象认为是私有的。
	 */
	public final static char PRIVATE_DATA_OBJECT_PREFIX = '$';

	/**
	 * 私有对象的名称后缀。
	 */
	public final static String PRIVATE_DATA_OBJECT_SUBFIX = ":";

	/**
	 * 默认的聚合类型的类型名。
	 */
	public final static String DEFAULT_COLLECTION_TYPE = "Collection";
}
