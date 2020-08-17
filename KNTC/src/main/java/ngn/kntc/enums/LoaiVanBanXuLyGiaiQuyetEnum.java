package ngn.kntc.enums;

public enum LoaiVanBanXuLyGiaiQuyetEnum {
	huongdan(1,"Hướng dẫn"),
	quyetdinh(2,"Quyết định"),
	cvchidao(3,"Công văn chỉ đạo"),
	cvdondoc(4,"Công văn đôn đốc"),
	cvbaocao(5,"Công văn báo cáo"),
	tochucdoithoai(6,"Tổ chức đối thoại");
	
	private int type;
	private String name;
	
	private LoaiVanBanXuLyGiaiQuyetEnum(int type,String name) {
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
