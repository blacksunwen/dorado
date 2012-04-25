package com.bstek.dorado.jdbc.ide;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.core.Configure;
import com.bstek.dorado.core.ConfigureStore;
import com.bstek.dorado.idesupport.initializer.InitializerContext;
import com.bstek.dorado.idesupport.initializer.RuleTemplateInitializer;
import com.bstek.dorado.idesupport.template.PropertyTemplate;
import com.bstek.dorado.idesupport.template.RuleTemplate;
import com.bstek.dorado.jdbc.KeyGeneratorManager;
import com.bstek.dorado.jdbc.model.table.KeyGenerator;
import com.bstek.dorado.web.ConsoleUtils;

public class TableKeyColumnRuleTemplateInitializer implements
		RuleTemplateInitializer {

	public static final String KEY_GENERATORS_STORE_KEY = "jdbc.ide.keyGenerators";
	
	private KeyGeneratorManager keyGeneratorManager;
	
	public KeyGeneratorManager getKeyGeneratorManager() {
		return keyGeneratorManager;
	}

	public void setKeyGeneratorManager(KeyGeneratorManager keyGeneratorManager) {
		this.keyGeneratorManager = keyGeneratorManager;
	}

	public void initRuleTemplate(RuleTemplate columnRule,
			InitializerContext initializerContext) throws Exception {
		ConfigureStore configureStore = Configure.getStore();
		String propertyName = "keyGenerator";
		String storeKey = KEY_GENERATORS_STORE_KEY;
		String valueStr = configureStore.getString(storeKey);
		
		String[] typeAry = null;
		if (StringUtils.isNotEmpty(valueStr)) {
			typeAry = StringUtils.split(valueStr, ",");
			if (typeAry[0].equals("SYSTEM")) {
				String[] defaultValue = getDefaultValue();
				String[] newValue = new String[typeAry.length + defaultValue.length-1];
				System.arraycopy(typeAry, 1, newValue, 0, typeAry.length-1);
				System.arraycopy(defaultValue, 0, newValue, typeAry.length-1, defaultValue.length);
				typeAry = newValue;
			}
		} else {
			typeAry = getDefaultValue();
			String defaultValue = StringUtils.join(typeAry, ',');
			
			ConsoleUtils.outputLoadingInfo("[WARN] " + KEY_GENERATORS_STORE_KEY + " is null, default value '" + defaultValue + "' be used.");
		}
		
		PropertyTemplate jdbcTypeProperty = columnRule.getProperty(propertyName);
		if (jdbcTypeProperty == null) {
			jdbcTypeProperty = new PropertyTemplate(propertyName);
			columnRule.addProperty(jdbcTypeProperty);
		}
		jdbcTypeProperty.setHighlight(1);
		jdbcTypeProperty.setEnumValues(typeAry);
	}

	protected String[] getDefaultValue() {
		KeyGenerator<Object>[] gens = getKeyGeneratorManager().list();
		String[] genNames = new String[gens.length];
		for (int i=0; i<gens.length; i++) {
			genNames[i] = gens[i].getName();
		}
		
		return genNames;
	}
}
