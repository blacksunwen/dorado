package com.bstek.dorado.view.widget.blockview;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.view.widget.list.AbstractList;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-2-11
 */
@ClientEvents({ @ClientEvent(name = "onRenderBlock"),
		@ClientEvent(name = "onBlockMouseDown"),
		@ClientEvent(name = "onBlockMouseUp"),
		@ClientEvent(name = "onBlockClick"),
		@ClientEvent(name = "onBlockDoubleClick") })
public abstract class AbstractBlockView extends AbstractList {
	private BlockLayout blockLayout = BlockLayout.vertical;
	private int lineSize;
	private int blockWidth;
	private int blockHeight;
	private boolean fillLine;
	private String renderer;
	private int horiSpacing;
	private int vertSpacing;
	private int horiPadding;
	private int vertPadding;
	private int blockDecoratorSize;

	@ClientProperty(escapeValue = "vertical")
	public BlockLayout getBlockLayout() {
		return blockLayout;
	}

	public void setBlockLayout(BlockLayout blockLayout) {
		this.blockLayout = blockLayout;
	}

	public int getLineSize() {
		return lineSize;
	}

	public void setLineSize(int lineSize) {
		this.lineSize = lineSize;
	}

	public int getBlockWidth() {
		return blockWidth;
	}

	public void setBlockWidth(int blockWidth) {
		this.blockWidth = blockWidth;
	}

	public int getBlockHeight() {
		return blockHeight;
	}

	public void setBlockHeight(int blockHeight) {
		this.blockHeight = blockHeight;
	}

	public boolean isFillLine() {
		return fillLine;
	}

	public void setFillLine(boolean fillLine) {
		this.fillLine = fillLine;
	}

	public String getRenderer() {
		return renderer;
	}

	public void setRenderer(String renderer) {
		this.renderer = renderer;
	}

	public int getHoriSpacing() {
		return horiSpacing;
	}

	public void setHoriSpacing(int spacing) {
		horiSpacing = spacing;
	}

	public int getVertSpacing() {
		return vertSpacing;
	}

	public void setVertSpacing(int spacing) {
		vertSpacing = spacing;
	}

	public int getHoriPadding() {
		return horiPadding;
	}

	public void setHoriPadding(int padding) {
		horiPadding = padding;
	}

	public int getVertPadding() {
		return vertPadding;
	}

	public void setVertPadding(int padding) {
		vertPadding = padding;
	}

	public int getBlockDecoratorSize() {
		return blockDecoratorSize;
	}

	public void setBlockDecoratorSize(int blockDecoratorSize) {
		this.blockDecoratorSize = blockDecoratorSize;
	}
}
