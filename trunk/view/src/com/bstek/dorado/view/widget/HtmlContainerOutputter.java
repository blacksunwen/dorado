/**
 * 
 */
package com.bstek.dorado.view.widget;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.common.event.DefaultClientEvent;
import com.bstek.dorado.view.output.OutputContext;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-5-13
 */
public class HtmlContainerOutputter extends ContainerOutputter {

	@Override
	public void output(Object object, OutputContext context) throws Exception {
		HtmlContainer htmlContainer = (HtmlContainer) object;
		if (StringUtils.isNotEmpty(htmlContainer.getContentFile())) {
			String calloutId = "html_" + context.getCalloutId();
			context.addCalloutHtml(htmlContainer, calloutId);

			StringBuffer script = new StringBuffer();
			script.append("self.assignDom(document.getElementById(\"")
					.append(calloutId).append("\"));");

			htmlContainer.addClientEventListener("onCreate",
					new DefaultClientEvent(script.toString()));
		}
		super.output(object, context);
	}

}
