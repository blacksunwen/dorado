package com.bstek.dorado.jdbc.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDao;
import com.bstek.dorado.jdbc.model.DbTable;

public class SaveCommand {

	@SuppressWarnings("unchecked")
	public void execute(SaveOperation operation) throws Exception {
		Object data = operation.getData();
		
		DbTable dbTable = operation.getDbTable();
		DataResolverContext jdbcContext = operation.getJdbcContext();
		
		JdbcDao jdbcDao = operation.getJdbcDao();
		
		if (data instanceof Record) {
			SaveRecordOperation sro = new SaveRecordOperation(operation.getBatchSql(), dbTable, (Record)data, jdbcContext);
			jdbcDao.doSave(sro);
		} else if (data instanceof Collection) {
			Collection<Object> datas = (Collection<Object>) data;
			List<Record> records = new ArrayList<Record>(datas.size());
			for (Object d: datas) {
				if (d instanceof Record) {
					if (EntityUtils.getState((Record)d) == EntityState.DELETED) {
						SaveRecordOperation sro = new SaveRecordOperation(operation.getBatchSql(), dbTable, (Record)d, jdbcContext);
						jdbcDao.doSave(sro);
					} else {
						records.add((Record)d);
					}
				} else {
					throw new IllegalArgumentException("Unknown data type [" + d.getClass() + "]");
				}
			}
			
			for(Record record: records) {
				SaveRecordOperation sro = new SaveRecordOperation(operation.getBatchSql(), dbTable, record, jdbcContext);
				jdbcDao.doSave(sro);
			}
		}
	}

}
