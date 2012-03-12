package com.bstek.dorado.jdbc.support;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcTypeManager;
import com.bstek.dorado.jdbc.config.DomHelper;
import com.bstek.dorado.jdbc.meta.StoredProcedureGenerator;
import com.bstek.dorado.jdbc.model.storedprogram.ProgramParameter;
import com.bstek.dorado.jdbc.model.storedprogram.StoredProgram;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class DefaultStoredProcedureGenerator implements
		StoredProcedureGenerator {

	private JdbcTypeManager jdbcTypeManager;
	
	public JdbcTypeManager getJdbcTypeManager() {
		return jdbcTypeManager;
	}

	public void setJdbcTypeManager(JdbcTypeManager jdbcTypeManager) {
		this.jdbcTypeManager = jdbcTypeManager;
	}

	@Override
	public Document create(JdbcEnviroment jdbcEnv, String catalog,
			String schema, String procedureName) {
		DataSource dataSource = jdbcEnv.getDataSource();
		try {
			Generator generator = new Generator(jdbcEnv, catalog, schema, procedureName);
			return (Document)org.springframework.jdbc.support.JdbcUtils.extractDatabaseMetaData(dataSource, generator);
		} catch (MetaDataAccessException e) {
			throw new RuntimeException(e);
		} 
		
	}

	@Override
	public Document merge(JdbcEnviroment jdbcEnv, String catalog,
			String schema, String spName, Document oldDocument) {

		return null;
	}

	protected static class Generator implements DatabaseMetaDataCallback {
		private JdbcEnviroment jdbcEnv;
		private String catalog;
		private String schema;
		private String procedureName;
		
		private String usedCatalog;
		private String usedSchema;
		private String usedProcedureName;
		
		public Generator (JdbcEnviroment jdbcEnv, String catalog, String schema, String procedureName) {
			this.jdbcEnv = jdbcEnv;
			this.catalog = catalog;
			this.schema = schema;
			this.procedureName = procedureName;
			
			this.usedCatalog = catalog;
			this.usedSchema = schema;
			this.usedProcedureName = procedureName;
		}
		
		@Override
		public Object processMetaData(DatabaseMetaData dbmd)
				throws SQLException, MetaDataAccessException {
			this.init(dbmd);
			this.check(dbmd);
			
			Document document = DomHelper.newDocument();
			
			Element rootElement = this.rootElement(document);
			document.appendChild(rootElement);
			
			ResultSet columnRS = dbmd.getProcedureColumns(usedCatalog, usedSchema, usedProcedureName, null);
			List<Map<String,String>> columnMetaList = Utils.toListMap(columnRS);
			for (Map<String,String> columnMeta: columnMetaList) {
				Map<String,String> columnProperties = columnProperties(columnMeta);
				if (columnProperties != null) {
					Element parameterElement = createParameterElement(columnProperties, document);
					rootElement.appendChild(parameterElement);
				}
			}
			
			return document;
		}
		
		protected void init(DatabaseMetaData dbmd) throws SQLException {
			if (dbmd.supportsCatalogsInProcedureCalls()){
				if (StringUtils.isEmpty(usedCatalog)) {
					usedCatalog = this.jdbcEnv.getDialect().defaultCatalog(jdbcEnv.getDataSource(), dbmd);
				}
				
				if (!dbmd.supportsMixedCaseIdentifiers()) {
					if (dbmd.storesLowerCaseIdentifiers()) {
						if (StringUtils.isNotEmpty(usedCatalog)){
							usedCatalog = usedCatalog.toLowerCase();
						} 
					}
					if (dbmd.storesUpperCaseIdentifiers()) {
						if (StringUtils.isNotEmpty(usedCatalog)){
							usedCatalog = usedCatalog.toUpperCase();
						}
					}
				}
			}
			
			if (dbmd.supportsSchemasInProcedureCalls()){
				if (StringUtils.isEmpty(usedSchema)) {
					usedSchema = this.jdbcEnv.getDialect().defaultSchema(jdbcEnv.getDataSource(), dbmd);
				}
				
				if (!dbmd.supportsMixedCaseIdentifiers()) {
					if (dbmd.storesLowerCaseIdentifiers()) {
						if (StringUtils.isNotEmpty(usedSchema)){
							usedSchema = usedSchema.toLowerCase();
						} 
					}
					if (dbmd.storesUpperCaseIdentifiers()) {
						if (StringUtils.isNotEmpty(usedSchema)){
							usedSchema = usedSchema.toUpperCase();
						}
					}
				}
			}
			
			if (!dbmd.supportsMixedCaseIdentifiers()) {
				if (dbmd.storesLowerCaseIdentifiers()) {
					if (StringUtils.isNotEmpty(usedProcedureName)){
						usedProcedureName = usedProcedureName.toLowerCase();
					} 
				}
				if (dbmd.storesUpperCaseIdentifiers()) {
					if (StringUtils.isNotEmpty(usedProcedureName)){
						usedProcedureName = usedProcedureName.toUpperCase();
					}
				}
			}
		}
	
		protected Map<String,String> columnProperties(Map<String,String> columnMeta) {
			String columnName = columnMeta.get("COLUMN_NAME");
			if (StringUtils.isNotEmpty(columnName)) {
				Map<String,String> properties = new HashMap<String,String>();
				properties.put("name", columnName);
				
				String columnType = columnMeta.get("COLUMN_TYPE");
				if (String.valueOf(DatabaseMetaData.procedureColumnIn).equals(columnType)) {
					properties.put("type", ProgramParameter.Type.IN.name());
				} else if (String.valueOf(DatabaseMetaData.procedureColumnInOut).equals(columnType)) {
					properties.put("type", ProgramParameter.Type.INOUT.name());
				} else if (String.valueOf(DatabaseMetaData.procedureColumnOut).equals(columnType)) {
					properties.put("type", ProgramParameter.Type.OUT.name());
				}
				
				//TODO 重新实现
//				JdbcType jdbcType = jdbcEnv.getDialect().jdbcType(columnMeta);
//				if (jdbcType != null) {
//					properties.put("jdbcType", jdbcType.getName());
//				}
				return properties;
			} else {
				return null;
			}
		}
		
		protected Element createParameterElement(Map<String,String> columnProperties, Document document) {
			Element element = document.createElement("Parameter");
			for (Iterator<String> keyItr = columnProperties.keySet().iterator(); keyItr.hasNext();){
				String key = keyItr.next();
				String value = columnProperties.get(key);
				if (StringUtils.isNotEmpty(value)) {
					element.setAttribute(key, value);
				}
			}
			return element;
		}
		
		protected Element rootElement(Document document) {
			Element rootElement = document.createElement(StoredProgram.TYPE);
			if (StringUtils.isNotEmpty(schema)) {
				rootElement.setAttribute("schema", schema);
			}
			if (StringUtils.isNotEmpty(catalog)) {
				rootElement.setAttribute("catalog", catalog);
			}
			rootElement.setAttribute("programName", usedProcedureName);
			rootElement.setAttribute("name", procedureName);
			
			return rootElement;
		}
		
		protected void check(DatabaseMetaData dbmd) throws SQLException {
			int i = 0;
			ResultSet procedureRS = dbmd.getProcedures(usedCatalog, usedSchema,  usedProcedureName);
			try {
				while (procedureRS.next()) {
					i++;
				}
			} finally {
				procedureRS.close();
			}
			
			if (i == 0) {
				throw new IllegalArgumentException("no procedure named [" + usedProcedureName + "] can be found.");
			}
			
			if (i > 1) {
				throw new IllegalArgumentException("more than one procedure named [" + usedProcedureName + "] can be found, it is [" + i +"].");
			}
		}
	}
}
