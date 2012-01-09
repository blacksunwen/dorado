package com.bstek.dorado.hibernate.provider;

import org.hibernate.Session;

import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.type.AggregationDataType;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.hibernate.hql.AutoFilterVar;
import com.bstek.dorado.hibernate.hql.Hql;
import com.bstek.dorado.hibernate.hql.HqlQuerier;
import com.bstek.dorado.hibernate.hql.HqlUtil;
import com.bstek.dorado.util.Assert;

/**
 * 利用Hibernate HQL功能实现的DataProvider
 * 
 * @author mark
 */
@XmlNode(fixedProperties = "type=hibernateHql")
public class HqlDataProvider extends HibernateDataProviderSupport {
	private String hql;

	@IdeProperty(editor = "multiLines")
	public String getHql() {
		return hql;
	}

	public void setHql(String hql) {
		this.hql = hql;
	}

	protected Object internalGetResult(Object parameter, DataType resultDataType)
			throws Exception {
		Assert.notNull(this.hql);

		Hql hql = createHql(this.hql, parameter, resultDataType);
		HqlQuerier querier = createHqlQuerier();

		Session session = session();
		try {
			return querier.query(session, parameter, hql, this);
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
	}

	protected void internalGetResult(Object parameter, Page<?> page,
			DataType resultDataType) throws Exception {
		Assert.notNull(this.hql);

		Hql hql = createHql(this.hql, parameter, resultDataType);
		HqlQuerier querier = createHqlQuerier();

		Session session = session();
		try {
			querier.query(session, parameter, hql, page, this);
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
	}

	protected HqlQuerier createHqlQuerier() throws Exception {
		HqlQuerier querier = (HqlQuerier) Context.getCurrent().getServiceBean(
				"hqlQuerier");
		return querier;
	}

	protected Hql createHql(String hqlClause, Object parameter,
			DataType resultDataType) throws Exception {
		Assert.notEmpty(hqlClause);
		UserCriteria userCriteria = UserCriteriaUtils
				.getUserCriteria(parameter);
		if (userCriteria != null) {
			if (!this.isAutoFilter()) {
				throw new Exception(
						"Unsupported autofiler operation, please check out your configuration.");
			}
			EntityDataType entityDataType = findEntityDataType(resultDataType);
			if (entityDataType == null)
				throw new Exception("can't find any EntityDataType.");

			AutoFilterVar filter = new AutoFilterVar(
					this.getSessionFactoryOject(), userCriteria, entityDataType);
			String clause = HqlUtil.build(hqlClause, parameter, filter);
			Hql hql = HqlUtil.build(clause);
			hql.setFilter(filter);
			return hql;
		}

		String clause = HqlUtil.build(hqlClause, parameter);
		Hql hql = HqlUtil.build(clause);

		return hql;
	}

	private EntityDataType findEntityDataType(DataType resultDataType) {
		EntityDataType entityDataType = null;
		if (resultDataType instanceof AggregationDataType) {
			DataType dataType = ((AggregationDataType) resultDataType)
					.getElementDataType();
			if (dataType instanceof EntityDataType) {
				entityDataType = (EntityDataType) dataType;
			}
		} else {
			if (resultDataType instanceof EntityDataType) {
				entityDataType = (EntityDataType) resultDataType;
			}
		}
		return entityDataType;
	}
}
