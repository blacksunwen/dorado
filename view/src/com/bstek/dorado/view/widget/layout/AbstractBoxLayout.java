package com.bstek.dorado.view.widget.layout;

import com.bstek.dorado.annotation.ViewAttribute;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-7-20
 */
public abstract class AbstractBoxLayout extends Layout {
	private Pack pack = Pack.start;
	private boolean stretch = true;
	private int padding = 2;
	private int regionPadding = 2;

	@ViewAttribute(defaultValue = "start")
	public Pack getPack() {
		return pack;
	}

	public void setPack(Pack pack) {
		this.pack = pack;
	}

	@ViewAttribute(defaultValue = "true")
	public boolean isStretch() {
		return stretch;
	}

	public void setStretch(boolean stretch) {
		this.stretch = stretch;
	}

	@Override
	@ViewAttribute(defaultValue = "2")
	public int getPadding() {
		return padding;
	}

	public void setPadding(int padding) {
		this.padding = padding;
	}

	@ViewAttribute(defaultValue = "2")
	public int getRegionPadding() {
		return regionPadding;
	}

	public void setRegionPadding(int regionPadding) {
		this.regionPadding = regionPadding;
	}

}
