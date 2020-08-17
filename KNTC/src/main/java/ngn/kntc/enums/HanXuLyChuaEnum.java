package ngn.kntc.enums;

public enum HanXuLyChuaEnum {
	tatca(1,"Tất cả"),
	conhanxuly(2,"Còn hạn xử lý"),
	quahanxuly(3,"Quá hạn xử lý");
	
	private int type;
	private String name;
	
	private HanXuLyChuaEnum(int type,String name) {
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
