package com.bstek.dorado.sample.widget.customize;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.config.definition.DefinitionPostProcessor;
import com.bstek.dorado.view.widget.AssembledComponent;
import com.bstek.dorado.view.widget.form.autoform.AutoForm;
import com.bstek.dorado.view.widget.form.autoform.AutoFormElement;

public class QuickForm extends AutoForm implements AssembledComponent,
		DefinitionPostProcessor {
	private String items;

	public QuickForm() {
		setCols("*");
	}

	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}

	public void onInit() throws Exception {
		if (items != null) {
			for (String item : StringUtils.split(items, ',')) {
				if (StringUtils.isEmpty(item))
					continue;
				AutoFormElement element = new AutoFormElement();
				element.setProperty(item);
				addElement(element);
			}
		}
	}

}
