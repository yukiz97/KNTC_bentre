package ngn.kntc.page.tracuu;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ngn.kntc.UI.kntcUI;
import ngn.kntc.beans.DonThuBean;
import ngn.kntc.beans.ThongTinDonThuBean;
import ngn.kntc.beans.TraCuuBean;
import ngn.kntc.enums.LoaiDonThuEnum;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.page.donthu.chitiet.ChiTietDonThuTCDGeneralLayout;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.SessionUtil;
import ngn.kntc.windows.WindowLinhVucHoSo;
import ngn.kntc.windows.WindowViewPDF;

import com.jarektoro.responsivelayout.ResponsiveRow;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class TraCuuEvents extends TraCuuDesign{
	List<DonThuBean> list = new ArrayList<DonThuBean>();
	
	@SuppressWarnings("deprecation")
	public TraCuuEvents() {
		txtSearch.addFocusListener(new FocusListener() {
			
			@Override
			public void focus(FocusEvent event) {
				btnSearch.setClickShortcut(KeyCode.ENTER);
			}
		});
		
		txtSearch.addBlurListener(new BlurListener() {
			
			@Override
			public void blur(BlurEvent event) {
				btnSearch.removeClickShortcut();
			}
		});
		
		btnSearch.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					if(validateForm())
					{
						TraCuuBean model = new TraCuuBean();
						model.setChuThe(layoutNguoiDiKNTC.getValueNguoiDiKNTC());
						if(!txtSearch.isEmpty())
							model.setKeyWord(txtSearch.getValue().trim());
						if(cmbNguonDon.getValue()!=null)
						{
							model.setNguonDon((int)cmbNguonDon.getValue());
						}
						if(cmbLoaiDon.getValue()!=null)
						{
							model.setLoaiDonThu((int)cmbLoaiDon.getValue());
						}
						if(!listLinhVuc.isEmpty())
						{
							model.setListLinhVuc(listLinhVuc);
						}
						if(cbNgayNhap.getValue())
						{
							model.setHasNgayNhap(true);
							model.setNgayNhapStart(dfNgayNhapStart.getValue());
							model.setNgayNhapEnd(dfNgayNhapEnd.getValue());
						}
						if(cbNgayThuLy.getValue())
						{
							model.setHasNgayThuLy(true);
							model.setNgayThuLyStart(dfNgayThuLyStart.getValue());
							model.setNgayThuLyEnd(dfNgayThuLyEnd.getValue());
						}
						if(cbNgayGiaiQuyet.getValue())
						{
							model.setHasNgayHanGiaiQuyet(true);
							model.setNgayHanGQStart(dfNgayGiaiQuyetStart.getValue());
							model.setNgayHanGQEnd(dfNgayGiaiQuyetEnd.getValue());
						}
						loadData(model);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnPdf.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					/*SearchPDF pdf =new SearchPDF();
					pdf.setStrTochuc(OrganizationLocalServiceUtil.getOrganization(SessionUtil.getMasterOrgId()).getName());
					pdf.setStrTieude("DANH SÁCH ĐƠN THƯ");
					pdf.setStrSubTieude("Của cán bộ "+SessionUtil.getUser().getFirstName());
					pdf.setTblChitiet(tblDanhSach);
					pdf.createPdf();
					WindowViewPDF windowViewPDF=new WindowViewPDF();
					windowViewPDF.setFilename("danhsachdonthu"+new Date().getTime()+".pdf"); 
					windowViewPDF.setCaption("DANH SÁCH ĐƠN THƯ TRA CỨU");
					windowViewPDF.loadData(pdf.getByteArrayOutputStream());
					UI.getCurrent().addWindow(windowViewPDF);*/
					Date startDate = null;
					Date endDate = null;
					String dateType = null;
					
					if(cbNgayNhap.getValue())
					{
						dateType="ngaynhapdon";
						startDate = dfNgayNhapStart.getValue();
						endDate = dfNgayNhapStart.getValue();
					}
					else
					{
						if(cbNgayThuLy.getValue())
						{
							dateType="ngaythuly";
							startDate = dfNgayThuLyStart.getValue();
							endDate = dfNgayGiaiQuyetEnd.getValue();
						}
						else
						{
							if(cbNgayThuLy.getValue())
							{
								dateType="ngaygiaiquyet";
								startDate = dfNgayGiaiQuyetStart.getValue();
								endDate = dfNgayGiaiQuyetEnd.getValue();
							}
						}
					}
					
					TraCuuExcel traCuuExcel = new TraCuuExcel(startDate, endDate, dateType, list);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnLinhVuc.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				WindowLinhVucHoSo wdLinhVuc = new WindowLinhVucHoSo((int)cmbLoaiDon.getValue(), listLinhVuc);

				UI.getCurrent().addWindow(wdLinhVuc);

				wdLinhVuc.getBtnOk().addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						listLinhVuc.clear();
						listLinhVuc.addAll(wdLinhVuc.getListLinhVucSelected());
						loadDisplayLinhVuc();
					}
				});
			}
		});
		cmbLoaiDon.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				listLinhVuc.clear();
				loadDisplayLinhVuc();
				if(cmbLoaiDon.getValue()!=null)
				{
					if((int)cmbLoaiDon.getValue()>=1 && (int)cmbLoaiDon.getValue()<=3)
					{
						btnLinhVuc.setEnabled(true);
					}
					else
					{
						btnLinhVuc.setEnabled(false);
					}
				}
				else
				{
					btnLinhVuc.setEnabled(false);
				}
			}
		});
		
		tblDanhSach.addListener(new ItemClickEvent.ItemClickListener() {

			@Override
			public void itemClick(ItemClickEvent event) {
				if(event.isDoubleClick())
				{
					Window wdChiTiet = new Window();

					UI.getCurrent().addWindow(wdChiTiet);
					try {
						wdChiTiet.setContent(new ChiTietDonThuTCDGeneralLayout((int)event.getItemId()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					wdChiTiet.setSizeFull();
					wdChiTiet.setModal(true);
					wdChiTiet.setCaption("Thông tin chi tiết đơn thư");
				}
			}
		});
	}
	public void loadDisplayLinhVuc()
	{
		vLinhVucDisplay.removeAllComponents();
		if(!listLinhVuc.isEmpty())
		{
			rowDisplayLinhVuc.setVisible(true);
			for(String idLinhVuc : listLinhVuc)
			{
				ResponsiveRow rowTmp = new ResponsiveRow();
				HashMap<String, String> lv = DonThuModule.getLinhVucLevel(idLinhVuc);
				try {
					if(lv.containsKey("lv2"))
						rowTmp.addColumn().withComponent(new Label("<b style='color: #19619a'>Loại lĩnh vực: </b>"+svDanhMuc.getLinhVuc(lv.get("lv2")).getName(),ContentMode.HTML)).withDisplayRules(12, 12, 4, 4);
					if(lv.containsKey("lv3"))
						rowTmp.addColumn().withComponent(new Label("<b style='color: #19619a'>Chi tiết lĩnh vực: </b>"+svDanhMuc.getLinhVuc(lv.get("lv3")).getName(),ContentMode.HTML)).withDisplayRules(12, 12, 4, 4);
					if(lv.containsKey("lv4"))
						rowTmp.addColumn().withComponent(new Label("<b style='color: #19619a'>Diễn giải: </b>"+svDanhMuc.getLinhVuc(lv.get("lv4")).getName(),ContentMode.HTML)).withDisplayRules(12, 12, 4, 4);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				vLinhVucDisplay.addComponent(rowTmp);
			}
		}
		else
		{
			rowDisplayLinhVuc.setVisible(false);
		}
	}

	@SuppressWarnings("unchecked")
	public void loadData(TraCuuBean modelTraCuu) throws Exception
	{
		container.removeAllItems();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		list = svDonThu.traCuuDonThu(modelTraCuu);

		int i = 0;
		for(DonThuBean model : list)
		{
			String noiDungDon = "";
			String loaiDonThu = "";
			String linhVuc = "<div style='line-height: 1.5'>";

			if(model.getMaDonThu()!=0)
			{
				noiDungDon = model.getNoiDungDonThu();
			}

			for(LoaiDonThuEnum e : LoaiDonThuEnum.values())
			{
				if(e.getType()==model.getLoaiDonThu())
					loaiDonThu = e.getName();
			}

			for(String idLinhVuc : svDonThu.getLinhVucList(model.getMaDonThu()))
			{
				HashMap<String, String> lv = DonThuModule.getLinhVucLevel(idLinhVuc);
				try {
					linhVuc+="- ";
					if(lv.containsKey("lv2"))
						linhVuc+=svDanhMuc.getLinhVuc(lv.get("lv2")).getName()+"<b> / </b>";
					if(lv.containsKey("lv3"))
						linhVuc+=svDanhMuc.getLinhVuc(lv.get("lv3")).getName()+"<b> / </b>";
					if(lv.containsKey("lv4"))
						linhVuc+=svDanhMuc.getLinhVuc(lv.get("lv3")).getName();
					linhVuc+="<br/>";
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			linhVuc+="</div>";
			
			ThongTinDonThuBean modelThongTinDon = svDonThu.getThongTinDonThu(model.getMaDonThu(), SessionUtil.getOrgId());
			
			Item item = container.addItem(++i);

			item.getItemProperty(STT).setValue(i);
			item.getItemProperty(LOAINGUOIDUNGDON).setValue(new Label(DonThuModule.returnLoaiNguoiDiKNTCForTable(model.getLoaiNguoiDiKNTC(), model.getSoNguoiDiKNTC(), model.getSoNguoiDaiDien()),ContentMode.HTML));
			item.getItemProperty(NGUOIDUNGDON).setValue(new Label(DonThuServiceUtil.returnTenNguoiDaiDienDonThu(model),ContentMode.HTML));
			item.getItemProperty(NOIDUNG).setValue(new Label(noiDungDon,ContentMode.HTML));
			item.getItemProperty(LOAIDONTHU).setValue(loaiDonThu);
			item.getItemProperty(LINHVUC).setValue(new Label(linhVuc,ContentMode.HTML));
			item.getItemProperty(NGAYNHAPDON).setValue(sdf.format(modelThongTinDon.getNgayNhan()));
			item.getItemProperty(NGUOINHAP).setValue(UserLocalServiceUtil.getUser(model.getUserNhapDon()).getFirstName());
		}
		tblDanhSach.setContainerDataSource(container);
	}

	public boolean validateForm()
	{
		if(cbNgayNhap.getValue())
		{
			if(dfNgayNhapStart.isEmpty())
			{
				Notification.show("Vui lòng chọn ngày bắt đầu",Type.WARNING_MESSAGE);
				dfNgayNhapStart.focus();
				return false;
			}
			if(dfNgayNhapEnd.isEmpty())
			{
				Notification.show("Vui lòng chọn ngày kết thúc",Type.WARNING_MESSAGE);
				dfNgayNhapEnd.focus();
				return false;
			}
		}
		if(cbNgayThuLy.getValue())
		{
			if(dfNgayThuLyStart.isEmpty())
			{
				Notification.show("Vui lòng chọn ngày bắt đầu",Type.WARNING_MESSAGE);
				dfNgayThuLyStart.focus();
				return false;
			}
			if(dfNgayThuLyEnd.isEmpty())
			{
				Notification.show("Vui lòng chọn ngày kết thúc",Type.WARNING_MESSAGE);
				dfNgayThuLyEnd.focus();
				return false;
			}
		}
		if(cbNgayGiaiQuyet.getValue())
		{
			if(dfNgayGiaiQuyetStart.isEmpty())
			{
				Notification.show("Vui lòng chọn ngày bắt đầu",Type.WARNING_MESSAGE);
				dfNgayGiaiQuyetStart.focus();
				return false;
			}
			if(dfNgayGiaiQuyetEnd.isEmpty())
			{
				Notification.show("Vui lòng chọn ngày kết thúc",Type.WARNING_MESSAGE);
				dfNgayGiaiQuyetEnd.focus();
				return false;
			}
		}
		return true;
	}
}
