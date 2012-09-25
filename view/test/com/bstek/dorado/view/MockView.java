package com.bstek.dorado.view;

import com.bstek.dorado.view.manager.ViewConfig;

public class MockView extends DefaultView {

	public MockView(ViewConfig viewConfig) {
		super(viewConfig);
	}

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
