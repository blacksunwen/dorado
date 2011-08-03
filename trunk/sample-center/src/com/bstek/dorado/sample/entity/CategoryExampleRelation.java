package com.bstek.dorado.sample.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CATEGORY_EXAMPLE_RELATIONS")
@IdClass(CategoryExampleRelationPK.class)
public class CategoryExampleRelation implements Serializable {
	private static final long serialVersionUID = -2213866134042328554L;

	private long categoryId;
	private long exampleId;
	private ExampleCategory category;
	private Example example;
	private int sortFlag;

	@Id
	@Column(name = "CATEGORY_ID")
	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	@Id
	@Column(name = "EXAMPLE_ID")
	public long getExampleId() {
		return exampleId;
	}

	public void setExampleId(long exampleId) {
		this.exampleId = exampleId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CATEGORY_ID", insertable = false, updatable = false)
	public ExampleCategory getCategory() {
		return category;
	}

	public void setCategory(ExampleCategory category) {
		this.category = category;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EXAMPLE_ID", insertable = false, updatable = false)
	public Example getExample() {
		return example;
	}

	public void setExample(Example example) {
		this.example = example;
	}

	@Column(name = "SORT_FLAG")
	public int getSortFlag() {
		return sortFlag;
	}

	public void setSortFlag(int sortFlag) {
		this.sortFlag = sortFlag;
	}

}
