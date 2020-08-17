package ngn.kntc.windows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import ngn.kntc.beans.VanBanQuyPhamBean;
import ngn.kntc.enums.LoaiVanBanEnum;
import ngn.kntc.modules.KNTCProps;
import ngn.kntc.page.vanbanquypham.ThemVanBanLayout;
import ngn.kntc.utils.UploadServiceUtil;
import ngn.kntc.utils.VanBanQuyPhamServiceUtil;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class WindowVanBanDetail extends Window{
	HorizontalLayout hLayoutMain = new HorizontalLayout();
	
	VerticalLayout vLayoutDetail = new VerticalLayout();
	Label lblTenVanBan = new Label("",ContentMode.HTML);
	Label lblSoHieu = new Label("",ContentMode.HTML);
	Label lblTrichDan = new Label("",ContentMode.HTML);
	Label lblCoQuanBanHanh = new Label("",ContentMode.HTML);
	Label lblNguoiKy = new Label("",ContentMode.HTML);
	Label lblNgayBanHanh = new Label("",ContentMode.HTML);
	Label lblLoaiVanBan = new Label("",ContentMode.HTML);
	Label lblTepDinhKem = new Label("<b>Tệp đính kèm:</b>",ContentMode.HTML);
	HorizontalLayout hDownload = new HorizontalLayout();
	Button btnDownload = new Button();
	
	ThemVanBanLayout formEdit;
	
	VanBanQuyPhamServiceUtil svVanBan = new VanBanQuyPhamServiceUtil();
	
	KNTCProps props = new KNTCProps();
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	int id;
	
	public WindowVanBanDetail(int id) {
		this.id = id;
		formEdit = new ThemVanBanLayout(id);
		formEdit.setCaptionInvisible();
		
		try {
			VanBanQuyPhamBean model = svVanBan.getVanBanQuyPham(this.id);
			setDetailLabelValue(model);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		buildLayout();
		configComponent();
	}
	
	private void buildLayout()
	{
		hLayoutMain.addComponent(vLayoutDetail);
		hLayoutMain.addComponent(formEdit);
		
		formEdit.getBtnHuy().setVisible(false);
		
		hLayoutMain.setMargin(true);
		hLayoutMain.setWidth("100%");
		
		this.setContent(hLayoutMain);
		this.setWidth("100%");
		this.center();
		this.setModal(true);
		this.setCaptionAsHtml(true);
		this.setCaption(FontAwesome.FILE.getHtml()+" Thông tin chi tiết của văn bản");
		this.addStyleName("window-detail-vanban");
		
		buildLayoutDetail();
	}
	
	private void configComponent()
	{
		formEdit.getBtnLuu().addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(formEdit.isValidateSuccess())
				{
					try {
						setDetailLabelValue(formEdit.getModelVanBan());
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	private void buildLayoutDetail()
	{
		hDownload.addComponents(lblTepDinhKem,btnDownload);
		hDownload.setSpacing(true);
		lblTepDinhKem.setSizeUndefined();
		btnDownload.setCaptionAsHtml(true);
		btnDownload.setIcon(FontAwesome.DOWNLOAD);
		
		vLayoutDetail.addComponent(lblTenVanBan);
		vLayoutDetail.addComponent(lblSoHieu);
		vLayoutDetail.addComponent(lblTrichDan);
		vLayoutDetail.addComponent(lblCoQuanBanHanh);
		vLayoutDetail.addComponent(lblNguoiKy);
		vLayoutDetail.addComponent(lblNgayBanHanh);
		vLayoutDetail.addComponent(lblLoaiVanBan);
		vLayoutDetail.addComponent(hDownload);
		
		vLayoutDetail.addStyleName("layout-detail-vanban");
		
		vLayoutDetail.setSpacing(true);
		vLayoutDetail.setWidth("100%");
	}
	
	private void setDetailLabelValue(VanBanQuyPhamBean model) throws SQLException
	{
		String loaiVanBanDisplay = "";
		hDownload.removeComponent(btnDownload);
		
		lblTenVanBan.setValue("<b style='font-size: 17px;color: #c12828;'>"+model.getTenVanBan()+"</b>");
		lblSoHieu.setValue("<b>Số hiệu: </b><b style='color: #cb4a4a'>"+model.getSoHieu()+"</b>");
		lblTrichDan.setValue("<b>Trích dẫn: </b>"+model.getTrichDan());
		lblCoQuanBanHanh.setValue("<b>Cơ quan ban hành: </b>"+model.getCoQuanBanHanh());
		lblNguoiKy.setValue("<b>Người ký: </b>"+model.getNguoiKy());
		lblNgayBanHanh.setValue("<b>Ngày ban hành: </b>"+sdf.format(model.getNgayBanHanh())+"<span style='float: right'><b>Ngày tạo: </b>"+sdf.format(svVanBan.getVanBanQuyPham(id).getNgayTao())+"</span>");
		
		for(LoaiVanBanEnum e : LoaiVanBanEnum.values())
		{
			for(String type : model.getLoaiVanBan().split(","))
			{
				int typeInt = Integer.parseInt(type);
				if(typeInt == e.getType())
					loaiVanBanDisplay +=e.getName()+", ";
			}
		}
		lblLoaiVanBan.setValue("<b>Loại văn bản: </b>"+loaiVanBanDisplay.substring(0, loaiVanBanDisplay.length()-2));
		
		btnDownload = new Button("<span style='color: #1f69a5'>"+model.getTenFileDinhKem()+"</span>",FontAwesome.DOWNLOAD);
		btnDownload.setCaptionAsHtml(true);
		btnDownload.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		
		Resource resource = new StreamResource(new StreamSource() {

			@Override
			public InputStream getStream() {
				String directory = UploadServiceUtil.getAbsolutePath()+File.separator+UploadServiceUtil.getFolderNameVanBan()+File.separator+id+File.separator+model.getLinkFileDinhKem();
				File file = new File(directory);
				try {
					return new FileInputStream(file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return null;
				}
			}
		}, model.getTenFileDinhKem());
		
		FileDownloader downloader = new FileDownloader(resource);
		downloader.extend(btnDownload);
		hDownload.addComponent(btnDownload);
	}
}
