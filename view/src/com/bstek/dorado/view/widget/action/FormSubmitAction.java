/**
 * 
 */
package com.bstek.dorado.view.widget.action;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-4-19
 */

@Widget(name = "FormSubmitAction", category = "Action", dependsPackage = "widget")
@ViewObject(prototype = "dorado.widget.FormSubmitAction", shortTypeName = "FormSubmitAction")
@XmlNode(nodeName = "FormSubmitAction")
public class FormSubmitAction extends Action {
	private String action;
	private String target;
	private SubmitMethod method = SubmitMethod.post;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@ViewAttribute(defaultValue = "post")
	public SubmitMethod getMethod() {
		return method;
	}

	public void setMethod(SubmitMethod method) {
		this.method = method;
	}

}
