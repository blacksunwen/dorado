/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @component General
 * @class 用于容纳一个子视图的控件。
 * @extends dorado.widget.Control
 */
dorado.widget.SubViewHolder = $extend(dorado.widget.Control, /** @scope dorado.widget.SubViewHolder.prototype */ {
	$className: "dorado.widget.SubViewHolder",
	
	ATTRIBUTES: /** @scope dorado.widget.SubViewHolder.prototype */ {
	
		/**
		 * 子视图。
		 * @attribute writeBeforeReady
		 * @type dorado.widget.View
		 */
		subView: {
			writeBeforeReady: true
		}
	},
	
	createDom: function(dom) {
		var dom = document.createElement("DIV");
		var subView = this._subView;
		if (subView) {
			this.registerInnerControl(subView);
			subView.render(dom);
		}
		return dom;
	},
	
	doOnResize: function() {
		var subView = this._subView;
		if (subView) {
			subView._realWidth = this._dom.offsetWidth;
			subView._realHeight = this._dom.offsetHeight;
			subView.resetDimension();
		}
	}

});
