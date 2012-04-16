package com.bstek.dorado.hibernate.criteria;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.bstek.dorado.hibernate.criteria.criterion.BaseCriterion;
import com.bstek.dorado.hibernate.criteria.order.Order;
import com.bstek.dorado.hibernate.criteria.projection.BaseProjection;
import com.bstek.dorado.util.Assert;

public abstract class BaseHibernateCriteriaTransformer implements
		HibernateCriteriaTransformer {

	public DetachedCriteria toHibernate(TopCriteria topCriteria, Object parameter,
			SessionFactory sessionFactory)
			throws Exception {
		DetachedCriteria criteria = this.create(topCriteria);
		this.attach(criteria, topCriteria, parameter, sessionFactory);
		return criteria;
	}
	
	protected DetachedCriteria create(TopCriteria topCriteria) {
		String entityName = topCriteria.getEntityName();
		Class<Object> entityClazz = topCriteria.getEntityClazz();
		if (StringUtils.isEmpty(entityName) && entityClazz == null) {
			throw new IllegalArgumentException("entityName or entityClazz must not be null.");
		}
		
		String alias = topCriteria.getAlias();
		if (StringUtils.isEmpty(alias)) {
			if (StringUtils.isNotEmpty(entityName)) {
				return DetachedCriteria.forEntityName(entityName);
			} else {
				return DetachedCriteria.forClass(entityClazz); 
			}
		} else {
			if (StringUtils.isNotEmpty(entityName)) {
				return DetachedCriteria.forEntityName(entityName, alias);
			} else {
				return DetachedCriteria.forClass(entityClazz, alias); 
			}
		}
	}
	
	protected void attach(DetachedCriteria criteria, BaseCriteria defCriteria, 
			Object parameter, SessionFactory sessionFactory) throws Exception {
		this.alias(criteria, defCriteria, parameter, sessionFactory);
		this.projection(criteria, defCriteria, sessionFactory);
		this.criterion(criteria, defCriteria, parameter, sessionFactory);
		this.order(criteria, defCriteria);
		this.fetchMode(criteria, defCriteria);
		this.subCriteria(criteria, defCriteria, parameter, sessionFactory);
	}
	
	//-------------------------- Alias ---------------------------
	protected void alias(DetachedCriteria criteria, BaseCriteria defCriteria, 
			Object parameter, SessionFactory sessionFactory) throws Exception{
		List<Alias> aliases = defCriteria.getAliases();
		if (aliases == null || aliases.size() == 0) return;
		
		for (Alias alias: aliases) {
			if (alias.isAvailable()) {
				this.addAlias(criteria, alias, parameter, sessionFactory);
			}
		}
	}
	
	protected abstract void addAlias(DetachedCriteria criteria, 
			Alias alias, Object parameter, SessionFactory sessionFactory) 
		throws Exception;
	
	//-------------------------- Projection ---------------------------
	protected void projection(DetachedCriteria criteria, 
			BaseCriteria defCriteria, SessionFactory sessionFactory) {
		List<BaseProjection> projections = defCriteria.getProjections();
		if (projections == null || projections.size() == 0) return;
		
		List<Projection> pList = new ArrayList<Projection>(projections.size());
		for (BaseProjection proj : projections) {
			if (!proj.isAvailable()) {
				continue;
			}
			
			Projection projection = proj.toHibernate(sessionFactory);
			
			if (projection != null) {
				String alias = proj.getAlias();
				if (StringUtils.isNotEmpty(alias)) {
					projection = Projections.alias(projection, alias);
				}
				pList.add(projection);
			}
		}
		
		if (pList.size() > 0) {
			if (pList.size() == 1) {
				Projection p = pList.get(0);
				criteria.setProjection(p);
			} else {
				ProjectionList projList = Projections.projectionList();
				for (Projection proj: pList) {
					projList.add(proj);
				}
				criteria.setProjection(projList);
			}
		}
	}
	
	//-------------------------- Criterion ---------------------------
	protected void criterion(DetachedCriteria criteria, 
			BaseCriteria defCriteria, Object parameter, 
			SessionFactory sessionFactory) throws Exception{
		List<BaseCriterion> criterions = defCriteria.getCriterions();
		if (criterions == null || criterions.size() == 0) {
			return;
		} else {
			for (BaseCriterion defCri: criterions) {
				Criterion criterion = this.criterion(defCri, parameter, 
						sessionFactory);
				if (criterion != null) {
					criteria.add(criterion);
				}
			}
		}
	}
	
	protected Criterion criterion(BaseCriterion cri, 
			Object parameter, SessionFactory sessionFactory) throws Exception{
		if (!cri.isAvailable()) {
			return null;
		}
		
		Criterion criterion = cri.toHibernate(parameter, sessionFactory, this);
		
		if (criterion != null) {
			if (cri.isNot()) {
				criterion = Restrictions.not(criterion);
			}
		}
		return criterion;
	}
	
	public List<Criterion> listCriterion(List<BaseCriterion> defCris, 
			Object parameter, SessionFactory sessionFactory) 
		throws Exception{
		if (defCris == null || defCris.size() == 0)return null;
		
		List<Criterion> criterions = new ArrayList<Criterion>();
		for (BaseCriterion defCriterion: defCris) {
			Criterion criterion = criterion(defCriterion, parameter, sessionFactory);
			if (criterion != null) {
				criterions.add(criterion);
			}
		}
		return criterions;
	}
	
	//-------------------------- Order ---------------------------
	protected abstract org.hibernate.criterion.Order toHibernate(Order def);
	
	protected void order(DetachedCriteria criteria, 
			BaseCriteria defCriteria) {
		List<Order> defOrders = defCriteria.getOrders();
		if (defOrders == null || defOrders.size() == 0) {
			return;
		} else {
			for (Order defOrder: defOrders) {
				if (defOrder.isAvailable()) {
					org.hibernate.criterion.Order order = toHibernate(defOrder);;
					if (order != null) {
						if (defOrder.isIgnoreCase()) {
							order = order.ignoreCase();
						}
						criteria.addOrder(order);
					}
				}
			}
		}
	}
	
	//-------------------------- FetchMode ---------------------------
	protected abstract org.hibernate.FetchMode toHibernate(FetchMode def);
	
	protected void fetchMode(DetachedCriteria criteria, 
			BaseCriteria defCriteria) {
		List<FetchMode> fetchModes = defCriteria.getFetchModes();
		if (fetchModes == null || fetchModes.size() == 0) {
			return;
		} else {
			for (FetchMode fetchMode: fetchModes) {
				if (fetchMode.isAvailable()) {
					org.hibernate.FetchMode f = toHibernate(fetchMode);
					if (f != null) {
						String associationPath = fetchMode.getAssociationPath();
						Assert.notEmpty(associationPath, "parameter 'associationPath' must not be empty.");
						criteria.setFetchMode(associationPath, f);
					}
				}
			}
		}
	}

	//-------------------------- SubCriteria ---------------------------
	protected void subCriteria(DetachedCriteria criteria,  BaseCriteria defCriteria, 
			Object parameter, SessionFactory sessionFactory) throws Exception{
		List<SubCriteria> subCriterias = defCriteria.getSubCriterias();
		if (subCriterias == null || subCriterias.size() == 0) {
			return;
		} else {
			for (SubCriteria sub: subCriterias) {
				if (sub.isAvailable()) {
					String alias = sub.getAlias();
					String associationPath = sub.getAssociationPath();
					JoinType joinType = sub.getJoinType();
					
					if (joinType != null) {
						DetachedCriteria subCriter = criteria.createCriteria(associationPath, alias, joinType.getHibernateFlag());
						this.attach(subCriter, sub, parameter, sessionFactory);
					} else {
						DetachedCriteria subCriter = criteria.createCriteria(associationPath, alias);
						this.attach(subCriter, sub, parameter, sessionFactory);
					}
				}
			}
		}
	}
}
