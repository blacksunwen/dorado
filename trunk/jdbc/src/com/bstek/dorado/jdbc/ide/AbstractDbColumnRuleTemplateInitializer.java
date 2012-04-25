package com.bstek.dorado.jdbc.ide;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.core.Configure;
import com.bstek.dorado.core.ConfigureStore;
import com.bstek.dorado.idesupport.initializer.InitializerContext;
import com.bstek.dorado.idesupport.initializer.RuleTemplateInitializer;
import com.bstek.dorado.idesupport.template.PropertyTemplate;
import com.bstek.dorado.idesupport.template.RuleTemplate;
import com.bstek.dorado.jdbc.JdbcTypeManager;
import com.bstek.dorado.jdbc.type.JdbcType;
import com.bstek.dorado.web.ConsoleUtils;

public class AbstractDbColumnRuleTemplateInitializer implements RuleTemplateInitializer {

	public static final String DATA_TYPES_STORE_KEY = "jdbc.ide.jdbcTypes";

	private JdbcTypeManager jdbcTypeManager;
	
	public JdbcTypeManager getJdbcTypeManager() {
		return jdbcTypeManager;
	}

	public void setJdbcTypeManager(JdbcTypeManager jdbcTypeManager) {
		this.jdbcTypeManager = jdbcTypeManager;
	}

	public void initRuleTemplate(RuleTemplate columnRule,
			InitializerContext initializerContext) throws Exception {
		{
			ConfigureStore configureStore = Configure.getStore();
			String propertyName = "jdbcType";
			String storeKey = DATA_TYPES_STORE_KEY;
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
				
				ConsoleUtils.outputLoadingInfo("[WARN] " + DATA_TYPES_STORE_KEY + " is null, default value '" + defaultValue + "' be used.");
			}
			
			PropertyTemplate jdbcTypeProperty = columnRule.getProperty(propertyName);
			if (jdbcTypeProperty == null) {
				jdbcTypeProperty = new PropertyTemplate(propertyName);
				columnRule.addProperty(jdbcTypeProperty);
			}
			jdbcTypeProperty.setHighlight(1);
			jdbcTypeProperty.setEnumValues(typeAry);
		}
	}
	
	protected String[] getDefaultValue() {
		JdbcType[] types = getJdbcTypeManager().list();
		String[] typeNames = new String[types.length];
		for (int i=0; i<types.length; i++) {
			typeNames[i] = types[i].getName();
		}
		
		return typeNames;
	}
}
