package com.bstek.dorado.view.widget.base;

import java.util.Date;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.view.widget.Control;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-7
 */
@Widget(name = "DatePicker", category = "General", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.DatePicker", shortTypeName = "DatePicker")
@XmlNode(nodeName = "DatePicker")
@ClientEvents({ @ClientEvent(name = "onPick"),
		@ClientEvent(name = "onCancel"), @ClientEvent(name = "onRefreshDateCell") })
public class DatePicker extends Control {
	private Date date;
	private boolean showTimeSpinner = false;
	private boolean showConfirmButton = true;
	private boolean showTodayButton = true;
	private boolean showClearButton = true;
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	@ViewAttribute(defaultValue = "false")
	public boolean getShowTimeSpinner() {
		return showTimeSpinner;
	}

	public void setShowTimeSpinner(boolean showTimeSpinner) {
		this.showTimeSpinner = showTimeSpinner;
	}
	
	@ViewAttribute(defaultValue = "true")
	public boolean isShowConfirmButton() {
		return showConfirmButton;
	}

	public void setShowConfirmButton(boolean showConfirmButton) {
		this.showConfirmButton = showConfirmButton;
	}
	
	@ViewAttribute(defaultValue = "true")
	public boolean isShowTodayButton() {
		return showTodayButton;
	}

	public void setShowTodayButton(boolean showTodayButton) {
		this.showTodayButton = showTodayButton;
	}
	
	@ViewAttribute(defaultValue = "true")
	public boolean isShowClearButton() {
		return showClearButton;
	}

	public void setShowClearButton(boolean showClearButton) {
		this.showClearButton = showClearButton;
	}	
}
