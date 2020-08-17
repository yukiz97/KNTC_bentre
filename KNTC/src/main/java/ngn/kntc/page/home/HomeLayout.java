package ngn.kntc.page.home;

import ngn.kntc.UI.kntcUI;
import ngn.kntc.enums.LoaiQuanLy;
import ngn.kntc.enums.LoaiTinhTrangDonThuEnum;
import ngn.kntc.layout.FormMenu;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.views.giaiquyet.ViewDonCanGiaiQuyet;
import ngn.kntc.views.giaiquyet.ViewDonDangThiHanhGiaiQuyet;
import ngn.kntc.views.thammuu.ViewThamMuuChuaGiaiQuyet;
import ngn.kntc.views.thammuu.ViewThamMuuDaGiaiQuyet;
import ngn.kntc.views.thuly.ViewDonCanThuLy;
import ngn.kntc.views.thuly.ViewDonDaThuLy;
import ngn.kntc.views.xulydon.ViewXuLyDonDaCoKetQua;
import ngn.kntc.views.xulydon.ViewXuLyDonDaTiepNhan;
import ngn.kntc.views.xulydon.ViewXuLyDonNhanTuDonViKhac;

import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class HomeLayout extends ResponsiveLayout{
	Label lblCaptionXuLy = new Label("Xử lý đơn thư",ContentMode.HTML);
	Label lblCaptionThuLy = new Label("Thụ lý đơn thư",ContentMode.HTML);
	Label lblCaptionGiaiQuyet = new Label("Giải quyết đơn thư",ContentMode.HTML);
	
	Label lblCaptionThamMuuGiaiQuyet = new Label("Tham mưu giải quyết",ContentMode.HTML);
	
	public HomeLayout() {
		try {
			buildLayout();
		} catch (Exception e) {
			e.printStackTrace();
		}
		configComponent();
	}

	private void buildLayout() throws Exception {
		if((int)kntcUI.getCurrent().getFormMenu().getOgChonLoai().getValue()==LoaiQuanLy.donvi.getType())
		{
			this.addComponent(lblCaptionXuLy);
			this.addComponent(buildRowXuLy());
			this.addComponent(lblCaptionThuLy);
			this.addComponent(buildRowThuLy());
			this.addComponent(lblCaptionGiaiQuyet);
			this.addComponent(buildRowGiaiQuyet());
		}
		else
		{
			this.addComponent(lblCaptionThamMuuGiaiQuyet);
			this.addComponent(buildRowThamMuuGiaiQuyet());
		}
		
		lblCaptionXuLy.addStyleName("lbl-home-caption");
		lblCaptionThuLy.addStyleName("lbl-home-caption");
		lblCaptionGiaiQuyet.addStyleName("lbl-home-caption");
		lblCaptionThamMuuGiaiQuyet.addStyleName("lbl-home-caption");
		
		this.setWidth("100%");
	}

	private void configComponent() {
		
	}
	
	public ResponsiveRow buildRowXuLy() throws Exception
	{
		ResponsiveRow row = new ResponsiveRow();
		
		HorizontalLayout hBlockDaTiepNhan = buildBlockHientThi(LoaiTinhTrangDonThuEnum.datiepnhan.getType());
		HorizontalLayout hBlockCQKhacChuyenDen = buildBlockHientThi(LoaiTinhTrangDonThuEnum.donvikhacchuyenden.getType());
		HorizontalLayout hBlockDaCoKQ = buildBlockHientThi(LoaiTinhTrangDonThuEnum.dacokq.getType());
		
		hBlockDaTiepNhan.addStyleName("hLayout-block-xuly");
		hBlockCQKhacChuyenDen.addStyleName("hLayout-block-xuly");
		hBlockDaCoKQ.addStyleName("hLayout-block-xuly");
		
		row.addColumn().withComponent(hBlockDaTiepNhan).withDisplayRules(6, 6, 6, 6);
		row.addColumn().withComponent(hBlockCQKhacChuyenDen).withDisplayRules(6, 6, 6, 6);
		row.addColumn().withComponent(hBlockDaCoKQ).withDisplayRules(6, 6, 6, 6);
		
		row.setVerticalSpacing(true);
		row.setSpacing(true);
		
		return row;
	}
	
	public ResponsiveRow buildRowThuLy() throws Exception
	{
		ResponsiveRow row = new ResponsiveRow();
		
		HorizontalLayout hBlockCanThuLy = buildBlockHientThi(LoaiTinhTrangDonThuEnum.canthuly.getType());
		HorizontalLayout hBlockDaThuLy = buildBlockHientThi(LoaiTinhTrangDonThuEnum.dathuly.getType());
		
		hBlockCanThuLy.addStyleName("hLayout-block-thuly");
		hBlockDaThuLy.addStyleName("hLayout-block-thuly");
		
		row.addColumn().withComponent(hBlockCanThuLy).withDisplayRules(6, 6, 6, 4);
		row.addColumn().withComponent(hBlockDaThuLy).withDisplayRules(6, 6, 6, 4);
		
		row.setVerticalSpacing(true);
		row.setSpacing(true);
		
		return row;
	}
	
	public ResponsiveRow buildRowGiaiQuyet() throws Exception
	{
		ResponsiveRow row = new ResponsiveRow();
		
		HorizontalLayout hBlockCanGiaiQuyet = buildBlockHientThi(LoaiTinhTrangDonThuEnum.cangiaiquyet.getType());
		HorizontalLayout hBlockDangThiHanh = buildBlockHientThi(LoaiTinhTrangDonThuEnum.dangthihanh.getType());
		
		hBlockCanGiaiQuyet.addStyleName("hLayout-block-giaiquyet");
		hBlockDangThiHanh.addStyleName("hLayout-block-giaiquyet");
		
		row.addColumn().withComponent(hBlockCanGiaiQuyet).withDisplayRules(6, 6, 6, 4);
		row.addColumn().withComponent(hBlockDangThiHanh).withDisplayRules(6, 6, 6, 4);
		
		row.setVerticalSpacing(true);
		row.setSpacing(true);
		
		return row;
	}
	
	public ResponsiveRow buildRowThamMuuGiaiQuyet() throws Exception
	{
		ResponsiveRow row = new ResponsiveRow();
		
		HorizontalLayout hBlockChuaGiaiQuyet = buildBlockHientThi(LoaiTinhTrangDonThuEnum.thammuuchuagiaiquyet.getType());
		HorizontalLayout hBlockDaGiaiQuyet = buildBlockHientThi(LoaiTinhTrangDonThuEnum.thammuudagiaiquyet.getType());
		
		hBlockChuaGiaiQuyet.addStyleName("hLayout-block-giaiquyet");
		hBlockDaGiaiQuyet.addStyleName("hLayout-block-giaiquyet");
		
		row.addColumn().withComponent(hBlockChuaGiaiQuyet).withDisplayRules(6, 6, 6, 4);
		row.addColumn().withComponent(hBlockDaGiaiQuyet).withDisplayRules(6, 6, 6, 4);
		
		row.setVerticalSpacing(true);
		row.setSpacing(true);
		
		return row;
	}
	
	public HorizontalLayout buildBlockHientThi(int type) throws Exception
	{
		HorizontalLayout hLayout = new HorizontalLayout();
		VerticalLayout vLayout = new VerticalLayout();
		
		String navigator = "";
		
		String strMain = "";
		String strSub = "";
		int countSum = DonThuServiceUtil.countDonThuOnTypeGeneral(type);
		int count1 = countSum>0? countSum-1:0;
		int count2 = countSum>0? countSum-count1:0;
		
		if(type==LoaiTinhTrangDonThuEnum.datiepnhan.getType())
		{
			navigator = ViewXuLyDonDaTiepNhan.NAME;
			strMain = "<b style='font-size: 17px'>Số đơn thư đã tiếp nhận: "+countSum+"</b>";
			strSub = "<b style='font-size: 15px'>+ Số đơn còn hạn xử lý: "+count1+"</b> <br/><b style='font-size: 15px'>+ Số đơn quá hạn xử lý: "+count2+"</b>";
		}
		if(type==LoaiTinhTrangDonThuEnum.donvikhacchuyenden.getType())
		{
			navigator = ViewXuLyDonNhanTuDonViKhac.NAME;
			strMain = "<b style='font-size: 17px'>Số đơn thư từ đơn vị khác chuyển đến: "+countSum+"</b>";
			strSub = "<b style='font-size: 15px'>+ Số đơn còn hạn xử lý: "+count1+"</b> <br/><b style='font-size: 15px'>+ Số đơn quá hạn xử lý: "+count2+"</b>";
		}
		if(type==LoaiTinhTrangDonThuEnum.dacokq.getType())
		{
			navigator = ViewXuLyDonDaCoKetQua.NAME;
			strMain = "<b style='font-size: 17px'>Số đơn thư đã có kết quả xử lý: "+countSum+"</b>";
			strSub = "<b style='font-size: 15px'>+ Số đơn xử lý trong hạn: "+count1+"</b> <br/><b style='font-size: 15px'>+ Số đơn xử lý quá hạn: "+count2+"</b>";
		}
		if(type==LoaiTinhTrangDonThuEnum.canthuly.getType())
		{
			navigator = ViewDonCanThuLy.NAME;
			strMain = "<b style='font-size: 17px'>Số đơn thư cần thụ lý: "+countSum+"</b>";
			strSub = "<b style='font-size: 15px'>+ Số đơn còn hạn thụ lý: "+count1+"</b> <br/><b style='font-size: 15px'>+ Số đơn quá hạn thụ lý: "+count2+"</b>";
		}
		if(type==LoaiTinhTrangDonThuEnum.dathuly.getType())
		{
			navigator = ViewDonDaThuLy.NAME;
			strMain = "<b style='font-size: 17px'>Số đơn thư đã thụ lý: "+countSum+"</b>";
			strSub = "<b style='font-size: 15px'>+ Số đơn thụ lý đúng hạn: "+count1+"</b> <br/><b style='font-size: 15px'>+ Số đơn thụ lý quá hạn: "+count2+"</b>";
		}
		if(type==LoaiTinhTrangDonThuEnum.cangiaiquyet.getType())
		{
			navigator = ViewDonCanGiaiQuyet.NAME;
			strMain = "<b style='font-size: 17px'>Số đơn thư cần giải quyết: "+countSum+"</b>";
			strSub = "<b style='font-size: 15px'>+ Số đơn trong hạn giải quyết: "+count1+"</b> <br/><b style='font-size: 15px'>+ Số đơn quá hạn giải quyết: "+count2+"</b>";
		}
		if(type==LoaiTinhTrangDonThuEnum.dangthihanh.getType())
		{
			navigator = ViewDonDangThiHanhGiaiQuyet.NAME;
			strMain = "<b style='font-size: 17px'>Số đơn thư đã đang thi hành: "+countSum+"</b>";
		}
		if(type==LoaiTinhTrangDonThuEnum.thammuuchuagiaiquyet.getType())
		{
			navigator = ViewThamMuuChuaGiaiQuyet.NAME;
			strMain = "<b style='font-size: 17px'>Số đơn thư chưa được giải quyết: "+countSum+"</b>";
			strSub = "<b style='font-size: 15px'>+ Số đơn trong hạn giải quyết: "+count1+"</b> <br/><b style='font-size: 15px'>+ Số đơn quá hạn giải quyết: "+count2+"</b>";
		}
		if(type==LoaiTinhTrangDonThuEnum.thammuudagiaiquyet.getType())
		{
			navigator = ViewThamMuuDaGiaiQuyet.NAME;
			strMain = "<b style='font-size: 17px'>Số đơn thư đã được giải quyết: "+countSum+"</b>";
		}
		strSub = "";
		
		Label lblIcon = new Label(FontAwesome.BOOK.getHtml(),ContentMode.HTML);
		Label lblSum = new Label(strMain,ContentMode.HTML);
		Label lblDetail = new Label(strSub,ContentMode.HTML);
		
		lblIcon.addStyleName("lbl-home-blockIcon");
		
		vLayout.addComponents(lblSum,lblDetail);
		vLayout.setSpacing(true);
		
		hLayout.addComponents(lblIcon,vLayout);
		hLayout.setExpandRatio(lblIcon, 0.3f);
		hLayout.setExpandRatio(vLayout, 0.7f);
		hLayout.setWidth("100%");
		hLayout.addStyleName("hLayout-block");
		
		final String tmp = navigator;
		hLayout.addLayoutClickListener(new LayoutClickListener() {
			
			@Override
			public void layoutClick(LayoutClickEvent event) {
				
				kntcUI.getCurrent().getNavigator().navigateTo(tmp);
			}
		});
		
		return hLayout;
	}
}
