package com.bstek.dorado.view.widget.list;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ViewAttribute;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-2-26
 */
@ClientEvents( { @ClientEvent(name = "onDataRowClick") })
public abstract class RowList extends AbstractList {
	private int rowHeight;
	private boolean highlightCurrentRow = true;

	public int getRowHeight() {
		return rowHeight;
	}

	public void setRowHeight(int rowHeight) {
		this.rowHeight = rowHeight;
	}

	@ViewAttribute(defaultValue = "true")
	public boolean isHighlightCurrentRow() {
		return highlightCurrentRow;
	}

	public void setHighlightCurrentRow(boolean highlightCurrentRow) {
		this.highlightCurrentRow = highlightCurrentRow;
	}

}