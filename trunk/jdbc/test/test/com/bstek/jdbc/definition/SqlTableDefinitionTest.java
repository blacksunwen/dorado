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

public class SqlTableDefinitionTest extends ConfigManagerTestSupport {

	public void test0() throws Exception {
		/*
		 * 	<SqlTable name="emp0">
				<Property name="querySql"><![CDATA[select * from EMPLOYEES]]></Property>
				<Columns>
					<Column name="ID"                columnName="ID"                propertyName="id"              jdbcType="INTEGER-Integer"/>
					<Column name="LAST_NAME"         columnName="LAST_NAME"         propertyName="lastName"        jdbcType="VARCHAR-String"/>
					<Column name="FIRST_NAME"        columnName="FIRST_NAME"        propertyName="firstName"       jdbcType="VARCHAR-String"/>
				</Columns>
			</SqlTable>
		 */
		Document document = JdbcTestUtils.newDocument();
		Element table = null;
		Element columns = null;
		Element column = null;
		Element property = null;
		
		table = document.createElement(DbElement.Type.SqlTable.name());
		property = document.createElement("Property");
		property.setTextContent("select * from EMPLOYEES");
		table.appendChild(property);
		columns = document.createElement("Columns");
		table.appendChild(columns);
		
		column = document.createElement("Column");
		column.setAttribute("name", "ID");
		column.setAttribute("columnName", "ID");
		column.setAttribute("propertyName", "id");
//		column.setAttribute("jdbcType", "INTEGER-Integer");
		columns.appendChild(column);
		
		column = document.createElement("Column");
		column.setAttribute("name", "LAST_NAME");
		column.setAttribute("columnName", "LAST_NAME");
		column.setAttribute("propertyName", "lastName");
//		column.setAttribute("jdbcType", "VARCHAR-String");
		columns.appendChild(column);
		
		column = document.createElement("Column");
		column.setAttribute("name", "FIRST_NAME");
		column.setAttribute("columnName", "FIRST_NAME");
		column.setAttribute("propertyName", "firstName");
//		column.setAttribute("jdbcType", "VARCHAR-String");
		columns.appendChild(column);
		
		//-
		ObjectParser parser = JdbcTestUtils.sqlTableParser();
		
		JdbcParseContext parseContext = new JdbcParseContext(null);
		ObjectDefinition def = (ObjectDefinition)parser.parse(table, parseContext);
		System.out.println(def);
		
		CreationContext creationContext = new CreationContext();
		Object object = def.create(creationContext);
		
		System.out.println(object);
	}
}
