package com.bstek.dorado.hibernate.criteria.projection;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.TypeHelper;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;

import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlNodeWrapper;
import com.bstek.dorado.annotation.XmlSubNode;

public class SqlProjection extends BaseProjection {

	private String clause;
	private String groupBy;
	private List<Column> columns = new ArrayList<Column>();

	public String getClause() {
		return clause;
	}

	public void setClause(String clause) {
		this.clause = clause;
	}

	public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	@XmlSubNode(wrapper= @XmlNodeWrapper(nodeName="Columns"))
	public List<Column> getColumns() {
		return columns;
	}

	public void addColumn(Column col) {
		columns.add(col);
	}

	@XmlNode
	public static class Column {
		private String columanAlias;
		private String hibernateType;

		public String getColumanAlias() {
			return columanAlias;
		}

		public void setColumanAlias(String alias) {
			this.columanAlias = alias;
		}

		@IdeProperty(enumValues="integer,long,short,float,double,character,byte,boolean,yes_no,true_false,string,date,time,timestamp,calendar,calendar_date,big_decimal,big_integer")
		public String getHibernateType() {
			return hibernateType;
		}

		public void setHibernateType(String hibernateType) {
			this.hibernateType = hibernateType;
		}
	}

	@Override
	public Projection toHibernate(SessionFactory sessionFactory) {
		String clause = this.getClause();
		String groupBy = this.getGroupBy();

		List<SqlProjection.Column> columns = this.getColumns();
		List<String> aliases = new ArrayList<String>(columns.size());
		List<org.hibernate.type.Type> types = new ArrayList<org.hibernate.type.Type>(
				columns.size());

		TypeHelper typeHelper = sessionFactory.getTypeHelper();
		for (SqlProjection.Column column : columns) {
			String alias = column.getColumanAlias();
			String typeName = column.getHibernateType();
			org.hibernate.type.Type type = typeHelper.basic(typeName);
			aliases.add(alias);
			types.add(type);
		}

		String[] aliasArray = aliases.toArray(new String[aliases.size()]);
		org.hibernate.type.Type[] typeArray = types
				.toArray(new org.hibernate.type.Type[types.size()]);

		if (StringUtils.isEmpty(groupBy)) {
			return Projections.sqlProjection(clause, aliasArray, typeArray);
		} else {
			return Projections.sqlGroupProjection(clause, groupBy, aliasArray,
					typeArray);
		}
	}
}
