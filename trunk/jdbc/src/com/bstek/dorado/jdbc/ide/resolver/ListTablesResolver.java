package com.bstek.dorado.jdbc.ide.resolver;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.bstek.dorado.jdbc.JdbcConstants;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.ModelGeneratorSuit;
import com.bstek.dorado.jdbc.config.xml.DomHelper;
import com.bstek.dorado.jdbc.ide.Constants;
import com.bstek.dorado.jdbc.meta.TableMetaDataGenerator;

public class ListTablesResolver extends Resolver {

	@Override
	public String getContent(HttpServletRequest request,
			HttpServletResponse response) {
		String envName = request.getParameter(Constants.PARAM_ENV);
		String catalog = request.getParameter(Constants.PARAM_CATA);
		String schema  = request.getParameter(Constants.PARAM_SCHE);
		String tbTypes = request.getParameter(Constants.PARAM_TBTY);
		String tbName  = request.getParameter(Constants.PARAM_TBNM);
		
		String[] tableTypes = null;
		if (StringUtils.isNotEmpty(tbTypes)) {
			tableTypes = StringUtils.split(tbTypes, ',');
		}
		
		return this.toContent(envName, catalog, schema, tableTypes, tbName);
	}

	public String toContent(String envName, String catalog, String schema, String[] tableTypes, String tableNamePattern) {
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		ModelGeneratorSuit generator = JdbcUtils.getModelGeneratorSuit();
		TableMetaDataGenerator tg = generator.getTableMetaDataGenerator();
		List<Map<String,String>> tableList = tg.listTableMetas(jdbcEnv, catalog, schema, tableNamePattern, tableTypes);
		
		Document document = DomHelper.newDocument();
		Element tables = DomHelper.addElement(document, "Tables");
		for (Map<String,String> tableObj: tableList) {
			Element element = DomHelper.addElement(tables, "Table");
			String name = tg.tableName(tableObj, jdbcEnv);
			element.setAttribute("name", name);
			element.setAttribute("catalog", tableObj.get(JdbcConstants.TABLE_CAT));
			element.setAttribute("schema", tableObj.get(JdbcConstants.TABLE_SCHEM));
			element.setAttribute("tableName", tableObj.get(JdbcConstants.TABLE_NAME));
		}
		
		return DomHelper.toString(document);
	}
}
