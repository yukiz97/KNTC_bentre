package ngn.kntc.page.baocao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import ngn.kntc.enums.LoaiBaoCao;
import ngn.kntc.utils.SessionUtil;
import ngn.kntc.utils.UploadServiceUtil;
import ngn.kntc.windows.WindowChonDonViBaoCao;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class LapBaoCaoEvents extends LapBaoCaoDesign{
	String donVi = "";
	String subDonVi = "";
	public LapBaoCaoEvents() {
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		buildMocThoiGianButton();
		setValueDateFieldMonth();
		
		cmbLoaiBaoCao.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				int typeBaoCao = (int)event.getProperty().getValue();
				
				if(typeBaoCao == LoaiBaoCao.a2.getType() || typeBaoCao == LoaiBaoCao.b2.getType())
				{
					ogChonLoai.setVisible(false);
				}
				else if(typeBaoCao == LoaiBaoCao.c2.getType() || typeBaoCao == LoaiBaoCao.d2.getType())
				{
					ogChonLoai.setVisible(true);
				}
			}
		});
		cmbThang.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				setValueDateFieldMonth();
			}
		});

		cmbQuy.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				setValueDateFieldQuarter();
			}
		});

		cmbNam.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				setValueDateFieldYear();
			}
		});

		ogLoaiDoiTuongLapBaoCao.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if((int)ogLoaiDoiTuongLapBaoCao.getValue()==1)
				{
					btnChonDonVi.setVisible(true);
					btnChonCanBo.setVisible(false);
					vSubLayout.getComponent(3).setVisible(true);
					vSubLayout.getComponent(4).setVisible(false);
				}
				else
				{
					btnChonDonVi.setVisible(false);
					btnChonCanBo.setVisible(true);
					vSubLayout.getComponent(3).setVisible(false);
					vSubLayout.getComponent(4).setVisible(true);
				}
			}
		});

		btnChonDonVi.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				WindowChonDonViBaoCao wdCoQuan = new WindowChonDonViBaoCao(listOrg);
				UI.getCurrent().addWindow(wdCoQuan);

				wdCoQuan.getBtnOk().addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						listOrg.clear();
						listOrg.addAll(wdCoQuan.getListDonViInput());
						String display = "<b>";
						for(long orgId : listOrg)
						{
							try {
								display+=OrganizationLocalServiceUtil.getOrganization(orgId).getName()+" / ";
							} catch (PortalException | SystemException e) {
								e.printStackTrace();
							}
						}
						display+="</b>";
						lblDonViBaoCao.setValue(display);
					}
				});
			}
		});
		
		btnLapBaoCao.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				Label lblDisplayTmp = new Label("",ContentMode.HTML);
				lblDisplayTmp.setSizeUndefined();
				lblDisplayTmp.addStyleName("label-baocao-hienthiketqua");
				
				hButton.removeComponent(btnXuatExcel);
				btnXuatExcel = new Button("Xuất Excel",FontAwesome.FILE_EXCEL_O);
				btnXuatExcel.addStyleName(ValoTheme.BUTTON_FRIENDLY);
				hButton.addComponent(btnXuatExcel);
				try {
					switch ((int)cmbLoaiBaoCao.getValue()) {
					case 1:
						lblDisplayTmp.setValue(returnBaoCao2a());
						break;
					case 2:
						lblDisplayTmp.setValue(returnBaoCao2b());
						break;
					case 3:
						lblDisplayTmp.setValue(returnBaoCao2c());
						break;
					case 4:
						lblDisplayTmp.setValue(returnBaoCao2d());
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				pnlDisplay.setVisible(true);
				pnlDisplay.setContent(lblDisplayTmp);
				btnXuatExcel.setEnabled(true);
			}
		});
	}

	private void buildMocThoiGianButton()
	{
		timesMenu.put("month",new Button("Tháng"));
		timesMenu.put("quarter",new Button("Quý"));
		timesMenu.put("6first",new Button("6 tháng đầu"));
		timesMenu.put("6last",new Button("6 tháng cuối"));
		timesMenu.put("year",new Button("Năm"));

		Iterator <String> itr = timesMenu.keySet().iterator();
		while(itr.hasNext())
		{
			String id = itr.next();
			Button btnTmp = timesMenu.get(id);

			if(id=="month")
				btnTmp.addStyleName("btn-baocao-mocthoigian-active");

			hTimeDefault.addComponent(btnTmp);
			btnTmp.addStyleName("btn-baocao-mocthoigian");
			btnTmp.addClickListener(new ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					for (Iterator<String> it = timesMenu.keySet().iterator(); it.hasNext();) {
						timesMenu.get(it.next()).removeStyleName("btn-baocao-mocthoigian-active");
					}
					btnTmp.addStyleName("btn-baocao-mocthoigian-active");
					switch (id) {
					case "month":
						cmbThang.setVisible(true);
						cmbQuy.setVisible(false);
						cmbNam.setVisible(false);
						setValueDateFieldMonth();
						break;
					case "quarter":
						cmbThang.setVisible(false);
						cmbQuy.setVisible(true);
						cmbNam.setVisible(false);
						setValueDateFieldQuarter();
						break;
					case "year":
						cmbThang.setVisible(false);
						cmbQuy.setVisible(false);
						cmbNam.setVisible(true);
						setValueDateFieldYear();
						break;
					case "6first":
						cmbThang.setVisible(false);
						cmbQuy.setVisible(false);
						cmbNam.setVisible(false);
						setValueDateFieldHalf("first");
						break;
					case "6last":
						cmbThang.setVisible(false);
						cmbQuy.setVisible(false);
						cmbNam.setVisible(false);
						setValueDateFieldHalf("last");
						break;
					default:
						break;
					}
				}
			});
		}
	}

	private void setValueDateFieldMonth()
	{
		YearMonth yearMonth = YearMonth.of(Calendar.getInstance().get(Calendar.YEAR),(int)cmbThang.getValue());
		LocalDate firstOfMonth = yearMonth.atDay(1);
		LocalDate lastOfMonth = yearMonth.atEndOfMonth();

		try {
			dfStartDate.setValue(sdfYYYY.parse(firstOfMonth.toString()));
			dfEndDate.setValue(sdfYYYY.parse(lastOfMonth.toString()));
		} catch (ReadOnlyException | ConversionException | ParseException e) {
			e.printStackTrace();
		}
	}

	private void setValueDateFieldQuarter()
	{
		int quarter = (int)cmbQuy.getValue();
		int first = 0, end = 0;
		switch (quarter) {
		case 1:
			first = 1;
			end = 3;
			break;
		case 2:
			first = 4;
			end = 6;
			break;
		case 3:
			first = 7;
			end = 9;
			break;
		case 4:
			first = 10;
			end = 12;
			break;
		default:
			break;
		}
		YearMonth startMonth = YearMonth.of(Calendar.getInstance().get(Calendar.YEAR),first);
		YearMonth endMonth = YearMonth.of(Calendar.getInstance().get(Calendar.YEAR),end);

		LocalDate firstOfMonth = startMonth.atDay(1);
		LocalDate lastOfMonth = endMonth.atEndOfMonth();

		try {
			dfStartDate.setValue(sdfYYYY.parse(firstOfMonth.toString()));
			dfEndDate.setValue(sdfYYYY.parse(lastOfMonth.toString()));
		} catch (ReadOnlyException | ConversionException | ParseException e) {
			e.printStackTrace();
		}
	}

	private void setValueDateFieldYear()
	{
		YearMonth startMonth = YearMonth.of((int)cmbNam.getValue(),1);
		YearMonth endMonth = YearMonth.of((int)cmbNam.getValue(),12);

		LocalDate firstOfMonth = startMonth.atDay(1);
		LocalDate lastOfMonth = endMonth.atEndOfMonth();

		try {
			dfStartDate.setValue(sdfYYYY.parse(firstOfMonth.toString()));
			dfEndDate.setValue(sdfYYYY.parse(lastOfMonth.toString()));
		} catch (ReadOnlyException | ConversionException | ParseException e) {
			e.printStackTrace();
		}
	}

	private void setValueDateFieldHalf(String type)
	{
		int start = 0,end = 0;
		if(type=="first")
		{
			start = 1;
			end = 6;
		}
		else
		{
			start = 6;
			end = 12;
		}

		YearMonth startMonth = YearMonth.of(Calendar.getInstance().get(Calendar.YEAR),start);
		YearMonth endMonth = YearMonth.of(Calendar.getInstance().get(Calendar.YEAR),end);

		LocalDate firstOfMonth = startMonth.atDay(1);
		LocalDate lastOfMonth = endMonth.atEndOfMonth();

		try {
			dfStartDate.setValue(sdfYYYY.parse(firstOfMonth.toString()));
			dfEndDate.setValue(sdfYYYY.parse(lastOfMonth.toString()));
		} catch (ReadOnlyException | ConversionException | ParseException e) {
			e.printStackTrace();
		}
	}

	private String returnBaoCao2a() throws Exception
	{ 
		String result; 
		String head = "<div class='table-baocao-display'><table><tbody><tr><th rowspan='4' style='width: 200px !important;'>Đơn vị</th><th colspan='8'>Tiếp thường xuyên</th><th colspan='8'>Tiếp định kỳ và đột xuất của Lãnh đạo</th><th colspan='10'>Nội dung tiếp công dân (số vụ việc)</th><th colspan='4'>Kết quả qua tiếp dân (số vụ việc)</th><th rowspan='4'>Ghi chú</th></tr><tr><th rowspan='3'>Lượt</th><th rowspan='3'>Người</th><th colspan='2'>Vụ việc</th><th colspan='4'>Đoàn đông người</th><th rowspan='3'>Lượt</th><th rowspan='3'>Người</th><th colspan='2'>Vụ việc</th><th colspan='4'>Đoàn đông người</th><th colspan='6'>Khiếu nại</th><th colspan='3'>Tố cáo</th><th rowspan='3' style='width: 60px'>Phản ánh, kiến nghị, khác</th><th rowspan='3' style='width: 60px'>Chưa được giải quyết</th><th colspan='3' style='width: 60px'>Đã được giải quyết</th></tr><tr><th rowspan='2'>Cũ</th><th rowspan='2' style='width: 60px'>Mới phát sinh</th><th rowspan='2'>Số đoàn</th><th rowspan='2'>Người</th><th colspan='2'>Vụ việc</th><th rowspan='2'>Cũ</th><th rowspan='2' style='width: 60px'>Mới phát sinh</th><th rowspan='2'>Số đoàn</th><th rowspan='2'>Người</th><th colspan='2'>Vụ việc</th><th colspan='4' style='width: 60px'>Lĩnh vực hành chính</th><th rowspan='2' style='width: 60px'>Lĩnh vực tư pháp</th><th rowspan='2' style='width: 60px'>Lĩnh vực CT, VH, XH, khác</th><th rowspan='2' style='width: 60px'>Lĩnh vực hành chính</th><th rowspan='2' style='width: 60px'>Lĩnh vực tư pháp</th><th rowspan='2' style='width: 60px'>Tham nhũng</th><th rowspan='2' style='width: 60px'>Chưa có QĐ giải quyết</th><th rowspan='2' style='width: 60px'>Đã có QĐ giải quyết (lần 1, 2, cuối cùng)</th><th rowspan='2' style='width: 60px'>Đã có bản án của Tòa</th></tr><tr><th>Cũ</th><th style='width: 60px'>Mới phát sinh</th><th>Cũ</th><th style='width: 60px'>Mới phát sinh</th><th style='width: 60px'>Về tranh chấp, đòi đất cũ, đền bù, giải tỏa,...</th><th style='width: 60px'>Về chính sách</th><th style='width: 60px'>Về nhà, tài sản</th><th style='width: 60px'> Về chế độ CC, VC</th></tr><tr style='text-align:center'><th>MS</th><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td><td>7</td><td>8</td><td>9</td><td>10</td><td>11</td><td>12</td><td>13</td><td>14</td><td>15</td><td>16</td><td>17</td><td>18</td><td>19</td><td>20</td><td>21</td><td>22</td><td>23</td><td>24</td><td>25</td><td>26</td><td>27</td><td>28</td><td>29</td><td>30</td><td>31</td></tr>";
		String body = "";
		String tail = "</tbody></table>";
		String startDate = new SimpleDateFormat("dd/MM/YYYY").format(dfStartDate.getValue());
		String endDate = new SimpleDateFormat("dd/MM/YYYY").format(dfEndDate.getValue());
		FileInputStream file = new FileInputStream(new File(UploadServiceUtil.getAbsolutePath()+File.separator+"maudon"+File.separator+"baocao2a.xls"));
		HSSFWorkbook workbook = new HSSFWorkbook(file);
		HSSFSheet sheet = workbook.getSheetAt(0);
		Cell cell = null;
		cell = sheet.getRow(1).getCell(0);
		cell.setCellValue(donVi);
		cell = sheet.getRow(2).getCell(0);
		cell.setCellValue(subDonVi);
		cell = sheet.getRow(6).getCell(0);
		cell.setCellValue("(Số liệu tính từ ngày "+startDate+" đến ngày "+endDate+")");
		cell = sheet.getRow(23).getCell(22);
		cell.setCellValue("Bến Tre, Ngày "+Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+" tháng "+Calendar.getInstance().get(Calendar.MONTH)+" năm "+Calendar.getInstance().get(Calendar.YEAR));
		cell = sheet.getRow(24).getCell(22);
		cell.setCellValue(SessionUtil.getUser().getFirstName());
		int startLine = 15;
		int[] sumCount = new int[31]; 
		for(long orgId : listOrg)
		{
			HashMap<Integer,Integer> map = getDataReport2a(orgId);
			cell= sheet.getRow(startLine).getCell(0);
			cell.setCellValue(OrganizationLocalServiceUtil.getOrganization(orgId).getName());
			String row1 = "<tr style='height: 50px'><td>"+OrganizationLocalServiceUtil.getOrganization(orgId).getName()+"</td>";
			Iterator <Integer> itr = map.keySet().iterator();
			while (itr.hasNext()){
				int i = itr.next();
				sumCount[i] += map.get(i);
				cell= sheet.getRow(startLine).getCell(i);
				cell.setCellValue(map.get(i));

				row1+="<td>"+map.get(i)+"</td>";
			}
			startLine++;
			row1+="<td></td></tr>";
			body+=row1;
		}
		String rowSum = "<tr> <td>Tổng</td>";
		for(int i = 1;i<sumCount.length;i++)
		{
			cell= sheet.getRow(20).getCell(i);
			cell.setCellValue(sumCount[i]);

			rowSum+="<td>"+sumCount[i]+"</td>";
		}
		rowSum+="<td></td></tr>";
		body+=rowSum;
		file.close();

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		workbook.write(byteArrayOutputStream);
		workbook.close();
		btnXuatExcel.setEnabled(true);
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
		StreamResource resource;
		resource = new StreamResource(source,"baocao2a.xls");
		FileDownloader downloader=new FileDownloader(resource);
		downloader.extend(btnXuatExcel);

		result =head+body+tail;
		return result;
	}

	private String returnBaoCao2b() throws Exception
	{
		String result;
		String head = "<div class='table-baocao-display'><table style='2200px !important'><tbody><tr><th rowspan='5' style='width: 200px !important;'>Đơn vị</th><th colspan='6'>Tiếp nhận</th><th colspan='19'>Phân loại đơn khiếu nại, tố cáo (số đơn)</th><th rowspan='5'>Đơn khác (kiến nghị, phản ánh, đơn nặc danh)</th><th colspan='5'>Kết quả xử lý đơn khiếu nại, tố cáo</th><th rowspan='5'>Ghi chú</th></tr><tr><th rowspan='4'>Tổng số đơn</th><th colspan='2' rowspan='2 '>Đơn tiếp nhận trong kỳ</th><th colspan='2' rowspan='2'>Đơn kỳ trước chuyển sang</th><th rowspan='4'>Đơn đủ điều kiện xử lý</th><th colspan='13'>Theo nội dung</th><th colspan='3'>Theo thẩm quyền giải quyết</th><th colspan='3'>Theo trình tự giải quyết</th><th rowspan='4'>Số văn bản hướng dẫn</th><th rowspan='4'>Số đơn chuyển cơ quan có thẩm quyền</th><th rowspan='4'>Số công văn đôn đốc việc giải quyết</th><th colspan='2' rowspan='2'>Đơn thuộc thẩm quyền</th></tr><tr><th colspan='7'>Khiếu nại</th><th colspan='6'>Tố cáo</th><th rowspan='3'>Của các cơ quan hành chính các cấp</th><th rowspan='3'>Của cơ quan tư pháp các cấp</th><th rowspan='3'>Của cơ quan Đảng</th><th rowspan='3'>Chưa được giải quyết</th><th rowspan='3'>Đã được giải quyết lần đầu</th><th rowspan='3'>Đã được giải quyết nhiều lần</th></tr><tr><th rowspan='2'>Đơn có nhiều người đứng tên</th><th rowspan='2'>Đơn một người đứng tên</th><th rowspan='2'>Đơn có nhiều người đứng tên</th><th rowspan='2'>Đơn một người đứng tên</th><th colspan='5'>Lĩnh vực hành chính</th><th rowspan='2'>Lĩnh vực tư pháp</th><th rowspan='2'>Về Đảng</th><th rowspan='2'>Tổng</th><th rowspan='2'>Lĩnh vực hành chính</th><th rowspan='2'>Lĩnh vực tư pháp</th><th rowspan='2'>Tham nhũng</th><th rowspan='2'>Về Đảng</th><th rowspan='2'>Lĩnh vực khác</th><th rowspan='2'>Khiếu nại</th><th rowspan='2'>Tố cáo</th></tr><tr><th>Tổng</th><th>Liên quan đến đất đai</th><th>Về nhà, tài sản</th><th>Về chính sách, chế độ CC, VC</th><th>Lĩnh vực CT, VH, XH khác</th></tr><tr style='text-align:center'><th>MS</th><td>1=2+3+ 4+5</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td><td>7=8+9+ 10+11</td><td>8</td><td>9</td><td>10</td><td>11</td><td>12</td><td>13</td><td>14=15+16+ 17+18+19</td><td>15</td><td>16</td><td>17</td><td>18</td><td>19</td><td>20</td><td>21</td><td>22</td><td>23</td><td>24</td><td>25</td><td>26</td><td>27</td><td>28</td><td>29</td><td>30</td><td>31</td><td>32</td></tr>";
		String body = "";
		String tail = "</tbody></table>";
		String startDate = new SimpleDateFormat("dd/MM/YYYY").format(dfStartDate.getValue());
		String endDate = new SimpleDateFormat("dd/MM/YYYY").format(dfEndDate.getValue());
		FileInputStream file = new FileInputStream(new File(UploadServiceUtil.getAbsolutePath()+File.separator+"maudon"+File.separator+"baocao2b.xls"));
		HSSFWorkbook workbook = new HSSFWorkbook(file);
		HSSFSheet sheet = workbook.getSheetAt(0);
		Cell cell = null;
		cell = sheet.getRow(1).getCell(0);
		cell.setCellValue(donVi);
		cell = sheet.getRow(2).getCell(0);
		cell.setCellValue(subDonVi);
		cell = sheet.getRow(6).getCell(0);
		cell.setCellValue("(Số liệu tính từ ngày "+startDate+" đến ngày "+endDate+")");
		cell = sheet.getRow(22).getCell(23);
		cell.setCellValue("Bến Tre, Ngày "+Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+" tháng "+Calendar.getInstance().get(Calendar.MONTH)+" năm "+Calendar.getInstance().get(Calendar.YEAR));
		cell = sheet.getRow(23).getCell(23);
		cell.setCellValue(SessionUtil.getUser().getFirstName());
		int startLine = 15;
		int[] sumCount = new int[32]; 
		for(long orgId : listOrg)
		{
			HashMap<Integer,Integer> map = getDataReport2b(orgId);
			cell= sheet.getRow(startLine).getCell(0);
			cell.setCellValue(OrganizationLocalServiceUtil.getOrganization(orgId).getName());
			String row1 = "<tr style='height: 50px'><td>"+OrganizationLocalServiceUtil.getOrganization(orgId).getName()+"</td>";
			Iterator <Integer> itr = map.keySet().iterator();
			while (itr.hasNext()){
				int i = itr.next();
				sumCount[i] += map.get(i);
				cell= sheet.getRow(startLine).getCell(i);
				cell.setCellValue(map.get(i));

				row1+="<td>"+map.get(i)+"</td>";
			}
			startLine++;
			row1+="<td></td></tr>";
			body+=row1;
		}
		String rowSum = "<tr> <td>Tổng</td>";
		for(int i = 1;i<sumCount.length;i++)
		{
			cell= sheet.getRow(19).getCell(i);
			cell.setCellValue(sumCount[i]);

			rowSum+="<td>"+sumCount[i]+"</td>";
		}
		rowSum+="<td></td></tr>";
		body+=rowSum;
		file.close();

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		workbook.write(byteArrayOutputStream);
		workbook.close();
		btnXuatExcel.setEnabled(true);
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
		StreamResource resource;
		resource = new StreamResource(source,"baocao2b.xls");
		FileDownloader downloader=new FileDownloader(resource);
		downloader.extend(btnXuatExcel);

		result =head+body+tail;
		return result;
	}

	private String returnBaoCao2c() throws Exception
	{
		String result;
		String head = "<div class='table-baocao-display'><table style='width: 2000px !important'> <tbody> <tr> <th rowspan='4' style='width: 200px !important;'>Đơn vị</th><th colspan='4'>Đơn khiếu nại thuộc thẩm quyền</th><th colspan='21'>Kết quả giải quyết</th><th colspan='2' rowspan='2'>Chấp hành thời gian giải quyết theo quy định</th><th colspan='10'>Việc thi hành quyết định giải quyết khiếu nại</th><th rowspan='4'>Ghi chú</th> </tr> <tr> <th rowspan='3' width='60px'>Tổng số đơn khiếu nại</th><th colspan='3' rowspan='2'>Trong đó</th><th colspan='4' rowspan='2'>Đã giải quyết</th><th colspan='6'>Phân tích kết quả (vụ việc)</th><th colspan='2' rowspan='2'>Kiến nghị thu hồi cho Nhà nước</th><th colspan='2' rowspan='2'>Trả lại cho công dân</th><th rowspan='3' style='width:60px'>Số người được trả lại quyền lợi</th><th colspan='2' rowspan='2' style='width:60px'>Kiến nghị xử lý hành chính</th><th colspan='4' style='width:240px'>Chuyển cơ quan điều tra, khởi tố</th><th rowspan='3' style='width:60px'>Tổng số quyết định phải tổ chức thực hiện trong kỳ báo cáo</th><th rowspan='3' style='width:60px'>Đã thực hiện</th><th colspan='4'>Thu hồi cho Nhà nước</th><th colspan='4'>Trả lại cho công dân</th></tr> <tr> <th rowspan='2' style='width:60px'>Khiếu nại đúng</th><th rowspan='2' style='width:60px'>Khiếu nại sai</th><th rowspan='2' style='width:60px'>Khiếu nại đúng một phần</th><th rowspan='2' style='width:60px'>Giải quyết lần 1</th><th colspan='2' style='width:120px'>Giải quyết lần 2</th><th rowspan='2' style='width:60px'>Số vụ</th><th rowspan='2' style='width:60px'>Số đối tượng</th><th colspan='2' style='width:120px'>Kết quả</th><th rowspan='2' style='width:60px'>Số vụ việc giải quyết đúng thời hạn</th><th rowspan='2' style='width:60px'>Số vụ việc giải quyết quá thời hạn</th><th colspan='2'>Phải thu</th><th colspan='2'>Đã thu</th> <th colspan='2'>Phải trả</th><th colspan='2'>Đã trả</th></tr> <tr> <th style='width:60px'>Đơn nhận trong kỳ báo cáo</th><th style='width:60px'>Đơn tồn kỳ trước chuyển sang</th><th style='width:60px'>Tổng số vụ việc</th><th style='width:60px'>Số đơn thuộc thẩm quyền</th><th style='width:60px'>Số vụ việc thuộc thẩm quyền</th><th style='width:60px'>Số vụ việc giải quyết bằng QĐ hành chính</th><th style='width:60px'>Số vụ việc rút đơn thông qua giải thích, thuyết phục</th><th style='width:60px'>Công nhận QĐ g/q lần 1</th><th style='width:60px'>Hủy, sửa QĐ g/q lần 1</th><th style='width:60px'>Tiền (Trđ)</th><th style='width:60px'>Đất (m2)</th><th style='width:60px'>Tiền (Trđ)</th><th style='width:60px'>Đất (m2)</th><th style='width:60px'>Tổng số người</th><th style='width:60px'>Số người đã bị xử lý</th><th style='width:60px'>Số vụ đã khởi tố</th><th style='width:60px !important'>Số đối tượng đã khởi tố</th><th style='width:60px'>Tiền (Trđ)</th><th style='width:60px'>Đất (m2)</th><th style='width:60px'>Tiền (Trđ)</th><th style='width:60px'>Đất (m2)</th><th style='width:60px'>Tiền (Trđ)</th><th style='width:60px'>Đất (m2)</th><th style='width:60px'>Tiền (Trđ)</th><th style='width:60px'>Đất (m2)</th></tr> <tr style='text-align:center'> <th>MS</th> <td>1=2+3</td> <td>2</td> <td>3</td> <td>4</td> <td>5</td> <td>6</td> <td>7</td> <td>8</td> <td>9</td> <td>10</td> <td>11</td> <td>12</td> <td>13</td> <td>14</td> <td>15</td> <td>16</td> <td>17</td> <td>18</td> <td>19</td> <td>20</td> <td>21</td> <td>22</td> <td>23</td> <td>24</td> <td>25</td> <td>26</td> <td>27</td> <td>28</td> <td>29</td> <td>30</td> <td>31</td> <td>32</td> <td>33</td> <td>34</td> <td>35</td> <td>36</td> <td>37</td> <td>38</td> </tr>";
		String body = "";
		String tail = "</tbody></table>";
		String startDate = new SimpleDateFormat("dd/MM/YYYY").format(dfStartDate.getValue());
		String endDate = new SimpleDateFormat("dd/MM/YYYY").format(dfEndDate.getValue());
		FileInputStream file = new FileInputStream(new File(UploadServiceUtil.getAbsolutePath()+File.separator+"maudon"+File.separator+"baocao2c.xls"));
		HSSFWorkbook workbook = new HSSFWorkbook(file);
		HSSFSheet sheet = workbook.getSheetAt(0);
		Cell cell = null;
		cell = sheet.getRow(1).getCell(0);
		cell.setCellValue(donVi);
		cell = sheet.getRow(2).getCell(0);
		cell.setCellValue(subDonVi);
		cell = sheet.getRow(7).getCell(0);
		cell.setCellValue("(Số liệu tính từ ngày "+startDate+" đến ngày "+endDate+")");
		cell = sheet.getRow(22).getCell(26);
		cell.setCellValue("Bến Tre, Ngày "+Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+" tháng "+Calendar.getInstance().get(Calendar.MONTH)+" năm "+Calendar.getInstance().get(Calendar.YEAR));
		cell = sheet.getRow(23).getCell(26);
		cell.setCellValue(SessionUtil.getUser().getFirstName());
		int startLine = 16;
		int[] sumCount = new int[38]; 
		for(long orgId : listOrg)
		{
			HashMap<Integer,Integer> map = getDataReport2c(orgId);
			cell= sheet.getRow(startLine).getCell(0);
			cell.setCellValue(OrganizationLocalServiceUtil.getOrganization(orgId).getName());
			String row1 = "<tr style='height: 50px'><td>"+OrganizationLocalServiceUtil.getOrganization(orgId).getName()+"</td>";
			Iterator <Integer> itr = map.keySet().iterator();
			while (itr.hasNext()){
				int i = itr.next();
				sumCount[i] += map.get(i);
				cell= sheet.getRow(startLine).getCell(i);
				cell.setCellValue(map.get(i));

				row1+="<td>"+map.get(i)+"</td>";
			}
			startLine++;
			row1+="<td></td></tr>";
			body+=row1;
		}
		String rowSum = "<tr> <td>Tổng</td>";
		for(int i = 1;i<sumCount.length;i++)
		{
			cell= sheet.getRow(20).getCell(i);
			cell.setCellValue(sumCount[i]);

			rowSum+="<td>"+sumCount[i]+"</td>";
		}
		rowSum+="<td></td></tr>";
		body+=rowSum;
		file.close();

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		workbook.write(byteArrayOutputStream);
		workbook.close();
		btnXuatExcel.setEnabled(true);
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
		StreamResource resource;
		resource = new StreamResource(source,"baocao2c.xls");
		FileDownloader downloader=new FileDownloader(resource);
		downloader.extend(btnXuatExcel);

		result =head+body+tail;
		return result;
	}

	private String returnBaoCao2d() throws Exception
	{
		String result;
		String head = "<div class='table-baocao-display'><table style='2000px !important'> <tbody> <tr> <th rowspan='4' style='width: 200px !important;'>Đơn vị</th><th colspan='4'>Đơn tố cáo thuộc thẩm quyền</th><th colspan='16'>Kết quả giải quyết</th><th colspan='2' rowspan='3'>Chấp hành thời gian giải quyết theo quy định</th><th colspan='10'>Việc thi hành quyết định xử lý tố cáo</th><th rowspan='4'>Ghi chú</th> </tr> <tr> <th rowspan='3' style='width:60px !important'>Tổng số đơn tố cáo</th><th colspan='3' rowspan='2'>Trong đó</th><th colspan='2' rowspan='2'>Đã giải quyết</th><th colspan='3' rowspan='2'>Phân tích kết quả (vụ việc)</th><th colspan='2' rowspan='2'>Kiến nghị thu hồi cho Nhà nước</th><th colspan='2' rowspan='2'>Trả lại cho công dân</th><th rowspan='3' style='width:60px !important'>Số người được bảo vệ quyền lợi</th><th colspan='2' rowspan='2'>Kiến nghị xử lý hành chính</th><th colspan='4'>Chuyển cơ quan điều tra, khởi tố</th><th rowspan='3' style='width:60px !important'>Tổng số quyết định phải tổ chức thực hiện trong kỳ báo cáo</th><th rowspan='3' style='width:60px !important'>Đã thực hiện xong</th><th colspan='4'>Thu hồi cho Nhà nước</th><th colspan='4'>Trả lại cho công dân</th></tr> <tr> <th rowspan='2'>Số vụ</th> <th rowspan='2' style='width:60px !important'>Số đối tượng</th><th colspan='2'>Kết quả</th> <th colspan='2'>Phải thu</th> <th colspan='2'>Đã thu</th> <th colspan='2'>Phải trả</th><th colspan='2'>Đã trả</th> </tr> <tr> <th style='width:60px !important'>Đơn nhận trong kỳ báo cáo</th><th style='width:60px !important'>Đơn tồn kỳ trước chuyển sang</th><th style='width:60px !important'>Tổng số vụ việc</th><th style='width:60px !important'>Số đơn thuộc thẩm quyền</th><th style='width:60px !important'>Số vụ việc thuộc thẩm quyền</th><th style='width:60px !important'>Tố cáo đúng</th><th style='width:60px !important'>Tố cáo sai</th><th style='width:60px !important'>Tố cáo đúng một phần</th><th style='width:60px !important'>Tiền (Trđ)</th><th style='width:60px !important'>Đất (m2)</th><th style='width:60px !important'> Tiền (Trđ)</th><th style='width:60px !important'>Đất (m2)</th><th style='width:60px !important'>Tổng số người</th><th style='width:60px !important'>Số người đã bị xử lý</th><th style='width:60px !important'>Số vụ đã khởi tố</th><th style='width:60px !important'>Số đối tượng đã khởi tố</th><th style='width:60px !important'>Số vụ việc giải quyết đúng thời hạn</th><th style='width:60px !important'>Số vụ việc giải quyết quá thời hạn</th><th style='width:60px !important'>Tiền (Trđ)</th><th style='width:60px !important'>Đất (m2)</th><th style='width:60px !important'>Tiền (Trđ)</th><th style='width:60px !important'>Đất (m2)</th><th style='width:60px !important'>Tiền (Trđ)</th><th style='width:60px !important'>Đất (m2)</th><th style='width:60px !important'>Tiền (Trđ)</th><th style='width:60px !important'>Đất (m2)</th></tr> <tr style='font-weight:bold;text-align:center'><th>MS</th> <td>1= 2+ 3</td> <td>2</td> <td>3</td> <td>4</td> <td>5</td> <td>6</td> <td>7</td> <td>8</td> <td>9</td> <td>10</td> <td>11</td> <td>12</td> <td>13</td> <td>14</td> <td>15</td> <td>16</td> <td>17</td> <td>18</td> <td>19</td> <td>20</td> <td>21</td> <td>22</td> <td>23</td> <td>24</td> <td>25</td> <td>26</td> <td>27</td> <td>28</td> <td>29</td> <td>30</td> <td>31</td> <td>32</td> <td>33</td> </tr> ";
		String body = "";
		String tail = "</tbody></table>";
		String startDate = new SimpleDateFormat("dd/MM/YYYY").format(dfStartDate.getValue());
		String endDate = new SimpleDateFormat("dd/MM/YYYY").format(dfEndDate.getValue());
		FileInputStream file = new FileInputStream(new File(UploadServiceUtil.getAbsolutePath()+File.separator+"maudon"+File.separator+"baocao2d.xls"));
		HSSFWorkbook workbook = new HSSFWorkbook(file);
		HSSFSheet sheet = workbook.getSheetAt(0);
		Cell cell = null;
		cell = sheet.getRow(0).getCell(0);
		cell.setCellValue(donVi);
		cell = sheet.getRow(1).getCell(0);
		cell.setCellValue(subDonVi);
		cell = sheet.getRow(7).getCell(0);
		cell.setCellValue("(Số liệu tính từ ngày "+startDate+" đến ngày "+endDate+")");
		cell = sheet.getRow(23).getCell(22);
		cell.setCellValue("Bến Tre, Ngày "+Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+" tháng "+Calendar.getInstance().get(Calendar.MONTH)+" năm "+Calendar.getInstance().get(Calendar.YEAR));
		cell = sheet.getRow(24).getCell(22);
		cell.setCellValue(SessionUtil.getUser().getFirstName());
		int startLine = 16;
		int[] sumCount = new int[33]; 
		for(long orgId : listOrg)
		{
			HashMap<Integer,Integer> map = getDataReport2d(orgId);
			cell= sheet.getRow(startLine).getCell(0);
			cell.setCellValue(OrganizationLocalServiceUtil.getOrganization(orgId).getName());
			String row1 = "<tr style='height: 50px'><td>"+OrganizationLocalServiceUtil.getOrganization(orgId).getName()+"</td>";
			Iterator <Integer> itr = map.keySet().iterator();
			while (itr.hasNext()){
				int i = itr.next();
				sumCount[i] += map.get(i);
				cell= sheet.getRow(startLine).getCell(i);
				cell.setCellValue(map.get(i));

				row1+="<td>"+map.get(i)+"</td>";
			}
			startLine++;
			row1+="<td></td></tr>";
			body+=row1;
		}
		String rowSum = "<tr> <td>Tổng</td>";
		for(int i = 1;i<sumCount.length;i++)
		{
			cell= sheet.getRow(20).getCell(i);
			cell.setCellValue(sumCount[i]);

			rowSum+="<td>"+sumCount[i]+"</td>";
		}
		rowSum+="<td></td></tr>";
		body+=rowSum;
		file.close();

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		workbook.write(byteArrayOutputStream);
		workbook.close();
		btnXuatExcel.setEnabled(true);
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
		StreamResource resource;
		resource = new StreamResource(source,"baocao2d.xls");
		FileDownloader downloader=new FileDownloader(resource);
		downloader.extend(btnXuatExcel);

		result =head+body+tail;
		return result;
	}

	HashMap<Integer,Integer> getDataReport2a(long orgId) throws Exception
	{
		HashMap<Integer,Integer> resultMap = new HashMap<Integer,Integer>();
		for(int i = 30;i>=1;i--)
		{
			resultMap.put(i, svBaoCao.getCountBaoCao2ATmp(dfStartDate.getValue(), dfEndDate.getValue(), i,orgId));
		}
		return resultMap;
	}

	HashMap<Integer,Integer> getDataReport2b(long orgId) throws Exception
	{
		HashMap<Integer,Integer> resultMap = new HashMap<Integer,Integer>();
		for(int i = 31;i>=1;i--)
		{
			resultMap.put(i, svBaoCao.getCountBaoCao2B(dfStartDate.getValue(), dfEndDate.getValue(), i,orgId));
			if(i==14)
			{
				resultMap.put(i, resultMap.get(15)+resultMap.get(16)+resultMap.get(17)+resultMap.get(18)+resultMap.get(19));
			}
			if(i==7)
			{
				resultMap.put(i, resultMap.get(8)+resultMap.get(9)+resultMap.get(10)+resultMap.get(11));
			}
			if(i==1)
			{
				resultMap.put(i, resultMap.get(2)+resultMap.get(3)+resultMap.get(4)+resultMap.get(5));
			}
		}
		return resultMap;
	}

	HashMap<Integer,Integer> getDataReport2c(long orgId) throws Exception
	{
		HashMap<Integer,Integer> resultMap = new HashMap<Integer,Integer>();
		for(int i = 37;i>=1;i--)
		{
			resultMap.put(i, svBaoCao.getCountBaoCao2C(dfStartDate.getValue(), dfEndDate.getValue(), i,orgId));
			if(i==1)
			{
				resultMap.put(i, resultMap.get(2)+resultMap.get(3));
			}
		}
		return resultMap;
	}

	HashMap<Integer,Integer> getDataReport2d(long orgId) throws Exception
	{
		HashMap<Integer,Integer> resultMap = new HashMap<Integer,Integer>();
		for(int i = 32;i>=1;i--)
		{
			resultMap.put(i, svBaoCao.getCountBaoCao2D(dfStartDate.getValue(), dfEndDate.getValue(), i,orgId));
			if(i==1)
			{
				resultMap.put(i, resultMap.get(2)+resultMap.get(3));
			}
		}
		return resultMap;
	}
	
	public void test()
	{
		
	}
}
