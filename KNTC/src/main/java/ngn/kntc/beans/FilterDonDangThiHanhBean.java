package ngn.kntc.beans;

import java.util.Date;

public class FilterDonDangThiHanhBean {
	String keyWord;
	Date ngayGiaiQuyetStart;
	Date ngayGiaiQuyetEnd;
	int loaiDonThu;
	int ketQuaGiaiQuyet;
	int hinhThucGiaiQuyet;
	
	public Date getNgayGiaiQuyetStart() {
		return ngayGiaiQuyetStart;
	}
	public void setNgayGiaiQuyetStart(Date ngayGiaiQuyetStart) {
		this.ngayGiaiQuyetStart = ngayGiaiQuyetStart;
	}
	public Date getNgayGiaiQuyetEnd() {
		return ngayGiaiQuyetEnd;
	}
	public void setNgayGiaiQuyetEnd(Date ngayGiaiQuyetEnd) {
		this.ngayGiaiQuyetEnd = ngayGiaiQuyetEnd;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public int getLoaiDonThu() {
		return loaiDonThu;
	}
	public void setLoaiDonThu(int loaiDonThu) {
		this.loaiDonThu = loaiDonThu;
	}
	public int getKetQuaGiaiQuyet() {
		return ketQuaGiaiQuyet;
	}
	public void setKetQuaGiaiQuyet(int ketQuaGiaiQuyet) {
		this.ketQuaGiaiQuyet = ketQuaGiaiQuyet;
	}
	public int getHinhThucGiaiQuyet() {
		return hinhThucGiaiQuyet;
	}
	public void setHinhThucGiaiQuyet(int hinhThucGiaiQuyet) {
		this.hinhThucGiaiQuyet = hinhThucGiaiQuyet;
	}
}
