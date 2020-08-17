package ngn.kntc.enums;

public enum TypeOrgCustomFieldEnum {
	DONVI("DONVI"),
	PHONGBAN("PHONGBAN"),
	SOROOT("SOROOT"),
	DONVIROOT("DONVIROOT"),
	SO("SO");
	
	private String name;
	private TypeOrgCustomFieldEnum(String name)
	{
		this.name = name;
	}
	public String getName() {
		return name;
	}
}
