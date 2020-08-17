package ngn.kntc.views.giaiquyet;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

import ngn.kntc.page.donthu.danhsach.giaiquyetdon.LayoutDanhSachDonThuCanGiaiQuyet;

@SuppressWarnings("serial")
public class ViewDonCanGiaiQuyet extends VerticalLayout implements View{
	public static final String NAME = "gq-don-can-giai-quyet";
	@Override
	public void enter(ViewChangeEvent event) {
		this.addComponent(new LayoutDanhSachDonThuCanGiaiQuyet());
	}
}
