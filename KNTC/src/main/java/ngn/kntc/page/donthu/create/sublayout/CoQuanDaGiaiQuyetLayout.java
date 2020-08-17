package ngn.kntc.page.donthu.create.sublayout;

import java.sql.SQLException;
import java.util.Date;

import ngn.kntc.beans.DonThuBean;
import ngn.kntc.beans.SoTiepCongDanBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.TiepCongDanServiceUtil;
import ngn.kntc.windows.WindowChonCoQuan;

import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

public class CoQuanDaGiaiQuyetLayout extends Panel{
	private ResponsiveLayout rslMainLayout = new ResponsiveLayout();
	private TextField txtTenCoQuan= new TextField();
	private Button btnTimKiemCoQuan = new Button("",FontAwesome.PLUS_CIRCLE);
	private TextField txtSoKyHieu = new TextField();
	private TextField txtLanGiaiQuyet = new TextField();
	private ComboBox cmbLoaiQuyetDinh = new ComboBox();
	private DateField dfNgayBanHanh = new DateField();
	private TextArea txtNoiDungQuyetDinh = new TextArea();

	private DanhMucServiceUtil svDanhMuc = new DanhMucServiceUtil();

	public CoQuanDaGiaiQuyetLayout() {
		buildLayout();
		configComponent();
		try {
			loadDefaultData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void loadDefaultData() throws SQLException {
		/* Ngày ban hành quyết định */
		dfNgayBanHanh.setValue(new Date());

		/* Loại quyết định */
		svDanhMuc.setValueDefaultForComboboxDanhMuc(cmbLoaiQuyetDinh, DanhMucTypeEnum.hinhthucgiaiquyet.getName(), DanhMucTypeEnum.hinhthucgiaiquyet.getIdType());
		cmbLoaiQuyetDinh.select(1);
	}

	private void buildLayout() {
		rslMainLayout.addRow(buildRow1());
		rslMainLayout.addRow(buildRow2());
		rslMainLayout.addRow(buildRow0());
		rslMainLayout.addRow(buildRow3());

		rslMainLayout.setWidth("100%");

		this.setContent(rslMainLayout);
		this.addStyleName("pnl-layout-thongtin");
	}

	private void configComponent() {
		btnTimKiemCoQuan.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				WindowChonCoQuan wdChonCoQuan = new WindowChonCoQuan();
				UI.getCurrent().addWindow(wdChonCoQuan);

				wdChonCoQuan.getBtnOk().addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						String coQuanId = wdChonCoQuan.getCoQuanID();
						try {
							txtTenCoQuan.setValue(DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.coquan.getName(), coQuanId).getName());
							txtTenCoQuan.setId(coQuanId);
						} catch (ReadOnlyException | SQLException e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
	}

	private ResponsiveRow buildRow1()
	{
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Tên cơ quan đã giải quyết"+DonThuModule.requiredMark, txtTenCoQuan,"150px")).withDisplayRules(12, 12, 6, 6);
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("", btnTimKiemCoQuan,"10px")).withDisplayRules(12, 12, 6, 6);
		txtTenCoQuan.setWidth("100%");
		
		txtTenCoQuan.setEnabled(false);
		txtTenCoQuan.setId("-1");

		return row;
	}
	
	private ResponsiveRow buildRow0()
	{
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Lần giải quyết"+DonThuModule.requiredMark, txtLanGiaiQuyet,"150px")).withDisplayRules(12, 12, 6, 6);
		txtLanGiaiQuyet.setWidth("100%");
		
		return row;
	}

	private ResponsiveRow buildRow2()
	{
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Loại văn bản giải quyết"+DonThuModule.requiredMark, cmbLoaiQuyetDinh,"150px")).withDisplayRules(12, 6, 3, 6);
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Số ký hiệu"+DonThuModule.requiredMark, txtSoKyHieu,"100px")).withDisplayRules(12, 6, 3, 3);
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Ngày ban hành"+DonThuModule.requiredMark, dfNgayBanHanh,"100px")).withDisplayRules(12, 6, 3, 3);
		
		txtSoKyHieu.setWidth("100%");
		dfNgayBanHanh.setWidth("100%");
		cmbLoaiQuyetDinh.setWidth("100%");
		cmbLoaiQuyetDinh.setNullSelectionAllowed(false);

		return row;
	}

	private ResponsiveRow buildRow3()
	{
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Kết quả giải quyết", txtNoiDungQuyetDinh,"150px")).withDisplayRules(12, 12, 12, 12);
		txtNoiDungQuyetDinh.setWidth("100%");

		return row;
	}
	
	public void setFieldsCQDGQ(DonThuBean model) throws Exception
	{
		txtTenCoQuan.setValue(DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.coquan.getName(), model.getMaCoQuanDaGiaiQuyet()).getName());
		txtTenCoQuan.setId(model.getMaCoQuanDaGiaiQuyet());
		txtSoKyHieu.setValue(model.getSoKyHieuVanBanGiaiQuyet()!=null?model.getSoKyHieuVanBanGiaiQuyet():"");
		txtLanGiaiQuyet.setValue(String.valueOf(model.getLanGiaiQuyet()));
		dfNgayBanHanh.setValue(model.getNgayBanHanhQDGQ());
		cmbLoaiQuyetDinh.setValue(model.getLoaiQuyetDinhGiaiQuyet());
		txtNoiDungQuyetDinh.setValue(model.getTomTatNoiDungGiaiQuyet()!=null?model.getTomTatNoiDungGiaiQuyet():"");
	}
	
