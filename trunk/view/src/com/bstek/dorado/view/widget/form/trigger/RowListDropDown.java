package com.bstek.dorado.view.widget.form.trigger;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.widget.grid.Column;
import com.bstek.dorado.view.widget.grid.ColumnHolder;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-10
 */
@ClientEvents({ @ClientEvent(name = "onFilterItems"),
		@ClientEvent(name = "onFilterItem") })
public abstract class RowListDropDown extends DropDown implements ColumnHolder {
	private String property;
	private String displayProperty;
	private List<Column> columns = new ArrayList<Column>();
	private boolean dynaFilter;
	private boolean filterOnTyping = true;
	private boolean filterOnOpen;
	private int minFilterInterval = 300;

	@IdeProperty(highlight = 1)
	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public void addColumn(Column column) {
		columns.add(column);
	}

	@XmlSubNode
	@ClientProperty
	public List<Column> getColumns() {
		return columns;
	}

	public String getDisplayProperty() {
		return displayProperty;
	}

	public void setDisplayProperty(String displayProperty) {
		this.displayProperty = displayProperty;
	}

	public boolean isDynaFilter() {
		return dynaFilter;
	}

	public void setDynaFilter(boolean dynaFilter) {
		this.dynaFilter = dynaFilter;
	}

	@ClientProperty(escapeValue = "true")
	public boolean isFilterOnTyping() {
		return filterOnTyping;
	}

	public void setFilterOnTyping(boolean filterOnTyping) {
		this.filterOnTyping = filterOnTyping;
	}

	public boolean isFilterOnOpen() {
		return filterOnOpen;
	}

	public void setFilterOnOpen(boolean filterOnOpen) {
		this.filterOnOpen = filterOnOpen;
	}

	@ClientProperty(escapeValue = "300")
	public int getMinFilterInterval() {
		return minFilterInterval;
	}

	public void setMinFilterInterval(int minFilterInterval) {
		this.minFilterInterval = minFilterInterval;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
}
