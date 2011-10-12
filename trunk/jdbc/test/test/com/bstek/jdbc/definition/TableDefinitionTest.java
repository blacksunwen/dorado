package test.com.bstek.jdbc.definition;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import test.com.bstek.jdbc.JdbcTestUtils;

import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.xml.ObjectParser;
import com.bstek.dorado.data.config.ConfigManagerTestSupport;
import com.bstek.dorado.jdbc.config.xml.JdbcParseContext;
import com.bstek.dorado.jdbc.model.DbElement;

public class TableDefinitionTest extends ConfigManagerTestSupport {

	public void test0() throws Exception {
		/* 
		 * <Table name="t1" tableName="PEOPLE">
		 *  <Property name="dynamicToken">xxxxxx</Property>
		 *  <Columns>
		 *  	<Column name="c_id"   columnName="ID"   propertyName="p_id"   selectable="false" jdbcType="INTEGER-Integer"/>
		 *  	<Column name="c_name" columnName="NAME" propertyName="p_name" selectable="true"  jdbcType="VARCHAR-String"/>
		 *  	<Column name="c_sex"  columnName="SEX"  propertyName="p_sex"  selectable="true"  jdbcType="BOOLEAN-Boolean"/>
		 *  </Columns>
		 * </Table>
		 */
		Document document = JdbcTestUtils.newDocument();
		Element table = null;
		Element columns = null;
		Element column = null;
		Element property = null;
		
		table = document.createElement(DbElement.Type.Table.name());
		table.setAttribute("name", "t1");
		table.setAttribute("tableName", "PEOPLE");
		property = document.createElement("Property");
		property.setAttribute("name", "dynamicToken");
		property.setTextContent("xxxxxx");
		table.appendChild(property);
		
		columns = document.createElement("Columns");
		table.appendChild(columns);
		
		column = document.createElement("Column");
		column.setAttribute("name", "c_id");
		column.setAttribute("columnName", "ID");
		column.setAttribute("propertyName", "p_id");
		column.setAttribute("selectable", "false");
//		column.setAttribute("jdbcType", "INTEGER-Integer");
		columns.appendChild(column);
		
		column = document.createElement("Column");
		column.setAttribute("name", "c_name");
		column.setAttribute("columnName", "NAME");
		column.setAttribute("propertyName", "p_name");
		column.setAttribute("selectable", "true");
//		column.setAttribute("jdbcType", "VARCHAR-String");
		columns.appendChild(column);
		
		column = document.createElement("Column");
		column.setAttribute("name", "c_sex");
		column.setAttribute("columnName", "SEX");
		column.setAttribute("propertyName", "p_sex");
		column.setAttribute("selectable", "true");
//		column.setAttribute("jdbcType", "BOOLEAN-Boolean");
		columns.appendChild(column);
		
		ObjectParser parser = JdbcTestUtils.tableParser();
		
		JdbcParseContext parseContext = new JdbcParseContext(null);
		ObjectDefinition def = (ObjectDefinition)parser.parse(table, parseContext);
		System.out.println(def);
		
		CreationContext creationContext = new CreationContext();
		Object object = def.create(creationContext);
		
		System.out.println(object);
	}
	
	public void test1() throws Exception {
		/* 
		 * <Table name="t1" tableName="PEOPLE">
		 *  <Columns>
		 *  	<KeyColumn name="c_id" columnName="ID"   propertyName="p_id"   selectable="false" jdbcType="INTEGER-Integer" keyGenerator="PKG"/>
		 *  	<Column name="c_name"  columnName="NAME" propertyName="p_name" selectable="true"  jdbcType="VARCHAR-String"/>
		 *  	<Column name="c_sex"   columnName="SEX"  propertyName="p_sex"  selectable="true"  jdbcType="BOOLEAN-Boolean"/>
		 *  </Columns>
		 * </Table>
		 */
		Document document = JdbcTestUtils.newDocument();
		Element table = null;
		Element column = null;
		Element columns = null;
		
		table = document.createElement("Table");
		table.setAttribute("name", "t1");
		table.setAttribute("tableName", "PEOPLE");
		
		columns = document.createElement("Columns");
		table.appendChild(columns);
		
		column = document.createElement("KeyColumn");
		column.setAttribute("name", "c_id");
		column.setAttribute("columnName", "ID");
		column.setAttribute("propertyName", "p_id");
		column.setAttribute("selectable", "false"); 
//		column.setAttribute("jdbcType", "INTEGER-Integer");
		column.setAttribute("keyGenerator", "PKG");
		columns.appendChild(column);
		
		column = document.createElement("Column");
		column.setAttribute("name", "c_name");
		column.setAttribute("columnName", "NAME");
		column.setAttribute("propertyName", "p_name");
		column.setAttribute("selectable", "true");
//		column.setAttribute("jdbcType", "VARCHAR-String");
		columns.appendChild(column);
		
		column = document.createElement("Column");
		column.setAttribute("name", "c_sex");
		column.setAttribute("columnName", "SEX");
		column.setAttribute("propertyName", "p_sex");
		column.setAttribute("selectable", "true");
//		column.setAttribute("jdbcType", "BOOLEAN-Boolean");
		columns.appendChild(column);
		
		ObjectParser parser = JdbcTestUtils.tableParser();
		
		JdbcParseContext parseContext = new JdbcParseContext(null);
		ObjectDefinition def = (ObjectDefinition)parser.parse(table, parseContext);
		System.out.println(def);
		
		CreationContext creationContext = new CreationContext();
		Object object = def.create(creationContext);
		
		System.out.println(object);
	}
}
