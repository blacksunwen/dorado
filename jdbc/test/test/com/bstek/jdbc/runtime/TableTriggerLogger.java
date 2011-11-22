package test.com.bstek.jdbc.runtime;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.bstek.dorado.jdbc.JdbcDataProviderOperation;
import com.bstek.dorado.jdbc.JdbcRecordOperation;
import com.bstek.dorado.jdbc.model.AbstractTableTrigger;

public class TableTriggerLogger extends AbstractTableTrigger {

	@Override
	public void doQuery(JdbcDataProviderOperation operation) {
		System.out.println("-");
		System.out.println("Begin::" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
		try {
			System.out.println("DBElement: " + operation.getDbElement().getName());
			System.out.println("Parameter: " + operation.getJdbcContext().getParameter());
			operation.execute();
		} finally {
			System.out.println("End::" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
		}
		
	}

	@Override
	public void doSave(JdbcRecordOperation operation) {
		System.out.println("-");
		System.out.println("Begin::" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
		try {
			System.out.println("DBElement: " + operation.getDbElement().getName());
			System.out.println("Record: " + operation.getRecord());
			operation.execute();
		} finally {
			System.out.println("End::" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
		}	
	}

}
