package com.bstek.dorado.jdbc.sql;

import java.util.List;

import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Criterion;
import com.bstek.dorado.data.provider.Order;
import com.bstek.dorado.data.provider.filter.FilterOperator;
import com.bstek.dorado.data.provider.filter.SingleValueFilterCriterion;
import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.sql.SqlConstants.KeyWord;
import com.bstek.dorado.jdbc.sql.SqlConstants.OrderDirection;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public abstract class SelectSql extends AbstractSql {
	private Criteria criteria;
	
	private String querySql;
	private String countSql;
	
	private boolean built = false;
	
	public Criteria getCriteria() {
		return criteria;
	}

	public void setCriteria(Criteria criteria) {
		this.criteria = criteria;
	}
	
	public String toCountSQL(Dialect dialect) throws Exception {
		if (!built) {
			this.build(dialect);
		} 
		
		return countSql;
	}
	
	@Override
	protected String toSQL(Dialect dialect) {
		if (!built) {
			try {
				this.build(dialect);
			} catch (Exception e) {
				if (e instanceof RuntimeException) {
					throw (RuntimeException)e;
				} else {
					throw new RuntimeException(e);
				}
			}
		}
		
		return querySql;
	}
	
	private void build(Dialect dialect) throws Exception {
		String sql = doBuild(dialect);
		
		Criteria criteria = this.getCriteria();
		if (criteria != null) {
			buildFilterSql(sql, criteria, getParameterSource(), dialect);
		} else {
			this.querySql = sql;
			this.countSql = dialect.toCountSQL(sql);
		}
		
		built = true;
	}

	abstract protected String doBuild(Dialect dialect) throws Exception;
	
	private void buildFilterSql(String sql, Criteria criteria, JdbcParameterSource parameterSource, Dialect dialect) throws Exception {
		SqlBuilder builder = new SqlBuilder(sql.length() + 50);
		builder.rightSpace(KeyWord.SELECT, "*", KeyWord.FROM, "(", sql, ")");
		
		//构建Criterion部分的SQL
		List<Criterion> criterionList = criteria.getCriterions();
		int criterionIndex = 0;
		if (!criterionList.isEmpty()) {
			for (Criterion c: criterionList) {
				if (criterionIndex > 0) {
					builder.rightSpace(KeyWord.AND);
				} else {
					builder.rightSpace(KeyWord.WHERE);
				}
				
				if (c instanceof SingleValueFilterCriterion) {
					SingleValueFilterCriterion valueCriterion = (SingleValueFilterCriterion)c;
					FilterOperator operator = valueCriterion.getFilterOperator();
					String property = valueCriterion.getProperty();
					Object value = valueCriterion.getValue();
					
					if (value instanceof String) {
						if (operator == null) {
							operator = FilterOperator.like;
						}
						String valueStr = (String)value;
						if (FilterOperator.like.equals(operator)) {
							if (valueStr.indexOf('%') != 0 && valueStr.lastIndexOf('%') != valueStr.length()-1) {
								valueStr = "%" + valueStr + "%";
							}
						}
						
						String varName = parameterSource.addValue(valueStr);
						builder.rightSpace(property, operator.toString(), ":" + varName);
					} else {
						if (operator == null) {
							operator = FilterOperator.eq;
						}
						
						if (FilterOperator.between.equals(operator)) {
							Object[] values = (Object[])value;
							Object value1 = values[0];
							Object value2 = values[1];
							
							String varName1 = parameterSource.addValue(value1);
							String varName2 = parameterSource.addValue(value2);
							
							builder.rightSpace(property, "between", ":" + varName1, "and", ":" + varName2);
						} else if (FilterOperator.in.equals(operator)) {
							Object[] values = (Object[])value;
							builder.rightSpace(property, "in", "(");
							for (int i=0; i<values.length; i++) {
								if (i > 0) {
									builder.rightSpace(",");
								}
								String varName = parameterSource.addValue(values[i]);
								builder.rightSpace(":" + varName);
							}
							builder.rightSpace(")");
						} else {
							String varName = parameterSource.addValue(value);
							builder.rightSpace(property, operator.toString(), ":" + varName);
						}
					}
				} else {
					throw new IllegalArgumentException("Unknown type: " + c.getClass());
				}
				
				criterionIndex++;
			}
		}
		
		//设置countSql
		String querySql = sql;
		if (criterionIndex > 0) {
			querySql = builder.build();
		}
		this.countSql = dialect.toCountSQL(querySql);
		
		//构建Order部分的SQL
		List<Order> orderList = criteria.getOrders();
		int orderIndex = 0;
		if (!orderList.isEmpty()) {
			for (Order order: orderList) {
				String property = order.getProperty();
				if (orderIndex > 0) {
					builder.rightSpace(",");
				} else {
					builder.rightSpace(KeyWord.ORDER_BY);
				}
				
				builder.rightSpace(property, (order.isDesc()) ? OrderDirection.DESC.name(): OrderDirection.ASC.name());
				orderIndex++;
			}
		}
		
		//设置querySql
		if (criterionIndex > 0 || orderIndex > 0) {
			this.querySql = builder.build();
		} else {
			this.querySql = sql;
		}
	}
}
