package com.bstek.dorado.jdbc.ide.resolver;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcSpace;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.ModelGeneratorSuit;
import com.bstek.dorado.jdbc.config.DomHelper;
import com.bstek.dorado.jdbc.config.XmlConstants;
import com.bstek.dorado.jdbc.ide.Constants;
import com.bstek.dorado.jdbc.meta.TableMetaDataGenerator;
import com.bstek.dorado.jdbc.support.JdbcConstants;

/**
 * 数据库表的列表
 * 
 * @author mark.li@bstek.com
 *
 */
public class ListTableResolver extends AbstractResolver {

	@Override
	public String getContent(HttpServletRequest request,
			HttpServletResponse response) {
		String envName = request.getParameter(Constants.PARAM_ENV);
		String namespace = request.getParameter(Constants.PARAM_SPACE);
		String tbTypes = request.getParameter(Constants.PARAM_TBTY);
		String tbName  = request.getParameter(Constants.PARAM_TBNM);
		
		String[] tableTypes = null;
		if (StringUtils.isNotEmpty(tbTypes)) {
			tableTypes = StringUtils.split(tbTypes, ',');
		}
		
		return this.toContent(envName, namespace, tableTypes, tbName);
	}

	public String toContent(String envName, String namespace, String[] tableTypes, String tableNamePattern) {
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		ModelGeneratorSuit generator = JdbcUtils.getModelGeneratorSuit();
		TableMetaDataGenerator tg = generator.getTableMetaDataGenerator();
		Dialect dialect = jdbcEnv.getDialect();
		List<Map<String,String>> tableList = tg.listTableMetas(jdbcEnv, namespace, tableNamePattern, tableTypes);
		
		Document document = DomHelper.newDocument();
		Element tables = DomHelper.addElement(document, "Tables");
		for (Map<String,String> tableObj: tableList) {
			Element element = DomHelper.addElement(tables, "Table");
			String name = tg.tableName(tableObj, jdbcEnv);
			element.setAttribute("name", name);
			element.setAttribute(XmlConstants.TABLE_NAME, tableObj.get(JdbcConstants.TABLE_NAME));
			if (dialect.getTableJdbcSpace() == JdbcSpace.CATALOG) {
				element.setAttribute(XmlConstants.NAME_SPACE, tableObj.get(JdbcConstants.TABLE_CAT));
			} else if (dialect.getTableJdbcSpace() == JdbcSpace.SCHEMA) {
				element.setAttribute(XmlConstants.NAME_SPACE, tableObj.get(JdbcConstants.TABLE_SCHEM));
			}
		}
		
		return DomHelper.toString(document);
	}
}
