var win = window;
var key = win.uniqueKey = (Math.random() + new Date().getTime());

function testGetOwnerWindow() {
	assertEquals(key, dorado.util.Dom.getOwnerWindow(document.body).uniqueKey);
}
