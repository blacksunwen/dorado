package com.bstek.dorado.view.widget.layout;

/**
 * 视图中布局条件的抽象类。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Sep 30, 2008
 */
public abstract class LayoutConstraintSupport {
	private String className;
	private int padding;

	public String getClassName() {
		return className;
	}

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
