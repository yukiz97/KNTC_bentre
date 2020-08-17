package ngn.kntc.page.donthu.create.sublayout;

import java.sql.SQLException;
import java.util.Date;

import ngn.kntc.beans.DoiTuongBiKNTCBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.enums.LoaiNguoiBiKNTCEnum;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.windows.searchdonthu.WindowSearchDoiTuongBiKNTC;

import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.jarektoro.responsivelayout.ResponsiveRow.NotResponsiveColumnException;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

public class NguoiBiKNTCLayout extends Panel{
	private ResponsiveLayout rslMainLayout = new ResponsiveLayout();

	private ComboBox cmbLoaiNguoiKTNC = new ComboBox();
	private Button btnSearchNguoiBiKNTC = new Button("Tìm kiếm thông tin",FontAwesome.SEARCH);
	private Button btnHuyVaNhapMoi = new Button("Hủy và nhập mới",FontAwesome.REMOVE);

	private TextField txtTenCoQuan = new TextField();

	private TextField txtNoiCongTac = new TextField();
	private TextField txtChucVu = new TextField();

	private TextField txtHoTen = new TextField();
	private ComboBox cmbGioiTinh = new ComboBox();

	private TextField txtSoDinhDanh = new TextField();
	private DateField dfNgayCap = new DateField();
	private TextField txtNoiCap = new TextField();

	private ComboBox cmbTinhThanh = new ComboBox();
	private ComboBox cmbQuanHuyen = new ComboBox();
	private ComboBox cmbPhuongXa = new ComboBox();
	private TextField txtDiaChi = new TextField();
	private TextField txtDisplayDiaChi = new TextField();

	private ComboBox cmbQuocTich = new ComboBox();
	private ComboBox cmbDanToc = new ComboBox();

	private ResponsiveRow rowLoaiNguoi = buildRow1();
	private ResponsiveRow rowChucVu = buildRow3();
	private ResponsiveRow rowTenCoQuan = buildRowCoQuan();
	private ResponsiveRow rowHoTen = buildRow4();
	private ResponsiveRow rowSoDinhDanh = buildRow5();
	private ResponsiveRow rowQuocTich = buildRow7();

	private DanhMucServiceUtil svDanhMuc = new DanhMucServiceUtil();
	private DonThuServiceUtil svDonThu = new DonThuServiceUtil();

