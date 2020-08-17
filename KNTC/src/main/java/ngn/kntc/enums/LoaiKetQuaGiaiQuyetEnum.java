package ngn.kntc.enums;

public enum LoaiKetQuaGiaiQuyetEnum {
	khieunaidungmotphan(1,"Khiếu nại đúng một phần"),
	khieunaidungtoanbo(2,"Khiếu nại đúng toàn bộ"),
	tocaodung(3,"Tố cáo đúng"),
	tocaosai(4,"Tố cáo sai"),
	tocaodungmotphan(5,"Tố cáo đúng một phần"),
	khieunaisaitoanbo(6,"Khiếu nại sai toàn bộ"),
	nguoitocaocoytocaosaisuthat(7,"Người tố cáo cố ý tố cáo sai sự thật"),
	chuyencoquandieutra(8,"Chuyển cơ quan điều tra");
	
	private int type;
	private String name;
	
	private LoaiKetQuaGiaiQuyetEnum(int type,String name) {
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
