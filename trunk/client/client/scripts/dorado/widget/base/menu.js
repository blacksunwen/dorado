(function() {
	var GROUP_CONTENT_CLASS = "group-content", GROUP_OVERFLOW_CLASS = "d-menu-overflow";

	/**
     * @author Frank Zhang (mailto:frank.zhang@bstek.com)
	 * @class 菜单项组。
	 * @extends dorado.widget.Control
	 */
	dorado.widget.Menu = $extend([dorado.widget.Control, dorado.widget.FloatControl], /** @scope dorado.widget.Menu.prototype */ {
		$className: "dorado.widget.Menu",

		focusable: true,
		selectable: false,

		ATTRIBUTES: /** @scope dorado.widget.Menu.prototype */ {
			className: {
				defaultValue: "d-menu"
			},

			floatingClassName: {
				defaultValue: "d-menu-floating"
			},

			visible: {
				defaultValue: false
			},

			animateType: {
				defaultValue: "slide",
				skipRefresh: true
			},

			hideAnimateType: {
				defaultValue: "fade",
				skipRefresh: true
			},

			/**
			 * 图标的显示位置，可选值：left、top。
			 * @attribute
			 * @default "left"
			 * @type String
			 */
			iconPosition: {
				defaultValue: "left"
			},

			/**
			 * 图标的显示大小，可选值：normal、big。
			 * @attribute
			 * @default "normal"
			 * @type String
			 */
			iconSize: {
				defaultValue: "normal"
			},

			/**
			 * 设置的时候以数组形式进行设置，读取的时候得到的属性为dorado.util.KeyedArray。
			 * @type dorado.util.KeyedArray
			 * @attribute
			 */
			items: {
				setter: function(value) {
					var group = this, items = group._items, doms = group._doms, rendered = group._rendered, item, i, l;
					if (items) {
						group.clearItems();
					}
					if (value) {
						if (!items) {
							group._items = items = new dorado.util.KeyedArray(function(value) {
								return value._name;
							});
						}
						if (value.constructor == Array.prototype.constructor) {
							for (i = 0, l = value.length; i < l; i++) {
								item = group.createMenuItem(value[i]);
								items.insert(item);
								if (rendered) {
									item.render(doms.groupContent);
								}
							}
						} else if (value instanceof dorado.util.KeyedArray) {
							for (i = 0, l = value.size; i < l; i++) {
								item = group.createMenuItem(value.get(i));
								items.append(item);

								if (rendered) {
									item.render(doms.groupContent);
								}
							}
						}
					} 
				}
			}
		},

		EVENTS: /** @scope dorado.widget.Menu.prototype */ {
			onHideTopMenu: {}
		},

		/**
		 * 创建MenuItem。
		 * @param {Object|dorado.widget.menu.AbstractMenuItem} config MenuItem的配置信息、或者MenuItem
		 * @return {dorado.widget.menu.MenuItem} 创建好的MenuItem。
		 * @protected
		 */
		createMenuItem: function(config) {
			if (!config) {
				return null;
			}
			var group = this, item;
			if (config instanceof dorado.widget.menu.AbstractMenuItem) {
                config._parent = group;
				item = config;
			} else {
				item = dorado.Toolkits.createInstance("menu", config);
                item._parent = group;
			}
			return item;
		},

		/**
		 * 创建overflow的上下箭头。
		 * @protected
		 */
		createOverflowarrow: function() {
			var group = this, dom = group._dom, doms = group._doms;
			if (dom) {
				var topArrow = $DomUtils.xCreateElement({
					tagName: "div",
					className: "overflow-top-arrow"
				});

				var bottomArrow = $DomUtils.xCreateElement({
					tagName: "div",
					className: "overflow-bottom-arrow"
				});
				
				$fly(dom).prepend(topArrow);
				dom.appendChild(bottomArrow);

				jQuery(topArrow).repeatOnClick(function(){
					group.doScrollUp();
				}, 120).addClassOnHover("overflow-top-arrow-hover");

				jQuery(bottomArrow).repeatOnClick(function(){
					group.doScrollDown();
				}, 120).addClassOnHover("overflow-bottom-arrow-hover");

				doms.overflowTopArrow = topArrow;
				doms.overflowBottomArrow = bottomArrow;
			}
		},

		/**
		 * 清除Overflow设置的height与scrollTop。
		 * @protected
		 */
		clearOverflow: function() {
			var group = this, dom = group._dom, doms = group._doms;
			if (dom) {
				group._overflowing = false;
				if (dorado.Browser.msie && dorado.Browser.version == 6) {
					$fly(dom).css("width", "");
				}
				$fly(dom).css("height", "").removeClass(GROUP_OVERFLOW_CLASS);
				$fly(doms.groupContent).scrollTop(0).css("height", "");
			}
		},

		/**
		 * 处理Group的Overflow。
		 * @param overflowHeight Menu对应的高度
		 * @protected
		 */
		handleOverflow: function(overflowHeight) {
			var group = this, dom = group._dom, doms = group._doms;
			if (dom) {
                $fly(dom).addClass(GROUP_OVERFLOW_CLASS).outerHeight(overflowHeight);
				if (!doms.overflowTopArrow) {
					group.createOverflowarrow();
				}
				group._overflowing = true;
				
				var contentHeight = $fly(dom).innerHeight() - $fly(doms.overflowTopArrow).outerHeight() -
					$fly(doms.overflowBottomArrow).outerHeight(), contentWidth = $fly(dom).width();

				if (dorado.Browser.msie && dorado.Browser.version == 6) {
					$fly(dom).width(contentWidth);
					$fly(doms.groupContent).outerHeight(contentHeight);
				} else {
					$fly(doms.groupContent).outerHeight(contentHeight);
				}

                $fly([doms.overflowTopArrow, doms.overflowBottomArrow]).outerWidth(contentWidth);
			}
		},

		/**
		 * 清除获得焦点的MenuItem。
		 * @private
		 */
		clearFocusItem: function() {
			var group = this, focusItem = group._focusItem;

			if (focusItem && focusItem.focusable) {
				focusItem.onBlur();
			}

			group._focusItem = null;
		},

		/**
		 * 取得可获得焦点的菜单项。
		 * 可获得焦点的MenuItem的条件是disabled属性为false。
		 * @param {String} [mode] 要获得可获得焦点的菜单项的模式，默认为next。可选值有：
		 *  1.next 下一个可以获得焦点的MenuItem，如果没有当前获得焦点的MenuItem，则自动转为first。
		 *  2.prev 上一个可以获得焦点的MenuItem，如果没有当前获得焦点的MenuItem，则自动转为last。
		 *  3.first 第一个可以获得焦点的MenuItem。
		 *  4.last 最后一个可获得焦点的MenuItem。
		 * @private
		 */
		getFocusableItem: function(mode) {
			var group = this, items = group._items, focusItem = group._focusItem, focusIndex = -1, result = null, i, j, item;

			if (!items) {
				return null;
			}

			mode = mode || "next";

			if (focusItem) {
				focusIndex = items.indexOf(focusItem);
			}

			if (mode == "next") {
				for (i = focusIndex + 1,j = items.size; i < j; i++) {
					item = items.get(i);
					if (!item._disabled && item._visible && !(item instanceof dorado.widget.menu.Separator)) {
						result = item;
						break;
					}
				}
				if (result == null) {
					mode = "first";
				} else {
					return result;
				}
			} else {
				if (mode == "prev") {
					for (i = focusIndex - 1; i >= 0; i--) {
						item = items.get(i);
						if (!item._disabled && item._visible && !(item instanceof dorado.widget.menu.Separator)) {
							result = item;
							break;
						}
					}
					if (result == null) {
						mode = "last";
					} else {
						return result;
					}
				}
			}

			if (mode == "first") {
				for (i = 0,j = items.size; i < j; i++) {
					item = items.get(i);
					if (!item._disabled && item._visible && !(item instanceof dorado.widget.menu.Separator)) {
						result = item;
						break;
					}
				}
				return result;
			} else if (mode == "last") {
				for (i = items.size - 1; i >= 0; i--) {
					item = items.get(i);
					if (!item._disabled && item._visible && !(item instanceof dorado.widget.menu.Separator)) {
						result = item;
						break;
					}
				}
				return result;
			}
		},

		/**
		 * 使得MenuItem获得焦点。(当鼠标移动到相应的MenuItem上以后，会自动显示子菜单。如果使用键盘，向上向下移动不会自动显示子菜单。)
		 * @param {dorado.widget.menu.AbstractMenuItem} item 要获得焦点的MenuItem
		 * @param {boolean} showSubmenu 获得焦点以后，是否显示子菜单。
		 * @private
		 */
		focusItem: function(item, showSubmenu) {
			var group = this, focusItem = group._focusItem;

			if (item && focusItem !== item) {
				group._focusItem = item;

				if (group._overflowing) {
					var itemDom = item._dom, doms = group._doms, viewTop = $fly(doms.groupContent).attr("scrollTop"),
						contentTop = $fly(doms.groupContent).attr("offsetTop"), itemTop = itemDom.offsetTop, itemHeight = itemDom.offsetHeight,
						itemBottom = itemTop + itemHeight, contentHeight = $fly(doms.groupContent).innerHeight(),
						viewBottom = contentHeight + viewTop;

					if (!dorado.Browser.msie) {
						itemTop = itemTop - contentTop;
						itemBottom = itemTop + itemHeight;
					}

					//log.debug("contentHeight:" + contentHeight + "\titemTop:" + itemTop + "\tviewTop:" + viewTop + "\titemBottom:" + itemBottom + "\tviewBottom:" + viewBottom);
					//log.debug("contentTop:" + contentTop + "\titemTop:" + itemTop + "\titem-offsetTop:" + itemDom.offsetTop + "\tcontent-Top:" + contentTop);

					if (itemTop < viewTop) {//top is not visible
						$fly(doms.groupContent).attr("scrollTop", itemTop);
					}
					else if (itemBottom > viewBottom) {//bottom is not visible
						$fly(doms.groupContent).attr("scrollTop", itemBottom - contentHeight);
					}
				}

				if (focusItem && focusItem.focusable) {
					focusItem.onBlur();
				}

				if (!item._disabled) {
					item.onFocus(showSubmenu);
				}
			}
		},

		/**
		 * 为Menu插入MenuItem。
		 * @param {Object|dorado.widget.menu.AbstractMenuItem} item 可以是MenuItem或者MenuItem的Json配置信息。
		 * @param {int} [index] 可以是整数或者MenuItem。
		 *                      如果是整数，代表插入以后该Item的位置。如果是MenuItem，则代表要插入到该MenuItem之前。
		 */
		addItem: function(item, index) {
			var group = this, items = group._items, doms = group._doms, refItem;
			items = group._items = items ? group._items : new dorado.util.KeyedArray(function(value) {
				return value._name;
			});
			if (item.constructor == Object.prototype.constructor || typeof item == "string") {
				item = group.createMenuItem(item);
			} else {
				item._parent = group;
			}

			if (typeof index == "number") {
				items.insert(item, index);
				refItem = items.get(index + 1);
			} else if (index instanceof dorado.widget.menu.AbstractMenuItem) {
				refItem = items.indexOf(index);
				if (index != -1) {
					items.insert(item, index);
				} else {
					throw new dorado.ResourceException("dorado.base.ItemNotInGroup");
				}
			} else {
				items.insert(item);
			}

			if (group._rendered) {
				item.render(doms.groupContent, refItem ? refItem._dom : null);
			}

			group.refresh();
		},

		/**
		 * 取得Menu中的MenuItem。
		 * @param {String|int|dorado.widget.menu.AbstractMenuItem} name 可以是Item的name，也可以是item的索引，也可以是MenuItem。
		 * @return {dorado.widget.menu.AbstractMenuItem} 找到的MenuItem。
		 */
		getItem: function(name) {
			var group = this, items = group._items;
			if (items) {
				if (typeof name == "number" || typeof name == "string") {
					return items.get(name);
				} else {
					return name;
				}
			}
			return null;
		},

		/**
		 * 根据路径描述取得MenuItem，可以是任意级别的item。
		 * 路径使用"/"来分割，如果需要找到name为File的MenuItem下的子菜单中的Name为Open的MenuItem，路径描述符为File/Open
		 * @param {String} path 要查找额MenuItem的路径描述符。
		 * @return {dorado.widget.menu.AbstractMenuItem} 找到的MenuItem。
		 */
		findItem: function(path) {
			var group = this, items = group._items, item, itemgroup, i, j;
			if (!items) {
				return null;
			}
			if (typeof path == "string") {
				var index = path.indexOf("/"), mainpath, subpath;
				if (index != -1) {
					mainpath = path.substring(0, index);
					subpath = path.substring(index + 1);
				} else {
					mainpath = path;
				}
				if (subpath) {
					for (i = 0,j = items.size; i < j; i++) {
						item = items.get(i);
						if (item._name == mainpath) {
							itemgroup = item._submenu;
							if (itemgroup) {
								return itemgroup.findItem(subpath);
							}
						}
					}
				} else {
					for (i = 0,j = items.size; i < j; i++) {
						item = items.get(i);
						if (item._name == mainpath) {
							return item;
						}
					}
					return null;
				}
			}
			return null;
		},

		/**
		 * 移除菜单项。
		 * @param {String|int|dorado.widget.menu.AbstractMenuItem} item 要移除的菜单项，可以是菜单项、菜单项的索引以及菜单项的name。
		 * @return  {dorado.widget.menu.AbstractMenuItem} 移除的菜单项。
		 */
		removeItem: function(item) {
			var group = this, items = group._items, dom;
			if (items) {
				if (typeof item == "number" || typeof item == "string") {
					item = items.get(item);
				}
				if (item) {
					items.remove(item);
					dom = item._dom;
					if (dom) {
						$fly(dom).remove();
					}
					return item;
				}
			}
			group.refresh();
			
			return null;
		},

		/**
		 * 删除所有的MenuItem
		 * @param deep 是否删除MenuItem的子Group。如果不传入该值，默认为false。
		 */
		clearItems: function(deep) {
			var group = this, items = group._items, dom;
			if (items) {
				var innerItems = items.items;
				for (var i = 0, j = innerItems.length; i < j; i++) {
					var item = innerItems[0];
					dom = item._dom;
					if (deep && item instanceof dorado.widget.menu.MenuItem) {
						var subGroup = item._submenu;
						if (subGroup) {
							subGroup.clearItems(deep);
							subGroup.destroy();
						}
					}
					if (dom) {
						$fly(dom).remove();
					}
					items.removeAt(0);
				}
				group.refresh();
			}
		},

		/**
		 * @protected
		 */
		hideTopMenu: function() {
			var group = this, opener = group.opener, parent;
			if (group._floating) {
				group.hide();
				group.fireEvent("onHideTopMenu", group);
				if (opener) {
					parent = opener._parent;
					parent.hideTopMenu();
				}
			}
		},

		/**
		 * @protected
		 */
		doScrollUp: function() {
			var group = this, doms = group._doms, groupContent = doms.groupContent, st = $fly(groupContent).scrollTop(),
				target = st - 22;

			if (target >= 0) {
				$fly(groupContent).scrollTop(target);
			} else {
				$fly(groupContent).scrollTop(0);
			}
		},

		/**
		 * @protected
		 */
		doScrollDown: function() {
			var group = this, doms = group._doms, groupContent = doms.groupContent, st = $fly(groupContent).scrollTop(),
				target = st + 22, scrollHeight = $fly(groupContent).attr("scrollHeight");
			if (target <= scrollHeight) {
				$fly(groupContent).scrollTop(target);
			} else {
				$fly(groupContent).scrollTop(scrollHeight - $fly(groupContent).innerHeight());
			}
		},

		doOnBlur: function() {
			if (this._floating) {
				this.hide();
			} else {
				this.clearFocusItem();
			}
		},

		/**
		 * 键盘事件主要是针对当前获得焦点的MenuItem，当鼠标移动到可用的菜单项(disable属性为false)上，则此菜单项为获得焦点的MenuItem。
		 *
		 * 处理按键事件，目前支持以下按钮：
		 * <ul>
		 *  <li>
		 *      向上按钮：如果当前获得焦点的MenuItem有上一个菜单项，并且可用，则使上一个菜单项获得焦点。
		 *  </li>
		 *  <li>
		 *      向下按钮: 如果当前获得焦点的MenuItem有下一个菜单项，并且可用，则使下一个菜单项获得焦点。
		 *  </li>
		 *  <li>
		 *      向左按钮: 如果当前获得焦点的MenuItem有上级菜单，则返回上级菜单，并隐藏当前获得焦点的MenuItem。
		 *  </li>
		 *  <li>
		 *      向右按钮: 如果当前获得焦点的MenuItem有下级菜单，则打开下级菜单，否则，什么也不做。
		 *  </li>
		 *  <li>
		 *      回车: 相当于单击当前获得焦点的MenuItem。
		 *  </li>
		 *  <li>
		 *      Esc: 隐藏打开的菜单。
		 *  </li>
		 * </ul>
		 * @param {Object} event dhtml中的event。
		 */
		doOnKeyDown: function(event) {
			var group = this, opener, focusItem;
			switch (event.keyCode) {
				case 37://left arrow
					if (group) {
						opener = group.opener;
						if (opener) {
							opener.hideSubmenu && opener.hideSubmenu();
						}
					}
					break;
				case 38://up arrow
					group.focusItem(group.getFocusableItem("prev"));
					break;
				case 39://right arrow
					if (group._focusItem) {
						group._focusItem.showSubmenu && group._focusItem.showSubmenu(true);
					}
					break;
				case 40://down arrow
					group.focusItem(group.getFocusableItem("next"));
					break;
				case 13://enter
					focusItem = group._focusItem;
					if (focusItem) {
						focusItem.onClick && focusItem.onClick();
					}
					return false;
				case 27://esc
					group.hideTopMenu();
					break;
			}
		},

		createDom: function() {
			var group = this, doms = {}, dom = $DomUtils.xCreateElement({
				tagName: "div",
				className: group._className,
				content: {
					tagName: "ul",
					className: "group-content",
					contextKey: "groupContent"
				}
			}, null, doms), items = group._items;

			group._doms = doms;

			var groupContent = doms.groupContent;

			if (items) {
				items.each(function(item) {
					item.render(groupContent);
				});
			}

			$fly(dom).hover(dorado._NULL_FUNCTION, function() {
				var focusItem = group._focusItem;

				if (focusItem) {
					if (focusItem instanceof dorado.widget.menu.MenuItem) {
						if (!focusItem._submenu) {
							group.clearFocusItem();
						}
					} else if(focusItem instanceof dorado.widget.menu.ControlMenuItem){
						if (!focusItem._control) {
							group.clearFocusItem();
						}
					} else {
						//如果是CheckedItem
						group.clearFocusItem();
					}
				}
			}).click(function(event) {
				event.stopImmediatePropagation();
			});

			$fly(groupContent).mousewheel(function(event, delta) {
				if (group._overflowing) {
					if (delta < 0) {//down
						group.doScrollDown();
					} else if (delta > 0) {//up
						group.doScrollUp();
					}
				}
			});

			if (group._iconPosition == "top") {
				$fly(dom).addClass(group._className + "-icon-top");
			}

			return dom;
		},

		refreshDom: function(dom) {
			$invokeSuper.call(this, arguments);

			var group = this, doms = group._doms, menuContentHeight = $fly(doms.groupContent).outerHeight();
			if (menuContentHeight > dom.offsetHeight) {
				group.handleOverflow();
			} else {
				//group.clearOverflow();
			}

			//empty panel
			var items = group._items || {}, visibleItemCount = 0;
			for(var i = 0, j = items.size;i < j;i++){
				var item = items.get(i);
				if(item._visible === false) continue;
				visibleItemCount++;
			}

			if(visibleItemCount == 0){
				if(!group._noContentEl){
					group._noContentEl = document.createElement("div");
					group._noContentEl.className = "no-content-group";
					group._noContentEl.innerHTML = "&lt;empty panel&gt;";
					dom.appendChild(group._noContentEl);
				}
				$fly(dom).addClass(group._className + "-no-content");
			} else {                            
				if(group._noContentEl){
					$fly(dom).removeClass(group._className + "-no-content");
				}
			}
		},

		getShowPosition: function(options){
			var menu = this, anchorTarget = options.anchorTarget, dom = menu._dom, fixedElement, result;

			options = options || {};
			options.overflowHandler = function(overflowHeight) {
				menu.handleOverflow(overflowHeight);
			};

			if (anchorTarget && anchorTarget instanceof dorado.widget.menu.MenuItem) {
				fixedElement = anchorTarget._dom;

				menu.opener = anchorTarget;
				menu._focusParent = anchorTarget._parent;

				result = $DomUtils.dockAround(dom, fixedElement, options);

				return result;
			}
			else {
				return $invokeSuper.call(this, arguments);
			}
		},

		doAfterShow: function(options){
			var menu = this, focusfirst = (options || {}).focusFirst;
			if (focusfirst) {
				var item = menu.getFocusableItem("first");
				if (item) {
					menu.focusItem(item);
				}
			}
			$invokeSuper.call(this, arguments);
		},

		/**
		 * @protected
		 */
		doAfterHide: function() {
			var menu = this, dom = menu._dom;
			if (!dom) {
				return;
			}

			menu.clearFocusItem();

			if (dom) {
				if (menu._overflowing) {
					menu.clearOverflow();
				}
			}
			menu.opener = null;

			$invokeSuper.call(this, arguments);
		}
	});
})();