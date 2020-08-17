package ngn.kntc.layout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.utils.IOUtils;

import ngn.kntc.UI.kntcUI;
import ngn.kntc.account.WindowHosocanhan;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;
import ngn.kntc.utils.SessionUtil;
import ngn.kntc.utils.UploadServiceUtil;
import ngn.kntc.views.ViewHome;

import com.liferay.portal.service.UserLocalServiceUtil;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.ResourceReference;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Window;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

public class FormHead extends AbsoluteLayout {
	private Image menuLogo = new Image(null, new ThemeResource("images/logo.png"));
	private Label menuTitle = new Label("QUẢN LÝ TIẾP CÔNG DÂN VÀ ĐƠN THƯ KHIẾU NẠI TỐ CÁO");
	private MenuBar menuBar = new MenuBar();
	private MenuItem menuItemHome;
	private MenuItem menuItemHelp;
	private MenuItem menuItemNotifiDenHan;
	private MenuItem menuItemNotification;
	private MenuItem menuItemAccount;

	public FormHead() {
		setSizeFull();
		buildLayout();
	}

	public void buildLayout() {
		this.addStyleName("menuArea");

		this.addComponent(menuLogo, "top:3px; left:5px");
		this.menuLogo.setHeight(38, Unit.PIXELS);
		this.menuLogo.addStyleName("animated zoomIn");

		this.addComponent(menuTitle, "top:8px; left:50px");
		this.menuTitle.setPrimaryStyleName("menuTitle");
		this.menuTitle.addStyleName("animated bounceInDown");

		try {
			this.addComponent(buildOptionTime(), "top:8px; right:3px");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Component buildOptionTime() throws Exception{
		this.menuBar.setCaptionAsHtml(true);
		this.menuBar.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
		this.menuBar.addStyleName(ValoTheme.MENUBAR_SMALL);

		/*Menu Notification*/
		menuItemNotifiDenHan=menuBar.addItem("12",FontAwesome.BELL_O, new Command() {
			@Override
			public void menuSelected(MenuItem selectedItem) {
				if(kntcUI.getCurrent().getRoot().getSliderThongbaoDenHan().isExpanded()){
					kntcUI.getCurrent().getRoot().getSliderThongbaoDenHan().collapse();
				}else{
					kntcUI.getCurrent().getRoot().getSliderThongbaoDenHan().expand();
					if(kntcUI.getCurrent().getRoot().getSliderThongbao().isExpanded())
					{
						kntcUI.getCurrent().getRoot().getSliderThongbao().collapse();
					}
				}
			}
		});
		menuItemNotifiDenHan.setStyleName("chitiet-thongbao-button-notifidenhan");
		menuItemNotifiDenHan.setDescription("Thông báo đến hạn");

		/*Menu Notification*/
		menuItemNotification=menuBar.addItem("12",FontAwesome.BELL_O, new Command() {
			@Override
			public void menuSelected(MenuItem selectedItem) {
				if(kntcUI.getCurrent().getRoot().getSliderThongbao().isExpanded()){
					kntcUI.getCurrent().getRoot().getSliderThongbao().collapse();
				}else{
					kntcUI.getCurrent().getRoot().getSliderThongbao().expand();
					if(kntcUI.getCurrent().getRoot().getSliderThongbaoDenHan().isExpanded()){
						kntcUI.getCurrent().getRoot().getSliderThongbaoDenHan().collapse();
					}
				}
			}
		});
		menuItemNotification.setText(QuaTrinhXuLyGiaiQuyetServiceUtil.countQuaTrinhXuLyAllAlert()+"");
		menuItemNotification.setStyleName("chitiet-thongbao-button-notifi");
		menuItemNotification.setDescription("Thông báo quá trình xử lý, giải quyết mới");

		/*Menu Home*/
		menuItemHome=menuBar.addItem("Trang chủ",FontAwesome.HOME, new Command() {
			@Override
			public void menuSelected(MenuItem selectedItem) {
				UI.getCurrent().getNavigator().navigateTo(ViewHome.NAME);
			}
		});

		menuItemHome.setDescription("Về trang chủ");

		//Menu Help
		menuItemHelp = this.menuBar.addItem("Hướng dẫn",FontAwesome.QUESTION_CIRCLE, new Command() {
			@Override
			public void menuSelected(MenuItem selectedItem) {
/*				try {
					FileInputStream fileInputStream = new FileInputStream(new File(UploadServiceUtil.getAbsolutePath()+UploadServiceUtil.getPathHDSD()+"HDSD.pdf"));
					byte[] bytes = IOUtils.toByteArray(fileInputStream);
					System.out.println(UploadServiceUtil.getAbsolutePath()+UploadServiceUtil.getPathHDSD()+"HDSD.pdf");
					StreamSource source = new StreamResource.StreamSource() {
						@Override
						public InputStream getStream() {
							try {
								InputStream inputStream = new ByteArrayInputStream(bytes);
								return inputStream;
							} catch (Exception e) {
								e.printStackTrace();
								return null;
							}
						}
					};
					StreamResource resource = new StreamResource(source,"Hướng dẫn sử dụng phần mềm KNTC.pdf");

					Page.getCurrent().open(resource,"Hướng dẫn sử dụng phần mềm KNTC",true);
				} catch (IOException e) {
					e.printStackTrace();
				}*/
				
				try {
					Resource source = new StreamResource(new StreamSource() {

						@Override
						public InputStream getStream() {
							FileInputStream fileInputStream = null;
							try {
								fileInputStream = new FileInputStream(new File(UploadServiceUtil.getAbsolutePath()+UploadServiceUtil.getPathHDSD()+"HDSD.pdf"));
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							}

							return fileInputStream;
						}
					}, "Hướng dẫn sử dụng KNTC.pdf");
			
					Window window = new Window();
					window.setWidth("90%");
					window.setHeight("90%");
					window.setCaption("Hướng dẫn sử dụng");
					BrowserFrame e = new BrowserFrame("PDF File",source);
					//BrowserFrame e = new BrowserFrame("PDF File",new ExternalResource("http://demo.vaadin.com/sampler/"));
					e.setWidth("100%");
					e.setHeight("100%");
					window.setContent(e);
					window.center();
					window.setModal(true);
					
					UI.getCurrent().addWindow(window);
					
					//window.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		menuItemHelp.setDescription("Hướng dẫn sử dụng");
		/*Menu Account*/
		menuItemAccount=menuBar.addItem(UserLocalServiceUtil.getUser(SessionUtil.getUserId()).getFirstName(), FontAwesome.USER, null);
		//		menuItemAccount.setText(SessionUtil.getUser().getFullName());
		/*menuItemAccount.addItem("Hồ sơ cá nhân", FontAwesome.FILE_TEXT_O, new Command() {
			@Override
			public void menuSelected(MenuItem selectedItem) {
				//WindowHosocanhan wd=new WindowHosocanhan(123,WindowHosocanhan.VIEW);
				//UI.getCurrent().addWindow(wd);
			}
		});
		menuItemAccount.addSeparator();*/
		menuItemAccount.addItem("Hồ sơ cá nhân", FontAwesome.USER, new Command() {
			@Override
			public void menuSelected(MenuItem selectedItem) {
				WindowHosocanhan wd = new WindowHosocanhan(SessionUtil.getUser(), WindowHosocanhan.EDIT);
				UI.getCurrent().addWindow(wd);
			}
		});
		menuItemAccount.addItem("Đăng xuất", FontAwesome.SIGN_OUT, new Command() {
			@Override
			public void menuSelected(MenuItem selectedItem) {
				SessionUtil.logout();
				kntcUI.getCurrent().close();
				Page.getCurrent().reload();
				//kntcUI.getCurrent().login();
			}
		});

		this.menuBar.setAutoOpen(true);
		this.addComponent(menuBar);
		this.menuBar.addStyleName("menuOptionTime");
		return this.menuBar;
	}

	public MenuItem getMenuItemNotification() {
		return menuItemNotification;
	}

	public MenuItem getMenuItemNotifiDenHan() {
		return menuItemNotifiDenHan;
	}

	public void setMenuItemNotifiDenHan(MenuItem menuItemNotifiDenHan) {
		this.menuItemNotifiDenHan = menuItemNotifiDenHan;
	}
}
