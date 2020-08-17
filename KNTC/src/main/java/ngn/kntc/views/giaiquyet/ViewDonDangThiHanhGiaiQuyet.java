package ngn.kntc.views.giaiquyet;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

import ngn.kntc.page.donthu.danhsach.giaiquyetdon.LayoutDanhSachDonThuDangThiHanh;

@SuppressWarnings("serial")
public class ViewDonDangThiHanhGiaiQuyet extends VerticalLayout implements View{
	public static final String NAME = "gq-don-dang-thi-hanh";
	@Override
	public void enter(ViewChangeEvent event) {
		this.addComponent(new LayoutDanhSachDonThuDangThiHanh());
	}
}
