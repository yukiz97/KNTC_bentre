package ngn.kntc.enums;

public enum LoaiBaoCao {
	a2(1,"Báo cáo 2a - Tổng hợp kết quả tiếp công dân"),
	b2(2,"Báo cáo 2b - Tổng hợp kết quả xử lý đơn thư khiếu nại, tố cáo"),
	c2(3,"Báo cáo 2c - Tổng hợp giải quyết đơn thư khiếu nại"),
	d2(4,"Báo cáo 2d - Tổng hợp giải quyết đơn thư tố cáo");
	
	private int type;
	private String name;
	
	private LoaiBaoCao(int type,String name) {
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
