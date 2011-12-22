package com.bstek.dorado.view.widget.portal;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlNodeWrapper;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.annotation.Widget;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.InnerElementList;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-9-24
 */
@Widget(name = "Portal", category = "Advance", dependsPackage = "portal")
@ClientObject(prototype = "dorado.widget.Portal", shortTypeName = "Portal")
@ClientEvents({ @ClientEvent(name = "onPortletAdd"),
		@ClientEvent(name = "onPortletRemove"),
		@ClientEvent(name = "onPortletMove") })
public class Portal extends Control {
	private int portletPadding;
	private int defaultPortletHeight = 200;
	private List<Column> columns = new ArrayList<Column>();
	private List<Portlet> portlets = new InnerElementList<Portlet>(this);

	public int getPortletPadding() {
		return portletPadding;
	}

	public void setPortletPadding(int portletPadding) {
		this.portletPadding = portletPadding;
	}

	public void addColumn(Column column) {
		columns.add(column);
	}

	@XmlSubNode(wrapper = @XmlNodeWrapper(nodeName = "Columns"))
	@ClientProperty
	public List<Column> getColumns() {
		return columns;
	}

	public void addPortlet(Portlet portlet) {
		portlets.add(portlet);
	}

	@XmlSubNode(wrapper = @XmlNodeWrapper(nodeName = "Portlets"))
	@ClientProperty
	public List<Portlet> getPortlets() {
		return portlets;
	}

	@ClientProperty(escapeValue = "200")
	public int getDefaultPortletHeight() {
		return defaultPortletHeight;
	}

	public void setDefaultPortletHeight(int defaultPortletHeight) {
		this.defaultPortletHeight = defaultPortletHeight;
	}
}
