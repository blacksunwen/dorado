/**
 * @namespace dorado的根命名空间。
 */
var dorado = {
	id: '_' + parseInt(Math.random() * Math.pow(10, 8)),
	
	_ID_SEED: 0,
	_TIMESTAMP_SEED: 0,
	
	_GET_ID: function(obj) {
		return obj._id;
	},
	
	_GET_NAME: function(obj) {
		return obj._name;
	},
	
	_NULL_FUNCTION: function() {
	},
	
	_UNSUPPORTED_FUNCTION: function() {
		return function() {
			throw new dorado.ResourceException("dorado.core.OperationNotSupported", dorado.getFunctionName(arguments.callee));
		};
	},
	
	/**
	 * @name dorado.Browser
	 * @class 用于获取当前浏览器信息的静态对象。
	 * @static
	 */
	Browser: jQuery.browser,
	
	/**
	 * @name dorado.Browser.version
	 * @property
	 * @type String
	 * @description 返回浏览器的版本号。
	 */
	/**
	 * @name dorado.Browser.safari
	 * @property
	 * @type boolean
	 * @description 返回是否Safari浏览器。
	 */
	/**
	 * @name dorado.Browser.opera
	 * @property
	 * @type boolean
	 * @description 返回是否Opera浏览器。
	 */
	/**
	 * @name dorado.Browser.msie
	 * @property
	 * @type boolean
	 * @description 返回是否IE浏览器。
	 */
	/**
	 * @name dorado.Browser.mozilla
	 * @property
	 * @type boolean
	 * @description 返回是否Mozilla浏览器。
	 */
	// =====
	
	defaultToString: function(obj) {
		var s = obj.constructor.className || "[Object]";
		if (obj.id) s += (" id=" + obj.id);
		if (obj.name) s += (" name=" + obj.name);
	},
	
	/**
	 * 返回一个方法的描述信息。
	 * @param {Function} fn 要描述的方法。
	 * @return {String} 方法的描述信息。
	 */
	getFunctionName: function(fn) {
		var defintion = fn.toString().split('\n')[0], name;
		if (fn.methodName) {
			var className;
			if (fn.declaringClass) className = fn.declaringClass.className;
			name = (className ? (className + '.') : "function ") +
			fn.methodName;
		} else {
			var regexpResult = defintion.match(/function (\w*)/);
			name = "function " +
			(regexpResult && regexpResult[1] || "anonymous");
		}
		
		var regexpResult = defintion.match(/\((.*)\)/);
		return name + (regexpResult && regexpResult[0]);
	}
};

if (dorado.Browser.webkit) {
	var ua = navigator.userAgent.toLowerCase();
	
	/**
	 * 返回是否Chrome浏览器。
	 * @type boolean
	 */
	dorado.Browser.chrome = /chrome/.test(ua);
	if (dorado.Browser.chrome) {
		var match = /(chrome)[ \/]([\w.]+)/.exec(ua);
		dorado.Browser.version = match[2];
	}
}

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @class dorado核心类，其中包含了若干最常用的工具方法。
 * @static
 */
