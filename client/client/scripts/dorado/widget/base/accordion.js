/**
 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
 * @component Base
 * @class dorado.widget.Section
 * <p>
 * Accordion的每个区域使用的组件。<br />
 * Accordion的每个区域会有一个标题栏和组件，Section的作用是把一个普通的组件包装成有标题栏的组件。<br />
 * 一般情况下不建议将Accordion之外的组件创建该类型的组件，可以使用Panel来实现类似效果。
 * </p>
 * @extends dorado.widget.Control
 */
dorado.widget.Section = $extend(dorado.widget.Control, /** @scope dorado.widget.Section.prototype */ {
	$className: "dorado.widget.Section",
	_inherentClassName: "i-section",
	
	ATTRIBUTES: /** @scope dorado.widget.Section.prototype */ {
		className: {
			defaultValue: "d-section"
		},

        /**
         * Section的name，可以不指定。但如果需要通过代码获得该菜单项，则必须指定。
         * @type String
         * @attribute
         */
        name: {},

		/**
		 * Section的标题栏使用的标题。
		 * @attribute
		 * @type String
		 */
		caption: {
			skipRefresh: true,
			path: "_captionBar.caption"
		},
		
		/**
		 * Section的标题栏使用的图标。
		 * @attribute
		 * @type String
		 */
		icon: {
			skipRefresh: true,
			path: "_captionBar.icon"
		},
		
		/**
		 * Section的标题栏使用的图标的className。
		 * @attribute
		 * @type String
		 */
		iconClass: {
			skipRefresh: true,
			path: "_captionBar.iconClass"
		},
		
		/**
		 * Section是否可以点击。
		 * @attribute
		 * @default false
		 * @type boolean
		 */
		disabled: {
			setter: function(value) {
				this._disabled = value;
				if (this._parent) {
					if (this._parent._currentSection == this) {
						this._parent.changeToAvialableSection();
						this._parent.refresh();
					}
				}
			}
		},
		
		visible: {
			setter: function(value) {
				this._visible = value;
				if (this._rendered) {
					$fly(this._dom).css("display", value ? "" : "none");
				}
				if (this._parent) {
					if (this._parent._currentSection == this) {
						this._parent.changeToAvialableSection();
					}
					this._parent.refresh();
				}
			}
		},
		
		/**
		 * 是否可以展开，此属性存在的意义是为了那些专门用来作为一个按钮的section提供便利。
		 * @attribute
		 * @default true
		 * @type boolean
		 */
		expandable: {
			defaultValue: true,
			setter: function(value) {
				this._expandable = value;
				if (this._parent) {
					if (this._parent._currentSection == this) {
						this._parent.changeToAvialableSection();
						this._parent.refresh();
					}
				}
			}
		},
		
		/**
		 * 该Section绑定的Control
		 * @attribute
		 * @type dorado.widget.Control
		 */
		control: {
			componentReference: true,
			innerComponent: "",
			setter: function(value) {
				if (value instanceof dorado.widget.Menu) {
					value.set("floating", false);
				}
				this._control = value;
			}
		},
		
		/**
		 * 用户自定义数据。
		 * @type Object
		 * @attribute
		 */
		userData: {}
	},
	
	EVENTS: /** @scope dorado.widget.Section.prototype */ {
		/**
		 * 当Caption被点击以后会触发的事件，对于expandable为false的Section比较有用。
		 * @param {Object} self 事件的发起者，即组件本身。
		 * @param {Object} arg 事件参数。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
		onCaptionClick: {}
	},
	
	createDom: function() {
		var section = this, doms = {}, dom = $DomUtils.xCreate({
			tagName: "div",
			className: "i-section " + section._className,
			content: [{
				tagName: "div",
				className: "container",
				contextKey: "container"
			}]
		}, null, doms);
		
		section._doms = doms;
		
		jQuery(dom).addClassOnHover("hover-section");
		
		var captionBar = section._captionBar = new dorado.widget.CaptionBar({
			caption: section._caption,
			className: "d-section-caption-bar",
			icon: section._icon,
			iconClass: section._iconClass
		});
		captionBar.render(dom, doms.container);
		section.registerInnerControl(captionBar);
		
		doms.captionBar = captionBar._dom;
		
		return dom;
	},
	
	doRenderControl: function() {
		var section = this, doms = section._doms, control = section._control;
		if (control) {
			control.render(doms.container);
			control.set("visible", true);
		}
	}
});

/**
 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
 * @class Accordion
 * <p>
 * 布局组件，分为多个Section，每个Section有一个标题栏和组件，但同时激活的只能有一个Section，类似TabBar或者TabControl。
 * </p>
 * @extends dorado.widget.Control
 */
dorado.widget.Accordion = $extend(dorado.widget.Control, /** @scope dorado.widget.Accordion.prototype */ {
	$className: "dorado.widget.Accordion",
	_inherentClassName: "i-accordion",
	focusable: true,
	
	ATTRIBUTES: /** @scope dorado.widget.Accordion.prototype */ {
		className: {
			defaultValue: "d-accordion"
		},

		/**
		 * 在切换Section的是否要使用动画。<br />
		 * 在IE下，该属性默认值为false，其他浏览器为true。
		 * @attribute
		 * @type boolean
		 */
		animate: {
	         defaultValue: dorado.Browser.msie ? false : true
		},

		/**
		 * Accordion中的Section。
		 * @attribute
		 * @type dorado.widget.Section[]
		 */
		sections: {
			innerComponent: "Section",
			setter: function(value) {
				var accordion = this, oldValue = accordion._sections;
				if (oldValue) {
					accordion.clearSections();
				}
				if (typeof value == "object" && value.constructor == Array.prototype.constructor) {
					for (var i = 0, j = value.length; i < j; i++) {
						accordion.addSection(value[i]);
					}
				}
			}
		},
		
		height: {
			defaultValue: 400
		},
		
		/**
		 * 当前的Section。
		 * @attribute
		 * @type dorado.widget.Section
		 */
		currentSection: {
			setter: function(value) {
				this.doSetCurrentSection(value);
			}
		},
		
		/**
         * 当前Section的序号（自0开始计算）。
         * @type int
         * @attribute
         */
        currentIndex: {
            skipRefresh: true,
            getter: function() {
                if (this._currentSection) {
                    return this._sections.indexOf(this._currentSection);
                }
                return -1;
            },
            setter: function(index) {
                this.set("currentSection", this._sections.get(index));
            }
        }

	},
	
	EVENTS: /** @scope dorado.widget.Accordion.prototype */ {
		/**
		 * 在currentSection进行切换之前触发的事件，在这个事件里面currentSection属性还是切换之前的Section。
		 *
		 * @param {Object} self 事件的发起者，即组件本身。
		 * @param {Object} arg 事件参数。
		 * @param {dorado.widget.Section} arg.newSection 要切换到的Section。
		 * @param {dorado.widget.Section} arg.oldSection 当前Section。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
		beforeCurrentSectionChange: {},
		
		/**
		 * 在currentSection进行切换之后触发的事件，在这个事件里面currentSection属性已经是切换之后的Section了。
		 *
		 * @param {Object} self 事件的发起者，即组件本身。
		 * @param {Object} arg 事件参数。
		 * @param {dorado.widget.Section} arg.newSection 要切换到的Section。
		 * @param {dorado.widget.Section} arg.oldSection 切换之前的Section。
		 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
		 * @event
		 */
		onCurrentSectionChange: {}
	},
		
	doGet: function(attr) {
		var c = attr.charAt(0);
		if (c == '#' || c == '&') {
			var name = attr.substring(1);
			return this.getSection(name);
		} else {
			return $invokeSuper.call(this, [attr]);
		}
	},

    /**
     * 根据索引、name属性来取得某个Section。
     * @param {int|String} name 要取得的Seciton的索引或者name。
     * @return {dorado.widget.Section} 取得的Section
     */
    getSection: function(name) {
        if (this._sections) return this._sections.get(name);
        return null;
    },

	doSetCurrentSection: function(section, animate) {
		var accordion = this, lastCurrent = accordion._currentSection, newCurrent = section;
		
		if (lastCurrent == newCurrent) {
			return;
		}
		
		if (!accordion._rendered) {
			accordion._currentSection = newCurrent;
			return;
		}
		
		var eventArg = {
			newSection: section,
			oldSection: lastCurrent
		};
		accordion.fireEvent("beforeCurrentSectionChange", accordion, eventArg);
		
		if (eventArg.processDefault === false) {
			return;
		}
		
		if (animate) {
			var lastCurrentCt = lastCurrent._doms.container, oldHeight = $fly(lastCurrentCt).height();
			$fly(lastCurrentCt).dockable("bottom", true);
			
			accordion._sliding = true;
			
			$fly(newCurrent._doms.container).safeSlideIn({
				direction: "t2b",
				complete: function() {
					$fly(lastCurrentCt).height(oldHeight).css("display", "").undockable(true);
					
					accordion._currentSection = newCurrent;
					accordion.refresh();
					accordion.fireEvent("onCurrentSectionChange", accordion, eventArg);
					
					accordion._sliding = false;
				},
				step: function(now) {
					$fly(lastCurrentCt).height(oldHeight - now.height);
				}
			});
		} else {
			accordion._currentSection = newCurrent;
			accordion.refresh();
			accordion.fireEvent("onCurrentSectionChange", accordion, eventArg);
		}
	},
	
	changeToAvialableSection: function() {
		var accordion = this, sections = accordion._sections;
		if (sections) {
			var startIndex = -1, currentSection = accordion._currentSection, i, j, section;
			if (currentSection) {
				startIndex = sections.indexOf(currentSection);
			}
			for (i = startIndex + 1, j = sections.size; i < j; i++) {
				section = sections.get(i);
				if (section && section._visible && section._expandable && !section._disabled) {
					accordion.doSetCurrentSection(section);
					return section;
				}
			}
			for (i = startIndex - 1; i >= 0; i--) {
				section = sections.get(i);
				if (section && section._visible && section._expandable && !section._disabled) {
					accordion.doSetCurrentSection(section);
					return section;
				}
			}
		}
		return null;
	},
	
	getVisibleSectionCount: function() {
		var accordion = this, sections = accordion._sections, result = 0;
		if (sections) {
			var section;
			for (var i = 0, j = sections.size; i < j; i++) {
				section = sections.get(i);
				if (section && section._visible) {
					result++;
				}
			}
		}
		return result;
	},
	
	/**
	 * 为Accordion插入Section。
	 * @param {Object|dorado.widget.Section} section 要插入的Section或者Section的配置信息。
	 * @param {int} [index] 要插入的Section的索引，如不指定，则添加到最后。
	 */
	addSection: function(section, index) {
		var accordion = this, sections = accordion._sections, refDom;
		if (!sections) {
			accordion._sections = sections = new dorado.util.KeyedArray(function(value) {
                return value._name;
            });
		}
		if (typeof section == "object" && section.constructor == Object.prototype.constructor) {
			section = new dorado.widget.Section(section);
		}
		if (typeof index == "number") {
			refDom = sections.get(index)._dom;
			sections.insert(section, index);
		} else {
			sections.insert(section);
		}
		if (accordion._rendered) {
			section.render(accordion._dom, refDom);
			accordion.registerInnerControl(section);
			accordion.bindAction(section);
			
			accordion.refresh();
		}
	},
	
	/**
	 * 移除一个Section。
	 * @param {int|dorado.widget.Section} section 要移除的Section或者Section的索引。
	 */
	removeSection: function(section) {
		var accordion = this, sections = accordion._sections;
		if (sections) {
			if (typeof section == "number") {
				section = sections.get(section);
			}
			if (section instanceof dorado.widget.Section) {
				if (accordion._rendered) {
					accordion.unregisterInnerControl(section);
					section.destroy();
					
					if (section == accordion._currentSection) {
						accordion.changeToAvialableSection();
					}
					sections.remove(section);
					
					accordion.refresh();
				} else {
					sections.remove(section);
				}
			}
		}
	},
	
	/**
	 * 删除掉所有的Section。
	 */
	clearSections: function() {
		var accordion = this, sections = accordion._sections, section;
		if (sections) {
			for (var i = 0, j = sections.size; i < j; i++) {
				section = sections.get(i);
				accordion.unregisterInnerControl(section);
				section.destroy();
			}
			accordion._currentSection = null;
			accordion._sections = [];
		}
	},
	
	bindAction: function(section) {
		var accordion = this;
		section._captionBar.addListener("onClick", function() {
			if (accordion._sliding || section._disabled) {
				return;
			}
			section.fireEvent("onCaptionClick", section);
			if (section._expandable) {
				accordion.doSetCurrentSection(section, !!accordion._animate);
			}
		});
	},
	
	createDom: function() {
		var accordion = this, dom = document.createElement("div"), sections = accordion._sections, section;
		
		dom.className = "i-accordion " + accordion._className;
		
		if (sections) {
			for (var i = 0, j = sections.size; i < j; i++) {
				section = sections.get(i);
				section.render(dom);
				accordion.registerInnerControl(section);
				accordion.bindAction(section);
			}
		}
		
		return dom;
	},
	
	refreshDom: function(dom) {
		$invokeSuper.call(this, arguments);
		
		var accordion = this, sections = accordion._sections, currentSection = accordion._currentSection;
		
		if (sections && !currentSection) {
			currentSection = accordion.changeToAvialableSection();
		}
		
		if (currentSection) {
			$fly(currentSection._dom).addClass("current-section");
			currentSection.setActualVisible(true);
		}
		
		var sectionMinHeight, ctHeight, accordionHeight = $fly(dom).height(), visibleCount = accordion.getVisibleSectionCount();
		
		if (sections) {
			var section, control, sectionCt;
			for (var i = 0, j = sections.size; i < j; i++) {
				section = sections.get(i);
				sectionCt = section._doms.container;
				
				if (currentSection != section) {
					$fly(section._dom).removeClass("current-section");
                    section.setActualVisible(false);
				}
				
				if (typeof sectionMinHeight != "number") {
					sectionMinHeight = $fly(section._dom).outerHeight(true) - sectionCt.offsetHeight;
					ctHeight = accordionHeight - sectionMinHeight * visibleCount;
				}
				
				$fly(sectionCt).outerHeight(ctHeight, true);
				
				control = section._control;
				if (control) {
					control.set({
						width: $fly(dom).width(),
						height: ctHeight
					});
				}
			}
		}
		
		if (currentSection && currentSection._control && !currentSection._control._rendered) {
			currentSection.doRenderControl();
		}
	},
	
	getFocusableSubControls: function() {
		return [this._currentSection ? this._currentSection._control : null];
	}
});
