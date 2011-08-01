package com.bstek.dorado.view.config.xml;

/**
 * 与视图XML配置文件解析过程相关的常量。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 13, 2007
 */
public abstract class ViewXmlConstants {

	public static final String PATH_VIEW_SHORT_NAME = "v";
	public static final String PATH_COMPONENT_PREFIX = "$";
	public static final char VIEW_NAME_DELIM = '|';

	/**
	 * 用于表示某控件不参与布局管理的特殊布局条件配置，即布局管理器将在渲染时忽略对该控件的处理。
	 */
	public static final String NON_LAYOUT_CONSTRAINT = "NO_CONSTRAINT";

	/**
	 * Model节点的节点名。
	 */
	public static final String MODEL = "Model";

	/**
	 * View节点的节点名。
	 */
	public static final String VIEW_CONFIG = "ViewConfig";

	public static final String ATTRIBUTE_TEMPALTE = "template";

	public static final String ATTRIBUTE_PACKAGES = "packages";

	/**
	 * ID属性的属性名。
	 */
	public static final String ATTRIBUTE_ID = "id";

	/**
	 * 布局属性的属性名。
	 */
	public static final String ATTRIBUTE_LAYOUT = "layout";

	/**
	 * 布局条件属性的属性名。
	 */
	public static final String ATTRIBUTE_LAYOUT_CONSTRAINT = "layoutConstraint";

	/**
	 * 参数集合节点。
	 */
	public static final String ARGUMENTS = "Arguments";

	/**
	 * 参数节点。
	 */
	public static final String ARGUMENT = "Argument";

	public static final String CONTEXT = "Context";

	public static final String ATTRIBUTE = "Attribute";
}
