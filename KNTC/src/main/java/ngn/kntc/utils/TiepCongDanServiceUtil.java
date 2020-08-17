package ngn.kntc.utils;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;

import ngn.kntc.beans.DoiTuongDiKNTCBean;
import ngn.kntc.beans.SoTiepCongDanBean;
import ngn.kntc.beans.TraCuuTiepCongDanBean;
import ngn.kntc.databases.DatabaseServices;
import ngn.kntc.modules.DonThuModule;

public class TiepCongDanServiceUtil implements Serializable{
	SimpleDateFormat sdfDateSql = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfDatetimeSql = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public int insertSoTiepCongDan(SoTiepCongDanBean model) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "INSERT INTO so_tiep_cong_dan(IdDonThu, IdDoiTuongBiKNTC, IdDoiTuongUyQuyen, MaLanhDaoTiep, UyQuyen, MaLanhDaoUyQuyen, TiepDinhKy, LoaiNguoiDiKNTC, SoNguoiDiKNTC, SoNguoiDaiDien, TenCoQuanDiKNTC, DiaChiCoQuanDiKNTC, LoaiNguoiBiKNTC, LoaiNguoiUyQuyen, NoiDungTiepCongDan, KetQuaTiepCongDan, NgayTiepCongDan, MaCoQuanDaGiaiQuyet, SoKyHieuVanBanGiaiQuyet, LanGiaiQuyet, NgayBanHanhQDGQ, LoaiQuyetDinhGiaiQuyet, TomTatNoiDungGiaiQuyet, LoaiLinhVuc,GanVuViec,UserTCD) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preSt = con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
		if(model.getMaDonThu()!=-1)
			preSt.setInt(1, model.getMaDonThu());
		else
			preSt.setNull(1, Types.INTEGER);
		if(model.getIdNguoiBiKNTC()!=0)
			preSt.setInt(2, model.getIdNguoiBiKNTC());
		else
			preSt.setNull(2, Types.INTEGER);
		if(model.getIdNguoiUyQuyen()!=0)
			preSt.setInt(3, model.getIdNguoiUyQuyen());
		else
			preSt.setNull(3, Types.INTEGER);
		if(model.getMaLanhDaoTiep()!=0)
			preSt.setLong(4, model.getMaLanhDaoTiep());
		else
			preSt.setNull(4, Types.INTEGER);
		preSt.setInt(5, model.isUyQuyenLanhDao() ? 1 : 0);
		if(model.getMaLanhDaoUyQuyen()!=0)
			preSt.setLong(6, model.getMaLanhDaoUyQuyen());
		else
			preSt.setNull(6, Types.INTEGER);
		preSt.setInt(7, model.isTiepDinhKy() ? 1 : 0);
		preSt.setInt(8, model.getLoaiNguoiDiKNTC());
		preSt.setInt(9, model.getSoNguoiDiKNTC());
		preSt.setInt(10, model.getSoNguoiDaiDien());
		preSt.setNString(11,model.getTenCoQuanDiKNTC());
		preSt.setNString(12,model.getDiaChiCoQuanDiKNTC());
		if(model.getLoaiNguoiBiKNTC()!=0)
			preSt.setLong(13, model.getLoaiNguoiBiKNTC());
		else
			preSt.setNull(13, Types.INTEGER);
		if(model.getLoaiNguoiUyQuyen()!=0)
			preSt.setLong(14, model.getLoaiNguoiUyQuyen());
		else
			preSt.setNull(14, Types.INTEGER);
		preSt.setNString(15,model.getNoiDungTiepCongDan());
		preSt.setNString(16,model.getKetQuaTiepCongDan());
		String ngayTiep = null;
		if(model.getNgayTiepCongDan()!=null)
			ngayTiep = sdfDateSql.format(model.getNgayTiepCongDan());
		preSt.setString(17, ngayTiep);
		preSt.setNString(18, model.getMaCoQuanDaGiaiQuyet());
		preSt.setNString(19, model.getSoKyHieuVanBanDen());
		preSt.setInt(20, model.getLanGiaiQuyet());
		String ngayBanHanh = null;
		if(model.getNgayBanHanhQDGQ()!=null)
			ngayBanHanh = sdfDatetimeSql.format(model.getNgayBanHanhQDGQ());
		else
			ngayBanHanh = null;
		preSt.setString(21,ngayBanHanh);
		if(model.getLoaiQuyetDinhGiaiQuyet()!=0)
			preSt.setInt(22, model.getLoaiQuyetDinhGiaiQuyet());
		else
			preSt.setNull(22, Types.INTEGER);
		preSt.setNString(23, model.getTomTatNoiDungGiaiQuyet());
		preSt.setInt(24, model.getLoaiLinhVuc());
		preSt.setString(25, model.getGanVuViec());
		preSt.setLong(26, model.getUserTCD());
		preSt.executeUpdate();
		ResultSet rs = preSt.getGeneratedKeys();
		int insert_id = -1;
		while(rs.next())
		{
			insert_id = rs.getInt(1);
		}
		preSt.close();
		rs.close();
		con.close();
		return insert_id;
	}
	
	public void updateSoTiepCongDan(SoTiepCongDanBean model) throws SQLException
	{
		String ngay="";
		Connection con = DatabaseServices.getConnection();
		String sql = "UPDATE so_tiep_cong_dan SET MaLanhDaoTiep=?,UyQuyen=?,MaLanhDaoUyQuyen=?,TiepDinhKy=?,SoNguoiDiKNTC=?,SoNguoiDaiDien=?,TenCoQuanDiKNTC=?,DiaChiCoQuanDiKNTC=?,KetQuaTiepCongDan=?,NgayTiepCongDan=?,NoiDungTiepCongDan=?,MaCoQuanDaGiaiQuyet=?,LanGiaiQuyet=?,NgayBanHanhQDGQ=?,LoaiQuyetDinhGiaiQuyet=?,TomTatNoiDungGiaiQuyet=?,SoKyHieuVanBanGiaiQuyet=? WHERE IdSoTiepCongDan=?";
		PreparedStatement preSt = con.prepareStatement(sql);
		if(model.getMaLanhDaoTiep()!=0)
			preSt.setLong(1, model.getMaLanhDaoTiep());
		else
			preSt.setNull(1, Types.INTEGER);
		preSt.setInt(2, model.isUyQuyenLanhDao() ? 1 : 0);
		if(model.getMaLanhDaoUyQuyen()!=0)
			preSt.setLong(3, model.getMaLanhDaoUyQuyen());
		else
			preSt.setNull(3, Types.INTEGER);
		preSt.setInt(4, model.isTiepDinhKy() ? 1 : 0);
		preSt.setInt(5, model.getSoNguoiDiKNTC());
		preSt.setInt(6, model.getSoNguoiDaiDien());
		preSt.setNString(7,model.getTenCoQuanDiKNTC());
		preSt.setNString(8,model.getDiaChiCoQuanDiKNTC());
		preSt.setNString(9,model.getKetQuaTiepCongDan());
		String ngayTiep = null;
		if(model.getNgayTiepCongDan()!=null)
			ngayTiep = sdfDatetimeSql.format(model.getNgayTiepCongDan());
		preSt.setString(10, ngayTiep);
		preSt.setNString(11, model.getNoiDungTiepCongDan());
		preSt.setNString(12, model.getMaCoQuanDaGiaiQuyet());
		preSt.setInt(13, model.getLanGiaiQuyet());
		if(model.getNgayBanHanhQDGQ()!=null)
			ngay = sdfDatetimeSql.format(model.getNgayBanHanhQDGQ());
		else
			ngay = null;
		preSt.setString(14,ngay);
		if(model.getLoaiQuyetDinhGiaiQuyet()!=0)
			preSt.setInt(15, model.getLoaiQuyetDinhGiaiQuyet());
		else
			preSt.setNull(15, Types.INTEGER);
		preSt.setNString(16, model.getTomTatNoiDungGiaiQuyet());
		preSt.setNString(17, model.getSoKyHieuVanBanDen());
		preSt.setInt(18, model.getMaSoTiepCongDan());
		preSt.executeUpdate();
		
		preSt.close();
		con.close();
	}
	
	public void insertNguoiDaiDienTiepCongDan(int idSoTiepCongDan, int idNguoiDaiDien) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		Statement st = con.createStatement();
		String sql = "INSERT INTO so_tiep_cong_dan_nguoi_dai_dien values("+idSoTiepCongDan+","+idNguoiDaiDien+")";
		st.executeUpdate(sql);
		st.close();
		con.close();
	}
	
	public void insertLinhVucTCD(int idTCD,String maLinhVuc) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "INSERT INTO so_tiep_cong_dan_linh_vuc VALUES(?,?)";
		PreparedStatement preSt = con.prepareStatement(sql);
		preSt.setInt(1, idTCD);
		preSt.setString(2, maLinhVuc);
		preSt.executeUpdate();
		preSt.close();
		con.close();
	}
	
	public SoTiepCongDanBean getSoTiepCongDan(int id) throws Exception
	{
		SoTiepCongDanBean model = new SoTiepCongDanBean();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM so_tiep_cong_dan WHERE IdSoTiepCongDan = "+id;
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			model = returnSoTiepCongDanBean(rs);
		}
		rs.close();
		con.close();
		
		return model;
	}
	
	public SoTiepCongDanBean getSoTiepCongDanOnDonThu(int idDonThu) throws Exception
	{
		SoTiepCongDanBean model = null;
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM so_tiep_cong_dan WHERE IdDonThu = "+idDonThu;
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			model = returnSoTiepCongDanBean(rs);
		}
		rs.close();
		con.close();
		
		return model;
	}
	
	public List<SoTiepCongDanBean> getSoTiepCongDanList(TraCuuTiepCongDanBean modelTraCuu) throws Exception
	{
		String strListUser = "";
		for(long l: UserLocalServiceUtil.getOrganizationUserIds(SessionUtil.getOrgId()))
		{
			strListUser += l+","; 
		}
		strListUser = strListUser.substring(0,strListUser.length()-1);
		List<SoTiepCongDanBean> list = new ArrayList<SoTiepCongDanBean>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM so_tiep_cong_dan WHERE UserTCD IN ("+strListUser+")";
		if(modelTraCuu.getKeyWord()!=null)
		{
			sql+=" AND(";
			sql+="(IdDonThu IN (SELECT IdDonThu FROM don_thu WHERE NoiDungDonThu LIKE ?))";
			sql+=" OR NoiDungTiepCongDan LIKE ? OR KetQuaTiepCongDan LIKE ?";
			sql+=" OR IdSoTiepCongDan IN ( SELECT IdSoTiepCongDan FROM so_tiep_cong_dan_nguoi_dai_dien WHERE IdDoiTuongDiKNTC IN (SELECT IdDoiTuongDiKNTC FROM don_thu_doi_tuong_di_kntc WHERE HoTen LIKE ?))";
			sql+=" OR IdDoiTuongBiKNTC IN ( SELECT IdDoiTuongBiKNTC FROM don_thu_doi_tuong_bi_kntc WHERE HoTen LIKE ? OR TenCoQuan LIKE ?)";
			sql+=")";
		}
		if(modelTraCuu.getStartDate()!=null)
		{
			sql+=" AND (NgayTiepCongDan BETWEEN '"+modelTraCuu.getStartDate()+"' AND '"+modelTraCuu.getEndDate()+"' )";
		}
		if(modelTraCuu.getMaHuongXuLy()!=0)
		{
			sql+=" AND IdDonThu IN (SELECT IdDonThu FROM don_thu_ket_qua_xu_ly WHERE MaHuongXuLy = "+modelTraCuu.getMaHuongXuLy()+")";
		}
		sql+=" ORDER BY NgayTiepCongDan DESC";
		
		System.out.println(sql);
		PreparedStatement preSt = con.prepareStatement(sql);
		if(modelTraCuu.getKeyWord()!=null)
		{
			String keyWord = "%"+modelTraCuu.getKeyWord()+"%";
			
			preSt.setNString(1, keyWord);
			preSt.setNString(2, keyWord);
			preSt.setNString(3, keyWord);
			preSt.setNString(4, keyWord);
			preSt.setNString(5, keyWord);
			preSt.setNString(6, keyWord);
		}
		ResultSet rs = preSt.executeQuery();
		while(rs.next())
		{
			SoTiepCongDanBean model = returnSoTiepCongDanBean(rs);
			list.add(model);
		}
		rs.close();
		con.close();
		
		return list;
	}
	
	public List<SoTiepCongDanBean> getSoTiepCongDanExportList(Date startDate,Date endDate,long userId) throws Exception
	{
		List<SoTiepCongDanBean> list = new ArrayList<SoTiepCongDanBean>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM so_tiep_cong_dan WHERE 1 = 1";
		if(userId!=0)
		{
			sql+= " AND UserTCD = "+userId;
		}
		else
		{
		    long[] listUser = UserLocalServiceUtil.getOrganizationUserIds(SessionUtil.getOrgId());
		    String user = "";
		    for(int i = 0;i<listUser.length;i++)
		    {
		    	user+=listUser[i];
		    	if(i<listUser.length-1)
		    		user+=",";
		    }
		    
		    sql+=" AND UserTCD IN ("+user+")";
		}
		if(startDate!=null)
		{
			sql+=" AND (NgayTiepCongDan BETWEEN '"+sdfDateSql.format(startDate)+"' AND '"+sdfDateSql.format(endDate)+"' )";
		}
		sql+=" ORDER BY NgayTiepCongDan DESC";
		System.out.println(sql);
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			SoTiepCongDanBean model = returnSoTiepCongDanBean(rs);
			list.add(model);
		}
		rs.close();
		con.close();
		
		return list;
	}
	
	public List<String> getLinhVucList(int idSoTCD) throws Exception
	{
		List<String> list = new ArrayList<String>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT ma FROM linh_vuc WHERE ma IN (SELECT IdLinhVuc FROM so_tiep_cong_dan_linh_vuc WHERE IdSoTiepCongDan = "+idSoTCD+")";
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			list.add(rs.getString("ma"));
		}
		rs.close();
		con.close();

		return list;
	}
	
	public List<SoTiepCongDanBean> searchThongTinTiepCongDan(String[] arrTenChuThe) throws Exception
	{
		List<SoTiepCongDanBean> list = new ArrayList<SoTiepCongDanBean>();
		Connection con = DatabaseServices.getConnection();
		
		String sql = "SELECT * FROM so_tiep_cong_dan WHERE IdDonThu IS NULL AND GanVuViec IS NULL";
		if(arrTenChuThe!=null && arrTenChuThe.length>0)
		{
			sql+=" AND IdSoTiepCongDan IN (SELECT DISTINCT IdSoTiepCongDan FROM so_tiep_cong_dan_nguoi_dai_dien WHERE IdDoiTuongDiKNTC IN(SELECT IdDoiTuongDiKNTC FROM don_thu_doi_tuong_di_kntc WHERE 1 = 1 AND (";
			for(int i = 0;i<arrTenChuThe.length;i++)
			{
				sql+=" HoTen LIKE ? ";
				if(i<arrTenChuThe.length-1)
					sql+=" OR ";
			}
			sql+=")))";
		}
		else
		{
			sql+=" AND 1 = 0";
		}
		sql+=" ORDER BY NgayTiepCongDan DESC";
		PreparedStatement preSt = con.prepareStatement(sql);
		if(arrTenChuThe!=null)
		{
			for(int i = 0;i<arrTenChuThe.length;i++)
			{
				preSt.setNString(i+1, "%"+arrTenChuThe[i]+"%");
			}
		}
		System.out.println(sql);
		ResultSet rs = preSt.executeQuery();
		while(rs.next())
		{
			SoTiepCongDanBean model = returnSoTiepCongDanBean(rs);
			list.add(model);
		}
		rs.close();
		con.close();
		
		return list;
	}
	
	public static int countTiepCongDan() throws Exception
	{
		String strListUser = "";
		for(long l: UserLocalServiceUtil.getOrganizationUserIds(SessionUtil.getOrgId()))
		{
			strListUser += l+","; 
		}
		strListUser = strListUser.substring(0,strListUser.length()-1);
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT count(IdSoTiepCongDan) FROM so_tiep_cong_dan WHERE  UserTCD IN ("+strListUser+")";
		ResultSet rs = con.createStatement().executeQuery(sql);
		rs.next();
		int count = rs.getInt(1);
		rs.close();
		con.close();
		
		return count;
	}
	
	public static int countAllTiepCongDan() throws Exception
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT count(IdSoTiepCongDan) FROM so_tiep_cong_dan";
		ResultSet rs = con.createStatement().executeQuery(sql);
		rs.next();
		int count = rs.getInt(1);
		rs.close();
		con.close();
		
		return count;
	}
	
	public int countSoLanTrungVuViecKhongDon(int idTCD) throws Exception
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT count(*) FROM so_tiep_cong_dan WHERE GanVuViec = "+idTCD;
		System.out.println(sql);
		ResultSet rs = con.createStatement().executeQuery(sql);
		rs.next();
		int count = rs.getInt(1);
		rs.close();
		con.close();

		return count;
	}
	
	public static List<DoiTuongDiKNTCBean> getNguoiDaiDienTiepCongDan(int idTCD) throws Exception
	{
		List<DoiTuongDiKNTCBean> list = new ArrayList<DoiTuongDiKNTCBean>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu_doi_tuong_di_kntc WHERE IdDoiTuongDiKNTC IN (SELECT IdDoiTuongDiKNTC FROM so_tiep_cong_dan_nguoi_dai_dien WHERE IdSoTiepCongDan = "+idTCD+")";
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			DoiTuongDiKNTCBean model = DonThuServiceUtil.returnDoiTuongDiKNTCBean(rs);
			list.add(model);
		}
		rs.close();
		con.close();
		
		return list;
	}
	
	public void deleteNguoiDaiDienTCD(int idTCD) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "DELETE FROM so_tiep_cong_dan_nguoi_dai_dien WHERE IdSoTiepCongDan = "+idTCD;
		con.createStatement().executeUpdate(sql);
		con.close();
	}
	
	public void deleteLinhVucTCD(int idTCD) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "DELETE FROM so_tiep_cong_dan_linh_vuc WHERE IdSoTiepCongDan = "+idTCD;
		con.createStatement().executeUpdate(sql);
		con.close();
	}
	
	public SoTiepCongDanBean returnSoTiepCongDanBean(ResultSet rs) throws Exception
	{
		SoTiepCongDanBean model = new SoTiepCongDanBean();
	
		model.setMaSoTiepCongDan(rs.getInt("IdSoTiepCongDan"));
		model.setMaDonThu(rs.getInt("IdDonThu"));
		model.setIdNguoiBiKNTC(rs.getInt("IdDoiTuongBiKNTC"));
		model.setIdNguoiUyQuyen(rs.getInt("IdDoiTuongUyQuyen"));
		model.setMaLanhDaoTiep(rs.getInt("MaLanhDaoTiep"));
		model.setUyQuyenLanhDao(rs.getInt("UyQuyen")==1?true:false);
		model.setMaLanhDaoUyQuyen(rs.getInt("MaLanhDaoUyQuyen"));
		model.setTiepDinhKy(rs.getInt("TiepDinhKy")==1?true:false);
		model.setLoaiNguoiDiKNTC(rs.getInt("LoaiNguoiDiKNTC"));
		model.setSoNguoiDiKNTC(rs.getInt("SoNguoiDiKNTC"));
		model.setSoNguoiDaiDien(rs.getInt("SoNguoiDaiDien"));
		model.setTenCoQuanDiKNTC(rs.getString("TenCoQuanDiKNTC"));
		model.setDiaChiCoQuanDiKNTC(rs.getString("DiaChiCoQuanDiKNTC"));
		model.setLoaiNguoiBiKNTC(rs.getInt("LoaiNguoiBiKNTC"));
		model.setLoaiNguoiUyQuyen(rs.getInt("LoaiNguoiUyQuyen"));
		model.setKetQuaTiepCongDan(rs.getString("KetQuaTiepCongDan"));
		model.setNoiDungTiepCongDan(rs.getString("NoiDungTiepCongDan"));
		model.setNgayTiepCongDan(new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("NgayTiepCongDan")));
		model.setMaCoQuanDaGiaiQuyet(rs.getString("MaCoQuanDaGiaiQuyet"));
		model.setSoKyHieuVanBanDen(rs.getString("SoKyHieuVanBanGiaiQuyet"));
		model.setLanGiaiQuyet(rs.getInt("LanGiaiQuyet"));
		Date ngayBanHanhQĐGQ = rs.getString("NgayBanHanhQDGQ")!=null?new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("NgayBanHanhQDGQ")):null;
		model.setNgayBanHanhQDGQ(ngayBanHanhQĐGQ);
		model.setLoaiQuyetDinhGiaiQuyet(rs.getInt("LoaiQuyetDinhGiaiQuyet"));
		model.setTomTatNoiDungGiaiQuyet(rs.getString("TomTatNoiDungGiaiQuyet"));
		model.setLoaiLinhVuc(rs.getInt("LoaiLinhVuc"));
		model.setGanVuViec(rs.getString("GanVuViec"));
		model.setUserTCD(rs.getLong("UserTCD"));
		
		return model;
	}
	
	public String returnTenNguoiDaiDienTCD(SoTiepCongDanBean model) throws Exception
	{
		String nguoiDaiDien = "";
		
		if(model.getLoaiNguoiDiKNTC()==1 || model.getLoaiNguoiDiKNTC() == 2)
		{
			List<DoiTuongDiKNTCBean> listNguoiDiKNTC = getNguoiDaiDienTiepCongDan(model.getMaSoTiepCongDan());
			for(DoiTuongDiKNTCBean modelNguoiDiKNTC : listNguoiDiKNTC)
			{
				nguoiDaiDien+="<p style='margin: 5px'>- <b>"+modelNguoiDiKNTC.getHoTen()+"</b></p>";
				nguoiDaiDien+="<p style='margin: 5px'>"+DonThuModule.returnDiaChiChiTiet(modelNguoiDiKNTC.getDiaChiChiTiet(), modelNguoiDiKNTC.getMaTinh(), modelNguoiDiKNTC.getMaHuyen(), modelNguoiDiKNTC.getMaXa())+"</p>";
				nguoiDaiDien+="<hr>";
			}
		}
		else
		{
			nguoiDaiDien = model.getTenCoQuanDiKNTC();
		}
		return nguoiDaiDien;
	}
	
	public String returnTenNguoiDaiDienSoTCDPDF(SoTiepCongDanBean model) throws Exception
	{
		String nguoiDaiDien = "";
		
		List<DoiTuongDiKNTCBean> listNguoiDiKNTC = getNguoiDaiDienTiepCongDan(model.getMaSoTiepCongDan());
		for(DoiTuongDiKNTCBean modelNguoiDiKNTC : listNguoiDiKNTC)
		{
			nguoiDaiDien+=modelNguoiDiKNTC.getHoTen()+" - "+DonThuModule.returnDiaChiChiTiet(modelNguoiDiKNTC.getDiaChiChiTiet(), modelNguoiDiKNTC.getMaTinh(), modelNguoiDiKNTC.getMaHuyen(), modelNguoiDiKNTC.getMaXa())+" - "+modelNguoiDiKNTC.getSoDinhDanhCaNhan()+"\n";
		}
		return nguoiDaiDien;
	}
}
