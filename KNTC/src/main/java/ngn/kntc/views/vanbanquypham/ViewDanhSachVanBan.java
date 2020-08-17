package ngn.kntc.views.vanbanquypham;

import java.sql.SQLException;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

import ngn.kntc.UI.kntcUI;
import ngn.kntc.page.vanbanquypham.DanhSachVanBanLayout;
import ngn.kntc.page.vanbanquypham.ViewVanBanGeneral;

@SuppressWarnings("serial")
public class ViewDanhSachVanBan extends ViewVanBanGeneral implements View{
	public static final String NAME = "vb-danh-sach-van-ban";
	DanhSachVanBanLayout layout = new DanhSachVanBanLayout();
	
	public ViewDanhSachVanBan() throws SQLException {
		super();
		kntcUI.getCurrent().getSubViewDisplay().addComponent(layout);
		kntcUI.getCurrent().getFormMenu().setCurrent(ViewThemVanBan.NAME);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
	}
}
