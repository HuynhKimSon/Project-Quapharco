package vn.com.irtech.irbot.business.dto.response;

import java.util.List;

public class ApiGetDataHdxNccRes extends ApiRes {

	private static final long serialVersionUID = 1L;

	private List<HdxNccMasterRes> data;
	
	private Integer total;

	public List<HdxNccMasterRes> getData() {
		return data;
	}

	public void setData(List<HdxNccMasterRes> data) {
		this.data = data;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}
	
	
}
