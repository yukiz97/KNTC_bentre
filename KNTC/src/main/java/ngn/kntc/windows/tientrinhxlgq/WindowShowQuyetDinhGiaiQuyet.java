package ngn.kntc.windows.tientrinhxlgq;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import ngn.kntc.beans.DoiTuongBiXuLyHanhChinhBean;
import ngn.kntc.beans.QuyetDinhGiaiQuyetBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;
import ngn.kntc.utils.UploadServiceUtil;

import com.jarektoro.responsivelayout.ResponsiveRow;
import com.vaadin.data.Item;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class WindowShowQuyetDinhGiaiQuyet extends Window{
	VerticalLayout vMainLayout = new VerticalLayout();
	
	Button btnDinhKem = new Button("KetQuaXuLy.pdf",FontAwesome.DOWNLOAD);
	
	VerticalLayout vDetail = new VerticalLayout();
	Label lblNgayDang = new Label("NGÀY ĐĂNG: ",ContentMode.HTML);
	Label lblCaptionNoiDung = new Label("<b style='color: #f15454;font-size: 16px;'>NỘI DUNG QUYẾT ĐỊNH:</b>",ContentMode.HTML);
	VerticalLayout vLayoutNoiDung = new VerticalLayout();
	Label lblCoQuanBanHanh = new Label("",ContentMode.HTML);
	Label lblNgayBanHanh = new Label("",ContentMode.HTML);
	Label lblTomTatNoiDung = new Label("",ContentMode.HTML);
	Label lblKetQuaGiaiQuyet = new Label("",ContentMode.HTML);
	Label lblHinhThucGiaiQuyet = new Label("",ContentMode.HTML);
	Label lblQuyenKhoiKien = new Label("",ContentMode.HTML);
	Label lblCaptionHuongXuLy = new Label("<b style='color: #f15454;font-size: 16px;'>BỒI THƯỜNG THIỆT HẠI:</b>",ContentMode.HTML);
	VerticalLayout vLayoutHuongXuLy = new VerticalLayout();
	Label lblThuHoiTien = new Label("",ContentMode.HTML);
	Label lblThuHoiDatO = new Label("",ContentMode.HTML);
	Label lblThuHoiDatSX = new Label("",ContentMode.HTML);
	Label lblTraLaiTien = new Label("",ContentMode.HTML);
	Label lblTraLaiDatO = new Label("",ContentMode.HTML);
	Label lblTraLaiDatSX = new Label("",ContentMode.HTML);
	Label lblSoNguoiDuocTraQuyenLoi = new Label("",ContentMode.HTML);
	Label lblXuPhatHanhChinh = new Label("",ContentMode.HTML);
	Label lblSoNguoiChuyenDieuTra = new Label("",ContentMode.HTML);
	Label lblCaptionXuPhatHanhChinh = new Label("<b style='color: #f15454;font-size: 16px;'>XỬ PHẠT HÀNH CHÍNH:</b>",ContentMode.HTML);
	Table tblDoiTuongBiXPHC = new Table();
	
	HorizontalLayout hLayoutQuyenKhoiKien = new HorizontalLayout();
	Label lblCheckQuyenKhoiKien = new Label("",ContentMode.HTML);
	
	String strCoQuanBanHanh,strNgayBanHanh,strTomTatNoiDung,strKetQuaGiaiQuyet,strHinhThucGiaiQuyet,strThuHoiTien,strThuHoiDatO,strThuHoiDatSX,strTraLaiTien,strTraLaiDatO,strTraLaiDatSX,strSoNguoiDuocTraQuyenLoi,strXuPhatHanhChinh,strSoNguoiChuyenDieuTra,strNgayDang;

	QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh = new QuaTrinhXuLyGiaiQuyetServiceUtil();
	UploadServiceUtil svUpload = new UploadServiceUtil();
	
	public WindowShowQuyetDinhGiaiQuyet(int idDonThu) {
		try {
			buildValue(idDonThu);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		lblNgayDang.setValue("<b style='color: #f15454;font-size: 16px;'>NGÀY ĐĂNG:</b> "+strNgayDang);
		lblNgayDang.addStyleName("lbl-ngaydang-tientrinh");
		
		vDetail.addComponents(lblNgayDang,lblCaptionNoiDung,vLayoutNoiDung,lblCaptionHuongXuLy,vLayoutHuongXuLy,lblCaptionXuPhatHanhChinh,tblDoiTuongBiXPHC);
		vDetail.setSpacing(true);
		vDetail.setSizeFull();
		vDetail.addStyleName("vLayout-detail-tientrinh");
		
		vMainLayout.addComponents(btnDinhKem,vDetail);
		vMainLayout.setExpandRatio(vDetail, 1.0f);
		vMainLayout.setComponentAlignment(btnDinhKem, Alignment.MIDDLE_RIGHT);
		vMainLayout.setMargin(true); 
		
		btnDinhKem.addStyleName("btn-download-detail-tientrinh");
		
		this.setCaption("   Quyết định giải quyết của đơn thư");
		this.setContent(vMainLayout);
		this.center();
		this.setWidth("900px");
		this.setIcon(FontAwesome.DEVIANTART);
		this.setModal(true);
		buildNoiDungQuyetDinh();
		buildBoiThuongThietHai();
	}
	
	private void buildNoiDungQuyetDinh()
	{
		/* row 1 */
		ResponsiveRow rowone=new ResponsiveRow();
		
		rowone.addColumn().withComponent(lblKetQuaGiaiQuyet).withDisplayRules(12, 6, 6, 6);
		rowone.addColumn().withComponent(lblHinhThucGiaiQuyet).withDisplayRules(12, 6, 6, 6);
		rowone.setHeight("44px");
		
		lblKetQuaGiaiQuyet.setValue("<b style='color: #3066bb;'>KẾT QUẢ GIẢI QUYẾT:</b> "+strKetQuaGiaiQuyet);
		lblHinhThucGiaiQuyet.setValue("<b style='color: #3066bb;'>HÌNH THỨC GIẢI QUYẾT:</b> "+strHinhThucGiaiQuyet);
		
		rowone.setWidth(100, Unit.PERCENTAGE);
		
		rowone.setSpacing(true);
		
		lblCoQuanBanHanh.setValue("<b style='color: #3066bb;'>CƠ QUAN BAN HÀNH:</b> "+strCoQuanBanHanh);
		lblNgayBanHanh.setValue("<b style='color: #3066bb;'>NGÀY BAN HÀNH:</b> "+strNgayBanHanh);
		lblTomTatNoiDung.setValue("<b style='color: #3066bb;'>TÓM TẮT NỘI DUNG:</b> "+strTomTatNoiDung);
		
		hLayoutQuyenKhoiKien.addComponents(lblQuyenKhoiKien,lblCheckQuyenKhoiKien);
		hLayoutQuyenKhoiKien.setSpacing(true);
		hLayoutQuyenKhoiKien.setHeight("22px");
		
		lblQuyenKhoiKien.setValue("<b style='color: #3066bb;'>QUYỀN KHỞI KIỆN: </b>");
		
		vLayoutNoiDung.addComponents(lblCoQuanBanHanh,lblNgayBanHanh,lblTomTatNoiDung,rowone,hLayoutQuyenKhoiKien);
		vLayoutNoiDung.setMargin(new MarginInfo(false,false,false,true));
		vLayoutNoiDung.setSpacing(true);
		vLayoutNoiDung.setSizeFull();
	}
	
	private void buildBoiThuongThietHai()
	{
		ResponsiveRow rowThuHoi=new ResponsiveRow();
		rowThuHoi.addColumn().withComponent(lblThuHoiTien).withDisplayRules(12, 4, 4, 4);
		rowThuHoi.addColumn().withComponent(lblThuHoiDatO).withDisplayRules(12, 4, 4, 4);
		rowThuHoi.addColumn().withComponent(lblThuHoiDatSX).withDisplayRules(12, 4, 4, 4);
		
		lblThuHoiTien.setValue("<b style='color: #3066bb;'>THU HỒI TIỀN:</b> "+strThuHoiTien+" <b>vnđ</b>");
		lblThuHoiDatO.setValue("<b style='color: #3066bb;'>THU HỒI ĐẤT Ở:</b> "+strThuHoiDatO+" <b>m²</b>");
		lblThuHoiDatSX.setValue("<b style='color: #3066bb;'>THU HỒI ĐẤT SẢN XUẤT:</b> "+strThuHoiDatSX+" <b>m²</b>");
		
		ResponsiveRow rowTraLai=new ResponsiveRow();
		rowTraLai.addColumn().withComponent(lblTraLaiTien).withDisplayRules(12, 4, 4, 4);
		rowTraLai.addColumn().withComponent(lblTraLaiDatO).withDisplayRules(12, 4, 4, 4);
		rowTraLai.addColumn().withComponent(lblTraLaiDatSX).withDisplayRules(12, 4, 4, 4);
		
		lblTraLaiTien.setValue("<b style='color: #3066bb;'>TRẢ LẠI TIỀN:</b> "+strTraLaiTien+" <b>vnđ</b>");
		lblTraLaiDatO.setValue("<b style='color: #3066bb;'>TRẢ LẠI ĐẤT Ở:</b> "+strTraLaiDatO+" <b>m²</b>");
		lblTraLaiDatSX.setValue("<b style='color: #3066bb;'>TRẢ LẠI ĐẤT SẢN XUẤT: </b> "+strTraLaiDatSX+" <b>m²</b>");
		
		ResponsiveRow rowSoNguoi=new ResponsiveRow();
		rowSoNguoi.addColumn().withComponent(lblSoNguoiDuocTraQuyenLoi).withDisplayRules(12, 6, 6, 6);
		rowSoNguoi.addColumn().withComponent(lblSoNguoiChuyenDieuTra).withDisplayRules(12, 6, 6, 6);
		
		lblSoNguoiDuocTraQuyenLoi.setValue("<b style='color: #3066bb;'>SỐ NGƯỜI ĐƯỢC TRẢ QUYỀN LỢI: </b>"+strSoNguoiDuocTraQuyenLoi+" <b>người</b>");
		lblSoNguoiChuyenDieuTra.setValue("<b style='color: #3066bb;'>SỐ NGƯỜI CHUYỂN ĐIỀU TRA: </b> "+strSoNguoiChuyenDieuTra+" <b>người</b>");
		
		lblXuPhatHanhChinh.setValue("<b style='color: #3066bb;'>XỬ PHẠT HÀNH CHÍNH: </b>"+strXuPhatHanhChinh+" <b>vnđ</b>");
		
		vLayoutHuongXuLy.addComponents(rowThuHoi,rowTraLai,rowSoNguoi,lblXuPhatHanhChinh);
		vLayoutHuongXuLy.setMargin(new MarginInfo(false,false,false,true));
		vLayoutHuongXuLy.setSpacing(true);
		vLayoutHuongXuLy.setSizeFull();
	}
	
	@SuppressWarnings("unchecked")
	private void buildTableDoiTuongBiXuPhatHanhChinh(int maQuyetDinh) throws SQLException
	{
		tblDoiTuongBiXPHC.addContainerProperty("STT", Integer.class, null);
		tblDoiTuongBiXPHC.addContainerProperty("Tên", String.class, null);
		tblDoiTuongBiXPHC.addContainerProperty("Chức vụ", String.class, null);
		tblDoiTuongBiXPHC.addContainerProperty("Hình thức xử lý", String.class, null);
		tblDoiTuongBiXPHC.addContainerProperty("Loại đối tượng", String.class, null);
		
		tblDoiTuongBiXPHC.addStyleName("table-vanban");
		tblDoiTuongBiXPHC.setSizeFull();
		tblDoiTuongBiXPHC.setPageLength(6);		
		
		tblDoiTuongBiXPHC.setColumnAlignment("STT", Align.CENTER);
		tblDoiTuongBiXPHC.setColumnAlignment("Loại đối tượng", Align.CENTER);
		
		List<DoiTuongBiXuLyHanhChinhBean> list = svQuaTrinh.getDoiTuongBiXuLyHanhChinhList(maQuyetDinh);
		int i = 0;
		for(DoiTuongBiXuLyHanhChinhBean model : list)
		{
			Item item = tblDoiTuongBiXPHC.addItem(++i);
			item.getItemProperty("STT").setValue(i);
			item.getItemProperty("Tên").setValue(model.getTenDoiTuong());
			item.getItemProperty("Chức vụ").setValue(model.getChucVu());
			item.getItemProperty("Hình thức xử lý").setValue(model.getHinhThucXuLy());
			item.getItemProperty("Loại đối tượng").setValue(model.isCaNhan()?"Cá nhân":"Cơ quan");
		}
	}
	
	private void buildValue(int idDonThu) throws SQLException
	{
		QuyetDinhGiaiQuyetBean modelQDGQ =  svQuaTrinh.getQuyetDinhGiaiQuyet(idDonThu);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			lblCheckQuyenKhoiKien.setIcon((modelQDGQ.isQuyenKhoiKien()?FontAwesome.CHECK:FontAwesome.REMOVE));
			strCoQuanBanHanh = DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.coquan.getName(),modelQDGQ.getMaCoQuanBanHanh()).getName();
			strNgayBanHanh = sdf.format(modelQDGQ.getNgayBanHanh());
			strNgayDang = sdf.format(modelQDGQ.getNgayTao());
			strTomTatNoiDung = modelQDGQ.getTomTatNoiDung();
			strKetQuaGiaiQuyet = DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.loaiquyetdinh.getName(),modelQDGQ.getLoaiKetQuaGiaiQuyet()).getName();
			strHinhThucGiaiQuyet = DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.hinhthucgiaiquyet.getName(),modelQDGQ.getHinhThucGiaiQuyet()).getName();
			strThuHoiTien = String.valueOf(modelQDGQ.getThuHoiTien());
			strThuHoiDatO = String.valueOf(modelQDGQ.getThuHoiDatO());
			strThuHoiDatSX = String.valueOf(modelQDGQ.getThuHoiDatSX());
			strTraLaiTien = String.valueOf(modelQDGQ.getTraLaiTien());
			strTraLaiDatO = String.valueOf(modelQDGQ.getTraLaiDatO());
			strTraLaiDatSX = String.valueOf(modelQDGQ.getTraLaiDatSX());
			strSoNguoiDuocTraQuyenLoi = String.valueOf(modelQDGQ.getSoNguoiDuocTraQuyenLoi());
			strSoNguoiChuyenDieuTra = String.valueOf(modelQDGQ.getSoNguoiChuyenDieuTra());
			strXuPhatHanhChinh = String.valueOf(modelQDGQ.getXuPhatHanhChinh());
			
			Resource source = new StreamResource(new StreamSource() {

				@Override
				public InputStream getStream() {
					String directory = UploadServiceUtil.getAbsolutePath()+File.separator+UploadServiceUtil.getFolderNameDonThu()+File.separator+idDonThu+File.separator+modelQDGQ.getLinkFileDinhKem();
					File file = new File(directory);
					try {
						return new FileInputStream(file);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						return null;
					}
				}
			}, modelQDGQ.getTenFileDinhKem());
			btnDinhKem.setCaption(modelQDGQ.getTenFileDinhKem());

			String fileExt = "";
			int lastIndexOf = modelQDGQ.getTenFileDinhKem().lastIndexOf(".");
			if (lastIndexOf == -1) {
				fileExt = ""; // empty extension
			}
			fileExt = modelQDGQ.getTenFileDinhKem().substring(lastIndexOf+1);
			System.out.println(fileExt+"------");

			if(fileExt.equalsIgnoreCase("pdf"))
			{
				btnDinhKem.addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						Window window = new Window();
						window.setWidth("90%");
						window.setHeight("90%");
						window.setCaption(modelQDGQ.getTenFileDinhKem());
						BrowserFrame e = new BrowserFrame("PDF File",source);
						//BrowserFrame e = new BrowserFrame("PDF File",new ExternalResource("http://demo.vaadin.com/sampler/"));
						e.setWidth("100%");
						e.setHeight("100%");
						window.setContent(e);
						window.center();
						window.setModal(true);
						UI.getCurrent().addWindow(window);	
					}
				});

			}
			else
			{
				FileDownloader downloader = new FileDownloader(source);
				downloader.extend(btnDinhKem);
			}
			
			buildTableDoiTuongBiXuPhatHanhChinh(modelQDGQ.getMaQuyetDinhGiaiQuyet());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
