package ngn.kntc.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

import ngn.kntc.page.baocao.LapBaoCaoEvents;
import ngn.kntc.page.home.TinhTrangLienThongLayout;

@SuppressWarnings("serial")
public class ViewTinhTrangLienThong extends VerticalLayout implements View{
	public static final String NAME = "tinh-trang-lien-thong";
	@Override
	public void enter(ViewChangeEvent event) {
		this.addComponent(new TinhTrangLienThongLayout());
	}
}
