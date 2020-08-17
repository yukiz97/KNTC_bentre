package ngn.kntc.word.bieumau;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.SessionUtil;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;

import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;

public abstract class WordBieuMauPattern {
	XWPFDocument document = new XWPFDocument();
	CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
	CTPageMar documentMargin = sectPr.addNewPgMar();

	String fontTimeNewRoman = "Times New Roman";
	XWPFRun runAllRun;

	String fileNameDownload = "";

	int idDonThu = -1;

	DonThuServiceUtil svDonThu = new DonThuServiceUtil();
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	public WordBieuMauPattern() {
		documentMargin.setLeft(BigInteger.valueOf(1440L));
		documentMargin.setTop(BigInteger.valueOf(720L));
		documentMargin.setRight(BigInteger.valueOf(1000L));
		documentMargin.setBottom(BigInteger.valueOf(720L));
	}

	public void createHeader() throws Exception
	{
		String valueCoQuanChuQuan = OrganizationLocalServiceUtil.getOrganization(SessionUtil.getMasterOrgId()).getName();
		String valueTenDonViChuQuan = "";
		String valueNgayThangNam = "Thứ "+Calendar.getInstance().get(Calendar.DAY_OF_WEEK)+", ngày "+Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+" tháng "+Calendar.getInstance().get(Calendar.MONTH)+" năm "+Calendar.getInstance().get(Calendar.YEAR);

		if(SessionUtil.getLienThongOrgId().equalsIgnoreCase("H811.01"))
		{
			valueTenDonViChuQuan+="BAN TIẾP CÔNG DÂN";
		}
		else
		{
			valueTenDonViChuQuan+=OrganizationLocalServiceUtil.getOrganization(SessionUtil.getOrgId()).getName().toLowerCase();
		}
		
		//create table
		XWPFTable table = document.createTable();
		table.setWidth("100%");
		table.getCTTbl().getTblPr().unsetTblBorders();
		table.removeRow(0);

		XWPFTableRow row1 = table.createRow();

		XWPFTableCell cell1 = row1.createCell();
		cell1.setWidth("35%");
		XWPFParagraph para1 = cell1.addParagraph();
		setRun(para1, fontTimeNewRoman, 13,valueCoQuanChuQuan, true,true);
		setRun(para1, fontTimeNewRoman, 13,valueTenDonViChuQuan, true,true);

		para1.setAlignment(ParagraphAlignment.CENTER);

		XWPFTableCell cell2 = row1.createCell();
		cell2.setWidth("65%");
		XWPFParagraph para2 = cell2.addParagraph();
		setRun(para2, fontTimeNewRoman, 13,"CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM", true,true);
		setRun(para2, fontTimeNewRoman, 13,"Độc lập - Tự do - Hạnh phúc", true,true);
		runAllRun = setRun(para2, fontTimeNewRoman, 13,"___________________", false,true);
		runAllRun.addBreak();
		setRun(para2, fontTimeNewRoman, 13,valueNgayThangNam, false,false);

		para2.setAlignment(ParagraphAlignment.CENTER);
	}

	public void createCellSignature(XWPFTableCell cell,String firstLine,String secondLine)
	{
		cell.setWidth("65%");
		XWPFParagraph para2 = cell.addParagraph();
		setRun(para2, "Times New Roman", 14,firstLine, true,true);
		setRun(para2, fontTimeNewRoman, 12,secondLine == null?"(Ký, ghi rõ họ tên và đóng dấu)":secondLine, false,false);

		para2.setAlignment(ParagraphAlignment.CENTER);
	}

	public void exportWord() throws Exception
	{
		document = new XWPFDocument();
		createHeader();
		createTitle();
		createContent();
		createFooter();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		document.write(byteArrayOutputStream);

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
		StreamResource resource = new StreamResource(source,fileNameDownload);
		Page.getCurrent().open(resource, null, false);
	}

	public abstract void createTitle() throws Exception;
	public abstract void createContent() throws Exception;
	public abstract void createFooter() throws Exception;

	public XWPFRun setRun(XWPFParagraph paragraph, String fontFamily , int fontSize , String text , boolean bold,boolean addBreak) {
		XWPFRun run = paragraph.createRun();
		run.setFontFamily(fontFamily);
		run.setFontSize(fontSize);
		run.setText(text);
		run.setBold(bold);
		if(addBreak)
			run.addBreak();
		return run;
	}

	public String getFileNameDownload() {
		return fileNameDownload;
	}

	public void setFileNameDownload(String fileNameDownload) {
		this.fileNameDownload = fileNameDownload;
	}

	public int getIdDonThu() {
		return idDonThu;
	}

	public void setIdDonThu(int idDonThu) {
		this.idDonThu = idDonThu;
	}

}
