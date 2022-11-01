package vn.com.irtech.irbot.business.type;

public enum ProcessMode {
	GOC(0), DA_DIEU_CHINH(1);

	private final Integer value;

	ProcessMode(Integer value) {
		this.value = value;
	}

	public Integer value() {
		return this.value;
	}

	public static ProcessMode fromValue(Integer value) {
		for (ProcessMode e : ProcessMode.values()) {
			if (value == e.value) {
				return e;
			}
		}
		return null;
	}
}
