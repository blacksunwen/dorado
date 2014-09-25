package com.bstek.dorado.vidorsupport.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.common.ClientType;
import com.bstek.dorado.core.Configure;
import com.bstek.dorado.vidorsupport.iapi.IDataTypeWorkshop;
import com.bstek.dorado.vidorsupport.iapi.IRuleSetOutputter;

public class InitSupport {

	public static final String SAVABLE = "dorado.vidor.savable";
	public static final String ENCODING = "dorado.vidor.encoding";
	public static final String DRAWPAD_PATH = "dorado.vidor.drawpad.path";
	public static final String PREVIEW_PATH = "dorado.vidor.preview.path";
	public static final String SAVE_VIEW_SERVICE = "dorado.vidor.saveViewService";
	private String ruleSet;
	private Collection<String> baseDataTypes;
	private IRuleSetOutputter ruleSetOutputter;
	protected IDataTypeWorkshop dataTypeWorkshop;
	protected String parseFileName(String path) throws Exception {
		String[] paths = StringUtils.replace(path, "\\", "/").split("/");
		return paths[paths.length - 1];
	}

	protected String parseClientType(String path) throws Exception {
		String viewName = this.parseFileName(path), clientType;
		if (viewName.indexOf("touch.xml") > 0) {
			clientType = ClientType.TOUCH_NAME;
		} else {
			clientType = ClientType.DESKTOP_NAME;
		}

		return clientType;
	}

	protected String getRuleSet() throws Exception {
		if (ruleSet == null) {
			ruleSet = this.ruleSetOutputter.output();
		}
		return ruleSet;
	}

	protected Collection<String> getBaseDataTypes() throws Exception {
		if (baseDataTypes == null) {
			baseDataTypes = this.dataTypeWorkshop.baseNames();
		}
		return baseDataTypes;
	}

	protected Map<String, Object> getSetting() {
		Map<String, Object> settings = new HashMap<String, Object>();
		settings.put("savable", Configure.getBoolean(SAVABLE));
		settings.put("drawPadPath", Configure.getString(DRAWPAD_PATH));
		settings.put("previewPath", Configure.getString(PREVIEW_PATH));
		settings.put("saveViewService", Configure.getString(SAVE_VIEW_SERVICE));
		return settings;
	}

	protected Map<String, Object> buildViewConfig(String viewName,
			String viewJson, String url) throws Exception {
		String clientType = "desktop";

		if (viewName.indexOf("touch.xml") > 0) {
			clientType = "touch";
		}

		Map<String, Object> viewConfig = new HashMap<String, Object>();
		viewConfig.put("name", viewName);
		viewConfig.put("url", url);
		viewConfig.put("clientType", clientType);
		viewConfig.put("data", viewJson);

		return viewConfig;
	}

	protected Map<String, Object> buildInitData(String viewName,
			String viewJson, String url) throws Exception {
		Map<String, Object> initData = new HashMap<String, Object>();
		initData.put("ruleSet", this.getRuleSet());
		initData.put("baseDataType", this.getBaseDataTypes());
		initData.put("settings", this.getSetting());
		initData.put("viewConfig",
				this.buildViewConfig(viewName, viewJson, url));
		return initData;
	}

	public void setDataTypeWorkshop(IDataTypeWorkshop dataTypeWorkshop) {
		this.dataTypeWorkshop = dataTypeWorkshop;
	}

	public void setRuleSetOutputter(IRuleSetOutputter ruleSetOutputter) {
		this.ruleSetOutputter = ruleSetOutputter;
	}
}
