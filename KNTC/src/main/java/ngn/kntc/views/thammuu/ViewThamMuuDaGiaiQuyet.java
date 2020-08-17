package ngn.kntc.views.thammuu;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

import ngn.kntc.page.donthu.danhsach.thammuugiaiquyet.LayoutDanhSachDonThuThamMuuDaGQ;
import ngn.kntc.page.donthu.danhsach.xulydon.LayoutDanhSachDonThuDaChuyenDi;

@SuppressWarnings("serial")
public class ViewThamMuuDaGiaiQuyet extends VerticalLayout implements View{
	public static final String NAME = "tham-muu-da-giai-quyet";
	public void enter(ViewChangeEvent event) {
		this.addComponent(new LayoutDanhSachDonThuThamMuuDaGQ());
	}
}
