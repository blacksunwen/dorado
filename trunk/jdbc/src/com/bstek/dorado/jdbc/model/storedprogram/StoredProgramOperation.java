package com.bstek.dorado.jdbc.model.storedprogram;

import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.util.Assert;

import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.support.AbstractJdbcContext;
import com.bstek.dorado.jdbc.type.JdbcType;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class StoredProgramOperation {

	private StoredProgram storedProgram;
	private StoredProgramContext jdbcContext;
	
	public StoredProgramOperation(StoredProgram storedProgram,
			StoredProgramContext jdbcContext) {
		this.storedProgram = storedProgram;
		this.jdbcContext = jdbcContext;
	}

	public StoredProgramContext getJdbcContext() {
		return this.jdbcContext;
	}
	
	public JdbcEnviroment getJdbcEnviroment() {
		AbstractJdbcContext jdbcContext = getJdbcContext();
		JdbcEnviroment env = jdbcContext.getJdbcEnviroment();
		if (env == null) {
			env = storedProgram.getJdbcEnviroment();
		}
		return env;
	}
	
	public void execute() {
		StoredProgram sp = storedProgram;
		SimpleJdbcCall call = newCall();
		
		StoredProgramContext spContext = getJdbcContext();
		sp.initCall(call, spContext);
		
		doCall(call);
	}
	
	protected SimpleJdbcCall newCall() {
		Assert.notNull(storedProgram, "StoredPropgram must not be null.");
		JdbcEnviroment jdbcEnv = this.getJdbcEnviroment();
		JdbcTemplate jdbcTemplate = jdbcEnv.getSpringNamedDao().getJdbcTemplate();
		SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate);
		
		return call;
	}
	
	protected void doCall(SimpleJdbcCall call) {
		StoredProgram sp = storedProgram;
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
