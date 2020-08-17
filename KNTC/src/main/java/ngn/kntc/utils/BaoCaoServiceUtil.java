package ngn.kntc.utils;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ngn.kntc.databases.DatabaseServices;
import ngn.kntc.enums.LoaiVanBanXuLyGiaiQuyetEnum;
import ngn.kntc.enums.ThamQuyenGiaiQuyetEnum;

public class BaoCaoServiceUtil implements Serializable{
	SimpleDateFormat sdfDateSql = new SimpleDateFormat("yyyy-MM-dd");
	
	public int getCountBaoCao2ATmp(Date startDate,Date endDate,int type,long orgId) throws Exception
	{
		String strUserTCD = LiferayServiceUtil.returnListTCDForSQL(orgId);
		int count = 0;
		String strStartDate = sdfDateSql.format(startDate);
		String strEndDate = sdfDateSql.format(endDate);
		Connection con = DatabaseServices.getConnection();
		String sqlDonThu = "SELECT IdDonThu FROM don_thu WHERE (NgayNhapDon BETWEEN '"+strStartDate+"' AND '"+strEndDate+"') AND UserNhapDon IN ("+strUserTCD+")";
		String sqlCount = "SELECT COUNT(IdSoTiepCongDan) FROM so_tiep_cong_dan WHERE (NgayTiepCongDan BETWEEN '"+strStartDate+"' AND '"+strEndDate+"') AND UserTCD IN ("+strUserTCD+")";
		String sql = "";
		if(type>=17)
			sql=sqlCount;
		switch (type) {
		case 1:case 2:case 3:case 4:case 5:case 6: case 7: case 8:case 9:case 10:case 11:case 12:case 13:case 14:case 15:case 16:
			String condition = "";
			/* Vu viec moi hoac cu */
			if(type == 4 || type == 8 || type == 12 || type == 16)
			{
				condition+=" AND ((IdDonThu IS NULL AND GanVuViec IS NULL AND IdSoTiepCongDan NOT IN (SELECT DISTINCT GanVuViec FROM so_tiep_cong_dan WHERE GanVuViec IS NOT NULL)) "
						+ "OR "
						+ "(IdDonThu IS NOT NULL  AND IdDonThu IN ("+sqlDonThu+" AND GanVuViec IS NULL AND IdDonThu NOT IN (SELECT DISTINCT GanVuViec FROM don_thu WHERE GanVuViec IS NOT NULL))))";
			}
			
			/* Lanh Dao tiep hay khong */
			if(type <= 8)
				condition+= " AND MaLanhDaoTiep IS NULL";
			else
				condition+= " AND MaLanhDaoTiep IS NOT NULL";
			/* chu the la doan dong nguoi*/
			if(type >=5 && type <=8 || type >= 13 && type <= 16)
				condition+= " AND LoaiNguoiDiKNTC = 2";
			
			if(type == 2 || type == 6 || type == 10 || type == 14)
				sql= "SELECT COUNT(DISTINCT IdDoiTuongDiKNTC) FROM so_tiep_cong_dan_nguoi_dai_dien WHERE IdSoTiepCongDan IN (SELECT IdSoTiepCongDan FROM so_tiep_cong_dan WHERE (NgayTiepCongDan BETWEEN '"+strStartDate+"' AND '"+strEndDate+"') AND UserTCD IN ("+strUserTCD+") "+condition+")";
			else if(type == 3 || type == 7 || type == 11 || type == 15){
				sql ="SELECT count(DISTINCT GanVuViec) FROM don_thu WHERE GanVuViec IS NOT NULL AND IdDonThu IN (SELECT IdDonThu FROM so_tiep_cong_dan WHERE (NgayTiepCongDan BETWEEN '"+strStartDate+"' AND '"+strEndDate+"') AND UserTCD IN ("+strUserTCD+") "+condition+")";
			}else
				sql=sqlCount+condition;
			break;
		case 17:case 18:case 19:case 20:case 21:case 22:case 23:case 24:case 25:
			String conditionLinhVuc = "";
			if(type==17)
				conditionLinhVuc="IdLinhVuc LIKE 'KN.01.02%' AND IdLinhVuc != 'KN.01.02.16'";
			if(type==18)
				conditionLinhVuc="IdLinhVuc LIKE 'KN.01.01%'";
			if(type==19)
				conditionLinhVuc="IdLinhVuc = 'KN.01.02.16'";
			if(type==20)
				conditionLinhVuc="IdLinhVuc LIKE 'KN.01.01%'";
			if(type==21)
				conditionLinhVuc="IdLinhVuc LIKE 'KN.02%'";
			if(type==22)
				conditionLinhVuc="IdLinhVuc = 'KN.05'";
			if(type==23)
				conditionLinhVuc="IdLinhVuc LIKE 'TC.01%'";
			if(type==24)
				conditionLinhVuc="IdLinhVuc LIKE 'TC.02%'";
			if(type==25)
				conditionLinhVuc="IdLinhVuc IN ('TC.01.05','TC.02.01')";
			sql+=" AND ((IdDonThu IS NULL AND IdSoTiepCongDan IN(SELECT IdSoTiepCongDan FROM so_tiep_cong_dan_linh_vuc WHERE "+conditionLinhVuc+")) OR (IdDonThu IS NOT NULL AND IdDonThu IN ("+sqlDonThu+" AND IdDonThu IN (SELECT IdDonThu FROM don_thu_linh_vuc WHERE "+conditionLinhVuc+"))))";
			break;
		case 26:
			sql+=" AND((IdDonThu IS NULL AND LoaiLinhVuc >=3) OR ( IdDonThu IS NOT NULL AND IdDonThu IN ("+sqlDonThu+" AND LoaiDonThu >= 3)))";
			break;
		case 27:
			sql+=" AND ((IdDonThu IS NULL AND MaCoQuanDaGiaiQuyet IS NULL) OR ( IdDonThu IS NOT NULL AND IdDonThu IN ("+sqlDonThu+" AND MaCoQuanDaGiaiQuyet IS NULL)))";
			break;
		case 28:case 29:case 30:
			String conditionHinhThuc = "";
			if(type==28)
				conditionHinhThuc+=" LoaiQuyetDinhGiaiQuyet = 1";
			if(type==29)
				conditionHinhThuc+=" LoaiQuyetDinhGiaiQuyet IN (2,3,4)";
			if(type==30)
				conditionHinhThuc+=" LoaiQuyetDinhGiaiQuyet IN (5,6,7,8)";
			sql+="AND ((IdDonThu IS NULL AND MaCoQuanDaGiaiQuyet IS NOT NULL AND "+conditionHinhThuc+") OR ( IdDonThu IS NOT NULL AND IdDonThu IN ("+sqlDonThu+" AND MaCoQuanDaGiaiQuyet IS NOT NULL AND "+conditionHinhThuc+")))";
			break;
		}
		System.out.println(type+": "+sql);
		ResultSet rs = con.createStatement().executeQuery(sql);
		rs.next();
		count = rs.getInt(1);
		
		rs.close();
		con.close();
		return count;
	}
	
