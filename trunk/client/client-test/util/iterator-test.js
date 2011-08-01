var v = [ 89, 35, 12, 785, 578, 54, 13, 589, 1452 ];

function test1() {
	var it = new dorado.util.ArrayIterator(v);

	var i = 0;
	while (it.hasNext()) {
		assertEquals(v[i], it.next());
		i++;
	}
	assertEquals(v.length, i);
}

function test2() {
	var it = new dorado.util.ArrayIterator(v);

	var i = v.length;
	it.last();
	while (it.hasPrevious()) {
		i--;
		assertEquals(v[i], it.previous());
	}
	assertEquals(0, i);

	it.first();
	i = 0;
	while (it.hasNext()) {
		assertEquals(v[i], it.next());
		i++;
	}
	assertEquals(v.length, i);
}
