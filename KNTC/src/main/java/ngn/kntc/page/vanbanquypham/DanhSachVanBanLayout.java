package ngn.kntc.page.vanbanquypham;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import ngn.kntc.beans.VanBanQuyPhamBean;
import ngn.kntc.enums.LoaiVanBanEnum;
import ngn.kntc.modules.ControlPagination;
import ngn.kntc.utils.VanBanQuyPhamServiceUtil;
import ngn.kntc.windows.WindowVanBanDetail;

import org.vaadin.dialogs.ConfirmDialog;

import com.jensjansson.pagedtable.PagedTable;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class DanhSachVanBanLayout extends VerticalLayout{
	private Label lblMainCaption = new Label(FontAwesome.BOOK.getHtml()+" Quản lý danh sách văn bản quy phạm pháp luật",ContentMode.HTML);
	private Label lblSubCaption = new Label("+ Quản lý danh sách được người dùng nhập.<br/>+ Lọc, tìm kiếm văn bản theo từ khóa thông tin, loại văn bản, ngày ban hành,... ",ContentMode.HTML);
	
	private HorizontalLayout hSearchLayout = new HorizontalLayout();
	private TextField txtSearch = new TextField();
	private ComboBox cboxLoaivanban = new ComboBox();
	private Button btnSearch = new Button("Tìm kiếm",FontAwesome.SEARCH);

	private PagedTable tblDanhsach = new PagedTable();
	private ControlPagination control = new ControlPagination(tblDanhsach);
	private IndexedContainer container=new IndexedContainer();
	
	private final String STT = "STT";
	private final String TENVANBAN = "Tên văn bản";
	private final String SOHIEU = "Số hiệu";
	private final String TRICHDAN = "Trích dẫn";
	private final String NGAYBANHANH = "Ngày ban hành";
	private final String NGAYNHAP = "Ngày nhập";
	private final String THAOTAC = "Thao tác";
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	private VanBanQuyPhamServiceUtil svVanBan = new VanBanQuyPhamServiceUtil();
	public DanhSachVanBanLayout() throws SQLException
	{
		buildLayout();
		configComponent();
	}

	public void buildLayout() throws SQLException
	{
		this.addComponent(lblMainCaption);
		this.addComponent(lblSubCaption);
		this.addComponent(hSearchLayout);
		this.addComponent(tblDanhsach);
		this.addComponent(control);
		
		lblMainCaption.addStyleName("lbl-caption-main");
		
		this.setSpacing(true);
		this.setMargin(new MarginInfo(false,true,false,true));
		
		buildSearchLayout();
		buildTable();
	}
	public void configComponent()
	{
		btnSearch.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					loadDataDanhSach(txtSearch.getValue(), (int)cboxLoaivanban.getValue());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void buildSearchLayout()
	{
		cboxLoaivanban.addItem(0);
		cboxLoaivanban.setItemCaption(0, "Tất cả");
		cboxLoaivanban.select(0);
		for(LoaiVanBanEnum e : LoaiVanBanEnum.values())
		{
			cboxLoaivanban.addItem(e.getType());
			cboxLoaivanban.setItemCaption(e.getType(), e.getName());
		}
		cboxLoaivanban.setNullSelectionAllowed(false);
		cboxLoaivanban.setTextInputAllowed(false);
		
		txtSearch.setWidth("100%");
		txtSearch.setInputPrompt("Nhập từ khóa của những thông tin trong các văn bản để tìm kiếm....");
		
		hSearchLayout.addComponents(txtSearch,cboxLoaivanban,btnSearch);

		hSearchLayout.setExpandRatio(txtSearch, 1.0f);
		hSearchLayout.setSpacing(true);
		hSearchLayout.setWidth("100%");
	}

	public void buildTable()
	{
		//create table
		container.addContainerProperty(STT, Integer.class, null);
		container.addContainerProperty(TENVANBAN, Label.class, null);
		container.addContainerProperty(SOHIEU, String.class, null);
		container.addContainerProperty(TRICHDAN, Label.class, null);
		container.addContainerProperty(NGAYBANHANH, String.class, null);
		container.addContainerProperty(NGAYNHAP, String.class, null);
		container.addContainerProperty(THAOTAC, HorizontalLayout.class, null);

		tblDanhsach.setSizeFull();
		tblDanhsach.setPageLength(10);
		
		tblDanhsach.setColumnAlignment(STT,Align.CENTER);
		tblDanhsach.setColumnAlignment(NGAYBANHANH, Align.CENTER);
		tblDanhsach.setColumnAlignment(NGAYNHAP, Align.CENTER);
		tblDanhsach.setColumnAlignment(THAOTAC, Align.CENTER);
		tblDanhsach.setImmediate(true);
		tblDanhsach.setColumnWidth(TENVANBAN, 450);
		tblDanhsach.setColumnWidth(TRICHDAN, 450);
		
		tblDanhsach.addStyleName("table-vanban");
		
		try {
			loadDataDanhSach("",0);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		control.getItemsPerPageLabel().setValue("Hiển thị");
		control.getBtnFirst().setCaption("Trang đầu");
		control.getBtnLast().setCaption("Trang cuối");
		control.getBtnNext().setCaption("Trang kế");
		control.getBtnPrevious().setCaption("Trang trước");
		control.getPageLabel().setValue("Hiện tại: ");
	}

	@SuppressWarnings("unchecked")
	public void loadDataDanhSach(String keyWord,int typeVanBan) throws SQLException
	{
		container.removeAllItems();
		List<VanBanQuyPhamBean> listVanBan = svVanBan.getVanBanQuyPhamList(keyWord, typeVanBan);
		int i = 0;
		for(VanBanQuyPhamBean model : listVanBan)
		{
			//int vbId = vb.getId();
			String tenVanBan = model.getTenVanBan();
			String trichDan = model.getTrichDan();
			
			if(tenVanBan.length()>120)
			{
				tenVanBan = tenVanBan.substring(0, 120)+"...";
			}
			if(trichDan.length()>120)
			{
				trichDan = trichDan.substring(0, 120)+"...";
			}
			
			HorizontalLayout hLayoutThaoTac = new HorizontalLayout();
			Button btnChitiet = new Button();
			Button btnXoa = new Button();
			btnChitiet.addStyleName(ValoTheme.BUTTON_PRIMARY);
			btnXoa.addStyleName(ValoTheme.BUTTON_DANGER);
			btnChitiet.addStyleName(ValoTheme.BUTTON_TINY);
			btnXoa.addStyleName(ValoTheme.BUTTON_TINY);
			btnChitiet.setIcon(FontAwesome.EYE);
			btnXoa.setIcon(FontAwesome.TRASH);
			btnChitiet.setDescription("Xem chi tiết và chỉnh sửa văn bản");
			btnXoa.setDescription("Xóa văn bản");
			
			hLayoutThaoTac.addComponents(btnChitiet,btnXoa);
			hLayoutThaoTac.setSpacing(true);
			
			btnChitiet.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					WindowVanBanDetail wdDetail = new WindowVanBanDetail(model.getId());
					UI.getCurrent().addWindow(wdDetail);
				}
			});
			
			btnXoa.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					ConfirmDialog.show(UI.getCurrent(),"Thông báo","Bạn muốn xóa văn bản này","Đồng ý","Hủy",new ConfirmDialog.Listener() {
						
						@Override
						public void onClose(ConfirmDialog dialog) {
							if(dialog.isConfirmed())
							{
								try {
									svVanBan.deleteVanBanQuyPham(model.getId());
									tblDanhsach.removeItem(model.getId());
									Notification.show("Xóa văn bản thành công",Type.TRAY_NOTIFICATION);
								} catch (SQLException e) {
									e.printStackTrace();
								}
							}						
						}
					});
				}
			});
			
			Item item = container.addItem(model.getId());
			item.getItemProperty(STT).setValue(++i);
			item.getItemProperty(TENVANBAN).setValue(new Label(tenVanBan));
			item.getItemProperty(SOHIEU).setValue(model.getSoHieu());
			item.getItemProperty(TRICHDAN).setValue(new Label(trichDan,ContentMode.HTML));
			item.getItemProperty(NGAYBANHANH).setValue(sdf.format(model.getNgayBanHanh()));
			item.getItemProperty(NGAYNHAP).setValue(sdf.format(model.getNgayTao()));
			item.getItemProperty(THAOTAC).setValue(hLayoutThaoTac);
		}
		tblDanhsach.setContainerDataSource(container);
	}
}
