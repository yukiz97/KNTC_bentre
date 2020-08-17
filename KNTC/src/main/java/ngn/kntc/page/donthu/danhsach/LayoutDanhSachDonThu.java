package ngn.kntc.page.donthu.danhsach;

import java.text.SimpleDateFormat;

import com.jensjansson.pagedtable.PagedTable;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.themes.ValoTheme;

import ngn.kntc.UI.kntcUI;
import ngn.kntc.enums.LoaiQuanLy;
import ngn.kntc.layout.FormMenu;
import ngn.kntc.modules.ControlPagination;
import ngn.kntc.page.donthu.chitiet.ChiTietDonThuTCDGeneralLayout;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;

public abstract class LayoutDanhSachDonThu extends VerticalLayout {
	public Label lblMainCaption = new Label("",ContentMode.HTML);
	public Label lblSubCaption = new Label("",ContentMode.HTML);
	
	public HorizontalLayout hSearchLayout = new HorizontalLayout();
	public TextField txtSearch = new TextField();
	public Button btnSearch = new Button("Tìm kiếm",FontAwesome.SEARCH);
	public Button btnExport = new Button("Xuất Excel",FontAwesome.FILE_EXCEL_O);
	
	public Panel pnlFilter = new Panel();
	public HorizontalLayout hFilter = new HorizontalLayout();
	
	public PagedTable tblDanhSach = new PagedTable();
	public ControlPagination control = new ControlPagination(tblDanhSach);
	public IndexedContainer container = new IndexedContainer();
	
	public DonThuServiceUtil svDonThu = new DonThuServiceUtil();
	public QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh= new QuaTrinhXuLyGiaiQuyetServiceUtil();
	public DanhMucServiceUtil svDanhMuc= new DanhMucServiceUtil();
	
	public SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat sdfDatetime = new SimpleDateFormat("dd/MM/yyyy");
	
	public LayoutDanhSachDonThu() {
		this.addComponent(lblMainCaption);
		//this.addComponent(lblSubCaption);
		this.addComponent(hSearchLayout);
		this.addComponent(pnlFilter);
		this.addComponent(tblDanhSach);
		this.addComponent(control);
		
		lblMainCaption.addStyleName("lbl-caption-main");
		
		pnlFilter.setContent(hFilter);
		hFilter.setSpacing(true);
		hFilter.setMargin(true);
		
		this.setSpacing(true);
		this.setMargin(new MarginInfo(false,true,true,true));
		this.setWidth("100%");
		
		buildSearchLayout();
		configComponent();
	}
	
	@SuppressWarnings("deprecation")
	public void configComponent() {
		txtSearch.addFocusListener(new FocusListener() {
			
			@Override
			public void focus(FocusEvent event) {
				btnSearch.setClickShortcut(KeyCode.ENTER);
			}
		});
		
		txtSearch.addBlurListener(new BlurListener() {
			
			@Override
			public void blur(BlurEvent event) {
				btnSearch.removeClickShortcut();
			}
		});
		
		btnSearch.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					if(validateFilter())
						loadData();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		tblDanhSach.addListener(new ItemClickEvent.ItemClickListener() {

			@Override
			public void itemClick(ItemClickEvent event) {
				if(event.isDoubleClick())
				{
					int idItem = (int)event.getItemId();
					
					Window wdChiTiet = new Window();

					UI.getCurrent().addWindow(wdChiTiet);
					try {
						wdChiTiet.setContent(new ChiTietDonThuTCDGeneralLayout(idItem));
					} catch (Exception e) {
						e.printStackTrace();
					}
					wdChiTiet.setSizeFull();
					wdChiTiet.setModal(true);
					wdChiTiet.setCaption("Thông tin chi tiết đơn thư");
					wdChiTiet.addCloseListener(new CloseListener() {

						@Override
						public void windowClose(CloseEvent e) {
							try {
								kntcUI.getCurrent().setMenuCountValue((int)kntcUI.getCurrent().getFormMenu().getOgChonLoai().getValue());
								loadData();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
					});
				}
			}
		});
	}

	public void buildTable()
	{
		tblDanhSach.setSizeFull();
		tblDanhSach.setSelectable(true);
		tblDanhSach.setPageLength(5);
		
		tblDanhSach.addStyleName("table-vanban");
		
		control.getItemsPerPageLabel().setValue("Hiển thị");
		control.getBtnFirst().setCaption("Trang đầu");
		control.getBtnLast().setCaption("Trang cuối");
		control.getBtnNext().setCaption("Trang kế");
		control.getBtnPrevious().setCaption("Trang trước");
		control.getPageLabel().setValue("Hiện tại: ");
	}
	
	public void buildSearchLayout()
	{
		txtSearch.setWidth("100%");
		txtSearch.setInputPrompt("Nhập từ khóa của những thông tin đối tượng khiếu tố, nội dung đơn thư để tìm kiếm....");
		
		hSearchLayout.addComponents(txtSearch,btnSearch);
		
		txtSearch.addStyleName(ValoTheme.TEXTFIELD_SMALL);
		
		btnExport.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		btnExport.addStyleName(ValoTheme.BUTTON_SMALL);
		btnSearch.addStyleName(ValoTheme.BUTTON_SMALL);


		hSearchLayout.setExpandRatio(txtSearch, 1.0f);
		hSearchLayout.setSpacing(true);
		hSearchLayout.setWidth("100%");
	}
	
	public abstract void loadDefaultValue();
	public abstract boolean validateFilter();
	public abstract void loadData() throws Exception;
}
