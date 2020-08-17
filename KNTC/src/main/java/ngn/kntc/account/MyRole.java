package ngn.kntc.account;
import java.util.List;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.vaadin.data.Item;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class MyRole extends VerticalLayout{
	private Label lbPass = new Label("Vai trò");
	private Table tblRole = new Table();
	
	public static enum COLUMS_ROLE {;
		public static final String TENVAITRO = "Tên vai trò";
		public static final String MOTA = "Mô tả";
	}
	
	public MyRole(User user) {
		buildLayout();
		loadData(user);
	}
	
	private void buildLayout(){
		lbPass.addStyleName(ValoTheme.LABEL_COLORED);
		lbPass.addStyleName("title-tab");
		
		tblRole.addContainerProperty(COLUMS_ROLE.TENVAITRO, Label.class,null);
		tblRole.addContainerProperty(COLUMS_ROLE.MOTA, Label.class, null);
		tblRole.setWidth(100, Unit.PERCENTAGE);
		tblRole.setHeightUndefined();
		
		addComponents(lbPass,tblRole);
		setMargin(true);
		setSpacing(true);
		
	}
	
	@SuppressWarnings("unchecked")
	private void loadData(User user){
		try {
			List<Role> roles = RoleLocalServiceUtil.getUserRoles(user.getUserId());
			for (Role role : roles) {
				Label lblRoleName=new Label(role.getName(),ContentMode.HTML);
				lblRoleName.addStyleName(ValoTheme.LABEL_BOLD);
				
				Label lblRoleDescription=new Label(role.getDescriptionCurrentValue(),ContentMode.HTML);
				lblRoleDescription.addStyleName(ValoTheme.LABEL_COLORED);
				
				Item rowRole = tblRole.addItem(role.getRoleId());
				rowRole.getItemProperty(COLUMS_ROLE.TENVAITRO).setValue(lblRoleName);
				rowRole.getItemProperty(COLUMS_ROLE.MOTA).setValue(lblRoleDescription);
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		tblRole.setPageLength(tblRole.size());
	}
}
