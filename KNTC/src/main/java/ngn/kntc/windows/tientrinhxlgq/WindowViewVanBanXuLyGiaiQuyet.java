package ngn.kntc.windows.tientrinhxlgq;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ngn.kntc.beans.VanBanXuLyGiaiQuyetBean;
import ngn.kntc.enums.LoaiVanBanXuLyGiaiQuyetEnum;
import ngn.kntc.modules.ControlPagination;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;
import ngn.kntc.utils.UploadServiceUtil;

import com.jensjansson.pagedtable.PagedTable;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.themes.ValoTheme;

public class WindowViewVanBanXuLyGiaiQuyet extends Window{
	VerticalLayout vMainLayout = new VerticalLayout();

	Panel pnlFilter = new Panel(); 
	HorizontalLayout hFilter = new HorizontalLayout();
	ComboBox cmbNguoiNhap = new ComboBox();

	PagedTable tblVanBan = new PagedTable();
	ControlPagination control = new ControlPagination(tblVanBan);
	IndexedContainer container = new IndexedContainer();

	String STT = "STT";
	String LOAIVANBAN = "Loại văn bản";
	String NGAYBANHANH = "Ngày ban hành";
	String HANGIAIQUYET = "Hạn giải quyết";
	String SOKYHIEU = "Số ký hiệu";
	String NOIDUNG = "Nội dung";
	String GHICHU = "Ghi chú";
	String TEPDINHKEM = "Tệp đính kèm";
	String HOANTHANH = "Ngày hoàn thành";

	int idDonThu = -1;

	QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh = new QuaTrinhXuLyGiaiQuyetServiceUtil();

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	public WindowViewVanBanXuLyGiaiQuyet(int idDonThu) {
		this.idDonThu = idDonThu;

		try {
			loadDefaultValue();
			buildLayout();
		} catch (Exception e) {
			e.printStackTrace();
		}
		configComponent();
	}

	private void loadDefaultValue() throws Exception {
		List<Integer> listUserNhap = svQuaTrinh.getListUserNhapVanBanXuLyGiaiQuyetOfDonThu(idDonThu);
		
		cmbNguoiNhap.addItem(1);
		cmbNguoiNhap.setItemCaption(1, "Tất cả");
		for(Integer userId : listUserNhap)
		{
			String userName = UserLocalServiceUtil.getUser(userId).getFirstName();
			
			cmbNguoiNhap.addItem(userId);
			cmbNguoiNhap.setItemCaption(userId, userName);
		}
		
		cmbNguoiNhap.setNullSelectionAllowed(false);
		cmbNguoiNhap.select(1);
	}

	private void buildLayout() throws Exception {
		vMainLayout.addComponent(pnlFilter);
		vMainLayout.addComponent(tblVanBan);
		vMainLayout.addComponent(control);
		
		pnlFilter.setContent(hFilter);
		
		hFilter.addComponents(new Label("<b>Người nhập:</b>",ContentMode.HTML),cmbNguoiNhap);
		
		hFilter.setSpacing(true);
		hFilter.setMargin(true);

		vMainLayout.setWidth("100%");
		vMainLayout.setMargin(true);
		vMainLayout.setSpacing(true);

		this.setContent(vMainLayout);
		this.center();
		this.setWidth("100%");
		this.setModal(true);
		this.setCaption("Danh sách văn bản đã được nhập của đơn thư");

		buildTable();
		loadData();
	}

