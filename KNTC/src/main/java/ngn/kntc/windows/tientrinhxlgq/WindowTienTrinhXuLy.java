package ngn.kntc.windows.tientrinhxlgq;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ngn.kntc.attachfile.FormAttachFile;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;
import ngn.kntc.utils.UploadServiceUtil;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public abstract class WindowTienTrinhXuLy extends Window{
	VerticalLayout vMainLayout = new VerticalLayout();
	FormLayout frmLayout = new FormLayout();
	FormAttachFile attachFile=new FormAttachFile();
	
	HorizontalLayout hButton = new HorizontalLayout();
	Button btnOk = new Button("Xác nhận",FontAwesome.SAVE);
	Button btnCancel = new Button("Hủy",FontAwesome.CLOSE);
	
	UploadServiceUtil svUpload = new UploadServiceUtil();
	QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh = new QuaTrinhXuLyGiaiQuyetServiceUtil();
	
	public WindowTienTrinhXuLy() {
	}
	
	public void buildLayout() {
		vMainLayout.addComponents(frmLayout,hButton);
		vMainLayout.setSpacing(true);
		vMainLayout.setMargin(new MarginInfo(false,true,true,true));
		
		vMainLayout.setComponentAlignment(hButton, Alignment.MIDDLE_RIGHT);
		vMainLayout.setWidth("100%");
		
		this.setContent(vMainLayout);
		this.setIcon(FontAwesome.PLUS);
		//this.setWidth("600px");
		this.setModal(true);
		this.center();
		
		frmLayout.setCaptionAsHtml(true);
		frmLayout.addStyleName("window-form-layout");
		attachFile.setCaption("Tệp đính kèm");
		
		buildControl();
		buildAttachFile();
		setTypeFileAccept();
	}

	public void configComponent() {
		btnCancel.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
	}
	
	private void buildControl()
	{
		hButton.addComponent(btnOk);
		hButton.addComponent(btnCancel);
		
		btnOk.addStyleName(ValoTheme.BUTTON_PRIMARY);
		btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
		
		hButton.setSpacing(true);
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
		//attachFile.setNameFolderTMP(File.separator + new Random().nextInt(1000000));
	}
	public abstract void validateFormSubmit();
	
	public FormAttachFile getAttachFile() {
		return attachFile;
	}

	public void setAttachFile(FormAttachFile attachFile) {
		this.attachFile = attachFile;
	}

	public Button getBtnOk() {
		return btnOk;
	}

	public void setBtnOk(Button btnOk) {
		this.btnOk = btnOk;
	}

	public Button getBtnCancel() {
		return btnCancel;
	}

	public void setBtnCancel(Button btnCancel) {
		this.btnCancel = btnCancel;
	}
}
