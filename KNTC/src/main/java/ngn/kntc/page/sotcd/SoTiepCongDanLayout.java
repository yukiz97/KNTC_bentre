package ngn.kntc.page.sotcd;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ngn.kntc.UI.kntcUI;
import ngn.kntc.beans.DanhMucBean;
import ngn.kntc.beans.DonThuBean;
import ngn.kntc.beans.KetQuaXuLyBean;
import ngn.kntc.beans.SoTiepCongDanBean;
import ngn.kntc.beans.ThongTinDonThuBean;
import ngn.kntc.beans.TraCuuTiepCongDanBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.enums.LoaiQuanLy;
import ngn.kntc.modules.ControlPagination;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.modules.LayoutButtonSubmit;
import ngn.kntc.page.donthu.chitiet.ChiTietDonThuTCDGeneralLayout;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;
import ngn.kntc.utils.SessionUtil;
import ngn.kntc.utils.TiepCongDanServiceUtil;
import ngn.kntc.windows.WindowTieuChiXuatSoTCD;
import ngn.kntc.windows.WindowViewPDF;

import com.jensjansson.pagedtable.PagedTable;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.themes.ValoTheme;

public class SoTiepCongDanLayout extends VerticalLayout{
	private Label lblMainCaption = new Label(FontAwesome.BOOK.getHtml()+" Quản lý sổ tiếp công dân",ContentMode.HTML);
	private Label lblSubCaption = new Label("+ Quản lý danh sách tiếp công dân được người dùng nhập.<br/>+ Lọc, tìm kiếm và in danh sách tiếp công dân theo từ khóa thông tin, ngày tiếp... ",ContentMode.HTML);

	private HorizontalLayout hSearchLayout = new HorizontalLayout();
	private TextField txtSearch = new TextField();
	private Button btnSearch = new Button("Tìm kiếm",FontAwesome.SEARCH);
	private Button btnPdf = new Button("In sổ",FontAwesome.PRINT);
	private HorizontalLayout hSearchAd = new HorizontalLayout();
	private DateField dfStartDate = new DateField();
	private DateField dfEndDate = new DateField();
	private ComboBox cmbHuongXuLy = new ComboBox();

	private PagedTable tblDanhsach = new PagedTable();
	private ControlPagination control = new ControlPagination(tblDanhsach);
	private IndexedContainer container=new IndexedContainer();

	private final String STT = "STT";
	private final String NGAYTIEP = "Ngày tiếp";
	private final String LOAINGUOIKNTC = "Loại đối tượng / số người";
	private final String NGUOIDAIDIEN = "Tên chủ đơn / địa chỉ";
	private final String NOIDUNGDON = "Nội dung vụ việc";
	private final String KETQUATIEP = "Kết quả tiếp";
	private final String HUONGXULY = "Hướng xử lý / số hiệu văn bản đi";
	private final String NGUOITCD = "Cán bộ tiếp";

	private List<SoTiepCongDanBean> listSoTCD = new ArrayList<SoTiepCongDanBean>();

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat sdfSQL = new SimpleDateFormat("yyyy-MM-dd");

	private TiepCongDanServiceUtil svTCD = new TiepCongDanServiceUtil();
	private DonThuServiceUtil svDonThu = new DonThuServiceUtil();
	private QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh = new QuaTrinhXuLyGiaiQuyetServiceUtil();

	public SoTiepCongDanLayout() throws Exception
	{
		buildLayout();
		configComponent();
	}

