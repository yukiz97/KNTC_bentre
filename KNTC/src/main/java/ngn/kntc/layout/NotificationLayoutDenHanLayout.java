package ngn.kntc.layout;

import java.sql.SQLException;
import java.util.List;

import ngn.kntc.UI.kntcUI;
import ngn.kntc.beans.ThongBaoDenHanBean;
import ngn.kntc.page.donthu.chitiet.ChiTietDonThuTCDGeneralLayout;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.themes.ValoTheme;

public class NotificationLayoutDenHanLayout extends VerticalLayout{
	QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh = new QuaTrinhXuLyGiaiQuyetServiceUtil();
	
	int count = 0;
	
	public NotificationLayoutDenHanLayout() {
		try {
			buildLayout();
			configComponent();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void buildLayout() throws SQLException {
		List<ThongBaoDenHanBean> listThongBaoXuLy = svQuaTrinh.getThongBaoDenHanXuLy();
		List<ThongBaoDenHanBean> listThongBaoThuLy = svQuaTrinh.getThongBaoDenHanThuLy();
		List<ThongBaoDenHanBean> listThongBaoGiaiQuyet = svQuaTrinh.getThongBaoDenHanGiaiQuyet();
		
		count = listThongBaoXuLy.size()+listThongBaoThuLy.size()+listThongBaoGiaiQuyet.size();
		
		this.addComponent(buildThongBaoDonThuDenHan("Xử lý đơn thư", listThongBaoXuLy));
		this.addComponent(buildThongBaoDonThuDenHan("Thụ lý đơn thư", listThongBaoThuLy));
		this.addComponent(buildThongBaoDonThuDenHan("Giải quyết đơn thư", listThongBaoGiaiQuyet));

		this.setWidth("500px");
		this.setSpacing(true);
		this.addStyleName("thongbao-denhan-mainlayout");
	}

	private void configComponent() {

	}

	private VerticalLayout buildThongBaoDonThuDenHan(String caption,List<ThongBaoDenHanBean> listThongBao)
	{
		VerticalLayout vBlockThongBao = new VerticalLayout();

		HorizontalLayout hCaption = new HorizontalLayout();
		Label lblCaption = new Label("<div style='background: #356db1; padding: 3px 9px; height: 20px; font-size: 14px; font-weight: bold; color: #fff;'>"+caption+" <span style='float:right'>"+listThongBao.size()+"</span></div>",ContentMode.HTML);
		Button btnCollapse = new Button("",FontAwesome.MINUS);

		hCaption.addComponents(lblCaption,btnCollapse);
		
		btnCollapse.addStyleName(ValoTheme.BUTTON_SMALL);

		hCaption.setExpandRatio(lblCaption, 1.0f);

		hCaption.setWidth("100%");
		hCaption.setSpacing(true);

		VerticalLayout vThongBaoContent = new VerticalLayout();
		
		if(listThongBao.size()>0)
		{
			for(ThongBaoDenHanBean model : listThongBao)
			{
				VerticalLayout vThongBao = new VerticalLayout();
				Label lblNoiDung = new Label(model.getNoiDung(),ContentMode.HTML);
				Label lblDenHan = new Label(model.getStrDenHan(),ContentMode.HTML);

				vThongBao.addComponent(lblNoiDung);
				vThongBao.addComponent(lblDenHan);
				
				vThongBao.setWidth("100%");
				vThongBao.addStyleName("chitiet-thongbao-thongbaodetail");
				
				vThongBaoContent.addComponent(vThongBao);

				vThongBao.addLayoutClickListener(new LayoutClickListener() {

					@Override
					public void layoutClick(LayoutClickEvent event) {
						Window wdChiTiet = new Window();

						UI.getCurrent().addWindow(wdChiTiet);
						try {
							wdChiTiet.setContent(new ChiTietDonThuTCDGeneralLayout(model.getIdDonThu()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						wdChiTiet.setSizeFull();
						wdChiTiet.setModal(true);
						wdChiTiet.setCaption("Thông tin chi tiết đơn thư");
						wdChiTiet.addCloseListener(new CloseListener() {

							@Override
							public void windowClose(CloseEvent e) {
								try {
									kntcUI.getCurrent().setMenuCountValue((int)kntcUI.getCurrent().getFormMenu().getOgChonLoai().getValue());
								} catch (Exception e1) {
									e1.printStackTrace();
								}
							}
						});					
					}
				});
			}
		}
		else
		{
			vThongBaoContent.addComponent(new Label("<i style='color:gray'>Không có thông báo trong mục này</i>",ContentMode.HTML));
		}
		
		vBlockThongBao.addComponent(hCaption);
		vBlockThongBao.addComponent(vThongBaoContent);

		vBlockThongBao.setWidth("100%");
		vBlockThongBao.setSpacing(true);
		vBlockThongBao.addStyleName("thongbao-denhan-thongbaoblock");
		
		btnCollapse.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(vThongBaoContent.isVisible())
				{
					vThongBaoContent.setVisible(false);
					btnCollapse.setIcon(FontAwesome.PLUS);
				}
				else
				{
					vThongBaoContent.setVisible(true);
					btnCollapse.setIcon(FontAwesome.MINUS);
				}
			}
		});

		return vBlockThongBao;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
