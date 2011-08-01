package com.bstek.dorado.view.widget.form;


import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.view.widget.datacontrol.AbstractPropertyDataControl;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-12-30
 */
@Widget(name = "DataLabel", category = "Form", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.DataLabel", shortTypeName = "DataLabel")
@XmlNode(nodeName = "DataLabel")
public class DataLabel extends AbstractPropertyDataControl {}
