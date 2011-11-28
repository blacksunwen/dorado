package com.bstek.dorado.jdbc.model.storedprogram;

import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.util.Assert;

import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.model.DbElement;
import com.bstek.dorado.jdbc.model.DbElementJdbcOperation;
import com.bstek.dorado.jdbc.type.JdbcType;

public class StoredProgramOperation extends DbElementJdbcOperation<StoredProgramContext> {

	public StoredProgramOperation(DbElement dbElement,
			StoredProgramContext jdbcContext) {
		super(dbElement, jdbcContext);
	}

	@Override
	public void execute() {
		StoredProgram sp = getStoredProgram();
		SimpleJdbcCall call = newCall();
		
		StoredProgramContext spContext = getJdbcContext();
		sp.initCall(call, spContext);
		
		doCall(call);
	}

	protected StoredProgram getStoredProgram() {
		StoredProgram sp = (StoredProgram) this.getDbElement();
		return sp;
	}
	
	protected SimpleJdbcCall newCall() {
		Assert.notNull(this.getDbElement(), "StoredPropgram must not be null.");
		JdbcEnviroment jdbcEnv = this.getJdbcEnviroment();
		JdbcTemplate jdbcTemplate = jdbcEnv.getNamedDao().getJdbcTemplate();
		SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate);
		
		return call;
	}
	
	protected void doCall(SimpleJdbcCall call) {
		StoredProgram sp = getStoredProgram();
		StoredProgramContext spContext = getJdbcContext();
		
		MapSqlParameterSource spSource = spContext.getSqlParameterSource();
		Map<String,Object> dbResult = call.execute(spSource);
		
		returnValue(sp, spContext, dbResult);
	}

	protected void returnValue(StoredProgram sp,
			StoredProgramContext spContext, Map<String, Object> dbResult) {
		ProgramParameter[] outParameters = sp.getOutableParameters();
		if (outParameters.length > 0) {
			if (outParameters.length == 1) {
				ProgramParameter parameter = outParameters[0];
				String name = parameter.getName();
				JdbcType jdbcType = parameter.getJdbcType();
				
				Object dbValue = dbResult.get(name);
				Object value = jdbcType.fromDB(dbValue);
				
				spContext.setReturnValue(value);
			} else {
				for (ProgramParameter parameter: outParameters) {
					String name = parameter.getName(); 
					JdbcType jdbcType = parameter.getJdbcType();
					
					Object dbValue = dbResult.get(name);
					Object value = jdbcType.fromDB(dbValue);
					dbResult.put(name, value);
				}
				spContext.setReturnValue(dbResult);
			}
		}
	}
}
