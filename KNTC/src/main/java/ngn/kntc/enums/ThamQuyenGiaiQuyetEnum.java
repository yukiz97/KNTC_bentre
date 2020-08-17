package ngn.kntc.enums;

public enum ThamQuyenGiaiQuyetEnum {
	coquanhanhchinhcaccap("H","Cơ Quan hành chính các cấp"),
	coquantuphapcaccap("G","Cơ quan tư pháp các cấp"),
	coquandang("A","Cơ quan đảng");
	
	private String type;
	private String name;
	
	private ThamQuyenGiaiQuyetEnum(String type,String name){
		this.type=type;
		this.name=name;
	}

	public String getType(){
		return type;
	}
	
	public String getName(){
		return name;
	}
}
