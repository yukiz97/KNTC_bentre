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

import ngn.kntc.beans.DoiTuongBiXuLyHanhChinhBean;
import ngn.kntc.beans.DonThuThongBaoBean;
import ngn.kntc.beans.KetQuaXuLyBean;
import ngn.kntc.beans.LuanChuyenBean;
import ngn.kntc.beans.QuaTrinhXuLyGiaiQuyetBean;
import ngn.kntc.beans.QuyetDinhGiaiQuyetBean;
import ngn.kntc.beans.QuyetDinhThuLyBean;
import ngn.kntc.beans.ThiHanhQuyetDinhBean;
import ngn.kntc.beans.ThongBaoDenHanBean;
import ngn.kntc.beans.VanBanXuLyGiaiQuyetBean;
import ngn.kntc.databases.DatabaseServices;
import ngn.kntc.modules.KNTCProps;

import com.liferay.portal.model.User;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;

public class QuaTrinhXuLyGiaiQuyetServiceUtil implements Serializable{
	SimpleDateFormat sdfDateSql = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfDatetimeSql = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public void insertLuanChuyen(LuanChuyenBean model) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "INSERT INTO don_thu_luan_chuyen(IdDonThu, UserGiao, UserNhan, PhanCong, NgayGiaoChuyen) VALUES (?,?,?,?,?)";
		PreparedStatement preSt = con.prepareStatement(sql);
		preSt.setInt(1, model.getIdDonThu());
		preSt.setLong(2, model.getIdUserChuyen());
		preSt.setLong(3, model.getIdUserNhan());
		preSt.setInt(4, model.isPhanCong()?1:0);
		preSt.setString(5, sdfDatetimeSql.format(model.getNgayChuyen()));
		preSt.executeUpdate();

