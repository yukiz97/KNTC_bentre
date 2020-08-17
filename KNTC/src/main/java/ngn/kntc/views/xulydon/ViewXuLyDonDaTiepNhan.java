package ngn.kntc.views.xulydon;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

import ngn.kntc.page.donthu.danhsach.xulydon.LayoutDanhSachDonThuDaTiepNhan;

@SuppressWarnings("serial")
public class ViewXuLyDonDaTiepNhan extends VerticalLayout implements View{
	public static final String NAME = "xl-don-da-tiep-nhan";
	@Override
	public void enter(ViewChangeEvent event) {
		this.addComponent(new LayoutDanhSachDonThuDaTiepNhan());
	}
}
