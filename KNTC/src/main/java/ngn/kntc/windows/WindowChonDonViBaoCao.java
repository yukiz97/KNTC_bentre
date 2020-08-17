package ngn.kntc.windows;

import java.util.ArrayList;
import java.util.List;

import ngn.kntc.utils.LiferayServiceUtil;
import ngn.kntc.utils.SessionUtil;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Organization;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class WindowChonDonViBaoCao extends Window{
	private Panel listDonVi = new Panel();
	private Button btnOk = new Button("Xác nhận");
	private Button btnCancel = new Button("Đóng");
	private List<Long> listDonViInput = new ArrayList<Long>();

	TreeTable treeTable = new TreeTable();
	public WindowChonDonViBaoCao(List<Long> orgInput)
	{
		try {
			this.listDonViInput.addAll(orgInput);
			buildLayout();
			configComponent();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void buildLayout() throws Exception {
		treeTable.addContainerProperty("Dánh sách đơn vị",String.class,null);
		treeTable.addContainerProperty("Chọn",CheckBox.class,null);
		treeTable.setSizeFull();

		listDonVi.setContent(treeTable);
		listDonVi.setSizeFull();

		loadData();

		HorizontalLayout btnGroup = new HorizontalLayout();
		btnOk.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btnCancel.setStyleName(ValoTheme.BUTTON_DANGER);
		btnGroup.addComponent(btnOk);
		btnGroup.addComponent(btnCancel);
		btnGroup.setSpacing(true);

		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(listDonVi);
		layout.addComponent(btnGroup);

		layout.setExpandRatio(listDonVi, 1.0f);

		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setSizeFull();

		this.setWidth("60%");
		this.setHeight("90%");
		this.center();
		this.setModal(true);
		this.setCaption("Lựa chọn đơn vị chuyển đến");
		this.setContent(layout);
	}
	private void configComponent() {
		btnOk.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
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

	public void loadData() throws Exception
	{
		try {
			System.out.println(SessionUtil.getCompanyid()+" "+SessionUtil.getOrgId());
			List<Organization> orgList = OrganizationLocalServiceUtil.getSuborganizations(SessionUtil.getCompanyid(), SessionUtil.getMasterOrgId());
			CheckBox cb = new CheckBox();
			cb.setId(SessionUtil.getOrgId()+"");
			
			if(listDonViInput.contains(Long.valueOf(SessionUtil.getOrgId())))
				cb.setValue(true);

			cb.addValueChangeListener(new ValueChangeListener() {

				@Override
				public void valueChange(ValueChangeEvent event) {
					if(cb.getValue())
					{
						if(!listDonViInput.contains(Long.valueOf(Long.parseLong(cb.getId()))))
						{
							listDonViInput.add(Long.parseLong(cb.getId()));
						}
					}
					else{
						if(listDonViInput.contains(Long.valueOf(Long.parseLong(cb.getId()))))
						{
							listDonViInput.remove(Long.valueOf(Long.parseLong(cb.getId())));
						}
					}
				}
			});
			treeTable.addItem(new Object[]{OrganizationLocalServiceUtil.getOrganization(SessionUtil.getMasterOrgId()).getName(),null},SessionUtil.getMasterOrgId()+"");
			for(int i = 0;i<orgList.size();i++)
			{
				String orgId = String.valueOf(orgList.get(i).getOrganizationId());
				loadOrganization(SessionUtil.getCompanyid()+"", orgId,SessionUtil.getMasterOrgId()+"");
			}
		}catch (SystemException e) {
			e.printStackTrace();
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		}
	}

	private void loadOrganization(String companyId,String orgId,String parentOrgId) throws Exception{
		try {
			Organization org=OrganizationLocalServiceUtil.getOrganization(Long.parseLong(orgId));
			String orgName=org.getName();
			addOrganizationToTree(String.valueOf(orgId),orgName,parentOrgId);
			List<Organization> list=OrganizationLocalServiceUtil.getSuborganizations(Long.parseLong(companyId), Long.parseLong(orgId));
			if(list.size()==0)return;
			for(Organization o:list){
				loadOrganization(companyId,String.valueOf(o.getOrganizationId()),String.valueOf(org.getOrganizationId()));
			}
			System.out.println("Tổ chức: "+orgName+" có "+list.size()+" thành viên");
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	private void addOrganizationToTree(String organizationId,String organizationName,String organizationParentId) throws Exception{
		if(!LiferayServiceUtil.isOrgHasCustomField(Long.parseLong(organizationId), "TypeOrg", "DONVIROOT") && !LiferayServiceUtil.isOrgHasCustomField(Long.parseLong(organizationId), "TypeOrg", "DONVI"))
		{
			CheckBox cb = new CheckBox();
			cb.setId(organizationId);
			
			if(listDonViInput.contains(Long.valueOf(organizationId)))
				cb.setValue(true);

			cb.addValueChangeListener(new ValueChangeListener() {

				@Override
				public void valueChange(ValueChangeEvent event) {
					if(cb.getValue())
					{
						if(!listDonViInput.contains(Long.valueOf(Long.parseLong(cb.getId()))))
						{
							listDonViInput.add(Long.parseLong(cb.getId()));
						}
					}
					else{
						if(listDonViInput.contains(Long.valueOf(Long.parseLong(cb.getId()))))
						{
							listDonViInput.remove(Long.valueOf(Long.parseLong(cb.getId())));
						}
					}
				}
			});

			treeTable.addItem(new Object[]{organizationName,cb},organizationId);
			/*treeOrganization.addItem(organizationId);
				treeOrganization.setItemCaption(organizationId, organizationName);*/
		}
		else
		{
			treeTable.addItem(new Object[]{organizationName,null},organizationId);
		}
		if(!organizationParentId.isEmpty())
			treeTable.setParent(organizationId,organizationParentId);
		System.out.println(organizationName+" -- "+organizationParentId);
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
	public List<Long> getListDonViInput() {
		return listDonViInput;
	}
	public void setListDonViInput(List<Long> listDonViInput) {
		this.listDonViInput = listDonViInput;
	}
}
