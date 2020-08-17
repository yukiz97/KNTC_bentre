package ngn.kntc.windows;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ngn.kntc.beans.LuanChuyenBean;
import ngn.kntc.beans.QuaTrinhXuLyGiaiQuyetBean;
import ngn.kntc.modules.ControlPagination;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.modules.RemoveUnicodeCharacter;
import ngn.kntc.utils.LiferayServiceUtil;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;
import ngn.kntc.utils.SessionUtil;

import com.jensjansson.pagedtable.PagedTable;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class WindowTrinhLanhDao extends Window{
	VerticalLayout vMain = new VerticalLayout();
	//Tìm kiếm
	HorizontalLayout hSearch = new HorizontalLayout();
	TextField txtSearch = new TextField();
	Button btnSearch = new Button("Tìm kiếm",FontAwesome.SEARCH);
	Button btnRefesh = new Button("Làm mới",FontAwesome.REFRESH);
	//table chọn
	PagedTable tblSelect = new PagedTable();
	ControlPagination control = new ControlPagination(tblSelect);
	IndexedContainer containerLanhDaoList = new IndexedContainer();
	List<Long> listLanhDaoSelected = new ArrayList<Long>();
	List<Long> listLanhDaoInDatabase = new ArrayList<Long>();
	//table đã chọn
	Table tblSelected = new Table();
	IndexedContainer containerLanhDaoListSelected = new IndexedContainer();
	//control
	HorizontalLayout hButton = new HorizontalLayout();
	Button btnOk = new Button("Xác nhận",FontAwesome.SAVE);
	Button btnCancel = new Button("Hủy",FontAwesome.CLOSE);

	private QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh = new QuaTrinhXuLyGiaiQuyetServiceUtil();
	private int idDonThu;

	public WindowTrinhLanhDao(int idDonThu) {
		this.idDonThu = idDonThu;
		try {
			listLanhDaoInDatabase = svQuaTrinh.getLanhDaoLuanChuyen(idDonThu);
			buildLayout();
			configComponent();

			loadLanhDaoSelected();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void buildLayout() {
		vMain.addComponent(hSearch);
		vMain.addComponent(tblSelect);
		vMain.addComponent(control);
		vMain.addComponent(tblSelected);
		vMain.addComponent(hButton);

		buildSearch();
		buildTableSelect();
		buildTableSelected();
		buildControl();

		try {
			loadLanhDaoSelect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		vMain.setMargin(true);
		vMain.setSpacing(true);

		vMain.setComponentAlignment(hButton, Alignment.MIDDLE_RIGHT);

		this.setContent(vMain);
		this.setCaption("    Lựa chọn lãnh đạo để chuyển");
		this.setIcon(FontAwesome.BOOK);
		this.setWidth("700px");
		this.center();
		this.setModal(true);
	}

	private void configComponent() {
		btnOk.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if(!listLanhDaoSelected.isEmpty())
				{
					String noiDung = "";
					for(Long n: listLanhDaoSelected)
					{
						LuanChuyenBean model = new LuanChuyenBean();
						model.setIdDonThu(idDonThu);
						model.setIdUserChuyen(SessionUtil.getUserId());
						model.setIdUserNhan(n);
						model.setPhanCong(true);
						model.setNgayChuyen(new Date());
						
						try {
							noiDung += svQuaTrinh.returnStringQuatrinhTrinhDon(SessionUtil.getUserId(), n)+"<br/>";
							svQuaTrinh.insertLuanChuyen(model);
							Notification.show("Đã trình đơn thư cho lãnh đạo",Type.TRAY_NOTIFICATION);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
					QuaTrinhXuLyGiaiQuyetBean modelQT = new QuaTrinhXuLyGiaiQuyetBean();
					modelQT.setMaDonThu(idDonThu);
					modelQT.setNoiDung(noiDung);
					modelQT.setHeThongTao(true);
					modelQT.setNgayDang(new Date());
					modelQT.setUserNhap(SessionUtil.getUserId());
					
					try {
						int idQuaTrinh = svQuaTrinh.insertQuaTrinhXLGQ(modelQT);
						DonThuModule.insertThongBao(idDonThu, idQuaTrinh);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				close();
			}
		});

		txtSearch.addFocusListener(new FocusListener() {

			@Override
			public void focus(FocusEvent event) {
				btnSearch.setClickShortcut(KeyCode.ENTER);
			}
		});

		txtSearch.addBlurListener(new BlurListener() {

			@Override
			public void blur(BlurEvent event) {
				btnSearch.removeClickShortcut();
			}
		});

		btnSearch.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				doFilter();
			}
		});

		btnRefesh.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				containerLanhDaoList.removeAllContainerFilters();
				tblSelect.setContainerDataSource(containerLanhDaoList);
				tblSelect.setVisibleColumns(new String[]{"STT","Lãnh đạo"});
			}
		});

		btnCancel.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
	}

	private void buildSearch()
	{
		hSearch.addComponent(txtSearch);
		hSearch.addComponent(btnSearch);
		hSearch.addComponent(btnRefesh);

		txtSearch.setInputPrompt("Nhập vào tên lãnh đạo cần tìm....");
		txtSearch.setSizeFull();

		hSearch.setExpandRatio(txtSearch, 1.0f);

		hSearch.setSpacing(true);
		hSearch.setSizeFull();
	}

	private void buildTableSelect()
	{
		containerLanhDaoList.addContainerProperty("STT", Integer.class, null);
		containerLanhDaoList.addContainerProperty("Lãnh đạo", CheckBox.class, null);
		containerLanhDaoList.addContainerProperty("filterView", String.class, "");

		tblSelect.setPageLength(10);
		tblSelect.setContainerDataSource(containerLanhDaoList);
		tblSelect.setSizeFull();
		tblSelect.setImmediate(true);
		tblSelect.setVisibleColumns(new String[]{"STT","Lãnh đạo"});
		
		tblSelect.addStyleName("table-vanban");

		control.getItemsPerPageLabel().setValue("Hiển thị");
		control.getBtnFirst().setCaption("Trang đầu");
		control.getBtnLast().setCaption("Trang cuối");
		control.getBtnNext().setCaption("Trang kế");
		control.getBtnPrevious().setCaption("Trang trước");
		control.getPageLabel().setValue("Hiện tại: ");

		control.getItemsPerPageSelect().setEnabled(false);
	}

	private void buildTableSelected()
	{
		containerLanhDaoListSelected.addContainerProperty("STT", Integer.class, null);
		containerLanhDaoListSelected.addContainerProperty("Lãnh đạo", String.class, null);
		containerLanhDaoListSelected.addContainerProperty("Xóa", Button.class, null);

		tblSelected.setPageLength(8);
		tblSelected.setContainerDataSource(containerLanhDaoListSelected);
		tblSelected.setSizeFull();
		tblSelected.addStyleName("table-vanban");

		tblSelected.setColumnAlignment("STT", Align.CENTER);
		tblSelected.setColumnAlignment("Xóa", Align.CENTER);
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
	private void loadLanhDaoSelect() throws Exception
	{
		List<User> listLanhDao = LiferayServiceUtil.returnListLanhDao();

		int i = 0;
		containerLanhDaoList.removeAllItems();
		for(User model : listLanhDao)
		{
			CheckBox cb = new CheckBox(model.getFirstName());
			cb.setId(model.getUserId()+"");
			if(listLanhDaoInDatabase.contains(Long.parseLong(cb.getId())))
			{
				cb.setValue(true);
				cb.setEnabled(false);
			}
			Item item = containerLanhDaoList.addItem(model.getUserId());
			item.getItemProperty("STT").setValue(++i);
			item.getItemProperty("Lãnh đạo").setValue(cb);
			item.getItemProperty("filterView").setValue(model.getFirstName()+" "+RemoveUnicodeCharacter.removeAccent(model.getFirstName()));

			if(listLanhDaoSelected.contains(Integer.valueOf(cb.getId())))
			{
				cb.setValue(true);
			}

			if(listLanhDaoInDatabase.contains(Integer.valueOf(cb.getId())))
			{
				cb.setEnabled(false);
			}

			cb.addValueChangeListener(new ValueChangeListener() {

				@Override
				public void valueChange(ValueChangeEvent event) {
					center();
					if(cb.getValue())
					{
						if(!listLanhDaoSelected.contains(Long.valueOf(Long.parseLong(cb.getId()))))
						{
							listLanhDaoSelected.add(Long.parseLong(cb.getId()));
						}
					}
					else{
						if(listLanhDaoSelected.contains(Long.valueOf(Long.parseLong(cb.getId()))))
						{
							listLanhDaoSelected.remove(Long.valueOf(Long.parseLong(cb.getId())));
						}
					}
					loadLanhDaoSelected();
				}
			});
		}
		tblSelect.setContainerDataSource(containerLanhDaoList);
		tblSelect.setVisibleColumns(new String[]{"STT","Lãnh đạo"});
	}

	@SuppressWarnings("unchecked")
	private void loadLanhDaoSelected()
	{
		List<Long> listTmp = new ArrayList<Long>();
		listTmp.addAll(listLanhDaoSelected);
		listTmp.addAll(listLanhDaoInDatabase);
		int i = 0;
		containerLanhDaoListSelected.removeAllItems();
		for(Long n : listTmp)
		{
			Button btnDelete = new Button(FontAwesome.TRASH_O);
			btnDelete.addStyleName(ValoTheme.BUTTON_DANGER);
			Item item = containerLanhDaoListSelected.addItem(n);
			item.getItemProperty("STT").setValue(++i);
			try {
				item.getItemProperty("Lãnh đạo").setValue(UserLocalServiceUtil.getUserById(n).getFirstName());
			} catch (ReadOnlyException | PortalException | SystemException e) {
				e.printStackTrace();
			}
			item.getItemProperty("Xóa").setValue(btnDelete);

			if(listLanhDaoInDatabase.contains(Long.valueOf(n)))
			{
				btnDelete.setEnabled(false);
			}

			btnDelete.addClickListener(new ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					containerLanhDaoListSelected.removeItem(n);
					listLanhDaoSelected.remove(Long.valueOf(n));
					try {
						loadLanhDaoSelect();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		tblSelected.setContainerDataSource(containerLanhDaoListSelected);
	}

	private void doFilter() {
		containerLanhDaoList.removeAllContainerFilters();
		if(txtSearch.getValue().equalsIgnoreCase(""))
		{
			Notification.show("Vui lòng nhập vào từ khóa tìm kiếm",Type.TRAY_NOTIFICATION);
		}
		else
		{
			Filter filter = new SimpleStringFilter("filterView", txtSearch.getValue(), true, false);
			containerLanhDaoList.addContainerFilter(filter);
		}
		tblSelect.setContainerDataSource(containerLanhDaoList);
		tblSelect.setVisibleColumns(new String[]{"STT","Lãnh đạo"});
	}

	public Button getBtnOk() {
		return btnOk;
	}

	public void setBtnOk(Button btnOk) {
		this.btnOk = btnOk;
	}
}
