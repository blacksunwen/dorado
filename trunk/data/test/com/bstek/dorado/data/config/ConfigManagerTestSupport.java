package com.bstek.dorado.data.config;

import java.io.IOException;
import java.util.Set;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.data.DataContextTestCase;
import com.bstek.dorado.data.config.DataConfigManager;
import com.bstek.dorado.data.config.ReloadableDataConfigManagerSupport;
import com.bstek.dorado.data.config.definition.DataProviderDefinitionManager;
import com.bstek.dorado.data.config.definition.DataTypeDefinitionManager;
import com.bstek.dorado.data.provider.manager.DataProviderManager;
import com.bstek.dorado.data.resolver.manager.DataResolverManager;
import com.bstek.dorado.data.type.manager.DataTypeManager;

public abstract class ConfigManagerTestSupport extends DataContextTestCase {
	protected static DataConfigManager configManager = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		if (configManager == null) {
			Context conetxt = Context.getCurrent();
			configManager = (DataConfigManager) conetxt
					.getServiceBean("dataConfigManager");
			reloadConfigs();
		}
	}

	@Override
	protected void tearDown() throws Exception {
		configManager = null;
		super.tearDown();
	}

	protected void reloadConfigs() throws IOException, Exception {
		if (configManager instanceof ReloadableDataConfigManagerSupport) {
			ReloadableDataConfigManagerSupport rcms = (ReloadableDataConfigManagerSupport) configManager;
			Set<Resource> resourceSet = rcms.getResources();
			Resource[] resources = new Resource[resourceSet.size()];
			resourceSet.toArray(resources);
			configManager.loadConfigs(resources);
		}
	}

	protected DataProviderDefinitionManager getDataProviderDefinitionManager()
			throws Exception {
		return getDataProviderManager().getDataProviderDefinitionManager();
	}

	protected DataProviderManager getDataProviderManager() throws Exception {
		Context conetxt = Context.getCurrent();
		DataProviderManager dataProviderManager = (DataProviderManager) conetxt
				.getServiceBean("dataProviderManager");
		return dataProviderManager;
	}

	protected DataResolverManager getDataResolverManager() throws Exception {
		Context conetxt = Context.getCurrent();
		DataResolverManager dataResolverManager = (DataResolverManager) conetxt
				.getServiceBean("dataResolverManager");
		return dataResolverManager;
	}

	protected DataTypeDefinitionManager getDataTypeDefinitionManager()
			throws Exception {
		return getDataTypeManager().getDataTypeDefinitionManager();
	}

	protected DataTypeManager getDataTypeManager() throws Exception {
		Context conetxt = Context.getCurrent();
		DataTypeManager dataTypeManager = (DataTypeManager) conetxt
				.getServiceBean("dataTypeManager");
		return dataTypeManager;
	}

}
