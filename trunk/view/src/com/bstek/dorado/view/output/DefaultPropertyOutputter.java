package com.bstek.dorado.view.output;

import net.sf.json.JSONArray;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.bstek.dorado.data.JsonUtils;
import com.bstek.dorado.data.entity.EntityUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-12-11
 */
public class DefaultPropertyOutputter implements PropertyOutputter,
		BeanFactoryAware {
	private BeanFactory beanFactory;
	private Outputter objectOutputter;

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public void setObjectOutputter(Outputter objectOutputter) {
		this.objectOutputter = objectOutputter;
	}

	public boolean isEscapeValue(Object value) {
		return OutputUtils.isEscapeValue(value);
	}

	public void output(Object object, OutputContext context) throws Exception {
		JsonBuilder json = context.getJsonBuilder();
		json.beginValue();
		if (EntityUtils.isSimpleValue(object)) {
			context.getWriter().append(JsonUtils.valueToString(object));
		} else if (object.getClass().isArray()
				&& EntityUtils.isSimpleType(object.getClass()
						.getComponentType())) {
			JSONArray jsonArray = JSONArray.fromObject(object);
			context.getWriter().append(jsonArray.toString());
		} else {
			TypeAnnotationInfo typeAnnotationInfo = ViewOutputUtils
					.getTypeAnnotationInfo(beanFactory, object);
			if (typeAnnotationInfo != null
					&& typeAnnotationInfo.getOutputter() != null) {
				typeAnnotationInfo.getOutputter().output(object, context);
			} else {
				objectOutputter.output(object, context);
			}
		}
		json.endValue();
	}
}
