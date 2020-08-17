package ngn.kntc.modules;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

public class ControlPaginationCustom extends HorizontalLayout{
	private ComboBox cmbItemPerPage = new ComboBox();

	private HorizontalLayout hChangePage = new HorizontalLayout();
	private Button btnFirst = new Button("Trang đầu");
	private Button btnPrevious = new Button("Trang trước");
	private TextField txtPage = new TextField();
	private Label lblTotalPage = new Label("",ContentMode.HTML);
	private Button btnNext = new Button("Trang tiếp");
	private Button btnLast = new Button("Trang cuối");
	private Button btnHidden = new Button();
	
	private int currentPage = 1;
	private float totalItem = -1;
	private int totalPage = -1;
	private int startIndex = 0;
	private int itemPerPage = 5;

	public ControlPaginationCustom() {
		buildLayout();
		configComponent();
		loadTotalPage();
	}

	private void buildLayout() {
		this.addComponents(cmbItemPerPage,hChangePage);
		
		cmbItemPerPage.addStyleName(ValoTheme.COMBOBOX_SMALL);
		
		cmbItemPerPage.addItem(5);
		cmbItemPerPage.addItem(10);
		cmbItemPerPage.addItem(15);
		cmbItemPerPage.addItem(20);
		
		cmbItemPerPage.setValue(5);
		cmbItemPerPage.setNullSelectionAllowed(false);
		
		cmbItemPerPage.setWidth("80px");

		this.setComponentAlignment(hChangePage, Alignment.MIDDLE_RIGHT);
		
		this.setExpandRatio(hChangePage, 1.0f);

		this.setWidth("100%");
		this.setSpacing(true);

		buildLayoutChangePage();
	}

	private void configComponent() {
		btnFirst.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if(currentPage!=1)
				{
					currentPage = 1;
					changePageEvent();
					
					btnHidden.click();
				}
				
			}
		});

		btnPrevious.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if(currentPage>1)
				{
					currentPage--;
					changePageEvent();
					
					btnHidden.click();
				}
			}
		});

		btnNext.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if(currentPage<totalPage)
				{
					currentPage++;
					changePageEvent();
					
					btnHidden.click();
				}
			}
		});

		btnLast.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if(currentPage!=totalPage)
				{
					currentPage = totalPage;
					changePageEvent();
					
					btnHidden.click();
				}
			}
		});
		
		cmbItemPerPage.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				itemPerPage = (int)cmbItemPerPage.getValue();
				
				loadTotalPage();
				currentPage=1;
				changePageEvent();
				
				btnHidden.click();
			}
		});
	}

	private void buildLayoutChangePage()
	{
		Label lblTmp = new Label("/");
		hChangePage.addComponents(btnFirst,btnPrevious,txtPage,lblTmp,lblTotalPage,btnNext,btnLast);

		txtPage.setWidth("50px");
		txtPage.setValue(String.valueOf(currentPage));
		
		btnFirst.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		btnPrevious.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		btnNext.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		btnLast.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);

		btnFirst.addStyleName(ValoTheme.BUTTON_SMALL);
		btnPrevious.addStyleName(ValoTheme.BUTTON_SMALL);
		btnNext.addStyleName(ValoTheme.BUTTON_SMALL);
		btnLast.addStyleName(ValoTheme.BUTTON_SMALL);

		txtPage.addStyleName(ValoTheme.TEXTFIELD_SMALL);
		
		hChangePage.setComponentAlignment(lblTmp, Alignment.MIDDLE_LEFT);
		hChangePage.setComponentAlignment(lblTotalPage, Alignment.MIDDLE_LEFT);

		hChangePage.setSpacing(true);
	}
	
	private void changePageEvent()
	{
		startIndex = (currentPage*itemPerPage)-itemPerPage;
		txtPage.setValue(String.valueOf(currentPage));
		
		System.out.println(startIndex+"---"+itemPerPage);
	}
	
	private void loadTotalPage()
	{
		float item = itemPerPage;
		
		int intResult = (int) (totalItem/item);
		if(totalItem%item!=0)
		{
			totalPage=intResult+1;
			lblTotalPage.setValue(String.valueOf(totalPage));
		}
		else
		{
			totalPage=intResult;
			lblTotalPage.setValue(String.valueOf(totalPage));
		}
	}
	
	public void refreshData(int total)
	{
		this.totalItem = total;
		currentPage = 1;
		
		loadTotalPage();
		changePageEvent();
		
		btnHidden.click();
	}
	
	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getItemPerPage() {
		return itemPerPage;
	}

	public void setItemPerPage(int itemPerPage) {
		this.itemPerPage = itemPerPage;
	}

	public Button getBtnHidden() {
		return btnHidden;
	}
	public void setBtnHidden(Button btnHidden) {
		this.btnHidden = btnHidden;
	}
}
