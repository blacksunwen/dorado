package com.bstek.dorado.view.widget.portal;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.view.widget.base.CloseAction;
import com.bstek.dorado.view.widget.base.Panel;
import com.bstek.dorado.view.widget.base.PanelBorder;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-9-27
 */
@ViewObject(prototype = "dorado.widget.Portlet")
@XmlNode(nodeName = "Portlet", parser = "dorado.Portal.PortletParser")
public class Portlet extends Panel {
	private boolean showCaptionBar = true;
	private PanelBorder border = PanelBorder.curve;
	private CloseAction closeAction = CloseAction.close;
	private int column;
	private boolean closeable = true;
	
	@Override
	@ViewAttribute(defaultValue = "true")
	public boolean isShowCaptionBar() {
		return showCaptionBar;
	}

	@Override
	public void setShowCaptionBar(boolean showCaptionBar) {
		this.showCaptionBar = showCaptionBar;
	}

	@Override
	@ViewAttribute(defaultValue = "curve")
	public PanelBorder getBorder() {
		return border;
	}

	@Override
	public void setBorder(PanelBorder border) {
		this.border = border;
	}

	@Override
	@ViewAttribute(defaultValue = "close")
	public CloseAction getCloseAction() {
		return closeAction;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}
	
	@ViewAttribute(defaultValue = "true")
	public boolean isCloseable() {
		return closeable;
	}
}
