/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @class 用于与数据实体中某个属性进行绑定的数据控件的基类。
 * <p>
 * 此种的数据控件的dataPath属性默认为"#"，表示与数据集中当前数据实体进行绑定。 主要用于跟DataSet中数据实体{@link dorado.Entity}的某个属性进行绑定。
 * </p>
 * @abstract
 * @extends dorado.widget.DataControl
 */
dorado.widget.PropertyDataControl = $extend(dorado.widget.DataControl, /** @scope dorado.widget.PropertyDataControl.prototype */ {
	$className: "dorado.widget.PropertyDataControl",
	
	ATTRIBUTES: /** @scope dorado.widget.PropertyDataControl.prototype */ {
	
		/**
		 * 数据路径，用于指定数据控件与数据集中的哪些数据节点进行关联。
		 * @type String
		 * @attribute
		 * @default "#"
		 * @see dorado.DataPath
		 */
		dataPath: {
			defaultValue: '#'
		},
		
		/**
		 * 数据控件绑定的属性名。
		 * @type String
		 * @attribute
		 * @see dorado.DataPath
		 */
		property: {}
	},
	
	filterDataSetMessage: function(messageCode, arg, data) {
		var b = true;
		switch (messageCode) {
			case dorado.widget.DataSet.MESSAGE_DATA_CHANGED:
				b = (arg.property == this._property);
				break;
			case dorado.widget.DataSet.MESSAGE_DELETED:
			case dorado.widget.DataSet.MESSAGE_INSERTED:
				b = false;
				break;
		}
		return b;
	},
	
	getBindingData: function(options) {
		var realOptions = {
			firstResultOnly: true,
			acceptAggregation: false
		};
		if (typeof options == "String") {
			realOptions.loadMode = options;
		} else {
			dorado.Object.apply(realOptions, options);
		}
		return $invokeSuper.call(this, [realOptions]);
	},
	
	getBindingPropertyDef: function() {
		var entityDataType = this.getBindingDataType(), p;
		if (entityDataType) p = entityDataType.getPropertyDef(this._property);
		return p;
	},
	
	getBindingPropertyValue: function() {
		var entity = this.getBindingData(true);
		return (entity) ? entity.get(this._property) : null;
	},
	
	getBindingPropertyText: function() {
		var entity = this.getBindingData(true);
		return (entity) ? entity.getText(this._property) : '';
	}
});
