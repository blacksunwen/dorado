package com.bstek.dorado.view.widget.treegrid;

import java.util.List;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.widget.grid.Column;
import com.bstek.dorado.view.widget.grid.GridSupport;
import com.bstek.dorado.view.widget.list.DropMode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-2-26
 */
@ClientEvents( { @ClientEvent(name = "beforeExpand"),
		@ClientEvent(name = "onExpand"), @ClientEvent(name = "beforeCollapse"),
		@ClientEvent(name = "onCollapse"),
		@ClientEvent(name = "onNodeAttached"),
		@ClientEvent(name = "onNodeDetached"),
		@ClientEvent(name = "onRenderNode") })
public abstract class AbstractTreeGrid extends GridSupport {
	private int indent;
	private String treeColumn;
	private DropMode dropMode = DropMode.onItem;

	public int getIndent() {
		return indent;
	}

	public void setIndent(int indent) {
		this.indent = indent;
	}

	public String getTreeColumn() {
		return treeColumn;
	}

	public void setTreeColumn(String treeColumn) {
		this.treeColumn = treeColumn;
	}

	@Override
	@ViewAttribute
	@XmlSubNode(parser = "dorado.Grid.ColumnsParser", fixed = true)
	public List<Column> getColumns() {
		return super.getColumns();
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