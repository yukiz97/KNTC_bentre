package ngn.kntc.lienthongttcp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.service.OrganizationLocalService;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;

import ngn.kntc.beans.DoiTuongBiKNTCBean;
import ngn.kntc.beans.DoiTuongDiKNTCBean;
import ngn.kntc.beans.DoiTuongUyQuyenBean;
import ngn.kntc.beans.DonThuBean;
import ngn.kntc.beans.HoSoDinhKemBean;
import ngn.kntc.beans.SoTiepCongDanBean;
import ngn.kntc.beans.ThongTinDonThuBean;
import ngn.kntc.enums.LoaiNguoiDiKNTCEnum;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.modules.KNTCBaseFunction;
import ngn.kntc.ttcp.LTTTCP;
import ngn.kntc.ttcp.model.AttachmentType;
import ngn.kntc.ttcp.model.CoQuanDaGiaiQuyetType;
import ngn.kntc.ttcp.model.NguoiBiKNTCType;
import ngn.kntc.ttcp.model.NguoiKNTC;
import ngn.kntc.ttcp.model.NguoiKNTCType;
import ngn.kntc.ttcp.model.NguoiUyQuyenType;
import ngn.kntc.ttcp.model.NguonDonDenType;
import ngn.kntc.ttcp.model.PhanLoaiDonType;
import ngn.kntc.ttcp.model.VuViecType;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.LiferayServiceUtil;
import ngn.kntc.utils.SessionUtil;
import ngn.kntc.utils.TiepCongDanServiceUtil;
import ngn.kntc.utils.UploadServiceUtil;

public class LienThongVuViec {
	VuViecType vuViec = new VuViecType();
	NguonDonDenType nguonDonDen = new NguonDonDenType();
	NguoiKNTC nguoiKNTC = new NguoiKNTC();
	NguoiBiKNTCType nguoiBiKNTC = new NguoiBiKNTCType();
	NguoiUyQuyenType nguoiUyQuyen = new NguoiUyQuyenType();
	CoQuanDaGiaiQuyetType coQuanDaGiaiQuyet = new CoQuanDaGiaiQuyetType();
	List<PhanLoaiDonType> phanLoaiDon = new ArrayList();
	List<AttachmentType> attachment = new ArrayList();

	List<NguoiKNTCType> dataNguoiKNTC = new ArrayList();

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	DonThuServiceUtil svDonThu = new DonThuServiceUtil();
	TiepCongDanServiceUtil svTCD = new TiepCongDanServiceUtil();
	LienThongLocalServicesUtil svLienThong = new LienThongLocalServicesUtil();

