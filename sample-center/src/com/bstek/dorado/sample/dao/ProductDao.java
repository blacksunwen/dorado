package com.bstek.dorado.sample.dao;

import org.springframework.stereotype.Repository;

import com.bstek.dorado.hibernate.HibernateDao;
import com.bstek.dorado.sample.entity.Product;

@Repository
public class ProductDao extends HibernateDao<Product, Long> {}
