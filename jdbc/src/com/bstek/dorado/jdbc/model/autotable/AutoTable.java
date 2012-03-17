package com.bstek.dorado.jdbc.model.autotable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlNodeWrapper;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.JdbcDataProviderContext;
import com.bstek.dorado.jdbc.JdbcDataProviderOperation;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcParameterSource;
import com.bstek.dorado.jdbc.model.AbstractDbColumn;
import com.bstek.dorado.jdbc.model.AbstractTable;
import com.bstek.dorado.jdbc.model.AbstractUpdatableColumn;
import com.bstek.dorado.jdbc.model.table.Table;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlBuilder;
import com.bstek.dorado.jdbc.sql.SqlConstants.JoinOperator;
import com.bstek.dorado.jdbc.sql.SqlConstants.KeyWord;
import com.bstek.dorado.jdbc.sql.SqlUtils;
import com.bstek.dorado.util.Assert;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
@XmlNode(
	parser = "spring:dorado.jdbc.autoTableParser",
	definitionType = "com.bstek.dorado.jdbc.model.autotable.AutoTableDefinition",
	subNodes = {
		@XmlSubNode(
			wrapper = @XmlNodeWrapper(nodeName = "Columns", fixed = true),
			propertyName = "Jdbc_AutoTableColumns",
			propertyType = "List<com.bstek.dorado.jdbc.model.autotable.AutoTableColumn>"
		),
		@XmlSubNode(
			wrapper = @XmlNodeWrapper(nodeName="FromTables", fixed = true),
			propertyName = "fromTables",
			propertyType = "List<com.bstek.dorado.jdbc.model.autotable.FromTable>"
		),
		@XmlSubNode(
			wrapper = @XmlNodeWrapper(nodeName = "JoinTables"),
			propertyName = "joinTables",
			propertyType = "List<com.bstek.dorado.jdbc.model.autotable.JoinTable>"
		),
		@XmlSubNode(
			wrapper = @XmlNodeWrapper(nodeName = "Orders"),
			propertyName = "orders",
			propertyType = "List<com.bstek.dorado.jdbc.model.autotable.Order>"
		),
		@XmlSubNode(
			nodeName="Where",
			propertyName="where",
			propertyType="com.bstek.dorado.jdbc.model.autotable.JunctionMatchRule",
			fixed=true
		)
	}
)
public class AutoTable extends AbstractTable {

	public static final String TYPE = "AutoTable";
	
	private Map<String, FromTable> fromTables = new LinkedHashMap<String, FromTable>();
	
	private List<JoinTable> joinTables = new ArrayList<JoinTable>(5);
	
	private List<Order> orders = new ArrayList<Order>(5);

	private JunctionMatchRule where;

	private String mainFromTable;
	private Table mainTable;
	
	public void addFromTable(FromTable fromTable) {
		String tableAlias = fromTable.getName();
		Assert.notEmpty(tableAlias, "[" + this.getName() + "] tableAlias must not be null");
		Assert.isTrue(!fromTables.containsKey(tableAlias), "[" + this.getName() + "] duplicate fromTable '" + tableAlias + "'");
		
		this.fromTables.put(tableAlias, fromTable);
	}
	
	public void addJoinTable(JoinTable joinTable) {
		this.joinTables.add(joinTable);
	}

	public List<FromTable> getFromTables() {
		return new ArrayList<FromTable>(fromTables.values());
	}
	
	public FromTable getFromTable(String alias) {
		FromTable fromTable = fromTables.get(alias);
		Assert.notNull(fromTable, "[" + getName()+ "] " + "No FromTable named [" + alias + "]");
		return fromTable;
	}

	public List<JoinTable> getJoinTables() {
		return joinTables;
	}

//	@XmlSubNode(nodeName="Where", fixed=true)
	public JunctionMatchRule getWhere() {
		return where;
	}

	public void setWhere(JunctionMatchRule where) {
		this.where = where;
	}

	public List<Order> getOrders() {
		return orders;
	}
	
	public void addOrder(Order order) {
		this.orders.add(order);
	}
	
	public String getType() {
		return TYPE;
	}

