package ngn.kntc.page.donthu.create;

import ngn.kntc.modules.DonThuModule;

import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class TaoDonThuDesign extends TaoDonThuDeclare{
	
	public TaoDonThuDesign() {
		this.addComponent(hPhuongThucTiepNhan);
		this.addComponent(pnlPhuongThuc);
		this.addComponent(vNguoiDiKNTC);
		this.addComponent(vNoiDungDonThu);
		this.addComponent(vCoQuanDaGiaiQuyet);
		this.addComponent(vNguoiBiKNTC);
		this.addComponent(vNguoiUyQuyen);
		this.addComponent(hMainControl);
		
		this.setComponentAlignment(hMainControl, Alignment.MIDDLE_RIGHT);
		
		layoutNguoiDiKNTC.getRowNacDanh().setVisible(false);
		
		this.setMargin(true);
		this.setSpacing(true);
		this.setWidth("100%");
		this.addStyleName("animated fadeIn");
		
		buildLayoutThongtinPhuongThuc();
		buildPhuongThucTiepNhan();
		buildMainControl();
		buildBlockThongTin(vNguoiDiKNTC, lblHeaderNguoiDiKNTC, layoutNguoiDiKNTC);
		buildBlockThongTin(vNoiDungDonThu, lblHeaderNoiDungDonThu, layoutNoiDungDonThu);
		buildBlockThongTin(vCoQuanDaGiaiQuyet, hHeaderCoQuanDaGiaiQuyet, cbHeaderCoQuanDaGiaiQuyet, lblHeaderCoQuanDaGiaiQuyet, layoutCoQuanDaGiaiQuyet);
		buildBlockThongTin(vNguoiBiKNTC, hHeaderNguoiBiKNTC, cbHeaderNguoiBiKNTC, lblHeaderNguoiBiKNTC, layoutNguoiBiKNTC);
		buildBlockThongTin(vNguoiUyQuyen, hHeaderNguoiUyQuyen, cbHeaderNguoiUyQuyen, lblHeaderNguoiUyQuyen, layoutNguoiUyQuyen);
	}
	
	private void buildLayoutThongtinPhuongThuc()
	{
		VerticalLayout vPhuongThuc = new VerticalLayout();
		
		rowTiepCongDan1.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Số thứ tự", txtSoThuTuTCD,"150px")).withDisplayRules(12, 12, 6, 4);
		rowTiepCongDan1.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Tiếp công dân không đơn", cbTiepCongDanKhongDon,"200px")).withDisplayRules(12, 12, 6, 4);
		rowTiepCongDan1.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Ngày tiếp"+DonThuModule.requiredMark, dfNgayTiepCongDan,"80px")).withDisplayRules(12, 12, 6, 4);
		
		rowTiepNhanDon1.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Số thứ tự", txtSoThuTuTiepNhan,"150px")).withDisplayRules(12, 12, 6, 4);
		rowTiepNhanDon1.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Cán bộ tiếp nhận", txtNguoiNhapDon,"150px")).withDisplayRules(12, 12, 6, 4);
		rowTiepNhanDon1.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Cơ quan tiếp nhận", txtTenCoQuanTiepNhan,"150px")).withDisplayRules(12, 12, 6, 4);
	
		rowTiepNhanDon2.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Ngày nhận đơn"+DonThuModule.requiredMark, dfNgayNhanDon,"150px")).withDisplayRules(12, 12, 6, 4);
		rowTiepNhanDon2.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Ngày nhập đơn"+DonThuModule.requiredMark, dfNgayNhapDon,"150px")).withDisplayRules(12, 12, 6, 4);
		rowTiepNhanDon2.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Nguồn đơn đến", cmbNguonDonDen,"150px")).withDisplayRules(12, 12, 6, 4);
		
		rowTiepNhanDon3.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Tên cơ quan chuyển đến"+DonThuModule.requiredMark, txtTenCoQuanChuyenDen,"150px")).withDisplayRules(12, 12, 6, 4);
		rowTiepNhanDon3.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("", btnTimKiemCoQuanChuyenDen,"10px")).withDisplayRules(12, 12, 6, 4);
		
		rowTiepNhanDon4.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Số văn bản"+DonThuModule.requiredMark, txtSoVanBanChuyenDen,"150px")).withDisplayRules(12, 12, 6, 4);
		rowTiepNhanDon4.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Ngày phát hành văn bản"+DonThuModule.requiredMark, dfNgayPhatHanhVanBanChuyenDen,"150px")).withDisplayRules(12, 12, 6, 4);

		try {
			txtSoThuTuTCD.setValue(svTCD.countAllTiepCongDan()+"");
			txtSoThuTuTiepNhan.setValue(svDonThu.countAllDonThu()+"");
		} catch (ReadOnlyException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		txtSoThuTuTCD.setWidth("100%");
		dfNgayTiepCongDan.setWidth("100%");
		txtSoThuTuTiepNhan.setWidth("100%");
		dfNgayNhapDon.setWidth("100%");
		dfNgayNhanDon.setWidth("100%");
		txtNguoiNhapDon.setWidth("100%");
		txtTenCoQuanTiepNhan.setWidth("100%");
		cmbNguonDonDen.setWidth("100%");
		txtTenCoQuanChuyenDen.setWidth("100%");
		txtSoVanBanChuyenDen.setWidth("100%");
		dfNgayPhatHanhVanBanChuyenDen.setWidth("100%");
		
		txtSoThuTuTCD.setEnabled(false);
		txtSoThuTuTiepNhan.setEnabled(false);
		txtNguoiNhapDon.setEnabled(false);
		txtTenCoQuanTiepNhan.setEnabled(false);
		txtTenCoQuanChuyenDen.setEnabled(false);
		
		txtTenCoQuanChuyenDen.setId("-1");
		
		cmbNguonDonDen.setNullSelectionAllowed(false);
		
		vPhuongThuc.addComponent(rowTiepCongDan1);
		vPhuongThuc.addComponent(rowTiepCongDan2);
		vPhuongThuc.addComponent(rowTiepCongDan3);
		vPhuongThuc.addComponent(rowTiepCongDan4);
		vPhuongThuc.addComponent(rowTiepNhanDon1);
		vPhuongThuc.addComponent(rowTiepNhanDon2);
		vPhuongThuc.addComponent(rowTiepNhanDon3);
		vPhuongThuc.addComponent(rowTiepNhanDon4);
		
		rowTiepCongDan3.addStyleName("row-border-top");
		rowTiepNhanDon3.addStyleName("row-border-top");
		
		rowTiepNhanDon1.setVisible(false);
		rowTiepNhanDon2.setVisible(false);
		rowTiepNhanDon3.setVisible(false);
		rowTiepNhanDon4.setVisible(false);
		
		pnlPhuongThuc.setContent(vPhuongThuc);
		pnlPhuongThuc.addStyleName("pnl-layout-thongtin");
		pnlPhuongThuc.setWidth("100%");
		
		buildBlockLanhDaotiep();
	}
	
	public void buildMainControl()
	{
		btnSave.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnReset.addStyleName(ValoTheme.BUTTON_PRIMARY);
		btnClose.addStyleName(ValoTheme.BUTTON_DANGER);
			
		hMainControl.addComponents(btnSave,btnReset,btnClose);
		hMainControl.setSpacing(true);
		hMainControl.setMargin(new MarginInfo(false,false,true,true));
		hMainControl.setSizeUndefined();
	}
	
	private void buildPhuongThucTiepNhan()
	{
		hPhuongThucTiepNhan.addComponents(btnTiepCongDan,btnLanhDaoTiep,btnTiepNhanDonGianTiep);
		btnTiepCongDan.addStyleName("btn-phuongthuc-active");
		btnLanhDaoTiep.addStyleName("btn-phuongthuc-deactive");
		btnTiepNhanDonGianTiep.addStyleName("btn-phuongthuc-deactive");
		
		hPhuongThucTiepNhan.setSpacing(true);
	}
	
	private void buildBlockLanhDaotiep()
	{
		HorizontalLayout hCbTmp = new HorizontalLayout();
		hCbTmp.addComponents(cbTiepDinhKy,cbUyQuyenLanhDao);
		hCbTmp.setSpacing(true);
		
		rowTiepCongDan3.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Lãnh đạo tiếp"+DonThuModule.requiredMark, cmbTenLanhDaoTiep,"150px")).withDisplayRules(12, 12, 6, 6);
		rowTiepCongDan3.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("", hCbTmp,"10px")).withDisplayRules(12, 12, 6, 6);
		
		rowTiepCongDan4.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Lãnh đạo ủy quyền"+DonThuModule.requiredMark, cmbTenLanhDaoUyQuyen,"150px")).withDisplayRules(12, 12, 6, 6);
	
		cmbTenLanhDaoTiep.setWidth("100%");
		cmbTenLanhDaoUyQuyen.setWidth("100%");
		cmbTenLanhDaoTiep.setNullSelectionAllowed(false);
		cmbTenLanhDaoUyQuyen.setNullSelectionAllowed(false);
		cmbTenLanhDaoUyQuyen.setEnabled(false);
		
		rowTiepCongDan3.setVisible(false);
		rowTiepCongDan4.setVisible(false);
	}
	
	private void buildBlockThongTin(VerticalLayout vLayout,Label lblHeader,Component layout)
	{
		vLayout.addComponents(lblHeader,layout);
		lblHeader.addStyleName("lbl-header-border");
		
		vLayout.setWidth("100%");
		vLayout.setSpacing(true);
		vLayout.addStyleName("animated fadeIn");
	}
	
	private void buildBlockThongTin(VerticalLayout vLayout,HorizontalLayout hHeader,CheckBox cbHeader,Label lblHeader,Component layout)
	{
		hHeader.addComponents(lblHeader,cbHeader);
		hHeader.setSpacing(true);
		hHeader.setWidth("100%");
		
		hHeader.setComponentAlignment(cbHeader, Alignment.MIDDLE_RIGHT);
		
		hHeader.addStyleName("horlayout-header-active");
		hHeader.addStyleName("horlayout-header-deactive");
		
		vLayout.addComponents(hHeader,layout);
		lblHeader.addStyleName("lbl-header");
		lblHeader.addStyleName("lbl-header-deactive");
		
		layout.setVisible(false);
		
		vLayout.setWidth("100%");
		vLayout.setSpacing(true);
		vLayout.addStyleName("animated fadeIn");
		
		cbHeader.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(cbHeader.getValue())
				{
					layout.setVisible(true);
					lblHeader.removeStyleName("lbl-header-deactive");
					hHeader.removeStyleName("horlayout-header-deactive");
				}
				else
				{
					layout.setVisible(false);
					lblHeader.addStyleName("lbl-header-deactive");
					hHeader.addStyleName("horlayout-header-deactive");
				}
			}
		});
		
		hHeader.addLayoutClickListener(new LayoutClickListener() {
			
			@Override
			public void layoutClick(LayoutClickEvent event) {
				if(cbHeader.getValue())
					cbHeader.setValue(false);
				else
					cbHeader.setValue(true);
			}
		});
	}
}