dorado.Core = {

	/**
	 * 返回dorado的版本号。
	 * @type String
	 */
	VERSION: "%version%",
	
	/**
	 * 生成一个新的id。
	 * @return {String} 新生成的id。
	 */
	newId: function() {
		return "_uid_" + (++dorado._ID_SEED);
	},
	
	/**
	 * 生成新的时间戳。<br>
	 * 此处的时间戳事实上只是一个自动递增的整数，并不代表当前的时间。
	 * @return {int} 新时间戳。
	 */
	getTimestamp: function() {
		return ++dorado._TIMESTAMP_SEED;
	},
	
	/**
	 * 为一个函数指定其调用时的scope，即指定该函数在调用时this对象的指向。
	 * @param {Object} scope 调用时this对象。
	 * @param {Function|String} fn 要处理的函数或文本形式表示的代码片段。
	 * @return {Function} 代理函数。
	 *
	 * @see $scopify
	 *
	 * @example
	 * var s = "hello!";
	 * dorado.Core.scopify(s, "alert(this)")(); // should say "hello!";
	 *
	 * @example
	 * var s = "hello!";
	 * dorado.Core.scopify(s, function(){
	 *	 alert(this);
	 * })(); // should say "hello"
	 */
	scopify: function(scope, fn) {
		if (fn instanceof Function) {
			return function() {
				return fn.apply(scope, arguments);
			};
		} else {
			return function() {
				return eval("(function(){return(" + fn + ")}).call(scope)");
			};
		}
	},
	
	/**
	 * 设定一个延时任务，同时指定该延时任务在调用时的scope。 该方法的功能类似于window.setTimeout。
	 * @param {Object} scope 调用时this对象。
	 * @param {Function|String} fn 要处理的函数或文本形式表示的代码片段。
	 * @param {int} timeMillis 延时的时长（毫秒数）。
	 * @return {int} 延时任务的id。
	 *
	 * @see dorado.Core.scopify
	 * @see $setInterval
	 *
	 * @example
	 * // should say "hello!" after one second.
	 * var s = "hello!";
	 * dorado.Core.setTimeout(s, function() {
	 *	 alert(this);
	 * }, 1000);
	 */
	setTimeout: function(scope, fn, timeMillis) {
		return setTimeout(dorado.Core.scopify(scope, fn), timeMillis);
	},
	
	/**
	 * 设定一个定时任务，同时指定该定时任务在调用时的scope。 该方法的功能类似于window.setInterval。
	 * @param {Object} scope 调用时this对象。
	 * @param {Function|String} fn 要处理的函数或文本形式表示的代码片段。
	 * @param {int} timeMillis 定时任务的间隔（毫秒数）。
	 * @return {int} 定时任务的id。
	 *
	 * @see dorado.Core.scopify
	 * @see $setInterval
	 */
	setInterval: function(scope, fn, timeMillis) {
		return setInterval(dorado.Core.scopify(scope, fn), timeMillis);
	},
	
	/**
	 * 克隆一个对象。
	 * <p>
	 * 如果被克隆的对象本身支持clone()方法，那么此处将直接使用该对象自身的clone()来完成克隆。
	 * 否则会使用默认的规则，按照类似属性反射的方式对对象进行浅克隆。
	 * </p>
	 * @param {Object} obj 将被克隆的对象。
	 * @param {boolean} [deep] 是否执行深度克隆。
	 * @return {Object} 对象的克隆。
	 */
	clone: function(obj, deep) {
	
		function doClone(obj, deep) {
			if (obj == null || typeof(obj) != "object") return obj;
			if (obj.clone instanceof Function) {
				return obj.clone(deep);
			} else {
				var constr = obj.constructor;
				var cloned = new constr();
				for (var attr in obj) {
					var v = obj[attr];
					if (deep) v = doClone(v, deep);
					cloned[attr] = v;
				}
				return cloned;
			}
		}
		
		return doClone(obj, deep);
	}
};

(function() {

	/**
	 * @name $create
	 * @function
	 * @description document.createElement()方法的快捷方式。
	 * @param {String} tagName 要创建的DOM元素的标签名。
	 * @return {HTMLElement} 新创建的DOM元素。
	 *
	 * @example
	 * var div = $create("DIV"); // 相当于document.createElement("DIV")
	 */
	window.$create = (dorado.Browser.msie && dorado.Browser.version < '9') ? document.createElement : function(arg) {
		return document.createElement(arg);
	};
	
	/**
	 * @name $scopify
	 * @function
	 * @description dorado.Core.scopify()方法的快捷方式。
	 * @param {Object} scope 调用时this对象。
	 * @param {Function|String} fn 要处理的函数或文本形式表示的代码片段。
	 * @return {Function} 代理函数。
	 *
	 * @see dorado.Core.scopify
	 *
	 * @example
	 * var s = "hello!";
	 * $scopify(s, "alert(this)")(); // should say "hello!"
	 *
	 * @example
	 * var s = "hello!";
	 * $scopify(s, function(){
	 *	 alert(this);
	 * })(); // should say "hello!"
	 */
	window.$scopify = dorado.Core.scopify;
	
	/**
	 * @name $setTimeout
	 * @function
	 * @description dorado.Core.setTimeout()方法的快捷方式。
	 * @param {Object} scope 调用时this对象。
	 * @param {Function|String} fn 要处理的函数或文本形式表示的代码片段。
	 * @param {int} timeMillis 延时的时长（毫秒数）。
	 * @return {int} 延时任务的id。
	 *
	 * @see dorado.Core.setTimeout
	 *
	 * @example
	 * // should say "hello!" after one second.
	 * var s = "hello!";
	 * $setTimeout(s, function() {
	 *	 alert(this);
	 * }, 1000);
	 */
	window.$setTimeout = dorado.Core.setTimeout;
	
	/**
	 * @name $setInterval
	 * @function
	 * @description dorado.Core.setInterval()方法的快捷方式。
	 * @param {Object} scope 调用时this对象。
	 * @param {Function|String} fn 要处理的函数或文本形式表示的代码片段。
	 * @param {int} timeMillis 定时任务的间隔（毫秒数）。
	 * @return {int} 定时任务的id。
	 *
	 * @see dorado.Core.setInterval
	 */
	window.$setInterval = dorado.Core.setInterval;
	
})();
