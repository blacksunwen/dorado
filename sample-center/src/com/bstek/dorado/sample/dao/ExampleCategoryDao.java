package com.bstek.dorado.sample.dao;

import org.springframework.stereotype.Repository;

import com.bstek.dorado.hibernate.HibernateDao;
import com.bstek.dorado.sample.entity.ExampleCategory;

@Repository
public class ExampleCategoryDao extends HibernateDao<ExampleCategory, Long> {

}
