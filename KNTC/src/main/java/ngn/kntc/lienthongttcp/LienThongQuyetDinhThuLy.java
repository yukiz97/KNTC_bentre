package ngn.kntc.lienthongttcp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import ngn.kntc.beans.DonThuBean;
import ngn.kntc.beans.KetQuaXuLyBean;
import ngn.kntc.beans.QuyetDinhThuLyBean;
import ngn.kntc.modules.KNTCBaseFunction;
import ngn.kntc.ttcp.LTTTCP;
import ngn.kntc.ttcp.model.HuongXuLyType;
import ngn.kntc.ttcp.model.NoiDungXuLy;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.LiferayServiceUtil;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;
import ngn.kntc.utils.UploadServiceUtil;

import com.fasterxml.jackson.core.JsonProcessingException;

public class LienThongQuyetDinhThuLy {
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	DonThuServiceUtil svDonThu = new DonThuServiceUtil();
	QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh = new QuaTrinhXuLyGiaiQuyetServiceUtil();
	LienThongLocalServicesUtil svLienThong = new LienThongLocalServicesUtil();
	public LienThongQuyetDinhThuLy() {
		// TODO Auto-generated constructor stub
	}
	
	public void executeLienThong(int idDonThu,long orgTao) throws Exception
	{
		QuyetDinhThuLyBean modelQDTL = svQuaTrinh.getQuyetDinhThuLy(idDonThu);
		String id = svLienThong.getIdLienThongOfDonThuOnThongTinDon(idDonThu, orgTao);
		String maCoQuanThuLy = LiferayServiceUtil.getOrgCustomFieldValue(Long.parseLong(modelQDTL.getMaCoQuanThuLy()),"OrgLienThongId");
		String ngayThuLy =sdf.format(modelQDTL.getNgayThuLy());
		String fileThuLy = KNTCBaseFunction.encodeFileToBase64Binary(UploadServiceUtil.getAbsolutePath()+File.separator+UploadServiceUtil.getFolderNameDonThu()+File.separator+idDonThu+File.separator+modelQDTL.getLinkFileDinhKem());
		String tenFileThuLy = modelQDTL.getTenFileDinhKem();
		String hanGiaiQuyet = sdf.format(modelQDTL.getHanGiaiQuyet());
		String canBoDuyet = modelQDTL.getCanBoDuyet();
		
		System.out.println("----Quyết định thụ lý----");
		System.out.println("Id Liên thông: "+id);
		System.out.println("Mã cơ quan thụ lý: "+maCoQuanThuLy);
		System.out.println("Ngày thụ lý: "+ngayThuLy);
		System.out.println("Tên file đính kèm: "+tenFileThuLy);
		System.out.println("Hạn giải quyết: "+hanGiaiQuyet);
		System.out.println("Cán bộ duyệt: "+canBoDuyet);
		try {
			LTTTCP.capNhatQuyetDinhThuLy(id, maCoQuanThuLy, ngayThuLy, fileThuLy, tenFileThuLy, hanGiaiQuyet, canBoDuyet,modelQDTL.getIdQuyetDinhThuLy());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
