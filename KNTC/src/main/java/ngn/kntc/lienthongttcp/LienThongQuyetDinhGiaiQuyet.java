package ngn.kntc.lienthongttcp;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ngn.kntc.beans.DoiTuongBiXuLyHanhChinhBean;
import ngn.kntc.beans.DonThuBean;
import ngn.kntc.beans.KetQuaXuLyBean;
import ngn.kntc.beans.QuyetDinhGiaiQuyetBean;
import ngn.kntc.beans.QuyetDinhThuLyBean;
import ngn.kntc.modules.KNTCBaseFunction;
import ngn.kntc.ttcp.LTTTCP;
import ngn.kntc.ttcp.model.BoiThuongThietHaiType;
import ngn.kntc.ttcp.model.CaNhanBiXuLyHanhChinhType;
import ngn.kntc.ttcp.model.HuongXuLyType;
import ngn.kntc.ttcp.model.NoiDungXuLy;
import ngn.kntc.ttcp.model.ToChucBiXuLyHanhChinhType;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;
import ngn.kntc.utils.UploadServiceUtil;

import com.fasterxml.jackson.core.JsonProcessingException;

public class LienThongQuyetDinhGiaiQuyet {
	QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh = new QuaTrinhXuLyGiaiQuyetServiceUtil();
	DonThuServiceUtil svDonThu = new DonThuServiceUtil();
	LienThongLocalServicesUtil svLienThong = new LienThongLocalServicesUtil();
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	public LienThongQuyetDinhGiaiQuyet() {
		// TODO Auto-generated constructor stub
	}
	
