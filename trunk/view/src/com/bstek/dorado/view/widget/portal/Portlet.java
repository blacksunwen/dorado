package com.bstek.dorado.view.widget.portal;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.view.widget.base.CloseAction;
import com.bstek.dorado.view.widget.base.Panel;
import com.bstek.dorado.view.widget.base.PanelBorder;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-9-27
 */
@XmlNode(implTypes = "com.bstek.dorado.view.widget.portal.*")
@ClientObject(prototype = "dorado.widget.Portlet", shortTypeName = "Portlet")
public class Portlet extends Panel {
	private int column;

	public Portlet() {
		setShowCaptionBar(true);
		setBorder(PanelBorder.curve);
		setCloseAction(CloseAction.close);
		setCloseable(true);
	}

	@Override
	@ClientProperty(escapeValue = "true")
	public boolean isShowCaptionBar() {
		return super.isShowCaptionBar();
	}

	@Override
	@ClientProperty(escapeValue = "curve")
	public PanelBorder getBorder() {
		return super.getBorder();
	}

	@Override
	@ClientProperty(escapeValue = "close")
	public CloseAction getCloseAction() {
		return super.getCloseAction();
	}

	@ClientProperty(escapeValue = "true")
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
