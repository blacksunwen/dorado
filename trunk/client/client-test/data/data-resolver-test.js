function setUpPage() {
	Test.preloadAsyncTests( [ ResolveAsync ]);
}

function testResolve() {
	var dataResolver = dorado.DataResolver.create("testResolver1");
	dataResolver.resolveOptions = {
		url: Test.ROOT + "resource/hr-data.js"
	};

	var result = dataResolver.resolve();
	assertNotNull(result);
	assertTrue(result instanceof Array);
	assertEquals(2, result.length);

	dataResolver2 = dorado.DataResolver.create("testResolver1");
	assertTrue(dataResolver == dataResolver2);
}

var ResolveAsync = {
	load: function() {
		var dataResolver = dorado.DataResolver.create();
		dataResolver.resolveOptions = {
			url: Test.ROOT + "resource/hr-data.js"
		};

		Test.asyncProcBegin("ResolveAsync");
		dataResolver.resolveAsync(null, function(result) {
			ResolveAsync.result = result;
			Test.asyncProcEnd("ResolveAsync");
		});
	},

	run: function() {
		var result = this.result;
		assertNotNull(result);
		assertTrue(result instanceof Array);
		assertEquals(2, result.length);
	}
};

function testResolveAsync() {
	ResolveAsync.run();
}