package ngn.kntc.windows;

import java.util.List;

import ngn.kntc.modules.RemoveUnicodeCharacter;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.SessionUtil;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.model.Organization;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
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
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class WindowSelectOrganization extends Window{
	private Panel listDonVi = new Panel();
	//Tìm kiếm
	private HorizontalLayout hSearch = new HorizontalLayout();
	private TextField txtSearch = new TextField();
	private Button btnSearch = new Button("Tìm kiếm",FontAwesome.SEARCH);
	private Button btnRefesh = new Button("Làm mới",FontAwesome.REFRESH);
	private HorizontalLayout hControl = new HorizontalLayout();
	private Button btnOk = new Button("Chọn",FontAwesome.PLUS);
	private Button btnCancel = new Button("Đóng",FontAwesome.REMOVE);
	private String coQuanID;

	final String TENCOQUAN = "Tên cơ quan";
	final String FILTER = "filterView";

	TreeTable treeTable = new TreeTable();
	IndexedContainer container = new IndexedContainer();

	DanhMucServiceUtil svDanhMuc = new DanhMucServiceUtil();

	public WindowSelectOrganization()
	{
		try {
			buildLayout();
		} catch (Exception e) {
			e.printStackTrace();
		}
		configComponent();
	}
	private void buildLayout() throws Exception {
		treeTable.addContainerProperty(TENCOQUAN,String.class,null);
		treeTable.addContainerProperty(FILTER,String.class,null);
		treeTable.setSizeFull();
		treeTable.setSelectable(true);

		listDonVi.setContent(treeTable);
		listDonVi.setSizeFull();

		loadData();
		buildSearch();

		btnOk.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btnCancel.setStyleName(ValoTheme.BUTTON_DANGER);
		btnOk.setEnabled(false);
		hControl.addComponent(btnOk);
		hControl.addComponent(btnCancel);
		hControl.setSpacing(true);

		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(hSearch);
		layout.addComponent(listDonVi);
		layout.addComponent(hControl);

		layout.setComponentAlignment(hControl, Alignment.MIDDLE_RIGHT);

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
				container.removeAllContainerFilters();
				treeTable.setContainerDataSource(container);
				treeTable.setVisibleColumns(new String[]{TENCOQUAN});
			}
		});
	}

	public void loadData() throws Exception
	{
		List<Organization> orgList = OrganizationLocalServiceUtil.getOrganizations(QueryUtil.ALL_POS, QueryUtil.ALL_POS);
		for(int i = 0;i<orgList.size();i++)
		{
			String orgId = String.valueOf(orgList.get(i).getOrganizationId());
			loadOrganization(SessionUtil.getCompanyid()+"", orgId,"");
		}
		container = (IndexedContainer) treeTable.getContainerDataSource();
		treeTable.setVisibleColumns(new String[]{TENCOQUAN});
	}

	private void loadOrganization(String companyId,String orgId,String parentOrgId) throws Exception{
		Organization org=OrganizationLocalServiceUtil.getOrganization(Long.parseLong(orgId));
		String orgName=org.getName();
		addOrganizationToTree(String.valueOf(orgId),orgName,parentOrgId);
		List<Organization> list=OrganizationLocalServiceUtil.getSuborganizations(Long.parseLong(companyId), Long.parseLong(orgId));
		if(list.size()==0)return;
		for(Organization o:list){
			loadOrganization(companyId,String.valueOf(o.getOrganizationId()),String.valueOf(org.getOrganizationId()));
		}
		System.out.println("Tổ chức: "+orgName+" có "+list.size()+" thành viên");
	}

	private void addOrganizationToTree(String organizationId,String organizationName,String organizationParentId) throws Exception{
		String filter = organizationName+" "+RemoveUnicodeCharacter.removeAccent(organizationName);
		treeTable.addItem(new Object[]{organizationName,filter},organizationId);
		if(!organizationParentId.isEmpty())
		{
			treeTable.setParent(organizationId,organizationParentId);
		}
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
		hSearch.setWidth("100%");
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
