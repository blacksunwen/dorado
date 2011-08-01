package com.bstek.dorado.core.io;

import java.io.IOException;


import com.bstek.dorado.core.ContextTestCase;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.io.ResourceUtils;

public class ResourceUtilsTest extends ContextTestCase {

	public void testGetResources() throws IOException {
		String location1 = "com/bstek/dorado/core/config/core-context.xml";
		String location2 = "com/bstek/dorado/data/config/xml/invalid-resource.xml";

		String[] resourceLocations = new String[] { location1, location2,
				location1 };
		Resource[] resources = ResourceUtils.getResources(resourceLocations);

		assertEquals(resources.length, 2);
		for (int i = 0; i < resources.length; i++) {
			Resource resource = resources[i];
			assertTrue(resource.getFilename().endsWith(".xml"));
		}
	}

}
