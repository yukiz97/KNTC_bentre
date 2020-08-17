package ngn.kntc.views.thuly;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

import ngn.kntc.page.donthu.danhsach.thulydon.LayoutDanhSachDonThuCanThuLy;

@SuppressWarnings("serial")
public class ViewDonCanThuLy extends VerticalLayout implements View{
	public static final String NAME = "tl-don-can-thu-ly";
	@Override
	public void enter(ViewChangeEvent event) {
		this.addComponent(new LayoutDanhSachDonThuCanThuLy());
	}
}
