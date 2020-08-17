
package ngn.kntc.layout;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import ngn.kntc.UI.kntcUI;
import ngn.kntc.beans.DonThuThongBaoBean;
import ngn.kntc.beans.QuaTrinhXuLyGiaiQuyetBean;
import ngn.kntc.page.donthu.chitiet.ChiTietDonThuTCDGeneralLayout;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;

import com.liferay.portal.service.UserLocalServiceUtil;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class NotificationLayout extends VerticalLayout{
	private QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh = new QuaTrinhXuLyGiaiQuyetServiceUtil();
	private SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat sdfDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	public NotificationLayout() {
		try {
			buildLayout();
		} catch (Exception e) {
			e.printStackTrace();
		}
		configComponent();
	}

	private void buildLayout() throws Exception {
		this.setWidth("500px");
		this.setSpacing(true);

		this.addStyleName("vLayout-notifi-main");
		loadAlert();
	}

	private void configComponent() {

	}

	private void loadAlert() throws Exception
	{
		List<Date> listDate =  svQuaTrinh.getAllAlertDateOfUser();
		for(Date date : listDate)
		{
			this.addComponent(buildDayBlock(date));
		}
	}

	private VerticalLayout buildDayBlock(Date date) throws Exception
	{
		VerticalLayout vDay = new VerticalLayout();

		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		DayOfWeek dayOfWeek = localDate.getDayOfWeek();
		int year  = localDate.getYear();
		int month = localDate.getMonthValue();
		int day   = localDate.getDayOfMonth();
		Label lblHeader = new Label("Thứ "+dayOfWeek.getValue()+", ngày "+day+" tháng "+month+" năm "+year,ContentMode.HTML);
		lblHeader.addStyleName("lbl-notifi-caption");

		List<Integer> listDonThu = svQuaTrinh.getAllDonThuOfUserByDate(date);

		vDay.addComponent(lblHeader);
		for(int idDonThu : listDonThu)
		{
			VerticalLayout vDonThu = buildDonThuBlock(idDonThu, date);
			VerticalLayout vTmp = new VerticalLayout();
			vDay.addComponent(vDonThu); 

			HorizontalLayout hTmp = (HorizontalLayout) vDonThu.getComponent(2);
			Label lblCountTmp = (Label) hTmp.getComponent(0);
			Button btnDetailTmp = (Button) hTmp.getComponent(1);
			HorizontalLayout hTmp2 = (HorizontalLayout) vDonThu.getComponent(0);
			Button btnDelete = (Button) hTmp2.getComponent(1);
			
			btnDelete.addStyleName(ValoTheme.BUTTON_SMALL);
			
			btnDetailTmp.setId("-1");
			btnDetailTmp.addClickListener(new ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					int index = vDay.getComponentIndex(vDonThu)+1;
					if(btnDetailTmp.getId()=="-1")
					{
						vTmp.setSpacing(true);
						vTmp.addStyleName("animated fadeIn");
						try {
							List<QuaTrinhXuLyGiaiQuyetBean> listQuaTrinhAlert = svQuaTrinh.getQuaTrinhXuLyAlert(idDonThu, date);
							for(QuaTrinhXuLyGiaiQuyetBean modelQuaTrinh : listQuaTrinhAlert)
							{
								VerticalLayout vDetail = buildNotificationContentBlock(modelQuaTrinh);
								vTmp.addComponent(vDetail);
								vTmp.setComponentAlignment(vDetail, Alignment.MIDDLE_RIGHT);
								
								HorizontalLayout hTmp = (HorizontalLayout) vDetail.getComponent(0);
								Button btnDelete = (Button) hTmp.getComponent(1);
								
								btnDelete.addClickListener(new ClickListener() {
									
									@Override
									public void buttonClick(ClickEvent event) {
										try {
											vTmp.removeComponent(vDetail);
											svQuaTrinh.deleteThongBao(modelQuaTrinh.getMaQuaTrinh());
											int count = svQuaTrinh.countQuaTrinhXuLyAlertByDate(idDonThu, date);
											lblCountTmp.setValue(FontAwesome.BELL_O.getHtml()+" "+count);
											kntcUI.getCurrent().getFormHead().getMenuItemNotification().setText(QuaTrinhXuLyGiaiQuyetServiceUtil.countQuaTrinhXuLyAllAlert()+"");
											if(count==0)
											{
												vDay.removeComponent(vTmp);
												vDay.removeComponent(vDonThu);
												if(vDay.getComponentCount()==1)
													removeComponent(vDay);
											}
										} catch (SQLException e) {
											e.printStackTrace();
										}
									}
								});

								vDetail.addStyleName("animated flipInY");
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						vDay.addComponent(vTmp, index);
						btnDetailTmp.setId("1");
						btnDetailTmp.setCaption("Ẩn");
						btnDetailTmp.setIcon(FontAwesome.EYE_SLASH);
					}
					else
					{
						vTmp.removeAllComponents();
						btnDetailTmp.setId("-1");
						btnDetailTmp.setCaption("Hiển thị");
						btnDetailTmp.setIcon(FontAwesome.EYE);
						vDay.removeComponent(vDay.getComponent(index));
					}
				}
			});
			
			btnDelete.addClickListener(new ClickListener() {
				
				@Override 
				public void buttonClick(ClickEvent event) {
					try {
						svQuaTrinh.deleteAllThongBao(idDonThu, date);
						vDay.removeComponent(vDonThu);
						vDay.removeComponent(vTmp);
						kntcUI.getCurrent().getFormHead().getMenuItemNotification().setText(QuaTrinhXuLyGiaiQuyetServiceUtil.countQuaTrinhXuLyAllAlert()+"");
						if(vDay.getComponentCount()==1)
						{
							removeComponent(vDay);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			});
		}

		vDay.setWidth("100%");
		vDay.setSpacing(true);
		vDay.addStyleName("vLayout-notifi-date");

		return vDay;
	}

	private VerticalLayout buildDonThuBlock(int idDonThu,Date ngay) throws SQLException
	{
		DonThuThongBaoBean model = svQuaTrinh.getDonThuThongBao(idDonThu);
		String ngayThang = sdfDate.format(ngay);
		String noiDung = model.getNoiDung();
		int count = svQuaTrinh.countQuaTrinhXuLyAlertByDate(idDonThu, ngay);

		VerticalLayout vDonThu = new VerticalLayout();

		HorizontalLayout hHeader = new HorizontalLayout();
		Label lblNgayThang = new Label("<b style='color: #1f7dce;'>"+ngayThang+"</b>",ContentMode.HTML);
		Button btnXoa = new Button("",FontAwesome.TRASH);

		Label lblNoiDung = new Label("<b>Nội dung:</b> <i style='color: #717070'>"+noiDung+"</i>",ContentMode.HTML);
		HorizontalLayout hNoiDung = new HorizontalLayout(lblNoiDung);
		Label lblCount = new Label(FontAwesome.BELL_O.getHtml()+" "+count,ContentMode.HTML); 
		Button btnHienThi = new Button("Hiển thị",FontAwesome.EYE);

		HorizontalLayout hFooter = new HorizontalLayout();

		vDonThu.addComponent(hHeader);
		vDonThu.addComponent(hNoiDung);
		vDonThu.addComponent(hFooter);
		
		hNoiDung.setWidth("100%");
		lblNoiDung.addStyleName("lbl-notifi-noidung lbl-notifi-noidung-hidden");
		lblNoiDung.setId("hidden");

		hHeader.addComponents(lblNgayThang,btnXoa);
		hHeader.setComponentAlignment(lblNgayThang,Alignment.MIDDLE_LEFT);
		hHeader.setComponentAlignment(btnXoa,Alignment.MIDDLE_RIGHT);
		hHeader.setWidth("100%");
		btnXoa.addStyleName("btn-notifi-delete");
		btnXoa.addStyleName(ValoTheme.BUTTON_SMALL);

		hFooter.addComponents(lblCount,btnHienThi);
		hFooter.setComponentAlignment(lblCount,Alignment.MIDDLE_LEFT);
		hFooter.setComponentAlignment(btnHienThi,Alignment.MIDDLE_RIGHT);
		hFooter.setWidth("100%");

		lblCount.setWidthUndefined();
		lblCount.addStyleName("lbl-notifi-count");
		btnHienThi.addStyleName("btn-notifi-detail");
		btnHienThi.addStyleName(ValoTheme.BUTTON_SMALL);

		vDonThu.setWidth("100%");

		vDonThu.addStyleName("vLayout-notifi-sub");
		hNoiDung.addLayoutClickListener(new LayoutClickListener() {

			@Override
			public void layoutClick(LayoutClickEvent event) {
				if(lblNoiDung.getId()=="hidden")
				{
					lblNoiDung.removeStyleName("lbl-notifi-noidung-hidden");
					lblNoiDung.setId("show");
				}
				else
				{
					lblNoiDung.addStyleName("lbl-notifi-noidung-hidden");
					lblNoiDung.setId("hidden");
				}
				
				Window wdChiTiet = new Window();

				UI.getCurrent().addWindow(wdChiTiet);
				try {
					wdChiTiet.setContent(new ChiTietDonThuTCDGeneralLayout(idDonThu));
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

		return vDonThu;
	}

	private VerticalLayout buildNotificationContentBlock(QuaTrinhXuLyGiaiQuyetBean model) throws Exception
	{
		String ngayThang = sdfDateTime.format(model.getNgayDang());
		String noiDung = model.getNoiDung();
		String user = UserLocalServiceUtil.getUser(model.getUserNhap()).getFirstName();

		VerticalLayout vDetail = new VerticalLayout();

		HorizontalLayout hHeader = new HorizontalLayout();
		Label lblNgayThang = new Label("<b style='color: #1f7dce;'>"+ngayThang+"</b>",ContentMode.HTML);
		Button btnXoa = new Button("",FontAwesome.REMOVE);

		Label lblNoiDung = new Label(noiDung,ContentMode.HTML);
		Label lblUser = new Label("<b style='margin-top: 5px !important; color: #fff; display: inline-block; background: #dc3a3a; padding: 1px 13px; font-size: 11px; border-radius: 3px;'>"+FontAwesome.USER.getHtml()+" - "+user+"</b>",ContentMode.HTML); 

		HorizontalLayout hFooter = new HorizontalLayout();

		vDetail.addComponent(hHeader);
		vDetail.addComponent(lblNoiDung);
		vDetail.addComponent(hFooter);

		hHeader.addComponents(lblNgayThang,btnXoa);
		hHeader.setComponentAlignment(lblNgayThang,Alignment.MIDDLE_LEFT);
		hHeader.setComponentAlignment(btnXoa,Alignment.MIDDLE_RIGHT);
		hHeader.setWidth("100%");
		hHeader.setHeight("30px");

		btnXoa.addStyleName("btn-notifi-delete");
		btnXoa.addStyleName(ValoTheme.BUTTON_SMALL);

		hFooter.addComponents(lblUser);
		hFooter.setWidth("100%");

		vDetail.setWidth("380px");
		vDetail.addStyleName("vLayout-notifi-detail");

		return vDetail;
	}
}
