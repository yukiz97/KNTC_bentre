package ngn.kntc.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ViewError extends VerticalLayout implements View{
	private Image menuLogo = new Image(null, new ThemeResource("images/404error.png"));
	
	public ViewError() {
		this.setSizeFull();
		this.addComponent(menuLogo);
		this.menuLogo.setWidth(50, Unit.PERCENTAGE);
		this.menuLogo.addStyleName("animated zoomIn");
		this.setComponentAlignment(menuLogo, Alignment.MIDDLE_CENTER);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
	}
}
