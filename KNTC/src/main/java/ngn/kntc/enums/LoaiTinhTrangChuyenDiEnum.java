package ngn.kntc.enums;

public enum LoaiTinhTrangChuyenDiEnum {
	chuaxuly(1,"Đơn thư chưa xử lý"),
	chuaxulytronghan(2,"Trong hạn xử lý"),
	chuaxulyquahan(3,"Quá hạn xử lý"),
	daxuly(4,"Đơn thư đã xử lý"),
	daxulytronghan(5,"Đã xử lý đúng hạn"),
	daxulyquahan(6,"Đã xử lý quá hạn"),
	chuagiaiquyet(7,"Đơn thư chưa  giải quyết"),
	chuagiaiquyettronghan(8,"Còn hạn giải quyết"),
	chuagiaiquyetquahan(9,"Quá hạn giải quyết"),
	dagiaiquyet(10,"Đơn thư đã giải quyết"),
	dagiaiquyettronghan(11,"Đã giải quyết đúng hạn"),
	dagiaiquyetquahan(12,"Đã giải quyết quá hạn");
	
	private int type;
	private String name;
	
	private LoaiTinhTrangChuyenDiEnum(int type,String name) {
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
