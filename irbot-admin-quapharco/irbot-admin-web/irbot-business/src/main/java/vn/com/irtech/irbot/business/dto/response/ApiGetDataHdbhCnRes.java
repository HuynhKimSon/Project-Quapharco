package vn.com.irtech.irbot.business.dto.response;

import java.util.List;

public class ApiGetDataHdbhCnRes extends ApiRes {

	private static final long serialVersionUID = 1L;

	private List<HdbhCnMasterRes> data;
	
	private Integer total;

	public List<HdbhCnMasterRes> getData() {
		return data;
	}

	public void setData(List<HdbhCnMasterRes> data) {
		this.data = data;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}
	
	
}
