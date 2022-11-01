package vn.com.irtech.irbot.business.type;

public enum Charset {
	UTF_8("UTF-8");

	private final String value;

	Charset(String value) {
		this.value = value;
	}

	public static Charset fromString(String text) {
		for (Charset e : Charset.values()) {
			if (e.value().equalsIgnoreCase(text)) {
				return e;
			}
		}
		return null;
	}

	public String value() {
		return this.value;
	}
}
