package ngn.kntc.lienthongttcp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import ngn.kntc.beans.DonThuBean;
import ngn.kntc.beans.KetQuaXuLyBean;
import ngn.kntc.modules.KNTCBaseFunction;
import ngn.kntc.ttcp.LTTTCP;
import ngn.kntc.ttcp.model.HuongXuLyType;
import ngn.kntc.ttcp.model.NoiDungXuLy;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;
import ngn.kntc.utils.UploadServiceUtil;

import com.fasterxml.jackson.core.JsonProcessingException;

public class LienThongKetQuaXuLy {
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	DonThuServiceUtil svDonThu = new DonThuServiceUtil();
	QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh = new QuaTrinhXuLyGiaiQuyetServiceUtil();
	LienThongLocalServicesUtil svLienThong = new LienThongLocalServicesUtil();
	
	public void executeLienThong(int idDonThu,long orgId) throws Exception
	{
		KetQuaXuLyBean modelKQXL = svQuaTrinh.getKetQuaXuLy(idDonThu, orgId);
		System.out.println("IdKQXL: "+modelKQXL.getIdKetQua());
		String id = svLienThong.getIdLienThongOfDonThuOnThongTinDon(idDonThu, orgId);
		
		String canBoDuyet=modelKQXL.getCanBoDuyet().split(Pattern.quote("-"))[0].trim();
		String canBoXuLy=modelKQXL.getCanBoXuLy();
		int donKhongDuDKXL=svDonThu.getIntFieldValueOfDonThu("DonKhongDuDieuKienXL", idDonThu)==1?0:1;
		int donNacDanh=svDonThu.getIntFieldValueOfDonThu("DonNacDanh", idDonThu);
		String ngayXuLy=sdf.format(modelKQXL.getNgayXuLy());
		String tomTatNoiDungXL=modelKQXL.getNoiDungXuLy();
		NoiDungXuLy noiDungXuLy = new NoiDungXuLy();
		noiDungXuLy.setCanBoDuyetKQXL(canBoDuyet);
		noiDungXuLy.setCanBoXuLy(canBoXuLy);
		noiDungXuLy.setDonDuDKXuLy(donKhongDuDKXL);
		noiDungXuLy.setDonNacDanh(donNacDanh);
		noiDungXuLy.setNgayXuLy(ngayXuLy);
		noiDungXuLy.setTomTatNoiDungXuLy(tomTatNoiDungXL);
		
		System.out.println("----Kết quả xử lý----");
		System.out.println("Id liên thông: "+id);
		System.out.println("Cán bộ duyệt: "+canBoDuyet);
		System.out.println("Cán bộ xử lý: "+canBoXuLy);
		System.out.println("Đơn đủ dkxl: "+donKhongDuDKXL);
		System.out.println("Đơn nặc danh: "+donNacDanh);
		System.out.println("Ngày xử lý: "+ngayXuLy);
		System.out.println("Tóm tắt nội dung xử lý: "+tomTatNoiDungXL);
		
		String maHuongXuLy=modelKQXL.getMaHuongXuLy()+"";
		String coQuanXuLy=modelKQXL.getMaCQXL();
		String maCoQuanXuLyTiep=modelKQXL.getMaCQXLTiep();
		String strNoiDungXuLy=modelKQXL.getNoiDungXuLy();
		String tenFileDinhKem=modelKQXL.getTenFileDinhKem();
		String fileDinhKem=KNTCBaseFunction.encodeFileToBase64Binary(UploadServiceUtil.getAbsolutePath()+File.separator+UploadServiceUtil.getFolderNameDonThu()+File.separator+idDonThu+File.separator+modelKQXL.getLinkFileDinhKem());
		
		HuongXuLyType huongXuLyType = new HuongXuLyType();
		huongXuLyType.setMaHuongXuLy(maHuongXuLy);
		huongXuLyType.setMaCoQuanXuLy(coQuanXuLy);
		huongXuLyType.setMaCoQuanXuLyTiep(maCoQuanXuLyTiep);
		huongXuLyType.setNoiDungXuLy(strNoiDungXuLy);
		huongXuLyType.setTenFileDinhKem(tenFileDinhKem);
		huongXuLyType.setFileDinhKem(fileDinhKem);
		noiDungXuLy.setHuongXuLy(huongXuLyType);
		
		System.out.println("Mã hướng xử lý: "+maHuongXuLy);
		System.out.println("Cơ quan xử lý: "+coQuanXuLy);
		System.out.println("Cơ quan xử lý tiếp: "+maCoQuanXuLyTiep);
		System.out.println("Nội dung xử lý: "+strNoiDungXuLy);
		System.out.println("Tên file đính kèm: "+tenFileDinhKem);
		try {
			LTTTCP.capNhatKQXuLy(id, noiDungXuLy,modelKQXL.getIdKetQua());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
