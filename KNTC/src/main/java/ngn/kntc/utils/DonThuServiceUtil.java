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

import ngn.kntc.beans.DoiTuongBiKNTCBean;
import ngn.kntc.beans.DoiTuongDiKNTCBean;
import ngn.kntc.beans.DoiTuongUyQuyenBean;
import ngn.kntc.beans.DonThuBean;
import ngn.kntc.beans.FilterDonCanGiaiQuyetBean;
import ngn.kntc.beans.FilterDonCanThuLyBean;
import ngn.kntc.beans.FilterDonChuyenAutoBean;
import ngn.kntc.beans.FilterDonDaChuyenDiBean;
import ngn.kntc.beans.FilterDonDaCoKetQuaBean;
import ngn.kntc.beans.FilterDonDaThuLyBean;
import ngn.kntc.beans.FilterDonDaTiepNhanBean;
import ngn.kntc.beans.FilterDonDangThiHanhBean;
import ngn.kntc.beans.FilterDonDuocRut;
import ngn.kntc.beans.FilterDonNhanTuDonViKhacBean;
import ngn.kntc.beans.HoSoDinhKemBean;
import ngn.kntc.beans.ThongTinDonThuBean;
import ngn.kntc.beans.TraCuuBean;
import ngn.kntc.beans.VanBanXuLyGiaiQuyetBean;
import ngn.kntc.databases.DatabaseServices;
import ngn.kntc.enums.HanXuLyChuaEnum;
import ngn.kntc.enums.HanXuLyDaXuLyEnum;
import ngn.kntc.enums.LoaiTinhTrangChuyenDiEnum;
import ngn.kntc.enums.LoaiTinhTrangDonThuEnum;
import ngn.kntc.enums.TinhTrangDonThu;
import ngn.kntc.modules.KNTCProps;

import com.liferay.portal.service.UserLocalServiceUtil;

public class DonThuServiceUtil implements Serializable{
	static SimpleDateFormat sdfDateSql = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfDatetimeSql = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/* Add Section */
	public int insertDoiTuongDiKNTC(DoiTuongDiKNTCBean model) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "INSERT INTO don_thu_doi_tuong_di_kntc (HoTen,SoDinhDanhCaNhan,GioiTinh,NgayCapSoDinhDanh,NoiCapSoDinhDanh,MaTinh,MaHuyen,MaXa,DiaChiChiTiet,SoDienThoai,MaQuocTich,MaDanToc,NacDanh) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preSt = con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
		preSt.setNString(1, model.getHoTen());
		preSt.setString(2, model.getSoDinhDanhCaNhan());
		preSt.setInt(3, model.getGioiTinh());
		String ngayCap = null;
		if(model.getNgayCapSoDinhDanh()!=null)
			ngayCap = sdfDateSql.format(model.getNgayCapSoDinhDanh());
		preSt.setString(4,ngayCap);
		preSt.setNString(5, model.getNoiCapSoDinhDanh());
		preSt.setString(6, model.getMaTinh());
		preSt.setString(7, model.getMaHuyen());
		preSt.setString(8, model.getMaXa());
		preSt.setNString(9, model.getDiaChiChiTiet());
		preSt.setString(10,model.getSoDienThoai());
		preSt.setString(11, model.getMaQuocTich());
		preSt.setInt(12, model.getMaDanToc());
		preSt.setInt(13, model.isNacDanh()?1:0);
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

	public int insertDoiTuongBiKNTC(DoiTuongBiKNTCBean model) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "INSERT INTO don_thu_doi_tuong_bi_kntc(HoTen,SoDinhDanhCaNhan,GioiTinh,NgayCapSoDinhDanh,NoiCapSoDinhDanh,ChucVu,NoiCongTac,MaTinh,MaHuyen,MaXa,DiaChiChiTiet,MaQuocTich,MaDanToc,TenCoQuan,MaTinhCoQuan,MaHuyenCoQuan,MaXaCoQuan,DiaChiChiTietCoQuan) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preSt = con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
		preSt.setNString(1,model.getHoTen());
		preSt.setString(2,model.getSoDinhDanhCaNhan());
		preSt.setInt(3,model.getGioiTinh());
		String ngayCap = null;
		if(model.getNgayCapSoDinhDanh()!=null)
			ngayCap = sdfDateSql.format(model.getNgayCapSoDinhDanh());
		preSt.setString(4,ngayCap);
		preSt.setNString(5,model.getNoiCapSoDinhDanh());
		preSt.setNString(6,model.getChucVu());
		preSt.setNString(7,model.getNoiCongTac());
		preSt.setString(8,model.getMaTinh());
		preSt.setString(9,model.getMaHuyen());
		preSt.setString(10,model.getMaXa());
		preSt.setNString(11,model.getDiaChiChiTiet());
		preSt.setString(12,model.getMaQuocTich());
		preSt.setInt(13,model.getMaDanToc());
		preSt.setNString(14,model.getTenCoQuanToChuc());
		preSt.setString(15,model.getMaTinhCoQuan());
		preSt.setString(16,model.getMaHuyenCoQuan());
		preSt.setString(17,model.getMaXaCoQuan());
		preSt.setNString(18,model.getDiaChiChiTietCoQuan());
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

	public int insertDoiTuongUyQuyen(DoiTuongUyQuyenBean model) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "INSERT INTO don_thu_doi_tuong_uy_quyen (HoTen,GioiTinh,SoDinhDanhCaNhan,NgayCapSoDinhDanh,NoiCapSoDinhDanh,MaTinh,MaHuyen,MaXa,DiaChiChiTiet,MaQuocTich,MaDanToc) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preSt = con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
		preSt.setNString(1, model.getHoTen());
		preSt.setInt(2, model.getGioiTinh());
		preSt.setString(3, model.getSoDinhDanhCaNhan());
		String ngayCap = null;
		if(model.getNgayCapSoDinhDanh()!=null)
			ngayCap = sdfDateSql.format(model.getNgayCapSoDinhDanh());
		preSt.setString(4, ngayCap);
		preSt.setNString(5, model.getNoiCapSoDinhDanh());
		preSt.setString(6, model.getMaTinh());
		preSt.setString(7, model.getMaHuyen());
		preSt.setString(8, model.getMaXa());
		preSt.setNString(9, model.getDiaChiChiTiet());
		preSt.setString(10,model.getMaQuocTich());
		preSt.setInt(11,model.getMaDanToc());
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

