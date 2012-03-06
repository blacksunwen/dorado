package com.bstek.dorado.jdbc.support;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class ThreadLocalJdbcEnviroment extends AbstractJdbcEnviroment {

	private ThreadLocal<DataSource> localDataSource = new ThreadLocal<DataSource>();
	
	@Override
	public DataSource getDataSource() {
		return localDataSource.get();
	}
	
	public void setDataSource(DataSource dataSource) {
		if (dataSource == null) {
			localDataSource.remove();
		} else {
			localDataSource.set(dataSource);
		}
		
		localDao.remove();
		localTxMgr.remove();
		localTxDef.remove();
	}

	private ThreadLocal<NamedParameterJdbcDaoSupport> localDao = new ThreadLocal<NamedParameterJdbcDaoSupport>();
	
	@Override
	public NamedParameterJdbcDaoSupport getNamedDao() {
		NamedParameterJdbcDaoSupport dao = localDao.get();
		if (dao == null) {
			DataSource dataSource = getDataSource();
			if (dataSource == null) {
				throw new NullPointerException("No DataSource be found in Thread.");
			}
			
			dao = new NamedParameterJdbcDaoSupport();
			dao.setDataSource(getDataSource());
			dao.afterPropertiesSet();
			
			localDao.set(dao);
		}
		
		return dao;
	}
	
	private ThreadLocal<PlatformTransactionManager> localTxMgr = new ThreadLocal<PlatformTransactionManager>();

	@Override
	public PlatformTransactionManager getTransactionManager() {
		PlatformTransactionManager txMgr = localTxMgr.get();
		if (txMgr == null) {
			txMgr = createTransactionManager();
			if (txMgr != null) {
				localTxMgr.set(txMgr);
			}
		}
		
		return txMgr;
	}
	
	protected PlatformTransactionManager createTransactionManager() {
		DataSource dataSource = getDataSource();
		if (dataSource != null) {
			DataSourceTransactionManager txMgr = new DataSourceTransactionManager(dataSource);
			return txMgr;
		} else {
			return null;
		}
	}
	
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		if (transactionManager != null) {
			localTxMgr.set(transactionManager);
		} else {
			localTxMgr.remove();
		}
	}

	private ThreadLocal<TransactionDefinition> localTxDef = new ThreadLocal<TransactionDefinition>();
	
	@Override
	public TransactionDefinition getTransactionDefinition() {
		return localTxDef.get();
	}

	public void setTransactionDefinition(TransactionDefinition transactionDefinition) {
		if (transactionDefinition != null) {
			this.localTxDef.set(transactionDefinition);
		} else {
			this.localTxDef.remove();
		}
	}

}
