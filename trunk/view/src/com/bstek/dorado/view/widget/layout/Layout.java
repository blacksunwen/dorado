package com.bstek.dorado.view.widget.layout;

/**
 * 布局管理器的抽象类。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jan 19, 2008
 */
public abstract class Layout {

	/**
	 * 用于表示某控件不参与布局管理的特殊布局条件，即布局管理器将在渲染时忽略对该控件的处理。
	 */
	public final static Object NON_LAYOUT_CONSTRAINT = new Object();

	private String className;
	private int padding;

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className
	 *            the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	public int getPadding() {
		return padding;
	}

	public void setPadding(int padding) {
		this.padding = padding;
	}
}
