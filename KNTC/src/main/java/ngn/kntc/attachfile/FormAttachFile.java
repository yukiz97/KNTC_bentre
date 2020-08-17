package ngn.kntc.attachfile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import ngn.kntc.UI.kntcUI;

public class FormAttachFile extends VerticalLayout{
	private long maxFileSize;
	private Uploader receiver = new Uploader();
	private Upload upload = new Upload (null, receiver);
	private VerticalLayout vUploading=new VerticalLayout();
	private VerticalLayout progressLayout = new VerticalLayout();
	private Label status=new Label();
	private ProgressBar progressBar = new ProgressBar(0.0f);
	private Button cancelUploading=new Button("Hủy",FontAwesome.REMOVE);
	private VerticalLayout vShowFile=new VerticalLayout();
	Label lblNameFile;
	
	private String fileNameNew=null;
	private String fileNameOld=null;
	private List<String> arrayTypeAccept=new ArrayList<String>();
	private boolean success=true;
	
	
	public FormAttachFile() {
		buildLayout();
		configComponents();
	}
	
	public FormAttachFile(String baseDirectory) {
		buildLayout();
		configComponents();
		setBaseDirectory(baseDirectory);
	}
	
	public FormAttachFile(String baseDirectory,String pathFolderUpload) {
		buildLayout();
		configComponents();
		setBaseDirectory(baseDirectory);
		setPathFolderUpload(pathFolderUpload);
	}
	
	public void buildLayout(){
		vShowFile.setSizeFull();
		
		progressBar.setWidth(100, Unit.PERCENTAGE);
		cancelUploading.setStyleName(ValoTheme.BUTTON_TINY);
		cancelUploading.addStyleName(ValoTheme.BUTTON_DANGER);
		progressLayout.setWidth(100,Unit.PERCENTAGE);
        progressLayout.addComponents(progressBar,cancelUploading);
        progressLayout.setComponentAlignment(progressBar, Alignment.MIDDLE_CENTER);
        progressLayout.setComponentAlignment(cancelUploading, Alignment.MIDDLE_CENTER);
        progressLayout.setWidth(100,Unit.PERCENTAGE);
        progressLayout.setSpacing(true);
        
        vUploading.setSizeFull();
        vUploading.addComponents(status,progressLayout);
        vUploading.setVisible(false);
        
        upload.setImmediate(true);
		upload.setButtonCaption("Chọn tệp tin");
		
		this.setSpacing(true);
		this.setWidth(100,Unit.PERCENTAGE);
		this.addComponents(vShowFile,vUploading,upload);
		this.setComponentAlignment(upload, Alignment.MIDDLE_LEFT);
	}
	
