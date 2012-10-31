/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
package com.bstek.dorado.view.widget.form;

import com.bstek.dorado.annotation.ClientProperty;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-9-24
 */
public abstract class Spinner extends AbstractTextBox {
	private int step = 1;
	private boolean showSpinTrigger = true;
	private boolean postValueOnSpin = true;

	@ClientProperty(escapeValue = "1")
	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	@ClientProperty(escapeValue = "true")
	public boolean isShowSpinTrigger() {
		return showSpinTrigger;
	}

	public void setShowSpinTrigger(boolean showSpinTrigger) {
		this.showSpinTrigger = showSpinTrigger;
	}

	@ClientProperty(escapeValue = "true")
	public boolean isPostValueOnSpin() {
		return postValueOnSpin;
	}

	public void setPostValueOnSpin(boolean postValueOnSpin) {
		this.postValueOnSpin = postValueOnSpin;
	}
}
