package test.com.bstek.jdbc.runtime;

import java.util.Date;

import org.apache.commons.lang.RandomStringUtils;

import com.bstek.dorado.data.config.ConfigManagerTestSupport;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDataResolverContext;
import com.bstek.dorado.jdbc.JdbcRecordOperation;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.model.table.Table;

public class Resolver_TableTest extends ConfigManagerTestSupport {
	public Resolver_TableTest() {
		this.addExtensionContextConfigLocation("test/com/bstek/jdbc/runtime/test-context.xml");
	}
	
	public void test_m1_1() throws Exception {
		String objRef = "EMP";
		Table table = (Table)JdbcUtils.getDbElement(objRef);
		
		Record r = new Record();
		r.set("ID", new Integer(102));
		r.set("LAST_NAME", RandomStringUtils.randomAscii(10));
		r.set("FIRST_NAME", RandomStringUtils.randomAscii(10));
		r.set("TITLE", RandomStringUtils.randomAscii(20));
		r.set("TITLE_OF_COURTESY", RandomStringUtils.randomAscii(20));
		r.set("SEX", Short.valueOf("1"));
		r.set("BIRTH_DATE", new Date(1980, 2, 21));
		r.set("HIRE_DATE", new Date(2010, 12, 30));
		r.set("ADDRESS", RandomStringUtils.randomAscii(60));
		r.set("CITY", RandomStringUtils.randomAscii(15));
		r.set("REGION", RandomStringUtils.randomAscii(15));
		r.set("POSTAL_CODE", RandomStringUtils.randomAscii(10));
		r.set("COUNTRY", RandomStringUtils.randomAscii(15));
		r.set("PHONE", RandomStringUtils.randomAlphabetic(20));
		r.set("EXTENSION", RandomStringUtils.randomAlphabetic(4));
		r.set("REPORTS_TO", "2");
		r.set("NOTES", RandomStringUtils.randomAscii(100));
		r.set("PHOTO_PATH", RandomStringUtils.randomAscii(100));
		
		r = (Record)EntityUtils.toEntity(r, null);
		
		{
			JdbcDataResolverContext jdbcContext = new JdbcDataResolverContext(table.getJdbcEnviroment(),null,null,null);
			
			r.getEntityEnhancer().setState(EntityState.DELETED);
			JdbcRecordOperation operation = new JdbcRecordOperation(table, r, jdbcContext);
			operation.execute();
		}{
			JdbcDataResolverContext jdbcContext = new JdbcDataResolverContext(table.getJdbcEnviroment(),null,null,null);
			
			r.getEntityEnhancer().setState(EntityState.NEW);
			JdbcRecordOperation operation = new JdbcRecordOperation(table, r, jdbcContext);
			operation.execute();
		}{
			JdbcDataResolverContext jdbcContext = new JdbcDataResolverContext(table.getJdbcEnviroment(),null,null,null);
			
			r.set("REPORTS_TO", "3");
			r.getEntityEnhancer().setState(EntityState.MODIFIED);
			JdbcRecordOperation operation = new JdbcRecordOperation(table, r, jdbcContext);
			operation.execute();
		}{
			JdbcDataResolverContext jdbcContext = new JdbcDataResolverContext(table.getJdbcEnviroment(),null,null,null);
			
			r.getEntityEnhancer().setState(EntityState.DELETED);
			JdbcRecordOperation operation = new JdbcRecordOperation(table, r, jdbcContext);
			operation.execute();
		}
		
	}
}
