package ngn.kntc.beans;

import java.util.Date;

public class FilterDonCanGiaiQuyetBean {
	String keyWord;
	Date ngayThuLyStart;
	Date ngayThuLyEnd;
	Date hanGiaiQuyetStart;
	Date hanGiaiQuyetEnd;
	int loaiDonThu;
	
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public Date getNgayThuLyStart() {
		return ngayThuLyStart;
	}
	public void setNgayThuLyStart(Date ngayThuLyStart) {
		this.ngayThuLyStart = ngayThuLyStart;
	}
	public Date getNgayThuLyEnd() {
		return ngayThuLyEnd;
	}
	public void setNgayThuLyEnd(Date ngayThuLyEnd) {
		this.ngayThuLyEnd = ngayThuLyEnd;
	}
	public Date getHanGiaiQuyetStart() {
		return hanGiaiQuyetStart;
	}
	public void setHanGiaiQuyetStart(Date hanGiaiQuyetStart) {
		this.hanGiaiQuyetStart = hanGiaiQuyetStart;
	}
	public Date getHanGiaiQuyetEnd() {
		return hanGiaiQuyetEnd;
	}
	public void setHanGiaiQuyetEnd(Date hanGiaiQuyetEnd) {
		this.hanGiaiQuyetEnd = hanGiaiQuyetEnd;
	}
	public int getLoaiDonThu() {
		return loaiDonThu;
	}
	public void setLoaiDonThu(int loaiDonThu) {
		this.loaiDonThu = loaiDonThu;
	}
}
