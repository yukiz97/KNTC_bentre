package ngn.kntc.page.tracuu;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import ngn.kntc.pdf.PDFParterm;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

public class SearchPDF extends PDFParterm{
	final String STT = "STT";
	final String LOAINGUOIDUNGDON = "Loại chủ thể";
	final String NGUOIDUNGDON = "Người đứng đơn";
	final String NOIDUNG = "Nội dung đơn thư";
	final String NGUOIBIKNTC = "Người bị khiếu tố";
	final String LOAIDONTHU = "Loại đơn thư";
	final String LINHVUC = "Lĩnh vực";
	final String NGAYNHAPDON = "Ngày nhập đơn";
	final String NGUOINHAP = "Người nhập";

	Table tblChitiet;

	public SearchPDF() {
		super();
	}

	public void createPdf() throws FileNotFoundException, DocumentException {
		Document document = new Document(PageSize.A4.rotate(), (float)getMLEFT(), (float)getMRIGHT(), (float)getMTOP(), (float)getMBOTTOM());

		PdfWriter writer = PdfWriter.getInstance(document, getByteArrayOutputStream());
		document.open();

		CSSResolver cssResolver = new StyleAttrCSSResolver();
		CssFile cssFile = XMLWorkerHelper.getCSS(new ByteArrayInputStream(getCSS().getBytes()));
		cssResolver.addCss(cssFile);

		// HTML
		XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
		fontProvider.register(VaadinService.getCurrent().getBaseDirectory()+File.separator+"VAADIN"+File.separator+"themes"+File.separator+UI.getCurrent().getTheme()+File.separator+"fonts"+File.separator+".VnArial.ttf");
		CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
		HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
		htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());

