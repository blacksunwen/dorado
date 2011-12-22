package com.bstek.dorado.view.widget.form;


import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.view.annotation.Widget;
import com.bstek.dorado.view.widget.datacontrol.AbstractPropertyDataControl;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-12-30
 */
@Widget(name = "DataLabel", category = "Form", dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.DataLabel", shortTypeName = "DataLabel")
public class DataLabel extends AbstractPropertyDataControl {}
