package com.smartcold.manage.cold.util;

import java.util.ArrayList;
import java.util.List;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

/**
 * JSON格式返回数据载体
 * @author Half.Lee
 */
public class TableData<T> {

	
	//---------------------------------------------------------
	//请求信息
	private boolean success=true;
	private String message;
	//数据对象组
	private List<T> rows;
	//数据对象组分页数据
	private Long total;
	private Integer pageNum;
	private Integer pageSize;
	private Integer totalPages;
	//额外信息
	private Object extra;

	@SuppressWarnings("rawtypes")
	private static ThreadLocal<TableData> TLRD = new ThreadLocal<TableData>();
	
	public static void clear() {
		TLRD.remove();
	}

	/**
	 * 获取当前可用的ResponseData对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> TableData<T> getInstance() {
		TableData<T> rd = TLRD.get();
		if (rd == null) {
			rd = new TableData<T>();
			TLRD.set(rd);
		}
		return rd;
	}


	public static <T> TableData<T> newSuccess() {
		TableData<T> rd = getInstance();
		rd.setSuccess(true);
		rd.setMessage(null);
		rd.setRows(null);
		rd.setPageNum(0);
		rd.setPageSize(0);
		rd.setTotalPages(0);
		return rd;
	}



	public static <T> TableData<T> newSuccess(Page<T> data) {
		TableData<T> rd = getInstance();
		rd.setSuccess(true);
		rd.setMessage(null);
		rd.setRows(data.getResult());
		rd.setTotal(data.getTotal());
		rd.setPageNum(data.getPageNum());
		rd.setPageSize(data.getPageSize());
		return rd;
	}

	/**
	 * 获取当前可用的ResponseData对象(SUCCESS)
	 * @param data
	 * @param message
	 * @return
	 */
	public static <T> TableData<T> newSuccess(Page<T> data, String message) {
		TableData<T> rd = getInstance();
		rd.setSuccess(true);
		rd.setMessage(message);

		rd.setRows(data == null?new ArrayList<T>():data);
		rd.setTotal(null);
		rd.setPageNum(null);
		rd.setPageSize(null);
		rd.setTotalPages(null);

		return rd;
	}
	/**
	 * 获取当前可用的ResponseData对象(SUCCESS)
	 * @param data
	 * @param message
	 * @return
	 */
	public static <T> TableData<T> newSuccess(PageInfo<T> data, String message) {
		TableData<T> rd = getInstance();
		rd.setSuccess(true);
		rd.setMessage(message);

		List<T> _tmp = data.getList();
		rd.setRows(_tmp == null?new ArrayList<T>():data.getList());
		rd.setTotal(data.getTotal());
		rd.setPageNum(data.getPageNum());
		rd.setPageSize(data.getPageSize());
		rd.setTotalPages(data.getPages());

		return rd;
	}

	/**
	 * 获取当前可用的ResponseData对象(FAILURE)
	 * @return
	 */
	public static <T> TableData<T> newFailure() {
		return newFailure("Failure");
	}

	/**
	 * 获取当前可用的ResponseData对象(FAILURE)
	 * @param exp
	 * @return
	 */
	public static <T> TableData<T> newFailure(Exception exp) {
		return newFailure(exp, "Failure");
	}

	/**
	 * 获取当前可用的ResponseData对象(FAILURE)
	 * @param message
	 * @return
	 */
	public static <T> TableData<T> newFailure(String message) {
		TableData<T> rd = getInstance();
		rd.setSuccess(false);
		rd.setMessage(message);
		return rd;
	}

	/**
	 * 获取当前可用的ResponseData对象(FAILURE)
	 * @param exp
	 * @param message
	 * @return
	 */
	public static <T> TableData<T> newFailure(Exception exp, String message) {
		TableData<T> rd = getInstance();
		rd.setSuccess(false);
		rd.setMessage(message);
		return rd;
	}


	//---------------------------------------------------------
	private TableData() {

	}

	//---------------------------------------------------------
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}




	

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}


	public Object getExtra() {
		return extra;
	}

	public void setExtra(Object extra) {
		this.extra = extra;
	}

	
}
