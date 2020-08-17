package ngn.kntc.windows;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ngn.kntc.beans.LuanChuyenBean;
import ngn.kntc.beans.QuaTrinhXuLyGiaiQuyetBean;
import ngn.kntc.beans.ThongTinDonThuBean;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.LiferayServiceUtil;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;
import ngn.kntc.utils.SessionUtil;

import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class WindowChuyenDonThu extends Window{
	private Panel listDonVi = new Panel();
	ResponsiveLayout rslThongTinChuyen = new ResponsiveLayout();

	private Button btnOk = new Button("Xác nhận");
	private Button btnCancel = new Button("Đóng");
	private int orgSelectedId = 0;
	private String orgSelectedLienThongId = null;
	private List<CheckBox> listCheckBox = new ArrayList<CheckBox>();
	private List<Long> listDonViDatabase = new ArrayList<Long>();
	private Map<String,Integer> listAllLienThongId = new HashMap<String,Integer>();

	private QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh = new QuaTrinhXuLyGiaiQuyetServiceUtil();
	private DonThuServiceUtil svDonThu = new DonThuServiceUtil();
	private int idDonThu;

	TreeTable treeTable = new TreeTable();
	public WindowChuyenDonThu(int idDonThu)
	{
		try {
			this.idDonThu = idDonThu;
			listDonViDatabase = svQuaTrinh.getDonViDaChuyenList(idDonThu);
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
		btnOk.setEnabled(false);
		btnGroup.addComponent(btnOk);
		btnGroup.addComponent(btnCancel);
		btnGroup.setSpacing(true);

		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(listDonVi);
		layout.addComponent(rslThongTinChuyen);
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
				/*if(validate())
				{
					try {
						List<Organization> subOrg = OrganizationLocalServiceUtil.getSuborganizations(SessionUtil.getCompanyid(),orgSelectedId);
						for(Organization org : subOrg)
						{
							if(LiferayServiceUtil.isOrgHasCustomField(org.getOrganizationId(), "OrgFunction", "TCD"))
							{
								List<Long> list = new ArrayList<Long>();
								List<User> listUserRecive = UserLocalServiceUtil.getOrganizationUsers(org.getOrganizationId());
								for(User user : listUserRecive)
								{
									list.add(user.getUserId());
								}
								for(Long n : list)
								{									
									LuanChuyenBean model = new LuanChuyenBean();
									model.setIdDonThu(idDonThu);
									model.setIdUserChuyen(SessionUtil.getUserId());
									model.setIdUserNhan(n);
									model.setPhanCong(false);
									model.setNgayChuyen(new Date());

									svQuaTrinh.insertLuanChuyen(model);

								}
								Notification.show("Đã chuyển đơn thư đến ban Tiếp Công Dân "+OrganizationLocalServiceUtil.getOrganization(orgSelectedId).getName(),Type.TRAY_NOTIFICATION);

								QuaTrinhXuLyGiaiQuyetBean modelQT = new QuaTrinhXuLyGiaiQuyetBean();
								modelQT.setMaDonThu(idDonThu);
								modelQT.setNoiDung(svQuaTrinh.returnStringChuyenDon(SessionUtil.getUserId(), orgSelectedId));
								modelQT.setHeThongTao(true);
								modelQT.setNgayDang(new Date());
								modelQT.setUserNhap(SessionUtil.getUserId());

								ThongTinDonThuBean modelThongTin = new ThongTinDonThuBean();
								modelThongTin.setNguonDonDen(4);
								modelThongTin.setMaCoQuanChuyenDen(LiferayServiceUtil.getOrgCustomFieldValue(SessionUtil.getMasterOrgId(), "OrgLienThongId"));
								modelThongTin.setSoVanBanDen(txtSoVanBan.getValue().trim());
								modelThongTin.setNgayPhatHanhVanBan(dfNgayPhatHanhVanBan.getValue());
								modelThongTin.setOrgChuyen(SessionUtil.getMasterOrgId());
								modelThongTin.setOrgNhan(orgSelectedId);
								modelThongTin.setNgayNhan(new Date());
								modelThongTin.setMaDonThu(idDonThu);
								try {
									int idQuaTrinh = svQuaTrinh.insertQuaTrinhXLGQ(modelQT);
									svDonThu.insertThongTinDon(modelThongTin);
									DonThuModule.insertThongBao(idDonThu, idQuaTrinh);
								} catch (SQLException e) {
									e.printStackTrace();
								}
								break;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					

					CloseWindow(); 
				}*/
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
			System.out.println(SessionUtil.getCompanyid()+" "+SessionUtil.getMasterOrgId());
			List<Organization> orgList = OrganizationLocalServiceUtil.getOrganizations(QueryUtil.ALL_POS, QueryUtil.ALL_POS);
			//treeTable.addItem(new Object[]{OrganizationLocalServiceUtil.getOrganization(SessionUtil.getMasterLocationId()).getName()},SessionUtil.getMasterLocationId());
			for(int i = 0;i<orgList.size();i++)
			{
				String orgId = String.valueOf(orgList.get(i).getOrganizationId());
				loadOrganization(SessionUtil.getCompanyid()+"", orgId,"");
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
		String orgLienThongId = LiferayServiceUtil.getOrgCustomFieldValue(Long.parseLong(organizationId), "OrgLienThongId");
		listAllLienThongId.put(orgLienThongId, Integer.parseInt(organizationId));
		if(!LiferayServiceUtil.isOrgHasCustomField(Long.parseLong(organizationId), "TypeOrg", "DONVIROOT") && !LiferayServiceUtil.isOrgHasCustomField(Long.parseLong(organizationId), "TypeOrg", "DONVI"))
		{
			CheckBox cb = new CheckBox();
			cb.setId(organizationId);
			
			if(defaulDisableCheckBox(cb))
			{
				cb.setValue(true);
				cb.setEnabled(false);
			}

			cb.addValueChangeListener(new ValueChangeListener() {

				@Override
				public void valueChange(ValueChangeEvent event) {
					if(cb.getValue())
					{
						btnOk.setEnabled(true);
						orgSelectedId = Integer.parseInt(cb.getId());
						try {
							orgSelectedLienThongId = orgLienThongId;
						} catch (Exception e) {
							e.printStackTrace();
						}
						for(CheckBox cbTmp : listCheckBox)
						{
							if(defaulDisableCheckBox(cbTmp))
								continue;
							cbTmp.setEnabled(false);
						}
						cb.setEnabled(true);
					}
					else{
						btnOk.setEnabled(false);
						orgSelectedId = -1;
						orgSelectedLienThongId = null;
						for(CheckBox cbTmp : listCheckBox)
						{
							if(defaulDisableCheckBox(cbTmp))
								continue;
							cbTmp.setEnabled(true);
						}
					}
				}
			});

			listCheckBox.add(cb);
			treeTable.addItem(new Object[]{organizationName,cb},organizationId);
			/*treeOrganization.addItem(organizationId);
				treeOrganization.setItemCaption(organizationId, organizationName);*/
			if(!organizationParentId.isEmpty())
				treeTable.setParent(organizationId,organizationParentId);
		}
		else
		{
			treeTable.addItem(new Object[]{organizationName,null},organizationId);
			
			if(!organizationParentId.isEmpty())
				treeTable.setParent(organizationId,organizationParentId);
		}
	}

	private boolean defaulDisableCheckBox(CheckBox cb)
	{
		if(SessionUtil.getOrgId()==Long.parseLong(cb.getId()))
		{
			return true;
		}
		for(Long n: listDonViDatabase)
		{
			if(Long.parseLong(cb.getId())==n)
				return true;
		}
		return false;
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
	public List<Long> getDanhSachDonViDatabase() {
		return listDonViDatabase;
	}
	public void setDanhSachDonViDatabase(List<Long> danhSachDonViDatabase) {
		this.listDonViDatabase = danhSachDonViDatabase;
	}
	public Map<String, Integer> getListAllLienThongId() {
		return listAllLienThongId;
	}
	public void setListAllLienThongId(Map<String, Integer> listAllLienThongId) {
		this.listAllLienThongId = listAllLienThongId;
	}
	public int getOrgSelectedId() {
		return orgSelectedId;
	}
	public void setOrgSelectedId(int orgSelectedId) {
		this.orgSelectedId = orgSelectedId;
	}
}
