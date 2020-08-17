package ngn.kntc.views;

import ngn.kntc.page.home.HomeLayout;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ViewHome extends VerticalLayout implements View{
	public static final String NAME = "trang-chu.html";
	//private FormPageHome formPageHome=new FormPageHome();
	
	public ViewHome() {
		this.setWidth(100, Unit.PERCENTAGE);
		this.addComponent(new HomeLayout());
		this.setMargin(new MarginInfo(false,true,true,true));
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		//formPageHome.loadData();
		//loadData();
	}

}
