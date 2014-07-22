/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.view.resolver;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bstek.dorado.view.View;
import com.bstek.dorado.view.output.Callout;
import com.bstek.dorado.view.output.OutputContext;
import com.bstek.dorado.view.output.Outputter;

/**
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-6-1
 */
public class PageFooterOutputter implements Outputter {

	public static class ViewWrapper {
		private View view;
		private HttpServletRequest request;
		private HttpServletResponse response;

		public ViewWrapper(View view, HttpServletRequest request,
				HttpServletResponse response) {
			this.view = view;
			this.request = request;
			this.response = response;
		}

		public View getView() {
			return view;
		}

		public HttpServletRequest getRequest() {
			return request;
		}

		public HttpServletResponse getResponse() {
			return response;
		}
	}

	public void output(Object object, OutputContext context) throws Exception {
		ViewWrapper wrapper = (ViewWrapper) object;
		output(wrapper.getView(), wrapper.getRequest(), wrapper.getResponse(),
				context);
	}

	protected void output(View view, HttpServletRequest request,
			HttpServletResponse response, OutputContext outputContext)
			throws Exception {
		List<Callout> callouts = outputContext.getCallouts();
		if (callouts != null) {
			for (Callout callout : callouts) {
				outputCallout(request, response, callout, outputContext);
			}
		}
	}

	protected void outputCallout(HttpServletRequest request,
			HttpServletResponse response, Callout callout,
			OutputContext outputContext) throws Exception {
		callout.getOutputter().output(callout, outputContext);
	}
}
