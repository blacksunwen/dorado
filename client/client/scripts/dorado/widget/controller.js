(function() {

	var EMPTY_CONTROLLER = {};
	
	dorado.widget.View.prototype.controller = EMPTY_CONTROLLER;
	
	dorado.widget.Controller = {
		registerFunctions: function(view, configs) {
		
			function doRegister(view, configs) {
				for (var i = 0; i < configs.length; i++) {
					var config = configs[i], name = config.name, func = config.func, bindingInfos = config.bindingInfos;
					if (bindingInfos) {
						for (var j = 0; j < bindingInfos.length; j++) {
							view.bind(bindingInfos[j], func, {
								autowire: true
							});
						}
					}
					
					if (config.global) {
						if (window[name] !== undefined) {
							throw new dorado.Exception("A gloal function or variable named \"" + name + "\" is already exists.");
						}
						window[name] = func;
					}
					
					if (config.view) {
						if (view[name] !== undefined) {
							throw new dorado.Exception("A method or property named \"" + name + "\" is already exists in View \"" + View._id + "\".");
						}
						view[name] = func;
					}
				}
			}
			
			doRegister(view, configs);
		}
	};
	
})();
