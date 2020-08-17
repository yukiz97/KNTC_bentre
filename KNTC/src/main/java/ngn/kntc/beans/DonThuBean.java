package ngn.kntc.beans;

import java.util.Date;

public class DonThuBean {
	private int maDonThu;
	private int maDoiTuongBiKNTC;
	private int maDoiTuongUyQuyen;
	private String noiDungDonThu;
	private String thamQuyenGiaiQuyet;
	private int loaiDonThu;
	
	private Date ngayNhanDon;
	private Date ngayNhapDon;
	private Date ngayThuLy;
	private Date ngayHoanThanhGiaiQuyet;
	private Date ngayTraKetQua;
	private Date ngayHanGiaiQuyet;
	private Date ngayCapNhat;
	
	private int loaiNguoiDiKNTC;
	private int soNguoiDiKNTC;
	private int soNguoiDaiDien;
	private String tenCoQuanDiKNTC;
	private String diaChiCoQuanDiKNTC;
	
	private int loaiNguoiBiKNTC;
	
	private int loaiNguoiUyQuyen;
	
	private String maCoQuanDaGiaiQuyet;
	private String soKyHieuVanBanDen;
	private int lanGiaiQuyet;
	private Date ngayBanHanhQDGQ;
	private int loaiQuyetDinhGiaiQuyet;
	private String tomTatNoiDungGiaiQuyet;
	
	private long userThuLy;
	private long userNhapDon;
	
	private boolean donNacDanh;
	private boolean donKhongDuDieuKienXuLy;
	
	private String ganVuViec;
	private String idHoSoLienThong;
	
	private int status;
	
