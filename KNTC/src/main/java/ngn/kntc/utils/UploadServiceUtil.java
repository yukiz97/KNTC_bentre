package ngn.kntc.utils;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.server.VaadinService;

import ngn.kntc.databases.DatabaseServices;
import ngn.kntc.modules.KNTCProps;

public class UploadServiceUtil {
	public long getMaxSize(){
		return Long.parseLong(KNTCProps.getProperty("file.folder.maxsize"));
	}
	
	public static String getAbsolutePath(){
		return VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	}

	public static String getFolderNameVanBan(){
		return File.separator+KNTCProps.getProperty("file.folder.upload.vanban");
	}
	
	public static String getFolderNameDonThu(){
		return File.separator+KNTCProps.getProperty("file.folder.upload.donthu");
	}
	
	public static String getFolderNameDonThuVanBanXLGQ(){
		return File.separator+KNTCProps.getProperty("file.folder.upload.donthu.vanban");
	}
	
	public static String getPathHDSD(){
		return File.separator+KNTCProps.getProperty("file.folder.hdsd");
	}
	
	public List<String> getTypeAccessedList() throws SQLException{
		List<String> result=new ArrayList<String>();
		Connection cn = DatabaseServices.getConnection();
		String sql="SELECT TYPE_FILE_ FROM `loai_van_ban_duoc_dinh_kem` WHERE STATUS_='1' ORDER BY ORDER_ ASC";
		PreparedStatement st=cn.prepareStatement(sql);
		ResultSet rs=st.executeQuery();
		while(rs.next()){
			result.add(rs.getString("TYPE_FILE_"));
		}
		st.close();
		rs.close();
		cn.close();

		return result;
	}
}
