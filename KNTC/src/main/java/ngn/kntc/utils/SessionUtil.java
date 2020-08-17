package ngn.kntc.utils;


import ngn.kntc.modules.KNTCProps;

import com.liferay.portal.model.User;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;

public class SessionUtil {
	final static String USERID="USERID";
	final static String USER = "USER";
	final static String ORGID="ORGID";
	final static String MASTERORGID = "MASTERORGID";
	final static String LIENTHONGORGID = "LIENTHONGORGID";
	final static String COMPANYID="COMPANYID";
	
	final static String ROLELANHDAO="ROLELANHDAO";
	final static String ROLETCD="ROLETCD";
	final static String ROLETRUONGPHONG="ROLETRUONGPHONG";
	
	public static void init(){
		KNTCProps props = new  KNTCProps();
 		long companyId=Long.parseLong(props.getProperty("liferay.companyid").toString());
 		SessionUtil.setCompanyId(companyId);
 		
 		VaadinSession.getCurrent().setAttribute(ROLELANHDAO,false);
 		VaadinSession.getCurrent().setAttribute(ROLETCD,false);
 		VaadinSession.getCurrent().setAttribute(ROLETRUONGPHONG,false);
 	}
	
	public static void setCompanyId(long i){
		VaadinSession.getCurrent().setAttribute(COMPANYID, i);
	}
	
	public static int getCompanyid() {
		int companyId;
		try{
			companyId=Integer.parseInt(VaadinSession.getCurrent().getAttribute(COMPANYID).toString());
			return companyId;
		}catch(Exception e){
			return -1;
		}
	}
	
	public static long getUserId() {
		long userId;
		try{
			userId=(long) VaadinSession.getCurrent().getAttribute(USERID);
			return userId;
		}catch(Exception e){
			return -1;
		}
	}
	
	public static User getUser() {
		User userId;
		try{
			userId=(User) VaadinSession.getCurrent().getAttribute(USER);
			return userId;
		}catch(Exception e){
			return null;
		}
	}
	
	public static long getOrgId() {
		long locationId;
		try{
			locationId=(long) VaadinSession.getCurrent().getAttribute(ORGID);
			return locationId;
		}catch(Exception e){
			return -1;
		}
	}
	
	public static long getMasterOrgId() {
		long masterOrgId;
		try{
			masterOrgId=(long) VaadinSession.getCurrent().getAttribute(MASTERORGID);
			return masterOrgId;
		}catch(Exception e){
			return -1;
		}
	}
	
	public static String getLienThongOrgId() {
		String masterOrgId;
		try{
			masterOrgId=(String) VaadinSession.getCurrent().getAttribute(LIENTHONGORGID);
			return masterOrgId;
		}catch(Exception e){
			return null;
		}
	}
	
	public static boolean isLanhDaoDonVi() {
		boolean hasRole;
		try{
			hasRole=(boolean) VaadinSession.getCurrent().getAttribute(ROLELANHDAO);
			return hasRole;
		}catch(Exception e){
			return false;
		}
	}
	
	public static boolean isTiepCongDan() {
		boolean hasRole;
		try{
			hasRole=(boolean) VaadinSession.getCurrent().getAttribute(ROLETCD);
			return hasRole;
		}catch(Exception e){
			return false;
		}
	}
	
	public static boolean isTruongPhong() {
		boolean hasRole;
		try{
			hasRole=(boolean) VaadinSession.getCurrent().getAttribute(ROLETRUONGPHONG);
			return hasRole;
		}catch(Exception e){
			return false;
		}
	}
	
	public static boolean isLogin(){
		try {
			if(VaadinService.getCurrentRequest().getWrappedSession().getAttribute("LOGIN").toString().isEmpty())
				return false;
			else{
				VaadinSession.getCurrent().setAttribute("LOGIN", VaadinService.getCurrentRequest().getWrappedSession().getAttribute("LOGIN").toString());
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void logout(){
 		VaadinSession.getCurrent().setAttribute(USERID, null);
 		VaadinSession.getCurrent().setAttribute(USER, null);
 		VaadinSession.getCurrent().setAttribute(ORGID, null);
 		VaadinSession.getCurrent().setAttribute(MASTERORGID, null);
 		VaadinSession.getCurrent().setAttribute(LIENTHONGORGID, null);
 		VaadinSession.getCurrent().setAttribute(COMPANYID, null);
 		VaadinSession.getCurrent().setAttribute(ROLELANHDAO, null);
 		VaadinSession.getCurrent().setAttribute(ROLETCD, null);
 		VaadinSession.getCurrent().setAttribute(ROLETRUONGPHONG, null);
 	}
	
	public static void setUserId(long userId) {
		VaadinSession.getCurrent().setAttribute(USERID,userId);
	}
	
	public static void setUser(User user) {
		VaadinSession.getCurrent().setAttribute(USER,user);
	}
	
	public static void setOrgId(long locationId) {
		VaadinSession.getCurrent().setAttribute(ORGID,locationId);
	}
	
	public static void setMasterOrgId(long masterLocationId) {
		VaadinSession.getCurrent().setAttribute(MASTERORGID,masterLocationId);
	}
	
	public static void setLienThongOrgId(String lienThongOrgID) {
		VaadinSession.getCurrent().setAttribute(LIENTHONGORGID,lienThongOrgID);
	}
	
	public static void setRoleLanhDao() {
		VaadinSession.getCurrent().setAttribute(ROLELANHDAO,true);
	}
	public static void setRoleTCD() {
		VaadinSession.getCurrent().setAttribute(ROLETCD,true);
	}
	public static void setRoleTruongPhong() {
		VaadinSession.getCurrent().setAttribute(ROLETRUONGPHONG,true);
	}
}
