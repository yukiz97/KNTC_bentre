package ngn.kntc.enums;

public enum LoaiDonThuEnum {
	khieunai(1,"Khiếu nại"),
	tocao(2,"Tố cáo"),
	phananhkiennghi(3,"Phản ánh, kiến nghị"),
	conhieunoidung(4,"Đơn có nhiều nội dung"),
	khac(5,"Khác");
	
	private int type;
	private String name;
	
	private LoaiDonThuEnum(int type,String name) {
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
