(function() {

	var validItemCodes = ["|<", "<", ">", ">|", "goto", "+", "-", "x", "|"];
	var defaultShowTextItemCodes = ["goto", "+", "-", "x"];
	
	/**
	 * @author Benny Bao (mailto:benny.bao@bstek.com)
	 * @component Base
	 * @class 数据导航条。
	 * <p>数据导航条很适合配合ToolBar控件一同使用，建议您在使用时将数据导航条放置在ToolBar中，当然这并不是必须的。</p>
	 * @extends dorado.widget.Control
	 * @extends dorado.widget.DataControl
	 */
	dorado.widget.DataPilot = $extend([dorado.widget.Control, dorado.widget.DataControl], /** @scope dorado.widget.DataPilot.prototype */ {
		$className: "dorado.widget.DataPilot",
		
		ATTRIBUTES: /** @scope dorado.widget.DataPilot.prototype */ {
			className: {
				defaultValue: "d-data-pilot"
			},
			
			/**
			 * 用于定义数据导航条中要显示那些子对象的表达式。
			 * <p>
			 * 该表达式的基本语法是：<code>子对象1的表达式,子对象2的表达式,子对象3的表达式,...</code>
			 * </p>
			 * <p>
			 * 其中，每一个子对象的表达式的语法是：<code>子对象代码[/选项]</code>
			 * </p>
			 * <p>
			 * 上面提到的子对象代码的可选范围如下：
			 * <ul>
			 * 	<li>|<	-	翻到第一页的按钮。</li>
			 * 	<li><	-	翻到上一页的按钮。</li>
			 * 	<li>>	-	翻到下一页的按钮。</li>
			 * 	<li>>|	-	翻到最后一页的按钮。</li>
			 * 	<li>+	-	添加记录按钮。</li>
			 * 	<li>-	-	删除当前记录按钮。</li>
			 * 	<li>x	-	取消当前记录按钮。</li>
			 * 	<li>goto	-	跳转到指定页的编辑框+按钮。</li>
			 * 	<li>|	-	分割线。</li>
			 * 	<li>pages	-	用于简单翻页按钮定义的特殊代码，表示<code>|<,<,goto,>,>|</code>这5个子对象的组合。</li>
			 * </ul>
			 * 子对象的选项多数情况下可以省略的，目前包含下面几种：
			 * <ul>
			 * 	<li>i	-	是否显示按钮中的图标。此选项对于某些子对象代码而言是没有作用的，例如:goto、pages。</li>
			 * 	<li>c	-	是否显示按钮中的文字。此选项对于某些子对象代码而言是没有作用的，例如:goto、pages。</li>
			 * </ul>
			 * </p>
			 * @attribute
			 * @type String
			 * @default "pages"
			 *
			 * @example
			 * // 下面的代码指定了数据导航条中将显示如下内容：
			 * // 所有翻页按钮+分割线+只显示图标的添加按钮+只显示图标的删除按钮+显示图标和文字的取消按钮
			 * dataPilot.set("itemCodes", "pages,|,+/i,-/i,x/ic");
			 */
			itemCodes: {
				defaultValue: "pages",
				setter: function(v) {
					if (this._itemCodes != v) {
						this._itemCodes = v;
						this.compileItemCodes(v);
					}
				}
			},
			
			height: {
				independent: true,
				readOnly: true
			}
		},
		
		EVENTS: /** @scope dorado.widget.DataPilot.prototype */ {
		
			/**
			 * 当内部的子控件被刷新时触发的事件。
			 * @param {Object} self 事件的发起者，即组件本身。
			 * @param {Object} arg 事件参数。
			 * @param {String} arg.code 子按钮对应的代码。
			 * @param {doraro.widget.Control} arg.control 子控件。
			 * @param {boolean} #arg.processDefault=true 是否要继续系统的默认操作。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			onSubControlRefresh: {},
			
			/**
			 * 当内部的子控件的操作被触发时触发的事件。
			 * <p>
			 * 对于其中的子按钮而言，此事件在该按钮被点击时触发。<br>
			 * 对于goto这样的子控件而言，此事件在其引起的页面跳转动作执行前被触发。
			 * </p>
			 * @param {Object} self 事件的发起者，即组件本身。
			 * @param {Object} arg 事件参数。
			 * @param {String} arg.code 子按钮对应的代码。
			 * @param {doraro.widget.Control} arg.control 子控件。
			 * @param {boolean} #arg.processDefault=true 是否要继续系统的默认操作。
			 * @return {boolean} 是否要继续后续事件的触发操作，不提供返回值时系统将按照返回值为true进行处理。
			 * @event
			 */
			onSubControlAction: {}
		},
		
		filterDataSetMessage: function(messageCode, arg, data) {
			var b = true;
			switch (messageCode) {
				case dorado.widget.DataSet.MESSAGE_CURRENT_CHANGED:
				case dorado.widget.DataSet.MESSAGE_REFRESH_ENTITY:
				case dorado.widget.DataSet.MESSAGE_ENTITY_STATE_CHANGED:{
					var entities = this.getBindingData();
					b = (!this._entities || entities == this._entities || dorado.DataUtil.isOwnerOf(entities, arg.entityList));
					break;
				}
				case dorado.widget.DataSet.MESSAGE_DATA_CHANGED:{
					var entities = this.getBindingData();
					b = (!this._entities || entities == this._entities || dorado.DataUtil.isOwnerOf(entities, arg.entity));
					break;
				}
				case dorado.widget.DataSet.MESSAGE_DELETED:
				case dorado.widget.DataSet.MESSAGE_INSERTED:{
					b = false;
					break;
				}
			}
			return b;
		},
		
		processDataSetMessage: function(messageCode, arg, data) {
			this._entities = this.getBindingData();
			switch (messageCode) {
				case dorado.widget.DataSet.MESSAGE_REFRESH:
				case dorado.widget.DataSet.MESSAGE_DATA_CHANGED:
				case dorado.widget.DataSet.MESSAGE_CURRENT_CHANGED:
				case dorado.widget.DataSet.MESSAGE_REFRESH_ENTITY:
				case dorado.widget.DataSet.MESSAGE_ENTITY_STATE_CHANGED:{
					this.refresh(true);
					break;
				}
			}
		},
		
		getBindingData: function(options) {
			var realOptions = {
				firstResultOnly: true,
				acceptAggregation: true
			};
			if (typeof options == "String") {
				realOptions.loadMode = options;
			} else {
				dorado.Object.apply(realOptions, options);
			}
			return $invokeSuper.call(this, [realOptions]);
		},
		
		refreshDom: function(dom) {
			$invokeSuper.call(this, arguments);
			
			if (this._currentItemCodeExpression === undefined) {
				this.compileItemCodes();
			}
			
			if (this._itemCodeExpression != this._currentItemCodeExpression) {
				this._currentItemCodeExpression = this._itemCodeExpression || null;
				
				var itemObjects = this._itemObjects, oldItemObjects = itemObjects;
				if (itemObjects) {
					for (var p in itemObjects) {
						var item = itemObjects[p];
						item.unrender();
						this.unregisterInnerControl(item);
					}
				}
				this._itemObjects = itemObjects = {};
				
				var itemCodes = this._compiledItemCodes;
				if (itemCodes) {
					for (var i = 0; i < itemCodes.length; i++) {
						var itemCode = itemCodes[i];
						var item = oldItemObjects ? oldItemObjects[itemCode.key] : null;
						if (!item) item = this.createItem(itemCode);
						item.render(dom);
						this.registerInnerControl(item);
						itemObjects[itemCode.key] = item;
					}
				}
			}
			this.refreshItems();
		},
		
		createItem: function(itemCode) {
		
			function fireOnActionEvent(code, control) {
				var eventArg = {
					code: code,
					control: control,
					processDefault: true
				};
				this.fireEvent("onSubControlAction", this, eventArg);
				return eventArg.processDefault;
			}
			
			var item, pilot = this;
			switch (itemCode.code) {
				case "|<":
					item = new PageButton({
						icon: itemCode.showIcon ? "url(>skin>common/icons.gif) 0px -220px" : null,
						caption: itemCode.showCaption ? $resource("dorado.baseWidget.DataPilotFirstPage") : null,
						onClick: function(self) {
							if (!fireOnActionEvent.call(pilot, itemCode.code, self)) return;
							var list = pilot._entities;
							if (list instanceof dorado.EntityList) list.firstPage(dorado._NULL_FUNCTION);
						}
					});
					break;
				case "<":
					item = new PageButton({
						icon: itemCode.showIcon ? "url(>skin>common/icons.gif) -40px -220px" : null,
						caption: itemCode.showCaption ? $resource("dorado.baseWidget.DataPilotPrevousPage") : null,
						onClick: function() {
							if (!fireOnActionEvent.call(pilot, itemCode.code, self)) return;
							var list = pilot._entities;
							if (list instanceof dorado.EntityList) list.previousPage(dorado._NULL_FUNCTION);
						}
					});
					break;
				case ">":
					item = new PageButton({
						icon: itemCode.showIcon ? "url(>skin>common/icons.gif) -60px -220px" : null,
						caption: itemCode.showCaption ? $resource("dorado.baseWidget.DataPilotNextPage") : null,
						onClick: function() {
							if (!fireOnActionEvent.call(pilot, itemCode.code, self)) return;
							var list = pilot._entities;
							if (list instanceof dorado.EntityList) list.nextPage(dorado._NULL_FUNCTION);
						}
					});
					break;
				case ">|":
					item = new PageButton({
						icon: itemCode.showIcon ? "url(>skin>common/icons.gif) -100px -220px" : null,
						caption: itemCode.showCaption ? $resource("dorado.baseWidget.DataPilotLastPage") : null,
						onClick: function() {
							if (!fireOnActionEvent.call(pilot, itemCode.code, self)) return;
							var list = pilot._entities;
							if (list instanceof dorado.EntityList) list.lastPage(dorado._NULL_FUNCTION);
						}
					});
					break;
				case "goto":
					item = new GotoPageControl({
						onAction: function(self, arg) {
							if (!fireOnActionEvent.call(pilot, itemCode.code, self)) return;
							var list = pilot._entities;
							if (list instanceof dorado.EntityList) list.gotoPage(arg.pageNo, dorado._NULL_FUNCTION);
						}
					});
					break;
				case "+":
					item = new ToolBarButton({
						icon: itemCode.showIcon ? "url(>skin>common/icons.gif) -120px 0px" : null,
						caption: itemCode.showCaption ? $resource("dorado.baseWidget.DataPilotInsert") : null,
						onClick: function() {
							if (!fireOnActionEvent.call(pilot, itemCode.code, self)) return;
							var list = pilot._entities;
							if (list instanceof dorado.EntityList) list.createChild();
						}
					});
					break;
				case "-":
					item = new ToolBarButton({
						icon: itemCode.showIcon ? "url(>skin>common/icons.gif) -140px 0px" : null,
						caption: itemCode.showCaption ? $resource("dorado.baseWidget.DataPilotDelete") : null,
						onClick: function() {
							if (!fireOnActionEvent.call(pilot, itemCode.code, self)) return;
							dorado.MessageBox.confirm($resource("dorado.baseWidget.DataPilotDeleteConfirm"), function() {
								var list = pilot._entities;
								if (list instanceof dorado.EntityList && list.current) list.current.remove();
							});
						}
					});
					break;
				case "x":
					item = new ToolBarButton({
						icon: itemCode.showIcon ? "url(>skin>common/icons.gif) -280px -220px" : null,
						caption: itemCode.showCaption ? $resource("dorado.baseWidget.DataPilotCancel") : null,
						onClick: function() {
							if (!fireOnActionEvent.call(pilot, itemCode.code, self)) return;
							dorado.MessageBox.confirm($resource("dorado.baseWidget.DataPilotCancelConfirm"), function() {
								var list = pilot._entities;
								if (list instanceof dorado.EntityList && list.current) list.current.cancel();
							});
						}
					});
					break;
				case "|":
					item = new dorado.widget.toolbar.Separator();
					break;
			}
			if (item) {
				item.set("style", "float: left");
			}
			return item;
		},
		
		compileItemCodes: function() {
			var itemCodes = this._itemCodes;
			if (itemCodes && itemCodes.indexOf("pages") >= 0) {
				itemCodes = itemCodes.replace("pages", "|<,<,goto,>,>|");
			}
			
			itemCodes = (itemCodes || '').split(',');
			var compiledItemCodes = this._compiledItemCodes = [], itemCodeExpression = '';
			for (var i = 0; i < itemCodes.length; i++) {
				var itemCode = itemCodes[i], index = itemCode.indexOf('/');
				var code, showIcon = true, showCaption, options = null;
				if (index > 0) {
					code = itemCode.substring(0, index);
					var options = itemCode.substring(index + 1);
					showIcon = (options.indexOf('i') >= 0);
					showCaption = (options.indexOf('c') >= 0);
				} else {
					code = itemCode;
					showCaption = (defaultShowTextItemCodes.indexOf(code) >= 0);
				}
				if (validItemCodes.indexOf(code) >= 0) {
					itemCode = {
						code: code,
						showIcon: showIcon,
						showCaption: showCaption,
						key: code + '/' + (showIcon ? 'i' : '') + (showCaption ? 'c' : '') + i
					};
					compiledItemCodes.push(itemCode);
					if (itemCodeExpression.length > 0) itemCodeExpression += ',';
					itemCodeExpression += itemCode.key;
				}
			}
			this._itemCodeExpression = itemCodeExpression;
		},
		
		refreshItems: function() {
			if (this._itemObjects) {
				var itemCodes = this._compiledItemCodes;
				if (itemCodes) {
					for (var i = 0; i < itemCodes.length; i++) {
						this.refreshItem(itemCodes[i]);
					}
				}
			}
		},
		
		refreshItem: function(itemCode) {
			var item = this._itemObjects[itemCode.key];
			if (!item) return;
			
			var eventArg = {
				code: itemCode.code,
				control: item,
				processDefault: true
			};
			this.fireEvent("onSubControlRefresh", this, eventArg);
			if (!eventArg.processDefault) return;
			
			var list = this._entities;
			if (!(list instanceof dorado.EntityList)) list = null;
			var pageNo = list ? list.pageNo : 1, pageCount = list ? list.pageCount : 1;
			var current = list ? list.current : null;
			switch (itemCode.code) {
				case "|<":
					item.set("disabled", pageNo <= 1);
					break;
				case "<":
					item.set("disabled", pageNo <= 1);
					break;
				case ">":
					item.set("disabled", pageNo >= pageCount);
					break;
				case ">|":
					item.set("disabled", pageNo >= pageCount);
					break;
				case "goto":
					item.set({
						disabled: pageCount == 1,
						pageNo: pageNo,
						pageCount: pageCount
					});
					break;
				case "+":
					item.set("disabled", this._dataSet ? this._dataSet._readOnly : true);
					break;
				case "-":
					item.set("disabled", !current || this._dataSet._readOnly);
					break;
				case "x":
					item.set("disabled", !current ||
					(current.state != dorado.Entity.STATE_MODIFIED && current.state != dorado.Entity.STATE_NEW) ||
					this._dataSet._readOnly);
					break;
			}
		}
	});
	
	var PageButton = dorado.widget.SimpleIconButton;
	var ToolBarButton = dorado.widget.toolbar.Button;
	var GotoPageControl = $extend(dorado.widget.Control, {
		ATTRIBUTES: {
			className: {
				defaultValue: "d-goto-page"
			},
			
			pageNo: {
				defaultValue: 1
			},
			
			pageCount: {
				defaultValue: 1
			},
			
			disabled: {}
		},
		
		EVENTS: {
			onAction: {
				interceptor: function(superFire, self, arg) {
					this._pageNo = arg.pageNo;
					return superFire(self, arg);
				}
			}
		},
		
		createDom: function(dom) {
			var dom = document.createElement("SPAN");
			
			var gotoPage = this;
			
			this._labelPrefix = $DomUtils.xCreate({
				tagName: "SPAN",
				className: "text",
				style: "float: left"
			});
			dom.appendChild(this._labelPrefix);
			
			var spinner = this._spinner = new dorado.widget.NumberSpinner({
				min: 1,
				max: 1,
				value: 1,
				showSpinTrigger: false,
				trigger: {
					$type: "Trigger",
					icon: ">skin>base/goto-page.gif",
					onExecute: function(self, arg) {
						spinner.post();
						gotoPage.fireEvent("onAction", gotoPage, {
							pageNo: spinner.get("value")
						});
					}
				},
				onKeyDown: function(self, arg) {
					if (arg.keyCode == 13) {
						spinner.post();
						gotoPage.fireEvent("onAction", gotoPage, {
							pageNo: spinner.get("value")
						});
					}
					arg.returnValue = true;
				},
				width: 60,
				style: "float: left; margin-top: 1px"
			});
			spinner.render(dom);
			this.registerInnerControl(spinner);
			
			this._labelSuffix = $DomUtils.xCreate({
				tagName: "SPAN",
				className: "text",
				style: "float: left"
			});
			dom.appendChild(this._labelSuffix);
			
			return dom;
		},
		
		refreshDom: function(dom) {
			$invokeSuper.call(this, arguments);
			
			var spinner = this._spinner, pageCountChanged = false;
			if (this._pageCount != this._currentPageCount) {
				$fly(this._labelPrefix).text($resource("dorado.baseWidget.DataPilotGotoPagePrefix", this._pageCount));
				$fly(this._labelSuffix).text($resource("dorado.baseWidget.DataPilotGotoPageSuffix", this._pageCount));
				
				this._currentPageCount = this._pageCount;
				spinner.set("max", this._pageCount);
				pageCountChanged = true;
			}
			spinner.set("value", this._pageNo);
			spinner.set("readOnly", this._disabled);
		}
	});
	
})();
