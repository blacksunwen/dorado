package com.bstek.dorado.view;


public class MockView extends DefaultView {
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