		preSt.close();
		con.close();
	}

	public int insertQuaTrinhXLGQ(QuaTrinhXuLyGiaiQuyetBean model) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "INSERT INTO don_thu_qua_trinh_xlgq(NoiDungXLGQ, NgayDang, TenFileDinhKem, LinkFileDinhKem, IdDonThu, UserNhap, HeThongTao) VALUES (?,?,?,?,?,?,?)";
		PreparedStatement preSt = con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
		preSt.setNString(1, model.getNoiDung());
		preSt.setString(2, sdfDatetimeSql.format(model.getNgayDang()));
		preSt.setNString(3, model.getTenFileDinhKem());
		preSt.setString(4, model.getLinkFileDinhKem());
		preSt.setInt(5, model.getMaDonThu());
		preSt.setLong(6, model.getUserNhap());
		preSt.setInt(7, model.isHeThongTao()?1:0);
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

	public void insertKetQuaXuLy(KetQuaXuLyBean model) throws SQLException
	{
		Connection cn= DatabaseServices.getConnection();
		String sql="INSERT INTO don_thu_ket_qua_xu_ly (TomTatNoiDungXL, NgayXuLy, CanBoXuLy, CanBoDuyetKQXL, MaHuongXuLy, MaCQXL, MaCQXLTiep,TenFileDinhKem,LinkFileDinhKem,NoiDungXuLy, IDDonThu,NgayTao,OrgTao) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preSt=cn.prepareStatement(sql);
		preSt.setNString(1,model.getTomTatNoiDung());
		preSt.setString(2, sdfDateSql.format(model.getNgayXuLy()));
		preSt.setNString(3,model.getCanBoXuLy());
		preSt.setNString(4, model.getCanBoDuyet());
		preSt.setInt(5,model.getMaHuongXuLy());
		preSt.setString(6,model.getMaCQXL());
		preSt.setString(7,model.getMaCQXLTiep());
		preSt.setNString(8,model.getTenFileDinhKem());
		preSt.setString(9,model.getLinkFileDinhKem());
		preSt.setNString(10, model.getNoiDungXuLy());
		preSt.setInt(11,model.getIdHoSo());
		preSt.setString(12,sdfDateSql.format(model.getNgayTao()));
		preSt.setLong(13, model.getOrgTao());
		preSt.executeUpdate();

		preSt.close();
		cn.close();
	}

	public void insertQuyetDinhThuLy(QuyetDinhThuLyBean model) throws SQLException
	{
		Connection cn= DatabaseServices.getConnection();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String sql="INSERT INTO don_thu_quyet_dinh_thu_ly (MaCoQuanThuLy, NgayThuLy, HanGiaiQuyet, CanBoDuyet, TenFileDinhKem, LinkFileDinhKem, IdDonThu,NgayTao) VALUES (?,?,?,?,?,?,?,?)";
		PreparedStatement preSt=cn.prepareStatement(sql);
		preSt.setString(1, model.getMaCoQuanThuLy());
		preSt.setString(2, sdf.format(model.getNgayThuLy()));
		preSt.setString(3, sdf.format(model.getHanGiaiQuyet()));
		preSt.setNString(4,model.getCanBoDuyet());
		preSt.setString(5,model.getTenFileDinhKem());
		preSt.setString(6,model.getLinkFileDinhKem());
		preSt.setInt(7,model.getMaDonThu());
		preSt.setString(8, sdf.format(model.getNgayTao()));
		preSt.executeUpdate();

		preSt.close();
		cn.close();
	}

	public int insertQuyetDinhGiaiQuyet(QuyetDinhGiaiQuyetBean model) throws SQLException
	{
		int idQuyetDinh = 0;
		Connection cn= DatabaseServices.getConnection();
		String sql="INSERT INTO don_thu_quyet_dinh_giai_quyet (IDCoQuanBanHanh, NgayBanHanh, TomTatNoiDung, IDKetQuaGiaiQuyet, IDHinhThucGiaiQuyet, QuyenKhoiKien, ThuHoiTien, ThuHoiDatO, ThuHoiDatSX, TraLaiTien, TraLaiDatO, TraLaiDatSX, SoNguoiDuocTraQuyenLoi, XuPhatHanhChinh, SoNguoiChuyenDieuTra,IdDonThu,TenFileDinhKem,LinkFileDinhKem,OrgTao,NgayTao) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preSt=cn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
		preSt.setString(1,model.getMaCoQuanBanHanh());
		preSt.setString(2, sdfDateSql.format(model.getNgayBanHanh()));
		preSt.setNString(3,model.getTomTatNoiDung());
		preSt.setInt(4, model.getLoaiKetQuaGiaiQuyet());
		preSt.setInt(5,model.getHinhThucGiaiQuyet());
		preSt.setInt(6,model.isQuyenKhoiKien() ? 1 : 0);
		preSt.setInt(7,model.getThuHoiTien());
		preSt.setInt(8,model.getThuHoiDatO());
		preSt.setInt(9,model.getThuHoiDatSX());
		preSt.setInt(10,model.getTraLaiTien());
		preSt.setInt(11,model.getTraLaiDatO());
		preSt.setInt(12,model.getTraLaiDatSX());
		preSt.setInt(13,model.getSoNguoiDuocTraQuyenLoi());
		preSt.setInt(14,model.getXuPhatHanhChinh());
		preSt.setInt(15,model.getSoNguoiChuyenDieuTra());
		preSt.setInt(16,model.getIdHoSo());
		preSt.setString(17,model.getTenFileDinhKem());
		preSt.setString(18,model.getLinkFileDinhKem());
		preSt.setLong(19, model.getOrgTao());
		preSt.setString(20,sdfDateSql.format(model.getNgayTao()));
		preSt.executeUpdate();
		ResultSet rs = preSt.getGeneratedKeys();
		rs.next();
		idQuyetDinh =rs.getInt(1);
		rs.close();
		preSt.close();
		cn.close();
		return idQuyetDinh;
	}

	public void insertDoiTuongBiXLHC(DoiTuongBiXuLyHanhChinhBean model) throws SQLException
	{
		Connection cn= DatabaseServices.getConnection();
		String sql="INSERT INTO don_thu_quyet_dinh_giai_quyet_doi_tuong_bi_xlhc(TenDoiTuong, ChucVu, HinhThucXuLy, CaNhan, IdQuyetDinhGiaiQuyet) VALUES (?,?,?,?,?)";
		PreparedStatement preSt=cn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
		preSt.setNString(1,model.getTenDoiTuong());
		preSt.setNString(2, model.getChucVu());
		preSt.setNString(3,model.getHinhThucXuLy());
		preSt.setInt(4, model.isCaNhan()?1:0);
		preSt.setInt(5,model.getMaQuyetDinhGiaiQuyet());
		preSt.executeUpdate();
		preSt.close();
		cn.close();
	}

	public int insertThiHanhGiaiQuyet(ThiHanhQuyetDinhBean model) throws SQLException
	{
		int idThiHanh = 0;
		Connection cn= DatabaseServices.getConnection();
		String sql="INSERT INTO don_thu_thi_hanh_giai_quyet (IDQuyetDinhGiaiQuyet, NgayCapNhat, MaCoQuanThiHanh, ThuHoiTien, ThuHoiDatO, ThuHoiDatSX, TraLaiTien, TraLaiDatO, TraLaiDatSX, SoNguoiBiXuLy, SoTapTheBiXuLy, TaiSanQuyRaTien, SoTienNopPhat, SoDOiTuongKhoiTo, OrgTao,NgayTao) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preSt=cn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
		preSt.setInt(1,model.getIdQuyetDinhGiaiQuyet());
		preSt.setString(2, sdfDateSql.format(model.getNgayCapNhat()));
		preSt.setString(3,model.getMaCoQuanThiHanh());
		preSt.setInt(4,model.getThuHoiTien());
		preSt.setInt(5,model.getThuHoiDatO());
		preSt.setInt(6,model.getThuHoiDatSX());
		preSt.setInt(7,model.getTraLaiTien());
		preSt.setInt(8,model.getTraLaiDatO());
		preSt.setInt(9,model.getTraLaiDatSX());
		preSt.setInt(10,model.getSoNguoiBiXuLy());
		preSt.setInt(11,model.getSoTapTheBiXuLy());
		preSt.setInt(12,model.getTaiSanQuyRaTien());
		preSt.setInt(13,model.getSoTienNopPhat());
		preSt.setInt(14,model.getSoDoiTuongBiKhoiTo());
		preSt.setLong(15, model.getOrgTao());
		preSt.setString(16,sdfDateSql.format(model.getNgayTao()));
		preSt.executeUpdate();
		ResultSet rs = preSt.getGeneratedKeys();
		rs.next();
		idThiHanh = rs.getInt(1);
		rs.close();
		preSt.close();
		cn.close();
		return idThiHanh;
	}

	public void insertThongBao(Long userId,int quaTrinhId) throws SQLException
	{
		Connection con= DatabaseServices.getConnection();
		String sql="INSERT INTO don_thu_qua_trinh_thong_bao(IdUser,IdQuaTrinh) VALUES ("+userId+","+quaTrinhId+")";
		con.createStatement().executeUpdate(sql);

		con.close();
	}

	public void updateHoanThanhVanBan(int idVanBan,Date dateComplete) throws SQLException
	{
		Connection con= DatabaseServices.getConnection();
		String sql="UPDATE don_thu_van_ban_xu_ly_giai_quyet SET NgayHoanThanh = '"+sdfDateSql.format(dateComplete)+"' WHERE IdVanBan = "+idVanBan;
		System.out.println(sql);
		con.createStatement().executeUpdate(sql);

		con.close();
	}

	public List<LuanChuyenBean> getLuanChuyenList(int idDonThu,int type) throws SQLException
	{
		/*
		 * 0 = tất cả
		 * 1 = phân công
		 * 2 = chuyển
		 * */
		List<LuanChuyenBean> list = new ArrayList<LuanChuyenBean>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu_luan_chuyen WHERE IdDonThu = "+idDonThu;
		if(type==1)
		{
			sql+=" AND PhanCong = 1";
		}else if(type==2)
		{
			sql+=" AND PhanCong = 0";
		}
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			LuanChuyenBean model = returnLuanChuyenBean(rs);
			list.add(model);
		}
		rs.close();
		con.close();
		return list;
	}

	public List<Long> getUserLuanChuyen(int idDonThu,int type) throws SQLException
	{
		/*
		 * 0 = tất cả
		 * 1 = phân công
		 * 2 = chuyển
		 * */
		List<Long> list = new ArrayList<Long>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT UserNhan FROM don_thu_luan_chuyen WHERE IdDonThu = "+idDonThu;
		if(type==1)
		{
			sql+=" AND PhanCong = 1";
		}else if(type==2)
		{
			sql+=" AND PhanCong = 0";
		}
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			list.add(rs.getLong(1));
		}
		rs.close();
		con.close();
		return list;
	}

	public List<Long> getLanhDaoLuanChuyen(int idDonThu) throws Exception
	{
		/*
		 * 0 = tất cả
		 * 1 = phân công
		 * 2 = chuyển
		 * */
		List<Long> list = new ArrayList<Long>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT UserNhan FROM don_thu_luan_chuyen WHERE PhanCong = 1 AND IdDonThu = "+idDonThu;
		ResultSet rs = con.createStatement().executeQuery(sql);
		List<User> listLanhDao = UserLocalServiceUtil.getOrganizationUsers(SessionUtil.getMasterOrgId());

		while(rs.next())
		{
			long i = rs.getLong(1);
			for(User user : listLanhDao)
			{
				if(user.getUserId() == i)
					list.add(i);
			}
		}
		rs.close();
		con.close();
		return list;
	}
	
	public List<Integer> getOrgDaChuyenList(int orgChuyenId) throws SQLException
	{
		List<Integer> list = new ArrayList<Integer>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT DISTINCT OrgNhan FROM don_thu_thong_tin_don_thu WHERE OrgChuyen != OrgNhan AND OrgChuyen = "+orgChuyenId;
		ResultSet rs = con.createStatement().executeQuery(sql);
		
		while(rs.next())
		{
			list.add(rs.getInt("OrgNhan"));
		}
		
		rs.close();
		con.close();
		
		return list;
	}

	public boolean isLanhDaoCuaCoQuanThamMuu(int idDonThu) throws Exception
	{
		boolean isLanhDaoCoQuanThamMuu = false;
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu_luan_chuyen WHERE UserGiao != UserNhan AND PhanCong = 1 AND IdDonThu = "+idDonThu;
		ResultSet rs = con.createStatement().executeQuery(sql);

		while(rs.next())
		{
			int userGiao = rs.getInt("UserGiao");
			int userNhan = rs.getInt("UserNhan");

			if(userNhan!=SessionUtil.getUserId())
				continue;
			if(LiferayServiceUtil.getUserOrgId(userGiao) != LiferayServiceUtil.getUserOrgId(userNhan))
				isLanhDaoCoQuanThamMuu = true;
		}
		rs.close();
		con.close();
		return isLanhDaoCoQuanThamMuu;
	}

	public List<Long> getDonViDaChuyenList(int idDonThu) throws Exception
	{
		List<Long> listOrgDaChuyen = new ArrayList<Long>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT UserNhan FROM don_thu_luan_chuyen WHERE PhanCong = 0 AND IdDonThu = "+idDonThu;
		ResultSet rs = con.createStatement().executeQuery(sql);
		while (rs.next()) {
			User user = UserLocalServiceUtil.getUser(rs.getInt(1));
			long masterLocation = LiferayServiceUtil.getMasterOrg((int)user.getOrganizations().get(0).getOrganizationId());
			if(!listOrgDaChuyen.contains(Long.valueOf(masterLocation)))
				listOrgDaChuyen.add(masterLocation);
		}
		rs.close();
		con.close();
		return listOrgDaChuyen;
	}

	public LuanChuyenBean getLuanChuyenOnUserNhan(long idUserNhan,long idDonThu) throws Exception
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu_luan_chuyen WHERE PhanCong = 0 AND IdDonThu = "+idDonThu+" AND UserNhan = "+idUserNhan;
		ResultSet rs = con.createStatement().executeQuery(sql);
		rs.next();
		LuanChuyenBean model = returnLuanChuyenBean(rs);

		rs.close();
		con.close();
		return model;
	}

	public LuanChuyenBean getLuanChuyenOnUserChuyen(long idUserChuyen,long idDonThu) throws Exception
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu_luan_chuyen WHERE PhanCong = 0 AND IdDonThu = "+idDonThu+" AND UserGiao = "+idUserChuyen;
		ResultSet rs = con.createStatement().executeQuery(sql);
		rs.next();
		LuanChuyenBean model = returnLuanChuyenBean(rs);

		rs.close();
		con.close();
		return model;
	}

	public List<QuaTrinhXuLyGiaiQuyetBean> getQuaTrinhXLGQList(int idDonThu, int loaiHienThi, int hienThiDinhKem) throws SQLException
	{
		List<QuaTrinhXuLyGiaiQuyetBean> list = new ArrayList<QuaTrinhXuLyGiaiQuyetBean>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu_qua_trinh_xlgq WHERE IdDonThu = "+idDonThu;
		switch (loaiHienThi) {
		case 2:
			sql+=" AND HeThongTao = 1";
			break;
		case 3:
			sql+=" AND HeThongTao = 0";
			break;
		}
		switch (hienThiDinhKem) {
		case 2:
			sql+=" AND TenFileDinhKem IS NOT NULL";
			break;
		case 3:
			sql+=" AND TenFileDinhKem IS NULL";
			break;
		}
		sql+=" ORDER BY NgayDang DESC";
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			QuaTrinhXuLyGiaiQuyetBean model = returnQuaTrinhXLGQBean(rs);
			list.add(model);
		}
		rs.close();
		con.close();
		return list;
	}

	public QuaTrinhXuLyGiaiQuyetBean getQuaTrinhXLGQ(int idQuaTrinh) throws SQLException
	{
		QuaTrinhXuLyGiaiQuyetBean model = new QuaTrinhXuLyGiaiQuyetBean();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu_qua_trinh_xlgq WHERE IdQuaTrinhXLGQ = "+idQuaTrinh;
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			model = returnQuaTrinhXLGQBean(rs);
		}
		rs.close();
		con.close();
		return model;
	}

	public List<KetQuaXuLyBean> getKetQuaXuLyList(int idDonThu) throws SQLException
	{
		List<KetQuaXuLyBean> list = new ArrayList<KetQuaXuLyBean>();
		Connection cn= DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu_ket_qua_xu_ly WHERE IdDonThu = "+idDonThu+" ORDER BY NgayXuLy DESC";
		ResultSet rs = cn.createStatement().executeQuery(sql);
		while(rs.next())
		{
			list.add(returnKetQuaXuLyBean(rs));
		}
		rs.close();
		cn.close();
		return list;
	}


	public KetQuaXuLyBean getKetQuaXuLy(int idDonThu,long l) throws SQLException
	{
		KetQuaXuLyBean model = null;
		Connection cn= DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu_ket_qua_xu_ly WHERE IdDonThu = "+idDonThu+" AND OrgTao = "+l;
		ResultSet rs = cn.createStatement().executeQuery(sql);
		while(rs.next())
		{
			model = new KetQuaXuLyBean();
			model = returnKetQuaXuLyBean(rs);
		}
		rs.close();
		cn.close();
		return model;
	}

	public QuyetDinhThuLyBean getQuyetDinhThuLy(int idDonThu) throws SQLException
	{
		QuyetDinhThuLyBean model = null;
		Connection cn= DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu_quyet_dinh_thu_ly WHERE IdDonThu = "+idDonThu;
		ResultSet rs = cn.createStatement().executeQuery(sql);
		while(rs.next())
		{
			model = new QuyetDinhThuLyBean();
			model.setIdQuyetDinhThuLy(rs.getInt("IDQuyetDinhThuLy"));
			model.setMaCoQuanThuLy(rs.getString("MaCoQuanThuLy"));
			model.setNgayThuLy(rs.getDate("NgayThuLy"));
			model.setHanGiaiQuyet(rs.getDate("HanGiaiQuyet"));
			model.setCanBoDuyet(rs.getString("CanBoDuyet"));
			model.setTenFileDinhKem(rs.getString("TenFileDinhKem"));
			model.setLinkFileDinhKem(rs.getString("LinkFileDinhKem"));
			model.setMaDonThu(rs.getInt("IdDonThu"));
			model.setNgayTao(rs.getDate("NgayTao"));
		}
		rs.close();
		cn.close();
		return model;
	}

	public QuyetDinhGiaiQuyetBean getQuyetDinhGiaiQuyet(int idDonThu) throws SQLException
	{
		QuyetDinhGiaiQuyetBean model = null;
		Connection cn= DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu_quyet_dinh_giai_quyet WHERE IdDonThu = "+idDonThu;
		ResultSet rs = cn.createStatement().executeQuery(sql);
		while(rs.next())
		{
			model = new QuyetDinhGiaiQuyetBean();
			model.setIdQuyetDinhLienThong(rs.getString("mattcp"));
			model.setMaQuyetDinhGiaiQuyet(rs.getInt("IDQuyetDinhGiaiQuyet"));
			model.setMaCoQuanBanHanh(rs.getString("IDCoQuanBanHanh"));
			model.setNgayBanHanh(rs.getDate("NgayBanHanh"));
			model.setTomTatNoiDung(rs.getString("TomTatNoiDung"));
			model.setLoaiKetQuaGiaiQuyet(rs.getInt("IDKetQuaGiaiQuyet"));
			model.setHinhThucGiaiQuyet(rs.getInt("IDHinhThucGiaiQuyet"));
			model.setThuHoiTien(rs.getInt("ThuHoiTien"));
			model.setThuHoiDatO(rs.getInt("ThuHoiDatO"));
			model.setThuHoiDatSX(rs.getInt("ThuHoiDatSX"));
			model.setTraLaiTien(rs.getInt("TraLaiTien"));
			model.setTraLaiDatO(rs.getInt("TraLaiDatO"));
			model.setTraLaiDatSX(rs.getInt("TraLaiDatSX"));
			model.setSoNguoiDuocTraQuyenLoi(rs.getInt("SoNguoiDuocTraQuyenLoi"));
			model.setSoNguoiChuyenDieuTra(rs.getInt("SoNguoiChuyenDieuTra"));
			model.setIdDonThu(rs.getInt("IdDonThu"));
			model.setQuyenKhoiKien(rs.getInt("QuyenKhoiKien")==1 ? true : false);
			model.setXuPhatHanhChinh(rs.getInt("XuPhatHanhChinh"));
			model.setTenFileDinhKem(rs.getString("TenFileDinhKem"));
			model.setLinkFileDinhKem(rs.getString("LinkFileDinhKem"));
			model.setOrgTao(rs.getLong("OrgTao"));
			model.setNgayTao(rs.getDate("NgayTao"));
		}
		rs.close();
		cn.close();
		return model;
	}

	public List<DoiTuongBiXuLyHanhChinhBean> getDoiTuongBiXuLyHanhChinhList(int maQuyetDinh) throws SQLException
	{
		List<DoiTuongBiXuLyHanhChinhBean> list = new ArrayList<DoiTuongBiXuLyHanhChinhBean>();
		Connection con= DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu_quyet_dinh_giai_quyet_doi_tuong_bi_xlhc WHERE IdQuyetDinhGiaiQuyet = "+maQuyetDinh+" ORDER BY CaNhan DESC";
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			DoiTuongBiXuLyHanhChinhBean model = new DoiTuongBiXuLyHanhChinhBean();
			model.setMaDoiTuong(rs.getInt("IdDoiTuongBiXLHC"));
			model.setTenDoiTuong(rs.getString("TenDoiTuong"));
			model.setChucVu(rs.getString("ChucVu"));
			model.setHinhThucXuLy(rs.getString("HinhThucXuLy"));
			model.setCaNhan(rs.getInt("CaNhan")==1?true:false);
			model.setMaQuyetDinhGiaiQuyet(rs.getInt("IdQuyetDinhGiaiQuyet"));
			list.add(model);
		}
		rs.close();
		con.close();
		return list;
	}

	public List<ThiHanhQuyetDinhBean> getThiHanhGiaiQuyetList(int idQuyetDinh) throws SQLException
	{
		List<ThiHanhQuyetDinhBean> list = new ArrayList<ThiHanhQuyetDinhBean>();
		Connection cn= DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu_thi_hanh_giai_quyet WHERE IDQuyetDinhGiaiQuyet = "+idQuyetDinh+" ORDER BY NgayTao DESC";
		ResultSet rs = cn.createStatement().executeQuery(sql);
		while(rs.next())
		{
			list.add(returnThiHanhGiaiQuyetBean(rs));
		}
		rs.close();
		cn.close();
		return list;
	}

	public List<ThiHanhQuyetDinhBean> getThiHanhGiaiQuyetNotLienThongList(int idQuyetDinh) throws SQLException
	{
		List<ThiHanhQuyetDinhBean> list = new ArrayList<ThiHanhQuyetDinhBean>();
		Connection cn= DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu_thi_hanh_giai_quyet WHERE IDQuyetDinhGiaiQuyet = "+idQuyetDinh+" AND mattcp IS NULL ORDER BY NgayTao DESC";
		ResultSet rs = cn.createStatement().executeQuery(sql);
		while(rs.next())
		{
			list.add(returnThiHanhGiaiQuyetBean(rs));
		}
		rs.close();
		cn.close();
		return list;
	}

	public List<Long> getDonThuUsers(int idDonThu) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		List<Long> list = new ArrayList<Long>();
		String sql = "SELECT DISTINCT UserNhan FROM don_thu_luan_chuyen WHERE idDonThu = "+idDonThu;
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			list.add(rs.getLong(1));
		}
		list.remove(Long.valueOf(SessionUtil.getUserId()));
		rs.close();
		con.close();
		return list;
	}

	public List<ThongBaoDenHanBean> getThongBaoDenHanXuLy() throws SQLException
	{
		int hanXuLy = Integer.parseInt(KNTCProps.getProperty("han.xu.ly"));
		List<ThongBaoDenHanBean> listThongBao = new ArrayList<ThongBaoDenHanBean>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT ("+hanXuLy+"-DATEDIFF(CURDATE(),NgayNhan)) as remain,a.IdDonThu,NoiDungDonThu FROM don_thu a,don_thu_thong_tin_don_thu b WHERE a.IdDonThu = b.IdDonThu AND OrgNhan = "+SessionUtil.getOrgId()+" AND NgayHoanThanhXuLy IS NULL AND ("+hanXuLy+"-DATEDIFF(CURDATE(),NgayNhan)) <= 3";
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			int remain = rs.getInt("remain");
			String strRemain =  "";

			if(remain>=0)
			{
				strRemain="<b style='color:orange'>Còn "+remain+" ngày đến hạn xử lý</b>";
			}
			else
			{
				strRemain="<b style='color:red'>Quá hạn xử lý "+remain*-1+" ngày</b>";
			}
			
			ThongBaoDenHanBean model = new ThongBaoDenHanBean();

			model.setIdDonThu(rs.getInt("IdDonThu"));
			model.setNoiDung("<span style='color: #425788;'><b>Nội dung</b>: "+rs.getString("NoiDungDonThu")+"</span>");

			model.setStrDenHan(strRemain);

			listThongBao.add(model);
		}
		rs.close();
		con.close();

		return listThongBao;
	}
	
	public List<ThongBaoDenHanBean> getThongBaoDenHanGiaiQuyet() throws SQLException
	{
		List<ThongBaoDenHanBean> listThongBao = new ArrayList<ThongBaoDenHanBean>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT (DATEDIFF(NgayHanGiaiQuyet,NgayThuLy)-DATEDIFF(CURDATE(),NgayThuLy)) as remain,a.IdDonThu,NoiDungDonThu FROM don_thu a,don_thu_thong_tin_don_thu b WHERE a.IdDonThu = b.IdDonThu AND NgayHoanThanhGiaiQuyet IS NULL AND (DATEDIFF(NgayHanGiaiQuyet,NgayThuLy)-DATEDIFF(CURDATE(),NgayThuLy)) <= 10 AND a.IdDonThu IN (SELECT IdDonThu FROM don_thu_quyet_dinh_thu_ly WHERE MaCoQuanThuLy = "+SessionUtil.getOrgId()+")";
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			int remain = rs.getInt("remain");
			String strRemain =  "";

			if(remain>=0)
			{
				strRemain="<b style='color:orange'>Còn "+remain+" ngày đến hạn giải quyết</b>";
			}
			else
			{
				strRemain="<b style='color:red'>Quá hạn giải quyết "+remain*-1+" ngày</b>";
			}
			
			ThongBaoDenHanBean model = new ThongBaoDenHanBean();

			model.setIdDonThu(rs.getInt("IdDonThu"));
			model.setNoiDung("<span style='color: #425788;'><b>Nội dung</b>: "+rs.getString("NoiDungDonThu")+"</span>");

			model.setStrDenHan(strRemain);

			listThongBao.add(model);
		}
		rs.close();
		con.close();

		return listThongBao;
	}
	
	public List<ThongBaoDenHanBean> getThongBaoDenHanThuLy() throws SQLException
	{
		int hanXuLy = Integer.parseInt(KNTCProps.getProperty("han.xu.ly"));
		List<ThongBaoDenHanBean> listThongBao = new ArrayList<ThongBaoDenHanBean>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT ("+hanXuLy+"-DATEDIFF(CURDATE(),NgayNhan)) as remain,a.IdDonThu,NoiDungDonThu,NgayNhan FROM don_thu a,don_thu_thong_tin_don_thu b WHERE a.IdDonThu = b.IdDonThu AND ("+hanXuLy+"-DATEDIFF(CURDATE(),NgayNhan)) <=3 AND a.IdDonThu IN (SELECT IdDonThu FROM don_thu_ket_qua_xu_ly WHERE OrgTao = "+SessionUtil.getOrgId()+" AND MaHuongXuLy = 4) AND a.IdDonThu NOT IN (SELECT IdDonThu FROM don_thu_quyet_dinh_thu_ly WHERE MaCoQuanThuLy = "+SessionUtil.getOrgId()+")";
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			int remain = rs.getInt("remain");
			String strRemain =  "";

			if(remain>=0)
			{
				strRemain="<b style='color:orange'>Còn "+remain+" ngày đến hạn thụ lý</b>";
			}
			else
			{
				strRemain="<b style='color:red'>Quá hạn thụ lý "+remain*-1+" ngày</b>";
			}
			
			ThongBaoDenHanBean model = new ThongBaoDenHanBean();

			model.setIdDonThu(rs.getInt("IdDonThu"));
			model.setNoiDung("<span style='color: #425788;'><b>Nội dung</b>: "+rs.getString("NoiDungDonThu")+"</span>");

			model.setStrDenHan(strRemain);

			listThongBao.add(model);
		}
		rs.close();
		con.close();

		return listThongBao;
	}

	public List<Date> getAllAlertDateOfUser() throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		List<Date> list = new ArrayList<Date>();
		String sql = "SELECT DISTINCT DATE(NgayDang) FROM don_thu_qua_trinh_xlgq,don_thu_qua_trinh_thong_bao WHERE don_thu_qua_trinh_xlgq.IdQuaTrinhXLGQ = don_thu_qua_trinh_thong_bao.IdQuaTrinh AND IdUser = "+SessionUtil.getUserId()+" ORDER BY DATE(NgayDang) DESC";
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			Date date = new Date(rs.getDate(1).getTime());
			list.add(date);
		}
		rs.close();
		con.close();
		return list;
	}

	public List<Integer> getAllDonThuOfUserByDate(Date date) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		List<Integer> list = new ArrayList<Integer>();
		String sql = "SELECT DISTINCT IdDonThu FROM don_thu_qua_trinh_xlgq,don_thu_qua_trinh_thong_bao WHERE don_thu_qua_trinh_xlgq.IdQuaTrinhXLGQ = don_thu_qua_trinh_thong_bao.IdQuaTrinh AND IdUser = "+SessionUtil.getUserId()+" AND DATE(NgayDang) = '"+sdfDateSql.format(date)+"' ORDER BY Date(NgayDang) DESC";
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			list.add(rs.getInt(1));
		}
		rs.close();
		con.close();
		return list;
	}

	public List<QuaTrinhXuLyGiaiQuyetBean> getQuaTrinhXuLyAlert(int idDonThu,Date date) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		List<QuaTrinhXuLyGiaiQuyetBean> list = new ArrayList<QuaTrinhXuLyGiaiQuyetBean>();
		String sql = "SELECT * FROM don_thu_qua_trinh_xlgq,don_thu_qua_trinh_thong_bao WHERE don_thu_qua_trinh_xlgq.IdQuaTrinhXLGQ = don_thu_qua_trinh_thong_bao.IdQuaTrinh AND IdUser = "+SessionUtil.getUserId()+" AND DATE(NgayDang) = '"+sdfDateSql.format(date)+"' AND IdDonThu = "+idDonThu+" ORDER BY Date(NgayDang) DESC";
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			list.add(returnQuaTrinhXLGQBean(rs));
		}
		rs.close();
		con.close();
		return list;
	}

	public List<Integer> getListUserNhapVanBanXuLyGiaiQuyetOfDonThu(int idDonThu) throws Exception
	{
		Connection con = DatabaseServices.getConnection();
		List<Integer> list = new ArrayList<Integer>();
		String sql = "SELECT DISTINCT UserNhap FROM don_thu_van_ban_xu_ly_giai_quyet WHERE IdDonThu = "+idDonThu;
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			list.add(rs.getInt(1));
		}
		rs.close();
		con.close();
		return list;
	}

	public List<VanBanXuLyGiaiQuyetBean> getVanBanXuLyGiaiQuyetOfDonThu(int idDonThu,int userId) throws Exception
	{
		Connection con = DatabaseServices.getConnection();
		List<VanBanXuLyGiaiQuyetBean> list = new ArrayList<VanBanXuLyGiaiQuyetBean>();
		String sql = "SELECT * FROM don_thu_van_ban_xu_ly_giai_quyet WHERE IdDonThu = "+idDonThu;
		if(userId!=1)
		{
			sql+=" AND UserNhap = "+userId;
		}
		sql+=" ORDER BY IdVanBan DESC";
		System.out.println(sql);
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			list.add(returnVanBanXuLyGiaiQuyet(rs));
		}
		rs.close();
		con.close();
		return list;
	}

	public String getStringFieldValueOfTable(String tableName,String fieldNameSelect,String fieldNameWhere,int idWhere) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT "+fieldNameSelect+" FROM "+tableName+" WHERE "+fieldNameWhere+" = "+idWhere;
		ResultSet rs = con.createStatement().executeQuery(sql);
		rs.next();
		String result = rs.getString(fieldNameSelect);
		rs.close();
		con.close();
		return result;
	}

	public void deleteAllThongBao(int idDonThu,Date date) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "DELETE FROM don_thu_qua_trinh_thong_bao WHERE IdUser = "+SessionUtil.getUserId()+" AND IdQuaTrinh IN (SELECT IDQuaTrinhXLGQ FROM don_thu_qua_trinh_xlgq WHERE IdDonThu = "+idDonThu+" AND DATE(NgayDang) = '"+sdfDateSql.format(date)+"')";
		System.out.println(sql);
		con.createStatement().executeUpdate(sql);
		con.close();
	}

	public void deleteThongBao(int idQuaTrinh) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "DELETE FROM don_thu_qua_trinh_thong_bao WHERE IdQuaTrinh = "+idQuaTrinh+" AND IdUser = "+SessionUtil.getUserId();
		System.out.println(sql);
		con.createStatement().executeUpdate(sql);
		con.close();
	}

	public static int countQuaTrinhXuLyAllAlert() throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT count(IdQuaTrinh) FROM don_thu_qua_trinh_thong_bao WHERE IdUser = "+SessionUtil.getUserId();
		ResultSet rs = con.createStatement().executeQuery(sql);
		rs.next();
		int count = rs.getInt(1);
		rs.close();
		con.close();
		return count;
	}

	public int countQuaTrinhXuLyAlertByDate(int idDonThu,Date date) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT count(IdQuaTrinhXLGQ) FROM don_thu_qua_trinh_xlgq,don_thu_qua_trinh_thong_bao WHERE don_thu_qua_trinh_xlgq.IdQuaTrinhXLGQ = don_thu_qua_trinh_thong_bao.IdQuaTrinh AND IdUser = "+SessionUtil.getUserId()+" AND DATE(NgayDang) = '"+sdfDateSql.format(date)+"' AND IdDonThu = "+idDonThu;
		ResultSet rs = con.createStatement().executeQuery(sql);
		rs.next();
		int count = rs.getInt(1);
		rs.close();
		con.close();
		return count;
	}

	public boolean checkIfDonViDaChuyenDonThu(int idDonThu,long org) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT count(IdThongTinDon) FROM don_thu_thong_tin_don_thu WHERE IdDonThu = "+idDonThu+" AND OrgNhan = "+org+" AND "+org+" NOT IN(SELECT OrgChuyen FROM don_thu_thong_tin_don_thu WHERE OrgChuyen != OrgNhan AND IdDonThu ="+idDonThu+")";
		System.out.println(sql);
		ResultSet rs = con.createStatement().executeQuery(sql);
		rs.next();
		int count = rs.getInt(1);
		rs.close();
		con.close();
		return count > 0 ? false : true;
	}

	public DonThuThongBaoBean getDonThuThongBao(int idDonThu) throws SQLException
	{
		DonThuThongBaoBean model = new DonThuThongBaoBean();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT NoiDungDonThu FROM don_thu WHERE IdDonThu = "+idDonThu;
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			model.setNoiDung(rs.getString(1));
		}
		rs.close();
		con.close();
		return model;
	}

	public QuaTrinhXuLyGiaiQuyetBean returnQuaTrinhXLGQBean(ResultSet rs) throws SQLException
	{
		QuaTrinhXuLyGiaiQuyetBean model = new QuaTrinhXuLyGiaiQuyetBean();

		model.setMaQuaTrinh(rs.getInt("IdQuaTrinhXLGQ"));
		model.setMaDonThu(rs.getInt("IdDonThu"));
		model.setNoiDung(rs.getString("NoiDungXLGQ"));
		model.setNgayDang(rs.getTimestamp("NgayDang"));
		model.setTenFileDinhKem(rs.getString("TenFileDinhKem"));
		model.setLinkFileDinhKem(rs.getString("LinkFileDinhKem"));
		model.setMaDonThu(rs.getInt("IdDonThu"));
		model.setUserNhap(rs.getInt("UserNhap"));
		model.setHeThongTao(rs.getInt("HeThongTao")==1?true:false);

		return model;
	}

	public KetQuaXuLyBean returnKetQuaXuLyBean(ResultSet rs) throws SQLException
	{
		KetQuaXuLyBean model = new KetQuaXuLyBean();
		model.setIdKetQua(rs.getInt("IDKetQua"));
		model.setTomTatNoiDung(rs.getString("TomTatNoiDungXL"));
		model.setNgayXuLy(rs.getDate("NgayXuLy"));
		model.setCanBoXuLy(rs.getString("CanBoXuLy"));
		model.setCanBoDuyet(rs.getString("CanBoDuyetKQXL"));
		model.setMaCQXL(rs.getString("MaCQXL"));
		model.setMaCQXLTiep(rs.getString("MaCQXLTiep"));
		model.setMaHuongXuLy(rs.getInt("MaHuongXuLy"));
		model.setTenFileDinhKem(rs.getString("TenFileDinhKem"));
		model.setLinkFileDinhKem(rs.getString("LinkFileDinhKem"));
		model.setNoiDungXuLy(rs.getString("NoiDungXuLy"));
		model.setMaDonThu(rs.getInt("IdDonThu"));
		model.setNgayTao(rs.getDate("NgayTao"));
		model.setOrgTao(rs.getLong("OrgTao"));
		return model;
	}

	public LuanChuyenBean returnLuanChuyenBean(ResultSet rs) throws SQLException
	{
		LuanChuyenBean model = new LuanChuyenBean();

		model.setIdLuanChuyen(rs.getInt("IdLuanChuyen"));
		model.setIdDonThu(rs.getInt("IdDonThu"));
		model.setIdUserChuyen(rs.getLong("UserGiao"));
		model.setIdUserNhan(rs.getLong("UserNhan"));
		model.setPhanCong(rs.getInt("PhanCong")==1?true:false);
		model.setNgayChuyen(rs.getTimestamp("NgayGiaoChuyen"));

		return model;
	}

	public VanBanXuLyGiaiQuyetBean returnVanBanXuLyGiaiQuyet(ResultSet rs) throws Exception
	{
		VanBanXuLyGiaiQuyetBean model = new VanBanXuLyGiaiQuyetBean();

		model.setIdVanBan(rs.getInt("IdVanBan"));
		model.setLoaiVanBan(rs.getInt("LoaiVanBan"));
		model.setNgayBanHanh(sdfDateSql.parse(rs.getString("NgayBanHanh")));
		model.setSoVanBan(rs.getString("SoVanBan"));
		model.setHanGiaiQuyet(rs.getString("HanGiaiQuyet")!=null?sdfDateSql.parse(rs.getString("HanGiaiQuyet")):null);
		model.setNgayHoanThanh(rs.getString("NgayHoanThanh")!=null?sdfDateSql.parse(rs.getString("NgayHoanThanh")):null);
		model.setNoiDungVanBan(rs.getString("NoiDung"));
		model.setGhiChuVanBan(rs.getString("GhiChu"));
		model.setTenFileDinhKem(rs.getString("TenFileDinhKem"));
		model.setLinkFileDinhKem(rs.getString("LinkFileDinhKem"));
		model.setNgayNhapVanBan(sdfDatetimeSql.parse(rs.getString("NgayNhapVanBan")));
		model.setUserNhap(rs.getInt("UserNhap"));
		model.setIdDonThu(rs.getInt("IdDonThu"));
		return model;
	}

	public String returnStringCanBoNhap(long canBo,String noiDung,String ghiChu) throws Exception
	{
		String rs =  "<b><span style='color: #0047b3'>"+UserLocalServiceUtil.getUser(canBo).getFirstName()+"</span> <span style='color: #35cc3b'>"+noiDung+"</span></b>";
		if(ghiChu!=null)
		{
			rs+="<br/><b>"+ghiChu+"</b>";
		}
		return rs;
	}

	public String returnStringQuatrinhTrinhDon(long canBoTrinh,long lanhDaoNhan) throws Exception
	{
		return "<b><span style='color: #0047b3'>"+UserLocalServiceUtil.getUser(canBoTrinh).getFirstName()+"</span> <span style='color: #ff8533'>đã trình đơn thư cho</span> <span style='color: #0047b3'>"+UserLocalServiceUtil.getUser(lanhDaoNhan).getFirstName()+"</span></b>";
	}

	public String returnStringPhanCong(long canBoPhanCong,long canBoNhan) throws Exception
	{
		return "<b><span style='color: #0047b3'>"+UserLocalServiceUtil.getUser(canBoPhanCong).getFirstName()+"</span> <span style='color: #ff8533'>đã chuyển đơn cho</span> <span style='color: #0047b3'>"+UserLocalServiceUtil.getUser(canBoNhan).getFirstName()+"</span> tham mưu giải quyết</b>";
	}

	public String returnStringChuyenDon(long canBoTrinh,long orgId) throws Exception
	{
		return "<b><span style='color: #0047b3'>"+UserLocalServiceUtil.getUser(canBoTrinh).getFirstName()+"</span> <span style='color: #ff8533'>đã chuyển đơn đến</span> <span style='color: #0047b3'>"+OrganizationLocalServiceUtil.getOrganization(orgId).getName()+"</span></b>";
	}

	public int countQuaTrinhXuLyGiaiQuyet(int type,int idDonThu) throws SQLException
	{
		/* type
		 * 1 = count quá trình tạo bởi hệ thống
		 * 2 = count quá trình tạo bởi người dùng
		 * 3 = count số tệp đính kèm
		 *  */
		int count = 0;
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT COUNT(IdQuaTrinhXLGQ) FROM don_thu_qua_trinh_xlgq WHERE IdDonThu = "+idDonThu;
		switch (type) {
		case 1:
			sql+=" AND HeThongTao = 1";
			break;
		case 2:
			sql+=" AND HeThongTao = 0";
			break;
		case 3:
			sql+=" AND TenFileDinhKem IS NOT NULL";
			break;
		default:
			break;
		}
		ResultSet rs = con.createStatement().executeQuery(sql);
		rs.next();
		count = rs.getInt(1);

		rs.close();
		con.close();
		return count;
	}

	public int countVanBanXuLyGiaiQuyetOfDonThu(int idDonThu) throws SQLException
	{
		/* type
		 * 1 = count quá trình tạo bởi hệ thống
		 * 2 = count quá trình tạo bởi người dùng
		 * 3 = count số tệp đính kèm
		 *  */
		int count = 0;
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT COUNT(IdVanBan) FROM don_thu_van_ban_xu_ly_giai_quyet WHERE IdDonThu = "+idDonThu;
		ResultSet rs = con.createStatement().executeQuery(sql);
		rs.next();
		count = rs.getInt(1);

		rs.close();
		con.close();
		return count;
	}

	public ThiHanhQuyetDinhBean returnThiHanhGiaiQuyetBean(ResultSet rs) throws SQLException
	{
		ThiHanhQuyetDinhBean model = new ThiHanhQuyetDinhBean();
		model.setIdLienThongThiHanh(rs.getString("mattcp"));
		model.setIdThiHanhQuyetDinh(rs.getInt("IDThiHanhGiaiQuyet"));
		model.setIdQuyetDinhGiaiQuyet(rs.getInt("IDQuyetDinhGiaiQuyet"));
		model.setNgayCapNhat(rs.getDate("NgayCapNhat"));
		model.setMaCoQuanThiHanh(rs.getString("MaCoQuanThiHanh"));
		model.setThuHoiTien(rs.getInt("ThuHoiTien"));
		model.setThuHoiDatO(rs.getInt("ThuHoiDatO"));
		model.setThuHoiDatSX(rs.getInt("ThuHoiDatSX"));
		model.setTraLaiTien(rs.getInt("TraLaiTien"));
		model.setTraLaiDatO(rs.getInt("TraLaiDatO"));
		model.setTraLaiDatSX(rs.getInt("TraLaiDatSX"));
		model.setSoNguoiBiXuLy(rs.getInt("SoNguoiBiXuLy"));
		model.setSoTapTheBiXuLy(rs.getInt("SoTapTheBiXuLy"));
		model.setTaiSanQuyRaTien(rs.getInt("TaiSanQuyRaTien"));
		model.setSoTienNopPhat(rs.getInt("SoTienNopPhat"));
		model.setSoDoiTuongBiKhoiTo(rs.getInt("SoDoiTuongKhoiTo"));
		model.setOrgTao(rs.getLong("OrgTao"));
		model.setNgayTao(rs.getDate("NgayTao"));

		return model;
	}
}
