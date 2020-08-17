package ngn.kntc.word.bieumau;

import ngn.kntc.beans.DoiTuongDiKNTCBean;
import ngn.kntc.beans.DonThuBean;
import ngn.kntc.beans.ThongTinDonThuBean;
import ngn.kntc.enums.LoaiDonThuEnum;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.utils.SessionUtil;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.liferay.portal.service.OrganizationLocalServiceUtil;

public class WordBieuMauThongBaoChuyenDon extends WordBieuMauPattern{
	public WordBieuMauThongBaoChuyenDon() {
	}

	@Override
	public void createTitle() throws Exception {
		DoiTuongDiKNTCBean modelDoiTuongDiKNTC = svDonThu.getNguoiDaiDienDonThu(idDonThu).get(0);
		
		String valueTenKinhGui = modelDoiTuongDiKNTC.getHoTen();
		String valueDiaChi = DonThuModule.returnDiaChiChiTiet(modelDoiTuongDiKNTC.getDiaChiChiTiet(), modelDoiTuongDiKNTC.getMaTinh(), modelDoiTuongDiKNTC.getMaHuyen(), modelDoiTuongDiKNTC.getMaXa());
		
		XWPFParagraph para1 = document.createParagraph();
		runAllRun = para1.createRun();
		runAllRun.addBreak();
		runAllRun.addBreak();
		para1.setAlignment(ParagraphAlignment.CENTER);
		setRun(para1, fontTimeNewRoman, 14,"THÔNG BÁO", true,true);
		setRun(para1, fontTimeNewRoman, 14,"Về việc chuyển đơn", true,true);
		setRun(para1, fontTimeNewRoman, 14,"__________", true,false);
		
		XWPFParagraph para4 = document.createParagraph();
		runAllRun = para4.createRun();
		runAllRun.addTab();runAllRun.addTab();runAllRun.addTab();
		setRun(para4, fontTimeNewRoman, 14,"Kính gửi: ông(bà) ", false,false);
		runAllRun = setRun(para4, fontTimeNewRoman, 14,valueTenKinhGui, false,true);
		runAllRun.addTab();runAllRun.addTab();runAllRun.addTab();
		setRun(para4, fontTimeNewRoman, 14,"Địa chỉ: ", false,false);
		setRun(para4, fontTimeNewRoman, 14,valueDiaChi, false,true);
	}

	@Override
	public void createContent() throws Exception {
		DonThuBean modelDonThu = svDonThu.getDonThu(idDonThu);
		ThongTinDonThuBean modelThongTinDon = svDonThu.getThongTinDonThuChuyenDi(idDonThu, SessionUtil.getOrgId());
		
		String valueTenCQDVThongBao = OrganizationLocalServiceUtil.getOrganization(SessionUtil.getOrgId()).getName();
		String valueLoaiDonThu = "";
		String valueNgayNhapDon = sdf.format(modelDonThu.getNgayNhapDon());
		String valueLuatDangCoHieuLuc = "";
		String valueNghiDinhDangCoHieuLuc = "";
		String valueThongTu = "";
		String valueThuTruongCoQuan = OrganizationLocalServiceUtil.getOrganization(modelThongTinDon.getOrgNhan()).getName();
		
		for(LoaiDonThuEnum e: LoaiDonThuEnum.values())
		{
			if(e.getType()==modelDonThu.getLoaiDonThu())
				valueLoaiDonThu = e.getName();
		}
		
		String valueNoiDung = valueTenCQDVThongBao+" nhận được đơn "+valueLoaiDonThu+" ghi tên Ông (Bà) đề ngày "+valueNgayNhapDon
				+ "Sau khi xem xét nội dung đơn; căn cứ "+valueLuatDangCoHieuLuc+";"+valueNghiDinhDangCoHieuLuc+" và "+valueThongTu+","
				+ valueTenCQDVThongBao+" đã chuyển đơn trên đến "+valueThuTruongCoQuan+" để xem xét, giải quyết theo quy định của pháp luật."
				+ valueTenCQDVThongBao+" thông báo để Ông (Bà) biết và liên hệ với cơ quan trên./.";

		XWPFParagraph para5 = document.createParagraph();
		runAllRun = para5.createRun();
		runAllRun.addTab();
		setRun(para5, fontTimeNewRoman, 14,valueNoiDung, false,true);
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
		setRun(para1, "Times New Roman", 13,"-.... để biết;", false,true);
		setRun(para1, "Times New Roman", 13,"-Lưu:....;", false,true);
		
		XWPFTableCell cell2 = row1.createCell();
		createCellSignature(cell2,valueChucVuCuaNguoiKyThongBao,null);
	}
}
