package ngn.kntc.windows;

import java.sql.SQLException;
import java.util.List;

import ngn.kntc.beans.DanhMucBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.modules.RemoveUnicodeCharacter;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.DonThuServiceUtil;

import com.vaadin.data.Container.Filter;
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
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class WindowChonCoQuan extends Window{
	private Panel listDonVi = new Panel();
	//Tìm kiếm
	HorizontalLayout hSearch = new HorizontalLayout();
	TextField txtSearch = new TextField();
	Button btnSearch = new Button("Tìm kiếm",FontAwesome.SEARCH);
	Button btnRefesh = new Button("Làm mới",FontAwesome.REFRESH);
	CheckBox cmbAnDonVi = new CheckBox("Chỉ hiển thị đơn vị trong Bến Tre");
	private Button btnOk = new Button("Xác nhận");
	private Button btnCancel = new Button("Đóng");
	private String coQuanID;
	
	final String TENCOQUAN = "Tên cơ quan";
	final String FILTER = "filterView";
	final String FILTERMASTERID = "IDCoQuan";

	TreeTable treeTable = new TreeTable();
	IndexedContainer container = new IndexedContainer();
	
	DanhMucServiceUtil svDanhMuc = new DanhMucServiceUtil();
	
	public WindowChonCoQuan()
	{
		try {
			buildLayout();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		configComponent();
		cmbAnDonVi.setValue(true);
	}
	private void buildLayout() throws SQLException {
		treeTable.addContainerProperty(TENCOQUAN,String.class,null);
		treeTable.addContainerProperty(FILTER,String.class,null);
		treeTable.addContainerProperty(FILTERMASTERID,String.class,null);

		treeTable.setSizeFull();
		treeTable.setSelectable(true);

		listDonVi.setContent(treeTable);
		listDonVi.setSizeFull();

		try {
			loadData();
		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		buildSearch();

		HorizontalLayout btnGroup = new HorizontalLayout();
		btnOk.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btnCancel.setStyleName(ValoTheme.BUTTON_DANGER);
		btnOk.setEnabled(false);
		btnGroup.addComponent(btnOk);
		btnGroup.addComponent(btnCancel);
		btnGroup.setSpacing(true);

		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(hSearch);
		layout.addComponent(cmbAnDonVi);
		layout.addComponent(listDonVi);
		layout.addComponent(btnGroup);

		layout.setExpandRatio(listDonVi, 1.0f);

		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setSizeFull();

		this.setWidth("60%");
		this.setHeight("90%");
		this.center();
		this.setCaption("Lựa chọn cơ quan");
		this.setModal(true);
		this.setContent(layout);
	}
	private void configComponent() {
		treeTable.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				coQuanID = (String)treeTable.getValue();
				if(coQuanID!=null)
				{
					btnOk.setEnabled(true);
				}
				else
				{
					btnOk.setEnabled(false);
				}
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
		
		btnOk.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});

		btnCancel.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				close();
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
				txtSearch.setValue("");
				container.removeContainerFilters(FILTER);
				treeTable.setContainerDataSource(container);
				treeTable.setVisibleColumns(new String[]{TENCOQUAN});
			}
		});
		
		cmbAnDonVi.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				doFilter();
			}
		});
	}

	public void loadData() throws UnsupportedOperationException, Exception
	{
		List<DanhMucBean> listCoQuan = svDanhMuc.getDanhMucList(DanhMucTypeEnum.coquan.getName(), DanhMucTypeEnum.coquan.getIdType());
		for(DanhMucBean model: listCoQuan)
		{
			String filter = model.getName()+" "+RemoveUnicodeCharacter.removeAccent(model.getName());
			treeTable.addItem(new Object[]{model.getName(),filter,model.getStrMa()},model.getStrMa());
			//treeTable.setChildrenAllowed(model.getStrMa(), DonThuServiceUtil.checkIfDonViHasChildren(model.getStrMa()));
			if(model.getStrMa().contains("."))
			{
				if(model.getStrMa().length()>3)
				{
					String parentId = model.getStrMa().substring(0,model.getStrMa().length()-3);
					treeTable.setParent(model.getStrMa(), parentId);
				}
			}
			else
			{
				if(model.getStrMa().length()>4)
				{
					String parentId = model.getStrMa().substring(0,model.getStrMa().length()-2);
					treeTable.setParent(model.getStrMa(), parentId);
				}
			}
		}
		container = (IndexedContainer) treeTable.getContainerDataSource();
		treeTable.setVisibleColumns(new String[]{TENCOQUAN});
	}
	
	private void buildSearch()
	{
		hSearch.addComponent(txtSearch);
		hSearch.addComponent(btnSearch);
		hSearch.addComponent(btnRefesh);
		
		txtSearch.setInputPrompt("Nhập vào tên cơ quan cần tìm....");
		txtSearch.setSizeFull();
		
		hSearch.setExpandRatio(txtSearch, 1.0f);
		
		hSearch.setSpacing(true);
		hSearch.setSizeFull();
		hSearch.setHeight("30px");
	}
	
	private void doFilter() {
		container.removeAllContainerFilters();
		if(txtSearch.getValue().equalsIgnoreCase(""))
		{

		}
		else
		{
			Filter filter = new SimpleStringFilter("filterView", txtSearch.getValue(), true, false);
			container.addContainerFilter(filter);
		}
		
		if(cmbAnDonVi.getValue())
		{
			Filter filterId = new SimpleStringFilter(FILTERMASTERID, "H811", true, false);
			container.addContainerFilter(filterId);
		}
			
		treeTable.setContainerDataSource(container);
		treeTable.setVisibleColumns(new String[]{TENCOQUAN});
	}

	public String getCoQuanID() {
		return coQuanID;
	}
	public void setCoQuanID(String coQuanID) {
		this.coQuanID = coQuanID;
	}
	public Button getBtnOk() {
		return btnOk;
	}
	public void setBtnOk(Button btnOk) {
		this.btnOk = btnOk;
	}
}
