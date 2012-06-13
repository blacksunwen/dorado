var tabSeed = 0;

function initNewTabOption() {
	view.set("#defaultFormProfile.entity", {
		caption : "NewTab" + (++tabSeed),
		type : "control",
		closeable : true,
		path : "http://www.bsdn.org"
	});
	view.set("^iFrameProperty.readOnly", true);
}

// @Bind #buttonTabPosition.onClick
!function(tabControl) {
	var tabPlacement = tabControl.get("tabPlacement");
	tabControl.set("tabPlacement", (tabPlacement == "top") ? "bottom" : "top");
}

// @Bind #buttonChangeCurrent.onClick
!function(tabControl) {
	var index = tabControl.get("currentIndex"), count = tabControl.get("tabs").size;
	index++;
	if (index >= count) {
		index = 0;
	}
	tabControl.set("currentIndex", index);
}

// @Bind #buttonRemoveTab.onClick
!function(tabControl) {
	var tab = tabControl.get("currentTab");
	if (tab) {
		tab.close();
	}
}

// @Bind #buttonAddTab.onClick
!function(floatPanel) {
	initNewTabOption();
	floatPanel.show();
}

// @Bind #radioGroupType.onValueChange
!function(self) {
	view.set("^iFrameProperty.readOnly", self.get("value") != "IFrame");
}

// @Bind #buttonOK.onClick
!function(tabControl, defaultFormProfile) {
	var options = defaultFormProfile.get("entity");
	var newTab;
	if (options.type == "IFrame") {
		newTab = tabControl.addTab({
			$type : "IFrame",
			caption : options.caption,
			closeable : options.closeable,
			path : options.path
		});
	} else {
		var color = Math.round(Math.random() * Math.pow(16, 6)).toString(16);
		newTab = tabControl.addTab({
			$type : "Control",
			caption : options.caption,
			closeable : options.closeable,
			control : {
				$type : "Control",
				style : {
					background : ("#" + color)
				}
			}
		});
	}
	tabControl.set("currentTab", newTab);
}

// @Bind #buttonOK.onClick
!function(self) {
	self.findParent(dorado.widget.FloatPanel).hide();
}

// @Bind #menuTab.&closeAll.onClick
!function(tabControl) {
	var tabs = [];
	tabControl.get("tabs").each(function(tab) {
		if (tab.get("closeable")) tabs.push(tab);
	});
	tabs.each(function(tab) {
		tab.close();
	});
}