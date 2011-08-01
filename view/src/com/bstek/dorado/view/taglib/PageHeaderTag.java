package com.bstek.dorado.view.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bstek.dorado.view.View;
import com.bstek.dorado.view.resolver.PageOutputUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-1-26
 */
public class PageHeaderTag extends TagSupport {
	private static final long serialVersionUID = 1505799261850356012L;

	private static final Log logger = LogFactory.getLog(PageHeaderTag.class);

	@Override
	public int doEndTag() throws JspException {
		try {
			View view = PageOutputUtils
					.getView((HttpServletRequest) pageContext.getRequest());
			PageOutputUtils.outputHeader(view, 
					(HttpServletRequest) pageContext.getRequest(),
					(HttpServletResponse) pageContext.getResponse(),
					pageContext.getOut());
		} catch (Exception e) {
			logger.error(e, e);
			throw new JspException(e);
		}
		return super.doEndTag();
	}
}
