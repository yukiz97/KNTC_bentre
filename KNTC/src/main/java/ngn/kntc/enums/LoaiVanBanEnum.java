package ngn.kntc.enums;

public enum LoaiVanBanEnum {
	vbapdung(1,"Văn bản áp dụng"),
	vbchidao(2,"Văn bản chỉ đạo"),
	vbphapluat(3,"Văn bản pháp luật");
	
	private int type;
	private String name;
	private LoaiVanBanEnum(int type,String name) {
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
