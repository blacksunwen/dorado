/**
 * 
 */
package com.bstek.dorado.view.widget.base.toolbar;

import java.util.List;

import org.apache.commons.lang.RandomStringUtils;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.annotation.Widget;
import com.bstek.dorado.view.widget.InnerElementReference;
import com.bstek.dorado.view.widget.base.menu.BaseMenuItem;
import com.bstek.dorado.view.widget.base.menu.Menu;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-8-8
 */
@Widget(name = "MenuButton", category = "ToolBar",
		dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.toolbar.ToolBarButton",
		shortTypeName = "ToolBarButton", properties = @ClientProperty(
				propertyName = "menu",
				outputter = "spring:dorado.menuButtonMenuPropertyOutputter"))
@XmlNode(nodeName = "MenuButton")
public class MenuButton extends com.bstek.dorado.view.widget.base.Button {
	private InnerElementReference<Menu> embededMenuRef = new InnerElementReference<Menu>(
			this);

	public MenuButton() {
		super();

		String menuId = RandomStringUtils.randomAlphanumeric(16);
		Menu embededMenu = new Menu();
		embededMenu.setId(menuId);
		embededMenuRef.set(embededMenu);
		super.setMenu(menuId);
	}

	public void addItem(BaseMenuItem menuItem) {
		embededMenuRef.get().addItem(menuItem);
	}

	@XmlSubNode
	@ClientProperty
	public List<BaseMenuItem> getItems() {
		return embededMenuRef.get().getItems();
	}

	public Menu getEmbededMenu() {
		Menu menu = embededMenuRef.get();
		if (menu.getItems().isEmpty()) {
			return null;
		}
		return menu;
	}

	@Override
	public void setMenu(String menu) {
		throw new UnsupportedOperationException();
	}

	@Override
	@XmlProperty(unsupported = true)
	@ClientProperty(outputter = "spring:dorado.menuButtonMenuPropertyOutputter")
	@IdeProperty(visible = false)
	public String getMenu() {
		throw new UnsupportedOperationException();
	}
}
