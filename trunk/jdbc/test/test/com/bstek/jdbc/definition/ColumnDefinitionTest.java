package test.com.bstek.jdbc.definition;

import junit.framework.Assert;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import test.com.bstek.jdbc.JdbcTestUtils;

import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.data.config.ConfigManagerTestSupport;
import com.bstek.dorado.jdbc.config.xml.ColumnParser;
import com.bstek.dorado.jdbc.config.xml.JdbcParseContext;
import com.bstek.dorado.jdbc.model.Column;

public class ColumnDefinitionTest extends ConfigManagerTestSupport {

	public void test_column_0() throws Exception {
		/*
		 * <Column columnName="ID" propertyName="p_id" selectable="false"/>
		 */
		Document document = JdbcTestUtils.newDocument();
		
		Element element = document.createElement("Column");
		element.setAttribute("columnName", "ID");
		element.setAttribute("propertyName", "p_id");
		element.setAttribute("selectable", "false");
		
		ColumnParser parser = JdbcTestUtils.columnParser();
		
		JdbcParseContext parseContext = new JdbcParseContext();
		ObjectDefinition columnDef = (ObjectDefinition)parser.parse(element, parseContext);
		System.out.println(columnDef);
		
		CreationContext creationContext = new CreationContext();
		Column c = (Column)columnDef.create(creationContext);
		
		Assert.assertEquals("ID", c.getColumnName());
		Assert.assertEquals("p_id", c.getPropertyName());
		Assert.assertEquals(false, c.isSelectable());
		Assert.assertNull(c.getJdbcType());
	}
	
}
