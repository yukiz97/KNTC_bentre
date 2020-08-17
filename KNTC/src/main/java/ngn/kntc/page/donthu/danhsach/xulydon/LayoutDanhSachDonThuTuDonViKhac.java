package ngn.kntc.page.donthu.danhsach.xulydon;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ngn.kntc.UI.kntcUI;
import ngn.kntc.beans.DonThuBean;
import ngn.kntc.beans.FilterDonNhanTuDonViKhacBean;
import ngn.kntc.beans.ThongTinDonThuBean;
import ngn.kntc.enums.HanXuLyChuaEnum;
import ngn.kntc.enums.LoaiDonThuEnum;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.modules.KNTCProps;
import ngn.kntc.page.donthu.chitiet.ChiTietDonThuTCDGeneralLayout;
import ngn.kntc.page.donthu.danhsach.LayoutDanhSachDonThu;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.SessionUtil;

import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

public class LayoutDanhSachDonThuTuDonViKhac extends LayoutDanhSachDonThu{
	private ComboBox cmbHanXuLy = new ComboBox();
	private DateField dfNgayNhanStart = new DateField();
	private DateField dfNgayNhanEnd = new DateField();
	private ComboBox cmbLoaiDonThu = new ComboBox();
	
	final String STT = "STT";
	final String NGUOIDUNGDON = "Người đứng đơn";
	final String NOIDUNG = "Nội dung đơn thư";
	//final String NGUOIBIKNTC = "Người bị khiếu tố";
	final String LOAIDONTHU = "Loại đơn thư";
	final String DONVICHUYENDEN = "Đơn vị chuyển đến";
	final String NGAYNHAPDON = "Ngày nhập đơn";
	final String NGAYCHUYEN = "Ngày nhận đơn chuyển";
	final String NGUOINHAP = "Người nhập";
	
