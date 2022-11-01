package vn.com.irtech.irbot.business.type;

public enum RobotServiceType {
	HOA_DON_GTGT_GOC(100, "Hoá đơn giá trị gia tăng gốc"), HOA_DON_GTGT_DIEU_CHINH(200, "Hoá đơn giá trị gia tăng điều chỉnh"),
	XUAT_NOI_BO_GOC(300, "Xuất nội bộ gốc"), XUAT_NOI_BO_DIEU_CHINH(400, "Xuất nội bộ điều chỉnh");

	private final Integer value;

	private final String title;

	RobotServiceType(Integer value, String title) {
		this.value = value;
		this.title = title;
	}

	public Integer value() {
		return this.value;
	}

	public String title() {
		return this.title;
	}

	public static RobotServiceType fromValue(Integer value) {
		for (RobotServiceType e : RobotServiceType.values()) {
			if (value == e.value) {
				return e;
			}
		}
		return null;
	}
}
