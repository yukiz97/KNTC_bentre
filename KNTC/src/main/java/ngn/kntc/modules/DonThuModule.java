package ngn.kntc.modules;

import java.sql.SQLException;
import java.util.HashMap;

import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.enums.LoaiDonThuEnum;
import ngn.kntc.enums.LoaiNguoiDiKNTCEnum;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class DonThuModule {
	public static String requiredMark = "<span style='color: red'>(*)</span>";

	public static HorizontalLayout buildFormLayoutSingle(String caption,Component component,String captionWidth)
	{
		HorizontalLayout hLayout = new HorizontalLayout();
		Label lbl1 = new Label(caption);
		lbl1.setContentMode(ContentMode.HTML);
		hLayout.addComponents(lbl1,component);

		lbl1.addStyleName("single-form-caption");
		if(captionWidth!=null)
			lbl1.setWidth(captionWidth);
		else
			lbl1.setWidthUndefined();
		hLayout.setExpandRatio(component, 1.0f);

		hLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		hLayout.addStyleName("single-form-layout");
		hLayout.setWidth("100%");
		return hLayout;
	}

	public static String returnDiaChiChiTiet(String diaChiChiTiet,String maTinh, String maHuyen, String maXa) throws SQLException
	{
		String diaChi = "";

		diaChi+=(diaChiChiTiet!=null && !diaChiChiTiet.isEmpty())?diaChiChiTiet+", ":"";

		if(maTinh!=null)
			diaChi+=DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.diagioi.getName(), maTinh).getName()+", ";
		if(maHuyen!=null)
			diaChi+=DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.diagioi.getName(), maHuyen).getName()+", ";
		if(maXa!=null)
			diaChi+=DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.diagioi.getName(), maXa).getName()+", ";
		
		diaChi = diaChi.trim();
		
		if(diaChi.length()>=2)
		{
			diaChi = diaChi.substring(0,diaChi.length()-1);
		}
		
		return diaChi;
	}

	public static String returnLoaiNguoiDiKNTCForTable(int loaiNguoi,int soNguoiDiKNTC,int soNguoiDaiDien)
	{
		String output = "";

		for(LoaiNguoiDiKNTCEnum e : LoaiNguoiDiKNTCEnum.values())
		{
			if(e.getType() == loaiNguoi)
			{
				output += "<p style='margin: 0px 0px 5px 0px'><b style='color: #b31717'>"+e.getName()+"</b></p>";
			}
		}
		//output+="<b>Số người:</b> "+soNguoiDiKNTC+" / <b>Đại diện:</b> "+soNguoiDaiDien;

		return output;
	}
	
	public static String returnLoaiNguoiDiKNTCForExcel(int loaiNguoi,int soNguoiDiKNTC,int soNguoiDaiDien)
	{
		String output = "";

		for(LoaiNguoiDiKNTCEnum e : LoaiNguoiDiKNTCEnum.values())
		{
			if(e.getType() == loaiNguoi)
			{
				output += e.getName()+"\n";
			}
		}
		output+="Số người: "+soNguoiDiKNTC+" / Đại diện: "+soNguoiDaiDien;

		return output;
	}

	public static HashMap<String,String> getLinhVucLevel(String id)
	{
		HashMap<String,String> list = new HashMap<>();

		if(id.length() > 8)
		{
			list.put("lv4", id);
			id = id.substring(0, id.length()-3);
		}
		if(id.length() > 5)
		{
			list.put("lv3", id);
			id = id.substring(0, id.length()-3);
		}
		list.put("lv2", id);

		return list;
	}

	public static void insertThongBao(int idDonThu,int idQuaTrinh) throws SQLException
	{
		QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh = new QuaTrinhXuLyGiaiQuyetServiceUtil();

		for(long n : svQuaTrinh.getDonThuUsers(idDonThu))
		{
			svQuaTrinh.insertThongBao(n, idQuaTrinh);
		}
	}

	public static String returnLoaiDonThuNameOnKey(int key)
	{
		String value = "";

		for(LoaiDonThuEnum e : LoaiDonThuEnum.values())
		{
			if(e.getType()==key)
				value = e.getName();
		}

		return value;
	}

	public static void returnComboboxLoaiDonThu(ComboBox cmb,boolean haveAll)
	{
		if(haveAll)
		{
			cmb.addItem(0);
			cmb.setItemCaption(0, "Tất cả");

			cmb.select(0);
		}
		for(LoaiDonThuEnum e : LoaiDonThuEnum.values())
		{
			cmb.addItem(e.getType());
			cmb.setItemCaption(e.getType(),e.getName());
		}
		cmb.setNullSelectionAllowed(false);
	}
}
