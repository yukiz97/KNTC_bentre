package ngn.kntc.account;
import java.util.List;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class MyOrganization extends VerticalLayout{
	private Label lbPass = new Label("Tổ chức trực thuộc");
	private Table tblOrgan = new Table();
	
	public static enum COLUMS_ORG {;
		public static final String TENDONVI = "Tên đơn vị";
		public static final String NUMBERACCOUNT = "Số người";
		public static final String XEM = "Xem";
	}
	
	public MyOrganization(User user) {
		buildLayout();
		loadData(user);
	}
	
	private void buildLayout(){
		lbPass.addStyleName(ValoTheme.LABEL_COLORED);
		lbPass.addStyleName("title-tab");
		
		tblOrgan.addContainerProperty(COLUMS_ORG.TENDONVI, Label.class,null);
		tblOrgan.addContainerProperty(COLUMS_ORG.NUMBERACCOUNT, Integer.class,null);
		tblOrgan.addContainerProperty(COLUMS_ORG.XEM, Button.class, null);
		tblOrgan.setWidth(100, Unit.PERCENTAGE);
		tblOrgan.setHeightUndefined();
		
		addComponents(lbPass, tblOrgan);
		setMargin(true);
		setSpacing(true);
	}
	
	@SuppressWarnings("unchecked")
	private void loadData(User user){
		tblOrgan.removeAllItems();
		try {
			List<Organization> organs = OrganizationLocalServiceUtil.getUserOrganizations(user.getUserId());
			for (Organization organ : organs) {
				Label lblOrgName=new Label(organ.getName(),ContentMode.HTML);
				lblOrgName.addStyleName(ValoTheme.LABEL_BOLD);
				
				Button btnDetail = new Button("Xem người dùng",FontAwesome.EYE);
				btnDetail.setWidth(null);
				btnDetail.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
				Item rowOrgan = tblOrgan.addItem(organ.getOrganizationId());
				rowOrgan.getItemProperty(COLUMS_ORG.TENDONVI).setValue(lblOrgName);
				int number=UserLocalServiceUtil.getOrganizationUsersCount(organ.getOrganizationId());
				rowOrgan.getItemProperty(COLUMS_ORG.NUMBERACCOUNT).setValue(number);
				rowOrgan.getItemProperty(COLUMS_ORG.XEM).setValue(btnDetail);
				btnDetail.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						WindowUserOrganization wd = new WindowUserOrganization(organ.getOrganizationId());
						UI.getCurrent().addWindow(wd);
					}
				});
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		tblOrgan.setPageLength(tblOrgan.size());
	}
}
