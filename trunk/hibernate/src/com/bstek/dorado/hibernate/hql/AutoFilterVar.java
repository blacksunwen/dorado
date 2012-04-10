package com.bstek.dorado.hibernate.hql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;

import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.hibernate.BaseUserCriteriaProcessor;
import com.bstek.dorado.hibernate.HibernateUtils;
import com.bstek.dorado.hibernate.provider.UserCriteria;
import com.bstek.dorado.hibernate.provider.UserCriteriaUtils;

/**
 * HQL中使用的自动过滤的变量
 * @author mark
 *
 */
public class AutoFilterVar extends BaseUserCriteriaProcessor<String>{

	private static final String HOLDER_PREFIX = "U_";
	public static boolean isHolder(String propertyName) {
		return propertyName.indexOf(HOLDER_PREFIX) == 0; 
	}
	public static Object getValue(AutoFilterVar filter, String holderName) {
		if (filter.parameterMap.containsKey(holderName)) {
			return filter.parameterMap.get(holderName);
		}
		throw new IllegalArgumentException("unknown AutoFilter holder name '" + holderName + "'");
	}
	
	private SessionFactoryImplementor factory;
	private String entityClazz;
	private String entityAlias;
	private Map<String, String> aliasMap = new HashMap<String, String>();
	
	private UserCriteria userCriteria;
	private EntityDataType dataType;
	private boolean prepareUserCriteria = false;
	private Map<String, Object> parameterMap;
	
	private String varHolder(String propertyName, Object value) {
		String holderName = createHolder(propertyName);
		parameterMap.put(holderName, value);
		return ":" + holderName;
	}
	private String varHolderArray(String propertyName, Object[] value) {
		if (value.length ==1) return varHolder(propertyName, value[0]);
		String[] holders = new String[value.length];
		for (int i=0; i< value.length; i++) {
			String holder = varHolder(propertyName, value[i]);
			holders[i] = holder;
		}
		String expr = StringUtils.join(holders, ", ");
		return "(" + expr + ")";
	}
	private String createHolder(String propertyName) {
		String holderName = HOLDER_PREFIX + parameterMap.size() + "." + propertyName;
		return holderName;
	}
	
	public AutoFilterVar() {
		
	}
	
	public AutoFilterVar(SessionFactory sessionFactory, UserCriteria userCriteria, 
			EntityDataType resultDataType) throws Exception {
		this.dataType = resultDataType;
		
		if (sessionFactory instanceof SessionFactoryImplementor) {
			this.factory = (SessionFactoryImplementor)sessionFactory;
		}
		
		this.userCriteria = userCriteria;
	}
	
	protected void prepareUserCriteria() throws Exception {
		if (!prepareUserCriteria) {
			parameterMap = new HashMap<String, Object>();
			UserCriteriaUtils.prepare(userCriteria, dataType);
			prepareUserCriteria = true;
		}
	}
	
	/**
	 * 接受HQL的别名部分
	 * @param as 别名，例如{@code Product | Product as p | Product as p, p.category as c}
	 */
	public void alias(String as) {
		String AS = " as ";
		if (as.indexOf(',') < 0) {
			int pot = as.toLowerCase().indexOf(AS);
			if (pot > 0) {
				String entityName = as.substring(0, pot).trim();
				String entityAlias = as.substring(pot + AS.length()).trim();
				this.entityAlias = entityAlias;
				this.entityClazz = factory.getImportedClassName(entityName);
			} else {
				this.entityClazz = factory.getImportedClassName(as.trim());
			}
		} else {
			String[] aliases = StringUtils.split(as, ',');
			this.alias(aliases[0]);
			
			this.aliasMap = new HashMap<String, String>(aliases.length-1);
			for (int i=1; i<aliases.length; i++) {
				String alias = aliases[i];
				int pot = alias.toLowerCase().indexOf(AS);
				if (pot > 0) {
					String entityPath = alias.substring(0, pot).trim();
					String entityAlias = alias.substring(pot + AS.length()).trim();
					if (this.aliasMap.containsKey(entityPath)) {
						throw new IllegalArgumentException("duplicate entityPath('" + entityPath + "'), " +
								"alias=(" + this.aliasMap.get(entityPath) + "," + entityAlias +").");
					}
					this.aliasMap.put(entityPath, entityAlias);
				}
			}
		}
	}

