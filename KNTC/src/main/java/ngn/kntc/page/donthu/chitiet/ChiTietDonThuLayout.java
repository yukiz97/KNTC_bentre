package ngn.kntc.page.donthu.chitiet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import ngn.kntc.beans.DoiTuongBiKNTCBean;
import ngn.kntc.beans.DoiTuongDiKNTCBean;
import ngn.kntc.beans.DoiTuongUyQuyenBean;
import ngn.kntc.beans.DonThuBean;
import ngn.kntc.beans.HoSoDinhKemBean;
import ngn.kntc.beans.ThongTinDonThuBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.enums.LoaiDonThuEnum;
import ngn.kntc.enums.LoaiNguoiBiKNTCEnum;
import ngn.kntc.enums.LoaiNguoiDiKNTCEnum;
import ngn.kntc.enums.LoaiNguoiUyQuyenEnum;
import ngn.kntc.enums.ThamQuyenGiaiQuyetEnum;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.page.donthu.create.TaoDonThuInput;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.SessionUtil;
import ngn.kntc.utils.TiepCongDanServiceUtil;
import ngn.kntc.utils.UploadServiceUtil;

import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class ChiTietDonThuLayout extends ResponsiveLayout{
	int idDonThu;
	Button btnSua = new Button("Sửa thông tin đơn thư");
	DonThuBean modelDonThu;
	ThongTinDonThuBean modelThongTinDon;
	VerticalLayout vThongTinDon1 = new VerticalLayout();
	Label lblCaptionThongTinDon1 = new Label("<b style='font-size: 25px; color: #da392d;'>THÔNG TIN ĐƠN THƯ</b>",ContentMode.HTML);	

	VerticalLayout vThongTinDon2 = new VerticalLayout();
	Label lblCaptionThongTinDon2 = new Label("<b style='font-size: 25px; color: #da392d;'>THÔNG TIN ĐỐI TƯỢNG</b>",ContentMode.HTML);

	DonThuServiceUtil svDonThu = new DonThuServiceUtil();
	TiepCongDanServiceUtil svTCD = new TiepCongDanServiceUtil();
	DanhMucServiceUtil svDanhMuc = new DanhMucServiceUtil();

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	public ChiTietDonThuLayout(int idDonThu) throws Exception {
		this.idDonThu = idDonThu;
		modelDonThu = svDonThu.getDonThu(idDonThu);
		modelThongTinDon = svDonThu.getThongTinDonThu(idDonThu, SessionUtil.getOrgId());

		ResponsiveRow row = new ResponsiveRow();

		row.addColumn().withComponent(vThongTinDon1).withDisplayRules(12,12,6,6);
		row.addColumn().withComponent(vThongTinDon2).withDisplayRules(12,12,6,6);

		vThongTinDon1.addStyleName("vLayout-thongtin-donthu-main");
		vThongTinDon2.addStyleName("vLayout-thongtin-donthu-main");

		HorizontalLayout hTmp = new HorizontalLayout(btnSua);
		hTmp.setComponentAlignment(btnSua, Alignment.MIDDLE_RIGHT);
		hTmp.addStyleName("hLayout-donthu-chitiet-margin");

		hTmp.setWidth("100%");

		btnSua.addStyleName("btn-donthu-thongtinquatrinhxlgq-mainbutton");
		btnSua.addStyleName("btn-donthu-thongtinquatrinhxlgq-mainbutton-tientrinh");

		this.addComponent(hTmp);
		this.addRow(row);
		this.setWidth("100%");

		buildThongTinDon();
		buildThongTinDoiTuong();
		configComponent();
	}

	private void configComponent() {
		btnSua.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				Window wdUpdate = new Window();
				TaoDonThuInput donThu = new TaoDonThuInput(idDonThu);
				wdUpdate.setContent(donThu);
				wdUpdate.setSizeFull();
				wdUpdate.setModal(true);
				wdUpdate.setCaption("Sửa thông tin đơn thư");
				donThu.getBtnSave().addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						if(donThu.isValidateSuccess())
						{
							wdUpdate.close();
							try {
								modelDonThu = svDonThu.getDonThu(idDonThu);
								modelThongTinDon = svDonThu.getThongTinDonThu(idDonThu, SessionUtil.getOrgId());
								buildThongTinDoiTuong();
								buildThongTinDon();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				});
				donThu.getBtnClose().addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						wdUpdate.close();
					}
				});
				UI.getCurrent().addWindow(wdUpdate);
			}
		});
	}

	public void buildThongTinDon() throws Exception
	{
		vThongTinDon1.removeAllComponents();
		vThongTinDon1.addComponent(lblCaptionThongTinDon1);
		vThongTinDon1.addComponent(buildLayoutThongTinDonThuTongQuan());
		if(modelThongTinDon.getMaCoQuanChuyenDen()!=null)
			vThongTinDon1.addComponent(buildLayoutCoQuanChuyenDen());
		if(modelDonThu.getMaCoQuanDaGiaiQuyet()!=null)
			vThongTinDon1.addComponent(buildLayoutCoQuanDaGiaiQuyet());
		vThongTinDon1.addComponent(buildLayoutNoiDungDonThu());

		vThongTinDon1.setMargin(true);
		vThongTinDon1.setSpacing(true);
		vThongTinDon1.setWidth("100%");
	}

	public void buildThongTinDoiTuong() throws Exception
	{
		vThongTinDon2.removeAllComponents();
		vThongTinDon2.addComponent(lblCaptionThongTinDon2);
		vThongTinDon2.addComponent(buildLayoutThongTinNguoiDiKNTC());

		if(modelDonThu.getMaDoiTuongBiKNTC()!=0)
			vThongTinDon2.addComponent(buildLayoutNguoiBiKNTC(svDonThu.getDoiTuongBiKNTC(modelDonThu.getMaDoiTuongBiKNTC())));

		if(modelDonThu.getMaDoiTuongUyQuyen()!=0)
			vThongTinDon2.addComponent(buildLayoutNguoiUyQuyen(svDonThu.getDoiTuongUyQuyen(modelDonThu.getMaDoiTuongUyQuyen())));

		vThongTinDon2.setMargin(true);
		vThongTinDon2.setSpacing(true);
		vThongTinDon2.setWidth("100%");
	}

	public VerticalLayout buildLayoutThongTinDonThuTongQuan() throws Exception
	{
		VerticalLayout vTmp = new VerticalLayout();
		String loaiDonThu = "";
		for(LoaiDonThuEnum e : LoaiDonThuEnum.values())
		{
			if(e.getType()==modelDonThu.getLoaiDonThu())
				loaiDonThu = e.getName();
		}

		ResponsiveRow row1 = new ResponsiveRow();
		row1.addColumn().withComponent(buildLabelDisplay("Loại đơn thư: ", "<b>"+loaiDonThu+"</b>", "left")).withDisplayRules(12, 12, 8, 4);
		row1.addColumn().withComponent(buildLabelDisplay("Đơn nặc danh: ", modelDonThu.isDonNacDanh()?FontAwesome.CHECK.getHtml():FontAwesome.REMOVE.getHtml(), "left")).withDisplayRules(12, 12, 8, 4);
		row1.addColumn().withComponent(buildLabelDisplay("Đơn không đủ điều kiện thụ lý: ", modelDonThu.isDonKhongDuDieuKienXuLy()?FontAwesome.CHECK.getHtml():FontAwesome.REMOVE.getHtml(), "right")).withDisplayRules(12, 12, 4, 4);

		ResponsiveRow row2 = new ResponsiveRow();
		String thamQuyen = "";
		for(ThamQuyenGiaiQuyetEnum e : ThamQuyenGiaiQuyetEnum.values())
		{
			if(e.getType()==modelDonThu.getThamQuyenGiaiQuyet())
				thamQuyen = e.getName();
		}
		row2.addColumn().withComponent(buildLabelDisplay("Nguồn đơn: ", DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.nguondon.getName(), modelThongTinDon.getNguonDonDen()).getName(), "left")).withDisplayRules(12, 12, 8, 6);
		row2.addColumn().withComponent(buildLabelDisplay("Thẩm quyền giải quyết: ",thamQuyen , "right")).withDisplayRules(12, 12, 4, 6);

		ResponsiveRow row3 = new ResponsiveRow();
		row3.addColumn().withComponent(buildLabelDisplay("Cán bộ nhập: ", UserLocalServiceUtil.getUser(modelDonThu.getUserNhapDon()).getFirstName(), "left")).withDisplayRules(12, 12, 8, 6);
		row3.addColumn().withComponent(buildLabelDisplay("Ngày nhận: ", sdf.format(modelDonThu.getNgayNhanDon()),"right")).withDisplayRules(12, 12, 4, 6);

		ResponsiveRow row4 = new ResponsiveRow();
		row3.addColumn().withComponent(buildLabelDisplay("Ngày nhập: ", sdf.format(modelDonThu.getNgayNhapDon()),"right")).withDisplayRules(12, 12, 12, 12);
		
		vTmp.addComponent(row1);
		vTmp.addComponent(row3);
		vTmp.addComponent(row4);
		vTmp.addComponent(row2);
		vTmp.setWidth("100%");
		vTmp.addStyleName("vLayout-sublayout-thongtin-donthu");

		return vTmp;
	}

	public VerticalLayout buildLayoutCoQuanChuyenDen() throws Exception
	{
		VerticalLayout vTmp = new VerticalLayout();
		Label lblCaptionCoQuanChuyenDen = new Label("Thông tin cơ quan chuyển đến",ContentMode.HTML);
		lblCaptionCoQuanChuyenDen.addStyleName("label-subcaption-thongtin-donthu");

		ResponsiveRow row1 = new ResponsiveRow();
		row1.addColumn().withComponent(buildLabelDisplay("Tên cơ quan: ", DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.coquan.getName(), modelThongTinDon.getMaCoQuanChuyenDen()).getName(), "left")).withDisplayRules(12, 12, 8, 6);
		row1.addColumn().withComponent(buildLabelDisplay("Số văn bản đến:", modelThongTinDon.getSoVanBanDen(), "right")).withDisplayRules(12, 12, 4, 6);

		ResponsiveRow row2 = new ResponsiveRow();
		row2.addColumn().withComponent(buildLabelDisplay("Ngày phát hành văn bản: ", sdf.format(modelThongTinDon.getNgayPhatHanhVanBanDen()), "left")).withDisplayRules(12, 12, 8, 6);

		vTmp.addComponent(lblCaptionCoQuanChuyenDen);
		vTmp.addComponent(row1);
		vTmp.addComponent(row2);
		vTmp.setWidth("100%");
		vTmp.addStyleName("vLayout-sublayout-thongtin-donthu");

		return vTmp;
	}

	public VerticalLayout buildLayoutCoQuanDaGiaiQuyet() throws Exception
	{
		VerticalLayout vTmp = new VerticalLayout();
		Label lblCaptionCoQuanDaGiaiQuyet = new Label("Thông tin cơ quan đã giải quyết",ContentMode.HTML);
		lblCaptionCoQuanDaGiaiQuyet.addStyleName("label-subcaption-thongtin-donthu");

		ResponsiveRow row1 = new ResponsiveRow();
		row1.addColumn().withComponent(buildLabelDisplay("Tên cơ quan: ", DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.coquan.getName(), modelDonThu.getMaCoQuanDaGiaiQuyet()).getName(), "left")).withDisplayRules(12, 12, 8, 6);
		row1.addColumn().withComponent(buildLabelDisplay("Lần giải quyết:", modelDonThu.getLanGiaiQuyet()+"", "right")).withDisplayRules(12, 12, 4, 6);
		ResponsiveRow row0 = new ResponsiveRow();
		row1.addColumn().withComponent(buildLabelDisplay("Số ký hiệu văn bản giải quyết: ", modelDonThu.getSoKyHieuVanBanGiaiQuyet(), "left")).withDisplayRules(12, 12, 12, 12);
		ResponsiveRow row2 = new ResponsiveRow();
		row2.addColumn().withComponent(buildLabelDisplay("Ngày ban hành quyết định: ", sdf.format(modelDonThu.getNgayBanHanhQDGQ()), "left")).withDisplayRules(12, 12, 8, 6);
		row2.addColumn().withComponent(buildLabelDisplay("Loại quyết định: ", DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.hinhthucgiaiquyet.getName(), modelDonThu.getLoaiQuyetDinhGiaiQuyet()).getName(), "right")).withDisplayRules(12, 12, 8, 6);

		ResponsiveRow row3 = new ResponsiveRow();
		row1.addColumn().withComponent(buildLabelDisplay("Tóm tắt nội dung: ", modelDonThu.getTomTatNoiDungGiaiQuyet(), "left")).withDisplayRules(12, 12, 8, 6);

		vTmp.addComponent(lblCaptionCoQuanDaGiaiQuyet);
		vTmp.addComponent(row1);
		vTmp.addComponent(row0);
		vTmp.addComponent(row2);
		vTmp.addComponent(row3);
		vTmp.setWidth("100%");
		vTmp.addStyleName("vLayout-sublayout-thongtin-donthu");

		return vTmp;
	}

	public VerticalLayout buildLayoutNoiDungDonThu() throws Exception
	{
		VerticalLayout vTmp = new VerticalLayout();
		Label lblCaptionNoiDung = new Label("Nội dung đơn thư",ContentMode.HTML);
		Label lblCaptionLinhVuc = new Label("Lĩnh vực đơn thư",ContentMode.HTML);
		Label lblCaptionDinhKem = new Label("Hồ sơ đính kèm",ContentMode.HTML);
		lblCaptionNoiDung.addStyleName("label-subcaption-thongtin-donthu");
		lblCaptionLinhVuc.addStyleName("label-subcaption-thongtin-donthu");
		lblCaptionDinhKem.addStyleName("label-subcaption-thongtin-donthu");

		vTmp.addComponent(lblCaptionNoiDung);
		vTmp.addComponent(buildLabelDisplay("Tóm tắt nội dung: ", modelDonThu.getNoiDungDonThu(), "left"));
		List<String> listLinhVuc = svDonThu.getLinhVucList(idDonThu);
		vTmp.addComponent(lblCaptionLinhVuc);
		if(!listLinhVuc.isEmpty())
		{
			for(String idLinhVuc : listLinhVuc)
			{
				ResponsiveRow rowTmp = new ResponsiveRow();
				HashMap<String, String> lv = DonThuModule.getLinhVucLevel(idLinhVuc);
				try {
					if(lv.containsKey("lv2"))
						rowTmp.addColumn().withComponent(new Label("<b style='color: #19619a'>Loại lĩnh vực: </b>"+svDanhMuc.getLinhVuc(lv.get("lv2")).getName(),ContentMode.HTML)).withDisplayRules(12, 12, 4, 4);
					if(lv.containsKey("lv3"))
						rowTmp.addColumn().withComponent(new Label("<b style='color: #19619a'>Chi tiết lĩnh vực: </b>"+svDanhMuc.getLinhVuc(lv.get("lv3")).getName(),ContentMode.HTML)).withDisplayRules(12, 12, 4, 4);
					if(lv.containsKey("lv4"))
						rowTmp.addColumn().withComponent(new Label("<b style='color: #19619a'>Diễn giải: </b>"+svDanhMuc.getLinhVuc(lv.get("lv4")).getName(),ContentMode.HTML)).withDisplayRules(12, 12, 4, 4);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				vTmp.addComponent(rowTmp);
			}
		}

		List<HoSoDinhKemBean> listHoSoDinhKem = svDonThu.getDinhKemHoSoList(idDonThu);
		vTmp.addComponent(lblCaptionDinhKem);
		if(!listHoSoDinhKem.isEmpty())
		{
			for(HoSoDinhKemBean model : listHoSoDinhKem)
			{
				Button btnDownload = new Button("<span style='color: #1f69a5'>"+model.getTenFileDinhKem()+"</span>",FontAwesome.DOWNLOAD);
				ResponsiveRow rowTmp = new ResponsiveRow();
				rowTmp.addColumn().withComponent(new Label("<b style='color: #19619a'>Tên hồ sơ: </b>"+model.getTenHoSo(),ContentMode.HTML)).withDisplayRules(12, 12, 4, 4);
				rowTmp.addColumn().withComponent(new Label("<b style='color: #19619a'>Loại hồ sơ: </b>"+DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.loaitailieu.getName(), model.getLoaiHoSo()).getName(),ContentMode.HTML)).withDisplayRules(12, 12, 4, 3);
				rowTmp.addColumn().withComponent(btnDownload).withDisplayRules(12, 12, 4, 4);

				/* Button download */
				btnDownload.setCaptionAsHtml(true);
				btnDownload.addStyleName(ValoTheme.BUTTON_BORDERLESS);

				Resource resource = new StreamResource(new StreamSource() {

					@Override
					public InputStream getStream() {
						String directory = UploadServiceUtil.getAbsolutePath()+File.separator+UploadServiceUtil.getFolderNameDonThu()+File.separator+idDonThu+File.separator+model.getLinkFileDinhKem();
						File file = new File(directory);
						try {
							return new FileInputStream(file);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
							return null;
						}
					}
				}, model.getTenFileDinhKem());

				FileDownloader downloader = new FileDownloader(resource);
				downloader.extend(btnDownload);

				vTmp.addComponent(rowTmp);
			}
		}
		vTmp.setWidth("100%");
		vTmp.addStyleName("vLayout-sublayout-thongtin-donthu");

		return vTmp;
	}

	public VerticalLayout buildLayoutThongTinNguoiDiKNTC() throws Exception
	{
		VerticalLayout vTmp = new VerticalLayout();
		List<DoiTuongDiKNTCBean> list = DonThuServiceUtil.getNguoiDaiDienDonThu(idDonThu);
		Label lblCaptionNguoiDiKNTC = new Label("Thông tin người chủ thể đơn thư",ContentMode.HTML);
		lblCaptionNguoiDiKNTC.addStyleName("label-subcaption-thongtin-donthu");

		String loaiChuThe = "";
		for(LoaiNguoiDiKNTCEnum e : LoaiNguoiDiKNTCEnum.values())
		{
			if(e.getType()== modelDonThu.getLoaiNguoiDiKNTC())
				loaiChuThe = e.getName();
		}
		ResponsiveRow row0 = new ResponsiveRow();
		row0.addColumn().withComponent(buildLabelDisplay("Số người: ", modelDonThu.getSoNguoiDiKNTC()+"", "left")).withDisplayRules(12, 12, 4, 6);
		row0.addColumn().withComponent(buildLabelDisplay("Số người đại diện: ", modelDonThu.getSoNguoiDaiDien()+"", "right")).withDisplayRules(12, 12, 4, 6);

		ResponsiveRow row1 = new ResponsiveRow();
		row1.addColumn().withComponent(buildLabelDisplay("Tên cơ quan: ", modelDonThu.getTenCoQuanDiKNTC(), "left")).withDisplayRules(12, 12, 8, 6);
		row1.addColumn().withComponent(buildLabelDisplay("Địa chỉ cơ quan: ", modelDonThu.getDiaChiCoQuanDiKNTC(), "right")).withDisplayRules(12, 12, 4, 6);

		vTmp.addComponent(lblCaptionNguoiDiKNTC);
		vTmp.addComponent(buildLabelDisplay("Loại chủ thể: ", "<b>"+loaiChuThe+"</b>", "left"));
		vTmp.addComponent(row0);
		vTmp.addComponent(row1);
		for(DoiTuongDiKNTCBean modelTmp : list)
		{
			vTmp.addComponent(buildLayoutNguoiDaiDienSingle(modelTmp));
		}

		vTmp.setWidth("100%");
		vTmp.addStyleName("vLayout-sublayout-thongtin-donthu");
		return vTmp;
	}

	public VerticalLayout buildLayoutNguoiBiKNTC(DoiTuongBiKNTCBean modelTmp) throws Exception
	{
		VerticalLayout vTmp = new VerticalLayout();
		Label lblCaptionNguoiBiKNTC = new Label("Thông tin người bị khiếu nại tố cáo",ContentMode.HTML);
		lblCaptionNguoiBiKNTC.addStyleName("label-subcaption-thongtin-donthu");

		String loaiBiKhieuTo = "";
		for(LoaiNguoiBiKNTCEnum e : LoaiNguoiBiKNTCEnum.values())
		{
			if(e.getType()== modelDonThu.getLoaiNguoiBiKNTC())
				loaiBiKhieuTo = e.getName();
		}

		vTmp.addComponent(lblCaptionNguoiBiKNTC);
		vTmp.addComponent(buildLabelDisplay("Loại người bị khiếu tố: ", "<b>"+loaiBiKhieuTo+"</b>", "left"));
		if(modelDonThu.getLoaiNguoiBiKNTC()==1)
		{
			ResponsiveRow row1 = new ResponsiveRow();
			row1.addColumn().withComponent(buildLabelDisplay("Họ tên: ", modelTmp.getHoTen(), "left")).withDisplayRules(12, 12, 8, 8);
			row1.addColumn().withComponent(buildLabelDisplay("Giới tính: ", modelTmp.getGioiTinh()==1?"Nam":"Nữ", "right")).withDisplayRules(12, 12, 4, 4);

			ResponsiveRow row2 = new ResponsiveRow();
			row2.addColumn().withComponent(buildLabelDisplay("Số định danh: ", modelTmp.getSoDinhDanhCaNhan(), "left")).withDisplayRules(12, 12, 8, 4);
			row2.addColumn().withComponent(buildLabelDisplay("Nơi cấp: ", modelTmp.getNoiCapSoDinhDanh(), "left")).withDisplayRules(12, 12, 4, 4);
			row2.addColumn().withComponent(buildLabelDisplay("Ngày cấp: ", modelTmp.getNgayCapSoDinhDanh()!=null ? sdf.format(modelTmp.getNgayCapSoDinhDanh()) : "", "right")).withDisplayRules(12, 12, 4, 4);

			ResponsiveRow row3 = new ResponsiveRow();
			row3.addColumn().withComponent(buildLabelDisplay("Nơi công tác: ", modelTmp.getNoiCongTac(), "left")).withDisplayRules(12, 12, 8, 8);
			row3.addColumn().withComponent(buildLabelDisplay("Chức vụ: ", modelTmp.getChucVu(),"right")).withDisplayRules(12, 12, 4, 4);

			ResponsiveRow row4 = new ResponsiveRow();
			row4.addColumn().withComponent(buildLabelDisplay("Quốc tịch: ", DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.quoctich.getName(), modelTmp.getMaQuocTich()).getName(), "left")).withDisplayRules(12, 12, 8, 8);
			row4.addColumn().withComponent(buildLabelDisplay("Dân tộc: ", DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.dantoc.getName(), modelTmp.getMaDanToc()).getName(),"right")).withDisplayRules(12, 12, 4, 4);

			vTmp.addComponent(row1);
			vTmp.addComponent(row2);
			vTmp.addComponent(row3);
			vTmp.addComponent(row4);
			vTmp.addComponent(buildLabelDisplay("Địa chỉ chi tiết: ", DonThuModule.returnDiaChiChiTiet(modelTmp.getDiaChiChiTiet(), modelTmp.getMaTinh(), modelTmp.getMaHuyen(), modelTmp.getMaXa()), "left"));
		}
		else if(modelDonThu.getLoaiNguoiBiKNTC()==2)
		{
			vTmp.addComponent(buildLabelDisplay("Tên cơ quan: ", modelTmp.getTenCoQuanToChuc(), "left"));
			vTmp.addComponent(buildLabelDisplay("Địa chỉ cơ quan: ", DonThuModule.returnDiaChiChiTiet(modelTmp.getDiaChiChiTietCoQuan(), modelTmp.getMaTinhCoQuan(), modelTmp.getMaHuyenCoQuan(), modelTmp.getMaXaCoQuan()), "left"));
		}
		vTmp.setWidth("100%");
		vTmp.addStyleName("vLayout-sublayout-thongtin-donthu");

		return vTmp;
	}

	public VerticalLayout buildLayoutNguoiUyQuyen(DoiTuongUyQuyenBean modelTmp) throws Exception
	{
		VerticalLayout vTmp = new VerticalLayout();
		Label lblCaptionNguoiUyQuyen = new Label("Thông tin người ủy quyền",ContentMode.HTML);
		lblCaptionNguoiUyQuyen.addStyleName("label-subcaption-thongtin-donthu");

		String loaiUyQuyen = "";
		for(LoaiNguoiUyQuyenEnum e : LoaiNguoiUyQuyenEnum.values())
		{
			if(e.getType()== modelDonThu.getLoaiNguoiUyQuyen())
				loaiUyQuyen = e.getName();
		}

		ResponsiveRow row1 = new ResponsiveRow();
		row1.addColumn().withComponent(buildLabelDisplay("Họ tên: ", modelTmp.getHoTen(), "left")).withDisplayRules(12, 12, 8, 8);
		row1.addColumn().withComponent(buildLabelDisplay("Giới tính: ", modelTmp.getGioiTinh()==1?"Nam":"Nữ", "right")).withDisplayRules(12, 12, 4, 4);

		ResponsiveRow row2 = new ResponsiveRow();
		row2.addColumn().withComponent(buildLabelDisplay("Số định danh: ", modelTmp.getSoDinhDanhCaNhan(), "left")).withDisplayRules(12, 12, 8, 4);
		row2.addColumn().withComponent(buildLabelDisplay("Nơi cấp: ", modelTmp.getNoiCapSoDinhDanh(), "left")).withDisplayRules(12, 12, 4, 4);
		row2.addColumn().withComponent(buildLabelDisplay("Ngày cấp: ", modelTmp.getNgayCapSoDinhDanh()!=null ? sdf.format(modelTmp.getNgayCapSoDinhDanh()):"", "right")).withDisplayRules(12, 12, 4, 4);

		ResponsiveRow row3 = new ResponsiveRow();
		row3.addColumn().withComponent(buildLabelDisplay("Quốc tịch: ", DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.quoctich.getName(), modelTmp.getMaQuocTich()).getName(), "left")).withDisplayRules(12, 12, 8, 8);
		row3.addColumn().withComponent(buildLabelDisplay("Dân tộc: ", DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.dantoc.getName(), modelTmp.getMaDanToc()).getName(),"right")).withDisplayRules(12, 12, 4, 4);

		vTmp.addComponent(lblCaptionNguoiUyQuyen);
		vTmp.addComponent(buildLabelDisplay("Loại người ủy quyền: ", "<b>"+loaiUyQuyen+"</b>", "left"));
		vTmp.addComponent(row1);
		vTmp.addComponent(row2);
		vTmp.addComponent(row3);
		vTmp.addComponent(buildLabelDisplay("Địa chỉ chi tiết: ", DonThuModule.returnDiaChiChiTiet(modelTmp.getDiaChiChiTiet(), modelTmp.getMaTinh(), modelTmp.getMaHuyen(), modelTmp.getMaXa()), "left"));
		vTmp.setWidth("100%");

		vTmp.addStyleName("vLayout-sublayout-thongtin-donthu");

		return vTmp;
	}


	public VerticalLayout buildLayoutNguoiDaiDienSingle(DoiTuongDiKNTCBean modelTmp) throws Exception
	{
		VerticalLayout vNguoiDaiDienTmp = new VerticalLayout();

		ResponsiveRow row1 = new ResponsiveRow();
		row1.addColumn().withComponent(buildLabelDisplay("Họ tên: ", modelTmp.getHoTen(), "left")).withDisplayRules(12, 12, 8, 8);
		row1.addColumn().withComponent(buildLabelDisplay("Giới tính: ", modelTmp.getGioiTinh()==1?"Nam":"Nữ", "right")).withDisplayRules(12, 12, 4, 4);

		ResponsiveRow row2 = new ResponsiveRow();
		row2.addColumn().withComponent(buildLabelDisplay("Số định danh: ", modelTmp.getSoDinhDanhCaNhan(), "left")).withDisplayRules(12, 12, 8, 4);
		row2.addColumn().withComponent(buildLabelDisplay("Nơi cấp: ", modelTmp.getNoiCapSoDinhDanh(), "left")).withDisplayRules(12, 12, 4, 4);
		row2.addColumn().withComponent(buildLabelDisplay("Ngày cấp: ", modelTmp.getNgayCapSoDinhDanh()!=null ? sdf.format(modelTmp.getNgayCapSoDinhDanh()): "", "right")).withDisplayRules(12, 12, 4, 4);

		ResponsiveRow row3 = new ResponsiveRow();
		row3.addColumn().withComponent(buildLabelDisplay("Quốc tịch: ", DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.quoctich.getName(), modelTmp.getMaQuocTich()).getName(), "left")).withDisplayRules(12, 12, 8, 8);
		row3.addColumn().withComponent(buildLabelDisplay("Dân tộc: ", DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.dantoc.getName(), modelTmp.getMaDanToc()).getName(),"right")).withDisplayRules(12, 12, 4, 4);


		vNguoiDaiDienTmp.addComponent(row1);
		vNguoiDaiDienTmp.addComponent(row2);
		vNguoiDaiDienTmp.addComponent(row3);
		vNguoiDaiDienTmp.addComponent(buildLabelDisplay("Địa chỉ chi tiết: ", DonThuModule.returnDiaChiChiTiet(modelTmp.getDiaChiChiTiet(), modelTmp.getMaTinh(), modelTmp.getMaHuyen(), modelTmp.getMaXa()), "left"));
		vNguoiDaiDienTmp.setWidth("100%");
		vNguoiDaiDienTmp.addStyleName("vLayout-sublayout-nguoidaidien-thongtin-donthu");

		return vNguoiDaiDienTmp;
	}

	public Label buildLabelDisplay(String caption,String value,String align)
	{
		Label lblReturn = new Label("",ContentMode.HTML);
		caption = "<b style='color: #0f50a2'>"+caption+"</b>";
		if(value==null)
		{
			value="";
		}

		lblReturn.setValue("<div style='text-align: "+align+"'>"+caption+value+"</div>");

		return lblReturn;
	}
}
