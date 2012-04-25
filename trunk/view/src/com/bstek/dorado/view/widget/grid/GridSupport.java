package com.bstek.dorado.view.widget.grid;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.widget.list.RowList;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-10
 */
@ClientEvents({ @ClientEvent(name = "onGetCellEditor"),
		@ClientEvent(name = "onGetEntityComparator"),
		@ClientEvent(name = "onRenderRow"),
		@ClientEvent(name = "onRenderCell"),
		@ClientEvent(name = "onRenderHeaderCell"),
		@ClientEvent(name = "onRenderFooterCell"),
		@ClientEvent(name = "onHeaderClick"),
		@ClientEvent(name = "beforeCellValueEdit"),
		@ClientEvent(name = "onCellValueEdit") })
public abstract class GridSupport extends RowList implements ColumnHolder {
	private int fixedColumnCount;
	private boolean readOnly;
	private int headerRowHeight;
	private int footerRowHeight;
	private boolean showHeader = true;
	private boolean showFooter;
	private boolean dynaRowHeight;
	private String cellRenderer;
	private String headerRenderer;
	private String footerRenderer;
	private String rowRenderer;
	private StretchColumnsMode stretchColumnsMode = StretchColumnsMode.stretchableColumns;

	private List<Column> columns = new ArrayList<Column>();

	public void addColumn(Column column) {
		columns.add(column);
	}

	@XmlSubNode
	@ClientProperty
	public List<Column> getColumns() {
		return columns;
	}

	public int getFixedColumnCount() {
		return fixedColumnCount;
	}

	public void setFixedColumnCount(int fixedColumnCount) {
		this.fixedColumnCount = fixedColumnCount;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public int getHeaderRowHeight() {
		return headerRowHeight;
	}

	public void setHeaderRowHeight(int headerRowHeight) {
		this.headerRowHeight = headerRowHeight;
	}

	public int getFooterRowHeight() {
		return footerRowHeight;
	}

	public void setFooterRowHeight(int footerRowHeight) {
		this.footerRowHeight = footerRowHeight;
	}

	@ClientProperty(escapeValue = "true")
	public boolean isShowHeader() {
		return showHeader;
	}

	public void setShowHeader(boolean showHeader) {
		this.showHeader = showHeader;
	}

	public boolean isShowFooter() {
		return showFooter;
	}

	public void setShowFooter(boolean showFooter) {
		this.showFooter = showFooter;
	}

	public boolean isDynaRowHeight() {
		return dynaRowHeight;
	}

	public void setDynaRowHeight(boolean dynaRowHeight) {
		this.dynaRowHeight = dynaRowHeight;
	}

	public String getCellRenderer() {
		return cellRenderer;
	}

	public void setCellRenderer(String cellRenderer) {
		this.cellRenderer = cellRenderer;
	}

	public String getHeaderRenderer() {
		return headerRenderer;
	}

	public void setHeaderRenderer(String headerRenderer) {
		this.headerRenderer = headerRenderer;
	}

	public String getFooterRenderer() {
		return footerRenderer;
	}

	public void setFooterRenderer(String footerRenderer) {
		this.footerRenderer = footerRenderer;
	}

	public String getRowRenderer() {
		return rowRenderer;
	}

	public void setRowRenderer(String rowRenderer) {
		this.rowRenderer = rowRenderer;
	}

	@ClientProperty(escapeValue = "stretchableColumns")
	public StretchColumnsMode getStretchColumnsMode() {
		return stretchColumnsMode;
	}

	public void setStretchColumnsMode(StretchColumnsMode stretchColumnsMode) {
		this.stretchColumnsMode = stretchColumnsMode;
	}
}