	public int getCountBaoCao2A(Date startDate,Date endDate,int type,long orgId) throws Exception
	{
		String strUserTCD = LiferayServiceUtil.returnListTCDForSQL(orgId);
		int count = 0;
		String strStartDate = sdfDateSql.format(startDate);
		String strEndDate = sdfDateSql.format(endDate);
		Connection con = DatabaseServices.getConnection();
		String sqlDonThu = "SELECT IdDonThu FROM don_thu WHERE (NgayNhapDon BETWEEN '"+strStartDate+"' AND '"+strEndDate+"') AND UserNhapDon IN ("+strUserTCD+")";
		String sqlCount = "SELECT COUNT(IdSoTiepCongDan) FROM so_tiep_cong_dan WHERE (NgayTiepCongDan BETWEEN '"+strStartDate+"' AND '"+strEndDate+"') AND UserTCD IN ("+strUserTCD+")";
		String sqlSum = "SELECT SUM(SoNguoiDiKNTC) FROM so_tiep_cong_dan WHERE (NgayTiepCongDan BETWEEN '"+strStartDate+"' AND '"+strEndDate+"') AND UserTCD IN ("+strUserTCD+")";
		String sql = "";
		if(type == 2 || type == 6 || type == 10 || type == 14)
			sql=sqlSum;
		else
			sql=sqlCount;
		switch (type) {
		case 1:case 2:case 3:case 4:case 5:case 6: case 7: case 8:case 9:case 10:case 11:case 12:case 13:case 14:case 15:case 16:
			/* Vu viec moi hoac cu */
			if(type == 3 || type == 7 || type == 11 || type == 15)
			{
				sql+=" AND IdDonThu IN ("+sqlDonThu+" AND GanVuViec IS NOT NULL)";
			}
			if(type == 4 || type == 8 || type == 12 || type == 16)
			{
				sql+=" AND IdDonThu IN ("+sqlDonThu+" AND GanVuViec IS NULL)";
			}
			/* Lanh Dao tiep hay khong */
			if(type <= 8)
				sql+= " AND MaLanhDaoTiep IS NULL";
			else
				sql+= " AND MaLanhDaoTiep IS NOT NULL";
			/* Chu the la ca nhan, doan dong nguoi , co quan hay gi */
			if(type >=1 && type <=4 || type >= 9 && type <= 12)
				sql+= " AND LoaiNguoiDiKNTC IN (1,3)";
			else
				sql+= " AND LoaiNguoiDiKNTC = 2";
			break;
		case 17:case 18:case 19:case 20:case 21:case 22:case 23:case 24:case 25:
			String conditionLinhVuc = "";
			if(type==17)
				conditionLinhVuc="IdLinhVuc LIKE 'KN.01.02%' OR IdLinhVuc LIKE 'KN.04%'";
			if(type==18)
				conditionLinhVuc=" 1 = 0";
			if(type==19)
				conditionLinhVuc=" 1 = 0";
			if(type==20)
				conditionLinhVuc=" 1 = 0";
			if(type==21)
				conditionLinhVuc="IdLinhVuc LIKE 'KN.02%'";
			if(type==22)
				conditionLinhVuc=" 1 = 0";
			if(type==23)
				conditionLinhVuc="IdLinhVuc LIKE 'TC.01%'";
			if(type==24)
				conditionLinhVuc="IdLinhVuc LIKE 'TC.02%'";
			if(type==25)
				conditionLinhVuc="IdLinhVuc IN ('TC.01.05','TC.02.01')";
			sql+=" AND IdDonThu IN ("+sqlDonThu+" AND IdDonThu IN (SELECT IdDonThu FROM don_thu_linh_vuc WHERE "+conditionLinhVuc+"))";
			break;
		case 26:
			sql+=" AND IdDonThu IN ("+sqlDonThu+" AND LoaiDonThu >= 3)";
			break;
		case 27:
			sql+=" AND IdDonThu NOT IN ("+sqlDonThu+" AND MaCoQuanDaGiaiQuyet IS NULL)";
			break;
		case 28:case 29:case 30:
			String conditionHinhThuc = "";
			if(type==28)
				conditionHinhThuc+=" LoaiQuyetDinhGiaiQuyet = 1";
			if(type==29)
				conditionHinhThuc+=" LoaiQuyetDinhGiaiQuyet IN (2,3,4)";
			if(type==30)
				conditionHinhThuc+=" LoaiQuyetDinhGiaiQuyet IN (5,6,7,8)";
			sql+=" AND IdDonThu IN ("+sqlDonThu+" AND MaCoQuanDaGiaiQuyet IS NOT NULL AND "+conditionHinhThuc+")";
			break;
		}
		System.out.println(type+": "+sql);
		ResultSet rs = con.createStatement().executeQuery(sql);
		rs.next();
		count = rs.getInt(1);
		
		rs.close();
		con.close();
		return count;
	}
	
