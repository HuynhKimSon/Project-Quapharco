package vn.com.irtech.irbot.business.dto.response;

import java.util.List;

public class ApiGetDataHdbhRes extends ApiRes {

	private static final long serialVersionUID = 1L;

	private List<HdbhMasterRes> data;

	private Integer total;

	public List<HdbhMasterRes> getData() {
		return data;
	}

	public void setData(List<HdbhMasterRes> data) {
		this.data = data;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

}
