package ngn.kntc.page.donthu.danhsach.xulydon;

import java.util.List;

import ngn.kntc.UI.kntcUI;
import ngn.kntc.beans.DonThuBean;
import ngn.kntc.beans.FilterDonDaChuyenDiBean;
import ngn.kntc.beans.FilterDonNhanTuDonViKhacBean;
import ngn.kntc.beans.KetQuaXuLyBean;
import ngn.kntc.beans.ThongTinDonThuBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.enums.LoaiDonThuEnum;
import ngn.kntc.enums.LoaiTinhTrangDonThuEnum;
import ngn.kntc.modules.DonThuModule;
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
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

public class LayoutDanhSachDonThuDaChuyenDi extends LayoutDanhSachDonThu{
	private DateField dfNgayChuyenStart = new DateField();
	private DateField dfNgayChuyenEnd = new DateField();
	private ComboBox cmbLoaiDonThu = new ComboBox();

	final String STT = "STT";
	final String NGUOIDUNGDON = "Người đứng đơn";
	final String NOIDUNG = "Nội dung đơn thư";
	//final String NGUOIBIKNTC = "Người bị khiếu tố";
	final String LOAIDONTHU = "Loại đơn thư";
	final String DONVICHUYENDI = "Đơn vị chuyển đi";
	final String NGAYNHAPDON = "Ngày nhận đơn";
	final String NGAYCHUYENDI = "Ngày chuyển đi";
	final String NGUOINHAP = "Người nhập";
	
	int status = -1;

	public LayoutDanhSachDonThuDaChuyenDi(int status) {
		/*
		 * status = 0 : chưa giải quyết
		 * status = 1 : đã giải quyết
		 * status = 2 : chuyển lưu trữ
		 * */
		String mainCaption = "",subCaption = "";
		if(status == 0)
		{
			this.status = LoaiTinhTrangDonThuEnum.chuyendonchuagq.getType();
			mainCaption = " Quản lý danh sách đơn thư đã chuyển đi chưa có kết quả giải quyết";
			subCaption = "+  Quản lý danh sách đơn thư không đủ thẩm quyền giải quyết và đã chuyển sang đơn vị khác bằng phần mềm nhưng đơn vị đó chưa giải quyết đơn";
		}
		else if(status == 1)
		{
			this.status = LoaiTinhTrangDonThuEnum.chuyendondagq.getType();
			mainCaption = " Quản lý danh sách đơn thư đã chuyển đi và đã có kết quả giải quyết";
			subCaption = "+  Quản lý danh sách đơn thư không đủ thẩm quyền giải quyết và đã chuyển sang đơn vị khác bằng phần mềm và đơn vị đó đã nhập kết quả giải quyết đơn";
		}
		else if(status == 2)
		{
			this.status = LoaiTinhTrangDonThuEnum.chuyendonluutru.getType();
			mainCaption = " Quản lý danh sách đơn thư có đã chuyển đi thủ công";
			subCaption = "+  Quản lý danh sách đơn thư có kết quả xử lý là chuyển đơn nhưng không chuyển qua phần mềm";
		}
		
		lblMainCaption.setValue(FontAwesome.BOOK.getHtml()+mainCaption);
		lblSubCaption.setValue(subCaption);

		hFilter.addComponents(new Label("<b>Ngày chuyển đơn</b>",ContentMode.HTML),dfNgayChuyenStart,new Label("-"),dfNgayChuyenEnd,new Label("<b>Loại đơn thư</b>",ContentMode.HTML),cmbLoaiDonThu);
		
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
	//	container.addContainerProperty(NGUOIBIKNTC, String.class, null);
		container.addContainerProperty(LOAIDONTHU, String.class, null);
		container.addContainerProperty(DONVICHUYENDI, String.class, null);
		container.addContainerProperty(NGAYNHAPDON, String.class, null);
		if(status!=LoaiTinhTrangDonThuEnum.chuyendonluutru.getType())
		{
			container.addContainerProperty(NGAYCHUYENDI, String.class, null);
		}
		container.addContainerProperty(NGUOINHAP, String.class, null);

		tblDanhSach.setContainerDataSource(container);
		
		tblDanhSach.setColumnExpandRatio(NOIDUNG, 1.0f);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadData() throws Exception {
		FilterDonDaChuyenDiBean modelFilter = new FilterDonDaChuyenDiBean();
		modelFilter.setKeyWord(txtSearch.getValue().trim());
		modelFilter.setNgayChuyenStart(dfNgayChuyenStart.getValue());
		modelFilter.setNgayChuyenEnd(dfNgayChuyenEnd.getValue());
		modelFilter.setLoaiDonThu((int)cmbLoaiDonThu.getValue());
		
		container.removeAllItems();
		List<DonThuBean> list = svDonThu.getDonThuDaChuyenDonViKhac(modelFilter,status);

		int i = 0;
		for(DonThuBean model : list)
		{
			String noiDungDon = "";
			String loaiDonThu = "";
			String donViDaChuyen = "";
			String ngayChuyenDi = "";
			
			Item item = container.addItem(model.getMaDonThu());
			
			ThongTinDonThuBean modelThongTinDon = svDonThu.getThongTinDonThuChuyenDi(model.getMaDonThu(), SessionUtil.getOrgId());
			KetQuaXuLyBean modelKQXL = svQuaTrinh.getKetQuaXuLy(model.getMaDonThu(), SessionUtil.getOrgId());

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
			
			if(status!=LoaiTinhTrangDonThuEnum.chuyendonluutru.getType())
			{
				ngayChuyenDi = sdfDatetime.format(modelThongTinDon.getNgayNhan());
				item.getItemProperty(NGAYCHUYENDI).setValue(ngayChuyenDi);
			}

			donViDaChuyen =svDanhMuc.getDanhMuc(DanhMucTypeEnum.coquan.getName(),modelKQXL.getMaCQXLTiep()).getName();

			item.getItemProperty(STT).setValue(++i);
			item.getItemProperty(NGUOIDUNGDON).setValue(new Label(DonThuModule.returnLoaiNguoiDiKNTCForTable(model.getLoaiNguoiDiKNTC(), model.getSoNguoiDiKNTC(), model.getSoNguoiDaiDien())+"<b>"+DonThuServiceUtil.returnTenNguoiDaiDienDonThu(model)+"</b>",ContentMode.HTML));
			item.getItemProperty(NOIDUNG).setValue(new Label(noiDungDon,ContentMode.HTML));
			//item.getItemProperty(NGUOIBIKNTC).setValue(svDonThu.returnTenNguoiBiKNTC(model));
			item.getItemProperty(LOAIDONTHU).setValue(loaiDonThu);
			item.getItemProperty(DONVICHUYENDI).setValue(donViDaChuyen);
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
		if(dfNgayChuyenStart.getValue()!=null && dfNgayChuyenEnd.getValue()==null)
		{
			Notification.show("Vui lòng nhập ngày bắt đầu để tìm kiếm",Type.WARNING_MESSAGE);
			dfNgayChuyenStart.focus();
			return false;
		}
		if(dfNgayChuyenStart.getValue()==null && dfNgayChuyenEnd.getValue()!=null)
		{
			Notification.show("Vui lòng nhập ngày kết thúc để tìm kiếm",Type.WARNING_MESSAGE);
			dfNgayChuyenEnd.focus();
			return false;
		}
		return true;
	}
}
