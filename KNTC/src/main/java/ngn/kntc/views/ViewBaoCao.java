package ngn.kntc.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

import ngn.kntc.page.baocao.LapBaoCaoEvents;

@SuppressWarnings("serial")
public class ViewBaoCao extends VerticalLayout implements View{
	public static final String NAME = "bao-cao-thong-ke";
	@Override
	public void enter(ViewChangeEvent event) {
		this.addComponent(new LapBaoCaoEvents());
	}
}