	private void configComponent() {
		cmbNguoiNhap.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				try {
					loadData();
				} catch (Exception e) {
					e.printStackTrace();
				}
				center();
			}
		});
	}

	private void buildTable()
	{
		container.addContainerProperty(STT, Integer.class, null);
		container.addContainerProperty(LOAIVANBAN, Label.class, null);
		container.addContainerProperty(NGAYBANHANH, String.class, null);
		container.addContainerProperty(HANGIAIQUYET, String.class, null);
		container.addContainerProperty(SOKYHIEU, String.class, null);
		container.addContainerProperty(NOIDUNG, Label.class, null);
		container.addContainerProperty(GHICHU, Label.class, null);
		container.addContainerProperty(TEPDINHKEM, Button.class, null);
		container.addContainerProperty(HOANTHANH, Component.class, null);

		tblVanBan.setWidth("100%");
		tblVanBan.setPageLength(5);
		tblVanBan.addStyleName("table-vanban");
		
		tblVanBan.setColumnAlignment(STT, Align.CENTER);
		tblVanBan.setColumnAlignment(LOAIVANBAN, Align.CENTER);
		tblVanBan.setColumnAlignment(NGAYBANHANH, Align.CENTER);
		tblVanBan.setColumnAlignment(HOANTHANH, Align.CENTER);
		tblVanBan.setColumnAlignment(HANGIAIQUYET, Align.CENTER);
		tblVanBan.setColumnAlignment(SOKYHIEU, Align.CENTER);

		tblVanBan.setColumnWidth(STT, 45);
		tblVanBan.setColumnWidth(NGAYBANHANH, 100);
		tblVanBan.setColumnWidth(HANGIAIQUYET, 100);
		tblVanBan.setColumnWidth(TEPDINHKEM, 135);
		tblVanBan.setColumnWidth(NOIDUNG, 300);
		tblVanBan.setColumnWidth(GHICHU, 200);


		control.getItemsPerPageLabel().setValue("Hiển thị");
		control.getBtnFirst().setCaption("Trang đầu");
		control.getBtnLast().setCaption("Trang cuối");
		control.getBtnNext().setCaption("Trang kế");
		control.getBtnPrevious().setCaption("Trang trước");
		control.getPageLabel().setValue("Hiện tại: ");
	}

	@SuppressWarnings("unchecked")
	private void loadData() throws Exception
	{
		container.removeAllItems();
		List<VanBanXuLyGiaiQuyetBean> list = svQuaTrinh.getVanBanXuLyGiaiQuyetOfDonThu(idDonThu, (int)cmbNguoiNhap.getValue());
		int i = 0;
		for(VanBanXuLyGiaiQuyetBean model : list)
		{
			String loaiVanBan = "";
			//String nguoiNhap = UserLocalServiceUtil.getUser(model.getUserNhap()).getFirstName();

			Item item = container.addItem(model.getIdVanBan());
			
			for(LoaiVanBanXuLyGiaiQuyetEnum e : LoaiVanBanXuLyGiaiQuyetEnum.values())
			{
				if(e.getType()==model.getLoaiVanBan())
					loaiVanBan = e.getName();
			}

			if(model.getTenFileDinhKem()!=null)
			{
				Button btnDownload = new Button();
				
				btnDownload = new Button("",FontAwesome.DOWNLOAD);
				btnDownload.setCaption(model.getTenFileDinhKem());
				btnDownload.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
				Resource resource = new StreamResource(new StreamSource() {

					@Override
					public InputStream getStream() {
						String directory = UploadServiceUtil.getAbsolutePath()+File.separator+UploadServiceUtil.getFolderNameDonThu()+File.separator+idDonThu+File.separator+model.getLinkFileDinhKem();
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
				
				btnDownload.setDescription(model.getTenFileDinhKem());
				
				item.getItemProperty(TEPDINHKEM).setValue(btnDownload);
			}
			
			if(model.getNgayHoanThanh()==null && model.getHanGiaiQuyet()!=null)
			{
				Button btnHoanThanh = new Button("Hoàn thành",FontAwesome.CHECK);
				btnHoanThanh.addStyleName(ValoTheme.BUTTON_FRIENDLY);
				
				item.getItemProperty(HOANTHANH).setValue(btnHoanThanh);
				
				btnHoanThanh.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						try {
							svQuaTrinh.updateHoanThanhVanBan(model.getIdVanBan(), new Date());
						} catch (SQLException e) {
							e.printStackTrace();
						}
						Notification.show("Cập nhật ngày hoàn thành giải quyết văn bản thành công",Type.TRAY_NOTIFICATION);
						item.getItemProperty(HOANTHANH).setValue(new Label(sdf.format(new Date()),ContentMode.HTML));
					}
				});
			}
			else if(model.getNgayHoanThanh()!=null)
			{
				item.getItemProperty(HOANTHANH).setValue(new Label("<p style='text-align:center'>"+sdf.format(model.getNgayHoanThanh())+"</p>",ContentMode.HTML));
			}
			
			item.getItemProperty(STT).setValue(++i);
			item.getItemProperty(LOAIVANBAN).setValue(new Label("<p style='text-align:center'>"+loaiVanBan+"</p>",ContentMode.HTML));
			item.getItemProperty(NGAYBANHANH).setValue(sdf.format(model.getNgayBanHanh()));
			item.getItemProperty(HANGIAIQUYET).setValue(model.getHanGiaiQuyet()!=null?sdf.format(model.getHanGiaiQuyet()):"");
			item.getItemProperty(SOKYHIEU).setValue(model.getSoVanBan());
			item.getItemProperty(NOIDUNG).setValue(new Label(model.getNoiDungVanBan(),ContentMode.HTML));
			item.getItemProperty(GHICHU).setValue(new Label(model.getGhiChuVanBan(),ContentMode.HTML));
		}
		tblVanBan.setContainerDataSource(container);
	}
}
