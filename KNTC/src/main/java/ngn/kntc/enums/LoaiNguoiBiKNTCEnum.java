package ngn.kntc.enums;

public enum LoaiNguoiBiKNTCEnum {
	canhan(1,"Cá nhân"),
	coquantochuc(2,"Cơ quan, tổ chức");
	
	private int type;
	private String name;
	private LoaiNguoiBiKNTCEnum(int type,String name) {
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
