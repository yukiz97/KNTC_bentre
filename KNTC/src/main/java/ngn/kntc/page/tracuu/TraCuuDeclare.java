package ngn.kntc.page.tracuu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.enums.LoaiDonThuEnum;
import ngn.kntc.modules.ControlPagination;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.page.donthu.create.sublayout.NguoiDiKNTCLayout;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.DonThuServiceUtil;

import com.jarektoro.responsivelayout.ResponsiveRow;
import com.jensjansson.pagedtable.PagedTable;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class TraCuuDeclare extends VerticalLayout{
	Label lblMainCaption = new Label("",ContentMode.HTML);
	Label lblSubCaption = new Label("",ContentMode.HTML);
	
	HorizontalLayout hSearchLayout = new HorizontalLayout();
	TextField txtSearch = new TextField();
	Button btnSearch = new Button("Tìm kiếm",FontAwesome.SEARCH);
	Button btnPdf = new Button("In kết quả",FontAwesome.PRINT);
	
	Panel pnlChuThe = new Panel();
	Label lblCaptionChuThe = new Label("Tiêu chí tra cứu theo chủ thể đơn thư");
	NguoiDiKNTCLayout layoutNguoiDiKNTC = new NguoiDiKNTCLayout();
	
	Panel pnlThongTinDon = new Panel();
	Label lblCaptionThongTinDon = new Label("Tiêu chí tra cứu theo thông tin đơn");
	ComboBox cmbNguonDon = new ComboBox();
	ComboBox cmbLoaiDon = new ComboBox();
	Button btnLinhVuc = new Button("Chọn lĩnh vực đơn thư",FontAwesome.SEARCH_PLUS);
	VerticalLayout vLinhVucDisplay = new VerticalLayout();
	ResponsiveRow rowDisplayLinhVuc = buildRowDisplayLinhVuc();
	
	Panel pnlDate = new Panel();
	Label lblCaptionDate = new Label("Tiêu chí tra cứu theo ngày tháng");
	CheckBox cbNgayNhap = new CheckBox("<b>Ngày nhập</b>");
	DateField dfNgayNhapStart = new DateField();
	DateField dfNgayNhapEnd = new DateField();
	CheckBox cbNgayThuLy = new CheckBox("<b>Ngày thụ lý</b>");
	DateField dfNgayThuLyStart = new DateField();
	DateField dfNgayThuLyEnd = new DateField();
	CheckBox cbNgayGiaiQuyet = new CheckBox("<b>Ngày giải quyết</b>");
	DateField dfNgayGiaiQuyetStart = new DateField();
	DateField dfNgayGiaiQuyetEnd = new DateField();
	
	PagedTable tblDanhSach = new PagedTable();
	ControlPagination control = new ControlPagination(tblDanhSach);
	IndexedContainer container = new IndexedContainer();
	
	final String STT = "STT";
	final String LOAINGUOIDUNGDON = "Loại chủ thể";
	final String NGUOIDUNGDON = "Người đứng đơn";
	final String NOIDUNG = "Nội dung đơn thư";
	final String LOAIDONTHU = "Loại đơn thư";
	final String LINHVUC = "Lĩnh vực";
	final String NGAYNHAPDON = "Ngày nhận đơn";
	final String NGUOINHAP = "Người nhập";
	
	List<String> listLinhVuc = new ArrayList<String>();
	DanhMucServiceUtil svDanhMuc = new DanhMucServiceUtil();
	DonThuServiceUtil svDonThu = new DonThuServiceUtil();
	
	public TraCuuDeclare() {
		try {
			svDanhMuc.setValueDefaultForComboboxDanhMuc(cmbNguonDon, DanhMucTypeEnum.nguondon.getName(), "Integer");
			for(LoaiDonThuEnum e: LoaiDonThuEnum.values())
			{
				cmbLoaiDon.addItem(e.getType());
				cmbLoaiDon.setItemCaption(e.getType(), e.getName());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private ResponsiveRow buildRowDisplayLinhVuc()
	{
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("", vLinhVucDisplay,"100px")).withDisplayRules(12, 12, 12, 12);
		vLinhVucDisplay.setWidth("100%");
		vLinhVucDisplay.addStyleName("vLayout-linhvuc");
		row.setVisible(false);

		return row;
	}
}
