package ngn.kntc.page.donthu.danhsach.xulydon;

import java.util.ArrayList;
import java.util.List;

import ngn.kntc.beans.DonThuBean;
import ngn.kntc.beans.FilterDonChuyenAutoBean;
import ngn.kntc.beans.FilterDonDaChuyenDiBean;
import ngn.kntc.beans.KetQuaXuLyBean;
import ngn.kntc.beans.QuyetDinhGiaiQuyetBean;
import ngn.kntc.beans.ThongTinDonThuBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.enums.LoaiDonThuEnum;
import ngn.kntc.enums.LoaiTinhTrangChuyenDiEnum;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.page.donthu.danhsach.LayoutDanhSachDonThu;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.SessionUtil;

import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class LayoutDanhSachDonDaChuyenDiAuto extends LayoutDanhSachDonThu{
	private HorizontalLayout hOverview = new HorizontalLayout();

	private ComboBox cmbDonViChuyen = new ComboBox();
	private ComboBox cmbLoaiDonThu = new ComboBox();

	private final String STT = "STT";
	private final String NGUOIDUNGDON = "Người đứng đơn";
	private final String NOIDUNG = "Nội dung đơn thư";
	private final String LOAIDONTHU = "Loại đơn thư";
	private final String DONVICHUYENDI = "Đơn vị chuyển đi";
	private final String NGAYNHAPDON = "Ngày nhận đơn";
	private final String NGAYCHUYENDI = "Ngày chuyển đi";

	private final String NGAYXULY = "Ngày xử lý";
	private final String KETQUAXULY = "Kết quả xử lý";
	private final String NGAYTHULY = "Ngày thụ lý";
	private final String HANGIAIQUYET = "Ngày hạn giải quyết";
	private final String NGAYGIAIQUYET = "Ngày giải quyết";
	private final String KETQUAGIAIQUYET = "Kết quả giải quyết";

	private LoaiTinhTrangChuyenDiEnum current_type = null;

	private 	List<VerticalLayout> listOverviewBlock = new ArrayList<VerticalLayout>();

	public LayoutDanhSachDonDaChuyenDiAuto() {
		String mainCaption = " Quản lý danh sách đơn thư đã chuyển đi chưa có kết quả giải quyết";
		String subCaption = "+  Quản lý danh sách đơn thư không đủ thẩm quyền giải quyết và đã chuyển sang đơn vị khác bằng phần mềm nhưng đơn vị đó chưa giải quyết đơn";

		lblMainCaption.setValue(FontAwesome.BOOK.getHtml()+mainCaption);
		lblSubCaption.setValue(subCaption);

		hFilter.addComponents(cmbDonViChuyen,new Label("<b>Loại đơn thư</b>",ContentMode.HTML),cmbLoaiDonThu);

		cmbDonViChuyen.addStyleName(ValoTheme.COMBOBOX_SMALL);
		cmbDonViChuyen.setWidth("250px");

		cmbLoaiDonThu.addStyleName(ValoTheme.COMBOBOX_SMALL);

		this.addComponent(hOverview,1);

		//buildTable();
		loadDefaultValue();
		buildOverviewLayout();
		try {
			loadData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void buildOverviewLayout()
	{
		hOverview.addComponent(buildBlockOverview(LoaiTinhTrangChuyenDiEnum.chuaxuly));
		hOverview.addComponent(buildBlockOverview(LoaiTinhTrangChuyenDiEnum.daxuly));
		hOverview.addComponent(buildBlockOverview(LoaiTinhTrangChuyenDiEnum.chuagiaiquyet));
		hOverview.addComponent(buildBlockOverview(LoaiTinhTrangChuyenDiEnum.dagiaiquyet));

		hOverview.setWidth("100%");
		hOverview.setSpacing(true);
	}

	public void buildTable() {
		super.buildTable();

		container = new IndexedContainer();
		
		container.addContainerProperty(STT, Integer.class, null);
		container.addContainerProperty(NGUOIDUNGDON, Label.class, null);
		container.addContainerProperty(NOIDUNG, Label.class, null);
		container.addContainerProperty(LOAIDONTHU, String.class, null);
		container.addContainerProperty(DONVICHUYENDI, String.class, null);
		container.addContainerProperty(NGAYNHAPDON, String.class, null);
		container.addContainerProperty(NGAYCHUYENDI, String.class, null);
		
		if(current_type!=null)
		{
			switch (current_type) {
			case daxuly:
				container.addContainerProperty(NGAYXULY, String.class, null);
				container.addContainerProperty(KETQUAXULY, String.class, null);
				break;
			case chuagiaiquyet:
				container.addContainerProperty(NGAYTHULY, String.class, null);
				container.addContainerProperty(HANGIAIQUYET, String.class, null);
				break;
			case dagiaiquyet:
				container.addContainerProperty(NGAYGIAIQUYET, String.class, null);
				container.addContainerProperty(KETQUAGIAIQUYET, String.class, null);
				break;
			default:
				break;
			}
		}

		tblDanhSach.setImmediate(true);

		tblDanhSach.setContainerDataSource(container);

		tblDanhSach.setColumnExpandRatio(NOIDUNG, 1.0f);
	}

	public VerticalLayout buildBlockOverview(LoaiTinhTrangChuyenDiEnum type)
	{
		VerticalLayout vBLock = new VerticalLayout();
		String strMainCaption = type.getName();
		String strUnExpired = "",strExpired = "";
		int keyUnExpiredValue = 0,keyExpiredValue = 0;

		LoaiTinhTrangChuyenDiEnum typeUnExpired = null;
		LoaiTinhTrangChuyenDiEnum typeExpired = null;

		switch (type) {

		case chuaxuly:
			typeUnExpired = LoaiTinhTrangChuyenDiEnum.chuaxulytronghan;
			typeExpired = LoaiTinhTrangChuyenDiEnum.chuaxulyquahan;
			break;
		case daxuly:
			typeUnExpired = LoaiTinhTrangChuyenDiEnum.daxulytronghan;
			typeExpired = LoaiTinhTrangChuyenDiEnum.daxulyquahan;
			break;
		case chuagiaiquyet:
			typeUnExpired = LoaiTinhTrangChuyenDiEnum.chuagiaiquyettronghan;
			typeExpired = LoaiTinhTrangChuyenDiEnum.chuagiaiquyetquahan;
			break;
		case dagiaiquyet:
			typeUnExpired = LoaiTinhTrangChuyenDiEnum.dagiaiquyettronghan;
			typeExpired = LoaiTinhTrangChuyenDiEnum.dagiaiquyetquahan;
			break;
		}

		strUnExpired = typeUnExpired.getName();
		strExpired = typeExpired.getName();

		keyUnExpiredValue = typeUnExpired.getType();
		keyExpiredValue = typeExpired.getType();

		Label lblMainCaption = new Label("<div class='overview-caption'>"+strMainCaption+"<div>",ContentMode.HTML);
		Label lblMainValue = new Label("<div class='overviewsum' style='width:50px' tinhtrangchuyen='"+type.getType()+"'>15<div>",ContentMode.HTML);

		Button btnUnExpiredCaption = new Button(strUnExpired);
		Label lblUnExpiredValue = new Label("<div class='overviewcount' style='width:50px' tinhtrangchuyen='"+keyUnExpiredValue+"'>10</div>",ContentMode.HTML);

		btnUnExpiredCaption.setCaptionAsHtml(true);
		btnUnExpiredCaption.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		btnUnExpiredCaption.addStyleName(ValoTheme.BUTTON_SMALL);
		btnUnExpiredCaption.addStyleName("overview-subcaption");
		btnUnExpiredCaption.setWidth("100%");

		Button btnExpiredCaption = new Button(strExpired);
		Label lblExpiredValue = new Label("<div class='overviewcount' style='width:50px' tinhtrangchuyen='"+keyExpiredValue+"'>5</div>",ContentMode.HTML);

		btnExpiredCaption.setCaptionAsHtml(true);
		btnExpiredCaption.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		btnExpiredCaption.addStyleName(ValoTheme.BUTTON_SMALL);
		btnExpiredCaption.addStyleName("overview-subcaption");
		btnExpiredCaption.setWidth("100%");

		HorizontalLayout hMainCaption = new HorizontalLayout(lblMainCaption,lblMainValue);

		hMainCaption.setExpandRatio(lblMainCaption, 0.8f);
		hMainCaption.setExpandRatio(lblMainValue, 0.2f);
		hMainCaption.setComponentAlignment(lblMainValue,Alignment.MIDDLE_CENTER);
		hMainCaption.setWidth("100%");
		hMainCaption.setSpacing(true);

		HorizontalLayout hUnExpired = new HorizontalLayout(btnUnExpiredCaption,lblUnExpiredValue);

		hUnExpired.setExpandRatio(btnUnExpiredCaption, 0.8f);
		hUnExpired.setExpandRatio(lblUnExpiredValue, 0.2f);
		hUnExpired.setComponentAlignment(lblUnExpiredValue,Alignment.MIDDLE_CENTER);
		hUnExpired.setWidth("100%");
		hUnExpired.setSpacing(true);

		HorizontalLayout hExpired = new HorizontalLayout(btnExpiredCaption,lblExpiredValue);

		hExpired.setExpandRatio(btnExpiredCaption, 0.8f);
		hExpired.setExpandRatio(lblExpiredValue, 0.2f);
		hExpired.setComponentAlignment(lblExpiredValue,Alignment.MIDDLE_CENTER);
		hExpired.setWidth("100%");
		hExpired.setSpacing(true);

		vBLock.addComponent(hMainCaption);
		vBLock.addComponent(hUnExpired);
		vBLock.addComponent(hExpired);

		vBLock.setWidth("100%");
		vBLock.addStyleName("qlds-chuyendon-overviewblock");
		listOverviewBlock.add(vBLock);

		LoaiTinhTrangChuyenDiEnum typeUnExpiredWTF = typeUnExpired;
		LoaiTinhTrangChuyenDiEnum typeExpiredWTF = typeExpired;

		btnUnExpiredCaption.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if(vBLock.getStyleName().indexOf("qlds-chuyendon-overviewblock-active")>-1 && current_type == typeUnExpiredWTF)
				{
					current_type = null;
					vBLock.removeStyleName("qlds-chuyendon-overviewblock-active");
				}
				else
				{
					current_type=typeUnExpiredWTF;

					for(VerticalLayout block : listOverviewBlock)
					{
						block.removeStyleName("qlds-chuyendon-overviewblock-active");
					}

					vBLock.addStyleName("qlds-chuyendon-overviewblock-active");
				}
				try {
					loadData();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		btnExpiredCaption.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if(vBLock.getStyleName().indexOf("qlds-chuyendon-overviewblock-active")>-1 && current_type == typeExpiredWTF)
				{
					current_type = null;
					vBLock.removeStyleName("qlds-chuyendon-overviewblock-active");
				}
				else
				{
					current_type=typeExpiredWTF;

					for(VerticalLayout block : listOverviewBlock)
					{
						block.removeStyleName("qlds-chuyendon-overviewblock-active");
					}

					vBLock.addStyleName("qlds-chuyendon-overviewblock-active");
				}
				try {
					loadData();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		vBLock.addLayoutClickListener(new LayoutClickListener() {

			@Override
			public void layoutClick(LayoutClickEvent event) {
				if(vBLock.getStyleName().indexOf("qlds-chuyendon-overviewblock-active")>-1 && current_type == type)
				{
					current_type = null;
					vBLock.removeStyleName("qlds-chuyendon-overviewblock-active");
				}
				else
				{
					current_type = type;

					for(VerticalLayout block : listOverviewBlock)
					{
						block.removeStyleName("qlds-chuyendon-overviewblock-active");
					}

					vBLock.addStyleName("qlds-chuyendon-overviewblock-active");
				}
				try {
					loadData();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		return vBLock;
	}

	private void setCountValueOverview(FilterDonChuyenAutoBean modelFilter) throws Exception
	{
		JavaScript js = Page.getCurrent().getJavaScript();
		LoaiTinhTrangChuyenDiEnum eChuaXuLy = LoaiTinhTrangChuyenDiEnum.chuaxuly;
		LoaiTinhTrangChuyenDiEnum eChuaXuLyTrongHan = LoaiTinhTrangChuyenDiEnum.chuaxulytronghan;
		LoaiTinhTrangChuyenDiEnum eChuaXuLyQuaHan = LoaiTinhTrangChuyenDiEnum.chuaxulyquahan;

		LoaiTinhTrangChuyenDiEnum eDaXuLy = LoaiTinhTrangChuyenDiEnum.daxuly;
		LoaiTinhTrangChuyenDiEnum eDaXuLyTrongHan = LoaiTinhTrangChuyenDiEnum.daxulytronghan;
		LoaiTinhTrangChuyenDiEnum eDaXuLyQuaHan = LoaiTinhTrangChuyenDiEnum.daxulyquahan;

		LoaiTinhTrangChuyenDiEnum eChuaGiaiQuyet = LoaiTinhTrangChuyenDiEnum.chuagiaiquyet;
		LoaiTinhTrangChuyenDiEnum eChuaGiaiQuyetTrongHan = LoaiTinhTrangChuyenDiEnum.chuagiaiquyettronghan;
		LoaiTinhTrangChuyenDiEnum eChuaGiaiQuyetQuaHan = LoaiTinhTrangChuyenDiEnum.chuagiaiquyetquahan;

		LoaiTinhTrangChuyenDiEnum eDaGiaiQuyet = LoaiTinhTrangChuyenDiEnum.dagiaiquyet;
		LoaiTinhTrangChuyenDiEnum eDaGiaiQuyetTrongHan = LoaiTinhTrangChuyenDiEnum.dagiaiquyettronghan;
		LoaiTinhTrangChuyenDiEnum eDaGiaiQuyetQuaHan = LoaiTinhTrangChuyenDiEnum.dagiaiquyetquahan;

		int countChuaXuLy = svDonThu.countDonThuChuyenDi(eChuaXuLy,modelFilter);
		int countChuaXuLyTrongHan = svDonThu.countDonThuChuyenDi(eChuaXuLyTrongHan,modelFilter);
		int countChuaXuLyQuaHan = svDonThu.countDonThuChuyenDi(eChuaXuLyQuaHan,modelFilter);

		int countDaXuLy = svDonThu.countDonThuChuyenDi(eDaXuLy,modelFilter);
		int countDaXuLyTrongHan = svDonThu.countDonThuChuyenDi(eDaXuLyTrongHan,modelFilter);
		int countDaXuLyQuaHan = svDonThu.countDonThuChuyenDi(eDaXuLyQuaHan,modelFilter);

		int countChuaGiaiQuyet = svDonThu.countDonThuChuyenDi(eChuaGiaiQuyet,modelFilter);
		int countChuaGiaiQuyetTrongHan = svDonThu.countDonThuChuyenDi(eChuaGiaiQuyetTrongHan,modelFilter);
		int countChuaGiaiQuyetQuaHan = svDonThu.countDonThuChuyenDi(eChuaGiaiQuyetQuaHan,modelFilter);

		int countDaGiaiQuyet = svDonThu.countDonThuChuyenDi(eDaGiaiQuyet,modelFilter);
		int countDaGiaiQuyetTrongHan = svDonThu.countDonThuChuyenDi(eDaGiaiQuyetTrongHan,modelFilter);
		int countDaGiaiQuyetQuaHan = svDonThu.countDonThuChuyenDi(eDaGiaiQuyetQuaHan,modelFilter);

		js.execute("document.querySelector(\".overviewsum[tinhtrangchuyen='"+eChuaXuLy.getType()+"']\").innerHTML='"+countChuaXuLy+"';");
		js.execute("document.querySelector(\".overviewcount[tinhtrangchuyen='"+eChuaXuLyTrongHan.getType()+"']\").innerHTML='"+countChuaXuLyTrongHan+"';");
		js.execute("document.querySelector(\".overviewcount[tinhtrangchuyen='"+eChuaXuLyQuaHan.getType()+"']\").innerHTML='"+countChuaXuLyQuaHan+"';");

		js.execute("document.querySelector(\".overviewsum[tinhtrangchuyen='"+eDaXuLy.getType()+"']\").innerHTML='"+countDaXuLy+"';");
		js.execute("document.querySelector(\".overviewcount[tinhtrangchuyen='"+eDaXuLyTrongHan.getType()+"']\").innerHTML='"+countDaXuLyTrongHan+"';");
		js.execute("document.querySelector(\".overviewcount[tinhtrangchuyen='"+eDaXuLyQuaHan.getType()+"']\").innerHTML='"+countDaXuLyQuaHan+"';");

		js.execute("document.querySelector(\".overviewsum[tinhtrangchuyen='"+eChuaGiaiQuyet.getType()+"']\").innerHTML='"+countChuaGiaiQuyet+"';");
		js.execute("document.querySelector(\".overviewcount[tinhtrangchuyen='"+eChuaGiaiQuyetTrongHan.getType()+"']\").innerHTML='"+countChuaGiaiQuyetTrongHan+"';");
		js.execute("document.querySelector(\".overviewcount[tinhtrangchuyen='"+eChuaGiaiQuyetQuaHan.getType()+"']\").innerHTML='"+countChuaGiaiQuyetQuaHan+"';");

		js.execute("document.querySelector(\".overviewsum[tinhtrangchuyen='"+eDaGiaiQuyet.getType()+"']\").innerHTML='"+countDaGiaiQuyet+"';");
		js.execute("document.querySelector(\".overviewcount[tinhtrangchuyen='"+eDaGiaiQuyetTrongHan.getType()+"']\").innerHTML='"+countDaGiaiQuyetTrongHan+"';");
		js.execute("document.querySelector(\".overviewcount[tinhtrangchuyen='"+eDaGiaiQuyetQuaHan.getType()+"']\").innerHTML='"+countDaGiaiQuyetQuaHan+"';");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadData() throws Exception {
		buildTable();
		
		int orgDaChuyen = cmbDonViChuyen.getValue()!=null?(int)cmbDonViChuyen.getValue():0;
		FilterDonChuyenAutoBean modelFilter = new FilterDonChuyenAutoBean();
		modelFilter.setOrgDaChuyen(orgDaChuyen);
		modelFilter.setKeyWord(txtSearch.getValue().trim());
		modelFilter.setLoaiDonThu((int)cmbLoaiDonThu.getValue());
		setCountValueOverview(modelFilter);
		container.removeAllItems();
		List<DonThuBean> list = svDonThu.getListDonThuChuyenDi(current_type,modelFilter);

		int i = 0;
		for(DonThuBean model : list)
		{
			String noiDungDon = "";
			String loaiDonThu = "";
			String donViDaChuyen = "";
			String ngayChuyenDi = "";

			Item item = container.addItem(model.getMaDonThu());

			ThongTinDonThuBean modelThongTinDon = svDonThu.getThongTinDonThuChuyenDi(model.getMaDonThu(), SessionUtil.getOrgId());
			KetQuaXuLyBean modelKQXL = svQuaTrinh.getKetQuaXuLy(model.getMaDonThu(), SessionUtil.getOrgId());

			if(model.getMaDonThu()!=0)
			{
				noiDungDon = model.getNoiDungDonThu();
				if(noiDungDon.length()>120)
				{
					noiDungDon = noiDungDon.substring(0, 120)+"<b>.......</b>";
				}
			}

			for(LoaiDonThuEnum e : LoaiDonThuEnum.values())
			{
				if(e.getType()==model.getLoaiDonThu())
					loaiDonThu = e.getName();
			}

			ngayChuyenDi = sdfDatetime.format(modelThongTinDon.getNgayNhan());

			donViDaChuyen =svDanhMuc.getDanhMuc(DanhMucTypeEnum.coquan.getName(),modelKQXL.getMaCQXLTiep()).getName();

			item.getItemProperty(STT).setValue(++i);
			item.getItemProperty(NGUOIDUNGDON).setValue(new Label(DonThuModule.returnLoaiNguoiDiKNTCForTable(model.getLoaiNguoiDiKNTC(), model.getSoNguoiDiKNTC(), model.getSoNguoiDaiDien())+"<b>"+DonThuServiceUtil.returnTenNguoiDaiDienDonThu(model)+"</b>",ContentMode.HTML));
			item.getItemProperty(NOIDUNG).setValue(new Label(noiDungDon,ContentMode.HTML));
			item.getItemProperty(LOAIDONTHU).setValue(loaiDonThu);
			item.getItemProperty(DONVICHUYENDI).setValue(donViDaChuyen);
			item.getItemProperty(NGAYNHAPDON).setValue(sdfDatetime.format(model.getNgayNhapDon()));
			item.getItemProperty(NGAYCHUYENDI).setValue(ngayChuyenDi);

			if(current_type!=null)
			{
				switch (current_type) {
				case daxuly:
					KetQuaXuLyBean modelKQXLDonViChuyen = svQuaTrinh.getKetQuaXuLy(model.getMaDonThu(), modelThongTinDon.getOrgNhan());

					item.getItemProperty(NGAYXULY).setValue(sdfDate.format(modelKQXLDonViChuyen.getNgayXuLy()));
					item.getItemProperty(KETQUAXULY).setValue(svDanhMuc.getDanhMuc(DanhMucTypeEnum.huongxuly.getName(),modelKQXLDonViChuyen.getMaHuongXuLy()).getName());
					break;
				case chuagiaiquyet:
					item.getItemProperty(NGAYTHULY).setValue(sdfDate.format(model.getNgayThuLy()));
					item.getItemProperty(HANGIAIQUYET).setValue(sdfDate.format(model.getNgayHanGiaiQuyet()));
					break;
				case dagiaiquyet:
					QuyetDinhGiaiQuyetBean modelQDGQ = svQuaTrinh.getQuyetDinhGiaiQuyet(model.getMaDonThu());

					item.getItemProperty(NGAYGIAIQUYET).setValue(sdfDate.format(model.getNgayHoanThanhGiaiQuyet()));
					item.getItemProperty(KETQUAGIAIQUYET).setValue(svDanhMuc.getDanhMuc(DanhMucTypeEnum.loaiquyetdinh.getName(),modelQDGQ.getLoaiKetQuaGiaiQuyet()).getName());
					break;
				default:
					break;
				}
			}
		}
		tblDanhSach.setContainerDataSource(container);
	}

	@Override
	public void loadDefaultValue() {
		try {
			DonThuModule.returnComboboxLoaiDonThu(cmbLoaiDonThu, true);	

			List<Integer> listOrgChuyen = svQuaTrinh.getOrgDaChuyenList((int)SessionUtil.getOrgId());

			for(Integer org: listOrgChuyen)
			{
				cmbDonViChuyen.addItem(org);
				cmbDonViChuyen.setItemCaption(org, OrganizationLocalServiceUtil.getOrganization(org).getName());
			}			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean validateFilter() {
		return true;
	}
}
