package com.bstek.dorado.sample.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

@Entity
@Table(name = "EXAMPLE_SOURCES")
public class ExampleSource implements Serializable {
	private static final long serialVersionUID = 3334714652067229216L;

	private long id;
	private String path;
	private String fileType;
	private String label;
	private int sortFlag;
	private String summary;
	private Example example;
	private Long exampleId;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID")
	@SequenceGenerator(name = "SEQ_ID", sequenceName = "SEQ_ID")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
		if (StringUtils.isNotEmpty(path)) {
			fileType = StringUtils.substringAfterLast(path, ".");
			if ("xml".equals(fileType)) {
				if (path.endsWith(".view.xml")) {
					fileType = "view";
				} else if (path.endsWith(".model.xml")) {
					fileType = "model";
				}
			}
		}
	}

	@Transient
	public String getFileType() {
		return fileType;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Column(name = "SORT_FLAG")
	public int getSortFlag() {
		return sortFlag;
	}

	public void setSortFlag(int sortFlag) {
		this.sortFlag = sortFlag;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EXAMPLE_ID", insertable = false, updatable = false)
	public Example getExample() {
		return example;
	}

	public void setExample(Example example) {
		this.example = example;
	}

	@Column(name = "EXAMPLE_ID")
	public Long getExampleId() {
		return exampleId;
	}

	public void setExampleId(Long exampleId) {
		this.exampleId = exampleId;
	}
}
