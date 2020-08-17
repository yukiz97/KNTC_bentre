package ngn.kntc.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TraCuuBean {
	private String keyWord;
	private DoiTuongDiKNTCBean chuThe = null;
	private int nguonDon;
	private int loaiDonThu;
	private List<String> listLinhVuc = new ArrayList<String>();
	private boolean isHasNgayNhap;
	private Date ngayNhapStart;
	private Date ngayNhapEnd;
	private boolean isHasNgayThuLy;
	private Date ngayThuLyStart;
	private Date ngayThuLyEnd;
	private boolean isHasNgayHanGiaiQuyet;
	private Date ngayHanGQStart;
	private Date ngayHanGQEnd;
	
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public DoiTuongDiKNTCBean getChuThe() {
		return chuThe;
	}
	public void setChuThe(DoiTuongDiKNTCBean chuThe) {
		this.chuThe = chuThe;
	}
	public int getNguonDon() {
		return nguonDon;
	}
	public void setNguonDon(int nguonDon) {
		this.nguonDon = nguonDon;
	}
	public int getLoaiDonThu() {
		return loaiDonThu;
	}
	public void setLoaiDonThu(int loaiDonThu) {
		this.loaiDonThu = loaiDonThu;
	}
	public List<String> getListLinhVuc() {
		return listLinhVuc;
	}
	public void setListLinhVuc(List<String> listLinhVuc) {
		this.listLinhVuc = listLinhVuc;
	}
	public boolean isHasNgayNhap() {
		return isHasNgayNhap;
	}
	public void setHasNgayNhap(boolean isHasNgayNhap) {
		this.isHasNgayNhap = isHasNgayNhap;
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
	public boolean isHasNgayThuLy() {
		return isHasNgayThuLy;
	}
	public void setHasNgayThuLy(boolean isHasNgayThuLy) {
		this.isHasNgayThuLy = isHasNgayThuLy;
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
	public boolean isHasNgayHanGiaiQuyet() {
		return isHasNgayHanGiaiQuyet;
	}
	public void setHasNgayHanGiaiQuyet(boolean isHasNgayHanGiaiQuyet) {
		this.isHasNgayHanGiaiQuyet = isHasNgayHanGiaiQuyet;
	}
	public Date getNgayHanGQStart() {
		return ngayHanGQStart;
	}
	public void setNgayHanGQStart(Date ngayHanGQStart) {
		this.ngayHanGQStart = ngayHanGQStart;
	}
	public Date getNgayHanGQEnd() {
		return ngayHanGQEnd;
	}
	public void setNgayHanGQEnd(Date ngayHanGQEnd) {
		this.ngayHanGQEnd = ngayHanGQEnd;
	}
}
