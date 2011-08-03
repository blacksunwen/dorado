package com.bstek.dorado.hibernate.entity;

import java.io.Serializable;

public class CategoryExampleRelationPK implements Serializable {
	private static final long serialVersionUID = -8873070611205629743L;

	private long categoryId;
	private long exampleId;

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public long getExampleId() {
		return exampleId;
	}

	public void setExampleId(long exampleId) {
		this.exampleId = exampleId;
	}
}