	public int getCountBaoCao2B(Date startDate,Date endDate,int type,long orgId) throws Exception
	{
		int count = 0;
		String strStartDate = sdfDateSql.format(startDate);
		String strEndDate = sdfDateSql.format(endDate);
		Connection con = DatabaseServices.getConnection();
		String sqlTrongKy = "SELECT IdDonThu FROM don_thu_thong_tin_don_thu WHERE OrgNhan = "+orgId+" AND (NgayNhan BETWEEN '"+strStartDate+"' AND '"+strEndDate+"')";
		String sqlKyTruoc = "SELECT IdDonThu FROM don_thu_thong_tin_don_thu WHERE OrgNhan = "+orgId+" AND (NgayNhan < '"+strStartDate+"') AND IdDonThu NOT IN (SELECT IdDonThu FROM don_thu_ket_qua_xu_ly WHERE OrgTao = "+orgId+")";
		String sql = "SELECT count(IdDonThu) FROM don_thu WHERE 1 = 1";
		switch (type) {
		case 2:case 3:case 4:case 5:
			/* tiếp nhận kỳ hoặc kỳ trước */
			if(type==2 || type==3)
				sql+=" AND IdDonThu IN ("+sqlTrongKy+")";
			else 
				sql+=" AND IdDonThu IN ("+sqlKyTruoc+")";
			/* ít người hoặc nhiều người */
			if(type == 2 || type == 4)
				sql+=" AND SoNguoiDaiDien > 1";
			else
				sql+=" AND SoNguoiDaiDien = 1";
			break;
		case 6: 
			sql+=" AND (IdDonThu IN ("+sqlTrongKy+") OR IdDonThu IN ("+sqlKyTruoc+"))";
			sql+=" AND DonKhongDuDieuKienXL = 0";
			break;
		case 8:case 9:case 10:case 11:case 12:case 13:case 15:case 16:case 17:case 18:case 19:
			String conditionLinhVuc = "";
			sql+=" AND IdDonThu IN ("+sqlTrongKy+")";
			if(type==8)
				conditionLinhVuc="IdLinhVuc LIKE 'KN.01.02%' OR IdLinhVuc LIKE 'KN.04%'";
			if(type==9)
				conditionLinhVuc="IdLinhVuc IN ('KN.01.04','KN.01.02.17','KN.01.02.16')";
			if(type==10)
				conditionLinhVuc="IdLinhVuc LIKE 'KN.01.01%'";
			if(type==11)
				conditionLinhVuc="IdLinhVuc LIKE 'KN.01.03%' OR IdLinhVuc LIKE 'KN.01.05%' OR IdLinhVuc LIKE 'KN.01.06%'";
			if(type==12)
				conditionLinhVuc="IdLinhVuc LIKE 'KN.02%'";
			if(type==13)
				conditionLinhVuc="IdLinhVuc LIKE 'KN.03%'";
			if(type==15)
				conditionLinhVuc="IdLinhVuc LIKE 'TC.01%'";
			if(type==16)
				conditionLinhVuc="IdLinhVuc LIKE 'TC.02%'";
			if(type==17)
				conditionLinhVuc="IdLinhVuc IN ('TC.01.05','TC.02.01')";
			if(type==18)
				conditionLinhVuc="IdLinhVuc LIKE 'TC.03%'";
			if(type==19)
				conditionLinhVuc="IdLinhVuc LIKE 'TC%' AND IdLinhVuc NOT LIKE 'TC.01%' AND IdLinhVuc NOT LIKE 'TC.02%' AND IdLinhVuc NOT LIKE 'TC.03%' AND IdLinhVuc NOT IN ('TC.01.05','TC.02.01')";
			sql+=" AND IdDonThu IN (SELECT IdDonThu FROM don_thu_linh_vuc WHERE "+conditionLinhVuc+")";
			break;
		case 20:case 21:case 22:
			String conditionThamQuyen = "";
			if(type==20)
				conditionThamQuyen = ThamQuyenGiaiQuyetEnum.coquanhanhchinhcaccap.getType();
			if(type==21)
				conditionThamQuyen = ThamQuyenGiaiQuyetEnum.coquantuphapcaccap.getType();
			if(type==22)
				conditionThamQuyen = ThamQuyenGiaiQuyetEnum.coquandang.getType();
			sql+=" AND IdDonThu IN ("+sqlTrongKy+")";
			sql+=" AND ThamQuyenGiaiQuyet = '"+conditionThamQuyen+"'";
			break;
		case 23:case 24:case 25:
			sql+=" AND IdDonThu IN ("+sqlTrongKy+") AND LoaiDonThu IN (1,2)";
			if(type==23)
				sql+=" AND MaCoQuanDaGiaiQuyet IS NULL";
			if(type==24)
				sql+=" AND MaCoQuanDaGiaiQuyet IS NOT NULL AND LanGiaiQuyet = 1";
			if(type==25)
				sql+="AND MaCoQuanDaGiaiQuyet IS NOT NULL AND LanGiaiQuyet > 1";
			break;
		case 26:
			sql+=" AND IdDonThu IN ("+sqlTrongKy+")";
			sql+=" AND (LoaiDonThu >= 3 OR DonNacDanh = 1)";
			break;
		case 27:case 28:case 29:
			if(type==27)
				sql+=" AND IdDonThu IN (SELECT IdDonThu FROM don_thu_ket_qua_xu_ly WHERE MaHuongXuLy IN (2,7))";
			if(type==28)
				sql+=" AND IdDonThu IN (SELECT IdDonThu FROM don_thu_ket_qua_xu_ly WHERE MaHuongXuLy = 5)";
			if(type==29)
				sql+=" AND IdDonThu IN (SELECT IdDonThu FROM don_thu_van_ban_xu_ly_giai_quyet WHERE LoaiVanBan = "+LoaiVanBanXuLyGiaiQuyetEnum.cvdondoc.getType()+")";
			sql+=" AND IdDonThu IN ("+sqlTrongKy+")";
			break;
		case 30:case 31:
			int conditionLoaiDon = type - 30 + 1;
			sql+=" AND IdDonThu IN ("+sqlTrongKy+")";
			sql+=" AND LoaiDonThu = "+conditionLoaiDon;
			sql+=" AND IdDonThu IN (SELECT IdDonThu FROM don_thu_ket_qua_xu_ly WHERE MaHuongXuLy = 4 AND OrgTao = "+orgId+")";
			break;
		}
		System.out.println(type+": "+sql);
		ResultSet rs = con.createStatement().executeQuery(sql);
		rs.next();
		count = rs.getInt(1);
		
		rs.close();
		con.close();
		
		return count;
	}

