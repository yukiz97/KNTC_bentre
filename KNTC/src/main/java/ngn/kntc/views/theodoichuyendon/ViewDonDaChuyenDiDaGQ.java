package ngn.kntc.views.theodoichuyendon;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

import ngn.kntc.page.donthu.danhsach.xulydon.LayoutDanhSachDonThuDaChuyenDi;

@SuppressWarnings("serial")
public class ViewDonDaChuyenDiDaGQ extends VerticalLayout implements View{
	public static final String NAME = "don-da-chuyen-di-da-gq";
	@Override
	public void enter(ViewChangeEvent event) {
		this.addComponent(new LayoutDanhSachDonThuDaChuyenDi(1));
	}
}
