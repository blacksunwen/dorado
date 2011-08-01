/**
 * @name dorado.validator
 * @namespace 各种数据校验器的命名空间。
 */
dorado.validator = {};

dorado.Toolkits.registerTypeTranslator("validator", function(type) {
	return dorado.util.Common.getClassType("dorado.validator." + type + "Validator", true);
});

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @class 数据校验器的抽象类。
 * @abstract
 * @extends dorado.AttributeSupport
 */
dorado.validator.Validator = $extend(dorado.AttributeSupport, /** @scope dorado.validator.Validator.prototype */ {
	className: "dorado.validator.Validator",
	
	ATTRIBUTES:/** @scope dorado.validator.Validator.prototype */ {
	
		/**
		 * 校验未通过时给出的信息。
		 * @type String
		 * @attribute
		 */
		defaultResultMessage: {},
		
		/**
		 * 校验未通过时给出的信息的默认级别。
		 * <p>
		 * 取值范围包括：info、ok、warn、error。默认值为error。
		 * </p>
		 * @type String
		 * @attribute
		 * @default "error"
		 */
		defaultResultState: {
			defaultValue: "error"
		}
	},
	
	/**
	 * @name dorado.validator.Validator#doValidate
	 * @function
	 * @protected
	 * @param {Object} data 要验证的数据。
	 * @return {String|Object|[String]|[Object]} 验证结果。
	 * 此处的返回值可以是单独的消息文本、消息对象等。最终Validator会将各种类型的返回值自动的转换成标准的形式。
	 * @description 内部的验证数据逻辑。
	 * @see dorado.validator.Validator#validate
	 */
	// =====
	
	constructor: function(config) {
		$invokeSuper.call(this, arguments);
		if (config) this.set(config);
	},
	
	/**
	 * 验证数据。
	 * @param {Object} data 要验证的数据。
	 * @return {[Object]} 验证结果。
	 * 返回的验证结果应该是由0到多个验证消息构成的数组。每一个验证结果是一个JSON对象，该JSON对象包含以下属性：
	 * <ul>
	 * <li>state	-	{String} 信息级别。取值范围包括：info、ok、warn、error。默认值为error。</li>
	 * <li>text	-	{String} 信息内容。</li>
	 * </ul>
	 * @see dorado.Entity#getPropertyMessages
	 */
	validate: function(data) {
		var result = this.doValidate(data);
		return dorado.Toolkits.trimMessages(result, this._defaultResultState);
	}
});

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @class 远程校验器。
 * @extends dorado.validator.Validator
 * @abstract
 */
dorado.validator.RemoteValidator = $extend(dorado.validator.Validator, /** @scope dorado.validator.RemoteValidator.prototype */ {
	className: "dorado.validator.RemoteValidator",
	
	ATTRIBUTES: /** @scope dorado.validator.RemoteValidator.prototype */ {
		/**
		 * 是否以异步的方式来执行验证。
		 * @type boolean
		 * @attribute
		 * @default true
		 */
		async: {
			defaultValue: true
		},
		
		/**
		 * 当此校验器正在执行时希望系统显示给用户的提示信息。
		 * <p>
		 * 此属性目前仅在校验器以异步模式执行时有效。
		 * </p>
		 * @type String
		 * @attribute
		 */
		executingMessage: {}
	},
	
	/**
	 * @name dorado.validator.RemoteValidator#doValidate
	 * @function
	 * @protected
	 * @param {Object} data 要验证的数据。
	 * @param {Function|dorado.Callback} callback 回调方法或对象。
	 * @return {String|Object|[String]|[Object]} 验证结果。
	 * <ul>
	 * <li>对于同步的验证方式，验证结果将直接通过方法的返回值返回。</li>
	 * <li>对于异步的验证方式，验证结果将通过回调方法的参数传给外界。</li>
	 * </ul>
	 * 此处的验证结果可以是单独的消息文本、消息对象等。最终Validator会将各种类型的返回值自动的转换成标准的形式。
	 * @description 内部的验证数据逻辑。
	 * @see dorado.validator.RemoteValidator#validate
	 */
	// =====
	
	/**
	 * @name dorado.validator.RemoteValidator.Validator#validate
	 * @function
	 * @param {Object} data 要验证的数据。
	 * @param {Function|dorado.Callback} callback 回调方法或对象。此参数对于同步或异步两种验证方式都有效。
	 * @return {[Object]} 验证结果。
	 * <ul>
	 * <li>对于同步的验证方式，验证结果将直接通过方法的返回值返回。</li>
	 * <li>对于异步的验证方式，验证结果将通过回调方法的参数传给外界。</li>
	 * </ul>
	 * @description 验证数据。
	 */
	validate: function(data, callback) {
		if (this._async) {
			this.doValidate(data, {
				scope: this,
				callback: function(success, result) {
					if (success) result = dorado.Toolkits.trimMessages(result, this._defaultResultState);
					else result = dorado.Toolkits.trimMessages(dorado.Exception.getExceptionMessage(result), "error");
					$callback(callback, true, result);
				}
			});
		} else {
			var result = $invokeSuper.call(this, arguments);
			if (callback) $callback(callback, true, result);
			return result;
		}
	}
});

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @class 简单校验器的抽象类。
 * @extends dorado.validator.Validator
 * @abstract
 */
