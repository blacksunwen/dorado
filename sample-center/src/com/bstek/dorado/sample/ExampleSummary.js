// @Controller

// @Global
function showURL() {
	dorado.MessageBox.alert(location.href);
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
	var id = self.getData("id");
	$("a").attr("target", "doc_" + id);
	var sources = self.getData().get("sources");
	if (sources.entityCount == 0) {
		sources.createChild({
			label : "(未定义源文件)"
		});
	}
}