package ngn.kntc.views.tiepnhan;

import java.sql.SQLException;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

import ngn.kntc.page.sotcd.SoTCDLayout;
import ngn.kntc.page.sotcd.SoTiepCongDanLayout;

public class ViewSoTiepCongDan extends VerticalLayout implements View{
	public static final String NAME = "so-tiep-cong-dan";
	@Override
	public void enter(ViewChangeEvent event) {
		try {
			this.addComponent(new SoTiepCongDanLayout());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
