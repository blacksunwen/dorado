// @Controller

// @Global
function showURL(id) {
	dorado.MessageBox.alert("http://bsdn.org/projects/dorado7/deploy/sample-center/com.bstek.dorado.sample.Main.d" +
		"#" + id);
}

// @Bind view.onReady
!function(dsExample, iframeExample) {
	var $iFrameHolder = $("#iFrameHolder");
	$iFrameHolder.css({
		width : dsExample.getData("embedWidth") || "100%",
		height : dsExample.getData("embedHeight") || 300
	}).slideDown(function() {
		$iFrameHolder.shadow({
			mode : "frame"
		});
		
		iframeExample.set({
			width : "100%",
			height : "100%",
			visible: true,
			path: dsExample.getData("url")
		});
	});
}

// @Bind #dsExample.onReady
!function(self) {
	var sources = self.getData().get("sources");
	if (sources.entityCount == 0) {
		sources.createChild({
			label : "(未定义源文件)"
		});
	}
}