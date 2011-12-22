package com.bstek.dorado.view.widget.treegrid;

import java.util.List;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNodeWrapper;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.widget.grid.Column;
import com.bstek.dorado.view.widget.grid.GridSupport;
import com.bstek.dorado.view.widget.list.DropMode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-2-26
 */
@ClientEvents({ @ClientEvent(name = "beforeExpand"),
		@ClientEvent(name = "onExpand"), @ClientEvent(name = "beforeCollapse"),
		@ClientEvent(name = "onCollapse"),
		@ClientEvent(name = "onNodeAttached"),
		@ClientEvent(name = "onNodeDetached"),
		@ClientEvent(name = "onRenderNode") })
public abstract class AbstractTreeGrid extends GridSupport {
	private int indent;
	private boolean showLines;
	private String treeColumn;
	private DropMode dropMode = DropMode.onItem;

	public int getIndent() {
		return indent;
	}

	public void setIndent(int indent) {
		this.indent = indent;
	}

	public boolean isShowLines() {
		return showLines;
	}

	public void setShowLines(boolean showLines) {
		this.showLines = showLines;
	}

	@IdeProperty(highlight = 1)
	public String getTreeColumn() {
		return treeColumn;
	}

	public void setTreeColumn(String treeColumn) {
		this.treeColumn = treeColumn;
	}

	@Override
	@XmlSubNode(wrapper = @XmlNodeWrapper(nodeName = "Columns"))
	@ClientProperty
	public List<Column> getColumns() {
		return super.getColumns();
	}

	@Override
	@ClientProperty(escapeValue = "onItem")
	public DropMode getDropMode() {
		return dropMode;
	}

	@Override
	public void setDropMode(DropMode dropMode) {
		this.dropMode = dropMode;
	}

}