	public NguoiBiKNTCLayout() {
		buildLayout();
		configComponent();
		try {
			loadDefaultData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void loadDefaultData() throws SQLException {
		/* Chủ thể đơn thư */
		for(LoaiNguoiBiKNTCEnum e: LoaiNguoiBiKNTCEnum.values())
		{
			cmbLoaiNguoiKTNC.addItem(e.getType());
			cmbLoaiNguoiKTNC.setItemCaption(e.getType(), e.getName());
		}
		cmbLoaiNguoiKTNC.select(1);

		/* Giới tính */
		cmbGioiTinh.addItem(1);
		cmbGioiTinh.setItemCaption(1, "Nam");
		cmbGioiTinh.addItem(2);
		cmbGioiTinh.setItemCaption(2, "Nữ");
		cmbGioiTinh.select(1);

		/* Ngày cấp */
		//dfNgayCap.setValue(new Date());

		/* Tỉnh thành */
		svDanhMuc.setValueDefaultComboboxDiaGioi(cmbTinhThanh,cmbQuanHuyen,cmbPhuongXa,"811");

		/* Quốc tịch, dân tộc */
		svDanhMuc.setValueDefaultForComboboxDanhMuc(cmbQuocTich, DanhMucTypeEnum.quoctich.getName(), DanhMucTypeEnum.quoctich.getIdType());
		cmbQuocTich.select("VN");
		svDanhMuc.setValueDefaultForComboboxDanhMuc(cmbDanToc, DanhMucTypeEnum.dantoc.getName(), DanhMucTypeEnum.dantoc.getIdType());
		cmbDanToc.select(1);
	}

	private void buildLayout() {
		rslMainLayout.addRow(rowLoaiNguoi);
		rslMainLayout.addRow(rowTenCoQuan);
		rslMainLayout.addRow(rowHoTen);
		rslMainLayout.addRow(rowChucVu);
		rslMainLayout.addRow(rowSoDinhDanh);
		rslMainLayout.addRow(buildRow6());
		rslMainLayout.addRow(rowQuocTich);
		rslMainLayout.addRow(buildRow8());

		rowTenCoQuan.setVisible(false);

		rslMainLayout.setWidth("100%");

		this.setContent(rslMainLayout);
		this.addStyleName("pnl-layout-thongtin");
	}

	private void configComponent() {	
		txtHoTen.addFocusListener(new FocusListener() {

			@Override
			public void focus(FocusEvent event) {
				btnSearchNguoiBiKNTC.setClickShortcut(KeyCode.ENTER);
			}
		});

		txtHoTen.addBlurListener(new BlurListener() {

			@Override
			public void blur(BlurEvent event) {
				btnSearchNguoiBiKNTC.removeClickShortcut();
			}
		});
		
		txtTenCoQuan.addFocusListener(new FocusListener() {

			@Override
			public void focus(FocusEvent event) {
				btnSearchNguoiBiKNTC.setClickShortcut(KeyCode.ENTER);
			}
		});

		txtTenCoQuan.addBlurListener(new BlurListener() {

			@Override
			public void blur(BlurEvent event) {
				btnSearchNguoiBiKNTC.removeClickShortcut();
			}
		});

		btnSearchNguoiBiKNTC.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					int loaiDoiTuong = (int)cmbLoaiNguoiKTNC.getValue();
					WindowSearchDoiTuongBiKNTC wdSearch = new WindowSearchDoiTuongBiKNTC(loaiDoiTuong);
					UI.getCurrent().addWindow(wdSearch);
					wdSearch.loadData(loaiDoiTuong==1?txtHoTen.getValue().trim():txtTenCoQuan.getValue().trim());
					wdSearch.getTxtSearch().setValue(loaiDoiTuong==1?txtHoTen.getValue().trim():txtTenCoQuan.getValue().trim());
					wdSearch.getBtnOk().addClickListener(new ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
							try {
								DoiTuongBiKNTCBean model  = svDonThu.getDoiTuongBiKNTC(wdSearch.getIdDoiTuong());
								setFieldsBiKNTC(model);
								wdSearch.close();
								btnHuyVaNhapMoi.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							} 
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		cmbLoaiNguoiKTNC.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				btnHuyVaNhapMoi.setVisible(false);
				setFieldsBiKNTC(null);
				switch ((int)cmbLoaiNguoiKTNC.getValue()) {
				case 1:
					try {
						rowHoTen.setVisible(true);
						rowChucVu.setVisible(true);
						rowSoDinhDanh.setVisible(true);
						rowQuocTich.getColumn(1).setVisible(true);
						rowQuocTich.getColumn(2).setVisible(true);
						rowTenCoQuan.setVisible(false);
					} catch (NotResponsiveColumnException e) {
						e.printStackTrace();
					}
					break;
				case 2:
					try {
						rowHoTen.setVisible(false);
						rowChucVu.setVisible(false);
						rowSoDinhDanh.setVisible(false);
						rowQuocTich.getColumn(1).setVisible(false);
						rowQuocTich.getColumn(2).setVisible(false);
						rowTenCoQuan.setVisible(true);
					} catch (NotResponsiveColumnException e) {
						e.printStackTrace();
					}
					break;
				}
			}
		});
		cmbTinhThanh.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				try {
					txtDisplayDiaChi.setValue(DonThuModule.returnDiaChiChiTiet(txtDiaChi.getValue(), (String)cmbPhuongXa.getValue(), (String)cmbPhuongXa.getValue(), (String)cmbPhuongXa.getValue()));
				} catch (ReadOnlyException | SQLException e) {
					e.printStackTrace();
				}
			}
		});
		cmbQuanHuyen.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				try {
					txtDisplayDiaChi.setValue(DonThuModule.returnDiaChiChiTiet(txtDiaChi.getValue(), (String)cmbPhuongXa.getValue(), (String)cmbPhuongXa.getValue(), (String)cmbPhuongXa.getValue()));
				} catch (ReadOnlyException | SQLException e) {
					e.printStackTrace();
				}
			}
		});
		cmbPhuongXa.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				try {
					txtDisplayDiaChi.setValue(DonThuModule.returnDiaChiChiTiet(txtDiaChi.getValue(), (String)cmbTinhThanh.getValue(), (String)cmbQuanHuyen.getValue(), (String)cmbPhuongXa.getValue()));
				} catch (ReadOnlyException | SQLException e) {
					e.printStackTrace();
				}
			}
		});

		txtDiaChi.addTextChangeListener(new TextChangeListener() {

			@Override
			public void textChange(TextChangeEvent event) {
				try {
					txtDisplayDiaChi.setValue(DonThuModule.returnDiaChiChiTiet(event.getText(), (String)cmbTinhThanh.getValue(), (String)cmbQuanHuyen.getValue(), (String)cmbPhuongXa.getValue()));
				} catch (ReadOnlyException | SQLException e) {
					e.printStackTrace();
				}				
			}
		});

		btnHuyVaNhapMoi.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				txtHoTen.setId("-1");
				btnHuyVaNhapMoi.setVisible(false);
				setFieldsBiKNTC(null);
			}
		});
	}

	private ResponsiveRow buildRow1()
	{
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Loại bị khiếu tố", cmbLoaiNguoiKTNC,"150px")).withDisplayRules(12, 12, 6, 6);
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("", btnSearchNguoiBiKNTC,"10px")).withDisplayRules(12, 12, 6, 6);

		cmbLoaiNguoiKTNC.setWidth("100%");
		cmbLoaiNguoiKTNC.setTextInputAllowed(false);
		cmbLoaiNguoiKTNC.setNullSelectionAllowed(false);

		btnSearchNguoiBiKNTC.addStyleName("btn-donthu-timkiem-doituong");

		return row;
	}

	private ResponsiveRow buildRowCoQuan()
	{
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Tên cơ quan"+DonThuModule.requiredMark, txtTenCoQuan,"150px")).withDisplayRules(12, 12, 6, 6);
		txtTenCoQuan.setWidth("100%");
		txtTenCoQuan.setId("-1");
		txtTenCoQuan.addStyleName("textfield-donthu-search");

		return row;
	}

	private ResponsiveRow buildRow3()
	{
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Nơi công tác", txtNoiCongTac,"150px")).withDisplayRules(12, 12, 6, 6);
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Chức vụ", txtChucVu,"100px")).withDisplayRules(12, 12, 6, 6);
		txtNoiCongTac.setWidth("100%");
		txtChucVu.setWidth("100%");

		return row;
	}

	private ResponsiveRow buildRow4()
	{
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Họ và tên"+DonThuModule.requiredMark, txtHoTen,"150px")).withDisplayRules(12, 12, 6, 6);
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Giới tính", cmbGioiTinh,"100px")).withDisplayRules(12, 12, 6, 6);
		txtHoTen.setWidth("100%");
		txtHoTen.setId("-1");
		txtHoTen.addStyleName("textfield-donthu-search");

		cmbGioiTinh.setWidth("100%");
		cmbGioiTinh.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
		cmbGioiTinh.setTextInputAllowed(false);
		cmbGioiTinh.setNullSelectionAllowed(false);

		return row;
	}

	private ResponsiveRow buildRow5()
	{
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Số CMND/ căn cước", txtSoDinhDanh,"150px")).withDisplayRules(12, 12, 6, 6);
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Ngày cấp", dfNgayCap,"100px")).withDisplayRules(12, 6, 3, 3);
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Nơi cấp", txtNoiCap,"100px")).withDisplayRules(12, 6, 3, 3);
		txtSoDinhDanh.setWidth("100%");

		dfNgayCap.setWidth("100%");
		dfNgayCap.setDateFormat("dd/MM/yyyy");

		txtNoiCap.setWidth("100%");

		return row;
	}

	private ResponsiveRow buildRow6()
	{
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Địa chỉ chi tiết", txtDiaChi,"150px")).withDisplayRules(12, 12, 6, 3);
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Tỉnh / Thành", cmbTinhThanh,"100px")).withDisplayRules(12, 12, 6, 3);
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Quận / Huyện", cmbQuanHuyen,"100px")).withDisplayRules(12, 6, 3, 3);
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Phường / Xã", cmbPhuongXa,"100px")).withDisplayRules(12, 6, 3, 3);
		txtDiaChi.setWidth("100%");
		cmbTinhThanh.setWidth("100%");
		cmbQuanHuyen.setWidth("100%");
		cmbPhuongXa.setWidth("100%");

		return row;
	}

	private ResponsiveRow buildRow7()
	{
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("", txtDisplayDiaChi,"150px")).withDisplayRules(12, 12, 6, 6);
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Quốc tịch", cmbQuocTich,"100px")).withDisplayRules(12, 12, 6, 3);
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Dân tộc", cmbDanToc,"100px")).withDisplayRules(12, 12, 6, 3);
		txtDisplayDiaChi.setWidth("100%");
		txtDisplayDiaChi.setEnabled(false);

		cmbQuocTich.setWidth("100%");
		cmbDanToc.setWidth("100%");

		return row;
	}

	private ResponsiveRow buildRow8()
	{
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("", btnHuyVaNhapMoi,"150px")).withDisplayRules(12, 12, 6, 6);
		btnHuyVaNhapMoi.setVisible(false);

		return row;
	}

	public DoiTuongBiKNTCBean getValueNguoiBiKNTC()
	{
		DoiTuongBiKNTCBean model = new DoiTuongBiKNTCBean();
		String maTinh = cmbTinhThanh.getValue()!=null ? (String)cmbTinhThanh.getValue() : null;
		String maHuyen = cmbQuanHuyen.getValue()!=null ?(String)cmbQuanHuyen.getValue() : null;
		String maXa = cmbPhuongXa.getValue()!=null ? (String)cmbPhuongXa.getValue() : null;
		String diaChi = txtDiaChi.isEmpty() ? null : txtDiaChi.getValue().trim();

		if((int)cmbLoaiNguoiKTNC.getValue()==1)
		{
			String hoTen = txtHoTen.isEmpty() ? null : txtHoTen.getValue().trim();
			String dinhDanh = txtSoDinhDanh.isEmpty() ? null : txtSoDinhDanh.getValue().trim();
			Date ngayCap = dfNgayCap.isEmpty() ? null : dfNgayCap.getValue();
			String noiCap = txtNoiCap.isEmpty() ? null : txtNoiCap.getValue().trim();
			int gioiTinh = (int)cmbGioiTinh.getValue();
			String maQuocTich = cmbQuocTich.getValue()!=null?(String)cmbQuocTich.getValue():null;
			int maDanToc = cmbDanToc.getValue()!=null?(int)cmbDanToc.getValue():0;
			String chucVu = txtChucVu.isEmpty() ? null : txtChucVu.getValue().trim();
			String noiCongTac = txtNoiCongTac.isEmpty() ? null : txtNoiCongTac.getValue().trim();

			if(txtHoTen.getId()!="-1")
				model.setMaDoiTuong(Integer.parseInt(txtHoTen.getId()));
			model.setHoTen(hoTen);
			model.setSoDinhDanhCaNhan(dinhDanh);
			model.setNgayCapSoDinhDanh(ngayCap);
			model.setNoiCapSoDinhDanh(noiCap);
			model.setChucVu(chucVu);
			model.setNoiCongTac(noiCongTac);
			model.setGioiTinh(gioiTinh);
			model.setMaQuocTich(maQuocTich);
			model.setMaDanToc(maDanToc);
			model.setMaTinh(maTinh);
			model.setMaHuyen(maHuyen);
			model.setMaXa(maXa);
			model.setDiaChiChiTiet(diaChi);
		}
		else
		{
			String tenCoQuan = txtTenCoQuan.isEmpty() ? null : txtTenCoQuan.getValue().trim();

			if(txtTenCoQuan.getId()!="-1")
				model.setMaDoiTuong(Integer.parseInt(txtTenCoQuan.getId()));
			model.setTenCoQuanToChuc(tenCoQuan);
			model.setMaTinhCoQuan(maTinh);
			model.setMaHuyenCoQuan(maHuyen);
			model.setMaXaCoQuan(maXa);
			model.setDiaChiChiTietCoQuan(diaChi);
		}
		return model;
	}

	public void setFieldsBiKNTC(DoiTuongBiKNTCBean model)
	{
		txtTenCoQuan.setId("-1");
		txtTenCoQuan.setValue("");
		txtHoTen.setId("-1");
		txtHoTen.setValue("");
		txtNoiCongTac.setValue("");
		txtChucVu.setValue("");
		cmbGioiTinh.select(1);
		txtSoDinhDanh.setValue("");
		dfNgayCap.setValue(null);
		txtNoiCap.setValue("");
		txtDiaChi.setValue("");
		cmbTinhThanh.select(null);
		cmbQuocTich.select("VN");
		cmbDanToc.select(1);
		if(model !=null)
		{
			if((int)cmbLoaiNguoiKTNC.getValue()==1)
			{
				txtHoTen.setId(model.getMaDoiTuong()+"");
				txtHoTen.setValue(model.getHoTen());
				cmbGioiTinh.select(model.getGioiTinh());
				txtSoDinhDanh.setValue(model.getSoDinhDanhCaNhan()!=null?model.getSoDinhDanhCaNhan():"");
				txtNoiCongTac.setValue(model.getNoiCongTac()!=null?model.getNoiCongTac():"");
				txtChucVu.setValue(model.getChucVu()!=null?model.getChucVu():"");
				dfNgayCap.setValue(model.getNgayCapSoDinhDanh()!=null?model.getNgayCapSoDinhDanh():null);
				txtNoiCap.setValue(model.getNoiCapSoDinhDanh()!=null?model.getNoiCapSoDinhDanh():"");
				txtDiaChi.setValue(model.getDiaChiChiTiet()!=null?model.getDiaChiChiTiet():"");
				cmbTinhThanh.select(model.getMaTinh()!=null?model.getMaTinh():null);
				cmbQuanHuyen.select(model.getMaHuyen()!=null?model.getMaHuyen():null);
				cmbPhuongXa.select(model.getMaXa()!=null?model.getMaXa():null);
				cmbQuocTich.select(model.getMaQuocTich()!=null?model.getMaQuocTich():null);
				cmbDanToc.select(model.getMaDanToc()!=0?model.getMaDanToc():null);
			}
			else
			{
				txtTenCoQuan.setId(model.getMaDoiTuong()+"");
				txtTenCoQuan.setValue(model.getTenCoQuanToChuc());
				txtDiaChi.setValue(model.getDiaChiChiTietCoQuan()!=null?model.getDiaChiChiTietCoQuan():null);
				cmbTinhThanh.select(model.getMaTinhCoQuan()!=null?model.getMaTinhCoQuan():null);
				cmbQuanHuyen.select(model.getMaHuyenCoQuan()!=null?model.getMaHuyenCoQuan():null);
				cmbPhuongXa.select(model.getMaXaCoQuan()!=null?model.getMaXaCoQuan():null);
			}
		}
	}

	public boolean validateForm()
	{
		int type = (int)cmbLoaiNguoiKTNC.getValue();
		switch (type) {
		case 1:
			if(txtHoTen.isEmpty())
			{
				Notification.show("Vui lòng nhập vào họ tên người bị khiếu tố",Type.WARNING_MESSAGE);
				txtHoTen.focus();
				return false;
			}
			break;
		case 2:
			if(txtTenCoQuan.isEmpty())
			{
				Notification.show("Vui lòng nhập vào tên cơ quan bị khiếu tố",Type.WARNING_MESSAGE);
				txtTenCoQuan.focus();
				return false;
			}
			break;
		}
		return true;
	}

	public ComboBox getCmbLoaiNguoiKTNC() {
		return cmbLoaiNguoiKTNC;
	}
	public void setCmbLoaiNguoiKTNC(ComboBox cmbLoaiNguoiKTNC) {
		this.cmbLoaiNguoiKTNC = cmbLoaiNguoiKTNC;
	}
	public Button getBtnSearchNguoiBiKNTC() {
		return btnSearchNguoiBiKNTC;
	}
	public void setBtnSearchNguoiBiKNTC(Button btnSearchNguoiBiKNTC) {
		this.btnSearchNguoiBiKNTC = btnSearchNguoiBiKNTC;
	}
}
