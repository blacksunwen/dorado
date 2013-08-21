(function() {
	
	function apply(target, source) {
		if (!source) return;
		for (var p in source) {
			target[p] = source[p];
		}
	}
	
	if (!window.$setting) window.$setting = {};
	$setting["widget.skinRoot"] = $setting["widget.skinRoot"] || ">dorado/skins/";

	var scripts = document.scripts, contextPath, clientType;
	for (var i = 0; i < scripts.length; i++) {
		var src = scripts[i].src, index = src.indexOf("dorado/script/loader.js");
		if (index > -1) {
			$setting["common.contextPath"] = contextPath = src.substring(0, index + 1);
			
			var index = src.indexOf("#");
			if (index > -1) {
				var hash = src.substring(index + 1);
				window.CLIENT_TYPE = clientType = (hash.toLowerCase() == "touch") ? "touch" : "desktop"; 
			}
			break;
		}
	}
	
	$packagesConfig.contextPath = contextPath;
	
	window.mergeConfig = function(config) {		
		apply($setting, config.setting);
		apply($packagesConfig.patterns, config.patterns);
		apply($packagesConfig.packages, config.packages);
	}
	
	$load(contextPath + "dorado/config.js");
	
	for (var p in $packagesConfig.patterns) {
		var pattern = $packagesConfig.patterns[p];
		if (pattern && pattern.url) {
			pattern.url = pattern.url.replace(/\$\{skinPath\}/g, $setting["widget.skinRoot"] + $setting["widget.skin"]);
		}
	}

})();