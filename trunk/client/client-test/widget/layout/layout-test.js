function testChildren() {
	var layout = new dorado.widget.layout.Layout();
	var c1 = new dorado.widget.Component();
	var c2 = new dorado.widget.Component();
	var c3 = new dorado.widget.Component();

	assertEquals(0, layout._regions.size);

	layout.addControl(c1, "C1");
	assertEquals(1, layout._regions.size);

	layout.addControl(c2, "C2");
	layout.addControl(c3, "C3");
	assertEquals(3, layout._regions.size);

	layout.removeControl(c2);
	assertEquals(2, layout._regions.size);
}
