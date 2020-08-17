package ngn.kntc.enums;

public enum HanXuLyDaXuLyEnum {
	tatca(1,"Tất cả"),
	xulytronghan(2,"Xử lý trong hạn"),
	xulyquahan(3,"Xử lý quá hạn");
	
	private int type;
	private String name;
	
	private HanXuLyDaXuLyEnum(int type,String name) {
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
