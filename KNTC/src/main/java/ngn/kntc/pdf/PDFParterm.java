package ngn.kntc.pdf;

import java.io.ByteArrayOutputStream;

public class PDFParterm{
	
	double MTOP=40,MRIGHT=40,MBOTTOM=40,MLEFT=40;
	int SIZEFONT=13;
	String BgColorHeader="#f7f7f7",BgColorBody="";
	String FONT="Times New Roman";
	String strTochuc="Ban khoa giáo Bình Dương";
	String htmlCHXH="<div align='center'><b>CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM</b><br/><u>Độc lập - Tự do - Hạnh phúc</u></div>";
	String strTieude="Báo cáo tổng hợp được giao";
	String strSubTieude="(Của cán bộ Quách Anh Dũng)";
	String htmlTitle="<div align='center'><b>BÁO CÁO CÔNG VIỆC TRONG THÁNG</b></div>";
	
	String CSS = " "
    		+ "table{font-family:"+FONT+"; font-size:"+SIZEFONT+"px;}   "
    		+ "tr { text-align: center; } "
    		+ "th { font-weight:bold; } "
    		+ "td { text-align:left; }";
	
	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	
	public PDFParterm() {
		
	}
	
	public StringBuilder getCoQuan_AND_CHXH(){
    	StringBuilder header = new StringBuilder();
	    	header.append("<div style='float;left; font-family:"+FONT+"; width:100%;'>"
	        		+ "<div style='float:left; width:400px; text-transform: uppercase;'><div align='center'><b>"+strTochuc+"</b></div></div>"
	        		+ "<div style='float:right; width:500px'>"+htmlCHXH+"</div>"
	        		+ "</div>");
        return header;
    }
    
    public StringBuilder getSignal(){
		StringBuilder signal = new StringBuilder();
		signal.append("<div style='font-family:" + FONT + "; width:100%;'>"
				+ "<div style='float:right; text-align:center; width:400px'>"
				+ "<div align='center'><i><br/><br/>.........., ngày......, tháng......, năm.......</i><br/><b>Ký tên</b></div>"
				+ "</div>" 
				+ "</div>");
		return signal;
    }
    
	public double getMBOTTOM() {
		return MBOTTOM;
	}

	public void setMBOTTOM(double mBOTTOM) {
		MBOTTOM = mBOTTOM;
	}
	
	public double getMTOP() {
		return MTOP;
	}

	public double getMRIGHT() {
		return MRIGHT;
	}

	public double getMLEFT() {
		return MLEFT;
	}

	public int getSIZEFONT() {
		return SIZEFONT;
	}

	public void setMTOP(double mTOP) {
		MTOP = mTOP;
	}

	public void setMRIGHT(double mRIGHT) {
		MRIGHT = mRIGHT;
	}

	public void setMLEFT(double mLEFT) {
		MLEFT = mLEFT;
	}

	public void setSIZEFONT(int sIZEFONT) {
		SIZEFONT = sIZEFONT;
	}

	public void setBgColorHeader(String bgColorHeader) {
		BgColorHeader = bgColorHeader;
	}

	public void setBgColorBody(String bgColorBody) {
		BgColorBody = bgColorBody;
	}

	public void setStrTochuc(String strTochuc) {
		this.strTochuc = strTochuc;
	}

	public void setStrTieude(String strTieude) {
		this.strTieude = strTieude;
	}

	public void setStrSubTieude(String strSubTieude) {
		this.strSubTieude = strSubTieude;
	}

	public void setCSS(String cSS) {
		CSS = cSS;
	}

	public String getCSS() {
		return CSS;
	}

	public ByteArrayOutputStream getByteArrayOutputStream() {
		return byteArrayOutputStream;
	}

	public void setByteArrayOutputStream(ByteArrayOutputStream byteArrayOutputStream) {
		this.byteArrayOutputStream = byteArrayOutputStream;
	}

	public String getFONT() {
		return FONT;
	}

	public void setFONT(String fONT) {
		FONT = fONT;
	}

	public String getHtmlCHXH() {
		return htmlCHXH;
	}

	public void setHtmlCHXH(String htmlCHXH) {
		this.htmlCHXH = htmlCHXH;
	}

	public String getHtmlTitle() {
		return htmlTitle;
	}

	public void setHtmlTitle(String htmlTitle) {
		this.htmlTitle = htmlTitle;
	}

	public String getBgColorHeader() {
		return BgColorHeader;
	}

	public String getBgColorBody() {
		return BgColorBody;
	}

	public String getStrTochuc() {
		return strTochuc;
	}

	public String getStrTieude() {
		return strTieude;
	}

	public String getStrSubTieude() {
		return strSubTieude;
	}
}
