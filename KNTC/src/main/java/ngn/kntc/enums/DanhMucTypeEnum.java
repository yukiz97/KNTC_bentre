package ngn.kntc.enums;

public enum DanhMucTypeEnum {
	diagioi(1,"dia_gioi","String"),
	nguondon(2,"nguon_don","Integer"),
	quoctich(3,"quoc_gia","String"),
	dantoc(4,"dan_toc","Integer"),
	loaiquyetdinh(5,"loai_ket_qua_giai_quyet","Integer"),
	linhvuc(6,"linh_vuc","String"),
	loaitailieu(7,"loai_tai_lieu","Integer"),
	coquan(8,"co_quan","String"),
	huongxuly(9,"huong_xu_ly","Integer"),
	hinhthucgiaiquyet(10,"hinh_thuc_giai_quyet","Integer");
	
	private int type;
	private String name;
	private String idType;
	
	private DanhMucTypeEnum(int type,String name,String idType) {
		this.type = type;
		this.name = name;
		this.idType = idType;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
}
