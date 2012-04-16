package com.bstek.dorado.hibernate;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.impl.CriteriaImpl.OrderEntry;
import org.hibernate.impl.CriteriaImpl.Subcriteria;

public class CriteriaImplHelper {

	private static Field CriteriaImpl_maxResults = null;
	private static Field CriteriaImpl_firstResult = null;
	private static Field Order_propertyName = null;
	private static Field Order_ignoreCase = null;
	private static Field DetachedCriteria_impl = null;
	
	private CriteriaImpl criteria;
	private Map<String, String> aliasMap;
	
	public CriteriaImplHelper(DetachedCriteria detachedCriteria) {
		try {
			criteria = getCriteriaImpl(detachedCriteria);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public CriteriaImpl getCriteriaImpl() {
		return criteria;
	}
	
	public CriteriaImplHelper(CriteriaImpl criteria) {
		this.criteria = criteria;
	}
	
	public void makeCount(CriteriaImpl entityCriteria) throws Exception {
		//查询记录条数
		CriteriaImpl countCriteria = entityCriteria;
		Iterator<?> orderItr = countCriteria.iterateOrderings();
		if (orderItr != null) {
			while (orderItr.hasNext()) {
				orderItr.next();
				orderItr.remove();
			}
		}
		countCriteria.setProjection(Projections.rowCount());
		countCriteria.setResultTransformer(Criteria.ROOT_ENTITY);
		
		this.clearMaxResults(countCriteria);
		this.clearFirstResult(countCriteria);
	}
	
	/**
	 * 根据DataType中的propertyPath计算criteriaPath
	 * @param propertyPath
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getCriteriaPath(String propertyPath) {
		if (aliasMap == null) {
			aliasMap = new HashMap<String, String>();
			Iterator<Subcriteria> subcriteriaList = criteria.iterateSubcriteria();
			while (subcriteriaList.hasNext()) {
				Subcriteria subcriteria = subcriteriaList.next();
				String subAlias = subcriteria.getAlias();
				if (StringUtils.isNotEmpty(subAlias)) {
					String fullPath = getFullPath(subcriteria);
					aliasMap.put(fullPath, subAlias);
				}
			}
		}
		
		if (propertyPath.indexOf('.') < 0) {
			if (aliasMap.containsKey(propertyPath)) {
				return aliasMap.get(propertyPath);
			} else {
				return propertyPath;
			}
		} else {
//			String[] tokens = StringUtils.split(propertyPath, '.');
//			String path = "";
//			
//			for (int i=0; i<tokens.length; i++) {
//				if (i > 0) {
//					path += ".";
//				}
//				path += tokens[i];
//				
//				if (aliasMap.containsKey(path)) {
//					path = aliasMap.get(path);
//				} 
//			}
//			return path;
			
			int splitIndex = propertyPath.lastIndexOf('.');
			String parentPath = propertyPath.substring(0, splitIndex);
			String parentAlias = aliasMap.get(parentPath);
			if (StringUtils.isEmpty(parentAlias)) {
				return propertyPath;
			} else {
				return parentAlias + "." + propertyPath.substring(splitIndex + 1);
			}
		}
	}
	
	private String getFullPath(Subcriteria subcriteria) {
		String subPath = subcriteria.getPath();
		
		Criteria parentCriteria = subcriteria.getParent();
		if (parentCriteria instanceof Subcriteria) {
			return getFullPath((Subcriteria)parentCriteria) + "." + subPath;
		} else {
			return subPath;
		}
	}
	
	/**
	 * 合并给定的排序列表<br>
	 *
	 * <code>规则：给定的排序列表优先级高，已经存在的排序优先级低</code>
	 * 
	 * @param hOrders
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void mergeOrders(List<org.hibernate.criterion.Order> hOrders) throws Exception {
		Iterator<OrderEntry> oldOrderEntryItr = criteria.iterateOrderings();
		Map<String, org.hibernate.criterion.Order> oldOrderMap = new LinkedHashMap<String, org.hibernate.criterion.Order>();
		while (oldOrderEntryItr.hasNext()) {
			OrderEntry orderEntry = oldOrderEntryItr.next();
			if (criteria.equals(orderEntry.getCriteria())) {
				org.hibernate.criterion.Order order = orderEntry.getOrder();
				oldOrderMap.put(getPropertyName(order), order);
				oldOrderEntryItr.remove();
			}
		}
		
		Set<String> newOrderNames = new HashSet<String>();
		for (org.hibernate.criterion.Order newOrder: hOrders) {
			criteria.addOrder(newOrder);
			String propertyName = getPropertyName(newOrder);
			
			if (oldOrderMap.containsKey(propertyName)) {
				org.hibernate.criterion.Order oldOrder = oldOrderMap.get(propertyName);
				if (isIgnoreCase(oldOrder)) {
					newOrder.ignoreCase();
				}
			}
			newOrderNames.add(this.getPropertyName(newOrder));
		}
		
		Set<String> oldOrderNames = oldOrderMap.keySet();
		for (String oldOrderName: oldOrderNames) {
			if (!newOrderNames.contains(oldOrderName)) {
				criteria.addOrder(oldOrderMap.get(oldOrderName));
			} 
		}
	}
	
	private boolean isIgnoreCase(org.hibernate.criterion.Order order) throws Exception {
		if (Order_ignoreCase == null) {
			Field f = org.hibernate.criterion.Order.class.getDeclaredField("ignoreCase");
			f.setAccessible(true);
			Order_ignoreCase = f;
		}
		
		return Order_ignoreCase.getBoolean(order);
	}

	private void clearMaxResults(CriteriaImpl entityCriteria) throws Exception {
		if (CriteriaImpl_maxResults == null) {
			Field f = entityCriteria.getClass().getDeclaredField("maxResults");
			f.setAccessible(true);
			CriteriaImpl_maxResults = f;
		}
		CriteriaImpl_maxResults.set(entityCriteria, null);
	}
	
	private void clearFirstResult(CriteriaImpl entityCriteria) throws Exception {
		if (CriteriaImpl_firstResult == null) {
			Field f = CriteriaImpl.class.getDeclaredField("firstResult");
			f.setAccessible(true);
			CriteriaImpl_firstResult = f;
		}
		CriteriaImpl_firstResult.set(entityCriteria, null);
	}
	
	private String getPropertyName(org.hibernate.criterion.Order order) throws Exception {
		if (Order_propertyName == null) {
			Field f = org.hibernate.criterion.Order.class.getDeclaredField("propertyName");
			f.setAccessible(true);
			Order_propertyName = f;
		}
		
		return (String)Order_propertyName.get(order);
	}
	
	private CriteriaImpl getCriteriaImpl(DetachedCriteria detachedCriteria) throws Exception {
		if (DetachedCriteria_impl == null) {
			Field f = DetachedCriteria.class.getDeclaredField("impl");
			f.setAccessible(true);
			DetachedCriteria_impl = f;
		}
		
		return (CriteriaImpl)DetachedCriteria_impl.get(detachedCriteria);
	}
}
