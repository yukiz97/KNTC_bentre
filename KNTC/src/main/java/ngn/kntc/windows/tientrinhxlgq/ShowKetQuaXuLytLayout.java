package ngn.kntc.windows.tientrinhxlgq;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import ngn.kntc.beans.KetQuaXuLyBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;
import ngn.kntc.utils.UploadServiceUtil;

import com.jarektoro.responsivelayout.ResponsiveRow;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class ShowKetQuaXuLytLayout{
	VerticalLayout vMainLayout = new VerticalLayout();

	Button btnDinhKem = new Button("KetQuaXuLy.pdf",FontAwesome.DOWNLOAD);

	VerticalLayout vDetail = new VerticalLayout();
	Label lblNgayDang = new Label("NGÀY ĐĂNG: ",ContentMode.HTML);
	Label lblCaptionNoiDung = new Label("<b style='color: #f15454;font-size: 16px;'>NỘI DUNG XỬ LÝ:</b>",ContentMode.HTML);
	VerticalLayout vLayoutNoiDung = new VerticalLayout();
	Label lblTomTatNoiDung = new Label("",ContentMode.HTML);
	Label lblNgayXuLy = new Label("",ContentMode.HTML);
	Label lblCanBoXuLy = new Label("",ContentMode.HTML);
	Label lblCanBoDuyet = new Label("",ContentMode.HTML);
	Label lblCaptionHuongXuLy = new Label("<b style='color: #f15454;font-size: 16px;'>HƯỚNG XỬ LÝ:</b>",ContentMode.HTML);
	VerticalLayout vLayoutHuongXuLy = new VerticalLayout();
	Label lblNoiDungXuLy = new Label("",ContentMode.HTML);
	Label lblCoQuanXuLy = new Label("",ContentMode.HTML);
	Label lblHuongXuLy = new Label("",ContentMode.HTML);

	String ngayDang,tomTatNoiDung = "",ngayXuLy = "",canBoXuLy = "",canBoDuyet = "",coQuanXuLy = "",huongXuLy = "",noiDungXuLy = "";

	QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh = new QuaTrinhXuLyGiaiQuyetServiceUtil();
	UploadServiceUtil svUpload = new UploadServiceUtil();

	public ShowKetQuaXuLytLayout(int maHoSo) {
		buildLayout();
	}

	public void buildLayout() {
		lblNgayDang.setValue("<b style='color: #f15454;font-size: 16px;'>NGÀY ĐĂNG:</b> "+ngayDang);
		lblNgayDang.addStyleName("lbl-ngaydang-tientrinh");

		/* row 2 */
		lblTomTatNoiDung.setValue("<b style='color: #3066bb;'>TÓM TẮT NỘI DUNG:</b>"+tomTatNoiDung);
		/* row 3 */
		lblNgayXuLy.setValue("<b style='color: #3066bb;'>NGÀY XỬ LÝ:</b> "+ngayXuLy);
		/* row 4 */
		ResponsiveRow rowtwo=new ResponsiveRow();
		rowtwo.addColumn().withComponent(lblCanBoXuLy).withDisplayRules(12, 6, 6, 6);
		rowtwo.addColumn().withComponent(lblCanBoDuyet).withDisplayRules(12, 6, 6, 6);

		lblCanBoXuLy.setValue("<b style='color: #3066bb;'>CÁN BỘ XỬ LÝ:</b> "+canBoXuLy);
		lblCanBoDuyet.setValue("<b style='color: #3066bb;'>CÁN BỘ DUYỆT:</b> "+canBoDuyet);

		vLayoutNoiDung.addComponents(rowtwo,lblTomTatNoiDung,lblNgayXuLy);
		vLayoutNoiDung.setMargin(new MarginInfo(false,false,false,true));
		vLayoutNoiDung.setSpacing(true);
		vLayoutNoiDung.setSizeFull();

		/* row 5 */
		ResponsiveRow rowthree=new ResponsiveRow();
		rowthree.addColumn().withComponent(lblCoQuanXuLy).withDisplayRules(12, 6, 6, 6);
		rowthree.addColumn().withComponent(lblHuongXuLy).withDisplayRules(12, 6, 6, 6);

		lblNoiDungXuLy.setValue("<b style='color: #3066bb;'>NỘI DUNG XỬ LÝ:</b>"+noiDungXuLy);

		lblCoQuanXuLy.setValue("<b style='color: #3066bb;'>CƠ QUAN XỬ LÝ:</b> "+coQuanXuLy);
		lblHuongXuLy.setValue("<b style='color: #3066bb;'>HƯỚNG XỬ LÝ:</b> "+huongXuLy);

		vLayoutHuongXuLy.addComponents(lblNoiDungXuLy,rowthree);
		vLayoutHuongXuLy.setMargin(new MarginInfo(false,false,false,true));
		vLayoutHuongXuLy.setSpacing(true);
		vLayoutHuongXuLy.setSizeFull();

		vDetail.addComponents(lblNgayDang,lblCaptionNoiDung,vLayoutNoiDung,lblCaptionHuongXuLy,vLayoutHuongXuLy);
		vDetail.setSpacing(true);
		vDetail.setSizeFull();
		vDetail.addStyleName("vLayout-detail-tientrinh");

		vMainLayout.addComponents(btnDinhKem,vDetail);
		vMainLayout.setExpandRatio(vDetail, 1.0f);
		vMainLayout.setComponentAlignment(btnDinhKem, Alignment.MIDDLE_RIGHT);

		btnDinhKem.addStyleName("btn-download-detail-tientrinh");		
	}

	public void buildValue(KetQuaXuLyBean modelKQXL) throws Exception
	{
		try {
			huongXuLy=DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.huongxuly.getName(), modelKQXL.getMaHuongXuLy()).getName();
			tomTatNoiDung = modelKQXL.getTomTatNoiDung();
			ngayXuLy = new SimpleDateFormat("dd/MM/yyyy").format(modelKQXL.getNgayXuLy());
			canBoXuLy=modelKQXL.getCanBoXuLy();
			canBoDuyet=modelKQXL.getCanBoDuyet()!=null?modelKQXL.getCanBoDuyet():"";
			coQuanXuLy=DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.coquan.getName(), modelKQXL.getMaCQXL()).getName();
			ngayDang = new SimpleDateFormat("dd/MM/yyy").format(modelKQXL.getNgayTao());
			noiDungXuLy= modelKQXL.getNoiDungXuLy();

			Resource source = new StreamResource(new StreamSource() {

				@Override
				public InputStream getStream() {
					String directory = UploadServiceUtil.getAbsolutePath()+File.separator+UploadServiceUtil.getFolderNameDonThu()+File.separator+modelKQXL.getIdHoSo()+File.separator+modelKQXL.getLinkFileDinhKem();
					File file = new File(directory);
					try {
						return new FileInputStream(file);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						return null;
					}
				}
			}, modelKQXL.getTenFileDinhKem());
			btnDinhKem.setCaption(modelKQXL.getTenFileDinhKem());

			String fileExt = "";
			int lastIndexOf = modelKQXL.getTenFileDinhKem().lastIndexOf(".");
			if (lastIndexOf == -1) {
				fileExt = ""; // empty extension
			}
			fileExt = modelKQXL.getTenFileDinhKem().substring(lastIndexOf+1);
			System.out.println(fileExt+"------");

			if(fileExt.equalsIgnoreCase("pdf"))
			{
				btnDinhKem.addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						Window window = new Window();
						window.setWidth("90%");
						window.setHeight("90%");
						window.setCaption(modelKQXL.getTenFileDinhKem());
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public VerticalLayout getvMainLayout() {
		return vMainLayout;
	}

	public void setvMainLayout(VerticalLayout vMainLayout) {
		this.vMainLayout = vMainLayout;
	}
}
