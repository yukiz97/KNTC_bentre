package ngn.kntc.views.tiepnhan;


import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

import ngn.kntc.page.donthu.create.TaoDonThuDeclare;
import ngn.kntc.page.donthu.create.TaoDonThuInput;

public class ViewTiepCongDan extends VerticalLayout implements View{
	public static final String NAME = "tiep-cong-dan";
	@Override
	public void enter(ViewChangeEvent event) {
		TaoDonThuInput donThu = new TaoDonThuInput();
		donThu.getBtnTiepNhanDonGianTiep().setVisible(false);
		this.addComponent(donThu, 0);
	}
}
