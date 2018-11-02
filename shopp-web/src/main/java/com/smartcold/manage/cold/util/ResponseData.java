package com.smartcold.manage.cold.util;

import java.util.ArrayList;
import java.util.List;

/**
 * JSON格式返回数据载体
 * 
 * @author MaQiang
 */
public class ResponseData<T> {

	// 请求信息
	private int status = 200;//[ 200 成功        400：失败][ 用户：   405:重新登录  406：刷新当前信息 ]
	private String message;
	private Exception exp;
	// 数据对象
	private T entity;
	// 数据对象组
	private List<T> data;
	// 数据对象组分页数据
	private Long total;
	private Integer pageNum;
	private Integer pageSize;
	private Integer totalPages;
	// dataTables 分页属性
	private Long recordsTotal;
	private Long recordsFiltered;
	// 额外信息
	private Object extra;

	@SuppressWarnings("rawtypes")
	private static ThreadLocal<ResponseData> TLRD = new ThreadLocal<ResponseData>();

	public static void clear() {
		TLRD.remove();
	}

	/**
	 * 获取当前可用的ResponseData对象
	 * 
	 * @return
	 */
	public static <T> ResponseData<T> getInstance() {
//		ResponseData<T> rd = TLRD.get();
//		if (rd == null) {
//			rd = new ResponseData<T>();
//			TLRD.set(rd);
//		}
//		//rd.clear();
		return  new ResponseData<T>();
	}

	/**
	 * 获取当前可用的ResponseData对象(SUCCESS)
	 * 
	 * @return
	 */
	public static <T> ResponseData<T> newSuccess() {
		return newSuccess("Success");
	}

	/**
	 * 获取当前可用的ResponseData对象(SUCCESS)
	 * 
	 * @param data
	 * @return
	 */
	public static <T> ResponseData<T> newSuccess(T data) {
		return newSuccess(data, "Success");
	}

	/**
	 * 获取当前可用的ResponseData对象(SUCCESS)
	 * 
	 * @param data
	 * @return
	 */
	public static <T> ResponseData<T> newSuccess(List<T> data) {
		return newSuccess(data, "Success");
	}

	/**
	 * 获取当前可用的ResponseData对象(SUCCESS)
	 * 
	 * @param message
	 * @return
	 */
	public static <T> ResponseData<T> newSuccess(String message) {
		ResponseData<T> rd = getInstance();
		rd.setStatus(200);
		rd.setMessage(message);
		rd.setExp(null);
		return rd;
	}

	/**
	 * 获取当前可用的ResponseData对象(SUCCESS)
	 * 
	 * @param data
	 * @param message
	 * @return
	 */
	public static <T> ResponseData<T> newSuccess(T data, String message) {
		ResponseData<T> rd = getInstance();
		rd.setStatus(200);
		rd.setMessage(message);
		rd.setExp(null);
		rd.setData(null);
		rd.setEntity(data);

		return rd;
	}

	/**
	 * 获取当前可用的ResponseData对象(SUCCESS)
	 * 
	 * @param data
	 * @param message
	 * @return
	 */
	public static <T> ResponseData<T> newSuccess(List<T> data, String message) {
		ResponseData<T> rd = getInstance();
		rd.setStatus(200);
		rd.setMessage(message);
		rd.setExp(null);

		rd.setData(data == null ? new ArrayList<T>() : data);
		rd.setTotal(null);
		rd.setPageNum(null);
		rd.setPageSize(null);
		rd.setTotalPages(null);

		return rd;
	}

	/**
	 * 获取当前可用的ResponseData对象(FAILURE)
	 * 
	 * @return
	 */
	public static <T> ResponseData<T> newFailure() {
		return newFailure("Failure");
	}

	/**
	 * 获取当前可用的ResponseData对象(FAILURE)
	 * 
	 * @param exp
	 * @return
	 */
	public static <T> ResponseData<T> newFailure(Exception exp) {
		return newFailure(exp, "Failure");
	}

	/**
	 * 获取当前可用的ResponseData对象(FAILURE)
	 * 
	 * @param message
	 * @return
	 */
	public static <T> ResponseData<T> newFailure(String message) {
		ResponseData<T> rd = getInstance();
		rd.setStatus(400);
		rd.setMessage(message);
		rd.setExp(null);

		return rd;
	}

	/**
	 * 获取当前可用的ResponseData对象(FAILURE)
	 * 
	 * @param exp
	 * @param message
	 * @return
	 */
	public static <T> ResponseData<T> newFailure(Exception exp, String message) {
		ResponseData<T> rd = getInstance();
		rd.setStatus(400);
		rd.setMessage(message);
		rd.setExp(exp);

		return rd;
	}

	// ---------------------------------------------------------

	// ---------------------------------------------------------
	private ResponseData() {

	}

	// ---------------------------------------------------------

	public String getMessage() {
		return message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Exception getExp() {
		return exp;
	}

	public void setExp(Exception exp) {
		this.exp = exp;
		if (this.exp != null) {
			StackTraceElement[] traces = this.exp.getStackTrace();
			if (traces != null && traces.length > 0)
				this.exp.setStackTrace(new StackTraceElement[] { traces[0] });
		}
	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
		this.recordsTotal = this.recordsFiltered = total;
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

	public Long getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(Long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public Long getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(Long recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public Object getExtra() {
		return extra;
	}

	public void setExtra(Object extra) {
		this.extra = extra;
	}
}
