package ngn.kntc.layout;

import org.vaadin.sliderpanel.SliderPanel;
import org.vaadin.sliderpanel.SliderPanelBuilder;
import org.vaadin.sliderpanel.SliderPanelStyles;
import org.vaadin.sliderpanel.client.SliderMode;
import org.vaadin.sliderpanel.client.SliderTabPosition;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@StyleSheet("pagelayout.css")
public class PageLayout extends VerticalLayout {
	private AbsoluteLayout headArea = new AbsoluteLayout();
	
	private HorizontalLayout bodytArea=new HorizontalLayout();
	private AbsoluteLayout menuArea = new AbsoluteLayout();
	private CssLayout contentArea = new CssLayout();
	private SliderPanel sliderThongbaoDenHan;
	private NotificationLayoutDenHanLayout layoutNotifiHetHan = new NotificationLayoutDenHanLayout();
	private SliderPanel sliderThongbao;
	public PageLayout() {
		this.setSizeFull();

		this.headArea.setHeight(45, Unit.PIXELS);
		this.headArea.setWidth(100, Unit.PERCENTAGE);
		
		this.menuArea.setWidth(215, Unit.PIXELS);
		this.menuArea.setHeight(100, Unit.PERCENTAGE);
		this.menuArea.setPrimaryStyleName(ValoTheme.MENU_ROOT);
		
		this.contentArea.setSizeFull();
		this.contentArea.addStyleName("v-scrollable");
		this.contentArea.addStyleName("contentArea");
		
		this.sliderThongbaoDenHan = new SliderPanelBuilder(layoutNotifiHetHan)
        .expanded(false)
        .mode(SliderMode.RIGHT)
        .caption("Thông báo")
        .tabPosition(SliderTabPosition.MIDDLE)
        .tabSize(0)
        .build();
		this.sliderThongbaoDenHan.setId("slider-thongbao-denhan");
		this.sliderThongbaoDenHan.setStyleName(SliderPanelStyles.COLOR_WHITE);
		
		this.sliderThongbao = new SliderPanelBuilder(new NotificationLayout())
        .expanded(false)
        .mode(SliderMode.RIGHT)
        .caption("Thông báo")
        .tabPosition(SliderTabPosition.MIDDLE)
        .tabSize(0)
        .build();
		this.sliderThongbao.setId("slider-thongbao");
		this.sliderThongbao.setStyleName(SliderPanelStyles.COLOR_WHITE);
		this.addStyleName("slide-notifi-panel");
		
		this.bodytArea.setSizeFull();
		this.bodytArea.addComponents(menuArea,contentArea,sliderThongbao,sliderThongbaoDenHan);
		this.bodytArea.setExpandRatio(contentArea, 1);
		
		this.addComponents(headArea, bodytArea);
		this.setExpandRatio(bodytArea, 1);
		
	}
	
	public void setHead(Component head) {
		this.headArea.addComponent(head);
	}
	
	public void setMenu(Component menu) {
		menu.addStyleName(ValoTheme.MENU_PART);
		this.menuArea.addComponent(menu);
	}
	public ComponentContainer getContentContainer() {
		return this.contentArea;
	}
	public SliderPanel getSliderThongbao() {
		return sliderThongbao;
	}
	public SliderPanel getSliderThongbaoDenHan() {
		return sliderThongbaoDenHan;
	}
	public void setSliderThongbaoDenHan(SliderPanel sliderThongbaoDenHan) {
		this.sliderThongbaoDenHan = sliderThongbaoDenHan;
	}
	public NotificationLayoutDenHanLayout getLayoutNotifiHetHan() {
		return layoutNotifiHetHan;
	}
	public void setLayoutNotifiHetHan(
			NotificationLayoutDenHanLayout layoutNotifiHetHan) {
		this.layoutNotifiHetHan = layoutNotifiHetHan;
	}
}
