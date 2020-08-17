package ngn.kntc.views.tiepnhan;


import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

import ngn.kntc.page.donthu.create.TaoDonThuDeclare;
import ngn.kntc.page.donthu.create.TaoDonThuInput;

public class ViewTiepNhanGianTiep extends VerticalLayout implements View{
	public static final String NAME = "tiep-nhan-don-gian-tiep";
	@Override
	public void enter(ViewChangeEvent event) {
		TaoDonThuInput donThu = new TaoDonThuInput();
		this.addComponent(donThu, 0);
		
		donThu.getBtnTiepNhanDonGianTiep().click();
		donThu.getBtnTiepCongDan().setVisible(false);
		donThu.getBtnLanhDaoTiep().setVisible(false);
	}
}
