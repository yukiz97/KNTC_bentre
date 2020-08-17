package ngn.kntc.page.donthu.danhsach.thulydon;

import java.util.List;

import ngn.kntc.UI.kntcUI;
import ngn.kntc.beans.DonThuBean;
import ngn.kntc.beans.FilterDonCanThuLyBean;
import ngn.kntc.beans.FilterDonDaThuLyBean;
import ngn.kntc.beans.QuyetDinhThuLyBean;
import ngn.kntc.enums.LoaiDonThuEnum;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.page.donthu.chitiet.ChiTietDonThuTCDGeneralLayout;
import ngn.kntc.page.donthu.danhsach.LayoutDanhSachDonThu;
import ngn.kntc.utils.DonThuServiceUtil;

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

public class LayoutDanhSachDonThuDaThuLy extends LayoutDanhSachDonThu{
	private DateField dfNgayThuLyStart = new DateField();
	private DateField dfNgayThuLyEnd = new DateField();
	private DateField dfHanGiaiQuyetStart = new DateField();
	private DateField dfHanGiaiQuyetEnd = new DateField();
	private ComboBox cmbLoaiDonThu = new ComboBox();

	final String STT = "STT";
	final String NGUOIDUNGDON = "Người đứng đơn";
	final String NOIDUNG = "Nội dung đơn thư";
	//final String NGUOIBIKNTC = "Người bị khiếu tố";
	final String LOAIDONTHU = "Loại đơn thư";
	final String NGAYTHULY = "Ngày thụ lý";
	final String CANBODUYET = "Cán bộ duyệt";
	final String HANGIAIQUYET = "Hạn giải quyết";
	final String NGAYNHAPDON = "Ngày nhập đơn";
	final String NGUOINHAP = "Người nhập";

	public LayoutDanhSachDonThuDaThuLy() {
		lblMainCaption.setValue(FontAwesome.BOOK.getHtml()+" Quản lý danh sách đơn thư đã thụ lý");
		lblSubCaption.setValue("+ Quản lý danh sách đơn thư đã được thụ lý bởi đơn vị");

		hFilter.addComponents(new Label("<b>Ngày thụ lý</b>",ContentMode.HTML),dfNgayThuLyStart,new Label("-"),dfNgayThuLyEnd,new Label("<b>Hạn giải quyết</b>",ContentMode.HTML),dfHanGiaiQuyetStart,new Label("-"),dfHanGiaiQuyetEnd,new Label("<b>Loại đơn thư</b>",ContentMode.HTML),cmbLoaiDonThu);

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
		container.addContainerProperty(NGAYTHULY, String.class, null);
		container.addContainerProperty(HANGIAIQUYET, String.class, null);
		container.addContainerProperty(CANBODUYET, String.class, null);
		container.addContainerProperty(NGAYNHAPDON, String.class, null);
		container.addContainerProperty(NGUOINHAP, String.class, null);

		tblDanhSach.setContainerDataSource(container);
		tblDanhSach.setColumnExpandRatio(NOIDUNG, 1.0f);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadData() throws Exception {
		FilterDonDaThuLyBean modelFilter = new FilterDonDaThuLyBean();
		modelFilter.setKeyWord(txtSearch.getValue().trim());
		modelFilter.setNgayThuLyStart(dfNgayThuLyStart.getValue());
		modelFilter.setNgayThuLyEnd(dfNgayThuLyEnd.getValue());
		modelFilter.setHanGiaiQuyetStart(dfHanGiaiQuyetStart.getValue());
		modelFilter.setHanGiaiQuyetEnd(dfHanGiaiQuyetEnd.getValue());
		modelFilter.setLoaiDonThu((int)cmbLoaiDonThu.getValue());
		
		container.removeAllItems();
		List<DonThuBean> list = svDonThu.getDonThuDaThuLy(modelFilter);

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

			QuyetDinhThuLyBean modelQDTL = svQuaTrinh.getQuyetDinhThuLy(model.getMaDonThu());

			Item item = container.addItem(model.getMaDonThu());

			item.getItemProperty(STT).setValue(++i);
			item.getItemProperty(NGUOIDUNGDON).setValue(new Label(DonThuModule.returnLoaiNguoiDiKNTCForTable(model.getLoaiNguoiDiKNTC(), model.getSoNguoiDiKNTC(), model.getSoNguoiDaiDien())+"<b>"+DonThuServiceUtil.returnTenNguoiDaiDienDonThu(model)+"</b>",ContentMode.HTML));
			item.getItemProperty(NOIDUNG).setValue(new Label(noiDungDon,ContentMode.HTML));
			//item.getItemProperty(NGUOIBIKNTC).setValue(svDonThu.returnTenNguoiBiKNTC(model));
			item.getItemProperty(LOAIDONTHU).setValue(loaiDonThu);
			item.getItemProperty(NGAYTHULY).setValue(sdfDate.format(modelQDTL.getNgayThuLy()));
			item.getItemProperty(HANGIAIQUYET).setValue(sdfDate.format(modelQDTL.getHanGiaiQuyet()));
			item.getItemProperty(CANBODUYET).setValue(modelQDTL.getCanBoDuyet());
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
		if(dfHanGiaiQuyetStart.getValue()!=null && dfHanGiaiQuyetEnd.getValue()==null)
		{
			Notification.show("Vui lòng nhập ngày hạn giải quyết bắt đầu để tìm kiếm",Type.WARNING_MESSAGE);
			dfHanGiaiQuyetStart.focus();
			return false;
		}
		if(dfHanGiaiQuyetStart.getValue()==null && dfHanGiaiQuyetEnd.getValue()!=null)
		{
			Notification.show("Vui lòng nhập ngày hạn giải quyết kết thúc để tìm kiếm",Type.WARNING_MESSAGE);
			dfHanGiaiQuyetEnd.focus();
			return false;
		}
		return true;
	}
}
