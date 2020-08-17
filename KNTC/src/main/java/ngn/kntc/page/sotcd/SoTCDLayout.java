package ngn.kntc.page.sotcd;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ngn.kntc.UI.kntcUI;
import ngn.kntc.beans.DanhMucBean;
import ngn.kntc.beans.DoiTuongDiKNTCBean;
import ngn.kntc.beans.DonThuBean;
import ngn.kntc.beans.KetQuaXuLyBean;
import ngn.kntc.beans.SoTiepCongDanBean;
import ngn.kntc.beans.ThongTinDonThuBean;
import ngn.kntc.beans.TraCuuTiepCongDanBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.enums.LoaiNguoiDiKNTCEnum;
import ngn.kntc.enums.LoaiQuanLy;
import ngn.kntc.modules.ControlPagination;
import ngn.kntc.modules.ControlPaginationCustom;
import ngn.kntc.page.donthu.chitiet.ChiTietDonThuTCDGeneralLayout;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;
import ngn.kntc.utils.SessionUtil;
import ngn.kntc.utils.TiepCongDanServiceUtil;
import ngn.kntc.windows.WindowTieuChiXuatSoTCD;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
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

public class SoTCDLayout extends VerticalLayout{
	private Label lblMainCaption = new Label(FontAwesome.BOOK.getHtml()+" Quản lý sổ tiếp công dân",ContentMode.HTML);

	VerticalLayout vFilter = new VerticalLayout();
	private HorizontalLayout hSearchLayout = new HorizontalLayout();
	private TextField txtSearch = new TextField();
	private Button btnSearch = new Button("Tìm kiếm",FontAwesome.SEARCH);
	private Button btnAdvanceSearch = new Button("Nâng cao",FontAwesome.SEARCH_PLUS);
	private Button btnExcel = new Button("Xuất Excel",FontAwesome.FILE_EXCEL_O);
	
	private Panel pnlAvanceSearch = new Panel();
	private DateField dfStartDate = new DateField();
	private DateField dfEndDate = new DateField();
	private ComboBox cmbHuongXuLy = new ComboBox();

	private ControlPaginationCustom controlPagination = new ControlPaginationCustom();

	private VerticalLayout vItems = new VerticalLayout();

	private List<SoTiepCongDanBean> listSoTCD = new ArrayList<SoTiepCongDanBean>();

	private TiepCongDanServiceUtil svTCD = new TiepCongDanServiceUtil();
	private DonThuServiceUtil svDonThu = new DonThuServiceUtil();
	private QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh = new QuaTrinhXuLyGiaiQuyetServiceUtil();

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat sdfSQL = new SimpleDateFormat("yyyy-MM-dd");

	public SoTCDLayout() {
		try {
			loadData();

			buildLayout();
			configComponent();

			displayItemFromList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void buildLayout() throws SQLException {
		this.addComponent(lblMainCaption);
		this.addComponent(vFilter);
		this.addComponent(controlPagination);
		this.addComponent(vItems);

		lblMainCaption.addStyleName("lbl-caption-main");

		vItems.setSpacing(true);
		vItems.setWidth("100%");

		this.setMargin(new MarginInfo(false,true,true,true));
		this.setSpacing(true);

		buildFilterLayout();
	}

	private void configComponent() {
		controlPagination.getBtnHidden().addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					displayItemFromList();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

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
					loadData();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		btnAdvanceSearch.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(pnlAvanceSearch.isVisible())
				{
					pnlAvanceSearch.setVisible(false);
				}
				else
				{
					pnlAvanceSearch.setVisible(true);
				}
			}
		});
		
