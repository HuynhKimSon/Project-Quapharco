package vn.com.irtech.irbot.business.type;

public enum DictType {
	BUSINESS_UNIT_CODE("business_unit_code"), BUSINESS_UNIT_QUYEN_HOA_DON("business_unit_quyen_hoa_don"),
	BUSINESS_UNIT_QUYEN_XUAT_KHO("business_unit_quyen_xuat_kho");

	private final String value;

	DictType(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

	public static DictType fromValue(String value) {
		for (DictType e : DictType.values()) {
			if (value.equals(e.value)) {
				return e;
			}
		}
		return null;
	}
}
