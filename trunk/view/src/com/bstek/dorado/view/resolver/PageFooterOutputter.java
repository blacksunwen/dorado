package com.bstek.dorado.view.resolver;

import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;

import com.bstek.dorado.view.View;
import com.bstek.dorado.view.output.OutputContext;
import com.bstek.dorado.view.output.Outputter;
import com.bstek.dorado.view.widget.HtmlContainer;
import com.bstek.dorado.web.DoradoContext;

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
		Writer writer = outputContext.getWriter();

		Map<Object, String> calloutHtmlMap = outputContext.getCalloutHtmlMap();
		if (calloutHtmlMap != null && !calloutHtmlMap.isEmpty()) {
			for (Map.Entry<Object, String> entry : calloutHtmlMap.entrySet()) {
				Object object = entry.getKey();
				String id = entry.getValue();
				if (object instanceof HtmlContainer) {
					outputHtmlContainerContent(request, response, writer,
							(HtmlContainer) object, id);
				} else if (object instanceof View) {
					outputSubViewHolderContent(request, response, writer,
							(View) object, id);
				}
			}
		}
	}

	protected void outputHtmlContainerContent(HttpServletRequest request,
			HttpServletResponse response, Writer writer,
			HtmlContainer htmlContainer, String id) throws Exception {
		String templateFile = htmlContainer.getContentFile();
		outputSubTemplate(request, response, writer, templateFile,
				htmlContainer.getView(), id);
	}

	protected void outputSubViewHolderContent(HttpServletRequest request,
			HttpServletResponse response, Writer writer, View subView, String id)
			throws Exception {
		String templateFile = subView.getPageTemplate();
		outputSubTemplate(request, response, writer, templateFile,
				subView.getView(), id);
	}

	protected void outputSubTemplate(HttpServletRequest request,
			HttpServletResponse response, Writer writer, String templateFile,
			View view, String id) throws Exception {
		writer.append("<div id=\"").append(id)
				.append("\" style=\"display:none\"/>");
		DoradoContext doradoContext = DoradoContext.getCurrent();
		VelocityHelper velocityHelper = (VelocityHelper) doradoContext
				.getServiceBean("velocityHelper");
		org.apache.velocity.context.Context subContext = velocityHelper
				.createContext(view, request, response);
		Template template = velocityHelper.getVelocityEngine().getTemplate(
				templateFile);
		template.merge(subContext, writer);
		writer.append("</div>\n");
	}
}
