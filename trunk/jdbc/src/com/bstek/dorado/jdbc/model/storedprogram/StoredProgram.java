package com.bstek.dorado.jdbc.model.storedprogram;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnType;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import com.bstek.dorado.jdbc.JdbcParameterSource;
import com.bstek.dorado.jdbc.model.AbstractDbElement;
import com.bstek.dorado.jdbc.model.storedprogram.ProgramParameter.Type;
import com.bstek.dorado.jdbc.sql.SqlUtils;
import com.bstek.dorado.jdbc.type.JdbcType;
import com.bstek.dorado.util.Assert;

public class StoredProgram extends AbstractDbElement {

	public static final String TYPE = "StoredProgram";
	
	private String programName;
	private String catalog;
	private String schema;
	
	private String   returnValueName = "return";
	private JdbcType returnValueType;
	
	private List<ProgramParameter> parameters = new ArrayList<ProgramParameter>(5);
	
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	
	@Override
	public String getType() {
		return StoredProgram.TYPE;
	}
	
	public ProgramParameter[] getParameters() {
		ProgramParameter[] parameters = this.parameters.toArray(new ProgramParameter[0]);
		if (returnValueType != null) {
			ProgramParameter[] parameters2 = new ProgramParameter[parameters.length + 1];
			ProgramParameter returnParameter = createReturnParameter();
			parameters2[0] = returnParameter;
			System.arraycopy(parameters, 0, parameters2, 1, parameters.length);
			
			return parameters2;
		} else {
			return parameters;
		}
	}
	
	protected ProgramParameter createReturnParameter() {
		ProgramParameter p = new ProgramParameter();
		p.setName(returnValueName);
		p.setJdbcType(returnValueType);
		p.setType(Type.OUT);
		
		return p;
	}
	
	public ProgramParameter[] getOutableParameters() {
		List<ProgramParameter> ps = outableParameters();
		ProgramParameter[] outParameters = ps.toArray(new ProgramParameter[ps.size()]);
		if (returnValueType != null) {
			ProgramParameter[] outParameters2 = new ProgramParameter[outParameters.length + 1];
			ProgramParameter returnParameter = createReturnParameter();
			outParameters2[0] = returnParameter;
			System.arraycopy(outParameters2, 0, outParameters2, 1, outParameters.length);
			
			return outParameters2;
		} else {
			return outParameters;
		}
		
	}
	
	protected List<ProgramParameter> outableParameters() {
		List<ProgramParameter> ins = new ArrayList<ProgramParameter>(parameters.size());
		for (ProgramParameter p: parameters) {
			if (p.getType().equals(ProgramParameter.Type.OUT) && p.getType().equals(ProgramParameter.Type.INOUT)) {
				ins.add(p);
			}
		}
		return ins;
	}
	
	public String getReturnValueName() {
		return returnValueName;
	}
	public void setReturnValueName(String returnValueName) {
		this.returnValueName = returnValueName;
	}
	
	public JdbcType getReturnValueType() {
		return returnValueType;
	}
	public void setReturnValueType(JdbcType returnValueType) {
		this.returnValueType = returnValueType;
	}
	
	public void addParameter(ProgramParameter parameter) {
		parameters.add(parameter);
	}
	
	public void initCall(SimpleJdbcCall call, StoredProgramContext spContext) {
		String catalog = this.getCatalog();
		String schema = this.getSchema();
		String programName = this.getProgramName();
		
		Assert.notEmpty(programName, "programName must not be empty.");
		
		if (returnValueType == null) {//StoredProcedure
			if (StringUtils.isNotEmpty(catalog)) {
				call.withCatalogName(catalog);
			}
			if (StringUtils.isNotEmpty(schema)) {
				call.withSchemaName(schema);
			}
			
			call.withProcedureName(programName);
			call.withoutProcedureColumnMetaDataAccess();
			
			this.declareParameters(call, spContext);
		} else {//StoredFunction
			if (StringUtils.isNotEmpty(catalog)) {
				call.withCatalogName(catalog);
			}
			if (StringUtils.isNotEmpty(schema)) {
				call.withSchemaName(schema);
			}

			call.withFunctionName(programName);
			call.withReturnValue();
			call.withoutProcedureColumnMetaDataAccess();
			
			this.declareParameters(call, spContext);
		}
	}
	
	protected void declareParameters(SimpleJdbcCall call, StoredProgramContext spContext) {
		MapSqlParameterSource spSource = spContext.getSqlParameterSource();
		JdbcParameterSource source = SqlUtils.createJdbcParameter(spContext.getParameter());
		ProgramParameter[] parameters = this.getParameters();
		for (ProgramParameter parameter: parameters) {
			SqlParameter spParam = null;
			JdbcType jdbcType = parameter.getJdbcType();
			String name = parameter.getName();
			int sqlType = jdbcType.getSqlType();
			Integer scale = jdbcType.getScale();
			
			if (parameter.getType() == ProgramParameter.Type.IN) {
				this.inValue(source, parameter, spSource);
				
				if (scale == null) {
					spParam = new SqlParameter(name, sqlType);
				} else {
					spParam = new SqlParameter(name, sqlType, scale);
				}
			} else if (parameter.getType() == ProgramParameter.Type.INOUT) {
				this.inValue(source, parameter, spSource);
				
				SqlReturnType returnType = new JdbcReturnType(jdbcType);
				if (scale == null) {
					spParam = new SqlInOutParameter(name, sqlType, null, returnType);
				} else {
					spParam = new SqlInOutParameter(name, sqlType, scale);
				}
			} else if (parameter.getType() == ProgramParameter.Type.OUT) {
				SqlReturnType returnType = new JdbcReturnType(jdbcType);
				if (scale == null) {
					spParam = new SqlOutParameter(name, sqlType, null, returnType);
				} else {
					spParam = new SqlOutParameter(name, sqlType, scale);
				}
			}
			call.addDeclaredParameter(spParam);
		}
	}
	
	protected void inValue(JdbcParameterSource source, ProgramParameter p, MapSqlParameterSource spSource) {
		JdbcType jdbcType = p.getJdbcType();
		Object pValue = p.getValue();
		Object value = pValue;
		
		if (pValue != null) {
			if (pValue instanceof String) {
				String pvStr = (String)pValue;
				if (pvStr.length() == 0) {
					value = null;
				} else if (pvStr.length() > 1 && pvStr.charAt(0) == ':') {
					String pName = pvStr.substring(1);
					if (source.hasValue(pName)) {
						value = source.getValue(pName);
					} else {
						value = null;
					}
				} 
			}
		}
		
		value = jdbcType.toDB(value);
		spSource.addValue(p.getName(), value, jdbcType.getSqlType(), jdbcType.getTypeName());
	}
}
