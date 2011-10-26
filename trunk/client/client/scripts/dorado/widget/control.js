(function() {

	var lastMouseDownTarget, lastMouseDownTimestamp = 0;

	/**
	 * @author Benny Bao (mailto:benny.bao@bstek.com)
	 * @component General
	 * @class 基本控件。 控件是指可用于在屏幕上显示的组件，例如：按钮、表格等。
	 * @extends dorado.widget.Component
	 * @extends dorado.RenderableElement
	 * @extends dorado.Draggable
	 * @extends dorado.Droppable
	 */
	dorado.widget.Control = $extend([dorado.widget.Component, dorado.RenderableElement, dorado.Draggable, dorado.Droppable], /** @scope dorado.widget.Control.prototype */
	{
		$className : "dorado.widget.Control",

		_ignoreRefresh : 1,
		_actualVisible : true,
		_parentActualVisible : true,

		/**
		 * @protected
		 * @type boolean
		 * @default true
		 * @description 用于指示该控件的DOM对象是否只能在非游离状态下才能被正确的刷新（渲染）。默认值为true。
		 * <p>
		 * 对于那些未通过appendChild操作添加中网页document.body中的DOM对象，我们称之为游离状态的对象。
		 * </p>
		 */
		renderUtilAttached : true,

		/**
		 * @protected
		 * @type boolean
		 * @description 用于指示该种控件是否支持获得控制焦点。
		 */
		focusable : false,

		/**
		 * @protected
		 * @type boolean
		 * @default true
		 * @description 该控件（包含其中的子控件）是否允许被鼠标选中。
		 */
		selectable : true,

		/**
		 * @name dorado.widget.Control#doOnFocus
		 * @function
		 * @protected
		 * @description 当控件获得控制焦点时被激活的内部方法。
		 */
		/**
		 * @name dorado.widget.Control#doOnBlur
		 * @function
		 * @protected
		 * @description 当控件失去控制焦点时被激活的内部方法。
		 */
		/**
		 * @name dorado.widget.Control#doOnAttachToDocument
		 * @function
		 * @protected
		 * @description 当控件所对应的HTML元素被真正的添加(相当于appendChild)到HTML的dom树中时激活的方法。
		 * @see dorado.widget.Control#onAttachToDocument
		 */
		/**
		 * @name dorado.widget.Control#doOnDetachFromDocument
		 * @function
		 * @protected
		 * @description 当控件所对应的HTML元素被从(相当于removeChild)到HTML的dom树中分离时激活的方法。
		 * @see dorado.widget.Control.prototype#onDetachFromDocument
		 */
		// =====

		ATTRIBUTES : /** @scope dorado.widget.Control.prototype */
		{

			/**
			 * 宽度。
			 * @type int|String
			 * @attribute
			 */
			width : {
				setter : function(v) {
					this._width = isFinite(v) ? parseInt(v) : v;
					delete this._realWidth;
					this._fixedWidth = !( typeof v == "string" && v.match('%'));
				}
			},

			/**
			 * 高度。
			 * @type int|String
			 * @attribute
			 */
			height : {
				setter : function(v) {
					this._height = isFinite(v) ? parseInt(v) : v;
					delete this._realHeight;
					this._fixedHeight = !( typeof v == "string" && v.match('%'));
				}
			},

			/**
			 * 用于指定该控件在创建后自动渲染到指定到网页中的什么位置中的属性。最终控件将被渲染目标位置对象的内部。
			 * <p>
			 * 该属性的内容是CSS Selector的表达式（用于确定渲染的目标位置）或直接传入目标位置的DOM对象。
			 * </p>
			 * <p>
			 * 注意：此属性的值只能在控件的构造参数中指定，当控件创建完成后再通过set方法修改将是没有任何意义的。
			 * </p>
			 * <p>
			 * 注意：如果同时定义了控件的renderTo和renderOn属性，那么系统将会忽略renderOn中的值。
			 * </p>
			 * @type String|HTMLElement
			 * @attribute writeOnce,writeBeforeReady
			 * @see dorado.widget.Control#attribute:renderOn
			 *
			 * @example // 指定此按钮自动渲染到id为divButtonHolder的DOM元素中。
			 * new dorado.widget.Button({
			 * 	caption: "Test Button",
			 * 	renderTo: "#divButtonHolder"
			 * });
			 */
			renderTo : {
				writeOnce : true,
				writeBeforeReady : true
			},

			/**
			 * 用于指定该控件在创建后自动渲染到指定到网页中的什么位置中的属性。最终控件将直接替换掉目标位置对象。
			 * <p>
			 * 该属性的内容是CSS Selector的表达式（用于确定渲染的目标位置）或直接传入目标位置的DOM对象。
			 * </p>
			 * <p>
			 * 注意：此属性的值只能在控件的构造参数中指定，当控件创建完成后再通过set方法修改将是没有任何意义的。
			 * </p>
			 * <p>
			 * 注意：如果同时定义了控件的renderTo和renderOn属性，那么系统将会忽略renderOn中的值。
			 * </p>
			 * @type String|HTMLElement
			 * @attribute writeOnce,writeBeforeReady
			 * @see dorado.widget.Control#attribute:renderTo
			 *
			 * @example // 指定此按钮自动渲染并替换掉id为myButton的DOM元素。
			 * new dorado.widget.Button({
			 * 	caption: "Test Button",
			 * 	renderOn: "#myButton"
			 * });
			 */
			renderOn : {
				writeOnce : true,
				writeBeforeReady : true
			},

			/**
			 * 是否可见。
			 * @type boolean
			 * @attribute
			 * @default true
			 */
			visible : {
				defaultValue : true,
				setter : function(visible) {
					if(this._visible != visible) {
						this._visible = visible;
						this.onActualVisibleChange();
					}
				}
			},

			/**
			 * 用于表述此控件当前是否真实可见。
			 * <p>
			 * 除控件的visible属性可以改变本控件的可见状态之外，父控件被隐藏、面板收起、窗口最小化等原因都可能造成某控件实际处于不可见状态。
			 * 因此visible为true的控件，其actualVisible属性未必也返回true。
			 * </p>
			 * @type boolean
			 * @attribute readOnly
			 */
			actualVisible : {
				readOnly : true,
				getter : function() {
					return this.isActualVisible() && this._attached && this._rendered;
				}
			},

			/**
			 * 隐藏模式，表示当用户将该控件的visible属性设置为false时，系统应通过怎样的方式隐藏它。取值范围如下：
			 * <ul>
			 * <li>visibility	-	仅将此控件的设置为不可见，它仍将占用布局控件。</li>
			 * <li>display	-	将此控件从布局中抹去，且其他控件可以占用它空出的位置。</li>
			 * </ul>
			 * @type boolean
			 * @default "visibility"
			 */
			hideMode : {
				defaultValue : "visibility"
			},

			/**
			 * 指示控件所对应的HTML元素被真正的添加(相当于appendChild)到HTML的dom树中。
			 * @type boolean
			 * @attribute readOnly
			 */
			attached : {
				readOnly : true
			},

			/**
			 * 指示控件当前是否拥有控制焦点
			 * @type boolean
			 * @attribute readOnly
			 */
			focused : {
				readOnly : true
			},

			/**
			 * 用表示控件在处理控制焦点是的上下级关系。
			 * <p>
			 * 在通常情况下，控件的focusParent与parent是一致的，只有在个别情况下例外。
			 * 例如：我们创建了一个TextEditor控件使其作为Grid控件的单元格编辑器， 由于Grid控件并不是一个{@link dorado.widget.Container}的子类，因此TextEditor的parent不可能是Grid。
			 * 然后TextEditor与Grid又确实具有上下级的关系，尤其是在处理控制焦点时，此时系统会将TextEditor的focusParent属性设置为Grid。
			 * </p>
			 * @type dorado.widget.Control
			 * @attribute readOnly
			 */
			focusParent : {
				skipRefresh : true,
				readOnly : true,
				getter : function() {
					return this._focusParent || this._parent;
				}
			},

			/**
			 * 提示信息。
			 * @type String
			 * @attribute
			 */
			tip : {},

			/**
			 * 控件使用的布局条件。
			 * <p>
			 * 此属性只会在控件被添加到布局管理器后才会产生实际效果。<br>
			 * 除了通过此方法为控件定义布局条件之外，也可以直接通过{@link dorado.widget.Container#addChild}方法的layoutConstraint参数。
			 * addChild方法会自动将传入的layoutConstaint参数的值（如果确实定义了该参数值的话）自动赋值给相应控件的layoutConstaint属性。
			 * </p>
			 * @type Object
			 * @attribute
			 * @see dorado.widget.Control#attribute:lc
			 * @see dorado.widget.Container#addChild
			 */
			layoutConstraint: {
				setter: function(layoutConstraint) {
					if (this._layoutConstraint != layoutConstraint) {
						this._layoutConstraint = layoutConstraint;
						if (this._layoutConstraint == dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT || layoutConstraint == dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT) {
							this.onActualVisibleChange();
						}
						if (this._rendered && this._parent && this._parent._layout) {
							this._parent._layout.refreshControl(this);
						}
					}
				}
			},

			view : {
				skipRefresh : true,
				setter : function(view) {
					if(this._view == view) return;
					$invokeSuper.call(this, [view]);
					if(this._innerControls) {
						jQuery.each(this._innerControls, function(i, control) {
							control.set("view", (this._view) ? null : view);
						});
					}
				}
			}
		},

		EVENTS : /** @scope dorado.widget.Control.prototype */
		{

			/**
			 * 当组件对应的根DOM对象被创建时触发的事件。
			 * @param {Object} self 事件的发起者，即组件本身。
			 * @param {Object} arg 事件参数。
			 * @param {HTMLElement} arg.dom 组件对应的根DOM对象。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			onCreateDom : {},

			/**
			 * 当组件将要刷新其对应的DOM对象之前触发的事件。
			 * @param {Object} self 事件的发起者，即组件本身。
			 * @param {Object} arg 事件参数。
			 * @param {HTMLElement} arg.dom 组件对应的根DOM对象。
			 * @param {boolean} #arg.processDefault=false 是否要继续系统默认的刷新操作。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			beforeRefreshDom : {},

			/**
			 * 当组件刷新其对应的DOM对象时触发的事件。
			 * <p>
			 * 在实际的使用过程中，{@link dorado.widget.Control#event:beforeRefreshDom}可能是更加常用的事件。
			 * </P>
			 * @param {Object} self 事件的发起者，即组件本身。
			 * @param {Object} arg 事件参数。
			 * @param {HTMLElement} arg.dom 组件对应的根DOM对象。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 * @see dorado.widget.Control#event:beforeRefreshDom
			 */
			onRefreshDom : {},

			/**
			 * 当控件被点击时触发的事件。
			 * @param {Object} self 事件的发起者，即组件本身。
			 * @param {Object} arg 事件参数。
			 * @param {int} arg.button 表示用户按下的是哪个按钮，具体请参考DHTML的相关文档。
			 * @param {Event} arg.event DHTML中的事件event参数。
			 * @param {boolean} #arg.returnValue 表示是否要终止该鼠标事件的冒泡处理机制。
			 * 如果返回false相当于调用了系统event的preventDefault()和stopPropagation()方法。
			 * 不定义此参数表示交由系统自行判断。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			onClick : {},

			/**
			 * 当控件被双击时触发的事件。
			 * @param {Object} self 事件的发起者，即组件本身。
			 * @param {Object} arg 事件参数。
			 * @param {int} arg.button 表示用户按下的是哪个按钮，具体请参考DHTML的相关文档。
			 * @param {Event} arg.event DHTML中的事件event参数。
			 * @param {boolean} #arg.returnValue 表示是否要终止该鼠标事件的冒泡处理机制。
			 * 如果返回false相当于调用了系统event的preventDefault()和stopPropagation()方法。
			 * 不定义此参数表示交由系统自行判断。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			onDoubleClick : {},

			/**
			 * 当鼠标在控件中按下时触发的事件。
			 * @param {Object} self 事件的发起者，即组件本身。
			 * @param {Object} arg 事件参数。
			 * @param {int} arg.button 表示用户按下的是哪个按钮，具体请参考DHTML的相关文档。
			 * @param {Event} arg.event DHTML中的事件event参数。
			 * @param {boolean} #arg.returnValue 表示是否要终止该鼠标事件的冒泡处理机制。
			 * 如果返回false相当于调用了系统event的preventDefault()和stopPropagation()方法。
			 * 不定义此参数表示交由系统自行判断。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			onMouseDown : {},

			/**
			 * 当鼠标在控件中抬起时触发的事件。
			 * @param {Object} self 事件的发起者，即组件本身。
			 * @param {Object} arg 事件参数。
			 * @param {int} arg.button 表示用户按下的是哪个按钮，具体请参考DHTML的相关文档。
			 * @param {Event} arg.event DHTML中的事件event参数。
			 * @param {boolean} #arg.returnValue 表示是否要终止该鼠标事件的冒泡处理机制。
			 * 如果返回false相当于调用了系统event的preventDefault()和stopPropagation()方法。
			 * 不定义此参数表示交由系统自行判断。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			onMouseUp : {},

			/**
			 * 当控件中的系统上下文菜单将要显示时触发的事件。
			 * @param {Object} self 事件的发起者，即组件本身。
			 * @param {Object} arg 事件参数。
			 * @param {Event} arg.event DHTML中的事件event参数。
			 * @param {boolean} #arg.processDefault=false
			 * 是否要继续系统的默认操作，让系统上下文菜单显示出来。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			onContextMenu : {},

			/**
			 * 当控件获得控制焦点时触发的事件。
			 * @param {Object} self 事件的发起者，即组件本身。
			 * @param {Object} arg 事件参数。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			onFocus : {},

			/**
			 * 当控件失去控制焦点时触发的事件。
			 * @param {Object} self 事件的发起者，即组件本身。
			 * @param {Object} arg 事件参数。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			onBlur : {},

			/**
			 * 当控件拥有焦点并且有按键被按下时触发的事件。
			 * @param {Object} self 事件的发起者，即控件本身。
			 * @param {Object} arg 事件参数。
			 * @param {int} arg.keyCode 按键对应的keyCode。
			 * @param {boolean} arg.shift Shift键是否按下。
			 * @param {boolean} arg.ctrl Ctrl键是否按下。
			 * @param {boolean} arg.alt Alt键是否按下。
			 * @param {Event} arg.event DHTML中的事件event参数。
			 * @patam {Boolean} #arg.returnValue=undefined 用于通知系统如何进行下一步处理的返回值。<br>
			 * 有下列三种可能结果:
			 * <ul>
			 * <li>true - 表示允许将此键盘消息进一步通知给上层控件（指此控件的focusParent属性指向的控件）。</li>
			 * <li>false - 表示禁止将此键盘消息进一步通知给上层控件，同时终止浏览器对此键盘操作的默认响应处理。</li>
			 * <li>不定义返回值 - 表示禁止将此键盘消息进一步通知给上层控件。</li>
			 * </ul>
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			onKeyDown : {},

			/**
			 * 当控件拥有焦点并且有ASCII码的按键被按下时触发的事件。
			 * @param {Object} self 事件的发起者，即控件本身。
			 * @param {Object} arg 事件参数。
			 * @param {int} arg.keyCode 按键对应的keyCode。
			 * @param {boolean} arg.shift Shift键是否按下。
			 * @param {boolean} arg.ctrl Ctrl键是否按下。
			 * @param {boolean} arg.alt Alt键是否按下。
			 * @param {Event} arg.event DHTML中的事件event参数。
			 * @patam {Boolean} #arg.returnValue=undefined 用于通知系统如何进行下一步处理的返回值。<br>
			 * 有下列三种可能结果:
			 * <ul>
			 * <li>true - 表示允许将此键盘消息进一步通知给上层控件（指此控件的focusParent属性指向的控件）。</li>
			 * <li>false - 表示禁止将此键盘消息进一步通知给上层控件，同时终止浏览器对此键盘操作的默认响应处理。</li>
			 * <li>不定义返回值 - 表示禁止将此键盘消息进一步通知给上层控件。</li>
			 * </ul>
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			onKeyPress : {}
		},

		constructor : function(config) {
			dorado.widget.Component.prototype.constructor.call(this, config);
			var renderTo = this._renderTo, renderOn = this._renderOn;
			if(renderTo || renderOn) {
				$setTimeout(this, function() {
					var container = renderTo || renderOn;
					if( typeof container == "string") container = jQuery(container)[0];
					if(container && container.nodeType) {(renderTo) ? this.render(container) : this.replace(container);
					}
				}, 0);
			}
		},
		
		onReady : function() {
			if(this._innerControls) {
				jQuery.each(this._innerControls, function(i, control) {
					if(!( control instanceof dorado.widget.Control) && !control._ready)
						control.onReady();
				});
			}
			$invokeSuper.call(this);
		},
		
		destroy : function() {
			if(this._innerControls) {
				jQuery.each(this._innerControls.slice(0, -1), function(i, control) {
					if(!control._destroyed)
						control.destroy();
				});
				delete this._innerControls;
			}

			var isClosed = (window.closed || dorado.windowClosed);
			if(!isClosed && this._parent) {
				if(this._isInnerControl) this._parent.unregisterInnerControl(this);
				else this._parent.removeChild(this);
			}

			var dom = this._dom;
			if(dom) {
				try {
					dom.doradoUniqueId = null;
				} catch (e) {
					// do nothing
				}
				if(!isClosed) $fly(dom).remove();
			}
			$invokeSuper.call(this);
		},
		
		doSet : function(attr, value, skipUnknownAttribute, lockWritingTimes) {
			$invokeSuper.call(this, [attr, value, skipUnknownAttribute, lockWritingTimes]);
			if(!this._rendered) return;
			var def = this.ATTRIBUTES[attr];
			if(!this._duringRefreshDom && (this._visible || attr == "visible") && this._ignoreRefresh < 1 && def && !def.skipRefresh) {
				dorado.Toolkits.setDelayedAction(this, "$refreshDelayTimerId", this.refresh, 50);
			}
		},
		
		setActualVisible : function(actualVisible) {
			if(this._actualVisible != actualVisible) {
				var oldVisible = this.isActualVisible();
				this._actualVisible = actualVisible;
				if(oldVisible != this.isActualVisible()) {
					this.onActualVisibleChange();
				}
			}
		},
		
		isActualVisible : function() {
			return this._parentActualVisible && this._actualVisible && this._visible && this._layoutConstraint != dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT;
		},
		
		onActualVisibleChange : function() {

			function notifyChildren(control, actualVisible) {
				if(control._innerControls) {
					jQuery.each(control._innerControls, function(i, child) {
						if(child._parentActualVisible == actualVisible) return;
						child._parentActualVisible = actualVisible;
						child.onActualVisibleChange();
					});
				}
			}

			var actualVisible = this.isActualVisible();
			if(actualVisible) {
				this._skipResize = this._shouldResizeOnVisible;
				if(this._shouldRefreshOnVisible) this.refresh();
				this._skipResize = false;
				if(this._shouldResizeOnVisible) this.onResize();
			}

			notifyChildren(this, actualVisible);
		},
		
		refresh : function(delay) {
			if(this._duringRefreshDom || !this._rendered || (!this._attached && this.renderUtilAttached)) return;

			if(!this.isActualVisible() && !this._currentVisible) {
				this._shouldRefreshOnVisible = true;
				return;
			}
			this._shouldRefreshOnVisible = false;

			if(delay) {
				dorado.Toolkits.setDelayedAction(this, "$refreshDelayTimerId", function() {
					this._duringRefreshDom = true;
					try {
						dorado.Toolkits.cancelDelayedAction(this, "$refreshDelayTimerId");

						if(!this.isActualVisible()) {
							this._shouldRefreshOnVisible = true;
							return;
						}
						this._shouldRefreshOnVisible = false;

						var dom = this.getDom(), arg =  {
							dom: dom,
							processDefault: true
						};
						if (this.getListenerCount("beforeRefreshDom")) {
							arg.processDefault = false;
							this.fireEvent("beforeRefreshDom", this, arg);
						}
						if (arg.processDefault) {
							this.refreshDom(dom);
							this.fireEvent("onRefreshDom", this, arg);
						}
					} finally {
						this._duringRefreshDom = false;
					}
				}, 50);
			} else {
				this._duringRefreshDom = true;
				try {
					dorado.Toolkits.cancelDelayedAction(this, "$refreshDelayTimerId");
					
					var dom = this.getDom(), arg =  {
						dom: dom,
						processDefault: true
					};
					if (this.getListenerCount("beforeRefreshDom")) {
						arg.processDefault = false;
						this.fireEvent("beforeRefreshDom", this, arg);
					}
					if (arg.processDefault) {
						this.refreshDom(dom);
						this.fireEvent("onRefreshDom", this, arg);
					}
				} finally {
					this._duringRefreshDom = false;
				}
			}
		},
		
		/**
		 * 根据控件自身的属性设定来刷新DOM对象。
		 * <p>
		 * 此方法会在控件每一次被刷新时调用，因此那些设置DOM对象的颜色、字体、尺寸的代码适合放置在此方法中。
		 * </p>
		 * @param {HTMLElement} dom 控件对应的DOM对象。
		 */
		refreshDom : function(dom) {
			if(!this.selectable) $DomUtils.disableUserSelection(dom);

			try {
				dom.doradoUniqueId = this._uniqueId;

				if(this._currentVisible !== undefined) {
					if(this._currentVisible != this._visible) {
						if(this._hideMode == "display") {
							if(this._visible) {
								dom.style.display = this._oldDisplay;
								this.set("layoutConstraint", this._oldLayoutConstraint);
							} else {
								this._oldDisplay = dom.style.display;
								dom.style.display = "none";

								this._oldLayoutConstraint = this._layoutConstraint;
								this.set("layoutConstraint", dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT);
							}
						} else {
							dom.style.visibility = (this._visible) ? '' : "hidden";
						}
					}
				} else {
					if(!this._visible) {
						if(this._hideMode == "display") {
							this._oldDisplay = dom.style.display;
							dom.style.display = "none";

							this._oldLayoutConstraint = this._layoutConstraint;
							// 在Container的layout.addControl中已处理
						} else {
							dom.style.visibility = "hidden";
						}
					}
				}

				if(this._tip) {
					this._currentTip = this._tip;
					dorado.TipManager.initTip(dom, {
						text : this._tip
					});
				} else if(this._currentTip) {
					dorado.TipManager.deleteTip(dom);
				}
			} catch (e) {
				// do nothing
			}

			this.applyDraggable(dom);
			this.applyDroppable(dom);
			$invokeSuper.call(this, [dom]);

			this._currentVisible = !!this._visible;
		},
		
		getRealWidth : function() {
			if(this._width == "none") return null;
			return (this._realWidth == null) ? this._width : this._realWidth;
		},
		
		getRealHeight : function() {
			if(this._height == "none") return null;
			return (this._realHeight == null) ? this._height : this._realHeight;
		},
		
		/**
		 * 重新设置控件DOM元素的尺寸。
		 * @protected
		 * @return {boolean} 返回在此次方法的调用过程中是否确实改变了控件的宽高设置。
		 */
		resetDimension : function() {
			if(this._skipResetDimension) return;
			var changed = $invokeSuper.call(this) || !this._fixedWidth || !this._fixedHeight;
			if((changed || !this._currentVisible) && this._visible) {
				this._skipResetDimension = true;
				this.onResize();
				this._skipResetDimension = false;
			}
			return changed;
		},
		
		/**
		 * 向控件所在的布局管理器通知本控件的尺寸已经发生了变化。
		 * @protected
		 */
		notifySizeChange : function() {
			if(!this._parent || !this._rendered) return;
			var layout = this._parent._layout;
			if(layout && layout._attached && layout.onControlSizeChange) {
				dorado.Toolkits.setDelayedAction(this, "$notifySizeChangeTimerId", function() {
					layout.onControlSizeChange(this);
				}, 200);
			}
		},
		
		/**
		 * 返回控件对应的DOM对象。
		 * @return {HTMLElement} 控件对应的DOM对象。
		 */
		getDom : function() {
			if(!this._dom) {
				var dom = this._dom = this.createDom();
				
				var className = (this._inherentClassName) ? this._inherentClassName : "";
				if (this._className) className += (" " + this._className);
				if (className) dom.className = className;
				if (this.focusable) dom.tabIndex = 1;

				var self = this;
				$fly(this._dom).mousedown(function(evt) {
					if (!self._eventBinded) {
						self._eventBinded = true;
						
						jQuery(this).click(function(evt) {
							if (!self.processDefaultMouseListener()) return;
							var defaultReturnValue;
							if (self.onClick) {
								defaultReturnValue = self.onClick(evt);
							}
							var arg = {
								button: evt.button,
								event: evt,
								returnValue: defaultReturnValue
							}
							self.fireEvent("onClick", self, arg);
							return arg.returnValue;
						}).bind("dblclick", function(evt) {
							if (!self.processDefaultMouseListener()) return;
							var defaultReturnValue;
							if (self.onDoubleClick) {
								defaultReturnValue = self.onDoubleClick(evt);
							}
							var arg = {
								button: evt.button,
								event: evt,
								returnValue: defaultReturnValue
							}
							self.fireEvent("onDoubleClick", self, arg);
							return arg.returnValue;
						}).mouseup(function(evt) {
							if (!self.processDefaultMouseListener()) return;
							var defaultReturnValue;
							if (self.onMouseUp) {
								defaultReturnValue = self.onMouseUp(evt);
							}
							var arg = {
								button: evt.button,
								event: evt,
								returnValue: defaultReturnValue
							}
							self.fireEvent("onMouseUp", self, arg);
							return arg.returnValue;
						}).bind("contextmenu", function(evt) {
							evt = jQuery.event.fix(evt || window.event);
							var eventArg = {
								event: evt,
								processDefault: true
							};
							if (self.getListenerCount("onContextMenu")) {
								eventArg.processDefault = false;
								self.fireEvent("onContextMenu", self, eventArg);
							}
							if (!eventArg.processDefault) {
								evt.preventDefault();
								evt.returnValue = false;
								return false;
							}
						});
					}
					
					if(evt.srcElement != lastMouseDownTarget || (new Date() - lastMouseDownTimestamp) > 500) {
						dorado.widget.setFocusedControl(self);
						lastMouseDownTarget = evt.srcElement;
						lastMouseDownTimestamp = new Date();
					}
					if(!self.processDefaultMouseListener()) return;
					var defaultReturnValue;
					if (self.onMouseDown) {
						defaultReturnValue = self.onMouseDown(evt);
					}
					var arg = {
						button : evt.button,
						event : evt,
						returnValue: defaultReturnValue
					}
					self.fireEvent("onMouseDown", self, arg);
					return arg.returnValue;
				});
				
				if (this.getListenerCount("onCreateDom")) {
					this.fireEvent("onCreateDom", this, {
						dom: dom
					});
				}
			}
			return this._dom;
		},
		
		processDefaultMouseListener : function() {
			return !this._disabled;
		},
		
		doRenderToOrReplace : function(replace, element, nextChildElement) {
			var renderTarget = this._renderTo || this._renderOn;

			$invokeSuper.call(this, [replace, element, nextChildElement]);

			var dom = this._dom;
			if(!dom) return;

			var attached = false;
			if(!renderTarget && this._parent && this._parent != dorado.widget.View.TOP) {
				attached = this._parent._attached;
			} else {
				var body = dom.ownerDocument.body;
				var node = dom.parentNode;
				while(node) {
					if(node == body) {
						attached = true;
						break;
					}
					node = node.parentNode;
				}
			}

			if(attached) {
				this.onAttachToDocument();
			} else if(this._attached) {
				this.onDetachFromDocument();
			}
			
			if(!this._ready) {
				this.onReady();
				if (this._view && !this._view._ready) {
					this._view.onReady();
				}
			}
		},
		
		/**
		 * 当控件所对应的HTML元素被真正的添加(相当于appendChild)到HTML的dom树中时激活的方法。
		 * <p>
		 * 当控件的createDom()方法执行时，控件所对应的HTML元素处于游离状态。此时我们调用HTML元素的offsetWidth、currentStyle属性都是无效的。
		 * 只有当该HTML元素最终被添加到HTML的dom树后，这些属性才会生效。
		 * </p>
		 * <p>
		 * 当HTML元素通过appendChild方法被添加为某个父HTML元素中后，并不一定意味着游离状态的结束。
		 * 如果父HTML元素是游离状态的，那么其中的所有各层子元素都是游离状态的。
		 * 同理，当父HTML元素最终被添加到HTML的dom树后，其中的所有各层子元素都将结束游离状态。
		 * </p>
		 * <p>
		 * 如无特殊需要，一般请不要复写此方法，而应复写doOnAttachToDocument。
		 * </p>
		 * @protected
		 * @see dorado.widget.Control#doOnDetachFromDocument
		 */
		onAttachToDocument : function() {
			if(this._rendered && !this._attached) {
				var dom = this.getDom();
				this._attached = true;
				this._ignoreRefresh--;
				if(this.renderUtilAttached) this.refreshDom(dom);
				if(this.doOnAttachToDocument) this.doOnAttachToDocument();

				if(this._innerControls) {
					jQuery.each(this._innerControls, function(i, control) {
						control.onAttachToDocument();
					});
				}
			}
		},
		
		/**
		 * 当控件所对应的HTML元素被从(相当于removeChild)到HTML的dom树中分离时激活的方法。
		 * <p>
		 * 如无特殊需要，一般请不要复写此方法，而应复写doOnDetachFromDocument。
		 * </p>
		 * @protected
		 * @see dorado.widget.Control#onAttachToDocument
		 * @see dorado.widget.Control#doOnDetachFromDocument
		 */
		onDetachFromDocument : function() {
			if(this._rendered && this._attached) {
				this._attached = false;
				this._ignoreRefresh++;
				if(this.doOnDetachFromDocument) this.doOnDetachFromDocument();

				if(this._innerControls) {
					jQuery.each(this._innerControls, function(i, control) {
						control.onDetachFromDocument();
					});
				}
			}
		},
		
		/**
		 * 将一个给定控件注册为本控件的内部控件。
		 * <p>
		 * 内部控件与{@link dorado.widget.Container}中的子控件是不同的概念。
		 * 内部控件是父控件自身的组成部分，例如RadioGroup控件本身就是由其中的一到多个RadioButton构成的。
		 * </p>
		 * <p>
		 * registerInnerControl方法一般也不应该被外部程序调用，而是应该有控件自身的代码在createDom或refreshDom等方法中调用。
		 * 利用registerInnerControl方法注册内部控件的目的是为了正确向这些控件传播系统消息，管理这些控件的状态。<br>
		 * 完成注册后的内部控件的parent和focusParent属性都将指向本控件。
		 * </p>
		 * @param {dorado.widget.Control} control 内部控件。
		 * @protected
		 */
		registerInnerControl : function(control) {
			if(!this._innerControls) this._innerControls = [];
			this._innerControls.push(control);
			if(this._attached) 	control.onAttachToDocument();
			control._isInnerControl = true;
			control._parent = control._focusParent = this;
			control.set("view", (this instanceof dorado.widget.View) ? this : this.get("view"));

			if(control.parentChanged) control.parentChanged();
		},
		
		/**
		 * 注销一个给定的内部控件。
		 * @param {dorado.widget.Control} control 内部控件。
		 * @protected
		 */
		unregisterInnerControl : function(control) {
			if(!this._innerControls) return;
			control.onDetachFromDocument();
			this._innerControls.remove(control);
			control._parent = control._focusParent = null;
			control.set("view", null);
			delete control._isInnerControl;

			if(control.parentChanged) control.parentChanged();
		},
		
		parentChanged : function() {
			var parent = this._parent;
			var parentActualVisible = (parent) ? parent.isActualVisible() : true;
			if(parentActualVisible != this._parentActualVisible) {
				this.onActualVisibleChange();
			}
		},
		
		/**
		 * @name dorado.widget.Control#onResize
		 * @function
		 * @protected
		 * @description 当控件自身的尺寸发生改变时激活的方法。
		 * <p>
		 * 例如：对于容器类控件而言，当其自身的尺寸发生该百年时，我们往往需要对容器内部的布局和子控件进行相应的处理，
		 * 以使这些对象能够正确的使用尺寸的改变。此时，我们可以将这些处理逻辑放置在onResize方法中。
		 * </p>
		 */
		onResize : function() {
			if(this._skipResize) return;

			if(!this.isActualVisible()) {
				this._shouldResizeOnVisible = true;
				return;
			}
			this._shouldResizeOnVisible = false;

			if(this.doOnResize) this.doOnResize.apply(this, arguments);
		},
		
		/**
		 * 根据给定的类型需要最靠近此控件的父控件。
		 * @param {Prototype} type 要需要的父控件的类型。
		 * @return {dorado.widget.Container} 找到的父控件。
		 *
		 * @example
		 * // 寻找某按钮所隶属的Dialog控件。
		 * var dialog = button1.findParent(dorado.widget.Dialog);
		 */
		findParent : function(type) {
			var parent = this._parent;
			while(parent) {
				if( parent instanceof type)
					return parent;
				parent = parent._parent;
			}
			return null;
		},
		
		/**
		 * 返回可聚焦的子控件的集合。
		 * @return {[dorado.widget.Control]} 可聚焦的子控件的集合。
		 */
		getFocusableSubControls: dorado._NULL_FUNCTION,
				
		/**
		 * 返回控件当前是否可以接收控制焦点成为当前控件。
		 * @param {boolean} [deep] 是否要分析控件各层父控件已确定该控件自身当前是否可以获得焦点。默认为false。
		 * 例如：当某个Panel对象被设置为不可用时，那么该Panel中所包含所有控件都将无法接受控制焦点。
		 * @return {boolean} 是否可以接收控制焦点。
		 */
		isFocusable : function(deep) {
			if (!this.focusable || !this._rendered || !this.isActualVisible() || !this.getDom() || this._disabled) {
				return false;
			}
			var dom = this.getDom();
			if (dom.disabled || dom.offsetWidth <= 0) {
				return false;
			}
			
			if(deep) {
				var child = this, parent = child._parent;
				while (parent && parent != $topView) {
					if (!parent._rendered) {
						break;
					}
					if (!parent.isFocusable()) {
						var focusableSubControls = parent.getFocusableSubControls();
						return (focusableSubControls) ? (focusableSubControls.indexOf(child) >= 0) : false;
					}
					child = parent;
					parent = child._parent;
				}
			}
			return true;
		},
		
		/**
		 * 使控件获得控制焦点。
		 */
		setFocus : function() {
			var dom = this._dom;
			if (dom) {
				setTimeout(function() {
					try {
						dom.focus();
					} catch (e) {
					}
				}, 0);
			}
		},
		
		/**
		 * 当控件获得控制焦点时被激活的方法。
		 * @protected
		 */
		onFocus : function() {
			this._focused = true;
			if(this.doOnFocus) this.doOnFocus();
			if(this._className) $fly(this.getDom()).addClass(this._className + "-focused");
			this.fireEvent("onFocus", this);
		},
		
		/**
		 * 当控件失去控制焦点时被激活的方法。
		 * @protected
		 */
		onBlur : function() {
			this._focused = false;
			if(this.doOnBlur) this.doOnBlur();
			$fly(this.getDom()).removeClass(this._className + "-focused");
			this.fireEvent("onBlur", this);
		},
		
		/**
		 * 当控件拥有控制焦点，并且用户按下任何键盘键（包括系统按钮，如箭头键和功能键）时被激活的方法。
		 * @param {Event} evt DHTML事件中的Event对象。
		 * @return {boolean} 用于通知系统如何进行下一步处理的返回值。<br>
		 * 有下列三种可能结果:
		 * <ul>
		 * <li>true - 表示允许将此事件进一步通知给上层控件（指此控件的focusParent属性指向的控件）。</li>
		 * <li>false - 表示禁止将此事件进一步通知给上层控件，同时终止浏览器对此键盘操作的默认响应处理。</li>
		 * <li>不定义返回值 - 表示禁止将此事件进一步通知给上层控件，但不中断浏览器对此键盘操作的默认响应处理。</li>
		 * </ul>
		 * @protected
		 */
		onKeyDown : function(evt) {
			var b = true;
			if(this.getListenerCount("onKeyDown")) {
				var arg = {
					keyCode : evt.keyCode,
					shiftKey : evt.shiftKey,
					ctrlKey : evt.ctrlKey,
					altlKey : evt.altlKey,
					event : evt
				};
				this.fireEvent("onKeyDown", this, arg);
				b = arg.returnValue;
			}
			if(!b) return b;

			var b = this.doOnKeyDown ? this.doOnKeyDown(evt) : true;
			if(!b) return b;

			var fp = this.get("focusParent");
			if(fp && dorado.widget.disableKeyBubble != fp) b = fp.onKeyDown(evt);
			return b;
		},
		
		/**
		 * 当控件拥有控制焦点，并且用户按下并放开任何字母数字键时被激活的方法。
		 * @param {Event} evt DHTML事件中的Event对象。
		 * @return {boolean} 用于通知系统如何进行下一步处理的返回值。<br>
		 * 有下列三种可能结果:
		 * <ul>
		 * <li>true - 表示允许将此事件进一步通知给上层控件（指此控件的focusParent属性指向的控件）。</li>
		 * <li>false - 表示禁止将此事件进一步通知给上层控件，同时终止浏览器对此键盘操作的默认响应处理。</li>
		 * <li>不定义返回值 - 表示禁止将此事件进一步通知给上层控件，但不中断浏览器对此键盘操作的默认响应处理。</li>
		 * </ul>
		 * @protected
		 */
		onKeyPress : function(evt) {
			var b = true;
			if(this.getListenerCount("onKeyPress")) {
				var arg = {
					keyCode : evt.keyCode,
					shiftKey : evt.shiftKey,
					ctrlKey : evt.ctrlKey,
					altlKey : evt.altlKey,
					event : evt
				};
				this.fireEvent("onKeyPress", this, arg);
				b = arg.returnValue;
			}
			if(!b) return b;

			var b = this.doOnKeyPress ? this.doOnKeyPress(evt) : true;
			if(!b) return b;

			var fp = this.get("focusParent");
			if(fp && dorado.widget.disableKeyBubble != fp) b = fp.onKeyPress(evt);
			return b;
		},
		
		initDraggingInfo : function(draggingInfo, evt) {
			$invokeSuper.call(this, arguments);
			draggingInfo.set({
				object : this,
				sourceControl : this
			});
		},
		
		onDraggingSourceOver : function(draggingInfo, evt) {
			draggingInfo.set({
				targetObject : this,
				targetControl : this
			});
			return $invokeSuper.call(this, arguments);
		},
		
		onDraggingSourceOut : function(draggingInfo, evt) {
			var retval = $invokeSuper.call(this, arguments);
			draggingInfo.set({
				targetObject : null,
				targetControl : null
			});
			return retval;
		}
	});

	dorado.widget.disableKeyBubble = false;
	dorado.widget.focusedControl = [];

	dorado.widget.onControlGainedFocus = function(control) {
		if(dorado.widget.focusedControl && dorado.widget.focusedControl.peek() == control) return;
		var ov = dorado.widget.focusedControl;
		var nv = [];
		if(control) {
			var c = control;
			while(c) {
				nv.push(c);
				var focusParent = c.get("focusParent");
				if(!focusParent) break;
				c = focusParent;
			}
			nv = nv.reverse();
		}

		var i = ov.length - 1;
		for(; i >= 0; i--) {
			var o = ov[i];
			if(o == nv[i]) break;
			if(o.onBlur) o.onBlur();
		}

		dorado.widget.focusedControl = nv;
		i++;
		for(; i < nv.length; i++) {
			if(nv[i].onFocus) nv[i].onFocus();
		}
	};

	dorado.widget.onControlLostFocus = function(control) {
		if(!control.get("focused")) return;
		do {
			control = control.get("focusParent");
		} while (control && !control.isFocusable());
		dorado.widget.onControlGainedFocus(control);
	};

	dorado.widget.setFocusedControl = function(control) {
		if(dorado.widget.focusedControl && dorado.widget.focusedControl.peek() == control) return;
		while(control && !control.isFocusable()) {
			control = control.get("focusParent");
		}
		if(control) {
			control.setFocus();
			dorado.widget.onControlGainedFocus(control);
		} else {
			if(document.body)
				document.body.focus();
			dorado.widget.onControlGainedFocus(null);
		}
	};

	dorado.widget.getMainFocusedControl = function() {
		var v = dorado.widget.focusedControl;
		for(var i = v.length - 1; i >= 0; i--) {
			if(!v[i]._focusParent) return v[i];
		}
		return v[0];
	};

	dorado.widget.getFocusedControl = function() {
		var v = dorado.widget.focusedControl;
		return v.peek();
	};
	
	dorado.widget.findFocusableControlInElement = function(element) {

		function findInChildren(element) {
			var el = element.firstChild, control = null;
			while(el) {
				control = findInChildren(el);
				if(control) break;
				if(el.doradoUniqueId) {
					var c = dorado.widget.Component.ALL[el.doradoUniqueId];
					if(c && c.isFocusable()) {
						control = c;
						break;
					}
				}
				el = el.nextSibling;
			}
			return control;
		}

		return findInChildren(element);
	};
	
	function findFocusableControl(control, options) {
		var focusableControls, subControls = control.getFocusableSubControls();
		if (control.isFocusable()) {
			focusableControls = [control];
		}
		if (subControls && subControls.length){
			if (focusableControls) {
				focusableControls = focusableControls.concat(subControls);
			}
			else {
				focusableControls = subControls;
			}
		}
		
		var focusableControl = null;
		if (focusableControls) {
			var reverse = false, from = null;
			if (options) {
				reverse = options.reverse;
				from = options.from;
			}
			
			if (reverse) focusableControls.reverse();
			var start = 0;
			if (from) start = focusableControls.indexOf(from) + 1;
			
			for (var i = start; i < focusableControls.length; i++) {
				var c = focusableControls[i];
				if (c && c instanceof dorado.widget.Control) {
					if (c == control) {
						focusableControl = c;
					} else {
						focusableControl = findFocusableControl(c, {
							reverse: reverse
						});
					}
					if (focusableControl) break;
				}
			}
		}
		return focusableControl;
	};
	
	function findNext(from) {
		var control = null, parent = from._parent;
		if (parent) {
			control = findFocusableControl(parent, {
				from: from
			});
		}
		return control;
	};

	function findPrevious(from) {
		var control = null, parent = from._parent;
		if (parent) {
			control = findFocusableControl(parent, {
				from: from,
				reverse: true
			});
		}
		return control;
	};

	dorado.widget.findNextFocusableControl = function(from) {
		var from = from || dorado.widget.getFocusedControl();
		while (from) {
			var control = findNext(from);
			if (control) control = findFocusableControl(control);
			if (control) return control;
			from = from._parent;
		}
		return findFocusableControl($topView);
	};

	dorado.widget.findPreviousFocusableControl = function(control) {
		var from = from || dorado.widget.getFocusedControl();
		while (from) {
			var control = findPrevious(from);
			if (control) control = findFocusableControl(control, {
				reverse: true
			});
			if (control) return control;
			from = from._parent;
		}
		return findFocusableControl($topView, {
			reverse: true
		});
	};
	
	/**
	 * 根据给定的DOM对象查找其所属的Dorado控件。
	 * <p>
	 * 注意：此查找动作的范围是跨越浏览器的框架的，亦即可以通过此方法查找父页面（父框架）中的Dorado控件。
	 * </p>
	 * @param {HTMLElement} element 作为查找起点的DOM对象。
	 * @param {Prototype|String} [type] 要查找的目标控件的类型。可以是具体的Prototype或类名。
	 * 如果不指定参数则表示包含此DOM对象且距离此此DOM对象最近的Dorado控件。
	 * @return {dorado.widget.Control} Dorado控件。
	 *
	 * @example
	 * // 查找某Element所属的控件。
	 * var control = dorado.widget.findParentControl(div);
	 *
	 * @example
	 * // 查找某Element元素所属的Dialog控件。
	 * var dialog = dorado.widget.findParentControl(div, dorado.widget.Dialog);
	 */
	dorado.widget.findParentControl = function(element, type) {

		function find(win, dom, className) {
			var control = null;
			do {
				var control;
				if (dom.doradoUniqueId) {
					control = win.dorado.widget.Component.ALL[dom.doradoUniqueId];
				}
				if(control) {
					if(className) {
						if(control.constructor.className === className)
							break;
					} else {
						break;
					}
				}
				dom = dom.parentNode;
			} while (dom != null);

			if(!control && win.parent) {
				var parentFrames;
				try {
					parentFrames = win.parent.jQuery("iframe,frame");
				} catch (e) {
					// do nothing;
				}

				if(parentFrames) {
					var frame;
					parentFrames.each(function() {
						if(this.contentWindow == win) {	// IE8下使用===判断会失败
							frame = this;
							return false;
						}
					});
					if(frame) {
						control = find(win.parent, frame, className);
					}
				}
			}
			return control;
		}

		var className;
		if( type instanceof Function) {
			className = type.className;
		} else if(type) {
			className = type + '';
		}
		return find(window, element, className);
	}

})();
