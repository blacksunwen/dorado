package com.bstek.dorado.jdbc.ide;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import com.bstek.dorado.jdbc.JdbcConstants;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.ModelGeneratorSuit;
import com.bstek.dorado.jdbc.meta.TableMetaDataGenerator;

public class ListTablesResolver extends Resolver {

	@Override
	public String getContent(HttpServletRequest request,
			HttpServletResponse response) {
		String envName = request.getParameter(PARAM_ENV);
		String catalog = request.getParameter(PARAM_CATA);
		String schema  = request.getParameter(PARAM_SCHE);
		String tbTypes = request.getParameter(PARAM_TBTY);
		String tbName  = request.getParameter(PARAM_TBNM);
		
		String[] tableTypes = null;
		if (StringUtils.isNotEmpty(tbTypes)) {
			tableTypes = StringUtils.split(tbTypes, ',');
		}
		
		return this.toContent(envName, catalog, schema, tableTypes, tbName);
	}

	public String toContent(String envName, String catalog, String schema, String[] tableTypes, String tableNamePattern) {
		final JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(envName);
		final ModelGeneratorSuit generator = jdbcEnv.getModelGeneratorSuit();
		final TableMetaDataGenerator tg = generator.getTableMetaDataGenerator();
		final List<Map<String,String>> tableList = tg.listTableMetas(jdbcEnv, catalog, schema, tableNamePattern, tableTypes);
		
		return toXml("Tables", new XML() {

			@Override
			public void build(XMLWriter xmlWriter, Element rootElement)
					throws Exception {
				for (Map<String,String> table: tableList) {
					Element element = DocumentHelper.createElement("Table");
					String name = tg.tableName(table, jdbcEnv);
					element.addAttribute("name", name);
					element.addAttribute("catalog", table.get(JdbcConstants.TABLE_CAT));
					element.addAttribute("schema", table.get(JdbcConstants.TABLE_SCHEM));
					element.addAttribute("tableName", table.get(JdbcConstants.TABLE_NAME));
					
					xmlWriter.write(element);
				}
			}
		
		});
	}
}
