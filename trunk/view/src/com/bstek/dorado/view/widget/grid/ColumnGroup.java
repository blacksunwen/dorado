package com.bstek.dorado.view.widget.grid;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlSubNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-9-28
 */
@ViewObject(shortTypeName = "Group")
@XmlNode(nodeName = "ColumnGroup")
public class ColumnGroup extends Column implements ColumnHolder {
	private List<Column> columns = new ArrayList<Column>();

	public void addColumn(Column column) {
		columns.add(column);
	}

	@ViewAttribute
	@XmlSubNode(path = "#self", parser = "dorado.Grid.ColumnsParser")
	public List<Column> getColumns() {
		return columns;
	}
}
