package com.bstek.dorado.jdbc.ide;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.core.Configure;
import com.bstek.dorado.core.ConfigureStore;
import com.bstek.dorado.idesupport.initializer.InitializerContext;
import com.bstek.dorado.idesupport.initializer.RuleTemplateInitializer;
import com.bstek.dorado.idesupport.template.PropertyTemplate;
import com.bstek.dorado.idesupport.template.RuleTemplate;

public class TableKeyColumnRuleTemplateInitializer implements
		RuleTemplateInitializer {

	@Override
	public void initRuleTemplate(RuleTemplate columnRule,
			InitializerContext initializerContext) throws Exception {
		ConfigureStore configureStore = Configure.getStore();
		String propertyName = "keyGenerator";
		String storeKey = "jdbc.ide.keyGenerators";
		String valueStr = configureStore.getString(storeKey);
		if (StringUtils.isNotEmpty(valueStr)) {
			String[] typeAry = StringUtils.split(valueStr, ",");
			
			PropertyTemplate jdbcTypeProperty = columnRule.getProperty(propertyName);
			if (jdbcTypeProperty == null) {
				jdbcTypeProperty = new PropertyTemplate(propertyName);
				columnRule.addProperty(jdbcTypeProperty);
			}
			jdbcTypeProperty.setHighlight(1);
			jdbcTypeProperty.setEnumValues(typeAry);
		}
	}

}
