package com.bstek.dorado.view.widget.portal;


import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.common.Ignorable;
import com.bstek.dorado.common.TagSupport;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-9-24
 */
@XmlNode(nodeName = "Column")
public class Column implements Ignorable, TagSupport {
	private String className;
	private String width;
	private boolean ignored;
	private String tags;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public boolean isIgnored() {
		return ignored;
	}

	public void setIgnored(boolean ignored) {
		this.ignored = ignored;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

}
