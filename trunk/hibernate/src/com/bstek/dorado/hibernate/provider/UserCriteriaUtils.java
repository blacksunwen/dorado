package com.bstek.dorado.hibernate.provider;

import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.type.AggregationDataType;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.util.Assert;

public class UserCriteriaUtils {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static UserCriteria getUserCriteria(Object parameter) {
		if (parameter == null) return null;
		
		List<Map> $orders = null;
		List<Map> $criterions = null;
		
		if (parameter instanceof Map) {
			Map userMap = (Map) parameter;
			$orders = (List<Map>)userMap.get("$orders");
			$criterions = (List<Map>)userMap.get("$criterions");
		} else {
			if (EntityUtils.isEntity(parameter)) {
				$orders = (List<Map>)EntityUtils.getValue(parameter, "$orders");
				$criterions = (List<Map>)EntityUtils.getValue(parameter, "$criterions");
			}
		}
		
		if (($orders != null && $orders.size()>0) || 
				($criterions != null && $criterions.size() > 0)) {
			UserCriteria userCriteria = new UserCriteria();
			
			if ($orders != null && $orders.size()>0) {
				for (Map order: $orders) {
					userCriteria.addOrder(
							(String)order.get("property"), 
							(Boolean)order.get("desc"));
				}
			}
			if ($criterions != null && $criterions.size() > 0) {
				for (Map cri: $criterions) {
					userCriteria.addFilterParameter(
							(String)cri.get("property"), 
							(String)cri.get("expression"));
				}
			}
			
			return userCriteria;
		} else {
			return null;
		}
	}
	
	/**
	 * 根据DataType初始化UserCriteria
	 * @param userCriteria
	 * @param resultDataType
	 * @throws Exception
	 */
	public static void prepare(UserCriteria userCriteria, DataType resultDataType) 
		throws Exception {
		Assert.notNull(resultDataType, "the value of resultDataType argument is empty.");
		EntityDataType entityDataType = null;
		if (resultDataType instanceof AggregationDataType) {
			DataType dataType = ((AggregationDataType)resultDataType).getElementDataType();
			if (dataType instanceof EntityDataType) {
				entityDataType = (EntityDataType)dataType;
			} else {
				throw new IllegalArgumentException("the resultDataType is not an instance of [EntityDataType]");
			}
		} else {
			if (resultDataType instanceof EntityDataType) {
				entityDataType = (EntityDataType)resultDataType;
			} else {
				throw new IllegalArgumentException("the resultDataType is not an instance of EntityDataType.");
			}
		}

		if (entityDataType.isAutoCreatePropertyDefs()) {
			entityDataType.createPropertyDefinitons();
		}
 		
		PropertyPathHelper helper = new PropertyPathHelper(entityDataType);
		List<UserCriteria.Parameter> parameters = userCriteria.getFilterParameters();
		for (UserCriteria.Parameter parameter: parameters) {
			String fieldPath = parameter.getField();
			String propertyPath = helper.getPropertyPath(fieldPath);
			parameter.setPropertyPath(propertyPath);
		}
		
		List<UserCriteria.Order> orders = userCriteria.getOrders();
		for (UserCriteria.Order order: orders) {
			String fieldPath = order.getField();
			String propertyPath = helper.getPropertyPath(fieldPath);
			order.setPropertyPath(propertyPath);
		}
	}
}
