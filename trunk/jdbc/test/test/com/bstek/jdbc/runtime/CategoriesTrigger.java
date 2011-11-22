package test.com.bstek.jdbc.runtime;

import java.util.Collections;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcRecordOperation;
import com.bstek.dorado.jdbc.model.AbstractTableTrigger;

public class CategoriesTrigger extends AbstractTableTrigger{

	@Override
	public void doSave(JdbcRecordOperation operation) {
		Record record = operation.getRecord();
		if (EntityUtils.getState(record) == EntityState.DELETED) {
			NamedParameterJdbcDaoSupport dao = operation.getJdbcEnviroment().getNamedDao();
			dao.getNamedParameterJdbcTemplate().update("DELETE FROM PRODUCTS WHERE CATEGORY_ID=:catId", 
					Collections.singletonMap("catId", record.get("ID")));
			super.doSave(operation);
		} else {
			super.doSave(operation);	
		}
	}

}