	/**
	 * 是否正在处理UserCriteria
	 * @return
	 */
	public boolean inProcess() {
		if (this.dataType != null 
				&& this.factory != null && this.userCriteria != null
				&& this.entityClazz != null) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 输出HQL的where和order by部分
	 * @return
	 * @throws Exception
	 */
	public String autoFilter() throws Exception {
		return autoWhere() + autoOrderBy();
	}
	
	/**
	 * 输出HQL的where和order by部分
	 * @param outputWhere
	 * @return
	 * @throws Exception
	 */
	public String autoFilter(boolean outputWhere) throws Exception {
		return autoWhere(outputWhere) + autoOrderBy(); 
	}
	
	/**
	 * 输出HQL的where部分
	 * @return
	 * @throws Exception
	 */
	public String autoWhere() throws Exception {
		return autoWhere(true);
	}
	
	/**
	 * 输出HQL的order by部分
	 * @return
	 * @throws Exception
	 */
	public String autoOrderBy() throws Exception {
		return autoOrderBy(true);
	}
	
	/**
	 * 输出HQL的where部分
	 * @param outputWhere 是否输出where关键字
	 * @return
	 * @throws Exception
	 */
	public String autoWhere(boolean outputWhere) throws Exception {
		if (!inProcess()) return "";
		
		prepareUserCriteria();
		
		List<UserCriteria.Parameter> parameters = this.userCriteria.getFilterParameters();
		if (parameters != null && parameters.size() > 0) {
			ClassMetadata classMetadata = factory.getClassMetadata(this.entityClazz);
			
			String where = "";
			
			for (UserCriteria.Parameter parameter: parameters) {
				String expr = parameter.getExpr();
				String propertyPath = parameter.getPropertyPath();
				Type type = HibernateUtils.getHibernateType(propertyPath, classMetadata, factory);
				
				String propertyAlias = this.toPropertyAlias(propertyPath);
				String whereToken  = this.createWhereToken(propertyAlias, expr, type, parameter);
				if (StringUtils.isNotEmpty(whereToken)) {
					if (where.length() > 0) {
						where += " and " + whereToken;
					} else {
						where = whereToken;
					}
				}
			}
			
			return outputWhere ? (" where " + where) : " " + where;
		}
		
		return "";
	}
	
	/**
	 * 输出HQL的order by部分
	 * @param outputOrderBy 是否输出order by关键字
	 * @return
	 * @throws Exception
	 */
	public String autoOrderBy(boolean outputOrderBy) throws Exception {
		if (!inProcess()) return "";
		
		prepareUserCriteria();
		
		List<UserCriteria.Order> orders = this.userCriteria.getOrders();
		if (orders != null && orders.size() > 0) {
			String clause = outputOrderBy ? " order by " : " ";

			UserCriteria.Order order = orders.get(0);
			String propertyPath = order.getPropertyPath();
			String propertyAlias = toPropertyAlias(propertyPath);
			clause += propertyAlias + " " + (order.isDesc() ? "desc" : "asc");
			
			for (int i=1; i<orders.size(); i++) {
				order = orders.get(i);
				propertyPath = order.getPropertyPath();
				propertyAlias = toPropertyAlias(propertyPath);
				clause += ", " + propertyAlias + " " + (order.isDesc() ? "desc" : "asc");
			}
			return clause;
		} else {
			return "";
		}
	}
	
	/**
	 * 将属性路径转换为别名
	 * @param propertyPath
	 * @return
	 */
	protected String toPropertyAlias(String propertyPath) {
		if (propertyPath.indexOf('.') < 0) {
			if (this.entityAlias != null) {
				return this.entityAlias + "." + propertyPath;
			} else {
				return propertyPath;
			}
		} else {
			String[] tokens = StringUtils.split(propertyPath, '.');
			String token = tokens[0];
			String alias = (this.entityAlias != null) ? 
					this.entityAlias + "." + token : token;
					
			if (this.aliasMap.containsKey(alias)) {
				alias = this.aliasMap.get(alias);
			}
			for (int i = 1; i<tokens.length; i++) {
				token = tokens[i];
				alias = alias + "." + token;
				if (this.aliasMap.containsKey(alias)) {
					alias = this.aliasMap.get(alias);
				}
			}
			return alias;
		}
	}

	@Override
	protected String toDefault(String propertyName, String expr, Type type, 
			UserCriteria.Parameter parameter) {
		Object value = toValue(expr, type);
		if (type instanceof StringType) {
			value = toLikeValue((String)value);
			return propertyName + " like " + varHolder(propertyName, value);
		} else {
			return propertyName + " = " + varHolder(propertyName, value); 
		}
	}

	@Override
	protected String toBetween(String propertyName, Object value1, Object value2, 
			UserCriteria.Parameter parameter) {
		return propertyName + " between " + varHolder(propertyName, value1) + " and " + varHolder(propertyName, value2);
	}

	@Override
	protected String toIn(String propertyName, Object[] objects, 
			UserCriteria.Parameter parameter) {
		return propertyName + " in " + varHolderArray(propertyName, objects);
	}

	@Override
	protected String toEQ(String propertyName, Object value, 
			UserCriteria.Parameter parameter) {
		if (value != null) {
			return propertyName + " = " + varHolder(propertyName, value);
		} else {
			return propertyName + " is null" ;
		}
	}

	@Override
	protected String toLike(String propertyName, String value, 
			UserCriteria.Parameter parameter) {
		return propertyName + " like " + varHolder(propertyName, value);
	}

	@Override
	protected String toLT(String propertyName, Object value, 
			UserCriteria.Parameter parameter) {
		return propertyName + " < " + varHolder(propertyName, value);
	}

	@Override
	protected String toGT(String propertyName, Object value, 
			UserCriteria.Parameter parameter) {
		return propertyName + " > " + varHolder(propertyName, value);
	}

	@Override
	protected String toNE(String propertyName, Object value, 
			UserCriteria.Parameter parameter) {
		return propertyName + " <> " + varHolder(propertyName, value);
	}

	@Override
	protected String toLE(String propertyName, Object value, 
			UserCriteria.Parameter parameter) {
		return propertyName + " <= " + varHolder(propertyName, value);
	}

	@Override
	protected String toGE(String propertyName, Object value, 
			UserCriteria.Parameter parameter) {
		return propertyName + " >= " + varHolder(propertyName, value);
	}
}