	public int getCountBaoCao2C(Date startDate,Date endDate,int type,long orgId) throws SQLException
	{
		int count = 0;
		String strStartDate = sdfDateSql.format(startDate);
		String strEndDate = sdfDateSql.format(endDate);
		Connection con = DatabaseServices.getConnection();
		String sqlTrongKy = "SELECT IdDonThu FROM don_thu_thong_tin_don_thu WHERE OrgNhan = "+orgId+" AND (NgayNhan BETWEEN '"+strStartDate+"' AND '"+strEndDate+"') AND IdDonThu IN (SELECT IdDonThu FROM don_thu WHERE LoaiDonThu = 1)";
		String sqlKyTruoc = "SELECT IdDonThu FROM don_thu_thong_tin_don_thu WHERE OrgNhan = "+orgId+" AND (NgayNhan < '"+strStartDate+"') AND IdDonThu IN (SELECT IdDonThu FROM don_thu_ket_qua_xu_ly WHERE OrgTao = "+orgId+" AND MaHuongXuLy = 4) AND IdDonThu NOT IN (SELECT IdDonThu FROM don_thu_quyet_dinh_giai_quyet) AND IdDonThu IN (SELECT IdDonThu FROM don_thu WHERE LoaiDonThu = 1)";
		String sqlCount = "SELECT COUNT(IdDonThu) FROM don_thu WHERE 1 = 1";
		String sql = "";
		sql = sqlCount;
		switch (type) {
		case 2:
			sql+=" AND IdDonThu IN ("+sqlTrongKy+")";
			sql+=" AND IdDonThu IN (SELECT IdDonThu FROM don_thu_ket_qua_xu_ly WHERE MaHuongXuLy = 4 AND OrgTao = "+orgId+")";
			break;
		case 3:
			sql+=" AND (IdDonThu IN ("+sqlKyTruoc+"))"; 
			break;
		case 4:
			sql+=" AND (IdDonThu IN ("+sqlKyTruoc+") OR IdDonThu IN ("+sqlTrongKy+"))"; 
			break;
		case 5:
			sql+=" AND IdDonThu IN ("+sqlTrongKy+")";
			sql+=" AND IdDonThu IN (SELECT IdDonThu FROM don_thu_quyet_dinh_giai_quyet WHERE OrgTao = "+orgId+")";
			break;
		case 6: 
			sql+=" AND 1 = 0";
			break;
		case 7: 
			sql+=" AND 1 = 0";
			break;
		case 8:
			sql+=" AND 1 = 0";
			break;
		case 9:case 10:case 11:
			int conditionLoaiKetQua = 0; 
			if(type==9)
				conditionLoaiKetQua = 2;
			if(type==10)
				conditionLoaiKetQua = 6;
			if(type==11)
				conditionLoaiKetQua = 1;
			sql+=" AND IdDonThu IN ("+sqlTrongKy+")";
			sql+=" AND IdDonThu IN (SELECT IdDonThu FROM don_thu_quyet_dinh_giai_quyet WHERE OrgTao = "+orgId+" AND IdKetQuaGiaiQuyet = "+conditionLoaiKetQua+")";
			break;
		case 12:
			sql+=" AND 1 = 0";
			break;
		case 13:
			sql+=" AND 1 = 0";
			break;
		case 14:
			sql+=" AND 1 = 0";
			break;
		case 15:case 16:case 17:case 18:case 19: case 30: case 31: case 34: case 35:
			String sumColumnQDGQ = "";
			if(type == 15 ||type == 30)
				sumColumnQDGQ = "ThuHoiTien";
			if(type == 16 || type == 31)
				sumColumnQDGQ = "ThuHoiDatO+ThuHoiDatSX";
			if(type == 17|| type == 34)
				sumColumnQDGQ = "TraLaiTien";
			if(type == 18 || type == 35)
				sumColumnQDGQ = "TraLaiDatO+TraLaiDatSX";
			if(type == 19)
				sumColumnQDGQ = "SoNguoiDuocTraQuyenLoi";
			sql = "SELECT SUM("+sumColumnQDGQ+") FROM don_thu_quyet_dinh_giai_quyet WHERE OrgTao = "+orgId+" AND IdDonThu IN ("+sqlTrongKy+")";
			break; 
		case 20:
			sql="SELECT COUNT(IdDoiTuongBiXLHC) FROM don_thu_quyet_dinh_giai_quyet_doi_tuong_bi_xlhc WHERE CaNhan = 1 AND IdQuyetDinhGiaiQuyet IN (SELECT IdQuyetDinhGiaiQuyet FROM don_thu_quyet_dinh_giai_quyet WHERE OrgTao = "+orgId+" AND IdDonThu IN ("+sqlTrongKy+"))";
			break;
		case 21:case 32:case 33:case 36:case 37:
			String sumColumnTHGQ = "";
			if(type == 21)
				sumColumnTHGQ = "SoNguoiBiXuLy";
			if(type == 32)
				sumColumnTHGQ = "ThuHoiTien";
			if(type == 33)
				sumColumnTHGQ = "ThuHoiDatO+ThuHoiDatSX";
			if(type == 36)
				sumColumnTHGQ = "TraLaiTien";
			if(type == 37)
				sumColumnTHGQ = "TraLaiDatO+TraLaiDatSX";
			sql="SELECT SUM("+sumColumnTHGQ+") FROM don_thu_thi_hanh_giai_quyet WHERE IdQuyetDinhGiaiQuyet IN (SELECT IdQuyetDinhGiaiQuyet FROM don_thu_quyet_dinh_giai_quyet WHERE OrgTao = "+orgId+" AND IdDonThu IN ("+sqlTrongKy+"))";
			break;
		case 22:
			sql+=" AND 1 = 0";
			break;
		case 23:
			sql+=" AND 1 = 0";
			break;
		case 24:
			sql+=" AND 1 = 0";
			break;
		case 25:
			sql+=" AND 1 = 0";
			break;
		case 26:case 27:
			String conditionThoiHan = "";
			if(type == 26)
				conditionThoiHan = "<= 0";
			if(type == 27)
				conditionThoiHan = "> 0";
			sql+=" AND IdDonThu IN ("+sqlTrongKy+")";
			sql+=" AND NgayHoanThanhGiaiQuyet IS NOT NULL AND DATEDIFF(NgayHoanThanhGiaiQuyet,NgayHanGiaiQuyet) "+conditionThoiHan;
			break;
		case 28:
			sql+=" AND 1 = 0";
			break;
		case 29:
			sql+=" AND 1 = 0";
			break;
		}
		System.out.println(type+": "+sql);
		ResultSet rs = con.createStatement().executeQuery(sql);
		rs.next();
		count = rs.getInt(1);
		
		rs.close();
		con.close();
		return count;
	}
	
