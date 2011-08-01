package com.bstek.dorado.view;

/**
 * 视图功能相关的常量。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jan 22, 2008
 */
public abstract class Constants {
	private Constants() {}

	/**
	 * XML类型的视图配置文件。
	 */
	public final static String VIEW_CONFIG_TYPE_XML = "xml";

	/**
	 * 视图对象在{@link com.bstek.dorado.core.bean.ScopeManager}中进行管理时使用的名称前缀。
	 */
	public final static String SCOPE_VIEW_PREFIX = "View:";
}
