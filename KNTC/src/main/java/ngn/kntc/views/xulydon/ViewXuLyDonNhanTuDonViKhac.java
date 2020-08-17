package ngn.kntc.views.xulydon;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

import ngn.kntc.page.donthu.danhsach.xulydon.LayoutDanhSachDonThuTuDonViKhac;

@SuppressWarnings("serial")
public class ViewXuLyDonNhanTuDonViKhac extends VerticalLayout implements View{
	public static final String NAME = "xl-don-nhan-tu-don-vi-khac";
	@Override
	public void enter(ViewChangeEvent event) {
		this.addComponent(new LayoutDanhSachDonThuTuDonViKhac());
	}
}
