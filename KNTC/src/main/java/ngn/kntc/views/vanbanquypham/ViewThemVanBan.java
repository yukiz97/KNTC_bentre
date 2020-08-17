package ngn.kntc.views.vanbanquypham;

import ngn.kntc.UI.kntcUI;
import ngn.kntc.page.vanbanquypham.ThemVanBanLayout;
import ngn.kntc.page.vanbanquypham.ViewVanBanGeneral;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

@SuppressWarnings("serial")
public class ViewThemVanBan extends ViewVanBanGeneral implements View{
	public static final String NAME = "vb-them-van-ban";
	ThemVanBanLayout layout = new ThemVanBanLayout(-1);
	
	public ViewThemVanBan() {
		super();
		kntcUI.getCurrent().getSubViewDisplay().addComponent(layout);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
	}
}
