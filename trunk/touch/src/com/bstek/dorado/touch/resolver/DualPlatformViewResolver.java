package com.bstek.dorado.touch.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.bstek.dorado.web.resolver.AbstractResolver;
import com.bstek.dorado.web.resolver.AbstractTextualResolver;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-12-18
 */
public class DualPlatformViewResolver extends AbstractResolver {

	static final String[] MOBILE_SPECIFIC_SUBSTRING = { "iPad", "iPhone",
			"Android", "BlackBerry", "Windows Phone", "WebOS", "Meego" };

	private AbstractTextualResolver viewResolverForPC;
	private AbstractTextualResolver viewResolverForTouch;

	public void setViewResolverForPC(AbstractTextualResolver viewResolverForPC) {
		this.viewResolverForPC = viewResolverForPC;
	}

	public void setViewResolverForTouch(
			AbstractTextualResolver viewResolverForTouch) {
		this.viewResolverForTouch = viewResolverForTouch;
	}

	@Override
	protected ModelAndView doHandleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean isPC = true;
		String userAgent = request.getHeader("user-agent");
		for (String mobile : MOBILE_SPECIFIC_SUBSTRING) {
			if (StringUtils.containsIgnoreCase(userAgent, mobile)) {
				isPC = false;
				break;
			}
		}

		if (isPC) {
			viewResolverForPC.execute(request, response);
		} else {
			viewResolverForTouch.execute(request, response);
		}
		return null;
	}

}
