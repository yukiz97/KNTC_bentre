package ngn.kntc.windows;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ngn.kntc.beans.BieuMauBean;
import ngn.kntc.beans.KetQuaXuLyBean;
import ngn.kntc.enums.BieuMauEnum;
import ngn.kntc.modules.RemoveUnicodeCharacter;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;
import ngn.kntc.utils.SessionUtil;
import ngn.kntc.word.bieumau.WordBieuMauBienNhan;
import ngn.kntc.word.bieumau.WordBieuMauPattern;
import ngn.kntc.word.bieumau.WordBieuMauPhieuChuyenDon;
import ngn.kntc.word.bieumau.WordBieuMauThongBaoChuyenDon;
import ngn.kntc.word.bieumau.WordBieuMauThongBaoKhongTLGQKhieuNai;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class WindowLapBieuMau extends Window{
	private VerticalLayout vMain = new VerticalLayout();
	private HorizontalLayout hSearch = new HorizontalLayout();
	private TextField txtSearch = new TextField();
	private Button btnSearch = new Button("Tìm kiếm",FontAwesome.SEARCH);
	private Button btnClear = new Button("Làm mới",FontAwesome.REFRESH);
	private Table tblBieuMau = new Table();
	private IndexedContainer container = new IndexedContainer();
	private Button btnCancel = new Button("Đóng",FontAwesome.REMOVE);

	private List<BieuMauBean> listBieuMau = new ArrayList<BieuMauBean>();

	private final String STT = "STT";
	private final String TENBIEUMAU = "Tên biểu mẫu";
	private final String CHON = "Chọn";
	private final String FILTER = "Filter";

	private int idDonThu = - 1;
	private QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh = new QuaTrinhXuLyGiaiQuyetServiceUtil();

	public WindowLapBieuMau(int idDonThu) {

		this.idDonThu = idDonThu;

		try {
			buildLayout();
		} catch (Exception e) {
			e.printStackTrace();
		}
		configComponent();
		buildTable();
		buildSearch();
	}

	private void buildLayout() throws Exception {

		vMain.addComponent(hSearch);
		vMain.addComponent(tblBieuMau);
		vMain.addComponent(btnCancel);

		btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);

		vMain.setComponentAlignment(btnCancel, Alignment.MIDDLE_RIGHT);

		vMain.setMargin(true);
		vMain.setSpacing(true);

		this.setContent(vMain);
		this.setWidth("600px");
		this.center();
		this.setModal(true);
		this.setCaptionAsHtml(true);
		this.setCaption(FontAwesome.FILE_WORD_O.getHtml()+" Lập biểu mẫu");

		buildTable();
		loadData();
	}

	private void configComponent() {
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
				doFilter();
			}
		});

		btnClear.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				container.removeAllContainerFilters();
				tblBieuMau.setContainerDataSource(container);
				tblBieuMau.setVisibleColumns(new String[]{STT,TENBIEUMAU,CHON});
			}
		});

		btnCancel.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
	}

	private void buildSearch()
	{
		hSearch.addComponents(txtSearch,btnSearch,btnClear);

		txtSearch.setWidth("100%");
		txtSearch.setInputPrompt("Nhập vào từ khóa tên của biểu mẫu để tìm kiếm......");

		hSearch.setExpandRatio(txtSearch, 1.0f);

		hSearch.setSpacing(true);
		hSearch.setWidth("100%");
	}

	private void buildTable()
	{
		container.addContainerProperty(STT, Integer.class, null);
		container.addContainerProperty(TENBIEUMAU, String.class, null);
		container.addContainerProperty(CHON, Button.class, null);
		container.addContainerProperty(FILTER, String.class, null);

		tblBieuMau.setPageLength(10);
		tblBieuMau.setImmediate(true);
		tblBieuMau.setWidth("100%");
		tblBieuMau.addStyleName("table-vanban");

		tblBieuMau.setColumnAlignment(STT, Align.CENTER);
		tblBieuMau.setColumnAlignment(CHON, Align.CENTER);
	}

	@SuppressWarnings("unchecked")
	private void loadData() throws Exception
	{
		loadBieuMau();
		int i = 0;
		for(BieuMauBean model : listBieuMau)
		{
			Button btnLapPhieu = new Button("",FontAwesome.PRINT);
			btnLapPhieu.setDescription("Lập phiếu "+model.getName());
			btnLapPhieu.addStyleName(ValoTheme.BUTTON_PRIMARY);

			String filter = model.getName()+" "+RemoveUnicodeCharacter.removeAccent(model.getName());

			Item item = container.addItem(++i);
			item.getItemProperty(STT).setValue(i);
			item.getItemProperty(TENBIEUMAU).setValue(model.getName());
			item.getItemProperty(CHON).setValue(btnLapPhieu);
			item.getItemProperty(FILTER).setValue(filter);
			model.getClassWord().setIdDonThu(idDonThu);
			model.getClassWord().setFileNameDownload(model.getNameFile());

			btnLapPhieu.addClickListener(new ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					try {
						model.getClassWord().exportWord();
					} catch (Exception e) {
						e.printStackTrace();
					}					
				}
			});
		}
		tblBieuMau.setContainerDataSource(container);
		tblBieuMau.setVisibleColumns(new String[]{STT,TENBIEUMAU,CHON});
	}

	private void doFilter() {
		container.removeAllContainerFilters();
		if(txtSearch.getValue().equalsIgnoreCase(""))
		{
			container.removeAllContainerFilters();
		}
		else
		{
			Filter filter = new SimpleStringFilter(FILTER, txtSearch.getValue(), true, false);
			container.addContainerFilter(filter);
		}
		tblBieuMau.setContainerDataSource(container);
		tblBieuMau.setVisibleColumns(new String[]{STT,TENBIEUMAU,CHON});
	}

	private void loadBieuMau() throws SQLException
	{
		KetQuaXuLyBean modelKQXL = svQuaTrinh.getKetQuaXuLy(idDonThu, SessionUtil.getOrgId());

		String tail = new Date().getTime()+".docx";
		listBieuMau.add(new BieuMauBean("Phiếu biên nhận", new WordBieuMauBienNhan(), "phieubiennhan"+tail));

		if(modelKQXL != null)
		{
			if(modelKQXL.getMaHuongXuLy()==5)
			{
				listBieuMau.add(new BieuMauBean("Phiếu thông báo chuyển đơn", new WordBieuMauThongBaoChuyenDon(), "phieuthongbaochuyen"+tail));
				listBieuMau.add(new BieuMauBean("Phiếu chuyển đơn", new WordBieuMauPhieuChuyenDon(), "phieuchuyendon"+tail));
			}
		}
	}
}