	public void insertNguoiDaiDienTiepCongDan(int idSoTiepCongDan, int idNguoiDaiDien) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		Statement st = con.createStatement();
		String sql = "INSERT INTO so_tiep_cong_dan_nguoi_dai_dien values("+idSoTiepCongDan+","+idNguoiDaiDien+")";
		st.executeUpdate(sql);
		st.close();
		con.close();
	}

	public int insertThongTinDon(ThongTinDonThuBean model) throws SQLException
	{
		String ngay=null;
		Connection con = DatabaseServices.getConnection();
		String sql = "INSERT INTO don_thu_thong_tin_don_thu(NguonDonDen, MaCoQuanChuyenDen, SoVanBanDen, NgayPhatHanhVanBan, OrgChuyen, OrgNhan, NgayNhan,IdDonThu) VALUES (?,?,?,?,?,?,?,?)";
		PreparedStatement preSt = con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);

		preSt.setInt(1, model.getNguonDonDen());
		preSt.setNString(2, model.getMaCoQuanChuyenDen());
		preSt.setNString(3, model.getSoVanBanDen());
		if(model.getNgayPhatHanhVanBanDen()!=null)
			ngay = sdfDatetimeSql.format(model.getNgayPhatHanhVanBanDen());
		else
			ngay = null;
		preSt.setString(4,ngay);
		preSt.setLong(5, model.getOrgChuyen());
		preSt.setLong(6, model.getOrgNhan());
		preSt.setString(7, sdfDateSql.format(model.getNgayNhan()));
		preSt.setInt(8,model.getMaDonThu());

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

	public int insertDonThu(DonThuBean model) throws SQLException
	{
		String ngay=null;
		Connection con = DatabaseServices.getConnection();
		String sql = "INSERT INTO don_thu(IdDoiTuongBiKNTC, IdDoiTuongUyQuyen, NoiDungDonThu, ThamQuyenGiaiQuyet, LoaiDonThu, NgayNhanDon, NgayNhapDon, LoaiNguoiDiKNTC, SoNguoiDiKNTC, SoNguoiDaiDien, TenCoQuanDiKNTC, DiaChiCoQuanDiKNTC, LoaiNguoiBiKNTC, LoaiNguoiUyQuyen, MaCoQuanDaGiaiQuyet, SoKyHieuVanBanGiaiQuyet, LanGiaiQuyet, NgayBanHanhQDGQ, LoaiQuyetDinhGiaiQuyet, TomTatNoiDungGiaiQuyet, UserThuLy, UserNhapDon, DonNacDanh, DonKhongDuDieuKienXL, GanVuViec) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preSt = con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
		if(model.getMaDoiTuongBiKNTC()!=0)
			preSt.setInt(1, model.getMaDoiTuongBiKNTC());
		else
			preSt.setNull(1, Types.INTEGER);
		if(model.getMaDoiTuongUyQuyen()!=0)
			preSt.setInt(2, model.getMaDoiTuongUyQuyen());
		else
			preSt.setNull(2, Types.INTEGER);
		preSt.setNString(3, model.getNoiDungDonThu());
		preSt.setString(4, model.getThamQuyenGiaiQuyet());
		preSt.setInt(5, model.getLoaiDonThu());

		if(model.getNgayNhanDon()!=null)
			ngay = sdfDatetimeSql.format(model.getNgayNhanDon());
		else
			ngay = null;
		preSt.setString(6,ngay);
		if(model.getNgayNhapDon()!=null)
			ngay = sdfDatetimeSql.format(model.getNgayNhapDon());
		else
			ngay = null;
		preSt.setString(7,ngay);

		if(model.getLoaiNguoiDiKNTC()!=0)
			preSt.setLong(8, model.getLoaiNguoiDiKNTC());
		else
			preSt.setNull(8, Types.INTEGER);
		if(model.getSoNguoiDiKNTC()!=0)
			preSt.setLong(9, model.getSoNguoiDiKNTC());
		else
			preSt.setNull(9, Types.INTEGER);
		if(model.getSoNguoiDaiDien()!=0)
			preSt.setLong(10, model.getSoNguoiDaiDien());
		else
			preSt.setNull(10, Types.INTEGER);
		preSt.setNString(11, model.getTenCoQuanDiKNTC());
		preSt.setNString(12,model.getDiaChiCoQuanDiKNTC());
		if(model.getLoaiNguoiBiKNTC()!=0)
			preSt.setLong(13, model.getLoaiNguoiBiKNTC());
		else
			preSt.setNull(13, Types.INTEGER);
		if(model.getLoaiNguoiUyQuyen()!=0)
			preSt.setLong(14, model.getLoaiNguoiUyQuyen());
		else
			preSt.setNull(14, Types.INTEGER);
		preSt.setNString(15, model.getMaCoQuanDaGiaiQuyet());
		preSt.setNString(16, model.getSoKyHieuVanBanGiaiQuyet());
		preSt.setInt(17, model.getLanGiaiQuyet());
		if(model.getNgayBanHanhQDGQ()!=null)
			ngay = sdfDatetimeSql.format(model.getNgayBanHanhQDGQ());
		else
			ngay = null;
		preSt.setString(18,ngay);
		if(model.getLoaiQuyetDinhGiaiQuyet()!=0)
			preSt.setInt(19, model.getLoaiQuyetDinhGiaiQuyet());
		else
			preSt.setNull(19, Types.INTEGER);
		preSt.setNString(20, model.getTomTatNoiDungGiaiQuyet());
		preSt.setLong(21, model.getUserThuLy());
		preSt.setLong(22, model.getUserNhapDon());
		preSt.setInt(23,model.isDonNacDanh()? 1:0);
		preSt.setInt(24,model.isDonKhongDuDieuKienXuLy()? 1:0);

		preSt.setString(25, model.getGanVuViec());
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

	public void insertNguoiDaiDienDonThu(int idDonThu, int idNguoiDaiDien) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		Statement st = con.createStatement();
		String sql = "INSERT INTO don_thu_nguoi_dai_dien values("+idDonThu+","+idNguoiDaiDien+")";
		st.executeUpdate(sql);
		st.close();
		con.close();
	}

	public void insertLinhVucDonThu(int maDonThu,String maLinhVuc) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "INSERT INTO don_thu_linh_vuc VALUES(?,?)";
		PreparedStatement preSt = con.prepareStatement(sql);
		preSt.setInt(1, maDonThu);
		preSt.setString(2, maLinhVuc);
		preSt.executeUpdate();
		preSt.close();
		con.close();
	}

	public void insertHoSoDinhKem(HoSoDinhKemBean model,int idDonThu) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "INSERT INTO don_thu_ho_so_dinh_kem(TenHoSo,LoaiHoSo,NoiDungTomTat,LinkFileDinhKem,TenFileDinhKem,IdDonThu) VALUES(?,?,?,?,?,?)";
		PreparedStatement preSt = con.prepareStatement(sql);
		preSt.setNString(1, model.getTenHoSo());
		preSt.setInt(2, model.getLoaiHoSo());
		preSt.setNString(3, model.getNoiDungTomTat());
		preSt.setString(4, model.getLinkFileDinhKem());
		preSt.setNString(5, model.getTenFileDinhKem());
		preSt.setInt(6, idDonThu);
		preSt.executeUpdate();
		preSt.close();
		con.close();
	}

	public void insertVanBanXuLyGiaiQuyet(VanBanXuLyGiaiQuyetBean model) throws SQLException
	{
		String ngay = null;
		Connection con = DatabaseServices.getConnection();
		String sql = "INSERT INTO don_thu_van_ban_xu_ly_giai_quyet(LoaiVanBan, NgayBanHanh, HanGiaiQuyet, SoVanBan, NoiDung, GhiChu, TenFileDinhKem, LinkFileDinhKem, UserNhap, idDonThu) VALUES (?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preSt = con.prepareStatement(sql);
		preSt.setInt(1,model.getLoaiVanBan());
		preSt.setString(2,sdfDateSql.format(model.getNgayBanHanh()));
		if(model.getHanGiaiQuyet()!=null)
			ngay = sdfDateSql.format(model.getHanGiaiQuyet());
		else
			ngay = null;
		preSt.setString(3,ngay);
		preSt.setString(4,model.getSoVanBan());
		preSt.setNString(5,model.getNoiDungVanBan());
		preSt.setNString(6,model.getGhiChuVanBan());
		preSt.setString(7,model.getTenFileDinhKem());
		preSt.setString(8,model.getLinkFileDinhKem());
		preSt.setInt(9,model.getUserNhap());
		preSt.setInt(10,model.getIdDonThu());

		preSt.executeUpdate();
		preSt.close();
		con.close();
	}
	/* Update Section */
	public void updateDoiTuongDiKNTC(DoiTuongDiKNTCBean model) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "UPDATE don_thu_doi_tuong_di_kntc SET HoTen=?,SoDinhDanhCaNhan=?,GioiTinh=?,NgayCapSoDinhDanh=?,NoiCapSoDinhDanh=?,MaTinh=?,MaHuyen=?,MaXa=?,DiaChiChiTiet=?,SoDienThoai=?,MaQuocTich=?,MaDanToc=? WHERE IdDoiTuongDiKNTC= ?";
		PreparedStatement preSt = con.prepareStatement(sql);
		preSt.setNString(1, model.getHoTen());
		preSt.setString(2, model.getSoDinhDanhCaNhan());
		preSt.setInt(3, model.getGioiTinh());
		String ngayCap = null;
		if(model.getNgayCapSoDinhDanh()!=null)
			ngayCap = sdfDateSql.format(model.getNgayCapSoDinhDanh());
		preSt.setString(4,ngayCap);
		preSt.setNString(5, model.getNoiCapSoDinhDanh());
		preSt.setString(6, model.getMaTinh());
		preSt.setString(7, model.getMaHuyen());
		preSt.setString(8, model.getMaXa());
		preSt.setNString(9, model.getDiaChiChiTiet());
		preSt.setString(10, model.getSoDienThoai());
		preSt.setString(11, model.getMaQuocTich());
		preSt.setInt(12, model.getMaDanToc());
		preSt.setInt(13, model.getMaDoiTuong());
		preSt.executeUpdate();

		preSt.close();
		con.close();
	}

	public void updateDoiTuongBiKNTC(DoiTuongBiKNTCBean model) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "UPDATE don_thu_doi_tuong_bi_kntc SET HoTen=?,SoDinhDanhCaNhan=?,GioiTinh=?,NgayCapSoDinhDanh=?,NoiCapSoDinhDanh=?,ChucVu=?,NoiCongTac=?,MaTinh=?,MaHuyen=?,MaXa=?,DiaChiChiTiet=?,MaQuocTich=?,MaDanToc=?,TenCoQuan=?,MaTinhCoQuan=?,MaHuyenCoQuan=?,MaXaCoQuan=?,DiaChiChiTietCoQuan=? WHERE IdDoiTuongBiKNTC=?";
		PreparedStatement preSt = con.prepareStatement(sql);
		preSt.setNString(1,model.getHoTen());
		preSt.setString(2,model.getSoDinhDanhCaNhan());
		preSt.setInt(3,model.getGioiTinh());
		String ngayCap = null;
		if(model.getNgayCapSoDinhDanh()!=null)
			ngayCap = sdfDateSql.format(model.getNgayCapSoDinhDanh());
		preSt.setString(4,ngayCap);
		preSt.setNString(5,model.getNoiCapSoDinhDanh());
		preSt.setNString(6,model.getChucVu());
		preSt.setNString(7,model.getNoiCongTac());
		preSt.setString(8,model.getMaTinh());
		preSt.setString(9,model.getMaHuyen());
		preSt.setString(10,model.getMaXa());
		preSt.setNString(11,model.getDiaChiChiTiet());
		preSt.setString(12,model.getMaQuocTich());
		preSt.setInt(13,model.getMaDanToc());
		preSt.setNString(14,model.getTenCoQuanToChuc());
		preSt.setString(15,model.getMaTinhCoQuan());
		preSt.setString(16,model.getMaHuyenCoQuan());
		preSt.setString(17,model.getMaXaCoQuan());
		preSt.setNString(18,model.getDiaChiChiTietCoQuan());
		preSt.setInt(19, model.getMaDoiTuong());
		preSt.executeUpdate();
		preSt.close();
		con.close();
	}

	public void updateDoiTuongUyQuyen(DoiTuongUyQuyenBean model) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "UPDATE don_thu_doi_tuong_uy_quyen SET HoTen=?,GioiTinh=?,SoDinhDanhCaNhan=?,NgayCapSoDinhDanh=?,NoiCapSoDinhDanh=?,DiaChiChiTiet=?,MaTinh=?,MaHuyen=?,MaXa=?,MaQuocTich=?,MaDanToc=? WHERE IdDoiTuongUyQuyen=?";
		PreparedStatement preSt = con.prepareStatement(sql);
		preSt.setNString(1, model.getHoTen());
		preSt.setInt(2, model.getGioiTinh());
		preSt.setString(3, model.getSoDinhDanhCaNhan());
		String ngayCap = null;
		if(model.getNgayCapSoDinhDanh()!=null)
			ngayCap = sdfDateSql.format(model.getNgayCapSoDinhDanh());
		preSt.setString(4, ngayCap);
		preSt.setNString(5, model.getNoiCapSoDinhDanh());
		preSt.setNString(6, model.getDiaChiChiTiet());
		preSt.setString(7, model.getMaTinh());
		preSt.setString(8, model.getMaHuyen());
		preSt.setString(9, model.getMaXa());
		preSt.setString(10,model.getMaQuocTich());
		preSt.setInt(11,model.getMaDanToc());
		preSt.setInt(12, model.getMaNguoiUyQuyen());
		preSt.executeUpdate();
		preSt.close();
		con.close();
	}
	
	public void updateDonThuStringField(String nameField,String value,int idDonThu) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "UPDATE don_thu SET "+nameField+" = N'"+value+"' WHERE idDonThu = "+idDonThu;

		con.createStatement().executeUpdate(sql);

		con.close();
	}

	public void updateDonThu(DonThuBean model) throws SQLException
	{
		String ngay="";
		Connection con = DatabaseServices.getConnection();
		String sql = "UPDATE don_thu SET NoiDungDonThu=?,NgayNhanDon=?,NgayNhapDon=?,SoNguoiDiKNTC=?,SoNguoiDaiDien=?,TenCoQuanDiKNTC=?,DiaChiCoQuanDiKNTC=?,MaCoQuanDaGiaiQuyet=?,LanGiaiQuyet=?,NgayBanHanhQDGQ=?,LoaiQuyetDinhGiaiQuyet=?,TomTatNoiDungGiaiQuyet=?,SoKyHieuVanBanGiaiQuyet=? WHERE IdDonThu = ?";
		PreparedStatement preSt = con.prepareStatement(sql);
		preSt.setNString(1, model.getNoiDungDonThu());
		if(model.getNgayNhanDon()!=null)
			ngay = sdfDatetimeSql.format(model.getNgayNhanDon());
		else
			ngay = null;
		preSt.setString(2,ngay);
		if(model.getNgayNhapDon()!=null)
			ngay = sdfDatetimeSql.format(model.getNgayNhapDon());
		else
			ngay = null;
		preSt.setString(3,ngay);

		if(model.getSoNguoiDiKNTC()!=0)
			preSt.setLong(4, model.getSoNguoiDiKNTC());
		else
			preSt.setNull(4, Types.INTEGER);
		if(model.getSoNguoiDaiDien()!=0)
			preSt.setLong(5, model.getSoNguoiDaiDien());
		else
			preSt.setNull(5, Types.INTEGER);
		preSt.setNString(6, model.getTenCoQuanDiKNTC());
		preSt.setNString(7,model.getDiaChiCoQuanDiKNTC());
		preSt.setNString(8, model.getMaCoQuanDaGiaiQuyet());
		preSt.setInt(9, model.getLanGiaiQuyet());
		if(model.getNgayBanHanhQDGQ()!=null)
			ngay = sdfDatetimeSql.format(model.getNgayBanHanhQDGQ());
		else
			ngay = null;
		preSt.setString(10,ngay);
		if(model.getLoaiQuyetDinhGiaiQuyet()!=0)
			preSt.setInt(11, model.getLoaiQuyetDinhGiaiQuyet());
		else
			preSt.setNull(11, Types.INTEGER);
		preSt.setNString(12, model.getTomTatNoiDungGiaiQuyet());
		preSt.setNString(13, model.getSoKyHieuVanBanGiaiQuyet());
		preSt.setInt(14, model.getMaDonThu());

		preSt.executeUpdate();

		preSt.close();
		con.close();
	}

	public void updateThongTinDonThuDate(String nameDateField,Date value,int idThongTinDon) throws SQLException
	{
		String ngay = sdfDatetimeSql.format(value);
		Connection con = DatabaseServices.getConnection();
		String sql = "UPDATE don_thu_thong_tin_don_thu SET "+nameDateField+" = '"+ngay+"' WHERE IdThongTinDon = "+idThongTinDon;

		con.createStatement().executeUpdate(sql);

		con.close();
	}

	public void updateDonThuDate(String nameDateField,Date value,int idDonThu) throws SQLException
	{
		String ngay = sdfDatetimeSql.format(value);
		Connection con = DatabaseServices.getConnection();
		String sql = "UPDATE don_thu SET "+nameDateField+" = '"+ngay+"' WHERE idDonThu = "+idDonThu;

		con.createStatement().executeUpdate(sql);

		con.close();
	}

	public void updateDonThuStatus(int status,int idDonThu) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "UPDATE don_thu SET Status = "+status+" WHERE idDonThu = "+idDonThu;

		con.createStatement().executeUpdate(sql);

		con.close();
	}

	/* Delete Section */
	public void deleteLinhVucDonThu(int idDonThu) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "DELETE FROM don_thu_linh_vuc WHERE IdDonThu = "+idDonThu;
		con.createStatement().executeUpdate(sql);
		con.close();
	}

	public void deleteNguoiDaiDienDonThu(int idDonThu) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "DELETE FROM don_thu_nguoi_dai_dien WHERE IdDonThu = "+idDonThu;
		con.createStatement().executeUpdate(sql);
		con.close();
	}

	/* Get Section */
	public DoiTuongDiKNTCBean getDoiTuongDiKNTC(int id) throws Exception
	{
		DoiTuongDiKNTCBean model = new DoiTuongDiKNTCBean();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu_doi_tuong_di_kntc WHERE IdDoiTuongDiKNTC = "+id;
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			model = returnDoiTuongDiKNTCBean(rs);
		}
		rs.close();
		con.close();

		return model;
	}

	public DoiTuongBiKNTCBean getDoiTuongBiKNTC(int id) throws Exception
	{
		DoiTuongBiKNTCBean model = new DoiTuongBiKNTCBean();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu_doi_tuong_bi_kntc WHERE IdDoiTuongBiKNTC = "+id;
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			model = returnDoiTuongBiKNTCBean(rs);
		}
		rs.close();
		con.close();

		return model;
	}

	public DoiTuongUyQuyenBean getDoiTuongUyQuyen(int id) throws Exception
	{
		DoiTuongUyQuyenBean model = new DoiTuongUyQuyenBean();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu_doi_tuong_uy_quyen WHERE IdDoiTuongUyQuyen = "+id;
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			model = returnDoiTuongUyQuyenBean(rs);
		}
		rs.close();
		con.close();

		return model;
	}

	public ThongTinDonThuBean getThongTinDonThu(int idDonThu,long idDonVi) throws Exception
	{
		// Dùng để lấy thông tin đơn của đơn vị đó
		ThongTinDonThuBean model = new ThongTinDonThuBean();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu_thong_tin_don_thu WHERE IdDonThu = "+idDonThu+" AND OrgNhan = "+idDonVi;
		System.out.println(sql);
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			model = returnThongTinDonThuBean(rs);
		}
		rs.close();
		con.close();

		return model;
	}

	public ThongTinDonThuBean getThongTinDonThuChuyenDi(int idDonThu,long idDonVi) throws Exception
	{
		// Dùng để lấy thông tin đơn của org nhận dựa vào org chuyển
		ThongTinDonThuBean model = null;
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu_thong_tin_don_thu WHERE IdDonThu = "+idDonThu+" AND OrgChuyen != OrgNhan AND OrgChuyen = "+idDonVi;
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			model = new ThongTinDonThuBean();
			model = returnThongTinDonThuBean(rs);
		}
		rs.close();
		con.close();

		return model;
	}

	public int getLoaiDonThu(int id) throws Exception
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT LoaiDonThu FROM don_thu WHERE IdDonThu = "+id;
		ResultSet rs = con.createStatement().executeQuery(sql);
		rs.next();
		int loaiDonThu = rs.getInt(1);
		rs.close();
		con.close();

		return loaiDonThu;
	}

	public DonThuBean getDonThu(int id) throws Exception
	{
		DonThuBean model = new DonThuBean();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu WHERE IdDonThu = "+id;
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			model = returnDonThuBean(rs);
		}
		rs.close();
		con.close();

		return model;
	}

	public List<DonThuBean> getDonThuDaTiepNhan(FilterDonDaTiepNhanBean modelFilter) throws Exception
	{
		String strListUser = "";
		for(long l: UserLocalServiceUtil.getOrganizationUserIds(SessionUtil.getOrgId()))
		{
			strListUser += l+","; 
		}
		strListUser = strListUser.substring(0,strListUser.length()-1);
		List<DonThuBean> list = new ArrayList<DonThuBean>();
		Connection con = DatabaseServices.getConnection();
		String conditionHanXuLy = "";
		if(modelFilter.getHanXuLy()==HanXuLyChuaEnum.conhanxuly.getType())
		{
			conditionHanXuLy=" AND DATEDIFF(CURDATE(),NgayNhanDon) <= "+Integer.parseInt(KNTCProps.getProperty("han.xu.ly"));
		}
		else if(modelFilter.getHanXuLy()==HanXuLyChuaEnum.quahanxuly.getType())
		{
			conditionHanXuLy=" AND DATEDIFF(CURDATE(),NgayNhanDon) > "+Integer.parseInt(KNTCProps.getProperty("han.xu.ly"));
		}
		String sql = "SELECT * FROM don_thu WHERE 1 = 1";
		sql+=returnDonThuTypeCondition(LoaiTinhTrangDonThuEnum.datiepnhan.getType());
		sql+=" AND ( NoiDungDonThu LIKE ?";
		sql+=" OR IdDonThu IN ( SELECT IdDonThu FROM don_thu_nguoi_dai_dien WHERE IdDoiTuongDiKNTC IN (SELECT IdDoiTuongDiKNTC FROM don_thu_doi_tuong_di_kntc WHERE HoTen LIKE ?))";
		sql+=" OR IdDoiTuongBiKNTC IN ( SELECT IdDoiTuongBiKNTC FROM don_thu_doi_tuong_bi_kntc WHERE HoTen LIKE ? OR TenCoQuan LIKE ?))";
		sql+=" AND IdDonThu IN (SELECT IdDonThu From don_thu_thong_tin_don_thu WHERE OrgChuyen = OrgNhan AND OrgNhan = "+SessionUtil.getOrgId()+" "+conditionHanXuLy+")";
		if(modelFilter.getNgayNhapStart()!=null)
			sql+=" AND (NgayNhapDon BETWEEN '"+sdfDateSql.format(modelFilter.getNgayNhapStart())+"' AND '"+sdfDateSql.format(modelFilter.getNgayNhapEnd())+"')";
		if(modelFilter.getLoaiDonThu()!=0)
			sql+=" AND LoaiDonThu = "+modelFilter.getLoaiDonThu();
		if(modelFilter.getDieuKienXuLy()!=-1)
			sql+=" AND DonKhongDuDieuKienXL = "+modelFilter.getDieuKienXuLy();
		sql+=" ORDER BY NgayNhapDon DESC";
		System.out.println(sql);
		PreparedStatement preSt = con.prepareStatement(sql);
		preSt.setNString(1, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(2, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(3, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(4, "%"+modelFilter.getKeyWord()+"%");
		ResultSet rs = preSt.executeQuery();
		while(rs.next())
		{
			list.add(returnDonThuBean(rs));
		}
		rs.close();
		con.close();

		return list;
	}

	public List<DonThuBean> getDonThuDonViKhacChuyenDen(FilterDonNhanTuDonViKhacBean modelFilter) throws Exception
	{
		String conditionHanXuLy = "";
		String strListUser = "";
		for(long l: UserLocalServiceUtil.getOrganizationUserIds(SessionUtil.getOrgId()))
		{
			strListUser += l+","; 
		}
		strListUser = strListUser.substring(0,strListUser.length()-1);
		List<DonThuBean> list = new ArrayList<DonThuBean>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu WHERE 1 = 1";
		sql+=returnDonThuTypeCondition(LoaiTinhTrangDonThuEnum.donvikhacchuyenden.getType());
		sql+=" AND ( NoiDungDonThu LIKE ?";
		sql+=" OR IdDonThu IN ( SELECT IdDonThu FROM don_thu_nguoi_dai_dien WHERE IdDoiTuongDiKNTC IN (SELECT IdDoiTuongDiKNTC FROM don_thu_doi_tuong_di_kntc WHERE HoTen LIKE ?))";
		sql+=" OR IdDoiTuongBiKNTC IN ( SELECT IdDoiTuongBiKNTC FROM don_thu_doi_tuong_bi_kntc WHERE HoTen LIKE ? OR TenCoQuan LIKE ?))";
		if(modelFilter.getHanXuLy()==HanXuLyChuaEnum.conhanxuly.getType())
		{
			conditionHanXuLy=" AND DATEDIFF(CURDATE(),NgayNhanDon) <= "+Integer.parseInt(KNTCProps.getProperty("han.xu.ly"));
		}
		else if(modelFilter.getHanXuLy()==HanXuLyChuaEnum.quahanxuly.getType())
		{
			conditionHanXuLy=" AND DATEDIFF(CURDATE(),NgayNhanDon) > "+Integer.parseInt(KNTCProps.getProperty("han.xu.ly"));
		}
		sql+=" AND IdDonThu IN (SELECT IdDonThu From don_thu_thong_tin_don_thu WHERE OrgChuyen != OrgNhan AND OrgNhan = "+SessionUtil.getOrgId()+conditionHanXuLy+")";
		if(modelFilter.getNgayNhanStart()!=null)
		{
			sql+=" AND IdDonThu IN (SELECT IdDonThu FROM don_thu_thong_tin_don_thu WHERE OrgChuyen != OrgNhan AND OrgNhan = "+SessionUtil.getOrgId()+" AND (NgayNhan BETWEEN '"+sdfDateSql.format(modelFilter.getNgayNhanStart())+"' AND '"+sdfDateSql.format(modelFilter.getNgayNhanEnd())+"'))";
		}
		if(modelFilter.getLoaiDonThu()!=0)
			sql+=" AND LoaiDonThu = "+modelFilter.getLoaiDonThu();
		sql+=" ORDER BY NgayNhapDon DESC";
		System.out.println(sql);
		PreparedStatement preSt = con.prepareStatement(sql);
		preSt.setNString(1, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(2, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(3, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(4, "%"+modelFilter.getKeyWord()+"%");
		ResultSet rs = preSt.executeQuery();
		while(rs.next())
		{
			list.add(returnDonThuBean(rs));
		}
		rs.close();
		con.close();

		return list;
	}

	public List<DonThuBean> getDonThuDaChuyenDonViKhac(FilterDonDaChuyenDiBean modelFilter,int status) throws Exception
	{
		String strListUser = "";
		for(long l: UserLocalServiceUtil.getOrganizationUserIds(SessionUtil.getOrgId()))
		{
			strListUser += l+","; 
		}
		strListUser = strListUser.substring(0,strListUser.length()-1);
		List<DonThuBean> list = new ArrayList<DonThuBean>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu WHERE 1 = 1";
		sql+=returnDonThuTypeCondition(status);
		sql+=" AND ( NoiDungDonThu LIKE ?";
		sql+=" OR IdDonThu IN ( SELECT IdDonThu FROM don_thu_nguoi_dai_dien WHERE IdDoiTuongDiKNTC IN (SELECT IdDoiTuongDiKNTC FROM don_thu_doi_tuong_di_kntc WHERE HoTen LIKE ?))";
		sql+=" OR IdDoiTuongBiKNTC IN ( SELECT IdDoiTuongBiKNTC FROM don_thu_doi_tuong_bi_kntc WHERE HoTen LIKE ? OR TenCoQuan LIKE ?))";
		if(modelFilter.getNgayChuyenStart()!=null)
		{
			sql+=" AND IdDonThu IN ( SELECT IdDonThu FROM don_thu_thong_tin_don_thu WHERE OrgNhan != OrgChuyen AND OrgChuyen = "+SessionUtil.getOrgId()+" AND (NgayNhan BETWEEN '"+sdfDateSql.format(modelFilter.getNgayChuyenStart())+"' AND '"+sdfDateSql.format(modelFilter.getNgayChuyenEnd())+"'))";
		}
		if(modelFilter.getLoaiDonThu()!=0)
			sql+=" AND LoaiDonThu = "+modelFilter.getLoaiDonThu();
		sql+=" ORDER BY NgayNhapDon DESC";
		System.out.println(sql);
		PreparedStatement preSt = con.prepareStatement(sql);
		preSt.setNString(1, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(2, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(3, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(4, "%"+modelFilter.getKeyWord()+"%");
		ResultSet rs = preSt.executeQuery();
		while(rs.next())
		{
			list.add(returnDonThuBean(rs));
		}
		rs.close();
		con.close();

		return list;
	}
	
	public List<DonThuBean> getDonThuDaChuyenDonViKhac(FilterDonDaChuyenDiBean modelFilter) throws Exception
	{
		String strListUser = "";
		for(long l: UserLocalServiceUtil.getOrganizationUserIds(SessionUtil.getOrgId()))
		{
			strListUser += l+","; 
		}
		strListUser = strListUser.substring(0,strListUser.length()-1);
		List<DonThuBean> list = new ArrayList<DonThuBean>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu WHERE 1 = 1";
		sql+=returnDonThuTypeCondition(LoaiTinhTrangDonThuEnum.chuyendonauto.getType());
		sql+=" AND ( NoiDungDonThu LIKE ?";
		sql+=" OR IdDonThu IN ( SELECT IdDonThu FROM don_thu_nguoi_dai_dien WHERE IdDoiTuongDiKNTC IN (SELECT IdDoiTuongDiKNTC FROM don_thu_doi_tuong_di_kntc WHERE HoTen LIKE ?))";
		sql+=" OR IdDoiTuongBiKNTC IN ( SELECT IdDoiTuongBiKNTC FROM don_thu_doi_tuong_bi_kntc WHERE HoTen LIKE ? OR TenCoQuan LIKE ?))";
		if(modelFilter.getNgayChuyenStart()!=null)
		{
			sql+=" AND IdDonThu IN ( SELECT IdDonThu FROM don_thu_thong_tin_don_thu WHERE OrgNhan != OrgChuyen AND OrgChuyen = "+SessionUtil.getOrgId()+" AND (NgayNhan BETWEEN '"+sdfDateSql.format(modelFilter.getNgayChuyenStart())+"' AND '"+sdfDateSql.format(modelFilter.getNgayChuyenEnd())+"'))";
		}
		if(modelFilter.getLoaiDonThu()!=0)
			sql+=" AND LoaiDonThu = "+modelFilter.getLoaiDonThu();
		sql+=" ORDER BY NgayNhapDon DESC";
		System.out.println(sql);
		PreparedStatement preSt = con.prepareStatement(sql);
		preSt.setNString(1, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(2, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(3, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(4, "%"+modelFilter.getKeyWord()+"%");
		ResultSet rs = preSt.executeQuery();
		while(rs.next())
		{
			list.add(returnDonThuBean(rs));
		}
		rs.close();
		con.close();

		return list;
	}

	public List<DonThuBean> getDonThuDaCoKetQuaXuLy(FilterDonDaCoKetQuaBean modelFilter) throws Exception
	{
		List<DonThuBean> list = new ArrayList<DonThuBean>();
		Connection con = DatabaseServices.getConnection();
		String conditionKQXL = "";
		String sql = "SELECT * FROM don_thu WHERE 1 = 1";
		sql+=returnDonThuTypeCondition(LoaiTinhTrangDonThuEnum.dacokq.getType());
		sql+=" AND ( NoiDungDonThu LIKE ?";
		sql+=" OR IdDonThu IN ( SELECT IdDonThu FROM don_thu_nguoi_dai_dien WHERE IdDoiTuongDiKNTC IN (SELECT IdDoiTuongDiKNTC FROM don_thu_doi_tuong_di_kntc WHERE HoTen LIKE ?))";
		sql+=" OR IdDoiTuongBiKNTC IN ( SELECT IdDoiTuongBiKNTC FROM don_thu_doi_tuong_bi_kntc WHERE HoTen LIKE ? OR TenCoQuan LIKE ?))";
		if(modelFilter.getNgayXuLyStart()!=null)
		{
			conditionKQXL+=" AND (NgayXuLy BETWEEN '"+sdfDateSql.format(modelFilter.getNgayXuLyStart())+"' AND '"+sdfDateSql.format(modelFilter.getNgayXuLyEnd())+"')";
		}
 		if(modelFilter.getHuongXuLy()!=0)
		{
			conditionKQXL+=" AND MaHuongXuLy = "+modelFilter.getHuongXuLy();
		}
		if(!conditionKQXL.isEmpty())
		{
			sql+=" AND IdDonThu IN (SELECT IdDonThu FROm don_thu_ket_qua_xu_ly WHERE OrgTao="+SessionUtil.getOrgId()+" "+conditionKQXL+")";
		}
		if(modelFilter.getHanXuLy()==HanXuLyDaXuLyEnum.xulytronghan.getType())
		{
			sql+=" AND IdDonThu IN (SELECT kqxl.IdDonThu FROM don_thu_thong_tin_don_thu ttdt,don_thu_ket_qua_xu_ly kqxl WHERE ttdt.IdDonThu = kqxl.IdDonThu AND OrgNhan = "+SessionUtil.getOrgId()+" AND DATEDIFF(NgayXuLy,NgayNhan) <= "+Integer.parseInt(KNTCProps.getProperty("han.xu.ly"))+")";
		}else if(modelFilter.getHanXuLy()==HanXuLyDaXuLyEnum.xulyquahan.getType())
		{
			sql+=" AND IdDonThu IN (SELECT kqxl.IdDonThu FROM don_thu_thong_tin_don_thu ttdt,don_thu_ket_qua_xu_ly kqxl WHERE ttdt.IdDonThu = kqxl.IdDonThu AND OrgNhan = "+SessionUtil.getOrgId()+" AND DATEDIFF(NgayXuLy,NgayNhan) > "+Integer.parseInt(KNTCProps.getProperty("han.xu.ly"))+")";
		}
		if(modelFilter.getLoaiDonThu()!=0)
			sql+=" AND LoaiDonThu = "+modelFilter.getLoaiDonThu();

		sql+=" ORDER BY NgayNhapDon DESC";
		System.out.println(sql);
		PreparedStatement preSt = con.prepareStatement(sql);
		preSt.setNString(1, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(2, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(3, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(4, "%"+modelFilter.getKeyWord()+"%");
		ResultSet rs = preSt.executeQuery();
		while(rs.next())
		{
			list.add(returnDonThuBean(rs));
		}
		rs.close();
		con.close();

		return list;
	}

	public List<DonThuBean> getDonThuCanThuLy(FilterDonCanThuLyBean modelFilter) throws Exception
	{
		List<DonThuBean> list = new ArrayList<DonThuBean>();
		Connection con = DatabaseServices.getConnection();
		String conditionKQXL = "";
		String sql = "SELECT * FROM don_thu WHERE 1 = 1";
		sql+=returnDonThuTypeCondition(LoaiTinhTrangDonThuEnum.canthuly.getType());
		sql+=" AND ( NoiDungDonThu LIKE ?";
		sql+=" OR IdDonThu IN ( SELECT IdDonThu FROM don_thu_nguoi_dai_dien WHERE IdDoiTuongDiKNTC IN (SELECT IdDoiTuongDiKNTC FROM don_thu_doi_tuong_di_kntc WHERE HoTen LIKE ?))";
		sql+=" OR IdDoiTuongBiKNTC IN ( SELECT IdDoiTuongBiKNTC FROM don_thu_doi_tuong_bi_kntc WHERE HoTen LIKE ? OR TenCoQuan LIKE ?))";

		if(modelFilter.getNgayXuLyStart()!=null)
		{
			conditionKQXL+=" (NgayXuLy BETWEEN '"+sdfDateSql.format(modelFilter.getNgayXuLyStart())+"' AND '"+sdfDateSql.format(modelFilter.getNgayXuLyEnd())+"')";
		}
		if(conditionKQXL.isEmpty())
			conditionKQXL+=" MaHuongXuLy = 4";
		else
			conditionKQXL+=" AND MaHuongXuLy = 4";
		if(!conditionKQXL.isEmpty())
		{
			sql+=" AND IdDonThu IN (SELECT IdDonThu FROm don_thu_ket_qua_xu_ly WHERE "+conditionKQXL+")";
		}
		if(modelFilter.getLoaiDonThu()!=0)
			sql+=" AND LoaiDonThu = "+modelFilter.getLoaiDonThu();

		sql+=" ORDER BY NgayNhapDon DESC";
		System.out.println(sql);
		PreparedStatement preSt = con.prepareStatement(sql);
		preSt.setNString(1, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(2, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(3, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(4, "%"+modelFilter.getKeyWord()+"%");
		ResultSet rs = preSt.executeQuery();
		while(rs.next())
		{
			list.add(returnDonThuBean(rs));
		}
		rs.close();
		con.close();

		return list;
	}

	public List<DonThuBean> getDonThuDaThuLy(FilterDonDaThuLyBean modelFilter) throws Exception
	{
		List<DonThuBean> list = new ArrayList<DonThuBean>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu WHERE 1 = 1";
		sql+=returnDonThuTypeCondition(LoaiTinhTrangDonThuEnum.dathuly.getType());
		sql+=" AND ( NoiDungDonThu LIKE ?";
		sql+=" OR IdDonThu IN ( SELECT IdDonThu FROM don_thu_nguoi_dai_dien WHERE IdDoiTuongDiKNTC IN (SELECT IdDoiTuongDiKNTC FROM don_thu_doi_tuong_di_kntc WHERE HoTen LIKE ?))";
		sql+=" OR IdDoiTuongBiKNTC IN ( SELECT IdDoiTuongBiKNTC FROM don_thu_doi_tuong_bi_kntc WHERE HoTen LIKE ? OR TenCoQuan LIKE ?))";
		if(modelFilter.getNgayThuLyStart()!=null)
			sql+=" AND (NgayThuLy BETWEEN '"+sdfDateSql.format(modelFilter.getNgayThuLyStart())+"' AND '"+sdfDateSql.format(modelFilter.getNgayThuLyEnd())+"')";
		if(modelFilter.getHanGiaiQuyetStart()!=null)
			sql+=" AND (NgayHanGiaiQuyet BETWEEN '"+sdfDateSql.format(modelFilter.getHanGiaiQuyetStart())+"' AND '"+sdfDateSql.format(modelFilter.getHanGiaiQuyetEnd())+"')";
		if(modelFilter.getLoaiDonThu()!=0)
			sql+=" AND LoaiDonThu = "+modelFilter.getLoaiDonThu();
		sql+=" ORDER BY NgayNhapDon DESC";
		System.out.println(sql);
		PreparedStatement preSt = con.prepareStatement(sql);
		preSt.setNString(1, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(2, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(3, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(4, "%"+modelFilter.getKeyWord()+"%");
		ResultSet rs = preSt.executeQuery();
		while(rs.next())
		{
			list.add(returnDonThuBean(rs));
		}
		rs.close();
		con.close();

		return list;
	}

	public List<DonThuBean> getDonThuCanGiaiQuyet(FilterDonCanGiaiQuyetBean modelFilter) throws Exception
	{
		List<DonThuBean> list = new ArrayList<DonThuBean>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu WHERE 1 = 1";
		sql+=returnDonThuTypeCondition(LoaiTinhTrangDonThuEnum.cangiaiquyet.getType());
		sql+=" AND ( NoiDungDonThu LIKE ?";
		sql+=" OR IdDonThu IN ( SELECT IdDonThu FROM don_thu_nguoi_dai_dien WHERE IdDoiTuongDiKNTC IN (SELECT IdDoiTuongDiKNTC FROM don_thu_doi_tuong_di_kntc WHERE HoTen LIKE ?))";
		sql+=" OR IdDoiTuongBiKNTC IN ( SELECT IdDoiTuongBiKNTC FROM don_thu_doi_tuong_bi_kntc WHERE HoTen LIKE ? OR TenCoQuan LIKE ?))";
		if(modelFilter.getNgayThuLyStart()!=null)
			sql+=" AND (NgayThuLy BETWEEN '"+sdfDateSql.format(modelFilter.getNgayThuLyStart())+"' AND '"+sdfDateSql.format(modelFilter.getNgayThuLyEnd())+"')";
		if(modelFilter.getHanGiaiQuyetStart()!=null)
			sql+=" AND (NgayHanGiaiQuyet BETWEEN '"+sdfDateSql.format(modelFilter.getHanGiaiQuyetStart())+"' AND '"+sdfDateSql.format(modelFilter.getHanGiaiQuyetEnd())+"')";
		if(modelFilter.getLoaiDonThu()!=0)
			sql+=" AND LoaiDonThu = "+modelFilter.getLoaiDonThu();
		sql+=" ORDER BY NgayNhapDon DESC";
		System.out.println(sql);
		PreparedStatement preSt = con.prepareStatement(sql);
		preSt.setNString(1, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(2, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(3, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(4, "%"+modelFilter.getKeyWord()+"%");
		ResultSet rs = preSt.executeQuery();
		while(rs.next())
		{
			list.add(returnDonThuBean(rs));
		}
		rs.close();
		con.close();

		return list;
	}

	public List<DonThuBean> getDonThuDangThiHanh(FilterDonDangThiHanhBean modelFilter) throws Exception
	{
		List<DonThuBean> list = new ArrayList<DonThuBean>();
		Connection con = DatabaseServices.getConnection();
		String conditionKQGQ = "";
		String sql = "SELECT * FROM don_thu WHERE 1 = 1";
		sql+=returnDonThuTypeCondition(LoaiTinhTrangDonThuEnum.dangthihanh.getType());
		sql+=" AND ( NoiDungDonThu LIKE ?";
		sql+=" OR IdDonThu IN ( SELECT IdDonThu FROM don_thu_nguoi_dai_dien WHERE IdDoiTuongDiKNTC IN (SELECT IdDoiTuongDiKNTC FROM don_thu_doi_tuong_di_kntc WHERE HoTen LIKE ?))";
		sql+=" OR IdDoiTuongBiKNTC IN ( SELECT IdDoiTuongBiKNTC FROM don_thu_doi_tuong_bi_kntc WHERE HoTen LIKE ? OR TenCoQuan LIKE ?))";
		if(modelFilter.getNgayGiaiQuyetStart()!=null)
			sql+=" AND (NgayHoanThanhGiaiQuyet BETWEEN '"+sdfDateSql.format(modelFilter.getNgayGiaiQuyetStart())+"' AND '"+sdfDateSql.format(modelFilter.getNgayGiaiQuyetEnd())+"')";

		if(modelFilter.getKetQuaGiaiQuyet()!=0)
			conditionKQGQ+= " IDKetQuaGiaiQuyet = "+modelFilter.getKetQuaGiaiQuyet();

		if(modelFilter.getHinhThucGiaiQuyet()!=0)
		{
			if(conditionKQGQ.isEmpty())
				conditionKQGQ+=" IDHinhThucGiaiQuyet = "+modelFilter.getHinhThucGiaiQuyet();
			else
				conditionKQGQ+=" AND IDHinhThucGiaiQuyet = "+modelFilter.getHinhThucGiaiQuyet();
		}
		if(!conditionKQGQ.isEmpty())
		{
			sql+=" AND IdDonThu IN (SELECT IdDonThu FROM don_thu_quyet_dinh_giai_quyet WHERE "+conditionKQGQ+")";
		}
		if(modelFilter.getLoaiDonThu()!=0)
			sql+=" AND LoaiDonThu = "+modelFilter.getLoaiDonThu();
		sql+=" ORDER BY NgayNhapDon DESC";
		System.out.println(sql);
		PreparedStatement preSt = con.prepareStatement(sql);
		preSt.setNString(1, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(2, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(3, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(4, "%"+modelFilter.getKeyWord()+"%");
		ResultSet rs = preSt.executeQuery();
		while(rs.next())
		{
			list.add(returnDonThuBean(rs));
		}
		rs.close();
		con.close();

		return list;
	}

	public List<DonThuBean> getDonThuDaKetThuc(FilterDonDangThiHanhBean modelFilter) throws Exception
	{
		List<DonThuBean> list = new ArrayList<DonThuBean>();
		Connection con = DatabaseServices.getConnection();
		String conditionKQGQ = "";
		String sql = "SELECT * FROM don_thu WHERE 1 = 1";
		sql+=returnDonThuTypeCondition(LoaiTinhTrangDonThuEnum.daketthuc.getType());
		sql+=" AND ( NoiDungDonThu LIKE ?";
		sql+=" OR IdDonThu IN ( SELECT IdDonThu FROM don_thu_nguoi_dai_dien WHERE IdDoiTuongDiKNTC IN (SELECT IdDoiTuongDiKNTC FROM don_thu_doi_tuong_di_kntc WHERE HoTen LIKE ?))";
		sql+=" OR IdDoiTuongBiKNTC IN ( SELECT IdDoiTuongBiKNTC FROM don_thu_doi_tuong_bi_kntc WHERE HoTen LIKE ? OR TenCoQuan LIKE ?))";

		if(modelFilter.getNgayGiaiQuyetStart()!=null)
			sql+=" AND (NgayHoanThanhGiaiQuyet BETWEEN '"+sdfDateSql.format(modelFilter.getNgayGiaiQuyetStart())+"' AND '"+sdfDateSql.format(modelFilter.getNgayGiaiQuyetEnd())+"')";

		if(modelFilter.getKetQuaGiaiQuyet()!=0)
			conditionKQGQ+= " IDKetQuaGiaiQuyet = "+modelFilter.getKetQuaGiaiQuyet();

		if(modelFilter.getHinhThucGiaiQuyet()!=0)
		{
			if(conditionKQGQ.isEmpty())
				conditionKQGQ+=" IDHinhThucGiaiQuyet = "+modelFilter.getHinhThucGiaiQuyet();
			else
				conditionKQGQ+=" AND IDHinhThucGiaiQuyet = "+modelFilter.getHinhThucGiaiQuyet();
		}
		if(!conditionKQGQ.isEmpty())
		{
			sql+=" AND IdDonThu IN (SELECT IdDonThu FROM don_thu_quyet_dinh_giai_quyet WHERE "+conditionKQGQ+")";
		}
		if(modelFilter.getLoaiDonThu()!=0)
			sql+=" AND LoaiDonThu = "+modelFilter.getLoaiDonThu();
		sql+=" ORDER BY NgayNhapDon DESC";
		System.out.println(sql);
		PreparedStatement preSt = con.prepareStatement(sql);
		preSt.setNString(1, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(2, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(3, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(4, "%"+modelFilter.getKeyWord()+"%");
		ResultSet rs = preSt.executeQuery();
		while(rs.next())
		{
			list.add(returnDonThuBean(rs));
		}
		rs.close();
		con.close();

		return list;
	}

	public List<DonThuBean> getDonThuPhucTapKeoDai(FilterDonDangThiHanhBean modelFilter) throws Exception
	{
		List<DonThuBean> list = new ArrayList<DonThuBean>();
		Connection con = DatabaseServices.getConnection();
		String conditionKQGQ = "";
		String sql = "SELECT * FROM don_thu WHERE 1 = 1";
		sql+=returnDonThuTypeCondition(LoaiTinhTrangDonThuEnum.phuctapkeodai.getType());
		sql+=" AND ( NoiDungDonThu LIKE ?";
		sql+=" OR IdDonThu IN ( SELECT IdDonThu FROM don_thu_nguoi_dai_dien WHERE IdDoiTuongDiKNTC IN (SELECT IdDoiTuongDiKNTC FROM don_thu_doi_tuong_di_kntc WHERE HoTen LIKE ?))";
		sql+=" OR IdDoiTuongBiKNTC IN ( SELECT IdDoiTuongBiKNTC FROM don_thu_doi_tuong_bi_kntc WHERE HoTen LIKE ? OR TenCoQuan LIKE ?))";

		if(modelFilter.getNgayGiaiQuyetStart()!=null)
			sql+=" AND (NgayHoanThanhGiaiQuyet BETWEEN '"+sdfDateSql.format(modelFilter.getNgayGiaiQuyetStart())+"' AND '"+sdfDateSql.format(modelFilter.getNgayGiaiQuyetEnd())+"')";

		if(modelFilter.getKetQuaGiaiQuyet()!=0)
			conditionKQGQ+= " IDKetQuaGiaiQuyet = "+modelFilter.getKetQuaGiaiQuyet();

		if(modelFilter.getHinhThucGiaiQuyet()!=0)
		{
			if(conditionKQGQ.isEmpty())
				conditionKQGQ+=" IDHinhThucGiaiQuyet = "+modelFilter.getHinhThucGiaiQuyet();
			else
				conditionKQGQ+=" AND IDHinhThucGiaiQuyet = "+modelFilter.getHinhThucGiaiQuyet();
		}
		if(!conditionKQGQ.isEmpty())
		{
			sql+=" AND IdDonThu IN (SELECT IdDonThu FROM don_thu_quyet_dinh_giai_quyet WHERE "+conditionKQGQ+")";
		}
		if(modelFilter.getLoaiDonThu()!=0)
			sql+=" AND LoaiDonThu = "+modelFilter.getLoaiDonThu();
		sql+=" ORDER BY NgayNhapDon DESC";
		System.out.println(sql);
		PreparedStatement preSt = con.prepareStatement(sql);
		preSt.setNString(1, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(2, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(3, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(4, "%"+modelFilter.getKeyWord()+"%");
		ResultSet rs = preSt.executeQuery();
		while(rs.next())
		{
			list.add(returnDonThuBean(rs));
		}
		rs.close();
		con.close();

		return list;
	}
	
	public List<DonThuBean> getDonThuDaThuHoi(FilterDonDuocRut modelFilter) throws Exception
	{
		List<DonThuBean> list = new ArrayList<DonThuBean>();
		Connection con = DatabaseServices.getConnection();
		String conditionKQXL = "";
		String sql = "SELECT * FROM don_thu WHERE 1 = 1";
		sql+=returnDonThuTypeCondition(LoaiTinhTrangDonThuEnum.donduocrut.getType());
		sql+=" AND ( NoiDungDonThu LIKE ?";
		sql+=" OR IdDonThu IN ( SELECT IdDonThu FROM don_thu_nguoi_dai_dien WHERE IdDoiTuongDiKNTC IN (SELECT IdDoiTuongDiKNTC FROM don_thu_doi_tuong_di_kntc WHERE HoTen LIKE ?))";
		sql+=" OR IdDoiTuongBiKNTC IN ( SELECT IdDoiTuongBiKNTC FROM don_thu_doi_tuong_bi_kntc WHERE HoTen LIKE ? OR TenCoQuan LIKE ?))";
 		if(modelFilter.getHuongXuLy()!=0)
		{
			conditionKQXL+=" AND MaHuongXuLy = "+modelFilter.getHuongXuLy();
		}
		if(!conditionKQXL.isEmpty())
		{
			sql+=" AND IdDonThu IN (SELECT IdDonThu FROm don_thu_ket_qua_xu_ly WHERE OrgTao="+SessionUtil.getOrgId()+" "+conditionKQXL+")";
		}
		if(modelFilter.getLoaiDonThu()!=0)
			sql+=" AND LoaiDonThu = "+modelFilter.getLoaiDonThu();

		sql+=" ORDER BY NgayNhapDon DESC";
		System.out.println(sql);
		PreparedStatement preSt = con.prepareStatement(sql);
		preSt.setNString(1, "%"+modelFilter.getKeyword()+"%");
		preSt.setNString(2, "%"+modelFilter.getKeyword()+"%");
		preSt.setNString(3, "%"+modelFilter.getKeyword()+"%");
		preSt.setNString(4, "%"+modelFilter.getKeyword()+"%");
		ResultSet rs = preSt.executeQuery();
		while(rs.next())
		{
			list.add(returnDonThuBean(rs));
		}
		rs.close();
		con.close();

		return list;
	}
	
	public List<DonThuBean> getDonThuThamMuuChuaGiaiQuyet(FilterDonCanGiaiQuyetBean modelFilter) throws Exception
	{
		List<DonThuBean> list = new ArrayList<DonThuBean>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu WHERE 1 = 1";
		sql+=returnDonThuTypeCondition(LoaiTinhTrangDonThuEnum.thammuuchuagiaiquyet.getType());
		sql+=" AND ( NoiDungDonThu LIKE ?";
		sql+=" OR IdDonThu IN ( SELECT IdDonThu FROM don_thu_nguoi_dai_dien WHERE IdDoiTuongDiKNTC IN (SELECT IdDoiTuongDiKNTC FROM don_thu_doi_tuong_di_kntc WHERE HoTen LIKE ?))";
		sql+=" OR IdDoiTuongBiKNTC IN ( SELECT IdDoiTuongBiKNTC FROM don_thu_doi_tuong_bi_kntc WHERE HoTen LIKE ? OR TenCoQuan LIKE ?))";
		if(modelFilter.getNgayThuLyStart()!=null)
			sql+=" AND (NgayThuLy BETWEEN '"+sdfDateSql.format(modelFilter.getNgayThuLyStart())+"' AND '"+sdfDateSql.format(modelFilter.getNgayThuLyEnd())+"')";
		if(modelFilter.getHanGiaiQuyetStart()!=null)
			sql+=" AND (NgayHanGiaiQuyet BETWEEN '"+sdfDateSql.format(modelFilter.getHanGiaiQuyetStart())+"' AND '"+sdfDateSql.format(modelFilter.getHanGiaiQuyetEnd())+"')";
		if(modelFilter.getLoaiDonThu()!=0)
			sql+=" AND LoaiDonThu = "+modelFilter.getLoaiDonThu();
		sql+=" ORDER BY NgayNhapDon DESC";
		System.out.println(sql);
		PreparedStatement preSt = con.prepareStatement(sql);
		preSt.setNString(1, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(2, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(3, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(4, "%"+modelFilter.getKeyWord()+"%");
		ResultSet rs = preSt.executeQuery();
		while(rs.next())
		{
			list.add(returnDonThuBean(rs));
		}
		rs.close();
		con.close();

		return list;
	}
	
	public List<DonThuBean> getDonThuThamMuuDaGiaiQuyet(FilterDonDangThiHanhBean modelFilter) throws Exception
	{
		List<DonThuBean> list = new ArrayList<DonThuBean>();
		Connection con = DatabaseServices.getConnection();
		String conditionKQGQ = "";
		String sql = "SELECT * FROM don_thu WHERE 1 = 1";
		sql+=returnDonThuTypeCondition(LoaiTinhTrangDonThuEnum.thammuudagiaiquyet.getType());
		sql+=" AND ( NoiDungDonThu LIKE ?";
		sql+=" OR IdDonThu IN ( SELECT IdDonThu FROM don_thu_nguoi_dai_dien WHERE IdDoiTuongDiKNTC IN (SELECT IdDoiTuongDiKNTC FROM don_thu_doi_tuong_di_kntc WHERE HoTen LIKE ?))";
		sql+=" OR IdDoiTuongBiKNTC IN ( SELECT IdDoiTuongBiKNTC FROM don_thu_doi_tuong_bi_kntc WHERE HoTen LIKE ? OR TenCoQuan LIKE ?))";
		if(modelFilter.getNgayGiaiQuyetStart()!=null)
			sql+=" AND (NgayHoanThanhGiaiQuyet BETWEEN '"+sdfDateSql.format(modelFilter.getNgayGiaiQuyetStart())+"' AND '"+sdfDateSql.format(modelFilter.getNgayGiaiQuyetEnd())+"')";

		if(modelFilter.getKetQuaGiaiQuyet()!=0)
			conditionKQGQ+= " IDKetQuaGiaiQuyet = "+modelFilter.getKetQuaGiaiQuyet();

		if(modelFilter.getHinhThucGiaiQuyet()!=0)
		{
			if(conditionKQGQ.isEmpty())
				conditionKQGQ+=" IDHinhThucGiaiQuyet = "+modelFilter.getHinhThucGiaiQuyet();
			else
				conditionKQGQ+=" AND IDHinhThucGiaiQuyet = "+modelFilter.getHinhThucGiaiQuyet();
		}
		if(!conditionKQGQ.isEmpty())
		{
			sql+=" AND IdDonThu IN (SELECT IdDonThu FROM don_thu_quyet_dinh_giai_quyet WHERE "+conditionKQGQ+")";
		}
		if(modelFilter.getLoaiDonThu()!=0)
			sql+=" AND LoaiDonThu = "+modelFilter.getLoaiDonThu();
		sql+=" ORDER BY NgayNhapDon DESC";
		System.out.println(sql);
		PreparedStatement preSt = con.prepareStatement(sql);
		preSt.setNString(1, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(2, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(3, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(4, "%"+modelFilter.getKeyWord()+"%");
		ResultSet rs = preSt.executeQuery();
		while(rs.next())
		{
			list.add(returnDonThuBean(rs));
		}
		rs.close();
		con.close();

		return list;
	}

	public static List<DoiTuongDiKNTCBean> getNguoiDaiDienDonThu(int idDonThu) throws Exception
	{
		List<DoiTuongDiKNTCBean> list = new ArrayList<DoiTuongDiKNTCBean>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu_doi_tuong_di_kntc WHERE IdDoiTuongDiKNTC IN (SELECT IdDoiTuongDiKNTC FROM don_thu_nguoi_dai_dien WHERE IdDonThu = "+idDonThu+")";
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

	public List<String> getLinhVucList(int idDonThu) throws Exception
	{
		List<String> list = new ArrayList<String>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT ma FROM linh_vuc WHERE ma IN (SELECT IdLinhVuc FROM don_thu_linh_vuc WHERE IdDonThu = "+idDonThu+")";
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			list.add(rs.getString("ma"));
		}
		rs.close();
		con.close();

		return list;
	}

	public List<HoSoDinhKemBean> getDinhKemHoSoList(int idDonThu) throws Exception
	{
		List<HoSoDinhKemBean> list = new ArrayList<HoSoDinhKemBean>();
		Connection con = DatabaseServices.getConnection();

		String sql = "SELECT * FROM don_thu_ho_so_dinh_kem WHERE IDDonThu = "+idDonThu;
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			HoSoDinhKemBean model = returnHoSoDinhKem(rs);
			list.add(model);
		}
		rs.close();
		con.close();
		return list;
	}
	
	public String getStringFieldValueOfDonThu(String fieldName,int idDonThu) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT "+fieldName+" FROM don_thu WHERE IdDonThu = "+idDonThu;
		ResultSet rs = con.createStatement().executeQuery(sql);
		rs.next();
		String result = rs.getString(fieldName);
		rs.close();
		con.close();
		return result;
	}
	
	public int getIntFieldValueOfDonThu(String fieldName,int idDonThu) throws SQLException
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT "+fieldName+" FROM don_thu WHERE IdDonThu = "+idDonThu;
		ResultSet rs = con.createStatement().executeQuery(sql);
		rs.next();
		int result = rs.getInt(fieldName);
		rs.close();
		con.close();
		return result;
	}
	
	public List<DonThuBean> exportDonThuDaCoKetQuaXuLy(String dateType,Date startDate,Date endDate) throws Exception
	{
		List<DonThuBean> list = new ArrayList<DonThuBean>();
		Connection con = DatabaseServices.getConnection();
		String strStartDate = sdfDateSql.format(startDate);
		String strEndDate = sdfDateSql.format(endDate);
		String sql = "SELECT * FROM don_thu WHERE 1 = 1";
		sql+=returnDonThuTypeCondition(LoaiTinhTrangDonThuEnum.dacokq.getType());
		if(dateType=="ngayxuly")
		{
			sql+=" AND IdDonThu IN (SELECT IdDonThu FROM don_thu_ket_qua_xu_ly WHERE ";
			sql+=" OrgTao = "+SessionUtil.getOrgId();
			sql+=" AND (NgayXuLy Between '"+strStartDate+"' AND '"+strEndDate+"')";
			sql+=")";
		}else if(dateType=="ngaynhandon")
		{
			sql+=" AND IdDonThu IN (SELECT IdDonThu FROM don_thu_thong_tin_don_thu WHERE";
			sql+=" OrgNhan = "+SessionUtil.getOrgId();
			sql+=" AND (NgayNhan Between '"+strStartDate+"' AND '"+strEndDate+"')";
			sql+=")";
		}

		sql+=" ORDER BY NgayNhapDon DESC";
		System.out.println(sql);
		PreparedStatement preSt = con.prepareStatement(sql);
		ResultSet rs = preSt.executeQuery();
		while(rs.next())
		{
			list.add(returnDonThuBean(rs));
		}
		rs.close();
		con.close();

		return list;
	}

	/* Search Section */
	public List<DoiTuongDiKNTCBean> searchNguoiDaiDien(String keyWord) throws Exception
	{
		List<DoiTuongDiKNTCBean> list = new ArrayList<DoiTuongDiKNTCBean>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu_doi_tuong_di_kntc WHERE NacDanh = 0 AND( SoDinhDanhCaNhan LIKE ? OR HoTen LIKE ?)";
		PreparedStatement preSt = con.prepareStatement(sql);
		preSt.setNString(1, "%"+keyWord+"%");
		preSt.setNString(2, "%"+keyWord+"%");
		ResultSet rs = preSt.executeQuery();
		while(rs.next())
		{
			DoiTuongDiKNTCBean model = DonThuServiceUtil.returnDoiTuongDiKNTCBean(rs);
			list.add(model);
		}
		rs.close();
		con.close();

		return list;
	}

	public List<DoiTuongBiKNTCBean> searchNguoiBiKNTC(String keyWord,int loaiNguoi) throws Exception
	{
		List<DoiTuongBiKNTCBean> list = new ArrayList<DoiTuongBiKNTCBean>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu_doi_tuong_bi_kntc";
		if(loaiNguoi==1)
		{
			sql+=  " WHERE SoDinhDanhCaNhan LIKE ? OR HoTen LIKE ?";
		}
		else
		{
			sql+=  " WHERE TenCoQuan LIKE ?";
		}
		PreparedStatement preSt = con.prepareStatement(sql);
		if(loaiNguoi==1)
		{
			preSt.setNString(1, "%"+keyWord+"%");
			preSt.setNString(2, "%"+keyWord+"%");
		}
		else
		{
			preSt.setNString(1, "%"+keyWord+"%");
		}
		ResultSet rs = preSt.executeQuery();
		while(rs.next())
		{
			DoiTuongBiKNTCBean model = DonThuServiceUtil.returnDoiTuongBiKNTCBean(rs);
			list.add(model);
		}
		rs.close();
		con.close();

		return list;
	}

	public List<DonThuBean> searchDonThu(String[] arrChuTheDonThu,String noiDungDonThu) throws Exception
	{
		List<DonThuBean> list = new ArrayList<DonThuBean>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu WHERE 1 = 1 AND GanVuViec IS NULL";
		if(!noiDungDonThu.isEmpty())
		{
			sql+=" AND NoiDungDonThu LIKE ?";
		}
		if(arrChuTheDonThu!=null)
		{
			sql+=" AND IdDonThu IN (SELECT DISTINCT IdDonThu FROM don_thu_nguoi_dai_dien WHERE IdDoiTuongDiKNTC IN(SELECT IdDoiTuongDiKNTC FROM don_thu_doi_tuong_di_kntc WHERE 1 = 1 AND (";
			for(int i = 0;i<arrChuTheDonThu.length;i++)
			{
				sql+=" HoTen LIKE ? ";
				if(i<arrChuTheDonThu.length-1)
					sql+=" OR ";
			}
			sql+=")))";
		}
		System.out.println(sql);
		PreparedStatement preSt = con.prepareStatement(sql); 
		if(!noiDungDonThu.trim().isEmpty())
		{
			preSt.setNString(1, "%"+noiDungDonThu+"%");
		}
		if(arrChuTheDonThu!=null)
		{
			int step = 1;
			if(!noiDungDonThu.trim().isEmpty())
				step = 2;
			for(int i = 0;i<arrChuTheDonThu.length;i++)
			{
				preSt.setNString(i+step, "%"+arrChuTheDonThu[i].trim()+"%");
			}
		}
		ResultSet rs = preSt.executeQuery();
		while(rs.next())
		{
			DonThuBean model = new DonThuBean();
			model = returnDonThuBean(rs);
			list.add(model);
		}
		rs.close();
		con.close();

		return list;
	}

	public List<DonThuBean> traCuuDonThu(TraCuuBean modelTraCuu) throws Exception
	{
		String thongTinDonCondition = "";
		List<DonThuBean> list = new ArrayList<DonThuBean>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu WHERE 1 = 1";
		if(modelTraCuu.getKeyWord()!=null)
		{
			sql+=" AND NoiDungDonThu LIKE N'%"+modelTraCuu.getKeyWord()+"%'";
		}
		if(modelTraCuu.getChuThe()!=null)
		{
			sql+=" AND IdDonThu IN ( SELECT IdDonThu FROM don_thu_nguoi_dai_dien WHERE IdDoiTuongDiKNTC IN (SELECT IdDoiTuongDiKNTC FROM don_thu_doi_tuong_di_kntc WHERE 1 = 1";
			DoiTuongDiKNTCBean modelChuThe = modelTraCuu.getChuThe();
			if(modelChuThe.getHoTen()!=null)
			{
				sql+= " AND HoTen Like N'%"+modelChuThe.getHoTen()+"%'";
			}
			if(modelChuThe.getGioiTinh()!=0)
			{
				sql+= " AND GioiTinh = "+modelChuThe.getGioiTinh();
			}
			if(modelChuThe.getSoDinhDanhCaNhan()!=null)
			{
				sql+= " AND SoDinhDanhCaNhan LIKE '%"+modelChuThe.getSoDinhDanhCaNhan()+"%'";
			}
			if(modelChuThe.getNgayCapSoDinhDanh()!=null)
			{
				sql+= " AND NgayCapSoDinhDanh = '"+sdfDateSql.format(modelChuThe.getNgayCapSoDinhDanh())+"'";
			}
			if(modelChuThe.getNoiCapSoDinhDanh()!=null)
			{
				sql+= " AND NoiCapSoDinhDanh LIKE N'%"+modelChuThe.getNoiCapSoDinhDanh()+"%'";
			}
			if(modelChuThe.getDiaChiChiTiet()!=null)
			{
				sql+= " AND DiaChiChiTiet  LIKE N'%"+modelChuThe.getDiaChiChiTiet()+"%'";
			}
			if(modelChuThe.getMaTinh()!=null)
			{
				sql+=" AND MaTinh = '"+modelChuThe.getMaTinh()+"'";
				if(modelChuThe.getMaHuyen()!=null)
				{
					sql+=" AND MaHuyen = '"+modelChuThe.getMaHuyen()+"'";
					if(modelChuThe.getMaXa()!=null)
					{
						sql+=" AND MaXa = '"+modelChuThe.getMaXa()+"'";
					}
				}
			}
			sql+="))";
		}
		if(modelTraCuu.getNguonDon()!=0 || modelTraCuu.isHasNgayNhap())
		{
			if(modelTraCuu.getNguonDon()!=0)
				thongTinDonCondition+=" AND NguonDonDen = "+modelTraCuu.getNguonDon();
			if(modelTraCuu.isHasNgayNhap())
				thongTinDonCondition+=" AND (NgayNhan BETWEEN '"+sdfDateSql.format(modelTraCuu.getNgayNhapStart())+"' AND '"+sdfDateSql.format(modelTraCuu.getNgayNhapEnd())+"')";
		}
		sql+="AND IdDonThu IN (SELECT IdDonThu FROM don_thu_thong_tin_don_thu WHERE OrgNhan = "+SessionUtil.getOrgId()+" "+thongTinDonCondition+")";
		if(modelTraCuu.getLoaiDonThu()!=0)
		{
			sql+=" AND LoaiDonThu = "+modelTraCuu.getLoaiDonThu();
			if(!modelTraCuu.getListLinhVuc().isEmpty())
			{
				String listLv = "";
				listLv = modelTraCuu.getListLinhVuc().toString();
				listLv = listLv.substring(1, listLv.length()-1);
				listLv = listLv.replace(", ", "','");
				listLv = "'"+listLv+"'";
				sql+= " AND IdDonThu IN (SELECT IdDonThu FROM don_thu_linh_vuc WHERE IdLinhVuc IN ("+listLv+"))";
			}
		}
		if(modelTraCuu.isHasNgayThuLy())
		{
			sql+=" AND (NgayThuLy BETWEEN '"+sdfDateSql.format(modelTraCuu.getNgayThuLyStart())+"' AND '"+sdfDateSql.format(modelTraCuu.getNgayThuLyEnd())+"')";
		}
		if(modelTraCuu.isHasNgayHanGiaiQuyet())
		{
			sql+=" AND (NgayHoanThanhGiaiQuyet BETWEEN '"+sdfDateSql.format(modelTraCuu.getNgayHanGQStart())+"' AND '"+sdfDateSql.format(modelTraCuu.getNgayHanGQEnd())+"')";
		}
		sql+=" ORDER BY NgayNhapDon DESC";
		System.out.println(sql);
		PreparedStatement preSt = con.prepareStatement(sql);
		ResultSet rs = preSt.executeQuery();
		while(rs.next())
		{
			DonThuBean model = new DonThuBean();
			model = returnDonThuBean(rs);
			list.add(model);
		}
		rs.close();
		con.close();

		return list;
	}
	
	public static boolean checkIfLinhVucHasChildren(String idLinhVuc) throws Exception
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT count(*) FROM linh_vuc WHERE ma LIKE '%"+idLinhVuc+"%'";
		System.out.println(sql);
		ResultSet rs = con.createStatement().executeQuery(sql);
		rs.next();
		int count = rs.getInt(1);
		rs.close();
		con.close();

		return count>1?true:false;
	}
	
	public static boolean checkIfDonViHasChildren(String idCoQuan) throws Exception
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT count(*) FROM co_quan WHERE ma LIKE '%"+idCoQuan+"%'";
		System.out.println(sql);
		ResultSet rs = con.createStatement().executeQuery(sql);
		rs.next();
		int count = rs.getInt(1);
		rs.close();
		con.close();

		return count>1?true:false;
	}

	public static int countDonThuOnTypeGeneral(int type) throws Exception
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT count(IdDonThu) FROM don_thu WHERE 1 = 1";
		if(type==LoaiTinhTrangDonThuEnum.datiepnhan.getType())
		{
			sql+=" AND IdDonThu IN (SELECT IdDonThu From don_thu_thong_tin_don_thu WHERE OrgChuyen = OrgNhan AND OrgNhan = "+SessionUtil.getOrgId()+")";
		}
		if(type==LoaiTinhTrangDonThuEnum.donvikhacchuyenden.getType())
		{
			sql+=" AND IdDonThu IN (SELECT IdDonThu From don_thu_thong_tin_don_thu WHERE OrgChuyen != OrgNhan AND OrgNhan = "+SessionUtil.getOrgId()+")";
		}
		sql+=returnDonThuTypeCondition(type);
		
		System.out.println(sql);
		ResultSet rs = con.createStatement().executeQuery(sql);
		rs.next();
		int count = rs.getInt(1);
		rs.close();
		con.close();

		return count;
	}

	public static int countAllDonThu() throws Exception
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT count(IdDonThu) FROM don_thu";
		System.out.println(sql);
		ResultSet rs = con.createStatement().executeQuery(sql);
		rs.next();
		int count = rs.getInt(1);
		rs.close();
		con.close();

		return count;
	}

	public static int countSoLanTrungDon(int idDonThu) throws Exception
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT count(*) FROM don_thu WHERE GanVuViec = "+idDonThu;
		System.out.println(sql);
		ResultSet rs = con.createStatement().executeQuery(sql);
		rs.next();
		int count = rs.getInt(1);
		rs.close();
		con.close();

		return count;
	}

	/* return Model Section */
	public static DoiTuongDiKNTCBean returnDoiTuongDiKNTCBean(ResultSet rs) throws Exception
	{
		DoiTuongDiKNTCBean model = new DoiTuongDiKNTCBean();
		Date ngayCap = null;

		if(rs.getString("NgayCapSoDinhDanh") != null)
			ngayCap = sdfDateSql.parse(rs.getString("NgayCapSoDinhDanh"));

		model.setMaDoiTuong(rs.getInt("IdDoiTuongDiKNTC"));
		model.setHoTen(rs.getString("HoTen"));
		model.setSoDinhDanhCaNhan(rs.getString("SoDinhDanhCaNhan"));
		model.setNgayCapSoDinhDanh(ngayCap);
		model.setNoiCapSoDinhDanh(rs.getString("NoiCapSoDinhDanh"));
		model.setGioiTinh(rs.getInt("GioiTinh"));
		model.setMaTinh(rs.getString("MaTinh"));
		model.setMaHuyen(rs.getString("MaHuyen"));
		model.setMaXa(rs.getString("MaXa"));
		model.setDiaChiChiTiet(rs.getString("DiaChiChiTiet"));
		model.setSoDienThoai(rs.getString("SoDienThoai"));
		model.setMaQuocTich(rs.getString("MaQuocTich"));
		model.setMaDanToc(rs.getInt("MaDanToc"));
		model.setNacDanh(rs.getInt("NacDanh")==1?true:false);

		return model;
	}

	public static DoiTuongBiKNTCBean returnDoiTuongBiKNTCBean(ResultSet rs) throws Exception
	{
		DoiTuongBiKNTCBean model = new DoiTuongBiKNTCBean();
		Date ngayCap =null;
		if(rs.getString("NgayCapSoDinhDanh")!=null)
			ngayCap = sdfDateSql.parse(rs.getString("NgayCapSoDinhDanh"));

		model.setMaDoiTuong(rs.getInt("IdDoiTuongBiKNTC"));
		model.setHoTen(rs.getString("HoTen"));
		model.setSoDinhDanhCaNhan(rs.getString("SoDinhDanhCaNhan"));
		model.setGioiTinh(rs.getInt("GioiTinh"));
		model.setNgayCapSoDinhDanh(ngayCap);
		model.setNoiCapSoDinhDanh(rs.getString("NoiCapSoDinhDanh"));
		model.setChucVu(rs.getString("ChucVu"));
		model.setNoiCongTac(rs.getString("NoiCongTac"));
		model.setMaTinh(rs.getString("MaTinh"));
		model.setMaHuyen(rs.getString("MaHuyen"));
		model.setMaXa(rs.getString("MaXa"));
		model.setDiaChiChiTiet(rs.getString("DiaChiChiTiet"));
		model.setMaQuocTich(rs.getString("MaQuocTich"));
		model.setMaDanToc(rs.getInt("MaDanToc"));
		model.setTenCoQuanToChuc(rs.getString("TenCoQuan"));
		model.setMaTinhCoQuan(rs.getString("MaTinhCoQuan"));
		model.setMaHuyenCoQuan(rs.getString("MaHuyenCoQuan"));
		model.setMaXaCoQuan(rs.getString("MaXaCoQuan"));
		model.setDiaChiChiTietCoQuan(rs.getString("DiaChiChiTietCoQuan"));

		return model;
	}

	public static DoiTuongUyQuyenBean returnDoiTuongUyQuyenBean(ResultSet rs) throws Exception
	{
		DoiTuongUyQuyenBean model = new DoiTuongUyQuyenBean();
		Date ngayCap =null;
		if(rs.getString("NgayCapSoDinhDanh")!=null)
			ngayCap = sdfDateSql.parse(rs.getString("NgayCapSoDinhDanh"));

		model.setMaNguoiUyQuyen(rs.getInt("IdDoiTuongUyQuyen"));
		model.setHoTen(rs.getString("HoTen"));
		model.setSoDinhDanhCaNhan(rs.getString("SoDinhDanhCaNhan"));
		model.setGioiTinh(rs.getInt("GioiTinh"));
		model.setNgayCapSoDinhDanh(ngayCap);
		model.setNoiCapSoDinhDanh(rs.getString("NoiCapSoDinhDanh"));
		model.setMaTinh(rs.getString("MaTinh"));
		model.setMaHuyen(rs.getString("MaHuyen"));
		model.setMaXa(rs.getString("MaXa"));
		model.setDiaChiChiTiet(rs.getString("DiaChiChiTiet"));
		model.setMaQuocTich(rs.getString("MaQuocTich"));
		model.setMaDanToc(rs.getInt("MaDanToc"));

		return model;
	}

	public static ThongTinDonThuBean returnThongTinDonThuBean(ResultSet rs) throws Exception
	{
		ThongTinDonThuBean model = new ThongTinDonThuBean();

		model.setMaThongTinDon(rs.getInt("IdThongTinDon"));
		model.setNguonDonDen(rs.getInt("NguonDonDen"));
		if(rs.getString("NgayHoanThanhXuLy")!=null)
			model.setNgayHoanThanhXuLy(sdfDateSql.parse(rs.getString("NgayHoanThanhXuLy")));
		model.setMaCoQuanChuyenDen(rs.getString("MaCoQuanChuyenDen"));
		model.setSoVanBanDen(rs.getString("SoVanBanDen"));
		if(rs.getString("NgayPhatHanhVanBan")!=null)
			model.setNgayPhatHanhVanBan(sdfDateSql.parse(rs.getString("NgayPhatHanhVanBan")));
		model.setOrgChuyen(rs.getLong("OrgChuyen"));
		model.setOrgNhan(rs.getLong("OrgNhan"));
		model.setNgayNhan(sdfDateSql.parse(rs.getString("NgayNhan")));
		model.setMaDonThu(rs.getInt("IdDonThu"));

		return model;
	}

	public static DonThuBean returnDonThuBean(ResultSet rs) throws SQLException
	{
		DonThuBean model = new DonThuBean();
		Timestamp timestamp;
		Date ngayNhanDon=null,ngayNhapDon = null, ngayThuLy = null, ngayHanGiaiQuyet = null, ngayHoanThanhGiaiQuyet = null, ngayTraKetQua = null, ngayCapNhat = null, ngayBanHanhQDGQ = null;
		timestamp = rs.getTimestamp("NgayNhanDon");
		if(timestamp!=null)
		{
			ngayNhanDon = new Date(timestamp.getTime());
		}
		timestamp = rs.getTimestamp("NgayNhapDon");
		if(timestamp!=null)
		{
			ngayNhapDon = new Date(timestamp.getTime());
		}
		timestamp = rs.getTimestamp("NgayThuLy");
		if(timestamp!=null)
		{
			ngayThuLy = new Date(timestamp.getTime());
		}
		timestamp = rs.getTimestamp("NgayHanGiaiQuyet");
		if(timestamp!=null)
		{
			ngayHanGiaiQuyet = new Date(timestamp.getTime());
		}
		timestamp = rs.getTimestamp("NgayHoanThanhGiaiQuyet");
		if(timestamp!=null)
		{
			ngayHoanThanhGiaiQuyet = new Date(timestamp.getTime());
		}
		timestamp = rs.getTimestamp("NgayTraKQ");
		if(timestamp!=null)
		{
			ngayTraKetQua = new Date(timestamp.getTime());
		}
		timestamp = rs.getTimestamp("NgayCapNhat");
		if(timestamp!=null)
		{
			ngayCapNhat = new Date(timestamp.getTime());
		}
		timestamp = rs.getTimestamp("NgayBanHanhQDGQ");
		if(timestamp!=null)
		{
			ngayBanHanhQDGQ = new Date(timestamp.getTime());
		}

		model.setMaDonThu(rs.getInt("IdDonThu"));
		model.setMaDoiTuongBiKNTC(rs.getInt("IdDoiTuongBiKNTC"));
		model.setMaDoiTuongUyQuyen(rs.getInt("IdDoiTuongUyQuyen"));
		model.setNoiDungDonThu(rs.getString("NoiDungDonThu"));
		model.setThamQuyenGiaiQuyet(rs.getString("ThamQuyenGiaiQuyet"));
		model.setLoaiDonThu(rs.getInt("LoaiDonThu"));
		model.setNgayNhanDon(ngayNhanDon);
		model.setNgayNhapDon(ngayNhapDon);
		model.setNgayThuLy(ngayThuLy);
		model.setNgayHanGiaiQuyet(ngayHanGiaiQuyet);
		model.setNgayHoanThanhGiaiQuyet(ngayHoanThanhGiaiQuyet);
		model.setNgayTraKetQua(ngayTraKetQua);
		model.setNgayCapNhat(ngayCapNhat);
		model.setLoaiNguoiDiKNTC(rs.getInt("LoaiNguoiDiKNTC"));
		model.setSoNguoiDiKNTC(rs.getInt("SoNguoiDiKNTC"));
		model.setSoNguoiDaiDien(rs.getInt("SoNguoiDaiDien"));
		model.setTenCoQuanDiKNTC(rs.getString("TenCoQuanDiKNTC"));
		model.setDiaChiCoQuanDiKNTC(rs.getString("DiaChiCoQuanDiKNTC"));
		model.setLoaiNguoiBiKNTC(rs.getInt("LoaiNguoiBiKNTC"));
		model.setLoaiNguoiUyQuyen(rs.getInt("LoaiNguoiUyQuyen"));
		model.setMaCoQuanDaGiaiQuyet(rs.getString("MaCoQuanDaGiaiQuyet"));
		model.setSoKyHieuVanBanGiaiQuyet(rs.getString("SoKyHieuVanBanGiaiQuyet"));
		model.setLanGiaiQuyet(rs.getInt("LanGiaiQuyet"));
		model.setNgayBanHanhQDGQ(ngayBanHanhQDGQ);
		model.setLoaiQuyetDinhGiaiQuyet(rs.getInt("LoaiQuyetDinhGiaiQuyet"));
		model.setTomTatNoiDungGiaiQuyet(rs.getString("TomTatNoiDungGiaiQuyet"));
		model.setUserThuLy(rs.getInt("UserThuLy"));
		model.setUserNhapDon(rs.getInt("UserNhapDon"));
		model.setDonNacDanh(rs.getInt("DonNacDanh")==1?true:false);
		model.setDonKhongDuDieuKienXuLy(rs.getInt("DonKhongDuDieuKienXL")==1?true:false);
		model.setGanVuViec(rs.getString("GanVuViec"));
		model.setIdHoSoLienThong(rs.getString("mattcp"));
		model.setStatus(rs.getInt("Status"));

		return model;
	}

	public HoSoDinhKemBean returnHoSoDinhKem(ResultSet rs) throws SQLException
	{
		HoSoDinhKemBean model = new HoSoDinhKemBean();
		model.setMaDinhKem(rs.getInt("IDHoSoDinhKem"));
		model.setTenHoSo(rs.getString("TenHoSo"));
		model.setNoiDungTomTat(rs.getString("NoiDungTomTat"));
		model.setLoaiHoSo(rs.getInt("LoaiHoSo"));
		model.setTenFileDinhKem(rs.getString("TenFileDinhKem"));
		model.setLinkFileDinhKem(rs.getString("LinkFileDinhKem"));
		model.setMaHoSo(rs.getInt("IDDonThu"));

		return model;
	}

	public static String returnDonThuTypeCondition(int type) throws Exception
	{ 
		String sql = "";
		if(type==LoaiTinhTrangDonThuEnum.datiepnhan.getType())
		{
			sql+=" AND Status = "+TinhTrangDonThu.dangthuchien.getType();
			sql+=" AND IdDonThu NOT IN (SELECT IdDonThu FROM don_thu_ket_qua_xu_ly)";
		}
		if(type==LoaiTinhTrangDonThuEnum.donvikhacchuyenden.getType())
		{
			sql+=" AND Status = "+TinhTrangDonThu.dangthuchien.getType();
			sql+=" AND IdDonThu NOT IN (SELECT IdDonThu FROM don_thu_ket_qua_xu_ly WHERE OrgTao IN ("+SessionUtil.getOrgId()+"))";
		}
		if(type==LoaiTinhTrangDonThuEnum.dacokq.getType())
		{
			sql+=" AND IdDonThu IN (SELECT IdDonThu FROM don_thu_ket_qua_xu_ly WHERE OrgTao = "+SessionUtil.getOrgId()+")";
		}
		if(type==LoaiTinhTrangDonThuEnum.canthuly.getType())
		{
			sql+=" AND IdDonThu IN (SELECT IdDonThu FROM don_thu_ket_qua_xu_ly WHERE MaHuongXuLy = 4 AND OrgTao = "+SessionUtil.getOrgId()+")";
			sql+=" AND IdDonThu NOT IN (SELECT IdDonThu FROM don_thu_quyet_dinh_thu_ly WHERE MaCoQuanThuLy = "+SessionUtil.getOrgId()+")";
		}
		if(type==LoaiTinhTrangDonThuEnum.dathuly.getType())
		{
			sql+=" AND IdDonThu IN (SELECT IdDonThu FROM don_thu_quyet_dinh_thu_ly WHERE MaCoQuanThuLy = "+SessionUtil.getOrgId()+")";
		}
		if(type==LoaiTinhTrangDonThuEnum.cangiaiquyet.getType())
		{
			sql+=" AND IdDonThu IN (SELECT IdDonThu FROM don_thu_quyet_dinh_thu_ly WHERE MaCoQuanThuLy = "+SessionUtil.getOrgId()+")";
			sql+=" AND IdDonThu NOT IN (SELECT IdDonThu From don_thu_quyet_dinh_giai_quyet)";
		}
		if(type==LoaiTinhTrangDonThuEnum.dangthihanh.getType())
		{
			sql+=" AND Status = "+TinhTrangDonThu.dangthuchien.getType();
			sql+=" AND IdDonThu IN(SELECT IdDonThu From don_thu_quyet_dinh_giai_quyet WHERE OrgTao ="+SessionUtil.getOrgId()+")";
		}
		if(type==LoaiTinhTrangDonThuEnum.daketthuc.getType())
		{
			sql+=" AND Status = "+TinhTrangDonThu.ketthuchoso.getType();
			sql+=" AND IdDonThu IN(SELECT IdDonThu From don_thu_quyet_dinh_giai_quyet WHERE OrgTao ="+SessionUtil.getOrgId()+")";
		}
		if(type==LoaiTinhTrangDonThuEnum.phuctapkeodai.getType())
		{
			sql+=" AND Status = "+TinhTrangDonThu.vuviecphuctap.getType();
			sql+=" AND IdDonThu IN(SELECT IdDonThu From don_thu_quyet_dinh_giai_quyet WHERE OrgTao ="+SessionUtil.getOrgId()+")";
		}
		if(type==LoaiTinhTrangDonThuEnum.donduocrut.getType())
		{
			sql+=" AND Status = "+TinhTrangDonThu.rutdon.getType();
			sql+=" AND IdDonThu IN(SELECT IdDonThu From don_thu_thong_tin_don_thu WHERE OrgNhan ="+SessionUtil.getOrgId()+")";
		}
		if(type==LoaiTinhTrangDonThuEnum.chuyendonchuagq.getType())
		{
			sql+=" AND IdDonThu IN (SELECT IdDonThu From don_thu_thong_tin_don_thu WHERE OrgChuyen != OrgNhan AND OrgChuyen = "+SessionUtil.getOrgId()+")";
			//sql+=" AND IdDonThu NOT IN (SELECT IdDonThu FROM don_thu_quyet_dinh_giai_quyet WHERE OrgTao != "+SessionUtil.getOrgId()+")";
		}
		if(type==LoaiTinhTrangDonThuEnum.chuyendondagq.getType())
		{
			sql+=" AND IdDonThu IN (SELECT IdDonThu From don_thu_thong_tin_don_thu WHERE OrgChuyen != OrgNhan AND OrgChuyen = "+SessionUtil.getOrgId()+")";
			sql+=" AND IdDonThu IN (SELECT IdDonThu FROM don_thu_quyet_dinh_giai_quyet WHERE OrgTao != "+SessionUtil.getOrgId()+")";
		}
		if(type==LoaiTinhTrangDonThuEnum.chuyendonluutru.getType())
		{
			sql+=" AND IdDonThu IN (SELECT IdDonThu FROM `don_thu_ket_qua_xu_ly` a WHERE OrgTao = "+SessionUtil.getOrgId()+" AND MaHuongXuLy = 5 AND ((SELECT count(IdDonThu) FROM don_thu_thong_tin_don_thu b WHERE b.IdDonThu = a.IdDonThu AND OrgChuyen = "+SessionUtil.getOrgId()+" AND OrgNhan != OrgChuyen) = 0))";
		}
		if(type==LoaiTinhTrangDonThuEnum.thammuuchuagiaiquyet.getType())
		{
			sql+=" AND IdDonThu IN (SELECT DISTINCT IdDonThu FROM don_thu_luan_chuyen WHERE UserGiao != UserNhan AND PhanCong = 1 AND UserNhan = "+SessionUtil.getUserId()+")";
			sql+=" AND IdDonThu NOT IN (SELECT IdDonThu From don_thu_quyet_dinh_giai_quyet)";
		}
		if(type==LoaiTinhTrangDonThuEnum.thammuudagiaiquyet.getType())
		{
			sql+=" AND IdDonThu IN (SELECT DISTINCT IdDonThu FROM don_thu_luan_chuyen WHERE UserGiao != UserNhan AND PhanCong = 1 AND UserNhan = "+SessionUtil.getUserId()+")";
			sql+=" AND IdDonThu IN (SELECT IdDonThu From don_thu_quyet_dinh_giai_quyet)";
		}

		return sql; 
	}
	
	public List<DonThuBean> getListDonThuChuyenDi(LoaiTinhTrangChuyenDiEnum type,FilterDonChuyenAutoBean modelFilter) throws Exception
	{
		List<DonThuBean> list = new ArrayList<DonThuBean>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM don_thu WHERE 1 = 1";
		
		sql+=returnChuyenDiCondition(type,modelFilter.getOrgDaChuyen());
		sql+=" AND ( NoiDungDonThu LIKE ?";
		sql+=" OR IdDonThu IN ( SELECT IdDonThu FROM don_thu_nguoi_dai_dien WHERE IdDoiTuongDiKNTC IN (SELECT IdDoiTuongDiKNTC FROM don_thu_doi_tuong_di_kntc WHERE HoTen LIKE ?))";
		sql+=" OR IdDoiTuongBiKNTC IN ( SELECT IdDoiTuongBiKNTC FROM don_thu_doi_tuong_bi_kntc WHERE HoTen LIKE ? OR TenCoQuan LIKE ?))";
		if(modelFilter.getLoaiDonThu()!=0)
			sql+=" AND LoaiDonThu = "+modelFilter.getLoaiDonThu();
		
		System.out.println(sql);
		
		PreparedStatement preSt = con.prepareStatement(sql);
		preSt.setNString(1, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(2, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(3, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(4, "%"+modelFilter.getKeyWord()+"%");
		ResultSet rs = preSt.executeQuery();
		
		while(rs.next())
		{
			list.add(returnDonThuBean(rs));
		}
		
		rs.close();
		con.close();
		
		return list;
	}
	
	public int countDonThuChuyenDi(LoaiTinhTrangChuyenDiEnum type,FilterDonChuyenAutoBean modelFilter) throws Exception
	{
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT count(IdDonThu) FROM don_thu WHERE 1 = 1";
		
		sql+=returnChuyenDiCondition(type,modelFilter.getOrgDaChuyen());
		sql+=" AND ( NoiDungDonThu LIKE ?";
		sql+=" OR IdDonThu IN ( SELECT IdDonThu FROM don_thu_nguoi_dai_dien WHERE IdDoiTuongDiKNTC IN (SELECT IdDoiTuongDiKNTC FROM don_thu_doi_tuong_di_kntc WHERE HoTen LIKE ?))";
		sql+=" OR IdDoiTuongBiKNTC IN ( SELECT IdDoiTuongBiKNTC FROM don_thu_doi_tuong_bi_kntc WHERE HoTen LIKE ? OR TenCoQuan LIKE ?))";
		if(modelFilter.getLoaiDonThu()!=0)
			sql+=" AND LoaiDonThu = "+modelFilter.getLoaiDonThu();
		
		System.out.println(sql);
		
		PreparedStatement preSt = con.prepareStatement(sql);
		preSt.setNString(1, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(2, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(3, "%"+modelFilter.getKeyWord()+"%");
		preSt.setNString(4, "%"+modelFilter.getKeyWord()+"%");
		
		ResultSet rs = preSt.executeQuery();		
		rs.next();
		
		int count = rs.getInt(1);
		
		rs.close();
		con.close();
		
		return count;
	}
	
	public static String returnChuyenDiCondition(LoaiTinhTrangChuyenDiEnum type,int orgDaChuyen) throws Exception
	{ 
		String sql = "";
		int hanXuLy = Integer.parseInt(KNTCProps.getProperty("han.xu.ly"));
		String orgNhanCondition = orgDaChuyen!=0?" AND OrgNhan = "+orgDaChuyen+" ":"";
		if(type!=null)
		{
			switch (type) {
			case chuaxuly:
				sql+=" AND IdDonThu IN (SELECT IdDonThu From don_thu_thong_tin_don_thu WHERE OrgChuyen != OrgNhan AND OrgChuyen = "+SessionUtil.getOrgId()+orgNhanCondition+" AND NgayHoanThanhXuLy IS NULL)";
				break;
			case chuaxulytronghan:
				sql+=" AND IdDonThu IN (SELECT IdDonThu From don_thu_thong_tin_don_thu WHERE OrgChuyen != OrgNhan AND OrgChuyen = "+SessionUtil.getOrgId()+orgNhanCondition+" AND NgayHoanThanhXuLy IS NULL AND ("+hanXuLy+" - DATEDIFF(CURDATE(),NgayNhan))>=0)";
				break;
			case chuaxulyquahan:
				sql+=" AND IdDonThu IN (SELECT IdDonThu From don_thu_thong_tin_don_thu WHERE OrgChuyen != OrgNhan AND OrgChuyen = "+SessionUtil.getOrgId()+orgNhanCondition+" AND NgayHoanThanhXuLy IS NULL AND ("+hanXuLy+" - DATEDIFF(CURDATE(),NgayNhan))<0)";
				break;
			case daxuly:
				sql+=" AND IdDonThu IN (SELECT IdDonThu From don_thu_thong_tin_don_thu WHERE OrgChuyen != OrgNhan AND OrgChuyen = "+SessionUtil.getOrgId()+orgNhanCondition+" AND NgayHoanThanhXuLy IS NOT NULL)";
				break;
			case daxulytronghan:
				sql+=" AND IdDonThu IN (SELECT IdDonThu From don_thu_thong_tin_don_thu WHERE OrgChuyen != OrgNhan AND OrgChuyen = "+SessionUtil.getOrgId()+orgNhanCondition+" AND NgayHoanThanhXuLy IS NOT NULL AND ("+hanXuLy+" - DATEDIFF(NgayHoanThanhXuLy,NgayNhan))>=0)";
				break;
			case daxulyquahan:
				sql+=" AND IdDonThu IN (SELECT IdDonThu From don_thu_thong_tin_don_thu WHERE OrgChuyen != OrgNhan AND OrgChuyen = "+SessionUtil.getOrgId()+orgNhanCondition+" AND NgayHoanThanhXuLy IS NOT NULL AND ("+hanXuLy+" - DATEDIFF(NgayHoanThanhXuLy,NgayNhan))<0)";
				break;
			case chuagiaiquyet:
				sql+=" AND IdDonThu IN (SELECT IdDonThu From don_thu_thong_tin_don_thu WHERE OrgChuyen != OrgNhan AND OrgChuyen = "+SessionUtil.getOrgId()+orgNhanCondition+")";
				sql+=" AND IdDonThu NOT IN (SELECT IdDonThu FROM don_thu_quyet_dinh_giai_quyet WHERE OrgTao != "+SessionUtil.getOrgId()+")";
				sql+=" AND NgayHanGiaiQuyet IS NOT NULL AND NgayHoanThanhGiaiQuyet IS NULL";
				break;
			case chuagiaiquyettronghan:
				sql+=" AND IdDonThu IN (SELECT IdDonThu From don_thu_thong_tin_don_thu WHERE OrgChuyen != OrgNhan AND OrgChuyen = "+SessionUtil.getOrgId()+orgNhanCondition+")";
				sql+=" AND IdDonThu NOT IN (SELECT IdDonThu FROM don_thu_quyet_dinh_giai_quyet WHERE OrgTao != "+SessionUtil.getOrgId()+")";
				sql+=" AND NgayHanGiaiQuyet IS NOT NULL AND NgayHoanThanhGiaiQuyet IS NULL AND (DATEDIFF(NgayHanGiaiQuyet,NgayThuLy)-DATEDIFF(CURDATE(),NgayThuLy)) > 0";
				break;
			case chuagiaiquyetquahan:
				sql+=" AND IdDonThu IN (SELECT IdDonThu From don_thu_thong_tin_don_thu WHERE OrgChuyen != OrgNhan AND OrgChuyen = "+SessionUtil.getOrgId()+orgNhanCondition+")";
				sql+=" AND IdDonThu NOT IN (SELECT IdDonThu FROM don_thu_quyet_dinh_giai_quyet WHERE OrgTao != "+SessionUtil.getOrgId()+")";
				sql+=" AND NgayHanGiaiQuyet IS NOT NULL AND NgayHoanThanhGiaiQuyet IS NULL AND (DATEDIFF(NgayHanGiaiQuyet,NgayThuLy)-DATEDIFF(CURDATE(),NgayThuLy))< 0";
				break;
			case dagiaiquyet:
				sql+=" AND IdDonThu IN (SELECT IdDonThu From don_thu_thong_tin_don_thu WHERE OrgChuyen != OrgNhan AND OrgChuyen = "+SessionUtil.getOrgId()+orgNhanCondition+")";
				sql+=" AND IdDonThu IN (SELECT IdDonThu FROM don_thu_quyet_dinh_giai_quyet WHERE OrgTao != "+SessionUtil.getOrgId()+")";
				sql+=" AND NgayHoanThanhGiaiQuyet IS NOT NULL";
				break;
			case dagiaiquyettronghan:
				sql+=" AND IdDonThu IN (SELECT IdDonThu From don_thu_thong_tin_don_thu WHERE OrgChuyen != OrgNhan AND OrgChuyen = "+SessionUtil.getOrgId()+orgNhanCondition+")";
				sql+=" AND IdDonThu IN (SELECT IdDonThu FROM don_thu_quyet_dinh_giai_quyet WHERE OrgTao != "+SessionUtil.getOrgId()+")";
				sql+=" AND NgayHoanThanhGiaiQuyet IS NOT NULL AND (DATEDIFF(NgayHanGiaiQuyet,NgayThuLy)-DATEDIFF(NgayHoanThanhGiaiQuyet,NgayThuLy)) > 0";
				break;
			case dagiaiquyetquahan:
				sql+=" AND IdDonThu IN (SELECT IdDonThu From don_thu_thong_tin_don_thu WHERE OrgChuyen != OrgNhan AND OrgChuyen = "+SessionUtil.getOrgId()+orgNhanCondition+")";
				sql+=" AND IdDonThu IN (SELECT IdDonThu FROM don_thu_quyet_dinh_giai_quyet WHERE OrgTao != "+SessionUtil.getOrgId()+")";
				sql+=" AND NgayHoanThanhGiaiQuyet IS NOT NULL AND (DATEDIFF(NgayHanGiaiQuyet,NgayThuLy)-DATEDIFF(NgayHoanThanhGiaiQuyet,NgayThuLy)) < 0";
				break;
			}
		}
		else
		{
			sql+=" AND IdDonThu IN (SELECT IdDonThu From don_thu_thong_tin_don_thu WHERE OrgChuyen != OrgNhan AND OrgChuyen = "+SessionUtil.getOrgId()+orgNhanCondition+")";
		}
		
		return sql; 
	}

	public static String returnTenNguoiDaiDienDonThu(DonThuBean model) throws Exception
	{
		String nguoiDaiDien = "";

		if(model.getLoaiNguoiDiKNTC()==1 || model.getLoaiNguoiDiKNTC() == 2)
		{
			List<DoiTuongDiKNTCBean> listNguoiDiKNTC = getNguoiDaiDienDonThu(model.getMaDonThu());
			for(DoiTuongDiKNTCBean modelNguoiDiKNTC : listNguoiDiKNTC)
			{
				nguoiDaiDien+="<p style='margin: 5px'>- "+modelNguoiDiKNTC.getHoTen()+"</p>";
			}
		}
		else
		{
			nguoiDaiDien = model.getTenCoQuanDiKNTC();
		}
		return nguoiDaiDien;
	}

	public String returnTenNguoiBiKNTC(DonThuBean model) throws Exception
	{
		String tenNguoiBiKNTC = "";

		DoiTuongBiKNTCBean modelDT = getDoiTuongBiKNTC(model.getMaDoiTuongBiKNTC());

		if(modelDT.getHoTen()!=null)
			tenNguoiBiKNTC = modelDT.getHoTen();
		else
			tenNguoiBiKNTC = modelDT.getTenCoQuanToChuc();

		return tenNguoiBiKNTC;
	}
}
