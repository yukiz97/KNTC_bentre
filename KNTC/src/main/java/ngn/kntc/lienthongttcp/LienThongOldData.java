package ngn.kntc.lienthongttcp;

import java.sql.SQLException;
import java.util.List;

import ngn.kntc.beans.LienThongIdDonThuAndIdQuyetDinhAndOrgTaoTHGQ;
import ngn.kntc.beans.LienThongIdDonThuAndOrgNhanBean;
import ngn.kntc.beans.LienThongIdDonThuAndOrgTaoKQXLBean;
import ngn.kntc.beans.LienThongIdDonThuAndOrgTaoQDGQBean;
import ngn.kntc.beans.LienThongIdDonThuAndOrgThuLyQDTLBean;
import ngn.kntc.utils.DonThuServiceUtil;

public class LienThongOldData {
	static DonThuServiceUtil svDonThu = new DonThuServiceUtil();
	static LienThongLocalServicesUtil svLienThong = new LienThongLocalServicesUtil();
	
	public LienThongOldData() {
		
	}
	
	public static void main(String[] args) {
		try {
			lienThongDonThu();
			//lienThongKetQuaXuLy();
			//lienThongQuyetDinhThuLy();
			//lienThongQuyetDinhGiaiQuyet();
			//lienThongThiHanhGiaiQuyet();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void lienThongDonThu() throws NumberFormatException, Exception
	{
		List<LienThongIdDonThuAndOrgNhanBean> listIdDonThu = svLienThong.getAllIdDonThuInThongTinDonNotLienThong();
		
		System.out.println("****Liên thông vụ việc cũ****");
		for(LienThongIdDonThuAndOrgNhanBean model : listIdDonThu)
		{
			int idDonThu = model.getIdDonThu();
			long orgTao = model.getOrgTao();
			String idLienThongGanVuViec = null;
			String ganVuViec = svDonThu.getDonThu(model.getIdDonThu()).getGanVuViec();
			if(ganVuViec!=null)
			{
				idLienThongGanVuViec = svDonThu.getDonThu(Integer.parseInt(ganVuViec)).getIdHoSoLienThong();
			}
			System.out.println("IdDonThu: "+idDonThu);
			System.out.println("OrgTao: "+orgTao);
			new LienThongVuViec().executeLienThong(idDonThu, orgTao, idLienThongGanVuViec, "save");
		}
	}
	
	public static void lienThongKetQuaXuLy() throws Exception
	{
		List<LienThongIdDonThuAndOrgTaoKQXLBean> listIdKQXL = svLienThong.getAllIdKQXLNotLienThong();
		
		System.out.println("****Liên thông kết quả xử lý****");
		for(LienThongIdDonThuAndOrgTaoKQXLBean model : listIdKQXL)
		{
			int idDonThu = model.getIdDonThu();
			long orgTao = model.getOrgTao();
			
			System.out.println("IdDonThu: "+idDonThu);
			System.out.println("OrgTao: "+orgTao);
			
			new LienThongKetQuaXuLy().executeLienThong(idDonThu, orgTao);
		}
	}
	
	public static void lienThongQuyetDinhThuLy() throws Exception
	{
		List<LienThongIdDonThuAndOrgThuLyQDTLBean> listIdQDTL = svLienThong.getAllIdQDTLNotLienThong();
		
		System.out.println("****Liên thông quyết định thụ lý****");
		for(LienThongIdDonThuAndOrgThuLyQDTLBean model : listIdQDTL)
		{
			int idDonThu = model.getIdDonThu();
			long orgTao = model.getOrgThuLy();
			
			System.out.println("IdDonThu: "+idDonThu);
			System.out.println("OrgTao: "+orgTao);
			
			new LienThongQuyetDinhThuLy().executeLienThong(model.getIdDonThu(), model.getOrgThuLy());
		}
	}
	
	public static void lienThongQuyetDinhGiaiQuyet() throws Exception
	{
		List<LienThongIdDonThuAndOrgTaoQDGQBean> listIdQDGQ = svLienThong.getAllIdQDGQNotLienThong();
		
		System.out.println("****Liên quyết định giải quyết****");
		for(LienThongIdDonThuAndOrgTaoQDGQBean model : listIdQDGQ)
		{
			int idDonThu = model.getIdDonThu();
			long orgTao = model.getOrgTao();
			
			System.out.println("IdDonThu: "+idDonThu);
			System.out.println("OrgTao: "+orgTao);
			
			//new LienThongQuyetDinhGiaiQuyet().executeLienThong(model.getIdDonThu(), model.getOrgTao());
		}
	}
	
	public static void lienThongThiHanhGiaiQuyet() throws Exception
	{
		List<LienThongIdDonThuAndIdQuyetDinhAndOrgTaoTHGQ> listTHGQ = svLienThong.getAllIdTHGQNotLienThong();
		
		System.out.println("****Liên thông thi hành giải quyết****");
		for(LienThongIdDonThuAndIdQuyetDinhAndOrgTaoTHGQ model : listTHGQ)
		{
			int idDonThu = model.getIdDonThu();
			int idQuyetDinh = model.getIdQuyetDinh();
			long orgTao = model.getOrgTao();
			
			System.out.println("IdDonThu: "+idDonThu);
			System.out.println("IdQuyetDinh: "+idQuyetDinh);
			System.out.println("OrgTao: "+orgTao);
			
			//new LienThongThiHanhGiaiQuyet().executeLienThong(model.getIdDonThu(), model.getIdQuyetDinh(), model.getOrgTao());
		}
	}
}
