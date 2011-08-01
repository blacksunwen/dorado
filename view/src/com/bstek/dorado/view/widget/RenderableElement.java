package com.bstek.dorado.view.widget;

import java.util.Map;


/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-7
 */
public interface RenderableElement extends HtmlElement {
	String getClassName();

	void setClassName(String className);

	String getExClassName();

	void setExClassName(String exClassName);

	String getWidth();

	void setWidth(String width);

	String getHeight();

	void setHeight(String height);

	Map<String, Object> getStyle();

	void setStyle(Map<String, Object> style);
}