	public void configComponents(){
		cancelUploading.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                upload.interruptUpload();
                return;
            }
        });
		
		upload.addStartedListener(new Upload.StartedListener() {
			public void uploadStarted(StartedEvent event) {  
				String type=event.getMIMEType().toString();
            	if(!arrayTypeAccept.contains(type)){
            		Notification.show("Định dạng file này không được đính kèm",Notification.Type.ERROR_MESSAGE);
            		event.getUpload().interruptUpload();
            		return;
            	}
            	beginUpload(event);
            }
        });

        upload.addProgressListener(new Upload.ProgressListener() {
            public void updateProgress(long readBytes, long contentLength) { 
                progressBar.setValue(new Float(readBytes / (float) contentLength));
            }
        });

        upload.addSucceededListener(new Upload.SucceededListener() {
			public void uploadSucceeded(SucceededEvent event) {
            	uploadSuccess(event);
            }
        });

        upload.addFailedListener(new Upload.FailedListener() {
            public void uploadFailed(FailedEvent event) {
            	uploadFail();
            }
        });

        upload.addFinishedListener(new Upload.FinishedListener() {
            public void uploadFinished(FinishedEvent event) {
            	finishUpload(event.getFilename());
            }
        });
	}
	
	public void beginUpload(StartedEvent event){
		deleteFileBeforeOld();
		setSuccess(false);
    	if(event.getContentLength()>maxFileSize){
    		Notification.show("File quá lớn",Notification.Type.ERROR_MESSAGE);
    		upload.interruptUpload();
			return;
		}
    	vUploading.setVisible(true);
        progressBar.setValue(0f);
       	kntcUI.getCurrent().setPollInterval(1000);
        status.setValue("Đang upload tệp: " + event.getFilename());
	}
	
	public void uploadSuccess(SucceededEvent event){
		fileNameNew=receiver.getFileNameNew();
		fileNameOld=event.getFilename();
		kntcUI.getCurrent().setPollInterval(-1);
	}
	
	public void finishUpload(String fileName){
		vUploading.setVisible(false);
        upload.setButtonCaption("Chọn file khác");
        setSuccess(true);
        showListAttachFiles();
        kntcUI.getCurrent().setPollInterval(-1);
	}
	
	public void uploadFail(){
		status.setValue("Uploading interrupted");
		String derectory=receiver.getPathFolderDestination()+File.separator+receiver.getFileNameNew();
		File file=new File(derectory);
		if(file.exists())
			deleteFile(derectory);
		setSuccess(true);
		kntcUI.getCurrent().setPollInterval(-1);
	}
	
	public void showListAttachFiles(){
		this.vShowFile.removeAllComponents();
		lblNameFile = new Label(fileNameOld);
		lblNameFile.addStyleName(ValoTheme.LABEL_COLORED);
		this.vShowFile.addComponent(lblNameFile);
	}
	
	public boolean deleteFile(String pathFile){
		return new File(pathFile).delete();
	}
	
	public void deleteFileBeforeOld(){
		try {
			String derectory=receiver.getPathFolderDestination();
			String address=derectory+File.separator+fileNameNew;
			new File(address).delete();
			fileNameNew=null;
			fileNameOld=null;
		} catch (Exception e) {}
	}
	
	public Upload getUpload() {
		return upload;
	}

	public void setUpload(Upload upload) {
		this.upload = upload;
	}

	public Label getLblNameFile() {
		return lblNameFile;
	}

	public void setLblNameFile(Label lblNameFile) {
		this.lblNameFile = lblNameFile;
	}

	public void setFileNameNew(String fileNameNew) {
		this.fileNameNew = fileNameNew;
	}

	public void setFileNameOld(String fileNameOld) {
		this.fileNameOld = fileNameOld;
	}

	public boolean renameFolderTMP(String newName){
		return this.receiver.renameFolderTMP(newName);
	}

	public String getBaseDirectory() {
		return receiver.getBaseDirectory();
	}

	public void setBaseDirectory(String baseDirectory) {
		this.receiver.setBaseDirectory(baseDirectory.trim());
	}

	public String getPathFolderUpload() {
		return receiver.getPathFolderUpload();
	}

	public void setPathFolderUpload(String pathFolderUpload) {
		this.receiver.setPathFolderUpload(pathFolderUpload.trim());
	}

	public String getNameFolderTMP() {
		return receiver.getNameFolderTMP().split("\\"+File.separator)[1];
	}
	
	public void setNameFolderTMP(String nameFolderTMP) {
		this.receiver.setNameFolderTMP(nameFolderTMP.trim());
	}
	
	public boolean isMakeFolderTMP() {
		return receiver.isMakeFolderTMP();
	}

	public void setMakeFolderTMP(boolean makeFolderTMP) {
		this.receiver.setMakeFolderTMP(makeFolderTMP);
	}
	
	public long getMaxFileSize() {
		return maxFileSize;
	}

	public void setMaxFileSize(long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<String> getArrayTypeAccept() {
		return arrayTypeAccept;
	}

	public void setTypeAccept(List<String> arrayTypeAccept) {
		for (String typeAccept : arrayTypeAccept) {
			addTypeAccept(typeAccept);
		}
	}

	public void addTypeAccept(String typeAccept) {
		this.arrayTypeAccept.add(typeAccept);
	}

	public String getFileNameNew() {
		return fileNameNew;
	}

	public String getFileNameOld() {
		return fileNameOld;
	}
	
}
	