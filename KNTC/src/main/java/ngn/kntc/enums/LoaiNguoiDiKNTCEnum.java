package ngn.kntc.enums;

public enum LoaiNguoiDiKNTCEnum {
	canhan(1,"Cá nhân"),
	doandongnguoi(2,"Đoàn đông người"),
	coquantochuc(3,"Cơ quan tổ chức");
	
	private int type;
	private String name;
	private LoaiNguoiDiKNTCEnum(int type,String name) {
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
