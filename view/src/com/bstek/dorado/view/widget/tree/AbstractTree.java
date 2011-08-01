package com.bstek.dorado.view.widget.tree;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.view.widget.list.DropMode;
import com.bstek.dorado.view.widget.list.RowList;
import com.bstek.dorado.view.widget.list.ScrollMode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-9-30
 */
@ClientEvents( { @ClientEvent(name = "beforeExpand"),
		@ClientEvent(name = "onExpand"), @ClientEvent(name = "beforeCollapse"),
		@ClientEvent(name = "onCollapse"),
		@ClientEvent(name = "onNodeAttached"),
		@ClientEvent(name = "onNodeDetached"),
		@ClientEvent(name = "beforeCurrentChange"),
		@ClientEvent(name = "onRenderNode"),
		@ClientEvent(name = "beforeNodeCheckedChange"),
		@ClientEvent(name = "onNodeCheckedChange") })
public abstract class AbstractTree extends RowList {
	private String renderer;
	private int indent;
	private ExpandingMode expandingMode = ExpandingMode.async;
	private boolean expandingAnimated = true;
	private String defaultIcon;
	private String defaultIconClass;
	private String defaultExpandedIcon;
	private String defaultExpandedIconClass;
	private DropMode dropMode = DropMode.onItem;

	@Override
	public ScrollMode getScrollMode() {
		return super.getScrollMode();
	}

	public String getRenderer() {
		return renderer;
	}

	public void setRenderer(String renderer) {
		this.renderer = renderer;
	}

	public int getIndent() {
		return indent;
	}

	public void setIndent(int indent) {
		this.indent = indent;
	}

	@ViewAttribute(defaultValue = "async")
	public ExpandingMode getExpandingMode() {
		return expandingMode;
	}

	public void setExpandingMode(ExpandingMode expandingMode) {
		this.expandingMode = expandingMode;
	}

	@ViewAttribute(defaultValue = "true")
	public boolean isExpandingAnimated() {
		return expandingAnimated;
	}

	public void setExpandingAnimated(boolean expandingAnimated) {
		this.expandingAnimated = expandingAnimated;
	}

	public String getDefaultIcon() {
		return defaultIcon;
	}

	public void setDefaultIcon(String defaultIcon) {
		this.defaultIcon = defaultIcon;
	}

	public String getDefaultIconClass() {
		return defaultIconClass;
	}

	public void setDefaultIconClass(String defaultIconClass) {
		this.defaultIconClass = defaultIconClass;
	}

	public String getDefaultExpandedIcon() {
		return defaultExpandedIcon;
	}

	public void setDefaultExpandedIcon(String defaultExpandedIcon) {
		this.defaultExpandedIcon = defaultExpandedIcon;
	}

	public String getDefaultExpandedIconClass() {
		return defaultExpandedIconClass;
	}

	public void setDefaultExpandedIconClass(String defaultExpandedIconClass) {
		this.defaultExpandedIconClass = defaultExpandedIconClass;
	}

	@Override
	@ViewAttribute(defaultValue = "onItem")
	public DropMode getDropMode() {
		return dropMode;
	}

	@Override
	public void setDropMode(DropMode dropMode) {
		this.dropMode = dropMode;
	}
}