dorado.validator.BaseValidator = $extend(dorado.validator.Validator, /** @scope dorado.validator.BaseValidator.prototype */ {
	className: "dorado.validator.BaseValidator",
	
	ATTRIBUTES:/** @scope dorado.validator.BaseValidator.prototype */ {
	
		/**
		 * 默认的验证信息内容。
		 * @type String
		 * @attribute
		 */
		resultMessage: {}
	},
	
	validate: function(data) {
		var result = this.doValidate(data);
		if (this._resultMessage && result && typeof result == "string") result = this._resultMessage;
		return dorado.Toolkits.trimMessages(result, this._defaultResultState);
	}
});

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @class 文本长度校验器。
 * @shortTypeName Length
 * @extends dorado.validator.BaseValidator
 */
dorado.validator.LengthValidator = $extend(dorado.validator.BaseValidator, /** @scope dorado.validator.LengthValidator.prototype */ {
	className: "dorado.validator.LengthValidator",
	
	ATTRIBUTES: /** @scope dorado.validator.LengthValidator.prototype */ {
	
		/**
		 * 最小合法长度。如果设置为-1则表示忽略对于最小合法长度的校验。
		 * @type int
		 * @attribute
		 * @default -1
		 */
		minLength: {
			defaultValue: -1
		},
		
		/**
		 * 最大合法长度。如果设置为-1则表示忽略对于最大合法长度的校验。
		 * @type int
		 * @attribute
		 * @default -1
		 */
		maxLength: {
			defaultValue: -1
		}
	},
	
	doValidate: function(data) {
		if (typeof data != "string") return;
		var invalid, message = '', len = data.length;
		if (this._minLength > 0 && len < this._minLength) {
			invalid = true;
			message += $resource("dorado.data.ErrorContentTooShort", this._minLength);
		}
		if (this._maxLength > 0 && len > this._maxLength) {
			invalid = true;
			if (message) message += '\n';
			message += $resource("dorado.data.ErrorContentTooLong", this._maxLength);
		}
		return message;
	}
});


/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @class 数值区间校验器。
 * @shortTypeName Range
 * @extends dorado.validator.BaseValidator
 */
dorado.validator.RangeValidator = $extend(dorado.validator.BaseValidator, /** @scope dorado.validator.RangeValidator.prototype */ {
	className: "dorado.validator.RangeValidator",
	
	ATTRIBUTES: /** @scope dorado.validator.RangeValidator.prototype */ {
	
		/**
		 * 最小值。
		 * @type float
		 * @attribute
		 * @see dorado.validator.RangeValidator#attribute:minValueValidateMode
		 */
		minValue: {},
		
		/**
		 * 最小值的校验方式。
		 * <p>
		 * 目前支持如下几种取值:
		 * <ul>
		 * <li>ignore	-	忽略对于最小值的校验。</li>
		 * <li>allowEquals	-	被校验的数据必须大于或等于设定的最小值。</li>
		 * <li>notAllowEquals	-	被校验的数据必须大于设定的最小值。</li>
		 * </ul>
		 * </p>
		 * @type String
		 * @attribute
		 * @default "ignore"
		 * @see dorado.validator.RangeValidator#attribute:minValue
		 */
		minValueValidateMode: {
			defaultValue: "ignore"
		},
		
		/**
		 * 最大值。
		 * @type float
		 * @attribute
		 * @see dorado.validator.RangeValidator#attribute:maxValueValidateMode
		 */
		maxValue: {},
		
		
		
		/**
		 * 最大值的校验方式。
		 * <p>
		 * 目前支持如下几种取值:
		 * <ul>
		 * <li>ignore	-	忽略对于最大值的校验。</li>
		 * <li>allowEquals	-	被校验的数据必须小于或等于设定的最大值。</li>
		 * <li>notAllowEquals	-	被校验的数据必须小于设定的最大值。</li>
		 * </ul>
		 * </p>
		 * @type String
		 * @attribute
		 * @default "ignore"
		 * @see dorado.validator.RangeValidator#attribute:maxValue
		 */
		maxValueValidateMode: {
			defaultValue: "ignore"
		}
	},
	
	doValidate: function(data) {
		var invalid, message = '', subMessage = '', data = (typeof data == "number") ? data : parseFloat(data);
		if (this._minValueValidateMode != "ignore") {
			if (data == this._minValue && this._minValueValidateMode != "allowEquals") {
				invalid = true;
				subMessage = $resource("dorado.data.ErrorOrEqualTo");
			}
			if (data < this._minValue) {
				invalid = true;
			}
			if (invalid) {
				message += $resource("dorado.data.ErrorNumberTooLess", subMessage, this._minValue);
			}
		}
		if (this._maxValueValidateMode != "ignore") {
			if (data == this._maxValue && this._maxValueValidateMode != "allowEquals") {
				invalid = true;
				subMessage = $resource("dorado.data.ErrorOrEqualTo");
			}
			if (data > this._maxValue) {
				invalid = true;
			}
			if (invalid) {
				if (message) message += '\n';
				message += $resource("dorado.data.ErrorNumberTooGreat", subMessage, this._maxValue);
			}
		}
		if (invalid) return message;
	}
});

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @class 枚举值校验器。
 * <p>
 * 即允许用户设置一个合法值的列表，只有处于合法值的列表中的数值才能通过此校验。
 * </p>
 * @shortTypeName Enum
 * @extends dorado.validator.BaseValidator
 */
