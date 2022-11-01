package vn.com.irtech.irbot.business.type;

public enum InvoiceMode {

	HOA_DON_BAN_HANG_GOC(1, "HOA_DON_BAN_HANG_GOC"),
	HOA_DON_BAN_HANG_DIEU_CHINH_TANG(2, "HOA_DON_BAN_HANG_DIEU_CHINH_TANG"),
	HOA_DON_BAN_HANG_DIEU_CHINH_GIAM(3, "HOA_DON_BAN_HANG_DIEU_CHINH_GIAM"),
	
	HOA_DON_BAN_HANG_CN_GOC(1, "HOA_DON_BAN_HANG_CN_GOC"),
	HOA_DON_BAN_HANG_CN_DIEU_CHINH_TANG(2, "HOA_DON_BAN_HANG_CN_DIEU_CHINH_TANG"),
	HOA_DON_BAN_HANG_CN_DIEU_CHINH_GIAM(3, "HOA_DON_BAN_HANG_CN_DIEU_CHINH_GIAM"),
	
	HOA_DON_DICH_VU_GOC(1, "HOA_DON_DICH_VU_GOC"),
	HOA_DON_DICH_VU_DIEU_CHINH_TANG(2, "HOA_DON_DICH_VU_DIEU_CHINH_TANG"),
	HOA_DON_DICH_VU_DIEU_CHINH_GIAM(3, "HOA_DON_DICH_VU_DIEU_CHINH_GIAM"),
	
	HOA_DON_XUAT_NCC_GOC(1, "HOA_DON_XUAT_NCC_GOC"),
	HOA_DON_XUAT_NCC_DIEU_CHINH_TANG(2, "HOA_DON_XUAT_NCC_DIEU_CHINH_TANG"),
	HOA_DON_XUAT_NCC_DIEU_CHINH_GIAM(3, "HOA_DON_XUAT_NCC_DIEU_CHINH_GIAM"),
	
	HOA_DON_NOI_BO_GOC(1, "HOA_DON_NOI_BO_GOC"),
	HOA_DON_NOI_BO_DIEU_CHINH_TANG(2, "HOA_DON_NOI_BO_DIEU_CHINH_TANG"),
	HOA_DON_NOI_BO_DIEU_CHINH_GIAM(3, "HOA_DON_NOI_BO_DIEU_CHINH_GIAM");
	
	private final Integer value;

	private final String title;

	InvoiceMode(Integer value, String title) {
		this.value = value;
		this.title = title;
	}

	public Integer value() {
		return this.value;
	}

	public String title() {
		return this.title;
	}

	public static InvoiceMode fromValue(Integer value) {
		for (InvoiceMode e : InvoiceMode.values()) {
			if (value == e.value) {
				return e;
			}
		}
		return null;
	}
}
