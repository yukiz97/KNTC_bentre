package ngn.kntc.page.donthu.danhsach.xulydon;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.liferay.portal.service.UserLocalServiceUtil;
import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.themes.ValoTheme;

import ngn.kntc.UI.kntcUI;
import ngn.kntc.beans.DonThuBean;
import ngn.kntc.beans.FilterDonDaTiepNhanBean;
import ngn.kntc.beans.ThongTinDonThuBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.enums.HanXuLyChuaEnum;
import ngn.kntc.enums.LoaiDonThuEnum;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.modules.KNTCProps;
import ngn.kntc.page.donthu.chitiet.ChiTietDonThuTCDGeneralLayout;
import ngn.kntc.page.donthu.danhsach.LayoutDanhSachDonThu;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.SessionUtil;
import ngn.kntc.windows.WindowTieuChiXuatDanhSachDonDaXuLy;

public class LayoutDanhSachDonThuDaTiepNhan extends LayoutDanhSachDonThu{
	private Button btnExport = new Button("Xuất Excel",FontAwesome.FILE_EXCEL_O);
	
	private ComboBox cmbHanXuLy = new ComboBox();
	private DateField dfNgayNhapStart = new DateField();
	private DateField dfNgayNhapEnd = new DateField();
	private ComboBox cmbLoaiDonThu = new ComboBox();
	private ComboBox cmbDieuKienXuLy = new ComboBox();
	
	final String STT = "STT";
	final String NGUOIDUNGDON = "Người đứng đơn";
	final String NOIDUNG = "Nội dung đơn thư";
	//final String NGUOIBIKNTC = "Người bị khiếu tố";
	final String LOAIDONTHU = "Loại đơn thư";
	final String NGAYNHAPDON = "Ngày nhập đơn";
	final String NGUOINHAP = "Người nhập";
	final String DKXL = "Điều kiện XL";
	final String NGUONDON = "Nguồn đơn";
	
