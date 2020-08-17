package ngn.kntc.lienthongttcp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ngn.kntc.beans.LienThongIdDonThuAndIdQuyetDinhAndOrgTaoTHGQ;
import ngn.kntc.beans.LienThongIdDonThuAndOrgNhanBean;
import ngn.kntc.beans.LienThongIdDonThuAndOrgTaoKQXLBean;
import ngn.kntc.beans.LienThongIdDonThuAndOrgTaoQDGQBean;
import ngn.kntc.beans.LienThongIdDonThuAndOrgThuLyQDTLBean;
import ngn.kntc.beans.LienThongMaVuViecAndMaQuyetDinhAndMaThiHanhBean;
import ngn.kntc.beans.LienThongMaVuViecAndMaQuyetDinhBean;
import ngn.kntc.databases.DatabaseServices;

public class LienThongLocalServicesUtil{
	SimpleDateFormat sdfSQL = new SimpleDateFormat("yyyy-MM-dd");
	
	public List<String> getDonThuLienThongByDate(Date date) throws SQLException
	{
		List<String> list = new ArrayList<String>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT mattcp FROM don_thu_thong_tin_don_thu WHERE mattcp IS NOT NULL AND DATE(NgayLienThong) = '"+sdfSQL.format(date)+"'";
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			list.add(rs.getString("mattcp"));
		}
		rs.close();
		con.close();
		
