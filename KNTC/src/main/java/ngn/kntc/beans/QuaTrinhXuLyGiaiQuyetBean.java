package ngn.kntc.beans;

import java.util.Date;

public class QuaTrinhXuLyGiaiQuyetBean {
	private int maQuaTrinh;
	private String noiDung;
	private Date ngayDang;
	private String tenFileDinhKem;
	private String linkFileDinhKem;
	private int maDonThu;
	private long UserNhap;
	private boolean heThongTao;
	
	public int getMaQuaTrinh() {
		return maQuaTrinh;
	}
	public void setMaQuaTrinh(int maQuaTrinh) {
		this.maQuaTrinh = maQuaTrinh;
	}
	public String getNoiDung() {
		return noiDung;
	}
	public void setNoiDung(String noiDung) {
		this.noiDung = noiDung;
	}
	public Date getNgayDang() {
		return ngayDang;
	}
	public void setNgayDang(Date ngayDang) {
		this.ngayDang = ngayDang;
	}
	public String getTenFileDinhKem() {
		return tenFileDinhKem;
	}
	public void setTenFileDinhKem(String tenFileDinhKem) {
		this.tenFileDinhKem = tenFileDinhKem;
	}
	public String getLinkFileDinhKem() {
		return linkFileDinhKem;
	}
	public void setLinkFileDinhKem(String linkFileDinhKem) {
		this.linkFileDinhKem = linkFileDinhKem;
	}
	public int getMaDonThu() {
		return maDonThu;
	}
	public void setMaDonThu(int maDonThu) {
		this.maDonThu = maDonThu;
	}
	public long getUserNhap() {
		return UserNhap;
	}
	public void setUserNhap(long userNhap) {
		UserNhap = userNhap;
	}
	public boolean isHeThongTao() {
		return heThongTao;
	}
	public void setHeThongTao(boolean heThongTao) {
		this.heThongTao = heThongTao;
	}
}
