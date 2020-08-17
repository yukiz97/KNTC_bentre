package ngn.kntc.account;
import java.util.List;

import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class WindowUserOrganization extends Window {
	private VerticalLayout vLayout=new VerticalLayout();
	private Table tableAccount = new Table("Danh sách tài người dùng");

	public WindowUserOrganization(long orgId) {
		this.buildLayout();
		this.configComponent();
		this.loadData(orgId);
	}

	private void buildLayout() {
		this.tableAccount.setWidth(100, Unit.PERCENTAGE);
		this.tableAccount.setHeightUndefined();
		this.tableAccount.addStyleName(ValoTheme.TABLE_BORDERLESS);
		this.tableAccount.addContainerProperty("STT", Integer.class, null);
		this.tableAccount.addContainerProperty("Tên thành viên",String.class, null);
		this.tableAccount.addContainerProperty("Email", String.class, null);
		this.tableAccount.addContainerProperty("Chức vụ", String.class, null);
		this.tableAccount.addContainerProperty("Số điện thoại", List.class,null);
		
		this.vLayout.setWidth(100, Unit.PERCENTAGE);
		this.vLayout.setHeightUndefined();
		this.vLayout.setMargin(true);
		this.vLayout.addComponent(tableAccount);
		
		this.center();
		this.setModal(true);
		this.setWidth(70, Unit.PERCENTAGE);
		this.setHeightUndefined();
		this.setIcon(FontAwesome.LIST);
		this.setDraggable(false);
		this.setContent(vLayout);
		this.setCaption("Danh sách người dùng");
	}

	private void configComponent() {

	}

	@SuppressWarnings("unchecked")
	private void loadData(long orgId) {
		tableAccount.removeAllItems();
		List<User> users = null;
		try {
			users = UserLocalServiceUtil.getOrganizationUsers(orgId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		int stt = 1;
		for (User user : users) {
			try {
				Item rowUser = tableAccount.addItem(user.getUserId());
				rowUser.getItemProperty("STT").setValue(stt++);
				rowUser.getItemProperty("Tên thành viên").setValue(user.getFullName());
				rowUser.getItemProperty("Email").setValue(user.getEmailAddress());
				rowUser.getItemProperty("Chức vụ").setValue(user.getJobTitle());
				rowUser.getItemProperty("Số điện thoại").setValue(user.getPhones());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		tableAccount.setPageLength(users.size());
	}

}
