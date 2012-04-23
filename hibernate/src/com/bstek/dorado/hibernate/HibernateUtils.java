package com.bstek.dorado.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.EntityType;
import org.hibernate.type.Type;

import com.bstek.dorado.data.ParameterWrapper;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.filter.FilterOperator;
import com.bstek.dorado.data.provider.filter.SingleValueFilterCriterion;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.hibernate.criteria.criterion.SingleCriterion;

public final class HibernateUtils {
	private HibernateUtils(){}

	/**
	 * 根据属性路径获取在hibernate中的Type
	 * @param propertyPath
	 * @param classMetadata
	 * @param sessionFactory
	 * @return
	 */
	public static Type getHibernateType(String propertyPath, 
			ClassMetadata classMetadata, SessionFactory sessionFactory) {
		String [] tokens = StringUtils.split(propertyPath, '.');
		if (tokens.length == 1) {
			return classMetadata.getPropertyType(propertyPath);
		} else if (tokens.length > 1) {
			Type type = null;
			ClassMetadata meta = classMetadata;
			for (String token: tokens) {
				type = meta.getPropertyType(token);
				if (type instanceof EntityType) {
					EntityType entityType = (EntityType)type;
					String entityName = entityType.getAssociatedEntityName();
					meta = sessionFactory.getClassMetadata(entityName);
				}
			}
			return type;
		}
		return null;
	}
	
	public static void makeCount(CriteriaImpl entityCriteria) throws Exception {
		CriteriaImplHelper helper = new CriteriaImplHelper(entityCriteria);
		helper.makeCount(entityCriteria);
	}
	
	public static com.bstek.dorado.data.provider.Criteria getFilterCriteria(Object parameter) {
		if (parameter instanceof ParameterWrapper) {
			ParameterWrapper pw = (ParameterWrapper)parameter;
			Map<String, Object> sysParameter = pw.getSysParameter();
			
			if (sysParameter instanceof Record) {
				Record paraRecord = (Record)sysParameter;
				return (Criteria)paraRecord.get("criteria");
			}
		}
		
		return null;
	}
	
	public static DetachedCriteria createFilter(DetachedCriteria detachedCriteria, com.bstek.dorado.data.provider.Criteria filterCriteria) throws Exception {
		CriteriaImplHelper helper = new CriteriaImplHelper(detachedCriteria);
		mergeFilter(helper, filterCriteria);
		return detachedCriteria;
	}
	
	public static void mergeFilter(CriteriaImplHelper helper, com.bstek.dorado.data.provider.Criteria filterCriteria) throws Exception {
		CriteriaImpl entityCriteria = helper.getCriteriaImpl();
		
		List<com.bstek.dorado.data.provider.Criterion> filterCriterions = filterCriteria.getCriterions();
		for (com.bstek.dorado.data.provider.Criterion fCriterion : filterCriterions) {
			if (fCriterion instanceof SingleValueFilterCriterion) {
				SingleValueFilterCriterion filterCriterion = (SingleValueFilterCriterion)fCriterion;
				FilterOperator filterOperator = filterCriterion.getFilterOperator();
				Object filterValue = filterCriterion.getValue();
				String property = filterCriterion.getProperty();
				String propertyPath = StringUtils.defaultIfEmpty(filterCriterion.getPropertyPath(), property) ;
				String criteriaPath = helper.getCriteriaPath(propertyPath);
				
				if (FilterOperator.eq.equals(filterOperator)) {
					Criterion expression = SingleCriterion.OP.eq.criterion(criteriaPath, filterValue);
					entityCriteria.add(expression);
				} else if (FilterOperator.ne.equals(filterOperator)) {
					Criterion expression = SingleCriterion.OP.ne.criterion(criteriaPath, filterValue);
					entityCriteria.add(expression);
				} else if (FilterOperator.gt.equals(filterOperator)) {
					Criterion expression = SingleCriterion.OP.gt.criterion(criteriaPath, filterValue);
					entityCriteria.add(expression);
				} else if (FilterOperator.lt.equals(filterOperator)) {
					Criterion expression = SingleCriterion.OP.lt.criterion(criteriaPath, filterValue);
					entityCriteria.add(expression);
				} else if (FilterOperator.ge.equals(filterOperator)) {
					Criterion expression = SingleCriterion.OP.ge.criterion(criteriaPath, filterValue);
					entityCriteria.add(expression);
				} else if (FilterOperator.le.equals(filterOperator)) {
					Criterion expression = SingleCriterion.OP.le.criterion(criteriaPath, filterValue);
					entityCriteria.add(expression);
				} else if (FilterOperator.like.equals(filterOperator)) {
					Criterion expression = SingleCriterion.OP.likeAnyWhere.criterion(criteriaPath, filterValue);
					entityCriteria.add(expression);
				} else if (FilterOperator.between.equals(filterOperator)) {
					Object[] values = (Object[])filterValue;
					Criterion expression = Restrictions.between(criteriaPath, values[0], values[1]);
					entityCriteria.add(expression);
				} else if (FilterOperator.in.equals(filterOperator)) {
					Object[] values = (Object[])filterValue;
					Criterion expression = Restrictions.in(criteriaPath, values);
					entityCriteria.add(expression);
				} else {
					throw new IllegalArgumentException("unknown FilterOperator [" + filterOperator + "]");
				}
			}
		}
		
		List<com.bstek.dorado.data.provider.Order> filterOrders = filterCriteria.getOrders();
		List<org.hibernate.criterion.Order> hOrders = new ArrayList<org.hibernate.criterion.Order>();
		
		for (com.bstek.dorado.data.provider.Order fOrder: filterOrders) {
			String property = fOrder.getProperty();
			String propertyPath = StringUtils.defaultIfEmpty(fOrder.getPropertyPath(), property) ;
			String criteriaPath = helper.getCriteriaPath(propertyPath);
			
			org.hibernate.criterion.Order hOrder = fOrder.isDesc()? org.hibernate.criterion.Order.desc(criteriaPath):
				org.hibernate.criterion.Order.asc(criteriaPath);
			
			hOrders.add(hOrder);
		}
		
		if (!hOrders.isEmpty()) {
			helper.mergeOrders(hOrders);
		}
	}
}
