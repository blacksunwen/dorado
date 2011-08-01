package com.bstek.dorado.view.widget;

import java.util.Collection;


import com.bstek.dorado.common.Ignorable;
import com.bstek.dorado.view.output.JsonBuilder;
import com.bstek.dorado.view.output.OutputContext;
import com.bstek.dorado.view.output.Outputter;
import com.bstek.dorado.view.output.PropertyOutputter;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-12-14
 */
public class SubComponentOutputter implements PropertyOutputter {
	private Outputter componentOutputterDispatcher;

	public void setComponentOutputterDispatcher(
			Outputter componentOutputterDispatcher) {
		this.componentOutputterDispatcher = componentOutputterDispatcher;
	}

	public Object getEscapeValue() {
		return null;
	}

	public boolean isEscapeValue(Object value) {
		if (value == null) return true;
		if (value instanceof Collection<?>) {
			return ((Collection<?>) value).isEmpty();
		}
		return (value instanceof Ignorable && ((Ignorable) value).isIgnored());
	}

	public void output(Object object, OutputContext context) throws Exception {
		JsonBuilder json = context.getJsonBuilder();
		if (object instanceof Collection<?>) {
			Collection<?> collection = (Collection<?>) object;
			json.array();
			for (Object element : collection) {
				Component component = (Component) element;
				if (!component.isIgnored()) {
					outputComponent(component, context);
				}
			}
			json.endArray();
		}
		else {
			Component component = (Component) object;
			outputComponent(component, context);
		}
	}

	protected void outputComponent(Component component, OutputContext context)
			throws Exception {
		componentOutputterDispatcher.output(component, context);
	}

}
