package ngn.kntc.windows.searchdonthu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ngn.kntc.beans.CheckBoxSearchDonThuBean;
import ngn.kntc.beans.SoTiepCongDanBean;
import ngn.kntc.enums.LoaiDonThuEnum;
import ngn.kntc.modules.ControlPagination;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.LiferayServiceUtil;
import ngn.kntc.utils.TiepCongDanServiceUtil;

import com.jensjansson.pagedtable.PagedTable;
import com.liferay.portal.NoSuchUserException;
import com.liferay.portal.model.User;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class WindowSearchTiepCongDan extends Window{
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	VerticalLayout vMain = new VerticalLayout();

	PagedTable tblDanhSach = new PagedTable();
	ControlPagination control = new ControlPagination(tblDanhSach);
	IndexedContainer container = new IndexedContainer();

	//control
	HorizontalLayout hButton = new HorizontalLayout();
	Button btnOk = new Button("Xác nhận",FontAwesome.SAVE);
	Button btnCancel = new Button("Hủy",FontAwesome.CLOSE);

	final String STT = "STT";
	final String HOTEN = "Họ tên";
	final String NOIDUNGTIEP = "Nội dung tiếp";
	final String KETQUATIEP = "Kết quả tiếp";
	final String LOAILINHVUC = "Loại lĩnh vực";
	final String NGUOINHAPCOQUANNHAP = "Người nhập / Cơ quan nhập";
	final String NGAYNHAP = "Ngày tiếp";
	final String LANTRUNG = "Lần trùng";
	final String GANVUVIEC = "Gắn vụ việc";
	final String LAYTHONGTINDON = "Lấy thông tin";

	List<CheckBoxSearchDonThuBean> listCheckBox = new ArrayList<CheckBoxSearchDonThuBean>();

	SoTiepCongDanBean tiepCongDan = null;
	boolean ganVuViec = false;
	boolean layThongTin = false;
	boolean trungDon = false;

	DonThuServiceUtil svDonThu = new DonThuServiceUtil();
	TiepCongDanServiceUtil svTCD = new TiepCongDanServiceUtil();

	public WindowSearchTiepCongDan() {
		buildLayout();
		configComponent();
	}

	private void buildLayout() {
		vMain.addComponent(control);
		vMain.addComponent(tblDanhSach);
		vMain.addComponent(hButton);

		//vMain.setExpandRatio(tblDanhSach, 1.0f);
		vMain.setComponentAlignment(hButton, Alignment.MIDDLE_RIGHT);
		vMain.setSpacing(true);
		vMain.setMargin(true);
		vMain.setWidth("100%");

		this.setContent(vMain);
		this.setWidth("100%");
		this.setHeight("99%");
		this.setCaption("Tìm kiếm thông tin tiếp công dân không đơn");
		this.setModal(true);

		buildTable();
		buildControl();
	}

	private void configComponent() {
		btnCancel.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
	}

	private void buildTable() {
		tblDanhSach.setSizeFull();
		tblDanhSach.setPageLength(5);
		tblDanhSach.setColumnAlignment(STT, Align.CENTER);
		tblDanhSach.setColumnAlignment(GANVUVIEC, Align.CENTER);
		tblDanhSach.setColumnAlignment(LAYTHONGTINDON, Align.CENTER);
		tblDanhSach.setColumnAlignment(NGAYNHAP, Align.CENTER);
		tblDanhSach.setColumnAlignment(LANTRUNG, Align.CENTER);
		
		tblDanhSach.setColumnWidth(HOTEN, 180);
		tblDanhSach.setColumnWidth(GANVUVIEC, 75);
		tblDanhSach.setColumnWidth(LAYTHONGTINDON, 80);
		tblDanhSach.setColumnWidth(LANTRUNG, 60);
		tblDanhSach.setColumnWidth(NGUOINHAPCOQUANNHAP, 180);

		tblDanhSach.addStyleName("table-vanban");

		control.getItemsPerPageLabel().setValue("Hiển thị");
		control.getBtnFirst().setCaption("Trang đầu");
		control.getBtnLast().setCaption("Trang cuối");
		control.getBtnNext().setCaption("Trang kế");
		control.getBtnPrevious().setCaption("Trang trước");
		control.getPageLabel().setValue("Hiện tại: ");

		container.addContainerProperty(STT, Integer.class, null);
		container.addContainerProperty(HOTEN, Label.class, null);
		container.addContainerProperty(NOIDUNGTIEP, Label.class, null);
		container.addContainerProperty(KETQUATIEP, Label.class, null);
		container.addContainerProperty(LOAILINHVUC, String.class, null);
		container.addContainerProperty(NGUOINHAPCOQUANNHAP, Label.class, null);
		container.addContainerProperty(NGAYNHAP, String.class, null);
		container.addContainerProperty(LANTRUNG, Integer.class, null);
		container.addContainerProperty(GANVUVIEC, CheckBox.class, null);
		container.addContainerProperty(LAYTHONGTINDON, CheckBox.class, null);
	}

	private void buildControl()
	{
		hButton.addComponent(btnOk);
		hButton.addComponent(btnCancel);

		btnOk.addStyleName(ValoTheme.BUTTON_PRIMARY);
		btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);

		hButton.setSpacing(true);
	}

	@SuppressWarnings("unchecked")
	public void loadData(String[] arrChuTheDonThu) throws Exception
	{
		container.removeAllItems();
		List<SoTiepCongDanBean> listDonThu = svTCD.searchThongTinTiepCongDan(arrChuTheDonThu);
		int i = 0;
		System.out.println(listDonThu.size()+"---------");
		for(SoTiepCongDanBean model : listDonThu)
		{
			CheckBoxSearchDonThuBean checkboxGroup = new CheckBoxSearchDonThuBean();
			CheckBox cbGanVuViec = new CheckBox();
			CheckBox cbLayThongTin = new CheckBox();
			checkboxGroup.setCbGanVuViec(cbGanVuViec);
			checkboxGroup.setCbLayThongTin(cbLayThongTin);
			listCheckBox.add(checkboxGroup);

			String loaiDonThu = "";
			for(LoaiDonThuEnum e : LoaiDonThuEnum.values())
			{
				if(e.getType()==model.getLoaiLinhVuc())
					loaiDonThu = e.getName();
			}
			
			String strNguoiNhap = "";
			String strCoQuanNhap = "";
			try{
				User user = UserLocalServiceUtil.getUser(model.getUserTCD());
				strNguoiNhap = user.getFirstName();
				strCoQuanNhap = LiferayServiceUtil.getUserOrgName(user.getUserId());
			}catch(NoSuchUserException e){
				e.printStackTrace();
			}

			Item item = container.addItem(++i);
			item.getItemProperty(STT).setValue(i);
			item.getItemProperty(HOTEN).setValue(new Label(svTCD.returnTenNguoiDaiDienTCD(model),ContentMode.HTML));
			item.getItemProperty(NOIDUNGTIEP).setValue(new Label(model.getNoiDungTiepCongDan(),ContentMode.HTML));
			item.getItemProperty(KETQUATIEP).setValue(new Label(model.getKetQuaTiepCongDan(),ContentMode.HTML));
			item.getItemProperty(LOAILINHVUC).setValue(loaiDonThu);
			item.getItemProperty(NGUOINHAPCOQUANNHAP).setValue(new Label(strNguoiNhap+"<br/> "+strCoQuanNhap,ContentMode.HTML));
			item.getItemProperty(NGAYNHAP).setValue(sdf.format(model.getNgayTiepCongDan()));
			item.getItemProperty(LANTRUNG).setValue(svTCD.countSoLanTrungVuViecKhongDon(model.getMaSoTiepCongDan()));
			item.getItemProperty(GANVUVIEC).setValue(cbGanVuViec);
			item.getItemProperty(LAYTHONGTINDON).setValue(cbLayThongTin);

			cbGanVuViec.addValueChangeListener(new ValueChangeListener() {

				@Override
				public void valueChange(ValueChangeEvent event) {
					if(cbGanVuViec.getValue())
					{
						tiepCongDan = model;
					}
					else
					{
						tiepCongDan = null;
					}
					ganVuViec = cbGanVuViec.getValue();
					enableCheckBoxList(checkboxGroup);
				}
			});
			cbLayThongTin.addValueChangeListener(new ValueChangeListener() {

				@Override
				public void valueChange(ValueChangeEvent event) {
					if(cbLayThongTin.getValue())
					{
						tiepCongDan = model;
					}
					else
					{
						tiepCongDan = null;
					}
					layThongTin = cbLayThongTin.getValue();
					enableCheckBoxList(checkboxGroup);
				}
			});
		}
		tblDanhSach.setContainerDataSource(container);
	}

	private void enableCheckBoxList(CheckBoxSearchDonThuBean groupCheckBox)
	{
		boolean enable=true;
		if(groupCheckBox.getCbGanVuViec().getValue() || groupCheckBox.getCbLayThongTin().getValue())
			enable = false;

		for(CheckBoxSearchDonThuBean cbGroup : listCheckBox)
		{
			cbGroup.getCbGanVuViec().setEnabled(enable);
			cbGroup.getCbLayThongTin().setEnabled(enable);
		}
		groupCheckBox.getCbGanVuViec().setEnabled(true);
		groupCheckBox.getCbLayThongTin().setEnabled(true);
	}

	public Button getBtnOk() {
		return btnOk;
	}
	public void setBtnOk(Button btnOk) {
		this.btnOk = btnOk;
	}
	public boolean isGanVuViec() {
		return ganVuViec;
	}
	public boolean isLayThongTin() {
		return layThongTin;
	}
	public SoTiepCongDanBean getTiepCongDan() {
		return tiepCongDan;
	}
	public void setTiepCongDan(SoTiepCongDanBean tiepCongDan) {
		this.tiepCongDan = tiepCongDan;
	}
}
