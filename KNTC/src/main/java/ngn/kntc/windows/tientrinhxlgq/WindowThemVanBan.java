package ngn.kntc.windows.tientrinhxlgq;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ngn.kntc.attachfile.FormAttachFile;
import ngn.kntc.beans.QuaTrinhXuLyGiaiQuyetBean;
import ngn.kntc.beans.VanBanQuyPhamBean;
import ngn.kntc.beans.VanBanXuLyGiaiQuyetBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.enums.LoaiVanBanXuLyGiaiQuyetEnum;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.modules.LayoutButtonSubmit;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;
import ngn.kntc.utils.SessionUtil;
import ngn.kntc.utils.UploadServiceUtil;

import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

public class WindowThemVanBan extends Window{
	VerticalLayout vMainLayout = new VerticalLayout();
	private ResponsiveLayout rslMainLayout = new ResponsiveLayout();
	
	ComboBox cmbLoaiVanBan = new ComboBox();
	DateField dfNgayBanHanh = new DateField();
	DateField dfHanGiaiQuyet = new DateField();
	HorizontalLayout hHanGiaiQuyet = new HorizontalLayout();
	Label lblHanGiaiQuyet = new Label();
	
	TextField txtSoHieuVanBan = new TextField();
	TextArea txtNoiDung = new TextArea();
	TextArea txtGhiChu = new TextArea();
	
	FormAttachFile attachFile = new FormAttachFile();
	
	LayoutButtonSubmit layoutSubmit = new LayoutButtonSubmit();
	
	int idDonThu = -1;
	boolean validateSuccess = false;
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	UploadServiceUtil svUpload = new UploadServiceUtil();
	DonThuServiceUtil svDonThu = new DonThuServiceUtil();
	QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh = new QuaTrinhXuLyGiaiQuyetServiceUtil();
	
	public WindowThemVanBan(int idDonThu) {
		this.idDonThu = idDonThu;
		
		buildLayout();
		configComponent();
	}

	private void buildLayout() {
		vMainLayout.addComponent(rslMainLayout);
		vMainLayout.addComponent(DonThuModule.buildFormLayoutSingle("Số hiệu"+DonThuModule.requiredMark, txtSoHieuVanBan,"90px"));
		vMainLayout.addComponent(DonThuModule.buildFormLayoutSingle("Nội dung"+DonThuModule.requiredMark, txtNoiDung,"90px"));
		vMainLayout.addComponent(DonThuModule.buildFormLayoutSingle("Ghi chú", txtGhiChu,"90px"));
		vMainLayout.addComponent(DonThuModule.buildFormLayoutSingle("Tệp đính kèm", attachFile,"90px"));
		vMainLayout.addComponent(layoutSubmit);
		
		rslMainLayout.addRow(buildRow1());
		
		txtGhiChu.setWidth("100%");
		txtNoiDung.setWidth("100%");
		txtSoHieuVanBan.setWidth("100%");
		
		attachFile.setNameFolderTMP(File.separator+idDonThu);
		
		vMainLayout.setComponentAlignment(layoutSubmit, Alignment.MIDDLE_RIGHT);
		
		vMainLayout.setSpacing(true);
		vMainLayout.setMargin(true);
		vMainLayout.setWidth("100%");
		
		this.setCaption("Thêm văn bản");
		this.setContent(vMainLayout);
		this.setWidth("900px");
		this.center();
		this.setModal(true);
		
		setTypeFileAccept();
		buildAttachFile();
	}

	private void configComponent() {
		layoutSubmit.getBtnSave().addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				validateSuccess();
				if(validateSuccess)
				{
					VanBanXuLyGiaiQuyetBean modelVanBan = new VanBanXuLyGiaiQuyetBean();
				
					modelVanBan.setLoaiVanBan((int)cmbLoaiVanBan.getValue());
					modelVanBan.setNgayBanHanh(dfNgayBanHanh.getValue());
					modelVanBan.setHanGiaiQuyet(dfHanGiaiQuyet.getValue());
					modelVanBan.setSoVanBan(txtSoHieuVanBan.getValue());
					modelVanBan.setNoiDungVanBan(txtNoiDung.getValue().trim());
					modelVanBan.setGhiChuVanBan(!txtGhiChu.isEmpty()?txtGhiChu.getValue():null);
					modelVanBan.setTenFileDinhKem(attachFile.getFileNameOld());
					modelVanBan.setLinkFileDinhKem(attachFile.getFileNameNew());
					modelVanBan.setUserNhap((int)SessionUtil.getUserId());
					modelVanBan.setIdDonThu(idDonThu);
					
					try {
						svDonThu.insertVanBanXuLyGiaiQuyet(modelVanBan);
						Notification.show("Đã thêm văn bản thành công",Type.TRAY_NOTIFICATION);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					QuaTrinhXuLyGiaiQuyetBean model = new QuaTrinhXuLyGiaiQuyetBean();
					String loaiVanBan = "";
					
					for(LoaiVanBanXuLyGiaiQuyetEnum e : LoaiVanBanXuLyGiaiQuyetEnum.values())
					{
						if(e.getType()==modelVanBan.getLoaiVanBan())
							loaiVanBan = e.getName();
					}

					try {
						String hanGiaiQuyet = modelVanBan.getHanGiaiQuyet()!=null?sdf.format(modelVanBan.getHanGiaiQuyet()):"";
						model.setNoiDung(svQuaTrinh.returnStringCanBoNhap(SessionUtil.getUserId(), "đã nhập một văn bản","<span style='color: #a2271e'>Loại văn bản:</span> "+loaiVanBan+"</span> - <span style='color: #a2271e'>Ngày ban hành:</span> "+sdf.format(modelVanBan.getNgayBanHanh())+" - "+" <span style='color: #a2271e'>Hạn giải quyết:</span> "+hanGiaiQuyet+"<br/><span style='color: #a2271e'>Nội dung:</span> "+modelVanBan.getNoiDungVanBan()));
					} catch (Exception e1) {
						e1.printStackTrace();
					}

					model.setUserNhap(SessionUtil.getUserId());
					model.setNgayDang(new Date());
					model.setMaDonThu(idDonThu);
					if(attachFile.getFileNameNew()!=null)
					{
						model.setTenFileDinhKem(attachFile.getFileNameOld());
						model.setLinkFileDinhKem(attachFile.getFileNameNew());
					}
					model.setHeThongTao(true);
					
					try {
						int idQuaTrinh = svQuaTrinh.insertQuaTrinhXLGQ(model);
						DonThuModule.insertThongBao(idDonThu, idQuaTrinh);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					
					close();
				}
			}
		});
		