	public void executeLienThong(int idDonThu,long orgId) throws IOException, SQLException
	{
		QuyetDinhGiaiQuyetBean modelQDGQ = svQuaTrinh.getQuyetDinhGiaiQuyet(idDonThu);
		long soHoSo = modelQDGQ.getMaQuyetDinhGiaiQuyet();
		String Id = svLienThong.getIdLienThongOfDonThuOnThongTinDon(idDonThu, orgId);
		String maCoQuanBanHanh = modelQDGQ.getMaCoQuanBanHanh();
		String ngayBanHanh = sdf.format(modelQDGQ.getNgayBanHanh());
		String fileBanHanh = KNTCBaseFunction.encodeFileToBase64Binary(UploadServiceUtil.getAbsolutePath()+File.separator+UploadServiceUtil.getFolderNameDonThu()+File.separator+idDonThu+File.separator+modelQDGQ.getLinkFileDinhKem());
		String tenFileBanHanh =modelQDGQ.getTenFileDinhKem();
		String tomTatNoiDungGiaiQuyet = modelQDGQ.getTomTatNoiDung();
		int loaiKetQuaGiaiQuyet = modelQDGQ.getLoaiKetQuaGiaiQuyet();
		int hinhThucGiaiQuyet = modelQDGQ.getHinhThucGiaiQuyet();
		int quyenKhoiKien = modelQDGQ.isQuyenKhoiKien()==true?1:0;
		
		System.out.println("----Quyết định giải quyết----");
		System.out.println("Id Liên thông: "+Id);
		System.out.println("Mã cơ quan ban hành: "+maCoQuanBanHanh);
		System.out.println("Ngày ban hành: "+ngayBanHanh);
		System.out.println("Tên file ban hành: "+tenFileBanHanh);
		System.out.println("Tóm tắt nội dung giải quyết: "+tomTatNoiDungGiaiQuyet);
		System.out.println("Loại kết quả giải quyết: "+loaiKetQuaGiaiQuyet);
		System.out.println("Hình thức giải quyết: "+hinhThucGiaiQuyet);
		System.out.println("Quyền khởi kiện"+quyenKhoiKien);
		
		BoiThuongThietHaiType boiThuongThietHai = new BoiThuongThietHaiType();
		int thuHoiTien=modelQDGQ.getThuHoiTien();
		int thuHoiDatO=modelQDGQ.getThuHoiDatO();
		int thuHoiDatSX=modelQDGQ.getThuHoiDatSX();
		int traLaiTien=modelQDGQ.getTraLaiTien();
		int traLaiDatO=modelQDGQ.getTraLaiDatO();
		int traLaiDatSX=modelQDGQ.getThuHoiDatSX();
		int soNguoiChuyenDieuTra=modelQDGQ.getSoNguoiChuyenDieuTra();
		int soNguoiDuocHuongQuyenLoi=modelQDGQ.getSoNguoiDuocTraQuyenLoi();
		int xuPhatHanhChinh=modelQDGQ.getXuPhatHanhChinh();
		
		boiThuongThietHai.setThuHoiTien(thuHoiTien);
		boiThuongThietHai.setThuHoiDatO(thuHoiDatO);
		boiThuongThietHai.setThuHoiDatSX(thuHoiDatSX);
		boiThuongThietHai.setTraLaiTien(traLaiTien);
		boiThuongThietHai.setTraLaiDatO(traLaiDatO);
		boiThuongThietHai.setThuHoiDatSX(traLaiDatSX);
		boiThuongThietHai.setSoNguoiChuyenDieuTra(soNguoiChuyenDieuTra);
		boiThuongThietHai.setSoNguoiDuocTraQuyenLoi(soNguoiDuocHuongQuyenLoi);
		boiThuongThietHai.setXuPhatHanhChinh(xuPhatHanhChinh);
		
		System.out.println("Thu hồi tiền, đất ở, đất sản xuất:"+thuHoiTien+","+thuHoiDatO+","+thuHoiDatSX);
		System.out.println("Trả lại tiền, đất ở, đất sản xuất:"+traLaiTien+","+traLaiDatO+","+traLaiDatSX);
		System.out.println("Số người chuyển điều tra: "+soNguoiChuyenDieuTra);
		System.out.println("Số người được trả quyền lợi: "+soNguoiDuocHuongQuyenLoi);
		System.out.println("Xử phạt hành chính: "+xuPhatHanhChinh);
		
		List<CaNhanBiXuLyHanhChinhType> caNhanBiXuLyHanhChinh = new ArrayList();
		List<ToChucBiXuLyHanhChinhType> toChucBiXuLyHanhChinh = new ArrayList();
		
		List<DoiTuongBiXuLyHanhChinhBean> listDoiTuongBiXuLyHanhChinh = svQuaTrinh.getDoiTuongBiXuLyHanhChinhList(modelQDGQ.getMaQuyetDinhGiaiQuyet());
		System.out.println("----Đối tượng bị xử phạt hành chính----"+listDoiTuongBiXuLyHanhChinh.size());
		for(DoiTuongBiXuLyHanhChinhBean modelDoiTuong : listDoiTuongBiXuLyHanhChinh)
		{
			if(modelDoiTuong.isCaNhan())
			{
				System.out.println("Cá nhân:");
				CaNhanBiXuLyHanhChinhType caNhan = new CaNhanBiXuLyHanhChinhType();
				String hoTen = modelDoiTuong.getTenDoiTuong();
				String hinhThucXuLy = modelDoiTuong.getHinhThucXuLy();
				String chucVu = modelDoiTuong.getChucVu();
				caNhan.setHoTen(hoTen);
				caNhan.setHinhThucXuLy(hinhThucXuLy);
				caNhan.setChucVu(chucVu);
				System.out.println("Họ tên"+hoTen);
				System.out.println("Hình thức xử lý: "+hinhThucXuLy);
				System.out.println("Chức vụ: "+chucVu);
				
				caNhanBiXuLyHanhChinh.add(caNhan);
			}
			else
			{
				System.out.println("Tổ chức:");
				ToChucBiXuLyHanhChinhType toChuc = new ToChucBiXuLyHanhChinhType();
				String tenToChuc = modelDoiTuong.getTenDoiTuong();
				String hinhThucXuLy = modelDoiTuong.getHinhThucXuLy();
				toChuc.setTenToChuc(tenToChuc);
				toChuc.setHinhThucXuLy(hinhThucXuLy);
				System.out.println("Tên tổ chức: "+tenToChuc);
				System.out.println("Hình thức xử lý: "+hinhThucXuLy);
				toChucBiXuLyHanhChinh.add(toChuc);
			}
		}
		try {
			LTTTCP.capNhatKQGiaiQuyet(
					soHoSo,
					Id, 
					maCoQuanBanHanh, 
					ngayBanHanh, 
					fileBanHanh, 
					tenFileBanHanh, 
					tomTatNoiDungGiaiQuyet, 
					loaiKetQuaGiaiQuyet, 
					hinhThucGiaiQuyet, 
					quyenKhoiKien, 
					boiThuongThietHai, 
					caNhanBiXuLyHanhChinh, 
					toChucBiXuLyHanhChinh);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
