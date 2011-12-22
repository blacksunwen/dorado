package com.bstek.dorado.view.widget.base.tab;

import java.util.List;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.InnerElementList;

@ClientEvents({ @ClientEvent(name = "beforeTabChange"),
		@ClientEvent(name = "onTabChange"),
		@ClientEvent(name = "onTabContextMenu") })
public abstract class AbstractTabControl extends Control {
	private boolean alwaysShowNavButtons;
	private boolean showMenuButton;
	private int tabMinWidth;
	private TabPlacement tabPlacement = TabPlacement.top;
	private List<Tab> tabs = new InnerElementList<Tab>(this);
	private int currentTab;

	public boolean isAlwaysShowNavButtons() {
		return alwaysShowNavButtons;
	}

	public void setAlwaysShowNavButtons(boolean alwaysShowNavButtons) {
		this.alwaysShowNavButtons = alwaysShowNavButtons;
	}

	public boolean isShowMenuButton() {
		return showMenuButton;
	}

	public void setShowMenuButton(boolean showMenuButton) {
		this.showMenuButton = showMenuButton;
	}

	public int getTabMinWidth() {
		return tabMinWidth;
	}

	public void setTabMinWidth(int tabMinWidth) {
		this.tabMinWidth = tabMinWidth;
	}

	@ClientProperty(escapeValue = "top")
	public TabPlacement getTabPlacement() {
		return tabPlacement;
	}

	public void setTabPlacement(TabPlacement tabPlacement) {
		this.tabPlacement = tabPlacement;
	}

	public int getCurrentTab() {
		return currentTab;
	}

	public void setCurrentTab(int currentTab) {
		this.currentTab = currentTab;
	}

	public void setCurrentTab(Tab currentTab) {
		int i = tabs.indexOf(currentTab);
		if (i >= 0) {
			setCurrentTab(i);
		} else {
			throw new IllegalArgumentException(
					"The current tab must belongs to this TabControl.");
		}
	}

	public void setCurrentTab(String currentTabName) {
		int i = 0;
		for (Tab tab : tabs) {
			if (currentTabName.equals(tab.getName())) {
				setCurrentTab(i);
				return;
			}
			i++;
		}
		throw new IllegalArgumentException("No such tab [" + currentTabName
				+ "] in TabControl.");
	}

	public void addTab(Tab tab) {
		tabs.add(tab);
	}

	@XmlSubNode
	@ClientProperty
	public List<Tab> getTabs() {
		return tabs;
	}

}