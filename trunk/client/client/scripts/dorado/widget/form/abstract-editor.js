/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @class 编辑器的抽象类。
 * @abstract
 * @extends dorado.widget.Control
 */
dorado.widget.AbstractEditor = $extend(dorado.widget.Control, /** @scope dorado.widget.AbstractEditor.prototype */ {
	$className: "dorado.widget.AbstractEditor",
	
	focusable: true,
	
	/**
	 * @name dorado.widget.AbstractEditor#doPost
	 * @function
	 * @protected
	 * @description 当系统尝试确认编辑器中的编辑内容时执行的内部方法。
	 * @return boolean 编辑器中的内容是否得到了确认。
	 * @see dorado.widget.AbstractEditor#post
	 */
	// =====
	
	ATTRIBUTES: /** @scope dorado.widget.AbstractEditor.prototype */ {
		/**
		 * 编辑器中的数值。
		 * @type Object
		 * @attribute
		 */
		value: {},
		
		/**
		 * 编辑器支持与一个数据对象中的某个属性进行关联，自动显示和更新该属性的值。
		 * <p>
		 * 注意：此种关联并不是全动态的，当关联关系建立后，如果数据对象中的数值发生了改变，编辑器是不会自动更新显示的。
		 * </p>
		 * @type Object|dorado.Entity
		 * @attribute
		 * @see dorado.widget.TextEditor#attribute:property
		 */
		entity: {
			setter: function(v) {
				this._entity = v;
				this.refreshData();
			}
		},
		
		/**
		 * 当编辑器与一个数据对象进行关联时，此属性指示究竟就数据对象中的那个属性进行关联。
		 * @type String
		 * @attribute
		 * @see dorado.widget.TextEditor#attribute:entity
		 */
		property: {
			setter: function(v) {
				this._property = v;
				this.refreshData();
			}
		},
		
		/**
		 * 是否只读。
		 * @type boolean
		 * @attribute
		 */
		readOnly: {},
		
		/**
		 * 是否支持脏状态标记的显示。
		 * @type boolean
		 * @attribute
		 * @default true
		 */
		supportsDirtyFlag: {
			defaultValue: true
		},
		
		/**
		 * 编辑器中当前是否包含有未被确认的脏数据。
		 * @type boolean
		 * @attribute readOnly
		 */
		modified: {
			readOnly: true
		}
	},
	
	EVENTS: /** @scope dorado.widget.AbstractEditor.prototype */ {
		/**
		 * 当编辑器将要尝试确认其中的编辑内容之前触发的事件。
		 * <p>
		 * 注意，在此事件中有两种方法可以终止本次确认操作。直接抛出一个异常或者将arg.processDefault参数设置为false。<br>
		 * 我们推荐您在此事件中使用直接抛出一个异常的方式在终止确认操作。
		 * 因为这样系统可以在终止的同时得到异常信息中包含的具体终止原因，在很多场景下系统可以自动向用户提示操作被终止的具体原因。
		 * 例如以弹出对话框的形式或显示在表单信息提示区中。
		 * </p>
		 * @param {Object} self 事件的发起者，即组件本身。
		 * @param {Object} arg 事件参数。
		 * @param {boolean} #arg.processDefault=true 是否在事件结束后继续执行系统默认的确认动作。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
		beforePost: {},
		
		/**
		 * 当编辑器确认了其中的编辑内容之后触发的事件。
		 * @param {Object} self 事件的发起者，即组件本身。
		 * @param {Object} arg 事件参数。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
		onPost: {},
		
		/**
		 * 当编辑器校验其中的内容，并且校验未通过时触发的事件。
		 * @param {Object} self 事件的发起者，即组件本身。
		 * @param {Object} arg 事件参数。
		 * @param {Error|dorado.Exception} arg.exception 包含校验失败信息的异常对象。
		 * @param {boolean} #arg.processDefault=true 是否在事件结束后继续执行系统默认的确认动作。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
		onPostFailed: {}
	},
	
	refreshDom: function(dom) {
			$invokeSuper.call(this, arguments);
			
			this._bindingInfo = null;
			if (this._dataSet) {
				this._entity = null;
				if (this._property) {
					var bindingInfo = this._bindingInfo = this.getBindingInfo();
					if (bindingInfo.entity instanceof dorado.Entity) {
						this._entity = bindingInfo.entity;
					}
				}
			}
		},
	
	/**
	 * 确认编辑器中的编辑内容，即将编辑器中的数值写入到与之关联的数据对象中。
	 * 一般而言，系统会在编辑器失去控制焦点或用户按下回车键时尝试确认编辑器中的编辑内容。
	 * @param {boolean} allowSkip 是否允许此方法在发现编辑器中的内容没有被修改过时跳过确认内容的操作。
	 * 此参数对于部分编辑器而言可能没用意义。
	 * @return {boolean} 返回是否编辑器中的修改被成功的确认。
	 */
	post: function(allowSkip) {
		return this.doPost ? this.doPost() : false;
	},
	
	/**
	 * 刷新编辑器中的数据，即重新从关联的数据对象中读取属性值并更新编辑器中的显示。
	 * <p>
	 * 如无特殊需要，一般请不要复写此方法，而应该复写doRefreshData。
	 * </p>
	 * @see dorado.widget.SingleValueEditor#doRefreshData
	 */
	refreshData: function() {
		if (this._property && this._entity) {
			if (this.doRefreshData) {
				if (!this._refreshDataPerformed && this._entity 
						&& this._entity instanceof dorado.widget.FormProfile.DefaultEntity
						&& this._entity[this._property] === undefined) {
					return;
				}
				this._refreshDataPerformed = true;
				this.doRefreshData();
			}
		}
	},
	
	/**
	 * 刷新编辑器中的数据。此方法供子类复写。
	 * @protected
	 * @see dorado.widget.SingleValueEditor#refreshData
	 */
	doRefreshData: function() {
		var p = this._property, e = this._entity;
		this.set("value", (e instanceof dorado.Entity) ? e.get(p) : e[p]);
		this.setDirty(false);
	},
	
	doPost: function() {
		var p = this._property, e = this._entity;
		if (p && e) {
			var v = this.get("value");
			if (e instanceof dorado.Entity) {
				e.set(p, v);
				this.setDirty(e.isDirty(p));
			} else {
				e[p] = v;
				this.setDirty(true);
			}
		}
		return true;
	},
	
	setDirty: function(dirty) {
		if (!this._supportsDirtyFlag) return;
		$fly(this.getDom()).toggleClass(this._className + "-dirty", !!dirty);
	}
});

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @class 支持数据绑定的编辑器抽象接口。
 * @abstract
 * @extends dorado.widget.AbstractEditor
 * @extends dorado.widget.PropertyDataControl
 */
dorado.widget.AbstractDataEditor = $extend([dorado.widget.AbstractEditor, dorado.widget.PropertyDataControl], /** @scope dorado.widget.AbstractDataEditor.prototype */ {
	$className: "dorado.widget.AbstractDataEditor",
	
	filterDataSetMessage: function(messageCode, arg, data) {
		var b = true;
		switch (messageCode) {
			case dorado.widget.DataSet.MESSAGE_ENTITY_STATE_CHANGED:
				b = false;
				break;
			default:
				b = $invokeSuper.call(this, arguments);
		}
		return b;
	},
	
	processDataSetMessage: function(messageCode, arg, data) {
		switch (messageCode) {
			case dorado.widget.DataSet.MESSAGE_REFRESH:
			case dorado.widget.DataSet.MESSAGE_DATA_CHANGED:
			case dorado.widget.DataSet.MESSAGE_CURRENT_CHANGED:
			case dorado.widget.DataSet.MESSAGE_ENTITY_STATE_CHANGED:
				this.refresh(true);
				break;
		}
	},
	
	/**
	 * 返回数据绑定相关的信息。
	 * @return {Object} 数据绑定相关的信息，其中包含下列子属性:
	 * <ul>
	 * <li>entity	-	{dorado.Entity} 目前与编辑器相关的数据实体。</li>
	 * <li>timestamp	-	{int} 与编辑器相关的数据实体的修改时间戳。</li>
	 * <li>propertyDef	-	{dorado.PropertyDef} 与编辑器关联的属性声明。</li>
	 * <li>entityDataType	-	{dorado.EntityDataType} 与编辑器相关的数据实体的数据类型。</li>
	 * <li>dataType	-	{dorado.DataType} 与编辑器相关的属性的数据类型。</li>
	 * </ul>
	 */
	getBindingInfo: function() {
		if (this._bindingInfoCache && (new Date().getTime() - this._bindingInfoCache.cacheTimestamp) < 50) {
			return this._bindingInfoCache;
		}
		
		var info = {};
		var entityDataType;
		var entity = info.entity = this.getBindingData();
		if (entity != null) {
			entityDataType = entity.dataType;
			info.timestamp = entity.timestamp;
		}
		if (!entityDataType) entityDataType = this.getBindingDataType();
		info.entityDataType = entityDataType;
		
		if (entityDataType) {
			info.propertyDef = entityDataType.getPropertyDef(this._property);
			info.dataType = info.propertyDef ? info.propertyDef.get("dataType") : null;
		}
		if (info.timestamp == null) info.timestamp = 0;
		info.cacheTimestamp = new Date().getTime();
		this._bindingInfoCache = info;
		return info;
		
	},
	
	doPost: function() {
		var p = this._property, e = this._entity;
		if (!p || !e) return false;
		
		var b = false;
		if (this._dataSet) {
			e.set(p, this.get("value"));
			this.timestamp = e.timestamp;
			b = true;
		} else {
			if (e instanceof dorado.Entity) {
				e.set(p, this.get("value"));
			} else {
				e[p] = this.get("value");
			}
			b = true;
		}
		return b;
	}
});
