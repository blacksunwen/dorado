package com.bstek.dorado.view.widget;

import java.util.Map;

/**
 * Html类控件的通用接口。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 5, 2008
 */
public interface HtmlElement {
	public String getWidth();

	public void setWidth(String width);

	public String getHeight();

	public void setHeight(String height);

	/**
	 * 返回CSS Class
	 */
	public String getClassName();

	/**
	 * 设置CSS Class
	 */
	public void setClassName(String className);

	/**
	 * 返回HTML Style样式
	 */
	public Map<String, Object> getStyle();

	/**
	 * 设置HTML Style样式
	 */
	public void setStyle(Map<String, Object> style);

}