package vn.com.irtech.irbot.business.dto.request;

import java.io.Serializable;

public class UpdateStatusReq implements Serializable {

	private static final long serialVersionUID = 1L;

	private String ids;

	private Integer status;

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
