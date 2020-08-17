package ngn.kntc.page.donthu.create.sublayout;

import java.sql.SQLException;
import java.util.Date;

import ngn.kntc.beans.DoiTuongUyQuyenBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.enums.LoaiNguoiUyQuyenEnum;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.utils.DanhMucServiceUtil;

import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class NguoiUyQuyenLayout extends Panel{
	private ResponsiveLayout rslMainLayout = new ResponsiveLayout();

	private ComboBox cmbLoaiNguoiUyQuyen = new ComboBox();

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

	private DanhMucServiceUtil svDanhMuc = new DanhMucServiceUtil();

	public NguoiUyQuyenLayout() {
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
		for(LoaiNguoiUyQuyenEnum e: LoaiNguoiUyQuyenEnum.values())
		{
			cmbLoaiNguoiUyQuyen.addItem(e.getType());
			cmbLoaiNguoiUyQuyen.setItemCaption(e.getType(), e.getName());
		}
		cmbLoaiNguoiUyQuyen.select(1);

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
		rslMainLayout.addRow(buildRow1());
		rslMainLayout.addRow(buildRow4());
		rslMainLayout.addRow(buildRow5());
		rslMainLayout.addRow(buildRow6());
		rslMainLayout.addRow(buildRow7());


		rslMainLayout.setWidth("100%");

		this.setContent(rslMainLayout);
		this.addStyleName("pnl-layout-thongtin");
	}

	private void configComponent() {
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
	}

	private ResponsiveRow buildRow1()
	{
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Chủ thể đơn thư", cmbLoaiNguoiUyQuyen,"150px")).withDisplayRules(12, 12, 6, 6);
		cmbLoaiNguoiUyQuyen.setWidth("100%");
		cmbLoaiNguoiUyQuyen.setTextInputAllowed(false);
		cmbLoaiNguoiUyQuyen.setNullSelectionAllowed(false);

		return row;
	}

	private ResponsiveRow buildRow4()
	{
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Họ và tên"+DonThuModule.requiredMark, txtHoTen,"150px")).withDisplayRules(12, 12, 6, 6);
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Giới tính", cmbGioiTinh,"100px")).withDisplayRules(12, 12, 6, 6);
		txtHoTen.setWidth("100%");
		txtHoTen.setId("-1");
		
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

	public DoiTuongUyQuyenBean getValueNguoiUyQuyen()
	{
		DoiTuongUyQuyenBean model = new DoiTuongUyQuyenBean();

		String hoTen = txtHoTen.isEmpty() ? null : txtHoTen.getValue().trim();
		String dinhDanh = txtSoDinhDanh.isEmpty() ? null : txtSoDinhDanh.getValue().trim();
		Date ngayCap = dfNgayCap.isEmpty()? null:dfNgayCap.getValue();
		String noiCap = txtNoiCap.isEmpty() ? null : txtNoiCap.getValue().trim();
		int gioiTinh = cmbGioiTinh.getValue()!=null?(int)cmbGioiTinh.getValue():0;
		String maQuocTich = cmbQuocTich.getValue()!=null?(String)cmbQuocTich.getValue():null;
		int maDanToc = cmbDanToc.getValue()!=null?(int)cmbDanToc.getValue():0;
		String maTinh = cmbTinhThanh.getValue()!=null?(String)cmbTinhThanh.getValue():null;
		String maHuyen = cmbQuanHuyen.getValue()!=null?(String)cmbQuanHuyen.getValue():null;
		String maXa = cmbPhuongXa.getValue()!=null?(String)cmbPhuongXa.getValue():null;
		String diaChi = txtDiaChi.isEmpty() ? null : txtDiaChi.getValue().trim();

		if(txtHoTen.getId()!="-1")
			model.setMaNguoiUyQuyen(Integer.parseInt(txtHoTen.getId()));
		model.setHoTen(hoTen);
		model.setSoDinhDanhCaNhan(dinhDanh);
		model.setNgayCapSoDinhDanh(ngayCap);
		model.setNoiCapSoDinhDanh(noiCap);
		model.setGioiTinh(gioiTinh);
		model.setMaQuocTich(maQuocTich);
		model.setMaDanToc(maDanToc);
		model.setMaTinh(maTinh);
		model.setMaHuyen(maHuyen);
		model.setMaXa(maXa);
		model.setDiaChiChiTiet(diaChi);
		
		return model;
	}
	
	public void setFieldsNguoiUyQuyen(DoiTuongUyQuyenBean model)
	{
		txtHoTen.setId("-1");
		txtHoTen.setValue("");
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
			txtHoTen.setId(model.getMaNguoiUyQuyen()+"");
			txtHoTen.setValue(model.getHoTen());
			cmbGioiTinh.select(model.getGioiTinh());
			txtSoDinhDanh.setValue(model.getSoDinhDanhCaNhan()!=null?model.getSoDinhDanhCaNhan():"");
			dfNgayCap.setValue(model.getNgayCapSoDinhDanh()!=null?model.getNgayCapSoDinhDanh():null);
			txtNoiCap.setValue(model.getNoiCapSoDinhDanh()!=null?model.getNoiCapSoDinhDanh():"");
			txtDiaChi.setValue(model.getDiaChiChiTiet()!=null?model.getDiaChiChiTiet():"");
			cmbTinhThanh.select(model.getMaTinh()!=null?model.getMaTinh():null);
			cmbQuanHuyen.select(model.getMaHuyen()!=null?model.getMaHuyen():null);
			cmbPhuongXa.select(model.getMaXa()!=null?model.getMaXa():null);
			cmbQuocTich.select(model.getMaQuocTich()!=null?model.getMaQuocTich():null);
			cmbDanToc.select(model.getMaDanToc()!=0?model.getMaDanToc():null);
		}
	}

	public boolean validateForm()
	{
		if(txtHoTen.isEmpty())
		{
			Notification.show("Vui lòng nhập vào tên người được ủy quyền",Type.WARNING_MESSAGE);
			txtHoTen.focus();
			return false;
		}
		return true;
	}
	
	public ComboBox getCmbLoaiNguoiUyQuyen() {
		return cmbLoaiNguoiUyQuyen;
	}

	public void setCmbLoaiNguoiUyQuyen(ComboBox cmbLoaiNguoiUyQuyen) {
		this.cmbLoaiNguoiUyQuyen = cmbLoaiNguoiUyQuyen;
	}
}
