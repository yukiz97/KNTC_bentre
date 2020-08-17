package ngn.kntc.windows.searchdonthu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ngn.kntc.beans.CheckBoxSearchDonThuBean;
import ngn.kntc.beans.DonThuBean;
import ngn.kntc.enums.LoaiDonThuEnum;
import ngn.kntc.modules.ControlPagination;
import ngn.kntc.page.donthu.chitiet.ChiTietDonThuTCDGeneralLayout;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.LiferayServiceUtil;

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
public class WindowSearchDonThu extends Window{
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	VerticalLayout vMain = new VerticalLayout();

	TextArea txtNoiDungDonThu = new TextArea();
	Button btnSearch = new Button("Tìm kiếm",FontAwesome.SEARCH);

	PagedTable tblDanhSach = new PagedTable();
	ControlPagination control = new ControlPagination(tblDanhSach);
	IndexedContainer container = new IndexedContainer();

	//control
	HorizontalLayout hButton = new HorizontalLayout();
	Button btnOk = new Button("Xác nhận",FontAwesome.SAVE);
	Button btnCancel = new Button("Hủy",FontAwesome.CLOSE);

	final String STT = "STT";
	final String HOTEN = "Họ tên";
	final String NOIDUNGDONTHU = "Nội dung đơn thư";
	final String LOAIDONTHU = "Loại đơn thư";
	final String NGUOINHAPCOQUANNHAP = "Người nhập / Cơ quan nhập";
	final String NGAYNHAP = "Ngày nhập";
	final String LANTRUNG = "Lần trùng đơn";
	final String GANVUVIEC = "Gắn vụ việc";
	final String LAYTHONGTINDON = "Lấy thông tin";
	final String CHITIET = "Chi tiết";

	List<CheckBoxSearchDonThuBean> listCheckBox = new ArrayList<CheckBoxSearchDonThuBean>();

	DonThuBean donThu = null;
	boolean ganVuViec = false;
	boolean layThongTin = false;
	boolean trungDon = false;

	DonThuServiceUtil svDonThu = new DonThuServiceUtil();

	public WindowSearchDonThu() {
		buildLayout();
		configComponent();
	}

	private void buildLayout() {
		vMain.addComponent(txtNoiDungDonThu);
		vMain.addComponent(btnSearch);
		vMain.addComponent(control);
		vMain.addComponent(tblDanhSach);
		vMain.addComponent(hButton);

		txtNoiDungDonThu.setWidth("100%");
		txtNoiDungDonThu.setInputPrompt("Nhập vào từ khóa nội dung của đơn thư...");

		//vMain.setExpandRatio(tblDanhSach, 1.0f);
		vMain.setComponentAlignment(btnSearch, Alignment.MIDDLE_RIGHT);
		vMain.setComponentAlignment(hButton, Alignment.MIDDLE_RIGHT);
		vMain.setSpacing(true);
		vMain.setMargin(true);
		vMain.setWidth("100%");

		this.setContent(vMain);
		this.setWidth("100%");
		this.setHeight("98%");
		this.setCaption("Tìm kiếm thông tin đơn thư");
		this.setModal(true);

		buildTable();
		buildControl();
	}

	private void configComponent() {
		btnSearch.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					loadData(null,txtNoiDungDonThu.getValue().trim());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnCancel.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
	}

