package com.bstek.dorado.view.widget.layout;


import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.view.widget.Align;
import com.bstek.dorado.view.widget.VerticalAlign;

public class FormLayoutConstraint extends LayoutConstraintSupport {
	private static final long serialVersionUID = 4215956783836177923L;

	private int colSpan = 1;
	private int rowSpan = 1;
	private Align align = Align.left;
	private VerticalAlign vAlign = VerticalAlign.top;

	public int getColSpan() {
		return colSpan;
	}

	public void setColSpan(int colSpan) {
		if (colSpan < 1) colSpan = 1;
		this.colSpan = colSpan;
	}

	public int getRowSpan() {
		return rowSpan;
	}

	public void setRowSpan(int rowSpan) {
		if (rowSpan < 1) rowSpan = 1;
		this.rowSpan = rowSpan;
	}

	@ViewAttribute(defaultValue = "left")
	public Align getAlign() {
		return align;
	}

	public void setAlign(Align align) {
		this.align = align;
	}

	@ViewAttribute(defaultValue = "top")
	public VerticalAlign getvAlign() {
		return vAlign;
	}

	public void setvAlign(VerticalAlign align) {
		vAlign = align;
	}
}
