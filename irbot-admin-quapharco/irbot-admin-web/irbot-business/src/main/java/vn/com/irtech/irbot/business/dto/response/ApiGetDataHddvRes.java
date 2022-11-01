package vn.com.irtech.irbot.business.dto.response;

import java.util.List;

public class ApiGetDataHddvRes extends ApiRes {

	private static final long serialVersionUID = 1L;

	private List<HddvMasterRes> data;

	public List<HddvMasterRes> getData() {
		return data;
	}

	public void setData(List<HddvMasterRes> data) {
		this.data = data;
	}

}
