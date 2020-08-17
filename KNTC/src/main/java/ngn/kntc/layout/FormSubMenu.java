package ngn.kntc.layout;

import java.util.LinkedHashMap;

import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.HorizontalLayout;

public class FormSubMenu extends AbsoluteLayout{
	private HorizontalLayout subMenuLayout=new HorizontalLayout();
	private LinkedHashMap<String, String> subMenuItems = new LinkedHashMap<String, String>();
	
	public FormSubMenu() {
		this.addStyleName("subMenuLayout");
		this.setHeight(30, Unit.PIXELS);
		this.setWidth(100, Unit.PERCENTAGE);
		this.addComponent(subMenuLayout, "left:0px; bottom:0px");
	}

	public HorizontalLayout getSubMenuLayout() {
		return subMenuLayout;
	}

	public void setSubMenuLayout(HorizontalLayout subMenuLayout) {
		this.subMenuLayout = subMenuLayout;
	}

	public LinkedHashMap<String, String> getSubMenuItems() {
		return subMenuItems;
	}

	public void setSubMenuItems(LinkedHashMap<String, String> subMenuItems) {
		this.subMenuItems = subMenuItems;
	}
	
}
