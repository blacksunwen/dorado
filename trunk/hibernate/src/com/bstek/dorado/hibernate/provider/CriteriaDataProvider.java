package com.bstek.dorado.hibernate.provider;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.impl.CriteriaImpl;
import org.springframework.util.Assert;

import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.provider.AbstractDataProvider;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.hibernate.HibernateUtils;
import com.bstek.dorado.hibernate.criteria.HibernateCriteriaTransformer;
import com.bstek.dorado.hibernate.criteria.TopCriteria;

/**
 * 利用Hibernate条件查询(Criteria Queries)功能实现的DataProvider
 * 
 * @author mark
 * 
 */
@XmlNode(fixedProperties = "type=hibernateCriteria")
public class CriteriaDataProvider extends AbstractDataProvider {
	private String sessionFactory;
	private boolean unique = false;
	private boolean autoFilter = false;

	public String getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(String sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public boolean isUnique() {
		return unique;
	}
	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public void setAutoFilter(boolean autoFilter) {
		this.autoFilter = autoFilter;
	}
	public boolean isAutoFilter() {
		return this.autoFilter;
	}
	
	protected SessionFactory getSessionFactoryOject() throws Exception {
		SessionFactoryManager sessionManager = (SessionFactoryManager) Context
				.getCurrent().getServiceBean("hibernateSessionFactoryManager");
		SessionFactory sessionFactoryBean = sessionManager
				.getSessionFactory(sessionFactory);
		Assert.notNull(sessionFactoryBean, "SessionFactory named [" + sessionFactory + "] cound not be found.");
		return sessionFactoryBean;
	}
	
	protected Session openSession() throws Exception {
		SessionFactory sessionFactory = this.getSessionFactoryOject();
		return sessionFactory.openSession();
	}
	
	protected Session currentSession() throws Exception {
		SessionFactory sessionFactory = this.getSessionFactoryOject();
		return sessionFactory.getCurrentSession();
	}
	
	protected Session session() throws Exception{
		Session session = null;
		try {
			session = this.currentSession();
		} catch (Exception e) {
			session = this.openSession();
		}
		
		return session;
	}
	
	private TopCriteria criterita;

	@XmlSubNode(fixed = true)
	public TopCriteria getCriteria() {
		Assert.notNull(criterita, "Criteria must not be null.");
		return criterita;
	}

	public void setCriteria(TopCriteria criterita) {
		this.criterita = criterita;
	}

	protected HibernateCriteriaTransformer getCriteriaTransformer()
			throws Exception {
		return (HibernateCriteriaTransformer) Context.getCurrent()
				.getServiceBean("criteriaTransformer");
	}

	/**
	 * 创建DetachedCriteria
	 * 
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	protected DetachedCriteria createDetachedCriteria(Object parameter) throws Exception {
		HibernateCriteriaTransformer transformer = getCriteriaTransformer();
		DetachedCriteria detachedCriteria = transformer.toHibernate(getCriteria(), parameter, this.getSessionFactoryOject());
		com.bstek.dorado.data.provider.Criteria filterCriteria = HibernateUtils.getFilterCriteria(parameter);
		if (filterCriteria != null) {
			detachedCriteria = HibernateUtils.createFilter(detachedCriteria, filterCriteria);
		}
		
		return detachedCriteria;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Object internalGetResult(Object parameter, DataType resultDataType)
			throws Exception {
		DetachedCriteria detachedCriteria = this.createDetachedCriteria(parameter);
		Session session = session();
		try {
			Criteria c = detachedCriteria.getExecutableCriteria(session);
			if (!isUnique()) {
				List list = c.list();
				return list;
			} else {
				return c.uniqueResult();
			}
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void internalGetResult(Object parameter, Page<?> page,
			DataType resultDataType) throws Exception {
		DetachedCriteria detachedCriteria = this.createDetachedCriteria(parameter);
		Session session = session();
		try {
			CriteriaImpl entityCriteria = (CriteriaImpl) detachedCriteria
					.getExecutableCriteria(session);
			if (!isUnique()) {
				// 查询结果记录
				entityCriteria.setFirstResult(page
						.getFirstEntityIndex());
				entityCriteria.setMaxResults(page.getPageSize());
				List list = entityCriteria.list();
				page.setEntities(list);

				// 查询记录条数
				CriteriaImpl countCriteria = entityCriteria;
				HibernateUtils.makeCount(countCriteria);

				Number c = (Number) countCriteria.uniqueResult();
				page.setEntityCount(c.intValue());
			} else {
				Object object = entityCriteria.uniqueResult();
				if (object != null) {
					List entities = new ArrayList(1);
					entities.add(object);
					page.setEntities(entities);
					page.setEntityCount(entities.size());
				}
			}
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
	}

//	/**
//	 * 从客户端传过来的参数中取出原始参数，因为Grid具有过滤栏的功能，所以
//	 * 当启用这个功能时参数的类型是UserCriteria，必须从这里取出原始参数
//	 * 
//	 * @param parameter
//	 * @return
//	 * @throws Exception
//	 */
//	protected void prepareProviderCriteria(Object parameter)
//			throws Exception {
//		if (this.isAutoFilter()) {
//			com.bstek.dorado.data.provider.Criteria providerCriteria = 
//					HibernateUtils.getFilterCriteria(parameter);
//			if (providerCriteria == null) return;
//			
//			TopCriteria topCriteria = this.getCriteria();
//			CriteriaPathHelper criteriaPath = new CriteriaPathHelper(topCriteria);
//			
//			//Criterions
//			List<com.bstek.dorado.data.provider.Criterion> providerCriterions = providerCriteria.getCriterions();
//			for (com.bstek.dorado.data.provider.Criterion pCriterion : providerCriterions) {
//				if (pCriterion instanceof SingleValueFilterCriterion) {
//					SingleValueFilterCriterion filterCriterion = (SingleValueFilterCriterion)pCriterion;
//					FilterOperator filterOperator = filterCriterion.getFilterOperator();
//					Object filterValue = filterCriterion.getValue();
//					String property = filterCriterion.getProperty();
//					String propertyPath = StringUtils.defaultIfEmpty(filterCriterion.getPropertyPath(), property) ;;
//					String propertyName = criteriaPath.getFieldAlias(propertyPath);
//					
//					if (FilterOperator.eq.equals(filterOperator)) {
//						SingleCriterion cri = new SingleCriterion();
//						cri.setOp(SingleCriterion.OP.eq);
//						cri.setPropertyName(propertyName);
//						cri.setValue(filterValue);
//						topCriteria.addCriterion(cri);
//					} else if (FilterOperator.ne.equals(filterOperator)) {
//						SingleCriterion cri = new SingleCriterion();
//						cri.setOp(SingleCriterion.OP.ne);
//						cri.setPropertyName(propertyName);
//						cri.setValue(filterValue);
//						topCriteria.addCriterion(cri);
//					} else if (FilterOperator.gt.equals(filterOperator)) {
//						SingleCriterion cri = new SingleCriterion();
//						cri.setOp(SingleCriterion.OP.gt);
//						cri.setPropertyName(propertyName);
//						cri.setValue(filterValue);
//						topCriteria.addCriterion(cri);
//					} else if (FilterOperator.lt.equals(filterOperator)) {
//						SingleCriterion cri = new SingleCriterion();
//						cri.setOp(SingleCriterion.OP.lt);
//						cri.setPropertyName(propertyName);
//						cri.setValue(filterValue);
//						topCriteria.addCriterion(cri);
//					} else if (FilterOperator.ge.equals(filterOperator)) {
//						SingleCriterion cri = new SingleCriterion();
//						cri.setOp(SingleCriterion.OP.ge);
//						cri.setPropertyName(propertyName);
//						cri.setValue(filterValue);
//						topCriteria.addCriterion(cri);
//					} else if (FilterOperator.le.equals(filterOperator)) {
//						SingleCriterion cri = new SingleCriterion();
//						cri.setOp(SingleCriterion.OP.le);
//						cri.setPropertyName(propertyName);
//						cri.setValue(filterValue);
//						topCriteria.addCriterion(cri);
//					} else if (FilterOperator.like.equals(filterOperator)) {
//						SingleCriterion cri = new SingleCriterion();
//						cri.setOp(SingleCriterion.OP.likeAnyWhere);
//						cri.setPropertyName(propertyName);
//						cri.setValue(filterValue);
//						topCriteria.addCriterion(cri);
//					} else if (FilterOperator.between.equals(filterOperator)) {
//						Object[] values = (Object[])filterValue;
//						BetweenCriterion cri = new BetweenCriterion();
//						cri.setValue1(values[0]);
//						cri.setValue2(values[1]);
//						cri.setPropertyName(propertyName);
//						topCriteria.addCriterion(cri);
//					} else if (FilterOperator.in.equals(filterOperator)) {
//						Object[] values = (Object[])filterValue;
//						InCriterion cri = new InCriterion();
//						cri.setPropertyName(propertyName);
//						cri.setValue(values);
//						topCriteria.addCriterion(cri);
//					} else {
//						throw new IllegalArgumentException("unknown FilterOperator [" + filterOperator + "]");
//					}
//				}
//			}
//			
//			//Orders
//			List<com.bstek.dorado.data.provider.Order> providerOrders = providerCriteria.getOrders();
//			for (com.bstek.dorado.data.provider.Order pOrder: providerOrders) {
//				List<com.bstek.dorado.hibernate.criteria.order.Order> hibernateOrders = topCriteria.getOrders();
//				if (!hibernateOrders.isEmpty()) {
//					String property = pOrder.getProperty();
//					String propertyPath = StringUtils.defaultIfEmpty(pOrder.getPropertyPath(), property) ;
//					String propertyName = criteriaPath.getFieldAlias(propertyPath);
//					
//					com.bstek.dorado.hibernate.criteria.order.Order foundOrder = null;
//					for (com.bstek.dorado.hibernate.criteria.order.Order hOrder: hibernateOrders) {
//						if (propertyName.equals(hOrder.getPropertyName())) {
//							foundOrder = hOrder;
//							break;
//						}
//					}
//					
//					if (foundOrder == null) {
//						foundOrder = new com.bstek.dorado.hibernate.criteria.order.Order();
//						foundOrder.setPropertyName(propertyName);
//						topCriteria.addOrder(foundOrder);
//					}
//					
//					if (pOrder.isDesc()) {
//						foundOrder.setDirection(Direction.desc);
//					} else {
//						foundOrder.setDirection(Direction.asc);
//					}
//				}
//			}
//		}
//	}
}
