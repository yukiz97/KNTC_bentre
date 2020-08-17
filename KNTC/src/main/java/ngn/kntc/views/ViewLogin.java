package ngn.kntc.views;

import ngn.kntc.UI.kntcUI;
import ngn.kntc.enums.UserRole;

import com.liferay.portal.kernel.cache.MultiVMPoolUtil;
import com.liferay.portal.kernel.cache.SingleVMPoolUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.CompanyConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.service.persistence.UserUtil;

import ngn.kntc.utils.LiferayServiceUtil;
import ngn.kntc.utils.SessionUtil;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Page.Styles;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class ViewLogin extends VerticalLayout implements View{
	public static final String NAME = "dang-nhap";
	public static final String SUCCESS = "login-success.html";
	
	VerticalLayout boxLogin=new VerticalLayout();
	
	Panel pnlLogin=new Panel();
	VerticalLayout bodyLogin=new VerticalLayout();
	HorizontalLayout hTop=new HorizontalLayout();
	Image imgLogo=new Image();
	Label lblTitle=new Label("<div align='center' style='font-weight:bold;font-size: 20px;line-height: 30px;margin: 25px 0px;'><span style='color: #15a743;'>UBND TỈNH BẾN TRE</span><br><span style='color: red'>PHẦN MỀM QUẢN LÝ TIẾP CÔNG DÂN VÀ ĐƠN THƯ KHIẾU NẠI TỐ CÁO</span></div>",ContentMode.HTML);
	TextField txtUsername=new TextField("Tài khoản");
	PasswordField txtpassword=new PasswordField("Mật khẩu");
	HorizontalLayout hFoot=new HorizontalLayout();
	Button btnForget=new Button("Quên mật khẩu",FontAwesome.KEY);
	Button btnLogin=new Button("Đăng nhập",FontAwesome.SIGN_IN);
	
	Label lblFoot=new Label("<div align='center'><b style='color:red'>Được phát triển bởi NGN Corp</b></div>",ContentMode.HTML);
	
	
	public ViewLogin() {
		VerticalLayout layout=new VerticalLayout();
		layout.setSizeFull();
		
		this.setSizeFull();
		this.addComponent(layout);
		
		layout.addComponent(boxLogin);
		layout.setComponentAlignment(boxLogin, Alignment.MIDDLE_CENTER);
		
		pnlLogin.setContent(bodyLogin);
		pnlLogin.setWidth("500px");
		pnlLogin.addStyleName("panel_login");
		
		bodyLogin.addComponent(hTop);
		bodyLogin.addComponent(txtUsername);
		bodyLogin.addComponent(txtpassword);
		bodyLogin.addComponent(hFoot);
		bodyLogin.addStyleName("body_login");
		
		imgLogo.setSource(new ThemeResource("images/logo.png"));
		imgLogo.setHeight(80, Unit.PIXELS);
		lblTitle.setHeight(100, Unit.PIXELS);
		hTop.setWidth(100, Unit.PERCENTAGE);
		hTop.addComponents(imgLogo,lblTitle);
		hTop.setExpandRatio(lblTitle, 1.0f);
		
		txtUsername.setIcon(FontAwesome.USER);
		txtUsername.setWidth(100, Unit.PERCENTAGE);
		txtUsername.addStyleName(ValoTheme.TEXTFIELD_LARGE);
		txtUsername.setInputPrompt("Nhập tài khoản");
		txtUsername.focus();
		txtUsername.setRequiredError("Tài khoản không được để trống");
		
		txtpassword.setIcon(FontAwesome.LOCK);
		txtpassword.setWidth(100, Unit.PERCENTAGE);
		txtpassword.addStyleName(ValoTheme.TEXTFIELD_LARGE);
		txtpassword.setInputPrompt("Nhập mật khẩu");
		txtpassword.setRequiredError("Mật khẩu không được để trống");
		
		btnForget.addStyleName(ValoTheme.BUTTON_LINK);
		btnLogin.addStyleName(ValoTheme.BUTTON_PRIMARY);
		hFoot.setWidth(100, Unit.PERCENTAGE);
		hFoot.addComponents(btnLogin);
	//	hFoot.setComponentAlignment(btnForget, Alignment.MIDDLE_LEFT);
		hFoot.setComponentAlignment(btnLogin, Alignment.MIDDLE_RIGHT);
		
		bodyLogin.setSpacing(true);
		bodyLogin.setMargin(true);
		bodyLogin.setWidth(100, Unit.PERCENTAGE);
		
		boxLogin.setWidth(500, Unit.PIXELS);
		boxLogin.addComponents(pnlLogin,lblFoot);
		boxLogin.setComponentAlignment(lblFoot, Alignment.MIDDLE_CENTER);
		boxLogin.setSpacing(true);
		
		layout.addStyleName("layout_login");
		Styles style=Page.getCurrent().getStyles();
		style.add(".layout_login{background: url(VAADIN/themes/"+UI.getCurrent().getTheme()+"/images/bg_login.jpg);} .panel_login{box-shadow: 10px 10px 20px 2px #51807d !important;} .body_login{padding:20px !important; color: #15a743;}");
		
		configComponent();
	}
	
	private void configComponent(){
		btnLogin.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				doLogin();
			}
		});
		
		btnLogin.setClickShortcut(KeyCode.ENTER);
	}
	
	private void doLogin(){
		if(isLogin()){
			kntcUI.getCurrent().getPage().reload();
		}
	}
	
	private boolean isLogin(){
		if(txtUsername.getValue().trim().isEmpty()){
			txtUsername.setRequired(true);
			return false;
		}
		if(txtpassword.getValue().trim().isEmpty()){
			txtpassword.setRequired(true);
			return false;
		}
		User user = null;
		long companyId = SessionUtil.getCompanyid();
		String authType = CompanyConstants.AUTH_TYPE_SN;
		String username = txtUsername.getValue();
		String password = txtpassword.getValue();
		long userId = 0;
		try {
			MultiVMPoolUtil.clear();
			SingleVMPoolUtil.clear();
			UserUtil.clearCache();
			userId = UserLocalServiceUtil.authenticateForBasic(companyId, authType, username, password);
			user = UserLocalServiceUtil.getUser(userId);
		} catch (PortalException e) {
			System.out.println("- Tài khoản hoặc mật khẩu không đúng");
		} catch (SystemException e) {
			System.out.println("- Tài khoản hoặc mật khẩu không đúng");
		}
		
		if(user!=null){
			try {
				SessionUtil.setUserId(userId);
				SessionUtil.setUser(user);
				SessionUtil.setOrgId(user.getOrganizations().get(0).getOrganizationId());
				SessionUtil.setMasterOrgId(LiferayServiceUtil.getMasterOrg(SessionUtil.getOrgId()));
				SessionUtil.setLienThongOrgId(LiferayServiceUtil.getOrgCustomFieldValue(SessionUtil.getOrgId(), "OrgLienThongId"));
				if(LiferayServiceUtil.isUserHasRole(userId, UserRole.LANHDAO.getName()))
					SessionUtil.setRoleLanhDao();
				if(LiferayServiceUtil.isUserHasRole(userId, UserRole.TIEPCONGDAN.getName()))
					SessionUtil.setRoleTCD();
				if(LiferayServiceUtil.isUserHasRole(userId, UserRole.TRUONGPHONG.getName()))
					SessionUtil.setRoleTruongPhong();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}else{
			Notification alert=new Notification("Tài khoản hoặc mật khẩu không đúng",Type.TRAY_NOTIFICATION);
			alert.setStyleName(ValoTheme.NOTIFICATION_ERROR);
			alert.show(UI.getCurrent().getPage());
			return false;
		}
	}


	@Override
	public void enter(ViewChangeEvent event) {
		doLogin();
	}
}
