package com.bstek.dorado.jdbc.support;

import java.util.Collection;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDao;
import com.bstek.dorado.jdbc.model.DbTable;
import com.bstek.dorado.util.Assert;

public class SaveCommand {

	@SuppressWarnings("unchecked")
	public void execute(SaveOperation operation) throws Exception {
		Object data = operation.getData();
		
		DbTable dbTable = operation.getDbTable();
		DataResolverContext jdbcContext = operation.getJdbcContext();
		
		JdbcDao jdbcDao = operation.getJdbcDao();
		
		if (data instanceof Record) {
			SaveRecordOperation sro = new SaveRecordOperation(dbTable, (Record)data, jdbcContext);
			jdbcDao.doSave(sro);
		} else if (data instanceof Collection) {
			Collection<Object> datas = (Collection<Object>) data;
			for (Object d: datas) {
				Assert.notNull(d, "data element must not be null.");
				if (d instanceof Record) {
					SaveRecordOperation sro = new SaveRecordOperation(dbTable, (Record)d, jdbcContext);
					jdbcDao.doSave(sro);
				} else {
					throw new IllegalArgumentException("Unknown data type [" + d.getClass() + "]");
				}
			}
		}
	}

}
