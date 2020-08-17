package ngn.kntc.page.donthu.chitiet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import ngn.kntc.beans.DoiTuongBiKNTCBean;
import ngn.kntc.beans.DoiTuongDiKNTCBean;
import ngn.kntc.beans.DoiTuongUyQuyenBean;
import ngn.kntc.beans.DonThuBean;
import ngn.kntc.beans.GeneralStringStringBean;
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

import com.jarektoro.responsivelayout.ResponsiveRow;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class ChiTietDonThuLayoutTmp extends VerticalLayout{
	HorizontalLayout hChiTiet = new HorizontalLayout();

	Button btnSua = new Button("Sửa thông tin đơn thư",FontAwesome.EDIT);

	VerticalLayout vThongTinDon = new VerticalLayout();
	VerticalLayout vThongTinDoiTuong = new VerticalLayout();

	DonThuServiceUtil svDonThu = new DonThuServiceUtil();
	TiepCongDanServiceUtil svTCD = new TiepCongDanServiceUtil();
	DanhMucServiceUtil svDanhMuc = new DanhMucServiceUtil();

	DonThuBean modelDonThu;
	ThongTinDonThuBean modelThongTinDon;
	int idDonThu = -1;
	
	String chuaXacDinhValue = ChiTietDonThuTCDGeneralFunction.chuaXacDinhValue;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	public ChiTietDonThuLayoutTmp(int idDonThu) {
		try {
			this.idDonThu = idDonThu;
			modelDonThu = svDonThu.getDonThu(idDonThu);
			modelThongTinDon = svDonThu.getThongTinDonThu(idDonThu, SessionUtil.getOrgId());

			buildLayout();
			configComponent();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void buildLayout() throws Exception {
		this.addComponent(btnSua);
		this.addComponent(hChiTiet);

		hChiTiet.addComponent(vThongTinDon);
		hChiTiet.addComponent(vThongTinDoiTuong);
		
		btnSua.addStyleName(ValoTheme.BUTTON_PRIMARY);
		btnSua.addStyleName(ValoTheme.BUTTON_SMALL);
		
		vThongTinDon.setWidth("100%");
		vThongTinDon.setSpacing(true);
		
		vThongTinDoiTuong.setWidth("100%");
		vThongTinDoiTuong.setSpacing(true);

		hChiTiet.setSpacing(true);
		hChiTiet.setWidth("100%");

		this.setExpandRatio(hChiTiet, 1.0f);
		
		this.setComponentAlignment(btnSua, Alignment.MIDDLE_RIGHT);

		this.addStyleName("chitiet-info-layout");

		this.setSizeFull();
		this.setSpacing(true);
		this.setMargin(true);

		buildThongTinDonLayout();
		buildThongTinDoiTuongLayout();
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
				
				UI.getCurrent().addWindow(wdUpdate);
				donThu.getBtnSave().addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						if(donThu.isValidateSuccess())
						{
							wdUpdate.close();
							try {
								modelDonThu = svDonThu.getDonThu(idDonThu);
								modelThongTinDon = svDonThu.getThongTinDonThu(idDonThu, SessionUtil.getOrgId());
								buildThongTinDonLayout();
								buildThongTinDoiTuongLayout();
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
			}
		});
	}

	private void buildThongTinDonLayout() throws Exception
	{
		vThongTinDon.removeAllComponents();
		
		vThongTinDon.addComponent(buildThongTinDonBlock());
		if(modelThongTinDon.getMaCoQuanChuyenDen()!=null)
			vThongTinDon.addComponent(buildThongTinCoQuanChuyenDenBlock());
		if(modelDonThu.getMaCoQuanDaGiaiQuyet()!=null)
			vThongTinDon.addComponent(buildThongTinCoQuanDaGiaiQuyetBlock());
		vThongTinDon.addComponent(buildThongTinDonNoiDungDonThu());
	}

	private void buildThongTinDoiTuongLayout() throws Exception
	{
		vThongTinDoiTuong.removeAllComponents();
		
		vThongTinDoiTuong.addComponent(ChiTietDonThuTCDGeneralFunction.buildThongTinDoiTuongDiKNTCBlock(true,modelDonThu,null));
		if(modelDonThu.getMaDoiTuongBiKNTC()!=0)
			vThongTinDoiTuong.addComponent(ChiTietDonThuTCDGeneralFunction.buildThongTinDoiTuongBiKNTCBlock(svDonThu.getDoiTuongBiKNTC(modelDonThu.getMaDoiTuongBiKNTC()),modelDonThu.getLoaiNguoiBiKNTC()));
		if(modelDonThu.getMaDoiTuongUyQuyen()!=0)
			vThongTinDoiTuong.addComponent(ChiTietDonThuTCDGeneralFunction.buildThongTinDoiTuongUyQuyentBlock(svDonThu.getDoiTuongUyQuyen(modelDonThu.getMaDoiTuongUyQuyen()),modelDonThu.getLoaiNguoiUyQuyen()));
	}

	private VerticalLayout buildThongTinDonBlock() throws Exception
	{
		String loaiDonThu = "";
		String donNacDanh = modelDonThu.isDonNacDanh()?FontAwesome.CHECK.getHtml():FontAwesome.REMOVE.getHtml();
		String donDuDKXuLy = modelDonThu.isDonKhongDuDieuKienXuLy()?FontAwesome.CHECK.getHtml():FontAwesome.REMOVE.getHtml();

		String nguonDon = DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.nguondon.getName(), modelThongTinDon.getNguonDonDen()).getName();
		String thamQuyen = chuaXacDinhValue;

		String ngayNhan = sdf.format(modelDonThu.getNgayNhanDon());
		String ngayNhap = sdf.format(modelDonThu.getNgayNhapDon());
		String nguoiNhap = UserLocalServiceUtil.getUser(modelDonThu.getUserNhapDon()).getFirstName();

		for(LoaiDonThuEnum e : LoaiDonThuEnum.values())
		{
			if(e.getType()==modelDonThu.getLoaiDonThu())
				loaiDonThu = e.getName();
		}

		for(ThamQuyenGiaiQuyetEnum e : ThamQuyenGiaiQuyetEnum.values())
		{
			if(e.getType()==modelDonThu.getThamQuyenGiaiQuyet())
				thamQuyen = e.getName();
		}

		List<Label> listThongTinDon = new ArrayList<Label>();
		listThongTinDon.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Loại đơn thư", loaiDonThu),new GeneralStringStringBean("Đơn nặc danh", donNacDanh),new GeneralStringStringBean("Đơn không đủ điều kiện thụ lý", donDuDKXuLy))));
		listThongTinDon.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Nguồn đơn", nguonDon),new GeneralStringStringBean("Thẩm quyền giải quyết", thamQuyen))));
		listThongTinDon.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Ngày nhận", ngayNhan),new GeneralStringStringBean("Ngày nhập", ngayNhap),new GeneralStringStringBean("Người nhập", nguoiNhap))));

		return ChiTietDonThuTCDGeneralFunction.buildBlockThongTin("Thông tin đơn thư", listThongTinDon,FontAwesome.PAPERCLIP.getHtml());
	}

	private VerticalLayout buildThongTinCoQuanChuyenDenBlock() throws Exception
	{
		String coQuanChuyen = DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.coquan.getName(), modelThongTinDon.getMaCoQuanChuyenDen()).getName();

		String soVanBan = modelThongTinDon.getSoVanBanDen();
		String ngayPhatHanhVanBan = sdf.format(modelThongTinDon.getNgayPhatHanhVanBanDen());

		List<Label> listCoQuanChuyenDen = new ArrayList<Label>();
		listCoQuanChuyenDen.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Cơ quan chuyển đơn", coQuanChuyen))));
		listCoQuanChuyenDen.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Số văn bản đến", soVanBan),new GeneralStringStringBean("Ngày phát hành", ngayPhatHanhVanBan))));

		return ChiTietDonThuTCDGeneralFunction.buildBlockThongTin("Cơ quan chuyển đến", listCoQuanChuyenDen,FontAwesome.PAPERCLIP.getHtml());
	}

	private VerticalLayout buildThongTinCoQuanDaGiaiQuyetBlock() throws Exception
	{
		String coQuanDaGiaiQuyet = DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.coquan.getName(), modelDonThu.getMaCoQuanDaGiaiQuyet()).getName();

		String loaiQuyetDinh = DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.hinhthucgiaiquyet.getName(), modelDonThu.getLoaiQuyetDinhGiaiQuyet()).getName();

		String lanGiaiQuyet = String.valueOf(modelDonThu.getLanGiaiQuyet());
		String soKyHieu = modelDonThu.getSoKyHieuVanBanGiaiQuyet();
		String ngayPhatHanh = sdf.format(modelDonThu.getNgayBanHanhQDGQ());

		List<Label> listCoQuanDaGiaiQuyet = new ArrayList<Label>();
		listCoQuanDaGiaiQuyet.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Cơ quan đã giải quyết", coQuanDaGiaiQuyet))));
		listCoQuanDaGiaiQuyet.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Loại quyết định", loaiQuyetDinh))));
		listCoQuanDaGiaiQuyet.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Lần giải quyết", lanGiaiQuyet),new GeneralStringStringBean("Số ký hiệu văn bản", soKyHieu),new GeneralStringStringBean("Ngày ban hành văn bản", ngayPhatHanh))));

		return ChiTietDonThuTCDGeneralFunction.buildBlockThongTin("Cơ quan đã giải quyết", listCoQuanDaGiaiQuyet,FontAwesome.PAPERCLIP.getHtml());
	}

	private VerticalLayout buildThongTinDonNoiDungDonThu() throws Exception
	{
		VerticalLayout vTmp = new VerticalLayout();
		Label lblCaption = new Label("Nội dung đơn thư",ContentMode.HTML);
		Label lblIcon = new Label(FontAwesome.PAPERCLIP.getHtml(),ContentMode.HTML);

		String noiDungDonThu = modelDonThu.getNoiDungDonThu();

		List<String> listLinhVuc = svDonThu.getLinhVucList(idDonThu);

		List<HoSoDinhKemBean> listHoSoDinhKem = svDonThu.getDinhKemHoSoList(idDonThu);

		vTmp.addComponent(lblCaption);

		vTmp.addComponent(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Nội dung đơn thư", noiDungDonThu))));

		if(!listLinhVuc.isEmpty())
		{
			for(String idLinhVuc : listLinhVuc)
			{
				HashMap<String, String> lv = DonThuModule.getLinhVucLevel(idLinhVuc);
				List<GeneralStringStringBean> listTmp = new ArrayList<GeneralStringStringBean>();

				if(lv.containsKey("lv2"))
					listTmp.add(new GeneralStringStringBean("Lĩnh vực",svDanhMuc.getLinhVuc(lv.get("lv2")).getName()));
				if(lv.containsKey("lv3"))
					listTmp.add(new GeneralStringStringBean("Chi tiết",svDanhMuc.getLinhVuc(lv.get("lv3")).getName()));
				if(lv.containsKey("lv4"))
					listTmp.add(new GeneralStringStringBean("Diễn giải",svDanhMuc.getLinhVuc(lv.get("lv4")).getName()));

				vTmp.addComponent(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(listTmp));
			}
		}

		if(!listHoSoDinhKem.isEmpty())
		{
			for(HoSoDinhKemBean model : listHoSoDinhKem)
			{
				Button btnDownload = new Button("<span style='color: #1f69a5'>"+model.getTenFileDinhKem()+"</span>",FontAwesome.DOWNLOAD);
				HorizontalLayout hTmp = new HorizontalLayout();
				hTmp.addComponent(new Label("<b>Tên hồ sơ: </b>"+model.getTenHoSo(),ContentMode.HTML));
				hTmp.addComponent(new Label("<b>Loại hồ sơ: </b>"+DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.loaitailieu.getName(), model.getLoaiHoSo()).getName(),ContentMode.HTML));
				hTmp.addComponent(btnDownload);

				hTmp.addStyleName("chitiet-info-block-row");
				hTmp.addStyleName("horizon-row-overflow");
				hTmp.setWidth("100%");

				Page.getCurrent().getStyles().add(".horizon-row-overflow{overflow:hidden !important;}");

				/* Button download */
				btnDownload.setCaptionAsHtml(true);
				btnDownload.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				btnDownload.addStyleName(ValoTheme.BUTTON_SMALL);
				btnDownload.setHeight("20px");

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

				vTmp.addComponent(hTmp);
			}
		}

		vTmp.addComponent(lblIcon);

		lblCaption.addStyleName("chitiet-info-block-caption");
		lblIcon.addStyleName("chitiet-info-block-icon");

		vTmp.setWidth("100%");
		vTmp.addStyleName("chitiet-info-block");

		return vTmp;
	}
}
