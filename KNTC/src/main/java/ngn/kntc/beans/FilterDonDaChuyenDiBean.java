package ngn.kntc.beans;

import java.util.Date;

public class FilterDonDaChuyenDiBean {
	String keyWord;
	Date ngayChuyenStart;
	Date ngayChuyenEnd;
	int loaiDonThu;
	
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public Date getNgayChuyenStart() {
		return ngayChuyenStart;
	}
	public void setNgayChuyenStart(Date ngayChuyenStart) {
		this.ngayChuyenStart = ngayChuyenStart;
	}
	public Date getNgayChuyenEnd() {
		return ngayChuyenEnd;
	}
	public void setNgayChuyenEnd(Date ngayChuyenEnd) {
		this.ngayChuyenEnd = ngayChuyenEnd;
	}
	public int getLoaiDonThu() {
		return loaiDonThu;
	}
	public void setLoaiDonThu(int loaiDonThu) {
		this.loaiDonThu = loaiDonThu;
	}
}
