/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @class 抽象的Dorado异常。
 * @abstract
 */
dorado.AbstractException = $class({
	$className: "dorado.AbstractException",
	
	constructor: function(script) {
		dorado.Exception.EXCEPTION_STACK.push(this);
		
		if (dorado.Browser.msie || dorado.Browser.mozilla) {
			/* 强行接管window.onerror事件，须建议用户不要自行声明此事件 */
			window.onerror = function(message, url, line) {
				var result = false;
				if (dorado.Exception.EXCEPTION_STACK.length > 0) {
					var e;
					while (e = dorado.Exception.EXCEPTION_STACK.peek()) {
						dorado.Exception.processException(e);
					}
					result = true;
				}
				window.onerror = null;
				return result;
			};
		}
		
		$setTimeout(this, function() {
			if (dorado.Exception.EXCEPTION_STACK.indexOf(this) >= 0) {
				dorado.Exception.processException(this);
			}
		}, 0);
	}
});


/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @class dorado异常的基类。
 * <p>
 * 此类可以实例化并抛出。
 * 虽然JavaScript支持抛出任何类型的对象，但是在dorado的框架下抛出dorado.Exception或其派生类的实例可以获得额外的好处。
 * 例如：可以通过dorado.Exception获得错误堆栈信息，以辅助对错误的定位和调试。
 * </p>
 * <p>
 * 需要特别注意的是dorado.Excpeption对象一旦被创建，即便没有抛出(throw)，最终都会激活{@link dorado.Exception.processException}方法，
 * 默认情况下此异常信息一定会弹出错误提示(dorado.AbortException)。<br>
 * 当然，尽管dorado.Excpeption一旦被创建会自动引起错误提示，大部分情况下我们仍需要将其抛出(throw)。
 * 否则当前程序仍会按照正常的情况继续向后执行，并不会被该异常打断。
 * </p>
 * @extends dorado.AbstractException
 * @param {String} [message] 异常信息。
 *
 * @example
 * throw new dorado.Exception("My Message.");
 */
dorado.Exception = $extend(dorado.AbstractException, /** @scope dorado.Exception.prototype */ {
	$className: "dorado.Exception",
	
	/**
	 * dorado生成的错误堆栈信息。
	 * @name dorado.Exception#stack
	 * @property
	 * @type String[]
	 */
	/**
	 * 系统提供的错误堆栈信息。目前仅在FireFox、Mozilla下可以得到。
	 * @name dorado.Exception#systemStack
	 * @property
	 * @type String[]
	 */
	// =====
	
	constructor: function(message) {
		this.message = message;
		this._buildStackTrace();
		$invokeSuper.call(this, arguments);
	},
	
	_buildStackTrace: function() {
		var stack = [];
		var funcCaller = dorado.Exception.caller, callers = [];
		while (funcCaller && callers.indexOf(funcCaller) < 0) {
			callers.push(funcCaller);
			stack.push(dorado.getFunctionName(funcCaller));
			funcCaller = funcCaller.caller;
		}
		this.stack = stack;
		
		if (dorado.Browser.mozilla || dorado.Browser.chrome) {
			var stack = new Error().stack;
			if (stack) {
				stack = stack.split('\n');
				this.systemStack = stack.slice(2, stack.length - 1);
			}
		}
	},
	
	/**
	 * 将传入的调用堆栈信息格式化为较便于阅读的文本格式。
	 * @param {String[]} stack 调用堆栈信息。
	 * @return {String} 格式化后的调用堆栈信息。
	 */
	formatStack: function(stack) {
		var msg = "";
		if (stack) {
			for (var i = 0; i < stack.length; i++) {
				if (i > 0) msg += '\n';
				msg += " > " + stack[i];
				if (i > 10) {
					msg += "\n > ... ... ...";
					break;
				}
			}
		}
		return msg;
	},
	
	toString: function() {
		return this.message;
	}
});

//=====

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @class 终止异常。
 * <p>
 * 这是一个特殊的异常对象，用于通知系统放弃当前的操作。该异常是哑异常，抛出后不会带来任何默认的异常提示。
 * </p>
 * @extends dorado.Exception
 */
