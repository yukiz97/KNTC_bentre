package ngn.kntc.account;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.UserLocalServiceUtil;
//import com.ngn.cddh.utils.CDDHUtil;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class MyCustomField extends VerticalLayout{
	private Label lbPass = new Label("Các trường mở rộng");
//	private TextField txtUserIOffice = new TextField("Tài khoản IOffice");
	private Button btnsavepass = new Button("Cập nhật",FontAwesome.SAVE);
	private PermissionChecker checker;
	
	public MyCustomField(User user) {
		buildlayout();
		configComponent();
		loadData(user);
	}
	
	private void buildlayout() {
		lbPass.addStyleName(ValoTheme.LABEL_COLORED);
		lbPass.addStyleName("title-tab");
		
//		txtUserIOffice.setRequired(true);
//		txtUserIOffice.setIcon(FontAwesome.KEY);
//		txtUserIOffice.setWidth(100, Unit.PERCENTAGE);
		
		btnsavepass.addStyleName(ValoTheme.BUTTON_PRIMARY);
		btnsavepass.setEnabled(false);
		
		setSpacing(true);
		setMargin(true);
		setWidth(100, Unit.PERCENTAGE);
		setHeightUndefined();
		addComponents(lbPass, btnsavepass);
		setComponentAlignment(btnsavepass, Alignment.MIDDLE_RIGHT);
	}
	
	private void configComponent(){
		btnsavepass.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				
			}
		});
	}
	
	private void loadData(User user){
//		initPermissionChecker(user);
//		txtUserIOffice.setValue(user.getExpandoBridge().getAttribute(CDDHUtil.getKeyUserIOffice()).toString());
	}
	
	public void initPermissionChecker(User user){
		try {
			checker = PermissionCheckerFactoryUtil.create(UserLocalServiceUtil.getUser(user.getUserId()));
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		PermissionThreadLocal.setPermissionChecker(checker);
	}
}
