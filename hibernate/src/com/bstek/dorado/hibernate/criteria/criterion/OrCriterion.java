package com.bstek.dorado.hibernate.criteria.criterion;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.hibernate.criteria.HibernateCriteriaTransformer;

@XmlNode(nodeName = "Or")
public class OrCriterion extends JunctionCriterion {

	public Criterion toHibernate(Object parameter,
			SessionFactory sessionFactory,
			HibernateCriteriaTransformer transformer) throws Exception {
		List<BaseCriterion> cris = this.getCriterions();
		if (cris != null && cris.size() > 0) {
			List<Criterion> criterions = transformer.listCriterion(cris,
					parameter, sessionFactory);
			if (criterions != null && criterions.size() > 0) {
				Disjunction orJunc = Restrictions.disjunction();
				for (Criterion c : criterions) {
					orJunc.add(c);
				}
				return orJunc;
			}
		}
		return null;
	}
}
