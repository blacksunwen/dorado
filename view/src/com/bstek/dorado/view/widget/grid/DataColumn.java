package com.bstek.dorado.view.widget.grid;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.view.widget.Align;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-9-28
 */
@XmlNode(nodeName = "DataColumn")
@ClientEvents({ @ClientEvent(name = "onRenderCell"),
		@ClientEvent(name = "onRenderFooterCell") })
public class DataColumn extends Column {
	private String property;
	private DataType dataType;
	private String width;
	private Align align = Align.left;
	private String renderer;
	private String footerRenderer;
	private boolean readOnly;
	private boolean required;
	private String displayFormat;
	private String typeFormat;
	private String dropDown;
	private String summaryType;
	private String summaryRenderer;
	private String editor;
	private String trigger;
	private SortState sortState = SortState.none;
	private boolean wrappable;
	private boolean filterable = true;

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	@XmlProperty(parser = "dorado.dataTypePropertyParser")
	@ViewAttribute(outputter = "dorado.dataTypePropertyOutputter")
	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public String getRenderer() {
		return renderer;
	}

	public void setRenderer(String renderer) {
		this.renderer = renderer;
	}

	public String getFooterRenderer() {
		return footerRenderer;
	}

	public void setFooterRenderer(String footerRenderer) {
		this.footerRenderer = footerRenderer;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	@Override
	@ViewAttribute(defaultValue = "left")
	public Align getAlign() {
		return align;
	}

	@Override
	public void setAlign(Align align) {
		this.align = align;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getDisplayFormat() {
		return displayFormat;
	}

	public void setDisplayFormat(String displayFormat) {
		this.displayFormat = displayFormat;
	}

	public String getTypeFormat() {
		return typeFormat;
	}

	public void setTypeFormat(String typeFormat) {
		this.typeFormat = typeFormat;
	}

	public String getDropDown() {
		return dropDown;
	}

	public void setDropDown(String dropDown) {
		this.dropDown = dropDown;
	}

	@ViewAttribute(enumValues = "sum,average,count,max,min")
	public String getSummaryType() {
		return summaryType;
	}

	public void setSummaryType(String summaryType) {
		this.summaryType = summaryType;
	}

	public String getSummaryRenderer() {
		return summaryRenderer;
	}

	public void setSummaryRenderer(String summaryRenderer) {
		this.summaryRenderer = summaryRenderer;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	@ViewAttribute(referenceComponentName = "Trigger", enumValues = "triggerClear,autoMappingDropDown1,autoMappingDropDown2,defaultDateDropDown")
	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}

	@ViewAttribute(defaultValue = "none")
	public SortState getSortState() {
		return sortState;
	}

	public void setSortState(SortState sortState) {
		this.sortState = sortState;
	}

	public boolean isWrappable() {
		return wrappable;
	}

	public void setWrappable(boolean wrappable) {
		this.wrappable = wrappable;
	}

	@ViewAttribute(defaultValue = "true")
	public boolean isFilterable() {
		return filterable;
	}

	public void setFilterable(boolean filterable) {
		this.filterable = filterable;
	}
}
