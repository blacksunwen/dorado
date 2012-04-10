package com.bstek.dorado.hibernate.provider;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.impl.CriteriaImpl;
import org.springframework.util.Assert;

import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.hibernate.HibernateUtils;
import com.bstek.dorado.hibernate.criteria.HibernateCriteriaTransformer;
import com.bstek.dorado.hibernate.criteria.TopCriteria;
import com.bstek.dorado.hibernate.criteria.UserCriteriaProcessor;

/**
 * 利用Hibernate条件查询(Criteria Queries)功能实现的DataProvider
 * 
 * @author mark
 * 
 */
@XmlNode(fixedProperties = "type=hibernateCriteria")
public class CriteriaDataProvider extends HibernateDataProviderSupport {
	private TopCriteria criterita;

	@XmlSubNode(fixed = true)
	public TopCriteria getCriteria() {
		return criterita;
	}

	public void setCriteria(TopCriteria criterita) {
		this.criterita = criterita;
	}

	public UserCriteriaProcessor getUserCriteriaProcessor() throws Exception {
		return (UserCriteriaProcessor) Context.getCurrent().getServiceBean(
				"userCriteriaProcessor");
	}

	protected HibernateCriteriaTransformer getCriteriaTransformer()
			throws Exception {
		return (HibernateCriteriaTransformer) Context.getCurrent()
				.getServiceBean("criteriaTransformer");
	}

	/**
	 * 从客户端传过来的参数中取出原始参数，因为Grid具有过滤栏的功能，所以
	 * 当启用这个功能时参数的类型是UserCriteria，必须从这里取出原始参数
	 * 
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	protected void prepareProviderCriteria(final Object parameter, DataType resultDataType)
			throws Exception {
		UserCriteria userCriteria = UserCriteriaUtils
				.getUserCriteria(parameter);
		if (userCriteria != null) {
			if (!this.isAutoFilter()) {
				throw new Exception(
						"Unsupported autofilter operation, please check out your configuration.");
			}

			UserCriteriaProcessor processor = getUserCriteriaProcessor();
			UserCriteriaUtils.prepare(userCriteria, resultDataType);
			TopCriteria criteria = getCriteria();
			processor.process(criteria, userCriteria,
					this.getSessionFactoryOject());
		}
	}

	/**
	 * 创建DetachedCriteria
	 * 
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	protected DetachedCriteria createDetachedCriteria(Object parameter,
			DataType resultDataType) throws Exception {
		TopCriteria defCriteria = getCriteria();
		Assert.notNull(defCriteria, "Criteria must not be null.");
		
		this.prepareProviderCriteria(parameter, resultDataType);
		HibernateCriteriaTransformer transformer = getCriteriaTransformer();
		DetachedCriteria detachedCriteria = transformer.toHibernate(
				defCriteria, parameter, this.getSessionFactoryOject());
		return detachedCriteria;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Object internalGetResult(Object parameter, DataType resultDataType)
			throws Exception {
		DetachedCriteria detachedCriteria = createDetachedCriteria(parameter,
				resultDataType);
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
		DetachedCriteria detachedCriteria = createDetachedCriteria(
				parameter, resultDataType);
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

}
