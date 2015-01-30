{
	"version" : "1.1",
	"packageInfos" : [ {
		"name" : "dorado-core"
	}, {
		"name" : "dorado-hibernate",
		"version" : "1.2.3-SNAPSHOT.150130.1406"
	}, {
		"name" : "dorado-touch"
	}, {
		"name" : "dorado-touch-grid"
	}, {
		"name" : "dorado-intro",
		"version" : "0.1.1.140711.1249"
	}, {
		"name" : "dorado-skin-concise-red",
		"version" : "1.0.65"
	}, {
		"name" : "dorado-htmleditor",
		"version" : "0.7.4-BETA.140612.1136"
	}, {
		"name" : "dorado-raphael",
		"version" : "1.0.3.140506.1102"
	}, {
		"name" : "dorado-criteria-builder",
		"version" : "1.0.1.140709.1857"
	}, {
		"name" : "dorado-desktop",
		"version" : "0.4.9-BETA.140606.1938"
	}, {
		"name" : "dorado-cloudo",
		"version" : "0.0.1-SNAPSHOT.141015.1701"
	}, {
		"name" : "dorado-skin-daybreak",
		"version" : "1.0.35"
	}, {
		"name" : "dorado-portal",
		"version" : "0.1.8-BETA.140709.1600"
	}, {
		"name" : "dorado-map",
		"version" : "1.1.0-BETA.140709.1905"
	}, {
		"name" : "dorado-skin-blue-sky",
		"version" : "1.0.31"
	}, {
		"name" : "dorado-xchart",
		"version" : "0.9.2-BETA.140613.1632"
	}, {
		"name" : "dorado-qrcode",
		"version" : "1.0.4.140609.2313"
	}, {
		"name" : "dorado-chart",
		"version" : "0.4.6-BETA.140506.1451"
	}, {
		"name" : "dorado-canvas",
		"version" : "1.2.0.140709.1855"
	}, {
		"name" : "dorado-source-editor"
	}, {
		"name" : "dorado-skin-gray",
		"version" : "0.0.32"
	} ],
	"rules" : [
			{
				"name" : "Auxiliary",
				"abstract" : false,
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"children" : [ {
					"rule" : "Import",
					"name" : "Import",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"rule" : "GroupStart",
					"name" : "GroupStart",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"rule" : "GroupEnd",
					"name" : "GroupEnd",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"rule" : "PlaceHolder",
					"name" : "PlaceHolder",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"rule" : "PlaceHolderStart",
					"name" : "PlaceHolderStart",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"rule" : "PlaceHolderEnd",
					"name" : "PlaceHolderEnd",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "Import",
				"abstract" : false,
				"scope" : "public",
				"sortFactor" : 9001,
				"category" : "Auxiliary",
				"icon" : "\/com\/bstek\/dorado\/idesupport\/icons\/Import.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"primitiveProps" : [ {
					"name" : "id",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "src",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "GroupStart",
				"abstract" : false,
				"scope" : "public",
				"sortFactor" : 9002,
				"category" : "Auxiliary",
				"icon" : "\/com\/bstek\/dorado\/idesupport\/icons\/GroupStart.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"primitiveProps" : [ {
					"name" : "id",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "GroupEnd",
				"abstract" : false,
				"scope" : "public",
				"sortFactor" : 9003,
				"category" : "Auxiliary",
				"icon" : "\/com\/bstek\/dorado\/idesupport\/icons\/GroupEnd.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false
			},
			{
				"name" : "PlaceHolder",
				"abstract" : false,
				"scope" : "public",
				"sortFactor" : 9004,
				"category" : "Auxiliary",
				"icon" : "\/com\/bstek\/dorado\/idesupport\/icons\/PlaceHolder.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"primitiveProps" : [ {
					"name" : "id",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "PlaceHolderStart",
				"parents" : "PlaceHolder",
				"abstract" : false,
				"scope" : "public",
				"sortFactor" : 9005,
				"category" : "Auxiliary",
				"icon" : "\/com\/bstek\/dorado\/idesupport\/icons\/PlaceHolderStart.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false
			},
			{
				"name" : "PlaceHolderEnd",
				"abstract" : false,
				"scope" : "public",
				"sortFactor" : 9006,
				"category" : "Auxiliary",
				"icon" : "\/com\/bstek\/dorado\/idesupport\/icons\/PlaceHolderEnd.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false
			},
			{
				"name" : "Model",
				"abstract" : false,
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/view\/manager\/Model.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"children" : [ {
					"rule" : "AbstractDataType",
					"name" : "DataType",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"rule" : "AbstractDataProvider",
					"name" : "DataProvider",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"rule" : "AbstractDataResolver",
					"name" : "DataResolver",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "AbstractDataType",
				"abstract" : true,
				"nodeName" : "DataType",
				"type" : "com.bstek.dorado.data.type.AbstractDataType",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"primitiveProps" : [ {
					"name" : "name",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"props" : [ {
					"name" : "name",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "AbstractDataProvider",
				"abstract" : true,
				"nodeName" : "DataProvider",
				"type" : "com.bstek.dorado.data.provider.AbstractDataProvider",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"primitiveProps" : [ {
					"name" : "impl",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "parent",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "scope",
					"enumValues" : "instant,thread,singleton,session,request",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "listener",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "overwrite",
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"type" : "boolean"
				}, {
					"name" : "name",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"props" : [ {
					"name" : "impl",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "parent",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "scope",
					"enumValues" : "instant,thread,singleton,session,request",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "listener",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "overwrite",
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"type" : "boolean"
				}, {
					"name" : "name",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "AbstractDataResolver",
				"abstract" : true,
				"nodeName" : "DataResolver",
				"type" : "com.bstek.dorado.data.resolver.AbstractDataResolver",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"primitiveProps" : [ {
					"name" : "impl",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "parent",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "scope",
					"enumValues" : "instant,thread,singleton,session,request",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "listener",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "overwrite",
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"type" : "boolean"
				}, {
					"name" : "name",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"props" : [ {
					"name" : "impl",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "parent",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "scope",
					"enumValues" : "instant,thread,singleton,session,request",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "listener",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "overwrite",
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"type" : "boolean"
				}, {
					"name" : "name",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "ViewConfig",
				"label" : "ViewConfig",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.manager.ViewConfig",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/view\/manager\/ViewConfig.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"primitiveProps" : [ {
					"name" : "listener",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "template",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"props" : [ {
					"name" : "listener",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "template",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [
						{
							"name" : "Arguments",
							"fixed" : false,
							"aggregated" : false,
							"clientTypes" : "desktop,touch",
							"deprecated" : false,
							"rule" : {
								"name" : "Arguments",
								"abstract" : false,
								"nodeName" : "Arguments",
								"scope" : "public",
								"sortFactor" : 0,
								"icon" : "\/com\/bstek\/dorado\/view\/manager\/Arguments.png",
								"autoGenerateId" : false,
								"clientTypes" : "desktop,touch",
								"deprecated" : false,
								"children" : [ {
									"name" : "Argument",
									"fixed" : false,
									"aggregated" : true,
									"clientTypes" : "desktop,touch",
									"deprecated" : false,
									"rule" : {
										"name" : "Argument",
										"abstract" : false,
										"nodeName" : "Argument",
										"scope" : "public",
										"sortFactor" : 0,
										"icon" : "\/com\/bstek\/dorado\/view\/manager\/Argument.png",
										"autoGenerateId" : false,
										"clientTypes" : "desktop,touch",
										"deprecated" : false,
										"primitiveProps" : [ {
											"name" : "name",
											"clientTypes" : "desktop,touch",
											"deprecated" : false
										} ],
										"props" : [ {
											"name" : "name",
											"clientTypes" : "desktop,touch",
											"deprecated" : false
										} ]
									}
								} ]
							}
						},
						{
							"name" : "Context",
							"fixed" : false,
							"aggregated" : false,
							"clientTypes" : "desktop,touch",
							"deprecated" : false,
							"rule" : {
								"name" : "Context",
								"abstract" : false,
								"nodeName" : "Context",
								"scope" : "public",
								"sortFactor" : 0,
								"icon" : "\/com\/bstek\/dorado\/view\/manager\/ViewContext.png",
								"autoGenerateId" : false,
								"clientTypes" : "desktop,touch",
								"deprecated" : false,
								"children" : [ {
									"name" : "Attribute",
									"fixed" : false,
									"aggregated" : true,
									"clientTypes" : "desktop,touch",
									"deprecated" : false,
									"rule" : {
										"name" : "Attribute",
										"abstract" : false,
										"nodeName" : "Attribute",
										"scope" : "public",
										"sortFactor" : 0,
										"icon" : "\/com\/bstek\/dorado\/view\/manager\/Attribute.png",
										"autoGenerateId" : false,
										"clientTypes" : "desktop,touch",
										"deprecated" : false,
										"primitiveProps" : [ {
											"name" : "name",
											"clientTypes" : "desktop,touch",
											"deprecated" : false
										} ],
										"props" : [ {
											"name" : "name",
											"clientTypes" : "desktop,touch",
											"deprecated" : false
										} ]
									}
								} ]
							}
						}, {
							"rule" : "Model",
							"name" : "Model",
							"fixed" : false,
							"aggregated" : false,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						}, {
							"rule" : "View",
							"name" : "View",
							"fixed" : false,
							"aggregated" : false,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						} ]
			},
			{
				"name" : "LayoutHolder",
				"abstract" : false,
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"children" : [ {
					"rule" : "AnchorLayout",
					"name" : "anchor",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"rule" : "DockLayout",
					"name" : "dock",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"rule" : "HboxLayout",
					"name" : "hbox",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"rule" : "VboxLayout",
					"name" : "vbox",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"rule" : "FormLayout",
					"name" : "form",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"rule" : "NativeLayout",
					"name" : "native",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"rule" : "Css3-hboxLayout",
					"name" : "css3-hbox",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"rule" : "Css3-vboxLayout",
					"name" : "css3-vbox",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "View",
				"parents" : "Container",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.View",
				"scope" : "private",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"primitiveProps" : [ {
					"name" : "listener",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"props" : [ {
					"name" : "listener",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"clientEvents" : [ {
					"name" : "onComponentUnregistered",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onLoadData",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onComponentRegistered",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [ {
					"rule" : "Component",
					"name" : "Children",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "AutoForm",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "AutoForm",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.form.autoform.AutoForm",
				"scope" : "public",
				"sortFactor" : 2048,
				"category" : "Form",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/form\/autoform\/AutoForm.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"rule" : "SettingItem",
					"name" : "SettingItem",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"rule" : "AutoFormElement",
					"name" : "AutoFormElement",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"rule" : "com.bstek.dorado.view.widget.Control",
					"name" : "Elements",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "SettingItem",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "SettingItem",
				"abstract" : false,
				"type" : "com.bstek.dorado.touch.widget.form.SettingItem",
				"scope" : "public",
				"sortFactor" : 0,
				"category" : "AutoForm",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/form\/SettingItem.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onGetBindingDataType",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetBindingData",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "TouchMenu",
				"parents" : "com.bstek.dorado.view.widget.Control,FloatControl",
				"label" : "Menu",
				"abstract" : false,
				"nodeName" : "TouchMenu",
				"type" : "com.bstek.dorado.touch.widget.menu.Menu",
				"scope" : "public",
				"sortFactor" : 2094,
				"category" : "Floatable",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/menu\/Menu.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "beforeShow",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetBindingDataType",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeHide",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeClose",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onShow",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onClose",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onHide",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetBindingData",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [ {
					"rule" : "TouchMenuItem",
					"name" : "TouchMenuItem",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"rule" : "com.bstek.dorado.touch.widget.menu.BaseMenuItem",
					"name" : "Items",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "TouchMenuItem",
				"parents" : "com.bstek.dorado.touch.widget.menu.BaseMenuItem",
				"label" : "MenuItem",
				"abstract" : false,
				"nodeName" : "TouchMenuItem",
				"type" : "com.bstek.dorado.touch.widget.menu.MenuItem",
				"scope" : "public",
				"sortFactor" : 0,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/menu\/MenuItem.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onGetBindingDataType",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetBindingData",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [ {
					"rule" : "com.bstek.dorado.touch.widget.menu.BaseMenuItem",
					"name" : "Items",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "DataType",
				"parents" : "EntityDataTypeSupport",
				"label" : "DataType",
				"abstract" : false,
				"type" : "com.bstek.dorado.data.type.DefaultEntityDataType",
				"scope" : "public",
				"sortFactor" : 1,
				"robots" : "datatype-reflection|\u81EA\u52A8\u521B\u5EFAPropertyDefs",
				"icon" : "\/com\/bstek\/dorado\/data\/type\/DefaultEntityDataType.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"children" : [ {
					"rule" : "PropertyDef",
					"name" : "PropertyDef",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "NonAggregationDataType",
				"parents" : "AbstractDataType",
				"abstract" : true,
				"type" : "com.bstek.dorado.data.type.NonAggregationDataType",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false
			},
			{
				"name" : "EntityDataTypeSupport",
				"parents" : "NonAggregationDataType",
				"abstract" : true,
				"nodeName" : "DataType",
				"type" : "com.bstek.dorado.data.type.EntityDataTypeSupport",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"primitiveProps" : [ {
					"name" : "impl",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "parent",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "listener",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "overwrite",
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"type" : "boolean"
				} ],
				"props" : [ {
					"name" : "impl",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "parent",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "listener",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "overwrite",
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"type" : "boolean"
				} ],
				"clientEvents" : [ {
					"name" : "onEntityLoad",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onDataChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onRemove",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeDataChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onMessageChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onInsert",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeStateChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeInsert",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeRemove",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onEntityToText",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onCurrentChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeCurrentChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onAttributeChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onStateChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "PropertyDef",
				"abstract" : true,
				"type" : "com.bstek.dorado.data.type.property.PropertyDef",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"primitiveProps" : [ {
					"name" : "name",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"props" : [ {
					"name" : "name",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"clientEvents" : [ {
					"name" : "onGetText",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGet",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onSet",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onValidate",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [ {
					"rule" : "Validator",
					"name" : "Validators",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "Validator",
				"abstract" : true,
				"nodeName" : "Validator",
				"type" : "com.bstek.dorado.data.type.validator.Validator",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/view\/type\/property\/validator\/Validator.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"primitiveProps" : [ {
					"name" : "name",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "Reference",
				"parents" : "LazyPropertyDef",
				"label" : "Reference",
				"abstract" : false,
				"type" : "com.bstek.dorado.data.type.property.Reference",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/data\/type\/property\/Reference.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onLoadData",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeLoadData",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "PropertyDefSupport",
				"parents" : "PropertyDef",
				"abstract" : true,
				"type" : "com.bstek.dorado.data.type.property.PropertyDefSupport",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false
			},
			{
				"name" : "LazyPropertyDef",
				"parents" : "PropertyDefSupport",
				"abstract" : true,
				"type" : "com.bstek.dorado.data.type.property.LazyPropertyDef",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "BasePropertyDef",
				"parents" : "PropertyDefSupport",
				"label" : "PropertyDef",
				"abstract" : false,
				"nodeName" : "PropertyDef",
				"type" : "com.bstek.dorado.data.type.property.BasePropertyDef",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/data\/type\/property\/BasePropertyDef.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "DirectDataProvider",
				"parents" : "AbstractDataProvider",
				"label" : "DirectDataProvider",
				"abstract" : false,
				"type" : "com.bstek.dorado.data.provider.DirectDataProvider",
				"scope" : "public",
				"sortFactor" : 2,
				"icon" : "\/com\/bstek\/dorado\/data\/provider\/DirectDataProvider.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"primitiveProps" : [ {
					"name" : "type",
					"defaultValue" : "direct",
					"fixed" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"visible" : false
				} ],
				"props" : [ {
					"name" : "type",
					"defaultValue" : "direct",
					"fixed" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"visible" : false
				} ]
			},
			{
				"name" : "HqlDataProvider",
				"parents" : "AbstractDataProvider",
				"label" : "HqlDataProvider",
				"abstract" : false,
				"type" : "com.bstek.dorado.hibernate.provider.HqlDataProvider",
				"scope" : "public",
				"sortFactor" : 3,
				"icon" : "\/com\/bstek\/dorado\/hibernate\/provider\/HqlDataProvider.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"primitiveProps" : [ {
					"name" : "type",
					"defaultValue" : "hibernateHql",
					"fixed" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"visible" : false
				} ],
				"props" : [ {
					"name" : "type",
					"defaultValue" : "hibernateHql",
					"fixed" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"visible" : false
				} ]
			},
			{
				"name" : "CriteriaDataProvider",
				"parents" : "AbstractDataProvider",
				"label" : "CriteriaDataProvider",
				"abstract" : false,
				"type" : "com.bstek.dorado.hibernate.provider.CriteriaDataProvider",
				"scope" : "public",
				"sortFactor" : 4,
				"icon" : "\/com\/bstek\/dorado\/hibernate\/provider\/CriteriaDataProvider.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"primitiveProps" : [ {
					"name" : "type",
					"defaultValue" : "hibernateCriteria",
					"fixed" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"visible" : false
				} ],
				"props" : [ {
					"name" : "type",
					"defaultValue" : "hibernateCriteria",
					"fixed" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"visible" : false
				} ],
				"children" : [ {
					"rule" : "TopCriteria",
					"name" : "Criteria",
					"fixed" : true,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "TopCriteria",
				"parents" : "BaseCriteria",
				"label" : "TopCriteria",
				"abstract" : false,
				"nodeName" : "Criteria",
				"type" : "com.bstek.dorado.hibernate.criteria.TopCriteria",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "BaseCriteria",
				"abstract" : true,
				"type" : "com.bstek.dorado.hibernate.criteria.BaseCriteria",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"name" : "Aliases",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"rule" : {
						"name" : "Wrapper.Aliases",
						"label" : "Aliases",
						"abstract" : false,
						"nodeName" : "Aliases",
						"scope" : "public",
						"sortFactor" : 0,
						"autoGenerateId" : false,
						"clientTypes" : "desktop,touch",
						"deprecated" : false,
						"children" : [ {
							"rule" : "Alias",
							"name" : "Aliases",
							"fixed" : false,
							"aggregated" : true,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						} ]
					}
				}, {
					"name" : "Criterions",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"rule" : {
						"name" : "Wrapper.Criterions",
						"label" : "Criterions",
						"abstract" : false,
						"nodeName" : "Criterions",
						"scope" : "public",
						"sortFactor" : 0,
						"autoGenerateId" : false,
						"clientTypes" : "desktop,touch",
						"deprecated" : false,
						"children" : [ {
							"rule" : "BaseCriterion",
							"name" : "Criterions",
							"fixed" : false,
							"aggregated" : true,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						} ]
					}
				}, {
					"name" : "FetchModes",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"rule" : {
						"name" : "Wrapper.FetchModes",
						"label" : "FetchModes",
						"abstract" : false,
						"nodeName" : "FetchModes",
						"scope" : "public",
						"sortFactor" : 0,
						"autoGenerateId" : false,
						"clientTypes" : "desktop,touch",
						"deprecated" : false,
						"children" : [ {
							"rule" : "FetchMode",
							"name" : "FetchModes",
							"fixed" : false,
							"aggregated" : true,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						} ]
					}
				}, {
					"name" : "Orders",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"rule" : {
						"name" : "Wrapper.Orders",
						"label" : "Orders",
						"abstract" : false,
						"nodeName" : "Orders",
						"scope" : "public",
						"sortFactor" : 0,
						"autoGenerateId" : false,
						"clientTypes" : "desktop,touch",
						"deprecated" : false,
						"children" : [ {
							"rule" : "Order",
							"name" : "Orders",
							"fixed" : false,
							"aggregated" : true,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						} ]
					}
				}, {
					"name" : "Projections",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"rule" : {
						"name" : "Wrapper.Projections",
						"label" : "Projections",
						"abstract" : false,
						"nodeName" : "Projections",
						"scope" : "public",
						"sortFactor" : 0,
						"autoGenerateId" : false,
						"clientTypes" : "desktop,touch",
						"deprecated" : false,
						"children" : [ {
							"rule" : "BaseProjection",
							"name" : "Projections",
							"fixed" : false,
							"aggregated" : true,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						} ]
					}
				}, {
					"name" : "SubCriterias",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"rule" : {
						"name" : "Wrapper.SubCriterias",
						"label" : "SubCriterias",
						"abstract" : false,
						"nodeName" : "SubCriterias",
						"scope" : "public",
						"sortFactor" : 0,
						"autoGenerateId" : false,
						"clientTypes" : "desktop,touch",
						"deprecated" : false,
						"children" : [ {
							"rule" : "SubCriteria",
							"name" : "SubCriterias",
							"fixed" : false,
							"aggregated" : true,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						} ]
					}
				} ]
			},
			{
				"name" : "Alias",
				"label" : "Alias",
				"abstract" : false,
				"type" : "com.bstek.dorado.hibernate.criteria.Alias",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"name" : "Criterions",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"rule" : {
						"name" : "Wrapper.Criterions",
						"label" : "Criterions",
						"abstract" : false,
						"nodeName" : "Criterions",
						"scope" : "public",
						"sortFactor" : 0,
						"autoGenerateId" : false,
						"clientTypes" : "desktop,touch",
						"deprecated" : false,
						"children" : [ {
							"rule" : "BaseCriterion",
							"name" : "Criterions",
							"fixed" : false,
							"aggregated" : true,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						} ]
					}
				} ]
			},
			{
				"name" : "BaseCriterion",
				"abstract" : true,
				"type" : "com.bstek.dorado.hibernate.criteria.criterion.BaseCriterion",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "AndCriterion",
				"parents" : "JunctionCriterion",
				"label" : "AndCriterion",
				"abstract" : false,
				"nodeName" : "And",
				"type" : "com.bstek.dorado.hibernate.criteria.criterion.AndCriterion",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false
			},
			{
				"name" : "JunctionCriterion",
				"parents" : "BaseCriterion",
				"abstract" : true,
				"type" : "com.bstek.dorado.hibernate.criteria.criterion.JunctionCriterion",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"children" : [ {
					"rule" : "BaseCriterion",
					"name" : "Criterions",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "OrCriterion",
				"parents" : "JunctionCriterion",
				"label" : "OrCriterion",
				"abstract" : false,
				"nodeName" : "Or",
				"type" : "com.bstek.dorado.hibernate.criteria.criterion.OrCriterion",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false
			},
			{
				"name" : "SubQueryValueCriterion",
				"parents" : "BaseCriterion",
				"label" : "SubQueryValueCriterion",
				"abstract" : false,
				"type" : "com.bstek.dorado.hibernate.criteria.criterion.SubQueryValueCriterion",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"rule" : "TopCriteria",
					"name" : "Criteria",
					"fixed" : true,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "SizeCriterion",
				"parents" : "SingleProperyCriterion",
				"label" : "SizeCriterion",
				"abstract" : false,
				"type" : "com.bstek.dorado.hibernate.criteria.criterion.SizeCriterion",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "SingleProperyCriterion",
				"parents" : "BaseCriterion",
				"abstract" : true,
				"type" : "com.bstek.dorado.hibernate.criteria.criterion.SingleProperyCriterion",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "SubQueryNoValueCriterion",
				"parents" : "BaseCriterion",
				"label" : "SubQueryNoValueCriterion",
				"abstract" : false,
				"type" : "com.bstek.dorado.hibernate.criteria.criterion.SubQueryNoValueCriterion",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"rule" : "TopCriteria",
					"name" : "Criteria",
					"fixed" : true,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "IdEqCriterion",
				"parents" : "BaseCriterion",
				"label" : "IdEqCriterion",
				"abstract" : false,
				"type" : "com.bstek.dorado.hibernate.criteria.criterion.IdEqCriterion",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "SqlCriterion",
				"parents" : "BaseCriterion",
				"label" : "SqlCriterion",
				"abstract" : false,
				"type" : "com.bstek.dorado.hibernate.criteria.criterion.SqlCriterion",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"name" : "Parameters",
					"fixed" : true,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"rule" : {
						"name" : "Wrapper.Parameters",
						"label" : "Parameters",
						"abstract" : false,
						"nodeName" : "Parameters",
						"scope" : "public",
						"sortFactor" : 0,
						"autoGenerateId" : false,
						"clientTypes" : "desktop,touch",
						"deprecated" : false,
						"children" : [ {
							"rule" : "Parameter",
							"name" : "Parameters",
							"fixed" : false,
							"aggregated" : true,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						} ]
					}
				} ]
			},
			{
				"name" : "Parameter",
				"label" : "Parameter",
				"abstract" : false,
				"type" : "com.bstek.dorado.hibernate.criteria.criterion.SqlCriterion$Parameter",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "InCriterion",
				"parents" : "SingleProperyCriterion",
				"label" : "InCriterion",
				"abstract" : false,
				"type" : "com.bstek.dorado.hibernate.criteria.criterion.InCriterion",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "BetweenCriterion",
				"parents" : "SingleProperyCriterion",
				"label" : "BetweenCriterion",
				"abstract" : false,
				"type" : "com.bstek.dorado.hibernate.criteria.criterion.BetweenCriterion",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "NonValueCriterion",
				"parents" : "SingleProperyCriterion",
				"label" : "NonValueCriterion",
				"abstract" : false,
				"type" : "com.bstek.dorado.hibernate.criteria.criterion.NonValueCriterion",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"primitiveProps" : [ {
					"name" : "op",
					"highlight" : 0,
					"enumValues" : "null,!null,empty,!empty",
					"editor" : "",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "SingleCriterion",
				"parents" : "SingleProperyCriterion",
				"label" : "SingleCriterion",
				"abstract" : false,
				"type" : "com.bstek.dorado.hibernate.criteria.criterion.SingleCriterion",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "DoublePropertyCriterion",
				"parents" : "BaseCriterion",
				"label" : "DoublePropertyCriterion",
				"abstract" : false,
				"type" : "com.bstek.dorado.hibernate.criteria.criterion.DoublePropertyCriterion",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "SubQueryPropertyCriterion",
				"parents" : "SingleProperyCriterion",
				"label" : "SubQueryPropertyCriterion",
				"abstract" : false,
				"type" : "com.bstek.dorado.hibernate.criteria.criterion.SubQueryPropertyCriterion",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"rule" : "TopCriteria",
					"name" : "Criteria",
					"fixed" : true,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "FetchMode",
				"label" : "FetchMode",
				"abstract" : false,
				"type" : "com.bstek.dorado.hibernate.criteria.FetchMode",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "Order",
				"label" : "Order",
				"abstract" : false,
				"type" : "com.bstek.dorado.hibernate.criteria.order.Order",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "BaseProjection",
				"abstract" : true,
				"type" : "com.bstek.dorado.hibernate.criteria.projection.BaseProjection",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "SqlProjection",
				"parents" : "BaseProjection",
				"label" : "SqlProjection",
				"abstract" : false,
				"type" : "com.bstek.dorado.hibernate.criteria.projection.SqlProjection",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"name" : "Columns",
					"fixed" : true,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"rule" : {
						"name" : "Wrapper.Columns",
						"label" : "Columns",
						"abstract" : false,
						"nodeName" : "Columns",
						"scope" : "public",
						"sortFactor" : 0,
						"autoGenerateId" : false,
						"clientTypes" : "desktop,touch",
						"deprecated" : false,
						"children" : [ {
							"rule" : "Column",
							"name" : "Columns",
							"fixed" : false,
							"aggregated" : true,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						} ]
					}
				} ]
			},
			{
				"name" : "Column",
				"label" : "Column",
				"abstract" : false,
				"type" : "com.bstek.dorado.hibernate.criteria.projection.SqlProjection$Column",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "SumProjection",
				"parents" : "SinglePropertyProjection",
				"label" : "SumProjection",
				"abstract" : false,
				"type" : "com.bstek.dorado.hibernate.criteria.projection.SumProjection",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false
			},
			{
				"name" : "SinglePropertyProjection",
				"parents" : "BaseProjection",
				"abstract" : true,
				"type" : "com.bstek.dorado.hibernate.criteria.projection.SinglePropertyProjection",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "GroupByProjection",
				"parents" : "SinglePropertyProjection",
				"label" : "GroupByProjection",
				"abstract" : false,
				"type" : "com.bstek.dorado.hibernate.criteria.projection.GroupByProjection",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false
			},
			{
				"name" : "RowCountProjection",
				"parents" : "BaseProjection",
				"label" : "RowCountProjection",
				"abstract" : false,
				"type" : "com.bstek.dorado.hibernate.criteria.projection.RowCountProjection",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false
			},
			{
				"name" : "AvgProjection",
				"parents" : "SinglePropertyProjection",
				"label" : "AvgProjection",
				"abstract" : false,
				"type" : "com.bstek.dorado.hibernate.criteria.projection.AvgProjection",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false
			},
			{
				"name" : "CountProjection",
				"parents" : "SinglePropertyProjection",
				"label" : "CountProjection",
				"abstract" : false,
				"type" : "com.bstek.dorado.hibernate.criteria.projection.CountProjection",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "MaxProjection",
				"parents" : "SinglePropertyProjection",
				"label" : "MaxProjection",
				"abstract" : false,
				"type" : "com.bstek.dorado.hibernate.criteria.projection.MaxProjection",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false
			},
			{
				"name" : "PropertyProjection",
				"parents" : "SinglePropertyProjection",
				"label" : "PropertyProjection",
				"abstract" : false,
				"type" : "com.bstek.dorado.hibernate.criteria.projection.PropertyProjection",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false
			},
			{
				"name" : "MinProjection",
				"parents" : "SinglePropertyProjection",
				"label" : "MinProjection",
				"abstract" : false,
				"type" : "com.bstek.dorado.hibernate.criteria.projection.MinProjection",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false
			},
			{
				"name" : "SubCriteria",
				"parents" : "BaseCriteria",
				"label" : "SubCriteria",
				"abstract" : false,
				"type" : "com.bstek.dorado.hibernate.criteria.SubCriteria",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "DirectDataResolver",
				"parents" : "AbstractDataResolver",
				"label" : "DirectDataResolver",
				"abstract" : false,
				"type" : "com.bstek.dorado.data.resolver.DirectDataResolver",
				"scope" : "public",
				"sortFactor" : 5,
				"icon" : "\/com\/bstek\/dorado\/data\/resolver\/DirectDataResolver.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"primitiveProps" : [ {
					"name" : "type",
					"defaultValue" : "direct",
					"fixed" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"visible" : false
				} ]
			},
			{
				"name" : "AbstractValidator",
				"parents" : "Validator",
				"abstract" : true,
				"nodeName" : "Validator",
				"type" : "com.bstek.dorado.view.type.property.validator.AbstractValidator",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/view\/type\/property\/validator\/Validator.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "RequiredValidator",
				"parents" : "BaseValidator",
				"label" : "RequiredValidator",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.type.property.validator.RequiredValidator",
				"scope" : "public",
				"sortFactor" : 6,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"primitiveProps" : [ {
					"name" : "type",
					"defaultValue" : "required",
					"fixed" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"visible" : false
				} ],
				"props" : [ {
					"name" : "type",
					"defaultValue" : "required",
					"fixed" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"visible" : false
				} ]
			},
			{
				"name" : "BaseValidator",
				"parents" : "AbstractValidator",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.type.property.validator.BaseValidator",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "LengthValidator",
				"parents" : "BaseValidator",
				"label" : "LengthValidator",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.type.property.validator.LengthValidator",
				"scope" : "public",
				"sortFactor" : 7,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"primitiveProps" : [ {
					"name" : "type",
					"defaultValue" : "length",
					"fixed" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"visible" : false
				} ],
				"props" : [ {
					"name" : "type",
					"defaultValue" : "length",
					"fixed" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"visible" : false
				} ]
			},
			{
				"name" : "CharLengthValidator",
				"parents" : "BaseValidator",
				"label" : "CharLengthValidator",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.type.property.validator.CharLengthValidator",
				"scope" : "public",
				"sortFactor" : 8,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"primitiveProps" : [ {
					"name" : "type",
					"defaultValue" : "charLength",
					"fixed" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"visible" : false
				} ],
				"props" : [ {
					"name" : "type",
					"defaultValue" : "charLength",
					"fixed" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"visible" : false
				} ]
			},
			{
				"name" : "RangeValidator",
				"parents" : "BaseValidator",
				"label" : "RangeValidator",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.type.property.validator.RangeValidator",
				"scope" : "public",
				"sortFactor" : 9,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"primitiveProps" : [ {
					"name" : "type",
					"defaultValue" : "range",
					"fixed" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"visible" : false
				} ],
				"props" : [ {
					"name" : "type",
					"defaultValue" : "range",
					"fixed" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"visible" : false
				} ]
			},
			{
				"name" : "EnumValidator",
				"parents" : "BaseValidator",
				"label" : "EnumValidator",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.type.property.validator.EnumValidator",
				"scope" : "public",
				"sortFactor" : 10,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"primitiveProps" : [ {
					"name" : "type",
					"defaultValue" : "enum",
					"fixed" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"visible" : false
				} ],
				"props" : [ {
					"name" : "type",
					"defaultValue" : "enum",
					"fixed" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"visible" : false
				} ]
			},
			{
				"name" : "RegExpValidator",
				"parents" : "BaseValidator",
				"label" : "RegExpValidator",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.type.property.validator.RegExpValidator",
				"scope" : "public",
				"sortFactor" : 11,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"primitiveProps" : [ {
					"name" : "type",
					"defaultValue" : "regExp",
					"fixed" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"visible" : false
				} ],
				"props" : [ {
					"name" : "type",
					"defaultValue" : "regExp",
					"fixed" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"visible" : false
				} ]
			},
			{
				"name" : "AjaxValidator",
				"parents" : "AbstractAjaxValidator",
				"label" : "AjaxValidator",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.type.property.validator.AjaxValidator",
				"scope" : "public",
				"sortFactor" : 12,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"primitiveProps" : [ {
					"name" : "type",
					"defaultValue" : "ajax",
					"fixed" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"visible" : false
				} ],
				"props" : [ {
					"name" : "type",
					"defaultValue" : "ajax",
					"fixed" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"visible" : false
				} ],
				"clientEvents" : [ {
					"name" : "beforeExecute",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "AbstractAjaxValidator",
				"parents" : "AbstractValidator",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.type.property.validator.AbstractAjaxValidator",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "CustomValidator",
				"parents" : "AbstractValidator",
				"label" : "CustomValidator",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.type.property.validator.CustomValidator",
				"scope" : "public",
				"sortFactor" : 13,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"primitiveProps" : [ {
					"name" : "type",
					"defaultValue" : "custom",
					"fixed" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"visible" : false
				} ],
				"clientEvents" : [ {
					"name" : "onValidate",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "AnchorLayout",
				"parents" : "Layout",
				"label" : "AnchorLayout",
				"abstract" : false,
				"nodeName" : "anchor",
				"type" : "com.bstek.dorado.view.widget.layout.AnchorLayout",
				"scope" : "public",
				"sortFactor" : 1001,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false
			},
			{
				"name" : "Layout",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.layout.Layout",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "AnchorLayoutConstraint",
				"parents" : "LayoutConstraintSupport",
				"label" : "AnchorLayoutConstraint",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.layout.AnchorLayoutConstraint",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "LayoutConstraintSupport",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.layout.LayoutConstraintSupport",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "DockLayout",
				"parents" : "Layout",
				"label" : "DockLayout",
				"abstract" : false,
				"nodeName" : "dock",
				"type" : "com.bstek.dorado.view.widget.layout.DockLayout",
				"scope" : "public",
				"sortFactor" : 1002,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "DockLayoutConstraint",
				"parents" : "LayoutConstraintSupport",
				"label" : "DockLayoutConstraint",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.layout.DockLayoutConstraint",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "HboxLayout",
				"parents" : "AbstractBoxLayout",
				"label" : "HBoxLayout",
				"abstract" : false,
				"nodeName" : "hbox",
				"type" : "com.bstek.dorado.view.widget.layout.HBoxLayout",
				"scope" : "public",
				"sortFactor" : 1003,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "AbstractBoxLayout",
				"parents" : "Layout",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.layout.AbstractBoxLayout",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "HboxLayoutConstraint",
				"parents" : "LayoutConstraintSupport",
				"label" : "HBoxLayoutConstraintSupport",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.layout.HBoxLayoutConstraintSupport",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "VboxLayout",
				"parents" : "AbstractBoxLayout",
				"label" : "VBoxLayout",
				"abstract" : false,
				"nodeName" : "vbox",
				"type" : "com.bstek.dorado.view.widget.layout.VBoxLayout",
				"scope" : "public",
				"sortFactor" : 1004,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "VboxLayoutConstraint",
				"parents" : "LayoutConstraintSupport",
				"label" : "VBoxLayoutConstraintSupport",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.layout.VBoxLayoutConstraintSupport",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "FormLayout",
				"parents" : "Layout",
				"label" : "FormLayout",
				"abstract" : false,
				"nodeName" : "form",
				"type" : "com.bstek.dorado.view.widget.layout.FormLayout",
				"scope" : "public",
				"sortFactor" : 1005,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "FormLayoutConstraint",
				"parents" : "LayoutConstraintSupport",
				"label" : "FormLayoutConstraint",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.layout.FormLayoutConstraint",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "NativeLayout",
				"parents" : "Layout",
				"label" : "NativeLayout",
				"abstract" : false,
				"nodeName" : "native",
				"type" : "com.bstek.dorado.view.widget.layout.NativeLayout",
				"scope" : "public",
				"sortFactor" : 1006,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "NativeLayoutConstraint",
				"parents" : "HashMap",
				"label" : "CommonLayoutConstraint",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.layout.CommonLayoutConstraint",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false
			},
			{
				"name" : "AbstractMap",
				"abstract" : true,
				"type" : "java.util.AbstractMap",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false
			},
			{
				"name" : "HashMap",
				"parents" : "AbstractMap",
				"label" : "HashMap",
				"abstract" : false,
				"type" : "java.util.HashMap",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false
			},
			{
				"name" : "Css3-hboxLayout",
				"parents" : "AbstractCSS3BoxLayout",
				"label" : "CSS3HBoxLayout",
				"abstract" : false,
				"nodeName" : "css3-hbox",
				"type" : "com.bstek.dorado.touch.widget.layout.CSS3HBoxLayout",
				"scope" : "public",
				"sortFactor" : 1007,
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false
			},
			{
				"name" : "AbstractCSS3BoxLayout",
				"parents" : "Layout",
				"label" : "AbstractCSS3BoxLayout",
				"abstract" : false,
				"type" : "com.bstek.dorado.touch.widget.layout.AbstractCSS3BoxLayout",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "Css3-hboxLayoutConstraint",
				"parents" : "LayoutConstraintSupport",
				"label" : "CSS3BoxLayoutConstraint",
				"abstract" : false,
				"type" : "com.bstek.dorado.touch.widget.layout.CSS3BoxLayoutConstraint",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "Css3-vboxLayout",
				"parents" : "AbstractCSS3BoxLayout",
				"label" : "CSS3VBoxLayout",
				"abstract" : false,
				"nodeName" : "css3-vbox",
				"type" : "com.bstek.dorado.touch.widget.layout.CSS3VBoxLayout",
				"scope" : "public",
				"sortFactor" : 1008,
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false
			},
			{
				"name" : "Css3-vboxLayoutConstraint",
				"parents" : "LayoutConstraintSupport",
				"label" : "CSS3BoxLayoutConstraint",
				"abstract" : false,
				"type" : "com.bstek.dorado.touch.widget.layout.CSS3BoxLayoutConstraint",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "DataSet",
				"parents" : "Component",
				"label" : "DataSet",
				"abstract" : false,
				"nodeName" : "DataSet",
				"type" : "com.bstek.dorado.view.widget.data.DataSet",
				"scope" : "public",
				"sortFactor" : 2001,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/data\/DataSet.png",
				"autoGenerateId" : true,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"primitiveProps" : [ {
					"name" : "listener",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"props" : [ {
					"name" : "listener",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"clientEvents" : [ {
					"name" : "onLoadData",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onDataLoad",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : true
				}, {
					"name" : "beforeLoadData",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "Control",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "Control",
				"abstract" : false,
				"nodeName" : "Control",
				"type" : "com.bstek.dorado.view.widget.DefaultControl",
				"scope" : "public",
				"sortFactor" : 2002,
				"category" : "General",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false
			},
			{
				"name" : "Container",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "Container",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.Container",
				"scope" : "public",
				"sortFactor" : 2003,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/Container.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"primitiveProps" : [ {
					"name" : "listener",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"props" : [ {
					"name" : "listener",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [ {
					"rule" : "Component",
					"name" : "Children",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "HtmlContainer",
				"parents" : "Container",
				"label" : "HtmlContainer",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.HtmlContainer",
				"scope" : "public",
				"sortFactor" : 2004,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/HtmlContainer.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "SubViewHolder",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "SubViewHolder",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.SubViewHolder",
				"scope" : "public",
				"sortFactor" : 2005,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/SubViewHolder.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "Action",
				"parents" : "Component",
				"label" : "Action",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.action.Action",
				"scope" : "public",
				"sortFactor" : 2006,
				"category" : "Action",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/action\/Action.png",
				"autoGenerateId" : true,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onFailure",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onSuccess",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeExecute",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onExecute",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "AjaxAction",
				"parents" : "AsyncAction",
				"label" : "AjaxAction",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.action.AjaxAction",
				"scope" : "public",
				"sortFactor" : 2007,
				"category" : "Action",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/action\/AjaxAction.png",
				"autoGenerateId" : true,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "UpdateAction",
				"parents" : "AsyncAction",
				"label" : "UpdateAction",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.action.UpdateAction",
				"scope" : "public",
				"sortFactor" : 2008,
				"category" : "Action",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/action\/UpdateAction.png",
				"autoGenerateId" : true,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onGetUpdateData",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [ {
					"rule" : "UpdateItem",
					"name" : "UpdateItems",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "FormSubmitAction",
				"parents" : "Action",
				"label" : "FormSubmitAction",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.action.FormSubmitAction",
				"scope" : "public",
				"sortFactor" : 2009,
				"category" : "Action",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/action\/FormSubmitAction.png",
				"autoGenerateId" : true,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "LongTask",
				"parents" : "Action",
				"label" : "LongTask",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.action.LongTask",
				"scope" : "public",
				"sortFactor" : 2010,
				"category" : "Action",
				"autoGenerateId" : true,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onLog",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onTaskScheduled",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onTaskEnd",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onStateChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "Button",
				"parents" : "AbstractButton",
				"label" : "Button",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.Button",
				"scope" : "public",
				"sortFactor" : 2011,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/Button.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onTriggerClick",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "SimpleButton",
				"parents" : "AbstractButton",
				"label" : "SimpleButton",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.SimpleButton",
				"scope" : "public",
				"sortFactor" : 2012,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/SimpleButton.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "SimpleIconButton",
				"parents" : "SimpleButton",
				"label" : "SimpleIconButton",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.SimpleIconButton",
				"scope" : "public",
				"sortFactor" : 2013,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/SimpleIconButton.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "Panel",
				"parents" : "AbstractPanel",
				"label" : "Panel",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.Panel",
				"scope" : "public",
				"sortFactor" : 2014,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/Panel.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onMaximize",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeMaximize",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [ {
					"name" : "Tools",
					"fixed" : true,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"rule" : {
						"name" : "Wrapper.Tools",
						"label" : "Tools",
						"abstract" : false,
						"nodeName" : "Tools",
						"scope" : "public",
						"sortFactor" : 0,
						"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/Tools.png",
						"autoGenerateId" : false,
						"clientTypes" : "desktop,touch",
						"deprecated" : false,
						"children" : [ {
							"rule" : "SimpleIconButton",
							"name" : "Tools",
							"fixed" : false,
							"aggregated" : true,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						} ]
					}
				} ]
			},
			{
				"name" : "GroupBox",
				"parents" : "AbstractPanel",
				"label" : "GroupBox",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.GroupBox",
				"scope" : "public",
				"sortFactor" : 2015,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/GroupBox.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false
			},
			{
				"name" : "FieldSet",
				"parents" : "AbstractPanel",
				"label" : "FieldSet",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.FieldSet",
				"scope" : "public",
				"sortFactor" : 2016,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/FieldSet.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false
			},
			{
				"name" : "IFrame",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "IFrame",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.IFrame",
				"scope" : "public",
				"sortFactor" : 2017,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/IFrame.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onLoad",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "CardBook",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "CardBook",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.CardBook",
				"scope" : "public",
				"sortFactor" : 2018,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/CardBook.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onCurrentChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeCurrentChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [ {
					"rule" : "com.bstek.dorado.view.widget.Control",
					"name" : "Controls",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "TabControl",
				"parents" : "TabBar",
				"label" : "TabControl",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.tab.TabControl",
				"scope" : "public",
				"sortFactor" : 2019,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/tab\/TabControl.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"rule" : "IFrameTab",
					"name" : "IFrameTab",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"rule" : "ControlTab",
					"name" : "ControlTab",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "VerticalTabControl",
				"parents" : "TabColumn",
				"label" : "VerticalTabControl",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.tab.VerticalTabControl",
				"scope" : "public",
				"sortFactor" : 2020,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/tab\/VerticalTabControl.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"rule" : "IFrameTab",
					"name" : "IFrameTab",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"rule" : "ControlTab",
					"name" : "ControlTab",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "TabBar",
				"parents" : "AbstractTabControl",
				"label" : "TabBar",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.tab.TabBar",
				"scope" : "public",
				"sortFactor" : 2021,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/tab\/TabBar.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"rule" : "Tab",
					"name" : "Tab",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"public" : false
				} ]
			},
			{
				"name" : "TabColumn",
				"parents" : "AbstractTabControl",
				"label" : "TabColumn",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.tab.TabColumn",
				"scope" : "public",
				"sortFactor" : 2022,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/tab\/TabColumn.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"rule" : "Tab",
					"name" : "Tab",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"public" : false
				} ]
			},
			{
				"name" : "ToolBar",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "ToolBar",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.toolbar.ToolBar",
				"scope" : "public",
				"sortFactor" : 2023,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/toolbar\/ToolBar.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"children" : [
						{
							"rule" : "MenuButton",
							"name" : "MenuButton",
							"fixed" : false,
							"aggregated" : true,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						},
						{
							"rule" : "com.bstek.dorado.view.widget.base.toolbar.Label",
							"name" : "ToolBarLabel",
							"fixed" : false,
							"aggregated" : true,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						},
						{
							"rule" : "com.bstek.dorado.view.widget.base.toolbar.Separator",
							"name" : "Separator",
							"fixed" : false,
							"aggregated" : true,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						},
						{
							"rule" : "Fill",
							"name" : "Fill",
							"fixed" : false,
							"aggregated" : true,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						},
						{
							"rule" : "com.bstek.dorado.view.widget.base.toolbar.Button",
							"name" : "ToolBarButton",
							"fixed" : false,
							"aggregated" : true,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						}, {
							"rule" : "com.bstek.dorado.view.widget.Control",
							"name" : "Items",
							"fixed" : false,
							"aggregated" : true,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						} ]
			},
			{
				"name" : "SplitPanel",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "SplitPanel",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.SplitPanel",
				"scope" : "public",
				"sortFactor" : 2024,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/SplitPanel.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onCollapsedChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeCollapsedChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [
						{
							"name" : "MainControl",
							"fixed" : true,
							"aggregated" : false,
							"clientTypes" : "desktop,touch",
							"deprecated" : false,
							"rule" : {
								"name" : "Wrapper.MainControl",
								"label" : "MainControl",
								"abstract" : false,
								"nodeName" : "MainControl",
								"scope" : "public",
								"sortFactor" : 0,
								"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/MainControl.png",
								"autoGenerateId" : false,
								"clientTypes" : "desktop,touch",
								"deprecated" : false,
								"children" : [ {
									"rule" : "com.bstek.dorado.view.widget.Control",
									"name" : "MainControl",
									"fixed" : false,
									"aggregated" : false,
									"clientTypes" : "desktop,touch",
									"deprecated" : false
								} ]
							}
						},
						{
							"name" : "SideControl",
							"fixed" : true,
							"aggregated" : false,
							"clientTypes" : "desktop,touch",
							"deprecated" : false,
							"rule" : {
								"name" : "Wrapper.SideControl",
								"label" : "SideControl",
								"abstract" : false,
								"nodeName" : "SideControl",
								"scope" : "public",
								"sortFactor" : 0,
								"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/SideControl.png",
								"autoGenerateId" : false,
								"clientTypes" : "desktop,touch",
								"deprecated" : false,
								"children" : [ {
									"rule" : "com.bstek.dorado.view.widget.Control",
									"name" : "SideControl",
									"fixed" : false,
									"aggregated" : false,
									"clientTypes" : "desktop,touch",
									"deprecated" : false
								} ]
							}
						} ]
			},
			{
				"name" : "Accordion",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "Accordion",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.accordion.Accordion",
				"scope" : "public",
				"sortFactor" : 2025,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/accordion\/Accordion.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "beforeCurrentSectionChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onCurrentSectionChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [ {
					"rule" : "Section",
					"name" : "Sections",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "Slider",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "Slider",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.Slider",
				"scope" : "public",
				"sortFactor" : 2026,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/Slider.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "beforeValueChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onValueChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "ProgressBar",
				"parents" : "AbstractPropertyDataControl",
				"label" : "ProgressBar",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.ProgressBar",
				"scope" : "public",
				"sortFactor" : 2027,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/ProgressBar.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "Tip",
				"parents" : "com.bstek.dorado.view.widget.Control,FloatControl",
				"label" : "Tip",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.Tip",
				"scope" : "public",
				"sortFactor" : 2028,
				"category" : "Floatable",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/Tip.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "beforeShow",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeHide",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeClose",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onShow",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onClose",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onHide",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "FloatContainer",
				"parents" : "Container,FloatControl",
				"label" : "FloatContainer",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.FloatContainer",
				"scope" : "public",
				"sortFactor" : 2029,
				"category" : "Floatable",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/FloatContainer.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "beforeShow",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeHide",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeClose",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onShow",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onClose",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onHide",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "FloatPanel",
				"parents" : "Panel,FloatControl",
				"label" : "FloatPanel",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.FloatPanel",
				"scope" : "public",
				"sortFactor" : 2030,
				"category" : "Floatable",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/FloatPanel.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "beforeShow",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeHide",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeClose",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onShow",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onClose",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onHide",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "Dialog",
				"parents" : "FloatPanel",
				"label" : "Dialog",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.Dialog",
				"scope" : "public",
				"sortFactor" : 2031,
				"category" : "Floatable",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/Dialog.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onMinimize",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeMinimize",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "Menu",
				"parents" : "com.bstek.dorado.view.widget.Control,FloatControl",
				"label" : "Menu",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.menu.Menu",
				"scope" : "public",
				"sortFactor" : 2032,
				"category" : "Floatable",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/menu\/Menu.png",
				"autoGenerateId" : true,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "beforeShow",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeHide",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeClose",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onShow",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onClose",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onHideTopMenu",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onHide",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [ {
					"rule" : "BaseMenuItem",
					"name" : "Items",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "DatePicker",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "DatePicker",
				"abstract" : false,
				"nodeName" : "DatePicker",
				"type" : "com.bstek.dorado.view.widget.base.DatePicker",
				"scope" : "public",
				"sortFactor" : 2033,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/DatePicker.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onClear",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onFilterDate",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onRefreshDateCell",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onPick",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onConfirm",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onCancel",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "YearMonthPicker",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "YearMonthPicker",
				"abstract" : false,
				"nodeName" : "YearMonthPicker",
				"type" : "com.bstek.dorado.view.widget.base.YearMonthPicker",
				"scope" : "public",
				"sortFactor" : 2034,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/YearMonthPicker.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onPick",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onCancel",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "Label",
				"parents" : "AbstractPropertyDataControl",
				"label" : "Label",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.form.Label",
				"scope" : "public",
				"sortFactor" : 2035,
				"category" : "Form",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/form\/Label.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "DataLabel",
				"parents" : "AbstractPropertyDataControl",
				"label" : "DataLabel",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.form.DataLabel",
				"scope" : "public",
				"sortFactor" : 2036,
				"category" : "Form",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/form\/DataLabel.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : true
			},
			{
				"name" : "Link",
				"parents" : "Label",
				"label" : "Link",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.form.Link",
				"scope" : "public",
				"sortFactor" : 2037,
				"category" : "Form",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/form\/Link.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "Image",
				"parents" : "AbstractPropertyDataControl",
				"label" : "Image",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.form.Image",
				"scope" : "public",
				"sortFactor" : 2038,
				"category" : "Form",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/form\/Image.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "TemplateField",
				"parents" : "AbstractDataControl",
				"label" : "TemplateField",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.form.TemplateField",
				"scope" : "public",
				"sortFactor" : 2039,
				"category" : "Form",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/form\/TemplateField.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "TextEditor",
				"parents" : "AbstractTextEditor",
				"label" : "TextEditor",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.form.TextEditor",
				"scope" : "public",
				"sortFactor" : 2040,
				"category" : "Form",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/form\/TextEditor.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "PasswordEditor",
				"parents" : "AbstractTextEditor",
				"label" : "PasswordEditor",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.form.PasswordEditor",
				"scope" : "public",
				"sortFactor" : 2041,
				"category" : "Form",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/form\/PasswordEditor.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false
			},
			{
				"name" : "TextArea",
				"parents" : "AbstractTextEditor",
				"label" : "TextArea",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.form.TextArea",
				"scope" : "public",
				"sortFactor" : 2042,
				"category" : "Form",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/form\/TextArea.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "CheckBox",
				"parents" : "AbstractDataEditor",
				"label" : "CheckBox",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.form.CheckBox",
				"scope" : "public",
				"sortFactor" : 2043,
				"category" : "Form",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/form\/CheckBox.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onValueChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "RadioGroup",
				"parents" : "AbstractDataEditor",
				"label" : "RadioGroup",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.form.RadioGroup",
				"scope" : "public",
				"sortFactor" : 2044,
				"category" : "Form",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/form\/RadioGroup.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onValueChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [ {
					"rule" : "RadioButton",
					"name" : "RadioButtons",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "DataMessage",
				"parents" : "AbstractPropertyDataControl",
				"label" : "DataMessage",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.form.DataMessage",
				"scope" : "public",
				"sortFactor" : 2045,
				"category" : "Form",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/form\/DataMessage.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "FormProfile",
				"parents" : "Component",
				"label" : "FormProfile",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.form.FormProfile",
				"scope" : "public",
				"sortFactor" : 2046,
				"category" : "Form",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/form\/FormProfile.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "FormElement",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "FormElement",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.form.FormElement",
				"scope" : "public",
				"sortFactor" : 2047,
				"category" : "Form",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/form\/FormElement.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onGetBindingDataType",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetBindingData",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [ {
					"name" : "Editor",
					"fixed" : true,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"rule" : {
						"name" : "Wrapper.Editor",
						"label" : "Editor",
						"abstract" : false,
						"nodeName" : "Editor",
						"scope" : "public",
						"sortFactor" : 0,
						"icon" : "\/com\/bstek\/dorado\/view\/widget\/form\/Editor.png",
						"autoGenerateId" : false,
						"clientTypes" : "desktop,touch",
						"deprecated" : false,
						"children" : [ {
							"rule" : "com.bstek.dorado.view.widget.Control",
							"name" : "Editor",
							"fixed" : false,
							"aggregated" : false,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						} ]
					}
				} ]
			},
			{
				"name" : "NumberSpinner",
				"parents" : "Spinner",
				"label" : "NumberSpinner",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.form.NumberSpinner",
				"scope" : "public",
				"sortFactor" : 2049,
				"category" : "Form",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/form\/NumberSpinner.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "DateTimeSpinner",
				"parents" : "Spinner",
				"label" : "DateTimeSpinner",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.form.DateTimeSpinner",
				"scope" : "public",
				"sortFactor" : 2050,
				"category" : "Form",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/form\/DateTimeSpinner.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "CustomSpinner",
				"parents" : "Spinner",
				"label" : "CustomSpinner",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.form.CustomSpinner",
				"scope" : "public",
				"sortFactor" : 2051,
				"category" : "Form",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/form\/CustomSpinner.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "Trigger",
				"parents" : "Component",
				"label" : "Trigger",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.form.trigger.Trigger",
				"scope" : "public",
				"sortFactor" : 2052,
				"category" : "Trigger",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/form\/trigger\/Trigger.png",
				"autoGenerateId" : true,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "beforeExecute",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onExecute",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "ListDropDown",
				"parents" : "RowListDropDown",
				"label" : "ListDropDown",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.form.trigger.ListDropDown",
				"scope" : "public",
				"sortFactor" : 2053,
				"category" : "Trigger",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/form\/trigger\/ListDropDown.png",
				"autoGenerateId" : true,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "DataSetDropDown",
				"parents" : "RowListDropDown",
				"label" : "DataSetDropDown",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.form.trigger.DataSetDropDown",
				"scope" : "public",
				"sortFactor" : 2054,
				"category" : "Trigger",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/form\/trigger\/DataSetDropDown.png",
				"autoGenerateId" : true,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onGetBindingDataType",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onSetFilterParameter",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetBindingData",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "AutoMappingDropDown",
				"parents" : "RowListDropDown",
				"label" : "AutoMappingDropDown",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.form.trigger.AutoMappingDropDown",
				"scope" : "public",
				"sortFactor" : 2055,
				"category" : "Trigger",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/form\/trigger\/AutoMappingDropDown.png",
				"autoGenerateId" : true,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "DateDropDown",
				"parents" : "DropDown",
				"label" : "DateDropDown",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.form.trigger.DateDropDown",
				"scope" : "public",
				"sortFactor" : 2056,
				"category" : "Trigger",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/form\/trigger\/DateDropDown.png",
				"autoGenerateId" : true,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onFilterDate",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onRefreshDateCell",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "YearMonthDropDown",
				"parents" : "DropDown",
				"label" : "YearMonthDropDown",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.form.trigger.YearMonthDropDown",
				"scope" : "public",
				"sortFactor" : 2057,
				"category" : "Trigger",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/form\/trigger\/YearMonthDropDown.png",
				"autoGenerateId" : true,
				"clientTypes" : "desktop",
				"deprecated" : false
			},
			{
				"name" : "YearDropDown",
				"parents" : "DropDown",
				"label" : "YearDropDown",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.form.trigger.YearDropDown",
				"scope" : "public",
				"sortFactor" : 2058,
				"category" : "Trigger",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/form\/trigger\/YearDropDown.png",
				"autoGenerateId" : true,
				"clientTypes" : "desktop",
				"deprecated" : false
			},
			{
				"name" : "MonthDropDown",
				"parents" : "DropDown",
				"label" : "MonthDropDown",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.form.trigger.MonthDropDown",
				"scope" : "public",
				"sortFactor" : 2059,
				"category" : "Trigger",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/form\/trigger\/MonthDropDown.png",
				"autoGenerateId" : true,
				"clientTypes" : "desktop",
				"deprecated" : false
			},
			{
				"name" : "CustomDropDown",
				"parents" : "DropDown",
				"label" : "CustomDropDown",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.form.trigger.CustomDropDown",
				"scope" : "public",
				"sortFactor" : 2060,
				"category" : "Trigger",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/form\/trigger\/CustomDropDown.png",
				"autoGenerateId" : true,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"children" : [ {
					"rule" : "com.bstek.dorado.view.widget.Control",
					"name" : "Control",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "DataPilot",
				"parents" : "AbstractDataControl",
				"label" : "DataPilot",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.datacontrol.DataPilot",
				"scope" : "public",
				"sortFactor" : 2061,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/datacontrol\/DataPilot.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onSubControlAction",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onSubControlRefresh",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "ListBox",
				"parents" : "AbstractListBox",
				"label" : "ListBox",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.list.ListBox",
				"scope" : "public",
				"sortFactor" : 2062,
				"category" : "Collection",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/list\/ListBox.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "DataListBox",
				"parents" : "AbstractListBox",
				"label" : "DataListBox",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.list.DataListBox",
				"scope" : "public",
				"sortFactor" : 2063,
				"category" : "Collection",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/list\/DataListBox.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onGetBindingDataType",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetBindingData",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "Grid",
				"parents" : "AbstractGrid",
				"label" : "Grid",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.grid.Grid",
				"scope" : "public",
				"sortFactor" : 2064,
				"category" : "Collection",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/grid\/Grid.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "DataGrid",
				"parents" : "AbstractGrid",
				"label" : "DataGrid",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.grid.DataGrid",
				"scope" : "public",
				"sortFactor" : 2065,
				"category" : "Collection",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/grid\/DataGrid.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onGetBindingDataType",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetBindingData",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "Tree",
				"parents" : "AbstractTree",
				"label" : "Tree",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.tree.Tree",
				"scope" : "public",
				"sortFactor" : 2066,
				"category" : "Collection",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/tree\/Tree.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"children" : [ {
					"rule" : "BaseNode",
					"name" : "Nodes",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "DataTree",
				"parents" : "AbstractTree",
				"label" : "DataTree",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.tree.DataTree",
				"scope" : "public",
				"sortFactor" : 2067,
				"category" : "Collection",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/tree\/DataTree.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "beforeDataNodeCreate",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetBindingDataType",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onDataNodeCreate",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetBindingData",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [ {
					"name" : "BindingConfigs",
					"fixed" : true,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"rule" : {
						"name" : "Wrapper.BindingConfigs",
						"label" : "BindingConfigs",
						"abstract" : false,
						"nodeName" : "BindingConfigs",
						"scope" : "public",
						"sortFactor" : 0,
						"icon" : "\/com\/bstek\/dorado\/view\/widget\/tree\/BindingConfigs.png",
						"autoGenerateId" : false,
						"clientTypes" : "desktop,touch",
						"deprecated" : false,
						"children" : [ {
							"rule" : "BindingConfig",
							"name" : "BindingConfigs",
							"fixed" : false,
							"aggregated" : true,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						} ]
					}
				} ]
			},
			{
				"name" : "BlockView",
				"parents" : "AbstractBlockView",
				"label" : "BlockView",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.blockview.BlockView",
				"scope" : "public",
				"sortFactor" : 2068,
				"category" : "Collection",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/blockview\/BlockView.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "DataBlockView",
				"parents" : "AbstractBlockView",
				"label" : "DataBlockView",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.blockview.DataBlockView",
				"scope" : "public",
				"sortFactor" : 2069,
				"category" : "Collection",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/blockview\/DataBlockView.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onGetBindingDataType",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetBindingData",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "TreeGrid",
				"parents" : "AbstractTreeGrid",
				"label" : "TreeGrid",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.treegrid.TreeGrid",
				"scope" : "public",
				"sortFactor" : 2070,
				"category" : "Collection",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/treegrid\/TreeGrid.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"children" : [ {
					"name" : "Nodes",
					"fixed" : true,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"rule" : {
						"name" : "Wrapper.Nodes",
						"label" : "Nodes",
						"abstract" : false,
						"nodeName" : "Nodes",
						"scope" : "public",
						"sortFactor" : 0,
						"icon" : "\/com\/bstek\/dorado\/view\/widget\/treegrid\/Nodes.png",
						"autoGenerateId" : false,
						"clientTypes" : "desktop,touch",
						"deprecated" : false,
						"children" : [ {
							"rule" : "BaseNode",
							"name" : "Nodes",
							"fixed" : false,
							"aggregated" : true,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						} ]
					}
				} ]
			},
			{
				"name" : "DataTreeGrid",
				"parents" : "AbstractTreeGrid",
				"label" : "DataTreeGrid",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.treegrid.DataTreeGrid",
				"scope" : "public",
				"sortFactor" : 2071,
				"category" : "Collection",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/treegrid\/DataTreeGrid.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "beforeDataNodeCreate",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetBindingDataType",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onDataNodeCreate",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetBindingData",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [ {
					"name" : "BindingConfigs",
					"fixed" : true,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"rule" : {
						"name" : "Wrapper.BindingConfigs",
						"label" : "BindingConfigs",
						"abstract" : false,
						"nodeName" : "BindingConfigs",
						"scope" : "public",
						"sortFactor" : 0,
						"icon" : "\/com\/bstek\/dorado\/view\/widget\/tree\/BindingConfigs.png",
						"autoGenerateId" : false,
						"clientTypes" : "desktop,touch",
						"deprecated" : false,
						"children" : [ {
							"rule" : "BindingConfig",
							"name" : "BindingConfigs",
							"fixed" : false,
							"aggregated" : true,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						} ]
					}
				} ]
			},
			{
				"name" : "TagEditor",
				"parents" : "AbstractTextEditor",
				"label" : "TagEditor",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.advance.TagEditor",
				"scope" : "public",
				"sortFactor" : 2072,
				"category" : "Form",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/advance\/TagEditor.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onTagRemove",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onUnknownTagAccept",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeTagAdd",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeUnknownTagAccept",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeTagRemove",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onTagAdd",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "ColorPicker",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "ColorPicker",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.advance.ColorPicker",
				"scope" : "public",
				"sortFactor" : 2073,
				"category" : "Form",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/advance\/ColorPicker.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "ColorEditor",
				"parents" : "AbstractDataEditor",
				"label" : "ColorEditor",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.advance.ColorEditor",
				"scope" : "public",
				"sortFactor" : 2074,
				"category" : "Form",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/advance\/ColorEditor.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "TouchCarousel",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "Carousel",
				"abstract" : false,
				"nodeName" : "TouchCarousel",
				"type" : "com.bstek.dorado.touch.widget.Carousel",
				"scope" : "public",
				"sortFactor" : 2075,
				"category" : "Touch",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/Carousel.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onCurrentChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetBindingDataType",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeCurrentChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetBindingData",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [ {
					"rule" : "com.bstek.dorado.view.widget.Control",
					"name" : "Items",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "TouchButton",
				"parents" : "AbstractButton",
				"label" : "Button",
				"abstract" : false,
				"nodeName" : "TouchButton",
				"type" : "com.bstek.dorado.touch.widget.Button",
				"scope" : "public",
				"sortFactor" : 2076,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/Button.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onClick",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "TouchButtonGroup",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "ButtonGroup",
				"abstract" : false,
				"nodeName" : "TouchButtonGroup",
				"type" : "com.bstek.dorado.touch.widget.ButtonGroup",
				"scope" : "public",
				"sortFactor" : 2077,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/ButtonGroup.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onGetBindingDataType",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetBindingData",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [ {
					"rule" : "TouchButton",
					"name" : "Items",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "TouchToolBar",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "ToolBar",
				"abstract" : false,
				"nodeName" : "TouchToolBar",
				"type" : "com.bstek.dorado.touch.widget.toolbar.ToolBar",
				"scope" : "public",
				"sortFactor" : 2078,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/toolbar\/ToolBar.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"rule" : "Spacer",
					"name" : "TouchToolBarSpacer",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"rule" : "com.bstek.dorado.touch.widget.toolbar.Separator",
					"name" : "TouchToolBarSeparator",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"rule" : "com.bstek.dorado.view.widget.Control",
					"name" : "Items",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "TouchTabControl",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "TabControl",
				"abstract" : false,
				"nodeName" : "TouchTabControl",
				"type" : "com.bstek.dorado.touch.widget.tab.TabControl",
				"scope" : "public",
				"sortFactor" : 2079,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/tab\/TabControl.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onCurrentChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeCurrentChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [ {
					"rule" : "TabButton",
					"name" : "TabButton",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"public" : false
				} ]
			},
			{
				"name" : "TouchStack",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "Stack",
				"abstract" : false,
				"nodeName" : "TouchStack",
				"type" : "com.bstek.dorado.touch.widget.Stack",
				"scope" : "public",
				"sortFactor" : 2080,
				"category" : "Touch",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/Stack.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"rule" : "com.bstek.dorado.view.widget.Control",
					"name" : "Items",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "TouchVBox",
				"parents" : "ItemGroup",
				"label" : "VBox",
				"abstract" : false,
				"nodeName" : "TouchVBox",
				"type" : "com.bstek.dorado.touch.widget.VBox",
				"scope" : "public",
				"sortFactor" : 2081,
				"category" : "Touch",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "TouchBubble",
				"parents" : "FloatContainer",
				"label" : "Bubble",
				"abstract" : false,
				"nodeName" : "TouchBubble",
				"type" : "com.bstek.dorado.touch.widget.Bubble",
				"scope" : "public",
				"sortFactor" : 2082,
				"category" : "Floatable",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "TouchSplitView",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "SplitView",
				"abstract" : false,
				"nodeName" : "TouchSplitView",
				"type" : "com.bstek.dorado.touch.widget.SplitView",
				"scope" : "public",
				"sortFactor" : 2083,
				"category" : "Touch",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/SplitView.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"name" : "MainControl",
					"fixed" : true,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"rule" : {
						"name" : "Wrapper.MainControl",
						"label" : "MainControl",
						"abstract" : false,
						"nodeName" : "MainControl",
						"scope" : "public",
						"sortFactor" : 0,
						"autoGenerateId" : false,
						"clientTypes" : "desktop,touch",
						"deprecated" : false,
						"children" : [ {
							"rule" : "com.bstek.dorado.view.widget.Control",
							"name" : "MainControl",
							"fixed" : false,
							"aggregated" : false,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						} ]
					}
				}, {
					"name" : "SideControl",
					"fixed" : true,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"rule" : {
						"name" : "Wrapper.SideControl",
						"label" : "SideControl",
						"abstract" : false,
						"nodeName" : "SideControl",
						"scope" : "public",
						"sortFactor" : 0,
						"autoGenerateId" : false,
						"clientTypes" : "desktop,touch",
						"deprecated" : false,
						"children" : [ {
							"rule" : "com.bstek.dorado.view.widget.Control",
							"name" : "SideControl",
							"fixed" : false,
							"aggregated" : false,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						} ]
					}
				} ]
			},
			{
				"name" : "TouchDrawerView",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "DrawerView",
				"abstract" : false,
				"nodeName" : "TouchDrawerView",
				"type" : "com.bstek.dorado.touch.widget.DrawerView",
				"scope" : "public",
				"sortFactor" : 2084,
				"category" : "Touch",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/DrawerView.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : [],
				"children" : [
						{
							"name" : "LeftDrawer",
							"fixed" : false,
							"aggregated" : false,
							"clientTypes" : "desktop,touch",
							"deprecated" : false,
							"rule" : {
								"name" : "Wrapper.LeftDrawer",
								"label" : "LeftDrawer",
								"abstract" : false,
								"nodeName" : "LeftDrawer",
								"scope" : "public",
								"sortFactor" : 0,
								"icon" : "\/com\/bstek\/dorado\/touch\/widget\/LeftDrawer.png",
								"autoGenerateId" : false,
								"clientTypes" : "desktop,touch",
								"deprecated" : false,
								"children" : [ {
									"rule" : "com.bstek.dorado.view.widget.Control",
									"name" : "LeftDrawer",
									"fixed" : false,
									"aggregated" : false,
									"clientTypes" : "desktop,touch",
									"deprecated" : false
								} ]
							}
						},
						{
							"name" : "MainControl",
							"fixed" : true,
							"aggregated" : false,
							"clientTypes" : "desktop,touch",
							"deprecated" : false,
							"rule" : {
								"name" : "Wrapper.MainControl",
								"label" : "MainControl",
								"abstract" : false,
								"nodeName" : "MainControl",
								"scope" : "public",
								"sortFactor" : 0,
								"icon" : "\/com\/bstek\/dorado\/touch\/widget\/MainControl.png",
								"autoGenerateId" : false,
								"clientTypes" : "desktop,touch",
								"deprecated" : false,
								"children" : [ {
									"rule" : "com.bstek.dorado.view.widget.Control",
									"name" : "MainControl",
									"fixed" : false,
									"aggregated" : false,
									"clientTypes" : "desktop,touch",
									"deprecated" : false
								} ]
							}
						},
						{
							"name" : "RightDrawer",
							"fixed" : false,
							"aggregated" : false,
							"clientTypes" : "desktop,touch",
							"deprecated" : false,
							"rule" : {
								"name" : "Wrapper.RightDrawer",
								"label" : "RightDrawer",
								"abstract" : false,
								"nodeName" : "RightDrawer",
								"scope" : "public",
								"sortFactor" : 0,
								"icon" : "\/com\/bstek\/dorado\/touch\/widget\/RightDrawer.png",
								"autoGenerateId" : false,
								"clientTypes" : "desktop,touch",
								"deprecated" : false,
								"children" : [ {
									"rule" : "com.bstek.dorado.view.widget.Control",
									"name" : "RightDrawer",
									"fixed" : false,
									"aggregated" : false,
									"clientTypes" : "desktop,touch",
									"deprecated" : false
								} ]
							}
						} ]
			},
			{
				"name" : "TouchLayer",
				"parents" : "FloatContainer",
				"label" : "Layer",
				"abstract" : false,
				"nodeName" : "TouchLayer",
				"type" : "com.bstek.dorado.touch.widget.Layer",
				"scope" : "public",
				"sortFactor" : 2085,
				"category" : "Floatable",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/Layer.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "TouchToggle",
				"parents" : "AbstractDataEditor",
				"label" : "Toggle",
				"abstract" : false,
				"nodeName" : "TouchToggle",
				"type" : "com.bstek.dorado.touch.widget.Toggle",
				"scope" : "public",
				"sortFactor" : 2086,
				"category" : "Form",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/Toggle.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onValueChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "TouchDataPilot",
				"parents" : "AbstractDataControl",
				"label" : "DataPilot",
				"abstract" : false,
				"nodeName" : "TouchDataPilot",
				"type" : "com.bstek.dorado.touch.widget.DataPilot",
				"scope" : "public",
				"sortFactor" : 2087,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/DataPilot.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "TouchPanel",
				"parents" : "Container",
				"label" : "Panel",
				"abstract" : false,
				"nodeName" : "TouchPanel",
				"type" : "com.bstek.dorado.touch.widget.Panel",
				"scope" : "public",
				"sortFactor" : 2088,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/Panel.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"name" : "Buttons",
					"fixed" : true,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"rule" : {
						"name" : "Wrapper.Buttons",
						"label" : "Buttons",
						"abstract" : false,
						"nodeName" : "Buttons",
						"scope" : "public",
						"sortFactor" : 0,
						"autoGenerateId" : false,
						"clientTypes" : "desktop,touch",
						"deprecated" : false,
						"children" : [ {
							"rule" : "TouchButton",
							"name" : "Buttons",
							"fixed" : false,
							"aggregated" : true,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						} ]
					}
				}, {
					"name" : "Children",
					"fixed" : true,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"rule" : {
						"name" : "Wrapper.Children",
						"label" : "Children",
						"abstract" : false,
						"nodeName" : "Children",
						"scope" : "public",
						"sortFactor" : 0,
						"autoGenerateId" : false,
						"clientTypes" : "desktop,touch",
						"deprecated" : false,
						"children" : [ {
							"rule" : "Component",
							"name" : "Children",
							"fixed" : false,
							"aggregated" : true,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						} ]
					}
				}, {
					"name" : "Tools",
					"fixed" : true,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"rule" : {
						"name" : "Wrapper.Tools",
						"label" : "Tools",
						"abstract" : false,
						"nodeName" : "Tools",
						"scope" : "public",
						"sortFactor" : 0,
						"autoGenerateId" : false,
						"clientTypes" : "desktop,touch",
						"deprecated" : false,
						"children" : [ {
							"rule" : "Spacer",
							"name" : "TouchToolBarSpacer",
							"fixed" : false,
							"aggregated" : true,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						}, {
							"rule" : "com.bstek.dorado.view.widget.Control",
							"name" : "Tools",
							"fixed" : false,
							"aggregated" : true,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						} ]
					}
				} ]
			},
			{
				"name" : "TouchFloatPanel",
				"parents" : "TouchPanel,FloatControl",
				"label" : "FloatPanel",
				"abstract" : false,
				"nodeName" : "TouchFloatPanel",
				"type" : "com.bstek.dorado.touch.widget.FloatPanel",
				"scope" : "public",
				"sortFactor" : 2089,
				"category" : "Floatable",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/FloatPanel.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "beforeShow",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeHide",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeClose",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onShow",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onClose",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onHide",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "TouchDialog",
				"parents" : "TouchFloatPanel",
				"label" : "Dialog",
				"abstract" : false,
				"nodeName" : "TouchDialog",
				"type" : "com.bstek.dorado.touch.widget.Dialog",
				"scope" : "public",
				"sortFactor" : 2090,
				"category" : "Floatable",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/Dialog.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "TouchSlider",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "Slider",
				"abstract" : false,
				"nodeName" : "TouchSlider",
				"type" : "com.bstek.dorado.touch.widget.Slider",
				"scope" : "public",
				"sortFactor" : 2091,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/Slider.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "beforeValueChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onValueChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "TouchCalendar",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "Calendar",
				"abstract" : false,
				"nodeName" : "TouchCalendar",
				"type" : "com.bstek.dorado.touch.widget.Calendar",
				"scope" : "public",
				"sortFactor" : 2092,
				"category" : "Touch",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/Calendar.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"clientEvents" : [ {
					"name" : "onDateChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onCellRefresh",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "TouchDateTimePicker",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "DateTimePicker",
				"abstract" : false,
				"nodeName" : "TouchDateTimePicker",
				"type" : "com.bstek.dorado.touch.widget.DateTimePicker",
				"scope" : "public",
				"sortFactor" : 2093,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/DateTimePicker.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "TouchListBox",
				"parents" : "AbstractList",
				"label" : "ListBox",
				"abstract" : false,
				"nodeName" : "TouchListBox",
				"type" : "com.bstek.dorado.touch.widget.list.ListBox",
				"scope" : "public",
				"sortFactor" : 2095,
				"category" : "Collection",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/list\/ListBox.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onItemTapHold",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onItemTap",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetGroupString",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onItemSwipe",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [
						{
							"name" : "LeftActionGroup",
							"fixed" : false,
							"aggregated" : false,
							"clientTypes" : "desktop,touch",
							"deprecated" : false,
							"rule" : {
								"name" : "@LeftActionGroup",
								"parents" : "TouchButtonGroup",
								"label" : "LeftActionGroup",
								"abstract" : false,
								"nodeName" : "LeftActionGroup",
								"scope" : "private",
								"sortFactor" : 0,
								"icon" : "\/com\/bstek\/dorado\/touch\/widget\/list\/LeftActionGroup.png",
								"autoGenerateId" : false,
								"clientTypes" : "desktop,touch",
								"deprecated" : false
							}
						},
						{
							"name" : "RightActionGroup",
							"fixed" : false,
							"aggregated" : false,
							"clientTypes" : "desktop,touch",
							"deprecated" : false,
							"rule" : {
								"name" : "@RightActionGroup",
								"parents" : "TouchButtonGroup",
								"label" : "RightActionGroup",
								"abstract" : false,
								"nodeName" : "RightActionGroup",
								"scope" : "private",
								"sortFactor" : 0,
								"icon" : "\/com\/bstek\/dorado\/touch\/widget\/list\/RightActionGroup.png",
								"autoGenerateId" : false,
								"clientTypes" : "desktop,touch",
								"deprecated" : false
							}
						} ]
			},
			{
				"name" : "TouchDataListBox",
				"parents" : "TouchListBox",
				"label" : "DataListBox",
				"abstract" : false,
				"nodeName" : "TouchDataListBox",
				"type" : "com.bstek.dorado.touch.widget.list.DataListBox",
				"scope" : "public",
				"sortFactor" : 2096,
				"category" : "Collection",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/list\/DataListBox.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onGetBindingDataType",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetBindingData",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "TouchDataView",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "DataView",
				"abstract" : false,
				"nodeName" : "TouchDataView",
				"type" : "com.bstek.dorado.touch.widget.list.DataView",
				"scope" : "public",
				"sortFactor" : 2097,
				"category" : "Collection",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/list\/DataView.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onSelectionChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onCurrentChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeSelectionChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetBindingDataType",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onItemTapHold",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onItemTap",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onPullDownRelease",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetBindingData",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onItemSwipe",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "TouchNestedList",
				"parents" : "AbstractNestedList",
				"label" : "NestedList",
				"abstract" : false,
				"nodeName" : "TouchNestedList",
				"type" : "com.bstek.dorado.touch.widget.list.NestedList",
				"scope" : "public",
				"sortFactor" : 2098,
				"category" : "Collection",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "TouchDataNestedList",
				"parents" : "AbstractNestedList",
				"label" : "DataNestedList",
				"abstract" : false,
				"nodeName" : "TouchDataNestedList",
				"type" : "com.bstek.dorado.touch.widget.list.DataNestedList",
				"scope" : "public",
				"sortFactor" : 2099,
				"category" : "Collection",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onGetBindingDataType",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetBindingData",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [ {
					"name" : "BindingConfigs",
					"fixed" : true,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"rule" : {
						"name" : "Wrapper.BindingConfigs",
						"label" : "BindingConfigs",
						"abstract" : false,
						"nodeName" : "BindingConfigs",
						"scope" : "public",
						"sortFactor" : 0,
						"icon" : "\/com\/bstek\/dorado\/view\/widget\/tree\/BindingConfigs.png",
						"autoGenerateId" : false,
						"clientTypes" : "desktop,touch",
						"deprecated" : false,
						"children" : [ {
							"rule" : "com.bstek.dorado.touch.widget.list.BindingConfig",
							"name" : "BindingConfigs",
							"fixed" : false,
							"aggregated" : true,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						} ]
					}
				} ]
			},
			{
				"name" : "TouchNumberSpinner",
				"parents" : "AbstractDataEditor",
				"label" : "NumberSpinner",
				"abstract" : false,
				"nodeName" : "TouchNumberSpinner",
				"type" : "com.bstek.dorado.touch.widget.form.NumberSpinner",
				"scope" : "public",
				"sortFactor" : 2100,
				"category" : "Form",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/form\/NumberSpinner.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "TouchListDropDown",
				"parents" : "com.bstek.dorado.touch.widget.form.RowListDropDown",
				"label" : "ListDropDown",
				"abstract" : false,
				"nodeName" : "TouchListDropDown",
				"type" : "com.bstek.dorado.touch.widget.form.ListDropDown",
				"scope" : "public",
				"sortFactor" : 2101,
				"category" : "Trigger",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/form\/ListDropDown.png",
				"autoGenerateId" : true,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "TouchAutoMappingDropDown",
				"parents" : "com.bstek.dorado.touch.widget.form.RowListDropDown",
				"label" : "AutoMappingDropDown",
				"abstract" : false,
				"nodeName" : "TouchAutoMappingDropDown",
				"type" : "com.bstek.dorado.touch.widget.form.AutoMappingDropDown",
				"scope" : "public",
				"sortFactor" : 2102,
				"category" : "Trigger",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/form\/AutoMappingDropDown.png",
				"autoGenerateId" : true,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "TouchDataSetDropDown",
				"parents" : "com.bstek.dorado.touch.widget.form.RowListDropDown",
				"label" : "DataSetDropDown",
				"abstract" : false,
				"nodeName" : "TouchDataSetDropDown",
				"type" : "com.bstek.dorado.touch.widget.form.DataSetDropDown",
				"scope" : "public",
				"sortFactor" : 2103,
				"category" : "Trigger",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/form\/DataSetDropDown.png",
				"autoGenerateId" : true,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onGetBindingDataType",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onSetFilterParameter",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetBindingData",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "TouchDateTimeDropDown",
				"parents" : "com.bstek.dorado.touch.widget.form.DropDown",
				"label" : "DateTimeDropDown",
				"abstract" : false,
				"nodeName" : "TouchDateTimeDropDown",
				"type" : "com.bstek.dorado.touch.widget.form.DateTimeDropDown",
				"scope" : "public",
				"sortFactor" : 2104,
				"category" : "Trigger",
				"autoGenerateId" : true,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "TouchCustomDropDown",
				"parents" : "com.bstek.dorado.touch.widget.form.DropDown",
				"label" : "CustomDropDown",
				"abstract" : false,
				"nodeName" : "TouchCustomDropDown",
				"type" : "com.bstek.dorado.touch.widget.form.CustomDropDown",
				"scope" : "public",
				"sortFactor" : 2105,
				"category" : "Trigger",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/form\/CustomDropDown.png",
				"autoGenerateId" : true,
				"clientTypes" : "touch",
				"deprecated" : false,
				"children" : [ {
					"rule" : "com.bstek.dorado.view.widget.Control",
					"name" : "Control",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "TouchDesktop",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "Desktop",
				"abstract" : false,
				"nodeName" : "TouchDesktop",
				"type" : "com.bstek.dorado.touch.widget.desktop.Desktop",
				"scope" : "public",
				"sortFactor" : 2106,
				"category" : "Touch",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/desktop\/Desktop.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"rule" : "Shortcut",
					"name" : "Items",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "TouchGrid",
				"parents" : "com.bstek.dorado.touch.grid.AbstractGrid",
				"label" : "Grid",
				"abstract" : false,
				"nodeName" : "TouchGrid",
				"type" : "com.bstek.dorado.touch.grid.Grid",
				"scope" : "public",
				"sortFactor" : 2107,
				"category" : "Collection",
				"icon" : "\/com\/bstek\/dorado\/touch\/grid\/Grid.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "TouchDataGrid",
				"parents" : "com.bstek.dorado.touch.grid.AbstractGrid",
				"label" : "DataGrid",
				"abstract" : false,
				"nodeName" : "TouchDataGrid",
				"type" : "com.bstek.dorado.touch.grid.DataGrid",
				"scope" : "public",
				"sortFactor" : 2108,
				"category" : "Collection",
				"icon" : "\/com\/bstek\/dorado\/touch\/grid\/DataGrid.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onGetBindingDataType",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetBindingData",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "Intro",
				"parents" : "Component",
				"label" : "Intro",
				"abstract" : false,
				"type" : "com.bstek.dorado.intro.Intro",
				"scope" : "public",
				"sortFactor" : 2109,
				"category" : "Advance",
				"icon" : "\/com\/bstek\/dorado\/intro\/Intro.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onBeforeChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onBeforeStart",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onComplete",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onExit",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onStart",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [ {
					"rule" : "Step",
					"name" : "Step",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"rule" : "Step",
					"name" : "Steps",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "Step",
				"label" : "Step",
				"abstract" : false,
				"type" : "com.bstek.dorado.intro.Step",
				"scope" : "public",
				"sortFactor" : 2110,
				"icon" : "\/com\/bstek\/dorado\/intro\/Step.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "HtmlEditor",
				"parents" : "AbstractDataEditor",
				"label" : "HtmlEditor",
				"abstract" : false,
				"type" : "com.bstek.dorado.htmleditor.HtmlEditor",
				"scope" : "public",
				"sortFactor" : 2111,
				"category" : "Advance",
				"icon" : "\/com\/bstek\/dorado\/htmleditor\/HtmlEditor.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "Raphael",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "Raphael",
				"abstract" : false,
				"type" : "com.bstek.dorado.raphael.Raphael",
				"scope" : "public",
				"sortFactor" : 2112,
				"category" : "Advance",
				"icon" : "\/com\/bstek\/dorado\/raphael\/Raphael.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false
			},
			{
				"name" : "CriteriaBuilder",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "CriteriaBuilder",
				"abstract" : false,
				"type" : "com.bstek.dorado.criteriabuilder.CriteriaBuilder",
				"scope" : "public",
				"sortFactor" : 2113,
				"category" : "Advance",
				"icon" : "\/com\/bstek\/dorado\/criteriabuilder\/CriteriaBuilder.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "SimpleDesktop",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "SimpleDesktop",
				"abstract" : false,
				"type" : "com.bstek.dorado.desktop.SimpleDesktop",
				"scope" : "public",
				"sortFactor" : 2114,
				"category" : "Advance",
				"icon" : "\/com\/bstek\/dorado\/desktop\/SimpleDesktop.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"rule" : "com.bstek.dorado.desktop.Shortcut",
					"name" : "Items",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "Desktop",
				"parents" : "AbstractDesktop",
				"label" : "Desktop",
				"abstract" : false,
				"type" : "com.bstek.dorado.desktop.Desktop",
				"scope" : "public",
				"sortFactor" : 2115,
				"category" : "Advance",
				"icon" : "\/com\/bstek\/dorado\/desktop\/Desktop.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onShortcutDrop",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [ {
					"rule" : "com.bstek.dorado.desktop.Shortcut",
					"name" : "Items",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "DesktopCarousel",
				"parents" : "AbstractDesktop",
				"label" : "DesktopCarousel",
				"abstract" : false,
				"type" : "com.bstek.dorado.desktop.DesktopCarousel",
				"scope" : "public",
				"sortFactor" : 2116,
				"category" : "Advance",
				"icon" : "\/com\/bstek\/dorado\/desktop\/DesktopCarousel.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onCurrentChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeCurrentChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [ {
					"rule" : "Desktop",
					"name" : "Controls",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "Flash",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "Flash",
				"abstract" : false,
				"type" : "com.bstek.dorado.desktop.Flash",
				"scope" : "public",
				"sortFactor" : 2117,
				"category" : "Advance",
				"icon" : "\/com\/bstek\/dorado\/desktop\/Flash.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "Shell",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "Shell",
				"abstract" : false,
				"type" : "com.bstek.dorado.desktop.Shell",
				"scope" : "public",
				"sortFactor" : 2118,
				"category" : "Advance",
				"icon" : "\/com\/bstek\/dorado\/desktop\/Shell.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"name" : "Apps",
					"fixed" : true,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"rule" : {
						"name" : "Wrapper.Apps",
						"label" : "Apps",
						"abstract" : false,
						"nodeName" : "Apps",
						"scope" : "public",
						"sortFactor" : 0,
						"icon" : "\/com\/bstek\/dorado\/desktop\/Apps.png",
						"autoGenerateId" : false,
						"clientTypes" : "desktop,touch",
						"deprecated" : false,
						"children" : [ {
							"rule" : "App",
							"name" : "Apps",
							"fixed" : false,
							"aggregated" : true,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						} ]
					}
				}, {
					"rule" : "AbstractDesktop",
					"name" : "Desktop",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"rule" : "Taskbar",
					"name" : "Taskbar",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "Taskbar",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "Taskbar",
				"abstract" : false,
				"type" : "com.bstek.dorado.desktop.Taskbar",
				"scope" : "public",
				"sortFactor" : 2119,
				"category" : "Advance",
				"icon" : "\/com\/bstek\/dorado\/desktop\/Taskbar.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onTaskButtonContextMenu",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [
						{
							"name" : "QuickButtons",
							"fixed" : true,
							"aggregated" : false,
							"clientTypes" : "desktop,touch",
							"deprecated" : false,
							"rule" : {
								"name" : "Wrapper.QuickButtons",
								"label" : "QuickButtons",
								"abstract" : false,
								"nodeName" : "QuickButtons",
								"scope" : "public",
								"sortFactor" : 0,
								"icon" : "\/com\/bstek\/dorado\/desktop\/Buttons.png",
								"autoGenerateId" : false,
								"clientTypes" : "desktop,touch",
								"deprecated" : false,
								"children" : [ {
									"rule" : "QuickButton",
									"name" : "QuickButtons",
									"fixed" : false,
									"aggregated" : true,
									"clientTypes" : "desktop,touch",
									"deprecated" : false
								} ]
							}
						},
						{
							"name" : "StartButton",
							"fixed" : true,
							"aggregated" : false,
							"clientTypes" : "desktop,touch",
							"deprecated" : false,
							"rule" : {
								"name" : "Wrapper.StartButton",
								"label" : "StartButton",
								"abstract" : false,
								"nodeName" : "StartButton",
								"scope" : "public",
								"sortFactor" : 0,
								"icon" : "\/com\/bstek\/dorado\/desktop\/StartButton.png",
								"autoGenerateId" : false,
								"clientTypes" : "desktop,touch",
								"deprecated" : false,
								"children" : [ {
									"rule" : "SimpleButton",
									"name" : "StartButton",
									"fixed" : false,
									"aggregated" : false,
									"clientTypes" : "desktop,touch",
									"deprecated" : false
								} ]
							}
						},
						{
							"name" : "TaskButtons",
							"fixed" : true,
							"aggregated" : false,
							"clientTypes" : "desktop,touch",
							"deprecated" : false,
							"rule" : {
								"name" : "Wrapper.TaskButtons",
								"label" : "TaskButtons",
								"abstract" : false,
								"nodeName" : "TaskButtons",
								"scope" : "public",
								"sortFactor" : 0,
								"icon" : "\/com\/bstek\/dorado\/desktop\/Buttons.png",
								"autoGenerateId" : false,
								"clientTypes" : "desktop,touch",
								"deprecated" : false,
								"children" : [ {
									"rule" : "TaskButton",
									"name" : "TaskButtons",
									"fixed" : false,
									"aggregated" : true,
									"clientTypes" : "desktop,touch",
									"deprecated" : false
								} ]
							}
						},
						{
							"name" : "TrayButtons",
							"fixed" : true,
							"aggregated" : false,
							"clientTypes" : "desktop,touch",
							"deprecated" : false,
							"rule" : {
								"name" : "Wrapper.TrayButtons",
								"label" : "TrayButtons",
								"abstract" : false,
								"nodeName" : "TrayButtons",
								"scope" : "public",
								"sortFactor" : 0,
								"icon" : "\/com\/bstek\/dorado\/desktop\/Buttons.png",
								"autoGenerateId" : false,
								"clientTypes" : "desktop,touch",
								"deprecated" : false,
								"children" : [ {
									"rule" : "SimpleButton",
									"name" : "TrayButtons",
									"fixed" : false,
									"aggregated" : true,
									"clientTypes" : "desktop,touch",
									"deprecated" : false
								} ]
							}
						} ]
			},
			{
				"name" : "Portal",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "Portal",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.portal.Portal",
				"scope" : "public",
				"sortFactor" : 2120,
				"category" : "Advance",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/portal\/Portal.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onPortletRemove",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onPortletAdd",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onPortletMove",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [
						{
							"name" : "Columns",
							"fixed" : true,
							"aggregated" : false,
							"clientTypes" : "desktop,touch",
							"deprecated" : false,
							"rule" : {
								"name" : "Wrapper.Columns",
								"label" : "Columns",
								"abstract" : false,
								"nodeName" : "Columns",
								"scope" : "public",
								"sortFactor" : 0,
								"autoGenerateId" : false,
								"clientTypes" : "desktop,touch",
								"deprecated" : false,
								"children" : [ {
									"rule" : "com.bstek.dorado.view.widget.portal.Column",
									"name" : "Columns",
									"fixed" : false,
									"aggregated" : true,
									"clientTypes" : "desktop,touch",
									"deprecated" : false
								} ]
							}
						},
						{
							"name" : "Portlets",
							"fixed" : true,
							"aggregated" : false,
							"clientTypes" : "desktop,touch",
							"deprecated" : false,
							"rule" : {
								"name" : "Wrapper.Portlets",
								"label" : "Portlets",
								"abstract" : false,
								"nodeName" : "Portlets",
								"scope" : "public",
								"sortFactor" : 0,
								"icon" : "\/com\/bstek\/dorado\/view\/widget\/portal\/Portlets.png",
								"autoGenerateId" : false,
								"clientTypes" : "desktop,touch",
								"deprecated" : false,
								"children" : [ {
									"rule" : "Portlet",
									"name" : "Portlets",
									"fixed" : false,
									"aggregated" : true,
									"clientTypes" : "desktop,touch",
									"deprecated" : false
								} ]
							}
						} ]
			},
			{
				"name" : "Map",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "Map",
				"abstract" : false,
				"type" : "com.bstek.dorado.map.Map",
				"scope" : "public",
				"sortFactor" : 2121,
				"category" : "Advance",
				"icon" : "\/com\/bstek\/dorado\/map\/Map.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onRenderElement",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onElementDoubleClick",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onRefreshPaper",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onElementMouseOver",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onElementClick",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onRenderText",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onElementMouseOut",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeRefreshPaper",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [ {
					"rule" : "SubMap",
					"name" : "SubMap",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "SubMap",
				"parents" : "Map,FloatControl",
				"label" : "SubMap",
				"abstract" : false,
				"type" : "com.bstek.dorado.map.SubMap",
				"scope" : "public",
				"sortFactor" : 2122,
				"category" : "Advance",
				"icon" : "\/com\/bstek\/dorado\/map\/SubMap.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "beforeShow",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeHide",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeClose",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onShow",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onClose",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onHide",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "Chart",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "Chart",
				"abstract" : false,
				"type" : "com.bstek.dorado.xchart.Chart",
				"scope" : "public",
				"sortFactor" : 2123,
				"category" : "Advance",
				"icon" : "\/com\/bstek\/dorado\/xchart\/Chart.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"name" : "Axes",
					"fixed" : true,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"rule" : {
						"name" : "Wrapper.Axes",
						"label" : "Axes",
						"abstract" : false,
						"nodeName" : "Axes",
						"scope" : "public",
						"sortFactor" : 0,
						"icon" : "\/com\/bstek\/dorado\/xchart\/Axes.png",
						"autoGenerateId" : false,
						"clientTypes" : "desktop,touch",
						"deprecated" : false,
						"children" : [ {
							"rule" : "Axis",
							"name" : "Axes",
							"fixed" : false,
							"aggregated" : true,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						} ]
					}
				}, {
					"name" : "Series",
					"fixed" : true,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"rule" : {
						"name" : "Wrapper.Series",
						"label" : "Series",
						"abstract" : false,
						"nodeName" : "Series",
						"scope" : "public",
						"sortFactor" : 0,
						"icon" : "\/com\/bstek\/dorado\/xchart\/Series.png",
						"autoGenerateId" : false,
						"clientTypes" : "desktop,touch",
						"deprecated" : false,
						"children" : [ {
							"rule" : "Series",
							"name" : "Series",
							"fixed" : false,
							"aggregated" : true,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						} ]
					}
				} ]
			},
			{
				"name" : "QRCodeImage",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "QRCodeImage",
				"abstract" : false,
				"type" : "com.bstek.dorado.qrcode.QRCodeImage",
				"scope" : "public",
				"sortFactor" : 2124,
				"category" : "Advance",
				"icon" : "\/com\/bstek\/dorado\/qrcode\/QRCodeImage.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "OpenFlashChart",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "OpenFlashChart",
				"abstract" : false,
				"type" : "com.bstek.dorado.ofc.OpenFlashChart",
				"scope" : "public",
				"sortFactor" : 2125,
				"category" : "Advance",
				"icon" : "\/com\/bstek\/dorado\/ofc\/OpenFlashChart.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"name" : "Elements",
					"fixed" : true,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"rule" : {
						"name" : "Wrapper.Elements",
						"label" : "Elements",
						"abstract" : false,
						"nodeName" : "Elements",
						"scope" : "public",
						"sortFactor" : 0,
						"autoGenerateId" : false,
						"clientTypes" : "desktop,touch",
						"deprecated" : false,
						"children" : [ {
							"rule" : "Element",
							"name" : "Elements",
							"fixed" : false,
							"aggregated" : true,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						} ]
					}
				}, {
					"rule" : "com.bstek.dorado.ofc.axis.RadarAxis",
					"name" : "RadarAxis",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"rule" : "XAxis",
					"name" : "XAxis",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "YAxis",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"rule" : {
						"name" : "@YAxis",
						"parents" : "YAxis",
						"label" : "YAxis",
						"abstract" : false,
						"nodeName" : "YAxis",
						"scope" : "private",
						"sortFactor" : 0,
						"autoGenerateId" : false,
						"clientTypes" : "desktop,touch",
						"deprecated" : false
					}
				}, {
					"name" : "YAxisRight",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"rule" : {
						"name" : "@YAxisRight",
						"parents" : "YAxisRight",
						"label" : "YAxisRight",
						"abstract" : false,
						"nodeName" : "YAxisRight",
						"scope" : "private",
						"sortFactor" : 0,
						"autoGenerateId" : false,
						"clientTypes" : "desktop,touch",
						"deprecated" : false
					}
				} ]
			},
			{
				"name" : "Canvas",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "Canvas",
				"abstract" : false,
				"type" : "com.bstek.dorado.canvas.Canvas",
				"scope" : "public",
				"sortFactor" : 2126,
				"category" : "Advance",
				"icon" : "\/com\/bstek\/dorado\/canvas\/Canvas.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"clientEvents" : [ {
					"name" : "onPaint",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "Kinetic",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "Kinetic",
				"abstract" : false,
				"type" : "com.bstek.dorado.canvas.Kinetic",
				"scope" : "public",
				"sortFactor" : 2127,
				"category" : "Advance",
				"icon" : "\/com\/bstek\/dorado\/canvas\/Kinetic.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false
			},
			{
				"name" : "SourceEditor",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "SourceEditor",
				"abstract" : false,
				"type" : "com.bstek.dorado.editor.SourceEditor",
				"scope" : "public",
				"sortFactor" : 2128,
				"category" : "Form",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onHighlightComplete",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGutterClick",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onScroll",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onCursorActivity",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onUpdate",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "PageBanner",
				"parents" : "HtmlContainer",
				"label" : "PageBanner",
				"abstract" : false,
				"nodeName" : "PageBanner",
				"scope" : "public",
				"sortFactor" : 2129,
				"category" : "Others",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "ToggleButton",
				"parents" : "Button",
				"label" : "ToggleButton",
				"abstract" : false,
				"nodeName" : "ToggleButton",
				"scope" : "public",
				"sortFactor" : 2130,
				"category" : "Others",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false
			},
			{
				"name" : "Calculator",
				"parents" : "Panel",
				"label" : "Calculator",
				"abstract" : false,
				"nodeName" : "Calculator",
				"scope" : "public",
				"sortFactor" : 2131,
				"category" : "Others",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onCalculate",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "SimpleCRUD",
				"parents" : "Container",
				"label" : "SimpleCRUD",
				"abstract" : false,
				"nodeName" : "SimpleCRUD",
				"scope" : "public",
				"sortFactor" : 2132,
				"category" : "Others",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "DualListSelection",
				"parents" : "Container",
				"label" : "DualListSelection",
				"abstract" : false,
				"nodeName" : "DualListSelection",
				"scope" : "public",
				"sortFactor" : 2133,
				"category" : "Others",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onSelectionChange",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "QuickForm",
				"parents" : "AutoForm",
				"label" : "QuickForm",
				"abstract" : false,
				"nodeName" : "QuickForm",
				"type" : "com.bstek.dorado.sample.widget.customize.QuickForm",
				"scope" : "public",
				"sortFactor" : 2134,
				"category" : "Others",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "AbstractViewElement",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.AbstractViewElement",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"primitiveProps" : [ {
					"name" : "id",
					"highlight" : 1,
					"editor" : "",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"props" : [ {
					"name" : "id",
					"highlight" : 1,
					"editor" : "",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "ClientEventSupportedElement",
				"parents" : "AbstractViewElement",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.ClientEventSupportedElement",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false
			},
			{
				"name" : "Component",
				"parents" : "ClientEventSupportedElement",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.Component",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"primitiveProps" : [ {
					"name" : "listener",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"props" : [ {
					"name" : "listener",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"clientEvents" : [ {
					"name" : "onCreate",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onReady",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onDestroy",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onAttributeChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "com.bstek.dorado.view.widget.Control",
				"parents" : "Component",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.Control",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/Control.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"primitiveProps" : [ {
					"name" : "listener",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"props" : [ {
					"name" : "listener",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"clientEvents" : [ {
					"name" : "onClick",
					"parameters" : "self,arg",
					"clientTypes" : "desktop",
					"deprecated" : false
				}, {
					"name" : "onDraggingSourceOut",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetDraggingIndicator",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onKeyPress",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onDragStop",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onCreateDom",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onResize",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onDoubleClick",
					"parameters" : "self,arg",
					"clientTypes" : "desktop",
					"deprecated" : false
				}, {
					"name" : "onMouseUp",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onFocus",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeDraggingSourceDrop",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onKeyDown",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onBlur",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onDraggingSourceOver",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onSwipe",
					"parameters" : "self,arg",
					"clientTypes" : "touch",
					"deprecated" : false
				}, {
					"name" : "onDraggingSourceDrop",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onDraggingSourceMove",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onDragStart",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeRefreshDom",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onDragMove",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onContextMenu",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onTap",
					"parameters" : "self,arg",
					"clientTypes" : "touch",
					"deprecated" : false
				}, {
					"name" : "onDoubleTap",
					"parameters" : "self,arg",
					"clientTypes" : "touch",
					"deprecated" : false
				}, {
					"name" : "onTapHold",
					"parameters" : "self,arg",
					"clientTypes" : "touch",
					"deprecated" : false
				}, {
					"name" : "onMouseDown",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onRefreshDom",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "AsyncAction",
				"parents" : "Action",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.action.AsyncAction",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "UpdateItem",
				"label" : "UpdateItem",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.action.UpdateItem",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/action\/UpdateItem.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "AbstractButton",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.base.AbstractButton",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "AbstractPanel",
				"parents" : "Container",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.base.AbstractPanel",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onCollapsedChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeCollapsedChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [
						{
							"name" : "Buttons",
							"fixed" : true,
							"aggregated" : false,
							"clientTypes" : "desktop,touch",
							"deprecated" : false,
							"rule" : {
								"name" : "Wrapper.Buttons",
								"label" : "Buttons",
								"abstract" : false,
								"nodeName" : "Buttons",
								"scope" : "public",
								"sortFactor" : 0,
								"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/Buttons.png",
								"autoGenerateId" : false,
								"clientTypes" : "desktop,touch",
								"deprecated" : false,
								"children" : [ {
									"rule" : "Button",
									"name" : "Buttons",
									"fixed" : false,
									"aggregated" : true,
									"clientTypes" : "desktop,touch",
									"deprecated" : false
								} ]
							}
						},
						{
							"name" : "Children",
							"fixed" : true,
							"aggregated" : false,
							"clientTypes" : "desktop,touch",
							"deprecated" : false,
							"rule" : {
								"name" : "Wrapper.Children",
								"label" : "Children",
								"abstract" : false,
								"nodeName" : "Children",
								"scope" : "public",
								"sortFactor" : 0,
								"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/Children.png",
								"autoGenerateId" : false,
								"clientTypes" : "desktop,touch",
								"deprecated" : false,
								"children" : [ {
									"rule" : "Component",
									"name" : "Children",
									"fixed" : false,
									"aggregated" : true,
									"clientTypes" : "desktop,touch",
									"deprecated" : false
								} ]
							}
						} ]
			},
			{
				"name" : "IFrameTab",
				"parents" : "Tab",
				"label" : "IFrameTab",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.tab.IFrameTab",
				"scope" : "protected",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/tab\/IFrameTab.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "Tab",
				"parents" : "ClientEventSupportedElement",
				"label" : "Tab",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.tab.Tab",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/tab\/Tab.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onClick",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeClose",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onClose",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "ControlTab",
				"parents" : "Tab",
				"label" : "ControlTab",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.tab.ControlTab",
				"scope" : "protected",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/tab\/ControlTab.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"children" : [ {
					"rule" : "com.bstek.dorado.view.widget.Control",
					"name" : "Control",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "AbstractTabControl",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.base.tab.AbstractTabControl",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onTabContextMenu",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onTabRemove",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeTabChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onTabChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "MenuButton",
				"parents" : "Button",
				"label" : "MenuButton",
				"abstract" : false,
				"nodeName" : "MenuButton",
				"type" : "com.bstek.dorado.view.widget.base.toolbar.MenuButton",
				"scope" : "protected",
				"sortFactor" : 0,
				"category" : "ToolBar",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/toolbar\/MenuButton.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"rule" : "BaseMenuItem",
					"name" : "Items",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "BaseMenuItem",
				"parents" : "ClientEventSupportedElement",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.base.menu.BaseMenuItem",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "TextMenuItem",
				"parents" : "BaseMenuItem",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.base.menu.TextMenuItem",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onClick",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "MenuItem",
				"parents" : "TextMenuItem",
				"label" : "MenuItem",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.menu.MenuItem",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/menu\/MenuItem.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"children" : [ {
					"rule" : "BaseMenuItem",
					"name" : "Items",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "Separator",
				"parents" : "BaseMenuItem",
				"label" : "Separator",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.menu.Separator",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/menu\/Separator.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false
			},
			{
				"name" : "CheckableMenuItem",
				"parents" : "MenuItem",
				"label" : "CheckableMenuItem",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.menu.CheckableMenuItem",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/menu\/CheckableMenuItem.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onCheckedChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "ControlMenuItem",
				"parents" : "TextMenuItem",
				"label" : "ControlMenuItem",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.menu.ControlMenuItem",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/menu\/ControlMenuItem.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"children" : [ {
					"rule" : "FloatControl",
					"name" : "Control",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "FloatControl",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.FloatControl",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "com.bstek.dorado.view.widget.base.toolbar.Label",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "Label",
				"abstract" : false,
				"nodeName" : "ToolBarLabel",
				"type" : "com.bstek.dorado.view.widget.base.toolbar.Label",
				"scope" : "protected",
				"sortFactor" : 0,
				"category" : "ToolBar",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/toolbar\/Label.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "com.bstek.dorado.view.widget.base.toolbar.Separator",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "Separator",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.toolbar.Separator",
				"scope" : "protected",
				"sortFactor" : 0,
				"category" : "ToolBar",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/toolbar\/Separator.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false
			},
			{
				"name" : "Fill",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "Fill",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.toolbar.Fill",
				"scope" : "protected",
				"sortFactor" : 0,
				"category" : "ToolBar",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/toolbar\/Fill.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false
			},
			{
				"name" : "com.bstek.dorado.view.widget.base.toolbar.Button",
				"parents" : "Button",
				"label" : "ToolBarButton",
				"abstract" : false,
				"nodeName" : "ToolBarButton",
				"type" : "com.bstek.dorado.view.widget.base.toolbar.Button",
				"scope" : "protected",
				"sortFactor" : 0,
				"category" : "ToolBar",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/toolbar\/Button.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "Section",
				"parents" : "ClientEventSupportedElement",
				"label" : "Section",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.base.accordion.Section",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/base\/accordion\/Section.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onCaptionClick",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [ {
					"rule" : "com.bstek.dorado.view.widget.Control",
					"name" : "Control",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "AbstractDataControl",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.datacontrol.AbstractDataControl",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onGetBindingDataType",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetBindingData",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "AbstractPropertyDataControl",
				"parents" : "AbstractDataControl",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.datacontrol.AbstractPropertyDataControl",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onGetBindingDataType",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetBindingData",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "AbstractEditor",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.form.AbstractEditor",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "beforePost",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onPost",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onPostFailed",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "AbstractDataEditor",
				"parents" : "AbstractEditor",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.form.AbstractDataEditor",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onGetBindingDataType",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetBindingData",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "AbstractTextBox",
				"parents" : "AbstractDataEditor",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.form.AbstractTextBox",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onTextEdit",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onValidationStateChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onTriggerClick",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "AbstractTextEditor",
				"parents" : "AbstractTextBox",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.form.AbstractTextEditor",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "RadioButton",
				"label" : "RadioButton",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.form.RadioButton",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/form\/RadioButton.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "Spinner",
				"parents" : "AbstractTextBox",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.form.Spinner",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "DropDown",
				"parents" : "Trigger",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.form.trigger.DropDown",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onOpen",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onValueSelect",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onClose",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "RowListDropDown",
				"parents" : "DropDown",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.form.trigger.RowListDropDown",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onFilterItems",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onFilterItem",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [ {
					"rule" : "com.bstek.dorado.view.widget.grid.Column",
					"name" : "Columns",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "com.bstek.dorado.view.widget.grid.Column",
				"parents" : "ClientEventSupportedElement",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.grid.Column",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onRenderHeaderCell",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onHeaderClick",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetCellEditor",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "DataColumn",
				"parents" : "AbstractDataColumn",
				"label" : "DataColumn",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.grid.DataColumn",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/grid\/DataColumn.png",
				"labelProperty" : "name,property",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"name" : "Editor",
					"fixed" : true,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"rule" : {
						"name" : "Wrapper.Editor",
						"label" : "Editor",
						"abstract" : false,
						"nodeName" : "Editor",
						"scope" : "public",
						"sortFactor" : 0,
						"icon" : "\/com\/bstek\/dorado\/view\/widget\/grid\/Editor.png",
						"autoGenerateId" : false,
						"clientTypes" : "desktop,touch",
						"deprecated" : false,
						"children" : [ {
							"rule" : "com.bstek.dorado.view.widget.Control",
							"name" : "Editor",
							"fixed" : false,
							"aggregated" : false,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						} ]
					}
				} ]
			},
			{
				"name" : "AbstractDataColumn",
				"parents" : "com.bstek.dorado.view.widget.grid.Column",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.grid.AbstractDataColumn",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onRenderFooterCell",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onRenderCell",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "ColumnGroup",
				"parents" : "com.bstek.dorado.view.widget.grid.Column",
				"label" : "ColumnGroup",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.grid.ColumnGroup",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/grid\/ColumnGroup.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"rule" : "com.bstek.dorado.view.widget.grid.Column",
					"name" : "Columns",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "RowSelectorColumn",
				"parents" : "AbstractDataColumn",
				"label" : "RowSelectorColumn",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.grid.RowSelectorColumn",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/grid\/RowSelectorColumn.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false
			},
			{
				"name" : "RowNumColumn",
				"parents" : "AbstractDataColumn",
				"label" : "RowNumColumn",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.grid.RowNumColumn",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/grid\/RowNumColumn.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false
			},
			{
				"name" : "IndicatorColumn",
				"parents" : "AbstractDataColumn",
				"label" : "IndicatorColumn",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.grid.IndicatorColumn",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/grid\/IndicatorColumn.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false
			},
			{
				"name" : "AbstractList",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.list.AbstractList",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onSelectionChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onCurrentChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeSelectionChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onCompareItems",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onFilterItem",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "RowList",
				"parents" : "AbstractList",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.list.RowList",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onDataRowClick",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onDataRowDoubleClick",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "AbstractListBox",
				"parents" : "RowList",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.list.AbstractListBox",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onRenderRow",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "GridSupport",
				"parents" : "RowList",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.grid.GridSupport",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onRenderHeaderCell",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onHeaderClick",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeCellValueEdit",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onRenderFooterCell",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onRenderRow",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onRenderCell",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onCellValueEdit",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetCellEditor",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [ {
					"rule" : "com.bstek.dorado.view.widget.grid.Column",
					"name" : "Columns",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "AbstractGrid",
				"parents" : "GridSupport",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.grid.AbstractGrid",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "AbstractTree",
				"parents" : "RowList",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.tree.AbstractTree",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onNodeCheckedChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onRenderNode",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeCurrentChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onExpand",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeCollapse",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onCollapse",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeExpand",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onNodeDetached",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeNodeCheckedChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onNodeAttached",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "BaseNode",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.tree.BaseNode",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"rule" : "BaseNode",
					"name" : "Nodes",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "Node",
				"parents" : "BaseNode",
				"label" : "Node",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.tree.Node",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/tree\/Node.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"primitiveProps" : [ {
					"name" : "id",
					"highlight" : 1,
					"editor" : "",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"props" : [ {
					"name" : "id",
					"highlight" : 1,
					"editor" : "",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "BindingConfig",
				"label" : "BindingConfig",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.tree.BindingConfig",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/tree\/BindingConfig.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"rule" : "BindingConfig",
					"name" : "ChildBindingConfigs",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "AbstractBlockView",
				"parents" : "AbstractList",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.blockview.AbstractBlockView",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onRenderBlock",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onBlockTap",
					"parameters" : "self,arg",
					"clientTypes" : "touch",
					"deprecated" : false
				}, {
					"name" : "onBlockDoubleClick",
					"parameters" : "self,arg",
					"clientTypes" : "desktop",
					"deprecated" : false
				}, {
					"name" : "onBlockMouseDown",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onBlockClick",
					"parameters" : "self,arg",
					"clientTypes" : "desktop",
					"deprecated" : false
				}, {
					"name" : "onBlockMouseUp",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onBlockTapHold",
					"parameters" : "self,arg",
					"clientTypes" : "touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "AbstractTreeGrid",
				"parents" : "GridSupport",
				"abstract" : true,
				"type" : "com.bstek.dorado.view.widget.treegrid.AbstractTreeGrid",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onNodeCheckedChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onRenderNode",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onExpand",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeCollapse",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onCollapse",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeExpand",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onNodeDetached",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "beforeNodeCheckedChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onNodeAttached",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [ {
					"name" : "Columns",
					"fixed" : true,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"rule" : {
						"name" : "Wrapper.Columns",
						"label" : "Columns",
						"abstract" : false,
						"nodeName" : "Columns",
						"scope" : "public",
						"sortFactor" : 0,
						"icon" : "\/com\/bstek\/dorado\/view\/widget\/treegrid\/Columns.png",
						"autoGenerateId" : false,
						"clientTypes" : "desktop,touch",
						"deprecated" : false,
						"children" : [ {
							"rule" : "com.bstek.dorado.view.widget.grid.Column",
							"name" : "Columns",
							"fixed" : false,
							"aggregated" : true,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						} ]
					}
				} ]
			},
			{
				"name" : "Spacer",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "ToolBarSpacer",
				"abstract" : false,
				"nodeName" : "TouchToolBarSpacer",
				"type" : "com.bstek.dorado.touch.widget.toolbar.Spacer",
				"scope" : "protected",
				"sortFactor" : 0,
				"category" : "ToolBar",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/toolbar\/Spacer.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "com.bstek.dorado.touch.widget.toolbar.Separator",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "ToolBarSeparator",
				"abstract" : false,
				"nodeName" : "TouchToolBarSeparator",
				"type" : "com.bstek.dorado.touch.widget.toolbar.Separator",
				"scope" : "protected",
				"sortFactor" : 0,
				"category" : "ToolBar",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/toolbar\/Separator.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false
			},
			{
				"name" : "TabButton",
				"parents" : "com.bstek.dorado.touch.widget.tab.Tab",
				"label" : "TabButton",
				"abstract" : false,
				"nodeName" : "TabButton",
				"type" : "com.bstek.dorado.touch.widget.tab.TabButton",
				"scope" : "protected",
				"sortFactor" : 0,
				"category" : "",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/tab\/TabButton.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"children" : [ {
					"rule" : "com.bstek.dorado.view.widget.Control",
					"name" : "Control",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "com.bstek.dorado.touch.widget.tab.Tab",
				"parents" : "TouchButton",
				"abstract" : true,
				"type" : "com.bstek.dorado.touch.widget.tab.Tab",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "ItemGroup",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "ItemGroup",
				"abstract" : false,
				"type" : "com.bstek.dorado.touch.widget.ItemGroup",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"children" : [ {
					"rule" : "com.bstek.dorado.view.widget.Control",
					"name" : "Items",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "AbstractNestedList",
				"parents" : "TouchPanel",
				"abstract" : true,
				"type" : "com.bstek.dorado.touch.widget.list.AbstractNestedList",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onSelectionChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onCurrentChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onRenderRow",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onItemTapHold",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onItemTap",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onListChange",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onItemSwipe",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onLeafItemTap",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [ {
					"name" : "Buttons",
					"fixed" : true,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"visible" : false,
					"rule" : {
						"name" : "Wrapper.Buttons",
						"label" : "Buttons",
						"abstract" : false,
						"nodeName" : "Buttons",
						"scope" : "public",
						"sortFactor" : 0,
						"autoGenerateId" : false,
						"clientTypes" : "desktop,touch",
						"deprecated" : false,
						"children" : [ {
							"rule" : "TouchButton",
							"name" : "Buttons",
							"fixed" : false,
							"aggregated" : true,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						} ]
					}
				}, {
					"name" : "Children",
					"fixed" : true,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false,
					"visible" : false,
					"rule" : {
						"name" : "Wrapper.Children",
						"label" : "Children",
						"abstract" : false,
						"nodeName" : "Children",
						"scope" : "public",
						"sortFactor" : 0,
						"autoGenerateId" : false,
						"clientTypes" : "desktop,touch",
						"deprecated" : false,
						"children" : [ {
							"rule" : "Component",
							"name" : "Children",
							"fixed" : false,
							"aggregated" : true,
							"clientTypes" : "desktop,touch",
							"deprecated" : false
						} ]
					}
				} ]
			},
			{
				"name" : "com.bstek.dorado.touch.widget.list.BindingConfig",
				"label" : "BindingConfig",
				"abstract" : false,
				"type" : "com.bstek.dorado.touch.widget.list.BindingConfig",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"rule" : "com.bstek.dorado.touch.widget.list.BindingConfig",
					"name" : "ChildBindingConfigs",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "com.bstek.dorado.touch.widget.form.DropDown",
				"parents" : "DropDown",
				"abstract" : true,
				"type" : "com.bstek.dorado.touch.widget.form.DropDown",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "com.bstek.dorado.touch.widget.form.RowListDropDown",
				"parents" : "com.bstek.dorado.touch.widget.form.DropDown",
				"abstract" : true,
				"type" : "com.bstek.dorado.touch.widget.form.RowListDropDown",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "Shortcut",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "Shortcut",
				"abstract" : false,
				"nodeName" : "TouchShortcut",
				"type" : "com.bstek.dorado.touch.widget.desktop.Shortcut",
				"scope" : "public",
				"sortFactor" : 0,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/touch\/widget\/desktop\/Shortcut.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "com.bstek.dorado.touch.grid.GridSupport",
				"parents" : "RowList",
				"abstract" : true,
				"type" : "com.bstek.dorado.touch.grid.GridSupport",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onRenderHeaderCell",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onHeaderClick",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onRenderFooterCell",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onRenderRow",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onRenderCell",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetCellEditor",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ],
				"children" : [ {
					"rule" : "com.bstek.dorado.touch.grid.Column",
					"name" : "Columns",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "com.bstek.dorado.touch.grid.Column",
				"parents" : "ClientEventSupportedElement",
				"abstract" : true,
				"nodeName" : "TouchColumn",
				"type" : "com.bstek.dorado.touch.grid.Column",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onRenderHeaderCell",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onHeaderClick",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "com.bstek.dorado.touch.grid.RowNumColumn",
				"parents" : "com.bstek.dorado.touch.grid.AbstractDataColumn",
				"label" : "RowNumColumn",
				"abstract" : false,
				"nodeName" : "TouchRowNumColumn",
				"type" : "com.bstek.dorado.touch.grid.RowNumColumn",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/touch\/grid\/RowNumColumn.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false
			},
			{
				"name" : "com.bstek.dorado.touch.grid.AbstractDataColumn",
				"parents" : "com.bstek.dorado.touch.grid.Column",
				"abstract" : true,
				"type" : "com.bstek.dorado.touch.grid.AbstractDataColumn",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onRenderFooterCell",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onRenderCell",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "com.bstek.dorado.touch.grid.DataColumn",
				"parents" : "com.bstek.dorado.touch.grid.AbstractDataColumn",
				"label" : "DataColumn",
				"abstract" : false,
				"nodeName" : "TouchDataColumn",
				"type" : "com.bstek.dorado.touch.grid.DataColumn",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/touch\/grid\/DataColumn.png",
				"labelProperty" : "name,property",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "com.bstek.dorado.touch.grid.IndicatorColumn",
				"parents" : "com.bstek.dorado.touch.grid.AbstractDataColumn",
				"label" : "IndicatorColumn",
				"abstract" : false,
				"nodeName" : "TouchIndicatorColumn",
				"type" : "com.bstek.dorado.touch.grid.IndicatorColumn",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/touch\/grid\/IndicatorColumn.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false
			},
			{
				"name" : "com.bstek.dorado.touch.grid.RowSelectorColumn",
				"parents" : "com.bstek.dorado.touch.grid.AbstractDataColumn",
				"label" : "RowSelectorColumn",
				"abstract" : false,
				"nodeName" : "TouchRowSelectorColumn",
				"type" : "com.bstek.dorado.touch.grid.RowSelectorColumn",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/touch\/grid\/RowSelectorColumn.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false
			},
			{
				"name" : "com.bstek.dorado.touch.grid.ColumnGroup",
				"parents" : "com.bstek.dorado.touch.grid.Column",
				"label" : "ColumnGroup",
				"abstract" : false,
				"nodeName" : "TouchColumnGroup",
				"type" : "com.bstek.dorado.touch.grid.ColumnGroup",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/touch\/grid\/ColumnGroup.png",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false,
				"children" : [ {
					"rule" : "com.bstek.dorado.touch.grid.Column",
					"name" : "Columns",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "com.bstek.dorado.touch.grid.AbstractGrid",
				"parents" : "com.bstek.dorado.touch.grid.GridSupport",
				"abstract" : true,
				"type" : "com.bstek.dorado.touch.grid.AbstractGrid",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "com.bstek.dorado.desktop.Shortcut",
				"label" : "Shortcut",
				"abstract" : false,
				"type" : "com.bstek.dorado.desktop.Shortcut",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/desktop\/Shortcut.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "AbstractDesktop",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"abstract" : true,
				"type" : "com.bstek.dorado.desktop.AbstractDesktop",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onShortcutContextMenu",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "App",
				"parents" : "AbstractViewElement",
				"abstract" : true,
				"type" : "com.bstek.dorado.desktop.App",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "WidgetApp",
				"parents" : "AbstractControlApp",
				"label" : "WidgetApp",
				"abstract" : false,
				"type" : "com.bstek.dorado.desktop.WidgetApp",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/desktop\/WidgetApp.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"rule" : "com.bstek.dorado.view.widget.Control",
					"name" : "Control",
					"fixed" : true,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "AbstractControlApp",
				"parents" : "App",
				"abstract" : true,
				"type" : "com.bstek.dorado.desktop.AbstractControlApp",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false
			},
			{
				"name" : "ControlApp",
				"parents" : "AbstractControlApp",
				"label" : "ControlApp",
				"abstract" : false,
				"type" : "com.bstek.dorado.desktop.ControlApp",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/desktop\/ControlApp.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"rule" : "FloatControl",
					"name" : "Control",
					"fixed" : true,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "DialogApp",
				"parents" : "AbstractControlApp",
				"label" : "DialogApp",
				"abstract" : false,
				"type" : "com.bstek.dorado.desktop.DialogApp",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/desktop\/DialogApp.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"rule" : "Dialog",
					"name" : "Control",
					"fixed" : true,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "IFrameApp",
				"parents" : "DialogApp",
				"label" : "IFrameApp",
				"abstract" : false,
				"type" : "com.bstek.dorado.desktop.IFrameApp",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/desktop\/IFrameApp.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "QuickButton",
				"parents" : "SimpleIconButton",
				"label" : "SimpleIconButton",
				"abstract" : false,
				"type" : "com.bstek.dorado.desktop.QuickButton",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "TaskButton",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "TaskButton",
				"abstract" : false,
				"type" : "com.bstek.dorado.desktop.TaskButton",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/desktop\/TaskButton.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "com.bstek.dorado.view.widget.portal.Column",
				"label" : "Column",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.portal.Column",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "Portlet",
				"parents" : "Panel",
				"label" : "Panel",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.portal.Portlet",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "Axis",
				"abstract" : true,
				"type" : "com.bstek.dorado.xchart.Axis",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onGetBindingDataType",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetBindingData",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "RadarAxis",
				"parents" : "Axis",
				"label" : "RadarAxis",
				"abstract" : false,
				"type" : "com.bstek.dorado.xchart.axis.RadarAxis",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/xchart\/axis\/RadarAxis.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "CategoryAxis",
				"parents" : "Axis",
				"label" : "CategoryAxis",
				"abstract" : false,
				"type" : "com.bstek.dorado.xchart.axis.CategoryAxis",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/xchart\/axis\/CategoryAxis.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "NumbericAxis",
				"parents" : "Axis",
				"label" : "NumbericAxis",
				"abstract" : false,
				"type" : "com.bstek.dorado.xchart.axis.NumbericAxis",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/xchart\/axis\/NumbericAxis.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "Series",
				"abstract" : true,
				"type" : "com.bstek.dorado.xchart.Series",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/xchart\/Series.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onClick",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetBindingDataType",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetBindingData",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "Area",
				"parents" : "Line",
				"label" : "Area",
				"abstract" : false,
				"type" : "com.bstek.dorado.xchart.series.Area",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/xchart\/series\/Area.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "Line",
				"parents" : "Series",
				"label" : "Line",
				"abstract" : false,
				"type" : "com.bstek.dorado.xchart.series.Line",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/xchart\/series\/Line.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "Pie",
				"parents" : "Series",
				"label" : "Pie",
				"abstract" : false,
				"type" : "com.bstek.dorado.xchart.series.Pie",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/xchart\/series\/Pie.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "Bar",
				"parents" : "Series",
				"label" : "Bar",
				"abstract" : false,
				"type" : "com.bstek.dorado.xchart.series.Bar",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/xchart\/series\/Bar.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "com.bstek.dorado.xchart.series.Column",
				"parents" : "Series",
				"label" : "Column",
				"abstract" : false,
				"type" : "com.bstek.dorado.xchart.series.Column",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/xchart\/series\/Column.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "Scatter",
				"parents" : "Series",
				"label" : "Scatter",
				"abstract" : false,
				"type" : "com.bstek.dorado.xchart.series.Scatter",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/xchart\/series\/Scatter.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "Element",
				"abstract" : true,
				"type" : "com.bstek.dorado.ofc.Element",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"clientEvents" : [ {
					"name" : "onClick",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetBindingDataType",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"name" : "onGetBindingData",
					"parameters" : "self,arg",
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "com.bstek.dorado.ofc.element.Bar",
				"parents" : "Element",
				"label" : "Bar",
				"abstract" : false,
				"type" : "com.bstek.dorado.ofc.element.Bar",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/ofc\/element\/Bar.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "Shape",
				"parents" : "Element",
				"label" : "Shape",
				"abstract" : false,
				"type" : "com.bstek.dorado.ofc.element.Shape",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/ofc\/element\/Shape.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "StackedColumn",
				"parents" : "Element",
				"label" : "StackedColumn",
				"abstract" : false,
				"type" : "com.bstek.dorado.ofc.element.StackedColumn",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/ofc\/element\/StackedColumn.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"rule" : "Key",
					"name" : "Keys",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "Key",
				"label" : "Key",
				"abstract" : false,
				"type" : "com.bstek.dorado.ofc.Key",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "com.bstek.dorado.ofc.element.Scatter",
				"parents" : "Element",
				"label" : "Scatter",
				"abstract" : false,
				"type" : "com.bstek.dorado.ofc.element.Scatter",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/ofc\/element\/Scatter.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "FilledColumn",
				"parents" : "com.bstek.dorado.ofc.element.Column",
				"label" : "FilledColumn",
				"abstract" : false,
				"type" : "com.bstek.dorado.ofc.element.FilledColumn",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/ofc\/element\/FilledColumn.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "com.bstek.dorado.ofc.element.Column",
				"parents" : "Element",
				"label" : "Column",
				"abstract" : false,
				"type" : "com.bstek.dorado.ofc.element.Column",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/ofc\/element\/Column.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "com.bstek.dorado.ofc.element.Line",
				"parents" : "Element",
				"label" : "Line",
				"abstract" : false,
				"type" : "com.bstek.dorado.ofc.element.Line",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/ofc\/element\/Line.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "SketchColumn",
				"parents" : "FilledColumn",
				"label" : "SketchColumn",
				"abstract" : false,
				"type" : "com.bstek.dorado.ofc.element.SketchColumn",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/ofc\/element\/SketchColumn.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "com.bstek.dorado.ofc.element.Area",
				"parents" : "com.bstek.dorado.ofc.element.Line",
				"label" : "Area",
				"abstract" : false,
				"type" : "com.bstek.dorado.ofc.element.Area",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/ofc\/element\/Area.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "Arrow",
				"parents" : "Element",
				"label" : "Arrow",
				"abstract" : false,
				"type" : "com.bstek.dorado.ofc.element.Arrow",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/ofc\/element\/Arrow.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "com.bstek.dorado.ofc.element.Pie",
				"parents" : "Element",
				"label" : "Pie",
				"abstract" : false,
				"type" : "com.bstek.dorado.ofc.element.Pie",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/ofc\/element\/Pie.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "Tags",
				"parents" : "Element",
				"label" : "Tags",
				"abstract" : false,
				"type" : "com.bstek.dorado.ofc.element.Tags",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/ofc\/element\/Tags.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "CandleChart",
				"parents" : "com.bstek.dorado.ofc.element.Column",
				"label" : "CandleChart",
				"abstract" : false,
				"type" : "com.bstek.dorado.ofc.element.CandleChart",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/ofc\/element\/CandleChart.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "com.bstek.dorado.ofc.axis.RadarAxis",
				"parents" : "AbstractAxis",
				"label" : "RadarAxis",
				"abstract" : false,
				"type" : "com.bstek.dorado.ofc.axis.RadarAxis",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/ofc\/axis\/RadarAxis.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"rule" : "RadarAxisLabels",
					"name" : "Labels",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				}, {
					"rule" : "RadarAxisSpokeLabels",
					"name" : "SpokeLabels",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "AbstractAxis",
				"label" : "AbstractAxis",
				"abstract" : false,
				"type" : "com.bstek.dorado.ofc.axis.AbstractAxis",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "RadarAxisLabels",
				"label" : "RadarAxisLabels",
				"abstract" : false,
				"type" : "com.bstek.dorado.ofc.axis.RadarAxisLabels",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/ofc\/axis\/RadarAxisLabels.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"rule" : "AxisLabel",
					"name" : "Labels",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "AxisLabel",
				"label" : "AxisLabel",
				"abstract" : false,
				"type" : "com.bstek.dorado.ofc.axis.AxisLabel",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/ofc\/axis\/AxisLabel.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			},
			{
				"name" : "RadarAxisSpokeLabels",
				"label" : "RadarAxisSpokeLabels",
				"abstract" : false,
				"type" : "com.bstek.dorado.ofc.axis.RadarAxisSpokeLabels",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/ofc\/axis\/RadarAxisSpokeLabels.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"rule" : "AxisLabel",
					"name" : "Labels",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "XAxis",
				"parents" : "AbstractAxis",
				"label" : "XAxis",
				"abstract" : false,
				"type" : "com.bstek.dorado.ofc.axis.XAxis",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/ofc\/axis\/XAxis.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"rule" : "XAxisLabels",
					"name" : "Labels",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "XAxisLabels",
				"label" : "XAxisLabels",
				"abstract" : false,
				"nodeName" : "Labels",
				"type" : "com.bstek.dorado.ofc.axis.XAxisLabels",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/ofc\/axis\/XAxisLabels.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"rule" : "AxisLabel",
					"name" : "Labels",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "YAxis",
				"parents" : "AbstractAxis",
				"label" : "YAxis",
				"abstract" : false,
				"type" : "com.bstek.dorado.ofc.axis.YAxis",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/ofc\/axis\/YAxis.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"rule" : "YAxisLabels",
					"name" : "Labels",
					"fixed" : false,
					"aggregated" : false,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "YAxisLabels",
				"label" : "YAxisLabels",
				"abstract" : false,
				"nodeName" : "Labels",
				"type" : "com.bstek.dorado.ofc.axis.YAxisLabels",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/ofc\/axis\/YAxisLabels.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : [],
				"children" : [ {
					"rule" : "AxisLabel",
					"name" : "Labels",
					"fixed" : false,
					"aggregated" : true,
					"clientTypes" : "desktop,touch",
					"deprecated" : false
				} ]
			},
			{
				"name" : "YAxisRight",
				"parents" : "YAxis",
				"label" : "YAxisRight",
				"abstract" : false,
				"type" : "com.bstek.dorado.ofc.axis.YAxisRight",
				"scope" : "public",
				"sortFactor" : 0,
				"icon" : "\/com\/bstek\/dorado\/ofc\/axis\/YAxisRight.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false
			},
			{
				"name" : "DefaultView",
				"parents" : "View",
				"label" : "View",
				"abstract" : false,
				"nodeName" : "View",
				"type" : "com.bstek.dorado.view.DefaultView",
				"scope" : "public",
				"sortFactor" : 0,
				"category" : "General",
				"icon" : "\/com\/bstek\/dorado\/view\/DefaultView.png",
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false
			},
			{
				"name" : "AutoFormElement",
				"parents" : "FormElement",
				"label" : "AutoFormElement",
				"abstract" : false,
				"type" : "com.bstek.dorado.view.widget.form.autoform.AutoFormElement",
				"scope" : "protected",
				"sortFactor" : 0,
				"category" : "AutoForm",
				"icon" : "\/com\/bstek\/dorado\/view\/widget\/form\/autoform\/AutoFormElement.png",
				"labelProperty" : "id,name,property",
				"autoGenerateId" : false,
				"clientTypes" : "desktop,touch",
				"deprecated" : false,
				"props" : []
			}, {
				"name" : "com.bstek.dorado.touch.widget.menu.BaseMenuItem",
				"parents" : "com.bstek.dorado.view.widget.Control",
				"label" : "BaseMenuItem",
				"abstract" : false,
				"type" : "com.bstek.dorado.touch.widget.menu.BaseMenuItem",
				"scope" : "public",
				"sortFactor" : 0,
				"autoGenerateId" : false,
				"clientTypes" : "desktop",
				"deprecated" : false
			}, {
				"name" : "com.bstek.dorado.touch.widget.menu.Separator",
				"parents" : "com.bstek.dorado.touch.widget.menu.BaseMenuItem",
				"label" : "MenuSeparator",
				"abstract" : false,
				"type" : "com.bstek.dorado.touch.widget.menu.Separator",
				"scope" : "public",
				"sortFactor" : 0,
				"category" : "General",
				"autoGenerateId" : false,
				"clientTypes" : "touch",
				"deprecated" : false
			} ]
}