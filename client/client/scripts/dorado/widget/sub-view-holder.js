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
		
		className: {
			defaultValue: "d-sub-view"
		},

		/**
		 * 子视图的名称。
		 * 
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
		 * 
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
		 * 
		 * <pre class="symbol-example code">
		 * <code class="javascript">
		 * subViewHolder.set(&quot;context&quot;, {
		 * 	attr1 : &quot;value1&quot;,
		 * 	attr2 : &quot;value2&quot;
		 * });
		 * </code>
		 * </pre>
		 * 
		 * </p>
		 * 那么当SubViewHold所引用的SubView在服务端被创建时，我们就可以从DoradoContext中提取到attr1和attr2的值。
		 * 
		 * @type Object
		 */
		context: {},

		/**
		 * 子视图。
		 * 
		 * @attribute writeBeforeReady
		 * @type dorado.widget.View
		 */
		subView: {
			writeBeforeReady: true
		},

		/**
		 * 子视图是否已装载。
		 * 
		 * @attribute readOnly
		 * @type boolean
		 */
		loaded: {
			readOnly: true
		}
	},
	
	EVENTS: /** @scope dorado.widget.SubViewHolder.prototype */ {
		beforeLoad: {},
		
		onLoad: {},
		
		onLoadFailure: {}
	},

	_constructor: function(config) {
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

	onReady: function() {
		if (this._loadMode == "lazy") {
			this.load();
		}
		else {
			var subView = this._subView;
			if (subView) {
				this.registerInnerControl(subView);
				subView.render(this.getDom());
			}
		}
		$invokeSuper.call(this);
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
		var subViewHolder = this;
		if (subViewHolder._loaded) {
			$callback(callback, true);
			return;
		}		
		if (subViewHolder._loading) {
			throw new ResourceException("dorado.baseWidget.ErrorSubViewLoading");
		}
		
		var eventArg = {
			processDefault: true
		};
		this.fireEvent("beforeLoad", eventArg);
		if (!eventArg.processDefault) return; 
		
		subViewHolder._loaded = true;
		
		if (subViewHolder._subView) {
			subViewHolder._subView.unrender();
			subViewHolder._subView.destroy();
		}
		
		$fly(subViewHolder._dom).empty().xCreate({
			tagName: "DIV",
			className: "loading-indicator",
			content: {
				tagName: "div",
				className: "spinner"
			}
		});
		
		var ajax = subViewHolder.getAjaxEngine();
		subViewHolder._loading = true;
		ajax.request({
			jsonData: {
				action: "load-view",
				viewName: subViewHolder._subViewName,
				context: subViewHolder._context
			}
		}, {
			callback: function(success, result) {
				subViewHolder._loading = false;
				$fly(subViewHolder._dom).empty();
				
				if (success) {
					var jsonResult = result.getJsonData();
					if (jsonResult.packages) {
						$import(jsonResult.packages, function() {
							subViewHolder._subView = jsonResult.createView();
							this.registerInnerControl(subViewHolder._subView);
							subViewHolder._subView.render(subViewHolder._dom);
							subViewHolder.fireEvent("onLoad");
							$callback(callback, true);
						});
					}
					else {
						subViewHolder._subView = jsonResult.createView();
						this.registerInnerControl(subViewHolder._subView);
						subViewHolder._subView.render(subViewHolder._dom);
						subViewHolder.fireEvent("onLoad");
						$callback(callback, true);
					}
				}
				else {
					$fly(subViewHolder._dom).xCreate({
						tagName: "DIV",
						className: "error-message",
						content: dorado.Exception.getExceptionMessage(result.exception)
					});
					
					eventArg = {
						processDefault: true,
						exception: result.exception
					};
					subViewHolder.fireEvent("onLoadFailure", eventArg);
					if (!eventArg.processDefault) {
						dorado.Exception.removeException(result.exception);
					}
					$callback(callback, false, result.exception);
				}
			}
		});
	},
	
	reload: function(callback) {
		subViewHolder._loaded = false;
		subViewHolder._loading = false;
		this.load(callback);
	}

});
