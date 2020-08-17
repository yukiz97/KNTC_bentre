package ngn.kntc.windows;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ngn.kntc.beans.LuanChuyenBean;
import ngn.kntc.beans.QuaTrinhXuLyGiaiQuyetBean;
import ngn.kntc.enums.TypeOrgCustomFieldEnum;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.utils.LiferayServiceUtil;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;
import ngn.kntc.utils.SessionUtil;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class WindowPhanCong extends Window{
	private Panel pnlPhanXuLy = new Panel();
	private HorizontalSplitPanel hspanel = new HorizontalSplitPanel();
	TreeTable treeTable = new TreeTable();
	VerticalLayout vUser = new VerticalLayout();
	List<Long> listCanBoSelected = new ArrayList<Long>();
	List<Long> ListCanBoSelectedDataBase = new ArrayList<Long>();
	Table tblDanhSachCanBoSelect = new Table();
	Table tblDanhSachCanBoSelected = new Table();
	IndexedContainer containerDanhSachCanBo = new IndexedContainer();
	IndexedContainer containerDanhSachCanBoSelected = new IndexedContainer();
	/* Button Group */
	HorizontalLayout btnGroup = new HorizontalLayout();
	private Button btnOk = new Button("Xác nhận");
	private Button btnCancel = new Button("Đóng");
	
	private QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh = new QuaTrinhXuLyGiaiQuyetServiceUtil();
	private int idDonThu;

	public WindowPhanCong(int idDonThu)
	{
		try {
			this.idDonThu = idDonThu;
			buildLayout();
			configComponent();
			List<Long> listUserDatabase = svQuaTrinh.getUserLuanChuyen(idDonThu, 1);
			for(long n : listUserDatabase)
				ListCanBoSelectedDataBase.add(n);
			loadDataTableSelected();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void buildLayout() throws Exception {

		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(pnlPhanXuLy);
		layout.addComponent(btnGroup);

		layout.setComponentAlignment(btnGroup, Alignment.MIDDLE_RIGHT);

		layout.setExpandRatio(pnlPhanXuLy, 1.0f);

		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setSizeFull();

		pnlPhanXuLy.setSizeFull();
		pnlPhanXuLy.setContent(hspanel);

		buildMainLayout();
		buildControl();

		this.setWidth("80%");
		this.setHeight("90%");
		this.center();
		this.setCaption("Phân công cán bộ");
		this.setContent(layout);
		this.setModal(true);
	}
	private void configComponent()
	{
		treeTable.addValueChangeListener(new TreeTable.ValueChangeListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				containerDanhSachCanBo.removeAllItems();
				if(treeTable.getValue()!=null)
				{
					long location = Long.valueOf(String.valueOf(treeTable.getValue()));
					System.out.println(SessionUtil.getMasterOrgId()+"**--**"+location);
					try {
						if(LiferayServiceUtil.isOrgHasCustomField(location, "TypeOrg", TypeOrgCustomFieldEnum.DONVI.getName()))
							return;
						List<User> listUser = UserLocalServiceUtil.getOrganizationUsers(location);
						int i = 0;
						for(User user : listUser)
						{
							//tao checkbox
							CheckBox cb = new CheckBox();
							cb.setId(user.getUserId()+"");
							cb.setCaption(user.getFirstName());
							//disabled nhung can bo da co trong database
							for(Long n :ListCanBoSelectedDataBase)
							{
								if(Long.parseLong(cb.getId())==n)
								{
									cb.setValue(true);
									cb.setEnabled(false);
								}
							}

							//them vao bang danh sach
							Item item = containerDanhSachCanBo.addItem(user.getUserId());
							item.getItemProperty("STT").setValue(++i);
							item.getItemProperty("Cán bộ").setValue(cb);
							item.getItemProperty("Chức vụ").setValue(user.getJobTitle()==null?"":user.getJobTitle());
							item.getItemProperty("Đơn vị").setValue(LiferayServiceUtil.getUserOrgName(user.getUserId()));

							//check cac checkbox da duoc chon
							for(Long n : listCanBoSelected)
							{
								if(Integer.parseInt(cb.getId())==n)
									cb.setValue(true);
							}

							//Xu ly su kien check
							cb.addValueChangeListener(new CheckBox.ValueChangeListener() {

								@Override
								public void valueChange(ValueChangeEvent event) {
									if(cb.getValue())
									{
										if(!listCanBoSelected.contains(Long.valueOf(Long.parseLong(cb.getId()))))
										{
											listCanBoSelected.add(Long.parseLong(cb.getId()));
										}
									}
									else{
										if(listCanBoSelected.contains(Long.valueOf(Long.parseLong(cb.getId()))))
										{
											listCanBoSelected.remove(Long.valueOf(Long.parseLong(cb.getId())));
										}
									}
									try {
										loadDataTableSelected();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

		btnOk.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if(!listCanBoSelected.isEmpty())
				{
					String noiDung = "";
					for(Long n: listCanBoSelected)
					{
						LuanChuyenBean model = new LuanChuyenBean();
						model.setIdDonThu(idDonThu);
						model.setIdUserChuyen(SessionUtil.getUserId());
						model.setIdUserNhan(n);
						model.setPhanCong(true);
						model.setNgayChuyen(new Date());
						
						try {
							noiDung += svQuaTrinh.returnStringPhanCong(SessionUtil.getUserId(), n)+"<br/>";
							svQuaTrinh.insertLuanChuyen(model);
							Notification.show("Đã phân công thành công",Type.TRAY_NOTIFICATION);
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
				CloseWindow();
			}
		});
		btnCancel.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				CloseWindow();
			}
		});		
	}

	@SuppressWarnings("deprecation")
	public void buildMainLayout() throws Exception
	{
		/* First Component */
		treeTable.addContainerProperty("Danh sách sở ban ngành",String.class,null);
		loadDataLocation();

		treeTable.setSizeFull();
		treeTable.setSelectable(true);

		/* Second Component */
		Panel pnlTableSelect = new Panel();
		Panel pnlTableSelected = new Panel();

		vUser.addComponents(pnlTableSelect,pnlTableSelected);

		pnlTableSelect.setContent(tblDanhSachCanBoSelect); 
		pnlTableSelect.setSizeFull();

		pnlTableSelected.setContent(tblDanhSachCanBoSelected);
		pnlTableSelected.setSizeFull();

		vUser.setSizeFull();
		vUser.setSpacing(true);

		/* tbl thông tin những cán bộ */

		containerDanhSachCanBo.addContainerProperty("STT", Integer.class, null);
		containerDanhSachCanBo.addContainerProperty("Cán bộ", CheckBox.class, null);
		containerDanhSachCanBo.addContainerProperty("Chức vụ", String.class, null);
		containerDanhSachCanBo.addContainerProperty("Đơn vị", String.class, null);

		tblDanhSachCanBoSelect.setContainerDataSource(containerDanhSachCanBo);
		tblDanhSachCanBoSelect.setSizeFull();
		tblDanhSachCanBoSelect.addStyleName("table-vanban");

		tblDanhSachCanBoSelect.setColumnAlignment("STT", Align.CENTER);

		/* tbl thông tin những cán bộ được phân công */

		containerDanhSachCanBoSelected.addContainerProperty("STT", Integer.class, null);
		containerDanhSachCanBoSelected.addContainerProperty("Tên cán bộ", String.class, null);
		containerDanhSachCanBoSelected.addContainerProperty("Chức vụ", String.class, null);
		containerDanhSachCanBoSelected.addContainerProperty("Đơn vị", String.class, null);
		containerDanhSachCanBoSelected.addContainerProperty("Xóa", Button.class, null);

		tblDanhSachCanBoSelected.setContainerDataSource(containerDanhSachCanBoSelected);
		tblDanhSachCanBoSelected.setSizeFull();
		tblDanhSachCanBoSelected.addStyleName("table-vanban");
		tblDanhSachCanBoSelected.setColumnAlignment("STT", Align.CENTER);
		tblDanhSachCanBoSelected.setColumnAlignment("Xóa", Align.CENTER);

		hspanel.setFirstComponent(treeTable);
		hspanel.setSecondComponent(vUser);

		hspanel.setSplitPosition(25, Sizeable.UNITS_PERCENTAGE);
		hspanel.setSizeFull();
	}

	public void buildControl()
	{
		btnOk.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btnCancel.setStyleName(ValoTheme.BUTTON_DANGER);
		btnGroup.addComponent(btnOk);
		btnGroup.addComponent(btnCancel);
		btnGroup.setSpacing(true);
	}

	public void loadDataLocation() throws Exception
	{
		if(svQuaTrinh.isLanhDaoCuaCoQuanThamMuu(idDonThu))
		{
			addOrganizationToTree(String.valueOf(SessionUtil.getOrgId()),LiferayServiceUtil.getUserOrgName(SessionUtil.getUserId()),"");

			return;
		}

		List<Organization> orgList = OrganizationLocalServiceUtil.getSuborganizations(SessionUtil.getCompanyid(), SessionUtil.getMasterOrgId());
		treeTable.addItem(new Object[]{OrganizationLocalServiceUtil.getOrganization(SessionUtil.getMasterOrgId()).getName()},String.valueOf(SessionUtil.getMasterOrgId()+""));
		for(int i = 0;i<orgList.size();i++)
		{
			String orgId = String.valueOf(orgList.get(i).getOrganizationId());
			loadOrganization(SessionUtil.getCompanyid()+"", orgId,String.valueOf(SessionUtil.getMasterOrgId()));
		}
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

	private void addOrganizationToTree(String organizationId,String organizationName,String organizationParentId) throws NumberFormatException, UnsupportedOperationException, Exception{
		System.out.println(organizationId+" - "+organizationName+" - "+organizationParentId);
		treeTable.addItem(new Object[]{organizationName},organizationId);
		/*treeOrganization.addItem(organizationId);
		treeOrganization.setItemCaption(organizationId, organizationName);*/
		if(!organizationParentId.isEmpty())
		{
			treeTable.setParent(organizationId,organizationParentId);
		}
	}

	@SuppressWarnings("unchecked")
	public void loadDataTableSelected() throws Exception
	{
		int i =0;
		containerDanhSachCanBoSelected.removeAllItems();
		List<Long> listTmp = new ArrayList<Long>();
		listTmp.addAll(listCanBoSelected);
		listTmp.addAll(ListCanBoSelectedDataBase);
		for(Long n : listTmp)
		{
			User user= UserLocalServiceUtil.getUser(Long.valueOf(String.valueOf(n)));
			Button btnDelete = new Button(FontAwesome.TRASH_O);
			btnDelete.addStyleName(ValoTheme.BUTTON_DANGER);
			Item item = containerDanhSachCanBoSelected.addItem(n);
			item.getItemProperty("STT").setValue(++i);
			item.getItemProperty("Tên cán bộ").setValue(user.getFirstName());
			item.getItemProperty("Chức vụ").setValue(user.getJobTitle()==null?"":user.getJobTitle());
			item.getItemProperty("Đơn vị").setValue(LiferayServiceUtil.getUserOrgName(user.getUserId()));
			item.getItemProperty("Xóa").setValue(btnDelete);

			for(Long id : ListCanBoSelectedDataBase)
			{
				if(id==n)
				{
					btnDelete.setEnabled(false);
				}
			}

			btnDelete.addClickListener(new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					containerDanhSachCanBoSelected.removeItem(n);
					listCanBoSelected.remove(Long.valueOf(n));
					treeTable.unselect(treeTable.getValue());
				}
			});
		}
		tblDanhSachCanBoSelected.setContainerDataSource(containerDanhSachCanBoSelected);
	}
	
	private void CloseWindow() {
		this.close();
	}

	public Button getBtnOk() {
		return btnOk;
	}

	public void setBtnOk(Button btnOk) {
		this.btnOk = btnOk;
	}
}
