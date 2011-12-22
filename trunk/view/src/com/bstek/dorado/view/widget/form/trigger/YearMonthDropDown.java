package com.bstek.dorado.view.widget.form.trigger;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.view.annotation.Widget;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-10
 */
@Widget(name = "YearMonthDropDown", category = "Trigger",
		dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.YearMonthDropDown",
		shortTypeName = "YearMonthDropDown")
public class YearMonthDropDown extends DropDown {

}
