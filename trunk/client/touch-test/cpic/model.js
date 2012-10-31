var dataTypePriceList = new dorado.EntityDataType({
	name: "PriceList",
	propertyDefs: [
		{ name: "vehicleCode" },
		{ name: "RVehicleName" },
		{ name: "vehicleDescription" },
		{ name: "RVehicleBrand" },
		{ name: "RLimitLoadPerson" },
		{ name: "RVehicleTonnage" },
		{ name: "vehiclePrice" },
		{ name: "RVehicleFamily" },
		{ name: "RExhaustCapacity" },
		{ name: "RMarketDate" },
		{ name: "riskFlag" }
	]
});
$dataTypeRepository.register(dataTypePriceList);
var dataTypeModel = new dorado.EntityDataType({
	name: "Model",
	propertyDefs: [
		{ name: "searchSequenceNo" },
		{ name: "vehicleLicense" },
		{ name: "carMark" },
		{ name: "vehicleType" },
		{ name: "rackNo" },
		{ name: "engineNo" },
		{ name: "owner" },
		{ name: "vehicleRegisterDate" },
		{
			name: "vehiclePriceList",
			dataType: "PriceList"
		}
	]
});
$dataTypeRepository.register(dataTypeModel);
var dataSetModel = new dorado.widget.DataSet({
	loadMode: "never",
	dataType: "Model"
});

var dataTypeBasic = new dorado.EntityDataType({
	name: "Basic",
	propertyDefs: [
		{ name: "lastyearEndDate" },
		{ name: "currentStartDate" },
		{ name: "currentEndDate" },
		{ name: "lastyearEndDateAuto" },
		{ name: "currentStartDateAuto" },
		{ name: "currentEndDateAuto" },
		{
			name: "claimCountAutoComprenhensive"
		},
		{
			name: "claimAmtAutoComprenhensive"
		},
		{
			name: "claimCountTraffic"
		},
		{
			name: "claimAmtTraffic"
		},
		{ name: "vehicleLicense" },
		{ name: "licenseType" },
		{ name: "licenseOwner" },
		{ name: "makerModel" },
		{ name: "usageLimitYear" },
		{ name: "registerDate" },
		{ name: "vehicleOwner" },
		{ name: "vehicleUsage" },
		{ name: "currentValue" },//当前价值
		{ name: "purchasePrice" },//购置价
		{ name: "passengerSeats" }//座位数
	]
});
$dataTypeRepository.register(dataTypeBasic);
var dataSetBasic = new dorado.widget.DataSet({
	loadMode: "never",
	dataType: "Basic"
});

var dataTypeResult = new dorado.EntityDataType({
	name: "Result",
	propertyDefs: [
		{ name: "vehicleLicense" },
		{ name: "vehicleUsage" },
		{ name: "vehicleVariety" },
		{ name: "purchasePrice" },
		{ name: "usageLimitYear" },
		{ name: "currentStartDate" },
		{ name: "currentEndDate" },
		{ name: "currentStartDateAuto" },
		{ name: "currentEndDateAuto" },
		{ name: "totalFeeAuto" },
		{ name: "totalFeeTraffic" },
		{ name: "taxAmt" },
		{ name: "totalFeeAutoStandard" },
		{ name: "floatingRate" },
		{ name: "allFeeSum" }
	]
});
$dataTypeRepository.register(dataTypeResult);
var dataSetResult = new dorado.widget.DataSet({
	loadMode: "never",
	dataType: "Result"
});

var dataTypeLogin = new dorado.EntityDataType({
	name: "Login",
	propertyDefs: [
		{ name: "branchCode" },
		{ name: "userCode" },
		{ name: "failed" }
	]
});
$dataTypeRepository.register(dataTypeLogin);
var dataSetLogin = new dorado.widget.DataSet({
	loadMode: "never",
	dataType: "Login"
});
