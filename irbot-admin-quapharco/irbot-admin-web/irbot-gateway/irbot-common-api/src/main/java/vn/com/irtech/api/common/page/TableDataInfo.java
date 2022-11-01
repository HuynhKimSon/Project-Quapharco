package vn.com.irtech.api.common.page;

import java.io.Serializable;
import java.util.List;

public class TableDataInfo  implements Serializable {
	private static final long serialVersionUID = 1L;

	/** total */
	private long total;

	/** List data */
	private List<?> rows;

	/** Message status code */
	private int code;

	/** Message content */
	private String msg;

	/**
	 * Tabular data object
	 */
	public TableDataInfo() {
	}

	/**
	 * Pagination
	 * 
	 * @param list  List data
	 * @param total total
	 */
	public TableDataInfo(List<?> list, int total) {
		this.rows = list;
		this.total = total;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}