dorado.AbortException = $extend(dorado.Exception, {
	$className: "dorado.AbortException"
});

//=====

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @class 特殊的用于执行一段JavaScript的异常。
 * @extends dorado.AbstractException
 * @param {String} script 可执行的脚本。
 */
dorado.RunnableException = $extend(dorado.AbstractException, {
	$className: "dorado.RunnableException",
	
	/**
	 *可执行的脚本。
	 * @name dorado.RunnableException#script
	 * @property
	 * @type String
	 */
	// =====
	
	constructor: function(script) {
		this.script = script;
		$invokeSuper.call(this, arguments);
	},
	
	toString: function() {
		return this.script;
	}
});

//=====

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @class 支持自动从资源库中提取消息的异常。
 * @extends dorado.Exception
 * @param {String} path 由命名空间+资源项名称组成的资源路径。
 * @param {Object...} [args] 一到多个参数。
 * @see $resource
 */
dorado.ResourceException = $extend(dorado.Exception, {
	$className: "dorado.ResourceException",
	
	constructor: function() {
		$invokeSuper.call(this, [$resource.apply(this, arguments)]);
	}
});

//=====

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @class 来自外部系统的异常信息。
 * @extends dorado.Exception
 * @param {String} message 异常信息。
 * @param {String} [exceptionType] 外部系统中报出的异常类型。
 * @param {String} [remoteStack] 外部系统中报出的异常堆栈。
 */
dorado.RemoteException = $extend(dorado.Exception, {
	$className: "dorado.RemoteException",
	
	/**
	 * 异常类型。对于来自Java的异常而言此属性为异常对象的className。
	 * @name dorado.RemoteException#exceptionType
	 * @property
	 * @type String
	 */
	/**
	 * 外部系统中的错误堆栈信息。对于来自Java的异常而言即异常堆栈中的StackTrace信息。
	 * @name dorado.Exception#remoteStack
	 * @property
	 * @type String[]
	 */
	// =====
	
	constructor: function(message, exceptionType, remoteStack) {
		$invokeSuper.call(this, [message]);
		this.exceptionType = exceptionType;
		this.remoteStack = remoteStack;
	}
});

//=====

dorado.Exception.EXCEPTION_STACK = [];


dorado.Exception.getExceptionMessage = function(e) {
	if (!e || e instanceof dorado.AbortException) return null;
	var msg;
	if (e instanceof dorado.Exception) msg = e.message;
	else if (e instanceof Error) msg = e.message;
	else msg = e;
	return msg;
};

/**
 * 处理一个异常对象。
 * <p>
 * 注意：此方法需要处理的异常对象包括所有JavaScript所支持抛出的对象。
 * </p>
 * @param {dorado.Exception|Object} e 异常对象。
 */
dorado.Exception.processException = function(e) {
	dorado.Exception.removeException(e);
	if (!e || e instanceof dorado.AbortException) return;
	
	if (e instanceof dorado.RunnableException) {
		eval(e.script);
	} else {
		var msg = dorado.Exception.getExceptionMessage(e);
		if (e instanceof dorado.Exception) {
			if (e.stack) msg += "\n\nStack:\n" + e.formatStack(e.stack);
			if (e.remoteStack) msg += "\n\nRemote Stack:\n" + e.formatStack(e.remoteStack);
			if (e.systemStack) msg += "\n\nSystem Stack:\n" + e.formatStack(e.systemStack);
		} else if (e instanceof Error) {
			if (e.stack) msg += "\n\nSystem Stack:\n" + e.stack;
		}
		
		if (!dorado.MessageBox || !document.body) {
			alert(msg);
		} else {
			dorado.MessageBox.alert(msg, {
				icon: dorado.MessageBox.ERROR_ICON
			});
		}
	}
};

/**
 * 从系统的异常堆栈中移除一个异常对象。
 * @param {dorado.Exception|Object} e 异常对象。
 */
dorado.Exception.removeException = function(e) {
	dorado.Exception.EXCEPTION_STACK.remove(e);
};
