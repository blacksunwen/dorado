package com.bstek.dorado.sample;

public class SearchResult {
	private long id;
	private String label;
	private boolean isCategory;
	private boolean isNew;
	private boolean isHot;
	private String icon;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isCategory() {
		return isCategory;
	}

	public void setCategory(boolean isCategory) {
		this.isCategory = isCategory;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public boolean isHot() {
		return isHot;
	}

	public void setHot(boolean isHot) {
		this.isHot = isHot;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}
