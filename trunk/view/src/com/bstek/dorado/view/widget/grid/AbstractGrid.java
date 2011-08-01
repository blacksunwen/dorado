package com.bstek.dorado.view.widget.grid;

import com.bstek.dorado.annotation.ViewAttribute;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-9-28
 */
public abstract class AbstractGrid extends GridSupport {
	private String groupHeaderRenderer;
	private String groupFooterRenderer;
	private String groupProperty;
	private boolean groupOnSort = true;
	private boolean showGroupFooter;
	private boolean showFilterBar;

	public String getGroupHeaderRenderer() {
		return groupHeaderRenderer;
	}

	public void setGroupHeaderRenderer(String groupHeaderRenderer) {
		this.groupHeaderRenderer = groupHeaderRenderer;
	}

	public String getGroupFooterRenderer() {
		return groupFooterRenderer;
	}

	public void setGroupFooterRenderer(String groupFooterRenderer) {
		this.groupFooterRenderer = groupFooterRenderer;
	}

	public String getGroupProperty() {
		return groupProperty;
	}

	public void setGroupProperty(String groupProperty) {
		this.groupProperty = groupProperty;
	}

	@ViewAttribute(defaultValue = "true")
	public boolean isGroupOnSort() {
		return groupOnSort;
	}

	public void setGroupOnSort(boolean groupOnSort) {
		this.groupOnSort = groupOnSort;
	}

	public boolean isShowGroupFooter() {
		return showGroupFooter;
	}

	public void setShowGroupFooter(boolean showGroupFooter) {
		this.showGroupFooter = showGroupFooter;
	}

	public boolean isShowFilterBar() {
		return showFilterBar;
	}

	public void setShowFilterBar(boolean showFilterBar) {
		this.showFilterBar = showFilterBar;
	}

}
