package com.bstek.dorado.data.provider;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import com.bstek.dorado.data.JsonConvertContext;
import com.bstek.dorado.data.JsonConvertor;
import com.bstek.dorado.data.JsonUtils;
import com.bstek.dorado.data.provider.filter.ExpressionFilterCriterion;
import com.bstek.dorado.data.provider.filter.FilterCriterion;
import com.bstek.dorado.data.provider.filter.FilterOperator;
import com.bstek.dorado.data.provider.filter.PropertyFilterCriterion;
import com.bstek.dorado.data.provider.filter.SingleValueFilterCriterion;
import com.bstek.dorado.data.type.AbstractDataType;
import com.bstek.dorado.data.type.DataConvertException;
import com.bstek.dorado.data.type.DataType;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-9-18
 */
public class CriteriaDataType extends AbstractDataType implements JsonConvertor {
	public Object fromText(String text) {
		if (text == null) {
			return null;
		}
		throw new DataConvertException(String.class, Criteria.class);
	}

	private FilterOperator toFilterOperator(String s) {
		FilterOperator operator = null;
		try {
			operator = Enum.valueOf(FilterOperator.class, s);
		} catch (Exception e) {
			for (FilterOperator op : FilterOperator.values()) {
				if (op.toString().equals(s)) {
					operator = op;
					break;
				}
			}
		}
		return operator;
	}

	private Criterion jsonToCriterion(ObjectNode criterionNode,
			JsonConvertContext context) throws Exception {
		String junctionValue = JsonUtils.getString(criterionNode, "junction");
		if (StringUtils.isEmpty(junctionValue)) {
			FilterCriterion criterion;
			String type = JsonUtils.getString(criterionNode, "type");

			DataType dataType = null;
			String dataTypeName = JsonUtils
					.getString(criterionNode, "dataType");
			if (StringUtils.isNotEmpty(dataTypeName)) {
				dataType = JsonUtils.getDataType(dataTypeName, context);
			}

			if ("expression".equals(type)) {
				ExpressionFilterCriterion efc = new ExpressionFilterCriterion();
				criterion = efc;
				efc.setDataType(dataType);
				efc.setProperty(JsonUtils
						.getString(criterionNode, "expression"));
			} else if ("property".equals(type)) {
				PropertyFilterCriterion pfc = new PropertyFilterCriterion();
				criterion = pfc;
				pfc.setProperty(JsonUtils.getString(criterionNode, "property"));
			} else {
				SingleValueFilterCriterion vfc = new SingleValueFilterCriterion();
				criterion = vfc;
				vfc.setDataType(dataType);
				vfc.setValue(JsonUtils.toJavaObject(criterionNode.get("value"),
						dataType));
			}
			criterion.setProperty(JsonUtils
					.getString(criterionNode, "property"));
			criterion.setFilterOperator(toFilterOperator(JsonUtils.getString(
					criterionNode, "operator")));
			return criterion;
		} else {
			Junction junction;
			if ("or".equals(junctionValue)) {
				junction = new Or();
			} else {
				junction = new And();
			}

			JsonNode valueNode = criterionNode.get("criterions");
			if (valueNode != null && !valueNode.isNull()) {
				ArrayNode criterionsNode = (ArrayNode) valueNode;
				Iterator<JsonNode> it = criterionsNode.iterator();
				while (it.hasNext()) {
					Criterion criterion = jsonToCriterion(
							(ObjectNode) it.next(), context);
					junction.addCriterion(criterion);
				}
			}

			return junction;
		}
	}

	private Order jsonToOrder(ObjectNode orderNode, JsonConvertContext context)
			throws Exception {
		Order order = new Order();
		order.setProperty(JsonUtils.getString(orderNode, "property"));
		order.setDesc(JsonUtils.getBoolean(orderNode, "desc"));
		return order;
	}

	public Object fromJSON(JsonNode jsonNode, JsonConvertContext context)
			throws Exception {
		if (jsonNode == null || jsonNode.isNull()) {
			return null;
		}

		Criteria criteria = new Criteria();

		JsonNode valueNode;
		ObjectNode objectNode = (ObjectNode) jsonNode;

		valueNode = objectNode.get("criterions");
		if (valueNode != null && !valueNode.isNull()) {
			ArrayNode criterionsNode = (ArrayNode) valueNode;
			Iterator<JsonNode> it = criterionsNode.iterator();
			while (it.hasNext()) {
				ObjectNode criterionNode = (ObjectNode) it.next();
				Criterion criterion = jsonToCriterion(criterionNode, context);
				criteria.addCriterion(criterion);
			}
		}

		valueNode = objectNode.get("orders");
		if (valueNode != null && !valueNode.isNull()) {
			ArrayNode ordersNode = (ArrayNode) valueNode;
			Iterator<JsonNode> it = ordersNode.iterator();
			while (it.hasNext()) {
				ObjectNode orderNode = (ObjectNode) it.next();
				Order order = jsonToOrder(orderNode, context);
				criteria.addOrder(order);
			}
		}
		return criteria;
	}
}
