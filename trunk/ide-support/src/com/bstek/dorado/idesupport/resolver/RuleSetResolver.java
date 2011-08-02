package com.bstek.dorado.idesupport.resolver;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.bstek.dorado.idesupport.RuleTemplateBuilder;
import com.bstek.dorado.idesupport.RuleTemplateManager;
import com.bstek.dorado.idesupport.output.RuleSetOutputter;
import com.bstek.dorado.idesupport.robot.Robot;
import com.bstek.dorado.idesupport.robot.RobotInfo;
import com.bstek.dorado.web.DoradoContext;
import com.bstek.dorado.web.resolver.AbstractTextualResolver;
import com.bstek.dorado.web.resolver.HttpConstants;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-18
 */
public class RuleSetResolver extends AbstractTextualResolver implements
		BeanFactoryAware {
	public static final String ROBOT_MAP_ATTRIBUTE_KEY = RuleSetResolver.class
			.getName() + ".robotMap";

	private BeanFactory beanFactory;

	public RuleSetResolver() {
		setContentType(HttpConstants.CONTENT_TYPE_XML);
		setCacheControl(HttpConstants.NO_STORE);
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	protected RuleTemplateBuilder getRuleTemplateBuilder(DoradoContext context)
			throws Exception {
		return (RuleTemplateBuilder) context
				.getServiceBean("idesupport.ruleTemplateBuilder");
	}

	protected RuleSetOutputter getRuleSetOutputter(DoradoContext context)
			throws Exception {
		return (RuleSetOutputter) context
				.getServiceBean("idesupport.ruleSetOutputter");
	}

	@Override
	protected void execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DoradoContext context = DoradoContext.getCurrent();
		RobotResolver robotResolver = (RobotResolver) beanFactory
				.getBean("dorado.robotResolver");
		if (robotResolver != null) {
			Map<String, String> robotMap = new HashMap<String, String>();
			for (Map.Entry<String, Robot> entry : robotResolver.getRobotMap()
					.entrySet()) {
				Robot robot = entry.getValue();
				RobotInfo robotInfo = robot.getClass().getAnnotation(
						RobotInfo.class);
				if (robotInfo != null) {
					String viewObject = robotInfo.viewObject();
					String robots = robotMap.get(viewObject);
					if (robots == null) {
						robots = "";
					} else {
						robots += ',';
					}
					robotMap.put(viewObject, robots + entry.getKey() + "|"
							+ robotInfo.label());
				}
			}
			request.setAttribute(ROBOT_MAP_ATTRIBUTE_KEY, robotMap);
		}

		RuleTemplateManager ruleTemplateManager = getRuleTemplateBuilder(
				context).getRuleTemplateManager();
		PrintWriter writer = getWriter(request, response);
		try {
			getRuleSetOutputter(context).output(writer, ruleTemplateManager);
		} finally {
			context.removeAttribute(DoradoContext.REQUEST,
					ROBOT_MAP_ATTRIBUTE_KEY);
			writer.flush();
			writer.close();
		}
	}
}
