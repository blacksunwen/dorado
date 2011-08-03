package com.bstek.dorado.hibernate.hql;

import org.hibernate.Session;

import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.hibernate.provider.HqlDataProvider;

public interface HqlQuerier {

	HqlParameterResolver getHqlParameterResolver();

	Object query(Session session, Object parameter, 
			Hql hql, HqlDataProvider provider) throws Exception;

	void query(Session session, Object parameter, 
			Hql hql, Page<?> page, HqlDataProvider provider) throws Exception;
	
	int count(Session session, Object parameter, 
			Hql hql, HqlDataProvider provider) throws Exception;
}
