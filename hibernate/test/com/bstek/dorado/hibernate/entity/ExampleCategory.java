package com.bstek.dorado.hibernate.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "EXAMPLE_CATEGORIES")
public class ExampleCategory implements Serializable {
	private static final long serialVersionUID = -3446596720169668211L;

	private long id;
	private ExampleCategory category;
	private Long categoryId;
	private String label;
	private boolean isNew;
	private boolean isHot;
	private String icon;
	private String url;
	private String tags;
	private int sortFlag;
	private String summary;
	private Collection<ExampleCategory> categories;
	private Collection<CategoryExampleRelation> exampleRelations;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID")
	@SequenceGenerator(name = "SEQ_ID", sequenceName = "SEQ_ID")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CATEGORY_ID", insertable = false, updatable = false)
	public ExampleCategory getCategory() {
		return category;
	}

	public void setCategory(ExampleCategory category) {
		this.category = category;
	}

	@Column(name = "CATEGORY_ID")
	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Column(name = "IS_NEW")
	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	@Column(name = "IS_HOT")
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	@Column(name = "SORT_FLAG")
	public int getSortFlag() {
		return sortFlag;
	}

	public void setSortFlag(int sortFlag) {
		this.sortFlag = sortFlag;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "CATEGORY_ID", insertable = false, updatable = false)
	@OrderBy(value = "sortFlag")
	public Collection<ExampleCategory> getCategories() {
		return categories;
	}

	public void setCategories(Collection<ExampleCategory> categories) {
		this.categories = categories;
	}

	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	@JoinColumn(name = "CATEGORY_ID", insertable = false, updatable = false)
	@OrderBy(value = "sortFlag")
	public Collection<CategoryExampleRelation> getExampleRelations() {
		return exampleRelations;
	}

	public void setExampleRelations(
			Collection<CategoryExampleRelation> exampleRelations) {
		this.exampleRelations = exampleRelations;
	}
}
