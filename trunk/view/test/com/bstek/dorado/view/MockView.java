package com.bstek.dorado.view;

import com.bstek.dorado.view.View;

public class MockView extends View {
	private String title;

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}
}
