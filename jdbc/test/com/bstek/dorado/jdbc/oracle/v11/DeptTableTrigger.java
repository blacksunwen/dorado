package com.bstek.dorado.jdbc.oracle.v11;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDao;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.model.AbstractDbTableTrigger;
import com.bstek.dorado.jdbc.model.Table;
import com.bstek.dorado.jdbc.support.SaveRecordOperation;

public class DeptTableTrigger extends AbstractDbTableTrigger {

	@Override
	public void doSave(SaveRecordOperation operation) throws Exception {
		Record dept = operation.getRecord();
		if (EntityUtils.getState(dept) == EntityState.DELETED) {
			JdbcDao jdbcDao = operation.getJdbcDao();
			Table employee = JdbcUtils.getDbTable("EMPLOYEE");
			
			jdbcDao.deleteAll(employee, "DEPT_ID", dept.get("DEPT_ID"));
		}
		
		super.doSave(operation);
	}

//	@Override
//	public void doSave(SaveOperation operation) throws Exception{
//		Record dept = operation.getRecord();
//		if (EntityUtils.getState(dept) == EntityState.DELETED) {
//			JdbcEnviroment env = operation.getJdbcEnviroment();
//			NamedParameterJdbcDaoSupport dao = env.getSpringNamedDao();
//			
//			String sql = "delete from EMPLOYEE where DEPT_ID = :DEPT_ID";
//			Map<String, Object> paramMap = new HashMap<String, Object>();
//			paramMap.put("DEPT_ID", dept.get("DEPT_ID"));
//			
//			System.out.println("|- "+sql + " " + paramMap);
//			dao.getNamedParameterJdbcTemplate().update(sql, paramMap);
//		}
//		super.doSave(operation);
//	}

}
