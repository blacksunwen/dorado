package com.bstek.dorado.hibernate.criteria;

import org.hibernate.SessionFactory;

import com.bstek.dorado.hibernate.provider.UserCriteria;

/**
 * UserCriteria的处理器
 * @author mark
 *
 */
public interface UserCriteriaProcessor {

	void process(TopCriteria defCri, UserCriteria userCri, 
			SessionFactory sessionFactory);
}
