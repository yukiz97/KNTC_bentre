package ngn.kntc.page.donthu.create.sublayout;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ngn.kntc.beans.DoiTuongDiKNTCBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.enums.LoaiNguoiDiKNTCEnum;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.windows.searchdonthu.WindowSearchDoiTuongDiKNTC;
import ngn.kntc.windows.searchdonthu.WindowSearchTiepCongDan;

import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
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
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class NguoiDiKNTCLayout extends Panel{
	private ResponsiveLayout rslMainLayout = new ResponsiveLayout();

	private ComboBox cmbLoaiNguoiKTNC = new ComboBox();
	private Button btnSearchNguoiDaiDien = new Button("Tìm kiếm thông tin",FontAwesome.SEARCH);
	private CheckBox cbDonNacDanh = new CheckBox();

	private TextField txtSoNguoi = new TextField();
	private HorizontalLayout hNguoiDaiDien = new HorizontalLayout();
	private HorizontalLayout hBtnNguoiDaiDien = new HorizontalLayout();
	private Button btnThemNguoiDaiDien = new Button("Thêm người dại diện",FontAwesome.PLUS_SQUARE);
	private Button btnHuyVaNhapMoi = new Button("Hủy và nhập mới",FontAwesome.REMOVE);

	private TextField txtTenCoQuan = new TextField();
	private TextField txtDiaChiCoQuan = new TextField();

	private TextField txtHoTen = new TextField();
	private ComboBox cmbGioiTinh = new ComboBox();

	private TextField txtSoDinhDanh = new TextField();
	private TextField txtSoDienThoai = new TextField();
	private DateField dfNgayCap = new DateField();
	private TextField txtNoiCap = new TextField();

	private ComboBox cmbTinhThanh = new ComboBox();
	private ComboBox cmbQuanHuyen = new ComboBox();
	private ComboBox cmbPhuongXa = new ComboBox();
	private TextField txtDiaChi = new TextField();
	private TextField txtDisplayDiaChi = new TextField();

	private ComboBox cmbQuocTich = new ComboBox();
	private ComboBox cmbDanToc = new ComboBox();

	private VerticalLayout vLayoutDisplayNguoiDaiDien = new VerticalLayout();

	private Button btnLayThongTinTiepCongDan = new Button("Lấy thông tin tiếp công dân",FontAwesome.SEARCH_PLUS);
	private TextArea txtNoiDungTiep = new TextArea();
	private TextArea txtKetQuaTiep = new TextArea();

	private ResponsiveRow rowNacDanh = buildRow0();
	private ResponsiveRow rowLoaiNguoi = buildRow1();
	private ResponsiveRow rowSoNguoi = buildRow2();
	private ResponsiveRow rowCoQuan = buildRow3();
	private ResponsiveRow rowDisplayNguoiDaiDien = buildRowDisplayNguoiDaiDien();
	private ResponsiveRow rowDisplayDiaChi = buildRow7();
	private ResponsiveRow rowKetQuaTiep = buildRow8();
	private ResponsiveRow rowButtonThem = buildRow9();

	private List<Button> listButtonEdit = new ArrayList<Button>();
	private Map<Integer, DoiTuongDiKNTCBean> listNguoiDaiDien = new HashMap<Integer, DoiTuongDiKNTCBean>();
	private int indexList = 0;

	private DanhMucServiceUtil svDanhMuc = new DanhMucServiceUtil();
	private DonThuServiceUtil svDonThu = new DonThuServiceUtil();

	public NguoiDiKNTCLayout() {
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
		for(LoaiNguoiDiKNTCEnum e: LoaiNguoiDiKNTCEnum.values())
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
		rslMainLayout.addRow(rowNacDanh);
		rslMainLayout.addRow(rowLoaiNguoi);
		rslMainLayout.addRow(rowSoNguoi);
		rslMainLayout.addRow(rowCoQuan);
		rslMainLayout.addRow(buildRow4());
		rslMainLayout.addRow(buildRow5());
		rslMainLayout.addRow(buildRow6());
		rslMainLayout.addRow(rowDisplayDiaChi);
		rslMainLayout.addRow(rowButtonThem);
		rslMainLayout.addRow(rowDisplayNguoiDaiDien);
		rslMainLayout.addRow(rowKetQuaTiep);

		rowSoNguoi.setVisible(false);
		rowCoQuan.setVisible(false);
		rowKetQuaTiep.setVisible(false);

		rslMainLayout.setWidth("100%");

		this.setContent(rslMainLayout);
		this.addStyleName("pnl-layout-thongtin");
	}

	public ResponsiveRow getRowDisplayDiaChi() {
		return rowDisplayDiaChi;
	}

	public void setRowDisplayDiaChi(ResponsiveRow rowDisplayDiaChi) {
		this.rowDisplayDiaChi = rowDisplayDiaChi;
	}

	private void configComponent() {
		
		cbDonNacDanh.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				/*boolean tmp = true;
				if(cbDonNacDanh.getValue())
					tmp = false;
				for (Iterator<Component> it = rslMainLayout.iterator(); it.hasNext();) {
					it.next().setEnabled(tmp);
				}
				rowNacDanh.setEnabled(true);*/
			}
		});
		
		txtHoTen.addFocusListener(new FocusListener() {
			
			@Override
			public void focus(FocusEvent event) {
				btnSearchNguoiDaiDien.setClickShortcut(KeyCode.ENTER);
			}
		});
		
		txtHoTen.addBlurListener(new BlurListener() {
			
			@Override
			public void blur(BlurEvent event) {
				btnSearchNguoiDaiDien.removeClickShortcut();
			}
		});

		btnSearchNguoiDaiDien.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					WindowSearchDoiTuongDiKNTC wdSearch = new WindowSearchDoiTuongDiKNTC();
					UI.getCurrent().addWindow(wdSearch);
					wdSearch.loadData(txtHoTen.getValue().trim());
					wdSearch.getTxtSearch().setValue(txtHoTen.getValue().trim());

					wdSearch.getBtnOk().addClickListener(new ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
							try {
								DoiTuongDiKNTCBean model = svDonThu.getDoiTuongDiKNTC(wdSearch.getIdDoiTuong());
								setFieldsNguoiDaiDien(model);
								btnSearchNguoiDaiDien.setEnabled(false);
								txtHoTen.setId(wdSearch.getIdDoiTuong()+"");
								btnThemNguoiDaiDien.setCaption("Thêm và cập nhật thông tin người đại diện");
								btnHuyVaNhapMoi.setVisible(true);
								setEnablebuttonEdit(false);
								wdSearch.close();
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
				switch ((int)cmbLoaiNguoiKTNC.getValue()) {
				case 1:
					rowSoNguoi.setVisible(false);
					rowCoQuan.setVisible(false);
					hBtnNguoiDaiDien.removeComponent(btnThemNguoiDaiDien);
					rowDisplayNguoiDaiDien.setVisible(false);
					break;
				case 2:
					rowSoNguoi.setVisible(true);
					rowCoQuan.setVisible(false);
					hBtnNguoiDaiDien.addComponentAsFirst(btnThemNguoiDaiDien);
					rowDisplayNguoiDaiDien.setVisible(true);
					break;
				case 3:
					rowSoNguoi.setVisible(false);
					rowCoQuan.setVisible(true);
					hBtnNguoiDaiDien.removeComponent(btnThemNguoiDaiDien);
					rowDisplayNguoiDaiDien.setVisible(false);
					break;
				default:
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

		btnThemNguoiDaiDien.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if(validateDoiTuong())
				{
					String btnIndex = btnThemNguoiDaiDien.getId();
					DoiTuongDiKNTCBean model = getValueNguoiDiKNTC();
					if(btnIndex=="-1")
					{
						boolean flag = true;
						Iterator<Integer> itr = listNguoiDaiDien.keySet().iterator();
						while(itr.hasNext())
						{
							int index = itr.next();
							if(txtHoTen.getId()!="-1" && Integer.parseInt(txtHoTen.getId())==listNguoiDaiDien.get(index).getMaDoiTuong())
							{
								flag = false;
								break;
							}
						}
						if(flag)
						{
							listNguoiDaiDien.put(++indexList, model);
							Notification.show("Đã thêm người đại diện thành công",Type.TRAY_NOTIFICATION);
						}
						else
						{
							Notification.show("Người đại diện đã có trong danh sách",Type.WARNING_MESSAGE);
							return;
						}
					}
					else
					{
						listNguoiDaiDien.put(Integer.parseInt(btnIndex),model);
						Notification.show("Đã chỉnh sửa thông tin người đại diện thành công",Type.TRAY_NOTIFICATION);
						btnThemNguoiDaiDien.setIcon(FontAwesome.PLUS_SQUARE);
						btnThemNguoiDaiDien.setId("-1");
						btnThemNguoiDaiDien.removeStyleName("btn-sua-nguoidaidien");
					}
					btnThemNguoiDaiDien.setCaption("Thêm người đại diện");
					btnHuyVaNhapMoi.setVisible(false);
					btnSearchNguoiDaiDien.setEnabled(true);
					setEnablebuttonEdit(false);
					setFieldsNguoiDaiDien(null);
					try {
						loadDisplayNguoiDaiDien();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});

		btnHuyVaNhapMoi.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				txtHoTen.setId("-1");
				btnThemNguoiDaiDien.setCaption("Thêm người đại diện");
				btnHuyVaNhapMoi.setVisible(false);
				btnSearchNguoiDaiDien.setEnabled(true);
				setEnablebuttonEdit(true);
				setFieldsNguoiDaiDien(null);
			}
		});
	}

	private ResponsiveRow buildRow0()
	{
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Đơn nặc danh", cbDonNacDanh,"150px")).withDisplayRules(12, 12, 6, 6);

		return row;
	}

	private ResponsiveRow buildRow1()
	{
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Chủ thể đơn thư", cmbLoaiNguoiKTNC,"150px")).withDisplayRules(12, 12, 6, 6);
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("", btnSearchNguoiDaiDien,"10px")).withDisplayRules(12, 12, 6, 3);

		cmbLoaiNguoiKTNC.setWidth("100%");
		cmbLoaiNguoiKTNC.setTextInputAllowed(false);
		cmbLoaiNguoiKTNC.setNullSelectionAllowed(false);

		btnSearchNguoiDaiDien.addStyleName("btn-donthu-timkiem-doituong");

		return row;
	}

	private ResponsiveRow buildRow2()
	{
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Số người"+DonThuModule.requiredMark, txtSoNguoi,"150px")).withDisplayRules(12, 12, 6, 6);
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Số người đại diện", hNguoiDaiDien,"150px")).withDisplayRules(12, 12, 6, 2);

		hNguoiDaiDien.setSpacing(true);

		txtSoNguoi.setWidth("100%");
		row.addStyleName("row-border-bottom");

		countNguoiDaiDien(false);

		return row;
	}

	private ResponsiveRow buildRow3()
	{
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Tên cơ quan"+DonThuModule.requiredMark, txtTenCoQuan,"150px")).withDisplayRules(12, 12, 6, 6);
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Địa chỉ cơ quan", txtDiaChiCoQuan,"100px")).withDisplayRules(12, 12, 6, 6);
		txtTenCoQuan.setWidth("100%");
		txtDiaChiCoQuan.setWidth("100%");
		row.addStyleName("row-border-bottom");

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
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Số CMND/ căn cước", txtSoDinhDanh,"150px")).withDisplayRules(12, 12, 6, 3);
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Số điện thoại", txtSoDienThoai,"100px")).withDisplayRules(12, 12, 6, 3);
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Ngày cấp", dfNgayCap,"100px")).withDisplayRules(12, 6, 3, 3);
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Nơi cấp", txtNoiCap,"100px")).withDisplayRules(12, 6, 3, 3);
		txtSoDinhDanh.setWidth("100%");
		txtSoDienThoai.setWidth("100%");

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
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("", btnLayThongTinTiepCongDan,"150px")).withDisplayRules(12, 12, 12, 12);
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Nội dung tiếp"+DonThuModule.requiredMark, txtNoiDungTiep,"150px")).withDisplayRules(12, 12, 12, 12);
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Kết quả tiếp"+DonThuModule.requiredMark, txtKetQuaTiep,"150px")).withDisplayRules(12, 12, 12, 12);

		btnLayThongTinTiepCongDan.addStyleName(ValoTheme.BUTTON_PRIMARY);
		txtNoiDungTiep.setWidth("100%");
		txtKetQuaTiep.setWidth("100%");

		return row;
	}

	private ResponsiveRow buildRowDisplayNguoiDaiDien()
	{
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("", vLayoutDisplayNguoiDaiDien,"150px")).withDisplayRules(12, 12, 12, 12);
		vLayoutDisplayNguoiDaiDien.setWidth("100%");

		row.setVisible(false);

		return row;
	}

	private ResponsiveRow buildRow9()
	{
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("", hBtnNguoiDaiDien,"150px")).withDisplayRules(12, 12, 12, 12);

		hBtnNguoiDaiDien.addComponents(btnHuyVaNhapMoi);
		hBtnNguoiDaiDien.setSpacing(true);

		btnThemNguoiDaiDien.setId("-1");
		btnThemNguoiDaiDien.addStyleName("btn-them-nguoidaidien");

		btnHuyVaNhapMoi.setVisible(false);

		return row;
	}

	private void countNguoiDaiDien(boolean displaycount)
	{
		hNguoiDaiDien.removeAllComponents();
		for(int i = 1;i<=5;i++)
		{
			Label lblTmp = new Label(FontAwesome.USER.getHtml(),ContentMode.HTML);
			if(displaycount==true)
				if(i<=listNguoiDaiDien.size())
					lblTmp.addStyleName("lbl-songuoidaidien");
			hNguoiDaiDien.addComponent(lblTmp);
		}
	}

	@SuppressWarnings("static-access")
	public void loadDisplayNguoiDaiDien() throws SQLException
	{
		vLayoutDisplayNguoiDaiDien.removeAllComponents();
		countNguoiDaiDien(true);
		if(!listNguoiDaiDien.isEmpty())
		{
			rowDisplayNguoiDaiDien.setVisible(true);
			Iterator<Integer> itr = listNguoiDaiDien.keySet().iterator();
			while(itr.hasNext())
			{
				int index = itr.next();
				DoiTuongDiKNTCBean model = listNguoiDaiDien.get(index);
				VerticalLayout vTmp1 = new VerticalLayout();
				Button btnEdit = new Button("Sửa",FontAwesome.EDIT);
				VerticalLayout vTmp2 = new VerticalLayout();
				Button btnDelete = new Button("Xóa",FontAwesome.REMOVE);
				listButtonEdit.add(btnEdit);
				vTmp1.addComponent(btnEdit);
				vTmp2.addComponent(btnDelete);
				vTmp1.setWidth("100%");
				vTmp2.setWidth("100%");
				vTmp1.setComponentAlignment(btnEdit, Alignment.MIDDLE_RIGHT);
				vTmp2.setComponentAlignment(btnDelete, Alignment.MIDDLE_RIGHT);

				btnEdit.setId("edit");
				btnEdit.addStyleName("btn-layout-nguoidaidien");
				btnDelete.addStyleName("btn-layout-nguoidaidien");
				btnEdit.addStyleName("btn-layout-nguoidaidien-edit");
				btnDelete.addStyleName("btn-layout-nguoidaidien-delete");

				VerticalLayout vLayoutNguoiDaiDienTmp = new VerticalLayout();

				ResponsiveRow rowTmp1 = new ResponsiveRow();
				rowTmp1.addColumn().withComponent(new Label("<b style='color: #19619a'>Số định danh: </b>"+(model.getSoDinhDanhCaNhan()!=null?model.getSoDinhDanhCaNhan():""),ContentMode.HTML)).withDisplayRules(12, 12, 6, 4);
				rowTmp1.addColumn().withComponent(new Label("<b style='color: #19619a'>Họ tên: </b>"+model.getHoTen(),ContentMode.HTML)).withDisplayRules(12, 12, 6, 4);
				rowTmp1.addColumn().withComponent(new Label("<b style='color: #19619a'>Giới tính: </b>"+(model.getGioiTinh()==1 ?"Nam":"Nữ"),ContentMode.HTML)).withDisplayRules(12, 12, 6, 2);
				rowTmp1.addColumn().withComponent(vTmp1).withDisplayRules(12, 12, 6, 2);

				ResponsiveRow rowTmp2 = new ResponsiveRow();
				rowTmp2.addColumn().withComponent(new Label("<b style='color: #19619a'>Địa chỉ chi tiết: </b>"+DonThuModule.returnDiaChiChiTiet(model.getDiaChiChiTiet(),model.getMaTinh(),model.getMaHuyen(),model.getMaXa()),ContentMode.HTML)).withDisplayRules(12, 12, 6, 8);
				rowTmp2.addColumn().withComponent(new Label("<b style='color: #19619a'>Dân tộc: </b>"+svDanhMuc.getDanhMuc(DanhMucTypeEnum.dantoc.getName(), model.getMaDanToc()).getName(),ContentMode.HTML)).withDisplayRules(12, 12, 3, 2);
				rowTmp2.addColumn().withComponent(vTmp2).withDisplayRules(12, 12, 3, 2);

				rowTmp1.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
				rowTmp2.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

				vLayoutNguoiDaiDienTmp.addComponent(rowTmp1);
				vLayoutNguoiDaiDienTmp.addComponent(rowTmp2);

				vLayoutNguoiDaiDienTmp.addStyleName("vLayout-single-nguoidaidien");

				vLayoutDisplayNguoiDaiDien.addComponent(vLayoutNguoiDaiDienTmp);

				btnEdit.addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						if(btnEdit.getId()=="edit")
						{
							btnSearchNguoiDaiDien.setEnabled(false);
							btnThemNguoiDaiDien.addStyleName("btn-sua-nguoidaidien");
							btnThemNguoiDaiDien.setIcon(FontAwesome.EDIT);
							btnThemNguoiDaiDien.setCaption("Sửa thông tin");
							btnThemNguoiDaiDien.setId(index+"");
							btnEdit.setId("cancel");
							btnEdit.setCaption("Hủy");
							vLayoutNguoiDaiDienTmp.addStyleName("vLayout-single-nguoidaidien-active");
							setFieldsNguoiDaiDien(model);
							btnDelete.setEnabled(false);
						}
						else
						{
							btnSearchNguoiDaiDien.setEnabled(true);
							btnThemNguoiDaiDien.removeStyleName("btn-sua-nguoidaidien");
							btnThemNguoiDaiDien.setIcon(FontAwesome.PLUS_SQUARE);
							btnThemNguoiDaiDien.setCaption("Thêm người đại diện");
							btnThemNguoiDaiDien.setId("-1");
							btnEdit.setId("edit");
							btnEdit.setCaption("Sửa");
							vLayoutNguoiDaiDienTmp.removeStyleName("vLayout-single-nguoidaidien-active");
							setFieldsNguoiDaiDien(null);
							btnDelete.setEnabled(true);
						}
					}
				});
				btnDelete.addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						listNguoiDaiDien.remove(index);
						vLayoutDisplayNguoiDaiDien.removeComponent(vLayoutNguoiDaiDienTmp);
						countNguoiDaiDien(true);
					}
				});
			}
		}
		else
		{
			rowDisplayNguoiDaiDien.setVisible(false);
		}
	}

	public void setFieldsNguoiDaiDien(DoiTuongDiKNTCBean model)
	{
		if(model==null)
		{
			txtHoTen.setId("-1");
			txtHoTen.setValue("");
			cmbGioiTinh.select(1);
			txtSoDinhDanh.setValue("");
			txtSoDienThoai.setValue("");
			dfNgayCap.setValue(null);
			txtNoiCap.setValue("");
			txtDiaChi.setValue("");
			cmbTinhThanh.select(null);
			cmbQuocTich.select("VN");
			cmbDanToc.select(1);
		}
		else
		{
			if(model.getMaDoiTuong()!=0)
				txtHoTen.setId(model.getMaDoiTuong()+"");
			txtHoTen.setValue(model.getHoTen()!=null?model.getHoTen():"");
			cmbGioiTinh.select(model.getGioiTinh());
			txtSoDinhDanh.setValue(model.getSoDinhDanhCaNhan()!=null?model.getSoDinhDanhCaNhan():"");
			txtSoDienThoai.setValue(model.getSoDienThoai()!=null?model.getSoDienThoai():"");
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

	public DoiTuongDiKNTCBean getValueNguoiDiKNTC()
	{
		DoiTuongDiKNTCBean model = new DoiTuongDiKNTCBean();

		String hoTen = txtHoTen.isEmpty() ? null : txtHoTen.getValue().trim();
		String dinhDanh = txtSoDinhDanh.isEmpty() ? null : txtSoDinhDanh.getValue().trim();
		String soDienThoai = txtSoDienThoai.isEmpty() ? null : txtSoDienThoai.getValue().trim();
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
			model.setMaDoiTuong(Integer.parseInt(txtHoTen.getId()));
		model.setHoTen(hoTen);
		model.setSoDinhDanhCaNhan(dinhDanh);
		model.setSoDienThoai(soDienThoai);
		model.setNgayCapSoDinhDanh(ngayCap);
		model.setNoiCapSoDinhDanh(noiCap);
		model.setGioiTinh(gioiTinh);
		model.setMaQuocTich(maQuocTich);
		model.setMaDanToc(maDanToc);
		model.setMaTinh(maTinh);
		model.setMaHuyen(maHuyen);
		model.setMaXa(maXa);
		model.setDiaChiChiTiet(diaChi);
		model.setNacDanh(cbDonNacDanh.getValue());

		/*if((int)cmbLoaiNguoiKTNC.getValue()==3)
		{
			String tenCoQuan = txtTenCoQuan.isEmpty() ? null : txtTenCoQuan.getValue().trim();
			String diaChiCoQuann = txtDiaChiCoQuan.isEmpty() ? null : txtDiaChiCoQuan.getValue().trim();

			model.set
		}*/

		return model;
	}

	public boolean validateDoiTuong()
	{
		if(txtHoTen.isEmpty() && !cbDonNacDanh.getValue())
		{
			Notification.show("Vui lòng nhập vào họ tên người khiếu tố",Type.WARNING_MESSAGE);
			txtHoTen.focus();
			return false;
		}

		return true;
	}

	public boolean validateForm()
	{
		int type = (int)cmbLoaiNguoiKTNC.getValue();
		switch (type) {
		case 1:
			return validateDoiTuong();
		case 2:
			try{
				if(!txtSoNguoi.isEmpty())
					Integer.parseInt(txtSoNguoi.getValue());
				else
				{
					Notification.show("Vui lòng nhập vào số người đi khiếu tố",Type.WARNING_MESSAGE);
					txtSoNguoi.focus();
					return false;
				}
			}catch(Exception e){
				Notification.show("Số người đi khiếu tố phải là kiểu số nguyên",Type.WARNING_MESSAGE);
				txtSoNguoi.focus();
				return false;
			}
			if(listNguoiDaiDien.isEmpty())
			{
				Notification.show("Đơn thư phải có ít nhất 1 người đại diện",Type.WARNING_MESSAGE);
				btnThemNguoiDaiDien.focus();
				return false;
			}
			break;
		case 3:
			if(txtTenCoQuan.isEmpty())
			{
				Notification.show("Vui lòng nhập tên cơ quan đi khiếu tố",Type.WARNING_MESSAGE);
				txtTenCoQuan.focus();
				return false;
			}
			return validateDoiTuong();
		}
		return true;
	}

	private void setEnablebuttonEdit(boolean val)
	{
		for(Button btnDelete : listButtonEdit)
			btnDelete.setEnabled(val);
	}


	public TextField getTxtHoTen() {
		return txtHoTen;
	}
	public void setTxtHoTen(TextField txtHoTen) {
		this.txtHoTen = txtHoTen;
	}
	public ResponsiveRow getRowNacDanh() {
		return rowNacDanh;
	}
	public void setRowNacDanh(ResponsiveRow rowNacDanh) {
		this.rowNacDanh = rowNacDanh;
	}
	public ComboBox getCmbLoaiNguoiKTNC() {
		return cmbLoaiNguoiKTNC;
	}
	public void setCmbLoaiNguoiKTNC(ComboBox cmbLoaiNguoiKTNC) {
		this.cmbLoaiNguoiKTNC = cmbLoaiNguoiKTNC;
	}
	public TextField getTxtSoNguoi() {
		return txtSoNguoi;
	}
	public void setTxtSoNguoi(TextField txtSoNguoi) {
		this.txtSoNguoi = txtSoNguoi;
	}
	public TextField getTxtTenCoQuan() {
		return txtTenCoQuan;
	}
	public ResponsiveRow getRowButtonThem() {
		return rowButtonThem;
	}
	public void setRowButtonThem(ResponsiveRow rowButtonThem) {
		this.rowButtonThem = rowButtonThem;
	}
	public ComboBox getCmbGioiTinh() {
		return cmbGioiTinh;
	}
	public void setCmbGioiTinh(ComboBox cmbGioiTinh) {
		this.cmbGioiTinh = cmbGioiTinh;
	}
	public TextArea getTxtKetQuaTiep() {
		return txtKetQuaTiep;
	}
	public void setTxtKetQuaTiep(TextArea txtKetQuaTiep) {
		this.txtKetQuaTiep = txtKetQuaTiep;
	}
	public void setTxtTenCoQuan(TextField txtTenCoQuan) {
		this.txtTenCoQuan = txtTenCoQuan;
	}
	public TextField getTxtDiaChiCoQuan() {
		return txtDiaChiCoQuan;
	}
	public void setTxtDiaChiCoQuan(TextField txtDiaChiCoQuan) {
		this.txtDiaChiCoQuan = txtDiaChiCoQuan;
	}
	public CheckBox getCbDonNacDanh() {
		return cbDonNacDanh;
	}
	public void setCbDonNacDanh(CheckBox cbDonNacDanh) {
		this.cbDonNacDanh = cbDonNacDanh;
	}
	public ResponsiveRow getRowKetQuaTiep() {
		return rowKetQuaTiep;
	}
	public void setRowKetQuaTiep(ResponsiveRow rowKetQuaTiep) {
		this.rowKetQuaTiep = rowKetQuaTiep;
	}
	public Map<Integer, DoiTuongDiKNTCBean> getListNguoiDaiDien() {
		return listNguoiDaiDien;
	}
	public void setListNguoiDaiDien(Map<Integer, DoiTuongDiKNTCBean> listNguoiDaiDien) {
		this.listNguoiDaiDien = listNguoiDaiDien;
	}
	public Button getBtnSearchNguoiDaiDien() {
		return btnSearchNguoiDaiDien;
	}
	public void setBtnSearchNguoiDaiDien(Button btnSearchNguoiDaiDien) {
		this.btnSearchNguoiDaiDien = btnSearchNguoiDaiDien;
	}
	public int getIndexList() {
		return indexList;
	}
	public void setIndexList(int indexList) {
		this.indexList = indexList;
	}
	public ResponsiveRow getRowLoaiNguoi() {
		return rowLoaiNguoi;
	}
	public void setRowLoaiNguoi(ResponsiveRow rowLoaiNguoi) {
		this.rowLoaiNguoi = rowLoaiNguoi;
	}
	public ResponsiveRow getRowDisplayNguoiDaiDien() {
		return rowDisplayNguoiDaiDien;
	}
	public void setRowDisplayNguoiDaiDien(ResponsiveRow rowDisplayNguoiDaiDien) {
		this.rowDisplayNguoiDaiDien = rowDisplayNguoiDaiDien;
	}
	public TextArea getTxtNoiDungTiep() {
		return txtNoiDungTiep;
	}
	public void setTxtNoiDungTiep(TextArea txtNoiDungTiep) {
		this.txtNoiDungTiep = txtNoiDungTiep;
	}
	public Button getBtnLayThongTinTiepCongDan() {
		return btnLayThongTinTiepCongDan;
	}
	public void setBtnLayThongTinTiepCongDan(Button btnLayThongTinTiepCongDan) {
		this.btnLayThongTinTiepCongDan = btnLayThongTinTiepCongDan;
	}  
}