	public void buildLayout() throws Exception
	{
		this.addComponent(lblMainCaption);
		//this.addComponent(lblSubCaption);
		this.addComponent(hSearchLayout);
		this.addComponent(hSearchAd);
		this.addComponent(tblDanhsach);
		this.addComponent(control);

		lblMainCaption.addStyleName("lbl-caption-main");

		this.setSpacing(true);
		this.setMargin(new MarginInfo(false,true,false,true));

		buildSearchLayout();
		buildTable();
	}
	@SuppressWarnings("deprecation")
	public void configComponent()
	{
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
					loadDataDanhSach();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		btnPdf.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				WindowTieuChiXuatSoTCD wdTieuChiXuatSoTCD = new WindowTieuChiXuatSoTCD();
				UI.getCurrent().addWindow(wdTieuChiXuatSoTCD);
				
				/*wdTieuChiXuatSoTCD.getLayoutSubmit().getBtnSave().addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						wdTieuChiXuatSoTCD.validate();
						if(wdTieuChiXuatSoTCD.isValidateSuccess())
						{
							
						}
					}
				});
				try {
					SoTCDPDF pdf =new SoTCDPDF();
					pdf.setStrTochuc(OrganizationLocalServiceUtil.getOrganization(SessionUtil.getMasterOrgId()).getName());
					pdf.setStrTieude("SỔ TIẾP CÔNG DÂN");
					pdf.setStrSubTieude("Của cán bộ "+SessionUtil.getUser().getFirstName());
					pdf.setListTCD(listSoTCD);
					pdf.createPdf();
					WindowViewPDF windowViewPDF=new WindowViewPDF();
					windowViewPDF.setFilename("sotiepcongdan"+new Date().getTime()+".pdf"); 
					windowViewPDF.setCaption("SỔ TIẾP CÔNG DÂN");
					windowViewPDF.loadData(pdf.getByteArrayOutputStream());
					UI.getCurrent().addWindow(windowViewPDF);
				} catch (Exception e) {
					e.printStackTrace();
				}*/
			}
		});

		tblDanhsach.addListener(new ItemClickEvent.ItemClickListener() {

			@Override
			public void itemClick(ItemClickEvent event) {
				if(event.isDoubleClick())
				{
					String strMultiId = (String)event.getItemId();
					String[] arrItemId = strMultiId.split(",");
					
					int idTCd = Integer.parseInt(arrItemId[0]);
					int idDonThu = Integer.parseInt(arrItemId[1]);
					
					Window wdChiTiet = new Window();

					UI.getCurrent().addWindow(wdChiTiet);
					try {
						wdChiTiet.setContent(new ChiTietDonThuTCDGeneralLayout(idTCd, idDonThu));
					} catch (Exception e) {
						e.printStackTrace();
					}
					wdChiTiet.setSizeFull();
					wdChiTiet.setCaption("Thông tin chi tiết tiếp công dân và đơn thư");

					wdChiTiet.addCloseListener(new CloseListener() {

						@Override
						public void windowClose(CloseEvent e) {
							try {
								kntcUI.getCurrent().setMenuCountValue(LoaiQuanLy.donvi.getType());
								loadDataDanhSach();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
					});
				}
			}
		});
	}

	public void buildSearchLayout() throws SQLException
	{
		txtSearch.setWidth("100%");
		txtSearch.setInputPrompt("Nhập từ khóa của những thông tin tiếp công dân để tìm kiếm....");

		hSearchLayout.addComponents(txtSearch,btnSearch,btnPdf);

		hSearchAd.addComponents(new Label("<b>Ngày tiếp</b>",ContentMode.HTML),dfStartDate,new Label("-"),dfEndDate,new Label("<b>Hướng xử lý: </b>",ContentMode.HTML),cmbHuongXuLy);
		hSearchAd.setSpacing(true);

		DanhMucServiceUtil svDanhMuc = new DanhMucServiceUtil();
		List<DanhMucBean> listHuongXuLy = svDanhMuc.getDanhMucList(DanhMucTypeEnum.huongxuly.getName(),DanhMucTypeEnum.huongxuly.getIdType());

		for(DanhMucBean model : listHuongXuLy)
		{
			cmbHuongXuLy.addItem(model.getIntMa());
			cmbHuongXuLy.setItemCaption(model.getIntMa(), model.getName());
		}

		btnPdf.addStyleName(ValoTheme.BUTTON_PRIMARY);

		hSearchLayout.setExpandRatio(txtSearch, 1.0f);
		hSearchLayout.setSpacing(true);
		hSearchLayout.setWidth("100%");
	}

	public void buildTable() throws Exception
	{
		//create table
		container.addContainerProperty(STT, Integer.class, null);
		container.addContainerProperty(NGAYTIEP, String.class, null);
		container.addContainerProperty(LOAINGUOIKNTC, Label.class, null);
		container.addContainerProperty(NGUOIDAIDIEN, Label.class, null);
		container.addContainerProperty(NOIDUNGDON, Label.class, null);
		container.addContainerProperty(KETQUATIEP, Label.class, null);
		container.addContainerProperty(HUONGXULY, Label.class, null);
		container.addContainerProperty(NGUOITCD, Label.class, null);

		tblDanhsach.setSizeFull();
		tblDanhsach.setPageLength(10);

		tblDanhsach.setColumnAlignment(STT,Align.CENTER);
		tblDanhsach.setColumnAlignment(NGAYTIEP, Align.CENTER);
		tblDanhsach.setColumnAlignment(NGUOITCD, Align.CENTER);
		tblDanhsach.setImmediate(true);
		//tblDanhsach.setColumnExpandRatio(NOIDUNGDON, 1.0f);
		tblDanhsach.setColumnWidth(STT, 40);
		tblDanhsach.setColumnWidth(NGAYTIEP, 80);
		tblDanhsach.setColumnWidth(LOAINGUOIKNTC, 165);
		tblDanhsach.setColumnWidth(NGUOIDAIDIEN, 200);
		tblDanhsach.setColumnWidth(NOIDUNGDON, 300);
		tblDanhsach.setColumnWidth(KETQUATIEP, 300);
		tblDanhsach.setColumnWidth(NGUOITCD, 95);
		
		tblDanhsach.setSelectable(true);

		tblDanhsach.addStyleName("table-vanban");

		loadDataDanhSach();

		control.getItemsPerPageLabel().setValue("Hiển thị");
		control.getBtnFirst().setCaption("Trang đầu");
		control.getBtnLast().setCaption("Trang cuối");
		control.getBtnNext().setCaption("Trang kế");
		control.getBtnPrevious().setCaption("Trang trước");
		control.getPageLabel().setValue("Hiện tại: ");
	}

	@SuppressWarnings("unchecked")
	public void loadDataDanhSach() throws Exception
	{
		container.removeAllItems();

		TraCuuTiepCongDanBean modelTraCuu = new TraCuuTiepCongDanBean();
		if(!txtSearch.isEmpty())
			modelTraCuu.setKeyWord(txtSearch.getValue().trim());
		if(!dfStartDate.isEmpty() && !dfEndDate.isEmpty())
		{
			modelTraCuu.setStartDate(sdfSQL.format(dfStartDate.getValue()));
			modelTraCuu.setEndDate(sdfSQL.format(dfEndDate.getValue()));
		}
		if(cmbHuongXuLy.getValue()!=null)
			modelTraCuu.setMaHuongXuLy((int)cmbHuongXuLy.getValue());

		listSoTCD = svTCD.getSoTiepCongDanList(modelTraCuu);
		int i = 0;
		for(SoTiepCongDanBean model : listSoTCD)
		{
			DonThuBean modelDonThu = null;
			String noiDungVuViec = "";
			String ketQuaTiep = "";
			String huongXuly = "";

			/* Đơn thư */
			if(model.getMaDonThu()!=0)
			{
				modelDonThu = svDonThu.getDonThu(model.getMaDonThu());
				noiDungVuViec = modelDonThu.getNoiDungDonThu();
				ketQuaTiep = modelDonThu.getNoiDungDonThu();

				KetQuaXuLyBean modelKQXL = svQuaTrinh.getKetQuaXuLy(model.getMaDonThu(), SessionUtil.getOrgId());
				if(modelKQXL!=null)
				{
					huongXuly = DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.huongxuly.getName(), modelKQXL.getMaHuongXuLy()).getName();
					ThongTinDonThuBean modelTTDT = svDonThu.getThongTinDonThuChuyenDi(model.getMaDonThu(), SessionUtil.getOrgId());
					if(modelTTDT!=null)
					{
						if(modelTTDT.getSoVanBanDen()!=null)
							huongXuly+=" - <b>Số văn bản đi</b>: "+modelTTDT.getSoVanBanDen();
					}
				}
			}
			else
			{
				noiDungVuViec = "<b>Tiếp không đơn:</b><br/>"+model.getNoiDungTiepCongDan();
				ketQuaTiep = "<b>Tiếp không đơn:</b><br/>"+model.getKetQuaTiepCongDan();
			}

			Item item = container.addItem(model.getMaSoTiepCongDan()+","+model.getMaDonThu());
			Label lblNguoiDaiDien = new Label(svTCD.returnTenNguoiDaiDienTCD(model),ContentMode.HTML);
			Label lblNoiDungVuViec = new Label(noiDungVuViec,ContentMode.HTML);
			Label lblKetQuaTiep = new Label(ketQuaTiep,ContentMode.HTML);

			lblNguoiDaiDien.addStyleName("lbl-incell-height");
			lblNoiDungVuViec.addStyleName("lbl-incell-height");
			lblKetQuaTiep.addStyleName("lbl-incell-height");

			item.getItemProperty(STT).setValue(++i);
			item.getItemProperty(LOAINGUOIKNTC).setValue(new Label(DonThuModule.returnLoaiNguoiDiKNTCForTable(model.getLoaiNguoiDiKNTC(), model.getSoNguoiDiKNTC(), model.getSoNguoiDaiDien()),ContentMode.HTML));
			item.getItemProperty(NGUOIDAIDIEN).setValue(lblNguoiDaiDien);
			item.getItemProperty(NOIDUNGDON).setValue(lblNoiDungVuViec);
			item.getItemProperty(KETQUATIEP).setValue(lblKetQuaTiep);
			item.getItemProperty(HUONGXULY).setValue(new Label(huongXuly,ContentMode.HTML));
			item.getItemProperty(NGAYTIEP).setValue(sdf.format(model.getNgayTiepCongDan()));
			item.getItemProperty(NGUOITCD).setValue(new Label("<p style='text-align: center'>"+UserLocalServiceUtil.getUser(model.getUserTCD()).getFirstName()+"</p>",ContentMode.HTML));
		}
		tblDanhsach.setContainerDataSource(container);
	}
}
