package ngn.kntc.beans;

import java.util.Date;

public class FilterDonNhanTuDonViKhacBean {
	int hanXuLy;
	String keyWord;
	Date ngayNhanStart;
	Date ngayNhanEnd;
	int loaiDonThu;
	
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
	public Date getNgayNhanStart() {
		return ngayNhanStart;
	}
	public void setNgayNhanStart(Date ngayNhanStart) {
		this.ngayNhanStart = ngayNhanStart;
	}
	public Date getNgayNhanEnd() {
		return ngayNhanEnd;
	}
	public void setNgayNhanEnd(Date ngayNhanEnd) {
		this.ngayNhanEnd = ngayNhanEnd;
	}
	public int getLoaiDonThu() {
		return loaiDonThu;
	}
	public void setLoaiDonThu(int loaiDonThu) {
		this.loaiDonThu = loaiDonThu;
	}
}
