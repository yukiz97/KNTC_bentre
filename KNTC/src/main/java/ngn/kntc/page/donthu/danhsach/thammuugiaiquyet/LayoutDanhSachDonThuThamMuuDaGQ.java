package ngn.kntc.page.donthu.danhsach.thammuugiaiquyet;

import java.sql.SQLException;
import java.util.List;

import ngn.kntc.UI.kntcUI;
import ngn.kntc.beans.DanhMucBean;
import ngn.kntc.beans.DonThuBean;
import ngn.kntc.beans.FilterDonCanGiaiQuyetBean;
import ngn.kntc.beans.FilterDonDangThiHanhBean;
import ngn.kntc.beans.QuyetDinhGiaiQuyetBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.enums.LoaiDonThuEnum;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.page.donthu.chitiet.ChiTietDonThuTCDGeneralLayout;
import ngn.kntc.page.donthu.danhsach.LayoutDanhSachDonThu;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.DonThuServiceUtil;

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
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

public class LayoutDanhSachDonThuThamMuuDaGQ extends LayoutDanhSachDonThu{
	private DateField dfNgayThuLyStart = new DateField();
	private DateField dfNgayThuLyEnd = new DateField();
	private ComboBox cmbLoaiDonThu = new ComboBox();
	private ComboBox cmbKetQuaGiaiQuyet = new ComboBox();
	private ComboBox cmbHinhThucGiaiQuyet = new ComboBox();
	
	final String STT = "STT";
	final String NGUOIDUNGDON = "Người đứng đơn";
	final String NOIDUNG = "Nội dung đơn thư";
	//final String NGUOIBIKNTC = "Người bị khiếu tố";
	final String LOAIDONTHU = "Loại đơn thư";
	final String KETQUAGIAIQUYET = "Kết quả giải quyết";
	final String HINHTHUCGIAIQUYET = "Hình thức giải quyết";
	final String NGAYGIAIQUYET = "Ngày giải quyết";
	final String NGAYNHAPDON = "Ngày nhập đơn";
	
	private DanhMucServiceUtil svDanhMuc = new DanhMucServiceUtil();
	
