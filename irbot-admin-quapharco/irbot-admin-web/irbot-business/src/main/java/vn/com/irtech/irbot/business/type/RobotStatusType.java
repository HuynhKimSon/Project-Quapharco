package vn.com.irtech.irbot.business.type;

public enum RobotStatusType {
	OFFLINE("0"), BUSY("1"), AVAILABLE("2");

	private final String value;

	RobotStatusType(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	public static RobotStatusType fromValue(String value) {
		for (RobotStatusType e : RobotStatusType.values()) {
			if (value == e.value) {
				return e;
			}
		}
		return null;
	}
}
