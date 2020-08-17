package ngn.kntc.page.tracuu;

import ngn.kntc.modules.DonThuModule;

import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class TraCuuDesign extends TraCuuDeclare{
	public TraCuuDesign() {
		lblMainCaption.setValue(FontAwesome.BOOK.getHtml()+" Tra cứu đơn thư");
		
		this.addComponent(lblMainCaption);
		this.addComponent(lblSubCaption);
		this.addComponent(hSearchLayout);
		this.addComponent(pnlChuThe);
		this.addComponent(pnlThongTinDon);
		this.addComponent(pnlDate);
		this.addComponent(tblDanhSach);
		this.addComponent(control);
		
		lblMainCaption.addStyleName("lbl-caption-main");
		
		this.setSpacing(true);
		this.setMargin(new MarginInfo(false,true,true,true));
		this.setWidth("100%");
		
		buildSearchLayout();
		lblSubCaption.setValue("+ Tra cứu thông tin và in danh sách đơn thư ");
		
		buildTable();
		buildPanelChuThe();
		buildPanelThongTinDon();
		buildPanelDate();
	}
	
	public void buildPanelChuThe()
	{
		VerticalLayout vTmp = new VerticalLayout();
		ResponsiveLayout rsl = new ResponsiveLayout();
		
		
		vTmp.addComponent(lblCaptionChuThe);
		vTmp.addComponent(layoutNguoiDiKNTC);
		
		rsl.addComponent(vTmp);
		
		rsl.addStyleName("rsl-tracuu");
		
		vTmp.setWidth("100%");
		
		pnlChuThe.setContent(rsl);
		lblCaptionChuThe.addStyleName("lbl-tracuu-subcaption");
		
		layoutNguoiDiKNTC.getRowNacDanh().setVisible(false);
		layoutNguoiDiKNTC.getRowDisplayNguoiDaiDien().setVisible(false);
		layoutNguoiDiKNTC.getRowLoaiNguoi().setVisible(false);
		layoutNguoiDiKNTC.getRowDisplayDiaChi().setVisible(false);
		layoutNguoiDiKNTC.getRowButtonThem().setVisible(false);
		layoutNguoiDiKNTC.getCmbGioiTinh().setNullSelectionAllowed(true);
		layoutNguoiDiKNTC.getCmbGioiTinh().select(null);
		
		layoutNguoiDiKNTC.addStyleName(ValoTheme.PANEL_BORDERLESS);
	}
	
	public void buildPanelThongTinDon()
	{
		VerticalLayout vTmp = new VerticalLayout();
		ResponsiveLayout rsl = new ResponsiveLayout();
		
		ResponsiveRow row1 = new ResponsiveRow();
		row1.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Nguồn đơn", cmbNguonDon, "100px")).withDisplayRules(6, 6, 3, 3);
		ResponsiveRow row2 = new ResponsiveRow();
		row2.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Loại đơn thư", cmbLoaiDon, "100px")).withDisplayRules(6, 6, 3, 3);
		row2.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("", btnLinhVuc, "10px")).withDisplayRules(6, 6, 3, 3);
		
		cmbLoaiDon.setWidth("100%");
		cmbNguonDon.setWidth("100%");
		
		btnLinhVuc.addStyleName("button-chon-linh-vuc");
		btnLinhVuc.setEnabled(false);
		
		lblCaptionThongTinDon.addStyleName("lbl-tracuu-subcaption");
		
		vTmp.addComponent(lblCaptionThongTinDon);
		vTmp.addComponent(row1);
		vTmp.addComponent(row2);
		vTmp.addComponent(rowDisplayLinhVuc);
		
		rsl.addComponent(vTmp);
		
		rsl.addStyleName("rsl-tracuu");
		
		//vTmp.setSpacing(true);
		vTmp.setWidth("100%");
		
		pnlThongTinDon.setContent(rsl);
	}
	
	public void buildPanelDate()
	{
		VerticalLayout vTmp = new VerticalLayout();
		ResponsiveLayout rsl = new ResponsiveLayout();
		
		ResponsiveRow row1 = new ResponsiveRow();
		row1.addColumn().withComponent(buildDateBlock(cbNgayNhap, dfNgayNhapStart, dfNgayNhapEnd)).withDisplayRules(6, 6, 6, 4);
		row1.addColumn().withComponent(buildDateBlock(cbNgayThuLy, dfNgayThuLyStart, dfNgayThuLyEnd)).withDisplayRules(6, 6, 6, 4);
		row1.addColumn().withComponent(buildDateBlock(cbNgayGiaiQuyet, dfNgayGiaiQuyetStart, dfNgayGiaiQuyetEnd)).withDisplayRules(6, 6, 6, 4);

		
		lblCaptionDate.addStyleName("lbl-tracuu-subcaption");
		
		vTmp.addComponent(lblCaptionDate);
		vTmp.addComponent(row1);
		
		rsl.addComponent(vTmp);
		
		rsl.addStyleName("rsl-tracuu");
		
		vTmp.setWidth("100%");
		
		pnlDate.setContent(rsl);
	}
	
	public void buildTable() {
		tblDanhSach.setSizeFull();
		tblDanhSach.setPageLength(10);
		tblDanhSach.setColumnCollapsingAllowed(true);
		tblDanhSach.setSelectable(true);
		
		tblDanhSach.addStyleName("table-vanban");
		
		control.getItemsPerPageLabel().setValue("Hiển thị");
		control.getBtnFirst().setCaption("Trang đầu");
		control.getBtnLast().setCaption("Trang cuối");
		control.getBtnNext().setCaption("Trang kế");
		control.getBtnPrevious().setCaption("Trang trước");
		control.getPageLabel().setValue("Hiện tại: ");
		
		container.addContainerProperty(STT, Integer.class, null);
		container.addContainerProperty(LOAINGUOIDUNGDON, Label.class, null);
		container.addContainerProperty(NGUOIDUNGDON, Label.class, null);
		container.addContainerProperty(NOIDUNG, Label.class, null);
		container.addContainerProperty(LOAIDONTHU, String.class, null);
		container.addContainerProperty(LINHVUC, Label.class, null);
		container.addContainerProperty(NGAYNHAPDON, String.class, null);
		container.addContainerProperty(NGUOINHAP, String.class, null);
		
		tblDanhSach.setContainerDataSource(container);
	}
	
	public void buildSearchLayout()
	{
		txtSearch.setWidth("100%");
		txtSearch.setInputPrompt("Nhập từ khóa của những thông tin đối tượng khiếu tố, nội dung đơn thư để tìm kiếm....");
		
		hSearchLayout.addComponents(txtSearch,btnSearch,btnPdf);
		btnPdf.addStyleName(ValoTheme.BUTTON_PRIMARY);

		hSearchLayout.setExpandRatio(txtSearch, 1.0f);
		hSearchLayout.setSpacing(true);
		hSearchLayout.setWidth("100%");
	}
	
	public VerticalLayout buildDateBlock(CheckBox cb,DateField dfStartDate,DateField dfEndDate)
	{
		VerticalLayout vLayout = new VerticalLayout();
		HorizontalLayout hLayout = new HorizontalLayout();
		
		vLayout.addComponent(cb);
		vLayout.addComponent(hLayout);
		
		cb.setCaptionAsHtml(true);
		
		hLayout.addComponents(dfStartDate,new Label("-",ContentMode.HTML),dfEndDate);
		hLayout.setSpacing(true);
		hLayout.setEnabled(false);
		
		cb.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				hLayout.setEnabled(cb.getValue());
			}
		});
		
		vLayout.setWidth("100%");
		
		return vLayout;
	}
}
