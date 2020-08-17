package ngn.kntc.views.luutruhoso;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

import ngn.kntc.page.donthu.danhsach.daketthuc.LayoutDanhSachDonDuocRut;
import ngn.kntc.page.donthu.danhsach.daketthuc.LayoutDanhSachDonPhucTapKeoDai;
import ngn.kntc.page.donthu.danhsach.xulydon.LayoutDanhSachDonThuDaChuyenDi;

@SuppressWarnings("serial")
public class ViewDonDuocRut extends VerticalLayout implements View{
	public static final String NAME = "don-phuc-tap-ket-dai";
	@Override
	public void enter(ViewChangeEvent event) {
		this.addComponent(new LayoutDanhSachDonDuocRut());
	}
}
