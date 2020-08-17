package ngn.kntc.windows.tientrinhxlgq;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import ngn.kntc.beans.QuyetDinhThuLyBean;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;
import ngn.kntc.utils.UploadServiceUtil;

import com.jarektoro.responsivelayout.ResponsiveRow;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class WindowShowQuyetDinhThuLy extends Window{
	VerticalLayout vMainLayout = new VerticalLayout();
	
	Button btnDinhKem = new Button("KetQuaXuLy.pdf",FontAwesome.DOWNLOAD);
	
	VerticalLayout vDetail = new VerticalLayout();
	Label lblCoQuanThuLy = new Label("",ContentMode.HTML);
	Label lblNgayThuLy = new Label("",ContentMode.HTML);
	Label lblHanGiaiQuyet = new Label("",ContentMode.HTML);
	Label lblCanBoDuyet = new Label("",ContentMode.HTML);
	Label lblNgayDang = new Label("",ContentMode.HTML);
	
	QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh = new QuaTrinhXuLyGiaiQuyetServiceUtil();
	UploadServiceUtil svUpload = new UploadServiceUtil();
	
	String strNgayThuLy = "",strHanGiaiQuyet = "",strNgayDang = "",strCoQuanThuLy = "",strCanBoDuyet = "";
	
	public WindowShowQuyetDinhThuLy(int maHoSo) {
		try {
			buildValue(maHoSo);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		lblNgayDang.setValue("<b style='color: #f15454;font-size: 16px;'>NGÀY ĐĂNG:</b> "+strNgayDang);
		lblNgayDang.addStyleName("lbl-ngaydang-tientrinh");
		
		lblCoQuanThuLy.setValue("<b style='color: #3066bb;'>CƠ QUAN THỤ LÝ: </b>"+strCoQuanThuLy);
		lblCanBoDuyet.setValue("<b style='color: #3066bb;'>CÁN BỘ DUYỆT: </b>"+strCanBoDuyet);
		/* row 1 */
		ResponsiveRow rowone=new ResponsiveRow();
		
		rowone.addColumn().withComponent(lblNgayThuLy).withDisplayRules(12, 6, 6, 6);
		rowone.addColumn().withComponent(lblHanGiaiQuyet).withDisplayRules(12, 6, 6, 6);
		rowone.setHeight("44px");
		
		lblNgayThuLy.setValue("<b style='color: #3066bb;'>NGÀY THỤ LÝ: </b>"+strNgayThuLy);
		
		lblHanGiaiQuyet.setValue("<b style='color: #3066bb;'>HẠN GIẢI QUYẾT: </b>"+strHanGiaiQuyet);
		
		rowone.setWidth(100, Unit.PERCENTAGE);
		
		rowone.setSpacing(true);
		
		vDetail.addComponents(lblNgayDang,lblCoQuanThuLy,lblCanBoDuyet,rowone);
		vDetail.setSpacing(true);
		vDetail.setSizeFull();
		vDetail.addStyleName("vLayout-detail-tientrinh");
		
		vMainLayout.addComponents(btnDinhKem,vDetail);
		vMainLayout.setExpandRatio(vDetail, 1.0f);
		vMainLayout.setComponentAlignment(btnDinhKem, Alignment.MIDDLE_RIGHT);
		vMainLayout.setMargin(true); 
		
		btnDinhKem.addStyleName("btn-download-detail-tientrinh");
		
		this.setCaption("   Quyết định thụ lý của đơn thư");
		this.setContent(vMainLayout);
		this.center();
		this.setWidth("700px");
		this.setIcon(FontAwesome.DEVIANTART);
		this.setModal(true);
	}
	
	private void buildValue(int idDonThu) throws SQLException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		QuyetDinhThuLyBean modelQDTL =  svQuaTrinh.getQuyetDinhThuLy(idDonThu);
		
		try {
			strNgayThuLy = sdf.format(modelQDTL.getNgayThuLy());
			strHanGiaiQuyet = sdf.format(modelQDTL.getHanGiaiQuyet());
			strNgayDang = sdf.format(modelQDTL.getNgayTao());
			strCoQuanThuLy=OrganizationLocalServiceUtil.getOrganization(Integer.parseInt(modelQDTL.getMaCoQuanThuLy())).getName();
			strCanBoDuyet = modelQDTL.getCanBoDuyet();
			
			Resource source = new StreamResource(new StreamSource() {

				@Override
				public InputStream getStream() {
					String directory = UploadServiceUtil.getAbsolutePath()+File.separator+UploadServiceUtil.getFolderNameDonThu()+File.separator+idDonThu+File.separator+modelQDTL.getLinkFileDinhKem();
					File file = new File(directory);
					try {
						return new FileInputStream(file);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						return null;
					}
				}
			}, modelQDTL.getTenFileDinhKem());
			btnDinhKem.setCaption(modelQDTL.getTenFileDinhKem());

			String fileExt = "";
			int lastIndexOf = modelQDTL.getTenFileDinhKem().lastIndexOf(".");
			if (lastIndexOf == -1) {
				fileExt = ""; // empty extension
			}
			fileExt = modelQDTL.getTenFileDinhKem().substring(lastIndexOf+1);
			System.out.println(fileExt+"------");

			if(fileExt.equalsIgnoreCase("pdf"))
			{
				btnDinhKem.addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						Window window = new Window();
						window.setWidth("90%");
						window.setHeight("90%");
						window.setCaption(modelQDTL.getTenFileDinhKem());
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
		} catch (NumberFormatException | PortalException | SystemException e) {
			e.printStackTrace();
		}
	}
}
