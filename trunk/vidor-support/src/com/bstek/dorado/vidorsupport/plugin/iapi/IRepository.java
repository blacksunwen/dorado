package com.bstek.dorado.vidorsupport.plugin.iapi;

import java.util.Collection;

public interface IRepository<T extends INamed> {
	/**
	 * 注册元素
	 * @param t
	 */
	void register(T t);
	
	/**
	 * 注销元素
	 * @param t
	 */
	void deregister(T t);
	
	/**
	 * 通过名称注销元素
	 * 
	 * @param name
	 * @return
	 */
	<X extends T> X deregister(String name);

	/**
	 * 罗列出仓库中的所有元素对象
	 * @return
	 */
	Collection<T> list();
	
	/**
	 * 罗列出仓库中所有元素的名称
	 * 
	 * @return
	 */
	Collection<String> names();

	/**
	 * 通过名称获取一个元素
	 * 
	 * @param name
	 * @return
	 */
	<X extends T> X get(String name);

	/**
	 * 通过名称检查一个元素是否存在
	 * 
	 * @param name
	 * @return
	 */
	boolean has(String name);
	
	/**
	 * 检查仓库是否为空，即没有任何元素
	 */
	boolean isEmpty();
	
	/**
	 * 清空仓库的所有元素
	 */
	void empty();
	
	/**
	 * 仓库包含的元素的数量
	 * 
	 * @return
	 */
	int size();
}
