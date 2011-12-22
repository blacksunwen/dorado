package com.bstek.dorado.view.widget.form.trigger;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.view.annotation.Widget;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-10
 */
@Widget(name = "DateDropDown", category = "Trigger",
		dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.DateDropDown",
		shortTypeName = "DateDropDown")
public class DateDropDown extends DropDown {
	private boolean showTimeSpinner;

	public boolean isShowTimeSpinner() {
		return showTimeSpinner;
	}

	@ClientProperty(escapeValue = "false")
	public void setShowTimeSpinner(boolean showTimeSpinner) {
		this.showTimeSpinner = showTimeSpinner;
	}

}
