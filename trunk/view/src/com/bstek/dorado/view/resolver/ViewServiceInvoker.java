package com.bstek.dorado.view.resolver;

import java.io.Writer;

import net.sf.json.JSONObject;

import com.bstek.dorado.view.service.ServiceProcessor;
import com.bstek.dorado.web.DoradoContext;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-7-8
 */
public class ViewServiceInvoker {
	public void invoke(String action, ServiceProcessor serviceProcessor,
			Writer writer, JSONObject json, DoradoContext context)
			throws Exception {
		serviceProcessor.execute(writer, json, context);
	}
}
