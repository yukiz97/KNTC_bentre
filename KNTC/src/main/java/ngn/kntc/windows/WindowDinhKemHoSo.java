package ngn.kntc.windows;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ngn.kntc.attachfile.FormAttachFile;
import ngn.kntc.beans.HoSoDinhKemBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.UploadServiceUtil;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class WindowDinhKemHoSo extends Window{
	FormLayout frmLayout = new FormLayout();
	TextField txtTenHoSo = new TextField("Tên hồ sơ<span style='color:red'>(*)<span>");
	ComboBox cmbLoaiHoSo = new ComboBox("Loại hồ sơ<span style='color:red'>(*)<span>");
	RichTextArea txtNoiDungTomTat = new RichTextArea("Nội dung tóm tắt");
	FormAttachFile attachFile=new FormAttachFile();
	
	HorizontalLayout hButton = new HorizontalLayout();
	Button btnOk = new Button("Xác nhận",FontAwesome.SAVE);
	Button btnCancel = new Button("Hủy",FontAwesome.CLOSE);
	
	VerticalLayout vMainLayout = new VerticalLayout();
	
	HoSoDinhKemBean dinhKemHoSoBean = new HoSoDinhKemBean();
	
	DanhMucServiceUtil svDanhMuc = new DanhMucServiceUtil();
	UploadServiceUtil svUpload = new UploadServiceUtil();
	
	boolean validateSuccess;
	
	public WindowDinhKemHoSo() {
		buildLayout();
		configComponent();
	}
	
	public WindowDinhKemHoSo(HoSoDinhKemBean dinhKemHoSoBean) {
		buildLayout();
		configComponent();
		
		txtTenHoSo.setValue(dinhKemHoSoBean.getTenHoSo());
		cmbLoaiHoSo.setValue(dinhKemHoSoBean.getLoaiHoSo());
		txtNoiDungTomTat.setValue(dinhKemHoSoBean.getNoiDungTomTat());
		attachFile.setFileNameNew(dinhKemHoSoBean.getLinkFileDinhKem());
		attachFile.setFileNameOld(dinhKemHoSoBean.getTenFileDinhKem());
		attachFile.showListAttachFiles();
	}

	private void buildLayout() {
		vMainLayout.addComponents(frmLayout,hButton);
		vMainLayout.setSpacing(true);
		vMainLayout.setMargin(new MarginInfo(false,true,true,true));
		
		vMainLayout.setComponentAlignment(hButton, Alignment.MIDDLE_RIGHT);
		
		this.setContent(vMainLayout);
		this.setCaption(" Thêm hồ sơ đính kèm của đơn thư");
		this.setIcon(FontAwesome.PLUS);
		this.setModal(true);
		this.setWidth("650px");
		this.center();
		
		try {
			buildForm();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		buildControl();
		buildAttachFile();
		setTypeFileAccept();
	}

	private void configComponent() {
		btnOk.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				validateSuccess = validateForm();
				if(validateSuccess)
				{
					dinhKemHoSoBean.setTenHoSo(txtTenHoSo.getValue());
					dinhKemHoSoBean.setLoaiHoSo((int)cmbLoaiHoSo.getValue());
					dinhKemHoSoBean.setNoiDungTomTat(txtNoiDungTomTat.getValue());
					dinhKemHoSoBean.setTenFileDinhKem(attachFile.getFileNameOld());
					dinhKemHoSoBean.setLinkFileDinhKem(attachFile.getFileNameNew());
				}
			}
		});
		btnCancel.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
	}
	
	private void buildForm() throws SQLException
	{
		frmLayout.addComponent(txtTenHoSo);
		frmLayout.addComponent(cmbLoaiHoSo);
		frmLayout.addComponent(txtNoiDungTomTat);
		frmLayout.addComponent(attachFile);
		
		txtTenHoSo.setCaptionAsHtml(true);
		cmbLoaiHoSo.setCaptionAsHtml(true);
		attachFile.setCaptionAsHtml(true);
		
		txtTenHoSo.setWidth("100%");
		cmbLoaiHoSo.setWidth("50%");
		txtNoiDungTomTat.setWidth("100%");
		
		cmbLoaiHoSo.setTextInputAllowed(false);
		cmbLoaiHoSo.setNullSelectionAllowed(false);
		
		frmLayout.setCaptionAsHtml(true);
		frmLayout.addStyleName("window-form-layout");
		attachFile.setCaption("Tệp đính kèm<span style='color:red'>(*)<span>");
		
		svDanhMuc.setValueDefaultForComboboxDanhMuc(cmbLoaiHoSo, DanhMucTypeEnum.loaitailieu.getName(), DanhMucTypeEnum.loaitailieu.getIdType());
		cmbLoaiHoSo.select(1);
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
		attachFile.setPathFolderUpload(UploadServiceUtil.getFolderNameDonThu());
		attachFile.setMaxFileSize(svUpload.getMaxSize());
		
		attachFile.setMakeFolderTMP(true);
	}
	
	private void buildControl()
	{
		hButton.addComponent(btnOk);
		hButton.addComponent(btnCancel);
		
		btnOk.addStyleName(ValoTheme.BUTTON_PRIMARY);
		btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
		
		hButton.setSpacing(true);
	}
	
	private boolean validateForm()
	{
		if(txtTenHoSo.isEmpty())
		{
			Notification.show("Vui lòng nhập vào tên hồ sơ",Type.WARNING_MESSAGE);
			txtTenHoSo.focus();
			return false;
		}
		if(attachFile.getFileNameNew()==null)
		{
			Notification.show("Vui lòng chọn tệp tin đính kèm",Type.WARNING_MESSAGE);
			return false;
		}
		return true;
	}
	
	public FormAttachFile getAttachFile() {
		return attachFile;
	}
	public void setAttachFile(FormAttachFile attachFile) {
		this.attachFile = attachFile;
	}
	public boolean isValidateSuccess() {
		return validateSuccess;
	}
	public void setValidateSuccess(boolean validateSuccess) {
		this.validateSuccess = validateSuccess;
	}
	public Button getBtnOk() {
		return btnOk;
	}
	public void setBtnOk(Button btnOk) {
		this.btnOk = btnOk;
	}
	public HoSoDinhKemBean getDinhKemHoSoBean() {
		return dinhKemHoSoBean;
	}
	public void setDinhKemHoSoBean(HoSoDinhKemBean dinhKemHoSoBean) {
		this.dinhKemHoSoBean = dinhKemHoSoBean;
	}
}
