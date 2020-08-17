package ngn.kntc.enums;

import java.util.Date;

import ngn.kntc.word.bieumau.WordBieuMauPattern;
import ngn.kntc.word.bieumau.WordBieuMauThongBaoChuyenDon;

public enum BieuMauEnum {
	phieuchuyendon(1,"Phiếu chuyển đơn","phieuchuyendon"+new Date().getTime()+".docx");
	
	private int type;
	private String name;
	private WordBieuMauPattern classWord;
	private String nameFile;
	
	private BieuMauEnum(int type,String name,String nameFile) {
		this.type = type;
		this.name = name;
		this.nameFile = nameFile;
		
		switch (type) {
		case 1:
			classWord = new WordBieuMauThongBaoChuyenDon();
			break;

		default:
			break;
		}
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public WordBieuMauPattern getClassWord() {
		return classWord;
	}

	public void setClassWord(WordBieuMauPattern classWord) {
		this.classWord = classWord;
	}

	public String getNameFile() {
		return nameFile;
	}

	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}
}