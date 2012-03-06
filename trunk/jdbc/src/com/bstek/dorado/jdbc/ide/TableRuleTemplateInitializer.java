package com.bstek.dorado.jdbc.ide;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.core.Configure;
import com.bstek.dorado.core.ConfigureStore;
import com.bstek.dorado.idesupport.initializer.InitializerContext;
import com.bstek.dorado.idesupport.initializer.RuleTemplateInitializer;
import com.bstek.dorado.idesupport.template.PropertyTemplate;
import com.bstek.dorado.idesupport.template.RuleTemplate;

public class TableRuleTemplateInitializer implements RuleTemplateInitializer {

	@Override
	public void initRuleTemplate(RuleTemplate tableRule,
			InitializerContext initializerContext) throws Exception {
		ConfigureStore configureStore = Configure.getStore();
		String propertyName = "namespace";
		String storeKey = "jdbc.ide.namespaces";
		String valueStr = configureStore.getString(storeKey);
		if (StringUtils.isNotEmpty(valueStr)) {
			String[] typeAry = StringUtils.split(valueStr, ",");
			
			PropertyTemplate property = tableRule.getPrimitiveProperty(propertyName);
			if (property == null) {
				property = new PropertyTemplate(propertyName);
				tableRule.addPrimitiveProperty(property);
			}
			property.setHighlight(1);
			property.setEnumValues(typeAry);
		}
	}

}
