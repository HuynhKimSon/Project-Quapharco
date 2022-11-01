package vn.com.irtech.irbot.business.dto.response;

import java.util.List;

public class ApiGetDataHdbhNbRes extends ApiRes {

	private static final long serialVersionUID = 1L;

	private List<HdbhNbMasterRes> data;

	public List<HdbhNbMasterRes> getData() {
		return data;
	}

	public void setData(List<HdbhNbMasterRes> data) {
		this.data = data;
	}

}
