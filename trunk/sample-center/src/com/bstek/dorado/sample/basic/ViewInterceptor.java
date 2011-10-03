package com.bstek.dorado.sample.basic;

import com.bstek.dorado.data.listener.GenericObjectListener;
import com.bstek.dorado.view.View;

public class ViewInterceptor extends GenericObjectListener<View> {
	@Override
	public boolean beforeInit(View view) throws Exception {
		// your code
		return false;
	}

	@Override
	public void onInit(View view) throws Exception {
		// your code
	}
}
