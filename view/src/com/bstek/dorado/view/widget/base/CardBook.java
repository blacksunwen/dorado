package com.bstek.dorado.view.widget.base;

import java.util.List;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.annotation.Widget;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.InnerElementList;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-4
 */
@Widget(name = "CardBook", category = "General", dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.CardBook", shortTypeName = "CardBook")
@ClientEvents({ @ClientEvent(name = "beforeCurrentChange"),
		@ClientEvent(name = "onCurrentChange") })
public class CardBook extends Control {
	private List<Control> controls = new InnerElementList<Control>(this);
	private int currentControl;

	public int getCurrentControl() {
		return currentControl;
	}

	public void setCurrentControl(int currentControl) {
		this.currentControl = currentControl;
	}

	public void setCurrentControl(String activeControlId) {
		int i = 0;
		for (Control control : controls) {
			if (activeControlId.equals(control.getId())) {
				setCurrentControl(i);
				return;
			}
			i++;
		}
		throw new IllegalArgumentException("No such child control ["
				+ activeControlId + "] in CardBook");
	}

	public void addControl(Control control) {
		controls.add(control);
	}

	@XmlSubNode
	@ClientProperty
	public List<Control> getControls() {
		return controls;
	}

}