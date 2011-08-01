package com.bstek.dorado.view.widget.list;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.view.widget.Control;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-2-26
 */
@ClientEvents( { @ClientEvent(name = "onCurrentChange"),
		@ClientEvent(name = "onSelectionChange") })
public class AbstractList extends Control {
	private ScrollMode scrollMode = ScrollMode.lazyRender;
	private SelectionMode selectionMode = SelectionMode.none;
	private boolean allowNoCurrent;
	private DragMode dragMode = DragMode.item;
	private DropMode dropMode = DropMode.insertItems;

	@ViewAttribute(defaultValue = "lazyRender")
	public ScrollMode getScrollMode() {
		return scrollMode;
	}

	public void setScrollMode(ScrollMode scrollMode) {
		this.scrollMode = scrollMode;
	}

	@ViewAttribute(defaultValue = "none")
	public SelectionMode getSelectionMode() {
		return selectionMode;
	}

	public void setSelectionMode(SelectionMode selectionMode) {
		this.selectionMode = selectionMode;
	}

	public boolean isAllowNoCurrent() {
		return allowNoCurrent;
	}

	public void setAllowNoCurrent(boolean allowNoCurrent) {
		this.allowNoCurrent = allowNoCurrent;
	}

	@ViewAttribute(defaultValue = "item")
	public DragMode getDragMode() {
		return dragMode;
	}

	public void setDragMode(DragMode dragMode) {
		this.dragMode = dragMode;
	}

	@ViewAttribute(defaultValue = "insertItems")
	public DropMode getDropMode() {
		return dropMode;
	}

	public void setDropMode(DropMode dropMode) {
		this.dropMode = dropMode;
	}

}