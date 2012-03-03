package com.bstek.dorado.view.widget.grid;

import java.util.Map;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.common.Ignorable;
import com.bstek.dorado.common.MetaDataSupport;
import com.bstek.dorado.common.TagSupport;
import com.bstek.dorado.common.event.ClientEventSupportedObject;
import com.bstek.dorado.view.widget.Align;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-9-28
 */
@ClientEvents({ @ClientEvent(name = "onRenderHeaderCell"),
		@ClientEvent(name = "onHeaderClick"),
		@ClientEvent(name = "onGetCellEditor") })
@XmlNode(implTypes = "com.bstek.dorado.view.widget.grid.*")
public abstract class Column extends ClientEventSupportedObject implements
		Ignorable, TagSupport, MetaDataSupport {
	private String name;
	private String caption;
	private Align align;
	private Align headerAlign = Align.left;
	private String headerRenderer;
	private boolean visible = true;
	private boolean supportsOptionMenu = true;
	private boolean ignored;
	private Object userData;
	private String tags;
	private Map<String, Object> metaData;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public Align getAlign() {
		return align;
	}

	public void setAlign(Align align) {
		this.align = align;
	}

	@ClientProperty(escapeValue = "left")
	public Align getHeaderAlign() {
		return headerAlign;
	}

	public void setHeaderAlign(Align headerAlign) {
		this.headerAlign = headerAlign;
	}

	public String getHeaderRenderer() {
		return headerRenderer;
	}

	public void setHeaderRenderer(String headerRenderer) {
		this.headerRenderer = headerRenderer;
	}

	@ClientProperty(escapeValue = "true")
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@ClientProperty(escapeValue = "true")
	public boolean isSupportsOptionMenu() {
		return supportsOptionMenu;
	}

	public void setSupportsOptionMenu(boolean supportsOptionMenu) {
		this.supportsOptionMenu = supportsOptionMenu;
	}

	public boolean isIgnored() {
		return ignored;
	}

	public void setIgnored(boolean ignored) {
		this.ignored = ignored;
	}

	@XmlProperty
	@ClientProperty
	@IdeProperty(editor = "any")
	public Object getUserData() {
		return userData;
	}

	public void setUserData(Object userData) {
		this.userData = userData;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	@XmlProperty(composite = true)
	@ClientProperty(ignored = true)
	public Map<String, Object> getMetaData() {
		return metaData;
	}

	public void setMetaData(Map<String, Object> metaData) {
		this.metaData = metaData;
	}
}
