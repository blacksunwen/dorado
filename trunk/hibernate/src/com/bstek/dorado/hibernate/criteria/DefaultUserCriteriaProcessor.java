package com.bstek.dorado.hibernate.criteria;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;

import com.bstek.dorado.hibernate.BaseUserCriteriaProcessor;
import com.bstek.dorado.hibernate.HibernateUtils;
import com.bstek.dorado.hibernate.criteria.criterion.BaseCriterion;
import com.bstek.dorado.hibernate.criteria.criterion.BetweenCriterion;
import com.bstek.dorado.hibernate.criteria.criterion.InCriterion;
import com.bstek.dorado.hibernate.criteria.criterion.SingleCriterion;
import com.bstek.dorado.hibernate.criteria.order.Order;
import com.bstek.dorado.hibernate.provider.UserCriteria;

/**
 * 默认的UserCriteria的处理器
 * @author mark
 *
 */
public class DefaultUserCriteriaProcessor extends BaseUserCriteriaProcessor<BaseCriterion>
	implements UserCriteriaProcessor {

	public void process(TopCriteria defCri, UserCriteria userCri, 
			SessionFactory sessionFactory) {
		AssociationPathHelper pathHelper = new AssociationPathHelper(defCri);
		
		processCriterions(defCri, userCri, sessionFactory, pathHelper);
		processOrder(defCri, userCri, pathHelper);
	}

	/**
	 * 处理Criterions
	 * @param defCri
	 * @param userCri
	 * @param sessionFactory
	 */
	protected void processCriterions(TopCriteria defCri, UserCriteria userCri, 
			SessionFactory sessionFactory, AssociationPathHelper pathHelper) {
		String entityName = defCri.getEntityName();
		ClassMetadata classMetadata = sessionFactory.getClassMetadata(entityName);
		
		List<UserCriteria.Parameter> userParameters = 
			userCri.getFilterParameters();
		for (UserCriteria.Parameter userParameter: userParameters) {
			String propertyPath = userParameter.getPropertyPath();
			Type type = HibernateUtils.getHibernateType(propertyPath, classMetadata, sessionFactory);
			
			String expr = userParameter.getExpr();
			String associationPath = pathHelper.getFieldAlias(propertyPath);
			BaseCriterion criterion = createWhereToken(associationPath, 
					expr, type, userParameter);
			if (criterion != null) {
				defCri.addCriterion(criterion);
			}
		}
	}

	@Override
	protected BaseCriterion toDefault(String propertyName, String expr, Type type, UserCriteria.Parameter parameter) {
		Object value = toValue(expr, type);
		SingleCriterion cri = new SingleCriterion();
		if (type instanceof StringType) {
			cri.setOp(SingleCriterion.OP.likeAnyWhere);
		} else {
			cri.setOp(SingleCriterion.OP.eq);
		}
		cri.setPropertyName(propertyName);
		cri.setValue(value);
		return cri;
	}
	
	@Override
	protected BaseCriterion toBetween(String propertyName, Object value1, Object value2, 
			UserCriteria.Parameter parameter) {
		BetweenCriterion cri = new BetweenCriterion();
		cri.setValue1(value1);
		cri.setValue2(value2);
		cri.setPropertyName(propertyName);
		return cri;
	}
	
	@Override
	protected BaseCriterion toIn(String propertyName, Object[] objects, UserCriteria.Parameter parameter) {
		InCriterion cri = new InCriterion();
		cri.setPropertyName(propertyName);
		cri.setValue(objects);
		return cri;
	}

	@Override
	protected BaseCriterion toEQ(String propertyName, Object value, UserCriteria.Parameter parameter) {
		SingleCriterion cri = new SingleCriterion();
		cri.setOp(SingleCriterion.OP.eq);
		cri.setPropertyName(propertyName);
		cri.setValue(value);
		return cri;
	}

	@Override
	protected BaseCriterion toLike(String propertyName, String value, UserCriteria.Parameter parameter) {
		SingleCriterion cri = new SingleCriterion();
		cri.setOp(SingleCriterion.OP.like);
		cri.setPropertyName(propertyName);
		cri.setValue(value);
		return cri;
	}

	@Override
	protected BaseCriterion toLT(String propertyName, Object value, UserCriteria.Parameter parameter) {
		SingleCriterion cri = new SingleCriterion();
		cri.setOp(SingleCriterion.OP.lt);
		cri.setPropertyName(propertyName);
		cri.setValue(value);
		return cri;
	}

	@Override
	protected BaseCriterion toGT(String propertyName, Object value, UserCriteria.Parameter parameter) {
		SingleCriterion cri = new SingleCriterion();
		cri.setOp(SingleCriterion.OP.gt);
		cri.setPropertyName(propertyName);
		cri.setValue(value);
		return cri;
	}

	@Override
	protected BaseCriterion toNE(String propertyName, Object value, UserCriteria.Parameter parameter) {
		SingleCriterion cri = new SingleCriterion();
		cri.setOp(SingleCriterion.OP.ne);
		cri.setPropertyName(propertyName);
		cri.setValue(value);
		return cri;
	}

	@Override
	protected BaseCriterion toLE(String propertyName, Object value, UserCriteria.Parameter parameter) {
		SingleCriterion cri = new SingleCriterion();
		cri.setOp(SingleCriterion.OP.le);
		cri.setPropertyName(propertyName);
		cri.setValue(value);
		return cri;
	}

	@Override
	protected BaseCriterion toGE(String propertyName, Object value, UserCriteria.Parameter parameter) {
		SingleCriterion cri = new SingleCriterion();
		cri.setOp(SingleCriterion.OP.ge);
		cri.setPropertyName(propertyName);
		cri.setValue(value);
		return cri;
	}
	
	
	/**
	 * 处理UserOrders，UserCriteria优先
	 * @param defCri
	 * @param userCri
	 */
	protected void processOrder(TopCriteria defCri, UserCriteria userCri, 
			AssociationPathHelper pathHelper) {
		List<UserCriteria.Order> userOrders = userCri.getOrders();
		List<Order> defOrders = defCri.getOrders();
		
		if (userOrders != null && userOrders.size() > 0) {
			List<Order> defOrders2 = new ArrayList<Order>(userOrders.size());
			for (UserCriteria.Order userOrder: userOrders) {
				Order defOrder = new Order();
				String propertyPath = userOrder.getPropertyPath();
				String fieldAlias = pathHelper.getFieldAlias(propertyPath);
				defOrder.setPropertyName(fieldAlias);
				
				if (userOrder.isDesc()) {
					defOrder.setDirection(Order.Direction.desc);
				} else {
					defOrder.setDirection(Order.Direction.asc);
				}
				defOrders2.add(defOrder);
			}
			defCri.setOrders(defOrders2);
		}
		
		if (defOrders != null && defOrders.size() > 0) {
			for (Order defOrder: defOrders) {
				String propertyName = defOrder.getPropertyName();
				boolean cover = false;
				List<Order> defOrders2 = defCri.getOrders();
				for (Order defOrder2: defOrders2) {
					String propertyName2 = defOrder2.getPropertyName();
					if (propertyName2.equals(propertyName)) {
						cover = true;
						break;
					}
				}
				if (!cover) {
					defCri.addOrder(defOrder);
				}
			}
		}
	}
}
