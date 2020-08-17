package ngn.kntc.enums;

public enum UserRole {
	LANHDAO("LANHDAO"),
	TRUONGPHONG("TRUONGPHONG"),
	TIEPCONGDAN("TIEPCONGDAN");
	
	private String name;
	private UserRole(String name)
	{
		this.name = name;
	}
	public String getName() {
		return name;
	}
}
