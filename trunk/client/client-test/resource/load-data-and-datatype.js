{
	$dataTypeDefinitions: [{
		"name": "Department",
		"type": "entity",
		"propertyDefs": [{
			"name": "id"
		}, {
			"name": "name"
		}, {
			"name": "departments",
			"dataType": "Departments"
		}, {
			"name": "employees",
			"dataType": "Employees"
		}]
	}, {
		"name": "Employee",
		"type": "entity",
		"propertyDefs": [{
			"name": "id"
		}, {
			"name": "name"
		}, {
			"name": "sex",
			"dataType": "boolean"
		}, {
			"name": "birthday",
			"dataType": "Date"
		}, {
			"name": "salary",
			"dataType": "float"
		}, {
			"name": "comment"
		}]
	}],
	data: [{
		"id": "D1",
		"name": "部门1",
		"departments": [{
			"id": "D11",
			"name": "部门11",
			"employees": [{
				"id": "0001",
				"name": "John",
				"sex": "male",
				"salary": 5000
			}, {
				"id": "0002",
				"name": "Jerry",
				"sex": "female",
				"salary": 5500
			}, {
				"id": "0003",
				"name": "Marry",
				"sex": "female",
				"salary": 4500
			}]
		}, {
			"id": "D12",
			"name": "部门12",
			"employees": [{
				"id": "0004",
				"name": "Tom",
				"sex": "male",
				"salary": 8000
			}, {
				"id": "0005",
				"name": "Jack",
				"sex": "male",
				"salary": 6500
			}],
			"departments": [{
				"id": "D121",
				"name": "部门121",
				"employees": [{
					"id": "0006",
					"name": "Jenny",
					"sex": "female",
					"salary": 5500
				}, {
					"id": "0007",
					"name": "Teddy",
					"sex": "male",
					"salary": 7200
				}]
			}, {
				"id": "D122",
				"name": "部门122",
				"employees": [{
					"id": "0008",
					"name": "Ivy",
					"sex": "female",
					"salary": 6300
				}]
			}]
		}]
	}, {
		"id": "D2",
		"name": "部门2",
		"departments": [{
			"id": "D21",
			"name": "部门21",
			"employees": [{
				"id": "0009",
				"name": "Mike",
				"sex": "male",
				"salary": 6000
			}, {
				"id": "0010",
				"name": "Maggie",
				"sex": "female",
				"salary": 5000
			}]
		}]
	}]
}

