package com.bstek.dorado.jdbc.ide;

import com.bstek.dorado.core.Configure;
import com.bstek.dorado.core.ConfigureStore;
import com.bstek.dorado.idesupport.initializer.InitializerContext;
import com.bstek.dorado.idesupport.initializer.RuleTemplateInitializer;
import com.bstek.dorado.idesupport.template.RuleTemplate;

public class JdbcAgentRuleTemplateInitializer implements
		RuleTemplateInitializer {

	public static final String AGENT_STORE_KEY = "jdbc.ide.jdbcAgent";
	public static final String DEFAULT_AGENT_CLAZZ= DefaultAgent.class.getName();
	
	@Override
	public void initRuleTemplate(RuleTemplate ruleTemplate,
			InitializerContext initializerContext) throws Exception {
		ConfigureStore configureStore = Configure.getStore();
		String storeKey = AGENT_STORE_KEY;
		String valueStr = configureStore.getString(storeKey, DEFAULT_AGENT_CLAZZ);
		ruleTemplate.setType(valueStr);
	}

}
