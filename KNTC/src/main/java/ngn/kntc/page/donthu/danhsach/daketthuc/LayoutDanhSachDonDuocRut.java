package ngn.kntc.page.donthu.danhsach.daketthuc;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ngn.kntc.UI.kntcUI;
import ngn.kntc.beans.DanhMucBean;
import ngn.kntc.beans.DonThuBean;
import ngn.kntc.beans.FilterDonDaCoKetQuaBean;
import ngn.kntc.beans.FilterDonDuocRut;
import ngn.kntc.beans.FilterDonNhanTuDonViKhacBean;
import ngn.kntc.beans.KetQuaXuLyBean;
import ngn.kntc.beans.ThongTinDonThuBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.enums.HanXuLyChuaEnum;
import ngn.kntc.enums.HanXuLyDaXuLyEnum;
import ngn.kntc.enums.LoaiDonThuEnum;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.modules.KNTCProps;
import ngn.kntc.page.donthu.chitiet.ChiTietDonThuTCDGeneralLayout;
import ngn.kntc.page.donthu.danhsach.LayoutDanhSachDonThu;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.SessionUtil;
import ngn.kntc.windows.WindowTieuChiXuatDanhSachDonDaXuLy;

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
import com.vaadin.ui.Notification.Type;

public class LayoutDanhSachDonDuocRut extends LayoutDanhSachDonThu{
	private ComboBox cmbLoaiDonThu = new ComboBox();
	private ComboBox cmbHuongXuLy = new ComboBox();
	
	final String STT = "STT";
	final String NGUOIDUNGDON = "Người đứng đơn";
	final String NOIDUNG = "Nội dung đơn thư";
	//final String NGUOIBIKNTC = "Người bị khiếu tố";
	final String LOAIDONTHU = "Loại đơn thư";
	final String DONVICHUYENDEN = "Đơn vị chuyển đến";
	final String NGAYXULY = "Ngày xử lý";
	final String HUONGXULY = "Hướng xử lý";
	final String NGAYNHAPDON = "Ngày nhận đơn";
	final String NGUOINHAP = "Người nhập";
	
	private DanhMucServiceUtil svDanhMuc = new DanhMucServiceUtil();
	
	public LayoutDanhSachDonDuocRut() {
		lblMainCaption.setValue(FontAwesome.BOOK.getHtml()+" Quản lý danh sách đơn thư đã xử lý");
		lblSubCaption.setValue("+  Quản lý danh sách đơn thư đã được xử lý bởi đơn vị");
		
		hFilter.addComponents(new Label("<b>Loại đơn thư</b>",ContentMode.HTML),cmbLoaiDonThu,new Label("<b>Hướng xử lý</b>",ContentMode.HTML),cmbHuongXuLy);
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
		container.addContainerProperty(NGAYXULY, String.class, null);
		container.addContainerProperty(HUONGXULY, String.class, null);
		container.addContainerProperty(NGAYNHAPDON, String.class, null);
		container.addContainerProperty(NGUOINHAP, String.class, null);
		
		tblDanhSach.setContainerDataSource(container);
		tblDanhSach.setColumnExpandRatio(NOIDUNG, 1.0f);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadData() throws Exception {
		FilterDonDuocRut modelFilter = new FilterDonDuocRut();
		modelFilter.setKeyword(txtSearch.getValue().trim());
		modelFilter.setLoaiDonThu((int)cmbLoaiDonThu.getValue());
		modelFilter.setHuongXuLy((int)cmbHuongXuLy.getValue());
		
		container.removeAllItems();
		List<DonThuBean> list = svDonThu.getDonThuDaThuHoi(modelFilter);
		
		int i = 0;
		for(DonThuBean model : list)
		{
			String noiDungDon = "";
			String loaiDonThu = "";
			String strNgayXuLy = "Chưa được xử lý";
			String strHuongXuLy = "Chưa được xử lý";
			KetQuaXuLyBean modelKQXL = svQuaTrinh.getKetQuaXuLy(model.getMaDonThu(), SessionUtil.getOrgId());
			if(model.getMaDonThu()!=0)
			{
				noiDungDon = model.getNoiDungDonThu();
				if(noiDungDon.length()>120)
				{
					noiDungDon = noiDungDon.substring(0, 120)+"<b>.......</b>";
				}
			}
			if(modelKQXL!=null)
			{
				strNgayXuLy = sdfDate.format(modelKQXL.getNgayXuLy());
				strHuongXuLy = DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.huongxuly.getName(),modelKQXL.getMaHuongXuLy()).getName();
			}
			
			for(LoaiDonThuEnum e : LoaiDonThuEnum.values())
			{
				if(e.getType()==model.getLoaiDonThu())
					loaiDonThu = e.getName();
			}
			
			Item item = container.addItem(model.getMaDonThu());
			
			item.getItemProperty(STT).setValue(++i);
			item.getItemProperty(NGUOIDUNGDON).setValue(new Label(DonThuModule.returnLoaiNguoiDiKNTCForTable(model.getLoaiNguoiDiKNTC(), model.getSoNguoiDiKNTC(), model.getSoNguoiDaiDien())+"<br/><b>"+DonThuServiceUtil.returnTenNguoiDaiDienDonThu(model)+"</b>",ContentMode.HTML));
			item.getItemProperty(NOIDUNG).setValue(new Label(noiDungDon,ContentMode.HTML));
			//item.getItemProperty(NGUOIBIKNTC).setValue(svDonThu.returnTenNguoiBiKNTC(model));
			item.getItemProperty(LOAIDONTHU).setValue(loaiDonThu);
			item.getItemProperty(NGAYXULY).setValue(strNgayXuLy);
			item.getItemProperty(NGAYNHAPDON).setValue(sdfDatetime.format(model.getNgayNhapDon()));
			item.getItemProperty(HUONGXULY).setValue(strHuongXuLy);
			item.getItemProperty(NGUOINHAP).setValue(UserLocalServiceUtil.getUser(model.getUserNhapDon()).getFirstName());
		}
		tblDanhSach.setContainerDataSource(container);
	}
	
	@Override
	public void loadDefaultValue() {
		DonThuModule.returnComboboxLoaiDonThu(cmbLoaiDonThu, true);	
		
		cmbHuongXuLy.addItem(0);
		cmbHuongXuLy.setItemCaption(0, "Tất cả");
		List<DanhMucBean> listHuongXuLy;
		try {
			listHuongXuLy = svDanhMuc.getDanhMucList(DanhMucTypeEnum.huongxuly.getName(),DanhMucTypeEnum.huongxuly.getIdType());

			for(DanhMucBean model : listHuongXuLy)
			{
				cmbHuongXuLy.addItem(model.getIntMa());
				cmbHuongXuLy.setItemCaption(model.getIntMa(), model.getName());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		cmbHuongXuLy.setNullSelectionAllowed(false);
		cmbHuongXuLy.setValue(0);
	}

	@Override
	public boolean validateFilter() {
		
		return true;
	}
}
