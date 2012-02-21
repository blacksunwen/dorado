package com.bstek.dorado.jdbc.ide.resolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcSpace;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.ModelGeneratorSuit;
import com.bstek.dorado.jdbc.ide.Constants;
import com.bstek.dorado.jdbc.support.JdbcConstants;

public class ListSpaceResolver extends Resolver {

	@Override
	public String getContent(HttpServletRequest request,
			HttpServletResponse response) {
		String envName = request.getParameter(Constants.PARAM_ENV);
		return toContent(envName);
	}

	public String toContent(String envName) {
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		JdbcSpace jdbcSpace = jdbcEnv.getDialect().getTableJdbcSpace();
		switch (jdbcSpace) {
		case SCHEMA:
			return toSchemaContent(envName, null);
		case CATALOG:
			return toCatalogContent(envName);
			default :
				throw new RuntimeException("Unknown jdbcSpac: " + jdbcSpace);
		}
	}
	
	public String toSchemaContent(String envName, String catalog) {
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		List<Map<String,String>> schemaList = JdbcUtils.getModelGeneratorSuit().getJdbcEnviromentMetaDataGenerator().listSchemas(jdbcEnv, catalog);
		
		List<String> schemas = new ArrayList<String>(schemaList.size());
		for (Map<String,String> schemaObj: schemaList) {
			String schema = schemaObj.get(JdbcConstants.TABLE_SCHEM);
			schemas.add(schema);
		}
		return StringUtils.join(schemas, ',');
	}
	
	public String toCatalogContent(String envName) {
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		ModelGeneratorSuit generator = JdbcUtils.getModelGeneratorSuit();
		String[] catalogs = generator.getJdbcEnviromentMetaDataGenerator().listCatalogs(jdbcEnv);
		return StringUtils.join(catalogs, ',');
	}
}
