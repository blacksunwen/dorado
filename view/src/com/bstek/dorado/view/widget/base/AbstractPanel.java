package com.bstek.dorado.view.widget.base;

import java.util.List;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.widget.Align;
import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.Container;
import com.bstek.dorado.view.widget.InnerElementList;

@ClientEvents({ @ClientEvent(name = "beforeCollapsedChange"),
	@ClientEvent(name = "onCollapsedChange")})
public class AbstractPanel extends Container {
	private String caption;
	private List<Button> buttons = new InnerElementList<Button>(this);
	private Align buttonAlign = Align.center;
	private boolean collapseable;
	private boolean collapsed;

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public void addButton(Button button) {
		buttons.add(button);
	}

	@ViewAttribute
	@XmlSubNode(path = "Buttons/*")
	public List<Button> getButtons() {
		return buttons;
	}

	@ViewAttribute(defaultValue = "center")
	public Align getButtonAlign() {
		return buttonAlign;
	}

	public void setButtonAlign(Align buttonAlign) {
		this.buttonAlign = buttonAlign;
	}

	public boolean isCollapseable() {
		return collapseable;
	}

	public void setCollapseable(boolean collapseable) {
		this.collapseable = collapseable;
	}

	public boolean isCollapsed() {
		return collapsed;
	}

	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
	}

	@Override
	@XmlSubNode(path = "Children/*", parser = "dorado.childComponentParser", fixed = true)
	public List<Component> getChildren() {
		return super.getChildren();
	}

}