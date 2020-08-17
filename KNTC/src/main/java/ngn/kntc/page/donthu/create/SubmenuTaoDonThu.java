package ngn.kntc.page.donthu.create;

import java.util.Iterator;
import java.util.Map.Entry;

import ngn.kntc.UI.kntcUI;
import ngn.kntc.layout.FormSubMenu;
import ngn.kntc.views.vanbanquypham.ViewDanhSachVanBan;
import ngn.kntc.views.vanbanquypham.ViewThemVanBan;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.themes.ValoTheme;

public class SubmenuTaoDonThu extends FormSubMenu{
	public SubmenuTaoDonThu() {
		buildNavigator();
	}
	public void buildMenu() {
		getSubMenuItems().clear();
		getSubMenuItems().put(ViewDanhSachVanBan.NAME, FontAwesome.BOOK.getHtml()+" <span>Tiếp công dân</span>");
		getSubMenuItems().put(ViewThemVanBan.NAME, FontAwesome.PLUS_CIRCLE.getHtml()+" <span>Thêm văn bản</span>");
		getSubMenuLayout().removeAllComponents();
		
		for (final Entry<String, String> item : getSubMenuItems().entrySet()) {
			Button btnItem = new Button(item.getValue());
			btnItem.addClickListener(new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                	kntcUI.getCurrent().getNavigator().navigateTo(item.getKey());
                }
            });
			btnItem.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			btnItem.setPrimaryStyleName("subMenuItem");
			btnItem.setHtmlContentAllowed(true);
			btnItem.setCaptionAsHtml(true);
			if(item.getKey().equalsIgnoreCase(ViewDanhSachVanBan.NAME)){
				btnItem.addStyleName("subMenuSelected");
			}
			getSubMenuLayout().addComponent(btnItem);
		}
	}
	
	private void buildNavigator(){
		kntcUI.getCurrent().getNavigator().addView(ViewDanhSachVanBan.NAME, ViewDanhSachVanBan.class);
		kntcUI.getCurrent().getNavigator().addView(ViewThemVanBan.NAME, ViewThemVanBan.class);
		
		kntcUI.getCurrent().getNavigator().addViewChangeListener(new ViewChangeListener() {
			@Override
			public boolean beforeViewChange(ViewChangeEvent event) {
				return true;
			}
			@Override
			public void afterViewChange(ViewChangeEvent event) {
				if (event.getViewName().equals(ViewDanhSachVanBan.NAME) || event.getViewName().equals(ViewThemVanBan.NAME)) {
					kntcUI.getCurrent().getFormMenu().setCurrent(ViewDanhSachVanBan.NAME);
				}
				
				for (Iterator<Component> it = getSubMenuLayout().iterator(); it.hasNext();) {
                    it.next().removeStyleName("subMenuSelected");
                }
                for (Entry<String, String> item : getSubMenuItems().entrySet()) {
                    if (event.getViewName().equals(item.getKey())) {
                        for (Iterator<Component> it = getSubMenuLayout().iterator(); it.hasNext();) {
                            Component c = it.next();
                            if (c.getCaption() != null&& c.getCaption().startsWith(item.getValue())) {
                                c.addStyleName("subMenuSelected");
                                break;
                            }
                        }
                        break;
                    }
                }
			}
		});
	}
}
