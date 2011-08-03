package com.bstek.dorado.desktop;

import java.util.Collection;
import java.util.HashSet;

import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.ViewElement;
import com.bstek.dorado.view.ViewElementUtils;

@XmlNode(nodeName = "App")
public class App implements ViewElement {
	private ViewElement parent;
	private Collection<ViewElement> innerElements;

	private String caption;
	private String icon16;
	private String icon32;
	private String icon48;
	private String icon64;
	private String id;
	
	public ViewElement getParent() {
		return parent;
	}

	public void setParent(ViewElement parent) {
		ViewElementUtils.clearParentViewElement(this, this.parent);

		this.parent = parent;

		View view = ViewElementUtils.getParentView(parent);
		if (view != null) {
			ViewElementUtils.setParentViewElement(this, parent);
		}
	}

	public void registerInnerElement(ViewElement element) {
		if (innerElements == null) {
			innerElements = new HashSet<ViewElement>();
		}
		innerElements.add(element);
	}

	public void unregisterInnerElement(ViewElement element) {
		if (innerElements != null) {
			innerElements.remove(element);
		}
	}

	public Collection<ViewElement> getInnerElements() {
		return innerElements;
	}

	/**
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * @param caption
	 *            the caption to set
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * @return the appId
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the appId to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the icon16
	 */
	public String getIcon16() {
		return icon16;
	}

	/**
	 * @param icon16 the icon16 to set
	 */
	public void setIcon16(String icon16) {
		this.icon16 = icon16;
	}

	/**
	 * @return the icon32
	 */
	public String getIcon32() {
		return icon32;
	}

	/**
	 * @param icon32 the icon32 to set
	 */
	public void setIcon32(String icon32) {
		this.icon32 = icon32;
	}

	/**
	 * @return the icon48
	 */
	public String getIcon48() {
		return icon48;
	}

	/**
	 * @param icon48 the icon48 to set
	 */
	public void setIcon48(String icon48) {
		this.icon48 = icon48;
	}

	/**
	 * @return the icon64
	 */
	public String getIcon64() {
		return icon64;
	}

	/**
	 * @param icon64 the icon64 to set
	 */
	public void setIcon64(String icon64) {
		this.icon64 = icon64;
	}
	
	
	
}
