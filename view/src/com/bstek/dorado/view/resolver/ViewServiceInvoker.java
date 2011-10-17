package com.bstek.dorado.view.resolver;

import java.io.Writer;

import org.codehaus.jackson.node.ObjectNode;

import com.bstek.dorado.view.service.ServiceProcessor;
import com.bstek.dorado.web.DoradoContext;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-7-8
 */
public class ViewServiceInvoker {
	public void invoke(String action, ServiceProcessor serviceProcessor,
			Writer writer, ObjectNode objectNode, DoradoContext context)
			throws Exception {
		serviceProcessor.execute(writer, objectNode, context);
	}
}
