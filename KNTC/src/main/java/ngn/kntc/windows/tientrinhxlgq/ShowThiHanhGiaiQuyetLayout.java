package ngn.kntc.windows.tientrinhxlgq;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

import ngn.kntc.beans.ThiHanhQuyetDinhBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;

import com.jarektoro.responsivelayout.ResponsiveRow;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ShowThiHanhGiaiQuyetLayout {
	VerticalLayout vDetail = new VerticalLayout();
	Label lblNgayDang = new Label("NGÀY ĐĂNG: ",ContentMode.HTML);
	VerticalLayout vLayoutNoiDung = new VerticalLayout();
	Label lblCoQuanThiHanh = new Label("",ContentMode.HTML);
	Label lblNgayCapNhat = new Label("",ContentMode.HTML);
	Label lblCaptionHuongXuLy = new Label("<b style='color: #f15454;font-size: 16px;'>BỒI THƯỜNG THIỆT HẠI:</b>",ContentMode.HTML);
	VerticalLayout vLayoutHuongXuLy = new VerticalLayout();
	Label lblThuHoiTien = new Label("",ContentMode.HTML);
	Label lblThuHoiDatO = new Label("",ContentMode.HTML);
	Label lblThuHoiDatSX = new Label("",ContentMode.HTML);
	Label lblTraLaiTien = new Label("",ContentMode.HTML);
	Label lblTraLaiDatO = new Label("",ContentMode.HTML);
	Label lblTraLaiDatSX = new Label("",ContentMode.HTML);
	Label lblSoNguoiBiXuLy = new Label("",ContentMode.HTML);
	Label lblSoTapTheBiXuLy = new Label("",ContentMode.HTML);
	Label lblTaiSanQuyRaTien = new Label("",ContentMode.HTML);
	Label lblSoTienNopPhat = new Label("",ContentMode.HTML);
	Label lblSoDoiTuongBiKhoiTo = new Label("",ContentMode.HTML);
	
	String strCoQuanThiHanh,strNgayCapNhat,strThuHoiTien,strThuHoiDatO,strThuHoiDatSX,strTraLaiTien,strTraLaiDatO,strTraLaiDatSX,strSoNguoiBiXuLy,strTaiSanQuyRaTien,strSoTapTheBiXuLy,strSoTienNopPhat,strSoDoiRuongBiKhoiTo,strNgayDang;
	public ShowThiHanhGiaiQuyetLayout() {
		vDetail.addComponents(lblNgayDang,vLayoutNoiDung,lblCaptionHuongXuLy,vLayoutHuongXuLy);
		vDetail.setSpacing(true);
		vDetail.setSizeFull();
		vDetail.addStyleName("vLayout-detail-tientrinh");
		vDetail.addStyleName("vLayout-detail-tientrinh-no-button");
	}
	
	public void buildNoiDungQuyetDinh()
	{
		lblNgayDang.setValue("<b style='color: #f15454;font-size: 16px;'>NGÀY ĐĂNG:</b> "+strNgayDang);
		lblNgayDang.addStyleName("lbl-ngaydang-tientrinh");
		
		lblCoQuanThiHanh.setValue("<b style='color: #3066bb;'>CƠ QUAN THI HÀNH:</b> "+strCoQuanThiHanh);
		lblNgayCapNhat.setValue("<b style='color: #3066bb;'>NGÀY CẬP NHẬT:</b> "+strNgayCapNhat);
		
		vLayoutNoiDung.addComponents(lblCoQuanThiHanh,lblNgayCapNhat);
		vLayoutNoiDung.setMargin(new MarginInfo(false,false,false,true));
		vLayoutNoiDung.setSpacing(true);
		vLayoutNoiDung.setSizeFull();
	}
	
	public void buildBoiThuongThietHai()
	{
		ResponsiveRow rowThuHoi=new ResponsiveRow();
		rowThuHoi.addColumn().withComponent(lblThuHoiTien).withDisplayRules(12, 4, 4, 4);
		rowThuHoi.addColumn().withComponent(lblThuHoiDatO).withDisplayRules(12, 4, 4, 4);
		rowThuHoi.addColumn().withComponent(lblThuHoiDatSX).withDisplayRules(12, 4, 4, 4);
		
		lblThuHoiTien.setValue("<b style='color: #3066bb;'>THU HỒI TIỀN:</b> "+strThuHoiTien+" <b>vnđ</b>");
		lblThuHoiDatO.setValue("<b style='color: #3066bb;'>THU HỒI ĐẤT Ở:</b> "+strThuHoiDatO+" <b>m²</b>");
		lblThuHoiDatSX.setValue("<b style='color: #3066bb;'>THU HỒI ĐẤT SẢN XUẤT:</b> "+strThuHoiDatSX+" <b>m²</b>");
		
		ResponsiveRow rowTraLai=new ResponsiveRow();
		rowTraLai.addColumn().withComponent(lblTraLaiTien).withDisplayRules(12, 4, 4, 4);
		rowTraLai.addColumn().withComponent(lblTraLaiDatO).withDisplayRules(12, 4, 4, 4);
		rowTraLai.addColumn().withComponent(lblTraLaiDatSX).withDisplayRules(12, 4, 4, 4);
		
		lblTraLaiTien.setValue("<b style='color: #3066bb;'>TRẢ LẠI TIỀN:</b> "+strTraLaiTien+" <b>vnđ</b>");
		lblTraLaiDatO.setValue("<b style='color: #3066bb;'>TRẢ LẠI ĐẤT Ở:</b> "+strTraLaiDatO+" <b>m²</b>");
		lblTraLaiDatSX.setValue("<b style='color: #3066bb;'>TRẢ LẠI ĐẤT SẢN XUẤT: </b> "+strTraLaiDatSX+" <b>m²</b>");
		
		ResponsiveRow rowSoDoiTuongBiXuLy=new ResponsiveRow();
		rowSoDoiTuongBiXuLy.addColumn().withComponent(lblSoNguoiBiXuLy).withDisplayRules(12, 6, 6, 6);
		rowSoDoiTuongBiXuLy.addColumn().withComponent(lblSoTapTheBiXuLy).withDisplayRules(12, 6, 6, 6);
		
		lblSoNguoiBiXuLy.setValue("<b style='color: #3066bb;'>SỐ NGƯỜI BỊ XỬ LÝ HÀNH CHÍNH: </b>"+strSoNguoiBiXuLy+" <b>người</b>");
		lblSoTapTheBiXuLy.setValue("<b style='color: #3066bb;'>SỐ TẬP THỂ BỊ XỬ LÝ HÀNH CHÍNH: </b> "+strSoTapTheBiXuLy+" <b>tập thể</b>");
		
		lblSoTienNopPhat.setValue("<b style='color: #3066bb;'>SỐ TIỀN NỘP PHẠT: </b>"+strSoTienNopPhat+" <b>vnđ</b>");
		lblSoDoiTuongBiKhoiTo.setValue("<b style='color: #3066bb;'>SỐ ĐỐI TƯỢNG BỊ KHỞI TỐ: </b>"+strSoDoiRuongBiKhoiTo+" <b>người</b>");
		lblTaiSanQuyRaTien.setValue("<b style='color: #3066bb;'>TÀI SẢN QUY RA TIỀN: </b>"+strTaiSanQuyRaTien+" <b>vnđ</b>");
		
		vLayoutHuongXuLy.addComponents(rowThuHoi,rowTraLai,rowSoDoiTuongBiXuLy,lblSoTienNopPhat,lblTaiSanQuyRaTien,lblSoDoiTuongBiKhoiTo);
		vLayoutHuongXuLy.setMargin(new MarginInfo(false,false,false,true));
		vLayoutHuongXuLy.setSpacing(true);
		vLayoutHuongXuLy.setSizeFull();
	}
	
	public void buildValue(ThiHanhQuyetDinhBean modelQDGQ,QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			strCoQuanThiHanh = DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.coquan.getName(),modelQDGQ.getMaCoQuanThiHanh()).getName();
			strNgayCapNhat = sdf.format(modelQDGQ.getNgayCapNhat());
			strNgayDang = sdf.format(modelQDGQ.getNgayTao());
			strThuHoiTien = String.valueOf(modelQDGQ.getThuHoiTien());
			strThuHoiDatO = String.valueOf(modelQDGQ.getThuHoiDatO());
			strThuHoiDatSX = String.valueOf(modelQDGQ.getThuHoiDatSX());
			strTraLaiTien = String.valueOf(modelQDGQ.getTraLaiTien());
			strTraLaiDatO = String.valueOf(modelQDGQ.getTraLaiDatO());
			strTraLaiDatSX = String.valueOf(modelQDGQ.getTraLaiDatSX());
			strSoNguoiBiXuLy = String.valueOf(modelQDGQ.getSoNguoiBiXuLy());
			strSoTapTheBiXuLy = String.valueOf(modelQDGQ.getSoTapTheBiXuLy());
			strTaiSanQuyRaTien = String.valueOf(modelQDGQ.getTaiSanQuyRaTien());
			strSoTienNopPhat = String.valueOf(modelQDGQ.getSoTienNopPhat());
			strSoDoiRuongBiKhoiTo = String.valueOf(modelQDGQ.getSoDoiTuongBiKhoiTo());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public VerticalLayout getvDetail() {
		return vDetail;
	}

	public void setvDetail(VerticalLayout vDetail) {
		this.vDetail = vDetail;
	}
	
}
