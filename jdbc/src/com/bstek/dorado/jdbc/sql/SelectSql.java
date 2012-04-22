package com.bstek.dorado.jdbc.sql;

import java.util.List;

import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Criterion;
import com.bstek.dorado.data.provider.Order;
import com.bstek.dorado.data.provider.filter.FilterOperator;
import com.bstek.dorado.data.provider.filter.SimpleFilterCriterion;
import com.bstek.dorado.data.provider.filter.SingleValueFilterCriterion;
import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.JdbcParameterSource;
import com.bstek.dorado.jdbc.sql.SqlConstants.KeyWord;
import com.bstek.dorado.jdbc.sql.SqlConstants.Operator;
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
	public String toSQL(Dialect dialect) throws Exception {
		if (!built) {
			this.build(dialect);
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
				
				if (c instanceof SimpleFilterCriterion) {
					SimpleFilterCriterion simpleCriterion = (SimpleFilterCriterion)c;
					String property = simpleCriterion.getProperty();
					Object value = simpleCriterion.getValue();
					
					if (value instanceof String) {
						Operator operator = Operator.like_anywhere;
						String varName = parameterSource.addValue(operator.parameterValue(value));
						builder.rightSpace(property, operator.toSQL(), ":" + varName);
					} else {
						Operator operator = Operator.eq;
						String varName = parameterSource.addValue(value);
						builder.rightSpace(property, operator.toSQL(), ":" + varName);
					}
				} else if (c instanceof SingleValueFilterCriterion) {
					SingleValueFilterCriterion valueCriterion = (SingleValueFilterCriterion)c;
					FilterOperator operator = valueCriterion.getFilterOperator();
					String property = valueCriterion.getProperty();
					Object value = valueCriterion.getValue();
					
					if (value instanceof String) {
						if (operator != null) {
							operator = FilterOperator.like;
						}
						String varName = parameterSource.addValue("%" + value + "%");
						builder.rightSpace(property, operator.toString(), ":" + varName);
					} else {
						if (operator != null) {
							operator = FilterOperator.eq;
						}
						String varName = parameterSource.addValue(value);
						builder.rightSpace(property, operator.toString(), ":" + varName);
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
			querySql = builder.toString();
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
