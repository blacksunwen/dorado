package com.bstek.dorado.view.widget;

import java.util.List;
import java.util.Stack;

import com.bstek.dorado.view.output.JsonBuilder;
import com.bstek.dorado.view.output.OutputContext;
import com.bstek.dorado.view.output.Outputter;
import com.bstek.dorado.view.output.VirtualPropertyOutputter;
import com.bstek.dorado.view.widget.layout.Layout;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-12-12
 */
public class ChildrenComponentOutputter implements VirtualPropertyOutputter {
	private Outputter componentOutputterDispatcher;

	public void setComponentOutputterDispatcher(
			Outputter componentOutputterDispatcher) {
		this.componentOutputterDispatcher = componentOutputterDispatcher;
	}

	public void output(Object object, String property, OutputContext context)
			throws Exception {
		Container container = (Container) object;
		List<Component> children = container.getChildren();
		if (children.isEmpty()) return;

		JsonBuilder json = context.getJsonBuilder();
		json.escapeableKey(property);
		outputChildrenComponents(container, context);
		json.endKey();
	}

	public void outputChildrenComponents(Container container,
			OutputContext context) throws Exception {
		Stack<Layout> layoutStack = context.getLayoutStack();
		layoutStack.push(container.getLayout());
		try {
			JsonBuilder json = context.getJsonBuilder();
			json.escapeableArray();

			List<Component> children = container.getChildren();
			for (Component component : children) {
				if (!component.isIgnored()) {
					outputComponent(component, context);
				}
			}
			json.endArray();
		}
		finally {
			layoutStack.pop();
		}
	}

	protected void outputComponent(Component component, OutputContext context)
			throws Exception {
		componentOutputterDispatcher.output(component, context);
	}
}
