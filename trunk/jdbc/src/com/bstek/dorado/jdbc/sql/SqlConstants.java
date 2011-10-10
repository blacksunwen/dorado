package com.bstek.dorado.jdbc.sql;

import org.apache.commons.lang.StringUtils;

public class SqlConstants {

	public static class KeyWord {
		public static final String SELECT = "SELECT";
		public static final String FROM = "FROM";
		public static final String AS = "AS";
		public static final String ON = "ON";
		public static final String AND = "AND";
		public static final String WHERE = "WHERE";
		public static final String ORDER_BY = "ORDER BY";
		public static final String NOT = "NOT";
	}
	
	public static class RightSpace {
		public static final String SELECT = SqlUtils.rightSpace(KeyWord.SELECT);
		public static final String NOT = SqlUtils.rightSpace(KeyWord.NOT);
	}
	
	public static class BothSpace {
		public static final String AS = SqlUtils.bothSpace(KeyWord.AS);
		public static final String ON = SqlUtils.bothSpace(KeyWord.ON);
		public static final String AND = SqlUtils.bothSpace(KeyWord.AND);
		public static final String FROM = SqlUtils.bothSpace(KeyWord.FROM);
		public static final String WHERE = SqlUtils.bothSpace(KeyWord.WHERE);
		public static final String ORDER_BY = SqlUtils.bothSpace(KeyWord.ORDER_BY);
		public static final String NOT = SqlUtils.bothSpace(KeyWord.NOT);
	}
	
	public enum Operator {
		eq {
			@Override
			public String toSQL(){
				return "=";
			}
			
			@Override
			public String notSQL() {
				return ne.toSQL();
			}
		},ne{
			@Override
			public String toSQL(){
				return "<>";
			}

			@Override
			public String notSQL() {
				return eq.toSQL();
			}
		},gt{
			@Override
			public String toSQL(){
				return ">";
			}

			@Override
			public String notSQL() {
				return le.toSQL();
			}
		},lt{
			@Override
			public String toSQL(){
				return "<";
			}

			@Override
			public String notSQL() {
				return ge.toSQL();
			}
		},le{
			@Override
			public String toSQL(){
				return "<=";
			}

			@Override
			public String notSQL() {
				return gt.toSQL();
			}
		},ge{
			@Override
			public String toSQL(){
				return ">=";
			}

			@Override
			public String notSQL() {
				return lt.toSQL();
			}
		},in{
			@Override
			public String toSQL(){
				return "IN";
			}

			@Override
			public String notSQL() {
				return RightSpace.NOT + this.toSQL();
			}
		},isnull {
			@Override
			public String toSQL(){
				return "IS NULL";
			}

			@Override
			public String notSQL() {
				return "IS" + BothSpace.NOT + "NULL";
			}
		},like{
			@Override
			public String toSQL(){
				return "LIKE";
			}

			@Override
			public String notSQL() {
				return RightSpace.NOT + this.toSQL();
			}
		},like_start{
			public String toString(){
				return "like%";
			}
			
			@Override
			public String toSQL(){
				return "LIKE";
			}
			
			@Override
			public String notSQL() {
				return RightSpace.NOT + this.toSQL();
			}
			
			@Override
			public Object parameterValue(Object value) {
				return String.valueOf(value) + "%";
			}
		},like_end{
			public String toString(){
				return "%like";
			}
			
			@Override
			public String toSQL(){
				return "LIKE";
			}
			
			@Override
			public String notSQL() {
				return RightSpace.NOT + this.toSQL();
			}
			
			@Override
			public Object parameterValue(Object value) {
				return "%"+ String.valueOf(value);
			}
		},like_anywhere{
			public String toString(){
				return "%like%";
			}
			
			@Override
			public String toSQL(){
				return "LIKE";
			}
			
			@Override
			public String notSQL() {
				return RightSpace.NOT + this.toSQL();
			}
			
			@Override
			public Object parameterValue(Object value) {
				return "%" + String.valueOf(value) + "%";
			}
		};
		
		public abstract String notSQL();
		public abstract String toSQL();
		public Object parameterValue(Object value) {
			return value;
		}
		public String toString(){
			return this.toSQL().toLowerCase();
		}
		
		public static Operator value(String str) {
			if (StringUtils.isEmpty(str)) return null;
			
			for (Operator op: Operator.values()) {
				if (op.toString().equals(str))
					return op;
			}
			
			throw new IllegalArgumentException("unknown op [" + str+"].");
		}
	}
	
	public enum JoinModel {
		LEFT_JOIN, RIGHT_JOIN, INNER_JOIN, FULL_JOIN
	}
	
	public enum OrderModel {
		ASC, DESC 
	}
	
	public enum NullsModel {
		NULLS_FIRST,
		NULLS_LAST
	}
	
	public enum JunctionModel {
		AND, OR
	}
}
