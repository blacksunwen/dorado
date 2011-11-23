package com.bstek.dorado.view.widget.base;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.view.widget.Control;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-7
 */
@Widget(name = "YearMonthPicker", category = "General", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.YearMonthPicker", shortTypeName = "YearMonthPicker")
@XmlNode(nodeName = "YearMonthPicker")
@ClientEvents({ @ClientEvent(name = "onPick"),
		@ClientEvent(name = "onCancel") })
public class YearMonthPicker extends Control {
	private int year;
	private int month;
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public int getMonth() {
		return month;
	}
	
	public void setMonth(int month) {
		this.month = month;
	}
}
