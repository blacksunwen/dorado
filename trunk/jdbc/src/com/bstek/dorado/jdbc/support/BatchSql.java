package com.bstek.dorado.jdbc.support;

import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.jdbc.model.DbTable;
import com.bstek.dorado.jdbc.sql.DeleteSql;
import com.bstek.dorado.jdbc.sql.InsertSql;
import com.bstek.dorado.jdbc.sql.RetrieveSql;
import com.bstek.dorado.jdbc.sql.UpdateSql;

public class BatchSql {

	private InsertSql insertSql = null;
	private UpdateSql updateSql = null;
	private DeleteSql deleteSql = null;
	private RetrieveSql retrieveSql = null;
	
	private EntityDataType dataType;
	private DbTable dbTable; 
	
	public BatchSql(EntityDataType dataType, DbTable dbTable) {
		this.dataType = dataType;
		this.dbTable = dbTable;
	}
	
	public InsertSql getInsertSql() {
		return insertSql;
	}
	public void setInsertSql(InsertSql insertSql) {
		this.insertSql = insertSql;
	}
	public UpdateSql getUpdateSql() {
		return updateSql;
	}
	public void setUpdateSql(UpdateSql updateSql) {
		this.updateSql = updateSql;
	}
	public DeleteSql getDeleteSql() {
		return deleteSql;
	}
	public void setDeleteSql(DeleteSql deleteSql) {
		this.deleteSql = deleteSql;
	}
	public RetrieveSql getRetrieveSql() {
		return retrieveSql;
	}
	public void setRetrieveSql(RetrieveSql retrieveSql) {
		this.retrieveSql = retrieveSql;
	}
	
	public EntityDataType getDataType() {
		return dataType;
	}
	
	public DbTable getDbTable() {
		return dbTable;
	}
	
}
