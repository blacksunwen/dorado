/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
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
