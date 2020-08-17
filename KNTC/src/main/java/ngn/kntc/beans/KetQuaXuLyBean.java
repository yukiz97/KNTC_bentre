package ngn.kntc.beans;

import java.util.Date;

public class KetQuaXuLyBean {
	private int idKetQua;
	/* Nội dung xử lý */
	private String tomTatNoiDung;
	private Date ngayXuLy;
	private String canBoXuLy;
	private String canBoDuyet;
	/* Hướng xử lý */
	private String maCQXL;
	private String maCQXLTiep;
	private int maHuongXuLy;
	private String noiDungXuLy;
	private String linkFileDinhKem;
	private String tenFileDinhKem;
	private int idHoSo;
	private Date ngayTao;
	private long OrgTao;
	
	public int getIdKetQua() {
		return idKetQua;
	}
	public void setIdKetQua(int idKetQua) {
		this.idKetQua = idKetQua;
	}
	public String getTomTatNoiDung() {
		return tomTatNoiDung;
	}
	public void setTomTatNoiDung(String tomTatNoiDung) {
		this.tomTatNoiDung = tomTatNoiDung;
	}
	public Date getNgayXuLy() {
		return ngayXuLy;
	}
	public void setNgayXuLy(Date ngayXuLy) {
		this.ngayXuLy = ngayXuLy;
	}
	public String getCanBoXuLy() {
		return canBoXuLy;
	}
	public void setCanBoXuLy(String canBoXuLy) {
		this.canBoXuLy = canBoXuLy;
	}
	public String getCanBoDuyet() {
		return canBoDuyet;
	}
	public void setCanBoDuyet(String canBoDuyet) {
		this.canBoDuyet = canBoDuyet;
	}
	public String getMaCQXL() {
		return maCQXL;
	}
	public void setMaCQXL(String maCQXL) {
		this.maCQXL = maCQXL;
	}
	public String getMaCQXLTiep() {
		return maCQXLTiep;
	}
	public void setMaCQXLTiep(String maCQXLTiep) {
		this.maCQXLTiep = maCQXLTiep;
	}
	public int getIdHoSo() {
		return idHoSo;
	}
	public void setMaDonThu(int idHoSo) {
		this.idHoSo = idHoSo;
	}
	public int getMaHuongXuLy() {
		return maHuongXuLy;
	}
	public void setMaHuongXuLy(int maHuongXuLy) {
		this.maHuongXuLy = maHuongXuLy;
	}
	public String getNoiDungXuLy() {
		return noiDungXuLy;
	}
	public void setNoiDungXuLy(String noiDungXuLy) {
		this.noiDungXuLy = noiDungXuLy;
	}
	public String getLinkFileDinhKem() {
		return linkFileDinhKem;
	}
	public void setLinkFileDinhKem(String linkFileDinhKem) {
		this.linkFileDinhKem = linkFileDinhKem;
	}
	public String getTenFileDinhKem() {
		return tenFileDinhKem;
	}
	public void setTenFileDinhKem(String tenFileDinhKem) {
		this.tenFileDinhKem = tenFileDinhKem;
	}
	public Date getNgayTao() {
		return ngayTao;
	}
	public void setNgayTao(Date ngayTao) {
		this.ngayTao = ngayTao;
	}
	public long getOrgTao() {
		return OrgTao;
	}
	public void setOrgTao(long orgTao) {
		OrgTao = orgTao;
	}
}
