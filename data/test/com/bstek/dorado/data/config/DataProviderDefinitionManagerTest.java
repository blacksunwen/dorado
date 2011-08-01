package com.bstek.dorado.data.config;

import java.util.Map;

import com.bstek.dorado.config.definition.DefinitionReference;
import com.bstek.dorado.core.bean.Scope;
import com.bstek.dorado.data.config.definition.DataProviderDefinition;
import com.bstek.dorado.data.config.definition.DataProviderDefinitionManager;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;
import com.bstek.dorado.data.config.xml.XmlDataDefinition;
import com.bstek.dorado.data.provider.DirectDataProvider;

public class DataProviderDefinitionManagerTest extends ConfigManagerTestSupport {

	@SuppressWarnings("unchecked")
	public void testGetDefinitionByName() throws Exception {
		DataProviderDefinitionManager definitionManager = getDataProviderDefinitionManager();

		final String NAME = "testProvider1";
		DataProviderDefinition dataProvider = definitionManager
				.getDefinition(NAME);
		assertNotNull(dataProvider);
		assertEquals(NAME, dataProvider.getName());
		assertEquals(Scope.instant, dataProvider.getScope());
		assertEquals(DirectDataProvider.class.getName(),
				dataProvider.getDefaultImpl());

		Map<String, Object> properties = dataProvider.getProperties();
		DefinitionReference<DataTypeDefinition> ref = (DefinitionReference<DataTypeDefinition>) properties
				.get("resultDataType");
		assertNotNull(ref);
		DataTypeDefinition dataType = ref.getDefinition();
		assertNotNull(dataType);

		ref = (DefinitionReference<DataTypeDefinition>) dataType
				.getProperties().get("elementDataType");
		assertNotNull(ref);
		assertEquals(3, ref.getDefinition().getPropertyDefs().size());

		XmlDataDefinition result = (XmlDataDefinition) properties.get("result");
		assertNotNull(result);
	}
}
