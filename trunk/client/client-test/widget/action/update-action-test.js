function createDataSet() {
	var dataProvider = dorado.DataProvider.create("provider1");
	dataProvider.loadOptions = {
		url: Test.ROOT + "resource/hr-data.js"
	};
	
	return new dorado.widget.DataSet({
		dataProvider: dataProvider
	});
}

function testCreate() {
	var dataSet = createDataSet();
	var action = new dorado.widget.UpdateAction({
		dataResolver: dorado.DataResolver.create("dataResolver1"),
		updateItems: [{
			dataSet: dataSet,
			alias: "dataSet"
		}]
	});
	
	assertEquals("dataResolver1", action.get("dataResolver").name);
	assertEquals(1, action.get("updateItems").length);
}

function testPopulateData() {
	var dataSet = createDataSet();
	var action = new dorado.widget.UpdateAction();
	action.set("updateItems", [{
		dataSet: dataSet,
		alias: "dataSet"
	}]);
	
	var dataItems = action.getResolveContext().resolveArgument.dataItems;
	assertEquals("[{\"name\":\"dataSet\"}]", dorado.JSON.stringify(dataItems));
	
	dataSet.load();
	var dataItems = action.getResolveContext().resolveArgument.dataItems;
	assertEquals("[{\"name\":\"dataSet\",\"dataItem\":[]}]", dorado.JSON.stringify(dataItems));
	
	// modified data
	var departments = dataSet.getData();
	departments.remove();
	
	departments.current.set("name", "MODIFIED");
	
	var department = new dorado.Entity();
	department.set("id", "NEW_ID");
	department.set("name", "NEW_NAME");
	departments.insert(department);
	
	var dataItems = action.getResolveContext().resolveArgument.dataItems;
	var dataSetData = dataItems[0].dataItem;
	assertTrue(dataSetData != null);
	assertEquals(dataSetData.length, 3);
	assertEquals(dataSetData[0].$state, dorado.Entity.STATE_DELETED);
	assertEquals(dataSetData[1].$state, dorado.Entity.STATE_MODIFIED);
	assertEquals(dataSetData[2].$state, dorado.Entity.STATE_NEW);
}
