package com.bstek.dorado.view.widget.layout;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.IdeProperty;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-5
 */
@ClientObject(shortTypeName = "Form")
public class FormLayout extends Layout {
	private String cols;
	private boolean stretchWidth;
	private int rowHeight;
	private int colPadding = 6;
	private int rowPadding = 6;

	@IdeProperty(highlight = 1)
	public String getCols() {
		return cols;
	}

	public void setCols(String cols) {
		this.cols = cols;
	}

	public boolean isStretchWidth() {
		return stretchWidth;
	}

	public void setStretchWidth(boolean stretchWidth) {
		this.stretchWidth = stretchWidth;
	}

	public int getRowHeight() {
		return rowHeight;
	}

	public void setRowHeight(int rowHeight) {
		this.rowHeight = rowHeight;
	}

	@ClientProperty(escapeValue = "6")
	public int getColPadding() {
		return colPadding;
	}

	public void setColPadding(int colPadding) {
		this.colPadding = colPadding;
	}

	@ClientProperty(escapeValue = "6")
	public int getRowPadding() {
		return rowPadding;
	}

	public void setRowPadding(int rowPadding) {
		this.rowPadding = rowPadding;
	}
}
