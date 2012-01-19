package com.bstek.dorado.view.output;

import java.util.Collection;

import com.bstek.dorado.common.Ignorable;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-12-4
 */
public class ObjectOutputterDispatcher implements Outputter, PropertyOutputter {
	private boolean escapeable = true;
	private ClientOutputHelper clientOutputHelper;
	private Outputter defaultObjectOutputter;

	public boolean isEscapeable() {
		return escapeable;
	}

	public void setEscapeable(boolean escapeable) {
		this.escapeable = escapeable;
	}

	public void setClientOutputHelper(ClientOutputHelper clientOutputHelper) {
		this.clientOutputHelper = clientOutputHelper;
	}

	public void setDefaultObjectOutputter(Outputter defaultObjectOutputter) {
		this.defaultObjectOutputter = defaultObjectOutputter;
	}

	public boolean isEscapeValue(Object value) {
		return (value == null || value instanceof Ignorable
				&& ((Ignorable) value).isIgnored());
	}

	public void output(Object object, OutputContext context) throws Exception {
		if (object != null) {
			JsonBuilder json = context.getJsonBuilder();
			if (object instanceof Collection<?>) {
				Collection<?> collection = (Collection<?>) object;
				if (escapeable) {
					json.escapeableArray();
				} else {
					json.array();
				}

				for (Object element : collection) {
					if (!(element instanceof Ignorable)
							|| !((Ignorable) element).isIgnored()) {
						outputObject(element, context);
					}
				}
				json.endArray();
			} else if (!(object instanceof Ignorable)
					|| !((Ignorable) object).isIgnored()) {
				outputObject(object, context);
			} else {
				context.getWriter().append("null");
			}
		} else if (!context.isEscapeable()) {
			context.getWriter().append("null");
		}
	}

	protected void outputObject(Object object, OutputContext context)
			throws Exception {
		Outputter outputter = clientOutputHelper
				.getOutputter(object.getClass());
		if (outputter == null) {
			outputter = defaultObjectOutputter;
		}
		outputter.output(object, context);
	}

}
