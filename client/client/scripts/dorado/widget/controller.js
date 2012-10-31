﻿/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
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
