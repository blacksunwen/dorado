package com.bstek.dorado.view.widget.base;

import java.util.List;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlNodeWrapper;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.annotation.Widget;
import com.bstek.dorado.view.widget.InnerElementList;

/**
 * 面板控件。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 2, 2008
 */
@Widget(name = "Panel", category = "General", dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.Panel", shortTypeName = "Panel")
@ClientEvents({ @ClientEvent(name = "beforeMaximize"),
		@ClientEvent(name = "onMaximize") })
public class Panel extends AbstractPanel {
	private PanelBorder border = PanelBorder.normal;
	private boolean showCaptionBar = true;
	private boolean maximizeable;
	private boolean maximized;

	private boolean closeable;
	private String iconClass;
	private String background;
	private CloseAction closeAction = CloseAction.hide;
	private String icon;
	private List<SimpleIconButton> tools = new InnerElementList<SimpleIconButton>(
			this);

	public Panel() {
		setCollapseable(false);
	}

	@ClientProperty(escapeValue = "normal")
	public PanelBorder getBorder() {
		return border;
	}

	public void setBorder(PanelBorder border) {
		this.border = border;
	}

	@ClientProperty(escapeValue = "true")
	public boolean isShowCaptionBar() {
		return showCaptionBar;
	}

	public void setShowCaptionBar(boolean showCaptionBar) {
		this.showCaptionBar = showCaptionBar;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void addTool(SimpleIconButton tool) {
		tools.add(tool);
	}

	@XmlSubNode(wrapper = @XmlNodeWrapper(nodeName = "Tools"))
	@ClientProperty
	public List<SimpleIconButton> getTools() {
		return tools;
	}

	public boolean isMaximizeable() {
		return maximizeable;
	}

	public void setMaximizeable(boolean maximizeable) {
		this.maximizeable = maximizeable;
	}

	public boolean isMaximized() {
		return maximized;
	}

	public void setMaximized(boolean maximized) {
		this.maximized = maximized;
	}

	public boolean isCloseable() {
		return closeable;
	}

	public void setCloseable(boolean closeable) {
		this.closeable = closeable;
	}

	@ClientProperty(escapeValue = "hide")
	public CloseAction getCloseAction() {
		return closeAction;
	}

	public void setCloseAction(CloseAction closeAction) {
		this.closeAction = closeAction;
	}

	@Override
	@ClientProperty(escapeValue = "false")
	public boolean isCollapseable() {
		return super.isCollapseable();
	}

	public String getIconClass() {
		return iconClass;
	}

	public void setIconClass(String iconClass) {
		this.iconClass = iconClass;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

}