	public FromTable getMainFromTableObject() {
		if (StringUtils.isEmpty(mainFromTable)) {
			return null;
		} else {
			return this.getFromTable(mainFromTable);
		}
	}

	public void setMainFromTable(String mainTableAlias) {
		this.mainFromTable = mainTableAlias;
	}
	
	public String getMainFromTable() {
		return this.mainFromTable;
	}

	@Override
	public void addColumn(AbstractDbColumn column) {
		Assert.notNull(column);
		
		if (column instanceof AutoTableColumn) {
			super.addColumn(column);
		} else {
			throw new IllegalArgumentException(getType() + " [" + getName()+ "] " + "Unknown column class [" + column.getClass() + "]");
		}
	}

	@Override
	public boolean supportResolverTable() {
		return true;
	}

	@Override
	public Table getResolverTable() {
		if (mainTable == null) {
			FromTable fromTable = this.getMainFromTableObject();
			Table table = fromTable.getTableObject();
			
			mainTable = table;
		}
		return mainTable;
	}

	@Override
	protected boolean acceptByProxy(AbstractUpdatableColumn column,
			EntityState state) {
		String mainFromTableName = this.getMainFromTable();
		Assert.notEmpty(mainFromTableName, "[" + this.getName() + "] mainFromTable must not be null");
		boolean superResult = super.acceptByProxy(column, state);
		if (superResult) {
			AutoTableColumn autoColumn = (AutoTableColumn)column;
			String fromTableName = autoColumn.getFromTable();
			return mainFromTableName.equals(fromTableName);
		}
		
		return false;
	}

	@Override
	public SelectSql selectSql(JdbcDataProviderOperation operation) {
		AutoTable autoTable = (AutoTable)operation.getDbTable();
		JdbcDataProviderContext jdbcContext = operation.getJdbcContext();
		Object parameter = jdbcContext.getParameter();
		
		//columnsToken
		StringBuilder columnsToken = new StringBuilder();
		List<AbstractDbColumn> columns = autoTable.getAllColumns();
		for (int i=0, j=columns.size(), ableColumnCount = 0; i<j; i++) {
			AutoTableColumn column = (AutoTableColumn)columns.get(i);
			if (column.isSelectable()) {
				if (ableColumnCount++ > 0) {
					columnsToken.append(',');
				}
				
				String tableAlias = column.getFromTable();
				String nativeName = column.getNativeColumn();
				if (StringUtils.isNotEmpty(nativeName)) {
					String propertyName = column.getPropertyName();
					String token = tableAlias + "." + nativeName + " " + KeyWord.AS + " " + propertyName;
					columnsToken.append(token);
				}
			}
		}
		
		JdbcEnviroment jdbcEnv = operation.getJdbcEnviroment();
		Dialect dialect = jdbcEnv.getDialect();
		//fromToken
		StringBuilder fromToken = fromToken(autoTable, dialect);
		//where
		JdbcParameterSource parameterSource = SqlUtils.createJdbcParameter(parameter);
		StringBuilder whereToken = whereToken(autoTable, parameterSource);
		//order
		StringBuilder orderbyToken = orderByToken(autoTable, parameterSource, dialect);
		
		//--
		AutoTableSelectSql selectSql = new AutoTableSelectSql();
		selectSql.setParameterSource(parameterSource);
		selectSql.setColumnsToken(columnsToken.toString());
		selectSql.setFromToken(fromToken.toString());
		selectSql.setWhereToken(whereToken.toString());
		selectSql.setOrderToken(orderbyToken.toString());
		
		Criteria criteria = this.getCriteria(operation);
		if (criteria != null) {
			selectSql.setCriteria(criteria);
		}
		
		return selectSql;
	}
	
	private String token(Dialect dialect, FromTable fromTable) {
		return dialect.token(fromTable.getTableObject(), fromTable.getName());
	}
	