	public LayoutDanhSachDonThuTuDonViKhac() {
		lblMainCaption.setValue(FontAwesome.BOOK.getHtml()+" Quản lý danh sách đơn thư đơn vị khác chuyển đến");
		lblSubCaption.setValue("+ Quản lý danh sách đơn thư được đơn vị khác chuyển đến do không đủ thẩm quyền xử lý");
		
		hFilter.addComponents(cmbHanXuLy,new Label("<b>Ngày nhận đơn</b>",ContentMode.HTML),dfNgayNhanStart,new Label("-"),dfNgayNhanEnd,new Label("<b>Loại đơn thư</b>",ContentMode.HTML),cmbLoaiDonThu);
		
		buildTable();
		loadDefaultValue();
		try {
			loadData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void buildTable() {
		super.buildTable();
		
		container.addContainerProperty(STT, Integer.class, null);
		container.addContainerProperty(NGUOIDUNGDON, Label.class, null);
		container.addContainerProperty(NOIDUNG, Label.class, null);
		//container.addContainerProperty(NGUOIBIKNTC, String.class, null);
		container.addContainerProperty(LOAIDONTHU, String.class, null);
		container.addContainerProperty(DONVICHUYENDEN, String.class, null);
		container.addContainerProperty(NGAYNHAPDON, String.class, null);
		container.addContainerProperty(NGAYCHUYEN, Label.class, null);
		container.addContainerProperty(NGUOINHAP, String.class, null);
		
		tblDanhSach.setContainerDataSource(container);
		
		tblDanhSach.setColumnAlignment(STT, Align.CENTER);
		tblDanhSach.setColumnAlignment(LOAIDONTHU, Align.CENTER);
		tblDanhSach.setColumnAlignment(NGAYNHAPDON, Align.CENTER);
		tblDanhSach.setColumnAlignment(NGAYCHUYEN, Align.CENTER);
		
		tblDanhSach.setColumnExpandRatio(NOIDUNG, 1.0f);
		
		/*tblDanhSach.setColumnWidth(STT, 42);
		tblDanhSach.setColumnWidth(NGUOIDUNGDON, 154);
		tblDanhSach.setColumnWidth(NOIDUNG, 306);
		tblDanhSach.setColumnWidth(LOAIDONTHU, 86);
		tblDanhSach.setColumnWidth(NGAYNHAPDON, 100);
		tblDanhSach.setColumnWidth(NGAYCHUYEN, 140);*/
	}

	@SuppressWarnings({ "unchecked", "static-access" })
	@Override
	public void loadData() throws Exception {
		FilterDonNhanTuDonViKhacBean modelFilter = new FilterDonNhanTuDonViKhacBean();
		modelFilter.setHanXuLy((int)cmbHanXuLy.getValue());
		modelFilter.setKeyWord(txtSearch.getValue().trim());
		modelFilter.setNgayNhanStart(dfNgayNhanStart.getValue());
		modelFilter.setNgayNhanEnd(dfNgayNhanEnd.getValue());
		modelFilter.setLoaiDonThu((int)cmbLoaiDonThu.getValue());
		
		container.removeAllItems();
		List<DonThuBean> list = svDonThu.getDonThuDonViKhacChuyenDen(modelFilter);
		
		int i = 0;
		for(DonThuBean model : list)
		{
			String noiDungDon = "";
			String loaiDonThu = "";
			String donViDaChuyen = "";
			String strDisplayHanXuLy = "";
			
			ThongTinDonThuBean modelThongTinDon = svDonThu.getThongTinDonThu(model.getMaDonThu(), SessionUtil.getOrgId());
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
			
			if(model.getMaDonThu()!=0)
			{
				noiDungDon = model.getNoiDungDonThu();
				if(noiDungDon.length()>120)
				{
					noiDungDon = noiDungDon.substring(0, 120)+"<b>.......</b>";
				}
			}
			
			for(LoaiDonThuEnum e : LoaiDonThuEnum.values())
			{
				if(e.getType()==model.getLoaiDonThu())
					loaiDonThu = e.getName();
			}
			
			donViDaChuyen = OrganizationLocalServiceUtil.getOrganization(modelThongTinDon.getOrgChuyen()).getName();
			
			Item item = container.addItem(model.getMaDonThu());
			
			item.getItemProperty(STT).setValue(++i);
			item.getItemProperty(NGUOIDUNGDON).setValue(new Label(DonThuModule.returnLoaiNguoiDiKNTCForTable(model.getLoaiNguoiDiKNTC(), model.getSoNguoiDiKNTC(), model.getSoNguoiDaiDien())+"<b>"+DonThuServiceUtil.returnTenNguoiDaiDienDonThu(model)+"</b>",ContentMode.HTML));
			item.getItemProperty(NOIDUNG).setValue(new Label(noiDungDon,ContentMode.HTML));
			//item.getItemProperty(NGUOIBIKNTC).setValue(svDonThu.returnTenNguoiBiKNTC(model));
			item.getItemProperty(LOAIDONTHU).setValue(loaiDonThu);
			item.getItemProperty(DONVICHUYENDEN).setValue(donViDaChuyen);
			item.getItemProperty(NGAYNHAPDON).setValue(sdfDatetime.format(model.getNgayNhapDon()));
			item.getItemProperty(NGAYCHUYEN).setValue(new Label(sdfDatetime.format(modelThongTinDon.getNgayNhan())+"<br/>"+strDisplayHanXuLy,ContentMode.HTML));
			item.getItemProperty(NGUOINHAP).setValue(UserLocalServiceUtil.getUser(model.getUserNhapDon()).getFirstName());
		}
		tblDanhSach.setContainerDataSource(container);
	}

	@Override
	public void loadDefaultValue() {
		DonThuModule.returnComboboxLoaiDonThu(cmbLoaiDonThu, true);		
		
		for(HanXuLyChuaEnum e : HanXuLyChuaEnum.values())
		{
			cmbHanXuLy.addItem(e.getType());
			cmbHanXuLy.setItemCaption(e.getType(), e.getName());
		}
		cmbHanXuLy.setNullSelectionAllowed(false);
		cmbHanXuLy.select(HanXuLyChuaEnum.tatca.getType());
	}

	@Override
	public boolean validateFilter() {
		if(dfNgayNhanStart.getValue()!=null && dfNgayNhanEnd.getValue()==null)
		{
			Notification.show("Vui lòng nhập ngày bắt đầu để tìm kiếm",Type.WARNING_MESSAGE);
			dfNgayNhanStart.focus();
			return false;
		}
		if(dfNgayNhanStart.getValue()==null && dfNgayNhanEnd.getValue()!=null)
		{
			Notification.show("Vui lòng nhập ngày kết thúc để tìm kiếm",Type.WARNING_MESSAGE);
			dfNgayNhanEnd.focus();
			return false;
		}
		return true;
	}
}
