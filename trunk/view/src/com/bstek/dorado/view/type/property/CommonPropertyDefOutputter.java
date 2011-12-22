package com.bstek.dorado.view.type.property;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.view.output.ClientObjectOutputter;
import com.bstek.dorado.view.output.OutputContext;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Dec 30, 2008
 */
public class CommonPropertyDefOutputter extends ClientObjectOutputter {
	private String type;

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	@Override
	protected void outputObjectProperties(Object object, OutputContext context)
			throws Exception {
		if (StringUtils.isNotEmpty(type)) {
			context.getJsonBuilder().key("$type").value(type);
		}
		super.outputObjectProperties(object, context);
	}

}
