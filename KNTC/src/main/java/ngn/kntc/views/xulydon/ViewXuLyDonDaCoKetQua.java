package ngn.kntc.views.xulydon;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

import ngn.kntc.page.donthu.danhsach.xulydon.LayoutDanhSachDonThuDaXuLy;

@SuppressWarnings("serial")
public class ViewXuLyDonDaCoKetQua extends VerticalLayout implements View{
	public static final String NAME = "xl-don-da-co-ket-qua";
	@Override
	public void enter(ViewChangeEvent event) {
		this.addComponent(new LayoutDanhSachDonThuDaXuLy());
	}
}
