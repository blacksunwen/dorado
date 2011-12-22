/**
 * 
 */
package com.bstek.dorado.view.widget.action;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.view.annotation.Widget;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-4-19
 */

@Widget(name = "FormSubmitAction", category = "Action",
		dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.FormSubmitAction",
		shortTypeName = "FormSubmitAction")
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

	@ClientProperty(escapeValue = "post")
	public SubmitMethod getMethod() {
		return method;
	}

	public void setMethod(SubmitMethod method) {
		this.method = method;
	}

}
