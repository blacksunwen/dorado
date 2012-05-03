var traffic = "TRAFFICCOMPULSORYPRODUCT",//交强险
	damage = "DAMAGELOSSCOVERAGE", //机动车损失险
	damageAmount = damage + "AMOUNT",//机动车损失险 保额
	damageSepcial = "DAMAGELOSSEXEMPTDEDUCTIBLESPECIALCLAUSE",//机动车损失险不计免赔
	glass = "GLASSBROKENCOVERAGE",//玻璃破碎险
	glassKind = glass + "KIND",//玻璃产地
	paint = "CARBODYPAINTCOVERAGE",//车身油漆
	paintAmount = paint + "AMOUNT",//车身油漆 保额
	paddle = "PADDLEDAMAGECOVERAGE", //涉水损失险
	theft = "THEFTCOVERAGE", //全车盗抢损失险
	theftAmount = theft + "AMOUNT",//全车盗抢损失险 保额
	theftSpecial = "THEFTCOVERAGEEXEMPTDEDUCTIBLESPECIALCLAUSE", //全车盗抢不计免赔
	third = "THIRDPARTYLIABILITYCOVERAGE", //第三者责任险
	thirdAmount = third + "AMOUNT",//第三者责任险保额
	thirdAmountOther = thirdAmount + "OTHER",//第三者责任险其他保额
	thirdSpecial = "THIRDPARTYLIABILITYEXEMPTDEDUCTIBLESPECIALCLAUSE", //第三者责任险不计免赔
	driver = "INCARDRIVERLIABILITYCOVERAGE", //司机
	driverAmount = driver + "AMOUNT",//司机保额
	passenger = "INCARPASSENGERLIABILITYCOVERAGE", //乘客
	passengerAmount = passenger + "AMOUNT",//乘客保额
	incar = "INCARPERSONLIABILITYCOVERAGE",//车上人员责任险，不提交。
	incarSpecial = "INCARPERSONLIABILITYEXEMPTDEDUCTIBLESPECIALCLAUSE", //车上人员责任险不急免赔
	rider = "RIDEREXEMPTDEDUCTIBLESPECIALCLAUSE",//附加险不计免赔特约
	selfignite="SELFIGNITECOVERAGE",//自燃损失险
	selfigniteAmount = selfignite + "AMOUNT",//自燃损失险保额
    carDamageDeductible = "CARDAMAGEDEDUCTIBLESPECIALCLAUSE",//车损免赔额特约
    carDamageDeductibleAmount = carDamageDeductible + "AMOUNT",//车损免赔额特约 保额
    cargoIncar = "CARGOINCARLIABILITYCOVERAGE",//车上货物责任险
    cargoIncarAmount = cargoIncar + "AMOUNT",//车上货物责任险 保额
    specialCarDamage = "SPECIALCARDAMAGEEXTENDCOVERAGE",//特种车车辆损失扩展险
    appointedRepairFactory = "APPOINTEDREPAIRFACTORYSPECIALCLAUSE";//指定专修厂特约

