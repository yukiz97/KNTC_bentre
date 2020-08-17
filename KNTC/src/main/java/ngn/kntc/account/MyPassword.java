package ngn.kntc.account;
import com.liferay.portal.kernel.cache.MultiVMPoolUtil;
import com.liferay.portal.kernel.cache.SingleVMPoolUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.CompanyConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.service.UserServiceUtil;
import com.liferay.portal.service.persistence.UserUtil;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction.KeyCode;
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
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class MyPassword extends VerticalLayout {
	
	private Label lbPass = new Label("Đổi mật khẩu");
	private PasswordField txtPassWordOld = new PasswordField("Nhập lại mật khẩu cũ");
	private PasswordField txtPassWordNew = new PasswordField("Nhập mật khẩu mới");
	private PasswordField txtReTypePassWord = new PasswordField("Nhập lại mật khẩu");
	private Button btnsavepass = new Button("Cập nhật",FontAwesome.SAVE);
	private User user;
	
	public MyPassword(User user) {
		this.user=user;
		buildlayout();
		buttonConfig();
	}

	private void buildlayout() {
		lbPass.addStyleName(ValoTheme.LABEL_COLORED);
		lbPass.addStyleName("title-tab");
		
		txtPassWordOld.setIcon(FontAwesome.KEY);
		txtPassWordNew.setIcon(FontAwesome.KEY);
		txtReTypePassWord.setIcon(FontAwesome.KEY);

		txtPassWordOld.setRequired(true);
		txtPassWordNew.setRequired(true);
		txtReTypePassWord.setRequired(true);
		
		txtPassWordNew.setWidth(100, Unit.PERCENTAGE);
		txtPassWordOld.setWidth(100, Unit.PERCENTAGE);
		txtReTypePassWord.setWidth(100, Unit.PERCENTAGE);
		
		btnsavepass.addStyleName(ValoTheme.BUTTON_PRIMARY);
		
		setSpacing(true);
		setMargin(true);
		setWidth(100, Unit.PERCENTAGE);
		setHeightUndefined();
		addComponents(lbPass, txtPassWordOld, txtPassWordNew,txtReTypePassWord, btnsavepass);
		setComponentAlignment(btnsavepass, Alignment.MIDDLE_RIGHT);
	}

	private boolean check() {
		if (txtPassWordOld.getValue().isEmpty()) {
			txtPassWordOld.setRequiredError("Mật khẩu cũ không để trống");
			return false;
		} else if (txtPassWordNew.getValue().isEmpty()) {
			txtPassWordNew.setRequiredError("Mật khẩu mới không để trống");
			return false;
		} else if (txtReTypePassWord.getValue().isEmpty()) {
			txtReTypePassWord.setRequiredError("Mật khẩu mới không để trống");
			return false;
		} else if (!checkPassOld()) {
			txtPassWordOld.setValue("");
			txtPassWordOld.setRequiredError("Mật khẩu cũ không chính xác");
			Notification alert = new Notification("Mật khẩu cũ nhập vào không chính xác",Type.TRAY_NOTIFICATION);
			alert.setStyleName(ValoTheme.NOTIFICATION_ERROR);
			alert.setPosition(Position.TOP_RIGHT);
			alert.show(Page.getCurrent());
			return false;
		} else if (!txtPassWordNew.getValue().equals(txtReTypePassWord.getValue())) {
			txtPassWordNew.clear();
			txtReTypePassWord.clear();
			
			txtPassWordNew.setRequiredError("Mật khẩu mới không khớp");
			txtReTypePassWord.setRequiredError("Mật khẩu mới không khớp");
			Notification alert = new Notification("Mật khẩu nhập mới không khớp",Type.TRAY_NOTIFICATION);
			alert.setStyleName(ValoTheme.NOTIFICATION_ERROR);
			alert.setPosition(Position.TOP_RIGHT);
			alert.show(Page.getCurrent());
			return false;
		} else if(txtPassWordOld.getValue().equals(txtPassWordNew.getValue())){
			txtPassWordNew.clear();
			txtReTypePassWord.clear();
			Notification alert = new Notification("Mật khẩu nhập mới không được trùng với mật khẩu cũ",Type.TRAY_NOTIFICATION);
			alert.setStyleName(ValoTheme.NOTIFICATION_ERROR);
			alert.setPosition(Position.TOP_RIGHT);
			alert.show(Page.getCurrent());
			return false;
		}
		return true;
	}

	private boolean checkPassOld() {
		long companyId = user.getCompanyId();
		String authType = CompanyConstants.AUTH_TYPE_SN;
		String username = user.getScreenName();
		String password = txtPassWordOld.getValue();
		try {
			MultiVMPoolUtil.clear();
			SingleVMPoolUtil.clear();
			if(UserLocalServiceUtil.authenticateForBasic(companyId, authType, username, password)==user.getUserId())
				return true;
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void buttonConfig() {
		txtReTypePassWord.addFocusListener(new FocusListener() {
			@Override
			public void focus(FocusEvent event) {
				btnsavepass.setClickShortcut(KeyCode.ENTER);
			}
		});
		
		txtReTypePassWord.addBlurListener(new BlurListener() {
			@Override
			public void blur(BlurEvent event) {
				btnsavepass.setClickShortcut(-1);
			}
		});
		
		btnsavepass.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (check()) {
					updatePassword();
				}
			}
		});
	}

	private void updatePassword() {
		try {
			MultiVMPoolUtil.clear();
			SingleVMPoolUtil.clear();
			user = UserLocalServiceUtil.updatePassword(user.getUserId(),txtPassWordNew.getValue(),txtReTypePassWord.getValue(), true);
			Notification alert = new Notification("Cập nhật mật khẩu thành công",Type.TRAY_NOTIFICATION);
			alert.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
			alert.setPosition(Position.TOP_RIGHT);
			alert.show(Page.getCurrent());
			MultiVMPoolUtil.clear();
			SingleVMPoolUtil.clear();
			UserUtil.clearCache();
			
			
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}
}