/**
 * 
 */
package com.bstek.dorado.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-5-11
 */
public final class DoradoContextUtils {
	private static final String STACK_ATTRIBUTE = DoradoContextUtils.class
			.getName() + ".stack";
	private static final String MAP_ATTRIBUTE = DoradoContextUtils.class
			.getName() + ".map";

	private DoradoContextUtils() {
	}

	@SuppressWarnings("unchecked")
	private static Stack<Map<String, Object>> getViewContextStack(
			DoradoContext context) {
		Stack<Map<String, Object>> stack = (Stack<Map<String, Object>>) context
				.getAttribute(STACK_ATTRIBUTE);
		if (stack == null) {
			stack = new Stack<Map<String, Object>>();
			context.setAttribute(STACK_ATTRIBUTE, stack);
		}
		return stack;
	}

	@SuppressWarnings("unchecked")
	private static Map<Object, Map<String, Object>> getViewContextMap(
			DoradoContext context) {
		Map<Object, Map<String, Object>> map = (Map<Object, Map<String, Object>>) context
				.getAttribute(MAP_ATTRIBUTE);
		if (map == null) {
			map = new HashMap<Object, Map<String, Object>>();
			context.setAttribute(MAP_ATTRIBUTE, map);
		}
		return map;
	}

	public static void pushNewViewContext(DoradoContext context) {
		Map<String, Object> viewContext = new HashMap<String, Object>();
		getViewContextStack(context).push(viewContext);
		context.setViewContext(viewContext);
	}

	public static void popViewContext(DoradoContext context) {
		Stack<Map<String, Object>> stack = getViewContextStack(context);
		stack.pop();
		if (stack.isEmpty()) {
			context.setViewContext(null);
		} else {
			context.setViewContext(stack.peek());
		}
	}

	public static void bindViewContext(DoradoContext context, Object object) {
		getViewContextMap(context).put(object, context.getViewContext());
	}

	public static Map<String, Object> getViewContextByBindingObject(
			DoradoContext context, Object object) {
		return getViewContextMap(context).get(object);
	}

	public static Map<String, Object> getViewContext(DoradoContext context) {
		return context.getViewContext();
	}
}