		// Pipelines
		PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
		HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
		CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);

		// XML Worker
		XMLWorker worker = new XMLWorker(css, true);
		XMLParser p = new XMLParser(worker);

		StringBuilder header=getCoQuan_AND_CHXH();
		try {
			p.parse(new ByteArrayInputStream(header.toString().getBytes()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		StringBuilder title=getTitle();
		try {
			p.parse(new ByteArrayInputStream(title.toString().getBytes()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		/*  StringBuilder summary=getSummary();
        try {
			p.parse(new ByteArrayInputStream(summary.toString().getBytes()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}*/

		StringBuilder detail=getDetail();
		try {
			p.parse(new ByteArrayInputStream(detail.toString().getBytes()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		StringBuilder footer=getSignal();
		try {
			p.parse(new ByteArrayInputStream(footer.toString().getBytes()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		document.close();
	}

	public StringBuilder getTitle(){
		StringBuilder title = new StringBuilder();
		title.append("<div align='center' style='padding:40px; font-family:"+getFONT()+";'>"
				+ "<div align='center' style='font-size:18px'><b>"+getStrTieude()+"</b><br/><i>"+getStrSubTieude()+"</i></div>"
				+ "</div>");
		return title;
	}

	/*public StringBuilder getSummary(){
		StringBuilder sb = new StringBuilder();
		value = "<div style='padding:5px 0px 5px 0px; font-family:"+getFONT()+";'>"
				+"<h3><b>I. TÓM TẮT</b></h3>"
				+"<h3>&nbsp;&nbsp;&nbsp;&nbsp;+ Tổng số đơn thư chưa xử lý: "+getchuaXuLy()+"</h3>"
				+"<h3>&nbsp;&nbsp;&nbsp;&nbsp;+ Tổng số đơn thư đang xử lý: "+getdangXuLy()+"</h3>"
				+"<h3>&nbsp;&nbsp;&nbsp;&nbsp;+ Tổng số đơn thư đã xử lý: "+getdaXuLy()+"</h3>"
				+"<h3>&nbsp;&nbsp;&nbsp;&nbsp;+ Tổng số đơn thư đã trả kết quả: "+getDaCoKetQua()+"</h3>"
				+ "</div>");
		return sb;
	}*/

	public StringBuilder getDetail(){
		StringBuilder sb = new StringBuilder();
		String value = "";
		value += "<div style='padding:5px 0px 5px 0px; font-family:"+getFONT()+";'></div>";
		value += "<table width='100%' border='1' cellpadding='3px' cellspacing='0'>";
		value += "<tr style='background:"+getBgColorHeader()+"'>";
		if(!tblChitiet.isColumnCollapsed(STT))
			value += "<th>"+STT+"</th>";
		if(!tblChitiet.isColumnCollapsed(LOAINGUOIDUNGDON))	
			value += "<th>"+LOAINGUOIDUNGDON+"</th>";
		if(!tblChitiet.isColumnCollapsed(NGUOIDUNGDON))
			value += "<th>"+NGUOIDUNGDON+"</th>";
		if(!tblChitiet.isColumnCollapsed(NOIDUNG))
			value += "<th>"+NOIDUNG+"</th>";
		if(!tblChitiet.isColumnCollapsed(NGUOIBIKNTC))
			value += "<th>"+NGUOIBIKNTC+"</th>";
		if(!tblChitiet.isColumnCollapsed(LOAIDONTHU))
			value += "<th>"+LOAIDONTHU+"</th>";
		if(!tblChitiet.isColumnCollapsed(LINHVUC))
			value += "<th>"+LINHVUC+"</th>";
		if(!tblChitiet.isColumnCollapsed(NGAYNHAPDON))
			value += "<th>"+NGAYNHAPDON+"</th>";
		if(!tblChitiet.isColumnCollapsed(NGUOINHAP))
			value += "<th>"+NGUOINHAP+"</th>";
		/* if(!tblChitiet.isColumnCollapsed(NGAYHOANTHANH))
        	value = "<th>"+NGAYHOANTHANH+"</th>");
        if(!tblChitiet.isColumnCollapsed(KETQUA))
        	value = "<th>"+KETQUA+"</th>");*/
		value += "</tr>";

		String stt="",loaiNguoi="",tenChuThe="",noiDung="",nguoiBiKNTC="",loaiDonThu="",linhVuc="",ngayNhapDon="",nguoiNhap="";
		for(Object itemId:tblChitiet.getItemIds()){
			Item item = tblChitiet.getItem(itemId);
			for (Object column : item.getItemPropertyIds()) {
				Property<?> prop = item.getItemProperty(column);
				String value2="";
				if(prop.getType().equals(Label.class)){
					Label lbl = (Label) prop.getValue();
					value2=lbl.getValue();
				} else {
					if(prop.getValue()!=null)
						value2=prop.getValue().toString();
					else
						value2="";
				}
				if(column.equals(STT))
					stt=value2;
				if(column.equals(LOAINGUOIDUNGDON))
					loaiNguoi=value2;
				if(column.equals(NGUOIDUNGDON))
					tenChuThe=value2;
				if(column.equals(NOIDUNG))
					noiDung=value2;
				if(column.equals(NGUOIBIKNTC))
					nguoiBiKNTC=value2;
				if(column.equals(LOAIDONTHU))
					loaiDonThu=value2;
				if(column.equals(LINHVUC))
					linhVuc=value2;
				if(column.equals(NGAYNHAPDON))
					ngayNhapDon=value2;
				if(column.equals(NGUOINHAP))
					nguoiNhap=value2;
				/*if(column.equals(NGAYHOANTHANH))
        			ngayHoanThanh=value;
        		if(column.equals(KETQUA))
        			ketQua=value;*/
			}

			value += "<tr style='background:"+getBgColorBody()+"'>";
			if(!tblChitiet.isColumnCollapsed(STT))
				value += "<td style='text-align: center;'>"+stt+"</td>";
			if(!tblChitiet.isColumnCollapsed(LOAINGUOIDUNGDON))	
				value += "<td>"+loaiNguoi+"</td>";
			if(!tblChitiet.isColumnCollapsed(NGUOIDUNGDON))
				value += "<td>"+tenChuThe+"</td>";
			if(!tblChitiet.isColumnCollapsed(NOIDUNG))
				value += "<td style='text-align: center;'>"+noiDung+"</td>";
			if(!tblChitiet.isColumnCollapsed(NGUOIBIKNTC))
				value += "<td style='text-align: center;'>"+nguoiBiKNTC+"</td>";
			if(!tblChitiet.isColumnCollapsed(LOAIDONTHU))
				value += "<td style='text-align: center;'>"+loaiDonThu+"</td>";
			if(!tblChitiet.isColumnCollapsed(LINHVUC))
				value += "<td style='text-align: center;'>"+linhVuc+"</td>";
			if(!tblChitiet.isColumnCollapsed(NGAYNHAPDON))
				value += "<td>"+ngayNhapDon+"</td>";
			if(!tblChitiet.isColumnCollapsed(NGUOINHAP))
				value += "<td style='text-align: center;'>"+nguoiNhap+"</td>";
			/*if(!tblChitiet.isColumnCollapsed(NGAYHOANTHANH))
					value = "<td style='text-align: center;'>"+ngayHoanThanh+"</td>");
			if(!tblChitiet.isColumnCollapsed(KETQUA))
					value = "<td>"+ketQua+"</td>");*/
			value += "</tr>";
		}
		value += "</table>";
		
		value = value.replace("<br>", "<br/>");
    	value = value.replace("</br>", "<br/>");
		
    	sb.append(value);
    	
		return sb;
	}

	public Table getTblChitiet() {
		return tblChitiet;
	}

	public void setTblChitiet(Table tblChitiet) {
		this.tblChitiet = tblChitiet;
	}
}