	public LayoutDanhSachDonThuThamMuuDaGQ() {
		lblMainCaption.setValue(FontAwesome.BOOK.getHtml()+" Quản lý danh sách đơn thư tham mưu đã giải quyết");
		lblSubCaption.setValue("+ Quản lý danh sách đơn thư tham mưu cho đơn vị khác và đã ban hành quyết định giải quyết");
		
		hFilter.addComponents(new Label("<b>Ngày thụ lý</b>",ContentMode.HTML),dfNgayThuLyStart,new Label("-"),dfNgayThuLyEnd,new Label("<b>Loại đơn thư</b>",ContentMode.HTML),cmbLoaiDonThu,new Label("<b>Kết quả giải quyết</b>",ContentMode.HTML),cmbKetQuaGiaiQuyet,new Label("<b>Hình thức giải quyết</b>",ContentMode.HTML),cmbHinhThucGiaiQuyet);

		loadDefaultValue();
		buildTable();
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
		container.addContainerProperty(KETQUAGIAIQUYET, String.class, null);
		container.addContainerProperty(HINHTHUCGIAIQUYET, String.class, null);
		container.addContainerProperty(NGAYGIAIQUYET, String.class, null);
		container.addContainerProperty(NGAYNHAPDON, String.class, null);
		
		tblDanhSach.setContainerDataSource(container);
		tblDanhSach.setColumnExpandRatio(NOIDUNG, 1.0f);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadData() throws Exception {
		FilterDonDangThiHanhBean modelFilter = new FilterDonDangThiHanhBean();
		modelFilter.setKeyWord(txtSearch.getValue().trim());
		modelFilter.setNgayGiaiQuyetStart(dfNgayThuLyStart.getValue());
		modelFilter.setNgayGiaiQuyetEnd(dfNgayThuLyEnd.getValue());
		modelFilter.setLoaiDonThu((int)cmbLoaiDonThu.getValue());
		modelFilter.setKetQuaGiaiQuyet((int)cmbKetQuaGiaiQuyet.getValue());
		modelFilter.setHinhThucGiaiQuyet((int)cmbHinhThucGiaiQuyet.getValue());
		
		container.removeAllItems();
		List<DonThuBean> list = svDonThu.getDonThuThamMuuDaGiaiQuyet(modelFilter);
		
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
			
			QuyetDinhGiaiQuyetBean modelQDGQ = svQuaTrinh.getQuyetDinhGiaiQuyet(model.getMaDonThu());
			
			Item item = container.addItem(model.getMaDonThu());
			
			item.getItemProperty(STT).setValue(++i);
			item.getItemProperty(NGUOIDUNGDON).setValue(new Label(DonThuModule.returnLoaiNguoiDiKNTCForTable(model.getLoaiNguoiDiKNTC(), model.getSoNguoiDiKNTC(), model.getSoNguoiDaiDien())+"<b>"+DonThuServiceUtil.returnTenNguoiDaiDienDonThu(model)+"</b>",ContentMode.HTML));
			item.getItemProperty(NOIDUNG).setValue(new Label(noiDungDon,ContentMode.HTML));
		//	item.getItemProperty(NGUOIBIKNTC).setValue(svDonThu.returnTenNguoiBiKNTC(model));
			item.getItemProperty(LOAIDONTHU).setValue(loaiDonThu);
			item.getItemProperty(KETQUAGIAIQUYET).setValue(DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.loaiquyetdinh.getName(), modelQDGQ.getLoaiKetQuaGiaiQuyet()).getName());
			item.getItemProperty(HINHTHUCGIAIQUYET).setValue(DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.hinhthucgiaiquyet.getName(), modelQDGQ.getHinhThucGiaiQuyet()).getName());
			item.getItemProperty(NGAYGIAIQUYET).setValue(sdfDate.format(modelQDGQ.getNgayBanHanh()));
			item.getItemProperty(NGAYNHAPDON).setValue(sdfDatetime.format(model.getNgayNhapDon()));
		}
		tblDanhSach.setContainerDataSource(container);
	}
	
	@Override
	public void loadDefaultValue() {
		DonThuModule.returnComboboxLoaiDonThu(cmbLoaiDonThu, true);	
		
		cmbHinhThucGiaiQuyet.addItem(0);
		cmbHinhThucGiaiQuyet.setItemCaption(0, "Tất cả");
		List<DanhMucBean> listHinhThuc;
		try {
			listHinhThuc = svDanhMuc.getDanhMucList(DanhMucTypeEnum.hinhthucgiaiquyet.getName(), DanhMucTypeEnum.hinhthucgiaiquyet.getIdType());
			for(DanhMucBean model : listHinhThuc)
			{
				cmbHinhThucGiaiQuyet.addItem(model.getIntMa());
				cmbHinhThucGiaiQuyet.setItemCaption(model.getIntMa(), model.getName());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		cmbHinhThucGiaiQuyet.setNullSelectionAllowed(false);
		cmbHinhThucGiaiQuyet.setValue(0);
		/* List Loại kết quả */
		cmbKetQuaGiaiQuyet.addItem(0);
		cmbKetQuaGiaiQuyet.setItemCaption(0,"Tất cả");
		List<DanhMucBean> listLoaiKetQua;
		try {
			listLoaiKetQua = svDanhMuc.getDanhMucList(DanhMucTypeEnum.loaiquyetdinh.getName(), DanhMucTypeEnum.loaiquyetdinh.getIdType());
			for(DanhMucBean model : listLoaiKetQua)
			{
				cmbKetQuaGiaiQuyet.addItem(model.getIntMa());
				cmbKetQuaGiaiQuyet.setItemCaption(model.getIntMa(), model.getName());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		cmbKetQuaGiaiQuyet.setNullSelectionAllowed(false);
		cmbKetQuaGiaiQuyet.setValue(0);
	}

	@Override
	public boolean validateFilter() {
		if(dfNgayThuLyStart.getValue()!=null && dfNgayThuLyEnd.getValue()==null)
		{
			Notification.show("Vui lòng nhập ngày thụ lý bắt đầu để tìm kiếm",Type.WARNING_MESSAGE);
			dfNgayThuLyStart.focus();
			return false;
		}
		if(dfNgayThuLyStart.getValue()==null && dfNgayThuLyEnd.getValue()!=null)
		{
			Notification.show("Vui lòng nhập ngày thụ lý kết thúc để tìm kiếm",Type.WARNING_MESSAGE);
			dfNgayThuLyEnd.focus();
			return false;
		}
		return true;
	}
}
