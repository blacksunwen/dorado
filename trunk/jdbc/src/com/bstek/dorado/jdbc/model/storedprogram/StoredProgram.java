package com.bstek.dorado.jdbc.model.storedprogram;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.jdbc.model.AbstractDbElement;

public abstract class StoredProgram extends AbstractDbElement {

	private String programName;
	private String catalog;
	private String schema;
	
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
	
	public ProgramParameter[] getParameters() {
		return parameters.toArray(new ProgramParameter[0]);
	}
	
	public ProgramParameter[] getInableParameters() {
		List<ProgramParameter> ins = new ArrayList<ProgramParameter>(parameters.size());
		for (ProgramParameter p: parameters) {
			if (p.getType().equals(ProgramParameter.Type.IN) || p.getType().equals(ProgramParameter.Type.INOUT)) {
				ins.add(p);
			}
		}
		return ins.toArray(new ProgramParameter[0]);
	}
	
	public ProgramParameter[] getOutableParameters() {
		List<ProgramParameter> ins = new ArrayList<ProgramParameter>(parameters.size());
		for (ProgramParameter p: parameters) {
			if (p.getType().equals(ProgramParameter.Type.OUT) && p.getType().equals(ProgramParameter.Type.INOUT)) {
				ins.add(p);
			}
		}
		return ins.toArray(new ProgramParameter[0]);
	}
	
	public void addParameter(ProgramParameter parameter) {
		parameters.add(parameter);
	}
}
