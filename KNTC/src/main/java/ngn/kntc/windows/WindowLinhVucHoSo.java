package ngn.kntc.windows;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ngn.kntc.beans.DanhMucBean;
import ngn.kntc.enums.LoaiDonThuEnum;
import ngn.kntc.modules.RemoveUnicodeCharacter;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.DonThuServiceUtil;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
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
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class WindowLinhVucHoSo extends Window{
	int type;
	VerticalLayout vMain = new VerticalLayout();
	//Tìm kiếm
	HorizontalLayout hSearch = new HorizontalLayout();
	TextField txtSearch = new TextField();
	Button btnSearch = new Button("Tìm kiếm",FontAwesome.SEARCH);
	Button btnRefesh = new Button("Làm mới",FontAwesome.REFRESH);
	//table chọn
	List<String> listLinhVucSelected = new ArrayList<String>();
	//table đã chọn
	TreeTable treeTable = new TreeTable();
	IndexedContainer container = new IndexedContainer();
	final String TENLINHVUC = "Tên lĩnh vực"; 

	Table tblSelected = new Table();
	IndexedContainer containerLinhVucListSelected = new IndexedContainer();
	//control
	HorizontalLayout hButton = new HorizontalLayout();
	Button btnOk = new Button("Xác nhận",FontAwesome.SAVE);
	Button btnCancel = new Button("Hủy",FontAwesome.CLOSE);

	DanhMucServiceUtil svDanhMuc = new DanhMucServiceUtil();

	public WindowLinhVucHoSo(int type,List<String> listInput) {
		this.type = type;
		this.listLinhVucSelected.addAll(listInput);
		
		buildLayout();
		configComponent();

		loadLinhVucSelected();

		String typeCaption = "";
		switch (type) {
		case 1:
			typeCaption = LoaiDonThuEnum.khieunai.getName();
			break;
		case 2:
			typeCaption = LoaiDonThuEnum.tocao.getName();
			break;
		case 3:
			typeCaption = LoaiDonThuEnum.phananhkiennghi.getName();
			break;
		default:
			break;
		}
		this.setCaption("    Lựa chọn lĩnh vực hồ sơ đơn thư "+typeCaption.toLowerCase());
	}

	private void buildLayout() {
		vMain.addComponent(hSearch);
		vMain.addComponent(treeTable);
		vMain.addComponent(tblSelected);
		vMain.addComponent(hButton);

		buildSearch();
		buildTableSelect();
		buildTableSelected();
		buildControl();

		try {
			loadLinhVucSelect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		vMain.setMargin(true);
		vMain.setSpacing(true);

		vMain.setComponentAlignment(hButton, Alignment.MIDDLE_RIGHT);

		this.setContent(vMain);
		this.setIcon(FontAwesome.BOOK);
		this.setWidth("700px");
		this.setHeight("99%");
		this.center();
		this.setModal(true);
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

		txtSearch.addFocusListener(new FocusListener() {

			@Override
			public void focus(FocusEvent event) {
			}
		});

		btnSearch.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				doFilter();
			}
		});

		btnRefesh.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				container.removeAllContainerFilters();
				treeTable.setContainerDataSource(container);
				treeTable.setVisibleColumns(new String[]{TENLINHVUC});
			}
		});

		btnOk.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				close();
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
		hSearch.addComponent(txtSearch);
		hSearch.addComponent(btnSearch);
		hSearch.addComponent(btnRefesh);

		txtSearch.setInputPrompt("Nhập vào tên lĩnh vực cần tìm....");
		txtSearch.setSizeFull();

		hSearch.setExpandRatio(txtSearch, 1.0f);

		hSearch.setSpacing(true);
		hSearch.setSizeFull();
	}

	private void buildTableSelect()
	{
		treeTable.addContainerProperty(TENLINHVUC, CheckBox.class, null);
		treeTable.addContainerProperty("filterView", String.class, null);

		treeTable.setPageLength(10);
		treeTable.setSizeFull();
		treeTable.setImmediate(true);
	}

	private void buildTableSelected()
	{
		containerLinhVucListSelected.addContainerProperty("STT", Integer.class, null);
		containerLinhVucListSelected.addContainerProperty("Tên lĩnh vực được chọn", String.class, null);
		containerLinhVucListSelected.addContainerProperty("Xóa", Button.class, null);

		tblSelected.setPageLength(5);
		tblSelected.setContainerDataSource(containerLinhVucListSelected);
		tblSelected.setSizeFull();

		tblSelected.setColumnAlignment("STT", Align.CENTER);
		tblSelected.setColumnAlignment("Xóa", Align.CENTER);
	}

	private void buildControl()
	{
		hButton.addComponent(btnOk);
		hButton.addComponent(btnCancel);

		btnOk.addStyleName(ValoTheme.BUTTON_PRIMARY);
		btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);

		hButton.setSpacing(true);
	}

	@SuppressWarnings("unchecked")
	private void loadLinhVucSelect() throws Exception
	{
		List<DanhMucBean> list = new ArrayList<DanhMucBean>();
		try {
			switch (this.type) {
			case 1:
				list = svDanhMuc.getLinhVucList(1);
				break;
			case 2:
				list = svDanhMuc.getLinhVucList(2);
				break;
			case 3:
				list = svDanhMuc.getLinhVucList(3);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(DanhMucBean model : list)
		{
			String filter = "";
			CheckBox cb = new CheckBox(model.getName());
			cb.setId(model.getStrMa()+"");
			filter = model.getName()+" "+RemoveUnicodeCharacter.removeAccent(model.getName());
			treeTable.addItem(new Object[]{cb,filter},model.getStrMa().trim());
			treeTable.setChildrenAllowed(model.getStrMa(), DonThuServiceUtil.checkIfLinhVucHasChildren(model.getStrMa()));
			if(model.getStrMa().length() >= 8)
			{
				String parentId = model.getStrMa().substring(0,model.getStrMa().length()-3).trim();
				treeTable.setParent(model.getStrMa().trim(), parentId.trim());
			}

			cb.addValueChangeListener(new ValueChangeListener() {

				@Override
				public void valueChange(ValueChangeEvent event) {
					String idParent = "";
					if(cb.getId().contains("KN.01") || cb.getId().contains("KN.02") || cb.getId().contains("KN.03"))
						disableLinhVuc(cb);
					if(cb.getId().length()>5)
						idParent = (String) treeTable.getParent(cb.getId());
					if(cb.getValue() && cb.isEnabled())
					{
						if(!listLinhVucSelected.contains(String.valueOf(cb.getId())))
						{
							listLinhVucSelected.add(cb.getId());
						}
						if(idParent!="")
							disableParentCheckbox(idParent);
					}
					else if(!cb.getValue() && cb.isEnabled()){
						if(listLinhVucSelected.contains(String.valueOf(cb.getId())))
						{
							listLinhVucSelected.remove(String.valueOf(cb.getId()));
						}
						if(idParent!="")
							enableParentCheckbox(idParent);
					}
					loadLinhVucSelected();
				}
			});
		}
		for(String str : listLinhVucSelected)
		{
			CheckBox cb = (CheckBox) treeTable.getItem(str).getItemProperty(TENLINHVUC).getValue();
			cb.setValue(true);
		}
		container = (IndexedContainer) treeTable.getContainerDataSource();
		treeTable.setVisibleColumns(new String[]{TENLINHVUC});
	}
	
	private void disableLinhVuc(CheckBox cbInput)
	{
		String[] strDisable = {"KN.01","KN.02","KN.03"};
		for(int i = 0;i<3;i++)
		{
			if(!cbInput.getId().contains(strDisable[i]))
			{
				try {
					for(String str : svDanhMuc.getLinhVucSubList(strDisable[i]))
					{
						CheckBox cb = (CheckBox) treeTable.getItem(str).getItemProperty(TENLINHVUC).getValue();
						cb.setEnabled(!cbInput.getValue());
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void disableParentCheckbox(String idParent)
	{
		listLinhVucSelected.remove(String.valueOf(idParent));
		CheckBox cbParent = (CheckBox) treeTable.getItem(idParent).getItemProperty(TENLINHVUC).getValue();
		cbParent.setEnabled(false);
		cbParent.setValue(true);
		if(idParent.length()>5)
		{
			String newParentId = idParent.substring(0,idParent.length()-3);
			disableParentCheckbox(newParentId);
		}
	}

	private void enableParentCheckbox(String idParent)
	{
		Collection<?> childrenItemsId = treeTable.getChildren(idParent);
		Iterator itr = childrenItemsId.iterator();
		while(itr.hasNext())
		{
			CheckBox cbTmp = (CheckBox) treeTable.getItem((String)itr.next()).getItemProperty(TENLINHVUC).getValue();
			if(cbTmp.getValue())
			{
				return;
			}
		}
		CheckBox cbParent = (CheckBox) treeTable.getItem(idParent).getItemProperty(TENLINHVUC).getValue();
		cbParent.setValue(false);
		cbParent.setEnabled(true);
		if(idParent.length()>5)
		{
			String newParentString = idParent.substring(0,idParent.length()-3);
			enableParentCheckbox(newParentString);
		}
	}

	private void loadLinhVucSelected()
	{
		int i = 0;
		containerLinhVucListSelected.removeAllItems();
		for(String n : listLinhVucSelected)
		{
			Button btnDelete = new Button(FontAwesome.TRASH_O);
			btnDelete.addStyleName(ValoTheme.BUTTON_DANGER);
			String name = "";
			try {
				name = returnLinhVucName(n,name);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			Item item = containerLinhVucListSelected.addItem(n);
			item.getItemProperty("STT").setValue(++i);
			item.getItemProperty("Tên lĩnh vực được chọn").setValue(name);
			item.getItemProperty("Xóa").setValue(btnDelete);

			btnDelete.addClickListener(new ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					containerLinhVucListSelected.removeItem(n);
					listLinhVucSelected.remove(String.valueOf(n));
					CheckBox cbTMP = (CheckBox) treeTable.getItem(n).getItemProperty(TENLINHVUC).getValue();
					cbTMP.setValue(false);

					try {
						loadLinhVucSelect();
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
			});
		}
		tblSelected.setContainerDataSource(containerLinhVucListSelected);
		this.center();
	}

	private String returnLinhVucName(String ma,String output) throws SQLException
	{
		if(ma.length()>5)
		{
			output += " / "+svDanhMuc.getLinhVuc(ma).getName();
			ma = ma.substring(0, ma.length()-3);
			if(ma.length()>5)
				return returnLinhVucName(ma, output);
			else
			{
				output += " / "+svDanhMuc.getLinhVuc(ma).getName();
			}
			output = output.substring(2);
		}
		else
		{
			output += svDanhMuc.getLinhVuc(ma).getName();
		}
		return output;
	}

	private void doFilter() {
		container.removeAllContainerFilters();
		if(txtSearch.getValue().equalsIgnoreCase(""))
		{

		}
		else
		{
			Filter filter = new SimpleStringFilter("filterView", txtSearch.getValue(), true, false);
			container.addContainerFilter(filter);
		}
		treeTable.setContainerDataSource(container);
		treeTable.setVisibleColumns(new String[]{TENLINHVUC});
	}

	public Button getBtnOk() {
		return btnOk;
	}

	public void setBtnOk(Button btnOk) {
		this.btnOk = btnOk;
	}

	public List<String> getListLinhVucSelected() {
		return listLinhVucSelected;
	}

	public void setListLinhVucSelected(List<String> listLinhVucSelected) {
		this.listLinhVucSelected = listLinhVucSelected;
	}
}