		return list;
	}
	
	public List<String> getKQXLLienThongByDate(Date date) throws SQLException
	{
		List<String> list = new ArrayList<String>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT mattcp FROM don_thu_ket_qua_xu_ly kqxl,don_thu_thong_tin_don_thu ttd WHERE DaLienThong = 1 AND kqxl.IdDonThu = ttd.IdDonThu AND OrgNhan = OrgTao AND DATE(kqxl.NgayLienThong) = '"+sdfSQL.format(date)+"'";
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			list.add(rs.getString("mattcp"));
		}
		rs.close();
		con.close();
		
		return list;
	}
	
	public List<String> getQDTLLienThongByDate(Date date) throws SQLException
	{
		List<String> list = new ArrayList<String>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT mattcp FROM don_thu_quyet_dinh_thu_ly qdtl,don_thu_thong_tin_don_thu ttd WHERE qdtl.IdDonThu = ttd.IdDonThu AND MaCoQuanThuLy = OrgNhan AND DaLienThong = 1 AND DATE(qdtl.NgayLienThong) = '"+sdfSQL.format(date)+"'";
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			list.add(rs.getString("mattcp"));
		}
		rs.close();
		con.close();
		
		return list;
	}
	
	public List<LienThongMaVuViecAndMaQuyetDinhAndMaThiHanhBean> getTHGQLienThongByDate(Date date) throws SQLException
	{
		List<LienThongMaVuViecAndMaQuyetDinhAndMaThiHanhBean> list = new ArrayList<LienThongMaVuViecAndMaQuyetDinhAndMaThiHanhBean>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT qdgq.mattcp as maqd,ttd.mattcp as mavv,thgq.mattcp as math FROM don_thu_thi_hanh_giai_quyet thgq,don_thu_quyet_dinh_giai_quyet qdgq, don_thu_thong_tin_don_thu ttd WHERE thgq.mattcp IS NOT NULL AND qdgq.IdDonThu = ttd.IdDonThu AND thgq.IdQuyetDinhGiaiQuyet = qdgq.IdQuyetDinhGiaiQuyet AND ttd.OrgNhan = thgq.OrgTao AND DATE(thgq.NgayLienThong) = '"+sdfSQL.format(date)+"'";
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			LienThongMaVuViecAndMaQuyetDinhAndMaThiHanhBean model = new LienThongMaVuViecAndMaQuyetDinhAndMaThiHanhBean();
			model.setIdQuyetDinh(rs.getString("maqd"));
			model.setIdVuViec(rs.getString("mavv"));
			model.setIdThiHanh(rs.getString("math"));
			list.add(model);
		}
		rs.close();
		con.close();
		
		return list;
	}
	
	public List<LienThongMaVuViecAndMaQuyetDinhBean> getQDGQLienThongByDate(Date date) throws SQLException
	{
		List<LienThongMaVuViecAndMaQuyetDinhBean> list = new ArrayList<LienThongMaVuViecAndMaQuyetDinhBean>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT qdgq.mattcp as maqd,ttd.mattcp as mavv FROM don_thu_quyet_dinh_giai_quyet qdgq, don_thu_thong_tin_don_thu ttd WHERE qdgq.IdDonThu = ttd.IdDonThu AND OrgNhan = OrgTao AND qdgq.mattcp IS NOT NULL AND DATE(qdgq.NgayLienThong) = '"+sdfSQL.format(date)+"'";
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			LienThongMaVuViecAndMaQuyetDinhBean model = new LienThongMaVuViecAndMaQuyetDinhBean();
			model.setIdQuyetDinh(rs.getString("maqd"));
			model.setIdVuViec(rs.getString("mavv"));
			list.add(model);
		}
		rs.close();
		con.close();
		
		return list;
	}
	
	public String getIdLienThongOfDonThuOnThongTinDon(int idDonThu,long orgId) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT mattcp FROM don_thu_thong_tin_don_thu WHERE IdDonThu = "+idDonThu+" AND OrgNhan = "+orgId;
		ResultSet rs = con.createStatement().executeQuery(sql);
		rs.next();
		String mattcp = rs.getString("mattcp");
		rs.close();
		con.close();
		return mattcp;
	}
	
	public List<LienThongIdDonThuAndOrgNhanBean> getAllIdDonThuInThongTinDonNotLienThong() throws SQLException
	{
		List<LienThongIdDonThuAndOrgNhanBean> listIdDonThu  = new  ArrayList<LienThongIdDonThuAndOrgNhanBean>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT IdDonThu,OrgNhan FROM don_thu_thong_tin_don_thu WHERE mattcp IS NULL ORDER BY IdDonThu ASC";
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			LienThongIdDonThuAndOrgNhanBean model = new LienThongIdDonThuAndOrgNhanBean();
			model.setIdDonThu(rs.getInt("IdDonThu"));
			model.setOrgTao(rs.getInt("OrgNhan"));
			listIdDonThu.add(model);
		}
		rs.close();
		con.close();
		
		return listIdDonThu;
	}
	
	public List<LienThongIdDonThuAndOrgTaoKQXLBean> getAllIdKQXLNotLienThong() throws SQLException
	{
		List<LienThongIdDonThuAndOrgTaoKQXLBean> listIdKQXL  = new  ArrayList<LienThongIdDonThuAndOrgTaoKQXLBean>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT IdDonThu,OrgTao FROM don_thu_ket_qua_xu_ly WHERE DaLienThong = 0";
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			LienThongIdDonThuAndOrgTaoKQXLBean model = new LienThongIdDonThuAndOrgTaoKQXLBean();
			model.setIdKQXL(rs.getInt("IdDonThu"));
			model.setOrgTao(rs.getInt("OrgTao"));
			listIdKQXL.add(model);
		}
		rs.close();
		con.close();
		
		return listIdKQXL;
	}
	
	public List<LienThongIdDonThuAndOrgThuLyQDTLBean> getAllIdQDTLNotLienThong() throws SQLException
	{
		List<LienThongIdDonThuAndOrgThuLyQDTLBean> listIdKQXL  = new  ArrayList<LienThongIdDonThuAndOrgThuLyQDTLBean>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT IdDonThu,MaCoQuanThuLy FROM don_thu_quyet_dinh_thu_ly WHERE DaLienThong = 0";
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			LienThongIdDonThuAndOrgThuLyQDTLBean model = new LienThongIdDonThuAndOrgThuLyQDTLBean();
			model.setIdDonThu(rs.getInt("IdDonThu"));
			model.setOrgThuLy(rs.getInt("MaCoQuanThuLy"));
			listIdKQXL.add(model);
		}
		rs.close();
		con.close();
		
		return listIdKQXL;
	}
	
	public List<LienThongIdDonThuAndOrgTaoQDGQBean> getAllIdQDGQNotLienThong() throws SQLException
	{
		List<LienThongIdDonThuAndOrgTaoQDGQBean> listIdKQXL  = new  ArrayList<LienThongIdDonThuAndOrgTaoQDGQBean>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT IdDonThu,OrgTao FROM don_thu_quyet_dinh_giai_quyet WHERE mattcp IS NULL";
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			LienThongIdDonThuAndOrgTaoQDGQBean model = new LienThongIdDonThuAndOrgTaoQDGQBean();
			model.setIdDonThu(rs.getInt("IdDonThu"));
			model.setOrgTao(rs.getInt("OrgTao"));
			listIdKQXL.add(model);
		}
		rs.close();
		con.close();
		
		return listIdKQXL;
	}
	
	public List<LienThongIdDonThuAndIdQuyetDinhAndOrgTaoTHGQ> getAllIdTHGQNotLienThong() throws SQLException
	{
		List<LienThongIdDonThuAndIdQuyetDinhAndOrgTaoTHGQ> listIdTHGQ  = new  ArrayList<LienThongIdDonThuAndIdQuyetDinhAndOrgTaoTHGQ>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT IdDonThu,thgq.IDQuyetDinhGiaiQuyet,thgq.OrgTao FROM don_thu_thi_hanh_giai_quyet thgq,don_thu_quyet_dinh_giai_quyet qdgq WHERE thgq.IDQuyetDinhGiaiQuyet = qdgq.IDQuyetDinhGiaiQuyet AND thgq.mattcp IS NULL";
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			LienThongIdDonThuAndIdQuyetDinhAndOrgTaoTHGQ model = new LienThongIdDonThuAndIdQuyetDinhAndOrgTaoTHGQ();
			model.setIdDonThu(rs.getInt("IdDonThu"));
			model.setIdQuyetDinh(rs.getInt("IDQuyetDinhGiaiQuyet"));
			model.setOrgTao(rs.getInt("OrgTao"));
			listIdTHGQ.add(model);
		}
		rs.close();
		con.close();
		
		return listIdTHGQ;
	}
}
