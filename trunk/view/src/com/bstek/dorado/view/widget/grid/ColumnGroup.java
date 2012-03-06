package com.bstek.dorado.view.widget.grid;

import java.util.List;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.widget.InnerElementList;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-9-28
 */
@ClientObject(shortTypeName = "Group")
public class ColumnGroup extends Column implements ColumnHolder {
	private List<Column> columns = new InnerElementList<Column>(this);

	public void addColumn(Column column) {
		columns.add(column);
	}

	@XmlSubNode
	@ClientProperty
	public List<Column> getColumns() {
		return columns;
	}
}
