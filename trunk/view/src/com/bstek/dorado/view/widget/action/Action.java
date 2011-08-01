package com.bstek.dorado.view.widget.action;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.view.widget.Component;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since May 13, 2009
 */
@Widget(name = "Action", category = "Action", dependsPackage = "widget")
@ViewObject(prototype = "dorado.widget.Action", shortTypeName = "Action")
@XmlNode(nodeName = "Action")
@ClientEvents({ @ClientEvent(name = "beforeExecute"),
		@ClientEvent(name = "onExecute"), @ClientEvent(name = "onSuccess"),
		@ClientEvent(name = "onFailure") })
public class Action extends Component {
	private String caption;
	private String icon;
	private String iconClass;
	private String tip;
	private boolean disabled;
	private Object parameter;
	private boolean async;
	private String hotkey;
	private String confirmMessage;
	private String executingMessage;
	private String successMessage;

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIconClass() {
		return iconClass;
	}

	public void setIconClass(String iconClass) {
		this.iconClass = iconClass;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	@ViewAttribute(outputter = "dorado.doradoMapPropertyOutputter", editor = "any")
	public Object getParameter() {
		return parameter;
	}

	public void setParameter(Object parameter) {
		this.parameter = parameter;
	}

	@ViewAttribute(defaultValue = "true")
	public boolean isAsync() {
		return async;
	}

	public void setAsync(boolean async) {
		this.async = async;
	}

	@ViewAttribute(enumValues = "f1,f2,ctrl+s,alt+s,shift+s,ctrl+alt+shift+s,space,backspace,left,right,up,down")
	public String getHotkey() {
		return hotkey;
	}

	public void setHotkey(String hotkey) {
		this.hotkey = hotkey;
	}

	public String getConfirmMessage() {
		return confirmMessage;
	}

	public void setConfirmMessage(String confirmMessage) {
		this.confirmMessage = confirmMessage;
	}

	public String getExecutingMessage() {
		return executingMessage;
	}

	public void setExecutingMessage(String executingMessage) {
		this.executingMessage = executingMessage;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}
}
