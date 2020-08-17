package ngn.kntc.lienthongttcp;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import ngn.kntc.beans.DonThuBean;
import ngn.kntc.beans.ThiHanhQuyetDinhBean;
import ngn.kntc.ttcp.LTTTCP;
import ngn.kntc.ttcp.model.ThiHanhGiaiQuyetType;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;

public class LienThongThiHanhGiaiQuyet {
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	DonThuServiceUtil svDonThu = new DonThuServiceUtil();
	QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh = new QuaTrinhXuLyGiaiQuyetServiceUtil();
	LienThongLocalServicesUtil svLienThong = new LienThongLocalServicesUtil();
	public LienThongThiHanhGiaiQuyet() {
		// TODO Auto-generated constructor stub
	}
	
	public void executeLienThong(int idDonThu,int idQuyetDinh,long orgId) throws Exception
	{
		List<ThiHanhQuyetDinhBean> listTHGQ = svQuaTrinh.getThiHanhGiaiQuyetNotLienThongList(idQuyetDinh);
		
		for(ThiHanhQuyetDinhBean modelTHGQ : listTHGQ)
		{
			long idThiHanhGiaiQuyet = modelTHGQ.getIdThiHanhQuyetDinh();
			String id = svLienThong.getIdLienThongOfDonThuOnThongTinDon(idDonThu, orgId);
			String idKetQua = svQuaTrinh.getStringFieldValueOfTable("don_thu_quyet_dinh_giai_quyet", "mattcp", "IdQuyetDinhGiaiQuyet", modelTHGQ.getIdQuyetDinhGiaiQuyet());
			String thiHanhId = "";
			ThiHanhGiaiQuyetType thiHanhGiaiQuyet = new ThiHanhGiaiQuyetType();
			String maCoQuanThiHanh=modelTHGQ.getMaCoQuanThiHanh();
			String ngayCapNhat=sdf.format(modelTHGQ.getNgayCapNhat());
			int thuHoiTien=modelTHGQ.getThuHoiTien();
			int thuHoiDatO=modelTHGQ.getThuHoiDatO();
			int thuHoiDatSX=modelTHGQ.getThuHoiDatSX();
			int traLaiTien=modelTHGQ.getTraLaiTien();
			int traLaiDatO=modelTHGQ.getTraLaiDatO();
			int traLaiDatSX=modelTHGQ.getTraLaiDatSX();
			int soNguoiBiXuLy=modelTHGQ.getSoNguoiBiXuLy();
			int soTapTheBiXuLy=modelTHGQ.getSoTapTheBiXuLy();
			int taiSanQuyRaTien=modelTHGQ.getTaiSanQuyRaTien();
			int soTienNopPhat=modelTHGQ.getSoTienNopPhat();
			int soDoiTuongDaKhoiTo=modelTHGQ.getSoDoiTuongBiKhoiTo();
			
			thiHanhGiaiQuyet.setMaCoQuanThiHanh(maCoQuanThiHanh);
			thiHanhGiaiQuyet.setNgayCapNhat(ngayCapNhat);
			thiHanhGiaiQuyet.setThuHoiTien(thuHoiTien);
			thiHanhGiaiQuyet.setThuHoiDatO(thuHoiDatO);
			thiHanhGiaiQuyet.setThuHoiDatSX(thuHoiDatSX);
			thiHanhGiaiQuyet.setTraLaiTien(traLaiTien);
			thiHanhGiaiQuyet.setTraLaiDatO(traLaiDatO);
			thiHanhGiaiQuyet.setTraLaiDatSX(traLaiDatSX);
			thiHanhGiaiQuyet.setSoNguoiBiXuLy(soNguoiBiXuLy);
			thiHanhGiaiQuyet.setSoTapTheBiXuLy(soTapTheBiXuLy);
			thiHanhGiaiQuyet.setTaiSanQuyRaTien(taiSanQuyRaTien);
			thiHanhGiaiQuyet.setSoTienNopPhat(soTienNopPhat);
			thiHanhGiaiQuyet.setSoDoiTuongDaKhoiTo(soDoiTuongDaKhoiTo);
			
			System.out.println("----Thi hành giải quyết----");
			System.out.println("Id Liên Thông: "+id);
			System.out.println("Id Quyết định giải quyết liên thông: "+idKetQua);
			System.out.println("Id Quyết định giải quyết: "+modelTHGQ.getIdQuyetDinhGiaiQuyet());
			System.out.println("Id thi hành: "+idThiHanhGiaiQuyet);
			System.out.println("Ngày cập nhật: "+ngayCapNhat);
			System.out.println("Thu hồi tiền: "+thuHoiTien);
			System.out.println("Thu hồi đất ở: "+thuHoiDatO);
			System.out.println("Thu hồi đất sản xuất: "+thuHoiDatSX);
			System.out.println("Trả lại tiền: "+traLaiTien);
			System.out.println("Trả lại đất ở: "+traLaiDatO);
			System.out.println("Trả lại đất sản xuất:"+traLaiDatSX);
			System.out.println("Số người bị xử lý: "+soNguoiBiXuLy);
			System.out.println("Số tập thể bị xử lý: "+soTapTheBiXuLy);
			System.out.println("Tài sản nộp phạt: "+taiSanQuyRaTien);
			System.out.println("Số tiền nộp phạt: "+soTienNopPhat);
			System.out.println("Số đổi tượng đã khởi tố: "+soDoiTuongDaKhoiTo);
			try {
				LTTTCP.capNhatThiHanh(idThiHanhGiaiQuyet, id, idKetQua, thiHanhId, thiHanhGiaiQuyet);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
