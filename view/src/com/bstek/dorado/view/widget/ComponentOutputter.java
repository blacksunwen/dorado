package com.bstek.dorado.view.widget;

import java.util.Map;

import com.bstek.dorado.view.output.ViewObjectOutputter;

/**
 * 默认的视图组件的输出器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Sep 29, 2008
 */
public class ComponentOutputter extends ViewObjectOutputter implements
		Cloneable {

	@Override
	protected String[] getOutputProperties(Object object) {
		if (!(object instanceof Map<?, ?>)) {
			Class<?> type = object.getClass();
			while (type != null
					&& AssembledComponent.class.isAssignableFrom(type)) {
				type = type.getSuperclass();
			}
		}
		return super.getOutputProperties(object);
	}

}
