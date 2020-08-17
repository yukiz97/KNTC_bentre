package ngn.kntc.windows.searchdonthu;

import java.util.ArrayList;
import java.util.List;

import ngn.kntc.modules.ControlPagination;

import com.jensjansson.pagedtable.PagedTable;
import com.vaadin.data.util.IndexedContainer;
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
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class WindowSearchDoiTuong extends Window{
	VerticalLayout vMain = new VerticalLayout();

	HorizontalLayout hSearchLayout = new HorizontalLayout();
	TextField txtSearch = new TextField();
	Button btnSearch = new Button("Tìm kiếm",FontAwesome.SEARCH);

	PagedTable tblDanhSach = new PagedTable();
	ControlPagination control = new ControlPagination(tblDanhSach);
	IndexedContainer container = new IndexedContainer();

	List<Button> listButton = new ArrayList<Button>();

	//control
	HorizontalLayout hButton = new HorizontalLayout();
	Button btnOk = new Button("Xác nhận",FontAwesome.SAVE);
	Button btnCancel = new Button("Hủy",FontAwesome.CLOSE);

	public WindowSearchDoiTuong() {
		vMain.addComponent(hSearchLayout);
		vMain.addComponent(control);
		vMain.addComponent(tblDanhSach);
		vMain.addComponent(hButton);
		
		vMain.setComponentAlignment(hButton, Alignment.BOTTOM_RIGHT);

		vMain.setSpacing(true);
		vMain.setMargin(true);

		this.setContent(vMain);
		this.setWidth("100%");
		this.center();
		this.setModal(true);
		this.setCaptionAsHtml(true);

		buildSearchLayout();
		buildTable(); 
		buildControl();

		configComponent();
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
		
		btnCancel.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
	}

	public void buildTable() {
		tblDanhSach.setWidth("100%");
		tblDanhSach.setPageLength(8);

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
		txtSearch.setInputPrompt("Nhập số định danh hoặc họ tên đối tượng khiếu tố để tìm kiếm....");

		hSearchLayout.addComponents(txtSearch,btnSearch);
		
		hSearchLayout.setExpandRatio(txtSearch, 1.0f);
		hSearchLayout.setSpacing(true);
		hSearchLayout.setWidth("100%");
	}

	private void buildControl()
	{
		hButton.addComponent(btnOk);
		hButton.addComponent(btnCancel);
		
		btnOk.setEnabled(false);

		btnOk.addStyleName(ValoTheme.BUTTON_PRIMARY);
		btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);

		hButton.setSpacing(true);
	}

	public TextField getTxtSearch() {
		return txtSearch;
	}
	public void setTxtSearch(TextField txtSearch) {
		this.txtSearch = txtSearch;
	}
	public Button getBtnOk() {
		return btnOk;
	}
	public void setBtnOk(Button btnOk) {
		this.btnOk = btnOk;
	}
}
