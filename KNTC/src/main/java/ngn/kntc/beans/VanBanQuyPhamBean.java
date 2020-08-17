package ngn.kntc.beans;

import java.util.Date;

public class VanBanQuyPhamBean {
	private int id;
	private String tenVanBan;
	private String soHieu;
	private String trichDan;
	private String coQuanBanHanh;
	private String nguoiKy;
	private Date ngayBanHanh;
	private Date ngayTao;
	private String loaiVanBan;
	private String tenFileDinhKem;
	private String linkFileDinhKem;
	private int owner;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTenVanBan() {
		return tenVanBan;
	}
	public void setTenVanBan(String tenVanBan) {
		this.tenVanBan = tenVanBan;
	}
	public String getSoHieu() {
		return soHieu;
	}
	public void setSoHieu(String soHieu) {
		this.soHieu = soHieu;
	}
	public String getTrichDan() {
		return trichDan;
	}
	public void setTrichDan(String trichDan) {
		this.trichDan = trichDan;
	}
	public String getCoQuanBanHanh() {
		return coQuanBanHanh;
	}
	public void setCoQuanBanHanh(String coQuanBanHanh) {
		this.coQuanBanHanh = coQuanBanHanh;
	}
	public String getNguoiKy() {
		return nguoiKy;
	}
	public void setNguoiKy(String nguoiKy) {
		this.nguoiKy = nguoiKy;
	}
	public Date getNgayBanHanh() {
		return ngayBanHanh;
	}
	public Date getNgayTao() {
		return ngayTao;
	}
	public void setNgayTao(Date ngayTao) {
		this.ngayTao = ngayTao;
	}
	public void setNgayBanHanh(Date ngayBanHanh) {
		this.ngayBanHanh = ngayBanHanh;
	}
	public String getLoaiVanBan() {
		return loaiVanBan;
	}
	public void setLoaiVanBan(String loaiVanBan) {
		this.loaiVanBan = loaiVanBan;
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
	public int getOwner() {
		return owner;
	}
	public void setOwner(int owner) {
		this.owner = owner;
	}
}
