package ngn.kntc.utils;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ngn.kntc.beans.VanBanQuyPhamBean;
import ngn.kntc.databases.DatabaseServices;

public class VanBanQuyPhamServiceUtil implements Serializable{
	SimpleDateFormat sdfDateSql = new SimpleDateFormat("yyyy-MM-dd");

	/* Insert */
	public int insertVanBanQuyPham(VanBanQuyPhamBean model) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "INSERT INTO van_ban_quy_pham(TenVanBan, SoHieu, TrichDan, CoQuanBanHanh, NguoiKy, NgayBanHanh, NgayTao, LoaiVanBan, TenFileDinhKem, LinkFileDinhKem, Owner) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preSt = con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
		preSt.setNString(1,model.getTenVanBan());
		preSt.setNString(2,model.getSoHieu());
		preSt.setNString(3,model.getTrichDan());
		preSt.setNString(4,model.getCoQuanBanHanh());
		preSt.setNString(5,model.getNguoiKy());
		preSt.setString(6,sdfDateSql.format(model.getNgayBanHanh()));
		preSt.setString(7,sdfDateSql.format(new Date()));
		preSt.setString(8,model.getLoaiVanBan());
		preSt.setString(9,model.getTenFileDinhKem());
		preSt.setString(10,model.getLinkFileDinhKem());
		preSt.setInt(11,model.getOwner());
		preSt.executeUpdate();
		ResultSet rs = preSt.getGeneratedKeys();
		rs.next();
		int key = rs.getInt(1);

		rs.close();
		preSt.close();
		con.close();

		return key;
	}

	/* Update */
	public void updateVanBanQuyPham(VanBanQuyPhamBean model) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "UPDATE van_ban_quy_pham SET TenVanBan=?,SoHieu=?,TrichDan=?,CoQuanBanHanh=?,NguoiKy=?,NgayBanHanh=?,LoaiVanBan=?,TenFileDinhKem=?,LinkFileDinhKem=?,Owner=? WHERE IdVanBanQuyPham=?";
		PreparedStatement preSt = con.prepareStatement(sql);
		preSt.setNString(1,model.getTenVanBan());
		preSt.setNString(2,model.getSoHieu());
		preSt.setNString(3,model.getTrichDan());
		preSt.setNString(4,model.getCoQuanBanHanh());
		preSt.setNString(5,model.getNguoiKy());
		preSt.setString(6,sdfDateSql.format(model.getNgayBanHanh()));
		preSt.setString(7,model.getLoaiVanBan());
		preSt.setString(8,model.getTenFileDinhKem());
		preSt.setString(9,model.getLinkFileDinhKem());
		preSt.setInt(10,model.getOwner());
		preSt.setInt(11,model.getId());
		preSt.executeUpdate();
		preSt.close();
		con.close();
	}

	public List<VanBanQuyPhamBean> getVanBanQuyPhamList(String keyWord,int typeVanBan) throws SQLException
	{
		List<VanBanQuyPhamBean> list = new ArrayList<VanBanQuyPhamBean>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM van_ban_quy_pham WHERE Owner = "+SessionUtil.getUserId();
		if(keyWord!="")
		{
			sql+= " AND (TenVanBan LIKE ? OR SoHieu LIKE ? OR TrichDan LIKE ? OR CoQuanBanHanh LIKE ? OR NguoiKy LIKE ?)";
		}
		if(typeVanBan!=0)
		{
			sql+=" AND (LoaiVanBan LIKE "+typeVanBan+")";
		}
		PreparedStatement preSt = con.prepareStatement(sql);
		if(keyWord!="")
		{
			preSt.setNString(1, "%"+keyWord+"%");
			preSt.setNString(2, "%"+keyWord+"%");
			preSt.setNString(3, "%"+keyWord+"%");
			preSt.setNString(4, "%"+keyWord+"%");
			preSt.setNString(5, "%"+keyWord+"%");
		}
		ResultSet rs = preSt.executeQuery();

		while(rs.next())
		{
			list.add(returnVanBan(rs));
		}

		rs.close();
		preSt.close();
		con.close();

		return list;
	}
	
	public VanBanQuyPhamBean getVanBanQuyPham(int id) throws SQLException
	{
		VanBanQuyPhamBean model = new VanBanQuyPhamBean();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM van_ban_quy_pham WHERE IdVanBanQuyPham = "+id;
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			model = returnVanBan(rs);
		}
		rs.close();
		con.close();

		return model;
	}
	
	public void deleteVanBanQuyPham(int id) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "DELETE FROM van_ban_quy_pham WHERE IdVanBanQuyPham = "+id;
		con.createStatement().executeUpdate(sql);
		con.close();
	}

	public VanBanQuyPhamBean returnVanBan(ResultSet rs) throws SQLException
	{
		VanBanQuyPhamBean model = new VanBanQuyPhamBean();
		model.setId(rs.getInt("IdVanBanQuyPham"));
		model.setTenVanBan(rs.getString("TenVanBan"));
		model.setSoHieu(rs.getString("SoHieu"));
		model.setTrichDan(rs.getString("TrichDan"));
		model.setCoQuanBanHanh(rs.getString("CoQuanBanHanh"));
		model.setNguoiKy(rs.getString("NguoiKy"));
		model.setNgayBanHanh(rs.getDate("NgayBanHanh"));
		model.setNgayTao(rs.getDate("NgayTao"));
		model.setLoaiVanBan(rs.getString("LoaiVanBan"));
		model.setTenFileDinhKem(rs.getString("TenFileDinhKem"));
		model.setLinkFileDinhKem(rs.getString("LinkFileDinhKem"));
		model.setOwner(rs.getInt("Owner"));

		return model;
	}
}