	private StringBuilder fromToken(AutoTable t, Dialect dialect) {
		StringBuilder fromToken = new StringBuilder();
		List<JoinTable> joinTables = t.getJoinTables();
		
		if (joinTables.size() == 0) {
			List<FromTable> fromTables = t.getFromTables();
			Assert.isTrue(fromTables.size() > 0, "no from tables defined.");
			
			for (int i=0; i<fromTables.size(); i++) {
				FromTable fromTable = fromTables.get(i);
				if (i > 0) {
					fromToken.append(',');
				}
				
				String token = token(dialect, fromTable);
				fromToken.append(token);
			}
		} else {
			for (int i=0; i<joinTables.size(); i++) {
				JoinTable joinTable = joinTables.get(i);
				
				JoinOperator joinModel = joinTable.getOperator();
				String[] leftColumnNames = joinTable.getLeftColumns();
				String[] rightColumnNames = joinTable.getRightColumns();
				
				Assert.isTrue(leftColumnNames.length > 0, 
						"length of LeftColumnNames must greate than 0.");
				
				Assert.isTrue(rightColumnNames.length > 0, 
						"length of RightColumnNames length must greate than 0.");
				
				Assert.isTrue(leftColumnNames.length == rightColumnNames.length, 
						"length of LeftColumnNames and length of RightColumnNames not equals.");
				
				
				FromTable leftFromTable = t.getFromTable(joinTable.getLeftFromTable());
				FromTable rightFromTable = t.getFromTable(joinTable.getRightFromTable());
				String token = this.joinToken(dialect, t, joinModel, leftFromTable, leftColumnNames, rightFromTable, rightColumnNames);
				
				if (i > 0) {
					fromToken.append(',');
				}
				fromToken.append(token);
			}
		}
		
		return fromToken;
	}
	
	private StringBuilder whereToken(AutoTable t, JdbcParameterSource p) {
		StringBuilder whereToken = new StringBuilder();
		
		JunctionMatchRule where = t.getWhere();
		if (where != null) {
			String token = where.token(t, p);
			if (StringUtils.isNotEmpty(token)) {
				whereToken.append(token);
			}
		}
		
		return whereToken;
	}

	private StringBuilder orderByToken(AutoTable t, JdbcParameterSource p, Dialect dialect) {
		StringBuilder r = new StringBuilder();
		
		List<Order> orders = t.getOrders();
		if (orders != null && !orders.isEmpty()) {
			List<String> tokens = new ArrayList<String>(orders.size());
			for (int i=0; i<orders.size(); i++) {
				Order order = orders.get(i);
				if (order.isAvailable()) {
					String token = dialect.token(t, order);
					if (StringUtils.isNotEmpty(token)) {
						tokens.add(token);
					}
				}
			}
			
			if (tokens.size() > 0) {
				if (tokens.size() == 1) {
					String s = tokens.get(0).toString(); 
					r.append(s);
				} else {
					String s = StringUtils.join(tokens, ',');
					r.append(s);
				}
			}
		}
		
		return r;
	}
	
	private String joinToken(Dialect dialect, AutoTable autoTable, JoinOperator joinModel, FromTable leftFromTable,
			String[] leftColumnNames, FromTable rightFromTable,
			String[] rightColumnNames) {
		SqlBuilder token = new SqlBuilder();
		String leftTableAlias = leftFromTable.getName();
		String rightTableAlias = rightFromTable.getName();
		
		Table leftTable = leftFromTable.getTableObject();
		Table rightTable = rightFromTable.getTableObject();
		
		String tl = token(dialect, leftFromTable);
		String tr = token(dialect, rightFromTable);
		String jm = dialect.token(autoTable,joinModel);
		
		token.append(tl).bothSpace(jm).append(tr);
		token.bothSpace(KeyWord.ON);
		for (int i=0; i<leftColumnNames.length; i++) {
			if (i>0) {
				token.bothSpace(KeyWord.AND);
			}
			
			String leftColumnName = leftColumnNames[i];
			String rightColumnName = rightColumnNames[i];
			AbstractDbColumn leftColumn = leftTable.getColumn(leftColumnName);
			AbstractDbColumn rightColumn = rightTable.getColumn(rightColumnName);
			
			token.append(leftTableAlias, ".", leftColumn.getName());
			token.bothSpace("=");
			token.append(rightTableAlias, ".", rightColumn.getName());
		}
		
		return token.build();
	}
}
