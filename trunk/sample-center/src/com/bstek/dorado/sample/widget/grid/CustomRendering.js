// @Bind #dsPhones.onCreate
!function(self) {
	$ajax.request(">dorado/res/com/bstek/dorado/sample/data/Phones.js",
			function(result) {
				self.setData(result.getJsonData());
			});
}

var OperationCellRenderer = $extend(dorado.widget.grid.SubControlCellRenderer,
		{
			createSubControl : function(arg) {
				if (arg.data.rowType)
					return null;

				return new dorado.widget.Button({
					toggleable : true,
					onClick : function(self) {
						var entity = arg.data, originPrice, price;
						if (self.get("toggled")) {
							originPrice = entity.get("price");
							price = parseInt(originPrice * 0.8);
							entity.set({
								discount : true,
								price : price,
								originPrice : originPrice
							});
						} else {
							originPrice = entity.get("price");
							price = entity.get("originPrice");
							entity.set({
								discount : false,
								price : price
							});
						}
						dorado.widget.NotifyTipManager.notify(entity
								.get("product")
								+ "的价格已由" + originPrice + "调整为" + price + "!");
					}
				});
			},

			refreshSubControl : function(button, arg) {
				var storage = arg.data.get("storage");
				button.set({
					caption : (storage > 0) ? "折扣" : "无货",
					disabled : !(storage > 0),
					toggled : arg.data.get("discount")
				});
			}
		});

// @Bind #gridPhones.onCreate
!function(self) {
	self.set("&operation.renderer", new OperationCellRenderer());
}

// @Bind #gridPhones.onRenderRow
!function(arg) {
	arg.dom.style.background = (arg.data.get("discount")) ? "#d5e4fc" : "";
}

// @Bind #gridPhones.#operator.onRenderHeaderCell
!function(arg) {
	$(arg.dom).empty();
	var button = new dorado.widget.Button({
		caption : "切换折扣",
		renderTo : arg.dom,
		style : "margin: 0 2px",
		onClick : function() {
			view.get("#dsPhones.data").each(function(entity) {
				if (entity.get("storage") > 0) {
					var discount = entity.get("discount");
					if (discount) {
						originPrice = entity.get("price");
						price = entity.get("originPrice");
						entity.set({
							discount : false,
							price : price,
							originPrice : originPrice
						});
					} else {
						originPrice = entity.get("price");
						price = parseInt(originPrice * 0.8);
						entity.set({
							discount : true,
							price : price,
							originPrice : originPrice
						});
					}
				}
			});
		}
	});
}

// @Bind #gridPhones.#image.onRenderCell
!function(arg) {
	$(arg.dom).empty().xCreate(
			{
				tagName : "IMG",
				src : $url(">dorado/res/com/bstek/dorado/sample/data/images/"
						+ arg.data.get("product") + "-24.png"),
				style : "margin: 2px"
			});

	dorado.TipManager.initTip(arg.dom.parentNode, {
		content : {
			tagName : "IMG",
			src : $url(">dorado/res/com/bstek/dorado/sample/data/images/"
					+ arg.data.get("product") + "-128.png"),
			style : "width: 128px; height: 128px; margin: 8px"
		},
		arrowDirection : "top",
		arrowAlign : "left"
	});
}

// @Bind #gridPhones.#price.onRenderCell
!function(arg) {
	arg.dom.style.background = (arg.data.get("price") > 3000) ? "#fcc5c5" : "";
	arg.processDefault = true;
}

// @Bind #gridPhones.#price.onRenderFooterCell
!function(arg) {
	arg.dom.innerText = "平均："
			+ dorado.util.Common.formatFloat(arg.data.get("price"), "#,##0");
}

// @Bind #gridPhones.#storage.onRenderCell
!function(arg) {
	arg.dom.style.background = (arg.data.get("storage") <= 50) ? "#d3d3d3" : "";
	arg.dom.style.color = (arg.data.get("storage") <= 50) ? "red" : "";
	arg.processDefault = true;
}

// @Bind #gridPhones.#storage.onRenderFooterCell
!function(arg) {
	arg.dom.innerText = "总计：" + arg.data.get("storage");
}

//@Bind #gridPhones.#volumn.onRenderCell
!function(arg) {
	var entity = arg.data;
	arg.dom.innerText = entity.get("length") + " x " + entity.get("width") + " x " + entity.get("height");
}

