package ngn.kntc.account;
import com.liferay.portal.model.User;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Window;

@StyleSheet("profile.css")
@SuppressWarnings("serial")
public class WindowHosocanhan extends Window {
	public static final String VIEW = "VIEW";
	public static final String EDIT = "EDIT";

	private TabSheet tabWindowHscn = new TabSheet();

	private MyAccount profile;
	private MyPassword changepassword;
	private MyOrganization myOrganization;
	private MyRole myRole;
	private MyCustomField myCustomField;
	
	public WindowHosocanhan(User user, String mode) {
		this.profile=new MyAccount(user);
		this.changepassword=new MyPassword(user);
		this.myOrganization=new MyOrganization(user);
		this.myRole=new MyRole(user);
		this.myCustomField=new MyCustomField(user);
		this.buildLayout();
	}

	private void buildLayout() {
		tabWindowHscn.addTab(profile, "Hồ sơ", FontAwesome.FILE_O);
		tabWindowHscn.addTab(changepassword, "Mật khẩu", FontAwesome.KEY);
		tabWindowHscn.addTab(myOrganization, "Tổ chức", FontAwesome.MAP_MARKER);
		tabWindowHscn.addTab(myRole, "Vai trò", FontAwesome.MAGIC);
		tabWindowHscn.addTab(myCustomField, "Các trường mở rộng", FontAwesome.PLUS_CIRCLE);
		
		this.center();
		this.setModal(true);
		this.setWidth(500, Unit.PIXELS);
		this.setHeightUndefined();
		this.setContent(tabWindowHscn);
		this.setWindowMode(WindowMode.NORMAL);
		this.setCaption("Hồ sơ cá nhân");
		this.setIcon(FontAwesome.FILE);
	}

	
	
}