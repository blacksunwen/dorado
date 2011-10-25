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
	private int column;

	public Portlet() {
		setShowCaptionBar(true);
		setBorder(PanelBorder.curve);
		setCloseAction(CloseAction.close);
		setCloseable(true);
	}

	@Override
	@ViewAttribute(defaultValue = "true")
	public boolean isShowCaptionBar() {
		return super.isShowCaptionBar();
	}

	@Override
	@ViewAttribute(defaultValue = "curve")
	public PanelBorder getBorder() {
		return super.getBorder();
	}

	@Override
	@ViewAttribute(defaultValue = "close")
	public CloseAction getCloseAction() {
		return super.getCloseAction();
	}

	@ViewAttribute(defaultValue = "true")
	public boolean isCloseable() {
		return super.isCloseable();
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}
}
