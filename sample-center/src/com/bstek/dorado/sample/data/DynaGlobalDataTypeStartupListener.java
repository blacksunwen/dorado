package com.bstek.dorado.sample.data;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.bstek.dorado.core.EngineStartupListener;
import com.bstek.dorado.core.el.ExpressionHandler;
import com.bstek.dorado.data.config.definition.DataProviderDefinitionReference;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;
import com.bstek.dorado.data.config.definition.DataTypeDefinitionManager;
import com.bstek.dorado.data.config.definition.DataTypeDefinitionReference;
import com.bstek.dorado.data.config.definition.PropertyDefDefinition;
import com.bstek.dorado.data.type.DefaultEntityDataType;
import com.bstek.dorado.data.type.property.Reference;
import com.bstek.dorado.sample.entity.Category;

@Component
public class DynaGlobalDataTypeStartupListener extends EngineStartupListener {

	@Resource
	private DataTypeDefinitionManager dataTypeDefinitionManager;

	@Resource
	private ExpressionHandler expressionHandler;

	@Override
	public void onStartup() throws Exception {
		DataTypeDefinition dataType;
		PropertyDefDefinition propertyDef;

		// AutoCategoryType
		dataType = new DataTypeDefinition("AutoCategoryType");
		dataType.setImplType(DefaultEntityDataType.class);
		dataType.setMatchType(Category.class);
		dataType.setParentReference(new DataTypeDefinitionReference(
				"CommonEntity"));

		// categoryName
		propertyDef = new PropertyDefDefinition("categoryName");
		propertyDef.setProperty("label", "类别名称");
		dataType.addPropertyDef(propertyDef);

		// products
		propertyDef = new PropertyDefDefinition("products");
		propertyDef.setImplType(Reference.class);
		propertyDef.setProperty("dataType", new DataTypeDefinitionReference(
				"[Product]"));
		propertyDef.setProperty("dataProvider",
				new DataProviderDefinitionReference(
						"productInterceptor#getProductsByCategoryId"));
		propertyDef.setProperty("parameter",
				expressionHandler.compile("$${this.id}"));
		propertyDef.setProperty("pageSize", 20);
		dataType.addPropertyDef(propertyDef);

		dataTypeDefinitionManager.registerDefinition(dataType);
	}

}
