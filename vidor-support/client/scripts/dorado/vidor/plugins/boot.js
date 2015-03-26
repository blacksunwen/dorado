cloudo.onInit(function () {
	var resUrlRoot = cloudo.isBrowser ? ">" : cloudo.getServiceLocation();
	var cloudoPackagesConfig = {
		patterns: {
			"cloudo-plugin-js": {
				url: resUrlRoot + "/dorado/cloudo/plugin/res/${fileName}.js",
				contentType: "text/javascript", charset: "UTF-8", mergeRequests: false
			},
			"cloudo-plugin-css": {
				url: resUrlRoot + "/dorado/cloudo/plugin/res/${fileName}.css",
				contentType: "text/css", charset: "UTF-8", mergeRequests: false
			}
		},
		packages: {}
	}, ns = cloudo.plugins;

	function registerCloudoPatterns() {
		var patterns = cloudoPackagesConfig.patterns;
		var names = Object.keys(patterns);
		names.forEach(function (name) {
			var pattern = patterns[name];
			$packagesConfig.patterns[name] = pattern;
		});
	}

	function registerCloudoPackages() {
		var url = resUrlRoot + "/dorado/cloudo/plugin/manager/packages",
			$ajax = new dorado.util.AjaxEngine({
				defaultOptions: {
					headers: {
						"content-type": "application/x-www-form-urlencoded"
					}
				}
			}), result, data, packageDefs, bootPackages;
		result = $ajax.requestSync({url: url});
		data = result.getJsonData();
		packageDefs = data.def;
		bootPackages = data.boot;
		if (packageDefs) {
			cloudoPackagesConfig.packages = packageDefs;
			var names = Object.keys(cloudoPackagesConfig.packages);
			names.forEach(function (name) {
				var pkg = packageDefs[name];
				$packagesConfig.packages[name] = pkg;
			});
		}
		if (bootPackages) {
			$import(bootPackages, function () {
				console.log("boot plugin packages: " + bootPackages);
			});
		}
	}

	registerCloudoPatterns();
	registerCloudoPackages();

	ns.registerCloudoPackagesConfig = function (clientWindow) {
		var patterns = cloudoPackagesConfig.patterns, packages = cloudoPackagesConfig.packages,
			clientJSON = clientWindow.JSON;
		Object.keys(patterns).forEach(function (name) {
			var pattern = patterns[name];
			pattern = clientJSON.parse(clientJSON.stringify(pattern));
			clientWindow.$packagesConfig.patterns[name] = pattern;
		});

		Object.keys(packages).forEach(function (name) {
			var pkg = packages[name];
			pkg = clientJSON.parse(clientJSON.stringify(pkg));
			clientWindow.$packagesConfig.packages[name] = pkg;
		});
	};
});