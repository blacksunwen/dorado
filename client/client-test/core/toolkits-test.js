function testCreateInstance1() {
	var button = dorado.Toolkits.createInstance({
		$type: "dorado.widget.Button",
		id: "button1",
		caption: "TestButton"
	});
	assertNotNull(button);
	assertEquals("button1", button.get("id"));
	assertEquals("TestButton", button.get("caption"));
}

function testCreateInstance2() {
	var button = dorado.Toolkits.createInstance({
		$type: "Button",
		id: "button1",
		caption: "TestButton"
	}, "dorado.widget.");
	assertNotNull(button);
	assertEquals("button1", button.get("id"));
	assertEquals("TestButton", button.get("caption"));
}

function testCreateInstance3() {
	var button = dorado.Toolkits.createInstance({
		$type: "button",
		id: "button1",
		caption: "TestButton"
	}, function(type) {
		switch (type) {
			case "panel":
				return dorado.widget.Panel;
			case "button":
				return dorado.widget.Button;
			case "input":
				return dorado.widget.TextEditor;
			default:
				return dorado.widget.Control;
		}
	});
	assertNotNull(button);
	assertEquals("button1", button.get("id"));
	assertEquals("TestButton", button.get("caption"));
}
