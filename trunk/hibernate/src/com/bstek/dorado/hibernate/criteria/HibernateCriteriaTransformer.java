package com.bstek.dorado.hibernate.criteria;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;

import com.bstek.dorado.hibernate.criteria.criterion.BaseCriterion;
import com.bstek.dorado.hibernate.criteria.criterion.MisValueStrategy;

public interface HibernateCriteriaTransformer {

	DetachedCriteria toHibernate(TopCriteria topCriteria, Object parameter, 
			SessionFactory sessionFactory) throws Exception;
	
	List<Criterion> listCriterion(List<BaseCriterion> cris, 
			Object parameter, SessionFactory sessionFactory) throws Exception;
	
	Object getValueFromParameter(Object parameter, String dataType, 
			Object value) throws Exception;
	
	MisValueStrategy getMisValueStrategy();
}
