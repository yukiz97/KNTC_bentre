package ngn.kntc.page.donthu.create;
 
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.liferay.portal.model.User;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.enums.ThamQuyenGiaiQuyetEnum;
import ngn.kntc.enums.UserRole;
import ngn.kntc.lienthongttcp.LienThongVuViec;
import ngn.kntc.page.donthu.create.sublayout.CoQuanDaGiaiQuyetLayout;
import ngn.kntc.page.donthu.create.sublayout.NguoiBiKNTCLayout;
import ngn.kntc.page.donthu.create.sublayout.NguoiDiKNTCLayout;
import ngn.kntc.page.donthu.create.sublayout.NguoiUyQuyenLayout;
import ngn.kntc.page.donthu.create.sublayout.NoiDungDonThuLayout;
import ngn.kntc.ttcp.model.VuViecType;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.LiferayServiceUtil;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;
import ngn.kntc.utils.SessionUtil;
import ngn.kntc.utils.TiepCongDanServiceUtil;
import ngn.kntc.utils.UploadServiceUtil;

public class TaoDonThuDeclare extends VerticalLayout{
	boolean themMoi = true;
	String idGanVuViec = "";
	String idGanVuViecLienThong;
	int idDonThu = -1;
	int idTiepCongDan = -1;
	boolean tiepCongDan = true;
	boolean lanhDaoTiep = false;
	boolean validateSuccess = false;
	
	LienThongVuViec lienThongVuViec = new LienThongVuViec();
	
	String folderTmp = "xoadi_"+Calendar.getInstance().getTimeInMillis();
	/* Lựa chọn phương thức */
	HorizontalLayout hPhuongThucTiepNhan = new HorizontalLayout();
	Button btnTiepCongDan = new Button("Tiếp thường xuyên");
	Button btnLanhDaoTiep = new Button("Lãnh đạo tiếp");
	Button btnTiepNhanDonGianTiep = new Button("Tiếp nhận gián tiếp");
	
	/* Panel phương thức */
	Panel pnlPhuongThuc = new Panel();
	ResponsiveLayout rslPhuongThuc = new ResponsiveLayout();
	ResponsiveRow rowTiepCongDan1 = new ResponsiveRow();
	ResponsiveRow rowTiepCongDan2 = new ResponsiveRow();
	ResponsiveRow rowTiepCongDan3 = new ResponsiveRow();
	ResponsiveRow rowTiepCongDan4 = new ResponsiveRow();
	ResponsiveRow rowTiepNhanDon1 = new ResponsiveRow();
	ResponsiveRow rowTiepNhanDon2 = new ResponsiveRow();
	ResponsiveRow rowTiepNhanDon3 = new ResponsiveRow();
	ResponsiveRow rowTiepNhanDon4 = new ResponsiveRow();
	
	/* Thông tin phương thức TCD */
	TextField txtSoThuTuTCD = new TextField();
	CheckBox cbTiepCongDanKhongDon = new CheckBox();
	DateField dfNgayTiepCongDan = new DateField();
	
	ComboBox cmbTenLanhDaoTiep = new ComboBox();
	CheckBox cbUyQuyenLanhDao = new CheckBox("Ủy quyền");
	CheckBox cbTiepDinhKy = new CheckBox("Tiếp định kỳ");
	ComboBox cmbTenLanhDaoUyQuyen = new ComboBox();
	
	/* Thông tin phương thức tiếp nhận đơn */
	TextField txtSoThuTuTiepNhan = new TextField();
	ComboBox cmbNguonDonDen = new ComboBox();
	TextField txtNguoiNhapDon = new TextField();
	TextField txtTenCoQuanTiepNhan = new TextField();
	DateField dfNgayNhapDon = new DateField();
	DateField dfNgayNhanDon = new DateField();
	
	TextField txtTenCoQuanChuyenDen = new TextField();
	Button btnTimKiemCoQuanChuyenDen = new Button("",FontAwesome.PLUS_CIRCLE);
	TextField txtSoVanBanChuyenDen = new TextField();
	DateField dfNgayPhatHanhVanBanChuyenDen = new DateField();
	
	/* Người đi KNTC Layout */
	VerticalLayout vNguoiDiKNTC = new VerticalLayout();
	Label lblHeaderNguoiDiKNTC = new Label("Thông tin chủ thể đơn thư",ContentMode.HTML);
	NguoiDiKNTCLayout layoutNguoiDiKNTC = new NguoiDiKNTCLayout();
	
	/* Nội dung đơn thư */
	VerticalLayout vNoiDungDonThu = new VerticalLayout();
	Label lblHeaderNoiDungDonThu = new Label("Nội dung đơn thư");
	NoiDungDonThuLayout layoutNoiDungDonThu = new NoiDungDonThuLayout();
	
	/* Cơ quan đã giải quyết đơn thư */
	VerticalLayout vCoQuanDaGiaiQuyet = new VerticalLayout();
	HorizontalLayout hHeaderCoQuanDaGiaiQuyet = new HorizontalLayout();
	CheckBox cbHeaderCoQuanDaGiaiQuyet = new CheckBox();
	Label lblHeaderCoQuanDaGiaiQuyet = new Label("Thông tin cơ quan đã giải quyết");
	CoQuanDaGiaiQuyetLayout layoutCoQuanDaGiaiQuyet = new CoQuanDaGiaiQuyetLayout();
	
