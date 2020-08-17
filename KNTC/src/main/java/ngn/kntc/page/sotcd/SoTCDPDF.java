package ngn.kntc.page.sotcd;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ngn.kntc.beans.DonThuBean;
import ngn.kntc.beans.KetQuaXuLyBean;
import ngn.kntc.beans.SoTiepCongDanBean;
import ngn.kntc.beans.ThongTinDonThuBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.enums.LoaiDonThuEnum;
import ngn.kntc.pdf.PDFParterm;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;
import ngn.kntc.utils.SessionUtil;
import ngn.kntc.utils.TiepCongDanServiceUtil;

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
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.UI;

public class SoTCDPDF extends PDFParterm{
	List<SoTiepCongDanBean> listTCD = new ArrayList<SoTiepCongDanBean>();
	
	public SoTCDPDF() {
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
        fontProvider.register(VaadinService.getCurrent().getBaseDirectory()+File.separator+"VAADIN"+File.separator+"themes"+File.separator+UI.getCurrent().getTheme()+File.separator+"fonts"+File.separator+"TIMES.TTF");
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
		sb.append("<div style='padding:5px 0px 5px 0px; font-family:"+getFONT()+";'>"
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
		sb.append("<div style='padding:5px 0px 5px 0px; font-family:"+getFONT()+";'><h3><b>CHI TIẾT</b></h3></div>");
    	sb.append("<table width='100%' border='1' cellpadding='3px' cellspacing='0'>");
    	sb.append("<tr> <td width='48' rowspan='2' style='width:36.2pt;border-top:solid windowtext 1.0pt; border-left:solid windowtext 1.0pt;border-bottom:none;border-right:none; background:white;padding:0in 0in 0in 0in'> <p align='center' style='margin-top:6.0pt;text-align:center'><b>STT</b></p> </td> <td width='76' rowspan='2' style='width:57.2pt;border-top:solid windowtext 1.0pt; border-left:solid windowtext 1.0pt;border-bottom:none;border-right:none; background:white;padding:0in 0in 0in 0in'> <p align='center' style='margin-top:6.0pt;text-align:center'><b>Ngày tiếp</b></p> </td> <td width='142' rowspan='2' style='width:106.3pt;border-top:solid windowtext 1.0pt; border-left:solid windowtext 1.0pt;border-bottom:none;border-right:none; background:white;padding:0in 0in 0in 0in'> <p align='center' style='margin-top:6.0pt;text-align:center'><b>Họ tên - Địa chỉ - CMND/Hộ chiếu của công dân</b></p> </td> <td width='100' rowspan='2' style='width:74.9pt;border-top:solid windowtext 1.0pt; border-left:solid windowtext 1.0pt;border-bottom:none;border-right:none; background:white;padding:0in 0in 0in 0in'> <p align='center' style='margin-top:6.0pt;text-align:center'><b>Nội dung vụ việc</b></p> </td> <td width='86' rowspan='2' style='width:64.55pt;border-top:solid windowtext 1.0pt; border-left:solid windowtext 1.0pt;border-bottom:none;border-right:none; background:white;padding:0in 0in 0in 0in'> <p align='center' style='margin-top:6.0pt;text-align:center'><b>Phân <span style='background:white'>loại</span> đơn/Số người</b></p> </td> <td width='90' rowspan='2' style='width:67.2pt;border-top:solid windowtext 1.0pt; border-left:solid windowtext 1.0pt;border-bottom:none;border-right:none; background:white;padding:0in 0in 0in 0in'> <p align='center' style='margin-top:6.0pt;text-align:center'><b>Cơ quan đã giải quyết</b></p> </td> <td width='317' colspan='3' style='width:238.05pt;border-top:solid windowtext 1.0pt; border-left:solid windowtext 1.0pt;border-bottom:none;border-right:none; background:white;padding:0in 0in 0in 0in'> <p align='center' style='margin-top:6.0pt;text-align:center'><b>Hướng xử lý</b></p> </td> <td width='70' rowspan='2' style='width:52.45pt;border-top:solid windowtext 1.0pt; border-left:solid windowtext 1.0pt;border-bottom:none;border-right:none; background:white;padding:0in 0in 0in 0in'> <p align='center' style='margin-top:6.0pt;text-align:center'><b>Theo dõi kết quả giải quyết</b></p> </td> <td width='72' rowspan='2' style='width:54.3pt;border:solid windowtext 1.0pt; border-bottom:none;background:white;padding:0in 0in 0in 0in'> <p align='center' style='margin-top:6.0pt;text-align:center'><b>Ghi chú</b></p> </td> </tr> <tr> <td width='89' style='width:66.95pt;border-top:solid windowtext 1.0pt; border-left:solid windowtext 1.0pt;border-bottom:none;border-right:none; background:white;padding:0in 0in 0in 0in'> <p align='center' style='margin-top:6.0pt;text-align:center'><b>Thụ lý để giải quyết</b></p> </td> <td width='97' style='width:72.7pt;border-top:solid windowtext 1.0pt; border-left:solid windowtext 1.0pt;border-bottom:none;border-right:none; background:white;padding:0in 0in 0in 0in'> <p align='center' style='margin-top:6.0pt;text-align:center'><b>Trả lại đơn và hướng dẫn</b></p> </td> <td width='131' style='width:98.4pt;border-top:solid windowtext 1.0pt; border-left:solid windowtext 1.0pt;border-bottom:none;border-right:none; background:white;padding:0in 0in 0in 0in'> <p align='center' style='margin-top:6.0pt;text-align:center'><b>Chuyển đơn đến cơ quan, tổ chức <span style='background:white'>đơn vị</span> có thẩm quyền</b></p> </td> </tr>");
        
    	TiepCongDanServiceUtil svTCD = new TiepCongDanServiceUtil();
    	DonThuServiceUtil svDonThu = new DonThuServiceUtil();
    	QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh = new QuaTrinhXuLyGiaiQuyetServiceUtil();
    	String value = "";
    	int i = 1;
    	for(SoTiepCongDanBean model : listTCD)
    	{
    		try {
    			String noiDungVuViec = "";
    			String loaiDon = "";
    			String traDonVaHuongDan = "";
    			String coQuanDaGiaiQuyet = "";
    			String thamQuyenGiaiQuyet = "";
    			String chuyenCoQuan = "";
    			String theoDoiKetQuaGiaiQuyet = "";
    			DonThuBean modelDonThu = null;
    			/* Đơn thư */
    			if(model.getMaDonThu()!=0)
    			{
    				modelDonThu = svDonThu.getDonThu(model.getMaDonThu());
    				noiDungVuViec = modelDonThu.getNoiDungDonThu();
    				
    				if(modelDonThu.getMaCoQuanDaGiaiQuyet()!=null)
    					coQuanDaGiaiQuyet = DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.coquan.getName(), modelDonThu.getMaCoQuanDaGiaiQuyet()).getName();
    			
    				for(LoaiDonThuEnum e : LoaiDonThuEnum.values())
    				{
    					if(e.getType()==modelDonThu.getLoaiDonThu())
    						loaiDon = e.getName()+" / "+model.getSoNguoiDiKNTC();
    				}
    				
    				KetQuaXuLyBean modelKQXL = svQuaTrinh.getKetQuaXuLy(model.getMaDonThu(), SessionUtil.getOrgId());
    				if(modelKQXL!=null)
    				{
    					if(modelKQXL.getMaHuongXuLy()==2 || modelKQXL.getMaHuongXuLy()==7)
    						traDonVaHuongDan = "X";
    					if(modelKQXL.getMaHuongXuLy()==4)
    						thamQuyenGiaiQuyet = "X";
    					if(modelKQXL.getMaHuongXuLy()==5)
    					{
    						ThongTinDonThuBean modelTTDT = svDonThu.getThongTinDonThuChuyenDi(model.getMaDonThu(), SessionUtil.getOrgId());
    						if(modelTTDT!=null)
    							chuyenCoQuan = OrganizationLocalServiceUtil.getOrganization(modelTTDT.getOrgNhan()).getName()+" - <b>Số văn bản chuyển đơn:</b> "+modelTTDT.getSoVanBanDen();
    					}
    				}
    			}
    			else
    			{
    				loaiDon ="Không đơn / "+model.getSoNguoiDiKNTC();
    				noiDungVuViec = "<b>Tiếp không đơn:</b><br/>"+model.getNoiDungTiepCongDan();
    			}
    			
    			value+="<tr>";
        		value+="<td style='text-align: center'>"+i+"</td>";
        		value+="<td style='text-align: center'>"+new SimpleDateFormat("dd/MM/yyyy").format(model.getNgayTiepCongDan())+"</td>";
				value+="<td>"+svTCD.returnTenNguoiDaiDienSoTCDPDF(model)+"</td>";
				value+="<td>"+noiDungVuViec+"</td>";
				value+="<td>"+loaiDon+"</td>";
				value+="<td>"+coQuanDaGiaiQuyet+"</td>";
				value+="<td style='text-align: center'>"+thamQuyenGiaiQuyet+"</td>";
				value+="<td style='text-align: center'>"+traDonVaHuongDan+"</td>";
				value+="<td>"+chuyenCoQuan+"</td>";
				value+="<td>"+theoDoiKetQuaGiaiQuyet+"</td>";
				value+="<td>"+""+"</td>";
				value+="</tr>";
			} catch (Exception e) {
				e.printStackTrace();
			}
    		i++;
    	}
    	value = value.replace("<br>", "<br/>");
    	value = value.replace("</br>", "<br/>");
    	sb.append(value);
        sb.append("</table>");
    	return sb;
	}
	public List<SoTiepCongDanBean> getListTCD() {
		return listTCD;
	}
	public void setListTCD(List<SoTiepCongDanBean> listTCD) {
		this.listTCD = listTCD;
	}
}
