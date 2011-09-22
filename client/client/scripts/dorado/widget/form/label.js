dorado.widget.LabelRenderer = $extend(dorado.Renderer, {
	render: function(dom, arg) {
		dom.innerText = arg.text;
	}
});

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @component Form
 * @class 文本标签。
 * @extends dorado.widget.Control
 */
dorado.widget.Label = $extend(dorado.widget.Control, /** @scope dorado.widget.Label.prototype */ {
	$className: "dorado.widget.Label",
	_inherentClassName: "i-label",
	
	ATTRIBUTES: /** @scope dorado.widget.Label.prototype */ {
		className: {
			defaultValue: "d-label"
		},
		
		/**
		 * 显示的文本。
		 * @type String
		 * @attribute
		 */
		text: {},
		
		/**
		 * 渲染器。
		 * @type dorado.Renderer
		 * @attribute
		 */
		renderer: {}
	},
	
	refreshDom: function(dom) {
		$invokeSuper.call(this, arguments);
		(this._renderer || $singleton(dorado.widget.LabelRenderer)).render(dom, {
			text: this._text
		});
	}
});

dorado.widget.DataLabelRenderer = $extend(dorado.Renderer, {
	render: function(dom, arg) {
		var text = (arg.property && arg.entity != null) ? arg.entity.getText(arg.property) : '';
		dom.innerText = text;
	}
});

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @component Form
 * @class 数据标签。<br>
 * 用于展示数据集中某个数据项的数据的文本标签。
 * @extends dorado.widget.Control
 * @extends dorado.widget.PropertyDataControl
 */
dorado.widget.DataLabel = $extend([dorado.widget.Control, dorado.widget.PropertyDataControl], /** @scope dorado.widget.DataLabel.prototype */ {
	$className: "dorado.widget.DataLabel",
    _inherentClassName: "i-data-label",
	
	ATTRIBUTES: /** @scope dorado.widget.DataLabel.prototype */ {
		className: {
			defaultValue: "d-data-label"
		},
		
		width: {
			defaultValue: 100
		},
		
		/**
		 * 渲染器。
		 * @type dorado.Renderer
		 * @attribute
		 */
		renderer: {}
	},
	
	processDataSetMessage: function(messageCode, arg, data) {
		switch (messageCode) {
			case dorado.widget.DataSet.MESSAGE_REFRESH:
			case dorado.widget.DataSet.MESSAGE_DATA_CHANGED:
			case dorado.widget.DataSet.MESSAGE_CURRENT_CHANGED:
				this.refresh(true);
				break;
		}
	},
	
	refreshDom: function(dom) {
		$invokeSuper.call(this, arguments);
		
		var entity = this.getBindingData(true), timestamp;
		if (entity) timestamp = entity.timestamp;
		if (timestamp != this._timestamp) {
			(this._renderer || $singleton(dorado.widget.DataLabelRenderer)).render(dom, {
				entity: entity,
				property: this._property
			});
			this._timestamp = timestamp;
		}
	}
});

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @component Form
 * @class 超链接。
 * @extends dorado.widget.Label
 */
dorado.widget.Link = $extend(dorado.widget.Label, /** @scope dorado.widget.Link.prototype */ {
	$className: "dorado.widget.Link",
	
	ATTRIBUTES: /** @scope dorado.widget.Link.prototype */ {
		className: {
			defaultValue: "d-link"
		},
		
		/**
		 * 目标地址。
		 * @type String
		 * @attribute
		 */
		href: {
			setter: function(v) {
				this._href = v;
				if (!this._text) this._text = v;
			}
		},
		
		/**
		 * 目标框架。
		 * @type String
		 * @attribute
		 */
		target: {}
	},
	
	EVENTS: /** @scope dorado.widget.Link.prototype */ {
	
		/**
		 * 当控件被点击时触发的事件。
		 * @param {Object} self 事件的发起者，即组件本身。
		 * @param {Object} arg 事件参数。
		 * @param {int} arg.button 表示用户按下的是哪个按钮，具体请参考DHTML的相关文档。
		 * @param {Event} arg.event DHTML中的事件event参数。
		 * @param {boolean} #arg.processDefault=false 是否继续执行超链接默认的跳转动作。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
		onClick: {
			interceptor: function(superFire, self, arg) {
				if (this.getListenerCount("onClick") > 0) {
					arg.processDefault == false;
					superFire(self, arg);
					return !!arg.processDefault;
				}
			}
		}
	},
	
	createDom: function() {
		return document.createElement("A");
	},
	
	refreshDom: function(dom) {
		$invokeSuper.call(this, arguments);
		dom.href = this._href;
		dom.target = this._target || "_self";
	}
});
