package ngn.kntc.enums;

public enum LoaiNguoiUyQuyenEnum {
	luatsu(1,"Luật sư"),
	trogiupvienphaply(2,"Trợ giúp viên pháp lý");
	
	private int type;
	private String name;
	private LoaiNguoiUyQuyenEnum(int type,String name) {
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