	/* Người bị KNTC Layout */
	VerticalLayout vNguoiBiKNTC = new VerticalLayout();
	HorizontalLayout hHeaderNguoiBiKNTC = new HorizontalLayout();
	CheckBox cbHeaderNguoiBiKNTC = new CheckBox();
	Label lblHeaderNguoiBiKNTC = new Label("Người bị khiếu nại / tố cáo",ContentMode.HTML);
	NguoiBiKNTCLayout layoutNguoiBiKNTC = new NguoiBiKNTCLayout();
	
	/* Người ủy quyền Layout */
	VerticalLayout vNguoiUyQuyen = new VerticalLayout();
	HorizontalLayout hHeaderNguoiUyQuyen = new HorizontalLayout();
	CheckBox cbHeaderNguoiUyQuyen = new CheckBox();
	Label lblHeaderNguoiUyQuyen = new Label("Thông tin người được đại diện, ủy quyền",ContentMode.HTML);
	NguoiUyQuyenLayout layoutNguoiUyQuyen = new NguoiUyQuyenLayout();
	
	/* Main Control Layout */
	HorizontalLayout hMainControl=new HorizontalLayout();
	Button btnSave=new Button("Lưu",FontAwesome.SAVE);
	Button btnReset=new Button("Nhập lại",FontAwesome.REFRESH);
	Button btnClose=new Button("Hủy",FontAwesome.CLOSE);
	
	DanhMucServiceUtil svDanhMuc = new DanhMucServiceUtil();
	DonThuServiceUtil svDonThu = new DonThuServiceUtil();
	TiepCongDanServiceUtil svTCD = new TiepCongDanServiceUtil();
	QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh = new QuaTrinhXuLyGiaiQuyetServiceUtil();
	UploadServiceUtil svUpload = new UploadServiceUtil();
		
	public TaoDonThuDeclare() {
		layoutNoiDungDonThu.setFolderTmp(folderTmp);
		try {
			txtNguoiNhapDon.setValue(SessionUtil.getUser().getFirstName());
			txtTenCoQuanTiepNhan.setValue(OrganizationLocalServiceUtil.getOrganization(SessionUtil.getOrgId()).getName());
			loadDefaultValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadDefaultValue() throws Exception
	{
		/* Ngày nhập đơn, ngày tiếp công dân, Ngày ban hành văn bản */
		dfNgayNhanDon.setValue(new Date());
		dfNgayNhapDon.setValue(new Date());
		dfNgayTiepCongDan.setValue(new Date());
		dfNgayPhatHanhVanBanChuyenDen.setValue(new Date());
		
		dfNgayNhapDon.setEnabled(false);
		
		/* Loại quyết định */
		svDanhMuc.setValueDefaultForComboboxDanhMuc(cmbNguonDonDen, DanhMucTypeEnum.nguondon.getName(), DanhMucTypeEnum.nguondon.getIdType());
		cmbNguonDonDen.select(1);
		//cmbNguonDonDen.removeItem(6);
	
		/* Load combobox lãnh đạo */
		List<User> listLanhDao = LiferayServiceUtil.returnListLanhDao();
		
		for(User user: listLanhDao)
		{
			long idUser = user.getUserId();
			String tenUser = user.getFirstName();
			
			cmbTenLanhDaoTiep.addItem(idUser);
			cmbTenLanhDaoTiep.setItemCaption(idUser, tenUser);
			cmbTenLanhDaoUyQuyen.addItem(idUser);
			cmbTenLanhDaoUyQuyen.setItemCaption(idUser, tenUser);
		}
	}
	
	public Button getBtnTiepCongDan() {
		return btnTiepCongDan;
	}
	public void setBtnTiepCongDan(Button btnTiepCongDan) {
		this.btnTiepCongDan = btnTiepCongDan;
	}
	public Button getBtnLanhDaoTiep() {
		return btnLanhDaoTiep;
	}
	public void setBtnLanhDaoTiep(Button btnLanhDaoTiep) {
		this.btnLanhDaoTiep = btnLanhDaoTiep;
	}
	public Button getBtnTiepNhanDonGianTiep() {
		return btnTiepNhanDonGianTiep;
	}
	public void setBtnTiepNhanDonGianTiep(Button btnTiepNhanDonGianTiep) {
		this.btnTiepNhanDonGianTiep = btnTiepNhanDonGianTiep;
	}
	public Button getBtnSave() {
		return btnSave;
	}
	public void setBtnSave(Button btnSave) {
		this.btnSave = btnSave;
	}
	public Button getBtnReset() {
		return btnReset;
	}
	public void setBtnReset(Button btnReset) {
		this.btnReset = btnReset;
	}
	public Button getBtnClose() {
		return btnClose;
	}
	public void setBtnClose(Button btnClose) {
		this.btnClose = btnClose;
	}
	public boolean isValidateSuccess() {
		return validateSuccess;
	}
	public void setValidateSuccess(boolean validateSuccess) {
		this.validateSuccess = validateSuccess;
	}
}
