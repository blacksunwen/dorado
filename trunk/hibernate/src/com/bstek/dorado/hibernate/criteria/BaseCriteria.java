package com.bstek.dorado.hibernate.criteria;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.hibernate.criteria.criterion.BaseCriterion;
import com.bstek.dorado.hibernate.criteria.order.Order;
import com.bstek.dorado.hibernate.criteria.projection.BaseProjection;

public abstract class BaseCriteria {

	protected String alias;
	private List<Alias> aliases = new ArrayList<Alias>();
	private List<BaseCriterion> criterions = new ArrayList<BaseCriterion>();
	private List<BaseProjection> projections = new ArrayList<BaseProjection>();
	private List<Order> orders = new ArrayList<Order>();
	private List<FetchMode> fetchModes = new ArrayList<FetchMode>();
	private List<SubCriteria> subCriterias = new ArrayList<SubCriteria>();
	
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	// ********** Alias **********
	@XmlSubNode
	public List<Alias> getAliases() {
		return aliases;
	}
	public void setAliases(List<Alias> aliases) {
		this.aliases = aliases;
	}
	public void addAlias(Alias alias) {
		aliases.add(alias);
	}
	
	// ********** Projection **********
	@XmlSubNode(parser = "dorado.projectionsParser")
	public List<BaseProjection> getProjections() {
		return projections;
	}
	public void addProjection(BaseProjection projection) {
		projections.add(projection);
	}
	
	// ********** Criterion **********
	@XmlSubNode(parser = "dorado.criterionsParser")
	public List<BaseCriterion> getCriterions() {
		return criterions;
	}
	public void addCriterion(BaseCriterion criterion) {
		criterions.add(criterion);
	}
	
	// ********** Order **********
	@XmlSubNode
	public List<Order> getOrders() {
		return orders;
	}
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	public void addOrder(Order order) {
		orders.add(order);
	}

	// ********** FetchMode **********
	@XmlSubNode
	public List<FetchMode> getFetchModes() {
		return fetchModes;
	}
	public void addFetchMode(FetchMode fm) {
		fetchModes.add(fm);
	}
	
	// ********** SubCriteria **********
	@XmlSubNode
	public List<SubCriteria> getSubCriterias() {
		return this.subCriterias;
	}
	public void addSubCriteria(SubCriteria sub){
		subCriterias.add(sub);
	}
}
