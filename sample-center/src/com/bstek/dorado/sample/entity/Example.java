package com.bstek.dorado.sample.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.search.annotations.Boost;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

@Entity
@Table(name = "EXAMPLES")
@Indexed(index = "EXAMPLES")
public class Example implements Serializable {
	private static final long serialVersionUID = 111301216730379521L;

	private long id;
	private String label;
	private boolean isNew;
	private boolean isHot;
	private String author;
	private Date createDate;
	private Date lastModify;
	private String icon;
	private String url;
	private boolean isEmbedded;
	private int embedWidth;
	private int embedHeight;
	private String tags;
	private String summary;
	private Set<ExampleSource> sources;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID")
	@SequenceGenerator(name = "SEQ_ID", sequenceName = "SEQ_ID")
	@DocumentId
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Field(boost = @Boost(value = 2.0F))
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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Column(name = "CREATE_DATE")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "LAST_MODIFY")
	public Date getLastModify() {
		return lastModify;
	}

	public void setLastModify(Date lastModify) {
		this.lastModify = lastModify;
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

	@Column(name = "IS_EMBEDDED")
	public boolean isEmbedded() {
		return isEmbedded;
	}

	public void setEmbedded(boolean isEmbedded) {
		this.isEmbedded = isEmbedded;
	}

	@Column(name = "EMBED_WIDTH")
	public int getEmbedWidth() {
		return embedWidth;
	}

	public void setEmbedWidth(int embedWidth) {
		this.embedWidth = embedWidth;
	}

	@Column(name = "EMBED_HEIGHT")
	public int getEmbedHeight() {
		return embedHeight;
	}

	public void setEmbedHeight(int embedHeight) {
		this.embedHeight = embedHeight;
	}

	@Field
	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	@JoinColumn(name = "EXAMPLE_ID", insertable = false, updatable = false)
	@OrderBy(value = "sortFlag")
	public Set<ExampleSource> getSources() {
		return sources;
	}

	public void setSources(Set<ExampleSource> sources) {
		this.sources = sources;
	}

	@Field(boost = @Boost(value = 0.5F))
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
}
