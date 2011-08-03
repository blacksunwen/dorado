package com.bstek.dorado.hibernate.provider;

import org.hibernate.SessionFactory;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-2-20
 */
public interface SessionFactoryManager {

	/**
	 * @param sessionFactory
	 * @return
	 * @throws Exception
	 */
	SessionFactory getSessionFactory(String sessionFactory) throws Exception;

}
