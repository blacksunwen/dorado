package com.bstek.dorado.hibernate;

import java.lang.reflect.Field;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.EntityType;
import org.hibernate.type.Type;

public abstract class HibernateUtils {

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
	
	private static Field CriteriaImpl_maxResults = null;
	private static Field CriteriaImpl_firstResult = null;
	
	@SuppressWarnings("rawtypes")
	public static void makeCount(CriteriaImpl entityCriteria) throws Exception {
		//查询记录条数
		CriteriaImpl countCriteria = entityCriteria;
		Iterator orderItr = countCriteria.iterateOrderings();
		if (orderItr != null) {
			while (orderItr.hasNext()) {
				orderItr.next();
				orderItr.remove();
			}
		}
		countCriteria.setProjection(Projections.rowCount());
		countCriteria.setResultTransformer(Criteria.ROOT_ENTITY);
		
		if (CriteriaImpl_maxResults == null) {
			Field f = countCriteria.getClass().getDeclaredField("maxResults");
			f.setAccessible(true);
			CriteriaImpl_maxResults = f;
		}
		CriteriaImpl_maxResults.set(countCriteria, null);
		
		if (CriteriaImpl_firstResult == null) {
			Field f = countCriteria.getClass().getDeclaredField("firstResult");
			f.setAccessible(true);
			CriteriaImpl_firstResult = f;
		}
		CriteriaImpl_firstResult.set(countCriteria, null);
	}
}