	public int getCountBaoCao2D(Date startDate,Date endDate,int type,long orgId) throws SQLException
	{
		int count = 0;
		String strStartDate = sdfDateSql.format(startDate);
		String strEndDate = sdfDateSql.format(endDate);
		Connection con = DatabaseServices.getConnection();
		String sqlTrongKy = "SELECT IdDonThu FROM don_thu_thong_tin_don_thu WHERE OrgNhan = "+orgId+" AND (NgayNhan BETWEEN '"+strStartDate+"' AND '"+strEndDate+"') AND IdDonThu IN (SELECT IdDonThu FROM don_thu WHERE LoaiDonThu = 2)";
		String sqlKyTruoc = "SELECT IdDonThu FROM don_thu_thong_tin_don_thu WHERE OrgNhan = "+orgId+" AND (NgayNhan < '"+strStartDate+"') AND IdDonThu IN (SELECT IdDonThu FROM don_thu_ket_qua_xu_ly WHERE OrgTao = "+orgId+" AND MaHuongXuLy = 4) AND IdDonThu NOT IN (SELECT IdDonThu FROM don_thu_quyet_dinh_giai_quyet) AND IdDonThu IN (SELECT IdDonThu FROM don_thu WHERE LoaiDonThu = 2)";
		String sqlCount = "SELECT COUNT(IdDonThu) FROM don_thu WHERE 1 = 1";
		String sql = "";
		sql = sqlCount;
		switch (type) {
		case 2:
			sql+=" AND IdDonThu IN ("+sqlTrongKy+")";
			sql+=" AND IdDonThu IN (SELECT IdDonThu FROM don_thu_ket_qua_xu_ly WHERE MaHuongXuLy = 4 AND OrgTao = "+orgId+")";
			break;
		case 3:
			sql+=" AND IdDonThu IN ("+sqlKyTruoc+")"; 
			break;
		case 4:
			sql+=" AND 1 = 0";
			break;
		case 5:
			sql+=" AND IdDonThu IN ("+sqlTrongKy+")";
			sql+=" AND IdDonThu IN (SELECT IdDonThu FROM don_thu_quyet_dinh_giai_quyet WHERE OrgTao = "+orgId+")";
			break;
		case 6: 
			sql+=" AND 1 = 0";
			break;
		case 7:case 8:case 9:
			int conditionLoaiKetQua = 0; 
			if(type==7)
				conditionLoaiKetQua = 3;
			if(type==8)
				conditionLoaiKetQua = 4;
			if(type==9)
				conditionLoaiKetQua = 5;
			sql+=" AND IdDonThu IN ("+sqlTrongKy+")";
			sql+=" AND IdDonThu IN (SELECT IdDonThu FROM don_thu_quyet_dinh_giai_quyet WHERE OrgTao = "+orgId+" AND IdKetQuaGiaiQuyet = "+conditionLoaiKetQua+")";
			break;
		case 10:case 11:case 12:case 13:case 14: case 25: case 26: case 29: case 30:
			String sumColumnQDGQ = "";
			if(type == 10 ||type == 25)
				sumColumnQDGQ = "ThuHoiTien";
			if(type == 11 || type == 26)
				sumColumnQDGQ = "ThuHoiDatO+ThuHoiDatSX";
			if(type == 12|| type == 29)
				sumColumnQDGQ = "TraLaiTien";
			if(type == 13 || type == 30)
				sumColumnQDGQ = "TraLaiDatO+TraLaiDatSX";
			if(type == 14)
				sumColumnQDGQ = "SoNguoiDuocTraQuyenLoi";
			sql = "SELECT SUM("+sumColumnQDGQ+") FROM don_thu_quyet_dinh_giai_quyet WHERE OrgTao = "+orgId+" AND IdDonThu IN ("+sqlTrongKy+")";
			break;
		case 15:
			sql="SELECT COUNT(IdDoiTuongBiXLHC) FROM don_thu_quyet_dinh_giai_quyet_doi_tuong_bi_xlhc WHERE CaNhan = 1 AND IdQuyetDinhGiaiQuyet IN (SELECT IdQuyetDinhGiaiQuyet FROM don_thu_quyet_dinh_giai_quyet WHERE OrgTao = "+orgId+" AND IdDonThu IN ("+sqlTrongKy+"))";
			break;
		case 16:case 27:case 28:case 31:case 32:
			String sumColumnTHGQ = "";
			if(type == 16)
				sumColumnTHGQ = "SoNguoiBiXuLy";
			if(type == 27)
				sumColumnTHGQ = "ThuHoiTien";
			if(type == 28)
				sumColumnTHGQ = "ThuHoiDatO+ThuHoiDatSX";
			if(type == 31)
				sumColumnTHGQ = "TraLaiTien";
			if(type == 32)
				sumColumnTHGQ = "TraLaiDatO+TraLaiDatSX";
			sql="SELECT SUM("+sumColumnTHGQ+") FROM don_thu_thi_hanh_giai_quyet WHERE IdQuyetDinhGiaiQuyet IN (SELECT IdQuyetDinhGiaiQuyet FROM don_thu_quyet_dinh_giai_quyet WHERE OrgTao = "+orgId+" AND IdDonThu IN ("+sqlTrongKy+"))";
			break;
		case 17:
			sql+=" AND 1 = 0";
			break;
		case 18:
			sql+=" AND 1 = 0";
			break;
		case 19:
			sql+=" AND 1 = 0";
			break;
		case 20:
			sql+=" AND 1 = 0";
			break;
		case 21:case 22:
			String conditionThoiHan = "";
			if(type == 21)
				conditionThoiHan = "<= 0";
			if(type == 22)
				conditionThoiHan = "> 0";
			sql+=" AND IdDonThu IN ("+sqlTrongKy+")";
			sql+=" AND NgayHoanThanhGiaiQuyet IS NOT NULL AND DATEDIFF(NgayHoanThanhGiaiQuyet,NgayHanGiaiQuyet) "+conditionThoiHan;
			break;
		case 23:
			sql+=" AND 1 = 0";
			break;
		case 24:
			sql+=" AND 1 = 0";
			break;
		default:
			break;
		}
		System.out.println(type+": "+sql);
		ResultSet rs = con.createStatement().executeQuery(sql);
		rs.next();
		count = rs.getInt(1);
		
		rs.close();
		con.close();
		return count;
	}
}
