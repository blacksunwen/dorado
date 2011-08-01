package com.bstek.dorado.data.provider;

import java.util.Collections;

import com.bstek.dorado.util.proxy.ListProxySupport;

/**
 * 支持数据分页的List。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apr 3, 2008
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class PagingList extends ListProxySupport {
	private DataProvider dataProvider;
	private Object parameter;
	private int pageSize;
	private int pageNo;
	private int entityCount;
	private int pageCount;

	/**
	 * @param dataProvider
	 *            用于提取分页数据的DataProvider
	 * @param parameter
	 *            调用DataProvider时使用的参数
	 * @param pageSize
	 *            每页记录数
	 * @param pageNo
	 *            初始装载的页号
	 * @throws Exception
	 */
	public PagingList(DataProvider dataProvider, Object parameter,
			int pageSize, int pageNo) throws Exception {
		super(Collections.emptyList());
		this.dataProvider = dataProvider;
		this.parameter = parameter;
		this.pageSize = pageSize;
		this.gotoPage(pageNo);
	}

	/**
	 * @param dataProvider
	 *            用于提取分页数据的DataProvider
	 * @param parameter
	 *            调用DataProvider时使用的参数
	 * @param pageSize
	 *            每页记录数
	 * @throws Exception
	 */
	public PagingList(DataProvider dataProvider, Object parameter, int pageSize)
			throws Exception {
		this(dataProvider, parameter, pageSize, 1);
	}

	/**
	 * 返回每一页的大小，即每页的记录数。
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 返回要提取的页的序号，该序号是从1开始计算的。
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * 返回总记录数。
	 * <p>
	 * 此处的总记录数并不是指当页数据的总数，而是指整个结果的总数。即每一页数据累计的总数。
	 * </p>
	 */
	public int getEntityCount() {
		return entityCount;
	}

	/**
	 * 设置总记录数。
	 * <p>
	 * 此处的总记录数并不是指当页数据的总数，而是指整个结果的总数。 即每一页数据累计的总数。
	 * </p>
	 */
	public void setEntityCount(int entityCount) {
		if (entityCount < 0) {
			throw new IllegalArgumentException(
					"Illegal entityCount arguments. [entityCount="
							+ entityCount + "]");
		}

		this.entityCount = entityCount;
		pageCount = ((entityCount - 1) / pageSize) + 1;
	}

	/**
	 * 返回总的记录页数。
	 */
	public int getPageCount() {
		return pageCount;
	}

	/**
	 * 跳转到第一页。<br>
	 * 执行此操作后List中封装的数据将变为第一页的数据。
	 * 
	 * @throws Exception
	 */
	public void gotoFirstPage() throws Exception {
		gotoPage(1);
	}

	/**
	 * 跳转到上一页。<br>
	 * 执行此操作后List中封装的数据将变为上一页的数据。
	 * 
	 * @throws Exception
	 */
	public void gotoPreviousPage() throws Exception {
		gotoPage(pageNo + 1);
	}

	/**
	 * 跳转到下一页。<br>
	 * 执行此操作后List中封装的数据将变为下一页的数据。
	 * 
	 * @throws Exception
	 */
	public void gotoNextPage() throws Exception {
		gotoPage(pageNo - 1);
	}

	/**
	 * 跳转到最后一页。<br>
	 * 执行此操作后List中封装的数据将变为最后一页的数据。
	 * 
	 * @throws Exception
	 */
	public void gotoLastPage() throws Exception {
		gotoPage(pageCount);
	}

	/**
	 * 跳转到指定的页。<br>
	 * 执行此操作后List中封装的数据将变为指定页的数据。
	 * 
	 * @param pageNo
	 * @throws Exception
	 */
	public void gotoPage(int pageNo) throws Exception {
		if (pageNo > pageCount) {
			pageNo = pageCount;
		}
		if (pageNo < 1) {
			pageNo = 1;
		}

		if (pageNo != this.pageNo) {
			Page page = new Page(pageSize, pageNo);
			dataProvider.getResult(parameter, page);

			setTarget(page.getEntities());

			this.pageNo = pageNo;
			entityCount = page.getEntityCount();
			pageCount = page.getPageCount();
		}
	}
}
