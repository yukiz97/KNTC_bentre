package ngn.kntc.beans;

import java.util.Date;

public class LuanChuyenBean {
	private int idLuanChuyen;
	private int idDonThu;
	private long idUserChuyen;
	private long idUserNhan;
	private boolean phanCong;
	private Date ngayChuyen;
	
	public int getIdLuanChuyen() {
		return idLuanChuyen;
	}
	public void setIdLuanChuyen(int idLuanChuyen) {
		this.idLuanChuyen = idLuanChuyen;
	}
	public int getIdDonThu() {
		return idDonThu;
	}
	public void setIdDonThu(int idDonThu) {
		this.idDonThu = idDonThu;
	}
	public long getIdUserChuyen() {
		return idUserChuyen;
	}
	public void setIdUserChuyen(long idUserChuyen) {
		this.idUserChuyen = idUserChuyen;
	}
	public long getIdUserNhan() {
		return idUserNhan;
	}
	public void setIdUserNhan(long idUserNhan) {
		this.idUserNhan = idUserNhan;
	}
	public boolean isPhanCong() {
		return phanCong;
	}
	public void setPhanCong(boolean phanCong) {
		this.phanCong = phanCong;
	}
	public Date getNgayChuyen() {
		return ngayChuyen;
	}
	public void setNgayChuyen(Date ngayChuyen) {
		this.ngayChuyen = ngayChuyen;
	}
}
