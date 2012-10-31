/*
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
	var oldAjaxResult = dorado.util.AjaxResult;

	var ajaxResultPool = [];

	dorado.util.AjaxResult = $extend(oldAjaxResult, {
		constructor: function(options) {
			$invokeSuper.call(this, arguments);

			ajaxResultPool.push(this);
			if (options.url) {
				this.name = options.url.substr(options.url.lastIndexOf("/") + 1);
			}
			if (ajaxResultPoolChange) {
				ajaxResultPoolChange.apply(null, []);
			}
		}
	});

	/**
	 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
	 * @class AjaxGrid
	 * @extends dorado.widget.Grid
	 */
	dorado.debug.AjaxGrid = $extend(dorado.widget.Grid, {
		$className: "dorado.debug.AjaxGrid",
		constructor: function(config) {
			config = config || {};

			var grid = this;
			config = {
				columns: [{
					name: "Name",
					property: "name",
					width: 200
				}, {
					name: "Url",
					property: "url",
					width: 300
				}, {
					name: "Method",
					property: "method"
				}, {
					name: "Status",
					property: "status"
				}, {
					name: "StatusText",
					property: "statusText"
				}, {
					name: "Text",
					property: "text"
				}, {
					name: "Exception",
					property: "exception"
				}]
			};

			$invokeSuper.call(this, [config]);
		},
		reload: function() {
			this.set("items", ajaxResultPool);
		}
	});

	var ajaxGrid = new dorado.debug.AjaxGrid();

	function ajaxResultPoolChange() {
		ajaxGrid.reload();
	}

	dorado.Debugger.registerModule({
		$type: "Control",
		name: "ajax",
		caption: "Ajax",
		control: ajaxGrid
	});
})();
