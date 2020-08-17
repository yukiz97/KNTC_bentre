package ngn.kntc.utils;

import java.util.ArrayList;
import java.util.List;

import ngn.kntc.enums.TypeOrgCustomFieldEnum;
import ngn.kntc.enums.UserRole;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;

public class LiferayServiceUtil {
	public static boolean isUserHasRole(long userId,String roleName) throws Exception
	{
		List<Role> userRoles = UserLocalServiceUtil.getUser(userId).getRoles();
        for(Role r : userRoles){
            if(roleName.equalsIgnoreCase(r.getName())){
            	return true;
            }
        }
		return false;
	}
	
	public static boolean isOrgHasCustomField(long orgId,String customField,String value) throws Exception
	{
		initPermissionChecker();
		Organization org = OrganizationLocalServiceUtil.getOrganization(orgId);
		String[] customfield = (String[]) org.getExpandoBridge().getAttribute(customField);
		if(customfield.length>0)
		{
			if(customfield[0].equals(value))
			{
				return true;
			}
		}
		return false;
	}
	
	public static String getOrgCustomFieldValue(long orgId,String customField) throws Exception
	{
		initPermissionChecker();
		Organization org = OrganizationLocalServiceUtil.getOrganization(orgId);
		return (String) org.getExpandoBridge().getAttribute(customField);
	}
	
	public static long getMasterOrg(long orgInput) throws Exception
	{
		long result = 0;
		if(isOrgHasCustomField(orgInput, "TypeOrg", TypeOrgCustomFieldEnum.DONVI.getName()))
		{
			result = orgInput;
		}
		else
		{
			int parent = (int) OrganizationLocalServiceUtil.getOrganization(orgInput).getParentOrganizationId();
			return getMasterOrg(parent);
		}
		return result;
	}
	
	public static String returnListTCDForSQL(long Org) throws Exception
	{
		String strListUser = "";
		List<Organization> subOrg = OrganizationLocalServiceUtil.getSuborganizations(SessionUtil.getCompanyid(),Org);
		for(long l: UserLocalServiceUtil.getOrganizationUserIds(Org))
		{
			strListUser += l+","; 
		}
		strListUser = strListUser.substring(0,strListUser.length()-1);
		if(strListUser.isEmpty())
			strListUser="-1";
		return strListUser;
	}
	
	public static List<User> returnListLanhDao() throws Exception
	{
		/* Load combobox lãnh đạo */
		List<User> listLanhDao = new ArrayList<User>();
		if(LiferayServiceUtil.isOrgHasCustomField(SessionUtil.getOrgId(), "OrgFunction", "TCD"))
			listLanhDao.addAll(UserLocalServiceUtil.getOrganizationUsers(SessionUtil.getMasterOrgId()));
		
		List<User> listUserCurrentOrg = UserLocalServiceUtil.getOrganizationUsers(SessionUtil.getOrgId());
		for(User user: listUserCurrentOrg)
		{
			long idUser = user.getUserId();
			
			if(LiferayServiceUtil.isUserHasRole(idUser, UserRole.TRUONGPHONG.getName()))
			{
				listLanhDao.add(user);
			}
		}
		
		return listLanhDao;
	}
	
	public static long getMasterOrgByUser(long idUser) throws Exception
	{
		long orgId = OrganizationLocalServiceUtil.getUserOrganizations(idUser).get(0).getOrganizationId();
		
		return getMasterOrg(orgId);
	}
	
	public static long getParentOrgId(long org) throws Exception
	{
		return OrganizationLocalServiceUtil.getOrganization(org).getParentOrganizationId();
	}
	
	public static String getUserOrgName(long idUser) throws Exception
	{
		List<Organization> orgListTemp = OrganizationLocalServiceUtil.getUserOrganizations(idUser);
		return orgListTemp.get(0).getName();
	}
	
	public static long getUserOrgId(long idUser) throws Exception
	{
		List<Organization> orgListTemp = OrganizationLocalServiceUtil.getUserOrganizations(idUser);
		return orgListTemp.get(0).getOrganizationId();
	}
	
	public static void initPermissionChecker() throws PortalException, SystemException, Exception
	{
		PermissionChecker checker = PermissionCheckerFactoryUtil.create(UserLocalServiceUtil.getUser(SessionUtil.getUserId()));
		PermissionThreadLocal.setPermissionChecker(checker);
	}
}
