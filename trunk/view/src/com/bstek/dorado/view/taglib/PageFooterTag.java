/**
 * 
 */
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
 * @since 2011-6-1
 */
public class PageFooterTag extends TagSupport {
	private static final long serialVersionUID = -8653129987997044081L;

	private static final Log logger = LogFactory.getLog(PageFooterTag.class);

	@Override
	public int doEndTag() throws JspException {
		try {
			View view = PageOutputUtils
					.getView((HttpServletRequest) pageContext.getRequest());
			PageOutputUtils.outputFooter(view,
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
