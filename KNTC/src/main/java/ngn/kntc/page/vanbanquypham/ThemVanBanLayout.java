package ngn.kntc.page.vanbanquypham;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ngn.kntc.UI.kntcUI;
import ngn.kntc.attachfile.FormAttachFile;
import ngn.kntc.beans.VanBanQuyPhamBean;
import ngn.kntc.enums.LoaiVanBanEnum;
import ngn.kntc.modules.KNTCProps;
import ngn.kntc.utils.SessionUtil;
import ngn.kntc.utils.UploadServiceUtil;
import ngn.kntc.utils.VanBanQuyPhamServiceUtil;
import ngn.kntc.views.vanbanquypham.ViewDanhSachVanBan;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ThemVanBanLayout extends VerticalLayout{
	private Label lblMainCaption = new Label(FontAwesome.PLUS_CIRCLE.getHtml()+" Thêm văn bản quy phạm pháp luật",ContentMode.HTML);
	private Label lblSubCaption = new Label("+ Thêm những văn bản quy phạm cần thiết để tiện tra cứu trong quá trình xử lý, giải quyết đơn thư. ",ContentMode.HTML);

	private FormLayout frmVanban = new FormLayout();
	private Panel pnlForm = new Panel();
	private TextField txtTenvanban = new TextField("Tên văn bản <span style='color: red'>(*)</span>");
	private TextField txtSohieu = new TextField("Số hiệu <span style='color: red'>(*)</span>");
	private RichTextArea txtTrichdan = new RichTextArea("Trích dẫn <span style='color: red'>(*)</span>");
	private TextField txtCoquanbanhanh = new TextField("Cơ quan ban hành <span style='color: red'>(*)</span>");
	private TextField txtNguoiky = new TextField("Người ký <span style='color: red'>(*)</span>");
	private DateField dfNgaybanhanh = new DateField("Ngày ban hành <span style='color: red'>(*)</span>");
	private OptionGroup ogLoaiVanBan = new OptionGroup("Loại văn bản");

	private HorizontalLayout hControl = new HorizontalLayout();
	private Button btnLuu = new Button("Lưu");
	private Button btnHuy = new Button("Hủy");
	private FormAttachFile formAttachFile = new FormAttachFile();

	String folderUpload=KNTCProps.getProperty("file.folder.upload.vanban");	
	String folderTMP="xoadi_"+Calendar.getInstance().getTimeInMillis();

	VanBanQuyPhamServiceUtil svVanBan = new VanBanQuyPhamServiceUtil();

	VanBanQuyPhamBean modelVanBan = new VanBanQuyPhamBean();

	private boolean isValidateSuccess = false;
	private int formId;

	public ThemVanBanLayout(int vbId)
	{
		formId = vbId;
		buildLayout();
		configComponent();
		if(formId != -1)
		{
			try {
				fillForm();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public void buildLayout()
	{
		txtTenvanban.setWidth("100%");
		txtSohieu.setWidth("100%");
		txtTrichdan.setWidth("100%");
		txtCoquanbanhanh.setWidth("100%");
		txtNguoiky.setWidth("100%");
		dfNgaybanhanh.setDateFormat("dd/MM/yyyy");
		dfNgaybanhanh.setValue(new Date());

		dfNgaybanhanh.setCaptionAsHtml(true);
		txtTenvanban.setCaptionAsHtml(true);
		txtSohieu.setCaptionAsHtml(true);
		txtTrichdan.setCaptionAsHtml(true);
		txtCoquanbanhanh.setCaptionAsHtml(true);
		txtNguoiky.setCaptionAsHtml(true);

		frmVanban.addComponent(txtTenvanban);
		frmVanban.addComponent(txtSohieu);
		frmVanban.addComponent(txtTrichdan);
		frmVanban.addComponent(txtCoquanbanhanh);
		frmVanban.addComponent(txtNguoiky);
		frmVanban.addComponent(dfNgaybanhanh);
		frmVanban.addComponent(ogLoaiVanBan);
		frmVanban.addComponent(formAttachFile);

		frmVanban.addStyleName("form-them-vanban");
		frmVanban.setWidth("100%");
		frmVanban.setCaptionAsHtml(true);

		pnlForm.setContent(frmVanban);

		lblMainCaption.addStyleName("lbl-caption-main");

		this.addComponent(lblMainCaption);
		this.addComponent(lblSubCaption);
		this.addComponent(pnlForm);
		this.addComponent(hControl);

		this.setComponentAlignment(hControl, Alignment.MIDDLE_RIGHT);
		this.setMargin(new MarginInfo(false,true,false,true));
		this.setSpacing(true);

		buildControl();
		buildOptionGroupLoaiVanBan();
		buildUpload();
	}

	public void configComponent()
	{
		btnLuu.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				validateForm();
				if(isValidateSuccess)
				{
					VanBanQuyPhamBean model = new VanBanQuyPhamBean();
					String tenVanBan = txtTenvanban.getValue();
					String soHieu = txtSohieu.getValue();
					String trichDan = txtTrichdan.getValue();
					String nguoiKy = txtNguoiky.getValue();
					String cqBanHanh = txtCoquanbanhanh.getValue();
					Date ngayBanHanh = dfNgaybanhanh.getValue();
					String loaiVanBan = ogLoaiVanBan.getValue().toString();
					loaiVanBan = loaiVanBan.substring(1, loaiVanBan.length()-1).replace(" ", "");

					model.setTenVanBan(tenVanBan);
					model.setSoHieu(soHieu);
					model.setTrichDan(trichDan);
					model.setNguoiKy(nguoiKy);
					model.setCoQuanBanHanh(cqBanHanh);
					model.setNgayBanHanh(ngayBanHanh);
					model.setLoaiVanBan(loaiVanBan);
					model.setTenFileDinhKem(formAttachFile.getFileNameOld());
					model.setLinkFileDinhKem(formAttachFile.getFileNameNew());
					model.setOwner((int)SessionUtil.getUserId());
					model.setId(formId);
					modelVanBan = model;
					try {
						if(formId == -1)
						{
							formId = svVanBan.insertVanBanQuyPham(model);
							File old=new File(UploadServiceUtil.getAbsolutePath()+File.separator+UploadServiceUtil.getFolderNameVanBan()+File.separator+folderTMP);
							File rename=new File(UploadServiceUtil.getAbsolutePath()+File.separator+UploadServiceUtil.getFolderNameVanBan()+File.separator+formId);
							old.renameTo(rename);
							Notification.show("Thêm văn bản thành công",Type.TRAY_NOTIFICATION);
							kntcUI.getCurrent().getNavigator().navigateTo(ViewDanhSachVanBan.NAME);
						}
						else
						{
							svVanBan.updateVanBanQuyPham(model);
							File old=new File(UploadServiceUtil.getAbsolutePath()+File.separator+UploadServiceUtil.getFolderNameVanBan()+File.separator+folderTMP+File.separator+model.getLinkFileDinhKem());
							old.renameTo(new File(UploadServiceUtil.getAbsolutePath()+File.separator+UploadServiceUtil.getFolderNameVanBan()+File.separator+formId+File.separator+model.getLinkFileDinhKem()));
							Notification.show("Cập nhật văn bản thành công",Type.TRAY_NOTIFICATION);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});

		btnHuy.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if(formId==-1)
				{
					kntcUI.getCurrent().getNavigator().navigateTo(ViewDanhSachVanBan.NAME);
				}
			}
		});
	}

	public void fillForm() throws SQLException
	{
		VanBanQuyPhamBean vb = svVanBan.getVanBanQuyPham(this.formId);
		txtTenvanban.setValue(vb.getTenVanBan());
		txtCoquanbanhanh.setValue(vb.getCoQuanBanHanh());
		txtNguoiky.setValue(vb.getNguoiKy());
		txtSohieu.setValue(vb.getSoHieu());
		txtTrichdan.setValue(vb.getTrichDan());
		//Notification.show(vb.getNgayBanHanh()+"");
		dfNgaybanhanh.setValue(vb.getNgayBanHanh());

		for(String type : vb.getLoaiVanBan().split(","))
		{
			ogLoaiVanBan.select(Integer.parseInt(type));
		}
		formAttachFile.setFileNameNew(vb.getLinkFileDinhKem());
		formAttachFile.setFileNameOld(vb.getTenFileDinhKem());
		formAttachFile.showListAttachFiles();
	}
	public void buildUpload()
	{
		formAttachFile.setBaseDirectory(VaadinService.getCurrent().getBaseDirectory().getAbsolutePath());
		formAttachFile.setPathFolderUpload(File.separator+folderUpload);
		formAttachFile.setMakeFolderTMP(true);
		formAttachFile.setNameFolderTMP(File.separator+folderTMP);
		formAttachFile.setMaxFileSize(500000000);
		List<String> typeFileAcept=new ArrayList<String>();
		typeFileAcept.add("application/vnd.ms-excel");
		typeFileAcept.add("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		typeFileAcept.add("application/msword");
		typeFileAcept.add("application/vnd.ms-powerpoint");
		typeFileAcept.add("application/vnd.openxmlformats-officedocument.presentationml.presentation");
		typeFileAcept.add("application/pdf");
		typeFileAcept.add("image/png");
		typeFileAcept.add("application/octet-stream");
		typeFileAcept.add("application/octet-stream");
		typeFileAcept.add("application/octet-stream");
		typeFileAcept.add("application/octet-stream");
		formAttachFile.setTypeAccept(typeFileAcept);
	}

	public void buildControl()
	{
		hControl.setSpacing(true);
		btnLuu.setIcon(FontAwesome.SAVE);
		btnLuu.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnHuy.setIcon(FontAwesome.CLOSE);
		btnHuy.addStyleName(ValoTheme.BUTTON_DANGER);

		btnLuu.addStyleName(ValoTheme.BUTTON_SMALL);
		btnHuy.addStyleName(ValoTheme.BUTTON_SMALL);

		hControl.addComponent(btnLuu);
		hControl.addComponent(btnHuy);
	}

	public void buildOptionGroupLoaiVanBan()
	{
		ogLoaiVanBan.setMultiSelect(true);
		for(LoaiVanBanEnum e : LoaiVanBanEnum.values())
		{
			ogLoaiVanBan.addItem(e.getType());
			ogLoaiVanBan.setItemCaption(e.getType(), e.getName());
		}
	}

	public void setCaptionInvisible()
	{
		lblMainCaption.setVisible(false);
		lblSubCaption.setVisible(false);
	}

	public void validateForm()
	{
		if(txtTenvanban.isEmpty())			
		{
			Notification.show("Tên văn bản không được để trống",Type.WARNING_MESSAGE);
			txtTenvanban.focus();
			isValidateSuccess = false;
			return;
		}
		if(txtSohieu.isEmpty())
		{
			Notification.show("Số hiệu không được để trống",Type.WARNING_MESSAGE);
			txtSohieu.focus();
			isValidateSuccess = false;
			return;
		}
		if(txtTrichdan.isEmpty())
		{
			Notification.show("Trích dẫn không được để trống",Type.WARNING_MESSAGE);
			txtTrichdan.focus();
			isValidateSuccess = false;
			return;
		}
		if(txtCoquanbanhanh.isEmpty())
		{
			Notification.show("Cơ quan ban hành không được để trống",Type.WARNING_MESSAGE);
			txtCoquanbanhanh.focus();
			isValidateSuccess = false;
			return;
		}
		if(dfNgaybanhanh.isEmpty())
		{
			Notification.show("Ngày ban hành không được để trống",Type.WARNING_MESSAGE);
			dfNgaybanhanh.focus();
			isValidateSuccess = false;
			return;
		}
		isValidateSuccess =  true;
	}
	public Button getBtnLuu() {
		return btnLuu;
	}
	public void setBtnLuu(Button btnLuu) {
		this.btnLuu = btnLuu;
	}
	public Button getBtnHuy() {
		return btnHuy;
	}
	public void setBtnHuy(Button btnHuy) {
		this.btnHuy = btnHuy;
	}
	public FormAttachFile getFormAttachFile() {
		return formAttachFile;
	}
	public void setFormAttachFile(FormAttachFile formAttachFile) {
		this.formAttachFile = formAttachFile;
	}
	public VanBanQuyPhamBean getModelVanBan() {
		return modelVanBan;
	}
	public void setModelVanBan(VanBanQuyPhamBean modelVanBan) {
		this.modelVanBan = modelVanBan;
	}
	public boolean isValidateSuccess() {
		return isValidateSuccess;
	}
	public void setValidateSuccess(boolean isValidateSuccess) {
		this.isValidateSuccess = isValidateSuccess;
	}
}
