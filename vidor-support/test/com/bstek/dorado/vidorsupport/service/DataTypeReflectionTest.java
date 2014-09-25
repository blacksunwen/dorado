package com.bstek.dorado.vidorsupport.service;

import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.Assert;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.junit.Test;

import com.bstek.dorado.vidorsupport.AbstractCloudoTestCase;
import com.bstek.dorado.vidorsupport.CloudoTestHelper;
import com.bstek.dorado.vidorsupport.impl.DataTypeWorkshop;

public class DataTypeReflectionTest extends AbstractCloudoTestCase {
	@Test
	public void test_01() throws Exception {
		String resourceName = "DataType1";
		
		String json = CloudoTestHelper.json(resourceName, this.getClass());
		DataTypeWorkshop workshop = this.getServiceBean("&dorado.vidor.dataTypeWorkshop");
		ObjectNode objectNode = workshop.doReflection(json);
		
		ArrayNode nodes = (ArrayNode) objectNode.get("nodes");
		Map<String, ObjectNode> attrMap = new LinkedHashMap<String, ObjectNode>();
		for (int i=0; i<nodes.size(); i++) {
			JsonNode node = nodes.get(i);
			if (node.get("rule").asText().equals("BasePropertyDef")) {
				ObjectNode attrsNode = (ObjectNode)node.get("attrs");
				String name = attrsNode.get("name").asText();
				attrMap.put(name, attrsNode);
			}
		}
		
		Assert.assertEquals(18, attrMap.size());
	}
}
