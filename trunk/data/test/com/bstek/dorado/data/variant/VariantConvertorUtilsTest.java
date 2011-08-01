package com.bstek.dorado.data.variant;

import com.bstek.dorado.data.config.ConfigManagerTestSupport;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;
import com.bstek.dorado.data.config.definition.DataTypeDefinitionManager;
import com.bstek.dorado.data.variant.VariantConvertor;
import com.bstek.dorado.data.variant.VariantUtils;

public class VariantConvertorUtilsTest extends ConfigManagerTestSupport {
	public void testConfigureReload() throws Exception {
		DataTypeDefinitionManager definitionManager = getDataTypeDefinitionManager();

		VariantConvertor variantConvertor = VariantUtils.getVariantConvertor();
		assertTrue(variantConvertor.toBoolean("on"));

		reloadConfigs();
		DataTypeDefinition dataTypeDefinition = definitionManager
				.getDefinition(boolean.class);
		dataTypeDefinition.setImpl(ReversePrimitiveBooleanDataType.class
				.getName());
		dataTypeDefinition.setBeanId("DataType:reverseBoolean");

		assertFalse(variantConvertor.toBoolean("on"));

		reloadConfigs();
		assertTrue(variantConvertor.toBoolean("on"));
	}
}