	public LayoutDanhSachDonThuDaTiepNhan() {
		//hSearchLayout.addComponent(btnExport);
		btnExport.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		lblMainCaption.setValue(FontAwesome.BOOK.getHtml()+" Quản lý danh sách đơn thư đã tiếp nhận");
		lblSubCaption.setValue("+ Quản lý danh sách đơn thư được cán bộ Tiếp Công Dân đơn vị nhận và trực tiếp nhập liệu<br/>+ Quản lý đơn thư được tiếp nhận qua việc tiếp công dân");
		
		hFilter.addComponents(cmbHanXuLy,new Label("<b>Ngày nhập</b>",ContentMode.HTML),dfNgayNhapStart,new Label("-"),dfNgayNhapEnd,new Label("<b>Loại đơn thư</b>",ContentMode.HTML),cmbLoaiDonThu,new Label("<b>Điều kiện xử lý</b>",ContentMode.HTML),cmbDieuKienXuLy);
		
		cmbDieuKienXuLy.setNullSelectionAllowed(false);
		
		buildTable();
		loadDefaultValue();
		try {
			loadData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		btnExport.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				WindowTieuChiXuatDanhSachDonDaXuLy wdExport = new WindowTieuChiXuatDanhSachDonDaXuLy();
				UI.getCurrent().addWindow(wdExport);
			}
		});
	}
	
	@Override
	public void loadDefaultValue() {
		DonThuModule.returnComboboxLoaiDonThu(cmbLoaiDonThu, true);
		
		// Vì trong database cột là đơn không đủ điều kiện xử lý => đủ là 0, không đủ là 1
		cmbDieuKienXuLy.addItem(-1);
		cmbDieuKienXuLy.setItemCaption(-1, "Tất cả đơn");
		cmbDieuKienXuLy.addItem(0);
		cmbDieuKienXuLy.setItemCaption(0, "Đơn đủ điều kiện xử lý");
		cmbDieuKienXuLy.addItem(1);
		cmbDieuKienXuLy.setItemCaption(1, "Đơn không đủ điều kiện xử lý (Lưu trữ)");
		
		cmbDieuKienXuLy.select(0);
		
		for(HanXuLyChuaEnum e : HanXuLyChuaEnum.values())
		{
			cmbHanXuLy.addItem(e.getType());
			cmbHanXuLy.setItemCaption(e.getType(), e.getName());
		}
		cmbHanXuLy.setNullSelectionAllowed(false);
		cmbHanXuLy.select(HanXuLyChuaEnum.tatca.getType());
	}
	
	public void buildTable() {
		super.buildTable();
		
		container.addContainerProperty(STT, Integer.class, null);
		container.addContainerProperty(NGUOIDUNGDON, Label.class, null);
		container.addContainerProperty(NOIDUNG, Label.class, null);
		//container.addContainerProperty(NGUOIBIKNTC, String.class, null);
		container.addContainerProperty(LOAIDONTHU, String.class, null);
		container.addContainerProperty(NGAYNHAPDON, Label.class, null);
		container.addContainerProperty(NGUOINHAP, String.class, null);
		container.addContainerProperty(NGUONDON, String.class, null);
		container.addContainerProperty(DKXL, Label.class, null);
		
		tblDanhSach.setContainerDataSource(container);
		tblDanhSach.setImmediate(true);
		
		tblDanhSach.setColumnExpandRatio(NOIDUNG, 1.0f);
		
		tblDanhSach.setColumnAlignment(DKXL, Align.CENTER);
	}

	@SuppressWarnings({ "unchecked"})
	@Override
	public void loadData() throws Exception {
		FilterDonDaTiepNhanBean modelFilter = new FilterDonDaTiepNhanBean();
		modelFilter.setHanXuLy((int)cmbHanXuLy.getValue());
		modelFilter.setKeyWord(txtSearch.getValue().trim());
		modelFilter.setNgayNhapStart(dfNgayNhapStart.getValue());
		modelFilter.setNgayNhapEnd(dfNgayNhapEnd.getValue());
		modelFilter.setLoaiDonThu((int)cmbLoaiDonThu.getValue());
		modelFilter.setDieuKienXuLy((int)cmbDieuKienXuLy.getValue());
		
		container.removeAllItems();
		List<DonThuBean> list = svDonThu.getDonThuDaTiepNhan(modelFilter);
		
		int i = 0;
		for(DonThuBean model : list)
		{
			String noiDungDon = model.getNoiDungDonThu();
			String dkXl = model.isDonKhongDuDieuKienXuLy()?"<span style='color:red'>"+FontAwesome.REMOVE.getHtml()+"</span>":"<span style='color: green'>"+FontAwesome.CHECK.getHtml()+"</span>";
			String strDisplayHanXuLy = "";
			ThongTinDonThuBean modelThongTinDon = svDonThu.getThongTinDonThu(model.getMaDonThu(), SessionUtil.getOrgId());
			if(!model.isDonKhongDuDieuKienXuLy())
			{
				int dateDiff = (int) TimeUnit.DAYS.convert(new Date().getTime()-modelThongTinDon.getNgayNhan().getTime(), TimeUnit.MILLISECONDS);
				int hanXuLy = Integer.parseInt(KNTCProps.getProperty("han.xu.ly"));
				if(dateDiff<=hanXuLy)
				{
					String color = "";
					if(dateDiff<=10)
						color="green";
					else
						color="orange";
					strDisplayHanXuLy="<b style='color:"+color+"'>Còn "+(hanXuLy-dateDiff)+" đến hạn XL</b>";
				}
				else
				{
					strDisplayHanXuLy="<b style='color:red'>Quá hạn XL "+(dateDiff-hanXuLy)+" ngày</b>";
				}
			}
			if(noiDungDon.length()>120)
			{
				noiDungDon = noiDungDon.substring(0, 120)+"<b>.......</b>";
			}
			
			Item item = container.addItem(model.getMaDonThu());
			
			item.getItemProperty(STT).setValue(++i);
			item.getItemProperty(NGUOIDUNGDON).setValue(new Label(DonThuModule.returnLoaiNguoiDiKNTCForTable(model.getLoaiNguoiDiKNTC(), model.getSoNguoiDiKNTC(), model.getSoNguoiDaiDien())+"<b>"+DonThuServiceUtil.returnTenNguoiDaiDienDonThu(model)+"</b>",ContentMode.HTML));
			item.getItemProperty(NOIDUNG).setValue(new Label(noiDungDon,ContentMode.HTML));
			//item.getItemProperty(NGUOIBIKNTC).setValue(svDonThu.returnTenNguoiBiKNTC(model));
			item.getItemProperty(LOAIDONTHU).setValue(DonThuModule.returnLoaiDonThuNameOnKey(model.getLoaiDonThu()));
			item.getItemProperty(NGAYNHAPDON).setValue(new Label(sdfDatetime.format(model.getNgayNhapDon())+"<br/>"+strDisplayHanXuLy,ContentMode.HTML));
			item.getItemProperty(NGUOINHAP).setValue(UserLocalServiceUtil.getUser(model.getUserNhapDon()).getFirstName());
			item.getItemProperty(NGUONDON).setValue(DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.nguondon.getName(), svDonThu.getThongTinDonThu(model.getMaDonThu(), SessionUtil.getOrgId()).getNguonDonDen()).getName());
			item.getItemProperty(DKXL).setValue(new Label("<div style='text-align: center'>"+dkXl+"</div>",ContentMode.HTML));
		}
		tblDanhSach.setContainerDataSource(container);
	}

	@Override
	public boolean validateFilter() {
		if(dfNgayNhapStart.getValue()!=null && dfNgayNhapEnd.getValue()==null)
		{
			Notification.show("Vui lòng nhập ngày bắt đầu để tìm kiếm",Type.WARNING_MESSAGE);
			dfNgayNhapStart.focus();
			return false;
		}
		if(dfNgayNhapStart.getValue()==null && dfNgayNhapEnd.getValue()!=null)
		{
			Notification.show("Vui lòng nhập ngày kết thúc để tìm kiếm",Type.WARNING_MESSAGE);
			dfNgayNhapEnd.focus();
			return false;
		}
		return true;
	}
}