(function() {
	var hasError = function(result) {
		if (result && result.error != undefined ) {
			var error = result.error;
			dorado.MessageBox.alert(error.indexOf(":") == -1 ? error : error.substr(error.indexOf(":") + 2));
			return true;
		}
		if(result && result.loginTimeOut !=undefined){
			dorado.MessageBox.alert("登录超时，请重新登录！", function(){window.location.reload();} );
			return true;
		}
		return false;
	};

    var allCoverages = [
        "TRAFFICCOMPULSORYPRODUCT",
        "DAMAGELOSSCOVERAGE",
        "DAMAGELOSSEXEMPTDEDUCTIBLESPECIALCLAUSE",
        "GLASSBROKENCOVERAGE",
        "CARBODYPAINTCOVERAGE",
        "PADDLEDAMAGECOVERAGE",
        "THEFTCOVERAGE",
        "THEFTCOVERAGEEXEMPTDEDUCTIBLESPECIALCLAUSE",
        "THIRDPARTYLIABILITYCOVERAGE",
        "THIRDPARTYLIABILITYEXEMPTDEDUCTIBLESPECIALCLAUSE",
        "INCARDRIVERLIABILITYCOVERAGE",
        "INCARPASSENGERLIABILITYCOVERAGE",
        "INCARPERSONLIABILITYCOVERAGE",
        "INCARPERSONLIABILITYEXEMPTDEDUCTIBLESPECIALCLAUSE",
        "RIDEREXEMPTDEDUCTIBLESPECIALCLAUSE",
        "SELFIGNITECOVERAGE"
    ];

    var allCoveragesMap = {};

    for (var i = 0, j = allCoverages.length; i < j; i++) {
        allCoveragesMap[allCoverages[i]] = true;
    }

	var leftCoverages = [
		{
			name: traffic,
			label: "交强险及车船税",
			twoColumn: true,
			children: [
				{
					name: traffic,
					label: "应交保费"
				}
			]
		},
		{
			name: damage,
			label: "机动车损失险",
			children: [
				{
					name: damage,
					label: function(view) {
						return "保额 " + parseInt(view.id(damageAmount).get("text"), 10) + "元";
					}
				},
				{
					name: damageSepcial,
					label: "不计免赔"
				},
				{
					name: glass,
					label: "玻璃单独破碎险"
				},
				{
					name: paint,
					label: "车身油漆单独损伤险"
				},
				{
					name: paddle,
					label: "涉水损失险"
				},
				{
					name: selfignite,
					label: "自燃损失险"
				},
                {
                    name: carDamageDeductible,
                    label: "车损免赔额特约"
                },
                {
                    name: specialCarDamage,
                    label: "特种车车辆损失扩展险"
                },
                {
                    name: appointedRepairFactory,
                    label: "指定专修厂特约"
                }
			]
		}
	];

	var rightCoverages = [
        {
            name: theft,
            label: "机动车全车盗抢损失险",
            children: [
                {
                    name: theft,
                    label: function(view) {
                        return "保额 " + parseInt(view.id(theftAmount).get("text"), 10) + "元";
                    }
                },
                {
                    name: theftSpecial,
                    label: "不计免赔"
                }
            ]
        },
		{
			name: third,
			label: "机动车第三者责任险",
			children: [
				{
					name: third,
					label: function(view) {
						var thirdAmountEditor = view.id(thirdAmount),
							thirdAmountOtherEditor = view.id(thirdAmountOther);
						var suminsured = thirdAmountEditor.get("value");
						if (suminsured == "other") {
							suminsured = parseInt(thirdAmountOtherEditor.get("value"), 10) * 10000;
						}
						return "保额 " + suminsured + "元";
					}
				},
				{
					name: thirdSpecial,
					label: "不计免赔"
				},
                {
                    name: cargoIncar,
                    label: "车上货物责任险"
                }
			]
		},
		{
			name: incar,
			label: "车上人员责任险",
			possible: [driver, passenger, incarSpecial],
			children: [
				{
					name: driver,
					label: "司机"
				},
				{
					name: passenger,
					label: "乘客"
				},
				{
					name: incarSpecial,
					label: "不计免赔"
				}
			]
		},
		{
			name: rider,
			label: "附加险不计免赔险",
			children: [
				{
					name: rider,
					label: ""
				}
			]
		}
	];

	var submitMap = {
		coverageName: "COVERAGENAME",
		suminsured: "SUMINSURED",
		glassManufacturer: "GLASSMANUFACTURER",
		passengerseats: "PASSENGERSEATS"
	};

	var thirdMap = { "5": true, "10": true, "15": true, "20": true, "30“: true, ”50": true, "100": true };
	var triColTpl = "<table class='fee-table' cellpadding='5' cellspacing='0'>{{#table}}<tr><td>{{name}}</td><td width=80>应交保费</td><td width=100 align='right' class='amount'>{{fee}}元</td></tr>{{/table}}</table>";
	var twoColTpl = "<table class='fee-table' cellpadding='5' cellspacing='0'>{{#table}}<tr><td>{{name}}</td><td width=100 align='right' class='amount'>{{fee}}</td></tr>{{/table}}</table>";

	var verifyCoverage = function() {
		var view = controller.view, data = dataSetBasic.get("data"), currentValue = data.get("currentValue"), purchasePrice = data.get("purchasePrice"),
			theftMsg = '全车盗抢险"保额/限额"不能高于实际价值！实际价值为：' + currentValue + '元',
			selfigniteMsg = '自燃损失险"保额/限额"不能高于实际价值！实际价值为：' + currentValue + '元',
			damageMsg = '车辆损失险"保额/限额"不能低于新车购置价的20%，不能高于新车购置价!\n新车购置价为' + purchasePrice + '元。',
			thirdMsg = '第三者责任险"保额/限额"只能选择5、10、15、20、30、50、100、100万以上（50万的倍数）其中一档!';
		//车辆损失险
		var damageEditor = view.id(damage), damageAmountEditor = view.id(damageAmount);
		if (damageEditor.get("checked")) {
			var damageAmountValue = parseInt(damageAmountEditor.get("text"), 10);
			if (!isNaN(damageAmountValue)) {
				if (damageAmountValue > purchasePrice || damageAmountValue < purchasePrice * 0.8) {
					dorado.MessageBox.alert(damageMsg);
					return false;
				}
			} else {
				dorado.MessageBox.alert("请填写机动车损失险的保额！");
				return false;
			}

			var selfigniteEditor = view.id(selfignite), selfigniteAmountEditor = view.id(selfigniteAmount);
			if (selfigniteEditor.get("checked")) {
				var selfigniteAmountValue = parseInt(selfigniteAmountEditor.get("text"), 10);
				if (!isNaN(selfigniteAmountValue)) {
					if (selfigniteAmountValue > currentValue) {
						dorado.MessageBox.alert(selfigniteMsg);
						return false;
					}
				} else {
					dorado.MessageBox.alert("请填写自燃损失险的保额！");
					return false;
				}
			}
		}

		//全车盗抢损失险
		var theftEditor = view.id(theft), theftAmountEditor = view.id(theftAmount);
		if (theftEditor.get("checked")) {
			var theftAmountValue = parseInt(theftAmountEditor.get("text"), 10);
			if (!isNaN(theftAmountValue)) {
				if (theftAmountValue) {
					if (theftAmountValue > currentValue) {
						dorado.MessageBox.alert(theftMsg);
						return false;
					}
				}
			} else {
				dorado.MessageBox.alert("请填写全车盗抢损失险的保额！");
				return false;
			}
		}

		//第三者责任险
		var thirdEditor = view.id(third), thirdAmountEditor = view.id(thirdAmount),
			thirdAmountOtherEditor = view.id(thirdAmountOther);
		if (thirdEditor.get("checked")) {
			var suminsured = thirdAmountEditor.get("value");
			if (suminsured == "other") {
				suminsured = parseInt(thirdAmountOtherEditor.get("text"), 10);
				if (isNaN(suminsured)) {
					dorado.MessageBox.alert("请填写第三者责任险的保额！");
					return false;
				}
				if ((suminsured > 50 && suminsured % 50 != 0) || (suminsured < 50 && !(suminsured in thirdMap)) ) {
					dorado.MessageBox.alert(thirdMsg);
					return false;
				}
			}

            //车上货物责任险
            var cargoIncarEditor = view.id(cargoIncar), cargoIncarAmountEditor = view.id(cargoIncarAmount);
            if (cargoIncarEditor.get("checked")) {
                var cargoIncarAmountValue = parseInt(cargoIncarAmountEditor.get("text"), 10);
                if (isNaN(cargoIncarAmountValue)) {
                    dorado.MessageBox.alert("请填写车上货物责任险的保额！");
                    return false;
                }
            }
		}

		//车上人员责任险，不提交
		var incarEditor = view.id(incar);
		if (incarEditor.get("checked")) {
			//司机
			var driverEditor = view.id(driver), driverAmountEditor = view.id(driverAmount);
			if (driverEditor.get("checked")) {
				var driverAmountSuminsured = parseInt(driverAmountEditor.get("text"), 10);
				if (isNaN(driverAmountSuminsured)) {
					dorado.MessageBox.alert("请填写司机的保额！");
					return false;
				}
			}

			//乘客
			var passengerEditor = view.id(passenger), passengerAmountEditor = view.id(passengerAmount);
			if (passengerEditor.get("checked")) {
				var passengerAmountSuminsured = parseInt(passengerAmountEditor.get("text"), 10);
				if (isNaN(passengerAmountSuminsured)) {
					dorado.MessageBox.alert("请填写乘客的保额！");
					return false;
				}
			}
		}

		return true;
	};

	var getCoverageCollection = function() {
		var result = [], record, view = controller.view;

		//交强险
		var trafficEditor = view.id(traffic);
		if (trafficEditor.get("checked")) {
			record = {};
			record[submitMap["coverageName"]] = traffic;
			result.push(record);
		}

		//机动车损失险
		var damageEditor = view.id(damage), damageAmountEditor = view.id(damageAmount);
		if (damageEditor.get("checked")) {
			record = {};
			record[submitMap["coverageName"]] = damage;
			record[submitMap["suminsured"]] = "" + damageAmountEditor.get("text");
			result.push(record);

			//机动车损失险不计免赔
			var damageSepcialEditor = view.id(damageSepcial);
			if (damageSepcialEditor.get("checked")) {
				record = {};
				record[submitMap["coverageName"]] = damageSepcial;
				result.push(record);
			}

			//玻璃破碎险
			var glassEditor = view.id(glass), glassKindEditor = view.id(glassKind);
			if (glassEditor.get("checked")) {
				record = {};
				record[submitMap["coverageName"]] = glass;
				record[submitMap["glassManufacturer"]] = "" + glassKindEditor.get("value");
				result.push(record);
			}

			//车身油漆
			var paintEditor = view.id(paint), paintAmountEditor = view.id(paintAmount);
			if (paintEditor.get("checked")) {
				record = {};
				record[submitMap["coverageName"]] = paint;
				record[submitMap["suminsured"]] = "" + paintAmountEditor.get("value");
				result.push(record);
			}

			//涉水损失险
			var paddleEditor = view.id(paddle);
			if (paddleEditor.get("checked")) {
				record = {};
				record[submitMap["coverageName"]] = paddle;
				result.push(record);
			}

			//自燃损失险
			var selfigniteEditor = view.id(selfignite), selfigniteAmountEditor = view.id(damageAmount);
			if (selfigniteEditor.get("checked")) {
				record = {};
				record[submitMap["coverageName"]] = selfignite;
				record[submitMap["suminsured"]] = "" + selfigniteAmountEditor.get("text");
				result.push(record);
			}

            //车损免赔额特约
            var carDamageDeductibleEditor = view.id(carDamageDeductible), carDamageDeductibleAmountEditor = view.id(carDamageDeductibleAmount);
            if (carDamageDeductibleEditor.get("checked")) {
                record = {};
                record[submitMap["coverageName"]] = carDamageDeductible;
                record[submitMap["suminsured"]] = "" + carDamageDeductibleAmountEditor.get("value");
                result.push(record);
            }

            //涉水损失险
            var specialCarDamageEditor = view.id(specialCarDamage);
            if (specialCarDamageEditor.get("checked")) {
                record = {};
                record[submitMap["coverageName"]] = specialCarDamage;
                result.push(record);
            }

            //指定专修厂特约
            var appointedRepairFactoryEditor = view.id(appointedRepairFactory);
            if (appointedRepairFactoryEditor.get("checked")) {
                record = {};
                record[submitMap["coverageName"]] = appointedRepairFactory;
                result.push(record);
            }
		}

		//全车盗抢损失险
		var theftEditor = view.id(theft), theftAmountEditor = view.id(theftAmount);
		if (theftEditor.get("checked")) {
			record = {};
			record[submitMap["coverageName"]] = theft;
			record[submitMap["suminsured"]] = "" + theftAmountEditor.get("value");
			result.push(record);

			//全车盗抢损失险不计免赔
			var theftSpecialEditor = view.id(theftSpecial);
			if (theftSpecialEditor.get("checked")) {
				record = {};
				record[submitMap["coverageName"]] = theftSpecial;
				result.push(record);
			}
		}

		//第三者责任险
		var thirdEditor = view.id(third), thirdAmountEditor = view.id(thirdAmount),
			thirdAmountOtherEditor = view.id(thirdAmountOther);
		if (thirdEditor.get("checked")) {
			record = {};
			record[submitMap["coverageName"]] = third;
			var suminsured = thirdAmountEditor.get("value");
			if (suminsured == "other") {
				suminsured = parseInt(thirdAmountOtherEditor.get("value"), 10) * 10000;
			}
			record[submitMap["suminsured"]] = "" + parseInt(suminsured, 10);
			result.push(record);

			//第三者责任险不计免赔
			var thirdSpecialEditor = view.id(thirdSpecial);
			if (thirdSpecialEditor.get("checked")) {
				record = {};
				record[submitMap["coverageName"]] = thirdSpecial;
				result.push(record);
			}

            //车上货物责任险
            var cargoIncarEditor = view.id(cargoIncar), cargoIncarAmountEditor = view.id(cargoIncarAmount);
            if (cargoIncarEditor.get("checked")) {
                record = {};
                record[submitMap["coverageName"]] = cargoIncar;
                record[submitMap["suminsured"]] = "" + cargoIncarAmountEditor.get("value");
                result.push(record);
            }
		}

		//车上人员责任险，不提交
		var incarEditor = view.id(incar);
		if (incarEditor.get("checked")) {
			//司机
			var driverEditor = view.id(driver), driverAmountEditor = view.id(driverAmount);
			if (driverEditor.get("checked")) {
				record = {};
				record[submitMap["coverageName"]] = driver;
				record[submitMap["suminsured"]] = "" + driverAmountEditor.get("value");
				result.push(record);
			}

			//乘客
			var passengerEditor = view.id(passenger), passengerAmountEditor = view.id(passengerAmount);
			if (passengerEditor.get("checked")) {
				record = {};
				record[submitMap["coverageName"]] = passenger;
				record[submitMap["suminsured"]] = "" + passengerAmountEditor.get("value");
				record[submitMap["passengerseats"]] = dataSetBasic.get("data").get("passengerSeats");
				result.push(record);
			}

			//车上人员责任险不计免赔
			var incarSpecialEditor = view.id(incarSpecial);
			if (incarSpecialEditor.get("checked")) {
				record = {};
				record[submitMap["coverageName"]] = incarSpecial;
				result.push(record);
			}
		}

		//附加险不计免赔特约
		var riderEditor = view.id(rider);
		if (riderEditor.get("checked")) {
			record = {};
			record[submitMap["coverageName"]] = rider;
			result.push(record);
		}

		return result;
	};

	var getLeftHtml = function(coverageMap) {
		var leftHtml = "";
		for (var i = 0, j = leftCoverages.length ; i < j; i++) {
			var coverageConfig = leftCoverages[i], coverageName = coverageConfig.name;
			if (coverageMap[coverageName]) {
				leftHtml += '<div class="i-label d-label block-el fee-title">' + coverageConfig["label"] + "</div>\n";
				var coverageChildren = coverageConfig.children, table = [];
				if (coverageChildren) {
					for (var k = 0, l = coverageChildren.length; k < l; k++) {
						var child = coverageChildren[k], childName = child.name;
						if (coverageMap[childName]) {
							table.push({
								name: typeof child.label == "function" ? child.label(controller.view) : child.label,
								fee: coverageMap[childName]["POLICYPREMIUM"]
							});
						}
					}
					if (coverageConfig.twoColumn) {
						var taxInfo = coverageMap[coverageName]["TAXINFO"];
						jQuery.each(table, function(index, item) {
							if (item.fee) {
								item.fee += "元";
							}
						});
						if (taxInfo) {
							table.push({
								name: "需缴纳的车船使用税:",
								fee: ""
							});
							table.push({
								name: "当年应缴",
								fee: taxInfo.taxAmt + "元"
							});
							table.push({
								name: "往年补缴",
								fee: taxInfo.formerTax + "元"
							});
							table.push({
								name: "滞纳金",
								fee: taxInfo.lateFee + "元"
							});
						}
						leftHtml += Mustache.to_html(twoColTpl, {
							table: table
						});
					} else {
						leftHtml += Mustache.to_html(triColTpl, {
							table: table
						});
					}
				}
			}
		}
		return leftHtml;
	};

	var getRightHtml = function(coverageMap) {
		var rightHtml = "";
		for (var i = 0, j = rightCoverages.length ; i < j; i++) {
			var coverageConfig = rightCoverages[i], coverageName = coverageConfig.name,
				possible = coverageConfig.possible, continues = !!coverageMap[coverageName];

			if (possible) {
				jQuery.each(possible, function(index, item) {
					if (coverageMap[item]) continues = true;
				});
			}

			if (continues) {
				rightHtml += '<div class="i-label d-label block-el fee-title">' + coverageConfig["label"] + "</div>\n";
				var coverageChildren = coverageConfig.children, table = [];
				if (coverageChildren) {
					for (var k = 0, l = coverageChildren.length; k < l; k++) {
						var child = coverageChildren[k], childName = child.name;
						if (coverageMap[childName]) {
							table.push({
								name: typeof child.label == "function" ? child.label(controller.view) : child.label,
								fee: coverageMap[childName]["POLICYPREMIUM"]
							});
						}
					}
					rightHtml += Mustache.to_html(triColTpl, {
						table: table
					});
				}
			}
		}
		return rightHtml;
	};

	var controller = {
        setCookie: function (name, value, expire) {
            var exp = new Date();
            exp.setTime(exp.getTime() + expire);
            document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
        },

        getCookie: function (name) {
            var arr = document.cookie.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
            if (arr != null) return unescape(arr[2]);
            return null;
        },

        delCookie: function (name) {
            var exp = new Date();
            exp.setTime(exp.getTime() - 1);
            var cval = getCookie(name);
            if (cval != null) document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
        },

		//不勾选全部的分类CheckBox
		uncheckAllCategory: function() {
			var result = this.view.tag("category");
			result.each(function(item) {
				item.set("checked", false);
				item.fireEvent("onValueChange", item);
			});
		},

        syncCoverage: function(lastCoverageList,data) {
            if (!lastCoverageList) return;
            for (var i = 0, j = lastCoverageList.length; i < j; i++) {
                var coverage = lastCoverageList[i];
                if (allCoveragesMap[coverage]) {
                    var control = controller.view.id(coverage);
                    control.set("checked", true);
                    control.fireEvent("onValueChange", control);
                    if(coverage=="THIRDPARTYLIABILITYCOVERAGE"){
                    	var amout = controller.view.id(coverage+"AMOUNT");
                    	amout.set("value",data.THIRDPARTYLIABILITYCOVERAGEAMOUNT);
                    }
                    if(coverage=="CARBODYPAINTCOVERAGE"){
                    	var amout = controller.view.id(coverage+"AMOUNT");
                    	amout.set("value",data.CARBODYPAINTCOVERAGEAMOUNT);
                    	
                    }
                    if(coverage=="GLASSBROKENCOVERAGE"){
                    	var type=controller.view.id(coverage+"KIND");
                    	type.set("value","1");
                    }
                } else {
                    //console.log("skip coverage:" + coverage);
                }
            }
        },

        reloadImage: function() {
            var image = controller.image, dom = image._dom;
            $fly(dom).find("img").prop("src", "/CPIC09Auto/mobilecontroller/createRandomPicture.do?" + (new Date()).getTime());
        },

        renewInfo: function() {
        	controller.renewInfoDialog.show();
        },
        
		login: function() {
			var branchCode=controller.branchCodeEditor.get("value"),userCode=controller.userCodeEditor.get("value"),
			password=controller.passwordEditor.get("value"),rand=controller.imageEditor.get("value");
			
			if (!branchCode || !userCode||!password||!rand) {
				dorado.MessageBox.alert("请录入必要信息！");
				return;
			}
						
			dorado.touch.showLoadingPanel("加载数据中...");
			jQuery.post("/CPIC09Auto/mobilecontroller/userLogin.do", {
				branchCode: branchCode,
				userCode: userCode,
				password: password,
				rand:rand
			}, function(data) {
				dorado.touch.hideLoadingPanel();
				dataSetLogin.set("data",data);
				if (data.failed != null) {
					dorado.MessageBox.alert(data.failed);
					controller.reloadImage();
				} else {
                    controller.setCookie("cpic.branchCode", data.branchCode);
                    controller.setCookie("cpic.userCode", userCode);
					if (data.branchCode == "1010100") {
						controller.beijing = true;
						controller.cardbook.set("currentControl", controller.indexPanel);
						controller.loadVehiclePropertyList();
					} else {
						controller.beijing = false;
						controller.cardbook.set("currentControl", controller.indexPanelOther);
						controller.loadVehiclePropertyList();
					}
				}
				//window.location.reload();
			}, "json").error(function() {
				dorado.touch.hideLoadingPanel();
			});
		},
		//登陆
		index: function() {
			var plateno = controller.platenoEditor.get("value"), owner = controller.ownerEditor.get("value"),
			vehicleUsage = controller.vehicleUsageEditor.get("value");
			if (!plateno || !owner) {
				dorado.MessageBox.alert("请输入车牌号和车主姓名！");
				return;
			}

            var startDate = controller.startDateEditor.get("value"), endDate = controller.endDateEditor.get("value"),
                startDateAuto = controller.startDateAutoEditor.get("value"), endDateAuto = controller.endDateAutoEditor.get("value");

            var callback = function() {
                //plateno = "京" + plateno;
                dorado.touch.showLoadingPanel("加载数据中...");
                jQuery.post("/CPIC09Auto/mobilecontroller/queryLastInsuredInfo.do", {
                    plateno: plateno,
                    owner: jQuery.trim(owner),
                    startDate: startDate.getTime(),
                    endDate: endDate.getTime(),
                    startDateAuto: startDateAuto.getTime(),
                    endDateAuto: endDateAuto.getTime()
                }, function(list) {
                    if (!list) {
                        dorado.MessageBox.alert("没有得到返回结果");
                        return;
                    }
                    if (hasError(list)) {
                        dorado.touch.hideLoadingPanel();
                    } else {
                        var showCarList = false;
                        if (list.length == 1) {
                            if (list[0].owner != owner) {
                                dorado.MessageBox.alert("车主信息与平台的记录不一致!");
                                dorado.touch.hideLoadingPanel();
                                return;
                            }
                        } else {
                            showCarList = true;
                        }
                        if (showCarList) {
                            var items = [];
                            for (var i = 0, j = list.length; i < j; i++) {
                                var record = list[i];
                                items.push({
                                    rackNo: record["rackNo"],
                                    carMark: record["carMark"],
                                    vehBrand1: record["vehBrand1"],
                                    engineNo: record["engineNo"],
                                    owner: record["owner"]
                                });
                            }
                            controller.carGrid.set("items", items);
                            dorado.touch.hideLoadingPanel();
                            controller.selectCarDialog.show();
                        } else {
                            dorado.touch.hideLoadingPanel();
                            controller.loadVehicleModel(list[0]["rackNo"], vehicleUsage || "101", function(priceList) {
                                if (priceList.length == 1) {
                                    controller.loadVehicleInfo(priceList[0].vehicleCode, function() {
                                        controller.cardbook.set("currentControl", controller.selectPanel);
                                        controller.uncheckAllCategory();
                                    });
                                } else {
                                    controller.cardbook.set("currentControl", controller.selectModelPanel);
                                    controller.cardbook.refresh();
                                    controller.uncheckAllCategory();
                                }
                            });
                        }
                    }
                }, "json").error(function() {
                    dorado.touch.hideLoadingPanel();
                });
            };

            var msg = controller.verifyDate(startDate, endDate, startDateAuto, endDateAuto);
            if (msg === false)
                return;
            else if(msg) {
                dorado.MessageBox.alert(msg, callback);
            } else {
                callback();
            }
		},
        verifyDate: function(startDate, endDate, startDateAuto, endDateAuto) {
            var now = new Date();
            if (endDate < startDate) {
                dorado.MessageBox.alert("交强险终止日期应大于起保日期！");

                return false;
            }

            if (endDateAuto < startDateAuto) {
                dorado.MessageBox.alert("商业险终止日期应大于起保日期！");

                return false;
            }

            var msg = "";

            if (startDate < now) {
                msg += "交强险起保时间小于录入时间，该投保单属于倒签单，强行提交将被拒保！";
            }

            if (startDateAuto < now) {
                msg += "商业险起保时间小于录入时间，该投保单属于倒签单，强行提交将被拒保！";
            }

            return msg;
        },
		indexOther: function() {
			var plateno = controller.platenoEditorOther.get("value"), owner = controller.ownerEditorOther.get("value"),
				vehicleUsage = controller.vehicleUsageEditorOther.get("value"),
				rackNo = controller.rackNoEditor.get("value"), engineNo = controller.engineNoEditor.get("value"),
				registerDate = controller.registerDateEditor.get("value"), vehicleName = controller.vehicleNameEditor.get("value"),
				engineCapacity = controller.engineCapacityEditor.get("value"),emptyWeight = controller.emptyWeightEditor.get("value"),
				licenseType = controller.licenseTypeEditor.get("value"), vehicleType = controller.vehicleTypeEditor.get("value"),
                vehiclePurpose = controller.vehiclePurposeEditor.get("value");

			if (!plateno || !owner || !rackNo || !engineNo || !registerDate || !vehicleName || !licenseType || !engineCapacity || !emptyWeight || !vehiclePurpose || !vehicleType) {
				dorado.MessageBox.alert("所有的信息都为必须录入，请确认！");
				return;
			}

            var apointFactoryRate = controller.apointFactoryRateEditor.get("value");
            if (apointFactoryRate) {
                var value = parseFloat(apointFactoryRate);
                if (isNaN(value) || (parseFloat(apointFactoryRate) < 0.1 || parseFloat(apointFactoryRate) > 0.3)) {
                    dorado.MessageBox.alert("指定专修厂特约条款费率应该在0.1-0.3之间！");

                    return;
                }
            }

            var avgMile = controller.avgMileEditor.get("value"), appointDriver = controller.appointDriverEditor.get("value"),
                driverArea = controller.driverAreaEditor.get("value");

            var startDate = controller.startDateEditor1.get("value"), endDate = controller.endDateEditor1.get("value"),
                startDateAuto = controller.startDateAutoEditor1.get("value"), endDateAuto = controller.endDateAutoEditor1.get("value");

            var callback = function() {
                controller.loadVehicleModelForSd({
                    plateno: plateno,
                    owner: jQuery.trim(owner),
                    rackNo: rackNo,
                    engineNo: engineNo,
                    registerDate: registerDate.getTime(),
                    rVehicleName: vehicleName,
                    licenseType: licenseType,
                    vehicleUsage: vehicleUsage || "101",
                    startDate: startDate.getTime(),
                    endDate: endDate.getTime(),
                    startDateAuto: startDateAuto.getTime(),
                    endDateAuto: endDateAuto.getTime(),
                    engineCapacity: engineCapacity,
                    emptyWeight: emptyWeight,
                    vehicleType: vehicleType,
                    vehiclePurpose: vehiclePurpose,
                    apointFactoryRate: apointFactoryRate,
                    avgMile: avgMile,
                    appointDriver: appointDriver,
                    driverArea: driverArea
                }, function(priceList) {
                    if (priceList.length == 1) {
                        controller.loadVehicleInfo(priceList[0].vehicleCode, function() {
                            controller.cardbook.set("currentControl", controller.selectPanel);
                            controller.uncheckAllCategory();
                        });
                    } else if (priceList.length == 0) {
                        dorado.MessageBox.alert("查不到车型信息，请检查录入的数据!");
                    } else {
                        controller.cardbook.set("currentControl", controller.selectModelPanel);
                        controller.cardbook.refresh();
                        controller.uncheckAllCategory();
                    }
                });
            };

            var msg = controller.verifyDate(startDate, endDate, startDateAuto, endDateAuto);
            if (msg === false)
                return;
            else if(msg) {
                dorado.MessageBox.alert(msg, callback);
            } else {
                callback();
            }
		},
		//加载车辆信息
		loadVehicleInfo: function(vehicleCode, callback) {
			dorado.touch.showLoadingPanel("加载数据中...");
			jQuery.post("/CPIC09Auto/mobilecontroller/preCoveragePage.do", {
				vehicleCode: vehicleCode
			}, function(data) {
				dorado.touch.hideLoadingPanel();
				if (hasError(data)) return;
				if (typeof callback == "function") {
					callback.apply(null, []);
				}
				dataSetBasic.set("data", data);
				controller.passengerCountLabel.set("text", data.passengerSeats);
				controller.selectCarDialog.hide();
                controller.syncCoverage(data.lastCoverageList,data);
			}, "json").error(function() {
				dorado.touch.hideLoadingPanel();
			});
		},
		//加载车型信息
		loadVehicleModelForSd: function(options, callback) {
			dorado.touch.showLoadingPanel("加载数据中...");
			jQuery.post("/CPIC09Auto/mobilecontroller/queryVehicleModel.do", options, function(data) {
				dorado.touch.hideLoadingPanel();
				if (hasError(data)) return;
				var priceList = data.vehiclePriceList || [];
				if (typeof callback == "function") {
					callback.apply(null, [priceList]);
				}
				dataSetModel.set("data", data);
				controller.modelGrid.set("items", priceList);
			}, "json").error(function() {
				dorado.touch.hideLoadingPanel();
			});
		},
		//加载车型信息
		loadVehicleModel: function(rackNo, vehicleUsage, callback) {
			dorado.touch.showLoadingPanel("加载数据中...");
			jQuery.post("/CPIC09Auto/mobilecontroller/queryVehicleModel.do", {
				rackNo: rackNo,
				vehicleUsage: vehicleUsage
			}, function(data) {
				dorado.touch.hideLoadingPanel();
				if (hasError(data)) return;
				var priceList = data.vehiclePriceList || [];
				if (typeof callback == "function") {
					callback.apply(null, [priceList]);
				}
				dataSetModel.set("data", data);
				controller.modelGrid.set("items", priceList);
			}, "json").error(function() {
				dorado.touch.hideLoadingPanel();
			});
		},

        //加载下拉列表
        loadVehiclePropertyList: function() {
            function getMapping(data) {
                var names = [], mapping = [], defaultValue;
                for (var i = 0, j = data.length; i < j; i++) {
                    var record = data[i];
                    if (i == 0) {
                        defaultValue = record.code;
                    }
                    names.push(record["name"]);
                    mapping.push({ value: record.code, text: record.name});
                }

                return {
                    names: names,
                    mapping: mapping,
                    defaultValue: defaultValue
                }
            }
            if (controller.vehiclePropertyListLoaded) return;
            controller.vehiclePropertyListLoaded = true;
            dorado.touch.showLoadingPanel("加载数据中...");
            jQuery.post("/CPIC09Auto/mobilecontroller/queryVehiclePropertyList.do", {}, function(data) {
                dorado.touch.hideLoadingPanel();
                if (hasError(data)) return;
                if (data) {
                    for (var prop in data) {
                        var config = getMapping(data[prop]);
                        if (prop == "vehicleUsage") {
                            controller.vehicleUsageList.set("data", config.names);
                            controller.vehicleUsageEditor.set("mapping", config.mapping);
                            controller.vehicleUsageEditorOther.set("mapping", config.mapping);

                            controller.vehicleUsageEditor.set("value", config.defaultValue);
                            controller.vehicleUsageEditorOther.set("value", config.defaultValue);
                        } else if (prop == "licenseType") {
                            controller.licenseTypeList.set("data", config.names);
                            controller.licenseTypeEditor.set("mapping", config.mapping);
                        } else if (prop == "vehicleType") {
                            controller.vehicleTypeList.set("data", config.names);
                            controller.vehicleTypeEditor.set("mapping", config.mapping);
                        } else if (prop == "vehiclePurpose") {
                            controller.vehiclePurposeList.set("data", config.names);
                            controller.vehiclePurposeEditor.set("mapping", config.mapping);
                        }
                    }
                }
            }, "json").error(function() {
                dorado.touch.hideLoadingPanel();
            });
        },

		//加载使用性质
		loadVehicleUsage: function() {
			if (controller.vehicleUsageLoaded) return;
			controller.vehicleUsageLoaded = true;
			dorado.touch.showLoadingPanel("加载数据中...");
			jQuery.get("/CPIC09Auto/mobilecontroller/queryVehicleUsage.do", {}, function(data) {
				dorado.touch.hideLoadingPanel();
				if (hasError(data)) return;
				var names = [];
				if (data) {
					var mapping = [], defaultValue;
					for (var i = 0, j = data.length; i < j; i++) {
						var record = data[i];
						if (i == 0) {
							defaultValue = record.code;
						}
						names.push(record["name"]);
						mapping.push({ value: record.code, text: record.name});
					}
					controller.vehicleUsageList.set("data", names);
					controller.vehicleUsageEditor.set("mapping", mapping);
					controller.vehicleUsageEditorOther.set("mapping", mapping);

					controller.vehicleUsageEditor.set("value", defaultValue);
					controller.vehicleUsageEditorOther.set("value", defaultValue);
				}
			}, "json").error(function() {
				dorado.touch.hideLoadingPanel();
			});
		},
		//加载号牌类型
		loadLicenseType: function() {
			if (controller.licenseTypeLoaded) return;
			controller.licenseTypeLoaded = true;
			dorado.touch.showLoadingPanel("加载数据中...");
			jQuery.get("/CPIC09Auto/mobilecontroller/queryLicenseType.do", {}, function(data) {
				dorado.touch.hideLoadingPanel();
				if (hasError(data)) return;
				var names = [];
				if (data) {
					var mapping = [], defaultValue;
					for (var i = 0, j = data.length; i < j; i++) {
						var record = data[i];
						if (i == 0) {
							defaultValue = record.code;
						}
						names.push(record["name"]);
						mapping.push({ value: record.code, text: record.name});
					}
					controller.licenseTypeList.set("data", names);
					controller.licenseTypeEditor.set("mapping", mapping);
					//controller.licenseTypeEditor.set("value", defaultValue);
				}
			}, "json").error(function() {
				dorado.touch.hideLoadingPanel();
			});
		},

		//确定选择这辆车
		confirmCar: function() {
			var current = controller.carGrid.getCurrentItem(), owner = controller.ownerEditor.get("value"),
				vehicleUsage = controller.vehicleUsageEditor.get("value");

			if (!current) {
				dorado.MessageBox.alert("请选择相应的记录！");
				return;
			}

			if (current.owner != owner) {
				dorado.MessageBox.alert("车主信息与平台的记录不一致！");
				controller.selectCarDialog.hide();
				return;
			}

			controller.loadVehicleModel(current["rackNo"],  vehicleUsage || "101", function(priceList) {
				if (priceList.length == 1) {
					controller.loadVehicleInfo(priceList[0].vehicleCode, function() {
						controller.cardbook.set("currentControl", controller.selectPanel);
						controller.uncheckAllCategory();
					});
				} else {
					controller.cardbook.set("currentControl", controller.selectModelPanel);
					controller.cardbook.refresh();
					controller.selectCarDialog.hide();
				}
			});
		},
		
		//生成报价单
		createQuotation: function() {
			dorado.touch.showLoadingPanel("加载数据中...");
			jQuery.post("/CPIC09Auto/mobilecontroller/createQuotation.do", function(data) {
				dorado.touch.hideLoadingPanel();
				if (hasError(data)) return;
				dorado.MessageBox.alert(data.info);
				if (typeof callback == "function") {
					callback.apply(null, []);
				}
				controller.selectCarDialog.hide();
				controller.cardbook.set("currentControl", controller.personalPanel);
			}, "json").error(function() {
				dorado.touch.hideLoadingPanel();
			});
		},
		
		
		//补全详细信息
		reLoadDetailInfo: function() {
			dorado.touch.showLoadingPanel("加载数据中...");
			jQuery.post("/CPIC09Auto/mobilecontroller/reLoadDetailInfo.do", function(data) {
				dorado.touch.hideLoadingPanel();
				if (hasError(data)) return;
				if (typeof callback == "function") {
					callback.apply(null, []);
				}
				dataSetBasic.set("data", data);
				controller.selectCarDialog.hide();
				controller.cardbook.set("currentControl", controller.personalPanel);
			}, "json").error(function() {
				dorado.touch.hideLoadingPanel();
			});
		},

		//清空续保险种信息
		clearRenewInfo: function(callback) {
			jQuery.post("/CPIC09Auto/mobilecontroller/clearRenewInfo.do", {}, function(data) {
				if (hasError(data)) return;
				if (typeof callback == "function") {
					callback.apply(null, []);
				}
				controller.platenoEditorOther.set("value", "");
                controller.ownerEditorOther.set("value", "");
                controller.vehicleUsageEditorOther.set("value", "101");
                controller.rackNoEditor.set("value", "");
                controller.engineNoEditor.set("value", "");
                controller.registerDateEditor.set("value", "");
                controller.vehicleNameEditor.set("value", "");
                controller.engineCapacityEditor.set("value", "");
                controller.emptyWeightEditor.set("value", "");
                controller.licenseTypeEditor.set("value", "");
                controller.apointFactoryRateEditor.set("value", "");
                controller.vehicleTypeEditor.set("value", "");
                controller.vehiclePurposeEditor.set("value", "");

                controller.renewGrid.set("items", []);
			}, "json").error(function() {
				dorado.touch.hideLoadingPanel();
			});
		},
		//提交要计算的子险
		submitCoverage: function() {
			if (!verifyCoverage()) return;
			var coverage = getCoverageCollection();
			if (coverage.length == 0) {
				dorado.MessageBox.alert("请至少选择一个险种！");
				return;
			}
			dorado.touch.showLoadingPanel("保费试算中...");
			jQuery.post("/CPIC09Auto/mobilecontroller/calculatePremiumForMobileQuotation.do", {
				coverage: JSON.stringify(coverage)
			}, function(json) {
				dorado.touch.hideLoadingPanel();
				if (hasError(json)) return;

				var resultList = json.returnList, returnMap = json.returnMap;

				var coverageMap = {}, leftHtml, rightHtml;

				for (var i = 0, j = resultList.length; i < j; i++) {
					var coverage = resultList[i], coverageName = coverage["COVERAGENAME"];
					if (!coverageName) {
						if (coverage["ALLPOLICYPREMIUMAUTO"]) {
							returnMap.totalFeeAuto = coverage["ALLPOLICYPREMIUMAUTO"];
						}
						if (coverage["ALLPOLICYPREMIUMTRAFFIC"]) {
							returnMap.totalFeeTraffic = coverage["ALLPOLICYPREMIUMTRAFFIC"];
						}
					} else {
						coverageMap[coverageName] = coverage;
					}
				}

				dataSetResult.set("data", returnMap);

				var resultHtml = "<table class='result-table' cellpadding='5' cellspacing='0'><tr><td width='45%' valign='top'>" + getLeftHtml(coverageMap) +
					"</td><td width='10%'></td><td valign='top'>" + getRightHtml(coverageMap) + "</td></tr></table>";

				if (!controller.resultHtmlCt._dom) {
					controller.resultHtmlCt.set("content", resultHtml);
				} else {
					controller.resultHtmlCt._dom.innerHTML = resultHtml;
				}

				controller.cardbook.set("currentControl", controller.resultPanel);
				controller.cardbook.refresh();
			}, "json").error(function() {
				dorado.touch.hideLoadingPanel();
			});
		},

        queryRenew: function() {
            var plateno = controller.renewPlatenoEditor.get("value"), policyNo = controller.renewPolicyNoEditor.get("value"),
                carVIN = controller.renewCarVINEditor.get("value"), engineNo = controller.renewEngineNoEditor.get("value"),
                traffic = controller.renewTrafficEditor.get("value");

            if (!plateno && !policyNo && !carVIN && !engineNo) {
                dorado.MessageBox.alert("请输入至少一项查询条件！");
                return;
            }

            dorado.touch.showLoadingPanel("加载数据中...");
            jQuery.post("/CPIC09Auto/mobilecontroller/queryRenewCopyList.do", {
                plateno: plateno,
                policyNo: policyNo,
                carVIN: carVIN,
                engineNo: engineNo,
                traffic: traffic
            }, function(data) {
                dorado.touch.hideLoadingPanel();
                if (hasError(data)) return;
                var renewInfo = data || [];
                controller.renewGrid.set("items", renewInfo);
            }, "json").error(function() {
                dorado.touch.hideLoadingPanel();
            });
        },
        resetRenew: function() {
            controller.renewPlatenoEditor.set("value", "");
            controller.renewPolicyNoEditor.set("value", "");
            controller.renewCarVINEditor.set("value", "");
            controller.renewEngineNoEditor.set("value", "");
            controller.renewTrafficEditor.set("value", false);
        },
        copyRenew: function() {
            var renewGrid = controller.renewGrid, current = renewGrid.getCurrentItem();
            if (!current) {
                dorado.MessageBox.alert("请选择一条记录。");
                return;
            }

            dorado.touch.showLoadingPanel("加载数据中...");
            jQuery.post("/CPIC09Auto/mobilecontroller/renewInfoCopy.do", {
                actualId: current.actualId,
                trafficFlag: controller.renewTrafficEditor.get("value")
            }, function(data) {
                dorado.touch.hideLoadingPanel();
                if (hasError(data)) return;
                controller.platenoEditorOther.set("value", data.plateno);
                controller.ownerEditorOther.set("value", data.owner);
                controller.vehicleUsageEditorOther.set("value", data.vehicleUsage);
                controller.rackNoEditor.set("value", data.rackNo);
                controller.engineNoEditor.set("value", data.engineNo);
                controller.registerDateEditor.set("value", Date.parseDate(data.registerDate, "Y-m-d"));
                controller.vehicleNameEditor.set("value", data.vehicleName);
                controller.engineCapacityEditor.set("value", data.engineCapacity);
                controller.emptyWeightEditor.set("value", data.emptyWeight);
                controller.licenseTypeEditor.set("value", data.licenseType);
                controller.vehiclePurposeEditor.set("value", data.vehiclePurpose);
                controller.vehicleTypeEditor.set("value", data.vehicleType);

                controller.renewGrid.set("items", []);

                controller.renewInfoDialog.hide();
            }, "json").error(function() {
                dorado.touch.hideLoadingPanel();
            });
        }
	};

	window.controller = controller;
})();