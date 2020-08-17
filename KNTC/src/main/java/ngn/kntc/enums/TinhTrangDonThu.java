package ngn.kntc.enums;

public enum TinhTrangDonThu {
	rutdon(-1,"Rút đơn"),
	dangthuchien(0,"Đang thực hiện"),
	ketthuchoso(1,"Kết thúc hồ sơ"),
	vuviecphuctap(2,"Vụ việc phức tạp");
	
	private int type;
	private String name;
	
	private TinhTrangDonThu(int type,String name) {
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
