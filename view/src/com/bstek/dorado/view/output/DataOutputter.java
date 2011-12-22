package com.bstek.dorado.view.output;

import java.io.Writer;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang.StringEscapeUtils;

import com.bstek.dorado.core.el.Expression;
import com.bstek.dorado.data.JsonUtils;
import com.bstek.dorado.data.entity.EntityEnhancer;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.entity.EntityWrapper;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.provider.PagingList;
import com.bstek.dorado.data.type.AggregationDataType;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.type.property.LazyPropertyDef;
import com.bstek.dorado.data.type.property.PropertyDef;
import com.bstek.dorado.util.clazz.BeanPropertyUtils;
import com.bstek.dorado.view.el.CombinedExpression;
import com.bstek.dorado.view.el.OutputableExpressionUtils;
import com.bstek.dorado.view.el.SingleExpression;

/**
 * 用于向客户端输出JSON数据的输出器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Oct 6, 2008
 */
public class DataOutputter implements Outputter, PropertyOutputter {
	private static final DateFormat DATE_FORMATTER = new SimpleDateFormat(
			com.bstek.dorado.core.Constants.ISO_DATE_FORMAT);
	private static final DateFormat TIME_FORMATTER = new SimpleDateFormat(
			com.bstek.dorado.core.Constants.ISO_DATETIME_FORMAT1);
	private static final Long ONE_HOUR = 1000L * 60 * 60;

	private boolean evaluateExpression = true;
	private boolean ignoreEmptyProperty;
	private boolean simplePropertyValueOnly;

	public boolean isEvaluateExpression() {
		return evaluateExpression;
	}

	public void setEvaluateExpression(boolean evaluateExpression) {
		this.evaluateExpression = evaluateExpression;
	}

	public boolean isIgnoreEmptyProperty() {
		return ignoreEmptyProperty;
	}

	public void setIgnoreEmptyProperty(boolean ignoreEmptyProperty) {
		this.ignoreEmptyProperty = ignoreEmptyProperty;
	}

	public boolean isSimplePropertyValueOnly() {
		return simplePropertyValueOnly;
	}

	public void setSimplePropertyValueOnly(boolean simplePropertyValueOnly) {
		this.simplePropertyValueOnly = simplePropertyValueOnly;
	}

	public boolean isEscapeValue(Object value) {
		return OutputUtils.isEscapeValue(value);
	}

	/**
	 * 将一个EL表达式输出为JavaScript代码。
	 */
	protected void outputExpression(String expression, OutputContext context)
			throws Exception {
		Writer writer = context.getWriter();
		if (expression.equals("this")) {
			writer.write(expression);
		} else {
			writer.write(expression.replaceAll("this.",
					"dorado.DataPath.create(\""));
			writer.write("\").evaluate(this, true)");
		}
	}

	public void output(Object object, OutputContext context) throws Exception {
		if (object != null) {
			outputData(object, context);
		} else {
			JsonBuilder json = context.getJsonBuilder();
			json.value(null);
		}
	}

	private void outputPagingList(PagingList pagingList, OutputContext context)
			throws Exception {
		JsonBuilder json = context.getJsonBuilder();
		Writer writer = context.getWriter();
		writer.write("(function(){\n");
		writer.write("var a=");
		json.array();
		for (Object e : pagingList) {
			outputData(e, context);
		}
		json.endArray();
		writer.write(";\n");

		OutputUtils.outputProperty(writer, "a", pagingList, "pageSize");
		OutputUtils.outputProperty(writer, "a", pagingList, "pageNo");
		OutputUtils.outputProperty(writer, "a", pagingList, "pageCount");
		OutputUtils.outputProperty(writer, "a", pagingList, "entityCount");

		writer.write("return a;\n");
		writer.write("})()");
	}

	private void outputPage(Page<?> page, OutputContext context)
			throws Exception {
		JsonBuilder json = context.getJsonBuilder();
		Writer writer = context.getWriter();
		writer.write("(function(){\n");
		writer.write("var a=");
		json.array();
		Collection<?> entities = page.getEntities();
		if (entities != null) {
			outputDataTypeIfNecessary(context, entities);
			for (Object e : entities) {
				outputData(e, context);
			}
		}
		json.endArray();
		writer.write(";\n");

		OutputUtils.outputProperty(writer, "a", page, "pageSize");
		OutputUtils.outputProperty(writer, "a", page, "pageNo");
		OutputUtils.outputProperty(writer, "a", page, "pageCount");
		OutputUtils.outputProperty(writer, "a", page, "entityCount");

		writer.write("return a;\n");
		writer.write("})()");
	}