	public int getMaDonThu() {
		return maDonThu;
	}
	public void setMaDonThu(int maDonThu) {
		this.maDonThu = maDonThu;
	}
	public int getMaDoiTuongBiKNTC() {
		return maDoiTuongBiKNTC;
	}
	public void setMaDoiTuongBiKNTC(int maDoiTuongBiKNTC) {
		this.maDoiTuongBiKNTC = maDoiTuongBiKNTC;
	}
	public int getMaDoiTuongUyQuyen() {
		return maDoiTuongUyQuyen;
	}
	public void setMaDoiTuongUyQuyen(int maDoiTuongUyQuyen) {
		this.maDoiTuongUyQuyen = maDoiTuongUyQuyen;
	}
	public String getNoiDungDonThu() {
		return noiDungDonThu;
	}
	public void setNoiDungDonThu(String noiDungDonThu) {
		this.noiDungDonThu = noiDungDonThu;
	}
	public String getThamQuyenGiaiQuyet() {
		return thamQuyenGiaiQuyet;
	}
	public void setThamQuyenGiaiQuyet(String thamQuyenGiaiQuyet) {
		this.thamQuyenGiaiQuyet = thamQuyenGiaiQuyet;
	}
	public int getLoaiDonThu() {
		return loaiDonThu;
	}
	public void setLoaiDonThu(int loaiDonThu) {
		this.loaiDonThu = loaiDonThu;
	}
	public Date getNgayNhanDon() {
		return ngayNhanDon;
	}
	public void setNgayNhanDon(Date ngayNhanDon) {
		this.ngayNhanDon = ngayNhanDon;
	}
	public String getSoKyHieuVanBanDen() {
		return soKyHieuVanBanDen;
	}
	public void setSoKyHieuVanBanDen(String soKyHieuVanBanDen) {
		this.soKyHieuVanBanDen = soKyHieuVanBanDen;
	}
	public Date getNgayNhapDon() {
		return ngayNhapDon;
	}
	public void setNgayNhapDon(Date ngayNhapDon) {
		this.ngayNhapDon = ngayNhapDon;
	}
	public Date getNgayThuLy() {
		return ngayThuLy;
	}
	public void setNgayThuLy(Date ngayThuLy) {
		this.ngayThuLy = ngayThuLy;
	}
	public Date getNgayHoanThanhGiaiQuyet() {
		return ngayHoanThanhGiaiQuyet;
	}
	public void setNgayHoanThanhGiaiQuyet(Date ngayHoanThanhGiaiQuyet) {
		this.ngayHoanThanhGiaiQuyet = ngayHoanThanhGiaiQuyet;
	}
	public Date getNgayTraKetQua() {
		return ngayTraKetQua;
	}
	public String getSoKyHieuVanBanGiaiQuyet() {
		return soKyHieuVanBanDen;
	}
	public void setSoKyHieuVanBanGiaiQuyet(String soKyHieuVanBanDen) {
		this.soKyHieuVanBanDen = soKyHieuVanBanDen;
	}
	public void setNgayTraKetQua(Date ngayTraKetQua) {
		this.ngayTraKetQua = ngayTraKetQua;
	}
	public Date getNgayHanGiaiQuyet() {
		return ngayHanGiaiQuyet;
	}
	public void setNgayHanGiaiQuyet(Date ngayHanGiaiQuyet) {
		this.ngayHanGiaiQuyet = ngayHanGiaiQuyet;
	}
	public Date getNgayCapNhat() {
		return ngayCapNhat;
	}
	public void setNgayCapNhat(Date ngayCapNhat) {
		this.ngayCapNhat = ngayCapNhat;
	}
	public int getLoaiNguoiDiKNTC() {
		return loaiNguoiDiKNTC;
	}
	public void setLoaiNguoiDiKNTC(int loaiNguoiDiKNTC) {
		this.loaiNguoiDiKNTC = loaiNguoiDiKNTC;
	}
	public int getSoNguoiDiKNTC() {
		return soNguoiDiKNTC;
	}
	public void setSoNguoiDiKNTC(int soNguoiDiKNTC) {
		this.soNguoiDiKNTC = soNguoiDiKNTC;
	}
	public int getSoNguoiDaiDien() {
		return soNguoiDaiDien;
	}
	public void setSoNguoiDaiDien(int soNguoiDaiDien) {
		this.soNguoiDaiDien = soNguoiDaiDien;
	}
	public String getTenCoQuanDiKNTC() {
		return tenCoQuanDiKNTC;
	}
	public void setTenCoQuanDiKNTC(String tenCoQuanDiKNTC) {
		this.tenCoQuanDiKNTC = tenCoQuanDiKNTC;
	}
	public String getDiaChiCoQuanDiKNTC() {
		return diaChiCoQuanDiKNTC;
	}
	public void setDiaChiCoQuanDiKNTC(String diaChiCoQuanDiKNTC) {
		this.diaChiCoQuanDiKNTC = diaChiCoQuanDiKNTC;
	}
	public int getLoaiNguoiBiKNTC() {
		return loaiNguoiBiKNTC;
	}
	public void setLoaiNguoiBiKNTC(int loaiNguoiBiKNTC) {
		this.loaiNguoiBiKNTC = loaiNguoiBiKNTC;
	}
	public int getLoaiNguoiUyQuyen() {
		return loaiNguoiUyQuyen;
	}
	public void setLoaiNguoiUyQuyen(int loaiNguoiUyQuyen) {
		this.loaiNguoiUyQuyen = loaiNguoiUyQuyen;
	}
	public String getMaCoQuanDaGiaiQuyet() {
		return maCoQuanDaGiaiQuyet;
	}
	public void setMaCoQuanDaGiaiQuyet(String maCoQuanDaGiaiQuyet) {
		this.maCoQuanDaGiaiQuyet = maCoQuanDaGiaiQuyet;
	}
	public int getLanGiaiQuyet() {
		return lanGiaiQuyet;
	}
	public void setLanGiaiQuyet(int lanGiaiQuyet) {
		this.lanGiaiQuyet = lanGiaiQuyet;
	}
	public long getUserThuLy() {
		return userThuLy;
	}
	public void setUserThuLy(long userThuLy) {
		this.userThuLy = userThuLy;
	}
	public long getUserNhapDon() {
		return userNhapDon;
	}
	public void setUserNhapDon(long userNhapDon) {
		this.userNhapDon = userNhapDon;
	}
	public Date getNgayBanHanhQDGQ() {
		return ngayBanHanhQDGQ;
	}
	public void setNgayBanHanhQDGQ(Date ngayBanHanhQDGQ) {
		this.ngayBanHanhQDGQ = ngayBanHanhQDGQ;
	}
	public int getLoaiQuyetDinhGiaiQuyet() {
		return loaiQuyetDinhGiaiQuyet;
	}
	public void setLoaiQuyetDinhGiaiQuyet(int loaiQuyetDinhGiaiQuyet) {
		this.loaiQuyetDinhGiaiQuyet = loaiQuyetDinhGiaiQuyet;
	}
	public String getTomTatNoiDungGiaiQuyet() {
		return tomTatNoiDungGiaiQuyet;
	}
	public void setTomTatNoiDungGiaiQuyet(String tomTatNoiDungGiaiQuyet) {
		this.tomTatNoiDungGiaiQuyet = tomTatNoiDungGiaiQuyet;
	}
	public boolean isDonNacDanh() {
		return donNacDanh;
	}
	public void setDonNacDanh(boolean donNacDanh) {
		this.donNacDanh = donNacDanh;
	}
	public boolean isDonKhongDuDieuKienXuLy() {
		return donKhongDuDieuKienXuLy;
	}
	public void setDonKhongDuDieuKienXuLy(boolean donKhongDuDieuKienXuLy) {
		this.donKhongDuDieuKienXuLy = donKhongDuDieuKienXuLy;
	}
	public String getGanVuViec() {
		return ganVuViec;
	}
	public void setGanVuViec(String ganVuViec) {
		this.ganVuViec = ganVuViec;
	}
	public String getIdHoSoLienThong() {
		return idHoSoLienThong;
	}
	public void setIdHoSoLienThong(String idHoSoLienThong) {
		this.idHoSoLienThong = idHoSoLienThong;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
