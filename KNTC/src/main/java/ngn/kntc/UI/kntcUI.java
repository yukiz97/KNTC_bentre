package ngn.kntc.UI;

import javax.servlet.annotation.WebServlet;

import ngn.kntc.enums.LoaiQuanLy;
import ngn.kntc.enums.LoaiTinhTrangDonThuEnum;
import ngn.kntc.layout.FormHead;
import ngn.kntc.layout.FormMenu;
import ngn.kntc.layout.FormSubViewDisplay;
import ngn.kntc.layout.PageLayout;
import ngn.kntc.page.donthu.create.SubmenuTaoDonThu;
import ngn.kntc.page.vanbanquypham.SubmenuVanBan;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.NotificationUtil;
import ngn.kntc.utils.SessionUtil;
import ngn.kntc.utils.TiepCongDanServiceUtil;
import ngn.kntc.views.ViewHome;
import ngn.kntc.views.ViewLogin;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page.UriFragmentChangedEvent;
import com.vaadin.server.Page.UriFragmentChangedListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

@Title("Khiếu nại tố cáo")
@SuppressWarnings("serial")
@Theme("kntc")
@Widgetset("ngn.kntc.UI.widgetset.KntcWidgetset")
@StyleSheet({"vaadin.css","animate.min.css","headingstyle.css","home.css","vanbanquypham.css","donthu.css","sotiepcongdan.css","baocao.css","tracuus.css","profile.css","newitem.css"})
public class kntcUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = kntcUI.class)
	public static class Servlet extends VaadinServlet {
	}

	private Navigator navigator;
	private PageLayout root;
	private ComponentContainer viewDisplay;
	private FormHead formHead;
	private FormMenu formMenu;
	
	private SubmenuVanBan submenuVanBan;
	private SubmenuTaoDonThu submenuTaoDonThu;
	
	
	/*private TaskManagerMenu taskManagerMenu;
	private DocMenu docMenu;
	private SearchMenu searchMenu;*/
	private FormSubViewDisplay subViewDisplay = new FormSubViewDisplay();
	
	@Override
	protected void init(VaadinRequest request) {
		if(SessionUtil.getUserId()==-1){
			login();
		}else{
			root=new PageLayout();
			viewDisplay=root.getContentContainer();
			navigator=new Navigator(this, viewDisplay);
			
			formHead=new FormHead();
			formMenu=new FormMenu();
			root.setHead(formHead);
			root.setMenu(formMenu);
			setContent(root);
			setPrimaryStyleName("v-ui");
			setSizeFull();
			try {
				formHead.getMenuItemNotifiDenHan().setText(String.valueOf(root.getLayoutNotifiHetHan().getCount()));
				
				setMenuCountValue(LoaiQuanLy.donvi.getType());
			} catch (Exception e) {
				e.printStackTrace();
			}
			initNavigatorSubMenu();
		}
		
		UI.getCurrent().getPage().addUriFragmentChangedListener(new UriFragmentChangedListener() {
			@Override
			public void uriFragmentChanged(UriFragmentChangedEvent event) {
				if(SessionUtil.getUserId()==-1){
					login();
				}else{
					
				}
			}
		});
	}
	
	public void login(){
		SessionUtil.init();
		navigator=new Navigator(this, this);
		navigator.addView(ViewLogin.NAME, ViewLogin.class);
		if(!navigator.getState().equalsIgnoreCase(ViewLogin.NAME)){
			navigator.navigateTo(ViewLogin.NAME);
		} 
	}
	
	public static kntcUI getCurrent(){
		return (kntcUI)UI.getCurrent();
	}
	
	private void initNavigatorSubMenu(){
		submenuVanBan = new SubmenuVanBan();
		submenuVanBan.buildMenu();
		submenuTaoDonThu = new SubmenuTaoDonThu();
		submenuTaoDonThu.buildMenu();
	}
	
	public void loadData(){
		/*Get current name of view at moment*/
		String currentNameView=getNavigator().getState();
		
		if(currentNameView.equalsIgnoreCase(ViewHome.NAME)){
			ViewHome view=(ViewHome) getNavigator().getCurrentView();
		}
	}
	
	public void setMenuCountValue(int type) throws Exception
	{
		if(type==LoaiQuanLy.donvi.getType())
		{
			int countDaTiepNhan = DonThuServiceUtil.countDonThuOnTypeGeneral(LoaiTinhTrangDonThuEnum.datiepnhan.getType());
			int countDvKhacChuyenDen = DonThuServiceUtil.countDonThuOnTypeGeneral(LoaiTinhTrangDonThuEnum.donvikhacchuyenden.getType());
			int countDaCoKetQua = DonThuServiceUtil.countDonThuOnTypeGeneral(LoaiTinhTrangDonThuEnum.dacokq.getType());
			int countCanThuLy = DonThuServiceUtil.countDonThuOnTypeGeneral(LoaiTinhTrangDonThuEnum.canthuly.getType());
			int countDaThuLy = DonThuServiceUtil.countDonThuOnTypeGeneral(LoaiTinhTrangDonThuEnum.dathuly.getType());
			int countCanGiaiQuyet = DonThuServiceUtil.countDonThuOnTypeGeneral(LoaiTinhTrangDonThuEnum.cangiaiquyet.getType());
			int countDaGiaiQuyet = DonThuServiceUtil.countDonThuOnTypeGeneral(LoaiTinhTrangDonThuEnum.dangthihanh.getType());
			int countKetThucHoSo = DonThuServiceUtil.countDonThuOnTypeGeneral(LoaiTinhTrangDonThuEnum.daketthuc.getType());
			int countVuViecPhucTap = DonThuServiceUtil.countDonThuOnTypeGeneral(LoaiTinhTrangDonThuEnum.phuctapkeodai.getType());
			int countDonDuocRut = DonThuServiceUtil.countDonThuOnTypeGeneral(LoaiTinhTrangDonThuEnum.donduocrut.getType());
			int countChuyenDonChuaGQ = DonThuServiceUtil.countDonThuOnTypeGeneral(LoaiTinhTrangDonThuEnum.chuyendonchuagq.getType());
			int countChuyenDonDaGQ = DonThuServiceUtil.countDonThuOnTypeGeneral(LoaiTinhTrangDonThuEnum.chuyendondagq.getType());
			int countChuyenDonLuuTru = DonThuServiceUtil.countDonThuOnTypeGeneral(LoaiTinhTrangDonThuEnum.chuyendonluutru.getType());
			
			NotificationUtil.setNotification(NotificationUtil.Id.sumtcd, TiepCongDanServiceUtil.countTiepCongDan());
			NotificationUtil.setNotification(NotificationUtil.Id.tiepcongdan, TiepCongDanServiceUtil.countTiepCongDan());
			NotificationUtil.setNotification(NotificationUtil.Id.sumxl, countDaTiepNhan+countDvKhacChuyenDen+countDaCoKetQua);
			NotificationUtil.setNotification(NotificationUtil.Id.datiepnhan, countDaTiepNhan);
			NotificationUtil.setNotification(NotificationUtil.Id.donvikhacchuyenden, countDvKhacChuyenDen);
			NotificationUtil.setNotification(NotificationUtil.Id.dacokq,countDaCoKetQua);
			NotificationUtil.setNotification(NotificationUtil.Id.sumtl, countCanThuLy+countDaThuLy);
			NotificationUtil.setNotification(NotificationUtil.Id.canthuly,countCanThuLy);
			NotificationUtil.setNotification(NotificationUtil.Id.dathuly, countDaThuLy);
			NotificationUtil.setNotification(NotificationUtil.Id.sumgq, countCanGiaiQuyet+countDaGiaiQuyet);
			NotificationUtil.setNotification(NotificationUtil.Id.cangiaiquyet, countCanGiaiQuyet);
			NotificationUtil.setNotification(NotificationUtil.Id.dangthihanh, countDaGiaiQuyet);
			NotificationUtil.setNotification(NotificationUtil.Id.sumketthuc, countKetThucHoSo+countVuViecPhucTap+countDonDuocRut);
			NotificationUtil.setNotification(NotificationUtil.Id.daketthuc, countKetThucHoSo);
			NotificationUtil.setNotification(NotificationUtil.Id.phuctapkeodai, countVuViecPhucTap);
			NotificationUtil.setNotification(NotificationUtil.Id.donduocrut, countDonDuocRut);
			NotificationUtil.setNotification(NotificationUtil.Id.sumchuyen, countChuyenDonChuaGQ+countChuyenDonDaGQ+countChuyenDonLuuTru);
			NotificationUtil.setNotification(NotificationUtil.Id.chuyenchuagq, countChuyenDonChuaGQ);
			NotificationUtil.setNotification(NotificationUtil.Id.chuyendagq, countChuyenDonDaGQ);
			NotificationUtil.setNotification(NotificationUtil.Id.chuyenluutru, countChuyenDonLuuTru);
		}
		else if(type==LoaiQuanLy.thammuu.getType())
		{
			int countThamMuuChuaGQ = DonThuServiceUtil.countDonThuOnTypeGeneral(LoaiTinhTrangDonThuEnum.thammuuchuagiaiquyet.getType());
			int countThamMuuDaGQ = DonThuServiceUtil.countDonThuOnTypeGeneral(LoaiTinhTrangDonThuEnum.thammuudagiaiquyet.getType());

			NotificationUtil.setNotification(NotificationUtil.Id.sumthammuu, countThamMuuChuaGQ+countThamMuuDaGQ);
			NotificationUtil.setNotification(NotificationUtil.Id.thammuuchuagq, countThamMuuChuaGQ);
			NotificationUtil.setNotification(NotificationUtil.Id.thammuudagq, countThamMuuDaGQ);
		}
	}

	public Navigator getNavigator() {
		return navigator;
	}
	
	public PageLayout getRoot() {
		return root;
	}

	public FormSubViewDisplay getSubViewDisplay() {
		return subViewDisplay;
	}

	public ComponentContainer getViewDisplay() {
		return viewDisplay;
	}

	public FormHead getFormHead() {
		return formHead;
	}
	
	public FormMenu getFormMenu() {
		return formMenu;
	}

	public SubmenuVanBan getSubmenuVanBan() {
		return submenuVanBan;
	}

	public void setSubmenuVanBan(SubmenuVanBan submenuVanBan) {
		this.submenuVanBan = submenuVanBan;
	}
}