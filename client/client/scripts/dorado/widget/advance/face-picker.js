/**
 * @author frank.zhang
 */
dorado.widget.FacePicker = $extend([dorado.widget.Control, dorado.widget.FloatControl], {
	focusable: true,
	ATTRIBUTES: {
		className: {
			defaultValue: "d-face-picker"
		},
		animateType: {
			defaultValue: "slide"
		},
		hideAfterSelect: {
			defaultValue: true
		}
	},
	EVENTS: {
		onSelect: {}
	},
	createDom: function() {
		var picker = this, doms = {}, dom = $DomUtils.xCreateElement({
			tagName: "div",
			className: picker._className,
			content: [{
				tagName: "table",
				contextKey: "faceTable",
				border: 0,
				cellSpacing: 0,
				cellPadding: 0,
				content: [{
					tagName: "tbody",
					contextKey: "faceTableBody"
				}]
			}, {
				tagName: "div",
				className: "face-preview",
				contextKey: "facePreview",
				style: {
					display: "none"
				},
				content: [{
					tagName: "img"
				}]
			}]
		}, null, doms);

		picker._doms = doms;

		var faceTableBody = doms.faceTableBody;
		for(var i = 0; i < 7; i++){
			var tr = document.createElement("tr");
			for(var j = 0; j < 15; j++){
				var index = i * 15 + j, source = ">skin>/advance/emotion/" + index + ".gif";
				var td = document.createElement("td");
				td.className = "face-cell";
				td.source = $url(source);

				tr.appendChild(td);
			}
			faceTableBody.appendChild(tr);
		}

		$fly(doms.faceTable).mouseover(function(event) {
			var position = $DomUtils.getCellPosition(event) || {}, element = position.element;

			if (element) {
				if (picker.lastElement) {
					$fly(picker.lastElement).removeClass("cur-face-cell");
				}
				$fly(element).addClass("cur-face-cell");
				picker.lastElement = element;
				doms.facePreview.style.display = "";
				doms.facePreview.firstChild.src = element.source;
			}
		}).mouseout(function() {
			doms.facePreview.style.display = "none";
		}).click(function(event) {
			var position = $DomUtils.getCellPosition(event) || {}, element = position.element;
			var source = element.source;
			if (source) {
				picker.fireEvent("onSelect", picker, {image: source});
				if (picker._hideAfterSelect) {
					picker.hide();
				}
			}
		});

		$fly(doms.facePreview).mouseover(function(){
			if (this.style.right == "auto") {
				this.style.right = 0;
				this.style.left = "auto";
			} else {
				this.style.left = 0;
				this.style.right = "auto";
			}
		});

		return dom;
	},
	onBlur: function() {
		if (this._visible) {
			this.hide();
		}
	}
});