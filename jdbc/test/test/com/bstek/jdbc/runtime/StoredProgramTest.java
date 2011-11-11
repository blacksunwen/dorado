package test.com.bstek.jdbc.runtime;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;

import com.bstek.dorado.data.config.ConfigManagerTestSupport;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.model.storedprogram.ProgramParameter;
import com.bstek.dorado.jdbc.model.storedprogram.SPRunner;
import com.bstek.dorado.jdbc.model.storedprogram.StoredProcedure;

public class StoredProgramTest extends ConfigManagerTestSupport {

	public StoredProgramTest() {
		this.addExtensionContextConfigLocation("test/com/bstek/jdbc/runtime/test-context.xml");
	}
	
	public void test1() throws Exception {
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment("oracle11");
		
		StoredProcedure sp = new StoredProcedure();
		sp.setName("demo.ins_dic");
		sp.setCatalog("DEMO");
		sp.setProgramName("ins_dic");
		sp.setJdbcEnviroment(jdbcEnv);

		{
			ProgramParameter parameter = new ProgramParameter();
			parameter.setName("id");
			parameter.setType(ProgramParameter.Type.IN);
			parameter.setJdbcType(jdbcEnv.getDialect().getJdbcType("INTEGER-Integer"));
			parameter.setValue("100");
			
			sp.addParameter(parameter);
		}{
			ProgramParameter parameter = new ProgramParameter();
			parameter.setName("pid");
			parameter.setType(ProgramParameter.Type.IN);
			parameter.setJdbcType(jdbcEnv.getDialect().getJdbcType("INTEGER-Integer"));
			parameter.setValue("1");
			
			sp.addParameter(parameter);
		}{
			ProgramParameter parameter = new ProgramParameter();
			parameter.setName("title");
			parameter.setType(ProgramParameter.Type.IN);
			parameter.setJdbcType(jdbcEnv.getDialect().getJdbcType("VARCHAR-String"));
			parameter.setValue("上海...");
			
			sp.addParameter(parameter);
		}
		
		SPRunner.execute(sp, null);
	}
	
	public void test2() throws Exception {
		StoredProcedure sp = spInsDic();
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", Integer.valueOf(RandomStringUtils.randomNumeric(6)));
		param.put("pid", 10);
		param.put("title", RandomStringUtils.randomAlphabetic(10));
		
		SPRunner.execute(sp, param);
	}
	
	private StoredProcedure spInsDic() {
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment("oracle11");
		StoredProcedure sp = new StoredProcedure();
		sp.setName("demo.ins_dic");
		sp.setCatalog("DEMO");
		sp.setProgramName("ins_dic");
		sp.setJdbcEnviroment(jdbcEnv);
		{
			ProgramParameter parameter = new ProgramParameter();
			parameter.setName("id");
			parameter.setType(ProgramParameter.Type.IN);
			parameter.setJdbcType(jdbcEnv.getDialect().getJdbcType("INTEGER-Integer"));
			parameter.setValue(":id");
			
			sp.addParameter(parameter);
		}{
			ProgramParameter parameter = new ProgramParameter();
			parameter.setName("pid");
			parameter.setType(ProgramParameter.Type.IN);
			parameter.setJdbcType(jdbcEnv.getDialect().getJdbcType("INTEGER-Integer"));
			parameter.setValue(":pid");
			
			sp.addParameter(parameter);
		}{
			ProgramParameter parameter = new ProgramParameter();
			parameter.setName("title");
			parameter.setType(ProgramParameter.Type.IN);
			parameter.setJdbcType(jdbcEnv.getDialect().getJdbcType("VARCHAR-String"));
			parameter.setValue(":title");
			
			sp.addParameter(parameter);
		}
		
		return sp;
	}
}
