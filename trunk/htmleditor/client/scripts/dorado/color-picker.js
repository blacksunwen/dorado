/**
 * @author frank.zhang
 */
dorado.widget.ColorPicker = $extend([dorado.widget.Control, dorado.widget.FloatControl], {
	focusable: true,
	ATTRIBUTES: {
		className: {
			defaultValue: "d-color-picker"
		},
		animateType: {
			defaultValue: "slide"
		},
		focusAfterShow: {
			defaultValue: true
		},
		mode: {
			defaultValue: "simple",
			setter: function(value) {
				var picker = this, dom = picker._dom;
				if (dom) {
					$fly(dom)[value == "more" ? "addClass" : "removeClass"](picker._className + "-more");
				}
				picker._mode = value;
			}
		},
		hideAfterSelect: {
			defaultValue: true
		},
		hideAfterClear: {
			defaultValue: true
		}
	},
	EVENTS: {
		onSelect: {},
		onClear: {}
	},
	createDom: function() {
		var picker = this, doms = {}, dom = $DomUtils.xCreate({
			tagName: "div",
			className: picker._className,
			content: [{
				tagName: "div",
				className: "clear-color",
				contextKey: "clearColor",
				content:  "去除颜色"
			}, {
				tagName: "div",
				className: "color-preview",
				contextKey: "colorPreview"
			}, {
				tagName: "table",
				className: "simple-color-table",
				contextKey: "simpleColorTable",
				cellSpacing: 0,
				cellPadding: 0,
				content: [{
					tagName: "tbody",
					contextKey: "simpleColorTableBody"
				}]
			}, {
				tagName: "div",
				className: "more-color",
				contextKey: "moreColor",
				content: "更多颜色..."
			}, {
				tagName: "table",
				className: "more-color-table",
				contextKey: "moreColorTable",
				cellSpacing: 0,
				cellPadding: 0,
				content: [{
					tagName: "tbody",
					contextKey: "moreColorTableBody"
				}]
			}]
		}, null, doms);

		jQuery(doms.clearColor).click(function() {
			picker.fireEvent("onClear", picker, {});
			if (picker._hideAfterClear) {
				picker.hide();
			}
		}).addClassOnHover("clear-color-over");

		jQuery(doms.moreColor).click(function() {
			picker.set("mode", "more");
		}).addClassOnHover("more-color-over");

		var simpleColor = [
			["#000000","#993300","#333300","#003300","#003366","#000080","#333399","#333333"],
			["#800000","#FF6600","#808000","#008000","#008080","#0000FF","#666699","#808080"],
			["#FF0000","#FF9900","#99CC00","#339966","#33CCCC","#3366FF","#800080","#999999"],
			["#FF00FF","#FFCC00","#FFFF00","#00FF00","#00FFFF","#00CCFF","#993366","#C0C0C0"],
			["#FF99CC","#FFCC99","#FFFF99","#CCFFCC","#CCFFFF","#99CCFF","#CC99FF","#FFFFFF"]
		], i, j, tr, td;

		var simpleColorTableBody = doms.simpleColorTableBody;
		for(i = 0;i < 5;i++){
			tr = document.createElement("tr");
			for(j = 0;j < 8;j++){
				td = document.createElement("td");
				td.bgColor = simpleColor[i][j];
				td.innerHTML = "&nbsp;";
				tr.appendChild(td);
			}
			simpleColorTableBody.appendChild(tr);
		}

		function getColor(row,column){
			var colorArray = ["00","33","66","99","CC","FF"];
			var result = "#";
			//red color
			result += colorArray[row > 5 ? Math.floor(column / 6) + 3 : Math.floor(column / 6)];
			//green color
			result += colorArray[column % 6 == 6 ? 0 : column % 6];
			//blue color
			result += colorArray[row <= 5 ? row : row - 6];
			return result;
		}

		var moreColorTableBody = doms.moreColorTableBody;
		for(i = 0; i < 12; i++){
			tr = document.createElement("tr");
			for(j = 0; j < 18; j++){
				td = document.createElement("td");
				td.bgColor = getColor(i,j);
				td.innerHTML = "&nbsp;";
				tr.appendChild(td);
			}
			moreColorTableBody.appendChild(tr);
		}

		var colorTableMouseOver = function(event){
			var position = $DomUtils.getCellPosition(event) || {}, row = position.row,
				column = position.column, element = position.element;
			if (row != -1 && column != -1 && element) {
				doms.colorPreview.style.backgroundColor = element.bgColor;
			}
		};

		var colorTableClick = function(event){
			var position = $DomUtils.getCellPosition(event) || {}, row = position.row,
				column = position.column, element = position.element;

			if (row != -1 && column != -1 && element) {
				picker.fireEvent("onSelect", picker, { color: element.bgColor });
				if (picker._hideAfterSelect) {
					picker.hide();
				}
			}
		};

		$fly(doms.simpleColorTable).mouseover(colorTableMouseOver).click(colorTableClick);
		$fly(doms.moreColorTable).mouseover(colorTableMouseOver).click(colorTableClick);

		$fly(dom)[picker._mode == "more" ? "addClass" : "removeClass"](picker._className + "-more");

		return dom;
	},
	onBlur: function() {
		if (this._visible) {
			this.hide();
		}
	}
});