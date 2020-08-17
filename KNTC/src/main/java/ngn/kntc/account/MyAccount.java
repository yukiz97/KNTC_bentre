package ngn.kntc.account;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class MyAccount extends VerticalLayout {
	private Label lbDetail = new Label("Chi tiết hồ sơ cá nhân");
	private TextField txtScreenName = new TextField("Tài khoản");
	private TextField txtFullName = new TextField("Họ tên");
	private TextField txtEmail = new TextField("Email");
	private TextField txtJoinTitle = new TextField("Chức vụ");
	private Button btnSave = new Button("Lưu lại");
	
	private User user;
	
	public MyAccount(User user) {
		this.user=user;
		buildLayout();
		configComponent();
		loadData();
	}

	private void buildLayout() {
		lbDetail.addStyleName("title-tab");
		lbDetail.addStyleName(ValoTheme.LABEL_COLORED);
		
		txtJoinTitle.setEnabled(false);
		txtScreenName.setEnabled(false);
		txtScreenName.setRequired(true);
		txtFullName.setRequired(true);
		txtEmail.setRequired(true);

		txtJoinTitle.setIcon(FontAwesome.SUPPORT);
		txtFullName.setIcon(FontAwesome.USER_MD);
		txtScreenName.setIcon(FontAwesome.USER_SECRET);
		txtEmail.setIcon(FontAwesome.MAIL_FORWARD);
		btnSave.setIcon(FontAwesome.SAVE);

		txtEmail.setWidth(100, Unit.PERCENTAGE);
		txtJoinTitle.setWidth(100, Unit.PERCENTAGE);
		txtFullName.setWidth(100, Unit.PERCENTAGE);
		txtScreenName.setWidth(100, Unit.PERCENTAGE);

		btnSave.addStyleName(ValoTheme.BUTTON_PRIMARY);
		
		setSpacing(true);
		setMargin(true);
		setWidth(100, Unit.PERCENTAGE);
		setHeightUndefined();

		addComponents(lbDetail, txtScreenName, txtEmail, txtFullName, txtJoinTitle, btnSave);
		setComponentAlignment(btnSave, Alignment.MIDDLE_RIGHT);
	}

	private void configComponent() {
		btnSave.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (check()) {
					updateUser();
				}
			}
		});
	}

	private void loadData() {
		String screenName = user.getScreenName();
		String email = user.getEmailAddress();
		String firstName = user.getFirstName();
		String jobTitle = user.getJobTitle();

		txtScreenName.setValue(screenName);
		txtEmail.setValue(email);
		txtFullName.setValue(firstName);
		txtJoinTitle.setValue(jobTitle);
	}
	
	private void updateUser() {
		try {
			user.setScreenName(txtScreenName.getValue());
			user.setFirstName(txtFullName.getValue());
			user.setMiddleName("");
			user.setLastName("");
			user.setJobTitle(txtJoinTitle.getValue());
			user.setEmailAddress(txtEmail.getValue());
			UserLocalServiceUtil.updateUser(user);
			
			Notification alert=new Notification("Cập nhật thành công!",Type.TRAY_NOTIFICATION);
			alert.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
			alert.setPosition(Position.TOP_RIGHT);
			alert.show(Page.getCurrent());
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	private boolean check() {
		if(!isAvailableUsername()){
			txtScreenName.setRequiredError("Tài khoản này đã tồn tại");
		}if (txtScreenName.getValue().isEmpty()) {
			txtScreenName.setRequiredError("Tài khoản này không được để trống");
		}else if (!validateEmail(txtEmail.getValue())) {
			txtEmail.setRequiredError("Email không đúng định dạng");
			return false;
		}else if (txtFullName.getValue().isEmpty()) {
			txtFullName.setRequiredError("Họ tên không được để trống");
			return false;
		}
		return true;
	}

	private boolean isAvailableUsername() {
		/* Kiểm tra trong csdl có username này chưa */
		String screenNameOld = user.getScreenName();
		String screenNameNew = txtScreenName.getValue();
		if (screenNameOld.equals(screenNameNew)) {
			return true;
		} else if (!userExists(screenNameNew)) {
			Notification alert = new Notification("Tài khoản ["+screenNameNew+"] này đã tồn tại", Type.TRAY_NOTIFICATION);
			alert.setStyleName(ValoTheme.NOTIFICATION_ERROR);
			alert.setPosition(Position.TOP_RIGHT);
			alert.show(Page.getCurrent());
			return false;
		}else if(userExists(screenNameNew)) {
			return true;
		}
		return false;
	}

	private boolean userExists(String screenName) {
		try {
			User checkUserName = UserLocalServiceUtil.getUserByScreenName(user.getCompanyId(), screenName);
			String CheckUser = checkUserName.getScreenName();
			if (CheckUser != null) {
				return false;
			} 
		} catch (Exception e) {
			return true;
		}
		return true;
	}

	public static boolean validateEmail(String emailStr) {
		Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",Pattern.CASE_INSENSITIVE);
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
		return matcher.find();
	}
}

