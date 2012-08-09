(function() {

	/**
	 * @author Benny Bao (mailto:benny.bao@bstek.com)
	 * @component Form
	 * @class 多行文本编辑器。
	 * @extends dorado.widget.AbstractTextEditor
	 */
	dorado.widget.TextArea = $extend(dorado.widget.AbstractTextEditor, /** @scope dorado.widget.TextArea.prototype */ {
		$className: "dorado.widget.TextArea",
		_inherentClassName: "i-text-area",
		
		ATTRIBUTES: /** @scope dorado.widget.TextArea.prototype */ {
			width: {
				independent: false,
				defaultValue: 150
			},
			
			height: {
				independent: false,
				defaultValue: 60
			},
			
			className: {
				defaultValue: "d-text-area"
			}
		},
		
		createTextDom: function() {
			var textDom = document.createElement("TEXTAREA");
			textDom.className = "textarea";
			if (dorado.Browser.msie && dorado.Browser.version < 8) {
				this.doOnAttachToDocument = this.doOnResize;
			}
			return textDom;
		},
		
		refreshTriggerDoms: function() {
			var triggerButtons = this._triggerButtons, triggerButton, triggerPanel = this._triggerPanel;
			if (triggerButtons && triggerPanel) {
				for (var i = 0; i < triggerButtons.length; i++) {
					triggerButton = triggerButtons[i];
					triggerButton.unrender();
					this.unregisterInnerControl(triggerButton);
				}
			}
			
			var triggers = this.get("trigger");
			if (triggers) {
				if (!triggerPanel) {
					triggerPanel = this._triggerPanel = $create("DIV");
					triggerPanel.className = "i-trigger-panel d-trigger-panel";
					this._dom.appendChild(triggerPanel);
				}
				
				if (!(triggers instanceof Array)) triggers = [triggers];
				var trigger;
				this._triggerButtons = triggerButtons = [];
				for (var i = triggers.length - 1; i >= 0; i--) {
					trigger = triggers[i];
					triggerButton = trigger.createTriggerButton(this);
					triggerButton.set("style", {
						position: "absolute"
					});
					triggerButtons.push(triggerButton);
					this.registerInnerControl(triggerButton);
					triggerButton.render(triggerPanel);
				}
				this._triggersArranged = false;
				this.doOnResize = this.resizeTextDom;
				this.resizeTextDom();
			} else {
				if (this._triggerPanel) this._triggerPanel.style.display = "none";
				this._textDom.style.width = "100%";
				delete this.doOnResize;
			}
		},
		
		resizeTextDom: function() {
			if (!this._attached) return;
			
			if (!this._triggersArranged) {
				this._triggersArranged = true;
				var triggerButtons = this._triggerButtons;
				if (triggerButtons) {
					var bottom = 0;
					for (var i = 0; i < triggerButtons.length; i++) {
						var triggerButton = triggerButtons[i], buttonDom = triggerButton.getDom();
						buttonDom.style.bottom = bottom + "px";
						bottom += buttonDom.offsetWidth;
					}
				}
			}
			var w = this._dom.clientWidth, h = this._dom.clientHeight;
			if (this._triggerPanel) {
				w -= this._triggerPanel.offsetWidth;
			}
			this._textDom.style.width = (w < 0 ? 0 : w) + "px";
			this._textDom.style.height = h + "px";
		},
		
		doOnKeyDown: function(evt) {
			if (evt.ctrlKey) return true;
			if (evt.keyCode == 13) return;
			return $invokeSuper.call(this, arguments);
		},
		
		doOnFocus: function() {
			if (this._useBlankText) this.doSetText('');
			$invokeSuper.call(this, arguments);
		},
		
		doOnBlur: function() {
			if (this.get("readOnly")) return;
			try {
				$invokeSuper.call(this, arguments);
			}
			finally {
				this.doSetText(this.doGetText());
			}
		}
	});
	
})();
