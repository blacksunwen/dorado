package com.bstek.dorado.jdbc.ide;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.core.Configure;
import com.bstek.dorado.core.ConfigureStore;
import com.bstek.dorado.idesupport.initializer.InitializerContext;
import com.bstek.dorado.idesupport.initializer.RuleTemplateInitializer;
import com.bstek.dorado.idesupport.template.RuleTemplate;
import com.bstek.dorado.web.ConsoleUtils;

public class JdbcAgentRuleTemplateInitializer implements
		RuleTemplateInitializer {

	public static final String AGENT_STORE_KEY = "jdbc.ide.jdbcAgent";
	public static final String DEFAULT_AGENT_CLAZZ= DefaultAgent.class.getName();
	
	public void initRuleTemplate(RuleTemplate ruleTemplate,
			InitializerContext initializerContext) throws Exception {
		ConfigureStore configureStore = Configure.getStore();
		String storeKey = AGENT_STORE_KEY;
		String valueStr = configureStore.getString(storeKey);
		if (StringUtils.isEmpty(valueStr)) {
			valueStr = DEFAULT_AGENT_CLAZZ;
			
			ConsoleUtils.outputLoadingInfo("[WARN] " + AGENT_STORE_KEY + " is null, default value '" + DEFAULT_AGENT_CLAZZ + "' be used.");
		}
		ruleTemplate.setType(valueStr);
	}

}
