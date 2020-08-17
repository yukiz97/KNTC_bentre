package ngn.kntc.page.baocao;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import ngn.kntc.modules.DonThuModule;

public class LapBaoCaoDesign extends LapBaoCaoDeclare{
	public LapBaoCaoDesign() {
		this.addComponent(lblMainCaption);
		this.addComponent(lblSubCaption);
		this.addComponent(pnlMain);
		this.addComponent(pnlDisplay);
		
		pnlMain.setContent(vSubLayout);
		pnlMain.setWidth("100%");
		
		pnlDisplay.setWidth("100%");
		pnlDisplay.setVisible(false);
		
		lblMainCaption.addStyleName("lbl-caption-main");

		this.setWidth("100%");
		this.setSpacing(true);
		this.setMargin(new MarginInfo(false,true,true,true));
		
		buildChonThongTinLapBaoCaoLayout();
	}
	
	private void buildChonThongTinLapBaoCaoLayout()
	{
		hButton.addComponents(btnLapBaoCao,btnXuatExcel);
		hButton.setSpacing(true);
		btnXuatExcel.setEnabled(false);
		btnXuatExcel.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		vSubLayout.addComponent(DonThuModule.buildFormLayoutSingle("Loại báo cáo:", cmbLoaiBaoCao, "130px"));
		vSubLayout.addComponent(DonThuModule.buildFormLayoutSingle("Khung thời gian:", buildKhungThoiGian(), "130px"));
		vSubLayout.addComponent(DonThuModule.buildFormLayoutSingle("Báo cáo theo:", buildChonDoiTuongBaoCao(), "130px"));
		vSubLayout.addComponent(DonThuModule.buildFormLayoutSingle("Đơn vị báo cáo:", lblDonViBaoCao, "130px"));
		vSubLayout.addComponent(DonThuModule.buildFormLayoutSingle("Cá nhân báo cáo:", new Label(""), "130px"));
		vSubLayout.addComponent(DonThuModule.buildFormLayoutSingle("", ogChonLoai, "130px"));
		vSubLayout.addComponent(DonThuModule.buildFormLayoutSingle("", hButton, "130px"));
		
		vSubLayout.getComponent(4).setVisible(false);
		
		cmbLoaiBaoCao.setWidth("100%");
		cmbLoaiBaoCao.setNullSelectionAllowed(false);
		
		vSubLayout.setWidth("100%");
		vSubLayout.setSpacing(true);
		vSubLayout.setMargin(true);
	}
	
	private VerticalLayout buildKhungThoiGian()
	{
		VerticalLayout vChonThoiGian = new VerticalLayout();
		
		hTimeDefault.setSpacing(true);
		
		hTimeOption.addComponents(cmbThang,cmbQuy,cmbNam,dfStartDate,dfEndDate);
		hTimeOption.setSpacing(true);
		
		cmbQuy.setVisible(false);
		cmbNam.setVisible(false);
		
		cmbThang.setNullSelectionAllowed(false);
		cmbQuy.setNullSelectionAllowed(false);
		cmbNam.setNullSelectionAllowed(false);
		
		vChonThoiGian.addComponents(hTimeDefault,hTimeOption);
		vChonThoiGian.setSpacing(true);
		
		return vChonThoiGian;
	}
	
	
	private VerticalLayout buildChonDoiTuongBaoCao()
	{
		VerticalLayout vChonDoiTuong = new VerticalLayout();
		
		vChonDoiTuong.addComponent(ogLoaiDoiTuongLapBaoCao);
		vChonDoiTuong.addComponent(btnChonDonVi);
		vChonDoiTuong.addComponent(btnChonCanBo);
		
		btnChonDonVi.addStyleName(ValoTheme.BUTTON_PRIMARY);
		btnChonCanBo.addStyleName(ValoTheme.BUTTON_PRIMARY);
		
		ogLoaiDoiTuongLapBaoCao.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
		
		btnChonCanBo.setVisible(false);
		
		vChonDoiTuong.setSpacing(true);
		return vChonDoiTuong;
	}
}