	public void executeLienThong(int idDonThu,long orgId,String idLienThongGanVuViec,String typeExecute) throws Exception
	{
		/*
		 * Nếu idLienThongGanVuViec != null thì là thêm đơn thư và gắn vụ việc
		 * */
		DonThuBean modelDonThu = svDonThu.getDonThu(idDonThu);
		ThongTinDonThuBean modelThongTinDon = svDonThu.getThongTinDonThu(idDonThu, orgId);
		List<DoiTuongDiKNTCBean> listDoiTuongDiKNTC = svDonThu.getNguoiDaiDienDonThu(idDonThu);
		List<String> listLinhVuc = svDonThu.getLinhVucList(idDonThu);
		List<HoSoDinhKemBean> listHoSoDinhKem = svDonThu.getDinhKemHoSoList(idDonThu);
		SoTiepCongDanBean modelSoTCD = svTCD.getSoTiepCongDanOnDonThu(idDonThu);
		
		String donViNhap = LiferayServiceUtil.getOrgCustomFieldValue(orgId, "OrgLienThongId");

		System.out.println("----Vụ việc---");
		String idLienThong = "";
		String ngayNhapDon = sdf.format(modelDonThu.getNgayNhapDon());

		if(typeExecute=="save")
		{
			if(idLienThongGanVuViec!=null)
			{
				idLienThong = idLienThongGanVuViec;
				vuViec.setGanVuViec(1);
				System.out.println("Gắn vụ việc: 1");
			}
		} else if(typeExecute=="update")
		{
			idLienThong = svLienThong.getIdLienThongOfDonThuOnThongTinDon(idDonThu, orgId);
			vuViec.setGanVuViec(0);
			System.out.println("Gắn vụ việc: 0");
		}

		vuViec.setId(idLienThong);
		vuViec.setNgayNhapDon(ngayNhapDon);
		System.out.println("Id liên thông: "+idLienThong);
		System.out.println("Ngày nhập đơn: "+ngayNhapDon);

		if(modelSoTCD!=null)
		{
			UserLocalServiceUtil svUserLiferay = new UserLocalServiceUtil();
			if(modelSoTCD.getMaLanhDaoTiep()!=0)
			{
				String lanhDaoTiep=svUserLiferay.getUser(modelSoTCD.getMaLanhDaoTiep()).getFirstName();
				int tiepDinhKy = modelSoTCD.isTiepDinhKy()?1:0;
				int uyQuyenLanhDao = modelSoTCD.isUyQuyenLanhDao()?1:0;

				vuViec.setLanhDaoTiep(lanhDaoTiep);
				vuViec.setTiepDinhKy(tiepDinhKy);
				vuViec.setUyQuyen(uyQuyenLanhDao);
				System.out.println("Lãnh đạo tiếp: "+lanhDaoTiep);
				System.out.println("Tiếp định kỳ: "+tiepDinhKy);
				System.out.println("Ủy quyền lãnh đạo: "+uyQuyenLanhDao);
				if(modelSoTCD.isUyQuyenLanhDao())
				{
					String lanhDaoUyQuyen = svUserLiferay.getUser(modelSoTCD.getMaLanhDaoUyQuyen()).getFirstName();

					vuViec.setLanhDaoUyQuyen(lanhDaoUyQuyen);
					System.out.println("Lãnh đạo ủy quyền: "+lanhDaoUyQuyen);
				}
			}
		}

		// nguồn đơn đến
		String maNguonDonDen = modelThongTinDon.getNguonDonDen()+"";
		String maCoQuanChuyenDen = modelThongTinDon.getMaCoQuanChuyenDen();
		String maSoVanBanDen = modelThongTinDon.getSoVanBanDen();
		String ngayPhatHanhVanBan = modelThongTinDon.getNgayPhatHanhVanBanDen()!=null?sdf.format(modelThongTinDon.getNgayPhatHanhVanBanDen()):null;
		nguonDonDen.setMaNguonDonDen(maNguonDonDen);
		nguonDonDen.setMaCoQuanChuyenDon(maCoQuanChuyenDen);
		nguonDonDen.setSoVanBanDen(maSoVanBanDen);
		nguonDonDen.setNgayVanBan(ngayPhatHanhVanBan);
		System.out.println("----Nguồn đơn đến----");
		System.out.println("Mã nguồn đơn đến: "+maNguonDonDen);
		System.out.println("Mã cơ quan chuyển đến: "+maCoQuanChuyenDen);
		System.out.println("Mã số văn bản đến: "+maSoVanBanDen);
		System.out.println("Ngày phát hành văn bản: "+ngayPhatHanhVanBan);

		// cơ quan đã giải quyết
		if(modelDonThu.getMaCoQuanDaGiaiQuyet()!=null)
		{
			String maCoQuanGiaiQuyet = modelDonThu.getMaCoQuanDaGiaiQuyet();
			int lanGiaiQuyet = modelDonThu.getLanGiaiQuyet();
			String loaiQuyetDinhGiaiQuyet = modelDonThu.getLoaiQuyetDinhGiaiQuyet()+"";
			String ketQuaGiaiQuyet = modelDonThu.getTomTatNoiDungGiaiQuyet();
			String ngayBanHanhGiaiQuyet = sdf.format(modelDonThu.getNgayBanHanhQDGQ());
			coQuanDaGiaiQuyet.setMaCoQuan(maCoQuanGiaiQuyet);
			coQuanDaGiaiQuyet.setLanGiaiQuyet(lanGiaiQuyet);
			coQuanDaGiaiQuyet.setMaLoaiQuyetDinh(loaiQuyetDinhGiaiQuyet);
			coQuanDaGiaiQuyet.setKetQuaGiaiQuyet(ketQuaGiaiQuyet);
			coQuanDaGiaiQuyet.setNgayBanHanh(ngayBanHanhGiaiQuyet);

			System.out.println("----Cơ quan đã giải quyết----");
			System.out.println("Mã cơ quan đã giải quyết: "+maCoQuanGiaiQuyet);
			System.out.println("Lần giải quyết: "+lanGiaiQuyet);
			System.out.println("Mã loại quyết định giải quyết: "+loaiQuyetDinhGiaiQuyet);
			System.out.println("Kết quả giải quyết: "+ketQuaGiaiQuyet);
			System.out.println("Ngày ban hành quyết định: "+ngayBanHanhGiaiQuyet);
		}

		if(modelDonThu.getLoaiNguoiDiKNTC()==LoaiNguoiDiKNTCEnum.coquantochuc.getType())
		{
			String tenCoQuanDiKNTC = modelDonThu.getTenCoQuanDiKNTC();
			String diaChiCoQuanDiKNTC = modelDonThu.getDiaChiCoQuanDiKNTC();

			nguoiKNTC.setTenCoQuanToChuc(tenCoQuanDiKNTC);
			nguoiKNTC.setDiaChiCoQuanToChuc(diaChiCoQuanDiKNTC);

			System.out.println("Tên cơ quan đi KNTC: "+tenCoQuanDiKNTC);
			System.out.println("Địa chỉ cơ quan: "+diaChiCoQuanDiKNTC);
		}

		for(DoiTuongDiKNTCBean model : listDoiTuongDiKNTC)
		{
			String hoTen = "";
			if(model.isNacDanh())
				hoTen = "Đơn nặc danh";
			else 
				hoTen = model.getHoTen();
			String diaChiChiTiet = model.getDiaChiChiTiet();
			int gioiTinh = model.getGioiTinh();
			String maDanToc = model.getMaDanToc()+"";
			String maHuyen =model.getMaHuyen()+"";
			String maQuocTich = model.getMaQuocTich();
			String maTinh = model.getMaTinh();
			String maXa = model.getMaXa();
			String ngayCapSoDinhDanh = model.getNgayCapSoDinhDanh()!=null?sdf.format(model.getNgayCapSoDinhDanh()):null;
			String noiCapSoDinhDanh = model.getNoiCapSoDinhDanh();
			String soDinhDanh = model.getSoDinhDanhCaNhan();

			NguoiKNTCType tmp= new NguoiKNTCType();
			tmp.setHoTen(hoTen);
			tmp.setDiaChiChiTiet(diaChiChiTiet);
			tmp.setGioiTinh(gioiTinh);
			tmp.setMaDanToc(maDanToc);
			tmp.setMaHuyen(maHuyen);
			tmp.setMaQuocTich(maQuocTich);
			tmp.setMaTinh(maTinh);
			tmp.setMaXa(maXa);
			tmp.setNgayCapSoDinhDanh(ngayCapSoDinhDanh);
			tmp.setNoiCapSoDinhDanh(noiCapSoDinhDanh);
			tmp.setSoDinhDanhCaNhan(soDinhDanh);

			dataNguoiKNTC.add(tmp);
			System.out.println("----Người KNTC----");
			System.out.println("Họ tên: "+hoTen);
			System.out.println("Địa chỉ: "+diaChiChiTiet);
			System.out.println("Giới tính: "+gioiTinh);
			System.out.println("Dân tộc: "+maDanToc);
			System.out.println("Mã tỉnh: "+maTinh);
			System.out.println("Mã huyện: "+maHuyen);
			System.out.println("Mã xã: "+maXa);
			System.out.println("Mã Quốc tịch: "+maQuocTich);
			System.out.println("Ngày cấp số định danh: "+ngayCapSoDinhDanh);
			System.out.println("Nơi cấp số định danh"+noiCapSoDinhDanh);
			System.out.println("Số định danh: "+soDinhDanh);
		}

		int loaiNguoiDiKNTC = modelDonThu.getLoaiNguoiDiKNTC();
		int soNguoiDaiDien = modelDonThu.getSoNguoiDaiDien();
		int soNguoiDiKNTC = modelDonThu.getSoNguoiDiKNTC();

		nguoiKNTC.setDataNguoiKNTC(dataNguoiKNTC);
		nguoiKNTC.setLoaiNguoiKNTC(loaiNguoiDiKNTC);
		nguoiKNTC.setSoNguoiDaiDien(soNguoiDaiDien);
		nguoiKNTC.setSoNguoiKNTC(soNguoiDiKNTC);
		System.out.println("Loại người khiếu tố: "+loaiNguoiDiKNTC);

		if(modelDonThu.getMaDoiTuongBiKNTC()!=0)
		{
			DoiTuongBiKNTCBean modelDoiTuongBiKNTC = svDonThu.getDoiTuongBiKNTC(modelDonThu.getMaDoiTuongBiKNTC());

			if(modelDoiTuongBiKNTC.getHoTen()!=null)
			{
				int loaiBiKNTC = modelDonThu.getLoaiNguoiBiKNTC();
				String hoTen = modelDoiTuongBiKNTC.getHoTen();
				String diaChiChiTiet = modelDoiTuongBiKNTC.getDiaChiChiTiet();
				int gioiTinh = modelDoiTuongBiKNTC.getGioiTinh();
				String maDanToc = modelDoiTuongBiKNTC.getMaDanToc()+"";
				String maHuyen =modelDoiTuongBiKNTC.getMaHuyen()+"";
				String maQuocTich = modelDoiTuongBiKNTC.getMaQuocTich();
				String maTinh = modelDoiTuongBiKNTC.getMaTinh();
				String maXa = modelDoiTuongBiKNTC.getMaXa();
				String ngayCapSoDinhDanh = modelDoiTuongBiKNTC.getNgayCapSoDinhDanh()!=null?sdf.format(modelDoiTuongBiKNTC.getNgayCapSoDinhDanh()):null;
				String noiCapSoDinhDanh = modelDoiTuongBiKNTC.getNoiCapSoDinhDanh();
				String soDinhDanh = modelDoiTuongBiKNTC.getSoDinhDanhCaNhan();
				String chucVu = modelDoiTuongBiKNTC.getChucVu();
				String tenCoQuanCongTac = modelDoiTuongBiKNTC.getNoiCongTac();

				nguoiBiKNTC.setHoTen(hoTen);
				nguoiBiKNTC.setLoaiNguoiBiKNTC(loaiBiKNTC);
				nguoiBiKNTC.setSoDinhDanhCaNhan(soDinhDanh);
				nguoiBiKNTC.setNoiCapSoDinhDanh(noiCapSoDinhDanh);
				nguoiBiKNTC.setNgayCapSoDinhDanh(ngayCapSoDinhDanh);
				nguoiBiKNTC.setGioiTinh(gioiTinh);
				nguoiBiKNTC.setMaTinh(maTinh);
				nguoiBiKNTC.setMaHuyen(maHuyen);
				nguoiBiKNTC.setMaXa(maXa);
				nguoiBiKNTC.setDiaChiChiTiet(diaChiChiTiet);
				nguoiBiKNTC.setMaQuocTich(maQuocTich);
				nguoiBiKNTC.setMaDanToc(maDanToc);
				nguoiBiKNTC.setChucVu(chucVu);

				System.out.println("----Người bị KNTC----");
				System.out.println("Loại bị KNTC: "+loaiBiKNTC);
				System.out.println("Họ tên: "+hoTen);
				System.out.println("Địa chỉ: "+diaChiChiTiet);
				System.out.println("Giới tính: "+gioiTinh);
				System.out.println("Dân tộc: "+maDanToc);
				System.out.println("Mã tỉnh: "+maTinh);
				System.out.println("Mã huyện: "+maHuyen);
				System.out.println("Mã xã: "+maXa);
				System.out.println("Mã Quốc tịch: "+maQuocTich);
				System.out.println("Ngày cấp số định danh: "+ngayCapSoDinhDanh);
				System.out.println("Nơi cấp số định danh"+noiCapSoDinhDanh);
				System.out.println("Số định danh: "+soDinhDanh);
			}else if(modelDoiTuongBiKNTC.getTenCoQuanToChuc()!=null)
			{
				int loaiBiKNTC = modelDonThu.getLoaiNguoiBiKNTC();
				String tenCoQuan = modelDoiTuongBiKNTC.getTenCoQuanToChuc();
				String diaChiChiTietCoQuan = modelDoiTuongBiKNTC.getDiaChiChiTietCoQuan();
				String maTinhCoQuan = modelDoiTuongBiKNTC.getMaTinhCoQuan();
				String maHuyenCoQuan = modelDoiTuongBiKNTC.getMaHuyenCoQuan();
				String maXaCoQuan = modelDoiTuongBiKNTC.getMaXaCoQuan();

				nguoiBiKNTC.setLoaiNguoiBiKNTC(loaiBiKNTC);
				nguoiBiKNTC.setTenCoQuanToChuc(tenCoQuan);
				nguoiBiKNTC.setMaTinhCoQuan(maTinhCoQuan);
				nguoiBiKNTC.setMaHuyenCoQuan(maHuyenCoQuan);
				nguoiBiKNTC.setMaXaCoQuan(maXaCoQuan);
				nguoiBiKNTC.setDiaChiChiTietCoQuan(diaChiChiTietCoQuan);

				System.out.println("----Người bị KNTC----");
				System.out.println("Tên cơ quan:"+tenCoQuan);
				System.out.println("Địa chỉ chi tiết:"+diaChiChiTietCoQuan);
				System.out.println("Mã tỉnh:"+maTinhCoQuan);
				System.out.println("Mã huyện:"+maHuyenCoQuan);
				System.out.println("Mã xã:"+maXaCoQuan);
			}
		}

		if(modelDonThu.getMaDoiTuongUyQuyen()!=0)
		{
			DoiTuongUyQuyenBean modelDoiTuongUyQuyen = svDonThu.getDoiTuongUyQuyen(modelDonThu.getMaDoiTuongUyQuyen());

			int loaiUyQuyen = modelDonThu.getLoaiNguoiUyQuyen();
			String hoTen = modelDoiTuongUyQuyen.getHoTen();
			String diaChiChiTiet = modelDoiTuongUyQuyen.getDiaChiChiTiet();
			int gioiTinh = modelDoiTuongUyQuyen.getGioiTinh();
			String maDanToc = modelDoiTuongUyQuyen.getMaDanToc()+"";
			String maHuyen =modelDoiTuongUyQuyen.getMaHuyen()+"";
			String maQuocTich = modelDoiTuongUyQuyen.getMaQuocTich();
			String maTinh = modelDoiTuongUyQuyen.getMaTinh();
			String maXa = modelDoiTuongUyQuyen.getMaXa();
			String ngayCapSoDinhDanh = modelDoiTuongUyQuyen.getNgayCapSoDinhDanh()!=null?sdf.format(modelDoiTuongUyQuyen.getNgayCapSoDinhDanh()):null;
			String noiCapSoDinhDanh = modelDoiTuongUyQuyen.getNoiCapSoDinhDanh();
			String soDinhDanh = modelDoiTuongUyQuyen.getSoDinhDanhCaNhan();

			nguoiUyQuyen.setHoTen(hoTen);
			nguoiUyQuyen.setLoaiNguoiUyQuyen(loaiUyQuyen);
			nguoiUyQuyen.setSoDinhDanhCaNhan(soDinhDanh);
			nguoiUyQuyen.setNoiCapSoDinhDanh(noiCapSoDinhDanh);
			nguoiUyQuyen.setNgayCapSoDinhDanh(ngayCapSoDinhDanh);
			nguoiUyQuyen.setGioiTinh(gioiTinh);
			nguoiUyQuyen.setMaTinh(maTinh);
			nguoiUyQuyen.setMaHuyen(maHuyen);
			nguoiUyQuyen.setMaXa(maXa);
			nguoiUyQuyen.setDiaChiChiTiet(diaChiChiTiet);

			System.out.println("----Người Ủy Quyền----");
			System.out.println("Họ tên: "+hoTen);
			System.out.println("Địa chỉ: "+diaChiChiTiet);
			System.out.println("Giới tính: "+gioiTinh);
			System.out.println("Dân tộc: "+maDanToc);
			System.out.println("Mã tỉnh: "+maTinh);
			System.out.println("Mã huyện: "+maHuyen);
			System.out.println("Mã xã: "+maXa);
			System.out.println("Mã Quốc tịch: "+maQuocTich);
			System.out.println("Ngày cấp số định danh: "+ngayCapSoDinhDanh);
			System.out.println("Nơi cấp số định danh"+noiCapSoDinhDanh);
			System.out.println("Số định danh: "+soDinhDanh);
		}
		System.out.println("----Loại lĩnh vực----");
		for(String linhVuc : listLinhVuc)
		{
			Map<String, String> mapLevel = DonThuModule.getLinhVucLevel(linhVuc);
			String test = "";
			PhanLoaiDonType pld = new PhanLoaiDonType();
			test+="-"+mapLevel.get("lv2").split("\\.")[0]+"-"+mapLevel.get("lv2");
			pld.setMaLoaiDon(mapLevel.get("lv2").split("\\.")[0]);
			pld.setMaLoaiDon1(mapLevel.get("lv2"));
			if(mapLevel.containsKey("lv3"))
			{
				test+="-"+mapLevel.get("lv3");
				pld.setMaLoaiDon2(mapLevel.get("lv3"));
			}
			if(mapLevel.containsKey("lv4"))
			{
				test+="-"+mapLevel.get("lv4");
				pld.setMaLoaiDon3(mapLevel.get("lv4"));
			}
			phanLoaiDon.add(pld);
			System.out.println("Lĩnh vực: "+test);
		}
		System.out.println("----Hồ sơ đính kèm----");
		for(HoSoDinhKemBean model : listHoSoDinhKem)
		{
			String tenHoSo = model.getTenHoSo();
			String loaiHoSo = model.getLoaiHoSo()+"";
			String noiDungTomTat = model.getNoiDungTomTat();
			String tenFileDinhKem = model.getTenFileDinhKem();
			AttachmentType att = new AttachmentType();
			att.setTenHoSo(tenHoSo);
			att.setLoaiHoSo(loaiHoSo);
			att.setNoiDungTomTat(noiDungTomTat);
			att.setTenFileDinhKem(tenFileDinhKem);
			att.setFileDinhKem(KNTCBaseFunction.encodeFileToBase64Binary(UploadServiceUtil.getAbsolutePath()+File.separator+UploadServiceUtil.getFolderNameDonThu()+File.separator+modelDonThu.getMaDonThu()+File.separator+model.getLinkFileDinhKem()));
			//System.out.println(att.getFileDinhKem());
			attachment.add(att);

			System.out.println("Tên hồ sơ: "+tenHoSo);
			System.out.println("Loại hồ sơ: "+loaiHoSo);
			System.out.println("Nội dung tóm tắt: "+noiDungTomTat);
			System.out.println("Tên file đính kèm: "+tenFileDinhKem);
		}

		try {
			LTTTCP.capNhatVuViec(
					modelDonThu.getMaDonThu(),orgId,
					vuViec, nguonDonDen, nguoiKNTC, nguoiBiKNTC, 
					nguoiUyQuyen, coQuanDaGiaiQuyet, phanLoaiDon, attachment
					,modelDonThu.getNoiDungDonThu(), donViNhap);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
