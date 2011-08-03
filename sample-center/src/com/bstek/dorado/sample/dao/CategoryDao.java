package com.bstek.dorado.sample.dao;

import org.springframework.stereotype.Repository;

import com.bstek.dorado.hibernate.HibernateDao;
import com.bstek.dorado.sample.entity.Category;

@Repository
public class CategoryDao extends HibernateDao<Category, Long> {}
