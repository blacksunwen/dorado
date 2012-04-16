package com.bstek.dorado.hibernate.criteria.criterion;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlNodeWrapper;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.hibernate.criteria.HibernateCriteriaTransformer;

public class SqlCriterion extends BaseCriterion {

	private String clause;
	private List<Parameter> parameters = new ArrayList<Parameter>();

	public String getClause() {
		return clause;
	}

	public void setClause(String clause) {
		this.clause = clause;
	}

	@XmlSubNode(wrapper= @XmlNodeWrapper(nodeName="Parameters"))
	public List<Parameter> getParameters() {
		return parameters;
	}

	public void addParameter(Parameter parameter) {
		parameters.add(parameter);
	}

	@XmlNode
	public static class Parameter {
		private String hibernateType;
		private String dataType;
		private Object value;

		@IdeProperty(enumValues="integer,long,short,float,double,character,byte,boolean,yes_no,true_false,string,date,time,timestamp,calendar,calendar_date,big_decimal,big_integer")
		public String getHibernateType() {
			return hibernateType;
		}

		public void setHibernateType(String hibernateType) {
			this.hibernateType = hibernateType;
		}

		public String getDataType() {
			return dataType;
		}

		public void setDataType(String dataType) {
			this.dataType = dataType;
		}

		@XmlProperty
		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Criterion toHibernate(Object parameter,
			SessionFactory sessionFactory,
			HibernateCriteriaTransformer transformer) throws Exception {
		String clause = this.getClause();
		List<SqlCriterion.Parameter> parameters = this.getParameters();

		if (parameters.size() == 0) {
			return Restrictions.sqlRestriction(clause);
		}

		List values = new ArrayList(parameters.size());
		List<org.hibernate.type.Type> types = new ArrayList<org.hibernate.type.Type>(
				parameters.size());
		for (SqlCriterion.Parameter param : parameters) {
			String dataType = param.getDataType();
			String hibernateTypeName = param.getHibernateType();
			Object v1 = param.getValue();
			Object value1 = transformer.getValueFromParameter(parameter,
					dataType, v1);
			org.hibernate.type.Type type = sessionFactory.getTypeHelper()
					.basic(hibernateTypeName);

			values.add(value1);
			types.add(type);
		}

		Object[] valueArray = values.toArray(new Object[parameters.size()]);
		org.hibernate.type.Type[] typeArray = types
				.toArray(new org.hibernate.type.Type[parameters.size()]);

		return Restrictions.sqlRestriction(clause, valueArray, typeArray);
	}
}
