package ngn.kntc.beans;

import java.util.Date;

public class FilterDonCanThuLyBean {
	String keyWord;
	Date ngayXuLyStart;
	Date ngayXuLyEnd;
	int loaiDonThu;
	
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
}