	/**
	 * @param object
	 * @param writer
	 * @param context
	 * @throws Exception
	 */
	protected void outputData(Object object, OutputContext context)
			throws Exception {
		JsonBuilder json = context.getJsonBuilder();
		if (EntityUtils.isSimpleValue(object)) {
			if (object instanceof Date) {
				Date d = (Date) object;
				if (d instanceof Time || d instanceof Timestamp
						|| d.getTime() % ONE_HOUR != 0) {
					json.value(TIME_FORMATTER.format(d));
				} else {
					json.value(DATE_FORMATTER.format(d));
				}
			} else {
				json.value(object);
			}
		} else {
			if (object instanceof Expression) {
				json.beginValue();
				Writer writer = context.getWriter();
				writer.write("function(){return ");
				Expression expression = (Expression) object;
				if (expression instanceof SingleExpression) {
					outputExpression(
							(String) ((SingleExpression) expression)
									.prevaluate(),
							context);
				} else if (expression instanceof CombinedExpression) {
					CombinedExpression combinedExpression = (CombinedExpression) expression;
					int i = 0;
					for (Object section : (Object[]) combinedExpression
							.prevaluate()) {
						if (section == null)
							continue;

						if (i > 0)
							writer.write('+');
						if (section instanceof org.apache.commons.jexl2.Expression) {
							outputExpression(
									((org.apache.commons.jexl2.Expression) section)
											.getExpression(), context);
						} else {
							writer.write('"');
							writer.write(StringEscapeUtils
									.escapeJavaScript(String.valueOf(section)));
							writer.write('"');
						}
						i++;
					}
				}
				writer.write(";}");
				json.endValue();
			} else {
				internalOutputData(object, context);
			}
		}
	}

	private void internalOutputData(Object object, OutputContext context)
			throws Exception {
		JsonBuilder json = context.getJsonBuilder();
		Stack<Object> dataObjectStack = context.getDataObjectStack();
		if (dataObjectStack.contains(object)) {
			throw new IllegalArgumentException(
					"Reference circuit found on entity [" + object + "].");
		}

		dataObjectStack.push(object);
		try {
			if (object instanceof Collection<?>) {
				outputDataTypeIfNecessary(context, object);

				if (object instanceof PagingList) {
					outputPagingList((PagingList) object, context);
				} else {
					json.array();
					for (Object e : (Collection<?>) object) {
						outputData(e, context);
					}
					json.endArray();
				}
			} else if (object instanceof Page<?>) {
				outputPage((Page<?>) object, context);
			} else {
				outputEntity(object, context);
			}
		} finally {
			dataObjectStack.pop();
		}
	}

	protected void outputEntity(Object object, OutputContext context)
			throws Exception {
		JsonBuilder json = context.getJsonBuilder();
		EntityWrapper entity = EntityWrapper.create(object);
		EntityDataType dataType = entity.getDataType();

		json.object();
		Class<?> type = object.getClass();
		if (dataType == null || dataType.isAcceptUnknownProperty()) {
			for (String property : entity.getPropertySet()) {
				if (!BeanPropertyUtils.isValidProperty(type, property)) {
					continue;
				}
				outputEntityProperty(context, entity, property, false);
			}
		} else {
			for (String property : entity.getPropertySet()) {
				if (!BeanPropertyUtils.isValidProperty(type, property)) {
					continue;
				}

				PropertyDef propertyDef = dataType.getPropertyDef(property);
				if (propertyDef == null || propertyDef.isIgnored()) {
					continue;
				}

				if (propertyDef instanceof LazyPropertyDef) {
					LazyPropertyDef lazyPropertyDef = (LazyPropertyDef) propertyDef;
					if (lazyPropertyDef.isActiveAtClient()
							&& !entity.isLoaded(property)) {
						continue;
					}
				}
				outputEntityProperty(context, entity, property,
						ignoreEmptyProperty);
			}
		}

		outputDataTypeIfNecessary(context, object);
		if (context.isShouldOutputEntityState()) {
			EntityState state = entity.getState();
			if (state != EntityState.NONE) {
				json.key(JsonUtils.STATE_PROPERTY).value(
						EntityState.toInt(state));
			}
		}
		json.endObject();
	}

	protected void outputEntityProperty(OutputContext context,
			EntityWrapper entity, String property, boolean ignoreEmptyProperty)
			throws Exception {
		Object value = null;
		EntityEnhancer.resetHasPropertyResultSkiped();

		if (!isEvaluateExpression()) {
			OutputableExpressionUtils.disableOutputableExpression();
		}
		try {
			value = entity.get(property);
			if (OutputableExpressionUtils.getSkipedExpression() != null) {
				value = OutputableExpressionUtils.getSkipedExpression();
			}
		} finally {
			if (!isEvaluateExpression()) {
				OutputableExpressionUtils.enableOutputableExpression();
			}
		}

		if ((value != null
				&& !OutputUtils.isEscapeValue(value, OutputUtils.ESCAPE_VALUE) || !ignoreEmptyProperty)) {
			if (!EntityEnhancer.hasGetterResultSkiped()
					&& (!simplePropertyValueOnly || EntityUtils
							.isSimpleValue(value))) {
				JsonBuilder json = context.getJsonBuilder();
				json.escapeableKey(property);
				outputData(value, context);
				json.endKey();
			}
		}
	}

	private void outputDataTypeIfNecessary(OutputContext context, Object object) {
		if (context.isShouldOutputDataTypes()) {
			DataType dataType = null;
			if (object instanceof Collection<?>) {
				AggregationDataType aggDataType = EntityUtils
						.getDataType((Collection<?>) object);
				if (aggDataType != null) {
					dataType = aggDataType.getElementDataType();
				}
			} else {
				dataType = EntityUtils.getDataType(object);
			}
			if (dataType != null) {
				Map<String, DataType> includeDataTypes = context
						.getIncludeDataTypes(true);
				if (!includeDataTypes.containsKey(dataType.getName())) {
					includeDataTypes.put(dataType.getName(), dataType);
				}
			}
		}
	}
}