dorado.validator.EnumValidator = $extend(dorado.validator.BaseValidator, /** @scope dorado.validator.EnumValidator.prototype */ {
	className: "dorado.validator.EnumValidator",
	
	ATTRIBUTES: /** @scope dorado.validator.EnumValidator.prototype */ {
	
		/**
		 * 合法值的数组。
		 * @type Object[]
		 * @attribute
		 */
		enumValus: {}
	},
	
	doValidate: function(data) {
		if (this._enumValues instanceof Array && this._enumValues.indexOf(data) < 0) {
			return $resource("dorado.data.ErrorValueOutOfEnumRange");
		}
	}
});

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @class 正则表达式校验器。
 * @shortTypeName RegExp
 * @extends dorado.validator.BaseValidator
 */
dorado.validator.RegExpValidator = $extend(dorado.validator.BaseValidator, /** @scope dorado.validator.RegExpValidator.prototype */ {
	className: "dorado.validator.RegExpValidator",
	
	ATTRIBUTES: /** @scope dorado.validator.RegExpValidator.prototype */ {
	
		/**
		 * 白表达式。即用于描述怎样的数值是合法值的表达式。
		 * @attribute
		 * @type String
		 */
		whiteRegExp: {},
		
		/**
		 * 黑表达式。即用于描述怎样的数值是非法值的表达式。
		 * @attribute
		 * @type String
		 */
		blackRegExp: {},
		
		/**
		 * 校验模式，此属性用于决定黑白两种表达式哪一个的优先级更高。
		 * 该属性支持两种取值:
		 * <ul>
		 * <li>whiteBlack	-	先白后黑，即首先校验白表达式，即表示最终黑表达式的优先级更高。</li>
		 * <li>blackWhite	-	先黑后白，即首先校验黑表达式，即表示最终白表达式的优先级更高。</li>
		 * </ul>
		 * @attribute
		 * @type String
		 * @default "whiteBlack"
		 */
		validateMode: {
			defaultValue: "whiteBlack"
		}
	},
	
	doValidate: function(data) {
	
		function toRegExp(text) {
			var regexp = null;
			if (text) {
				regexp = (text.charAt(0) == '/') ? eval(text) : new RegExp(text);
			}
			return regexp;
		}
		
		if (typeof data != "string") return;
		var whiteRegExp = toRegExp(this._whiteRegExp), blackRegExp = toRegExp(this._blackRegExp);
		var valid;
		if (this._validateMode == "whiteBlack") {
			valid = blackRegExp && !data.match(blackRegExp) || whiteRegExp && data.match(whiteRegExp);
		} else {
			valid = whiteRegExp && data.match(whiteRegExp) || blackRegExp && !data.match(blackRegExp);
		}
		if (!valid) return $resource("dorado.data.ErrorBadFormat", data);
	}
});

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @class 通过Ajax操作执行的远程校验器。
 * @shortTypeName Ajax
 * @extends dorado.validator.RemoteValidator
 */
dorado.validator.AjaxValidator = $extend(dorado.validator.RemoteValidator, /** @scope dorado.validator.AjaxValidator.prototype */ {
	className: "dorado.validator.AjaxValidator",
	
	ATTRIBUTES: /** @scope dorado.validator.AjaxValidator.prototype */ {
		/**
		 * Dorado服务端暴露给客户端的某个服务的名称。
		 * @type String
		 * @attribute
		 */
		service: {}
	},
	
	constructor: function(config) {
		if (!dorado.widget || !dorado.widget.AjaxAction) {
			this._disabled = true;
			alert("'dorado.validator.AjaxValidator' is disabled because the 'dorado.widget.AjaxAction' is not available.");
		}
		$invokeSuper.call(this, arguments);
	},
	
	doValidate: function(data, callback) {
		var ajaxAction = this._ajaxAction;
		if (!ajaxAction) {
			this._ajaxAction = ajaxAction = new dorado.widget.AjaxAction();
		}
		ajaxAction.set({
			async: this._async,
			executingMessage: this._executingMessage,
			service: this._service,
			parameter: data
		});
		var retval = ajaxAction.execute(this._async ? callback : null);
		if (retval && !this._async) {
			return ajaxAction.get("returnValue");
		}
	}
});
