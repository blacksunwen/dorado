(function() {
	var controller = window.controller;
	var view = new dorado.widget.View({
		layout: { $type: "VBox", align: "center", padding: 0 },
		height: 650
	});
	
	dorado.MessageBox.defaultTitle = "信息提示";
	
	// 登录页面
	var branchCodeEditor = new dorado.touch.TextEditor();
	var userCodeEditor = new dorado.touch.TextEditor();
	var passwordEditor = new dorado.touch.TextEditor({ type: "password" });
    var imageEditor = new dorado.touch.TextEditor({ width: 300 });
    var image = new dorado.widget.HtmlContainer({
        content: "<img src='/CPIC09Auto/mobilecontroller/createRandomPicture.do' class='verify-code'/>",
        onClick: function(self, arg) {
            controller.reloadImage();
        }
    });
    var imageContainer = new dorado.widget.Container({
        layout: { $type: "HBox" },
        children: [imageEditor, image]
    });

    var branchCode = controller.getCookie("cpic.branchCode"), userCode = controller.getCookie("cpic.userCode");
    if (branchCode && userCode) {
        branchCodeEditor.set("value", branchCode);
        userCodeEditor.set("value", userCode);
    }

	var loginForm = new dorado.widget.AutoForm({
		labelWidth:100,
		width:600,
		cols:"*",
		layoutConstraint:{
			width:700
		},
		style:{
			margin:"0 auto"
		},
		elements:[
			{
				property:"branchCode",
				label:"公司代码",
				editor: branchCodeEditor
			},
			{
				property:"userCode",
				label:"员工工号",
				editor: userCodeEditor
			},
			{
				property: "password",
				label: "员工密码",
				editor: passwordEditor
			},
            {
                property: "image",
                label: "验证码",
                editor: imageContainer
            },
			new dorado.touch.Button({
				caption:"登录",
				exClassName: "login-button",
				width: 120,
				style: {
					margin: "0 auto"
				},
				onTap: controller.login
			})
		]
	});

	var loginPanel = new dorado.touch.Panel({
		layout : new dorado.widget.layout.NativeLayout(),
		buttonAlign : "right",
		children : [
			new dorado.widget.HtmlContainer({
				content : "<img class='logo' src='cpic_logo.jpg' width=879 height=253 /><br /><p style='font-size: 24px;color: #ed8508'>尊敬的客户,欢迎使用太平洋保险自助测算保费系统!</p>",
				height : 350,
				style : {
					display : "block",
					margin : "0 auto",
					textAlign : "center"
				}
			}),
			loginForm
		]
	});

	var platenoEditor = new dorado.touch.TextEditor({ value: "京" }), ownerEditor = new dorado.touch.TextEditor();
	var vehicleUsageList = new dorado.touch.ListPicker({ data: [] });
	var vehicleUsageEditor = new dorado.touch.TextEditor({
		editable: false,
		popup: vehicleUsageList
	});

    var now = (new Date).getTime(), tomorrow = now + 24 * 1000 * 3600;

    Date.prototype.tomorrow = function() {
        this.setTime(this.getTime() + 24000 * 3600);

        return this;
    };

    Date.prototype.nextyear = function() {
        this.setFullYear(this.getFullYear() + 1);

        return this;
    };

    Date.prototype.clone = function() {
        return new Date(this.getTime());
    };

    Date.prototype.clearTime = function() {
        this.setHours(0);
        this.setMinutes(0);
        this.setSeconds(0);

        return this;
    };

    var startDateEditor = new dorado.touch.TextEditor({
            popup: dorado.touch.defaultDatePicker, editable: false, value: new Date().tomorrow().clearTime(),
            onValueChange: function(editor, value) {
                if (value && endDateEditor) {
                    endDateEditor.set("value", value.clone().nextyear().clearTime());
                }
            }
        }),
        endDateEditor = new dorado.touch.TextEditor({
            popup: dorado.touch.defaultDatePicker, editable: false, value: new Date().tomorrow().nextyear().clearTime()
        }),
        startDateAutoEditor = new dorado.touch.TextEditor({
            popup: dorado.touch.defaultDatePicker, editable: false, value: new Date().tomorrow().clearTime(),
            onValueChange: function(editor, value) {
                if (value && endDateAutoEditor) {
                    endDateAutoEditor.set("value", value.clone().nextyear().clearTime());
                }
            }
        }),
        endDateAutoEditor = new dorado.touch.TextEditor({
            popup: dorado.touch.defaultDatePicker, editable: false, value: new Date().tomorrow().nextyear().clearTime()
        });

	var startForm, startFormOther;

	startForm = new dorado.widget.AutoForm({
		labelWidth:150,
		width:1000,
		cols:"*,*",
		layoutConstraint:{
			width:700
		},
		style:{
			margin:"0 auto"
		},
		elements:[
			{
				property:"palteno",
				label:"车牌号码",
				editor: platenoEditor
				//,value: "京A81707"
			},
			{
				property:"owner",
				label:"车主名称",
				editor: ownerEditor
				//,value: "于华"
			},
			
            {
                property: "startDate",
                label: "交强险起保日期",
                editor: startDateEditor
            },
            {
                property: "endDate",
                label: "交强险终止日期",
                editor: endDateEditor
            },
            {
                property: "startDateAuto",
                label: "商业险起保日期",
                editor: startDateAutoEditor
            },
            {
                property: "endDateAuto",
                label: "商业险终止日期",
                editor: endDateAutoEditor
            },
            {
				property: "vehicleUsage",
				label: "使用性质",
				editor: vehicleUsageEditor
			}
		]
	});

	var rackNoEditor, engineNoEditor, vehicleNameEditor, registerDateEditor,engineCapacityEditor,emptyWeightEditor;

	rackNoEditor = new dorado.touch.TextEditor();
	engineNoEditor = new dorado.touch.TextEditor();
	vehicleNameEditor = new dorado.touch.TextEditor();
	engineCapacityEditor = new dorado.touch.TextEditor();
	emptyWeightEditor = new dorado.touch.TextEditor();
	registerDateEditor = new dorado.touch.TextEditor({
        format: "Y-m-d",
		popup: dorado.touch.defaultDatePicker,
        editable: false
	});

	var platenoEditorOther = new dorado.touch.TextEditor({ value: "" }), ownerEditorOther = new dorado.touch.TextEditor();
	var vehicleUsageEditorOther = new dorado.touch.TextEditor({
		editable: false,
		popup: vehicleUsageList
	});
	var licenseTypeList = new dorado.touch.ListPicker({ data: [] });
	var licenseTypeEditor = new dorado.touch.TextEditor({
		editable: false,
		popup: licenseTypeList
	});

    var startDateEditor1 = new dorado.touch.TextEditor({
            popup: dorado.touch.defaultDatePicker, editable: false, value: new Date().tomorrow().clearTime(),
            onValueChange: function(editor, value) {
                if (value && endDateEditor1) {
                    endDateEditor1.set("value", value.clone().nextyear().clearTime());
                }
            }
        }),
        endDateEditor1 = new dorado.touch.TextEditor({
            popup: dorado.touch.defaultDatePicker, editable: false, value: new Date().tomorrow().nextyear().clearTime()
        }),
        startDateAutoEditor1 = new dorado.touch.TextEditor({
            popup: dorado.touch.defaultDatePicker, editable: false, value: new Date().tomorrow().clearTime(),
            onValueChange: function(editor, value) {
                if (value && endDateAutoEditor1) {
                    endDateAutoEditor1.set("value", value.clone().nextyear().clearTime());
                }
            }
        }),
        endDateAutoEditor1 = new dorado.touch.TextEditor({
            popup: dorado.touch.defaultDatePicker, editable: false, value: new Date().tomorrow().nextyear().clearTime()
        });

    var avgMileEditor = new dorado.touch.RadioButtonGroup({
        buttons: [
            new dorado.touch.RadioButton({ caption: "<3万公里", value: 0.9 }),
            new dorado.touch.RadioButton({ caption: "[3万公里,5万公里)", value: 1 })
        ]
    }), appointDriverEditor = new dorado.touch.RadioButtonGroup({
        buttons: [
            new dorado.touch.RadioButton({ caption: "指定驾驶人", value: 0.9 }),
            new dorado.touch.RadioButton({ caption: "不指定", value: 1 })
        ]
    }), driverAreaEditor = new dorado.touch.RadioButtonGroup({
        buttons: [
            new dorado.touch.RadioButton({ caption: "境内", value: 1 }),
            new dorado.touch.RadioButton({ caption: "省内", value: 0.95 })
        ]
    }), apointFactoryRateEditor = new dorado.touch.TextEditor(),
        vehicleTypeList = new dorado.touch.ListPicker({ data: [] }),
        vehicleTypeEditor = new dorado.touch.TextEditor({
            editable: false,
            popup: vehicleTypeList
        }),
        vehiclePurposeList = new dorado.touch.ListPicker({ data: [] }),
        vehiclePurposeEditor = new dorado.touch.TextEditor({
            editable: false,
            popup: vehiclePurposeList
        });

    startFormOther = new dorado.widget.Container({
        layout: new dorado.widget.layout.VBoxLayout(),
        width:1000,
        style:{
            margin:"0 auto"
        },
        children: [
            new dorado.touch.GroupBox({
                caption: "车辆基本信息",
                children: [
                    new dorado.widget.AutoForm({
                        labelWidth:150,
                        cols:"*,*",
                        elements:[
                            {
                                property:"palteno",
                                label:"车牌号码",
                                editor: platenoEditorOther
                            },
                            {
                                property:"owner",
                                label:"车主名称",
                                editor: ownerEditorOther
                            },
                            {
                                property: "engineNo",
                                label: "发动机号",
                                editor: engineNoEditor
                            },
                            {
                                property: "rackNo",
                                label: "车架号/VIN码",
                                editor: rackNoEditor
                            },
                            {
                                property: "vehicleUsage",
                                label: "使用性质",
                                editor: vehicleUsageEditorOther
                            },
                            {
                                property: "engineCapacity",
                                label: "排量(升)",
                                editor: engineCapacityEditor
                            },
                            {
                                property: "emptyWeight",
                                label: "整备质量(千克)",
                                editor: emptyWeightEditor
                            },
                            {
                                property: "registerDate",
                                label: "初次登记日期",
                                editor: registerDateEditor
                            },
                            {
                                property: "vehicleName",
                                label: "车辆型号",
                                editor: vehicleNameEditor
                            },
                            {
                                property: "licenseType",
                                label: "号牌类型",
                                editor: licenseTypeEditor
                            },
                            {
                                property: "vehicleType",
                                label: "车辆种类",
                                editor: vehicleTypeEditor
                            },
                            {
                                property: "vehiclePurpose",
                                label: "车辆用途",
                                editor: vehiclePurposeEditor
                            },
                            {
                                property: "startDate",
                                label: "交强险起保日期",
                                editor: startDateEditor1
                            },
                            {
                                property: "endDate",
                                label: "交强险终止日期",
                                editor: endDateEditor1
                            },
                            {
                                property: "startDateAuto",
                                label: "商业险起保日期",
                                editor: startDateAutoEditor1
                            },
                            {
                                property: "endDateAuto",
                                label: "商业险终止日期",
                                editor: endDateAutoEditor1
                            }
                        ]
                    }
                )]
            }),
            new dorado.touch.GroupBox({
                caption: "浮动项",
                children: [
                    new dorado.widget.AutoForm({
                        labelWidth:220,
                        cols:"*,*",
                        elements:[
                            {
                                property:"avgMile",
                                label:"平均行驶里程",
                                editor: avgMileEditor
                            },
                            {
                                property:"appointDriver",
                                label:"指定驾驶人",
                                editor: appointDriverEditor
                            },
                            {
                                property:"driverArea",
                                label:"行驶区域",
                                editor: driverAreaEditor
                            },
                            {
                                property:"apointFactoryRate",
                                label:"指定专修厂特约条款费率",
                                editor: apointFactoryRateEditor
                            }
                        ]
                    })
                ]
            })
        ]
    });

	var carGrid = new dorado.widget.Grid({
		readOnly: true,
		showFooter: false,
		rowHeight: 36,
		headerRowHeight: 36,
		columns: [
			{
				name: "车牌号",
				property: "carMark",
				supportsOptionMenu: false,
				resizeable: false,
				wrappable: true
			},
			{
				name: "中文品牌",
				property: "vehBrand1",
				supportsOptionMenu: false,
				resizeable: false,
				wrappable: true
			},
			{
				name: "车架号",
				property: "rackNo",
				supportsOptionMenu: false,
				resizeable: false,
				wrappable: true
			},
			{
				name: "发动机号",
				property: "engineNo",
				supportsOptionMenu: false,
				resizeable: false,
				wrappable: true
			}
		],
        scrollMode: "simple",
        draggable: false,
        droppable: false
	});

	var selectCarDialog = new dorado.touch.Dialog({
		caption: "请选择相应的记录",
		center: true,
		modal: true,
		width: 600,
		visible: false,
		contentOverflow: "hidden",
		children: [carGrid],
		buttons: [
			new dorado.touch.Button({
				caption: "确定",
				onTap: controller.confirmCar
			}),
			new dorado.touch.Button({
				caption: "取消",
				onTap: function() {
					selectCarDialog.hide();
				}
			})
		]
	});

    var renewPlatenoEditor = new dorado.touch.TextEditor(), renewPolicyNoEditor = new dorado.touch.TextEditor(),
        renewCarVINEditor = new dorado.touch.TextEditor(), renewEngineNoEditor = new dorado.touch.TextEditor(),
        renewTrafficEditor = new dorado.touch.CheckBox();

    var renewAutoForm = new dorado.widget.AutoForm({
        labelWidth:150,
        cols:"*,*",
        style:{
            margin:"0 auto"
        },
        elements:[
            {
                property:"palteno",
                label:"号牌号码",
                editor: renewPlatenoEditor
            },
            {
                property:"policyNo",
                label:"上年保单号",
                editor: renewPolicyNoEditor
            },
            {
                property: "carVIN",
                label: "车架号/VIN码",
                editor: renewCarVINEditor
            },
            {
                property: "engineNo",
                label: "发动机号",
                editor: renewEngineNoEditor
            },
            {
                property: "renewTraffic",
                label: "是否续保交强险",
                editor: renewTrafficEditor
            },
            {
                $type: "Container",
                layout: new dorado.widget.layout.HBoxLayout({ pack: "end" }),
                children: [
                    new dorado.touch.Button({
                        caption: "查询",
                        onTap: controller.queryRenew
                    }),
                    new dorado.touch.Button({
                        caption: "重置",
                        onTap: controller.resetRenew
                    })
                ]
            }
        ]
    });

    var renewGrid = new dorado.widget.Grid({
        readOnly: true,
        showFooter: false,
        rowHeight: 36,
        headerRowHeight: 36,
        height: 300,
        columns: [
            {
                property: "actualId",
                visible: false
            },
            {
                property: "policyId",
                visible: false
            },
            {
                name: "保单号",
                property: "policyNo",
                supportsOptionMenu: false,
                resizeable: false,
                wrappable: true,
                width: 200
            },
            {
                name: "号牌号码",
                property: "plateNo",
                supportsOptionMenu: false,
                resizeable: false,
                wrappable: true,
                width: 80
            },
            {
                name: "车架号/VIN码",
                property: "carVIN",
                supportsOptionMenu: false,
                resizeable: false,
                wrappable: true,
                width: 160
            },
            {
                name: "车辆种类",
                property: "vehicleKind",
                supportsOptionMenu: false,
                resizeable: false,
                wrappable: true,
                width: 80
            },
            {
                name: "发动机号",
                property: "engineNo",
                supportsOptionMenu: false,
                resizeable: false,
                wrappable: true,
                width: 170
            },
            {
                name: "起保日期",
                property: "inceptionDate",
                supportsOptionMenu: false,
                resizeable: false,
                wrappable: true,
                width: 90
            },
            {
                name: "终保日期",
                property: "terminationDate",
                supportsOptionMenu: false,
                resizeable: false,
                wrappable: true,
                width: 90
            },
            {
                name: "赔案数",
                property: "accident_num",
                supportsOptionMenu: false,
                resizeable: false,
                wrappable: true,
                width: 50,
                onRenderCell: function(self, arg) {
                    if (!arg.rowType) {
                        var data = arg.data;
                        arg.dom.innerHTML = data["accident_num"] || "0";
                    }
                }
            },
            {
                name: "保单保费",
                property: "policyPremium",
                supportsOptionMenu: false,
                resizeable: false,
                wrappable: true,
                width: 80,
                onRenderCell: function(self, arg) {
                    if (!arg.rowType) {
                        var data = arg.data;
                        arg.dom.innerHTML = data.policyPremium + "元";
                    }
                }
            }
        ],
        scrollMode: "simple",
        draggable: false,
        droppable: false
    });

	var renewInfoDialog = new dorado.touch.Dialog({
		caption: "续保信息",
		center: true,
		modal: true,
		width: 800,
		visible: false,
		contentOverflow: "hidden",
        buttonAlign: "right",
		children: [renewAutoForm, renewGrid],
		buttons: [
			new dorado.touch.Button({
				caption: "复制",
                style: {
                    marginRight: 3
                },
				onTap: controller.copyRenew
			}),
			new dorado.touch.Button({
				caption: "返回",
				onTap: function() {
                    renewGrid.set("items", []);
					renewInfoDialog.hide();
				}
			})
		]
	});
	
	var indexPanel = new dorado.touch.Panel({
		caption: "车辆信息录入",
		layout: new dorado.widget.layout.NativeLayout(),
		exClassName: "index-panel",
		children:[
		    startForm
		],
		toolbarItems:[
			new dorado.touch.Button({
				caption:"返回",
				onTap:function () {
					cardbook.set("currentControl", loginPanel);
				}
			}),
			new dorado.touch.Spacer(),
			new dorado.touch.Button({
				caption:"开始报价",
				onTap:controller.index
			})
			
		]
	});
	
	var indexPanelOther = new dorado.touch.Panel({
		caption: "车辆信息录入",
		layout:new dorado.widget.layout.NativeLayout(),
		exClassName: "index-panel",
		children:[
			startFormOther
		],
		toolbarItems:[
		  	new dorado.touch.Button({
		  		caption:"返回",
		  		onTap:function () {
		  		cardbook.set("currentControl", loginPanel);
		  	}
		  	}),
		  	new dorado.touch.Spacer(),
		  	new dorado.touch.Button({
		  		caption:"续保信息",
                style: {
                    marginRight: 10
                },
		  		onTap:controller.renewInfo
		  	}),
		  	new dorado.touch.Button({
		  		caption:"开始报价",
		  		onTap:controller.indexOther
		  	})
		  ]
	});
	
	var basicAutoForm = new dorado.widget.AutoForm({
		dataSet: dataSetBasic,
		labelWidth: 120,
		cols: "*,*",
		elements:[
			{
				property:"vehicleLicense",
				label:"号牌号码",
				editorType:"label"
			},
			{
				property:"licenseOwner",
				label:"车辆所有人",
				editorType:"label"
			},
			{
				property:"makerModel",
				label:"厂牌型号",
				editorType:"label"
			},
			{
				property:"usageLimitYear",
				label:"使用年限",
				editorType:"label"
			},
			{
				property:"registerDate",
				label:"初登日期",
				editorType:"label"
			},
			{
				property:"vehicleOwner",
				label:"车主名称",
				editorType:"label"
			}
		]
	});

	var modelForm = new dorado.widget.AutoForm({
		dataSet: dataSetModel,
		labelWidth: 160,
		cols: "*,*",
		elements:[
			{
				property:"searchSequenceNo",
				label:"速查码",
				editorType:"label"
			},
			{
				property:"carMark",
				label:"号牌号码",
				editorType:"label"
			},
			{
				property:"rackNo",
				label:"VIN码",
				editorType:"label"
			},
			{
				property:"engineNo",
				label:"发动机号",
				editorType:"label"
			},
			{
				property:"owner",
				label:"行驶证车主",
				editorType:"label"
			},
			{
				property:"vehicleRegisterDate",
				label:"车辆初始登记日期",
				editorType:"label"
			}
		]
	});

	var modelGrid =  new dorado.widget.Grid({
		readOnly: true,
		rowHeight: 36,
		headerRowHeight: 36,
		dynaRowHeight: true,
		columns: [
			{
				name: "车型代码",
				property: "vehicleCode",
				supportsOptionMenu: false,
				resizeable: false,
				wrappable: true
			},
			{
				name: "车型名称",
				property: "RVehicleName",
				supportsOptionMenu: false,
				resizeable: false,
				wrappable: true
			},
			{
				name: "车型描述",
				property: "vehicleDescription",
				supportsOptionMenu: false,
				resizeable: false,
				wrappable: true
			},
			{
				name: "品牌名称",
				supportsOptionMenu: false,
				resizeable: false,
				property: "RVehicleBrand"
			},
			{
				name: "载客量",
				supportsOptionMenu: false,
				resizeable: false,
				property: "RLimitLoadPerson"
			},
			{
				name: "载重量(吨)",
				supportsOptionMenu: false,
				resizeable: false,
				property: "RVehicleTonnage"
			},
			{
				name: "车价",
				supportsOptionMenu: false,
				resizeable: false,
				property: "vehiclePrice"
			},
			{
				name: "车系名称",
				supportsOptionMenu: false,
				resizeable: false,
				property: "RVehicleFamily",
				wrappable: true
			},
			{
				name: "排量",
				supportsOptionMenu: false,
				resizeable: false,
				property: "RExhaustCapacity"
			},
			{
				name: "年款",
				supportsOptionMenu: false,
				resizeable: false,
				property: "RMarketDate"
			},
			{
				name: "风险标识",
				supportsOptionMenu: false,
				resizeable: false,
				property: "riskFlag"
			}
		],
        scrollMode: "simple"
	});

	var selectModelPanel = new dorado.touch.Panel({
		caption: "选择车型信息",
		exClassName: "select-model-panel",
		children: [
			{
				$type: "Container",
				width: 1000,
				height: "100%",
				contentOverflow: "hidden",
				children: [
					new dorado.touch.GroupBox({
						layout: new dorado.widget.layout.NativeLayout(),
						caption: "车型查询",
						layoutConstraint: "top",
						children:[
							modelForm
						]
					}),
					new dorado.touch.GroupBox({
						caption: "新车购置价列表",
                        layout: "Dock",
						children: [
							modelGrid
						]
					})
				]
			}
		],
		contentOverflow: "hidden",
		layout: new dorado.widget.layout.VBoxLayout({
			//regionPadding: 5,
			align: "center"
		}),
		toolbarItems: [
			new dorado.touch.Button({
				caption: "返回",
				onTap: function () {
					//cardbook.set("currentControl", controller.beijing ? indexPanel : indexPanelOther);
					controller.clearRenewInfo(function() {
						cardbook.set("currentControl", controller.beijing ? indexPanel : indexPanelOther);
					});
				}
			}),
			new dorado.touch.Spacer(),
			new dorado.touch.Button({
				caption: "继续",
				onTap: function () {
					var current = modelGrid.getCurrentItem();
					if (!current) {
						dorado.MessageBox.alert("请选择一个车型！");
						return;
					}
					controller.loadVehicleInfo(current.vehicleCode, function() {
						cardbook.set("currentControl", selectPanel);
					});
				}
			})
		]
	});

	var personalPanel = new dorado.touch.Panel({
		caption: "相关信息",
		layout: new dorado.widget.layout.NativeLayout(),
		children:[
			new dorado.touch.GroupBox({
				width: 1000,
				style: {
					margin: "auto"
				},
				contentOverflow: "hidden",
				children: [
					new dorado.widget.AutoForm({
						cols: "*",
						dataSet: dataSetBasic,
						labelWidth: 280,
						elements: [
							{
								property:"lastyearEndDate",
								label:"上一张有效交强险保单到期日期",
								editorType:"label"
							},
							{
								property:"currentStartDate",
								label:"本次测算交强险的保险期间",
								editor: new dorado.widget.DataLabel({
									dataSet: dataSetBasic,
									property:"currentStartDate",
									renderer: {
										render: function(dom, arg) {
											var text = (arg.property && arg.entity != null) ? arg.entity.getText(arg.property) : '';
											dom.innerText = text + " 到 " + arg.entity.getText("currentEndDate");
										}
									}
								})
							},
							{
								property:"lastyearEndDateAuto",
								label:"上一张有效商业险保单到期日期",
								editorType:"label"
							},
							{
								property:"currentStartDateAuto",
								label:"本次测算商业险的保险期间",
								editor: new dorado.widget.DataLabel({
									dataSet: dataSetBasic,
									property:"currentStartDateAuto",
									renderer: {
										render: function(dom, arg) {
											var text = (arg.property && arg.entity != null) ? arg.entity.getText(arg.property) : '';
											dom.innerText = text + " 到 " + arg.entity.getText("currentEndDateAuto");
										}
									}
								})
							}
						]
					})
				]
			}),
			new dorado.touch.GroupBox({
				width: 1000,
				style: {
					margin: "auto"
				},
				contentOverflow: "hidden",
				caption: "上年度理赔记录",
				children: [
					new dorado.widget.AutoForm({
						cols: "*,*",
						dataSet: dataSetBasic,
						labelWidth: 120,
						elements: [
							{
								label:"交强险",
								editor: new dorado.widget.DataLabel({
									dataSet: dataSetBasic,
									property:"claimCountTraffic",
									renderer: {
										render: function(dom, arg) {
											var text = (arg.property && arg.entity != null) ? arg.entity.getText(arg.property) : '';
											dom.innerText = "出险次数" + text + "次";
										}
									}
								})
							},
							{
								label:"合计理赔金额",
								editor: new dorado.widget.DataLabel({
									dataSet: dataSetBasic,
									property:"claimAmtTraffic",
									renderer: {
										render: function(dom, arg) {
											var text = (arg.property && arg.entity != null) ? arg.entity.getText(arg.property) : '';
											dom.innerText = text + "元";
										}
									}
								})
							},
							{
								label:"商业险",
								editor: new dorado.widget.DataLabel({
									dataSet: dataSetBasic,
									property:"claimCountAutoComprenhensive",
									renderer: {
										render: function(dom, arg) {
											var text = (arg.property && arg.entity != null) ? arg.entity.getText(arg.property) : '';
											dom.innerText = "出险次数" + text + "次";
										}
									}
								})
							},
							{
								label:"合计理赔金额",
								editor: new dorado.widget.DataLabel({
									dataSet: dataSetBasic,
									property:"claimAmtAutoComprenhensive",
									renderer: {
										render: function(dom, arg) {
											var text = (arg.property && arg.entity != null) ? arg.entity.getText(arg.property) : '';
											dom.innerText = text + "元";
										}
									}
								})
							}
						]
					})
				]
			}),
			new dorado.touch.GroupBox({
				contentOverflow: "hidden",
				layout: new dorado.widget.layout.NativeLayout(),
				caption:"个人信息",
				width: 1000,
				style: {
					margin: "auto"
				},
				children:[basicAutoForm]
			}),
			new dorado.widget.Container({
                layout: new dorado.widget.layout.NativeLayout(),
                style: {
                    padding: 10
                },
                children: [
                    new dorado.touch.Button({
                        caption:"生成报价单",
                        style: {
                            float: "right"
                        },
                        //exClassName: "login-button",
                        width: 200,
                        onTap: controller.createQuotation
                    })
                ]
            })
		],
		toolbarItems:[
			new dorado.touch.Button({
				caption:"返回",
				onTap:function () {				
					cardbook.set("currentControl", premiumDetailPanel);
				}
			}),
			new dorado.touch.Spacer(),
			new dorado.touch.Button({
				caption:"结束",
				onTap:function () {
					window.location.reload();
				}
			})
		]
	});

	var passengerCountLabel = new dorado.widget.Label({
		style:{
			display:"inline-block"
		},
		width:40,
		text: ""
	});

	var selectPanel = new dorado.touch.Panel({
		caption: "请选择承保险种",
        exClassName: "select-panel",
		children:[
			new dorado.touch.Panel({
				layout: new dorado.widget.layout.NativeLayout(),
				children: [
					{
					   $type: "Container",
					   width: 1000,
					   style: {
					       margin: "auto"
					   },
					   layout:{
					       $type:"Form",
					       cols:"*,*"
					   },
					   children:[
					       {
					           $type:"Container",
					           layout:new dorado.widget.layout.NativeLayout(),
					           children:[
						           new dorado.touch.CheckBox({
							           tags: ["category"],
							           id: traffic,
							           checked:true,
							           caption:"交强险及车船税",
							           exClassName:"block-el"
						           }),
						           new dorado.touch.CheckBox({
							           tags: ["category"],
							           id: damage,
							           checked:true,
							           caption:"机动车损失险",
							           exClassName:"block-el  region-top",
							           onValueChange: function(self) {
								           var readOnly = !self._checked;
								           this.tag("vehicledamage").set("readOnly", readOnly);
								           var damageAmountEditor = view.id(damageAmount);
								           if (damageAmountEditor) {
									           if (!readOnly) {
										           var data = dataSetBasic.get("data"), purchasePrice = data.get("purchasePrice");
										           damageAmountEditor.set("value", purchasePrice);
									           } else {
										           damageAmountEditor.set("value", "");
									           }
								           }
							           }
						           }),
						           new dorado.widget.Container({
							           layout:new dorado.widget.layout.NativeLayout(),
							           exClassName:"detail-region",
							           children:[
								           new dorado.widget.Label({
									           style:{
										           display:"inline-block",
										           marginRight:20,
										           marginTop: 3,
										           marginBottom: 3
									           },
									           text:"保额(元)"
								           }),
								           new dorado.touch.TextEditor({
									           id: damageAmount,
									           tags: ["vehicledamage"],
									           style:{
										           display:"inline-block"
									           },
									           width:100
								           }),
								           new dorado.touch.CheckBox({
									           id: damageSepcial,
									           tags: ["vehicledamage"],
									           caption:"不计免赔",
									           exClassName:"block-el"
								           }),
								           new dorado.widget.Container({
									           height: 45,
									           layout: new dorado.widget.layout.NativeLayout(),
									           children: [
										           new dorado.touch.CheckBox({
											           id: glass,
											           tags: ["vehicledamage"],
											           caption:"玻璃单独破碎险",
											           style: {
												           "float": "left",
												           marginRight: 30
											           }
										           }),
										           new dorado.touch.RadioButtonGroup({
											           id: glassKind,
											           tags: ["vehicledamage", "glass"],
											           style:{
												           "float": "left",
												           marginTop: 3
											           },
											           value:true,
											           buttons:[
												           new dorado.touch.RadioButton({
													           value:0,
													           caption:"国产"
												           }),
												           new dorado.touch.RadioButton({
													           value:1,
													           caption:"进口"
												           })
											           ]
										           })
									           ]
								           }),
								           new dorado.touch.CheckBox({
									           id: paint,
									           tags: ["vehicledamage"],
									           caption:"车身油漆单独损伤险",
									           style:{
                                                   float: "left"
									           }
								           }),
								           new dorado.widget.Container({
									           layout: new dorado.widget.layout.NativeLayout(),
									           height: 45,
									           children: [
										           new dorado.widget.Label({
											           style:{
												           "float": "left",
												           marginRight: 20,
												           marginLeft: 20,
												           marginTop: 3
											           },
											           text:"保额"
										           }),
										           new dorado.touch.RadioButtonGroup({
											           id: paintAmount,
											           tags: ["vehicledamage", "paint"],
											           style: {
												           marginTop: 3,
												           clear: "right"
											           },
											           buttons: [
												           new dorado.touch.RadioButton({ caption: "2千", value: 2000 }),
												           new dorado.touch.RadioButton({ caption: "5千", value: 5000 }),
												           new dorado.touch.RadioButton({ caption: "1万", value: 10000 }),
												           new dorado.touch.RadioButton({ caption: "2万", value: 20000 })
											           ]
										           })
									           ]
								           }),
								           new dorado.touch.CheckBox({
									           id: paddle,
									           tags: ["vehicledamage"],
									           caption:"涉水损失险",
									           exClassName:"block-el"
								           }),
								           new dorado.touch.CheckBox({
									           id: selfignite,
									           tags: ["vehicledamage"],
									           caption:"自燃损失险",
									           exClassName:"block-el",
									           style:{
										           marginRight:"40px",
                                                   float: "left"
									           },
									           onValueChange: function(self) {
										           var readOnly = !self._checked;
										           var selfigniteAmountEditor=view.id(selfigniteAmount);
										           if (selfigniteAmountEditor) {
											           if (!readOnly) {
												           var data = dataSetBasic.get("data"), currentValue = data.get("currentValue");
												           selfigniteAmountEditor.set("value", currentValue);
											           } else {
												           selfigniteAmountEditor.set("value", "");
											           }
										           }
									           }
								           }),
								           new dorado.widget.Container({
									           layout: new dorado.widget.layout.NativeLayout(),
									           height: 45,
									           children: [
										           new dorado.widget.Label({
											           style:{
												           "float": "left",
												           marginRight: 20,
												           marginLeft: 20,
												           marginTop: 3
											           },
											           text:"保额"
										           }),
										           new dorado.touch.TextEditor({
											           id: selfigniteAmount,
											           tags: ["vehicledamage", "selfignite"],
											           style:{
												           display:"inline-block"
											           },
											           width:100
										           })
									           ]
								           }),
                                           new dorado.touch.CheckBox({
                                               id: carDamageDeductible,
                                               tags: ["vehicledamage"],
                                               caption: "车损免赔额特约",
                                               style: {
                                                   "float": "left",
                                                   marginRight: 30
                                               }
                                           }),
                                           new dorado.touch.RadioButtonGroup({
                                               id: carDamageDeductibleAmount,
                                               tags: ["vehicledamage", "carDamageDeductible"],
                                               style: {
                                                   marginTop: 3,
                                                   clear: "right"
                                               },
                                               buttons: [
                                                   new dorado.touch.RadioButton({ caption: "300", value: 300 }),
                                                   new dorado.touch.RadioButton({ caption: "500", value: 500 }),
                                                   new dorado.touch.RadioButton({ caption: "1000", value: 1000 }),
                                                   new dorado.touch.RadioButton({ caption: "2000", value: 2000 })
                                               ]
                                           }),
                                           new dorado.touch.CheckBox({
                                               id: specialCarDamage,
                                               exClassName:"block-el",
                                               tags: ["vehicledamage"],
                                               caption: "特种车车辆损失扩展险"
                                           }),
                                           new dorado.touch.CheckBox({
                                               id: appointedRepairFactory,
                                               exClassName:"block-el",
                                               tags: ["vehicledamage"],
                                               caption: "指定专修厂特约"
                                           })
							           ]
						           })
					           ]
					       },
					       {
					           $type:"Container",
					           layout:new dorado.widget.layout.NativeLayout(),
					           children:[
                                   new dorado.touch.CheckBox({
                                       tags: ["category"],
                                       id: theft,
                                       checked:true,
                                       caption:"机动车全车盗抢损失险",
                                       exClassName:"block-el",
                                       onValueChange: function(self) {
                                           var readOnly = !self._checked;
                                           this.tag("stealing").set("readOnly", readOnly);
                                           var theftAmountEditor = view.id(theftAmount);
                                           if (theftAmountEditor) {
                                               if (!readOnly) {
                                                   var data = dataSetBasic.get("data"), currentValue = data.get("currentValue");
                                                   theftAmountEditor.set("value", currentValue);
                                               } else {
                                                   theftAmountEditor.set("value", "");
                                               }
                                           }
                                       }
                                   }),
                                   new dorado.widget.Container({
                                       layout:new dorado.widget.layout.NativeLayout(),
                                       exClassName:"detail-region",
                                       children: [
                                           new dorado.widget.Label({
                                               style: {
                                                   display:"inline-block",
                                                   marginRight:20,
                                                   marginTop: 3,
                                                   marginBottom: 3
                                               },
                                               text:"保额(元)"
                                           }),
                                           new dorado.touch.TextEditor({
                                               id: theftAmount,
                                               tags: ["stealing"],
                                               style: {
                                                   display:"inline-block"
                                               },
                                               width:100
                                           }),
                                           new dorado.touch.CheckBox({
                                               id: theftSpecial,
                                               tags: ["stealing"],
                                               caption: "不计免赔",
                                               exClassName: "block-el"
                                           })
                                       ]
                                   }),
						           new dorado.touch.CheckBox({
							           tags: ["category"],
							           id: third,
							           checked: true,
							           caption: "机动车第三者责任险",
							           exClassName: "block-el region-top",
							           onValueChange: function(self) {
								           this.tag("thirdparty").set("readOnly", !self._checked);
							           }
						           }),
						           new dorado.widget.Container({
							           layout: new dorado.widget.layout.NativeLayout(),
                                       height: 45,
							           exClassName: "detail-region",
							           children:[
								           new dorado.widget.Label({
									           style: {
										           "float": "left",
										           display: "inline-block",
										           marginRight: 20,
										           marginTop: 3,
										           marginBottom: 3
									           },
									           text: "保额"
								           }),
								           new dorado.touch.RadioButtonGroup({
									           id: thirdAmount,
									           tags: ["thirdparty"],
									           style: {
										           "float": "left",
										           marginRight: 20,
										           marginTop: 3
									           },
									           buttons: [
										           new dorado.touch.RadioButton({ caption: "5万", value: 50000 }),
										           new dorado.touch.RadioButton({ caption: "10万", value: 100000 }),
										           new dorado.touch.RadioButton({ caption: "15万", value: 150000 }),
										           new dorado.touch.RadioButton({ caption: "20万", value: 200000 }),
										           new dorado.touch.RadioButton({ caption: "30万", value: 300000 }),
										           new dorado.touch.RadioButton({ caption: "50万", value: 500000 }),
										           new dorado.touch.RadioButton({ caption: "100万", value: 1000000 })
									           ]
								           })
							           ]
						           }),
						           new dorado.touch.CheckBox({
							           id: thirdSpecial,
							           tags: ["thirdparty"],
							           exClassName:"detail-region block-el",
							           caption:"不计免赔"
						           }),
                                   new dorado.touch.CheckBox({
                                       id: cargoIncar,
                                       tags: ["thirdparty"],
                                       exClassName:"detail-region block-el",
                                       caption:"车上货物责任险",
                                       style: {
                                           float: "left",
                                           marginRight: 20
                                       }
                                   }),
                                   new dorado.widget.Container({
                                       layout:new dorado.widget.layout.NativeLayout(),
                                       exClassName:"detail-region",
                                       children: [
                                           new dorado.widget.Label({
                                               style: {
                                                   display:"inline-block",
                                                   marginRight:20,
                                                   marginTop: 3,
                                                   marginBottom: 3
                                               },
                                               text:"保额(元)"
                                           }),
                                           new dorado.touch.TextEditor({
                                               id: cargoIncarAmount,
                                               tags: ["thirdparty"],
                                               style: {
                                                   display:"inline-block"
                                               },
                                               width:100
                                           })
                                       ]
                                   }),
						           new dorado.touch.CheckBox({
							           tags: ["category"],
							           id: incar,
							           checked:true,
							           caption:"车上人员责任险",
							           exClassName:"block-el region-top",
							           onValueChange: function(self) {
								           this.tag("person").set("readOnly", !self._checked);
							           }
						           }),
						           new dorado.widget.Container({
							           layout:new dorado.widget.layout.NativeLayout(),
							           exClassName:"detail-region",
							           children:[
								           new dorado.touch.CheckBox({
									           id: driver,
									           tags: ["person"],
									           caption:"司机"
								           }),
								           new dorado.widget.Label({
									           style:{
										           display:"inline-block",
										           marginRight:20,
										           marginLeft:40
									           },
									           text:"保额(元)"
								           }),
								           new dorado.touch.TextEditor({
									           id: driverAmount,
									           tags: ["person"],
									           style:{
										           display:"inline-block"
									           },
									           width:100,
									           value:10000
								           }),
								           new dorado.widget.Label({ text:"", style:{ height:0 } }),
								           new dorado.touch.CheckBox({
									           id: passenger,
									           tags: ["person"],
									           caption:"乘客"
								           }),
								           new dorado.widget.Label({
									           style:{
										           display:"inline-block",
										           marginRight:20,
										           marginLeft:40
									           },
									           text:"保额(元)"
								           }),
								           new dorado.touch.TextEditor({
									           id: passengerAmount,
									           tags: ["person"],
									           style:{
										           display:"inline-block"
									           },
									           width:100,
									           value:10000
								           }),
								           new dorado.widget.Label({
									           style:{
										           display:"inline-block",
										           marginRight:20,
										           marginLeft:40
									           },
									           text:"座位数"
								           }),
								           passengerCountLabel,
								           new dorado.touch.CheckBox({
									           id: incarSpecial,
									           tags: ["person"],
									           caption:"不计免赔",
									           exClassName:"block-el"
								           })
							           ]
						           }),
						           new dorado.touch.CheckBox({
							           tags: ["category"],
							           id: rider,
							           checked:true,
							           caption:"附加险不计免赔险",
							           exClassName:"block-el region-top"
						           })
					           ]
					       }
					   ]
					}
				]
			})
		],
		toolbarItems:[
			new dorado.touch.Button({
				caption:"返回",
				onTap:function () {
					var items = modelGrid.get("items");
					if (items.length == 1) {
						//cardbook.set("currentControl", controller.beijing ? indexPanel : indexPanelOther);
						controller.clearRenewInfo(function() {
							cardbook.set("currentControl", controller.beijing ? indexPanel : indexPanelOther);
						});
					} else {
						cardbook.set("currentControl", selectModelPanel);
					}
				}
			}),
			new dorado.touch.Spacer(),
			new dorado.touch.Button({
				caption:"保费试算",
				onTap: controller.submitCoverage
			})
		]
	});

	var resultHtmlCt = new dorado.widget.HtmlContainer({
		contentOverflow: "hidden"
	});

	var resultDetailPanel = new dorado.touch.GroupBox({
		caption: "",
		layout: new dorado.widget.layout.NativeLayout(),
		width: 1000,
		contentOverflow: "hidden",
		style: {
			margin: "30px auto"
		},
		children: [
			resultHtmlCt
		]
	});

	var resultPanel = new dorado.touch.Panel({
		caption: "保费试算结果",
		layout: new dorado.widget.layout.NativeLayout(),
		//scrollbar: true,
		exClassName: "result-panel",
		children:[
			new dorado.touch.GroupBox({
				caption: "",
				width: 1000,
				style: {
					margin: "40px auto"
				},
				layout: new dorado.widget.layout.NativeLayout(),
				contentOverflow: "hidden",
				children: [
					new dorado.widget.AutoForm({
						dataSet: dataSetResult,
						labelWidth:120,
						cols:"*,*",
						elements:[
							{
								property:"vehicleLicense",
								label:"号牌号码",
								editorType:"label"
							},
							{
								property:"vehicleUsage",
								label:"使用性质",
								editorType:"label"
							},
							{
								property:"vehicleVariety",
								label:"车辆种类",
								editorType:"label"
							},
							{
								property:"purchasePrice",
								label:"新车购置价",
								editor: new dorado.widget.DataLabel({
									dataSet: dataSetResult,
									property:"purchasePrice",
									renderer: {
										render: function(dom, arg) {
											var text = (arg.property && arg.entity != null) ? arg.entity.getText(arg.property) : '';
											dom.innerText = text + "元";
										}
									}
								})
							},
							{
								property:"usageLimitYear",
								label:"使用年限",
								editorType:"label"
							},
							null,
							{
								property:"currentStartDate",
								label:"交强险起期",
								editorType:"label"
							},
							{
								property:"currentEndDate",
								label:"交强险止期",
								editorType:"label"
							},
							{
								property:"currentStartDateAuto",
								label:"商业险起期",
								editorType:"label"
							},
							{
								property:"currentEndDateAuto",
								label:"商业险止期",
								editorType:"label"
							},
							{
								property:"totalFee",
								label:"商业险保费",
								editor: new dorado.widget.DataLabel({
									dataSet: dataSetResult,
									property:"totalFeeAuto",
									renderer: {
										render: function(dom, arg) {
											var text = (arg.property && arg.entity != null) ? arg.entity.getText(arg.property) : '';
											dom.innerText = (text || "0") + "元";
										}
									}
								})
							},
							{
								property:"totalFeeTraffic",
								label:"交强险保费",
								editor: new dorado.widget.DataLabel({
									dataSet: dataSetResult,
									property:"totalFeeTraffic",
									renderer: {
										render: function(dom, arg) {
											var text = (arg.property && arg.entity != null) ? arg.entity.getText(arg.property) : '';
											dom.innerText = (text || "0") + "元";
										}
									}
								})
							},
							{
								property:"taxAmt",
								label:"车船税",
								editor: new dorado.widget.DataLabel({
									dataSet: dataSetResult,
									property:"taxAmt",
									renderer: {
										render: function(dom, arg) {
											var text = (arg.property && arg.entity != null) ? arg.entity.getText(arg.property) : '';
											dom.innerText = text + "元";
										}
									}
								})
							}
						]
					})
				]
			}),
			new dorado.widget.HtmlContainer({
				content:"<div class='tip-done'>" +
					        "<p>恭喜您完成太保车险自主测算信息填写!</p>" +
					        "<p>一切测算结果基于测算当日时间为准,实际保单价格应以实际出单日价格为准。</p>" +
					"</div>",
				height:120
			}),
			resultDetailPanel
		],
		toolbarItems:[
			new dorado.touch.Button({
				caption:"返回",
				onTap:function () {
					cardbook.set("currentControl", selectPanel);
				}
			}),
			new dorado.touch.Spacer(),
			new dorado.touch.Button({
				caption:"保费详细",
				onTap:function () {
					//window.location.reload();
					cardbook.set("currentControl", premiumDetailPanel);
				}
			})
		]
	});

	var premiumDetailPanel = new dorado.touch.Panel({
		caption: "保费明细",
		layout: new dorado.widget.layout.NativeLayout(),
		//scrollbar: true,
		exClassName: "result-panel",
		children:[
			resultDetailPanel
		],
		toolbarItems:[
			new dorado.touch.Button({
				caption:"返回",
				onTap:function () {
					cardbook.set("currentControl", resultPanel);
				}
			}),
			new dorado.touch.Spacer(),
			new dorado.touch.Button({
				caption:"继续",
				onTap:controller.reLoadDetailInfo
			})
		]
	});

	var cardbook = new dorado.widget.CardBook({
		controls:[loginPanel, indexPanel, indexPanelOther, selectModelPanel, personalPanel, selectPanel, resultPanel, premiumDetailPanel],
        height: 650
	});

	jQuery.extend(controller, {
		view: view,
		cardbook: cardbook,
		resultPanel: resultPanel,
		resultDetailPanel: resultDetailPanel,
		resultHtmlCt: resultHtmlCt,
		selectPanel: selectPanel,
		loginPanel:	loginPanel,
		passengerCountLabel: passengerCountLabel,
		personalPanel: personalPanel,
		licenseTypeEditor: licenseTypeEditor,
		licenseTypeList: licenseTypeList,
        vehicleTypeList: vehicleTypeList,
        vehicleTypeEditor: vehicleTypeEditor,
        vehiclePurposeList: vehiclePurposeList,
        vehiclePurposeEditor: vehiclePurposeEditor,
		selectModelPanel: selectModelPanel,
		modelGrid: modelGrid,
		modelForm: modelForm,
		basicAutoForm: basicAutoForm,
		indexPanel: indexPanel,
		indexPanelOther: indexPanelOther,
		selectCarDialog: selectCarDialog,
		carGrid: carGrid,
		loginForm: loginForm,
		vehicleUsageEditor: vehicleUsageEditor,
		vehicleUsageEditorOther: vehicleUsageEditorOther,
		vehicleUsageList: vehicleUsageList,
		platenoEditor: platenoEditor,
		platenoEditorOther: platenoEditorOther,
		ownerEditor: ownerEditor,
		ownerEditorOther: ownerEditorOther,
		rackNoEditor: rackNoEditor,
		engineNoEditor: engineNoEditor,
		vehicleNameEditor: vehicleNameEditor,
		registerDateEditor: registerDateEditor,
		branchCodeEditor: branchCodeEditor,
		userCodeEditor: userCodeEditor,
		passwordEditor: passwordEditor,
        startDateEditor: startDateEditor,
        endDateEditor: endDateEditor,
        startDateAutoEditor: startDateAutoEditor,
        endDateAutoEditor: endDateAutoEditor,
        engineCapacityEditor :engineCapacityEditor,
    	emptyWeightEditor :emptyWeightEditor,
        //other
        startDateEditor1: startDateEditor1,
        endDateEditor1: endDateEditor1,
        startDateAutoEditor1: startDateAutoEditor1,
        endDateAutoEditor1: endDateAutoEditor1,
        imageEditor: imageEditor,
        image: image,
        renewGrid: renewGrid,
        renewInfoDialog: renewInfoDialog,
        renewPlatenoEditor: renewPlatenoEditor,
        renewPolicyNoEditor: renewPolicyNoEditor,
        renewCarVINEditor: renewCarVINEditor,
        renewEngineNoEditor: renewEngineNoEditor,
        renewTrafficEditor: renewTrafficEditor,

        avgMileEditor: avgMileEditor,
        appointDriverEditor: appointDriverEditor,
        driverAreaEditor: driverAreaEditor,
        apointFactoryRateEditor: apointFactoryRateEditor
	});

	view.addChild(cardbook);
    if ((/android/gi).test(navigator.appVersion) && PhoneGap) {
        document.addEventListener("deviceready", function(){
            view.render(document.body);
        },true);
    } else {
        $(document).ready(function () {
            view.render(document.body);
        });
    }

})();
