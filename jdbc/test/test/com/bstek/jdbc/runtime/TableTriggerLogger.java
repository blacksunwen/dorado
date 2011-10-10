package test.com.bstek.jdbc.runtime;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.bstek.dorado.jdbc.model.DbElementTrigger;
import com.bstek.dorado.jdbc.model.QueryOperation;

public class TableTriggerLogger implements DbElementTrigger{

	@Override
	public void doQuery(QueryOperation operation) {

		System.out.println("Begin::" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
		try {
			operation.execute();
		} finally {
			System.out.println("End::" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
		}
		
	}

}
