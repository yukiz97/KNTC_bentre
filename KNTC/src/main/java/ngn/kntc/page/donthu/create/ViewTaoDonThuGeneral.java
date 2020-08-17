package ngn.kntc.page.donthu.create;

import ngn.kntc.UI.kntcUI;

import com.vaadin.ui.VerticalLayout;

public class ViewTaoDonThuGeneral extends VerticalLayout{
	public ViewTaoDonThuGeneral() {
		kntcUI.getCurrent().getSubViewDisplay().removeAllComponents();
		this.addComponents(kntcUI.getCurrent().getSubmenuVanBan(),kntcUI.getCurrent().getSubViewDisplay());
		this.setExpandRatio(kntcUI.getCurrent().getSubViewDisplay(), 1.0f);
	}
}
