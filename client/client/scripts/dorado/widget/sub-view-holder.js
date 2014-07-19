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

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @component General
 * @class 用于容纳一个子视图的控件。
 * @extends dorado.widget.Control
 */
dorado.widget.SubViewHolder = $extend(dorado.widget.Control, /** @scope dorado.widget.SubViewHolder.prototype */ {
	$className: "dorado.widget.SubViewHolder",

	ATTRIBUTES: /** @scope dorado.widget.SubViewHolder.prototype */ {

		/**
		 * 子视图的名称。
		 * @attribute readOnly
		 * @type String
		 */
		subViewName: {
			readOnly: true
		},

		/**
		 * 子视图的装载方式。
		 * <p>
		 * 该属性具有下列两中可能的取值，默认情况下系统将按照lazy的方式来进行处理：
		 * <ul>
		 * <li>preload - 随着页面创建就预先从服务器端装载。</li>
		 * <li>lazy - 随着页面需要渲染次控件是装载。</li>
		 * <li>manual - 手工装载。即当用户手工调用load()方法时才装载。</li>
		 * </ul>
		 * </p>
		 * @attribute readOnly
		 * @type String
		 */
		loadMode: {
			readOnly: true
		},

		/**
		 * 上下文值。
		 * <p>
		 * 此对象的用法类似于Map。假设我们给此属性设置如下的值...
		 * <pre class="symbol-example code">
		 * <code class="javascript">
		 * subViewHolder.set("context", { attr1: "value1", attr2: "value2" });
		 * </code>
		 * </pre>
		 * </p>
		 * 那么当SubViewHold所引用的SubView在服务端被创建时，我们就可以从DoradoContext中提取到attr1和attr2的值。
		 * @type Object
		 */
		context: {},

		/**
		 * 子视图。
		 * @attribute writeBeforeReady
		 * @type dorado.widget.View
		 */
		subView: {
			writeBeforeReady: true
		},

		/**
		 * 子视图是否已装载。
		 * @attribute readOnly
		 * @type boolean
		 */
		loaded: {
			readOnly: true
		}
	},

	constructor: function(config) {
		if (config && typeof config == "object") {
			this._subViewName = config.subViewName;
			this._loadMode = config.loadMode;
			this._context = config.context;

			delete config.subViewName;
			delete config.loadMode;
			delete config.context;
		}
		$invokeSuper.call(this, arguments);
	},

	createDom: function(dom) {
		var dom = document.createElement("DIV");
		var subView = this._subView;
		if (subView) {
			this.registerInnerControl(subView);
			subView.render(dom);
		}

		if (this._loadMode == "lazy") {
			this.load();
		}
		return dom;
	},

	doOnResize: function() {
		if (!this._ready) return;
		var subView = this._subView;
		if (subView) {
			subView._realWidth = this._dom.offsetWidth;
			subView._realHeight = this._dom.offsetHeight;
			subView.resetDimension();
		}
	},

	getAjaxEngine: function() {
		var ajax = dorado.widget.SubViewHolder.AJAX_ENGINE;
		if (!ajax) {
			ajax = dorado.util.AjaxEngine.getInstance($setting["ajax.loadViewOptions"]);
		}
		return ajax;
	},

	load: function(callback) {
		if (this._loading) {

		}

		if (!this._loaded) {
			var ajax = this.getAjaxEngine();
			ajax.request({
				jsonData: {
					action: "load-view",
					viewName: this._subViewName,
					context: this._context
				}
			}, {
				callback: function(success, result) {

				}
			});
		}
	}

});
