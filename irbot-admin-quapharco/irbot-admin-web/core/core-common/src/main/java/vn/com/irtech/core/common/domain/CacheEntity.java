package vn.com.irtech.core.common.domain;

import java.io.Serializable;

/**
 * @author Trong Hieu
 *
 */
public class CacheEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String key;

	private String keyName;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

}
