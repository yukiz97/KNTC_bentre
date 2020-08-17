package ngn.kntc.page.donthu.chitiet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ngn.kntc.beans.DoiTuongBiKNTCBean;
import ngn.kntc.beans.DoiTuongDiKNTCBean;
import ngn.kntc.beans.DoiTuongUyQuyenBean;
import ngn.kntc.beans.DonThuBean;
import ngn.kntc.beans.GeneralStringStringBean;
import ngn.kntc.beans.SoTiepCongDanBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.enums.LoaiNguoiBiKNTCEnum;
import ngn.kntc.enums.LoaiNguoiDiKNTCEnum;
import ngn.kntc.enums.LoaiNguoiUyQuyenEnum;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.TiepCongDanServiceUtil;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ChiTietDonThuTCDGeneralFunction {
	public static String chuaXacDinhValue = "<i style='color: gray'>Chưa xác định</i>";
	
	static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	public static VerticalLayout buildThongTinDoiTuongDiKNTCBlock(boolean isDonThu,DonThuBean modelDonThu,SoTiepCongDanBean modelTCD) throws Exception
	{
		List<DoiTuongDiKNTCBean> listDoiTuongDiKNTC = isDonThu ? DonThuServiceUtil.getNguoiDaiDienDonThu(modelDonThu.getMaDonThu()) : TiepCongDanServiceUtil.getNguoiDaiDienTiepCongDan(modelTCD.getMaSoTiepCongDan());

		int loaiNguoiDiKNTC = isDonThu ? modelDonThu.getLoaiNguoiDiKNTC() : modelTCD.getLoaiNguoiDiKNTC();
		
		String loaiDiKhieuTo = "";

		String soNguoi = isDonThu ? String.valueOf(modelDonThu.getSoNguoiDiKNTC()) : String.valueOf(modelTCD.getSoNguoiDiKNTC());
		String soNguoiDaiDien = isDonThu ? String.valueOf(modelDonThu.getSoNguoiDaiDien()) : String.valueOf(modelTCD.getSoNguoiDaiDien());

		for(LoaiNguoiDiKNTCEnum e : LoaiNguoiDiKNTCEnum.values())
		{
			if(e.getType()==loaiNguoiDiKNTC)
				loaiDiKhieuTo = e.getName();
		}

		List<Label> listLabelDoiTuongDiKNTC = new ArrayList<Label>();
		listLabelDoiTuongDiKNTC.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Loại đối tượng đi khiếu tố", loaiDiKhieuTo))));
		listLabelDoiTuongDiKNTC.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Số người", soNguoi),new GeneralStringStringBean("Số người đại diện", soNguoiDaiDien))));

		if(loaiNguoiDiKNTC==LoaiNguoiDiKNTCEnum.coquantochuc.getType())
		{
			String tenCoQuan = isDonThu ? modelDonThu.getTenCoQuanDiKNTC() : modelTCD.getTenCoQuanDiKNTC();
			String diaChiTmp = isDonThu ? modelDonThu.getDiaChiCoQuanDiKNTC() : modelTCD.getDiaChiCoQuanDiKNTC();
			String diaChiCoQuan = diaChiTmp!=null?diaChiTmp:chuaXacDinhValue;

			listLabelDoiTuongDiKNTC.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Tên cơ quan", tenCoQuan),new GeneralStringStringBean("Địa chỉ cơ quan", diaChiCoQuan))));
		}

		for(DoiTuongDiKNTCBean modelDoiTuongDiKNTC : listDoiTuongDiKNTC)
		{
			listLabelDoiTuongDiKNTC.addAll(buildThongTinDoiNguoiDaiDienBlock(modelDoiTuongDiKNTC));
		}

		return ChiTietDonThuTCDGeneralFunction.buildBlockThongTin("Thông tin đối tượng đi khiếu nại tố cáo", listLabelDoiTuongDiKNTC,FontAwesome.USER.getHtml());
	}

	private static List<Label> buildThongTinDoiNguoiDaiDienBlock(DoiTuongDiKNTCBean modelDoiTuong) throws Exception
	{
		String hoTen = modelDoiTuong.getHoTen();
		String gioiTinh = modelDoiTuong.getGioiTinh()==1?"Nam":"Nữ";

		String soDinhDanh = modelDoiTuong.getSoDinhDanhCaNhan()!=null ? modelDoiTuong.getSoDinhDanhCaNhan() : chuaXacDinhValue;
		String noiCap = modelDoiTuong.getNoiCapSoDinhDanh()!=null ? modelDoiTuong.getNoiCapSoDinhDanh() : chuaXacDinhValue;
		String ngayCap = modelDoiTuong.getNgayCapSoDinhDanh()!=null ? sdf.format(modelDoiTuong.getNgayCapSoDinhDanh()):chuaXacDinhValue;

		String quocTich = modelDoiTuong.getMaQuocTich()!=null?DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.quoctich.getName(), modelDoiTuong.getMaQuocTich()).getName():chuaXacDinhValue;
		String danToc = modelDoiTuong.getMaDanToc()!= 0?DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.dantoc.getName(), modelDoiTuong.getMaDanToc()).getName():chuaXacDinhValue;

		String diaChiChiTiet = DonThuModule.returnDiaChiChiTiet(modelDoiTuong.getDiaChiChiTiet(), modelDoiTuong.getMaTinh(), modelDoiTuong.getMaHuyen(), modelDoiTuong.getMaXa());

		List<Label> listDoiTuongDiKNTC = new ArrayList<Label>();
		Label lblHoTen = ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Họ tên", hoTen),new GeneralStringStringBean("Giới tính", gioiTinh)));

		lblHoTen.addStyleName("chitiet-info-block-hoten-nguoidikntc");

		listDoiTuongDiKNTC.add(lblHoTen);
		listDoiTuongDiKNTC.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Số định danh", soDinhDanh),new GeneralStringStringBean("Nơi cấp", noiCap),new GeneralStringStringBean("Ngày cấp", ngayCap))));
		listDoiTuongDiKNTC.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Dân tộc", danToc),new GeneralStringStringBean("Quốc tịch", quocTich))));
		listDoiTuongDiKNTC.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Địa chỉ chi tiết", diaChiChiTiet))));

		return listDoiTuongDiKNTC;
	}
	
	public static VerticalLayout buildThongTinDoiTuongBiKNTCBlock(DoiTuongBiKNTCBean modelDoiTuong,int loaiNguoiBiKNTC) throws Exception
	{
		String loaiBiKhieuTo = "";

		for(LoaiNguoiBiKNTCEnum e : LoaiNguoiBiKNTCEnum.values())
		{
			if(e.getType()==loaiNguoiBiKNTC)
				loaiBiKhieuTo = e.getName();
		}

		List<Label> listDoiTuongBiKNTC = new ArrayList<Label>();
		listDoiTuongBiKNTC.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Loại đổi tượng bị khiếu tố", loaiBiKhieuTo))));

		if(loaiNguoiBiKNTC==1)
		{
			String hoTen = modelDoiTuong.getHoTen();
			String gioiTinh = modelDoiTuong.getGioiTinh()==1?"Nam":"Nữ";

			String soDinhDanh = modelDoiTuong.getSoDinhDanhCaNhan()!=null ? modelDoiTuong.getSoDinhDanhCaNhan() : chuaXacDinhValue;
			String noiCap = modelDoiTuong.getNoiCapSoDinhDanh()!=null ? modelDoiTuong.getNoiCapSoDinhDanh() : chuaXacDinhValue;
			String ngayCap = modelDoiTuong.getNgayCapSoDinhDanh()!=null ? sdf.format(modelDoiTuong.getNgayCapSoDinhDanh()):chuaXacDinhValue;

			String noiCongTac = modelDoiTuong.getNoiCongTac()!=null ? modelDoiTuong.getNoiCongTac() : chuaXacDinhValue;
			String chucVu = modelDoiTuong.getChucVu()!=null?modelDoiTuong.getChucVu():chuaXacDinhValue;

			String quocTich = modelDoiTuong.getMaQuocTich()!=null?DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.quoctich.getName(), modelDoiTuong.getMaQuocTich()).getName():chuaXacDinhValue;
			String danToc = modelDoiTuong.getMaDanToc()!= 0?DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.dantoc.getName(), modelDoiTuong.getMaDanToc()).getName():chuaXacDinhValue;

			String diaChiChiTiet = DonThuModule.returnDiaChiChiTiet(modelDoiTuong.getDiaChiChiTiet(), modelDoiTuong.getMaTinh(), modelDoiTuong.getMaHuyen(), modelDoiTuong.getMaXa());

			listDoiTuongBiKNTC.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Họ tên", hoTen),new GeneralStringStringBean("Giới tính", gioiTinh))));
			listDoiTuongBiKNTC.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Số định danh", soDinhDanh),new GeneralStringStringBean("Nơi cấp", noiCap),new GeneralStringStringBean("Ngày cấp", ngayCap))));
			listDoiTuongBiKNTC.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Nơi công tác", noiCongTac),new GeneralStringStringBean("Chức vụ", chucVu))));
			listDoiTuongBiKNTC.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Dân tộc", danToc),new GeneralStringStringBean("Quốc tịch", quocTich))));
			listDoiTuongBiKNTC.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Địa chỉ chi tiết", diaChiChiTiet))));
		}
		else if(loaiNguoiBiKNTC==2)
		{
			String coQuan = modelDoiTuong.getTenCoQuanToChuc();
			String diaChiCoQuan = DonThuModule.returnDiaChiChiTiet(modelDoiTuong.getDiaChiChiTietCoQuan(), modelDoiTuong.getMaTinhCoQuan(), modelDoiTuong.getMaHuyenCoQuan(), modelDoiTuong.getMaXaCoQuan());

			listDoiTuongBiKNTC.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Tên cơ quan / tổ chức", coQuan))));
			listDoiTuongBiKNTC.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Địa chỉ chi tiết", diaChiCoQuan))));
		}
		return ChiTietDonThuTCDGeneralFunction.buildBlockThongTin("Thông tin đối tượng bị khiếu nại tố cáo", listDoiTuongBiKNTC,FontAwesome.USER.getHtml());
	}
	
	public static VerticalLayout buildThongTinDoiTuongUyQuyentBlock(DoiTuongUyQuyenBean modelDoiTuong,int loaiNguoiUyQuyen) throws Exception
	{
		String loaiUyQuyen = "";

		String hoTen = modelDoiTuong.getHoTen();
		String gioiTinh = modelDoiTuong.getGioiTinh()==1?"Nam":"Nữ";

		String soDinhDanh = modelDoiTuong.getSoDinhDanhCaNhan()!=null ? modelDoiTuong.getSoDinhDanhCaNhan() : chuaXacDinhValue;
		String noiCap = modelDoiTuong.getNoiCapSoDinhDanh()!=null ? modelDoiTuong.getNoiCapSoDinhDanh() : chuaXacDinhValue;
		String ngayCap = modelDoiTuong.getNgayCapSoDinhDanh()!=null ? sdf.format(modelDoiTuong.getNgayCapSoDinhDanh()):chuaXacDinhValue;

		String quocTich = modelDoiTuong.getMaQuocTich()!=null?DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.quoctich.getName(), modelDoiTuong.getMaQuocTich()).getName():chuaXacDinhValue;
		String danToc = modelDoiTuong.getMaDanToc()!= 0?DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.dantoc.getName(), modelDoiTuong.getMaDanToc()).getName():chuaXacDinhValue;

		String diaChiChiTiet = DonThuModule.returnDiaChiChiTiet(modelDoiTuong.getDiaChiChiTiet(), modelDoiTuong.getMaTinh(), modelDoiTuong.getMaHuyen(), modelDoiTuong.getMaXa());

		List<Label> listDoiTuongUyQuyen = new ArrayList<Label>();

		for(LoaiNguoiUyQuyenEnum e : LoaiNguoiUyQuyenEnum.values())
		{
			if(e.getType()== loaiNguoiUyQuyen)
				loaiUyQuyen = e.getName();
		}

		listDoiTuongUyQuyen.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Loại đổi tượng ủy quyền", loaiUyQuyen))));

		listDoiTuongUyQuyen.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Họ tên", hoTen),new GeneralStringStringBean("Giới tính", gioiTinh))));
		listDoiTuongUyQuyen.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Số định danh", soDinhDanh),new GeneralStringStringBean("Nơi cấp", noiCap),new GeneralStringStringBean("Ngày cấp", ngayCap))));
		listDoiTuongUyQuyen.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Dân tộc", danToc),new GeneralStringStringBean("Quốc tịch", quocTich))));
		listDoiTuongUyQuyen.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Địa chỉ chi tiết", diaChiChiTiet))));

		return ChiTietDonThuTCDGeneralFunction.buildBlockThongTin("Thông tin đối tượng được ủy quyền", listDoiTuongUyQuyen,FontAwesome.USER.getHtml());
	}
	
	public static VerticalLayout buildBlockThongTin(String strCaption,List<Label> listLabel,String icon)
	{
		VerticalLayout vTmp = new VerticalLayout();
		Label lblCaption = new Label(strCaption,ContentMode.HTML);
		Label lblIcon = new Label(icon,ContentMode.HTML);

		vTmp.addComponent(lblCaption);

		for(Label lbl : listLabel)
		{
			vTmp.addComponent(lbl);
		}

		vTmp.addComponent(lblIcon);

		lblCaption.addStyleName("chitiet-info-block-caption");
		lblIcon.addStyleName("chitiet-info-block-icon");

		vTmp.setWidth("100%");
		vTmp.addStyleName("chitiet-info-block");

		return vTmp;
	}
	
	public static Label buildLabelThongTin(List<GeneralStringStringBean> list)
	{
		Label lblThongTin = new Label("",ContentMode.HTML);

		String strThongTin = "";

		for(GeneralStringStringBean model : list)
		{
			strThongTin+="<span class='chitiet-subinfo'><b>"+model.getFirstValue()+"</b>: "+model.getSecondValue()+"</span>";
		}
		lblThongTin.setValue(strThongTin);
		lblThongTin.addStyleName("chitiet-info-block-row");

		return lblThongTin;
	}
}
