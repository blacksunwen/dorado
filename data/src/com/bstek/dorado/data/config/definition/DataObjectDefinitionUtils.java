package com.bstek.dorado.data.config.definition;

public final class DataObjectDefinitionUtils {
	private DataObjectDefinitionUtils() {
	}

	public static void setDataTypeInner(DataTypeDefinition dataTypeDefinition,
			boolean inner) {
		dataTypeDefinition.setInner(inner);
	}

	@SuppressWarnings("deprecation")
	public static void setDataTypeGlobal(DataTypeDefinition dataTypeDefinition,
			boolean global) {
		dataTypeDefinition.setGlobal(global);
	}

	public static void setDataTypeId(DataTypeDefinition dataTypeDefinition,
			String id) {
		dataTypeDefinition.setId(id);
	}

	public static void setDataProviderId(
			DataProviderDefinition dataProviderDefinition, String id) {
		dataProviderDefinition.setId(id);
	}

	public static void setDataResolverId(
			DataResolverDefinition dataResolverDefinition, String id) {
		dataResolverDefinition.setId(id);
	}
}
