package com.bstek.dorado.jdbc.oracle.v11;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcRecordOperation;
import com.bstek.dorado.jdbc.model.AbstractTableTrigger;

public class DeptTableTrigger extends AbstractTableTrigger {

	@Override
	public void doSave(JdbcRecordOperation operation) {
		Record dept = operation.getRecord();
		if (EntityUtils.getState(dept) == EntityState.DELETED) {
			JdbcEnviroment env = operation.getJdbcEnviroment();
			NamedParameterJdbcDaoSupport dao = env.getNamedDao();
			String sql = "delete from EMPLOYEE where DEPT_ID = :DEPT_ID";
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("DEPT_ID", dept.get("DEPT_ID"));
			
			System.out.println(sql + " " + paramMap);
			dao.getNamedParameterJdbcTemplate().update(sql, paramMap);
		}
		super.doSave(operation);
	}

}
