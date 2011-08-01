package com.bstek.dorado.view.widget.form.trigger;

import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-10
 */
@Widget(name = "YearMonthDropDown", category = "Trigger", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.YearMonthDropDown", shortTypeName = "YearMonthDropDown")
@XmlNode(nodeName = "YearMonthDropDown")
public class YearMonthDropDown extends DropDown {

}
