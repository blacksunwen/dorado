package com.bstek.dorado.view.widget.form;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.view.annotation.Widget;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-9-22
 */
@Widget(name = "PasswordEditor", category = "Form",
		dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.PasswordEditor",
		shortTypeName = "PasswordEditor")
public class PasswordEditor extends AbstractTextEditor {

}
