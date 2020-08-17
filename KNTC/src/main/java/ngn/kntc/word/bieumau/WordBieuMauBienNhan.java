package ngn.kntc.word.bieumau;

import java.util.Date;
import java.util.List;

import ngn.kntc.beans.DoiTuongDiKNTCBean;
import ngn.kntc.beans.DonThuBean;
import ngn.kntc.beans.HoSoDinhKemBean;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.utils.SessionUtil;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.liferay.portal.model.User;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;

public class WordBieuMauBienNhan extends WordBieuMauPattern{
	public WordBieuMauBienNhan() {
	}

	@Override
	public void createTitle() throws Exception {
		XWPFParagraph para1 = document.createParagraph();
		runAllRun = para1.createRun();
		runAllRun.addBreak();
		runAllRun.addBreak();
		setRun(para1, fontTimeNewRoman, 13,"GIẤY BIÊN NHẬN", true,true);
		setRun(para1, fontTimeNewRoman, 13,"Thông tin, tài liệu, bằng chứng", true,true);
		
		para1.setAlignment(ParagraphAlignment.CENTER);
	}

	@Override
	public void createContent() throws Exception {
		DoiTuongDiKNTCBean modelDoiTuongDiKNTC = svDonThu.getNguoiDaiDienDonThu(idDonThu).get(0);
		DonThuBean modelDonThu = svDonThu.getDonThu(idDonThu);
		List<HoSoDinhKemBean> listDinhKem = svDonThu.getDinhKemHoSoList(idDonThu);
		User userNhapDon = UserLocalServiceUtil.getUser(modelDonThu.getUserNhapDon());
		Date ngayNhapDon = modelDonThu.getNgayNhapDon();
		
		String valueThoiGian = "Hồi "+ngayNhapDon.getHours()+" giờ "+ngayNhapDon.getMinutes()+" ngày "+ngayNhapDon.getDay()+" tháng "+ngayNhapDon.getMonth()+" năm "+(ngayNhapDon.getYear()+1900);
		String valueDonViTiepNhan = OrganizationLocalServiceUtil.getOrganization(SessionUtil.getOrgId()).getName();
		
		String nguoiTiepNhan = userNhapDon.getFirstName();
		String chucVu = userNhapDon.getJobTitle();
		
		String valueLoaiDon = DonThuModule.returnLoaiDonThuNameOnKey(modelDonThu.getLoaiDonThu());
		String valueNgayVietDon = sdf.format(ngayNhapDon);
		
		String valueOngBao = modelDoiTuongDiKNTC.getHoTen();
		String valueSoCMND = modelDoiTuongDiKNTC.getSoDinhDanhCaNhan()!=null?modelDoiTuongDiKNTC.getSoDinhDanhCaNhan():"                    ";
		String valueNgayCap = modelDoiTuongDiKNTC.getNgayCapSoDinhDanh()!=null?sdf.format(modelDoiTuongDiKNTC.getNgayCapSoDinhDanh()):"         ";
		String valueNoiCap = modelDoiTuongDiKNTC.getNoiCapSoDinhDanh()!=null?modelDoiTuongDiKNTC.getNoiCapSoDinhDanh():"               ";
		
		String valueDiaChi = DonThuModule.returnDiaChiChiTiet(modelDoiTuongDiKNTC.getDiaChiChiTiet(), modelDoiTuongDiKNTC.getMaTinh(), modelDoiTuongDiKNTC.getMaHuyen(), modelDoiTuongDiKNTC.getMaXa());
	
		String valueThoiGianDiaDiem = valueThoiGian+", tại: "+valueDonViTiepNhan;
		
		String valueNguoiTiepNhan = "Tôi là "+nguoiTiepNhan+". Chức vụ: "+chucVu;
		
		String valueCMND = "Số CMND/Hộ chiếu (hoặc giấy tờ tùy thân):"+valueSoCMND+", ngày cấp "+valueNgayCap+" nơi cấp "+valueNoiCap;
		
		String valueKetLuan = "Giấy biên nhận được lập thành …. bản, giao cho người cung cấp thông tin, tài liệu, bằng chứng 01 bản./.";
		
		XWPFParagraph para5 = document.createParagraph();
		runAllRun = para5.createRun();
		runAllRun.addTab();
		runAllRun = setRun(para5, fontTimeNewRoman, 13,valueThoiGianDiaDiem, false,true);
		runAllRun.addTab();
		runAllRun = setRun(para5, fontTimeNewRoman, 13,valueNguoiTiepNhan, false,true);
		runAllRun.addTab();
		runAllRun = setRun(para5, fontTimeNewRoman, 13,"Đã nhận được của ông (bà) "+valueOngBao, false,true);
		runAllRun.addTab();
		runAllRun = setRun(para5, fontTimeNewRoman, 13,valueCMND, false,true);
		runAllRun.addTab();
		runAllRun = setRun(para5, fontTimeNewRoman, 13,"Địa chỉ: "+valueDiaChi, false,true);
		runAllRun.addTab();
		runAllRun = setRun(para5, fontTimeNewRoman, 13,"Các thông tin, tài liệu, bằng chứng sau: ", false,true);
		for(HoSoDinhKemBean modelDinhKem : listDinhKem)
		{
			runAllRun.addTab();
			runAllRun = setRun(para5, fontTimeNewRoman, 13,"1. "+modelDinhKem.getTenHoSo(), false,true);
		}
		runAllRun.addTab();
		setRun(para5, fontTimeNewRoman, 13,valueKetLuan, false,false);
		
		para5.setSpacingBetween(1.5);
	}

	@Override
	public void createFooter() throws Exception {
		XWPFTable table = document.createTable();
		table.setWidth("100%");
		table.getCTTbl().getTblPr().unsetTblBorders();
		table.removeRow(0);
		
		XWPFTableRow row1 = table.createRow();	
		
		XWPFTableCell cell1 = row1.createCell();
		createCellSignature(cell1,"Người cung cấp thông tin, tài liệu, bằng chứng","(Ký, ghi rõ họ tên)");
		cell1.setWidth("50%");
		
		XWPFTableCell cell2 = row1.createCell();
		createCellSignature(cell2,"Người nhận","(Ký, ghi rõ họ tên, đóng dấu - nếu có)");
		cell2.setWidth("50%");
	}
}
