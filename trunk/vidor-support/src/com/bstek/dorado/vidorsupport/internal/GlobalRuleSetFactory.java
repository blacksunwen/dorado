package com.bstek.dorado.vidorsupport.internal;

import com.bstek.dorado.vidorsupport.plugin.iapi.ICloudoListener;
import com.bstek.dorado.vidorsupport.plugin.iapi.PluginMeta;
import com.bstek.dorado.vidorsupport.plugin.internal.PluginManager;
import com.bstek.dorado.vidorsupport.rule.RuleSet;

public class GlobalRuleSetFactory extends AbstractRuleSetFactory {
	
	private RuleSet singleRS;

	@Override
	protected RuleSet doGet() {
		return singleRS;
	}

	@Override
	protected void doCapture(RuleSet rs) {
		PluginManager pluginManager = PluginManager.getInstance();
		if (pluginManager != null) {
			PluginMeta[] metas = pluginManager.getPlugins();
			for (PluginMeta meta: metas) {
				ICloudoListener l = meta.getCloudoListener();
				if (l != null) {
					l.onInitRuleSet(rs);
				}
			}
			
			this.singleRS = rs;
		}
		
	}

}
