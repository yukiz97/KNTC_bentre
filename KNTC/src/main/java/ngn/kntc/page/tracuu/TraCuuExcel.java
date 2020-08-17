package ngn.kntc.page.tracuu;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ngn.kntc.beans.DonThuBean;
import ngn.kntc.beans.KetQuaXuLyBean;
import ngn.kntc.beans.QuyetDinhGiaiQuyetBean;
import ngn.kntc.beans.QuyetDinhThuLyBean;
import ngn.kntc.beans.ThongTinDonThuBean;
import ngn.kntc.beans.VanBanXuLyGiaiQuyetBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.enums.LoaiDonThuEnum;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.modules.KNTCProps;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.LiferayServiceUtil;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;
import ngn.kntc.utils.SessionUtil;
import ngn.kntc.utils.UploadServiceUtil;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.liferay.portal.model.User;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;

public class TraCuuExcel {
	FileInputStream fileExcel;
	XSSFWorkbook workbook;
	XSSFSheet sheetTongquan;

	DonThuServiceUtil svDonThu = new DonThuServiceUtil();
	QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh = new QuaTrinhXuLyGiaiQuyetServiceUtil();
	DanhMucServiceUtil svDanhMuc = new DanhMucServiceUtil();

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	public TraCuuExcel(Date startDate,Date endDate,String dateType,List<DonThuBean> listDonThu) {
		try {
			String canBoXuat = "";
			String donVi = "";
			String subDonVi = "";

			fileExcel=  new FileInputStream(new File(UploadServiceUtil.getAbsolutePath()+File.separator+"maudon"+File.separator+"TraCuuTemplate.xlsx"));
			workbook = new XSSFWorkbook(fileExcel);
			sheetTongquan = workbook.getSheetAt(0);
			Row row = null;
			Cell cell = null;

			CellStyle cellStyleGeneral = cellStyle(workbook, false);
			CellStyle cellStyleGeneralCenter = cellStyle(workbook, true);
			CellStyle cellStyleNgayThang = cellStyleNgayThang(workbook);
			CellStyle cellStyleKyTenBold = cellStyleKyTen(workbook, true);
			CellStyle cellStyleKyTen = cellStyleKyTen(workbook, false);

			if(SessionUtil.getLienThongOrgId().equalsIgnoreCase("H811.01"))
			{
				donVi+="VP "+OrganizationLocalServiceUtil.getOrganization(SessionUtil.getMasterOrgId()).getName().toUpperCase();
				subDonVi+="BAN TIẾP CÔNG DÂN";
			}
			else
			{
				donVi+=OrganizationLocalServiceUtil.getOrganization(SessionUtil.getMasterOrgId()).getName().toLowerCase();
				subDonVi+=OrganizationLocalServiceUtil.getOrganization(SessionUtil.getOrgId()).getName().toLowerCase();
			}
			row = sheetTongquan.getRow(1);
			cell = row.getCell(1);
			cell.setCellValue(donVi);
			cell.setCellStyle(cellStyleKyTen);
			
			row = sheetTongquan.getRow(2);
			cell = row.getCell(1);
			cell.setCellValue(subDonVi);
			cell.setCellStyle(cellStyleKyTenBold);

			if(dateType!=null)
			{
				if(dateType=="ngaynhapdon")
					canBoXuat+="Ngày nhập đơn";
				if(dateType=="ngaythuly")
					canBoXuat+="Ngày nhận đơn";
				if(dateType=="ngaygiaiquyet")
					canBoXuat+="Ngày giải quyết";
				canBoXuat+= " (tính từ ngày "+sdf.format(startDate)+" đến ngày "+sdf.format(endDate)+")";
			}

			row = sheetTongquan.getRow(5);
			cell = row.getCell(0);
			cell.setCellValue(canBoXuat);
			cell.setCellStyle(cellStyleNgayThang);

			int startLine = 10,i=1;

			for(DonThuBean model : listDonThu)
			{
				ThongTinDonThuBean modelThongTinDonThuBean = svDonThu.getThongTinDonThu(model.getMaDonThu(), SessionUtil.getOrgId());

				String nguoiDungDon = DonThuModule.returnLoaiNguoiDiKNTCForExcel(model.getLoaiNguoiDiKNTC(), model.getSoNguoiDiKNTC(), model.getSoNguoiDaiDien())+"\n"+DonThuServiceUtil.returnTenNguoiDaiDienDonThu(model).replaceAll("<[^>]*>", "");
				String loaiDonThu = "";
				String linhVuc = "";
				String theoDoiKetQuaGiaiQuyet = "";
				for(LoaiDonThuEnum e : LoaiDonThuEnum.values())
				{
					if(e.getType()==model.getLoaiDonThu())
						loaiDonThu = e.getName();
				}
				for(String idLinhVuc : svDonThu.getLinhVucList(model.getMaDonThu()))
				{
					HashMap<String, String> lv = DonThuModule.getLinhVucLevel(idLinhVuc);
					try {
						linhVuc+="- ";
						if(lv.containsKey("lv2"))
							linhVuc+=svDanhMuc.getLinhVuc(lv.get("lv2")).getName()+" / ";
						if(lv.containsKey("lv3"))
							linhVuc+=svDanhMuc.getLinhVuc(lv.get("lv3")).getName()+" / ";
						if(lv.containsKey("lv4"))
							linhVuc+=svDanhMuc.getLinhVuc(lv.get("lv3")).getName();
						linhVuc+="\n";
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}

				//theo doi ket qua giai quyet
				QuyetDinhGiaiQuyetBean modelQDGQ = svQuaTrinh.getQuyetDinhGiaiQuyet(model.getMaDonThu());
				if(modelQDGQ!=null)
				{
					theoDoiKetQuaGiaiQuyet+="+ Quyết định giải quyết ban hành ngày "+sdf.format(modelQDGQ.getNgayBanHanh())+": "+modelQDGQ.getTomTatNoiDung();
				}
				else
				{
					QuyetDinhThuLyBean modelQDTL = svQuaTrinh.getQuyetDinhThuLy(model.getMaDonThu());
					if(modelQDTL!=null)
					{
						theoDoiKetQuaGiaiQuyet+="+ Quyết định thụ lý ban hành ngày "+sdf.format(modelQDTL.getNgayThuLy())+" - hạn giải quyết "+sdf.format(modelQDTL.getHanGiaiQuyet());
					}
					else
					{
						KetQuaXuLyBean modelKQXLTmp = svQuaTrinh.getKetQuaXuLy(model.getMaDonThu(), SessionUtil.getOrgId());

						if(modelKQXLTmp!=null)
							theoDoiKetQuaGiaiQuyet+="+ Kết quả xử lý ban hành ngày "+sdf.format(modelKQXLTmp.getNgayXuLy())+" - "+modelKQXLTmp.getNoiDungXuLy();
					}
				}
				List<VanBanXuLyGiaiQuyetBean> listVanBan = svQuaTrinh.getVanBanXuLyGiaiQuyetOfDonThu(model.getMaDonThu(), 1);
				int j = 1;
				for(VanBanXuLyGiaiQuyetBean modelVanBan : listVanBan)
				{
					theoDoiKetQuaGiaiQuyet+="\n+ Số hiệu văn bản: "+modelVanBan.getSoVanBan()+" - nội dung: "+modelVanBan.getNoiDungVanBan();
					if(j==3)
						break;
					j++;
				}

				row = sheetTongquan.createRow(startLine++);
				cell = row.createCell(0);
				cell.setCellValue(i++);
				cell.setCellStyle(cellStyleGeneralCenter);

				cell = row.createCell(1);
				cell.setCellValue(nguoiDungDon);
				cell.setCellStyle(cellStyleGeneral);

				cell = row.createCell(2);
				cell.setCellValue(model.getNoiDungDonThu());
				cell.setCellStyle(cellStyleGeneral);

				cell = row.createCell(3);
				cell.setCellValue(loaiDonThu);
				cell.setCellStyle(cellStyleGeneralCenter);

				cell = row.createCell(4);
				cell.setCellValue(linhVuc);
				cell.setCellStyle(cellStyleGeneral);

				cell = row.createCell(5);
				cell.setCellValue(sdf.format(model.getNgayNhapDon()));
				cell.setCellStyle(cellStyleGeneralCenter);

				cell = row.createCell(6);
				cell.setCellValue(DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.nguondon.getName(),modelThongTinDonThuBean.getNguonDonDen()).getName());
				cell.setCellStyle(cellStyleGeneralCenter);

				cell = row.createCell(7);
				cell.setCellValue(theoDoiKetQuaGiaiQuyet);
				cell.setCellStyle(cellStyleGeneralCenter);
			}

			row = sheetTongquan.createRow(++startLine);
			sheetTongquan.addMergedRegion(new CellRangeAddress(startLine,startLine,5,7));
			cell = row.createCell(5);
			cell.setCellValue("Bến Tre, Ngày "+Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+" tháng "+Calendar.getInstance().get(Calendar.MONTH)+" năm "+Calendar.getInstance().get(Calendar.YEAR));
			cell.setCellStyle(cellStyleKyTen);

			row = sheetTongquan.createRow(++startLine);
			sheetTongquan.addMergedRegion(new CellRangeAddress(startLine,startLine+=2,5,7));
			cell = row.createCell(5);
			cell.setCellValue("KT. TRƯỞNG BAN \n PHÓ TRƯỞNG BAN");
			cell.setCellStyle(cellStyleKyTenBold);

			/*row = sheetTongquan.createRow(++startLine);
			sheetTongquan.addMergedRegion(new CellRangeAddress(startLine,startLine,5,7));
			cell = row.createCell(6);
			cell.setCellValue("PHÓ TRƯỞNG BAN");
			cell.setCellStyle(cellStyleKyTenBold);*/

			List<User> listLanhDao = LiferayServiceUtil.returnListLanhDao();
			row = sheetTongquan.createRow(startLine+=5);
			sheetTongquan.addMergedRegion(new CellRangeAddress(startLine,startLine,5,7));
			cell = row.createCell(5);
			cell.setCellValue(listLanhDao.get(0).getFirstName());
			cell.setCellStyle(cellStyleKyTenBold);

			fileExcel.close();

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			workbook.write(byteArrayOutputStream);
			StreamSource source = new StreamResource.StreamSource() {
				@Override
				public InputStream getStream() {
					try {
						byte[] bytes = byteArrayOutputStream.toByteArray();
						InputStream inputStream = new ByteArrayInputStream(bytes);
						return inputStream;
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				}
			};

			StreamResource resource = new StreamResource(source,"danhsachdonthu-"+new Date().getTime()+".xlsx");

			Page.getCurrent().open(resource, null, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CellStyle cellStyle(Workbook wb,boolean center){
		CellStyle style = wb.createCellStyle();
		BorderStyle thin = BorderStyle.THIN;
		short black = IndexedColors.BLACK.getIndex();
		Font font = wb.createFont();

		if(center)
			style.setAlignment(HorizontalAlignment.CENTER);

		font.setFontHeight((short)(13*20));
		font.setFontName("Times New Roman");
		style.setFont(font);

		style.setVerticalAlignment(VerticalAlignment.CENTER);

		style.setBorderRight(thin);
		style.setRightBorderColor(black);
		style.setBorderBottom(thin);
		style.setBottomBorderColor(black);
		style.setBorderLeft(thin);
		style.setLeftBorderColor(black);
		style.setBorderTop(thin);
		style.setTopBorderColor(black);

		style.setWrapText(true);

		return style;
	}
	
	public CellStyle cellStyleNgayThang(Workbook wb){
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setFontHeight((short)(14*20));

		style.setAlignment(HorizontalAlignment.CENTER);

		font.setFontName("Times New Roman");
		font.setItalic(true);
		style.setFont(font);

		style.setVerticalAlignment(VerticalAlignment.CENTER);

		style.setWrapText(true);

		return style;
	}

	public CellStyle cellStyleKyTen(Workbook wb,boolean bold){
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();

		font.setFontHeight((short)(14*20));
		font.setBold(bold);

		style.setAlignment(HorizontalAlignment.CENTER);
	
		font.setFontName("Times New Roman");
		style.setFont(font);

		style.setVerticalAlignment(VerticalAlignment.CENTER);
		
		style.setWrapText(true);

		return style;
	}
}
