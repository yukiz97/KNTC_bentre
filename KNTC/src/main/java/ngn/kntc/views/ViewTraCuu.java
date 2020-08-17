package ngn.kntc.views;

import ngn.kntc.page.tracuu.TraCuuEvents;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ViewTraCuu extends VerticalLayout implements View{
	public static final String NAME = "tra-cuu-don-thu";
	@Override
	public void enter(ViewChangeEvent event) {
		this.addComponent(new TraCuuEvents());
	}
}
