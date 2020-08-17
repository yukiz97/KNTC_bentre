package ngn.kntc.windows.tientrinhxlgq;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ngn.kntc.beans.ThiHanhQuyetDinhBean;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class WindowShowThiHanhGiaiQuyet extends Window{
	VerticalLayout vMainLayout = new VerticalLayout();
	
	QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh = new QuaTrinhXuLyGiaiQuyetServiceUtil();
	
	public WindowShowThiHanhGiaiQuyet(int maQuyetDinh) {
		vMainLayout.setMargin(true); 
		vMainLayout.setSpacing(true);
		
		List<ThiHanhQuyetDinhBean> listThiHanh = new ArrayList<ThiHanhQuyetDinhBean>();
		try {
			listThiHanh = svQuaTrinh.getThiHanhGiaiQuyetList(maQuyetDinh);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for(ThiHanhQuyetDinhBean model : listThiHanh)
		{
			ShowThiHanhGiaiQuyetLayout thiHanhGiaiQuyet = new ShowThiHanhGiaiQuyetLayout();
			thiHanhGiaiQuyet.buildValue(model, svQuaTrinh);
			thiHanhGiaiQuyet.buildBoiThuongThietHai();
			thiHanhGiaiQuyet.buildNoiDungQuyetDinh();
			vMainLayout.addComponent(thiHanhGiaiQuyet.getvDetail());
		}
		
		this.setCaption("   Kết quả thi hành giải quyết của đơn thư");
		this.setContent(vMainLayout);
		this.center();
		this.setWidth("900px");
		this.setIcon(FontAwesome.DEVIANTART);
		this.setModal(true);
	}
}
