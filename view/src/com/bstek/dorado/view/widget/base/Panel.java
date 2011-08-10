package com.bstek.dorado.view.widget.base;

import java.util.List;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.widget.InnerElementList;

/**
 * 面板控件。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 2, 2008
 */
@Widget(name = "Panel", category = "General", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.Panel", shortTypeName = "Panel")
@XmlNode(nodeName = "Panel")
@ClientEvents({ @ClientEvent(name = "beforeMaximize"),
		@ClientEvent(name = "onMaximize")})
public class Panel extends AbstractPanel {
	private PanelBorder border = PanelBorder.normal;
	private boolean showCaptionBar = true;
	private boolean maximizeable;
	private boolean maximized;
	
	private boolean closeable = true;
	private CloseAction closeAction = CloseAction.hide;
	private String icon;
	private List<SimpleIconButton> tools = new InnerElementList<SimpleIconButton>(
			this);

	@ViewAttribute(defaultValue = "normal")
	public PanelBorder getBorder() {
		return border;
	}

	public void setBorder(PanelBorder border) {
		this.border = border;
	}

	@ViewAttribute(defaultValue = "true")
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

	@ViewAttribute
	@XmlSubNode(path = "Tools/*")
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
	
	@ViewAttribute(defaultValue = "true")
	public boolean isCloseable() {
		return closeable;
	}

	public void setCloseable(boolean closeable) {
		this.closeable = closeable;
	}
	
	@ViewAttribute(defaultValue = "hide")
	public CloseAction getCloseAction() {
		return closeAction;
	}

	public void setCloseAction(CloseAction closeAction) {
		this.closeAction = closeAction;
	}
}
