/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @class 标签管理器。
 * <p>
 * 通过此对象可以方便的管理所以支持标签的对象，提取拥有某一标签的所以对象实例。
 * </p>
 * @static
 *
 * @see dorado.AttributeSupport
 * @see dorado.AttributeSupport@attribute:tags
 */
dorado.TagManager = {

	_map: {},
	
	_register: function(tag, object) {
		if (!object._id) object._id = dorado.Core.newId();		
		var info = this._map[tag];
		if (info) {
			if (!info.idMap[object._id]) {
				info.list.push(object);
				info.idMap[object._id] = object;
			}
		} else {
			this._map[tag] = info = {
				list: [object],
				idMap: {}
			};
			info.idMap[object._id] = object;
		}
	},
	
	_unregister: function(tag, object) {
		var info = this._map[tag];
		if (info) {
			if (!info.idMap[object._id]) {
				delete info.idMap[object._id];
				info.list.remove(object);
			}
		}
	},
	
	_regOrUnreg: function(object, remove) {
		var tags = object._tags;
		if (tags) {
			if (typeof tags == "string") tags = tags.split(',');
			if (tags instanceof Array) {
				for (var i = 0; i < tags.length; i++) {
					var tag = tags[i];
					if (typeof tag == "string" && tag.length > 0) {
						remove ? this._unregister(tag, object) : this._register(tag, object);
					}
				}
			}
		}
	},
	
	/**
	 * 向标签管理器中注册一个对象。
	 * <p>此方法一般由系统内部自动调用，如无特殊需要不必自行调用此方法。</p>
	 * @param {dorado.AttributeSupport} object
	 */
	register: function(object) {
		this._regOrUnreg(object);
	},
	
	/**
	 * 从标签管理器中注销一个对象。
	 * <p>此方法一般由系统内部自动调用，如无特殊需要不必自行调用此方法。</p>
	 * @param {dorado.AttributeSupport} object
	 */
	unregister: function(object) {
		this._regOrUnreg(object, true);
	},
	
	/**
	 * 返回所有具有某一指定标签的对象的对象组。
	 * @param {String} tags 标签值。
	 * @return {dorado.ObjectGroup} 对象组。
	 *
	 * @see dorado.ObjectGroup
	 * @see $tags
	 *
	 * @example
	 * // 寻找所有具有limited标签的对象，并统一设置他们的readOnly属性。
	 * dorado.TagManager.find("limited").set("readOnly", true);
	 */
	find: function(tags) {
		var info = this._map[tags];
		return new dorado.ObjectGroup(info ? info.list : null);
	}
};

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @class 对象组。
 * <p>用于将一批对象组合在一起，以便于方便的进行统一处理，例如统一的设置属性或调用方法。</p>
 * @param {Object[]} objects 要进行的组合的对象的数组。
 */
dorado.ObjectGroup = $class({
	constructor: function(objects) {
		if (objects && !(objects instanceof Array)) {
			objects = [objects];
		}
		this.objects = objects || [];
	},
	
	/**
	 * 对组中的所有对象进行属性赋值操作。
	 * <p>如果对象组中的某个对象不支持此处将设置属性，该方法会跳过该对象这一个操作并继续后续处理。
	 * 此方法的使用方法与(@link dorado.AttributeSupport#set)方法非常类似，具体使用说明请参考(@link dorado.AttributeSupport#set)方法的说明。</p>
	 * @param {String|Object} attr 属性名或包含多个属性值对的JSON对象。
	 * @param {Object} [value] 属性值。
	 * @return {dorado.AttributeSupport} 返回对象组自身。
	 *
	 * @see dorado.AttributeSupport#set
	 */
	set: function(attr, value) {
		if (!this.objects) return;
		for (var i = 0; i < this.objects.length; i++) {
			var object = this.objects[i];
			if (object) object.set(attr, value, true);
		}
		return this;
	},
	
	/**
	 * 调用组中的所有对象的某个方法。
	 * @param {String} methodName 要调用的方法名。
	 * @param {Object...} [arg] 调用方法时传入的参数。
	 *
	 * @example
	 * // 同时调用3个按钮的set方法，将他们的disabled属性设置为true。
	 * var group = new dorado.ObjectGroup([button1, button2, button3]);
	 * group.invoke("set", "disabled", true);
	 */
	invoke: function(methodName) {
		if (!this.objects) return;
		for (var i = 0; i < this.objects.length; i++) {
			var object = this.objects[i];
			if (object) {
				var method = object[methodName];
				if (method instanceof Function) method.apply(object, Array.prototype.slice.call(arguments, 1));
			}
		}
	}
});

/**
 * @name $group
 * @function
 * @description 创建一个对象组。new dorado.ObjectGroup()操作的快捷方式。
 * @param {Object..} objects 要进行的组合的对象的数组。
 * @return {dorado.ObjectGroup} 新创建的对象组。
 *
 * @see dorado.ObjectGroup
 */
window.$group = function() {
	return new dorado.ObjectGroup(Array.prototype.slice.call(arguments));
};

/**
 * @name $tag
 * @function
 * @description 返回所有具有某一指定标签的对象的对象组。dorado.TagManager.find()方法的快捷方式。
 * @param {String} tags 标签值。
 * @return {dorado.ObjectGroup} 对象组。
 *
 * @see dorado.TagManager.find
 *
 * @example
 * // 寻找所有具有limited标签的对象，并统一设置他们的readOnly属性。
 * $tag("limited").set("readOnly", true);
 */
window.$tag = function(tags) {
	return dorado.TagManager.find(tags);
};
