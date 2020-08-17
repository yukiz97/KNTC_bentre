package ngn.kntc.attachfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Random;

import com.vaadin.ui.Upload.Receiver;

public class Uploader implements Receiver{
	private File file;
	private String baseDirectory;
	private String pathFolderUpload;
	private boolean makeFolderTMP;
	private String fileNameNew;
    private String nameFolderTMP;
    private boolean flag;

    public Uploader() {
		super();
		makeFolderTMP=false;
		nameFolderTMP="";
		setFlag(true);
	}
    
	public String getFileNameNew() {
		return fileNameNew;
	}

	public String getNameFolderTMP() {
		return nameFolderTMP;
	}
	
	public void setNameFolderTMP(String nameFolderTMP) {
		this.nameFolderTMP = nameFolderTMP;
	}

	public String getPathFolderUpload() {
		return pathFolderUpload;
	}

	public void setPathFolderUpload(String pathFolderUpload) {
		this.pathFolderUpload = pathFolderUpload;
	}

	public boolean isMakeFolderTMP() {
		return makeFolderTMP;
	}

	public void setMakeFolderTMP(boolean makeFolderTMP) {
		this.makeFolderTMP = makeFolderTMP;
	}

	public String getBaseDirectory() {
		return baseDirectory;
	}

	public void setBaseDirectory(String baseDirectory) {
		this.baseDirectory = baseDirectory;
	}
	
	public String getPathFolderDestination(){
		return getBaseDirectory()+getPathFolderUpload()+getNameFolderTMP();
	}
	
	@Override
	public OutputStream receiveUpload(String filename, String mimetype) {
		fileNameNew=createFileNameNew(filename);
		FileOutputStream fos = null;
        try {
			if (isFlag()) {
				makePathFolderUpload();
				makeSubFolder();
				setFlag(false);
			}
	        file = new File(getPathFolderDestination()+File.separator+fileNameNew);
	        fos = new FileOutputStream(file);
        } 
        catch (FileNotFoundException e) {}
        return fos;
    }
	
	private String createFileNameNew(String filename){
		return randString(30)+"."+filename.split("\\.",2)[1];
	}
	
	private void makePathFolderUpload(){
		String derectory=getBaseDirectory();
		String[] arrayFolder=getPathFolderUpload().split("\\"+File.separator);
		for (String nameFolder : arrayFolder) {
			derectory+=File.separator+nameFolder;
			File filePath=new File(derectory);
			if(!filePath.exists())
				filePath.mkdir();
		}
	}
	
	private void makeSubFolder(){
		if(isMakeFolderTMP() && getNameFolderTMP().isEmpty()){
			setNameFolderTMP(File.separator+createFolder_tmp());
		}
		String derectory=getPathFolderDestination();
		File filePath=new File(derectory);
		if(!filePath.exists())
		{
			filePath.mkdir();
		}
	}
	
	private String randString(int length) {
		String character = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		int size = character.length();
		Random rand=new Random();
		String str="";
		for (int i = 0; i < length; i++) {
			str+=character.charAt(rand.nextInt(size));
		}
		return str;
	}
	
	public String createFolder_tmp(){
		Calendar now=Calendar.getInstance();
		String nameFolder;
		nameFolder="xoadi_";
		nameFolder+=now.get(Calendar.MILLISECOND)+now.get(Calendar.SECOND)+"_";
		nameFolder+="khue";
		return nameFolder;
	}
	
	public boolean renameFolderTMP(String newName){
		File old=new File(getPathFolderDestination());
		File rename=new File(getBaseDirectory()+getPathFolderUpload()+newName);
		return old.renameTo(rename);
	}
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
