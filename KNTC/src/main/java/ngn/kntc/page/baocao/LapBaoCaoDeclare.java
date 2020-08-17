package ngn.kntc.page.baocao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import ngn.kntc.UI.kntcUI;
import ngn.kntc.enums.LoaiBaoCao;
import ngn.kntc.enums.LoaiQuanLy;
import ngn.kntc.layout.FormMenu;
import ngn.kntc.utils.BaoCaoServiceUtil;
import ngn.kntc.utils.UploadServiceUtil;

public class LapBaoCaoDeclare extends VerticalLayout{
	Label lblMainCaption = new Label("Lập báo cáo, thống kê",ContentMode.HTML);
	Label lblSubCaption = new Label("+ Lập và in báo cáo theo các mẫu báo cáo có sẵn như 2a,2b,2c,2d,...<br/>+ Lập báo cáo theo các mốc thời gian hoặc theo khung thời gian của người dùng <br/>+ Lập báo cáo theo cá nhân hoặc theo đơn vị được chọn",ContentMode.HTML);
	
	Panel pnlMain = new Panel();
	VerticalLayout vSubLayout = new VerticalLayout();
	
	ComboBox cmbLoaiBaoCao = new ComboBox();
	
	HorizontalLayout hTimeDefault = new HorizontalLayout();
	LinkedHashMap<String, Button> timesMenu = new LinkedHashMap<String, Button>();
	
	HorizontalLayout hTimeOption = new HorizontalLayout();
	ComboBox cmbThang = new ComboBox();
	ComboBox cmbQuy = new ComboBox();
	ComboBox cmbNam = new ComboBox();
	DateField dfStartDate = new DateField();
	DateField dfEndDate = new DateField();
	
	HorizontalLayout hButton = new HorizontalLayout();
	Button btnLapBaoCao = new Button("Lập báo cáo");
	Button btnXuatExcel = new Button("Xuất Excel",FontAwesome.FILE_EXCEL_O);
	
	OptionGroup ogLoaiDoiTuongLapBaoCao = new OptionGroup();
	Label lblDonViBaoCao = new Label("",ContentMode.HTML);
	Button btnChonDonVi = new Button("Chọn đơn vị",FontAwesome.PLUS_CIRCLE);
	Button btnChonCanBo = new Button("Chọn cá nhân",FontAwesome.PLUS_CIRCLE);
	
	OptionGroup ogChonLoai = new OptionGroup();
	
	Panel pnlDisplay = new Panel();
	
	SimpleDateFormat sdfYYYY = new SimpleDateFormat("yyyy-MM-dd");
	
	BaoCaoServiceUtil svBaoCao = new BaoCaoServiceUtil();
	UploadServiceUtil svUpload = new UploadServiceUtil();
	
	List<Long> listOrg = new ArrayList<Long>();
	
	public LapBaoCaoDeclare() {
		loadDefaultData();
	}
	
	private void loadDefaultData() {
		/* combobox Loai Báo Cáo */
		for(LoaiBaoCao e : LoaiBaoCao.values())
		{
			cmbLoaiBaoCao.addItem(e.getType());
			cmbLoaiBaoCao.setItemCaption(e.getType(), e.getName());
		}
		cmbLoaiBaoCao.select(1);
		
		/* combobox tháng */
		for(int i = 1;i<=12;i++)
		{
			cmbThang.addItem(i);
			cmbThang.setItemCaption(i, "Tháng "+i);
		}
		cmbThang.select(Calendar.getInstance().get(Calendar.MONTH)+1);
		
		/* combobox quý */
		for(int i = 1;i<=4;i++)
		{
			cmbQuy.addItem(i);
			cmbQuy.setItemCaption(i, "Quý "+i);
		}
		cmbQuy.select((Calendar.getInstance().get(Calendar.MONTH)/3)+1);
		
		/* combobox năm */
		for(int i = 2000;i<=Calendar.getInstance().get(Calendar.YEAR);i++)
		{
			cmbNam.addItem(i);
			cmbNam.setItemCaption(i, "Năm "+i);
		}
		cmbNam.select(Calendar.getInstance().get(Calendar.YEAR));
		
		/* Optiongroup */
		ogLoaiDoiTuongLapBaoCao.addItem(1);
		ogLoaiDoiTuongLapBaoCao.setItemCaption(1, "Đơn vị");
		/*ogLoaiDoiTuongLapBaoCao.addItem(2);
		ogLoaiDoiTuongLapBaoCao.setItemCaption(2, "Cá nhân");*/
		ogLoaiDoiTuongLapBaoCao.select(1);
		
		ogChonLoai.addItem(LoaiQuanLy.donvi.getType());
		ogChonLoai.setItemCaption(LoaiQuanLy.donvi.getType(), "Thẩm quyền đơn vị");
		ogChonLoai.addItem(LoaiQuanLy.thammuu.getType());
		ogChonLoai.setItemCaption(LoaiQuanLy.thammuu.getType(), "Tham mưu giải quyết");
		
		ogChonLoai.select((int)kntcUI.getCurrent().getFormMenu().getOgChonLoai().getValue());
		ogChonLoai.setVisible(false);

		ogChonLoai.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
	}
}
