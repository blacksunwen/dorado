package com.bstek.dorado.data.config.definition;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bstek.dorado.config.definition.DefinitionReference;
import com.bstek.dorado.core.Context;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-2-3
 */
public class DataProviderDefinitionReference implements
		DefinitionReference<DataProviderDefinition> {
	private static Log logger = LogFactory
			.getLog(DataProviderDefinitionReference.class);

	private static DataProviderDefinitionManager dataProviderDefinitionManager;

	private String name;

	/**
	 * @param definitionManager
	 *            配置声明管理器
	 * @param name
	 *            指向的声明对象在注册时使用的名称
	 */
	public DataProviderDefinitionReference(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private DataProviderDefinitionManager getDataProviderDefinitionManager() {
		if (dataProviderDefinitionManager == null) {
			try {
				Context current = Context.getCurrent();
				dataProviderDefinitionManager = (DataProviderDefinitionManager) current
						.getServiceBean("dataProviderDefinitionManager");
			} catch (Exception e) {
				logger.error(e, e);
			}
		}
		return dataProviderDefinitionManager;
	}

	public DataProviderDefinition getDefinition() {
		Context current = Context.getCurrent();
		DataProviderDefinitionManager dtfm = (DataProviderDefinitionManager) current
				.getAttribute("privateDataProviderDefinitionManager");
		if (dtfm == null) {
			dtfm = getDataProviderDefinitionManager();
		}

		if (dtfm != null) {
			return dtfm.getDefinition(name);
		} else {
			return null;
		}
	}
}
