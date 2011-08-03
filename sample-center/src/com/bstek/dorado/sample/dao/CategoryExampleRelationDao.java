package com.bstek.dorado.sample.dao;

import org.springframework.stereotype.Repository;

import com.bstek.dorado.hibernate.HibernateDao;
import com.bstek.dorado.sample.entity.CategoryExampleRelation;

@Repository
public class CategoryExampleRelationDao extends
		HibernateDao<CategoryExampleRelation, Long> {}
