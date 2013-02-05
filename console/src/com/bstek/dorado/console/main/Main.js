/**
 * @author Alex Tong (mailto:alex.tong@bstek.com)
 * @since Nov 20,2012
 */
// @Bind view.onReady
!function(self, arg) {
	userData = view.get("userData");
	view.get('#startupTime').set("text", userData.startTime);
	var visible = userData.buttonLogoutVisible;
	view.get('#buttonLogout').set("visible", visible);
	if (!visible) {
		view.get('#startupTime').set('style', "right:10px;");
		view.get('#startupTimeLabel').set('style', "right:130px;");
	}
}

// @Bind ^menuBtn.onBlockClick
!function(self, arg) {
	var label = arg.data.label, parentCaption = self
			.get('parent.parent.caption');
	view.get('#framePanel').set('caption', parentCaption + '->' + label)
	var path = '>com.bstek.dorado.console.' + arg.data.path;
	view.get('#detailInfoSubView').set('path', path)
}

// @Bind #logoutAction.onSuccess
!function(self, arg) {
	window.location.reload();
}

// @Bind #systemeBlockView.onReady
!function(self, arg) {
	self.set("items", [ {
		label : "${res.system}",
		path : "runtime.SystemProperty.d",
		photo : ">dorado/console/images/computer.png"
	}, {
		label : "${res.doradoConfig}",
		path : "runtime.Configure.d",
		photo : ">dorado/console/images/dorado7.jpg"
	} ]);
	self.set("renderer", new dorado.widget.blockview.ImageBlockRenderer({
		captionProperty : "label",
		imageProperty : "photo"
	}));
}

// @Bind #parserTreeBlockView.onReady
!function(self, arg) {
	self.set("items", [ {
		label : "View Tree",
		path : "parser.ViewTree.d",
		photo : ">dorado/console/images/dorado7.jpg"
	}, {
		label : "Model Tree",
		path : "parser.ModelTree.d",
		photo : ">dorado/console/images/dorado7.jpg"
	}, {
		label : "Component Tree",
		path : "parser.ComponentTree.d",
		photo : ">dorado/console/images/dorado7.jpg"
	} ]);
	self.set("renderer", new dorado.widget.blockview.ImageBlockRenderer({
		captionProperty : "label",
		imageProperty : "photo"
	}));

}

// @Bind #logBlockView.onReady
!function(self, arg) {
	self.set("items", [ {
		label : "${res.consoleLog}",
		path : "system.log.console.SystemOutMonitor.d",
		photo : ">dorado/console/images/console.png"
	}, {
		label : "${res.fileLog}",
		path : "system.log.file.FileReaderMain.d",
		photo : ">dorado/console/images/file.png"
	} ]);
	self.set("renderer", new dorado.widget.blockview.ImageBlockRenderer({
		captionProperty : "label",
		imageProperty : "photo"
	}));

}

// @Bind #commonBlockView.onReady
!function(self, arg) {
	self.set("items", [ {
		label : "${res.globalObject}",
		path : "web.GlobalObject.d",
		photo : ">dorado/console/images/dorado7.jpg"
	}, {
		label : "Expos",
		path : "web.DoradoExpos.d",
		photo : ">dorado/console/images/dorado7.jpg"
	} ]);
	self.set("renderer", new dorado.widget.blockview.ImageBlockRenderer({
		captionProperty : "label",
		imageProperty : "photo"
	}));
}

// @Bind #webBlockView.onReady
!function(self, arg) {
	self.set("items", [ {
		label : "Packages",
		path : "web.PackagesConfig.d",
		photo : ">dorado/console/images/dorado7.jpg"
	}, {
		label : "Outputter",
		path : "web.Outputter.d",
		photo : ">dorado/console/images/dorado7.jpg"
	} ]);
	self.set("renderer", new dorado.widget.blockview.ImageBlockRenderer({
		captionProperty : "label",
		imageProperty : "photo"
	}));
}

// @Bind #performanceBlockView.onReady
!function(self, arg) {
	self.set("items", [ {
		label : "View",
		path : "performance.view.Performance.d?type=CreateDoradoView",
		photo : ">dorado/console/images/dorado7.jpg"
	}, {
		label : "Action",
		path : "performance.view.Performance.d?type=RemoteService",
		photo : ">dorado/console/images/dorado7.jpg"
	}, {
		label : "DataResolve",
		path : "performance.view.Performance.d?type=DataResolve",
		photo : ">dorado/console/images/dorado7.jpg"
	}, {
		label : "DataProvider",
		path : "performance.view.Performance.d?type=DataProviderGetResult",
		photo : ">dorado/console/images/dorado7.jpg"
	} ]);
	self.set("renderer", new dorado.widget.blockview.ImageBlockRenderer({
		captionProperty : "label",
		imageProperty : "photo"
	}));
}

// @Bind #addonBlockView.onReady
!function(self, arg) {
	self.set("items", [ {
		label : "Addons",
		path : "addon.DoradoAddon.d",
		photo : ">dorado/console/images/dorado7.jpg"
	} ]);
	self.set("renderer", new dorado.widget.blockview.ImageBlockRenderer({
		captionProperty : "label",
		imageProperty : "photo"
	}));

}

// @Bind #othersBlockView.onReady
!function(self, arg) {
	self.set("items", [ {
		label : "${res.version}",
		path : "others.Version.d",
		photo : ">dorado/console/images/dorado7.jpg"
	} ]);
	self.set("renderer", new dorado.widget.blockview.ImageBlockRenderer({
		captionProperty : "label",
		imageProperty : "photo"
	}));
}