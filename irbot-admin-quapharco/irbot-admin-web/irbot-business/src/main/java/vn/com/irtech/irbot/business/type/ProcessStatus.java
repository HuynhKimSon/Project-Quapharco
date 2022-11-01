package vn.com.irtech.irbot.business.type;

public enum ProcessStatus {
	NOTSEND(0), WAIT(1), SENT(2),  PROCESSING(3), FAIL(4), SUCCESS(5);

	private final Integer value;

	ProcessStatus(Integer value) {
		this.value = value;
	}

	public Integer value() {
		return this.value;
	}

	public static ProcessStatus fromValue(Integer value) {
		for (ProcessStatus e : ProcessStatus.values()) {
			if (value == e.value) {
				return e;
			}
		}
		return null;
	}
}