	private void buildTable() {
		tblDanhSach.setSizeFull();
		tblDanhSach.setPageLength(10);
		tblDanhSach.setColumnAlignment(STT, Align.CENTER);
		tblDanhSach.setColumnAlignment(GANVUVIEC, Align.CENTER);
		tblDanhSach.setColumnAlignment(LAYTHONGTINDON, Align.CENTER);
		tblDanhSach.setColumnAlignment(CHITIET, Align.CENTER);
		tblDanhSach.setColumnAlignment(NGAYNHAP, Align.CENTER);
		tblDanhSach.setColumnAlignment(LANTRUNG, Align.CENTER);

		tblDanhSach.addStyleName("table-vanban");

		control.getItemsPerPageLabel().setValue("Hiển thị");
		control.getBtnFirst().setCaption("Trang đầu");
		control.getBtnLast().setCaption("Trang cuối");
		control.getBtnNext().setCaption("Trang kế");
		control.getBtnPrevious().setCaption("Trang trước");
		control.getPageLabel().setValue("Hiện tại: ");

		container.addContainerProperty(STT, Integer.class, null);
		container.addContainerProperty(HOTEN, Label.class, null);
		container.addContainerProperty(NOIDUNGDONTHU, Label.class, null);
		container.addContainerProperty(LOAIDONTHU, String.class, null);
		container.addContainerProperty(NGUOINHAPCOQUANNHAP, String.class, null);
		container.addContainerProperty(NGAYNHAP, String.class, null);
		container.addContainerProperty(LANTRUNG, Integer.class, null);
		container.addContainerProperty(GANVUVIEC, CheckBox.class, null);
		container.addContainerProperty(LAYTHONGTINDON, CheckBox.class, null);
		container.addContainerProperty(CHITIET, Button.class, null);
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
	public void loadData(String[] arrChuTheDonThu,String noiDungDonThu) throws Exception
	{
		container.removeAllItems();
		List<DonThuBean> listDonThu = svDonThu.searchDonThu(arrChuTheDonThu,noiDungDonThu);
		int i = 0;
		System.out.println(listDonThu.size()+"---------");
		for(DonThuBean model : listDonThu)
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
				if(e.getType()==model.getLoaiDonThu())
					loaiDonThu = e.getName();
			}
			
			String strNguoiNhap = "";
			String strCoQuanNhap = "";
			try{
				User user = UserLocalServiceUtil.getUser(model.getUserNhapDon());
				strNguoiNhap = user.getFirstName();
				strCoQuanNhap = OrganizationLocalServiceUtil.getOrganization(LiferayServiceUtil.getMasterOrgByUser(model.getUserNhapDon())).getName();
			}catch(NoSuchUserException e){
				e.printStackTrace();
			}

			Button btnChiTiet = new Button("",FontAwesome.EYE);

			Item item = container.addItem(++i);
			item.getItemProperty(STT).setValue(i);
			item.getItemProperty(HOTEN).setValue(new Label(DonThuServiceUtil.returnTenNguoiDaiDienDonThu(model),ContentMode.HTML));
			item.getItemProperty(NOIDUNGDONTHU).setValue(new Label(model.getNoiDungDonThu(),ContentMode.HTML));
			item.getItemProperty(LOAIDONTHU).setValue(loaiDonThu);
			item.getItemProperty(NGUOINHAPCOQUANNHAP).setValue(strNguoiNhap+" / "+strCoQuanNhap);
			item.getItemProperty(NGAYNHAP).setValue(sdf.format(model.getNgayNhapDon()));
			item.getItemProperty(LANTRUNG).setValue(svDonThu.countSoLanTrungDon(model.getMaDonThu()));
			item.getItemProperty(GANVUVIEC).setValue(cbGanVuViec);
			item.getItemProperty(LAYTHONGTINDON).setValue(cbLayThongTin);
			item.getItemProperty(CHITIET).setValue(btnChiTiet);

			btnChiTiet.addClickListener(new ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					Window wdChiTiet = new Window();

					UI.getCurrent().addWindow(wdChiTiet);
					try {
						wdChiTiet.setContent(new ChiTietDonThuTCDGeneralLayout(model.getMaDonThu()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					wdChiTiet.setSizeFull();
					wdChiTiet.setModal(true);
					wdChiTiet.setCaption("Thông tin chi tiết đơn thư");
				}
			});

			cbGanVuViec.addValueChangeListener(new ValueChangeListener() {

				@Override
				public void valueChange(ValueChangeEvent event) {
					if(cbGanVuViec.getValue())
					{
						donThu = model;
					}
					else
					{
						donThu = null;
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
						donThu = model;
					}
					else
					{
						donThu = null;
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

	public TextArea getTxtNoiDungDonThu() {
		return txtNoiDungDonThu;
	}
	public void setTxtNoiDungDonThu(TextArea txtNoiDungDonThu) {
		this.txtNoiDungDonThu = txtNoiDungDonThu;
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
	public void setDonThu(DonThuBean donThu) {
		this.donThu = donThu;
	}
	public DonThuBean getDonThu() {
		return donThu;
	}
}
