package ngn.kntc.windows;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ngn.kntc.attachfile.FormAttachFile;
import ngn.kntc.beans.QuaTrinhXuLyGiaiQuyetBean;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;
import ngn.kntc.utils.SessionUtil;
import ngn.kntc.utils.UploadServiceUtil;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class WindowThemQuaTrinhXLGQ extends Window{
	private VerticalLayout layout =new VerticalLayout();
	private RichTextArea txtDescription=new RichTextArea();
	private FormAttachFile attachFile=new FormAttachFile();
	private HorizontalLayout hControl=new HorizontalLayout();
	private Button btnSubmit=new Button("Xác nhận",FontAwesome.PLUS);
	private Button btnCancel=new Button("Đóng",FontAwesome.CLOSE);

	private QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh =new QuaTrinhXuLyGiaiQuyetServiceUtil();
	private UploadServiceUtil svUpload = new UploadServiceUtil();

	private int idDonThu; 
	
	private boolean validateSucccess = false;
	
	public WindowThemQuaTrinhXLGQ(int idDonThu) {
		this.idDonThu = idDonThu;
		buildLayout();
		configComponent();
		setTypeFileAccept();
	}

	private void buildLayout(){
		this.setContent(layout);

		buildDescription();
		buildAttachFile();
		buildControl();

		layout.addComponent(txtDescription);
		layout.addComponent(attachFile);
		layout.addComponent(hControl);
		
		attachFile.setNameFolderTMP(File.separator+idDonThu);

		layout.setComponentAlignment(hControl, Alignment.MIDDLE_RIGHT);
		layout.setWidth("100%");
		layout.setMargin(true);
		layout.setSpacing(true);

		this.setCaption(FontAwesome.PLUS.getHtml()+" Thêm quá trình xử lý, giải quyết");
		this.setCaptionAsHtml(true);
		this.center();
		this.setModal(true);
		this.setWidth(80,Unit.PERCENTAGE);
		this.setResizable(false);
	}

	private void configComponent(){
		btnSubmit.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				isCompleted();
				if(validateSucccess)
				{
					QuaTrinhXuLyGiaiQuyetBean model = new QuaTrinhXuLyGiaiQuyetBean();
					String noiDung = txtDescription.getValue().trim();
					if(SessionUtil.isLanhDaoDonVi())
						noiDung = "<b style='color: red'>"+noiDung+"</b>";
					model.setNoiDung(noiDung);
					model.setUserNhap(SessionUtil.getUserId());
					model.setNgayDang(new Date());
					model.setMaDonThu(idDonThu);
					if(attachFile.getFileNameNew()!=null)
					{
						model.setTenFileDinhKem(attachFile.getFileNameOld());
						model.setLinkFileDinhKem(attachFile.getFileNameNew());
					}
					model.setHeThongTao(false);
					
					try {
						int idQuaTrinh = svQuaTrinh.insertQuaTrinhXLGQ(model);
						DonThuModule.insertThongBao(idDonThu, idQuaTrinh);
					} catch (SQLException e) {
						e.printStackTrace();
					}
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

	private void buildDescription(){
		layout.addComponent(txtDescription);
		txtDescription.setHeight("300px");
		txtDescription.setWidth("100%");
	}

	private void buildAttachFile(){
		attachFile.setBaseDirectory(UploadServiceUtil.getAbsolutePath());
		attachFile.setPathFolderUpload(UploadServiceUtil.getFolderNameDonThu());
		attachFile.setMaxFileSize(svUpload.getMaxSize());
		
		attachFile.setMakeFolderTMP(true);
	}

	private void buildControl(){
		btnSubmit.setCaption("Thêm");
		btnSubmit.setIcon(FontAwesome.PLUS);
		hControl.addComponent(btnSubmit);
		hControl.addComponent(btnCancel);
		btnSubmit.addStyleName(ValoTheme.BUTTON_SMALL);
		btnSubmit.addStyleName(ValoTheme.BUTTON_PRIMARY);
		btnCancel.addStyleName(ValoTheme.BUTTON_SMALL);
		btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);

		hControl.setSpacing(true);
	}

	public FormAttachFile getAttachFile() {
		return attachFile;
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

	public Button getBtnSubmit() {
		return btnSubmit;
	}

	public void isCompleted(){
		if(txtDescription.getValue().isEmpty()){
			Notification.show("LỖI", "Nội dung quá trình không được để trống", Type.WARNING_MESSAGE);
			validateSucccess = false;
		}
		validateSucccess = true;
	}

	public String getTxtDescription(){
		return txtDescription.getValue().trim();
	}

	public boolean isValidateSucccess() {
		return validateSucccess;
	}
	public void setValidateSucccess(boolean validateSucccess) {
		this.validateSucccess = validateSucccess;
	}
}
