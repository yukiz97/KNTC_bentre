package ngn.kntc.views.thammuu;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

import ngn.kntc.page.donthu.danhsach.thammuugiaiquyet.LayoutDanhSachDonThuThamMuuChuaGQ;
import ngn.kntc.page.donthu.danhsach.xulydon.LayoutDanhSachDonThuDaChuyenDi;

@SuppressWarnings("serial")
public class ViewThamMuuChuaGiaiQuyet extends VerticalLayout implements View{
	public static final String NAME = "tham-muu-chua-gq";
	@Override
	public void enter(ViewChangeEvent event) {
		this.addComponent(new LayoutDanhSachDonThuThamMuuChuaGQ());
	}
}
