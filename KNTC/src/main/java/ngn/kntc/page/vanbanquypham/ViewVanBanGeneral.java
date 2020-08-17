package ngn.kntc.page.vanbanquypham;

import ngn.kntc.UI.kntcUI;

import com.vaadin.ui.VerticalLayout;

public class ViewVanBanGeneral extends VerticalLayout{
	public ViewVanBanGeneral() {
		kntcUI.getCurrent().getSubViewDisplay().removeAllComponents();
		this.addComponents(kntcUI.getCurrent().getSubmenuVanBan(),kntcUI.getCurrent().getSubViewDisplay());
		this.setExpandRatio(kntcUI.getCurrent().getSubViewDisplay(), 1.0f);
	}
}