		layoutSubmit.getBtnCancel().addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
		
		cmbLoaiVanBan.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				if((int)cmbLoaiVanBan.getValue() == LoaiVanBanXuLyGiaiQuyetEnum.tochucdoithoai.getType())
					lblHanGiaiQuyet.setValue("Ngày đối thoại");
				else
					lblHanGiaiQuyet.setValue("Hạn giải quyết");
			}
		});
	}
	
	private ResponsiveRow buildRow1()
	{
		hHanGiaiQuyet = DonThuModule.buildFormLayoutSingle("Hạn giải quyết", dfHanGiaiQuyet,"80px");
		lblHanGiaiQuyet = (Label) hHanGiaiQuyet.getComponent(0);
		
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Loại văn bản"+DonThuModule.requiredMark, cmbLoaiVanBan,"90px")).withDisplayRules(12, 12, 4, 4);
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Ngày ban hành"+DonThuModule.requiredMark, dfNgayBanHanh,"80px")).withDisplayRules(12, 12, 4, 4);
		row.addColumn().withComponent(hHanGiaiQuyet).withDisplayRules(12, 12, 4, 4);
		
		cmbLoaiVanBan.setWidth("100%");
		dfNgayBanHanh.setWidth("100%");
		dfHanGiaiQuyet.setWidth("100%");
		
		for(LoaiVanBanXuLyGiaiQuyetEnum e : LoaiVanBanXuLyGiaiQuyetEnum.values())
		{
			cmbLoaiVanBan.addItem(e.getType());
			cmbLoaiVanBan.setItemCaption(e.getType(), e.getName());
		}
		
		cmbLoaiVanBan.setNullSelectionAllowed(false);
		cmbLoaiVanBan.select(1);
		
		return row;
	}
	
	private void validateSuccess()
	{
		if(dfNgayBanHanh.getValue()==null)
		{
			dfNgayBanHanh.focus();
			Notification.show("Vui lòng nhập vào ngày ban hành",Type.WARNING_MESSAGE);
			validateSuccess = false;
			return;
		}
		if(txtNoiDung.getValue().isEmpty())
		{
			txtNoiDung.focus();
			Notification.show("Vui lòng nhập vào nội dung",Type.WARNING_MESSAGE);
			validateSuccess = false;
			return;
		}
		if(txtSoHieuVanBan.getValue().isEmpty())
		{
			txtNoiDung.focus();
			Notification.show("Vui lòng nhập vào số văn bản",Type.WARNING_MESSAGE);
			validateSuccess = false;
			return;
		}
			
		validateSuccess = true;
	}
	
	private void setTypeFileAccept(){
		List<String> typeFileAcept = new ArrayList<String>();
		try {
			typeFileAcept = svUpload.getTypeAccessedList();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		attachFile.setTypeAccept(typeFileAcept);
	}
	
	private void buildAttachFile(){
		attachFile.setBaseDirectory(UploadServiceUtil.getAbsolutePath());
		attachFile.setPathFolderUpload(UploadServiceUtil.getFolderNameDonThuVanBanXLGQ());
		attachFile.setMaxFileSize(svUpload.getMaxSize());
		
		attachFile.setMakeFolderTMP(true);
		//attachFile.setNameFolderTMP(File.separator + new Random().nextInt(1000000));
	}

	public LayoutButtonSubmit getLayoutSubmit() {
		return layoutSubmit;
	}
	public void setLayoutSubmit(LayoutButtonSubmit layoutSubmit) {
		this.layoutSubmit = layoutSubmit;
	}
	public boolean isValidateSuccess() {
		return validateSuccess;
	}
	public void setValidateSuccess(boolean validateSuccess) {
		this.validateSuccess = validateSuccess;
	}
}
