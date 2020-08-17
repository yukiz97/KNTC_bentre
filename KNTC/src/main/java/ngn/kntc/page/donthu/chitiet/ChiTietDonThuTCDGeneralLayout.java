package ngn.kntc.page.donthu.chitiet;

import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

public class ChiTietDonThuTCDGeneralLayout extends VerticalLayout{
	TabSheet tabMain = new TabSheet();
	
	public ChiTietDonThuTCDGeneralLayout(int idDonThu) throws Exception {
		buildLayout();
		configComponent();
		tabMain.addTab(new ChiTietDonThuLayoutTmp(idDonThu),"Thông tin đơn thư");
		tabMain.addTab(new ChiTietQuaTrinhXLGQLayout(idDonThu),"Quá trình xử lý, giải quyết");
		tabMain.addTab(new ChiTietThongTinLuanChuyenLayout(idDonThu),"Thông tin luân chuyển");
	}
	
	public ChiTietDonThuTCDGeneralLayout(int idTCD,int idDonThu) throws Exception {
		buildLayout();
		configComponent();
		
		tabMain.addTab(new ChiTietTiepCongDanLayoutTmp(idTCD),"Thông tin tiếp công dân");
		if(idDonThu!=0)
			tabMain.addTab(new ChiTietDonThuLayoutTmp(idDonThu),"Thông tin đơn thư");
	}
	
	private void buildLayout()
	{
		this.addComponent(tabMain);
		
		tabMain.addStyleName("tab-donthu-chitiet");
		
		this.setMargin(true);
	}
	
	private void configComponent()
	{
		
	}
}
