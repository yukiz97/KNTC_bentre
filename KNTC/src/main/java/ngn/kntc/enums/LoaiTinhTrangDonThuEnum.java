package ngn.kntc.enums;

public enum LoaiTinhTrangDonThuEnum {
	datiepnhan(1),
	donvikhacchuyenden(2),
	dacokq(3),
	canthuly(4),
	dathuly(5),
	cangiaiquyet(6),
	dangthihanh(7),
	daketthuc(8),
	phuctapkeodai(9),
	donduocrut(10),
	chuyendonchuagq(11),
	chuyendondagq(12),
	chuyendonluutru(13),
	thammuudagiaiquyet(14),
	thammuuchuagiaiquyet(15),
	chuyendonauto(16);
	 
	private int type;
	private LoaiTinhTrangDonThuEnum(int type) {
		this.type = type;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
