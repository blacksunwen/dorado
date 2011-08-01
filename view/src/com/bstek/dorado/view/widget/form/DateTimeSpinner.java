package com.bstek.dorado.view.widget.form;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-9-24
 */
@Widget(name = "DateTimeSpinner", category = "Form", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.DateTimeSpinner", shortTypeName = "DateTimeSpinner")
@XmlNode(nodeName = "DateTimeSpinner")
public class DateTimeSpinner extends Spinner {
	private DateTimeSpinnerType type = DateTimeSpinnerType.time;
	private int year;
	private int month;
	private int date;
	private int hours;
	private int minutes;
	private int seconds;

	@ViewAttribute(defaultValue = "time")
	public DateTimeSpinnerType getType() {
		return type;
	}

	public void setType(DateTimeSpinnerType type) {
		this.type = type;
	}

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

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
}
