package ngn.kntc.page.donthu.danhsach.thulydon;

import java.sql.SQLException;
import java.util.List;

import com.liferay.portal.service.UserLocalServiceUtil;
import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

import ngn.kntc.UI.kntcUI;
import ngn.kntc.beans.DanhMucBean;
import ngn.kntc.beans.DonThuBean;
import ngn.kntc.beans.FilterDonCanThuLyBean;
import ngn.kntc.beans.FilterDonDaCoKetQuaBean;
import ngn.kntc.beans.KetQuaXuLyBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.enums.LoaiDonThuEnum;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.page.donthu.chitiet.ChiTietDonThuTCDGeneralLayout;
import ngn.kntc.page.donthu.danhsach.LayoutDanhSachDonThu;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.SessionUtil;

public class LayoutDanhSachDonThuCanThuLy extends LayoutDanhSachDonThu{
	private DateField dfNgayXuLyStart = new DateField();
	private DateField dfNgayXuLyEnd = new DateField();
	private ComboBox cmbLoaiDonThu = new ComboBox();
	
	final String STT = "STT";
	final String NGUOIDUNGDON = "Người đứng đơn";
	final String NOIDUNG = "Nội dung đơn thư";
	//final String NGUOIBIKNTC = "Người bị khiếu tố";
	final String LOAIDONTHU = "Loại đơn thư";
	final String NGAYXULY = "Ngày xử lý";
	final String NGAYNHAPDON = "Ngày nhập đơn";
	final String NGUOINHAP = "Người nhập";
	
	public LayoutDanhSachDonThuCanThuLy() {
		lblMainCaption.setValue(FontAwesome.BOOK.getHtml()+" Quản lý danh sách đơn thư cần thụ lý");
		lblSubCaption.setValue("+  Quản lý danh sách đơn thư chưa được thụ lý và đã có kết quả xử lý là thụ lý giải quyết");
		
		hFilter.addComponents(new Label("<b>Ngày xử lý</b>",ContentMode.HTML),dfNgayXuLyStart,new Label("-"),dfNgayXuLyEnd,new Label("<b>Loại đơn thư</b>",ContentMode.HTML),cmbLoaiDonThu);
		
		loadDefaultValue();
		buildTable();
		try {
			loadData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
		container.addContainerProperty(NGAYXULY, String.class, null);
		container.addContainerProperty(NGAYNHAPDON, String.class, null);
		container.addContainerProperty(NGUOINHAP, String.class, null);
		
		tblDanhSach.setContainerDataSource(container);
		tblDanhSach.setColumnExpandRatio(NOIDUNG, 1.0f);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadData() throws Exception {
		FilterDonCanThuLyBean modelFilter = new FilterDonCanThuLyBean();
		modelFilter.setKeyWord(txtSearch.getValue().trim());
		modelFilter.setNgayXuLyStart(dfNgayXuLyStart.getValue());
		modelFilter.setNgayXuLyEnd(dfNgayXuLyEnd.getValue());
		modelFilter.setLoaiDonThu((int)cmbLoaiDonThu.getValue());
		
		container.removeAllItems();
		List<DonThuBean> list = svDonThu.getDonThuCanThuLy(modelFilter);
		
		int i = 0;
		for(DonThuBean model : list)
		{
			String noiDungDon = "";
			String loaiDonThu = "";
			
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
			
			KetQuaXuLyBean modelKQXL = svQuaTrinh.getKetQuaXuLy(model.getMaDonThu(), SessionUtil.getOrgId());
			
			Item item = container.addItem(model.getMaDonThu());
			
			item.getItemProperty(STT).setValue(++i);
			item.getItemProperty(NGUOIDUNGDON).setValue(new Label(DonThuModule.returnLoaiNguoiDiKNTCForTable(model.getLoaiNguoiDiKNTC(), model.getSoNguoiDiKNTC(), model.getSoNguoiDaiDien())+"<b>"+DonThuServiceUtil.returnTenNguoiDaiDienDonThu(model)+"</b>",ContentMode.HTML));
			item.getItemProperty(NOIDUNG).setValue(new Label(noiDungDon,ContentMode.HTML));
			//item.getItemProperty(NGUOIBIKNTC).setValue(svDonThu.returnTenNguoiBiKNTC(model));
			item.getItemProperty(LOAIDONTHU).setValue(loaiDonThu);
			item.getItemProperty(NGAYXULY).setValue(sdfDate.format(modelKQXL.getNgayXuLy()));
			item.getItemProperty(NGAYNHAPDON).setValue(sdfDatetime.format(model.getNgayNhapDon()));
			item.getItemProperty(NGUOINHAP).setValue(UserLocalServiceUtil.getUser(model.getUserNhapDon()).getFirstName());
		}
		tblDanhSach.setContainerDataSource(container);
	}
	
	@Override
	public void loadDefaultValue() {
		DonThuModule.returnComboboxLoaiDonThu(cmbLoaiDonThu, true);	
	}

	@Override
	public boolean validateFilter() {
		if(dfNgayXuLyStart.getValue()!=null && dfNgayXuLyEnd.getValue()==null)
		{
			Notification.show("Vui lòng nhập ngày bắt đầu để tìm kiếm",Type.WARNING_MESSAGE);
			dfNgayXuLyStart.focus();
			return false;
		}
		if(dfNgayXuLyStart.getValue()==null && dfNgayXuLyEnd.getValue()!=null)
		{
			Notification.show("Vui lòng nhập ngày kết thúc để tìm kiếm",Type.WARNING_MESSAGE);
			dfNgayXuLyEnd.focus();
			return false;
		}
		return true;
	}
}
