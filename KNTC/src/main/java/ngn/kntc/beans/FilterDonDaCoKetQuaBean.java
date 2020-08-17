package ngn.kntc.beans;

import java.util.Date;

public class FilterDonDaCoKetQuaBean {
	int hanXuLy;
	String keyWord;
	Date ngayXuLyStart;
	Date ngayXuLyEnd;
	int loaiDonThu;
	int huongXuLy;
	
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
	public Date getNgayXuLyStart() {
		return ngayXuLyStart;
	}
	public void setNgayXuLyStart(Date ngayXuLyStart) {
		this.ngayXuLyStart = ngayXuLyStart;
	}
	public Date getNgayXuLyEnd() {
		return ngayXuLyEnd;
	}
	public void setNgayXuLyEnd(Date ngayXuLyEnd) {
		this.ngayXuLyEnd = ngayXuLyEnd;
	}
	public int getLoaiDonThu() {
		return loaiDonThu;
	}
	public void setLoaiDonThu(int loaiDonThu) {
		this.loaiDonThu = loaiDonThu;
	}
	public int getHuongXuLy() {
		return huongXuLy;
	}
	public void setHuongXuLy(int huongXuLy) {
		this.huongXuLy = huongXuLy;
	}
}
