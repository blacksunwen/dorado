/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
package com.bstek.dorado.core.el;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bstek.dorado.core.Context;

/**
 * 默认的EL表达式处理器的实现类。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 4, 2007
 */
public class DefaultExpressionHandler implements ExpressionHandler {
	private static final Log logger = LogFactory
			.getLog(ExpressionHandler.class);

	private final static char ESCAPE_CHAR = '\\';
	private final static char SPECIAL_CHAR = '$';
	private final static char BRACKET_BEGIN = '{';
	private final static char BRACKET_END = '}';
	private final static String DORADO_VAR = "dorado";
	private final static String CONTEXT_ATTRIBUTE_KEY = DefaultExpressionHandler.class
			.getName();

	private static ThreadLocal<JexlEngine> threadLocal = new ThreadLocal<JexlEngine>();
	private static Object doradoExpressionUtilsBean;

	private List<ContextVarsInitializer> contextInitializers;

	/**
	 * 设置所有的隐式变量初始化器。
	 */
	public void setContextInitializers(
			List<ContextVarsInitializer> contextInitializers) {
		this.contextInitializers = contextInitializers;
	}

	/**
	 * 返回所有的隐式变量初始化器。
	 */
	public List<ContextVarsInitializer> getContextInitializers() {
		return contextInitializers;
	}

	public Expression compile(String text) {
		if (StringUtils.isEmpty(text) || text.indexOf(SPECIAL_CHAR) < 0) {
			return null;
		}

		List<Object> sections = new ArrayList<Object>();
		boolean escaped = false;
		short specialCharFound = 0;
		boolean inBracket = false;
		EvaluateMode evaluateMode = EvaluateMode.onInstantiate;
		boolean hasExpression = false;

		try {
			StringBuffer section = new StringBuffer(text.length());
			for (char c : text.toCharArray()) {
				if (specialCharFound > 0) {
					if (c == BRACKET_BEGIN) {
						inBracket = true;
						evaluateMode = (specialCharFound == 1) ? EvaluateMode.onInstantiate
								: EvaluateMode.onRead;
						specialCharFound = 0;
						if (section.length() > 0) {
							sections.add(section.toString());
							section.setLength(0);
						}
						continue;
					} else if (specialCharFound == 1 && c == SPECIAL_CHAR) {
						specialCharFound++;
						continue;
					} else {
						section.append(SPECIAL_CHAR);
						if (specialCharFound == 2) {
							section.append(SPECIAL_CHAR);
						}
						specialCharFound = 0;
					}
				}

				if (escaped) {
					escaped = false;
					if (c != SPECIAL_CHAR) {
						section.append(ESCAPE_CHAR);
					}
				} else {
					if (c == ESCAPE_CHAR) {
						escaped = true;
						continue;
					} else if (c == SPECIAL_CHAR) {
						specialCharFound = 1;
						continue;
					} else if (c == BRACKET_END) {
						if (inBracket) {
							inBracket = false;
							hasExpression = true;
							sections.add(getJexlEngine().createExpression(
									section.toString()));
							section.setLength(0);
							continue;
						}
					}
				}
				section.append(c);
			}

			if (hasExpression && section.length() > 0) {
				sections.add(section.toString());
			}
		} catch (Exception e) {
			logger.error(e, e);
		}

		if (hasExpression) {
			Expression expression = createExpression(sections, evaluateMode);
			if (expression instanceof ExpressionHandlerAware) {
				((ExpressionHandlerAware) expression)
						.setExpressionHandler(this);
			}
			return expression;
		} else {
			return null;
		}
	}

	/**
	 * 根据与解析的结果创建具体的表达式对象。
	 * 
	 * @param sections
	 * @param evaluateMode
	 * @return
	 */
	protected Expression createExpression(List<Object> sections,
			EvaluateMode evaluateMode) {
		Expression expression;
		if (sections.size() == 1) {
			expression = new SingleExpression(
					(org.apache.commons.jexl2.Expression) sections.get(0),
					evaluateMode);

		} else {
			expression = new CombinedExpression(sections, evaluateMode);
		}
		return expression;
	}

	public JexlEngine getJexlEngine() throws Exception {
		JexlEngine engine = threadLocal.get();
		if (engine == null) {
			engine = new JexlEngine();
			engine.setCache(50);
			engine.setSilent(true);
			threadLocal.set(engine);
		}
		return engine;
	}

	public JexlContext getJexlContext() {
		Context context = Context.getCurrent();
		JexlContext ctx = (JexlContext) context
				.getAttribute(CONTEXT_ATTRIBUTE_KEY);
		if (ctx == null) {
			ctx = new MapContext();
			if (contextInitializers != null) {
				try {
					Map<String, Method> utilMethods = new HashMap<String, Method>();

					Map<String, Object> vars = new HashMap<String, Object>();
					for (ContextVarsInitializer initializer : contextInitializers) {
						initializer.initializeContext(vars);
					}
					for (Map.Entry<String, Object> entry : vars.entrySet()) {
						String key = entry.getKey();
						Object value = entry.getValue();
						if (value instanceof Method) {
							utilMethods.put(key, (Method) value);
						} else {
							ctx.set(key, value);
						}
					}

					if (!utilMethods.isEmpty()) {
						ctx.set(DORADO_VAR,
								createDoradoExpressionUtilsBean(utilMethods));
					}
				} catch (Exception e) {
					logger.error(e, e);
				}
			}

			try {
				context.setAttribute(CONTEXT_ATTRIBUTE_KEY, ctx);
			} catch (NullPointerException e) {
				// do nothing
			}
		}
		return ctx;
	}

	protected synchronized static Object createDoradoExpressionUtilsBean(
			Map<String, Method> utilMethods) throws Exception {
		if (doradoExpressionUtilsBean == null) {
			ClassPool pool = ClassPool.getDefault();
			CtClass ctClass = pool
					.makeClass("com.bstek.dorado.core.el.$DoradoExpressionUtils");
			for (Map.Entry<String, Method> entry : utilMethods.entrySet()) {
				String name = entry.getKey();
				Method method = entry.getValue();
				int methodIndex = ArrayUtils.indexOf(method.getDeclaringClass()
						.getMethods(), method);

				StringBuffer buf = new StringBuffer();
				StringBuffer args = new StringBuffer();
				buf.append("public ").append("Object").append(' ').append(name)
						.append('(');
				Class<?>[] parameterTypes = method.getParameterTypes();
				for (int i = 0; i < parameterTypes.length; i++) {
					if (i > 0) {
						buf.append(',');
						args.append(',');
					}
					buf.append("Object").append(' ').append("p" + i);
					args.append("p" + i);
				}
				buf.append(")");
				if (method.getExceptionTypes().length > 0) {
					buf.append(" throws ");
					int i = 0;
					for (Class<?> exceptionType : method.getExceptionTypes()) {
						if (i > 0)
							buf.append(',');
						buf.append(exceptionType.getName());
						i++;
					}
				}
				buf.append("{\n")
						.append("return Class.forName(\""
								+ method.getDeclaringClass().getName())
						.append("\").getMethods()[").append(methodIndex)
						.append("].invoke(null, new Object[]{").append(args)
						.append("});").append("\n}");
				CtMethod delegator = CtNewMethod.make(buf.toString(), ctClass);
				delegator.setName(name);
				ctClass.addMethod(delegator);
			}
			Class<?> cl = ctClass.toClass();
			doradoExpressionUtilsBean = cl.newInstance();
		}
		return doradoExpressionUtilsBean;
	}
}
