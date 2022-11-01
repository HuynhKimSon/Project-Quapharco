package vn.com.irtech.irbot.business.dto;

import java.util.Map;

import javax.mail.internet.InternetAddress;

import vn.com.irtech.irbot.business.type.Charset;

public class MailTemplate {
	
	private InternetAddress[] to;
	
	private String subject;

	private String contents;

	private String charset = Charset.UTF_8.value();
	
	private Map<String, Object> props;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public Map<String, Object> getProps() {
		return props;
	}

	public void setProps(Map<String, Object> props) {
		this.props = props;
	}

	public InternetAddress[] getTo() {
		return to;
	}

	public void setTo(InternetAddress[] to) {
		this.to = to;
	}

}
