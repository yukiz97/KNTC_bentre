package ngn.kntc.enums;

public enum HinhThucXuPhatHanhChinhEnum {
	canhcao(1,"Cảnh cáo"),
	phattien(2,"Phạt tiền"),
	tuocquyen(3,"Tước quyền sử dụng giấy phép, chứng chỉ hành nghề có thời hạn hoặc đình chỉ hoạt động có thời hạn"),
	tichthu(4,"Tịch thu tang vật vi phạm hành chính, phương tiện được sử dụng để vi phạm hành chính"),
	trucxuat(5,"Trục xuất");
	
	private int type;
	private String name;
	
	private HinhThucXuPhatHanhChinhEnum(int type,String name) {
		this.type = type;
		this.name = name;
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
}
