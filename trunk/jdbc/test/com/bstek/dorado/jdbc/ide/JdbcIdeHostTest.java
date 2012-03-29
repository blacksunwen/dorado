package com.bstek.dorado.jdbc.ide;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class JdbcIdeHostTest {

	/**
	 *                 <property name="user" value="dorado"/>
                <property name="password" value="dorado"/>
                <property name="URL" value="jdbc:mysql://192.168.18.95/DORADO?useUnicode=true&amp;characterEncoding=UTF-8"/>
	 * @throws Exception
	 */
	@Test
	public void testCreateColumns() throws Exception {
		String[] args = new String[] {
				"-jdbcAgent", "com.bstek.dorado.jdbc.ide.DefaultAgent",
				"-service", "createColumns",
				"-file", "C:\\Users\\TD\\tmp.d7\\abc.de",
				"-tableType", "Table",
				"-tableName", "DEPT",
				"-namespace", "DORADO",
				"-xml", "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <Table name=\"DEPT\" tableName=\"DEPT\"></Table>",
				
//				"-driver", "oracle.jdbc.OracleDriver",
//				"-password", "dorado",
//				"-user", "dorado",
//				"-url", "jdbc:oracle:thin:@192.168.18.90:1521/DORADO"
				
				"-password", "dorado",
				"-user", "dorado",
				"-url", "jdbc:mysql://192.168.18.95/DORADO?useUnicode=true&amp;characterEncoding=UTF-8"
			};
		JdbcIdeHost.main(args);
	}
	
	@Test
	public void testQuote() throws Exception  {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <Table name=\"DEPT\" tableName=\"DEPT\"></Table>";
		
		String quoteXml = quote(xml);
		
		System.out.println(quoteXml);
	}
	
	public static String quote(String xml) {
		List<Character> chars = new ArrayList<Character>();
		{
			char[] charAry = xml.toCharArray();
			for (char c: charAry) {
				if (c == '"') {
					chars.add('\\');
				}
				chars.add(c);
			}
		}
		
		
		char[] charAry = new char[chars.size() + 2];
		charAry[0] = '"';
		charAry[charAry.length-1] = '"';
		for (int i=0; i<chars.size(); i++) {
			charAry[i+1] = chars.get(i).charValue();
		}
		
		return String.valueOf(charAry);
	}
}
