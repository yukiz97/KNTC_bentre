package ngn.kntc.beans;

import ngn.kntc.word.bieumau.WordBieuMauPattern;

public class BieuMauBean{
	private String name;
	private WordBieuMauPattern classWord;
	private String nameFile;
	
	public BieuMauBean(String name,WordBieuMauPattern classWord,String nameFile) {
		this.name = name;
		this.classWord = classWord;
		this.nameFile = nameFile;
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
