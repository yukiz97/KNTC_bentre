package ngn.kntc.beans;

import java.util.Date;

public class FilterDonDaTiepNhanBean {
	int hanXuLy;
	String keyWord;
	Date ngayNhapStart;
	Date ngayNhapEnd;
	int loaiDonThu;
	int dieuKienXuLy;
	
	public int getHanXuLy() {
		return hanXuLy;
	}
	public void setHanXuLy(int hanXuLy) {
		this.hanXuLy = hanXuLy;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public Date getNgayNhapStart() {
		return ngayNhapStart;
	}
	public void setNgayNhapStart(Date ngayNhapStart) {
		this.ngayNhapStart = ngayNhapStart;
	}
	public Date getNgayNhapEnd() {
		return ngayNhapEnd;
	}
	public void setNgayNhapEnd(Date ngayNhapEnd) {
		this.ngayNhapEnd = ngayNhapEnd;
	}
	public int getLoaiDonThu() {
		return loaiDonThu;
	}
	public void setLoaiDonThu(int loaiDonThu) {
		this.loaiDonThu = loaiDonThu;
	}
	public int getDieuKienXuLy() {
		return dieuKienXuLy;
	}
	public void setDieuKienXuLy(int dieuKienXuLy) {
		this.dieuKienXuLy = dieuKienXuLy;
	}
}
