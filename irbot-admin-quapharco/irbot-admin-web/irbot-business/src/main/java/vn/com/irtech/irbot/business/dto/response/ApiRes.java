package vn.com.irtech.irbot.business.dto.response;

import java.io.Serializable;

public class ApiRes implements Serializable {

	private static final long serialVersionUID = 1L;

	protected Integer code;

	protected String msg;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