	public void setFieldsCQDGQ(SoTiepCongDanBean model) throws Exception
	{
		txtTenCoQuan.setValue(DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.coquan.getName(), model.getMaCoQuanDaGiaiQuyet()).getName());
		txtTenCoQuan.setId(model.getMaCoQuanDaGiaiQuyet());
		txtSoKyHieu.setValue(model.getSoKyHieuVanBanDen()!=null?model.getSoKyHieuVanBanDen():"");
		txtLanGiaiQuyet.setValue(String.valueOf(model.getLanGiaiQuyet()));
		dfNgayBanHanh.setValue(model.getNgayBanHanhQDGQ());
		cmbLoaiQuyetDinh.setValue(model.getLoaiQuyetDinhGiaiQuyet());
		txtNoiDungQuyetDinh.setValue(model.getTomTatNoiDungGiaiQuyet()!=null?model.getTomTatNoiDungGiaiQuyet():"");
	}
	
	public boolean validateForm()
	{
		if(txtTenCoQuan.getId()=="-1")
		{
			Notification.show("Vui lòng chọn cơ quan đã giải quyết",Type.WARNING_MESSAGE);
			btnTimKiemCoQuan.focus();
			return false;
		}
		try{
			if(!txtLanGiaiQuyet.isEmpty())
				Integer.parseInt(txtLanGiaiQuyet.getValue());
			else
			{
				Notification.show("Vui lòng nhập vào lần giải quyết",Type.WARNING_MESSAGE);
				txtLanGiaiQuyet.focus();
				return false;
			}
		}catch(Exception e){
			Notification.show("Lần giải quyết phải là số nguyên",Type.WARNING_MESSAGE);
			txtLanGiaiQuyet.focus();
			return false;
		}
		if(dfNgayBanHanh.isEmpty())
		{
			Notification.show("Vui lòng chọn ngày ban hành quyết định giải quyết",Type.WARNING_MESSAGE);
			dfNgayBanHanh.focus();
			return false;
		}
		return true;
	}

	public TextField getTxtTenCoQuan() {
		return txtTenCoQuan;
	}
	public void setTxtTenCoQuan(TextField txtTenCoQuan) {
		this.txtTenCoQuan = txtTenCoQuan;
	}
	public TextField getTxtLanGiaiQuyet() {
		return txtLanGiaiQuyet;
	}
	public void setTxtLanGiaiQuyet(TextField txtLanGiaiQuyet) {
		this.txtLanGiaiQuyet = txtLanGiaiQuyet;
	}
	public ComboBox getCmbLoaiQuyetDinh() {
		return cmbLoaiQuyetDinh;
	}
	public void setCmbLoaiQuyetDinh(ComboBox cmbLoaiQuyetDinh) {
		this.cmbLoaiQuyetDinh = cmbLoaiQuyetDinh;
	}
	public DateField getDfNgayBanHanh() {
		return dfNgayBanHanh;
	}
	public TextField getTxtSoKyHieu() {
		return txtSoKyHieu;
	}
	public void setTxtSoKyHieu(TextField txtSoKyHieu) {
		this.txtSoKyHieu = txtSoKyHieu;
	}
	public void setDfNgayBanHanh(DateField dfNgayBanHanh) {
		this.dfNgayBanHanh = dfNgayBanHanh;
	}
	public TextArea getTxtNoiDungQuyetDinh() {
		return txtNoiDungQuyetDinh;
	}
	public void setTxtNoiDungQuyetDinh(TextArea txtNoiDungQuyetDinh) {
		this.txtNoiDungQuyetDinh = txtNoiDungQuyetDinh;
	}
}