		btnExcel.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				WindowTieuChiXuatSoTCD wdTieuChiXuatSoTCD = new WindowTieuChiXuatSoTCD();
				UI.getCurrent().addWindow(wdTieuChiXuatSoTCD);
			}
		});
	}

	private void buildFilterLayout() throws SQLException
	{
		//search layout
		hSearchLayout.addComponents(txtSearch,btnSearch,btnAdvanceSearch,btnExcel);

		txtSearch.setWidth("100%");

		hSearchLayout.setExpandRatio(txtSearch, 1.0f);

		hSearchLayout.setWidth("100%");
		hSearchLayout.setSpacing(true);

		txtSearch.addStyleName(ValoTheme.TEXTFIELD_SMALL);

		btnExcel.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnAdvanceSearch.addStyleName(ValoTheme.BUTTON_PRIMARY);

		btnSearch.addStyleName(ValoTheme.BUTTON_SMALL);
		btnAdvanceSearch.addStyleName(ValoTheme.BUTTON_SMALL);
		btnExcel.addStyleName(ValoTheme.BUTTON_SMALL);
		
		//advance search layout
		HorizontalLayout hSearchAd = new HorizontalLayout();
		hSearchAd.addComponents(new Label("<b>Ngày tiếp</b>",ContentMode.HTML),dfStartDate,new Label("-"),dfEndDate,new Label("<b>Hướng xử lý</b>",ContentMode.HTML),cmbHuongXuLy);
		hSearchAd.setSpacing(true);
		
		dfStartDate.addStyleName(ValoTheme.TEXTAREA_SMALL);
		dfEndDate.addStyleName(ValoTheme.TEXTAREA_SMALL);
		
		cmbHuongXuLy.addStyleName(ValoTheme.COMBOBOX_SMALL);
		
		hSearchAd.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		
		hSearchAd.setMargin(true);

		DanhMucServiceUtil svDanhMuc = new DanhMucServiceUtil();
		List<DanhMucBean> listHuongXuLy = svDanhMuc.getDanhMucList(DanhMucTypeEnum.huongxuly.getName(),DanhMucTypeEnum.huongxuly.getIdType());

		for(DanhMucBean model : listHuongXuLy)
		{
			cmbHuongXuLy.addItem(model.getIntMa());
			cmbHuongXuLy.setItemCaption(model.getIntMa(), model.getName());
		}
		pnlAvanceSearch.setContent(hSearchAd);
		pnlAvanceSearch.setVisible(false);
		
		vFilter.addComponent(hSearchLayout);
		vFilter.addComponent(pnlAvanceSearch);

		vFilter.setSpacing(true);
		vFilter.setWidth("100%");
	}

	private void loadData() throws Exception
	{
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
		
		controlPagination.refreshData(listSoTCD.size());
	}

	private void displayItemFromList() throws Exception
	{
		vItems.removeAllComponents();

		int startIndex = controlPagination.getStartIndex();
		int endIndex = controlPagination.getItemPerPage()+startIndex;

		for (int i = startIndex; i < endIndex; i++) {
			if(i<listSoTCD.size())
			{
				SoTiepCongDanBean modelTCD = listSoTCD.get(i);

				DonThuBean modelDonThu = null;

				VerticalLayout vItem= new VerticalLayout();

				if(modelTCD.getMaDonThu()!=0)
				{
					modelDonThu = svDonThu.getDonThu(modelTCD.getMaDonThu());
				}

				/*Row thông tin TCD*/
				String tiepCoDonValue = "";
				if(modelTCD.getMaDonThu()!=0)
				{
					tiepCoDonValue = "<span style='color:green'>Có đơn</span>";
				}
				else
				{
					tiepCoDonValue = "<span style='color:red'>Không đơn</span>";
				}
				String strNgayTiep = "<span class='donthulist-item-info'>"+FontAwesome.CALENDAR.getHtml()+" <b>Ngày tiếp:</b> "+sdf.format(modelTCD.getNgayTiepCongDan())+"</span>";
				String strNguoiTiep = "<span class='donthulist-item-info'>"+FontAwesome.USER.getHtml()+" <b>Người tiếp:</b> "+UserLocalServiceUtil.getUser(modelTCD.getUserTCD()).getFirstName()+"</span>";
				String strTiepCoDon = "<span class='donthulist-item-info'>"+FontAwesome.FILE_TEXT.getHtml()+" <b>Tiếp có đơn:</b> "+tiepCoDonValue+"</span>";

				Label lblThongTinTCD = new Label(strNgayTiep+strNguoiTiep+strTiepCoDon,ContentMode.HTML);

				vItem.addComponent(lblThongTinTCD);
				/* Row chủ thể */
				int loaiChuThe = modelTCD.getLoaiNguoiDiKNTC();
				String loaiChuTheValue = "";
				String nguoiDaiDienValue = "";

				for(LoaiNguoiDiKNTCEnum e : LoaiNguoiDiKNTCEnum.values())
				{
					if(e.getType() == loaiChuThe)
					{
						loaiChuTheValue = e.getName();
					}
				}

				switch (loaiChuThe) {
				case 1:
				case 2:
					if(loaiChuThe==1)
						nguoiDaiDienValue=FontAwesome.USER.getHtml()+" <b>Họ tên</b>: ";
					else
						nguoiDaiDienValue=FontAwesome.USERS.getHtml()+" <b>Người đại diện</b>: ";

					List<DoiTuongDiKNTCBean> listNguoiDiKNTC = svTCD.getNguoiDaiDienTiepCongDan(modelTCD.getMaSoTiepCongDan());

					for(DoiTuongDiKNTCBean modelNguoiDiKNTC : listNguoiDiKNTC)
					{
						nguoiDaiDienValue+=modelNguoiDiKNTC.getHoTen()+", ";
					}
					nguoiDaiDienValue = nguoiDaiDienValue.trim();
					nguoiDaiDienValue = nguoiDaiDienValue.substring(0,nguoiDaiDienValue.length()-1);
					break;
				case 3:
					nguoiDaiDienValue=FontAwesome.HOME.getHtml()+" <b>Tên cơ quan / tổ chức:</b> ";
					nguoiDaiDienValue+=modelTCD.getTenCoQuanDiKNTC();
					break;
				}

				String strLoaiChuThe = "<span class='donthulist-item-info'>"+FontAwesome.USER.getHtml()+" <b>Loại đối tượng:</b> "+loaiChuTheValue+"</span>";
				String strNguoiDaiDien = "<span class='donthulist-item-info'>"+nguoiDaiDienValue+"</span>";

				Label lblChuTheTCD = new Label(strLoaiChuThe+strNguoiDaiDien,ContentMode.HTML);

				vItem.addComponent(lblChuTheTCD);
				/*Row nội dung tiếp*/

				if(modelTCD.getMaDonThu()!=0)
				{
					String strNoiDungDonThu = "<span class='donthulist-item-info'>"+FontAwesome.FILE_TEXT.getHtml()+" <b>Nội dung đơn thư:</b> "+modelDonThu.getNoiDungDonThu()+"</span>";

					Label lblNoiDungDonThu = new Label(strNoiDungDonThu,ContentMode.HTML);

					vItem.addComponent(lblNoiDungDonThu);
				}
				else
				{
					String strNoiDungTiep = "<span class='donthulist-item-info'>"+FontAwesome.FILE_TEXT.getHtml()+" <b>Nội dung tiếp:</b> "+modelTCD.getNoiDungTiepCongDan()+"</span>";
					String strKetQuaTiep = "<span class='donthulist-item-info'>"+FontAwesome.FILE_TEXT.getHtml()+" <b>Kết quả tiếp:</b> "+modelTCD.getKetQuaTiepCongDan()+"</span>";

					Label lblNoiDungTiep = new Label(strNoiDungTiep,ContentMode.HTML);
					Label lblKetQuaTiep = new Label(strKetQuaTiep,ContentMode.HTML);

					vItem.addComponent(lblNoiDungTiep);
					vItem.addComponent(lblKetQuaTiep);
				}

				/* Hướng xử lý */
				String huongXuLyValue = "";
				String soVanBanDiValue = "";
				KetQuaXuLyBean modelKQXL = svQuaTrinh.getKetQuaXuLy(modelTCD.getMaDonThu(), SessionUtil.getOrgId());
				if(modelKQXL!=null)
				{
					huongXuLyValue = DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.huongxuly.getName(), modelKQXL.getMaHuongXuLy()).getName();
					ThongTinDonThuBean modelTTDT = svDonThu.getThongTinDonThuChuyenDi(modelTCD.getMaDonThu(), SessionUtil.getOrgId());
					if(modelTTDT!=null)
					{
						if(modelTTDT.getSoVanBanDen()!=null)
							soVanBanDiValue=modelTTDT.getSoVanBanDen();
					}
				}

				String strHuongXuLy = "<span class='donthulist-item-info'>"+FontAwesome.FILE_TEXT.getHtml()+" <b>Hướng xử lý:</b> "+huongXuLyValue+"</span>";
				String strSoVanBanDi = "<span class='donthulist-item-info'>"+FontAwesome.FILE_TEXT.getHtml()+" <b>Số văn bản đi:</b> "+soVanBanDiValue+"</span>";

				Label lblHuongXuLy = new Label(strHuongXuLy+strSoVanBanDi,ContentMode.HTML);

				vItem.addComponent(lblHuongXuLy);

				Label lblIcon = new Label(FontAwesome.PAPERCLIP.getHtml(),ContentMode.HTML);
				lblIcon.addStyleName("donthulist-item-iconhead");

				vItem.addComponent(lblIcon);

				vItem.setWidth("100%");

				vItem.addStyleName("donthulist-item-block");

				vItems.addComponent(vItem);

				vItem.addLayoutClickListener(new LayoutClickListener() {

					@Override
					public void layoutClick(LayoutClickEvent event) {
						Window wdChiTiet = new Window();

						UI.getCurrent().addWindow(wdChiTiet);
						try {
							wdChiTiet.setContent(new ChiTietDonThuTCDGeneralLayout(modelTCD.getMaSoTiepCongDan(), modelTCD.getMaDonThu()));
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
								} catch (Exception e1) {
									e1.printStackTrace();
								}
							}
						});
					}
				});
			}
			else
			{
				break;
			}
		}
	}
}
