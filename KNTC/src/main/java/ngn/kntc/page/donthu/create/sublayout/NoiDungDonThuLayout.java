package ngn.kntc.page.donthu.create.sublayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ngn.kntc.beans.HoSoDinhKemBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.enums.LoaiDonThuEnum;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.UploadServiceUtil;
import ngn.kntc.windows.WindowDinhKemHoSo;
import ngn.kntc.windows.WindowLinhVucHoSo;

import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class NoiDungDonThuLayout extends Panel{
	private ResponsiveLayout rslMainLayout = new ResponsiveLayout();
	private Button btnKiemTraDonThu = new Button("Kiểm tra đơn thư");
	private CheckBox cbDonKhongDuDKXL = new CheckBox("<b style='color: #f33939'>Đơn không đủ điều kiện xử lý</b>");
	private ComboBox cmbLoaiDonThu = new ComboBox();
	private Button btnChonLinhVuc = new Button("Thêm lĩnh vực của đơn thư",FontAwesome.PLUS_CIRCLE);
	private VerticalLayout vLinhVucDisplay = new VerticalLayout();
	private TextArea txtNoiDungDonThu = new TextArea();
	private Button btnThemDinhKemHoSo = new Button("Thêm tập tin đính kèm cho hồ sơ",FontAwesome.PLUS_CIRCLE);
	private VerticalLayout vDinhKemDisplay = new VerticalLayout();
	
	private ResponsiveRow rowDisplayLinhVuc = buildRowDisplayLinhVuc();
	private ResponsiveRow rowDisplayDinhKem = buildRowDisplayDinhKem();
	private ResponsiveRow rowKiemTra = buildRow0();
	private	ResponsiveRow rowNoiDung = buildRow2();
	private	ResponsiveRow rowBtnDinhKem = buildRow3();

	private List<String> listLinhVuc = new ArrayList<String>();
	private List<HoSoDinhKemBean> listHoSoDinhKem = new ArrayList<HoSoDinhKemBean>();

	private DanhMucServiceUtil svDanhMuc = new DanhMucServiceUtil();
	
	private String folderTmp = "";

	public NoiDungDonThuLayout() {
		buildLayout();
		configComponent();
		try {
			loadDefaultData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void loadDefaultData() throws SQLException {
		for(LoaiDonThuEnum e : LoaiDonThuEnum.values())
		{
			cmbLoaiDonThu.addItem(e.getType());
			cmbLoaiDonThu.setItemCaption(e.getType(), e.getName());
		}
		cmbLoaiDonThu.select(1);
	}

	private void buildLayout() {
		rslMainLayout.addRow(rowKiemTra);
		rslMainLayout.addRow(buildRow1());
		rslMainLayout.addRow(rowDisplayLinhVuc);
		rslMainLayout.addRow(rowNoiDung);
		rslMainLayout.addRow(rowBtnDinhKem);
		rslMainLayout.addRow(rowDisplayDinhKem);

		rslMainLayout.setWidth("100%");

		this.setContent(rslMainLayout);
		this.addStyleName("pnl-layout-thongtin");
	}

	private void configComponent() {
		btnChonLinhVuc.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				WindowLinhVucHoSo wdLinhVuc = new WindowLinhVucHoSo((int)cmbLoaiDonThu.getValue(), listLinhVuc);

				UI.getCurrent().addWindow(wdLinhVuc);

				wdLinhVuc.getBtnOk().addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						listLinhVuc.clear();
						listLinhVuc.addAll(wdLinhVuc.getListLinhVucSelected());
						loadDisplayLinhVuc();
					}
				});
			}
		});

		cmbLoaiDonThu.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				listLinhVuc.clear();
				loadDisplayLinhVuc();
				if((int)cmbLoaiDonThu.getValue()>3)
				{
					btnChonLinhVuc.setVisible(false);
				}
				else
				{
					btnChonLinhVuc.setVisible(true);
				}
			}
		});

		btnThemDinhKemHoSo.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				WindowDinhKemHoSo wdDinhKem = new WindowDinhKemHoSo();
				UI.getCurrent().addWindow(wdDinhKem);
				wdDinhKem.getAttachFile().setNameFolderTMP(File.separator+folderTmp);

				wdDinhKem.getBtnOk().addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						if(wdDinhKem.isValidateSuccess())
						{
							wdDinhKem.close();
							listHoSoDinhKem.add(wdDinhKem.getDinhKemHoSoBean());
							try {
								loadDisplayDinhKem();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					}
				});
			}
		});
	}

	private ResponsiveRow buildRow0()
	{
		HorizontalLayout hTmp = new HorizontalLayout();
		hTmp.addComponents(btnKiemTraDonThu,cbDonKhongDuDKXL);
		hTmp.setSpacing(true);
		
		btnKiemTraDonThu.addStyleName(ValoTheme.BUTTON_PRIMARY);
		
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("", hTmp,"150px")).withDisplayRules(12, 12, 12, 12);
		cbDonKhongDuDKXL.setCaptionAsHtml(true);

		return row;
	}

	private ResponsiveRow buildRow1()
	{
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Lĩnh vực", cmbLoaiDonThu,"150px")).withDisplayRules(12, 12, 6, 3);
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("", btnChonLinhVuc,"10px")).withDisplayRules(12, 12, 6, 4);
		cmbLoaiDonThu.setWidth("100%");
		cmbLoaiDonThu.setNullSelectionAllowed(false);
		btnChonLinhVuc.addStyleName("button-chon-linh-vuc");

		return row;
	}

	private ResponsiveRow buildRowDisplayLinhVuc()
	{
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("", vLinhVucDisplay,"150px")).withDisplayRules(12, 12, 12, 12);
		vLinhVucDisplay.setWidth("100%");
		vLinhVucDisplay.addStyleName("vLayout-linhvuc");
		row.setVisible(false);

		return row;
	}

	private ResponsiveRow buildRow2()
	{
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Nội dung đơn thư"+DonThuModule.requiredMark, txtNoiDungDonThu,"150px")).withDisplayRules(12, 12, 12, 12);
		txtNoiDungDonThu.setWidth("100%");

		return row;
	}

	private ResponsiveRow buildRow3()
	{
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Đính kèm", btnThemDinhKemHoSo,"150px")).withDisplayRules(12, 12, 6, 6);
		btnThemDinhKemHoSo.addStyleName("button-chon-dinh-kem-ho-so");

		return row;
	}
	
	private ResponsiveRow buildRowDisplayDinhKem()
	{
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("", vDinhKemDisplay,"150px")).withDisplayRules(12, 12, 12, 12);
		vDinhKemDisplay.setWidth("100%");
		vDinhKemDisplay.addStyleName("vLayout-dinhkemhoso");
		row.setVisible(false);

		return row;
	}

	public void loadDisplayLinhVuc()
	{
		vLinhVucDisplay.removeAllComponents();
		if(!listLinhVuc.isEmpty())
		{
			rowDisplayLinhVuc.setVisible(true);
			for(String idLinhVuc : listLinhVuc)
			{
				ResponsiveRow rowTmp = new ResponsiveRow();
				HashMap<String, String> lv = DonThuModule.getLinhVucLevel(idLinhVuc);
				try {
					if(lv.containsKey("lv2"))
						rowTmp.addColumn().withComponent(new Label("<b style='color: #19619a'>Loại lĩnh vực: </b>"+svDanhMuc.getLinhVuc(lv.get("lv2")).getName(),ContentMode.HTML)).withDisplayRules(12, 12, 4, 4);
					if(lv.containsKey("lv3"))
						rowTmp.addColumn().withComponent(new Label("<b style='color: #19619a'>Chi tiết lĩnh vực: </b>"+svDanhMuc.getLinhVuc(lv.get("lv3")).getName(),ContentMode.HTML)).withDisplayRules(12, 12, 4, 4);
					if(lv.containsKey("lv4"))
						rowTmp.addColumn().withComponent(new Label("<b style='color: #19619a'>Diễn giải: </b>"+svDanhMuc.getLinhVuc(lv.get("lv4")).getName(),ContentMode.HTML)).withDisplayRules(12, 12, 4, 4);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				vLinhVucDisplay.addComponent(rowTmp);
			}
		}
		else
		{
			rowDisplayLinhVuc.setVisible(false);
		}
	}
	
	public void loadDisplayDinhKem() throws SQLException
	{
		vDinhKemDisplay.removeAllComponents();
		if(!listHoSoDinhKem.isEmpty())
		{
			rowDisplayDinhKem.setVisible(true);
			for(HoSoDinhKemBean model : listHoSoDinhKem)
			{
				Button btnDownload = new Button("<span style='color: #1f69a5'>"+model.getTenFileDinhKem()+"</span>",FontAwesome.DOWNLOAD);
				Button btnDelete = new Button("",FontAwesome.REMOVE);
				ResponsiveRow rowTmp = new ResponsiveRow();
				rowTmp.addColumn().withComponent(new Label("<b style='color: #19619a'>Tên hồ sơ: </b>"+model.getTenHoSo(),ContentMode.HTML)).withDisplayRules(12, 12, 4, 4);
				rowTmp.addColumn().withComponent(new Label("<b style='color: #19619a'>Loại hồ sơ: </b>"+DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.loaitailieu.getName(), model.getLoaiHoSo()).getName(),ContentMode.HTML)).withDisplayRules(12, 12, 4, 3);
				rowTmp.addColumn().withComponent(btnDownload).withDisplayRules(12, 12, 4, 4);
				if(model.getMaDinhKem()==0)
					rowTmp.addColumn().withComponent(btnDelete).withDisplayRules(12, 12, 4, 1);
				
				rowTmp.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
				
				/* Button download */
				btnDownload.setCaptionAsHtml(true);
				btnDownload.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				System.out.println(folderTmp+"-----------------------------5151551551515151515");
				Resource resource = new StreamResource(new StreamSource() {

					@Override
					public InputStream getStream() {
						String directory = UploadServiceUtil.getAbsolutePath()+File.separator+UploadServiceUtil.getFolderNameDonThu()+File.separator+folderTmp+File.separator+model.getLinkFileDinhKem();
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
				
				/* Button delete */
				btnDelete.addStyleName("button-delete-dinhkem");
				btnDelete.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						listHoSoDinhKem.remove(model);
						try {
							loadDisplayDinhKem();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				});
				
				vDinhKemDisplay.addComponent(rowTmp);
			}
		}
		else
		{
			rowDisplayDinhKem.setVisible(false);
		}
	}
	
	public boolean validateForm()
	{
		if(txtNoiDungDonThu.isEmpty())
		{
			Notification.show("Vui lòng nhập vào nội dung đơn thư",Type.WARNING_MESSAGE);
			txtNoiDungDonThu.focus();
			return false;
		}
		return true;
	}
	
	public Button getBtnChonLinhVuc() {
		return btnChonLinhVuc;
	}
	public void setBtnChonLinhVuc(Button btnChonLinhVuc) {
		this.btnChonLinhVuc = btnChonLinhVuc;
	}
	public ComboBox getCmbLoaiDonThu() {
		return cmbLoaiDonThu;
	}
	public void setCmbLoaiDonThu(ComboBox cmbLoaiDonThu) {
		this.cmbLoaiDonThu = cmbLoaiDonThu;
	}
	public CheckBox getCbDonKhongDuDKXL() {
		return cbDonKhongDuDKXL;
	}
	public void setCbDonKhongDuDKXL(CheckBox cbDonKhongDuDKXL) {
		this.cbDonKhongDuDKXL = cbDonKhongDuDKXL;
	}
	public TextArea getTxtNoiDungDonThu() {
		return txtNoiDungDonThu;
	}
	public void setTxtNoiDungDonThu(TextArea txtNoiDungDonThu) {
		this.txtNoiDungDonThu = txtNoiDungDonThu;
	}
	public List<String> getListLinhVuc() {
		return listLinhVuc;
	}
	public void setListLinhVuc(List<String> listLinhVuc) {
		this.listLinhVuc = listLinhVuc;
	}
	public List<HoSoDinhKemBean> getListHoSoDinhKem() {
		return listHoSoDinhKem;
	}

	public void setListHoSoDinhKem(List<HoSoDinhKemBean> listHoSoDinhKem) {
		this.listHoSoDinhKem = listHoSoDinhKem;
	}
	public String getFolderTmp() {
		return folderTmp;
	}
	public void setFolderTmp(String folderTmp) {
		this.folderTmp = folderTmp;
	}
	public Button getBtnKiemTraDonThu() {
		return btnKiemTraDonThu;
	}
	public void setBtnKiemTraDonThu(Button btnKiemTraDonThu) {
		this.btnKiemTraDonThu = btnKiemTraDonThu;
	}
	public ResponsiveRow getRowDisplayLinhVuc() {
		return rowDisplayLinhVuc;
	}
	public void setRowDisplayLinhVuc(ResponsiveRow rowDisplayLinhVuc) {
		this.rowDisplayLinhVuc = rowDisplayLinhVuc;
	}
	public ResponsiveRow getRowDisplayDinhKem() {
		return rowDisplayDinhKem;
	}
	public void setRowDisplayDinhKem(ResponsiveRow rowDisplayDinhKem) {
		this.rowDisplayDinhKem = rowDisplayDinhKem;
	}
	public ResponsiveRow getRowKiemTra() {
		return rowKiemTra;
	}
	public void setRowKiemTra(ResponsiveRow rowKiemTra) {
		this.rowKiemTra = rowKiemTra;
	}
	public ResponsiveRow getRowNoiDung() {
		return rowNoiDung;
	}
	public void setRowNoiDung(ResponsiveRow rowNoiDung) {
		this.rowNoiDung = rowNoiDung;
	}
	public ResponsiveRow getRowBtnDinhKem() {
		return rowBtnDinhKem;
	}
	public void setRowBtnDinhKem(ResponsiveRow rowBtnDinhKem) {
		this.rowBtnDinhKem = rowBtnDinhKem;
	}
}
