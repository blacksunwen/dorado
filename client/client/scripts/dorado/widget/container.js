(function() {

	var fireParentChanged = true;
	
	/**
	 * @author Benny Bao (mailto:benny.bao@bstek.com)
	 * @component General
	 * @class 容器控件。
	 * @extends dorado.widget.Control
	 */
	dorado.widget.Container = $extend(dorado.widget.Control, /** @scope dorado.widget.Container.prototype */ {
		$className: "dorado.widget.Container",
		
		ATTRIBUTES: /** @scope dorado.widget.Container.prototype */ {
		
			className: {
				defaultValue: "d-container"
			},
			
			/**
			 * 容器控件使用的布局管理器。
			 * <p>
			 * 在设置布局管理器时，既可以向方法中传入布局管理器的实例，也可以传入当以JSON配置对象描述的布局管理器，设置是一个代表$type简写的字符串。
			 * 当以JSON配置对象描述的子控件时，可以使用子控件类型名称中"dorado.widget."和"Layout"之间的部分作为$type的简写。
			 * 例如以Dock作为dorado.widget.layout.DockLayout的简写。
			 * </p>
			 * <p>
			 * 绝大部分容器的默认布局管理器都是{@link dorado.widget.layout.DockLayout}。
			 * </p>
			 * @attribute
			 * @type dorado.widget.layout.Layout
			 */
			layout: {
				setter: function(layout) {
					var oldLayout = this._layout, controls;
					if (oldLayout) {
						oldLayout.disableRendering();
						oldLayout.set("container", null);
						
						controls = [];
						oldLayout._regions.each(function(region){
							controls.push(region.control);
						});
						oldLayout.removeAllControls();
						
						oldLayout.enableRendering();
						oldLayout.onDetachFromDocument();
					}
					
					if (layout && !(layout instanceof dorado.widget.layout.Layout)) {
						layout = dorado.Toolkits.createInstance("layout", layout, function(type) {
							type = type || "Dock";
							return dorado.util.Common.getClassType("dorado.widget.layout." + type + "Layout", true);
						});
					}
					this._layout = layout;
					if (layout) {
						layout.set("container", this);
						
						if (controls && controls.length) {
							layout.disableRendering();
							controls.each(function(control){
								layout.addControl(control);
							});
							layout.enableRendering();
						}
						if (this._attached && layout._regions.size == 0 && !layout._rendered) {
							layout.onAttachToDocument(this.getContentContainer());
						}
					}
				},
				getter: function() {
					if (this._layout === undefined) {
						this._ignoreRefresh++;
						try {
							this.createDefaultLayout();
						}
						finally {
							this._ignoreRefresh--;
						}
					}
					return this._layout;
				}
			},
			
			/**
			 * 此属性在读取和写入时具有不同的含义。
			 * <ul>
			 * <li>读取时返回的是容器控件中子组件的集合。返回的对象是{@link dorado.util.KeyedList}，该集合的键为组件的id，值为相应的组件。</li>
			 * <li>而写入时则是作为用于简化为容器添加子控件操作的虚拟属性。
			 * 写入的数值应该是一个数组，数组中的每一个元素是子控件的实例（{@link dorado.widget.Component}）或以JSON配置对象描述的子控件。</li>
			 * </ul>
			 * <p>
			 * 当以JSON配置对象描述的子控件时，可以使用子控件类型名称中"dorado.widget."后面的部分作为$type的简写。
			 * 例如以Button作为dorado.widget.Button的简写。请参考后面的示例。
			 * </p>
			 * @type dorado.util.KeyedList|Array
			 * @attribute
			 * @see dorado.Toolkits.createInstance
			 *
			 * @example
			 * var container = new dorado.widget.Container();
			 * container.set("children", [
			 * 	new dorado.widget.Panel(),	// 简单的向容器中添加一个子控件
			 *
			 * 	{
			 * 		$type: "Button",
			 * 		caption: "Test Button"
			 * 	},	// 简单的向容器中添加一个子控件，以JSON配置对象的方式
			 *
			 * 	new dorado.widget.TextEditor({
			 * 		layoutConstraint: { width: "100%" }
			 * 	}),	// 添加一个子控件的同时指定布局条件
			 *
			 * 	{
			 * 		$type: "TextEditor",
			 * 		required: true,
			 * 		layoutConstraint: { width: "100%" }
			 * 	}	// 以JSON配置对象的方式添加一个子控件的同时指定布局条件
			 * ]);
			 */
			children: {
				setter: function(children) {
					if (!children || children.length < 1) return;
					
					for (var i = 0; i < children.length; i++) {
						var child = children[i];
						if (child instanceof dorado.widget.Component) {
							this.addChild(child);
						} else if (child.$type) {
							this.addChild(dorado.Toolkits.createInstance("widget", child));
						}
					}
				}
			},

			/**
			 * 容器控件中的内容超出了以后的处理方法。
			 * <p>
			 * 该值默认为auto，可选值为visible, hidden, scroll, auto。<br />
			 * 该值的含义与CSS中overflow的属性的意义相同，在实现上也仅仅是把这个值添加到内容容器上。
			 * </p>
			 * @attribute skipRefresh
			 * @default "auto"
			 * @type String
			 */
			contentOverflow: {
				skipRefresh: true,
				defaultValue: "auto",
				setter: function(value) {
					var container = this;
					if (container._rendered) {
						var contentContainer = container.getContentContainer();
						if (contentContainer) {
							contentContainer.style.overflow = value;
						}
					}
					container._contentOverflow = value;
				}
			},
			
			view: {
				setter: function(view) {
					if (this._view == view) return;
					$invokeSuper.call(this, arguments);
					this._children.each(function(child) {
						if (this._view) child.set("view", null);
						child.set("view", view);
					}, this);
				}
			},
			
			containerDom: {
				readOnly: true,
				getter: function() {
					return this._container;
				}
			}
		},
		
		constructor: function(config) {
			this._children = new dorado.util.KeyedList(dorado._GET_ID);
			
			var childrenConfig;
			if (config && config.children) {
				childrenConfig = config.children;
				delete config.children;
			}
			
			$invokeSuper.call(this, arguments);
			
			if (childrenConfig) {
				config.children = childrenConfig;
				this.set("children", childrenConfig);
			}
		},
		
		createDefaultLayout: function() {
			this.set("layout", new dorado.widget.layout.DockLayout());
		},
		
		onReady: function() {
			this._children.each(function(child) {
				if (!(child instanceof dorado.widget.Control) && !child._ready) child.onReady();

				if (dorado.Object.isInstanceOf(child, dorado.widget.FloatControl) && !child._ready && child._floating && child._visible) {
					child.show();
				}
			});
			$invokeSuper.call(this, arguments);
		},
		
		destroy: function() {
			jQuery.each(this._children.toArray(), function(i, child) {
				if (!child._destroyed) child.destroy();
			});
			$invokeSuper.call(this, arguments);
		},
		
		onActualVisibleChange: function() {
			
			function notifyChildren(control, actualVisible) {
				control._children.each(function(child) {
					if (child._parentActualVisible == actualVisible || !(child instanceof dorado.widget.Control)) {
						return;
					}
					child._parentActualVisible = actualVisible;
					child.onActualVisibleChange();
				});
			}
			
			$invokeSuper.call(this, arguments);
			notifyChildren(this, this.isActualVisible());
		},


		doRenderToOrReplace: function(replace, element) {
			if (replace && this._children.size == 0 && element.childNodes.length > 0) {
				var children = [];
				for (var i = 0; i < element.childNodes.length; i++) {
					children.push(element.childNodes[i]);
				}
				var htmlContrainer = new dorado.widget.HtmlContainer({
					content: children
				});
				this.addChild(htmlContrainer);
			}
			$invokeSuper.call(this, arguments);
		},
		
		/**
		 * 向容器中添加一个组件。
		 * @param {dorado.widget.Component} component 要添加的组件。
		 */
		addChild: function(component) {
			if (component._parent) {
				fireParentChanged = false;
				component._parent.removeChild(component);
				fireParentChanged = true;
			}
			
			this._children.insert(component);
			if (dorado.widget.View.TOP != this) component._parent = this;
			component.set("view", (this instanceof dorado.widget.View) ? this : this.get("view"));
			if (fireParentChanged && component.parentChanged) component.parentChanged();
			
			if (component instanceof dorado.widget.Control) {				
				var layout = this.get("layout");
				if (layout) {
					if (!(dorado.Object.isInstanceOf(component, dorado.widget.FloatControl) && component._floating)) {
						var shouldFireOnAttach = (this._attached && layout._regions.size == 0 && !layout._rendered);
						if (!component._visible && component._hideMode == "layout") {
							component._layoutConstraint = dorado.widget.layout.Layout.NONE_LAYOUT_CONSTRAINT;
						}
						layout.addControl(component);
						if (shouldFireOnAttach) layout.onAttachToDocument(this.getContentContainer());
					}
				}
			}
			if (!(component instanceof dorado.widget.Control) && !component._ready && this._ready) {
				component.onReady.call(component);
			}
		},
		
		/**
		 * 从容器中移除一个组件。
		 * @param {Component} component 要移除的组件。
		 */
		removeChild: function(component) {
			this._children.remove(component);
			component.set("view", null);
			
			component._parent = null;
			dorado.widget.View.TOP.addChild(component);
			if (fireParentChanged && component.parentChanged) component.parentChanged();
			
			if (component instanceof dorado.widget.Control) {	
				var layout = this._layout;
				if (layout) layout.removeControl(component);
			}
		},
		
		/**
		 * 从容器中移除所有子组件。
		 */
		removeAllChildren: function() {
			this._children.each(function(child) {
				this.removeChild(child);
			}, this);
		},
		
		/**
		 * 返回用于容纳子控件的DOM对象。
		 * @return {HTMLElement} DOM对象。
		 */
		getContentContainer: function() {
			return this.getDom();
		},
		
		doOnAttachToDocument: function() {
			var contentCt = this.getContentContainer();
			if (contentCt.nodeType && contentCt.nodeType == 1 && !contentCt.style.overflow) {
				contentCt.style.overflow = this._contentOverflow;
			}
			var layout = this._layout;
			if (layout && !(layout._regions.size == 0 && !layout._rendered)) {
				layout._overflow = (this._contentOverflow == "hidden") ? "hidden" : "visible";
				layout.onAttachToDocument(this.getContentContainer());
			}
		},
		
		doOnDetachToDocument: function() {
			var layout = this._layout;
			if (layout) layout.onDetachToDocument();
		},
		
		doOnResize: function() {
			var layout = this._layout;
			if (layout && layout._attached) {
				layout._overflow = (this._contentOverflow == "hidden") ? "hidden" : "visible";
				layout.onResize();
			}
		},
		
		/**
		 * 返回当前该容器中的子控件是否可以获得控制焦点。
		 * @param {boolean} [deep] 是否要分析控件各层父控件。默认为false。
		 * @return {boolean} 是否该容器控件中的子控件获得控制焦点。
		 * @see dorado.widget.Control.isFocusabled
		 */
		isChildrenFocusable: function(deep) {
			if (!this._rendered || !this.getDom()) return false;
			if (deep) {
				var parent = this._parent;
				while (parent) {
					if (!parent._rendered) break;
					if (!parent.isChildrenFocusable(false)) return false;
					parent = parent._parent;
				}
			}
			var dom = this.getDom();
			return !dom.disabled && dom.offsetWidth > 0;
		}
		
	});
	
})();
