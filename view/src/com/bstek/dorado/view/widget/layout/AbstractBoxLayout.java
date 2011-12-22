package com.bstek.dorado.view.widget.layout;

import com.bstek.dorado.annotation.ClientProperty;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-7-20
 */
public abstract class AbstractBoxLayout extends Layout {
	private Pack pack = Pack.start;
	private boolean stretch = true;
	private int padding = 2;
	private int regionPadding = 2;

	@ClientProperty(escapeValue = "start")
	public Pack getPack() {
		return pack;
	}

	public void setPack(Pack pack) {
		this.pack = pack;
	}

	@ClientProperty(escapeValue = "true")
	public boolean isStretch() {
		return stretch;
	}

	public void setStretch(boolean stretch) {
		this.stretch = stretch;
	}

	@Override
	@ClientProperty(escapeValue = "2")
	public int getPadding() {
		return padding;
	}

	public void setPadding(int padding) {
		this.padding = padding;
	}

	@ClientProperty(escapeValue = "2")
	public int getRegionPadding() {
		return regionPadding;
	}

	public void setRegionPadding(int regionPadding) {
		this.regionPadding = regionPadding;
	}

}
