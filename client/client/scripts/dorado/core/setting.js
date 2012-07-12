(function() {

	/**
	 * @author Benny Bao (mailto:benny.bao@bstek.com)
	 * @name dorado.Setting
	 * @class dorado的配置对象，用于维护一组dorado运行时所需的参数。
	 * @static
	 * @see $setting
	 *
	 * @example
	 * var debugEnabled = dorado.Setting["debugEnabled"]; // 取得一个参数值
	 */
	// =====
	
	var doradoServierURI = ">dorado/view-service";
	
	/**
	 * @author Benny Bao (mailto:benny.bao@bstek.com)
	 * @name $setting
	 * @property
	 * @description dorado.Setting的快捷方式。
	 * @type dorado.Setting
	 * @see dorado.Setting
	 *
	 * @example
	 * var debugEnabled = $setting["debugEnabled"]; // 相当于dorado.Setting["debugEnabled"]
	 */
	dorado.Setting = {
		"common.defaultDateFormat": "Y-m-d",
		"common.defaultTimeFormat": "H:i:s",
		"common.defaultDateTimeFormat": "Y-m-d H:i:s",

		"common.defaultDisplayDateFormat": "Y-m-d",
		"common.defaultDisplayTimeFormat": "H:i:s",
		"common.defaultDisplayDateTimeFormat": "Y-m-d H:i:s",
		
		"ajax.defaultOptions": null,
		"ajax.dataTypeRepositoryOptions": {
			url: doradoServierURI,
			method: "POST",
			autoBatchEnabled: true
		},
		"ajax.dataProviderOptions": {
			url: doradoServierURI,
			method: "POST",
			autoBatchEnabled: true
		},
		"ajax.dataResolverOptions": {
			url: doradoServierURI,
			method: "POST",
			autoBatchEnabled: false
		},
		"ajax.remoteServiceOptions": {
			url: doradoServierURI,
			method: "POST",
			autoBatchEnabled: false
		},
		"dom.useCssShadow": true,
		"widget.skin": "~current",
		"widget.panel.useCssCurveBorder": true
	};
	
	if (window.$setting instanceof Object) {
		dorado.Object.apply(dorado.Setting, $setting);
	}
	
	var contextPath = dorado.Setting["common.contextPath"];
	if (contextPath) {
		if (contextPath.charAt(contextPath.length - 1) != '/') contextPath += '/';
	}
	else {
		contextPath = '/';
	}
	dorado.Setting["common.contextPath"] = contextPath;
	
	window.$setting = dorado.Setting;
})();
