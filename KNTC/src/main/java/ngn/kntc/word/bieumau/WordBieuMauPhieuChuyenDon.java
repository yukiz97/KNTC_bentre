package ngn.kntc.word.bieumau;

import ngn.kntc.beans.DoiTuongDiKNTCBean;
import ngn.kntc.beans.DonThuBean;
import ngn.kntc.beans.ThongTinDonThuBean;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.utils.SessionUtil;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.liferay.portal.service.OrganizationLocalServiceUtil;

public class WordBieuMauPhieuChuyenDon extends WordBieuMauPattern{
	public WordBieuMauPhieuChuyenDon() {
	}

	@Override
	public void createTitle() throws Exception {
		DoiTuongDiKNTCBean modelDoiTuongDiKNTC = svDonThu.getNguoiDaiDienDonThu(idDonThu).get(0);
		
		XWPFParagraph para1 = document.createParagraph();
		runAllRun = para1.createRun();
		runAllRun.addBreak();
		runAllRun.addBreak();
		para1.setAlignment(ParagraphAlignment.CENTER);
		setRun(para1, fontTimeNewRoman, 14,"PHIẾU CHUYỂN ĐƠN", true,true);
		setRun(para1, fontTimeNewRoman, 14,"________", true,false);
	}

	@Override
	public void createContent() throws Exception {
		DoiTuongDiKNTCBean modelDoiTuongDiKNTC = svDonThu.getNguoiDaiDienDonThu(idDonThu).get(0);
		DonThuBean modelDonThu = svDonThu.getDonThu(idDonThu);
		ThongTinDonThuBean modelThongTinDon = svDonThu.getThongTinDonThuChuyenDi(idDonThu, SessionUtil.getOrgId());
		
		String valueThuTruongCoQuan = OrganizationLocalServiceUtil.getOrganization(modelThongTinDon.getOrgNhan()).getName();
		String valueKinhGui = "Kính gửi: "+valueThuTruongCoQuan;
		
		String valueTenCoQuanDonViChuyen = OrganizationLocalServiceUtil.getOrganization(SessionUtil.getOrgId()).getName();
		String valueNhanDon = valueTenCoQuanDonViChuyen+" nhận được đơn đề ngày "+sdf.format(modelDonThu.getNgayNhapDon());
		String valueChuTheDonThu = "Đơn ghi tên ông (bà): "+modelDoiTuongDiKNTC.getHoTen();
		String valueDiaChi = "Địa chỉ: "+DonThuModule.returnDiaChiChiTiet(modelDoiTuongDiKNTC.getDiaChiChiTiet(), modelDoiTuongDiKNTC.getMaTinh(), modelDoiTuongDiKNTC.getMaHuyen(), modelDoiTuongDiKNTC.getMaXa());
		String valueNoiDungDon = "Nội dung đơn: "+modelDonThu.getNoiDungDonThu();
		
		String valueLuatDangThiHanh = "";
		String valueNghiDinhChinhPhu ="";
		String valueThongTuBoCongAn = "";
		String valueCoQuanToChucCoThamQuyenChuyenDon = "";
		String valueMain = "Căn cứ nội dung đơn, "+valueLuatDangThiHanh+", "+valueNghiDinhChinhPhu+" và "+valueThongTuBoCongAn
				+valueTenCoQuanDonViChuyen+" đã chuyển đơn đến "+valueThuTruongCoQuan
				+" để xem xét giải quyết theo quy định của pháp luật; Thông báo kết quả cho "+valueTenCoQuanDonViChuyen
				+" và "+valueCoQuanToChucCoThamQuyenChuyenDon+" (nếu có) biết./.";
		
		XWPFParagraph para5 = document.createParagraph();
		runAllRun = para5.createRun();
		runAllRun.addTab();runAllRun.addTab();runAllRun.addTab();
		runAllRun = setRun(para5, fontTimeNewRoman, 14,valueKinhGui, false,true);
		runAllRun.addTab();
		runAllRun = setRun(para5, fontTimeNewRoman, 14,valueNhanDon, false,true);
		runAllRun.addTab();
		runAllRun = setRun(para5, fontTimeNewRoman, 14,valueChuTheDonThu, false,true);
		runAllRun.addTab();
		runAllRun = setRun(para5, fontTimeNewRoman, 14,valueDiaChi, false,true);
		runAllRun.addTab();
		runAllRun = setRun(para5, fontTimeNewRoman, 14,valueNoiDungDon, false,true);
		runAllRun.addTab();
		runAllRun = setRun(para5, fontTimeNewRoman, 14,valueMain, false,true);
		para5.setSpacingBetween(1.5);
	}

	@Override
	public void createFooter() throws Exception {
		String valueChucVuCuaNguoiKyThongBao = SessionUtil.getUser().getFirstName();
		
		XWPFTable table = document.createTable();
		table.setWidth("100%");
		table.getCTTbl().getTblPr().unsetTblBorders();
		table.removeRow(0);
		
		XWPFTableRow row1 = table.createRow();	
		
		XWPFTableCell cell1 = row1.createCell();
		cell1.setWidth("35%");
		XWPFParagraph para1 = cell1.addParagraph();
		setRun(para1, "Times New Roman", 14,"Nơi nhận", true,true);
		setRun(para1, "Times New Roman", 13,"-Như trên;", false,true);
		setRun(para1, "Times New Roman", 13,"-.... để biết;(nếu có)", false,true);
		setRun(para1, "Times New Roman", 13,"-.... để biết;", false,true);
		setRun(para1, "Times New Roman", 13,"-Lưu:....;", false,true);
		
		XWPFTableCell cell2 = row1.createCell();
		createCellSignature(cell2,valueChucVuCuaNguoiKyThongBao,null);
	}
}
