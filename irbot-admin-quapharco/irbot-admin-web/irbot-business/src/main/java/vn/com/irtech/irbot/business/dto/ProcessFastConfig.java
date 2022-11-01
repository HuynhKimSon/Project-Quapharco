package vn.com.irtech.irbot.business.dto;

import java.io.Serializable;

public class ProcessFastConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	private String username;

	private String password;

	private String url;

	private String companyName;

	private String bookHD;

	private String bookPXK;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getBookHD() {
		return bookHD;
	}

	public void setBookHD(String bookHD) {
		this.bookHD = bookHD;
	}

	public String getBookPXK() {
		return bookPXK;
	}

	public void setBookPXK(String bookPXK) {
		this.bookPXK = bookPXK;
	}
}
