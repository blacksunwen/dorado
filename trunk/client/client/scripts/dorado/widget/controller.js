/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
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
						var exp, expSections, fc, count, id, tag, liveIdBindingMap, liveBindings;
						for (var j = 0, len = bindingInfos.length; j < len; j++) {
							exp = bindingInfos[j];
							var fc = exp.charAt(0), count;
							
							if (exp === "view.onCreate" && $setting["widget.bindControllerAfterChildrenCreate"]) {
								var oldIgnore = view._ignoreOnCreateListeners;
								view._ignoreOnCreateListeners = true;
								count = view.bindByExpression(exp, func);
								view._ignoreOnCreateListeners = oldIgnore;
							}
							else {
								count = view.bindByExpression(exp, func);
							}
							
							if (fc === '#') {
								if (count === 0) {
									expSections = exp.split('.');
									id = expSections[0].substring(1);
									
									liveBindingMap = view._liveIdBindingMap;
									if (!liveBindingMap) {
										view._liveIdBindingMap = liveBindingMap = {};
									}
									
									liveBindings = liveBindingMap[id];
									if (!liveBindings) {
										liveBindingMap[id] = liveBindings = [];
									}
									
									if (expSections.length === 2) {
										liveBindings.push({
											id: id,
											event: expSections[1],
											listener: func
										});
									}
									else {
										liveBindings.push({
											id: id,
											subObject: expSections.slice(1, expSections.length - 2).join('.'),
											event: expSections[expSections.length - 1],
											listener: func
										});
									}
								}
							}
							else if (fc === '^') {
								expSections = exp.split('.');
								tag = expSections[0].substring(1);
								
								liveBindingMap = view._liveTagBindingMap;
								if (!liveBindingMap) {
									view._liveTagBindingMap = liveBindingMap = {};
								}
								
								liveBindings = liveBindingMap[tag];
								if (!liveBindings) {
									liveBindingMap[tag] = liveBindings = [];
								}
								
								if (expSections.length === 2) {
									liveBindings.push({
										tag: tag,
										event: expSections[1],
										listener: func
									});
								}
								else {
									liveBindings.push({
										tag: tag,
										subObject: expSections.slice(1, expSections.length - 2).join('.'),
										event: expSections[expSections.length - 1],
										listener: func
									});
								}
							}
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
