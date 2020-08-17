package ngn.kntc.windows.tientrinhxlgq;

import java.util.ArrayList;
import java.util.List;

import ngn.kntc.beans.KetQuaXuLyBean;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class WindowShowKetQuaXuLy extends Window{
	VerticalLayout vMainLayout = new VerticalLayout();
	
	QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh = new QuaTrinhXuLyGiaiQuyetServiceUtil();
	
	public WindowShowKetQuaXuLy(int idDonThu) {
		vMainLayout.setMargin(true); 
		vMainLayout.setSpacing(true);
		
		List<KetQuaXuLyBean> listKetQua = new ArrayList<KetQuaXuLyBean>();
		try {
			listKetQua = svQuaTrinh.getKetQuaXuLyList(idDonThu);
			
			for(KetQuaXuLyBean model : listKetQua)
			{
				ShowKetQuaXuLytLayout ketQuaXuLy = new ShowKetQuaXuLytLayout(idDonThu);
				ketQuaXuLy.buildValue(model);
				ketQuaXuLy.buildLayout();
				vMainLayout.addComponent(ketQuaXuLy.getvMainLayout());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.setCaption("   Kết quả xử lý của đơn thư");
		this.setContent(vMainLayout);
		this.center();
		this.setWidth("900px");
		this.setIcon(FontAwesome.DEVIANTART);
		this.setModal(true);
	}
}
