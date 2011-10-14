/**
 * @author Frank Zhang (mailto:frank.zhang@bstek.com)
 * @component Base
 * @class 标签页。
 * @extends dorado.widget.TabBar
 */
dorado.widget.TabControl = $extend(dorado.widget.TabBar, /** @scope dorado.widget.TabControl.prototype **/ {
	$className: "dorado.widget.TabControl",
	
	ATTRIBUTES: /** @scope dorado.widget.TabControl.prototype **/ {
		height: {
			defaultValue: 200,
			independent: false,
			readOnly: false
		}
	},
	
	constructor: function() {
		$invokeSuper.call(this, arguments);
		
		this._cardBook = new dorado.widget.CardBook();
		this.registerInnerControl(this._cardBook);
	},
	
	doChangeCurrentTab: function(tab) {
		var result = $invokeSuper.call(this, arguments);

        if(!result) return false;
		
		var tabControl = this, tabs = tabControl._tabs, index = typeof tab == "number" ? tab : tabs.indexOf(tab), card = tabControl._cardBook;
		if (card) {
			card.set("currentControl", index);
		}
        
        return true;
	},
	
	doChangeTabPlacement: function(value) {
		var result = $invokeSuper.call(this, arguments);
		
		if (!result) {
			return false;
		}
		
		var tabcontrol = this, dom = tabcontrol._dom;
		if (dom) {
			var tabbarDom = tabcontrol._tabbarDom, cardDom = tabcontrol._cardBook._dom;
			if (dorado.Browser.msie && dorado.Browser.version == 6){
				if (value == "top") {
					dom.appendChild(cardDom);
				} else {
					dom.insertBefore(cardDom, tabbarDom);
				}				
			} else {
				if (value == "top") {
					dom.insertBefore(tabbarDom, cardDom);
				} else {
					dom.appendChild(tabbarDom);
				}
			}
		}
		
		return true;
	},
	
	doRemoveTab: function(tab) {
		var tabcontrol = this, tabs = tabcontrol._tabs, index = typeof tab == "number" ? tab : tabs.indexOf(tab), card = tabcontrol._cardBook;
		
		if (card) {
			card.removeControl(index);
		}
		
		$invokeSuper.call(this, arguments);
	},
	
	doAddTab: function(tab, index, current) {
		$invokeSuper.call(this, arguments);
		
		var tabcontrol = this, card = tabcontrol._cardBook, tabs = tabcontrol._tabs;
		
		if (index == null) {
			index = tabs.indexOf(tab);
		}
		
		if (card) {
			card.addControl(tab.getControl(), index, current);
		}
	},
	
	createDom: function() {
		var tabcontrol = this, card = tabcontrol._cardBook, dom = document.createElement("div"),
			tabbarDom = $invokeSuper.call(this, arguments), tabPlacement = tabcontrol._tabPlacement;

		if (tabPlacement == "top") {
			dom.appendChild(tabbarDom);
		}

		tabcontrol._tabbarDom = tabbarDom;
		
		var controls = [], tabs = tabcontrol._tabs;
		for (var i = 0, j = tabs.size; i < j; i++) {
			var tab = tabs.get(i);
			controls.push(tab.getControl());
		}
		card.set("controls", controls);
		card.render(dom);

		if (tabPlacement == "bottom") {
			dom.appendChild(tabbarDom);
		}

		return dom;
	},
	
	refreshDom: function(dom) {
		var tabcontrol = this, card = tabcontrol._cardBook, tabbarDom = tabcontrol._tabbarDom, cardDom = tabcontrol._cardBook._dom;
		
		$invokeSuper.call(this, [tabbarDom]);
		
		$fly(tabbarDom).css("height", "auto");
		
		if (tabcontrol._height != null) {
			card._realHeight = tabcontrol.getRealHeight() - $fly(tabbarDom).height();
			card._realWidth = tabcontrol.getRealWidth();
		}
		
		var tabs = tabcontrol._tabs, currentTab = tabcontrol._currentTab, currentTabIndex = tabs.indexOf(currentTab);
		// console.log("tabcontrol._currentTab:" + tabcontrol._currentTab + "\tcurrentTabIndex:" + currentTabIndex + "\tcontrols size:" + this._card._controls.size + "\titem:" + this._card._controls.get(currentTabIndex));
		if (currentTabIndex != -1) {
			card._currentControl = card._controls.get(currentTabIndex);
		}
		card.refreshDom(cardDom);
	},
	
	getFocusableSubControls: function() {
		return [this._cardBook];
	},
	
	setFocus: function() {
		// 放置在IE容器滚动条的意外滚动
		var dom = this._tabbarDom;
		if (dom) setTimeout(function() {
			try {
				dom.focus();
			} 
			catch (e) {
			}
		}, 0);
	}
});
