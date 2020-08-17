package ngn.kntc.layout;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import ngn.kntc.UI.kntcUI;
import ngn.kntc.enums.LoaiQuanLy;
import ngn.kntc.enums.ThamQuyenGiaiQuyetEnum;
import ngn.kntc.lienthongttcp.LienThongOldData;
import ngn.kntc.utils.NotificationUtil;
import ngn.kntc.views.ViewBaoCao;
import ngn.kntc.views.ViewError;
import ngn.kntc.views.ViewHome;
import ngn.kntc.views.ViewLogin;
import ngn.kntc.views.ViewTinhTrangLienThong;
import ngn.kntc.views.ViewTraCuu;
import ngn.kntc.views.giaiquyet.ViewDonCanGiaiQuyet;
import ngn.kntc.views.giaiquyet.ViewDonDangThiHanhGiaiQuyet;
import ngn.kntc.views.luutruhoso.ViewDonDaKetThuc;
import ngn.kntc.views.luutruhoso.ViewDonDuocRut;
import ngn.kntc.views.luutruhoso.ViewDonPhucTapKeoDai;
import ngn.kntc.views.thammuu.ViewThamMuuChuaGiaiQuyet;
import ngn.kntc.views.thammuu.ViewThamMuuDaGiaiQuyet;
import ngn.kntc.views.theodoichuyendon.ViewDonDaChuyenDi;
import ngn.kntc.views.theodoichuyendon.ViewDonDaChuyenDiDaGQ;
import ngn.kntc.views.theodoichuyendon.ViewDonDaChuyenDiLuuTru;
import ngn.kntc.views.thuly.ViewDonCanThuLy;
import ngn.kntc.views.thuly.ViewDonDaThuLy;
import ngn.kntc.views.tiepnhan.ViewSoTiepCongDan;
import ngn.kntc.views.tiepnhan.ViewTiepCongDan;
import ngn.kntc.views.tiepnhan.ViewTiepNhanGianTiep;
import ngn.kntc.views.vanbanquypham.ViewDanhSachVanBan;
import ngn.kntc.views.xulydon.ViewXuLyDonDaCoKetQua;
import ngn.kntc.views.xulydon.ViewXuLyDonDaTiepNhan;
import ngn.kntc.views.xulydon.ViewXuLyDonNhanTuDonViKhac;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class FormMenu extends CssLayout {
	private String danhmuc="  Danh mục";
	private CssLayout topMenuLayout = new CssLayout();
	private AbsoluteLayout menuItemsLayout = new AbsoluteLayout();
	private OptionGroup ogChonLoai = new OptionGroup();
	private LinkedHashMap<String, String> menuItems = new LinkedHashMap<String, String>();
	private String current="home";

	private VerticalLayout vDonVi = new VerticalLayout();
	private VerticalLayout vThamMuu = new VerticalLayout();

	public FormMenu() {
		buildLayout();
		buildTop();
		buildMenu();
		buildNavigator(UI.getCurrent().getNavigator());

		configComponent();
		
		Page.getCurrent().getStyles().add(".leftmenu-button-archive{background: #feffcd;}");
	}

	private void buildLayout() {
		this.addComponent(topMenuLayout);
		this.addComponent(menuItemsLayout);
	}

	private void buildTop(){
		Label title = new Label(FontAwesome.REORDER.getHtml()+danhmuc,ContentMode.HTML);
		title.addStyleName("title-danhmuc");
		topMenuLayout.addComponent(title);
		topMenuLayout.setWidth("100%");
	}

	private void buildMenu() {
		JavaScript.getCurrent().execute("");
		/*Put nativagor to list manager*/
		menuItems.put(ViewHome.NAME, FontAwesome.HOME.getHtml()+" Tổng quan");
		menuItems.put(ViewTiepCongDan.NAME, FontAwesome.PLUS_CIRCLE.getHtml()+" Tiếp công dân");
		menuItems.put(ViewTiepNhanGianTiep.NAME, FontAwesome.PLUS_CIRCLE.getHtml()+" Tiếp nhận đơn gián tiếp");
		menuItems.put(ViewSoTiepCongDan.NAME, FontAwesome.BOOK.getHtml()+" Sổ tiếp công dân");
		menuItems.put(ViewXuLyDonDaTiepNhan.NAME, FontAwesome.STAR_O.getHtml()+" Đơn đã tiếp nhận");
		menuItems.put(ViewXuLyDonNhanTuDonViKhac.NAME, FontAwesome.STAR_O.getHtml()+" Đơn nhận từ đơn vị khác");
		menuItems.put(ViewXuLyDonDaCoKetQua.NAME, FontAwesome.ARCHIVE.getHtml()+" Lưu trữ đơn đã có kết quả");
		menuItems.put(ViewDonCanThuLy.NAME, FontAwesome.STAR_O.getHtml()+" Đơn cần thụ lý");
		menuItems.put(ViewDonDaThuLy.NAME, FontAwesome.ARCHIVE.getHtml()+" Lưu trữ đơn đã thụ lý");
		menuItems.put(ViewDonCanGiaiQuyet.NAME, FontAwesome.STAR_O.getHtml()+" Đơn cần giải quyết");
		menuItems.put(ViewDonDangThiHanhGiaiQuyet.NAME, FontAwesome.STAR_O.getHtml()+" Đơn đang thi hành");
		menuItems.put(ViewDonDaKetThuc.NAME, FontAwesome.ARCHIVE.getHtml()+" Đơn đã kết thúc");
		menuItems.put(ViewDonPhucTapKeoDai.NAME, FontAwesome.STAR_O.getHtml()+" Đơn phức tạp, kéo dài");
		menuItems.put(ViewDonDuocRut.NAME, FontAwesome.STAR_O.getHtml()+" Đơn được thu hồi");
		menuItems.put(ViewDonDaChuyenDi.NAME, FontAwesome.STAR_O.getHtml()+" Đơn đã chuyển");
		//menuItems.put(ViewDonDaChuyenDiDaGQ.NAME, FontAwesome.STAR_O.getHtml()+" Đơn đã giải quyết");
		menuItems.put(ViewDonDaChuyenDiLuuTru.NAME, FontAwesome.STAR_O.getHtml()+" Đơn lưu trữ");
		menuItems.put(ViewBaoCao.NAME, FontAwesome.FILE_PDF_O.getHtml()+" Báo cáo thống kê");
		menuItems.put(ViewTraCuu.NAME, FontAwesome.SEARCH.getHtml()+" Tra cứu đơn thư");
		menuItems.put(ViewDanhSachVanBan.NAME, FontAwesome.FILE_TEXT.getHtml()+" Văn bản quy phạm");
		menuItems.put(ViewTinhTrangLienThong.NAME, FontAwesome.UPLOAD.getHtml()+" Tình trạng liên thông");
		
		menuItems.put(ViewThamMuuChuaGiaiQuyet.NAME, FontAwesome.STAR_O.getHtml()+" Chưa có quyết định");
		menuItems.put(ViewThamMuuDaGiaiQuyet.NAME, FontAwesome.STAR_O.getHtml()+" Đã có quyết định");

		ogChonLoai.addItem(LoaiQuanLy.donvi.getType());
		ogChonLoai.setItemCaption(LoaiQuanLy.donvi.getType(), "Đơn vị");
		ogChonLoai.addItem(LoaiQuanLy.thammuu.getType());
		ogChonLoai.setItemCaption(LoaiQuanLy.thammuu.getType(), "Tham mưu");

		ogChonLoai.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
		ogChonLoai.addStyleName("og-chonloai");

		ogChonLoai.select(1);

		menuItemsLayout.addComponent(ogChonLoai);
		menuItemsLayout.addComponent(vDonVi);

		 
		Button btnTest = new Button("Test");
		//menuItemsLayout.addComponent(btnTest);
		btnTest.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					LienThongOldData.lienThongDonThu();
					//LienThongOldData.lienThongKetQuaXuLy();
					//LienThongOldData.lienThongQuyetDinhThuLy();
					//LienThongOldData.lienThongQuyetDinhGiaiQuyet();
					//LienThongOldData.lienThongThiHanhGiaiQuyet();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		String key=null;
		// Tổng quan
		key=ViewHome.NAME;
		vDonVi.addComponent(createButton(key,menuItems.get(key)));

		// Tiếp nhận đơn
		vDonVi.addComponent(createLabelTitleMenu("Tiếp nhận đơn", NotificationUtil.Id.sumtcd, 0));
		key=ViewTiepCongDan.NAME;
		vDonVi.addComponent(createSubButton(key,menuItems.get(key)));
		key=ViewTiepNhanGianTiep.NAME;
		vDonVi.addComponent(createSubButton(key,menuItems.get(key)));
		key=ViewSoTiepCongDan.NAME;
		vDonVi.addComponent(createSubButton(key,menuItems.get(key),NotificationUtil.Id.tiepcongdan,0));

		// Xử lý đơn thư
		vDonVi.addComponent(createLabelTitleMenu("Xử lý đơn thư", NotificationUtil.Id.sumxl, 0));
		key=ViewXuLyDonDaTiepNhan.NAME;
		vDonVi.addComponent(createSubButton(key,menuItems.get(key),NotificationUtil.Id.datiepnhan,0));
		key=ViewXuLyDonNhanTuDonViKhac.NAME;
		vDonVi.addComponent(createSubButton(key,menuItems.get(key),NotificationUtil.Id.donvikhacchuyenden,0));
		
		key=ViewXuLyDonDaCoKetQua.NAME;
		Button btnTmpDaXuLy = createSubButton(key,menuItems.get(key),NotificationUtil.Id.dacokq,0);
		btnTmpDaXuLy.addStyleName("leftmenu-button-archive");
		vDonVi.addComponent(btnTmpDaXuLy);

		// Thụ lý đơn thư
		vDonVi.addComponent(createLabelTitleMenu("Thụ lý đơn thư", NotificationUtil.Id.sumtl, 0));
		key=ViewDonCanThuLy.NAME;
		vDonVi.addComponent(createSubButton(key,menuItems.get(key),NotificationUtil.Id.canthuly,0));
		
		key=ViewDonDaThuLy.NAME;
		Button btnTmpDaThuLy = createSubButton(key,menuItems.get(key),NotificationUtil.Id.dathuly,0);
		btnTmpDaThuLy.addStyleName("leftmenu-button-archive");
		vDonVi.addComponent(btnTmpDaThuLy);

		// Giải quyết đơn thư
		vDonVi.addComponent(createLabelTitleMenu("Giải quyết đơn thư", NotificationUtil.Id.sumgq, 0));
		key=ViewDonCanGiaiQuyet.NAME;
		vDonVi.addComponent(createSubButton(key,menuItems.get(key),NotificationUtil.Id.cangiaiquyet,0));
		key=ViewDonDangThiHanhGiaiQuyet.NAME;
		vDonVi.addComponent(createSubButton(key,menuItems.get(key),NotificationUtil.Id.dangthihanh,0));

		// Kết thúc hồ sơ
		vDonVi.addComponent(createLabelTitleMenu("Hồ sơ đã kết thúc", NotificationUtil.Id.sumketthuc, 0));
		key=ViewDonPhucTapKeoDai.NAME;
		vDonVi.addComponent(createSubButton(key,menuItems.get(key),NotificationUtil.Id.phuctapkeodai,0));
		key=ViewDonDuocRut.NAME;
		vDonVi.addComponent(createSubButton(key,menuItems.get(key),NotificationUtil.Id.donduocrut,0));
		key=ViewDonDaKetThuc.NAME;
		Button btnTmpHoSoDaKetThuc = createSubButton(key,menuItems.get(key),NotificationUtil.Id.daketthuc,0);
		btnTmpHoSoDaKetThuc.addStyleName("leftmenu-button-archive");
		vDonVi.addComponent(btnTmpHoSoDaKetThuc);
		
		// Theo dõi chuyển đơn
		vDonVi.addComponent(createLabelTitleMenu("Theo dõi chuyển đơn", NotificationUtil.Id.sumchuyen, 0));
		key=ViewDonDaChuyenDi.NAME;
		vDonVi.addComponent(createSubButton(key,menuItems.get(key),NotificationUtil.Id.chuyenchuagq,0));
		//key=ViewDonDaChuyenDiDaGQ.NAME;
		//vDonVi.addComponent(createSubButton(key,menuItems.get(key),NotificationUtil.Id.chuyendagq,0));
		key=ViewDonDaChuyenDiLuuTru.NAME;
		vDonVi.addComponent(createSubButton(key,menuItems.get(key),NotificationUtil.Id.chuyenluutru,0));

		// Báo cáo
		key=ViewBaoCao.NAME;
		vDonVi.addComponent(createButton(key,menuItems.get(key)));

		// Tra cứu
		key=ViewTraCuu.NAME;
		vDonVi.addComponent(createButton(key,menuItems.get(key)));

		// Văn bản
		key=ViewDanhSachVanBan.NAME;
		vDonVi.addComponent(createButton(key,menuItems.get(key)));
		
		// Tình trạng liên thông
		key=ViewTinhTrangLienThong.NAME;
		vDonVi.addComponent(createButton(key,menuItems.get(key)));

		//tham mưu giải quyết
		// Tổng quan
		key=ViewHome.NAME;
		vThamMuu.addComponent(createButton(key,menuItems.get(key)));

		// tham mưu
		vThamMuu.addComponent(createLabelTitleMenu("Tham mưu giải quyết", NotificationUtil.Id.sumthammuu, 0));
		key=ViewThamMuuChuaGiaiQuyet.NAME;
		vThamMuu.addComponent(createSubButton(key,menuItems.get(key),NotificationUtil.Id.thammuuchuagq,0));
		key=ViewThamMuuDaGiaiQuyet.NAME;
		vThamMuu.addComponent(createSubButton(key,menuItems.get(key),NotificationUtil.Id.thammuudagq,0));

		// Báo cáo
		key=ViewBaoCao.NAME;
		vThamMuu.addComponent(createButton(key,menuItems.get(key)));

		// Tra cứu
		key=ViewTraCuu.NAME;
		vThamMuu.addComponent(createButton(key,menuItems.get(key)));

		// Văn bản
		key=ViewDanhSachVanBan.NAME;
		vThamMuu.addComponent(createButton(key,menuItems.get(key)));
		
		// Tình trạng liên thông
		key=ViewTinhTrangLienThong.NAME;
		vThamMuu.addComponent(createButton(key,menuItems.get(key)));

		menuItemsLayout.setPrimaryStyleName("valo-menuitems");

	}

	private void buildNavigator(Navigator navigator){
		navigator.addView("", ViewHome.class);
		navigator.addView(ViewHome.NAME, ViewHome.class);
		navigator.addView(ViewTiepCongDan.NAME, ViewTiepCongDan.class);
		navigator.addView(ViewTiepNhanGianTiep.NAME, ViewTiepNhanGianTiep.class);
		navigator.addView(ViewSoTiepCongDan.NAME, ViewSoTiepCongDan.class);
		navigator.addView(ViewXuLyDonDaTiepNhan.NAME, ViewXuLyDonDaTiepNhan.class);
		navigator.addView(ViewXuLyDonNhanTuDonViKhac.NAME, ViewXuLyDonNhanTuDonViKhac.class);
		navigator.addView(ViewXuLyDonDaCoKetQua.NAME, ViewXuLyDonDaCoKetQua.class);
		navigator.addView(ViewDonCanThuLy.NAME, ViewDonCanThuLy.class);
		navigator.addView(ViewDonDaThuLy.NAME, ViewDonDaThuLy.class);
		navigator.addView(ViewDonCanGiaiQuyet.NAME, ViewDonCanGiaiQuyet.class);
		navigator.addView(ViewDonDangThiHanhGiaiQuyet.NAME, ViewDonDangThiHanhGiaiQuyet.class);
		navigator.addView(ViewBaoCao.NAME, ViewBaoCao.class);
		navigator.addView(ViewTraCuu.NAME, ViewTraCuu.class);
		navigator.addView(ViewDanhSachVanBan.NAME, ViewDanhSachVanBan.class);
		navigator.addView(ViewDonDaChuyenDi.NAME, ViewDonDaChuyenDi.class);
		//navigator.addView(ViewDonDaChuyenDiDaGQ.NAME, ViewDonDaChuyenDiDaGQ.class);
		navigator.addView(ViewDonDaChuyenDiLuuTru.NAME, ViewDonDaChuyenDiLuuTru.class);
		navigator.addView(ViewDonDaKetThuc.NAME, ViewDonDaKetThuc.class);
		navigator.addView(ViewDonPhucTapKeoDai.NAME, ViewDonPhucTapKeoDai.class);
		navigator.addView(ViewDonDuocRut.NAME, ViewDonDuocRut.class);
		navigator.addView(ViewThamMuuChuaGiaiQuyet.NAME, ViewThamMuuChuaGiaiQuyet.class);
		navigator.addView(ViewThamMuuDaGiaiQuyet.NAME, ViewThamMuuDaGiaiQuyet.class);
		navigator.addView(ViewTinhTrangLienThong.NAME, ViewTinhTrangLienThong.class);

		if(navigator.getState().equalsIgnoreCase(ViewLogin.NAME)){
			navigator.navigateTo(ViewHome.NAME);
		}
		navigator.setErrorView(ViewError.class);

		navigator.addViewChangeListener(new ViewChangeListener() {
			@Override
			public boolean beforeViewChange(ViewChangeEvent event) {
				return true;
			}

			@Override
			public void afterViewChange(ViewChangeEvent event) {
				VerticalLayout vDisplay = null;
				if((int)ogChonLoai.getValue()==LoaiQuanLy.donvi.getType())
				{
					vDisplay = vDonVi;
				}
				else if((int)ogChonLoai.getValue()==LoaiQuanLy.thammuu.getType())
				{
					vDisplay = vThamMuu;
				}
				
				/*Remove class selected for all component*/
				for (Iterator<Component> it = vDisplay.iterator(); it.hasNext();) {
					it.next().removeStyleName("selected");
				}

				boolean flag=false;
				//Check viewName on browser to add class selected
				for (Entry<String, String> item : menuItems.entrySet()) {
					if (event.getViewName().equals(item.getKey())) {
						for (Iterator<Component> it = vDisplay.iterator(); it.hasNext();) {
							Component c = it.next();
							if (c.getId() != null&& c.getId().startsWith(item.getKey())) {
								c.addStyleName("selected");
								flag=true;
								break;
							}
						}
						break;
					}
				}

				//Exception, viewName with parameter
				if(!flag){
					for (Entry<String, String> item : menuItems.entrySet()) {
						if ((event.getViewName()+"/"+event.getParameters()).equals(item.getKey())) {
							for (Iterator<Component> it = vDisplay.iterator(); it.hasNext();) {
								Component c = it.next();
								System.out.println(c.getId());
								if (c.getId() != null&& c.getId().startsWith(item.getKey())) {
									c.addStyleName("selected");
									break;
								}
							}
						}
					}
				}

				removeStyleName("valo-menu-visible");
				actionPageChange();
			}
		});
	}

	private void actionPageChange(){

	}

	@SuppressWarnings("unused")
	private Label createLabelTitleMenu(String title){
		Label lbl = new Label(FontAwesome.INBOX.getHtml()+"&nbsp;&nbsp;"+title, ContentMode.HTML);
		lbl.setPrimaryStyleName(ValoTheme.MENU_SUBTITLE);
		lbl.addStyleName(ValoTheme.LABEL_H4);
		lbl.setSizeUndefined();
		return lbl;
	}

	private Label createLabelTitleMenu(String title, String idBaged, int count){
		Label lbl = new Label(FontAwesome.INBOX.getHtml()+"&nbsp;&nbsp;"+title, ContentMode.HTML);
		lbl.setValue(lbl.getValue()+ " <span class=\"valo-menu-badge\" id='"+idBaged+"'>" + 0 + "</span>");
		lbl.setPrimaryStyleName(ValoTheme.MENU_SUBTITLE);
		lbl.addStyleName(ValoTheme.LABEL_H4);
		lbl.setSizeUndefined();
		return lbl;
	}

	private Button createSubButton(String key, String caption, String idBaged, int count){
		Button btn = new Button(caption);
		btn.setDescription(caption);
		btn.setId(key);
		btn.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().getNavigator().navigateTo(key);
			}
		});
		btn.setCaption(btn.getCaption()+ " <span class=\"valo-menu-badge\" id='"+idBaged+"'>0</span>");
		btn.addStyleName("valo-menu-item-sub-caption");
		btn.setHtmlContentAllowed(true);
		btn.setPrimaryStyleName(ValoTheme.MENU_ITEM);
		return btn;
	}

	private Button createSubButton(String key, String caption){
		Button btn = new Button(caption);
		btn.setId(key);
		btn.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().getNavigator().navigateTo(key);
			}
		});
		btn.addStyleName("valo-menu-item-sub-caption");
		btn.setHtmlContentAllowed(true);
		btn.setPrimaryStyleName(ValoTheme.MENU_ITEM);
		return btn;
	}

	private Button createButton(String key, String caption){
		Button btn = new Button(caption);
		btn.setId(key);
		btn.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().getNavigator().navigateTo(key);
			}
		});
		btn.setHtmlContentAllowed(true);
		btn.setPrimaryStyleName(ValoTheme.MENU_ITEM);
		return btn;
	}

	@SuppressWarnings("unused")
	private Button createButton(String key, String caption, String idBaged, int count){
		Button btn = new Button(caption);
		btn.setId(key);
		btn.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().getNavigator().navigateTo(key);
			}
		});
		btn.setCaption(btn.getCaption()+ " <span class=\"valo-menu-badge\" id='"+idBaged+"'>0</span>");
		btn.setHtmlContentAllowed(true);
		btn.setPrimaryStyleName(ValoTheme.MENU_ITEM);
		return btn;
	}

	public String getCurrent() {
		return current;
	}

	public void setCurrent(String viewName) {
		this.current = viewName;
		this.setItemParentNavigatorSelect(viewName);
	}

	public void setMenuItems(LinkedHashMap<String, String> menuItems) {
		this.menuItems = menuItems;
	}

	public LinkedHashMap<String, String> getMenuItems() {
		return menuItems;
	}

	public void setItemParentNavigatorSelect(String viewName){
		for (Iterator<Component> it = menuItemsLayout.iterator(); it.hasNext();) {
			it.next().removeStyleName("selected");
		}

		for (Entry<String, String> item : menuItems.entrySet()) {
			if (viewName.equals(item.getKey())) {
				for (Iterator<Component> it = menuItemsLayout.iterator(); it.hasNext();) {
					Component c = it.next();
					if (c.getId() != null&& c.getId().startsWith(item.getKey())) {
						c.addStyleName("selected");
						break;
					}
				}
				break;
			}
		}
		removeStyleName("valo-menu-visible");
	}

	private void configComponent() {
		ogChonLoai.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				int type = (int) event.getProperty().getValue();
				UI.getCurrent().getNavigator().navigateTo(ViewHome.NAME);
				if(type==LoaiQuanLy.donvi.getType())
				{
					menuItemsLayout.removeComponent(vThamMuu);
					menuItemsLayout.addComponent(vDonVi);
					try {
						kntcUI.getCurrent().setMenuCountValue(LoaiQuanLy.donvi.getType());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else if(type==LoaiQuanLy.thammuu.getType())
				{
					menuItemsLayout.removeComponent(vDonVi);
					menuItemsLayout.addComponent(vThamMuu);
					try {
						kntcUI.getCurrent().setMenuCountValue(LoaiQuanLy.thammuu.getType());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	public OptionGroup getOgChonLoai() {
		return ogChonLoai;
	}
}
