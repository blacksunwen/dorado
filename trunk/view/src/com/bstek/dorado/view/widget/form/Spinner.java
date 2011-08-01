package com.bstek.dorado.view.widget.form;

import com.bstek.dorado.annotation.ViewAttribute;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-9-24
 */
public abstract class Spinner extends AbstractTextBox {
	private int step = 1;
	private boolean showSpinTrigger = true;

	@ViewAttribute(defaultValue = "1")
	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	@ViewAttribute(defaultValue = "true")
	public boolean isShowSpinTrigger() {
		return showSpinTrigger;
	}

	public void setShowSpinTrigger(boolean showSpinTrigger) {
		this.showSpinTrigger = showSpinTrigger;
	}
}
