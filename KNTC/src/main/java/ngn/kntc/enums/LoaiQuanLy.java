package ngn.kntc.enums;

public enum LoaiQuanLy {
	donvi(1,"Thẩm quyền đơn vị"),
	thammuu(2,"Tham mưu giải quyết");
	
	private int type;
	private String name;
	
	private LoaiQuanLy(int type,String name) {
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
