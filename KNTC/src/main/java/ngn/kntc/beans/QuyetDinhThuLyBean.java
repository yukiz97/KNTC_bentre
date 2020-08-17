package ngn.kntc.beans;

import java.util.Date;

public class QuyetDinhThuLyBean {
	private int idQuyetDinhThuLy;
	private String maCoQuanThuLy;
	private Date ngayThuLy;
	private Date hanGiaiQuyet;
	private String canBoDuyet;
	private String tenFileDinhKem;
	private String linkFileDinhKem;
	private int idHoSo;
	private Date ngayTao;
	
	public int getIdQuyetDinhThuLy() {
		return idQuyetDinhThuLy;
	}
	public void setIdQuyetDinhThuLy(int idQuyetDinhThuLy) {
		this.idQuyetDinhThuLy = idQuyetDinhThuLy;
	}
	public String getMaCoQuanThuLy() {
		return maCoQuanThuLy;
	}
	public void setMaCoQuanThuLy(String maCoQuanThuLy) {
		this.maCoQuanThuLy = maCoQuanThuLy;
	}
	public Date getNgayThuLy() {
		return ngayThuLy;
	}
	public void setNgayThuLy(Date ngayThuLy) {
		this.ngayThuLy = ngayThuLy;
	}
	public Date getHanGiaiQuyet() {
		return hanGiaiQuyet;
	}
	public void setHanGiaiQuyet(Date hanGiaiQuyet) {
		this.hanGiaiQuyet = hanGiaiQuyet;
	}
	public String getCanBoDuyet() {
		return canBoDuyet;
	}
	public void setCanBoDuyet(String canBoDuyet) {
		this.canBoDuyet = canBoDuyet;
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
	public int getMaDonThu() {
		return idHoSo;
	}
	public void setMaDonThu(int idHoSo) {
		this.idHoSo = idHoSo;
	}
	public Date getNgayTao() {
		return ngayTao;
	}
	public void setNgayTao(Date ngayTao) {
		this.ngayTao = ngayTao;
	}
	
}
