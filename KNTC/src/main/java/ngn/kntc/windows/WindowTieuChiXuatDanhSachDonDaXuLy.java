package ngn.kntc.windows;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ngn.kntc.beans.SoTiepCongDanBean;
import ngn.kntc.modules.LayoutButtonSubmit;
import ngn.kntc.page.donthu.danhsach.xulydon.DanhSachDonTiepNhanExcel;
import ngn.kntc.page.sotcd.SoTCDExcel;
import ngn.kntc.utils.SessionUtil;
import ngn.kntc.utils.TiepCongDanServiceUtil;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;

public class WindowTieuChiXuatDanhSachDonDaXuLy extends Window{
	VerticalLayout vMainLayout = new VerticalLayout();
	
	HorizontalLayout hDateType = new HorizontalLayout();
	ComboBox cmbDateType = new ComboBox();
	HorizontalLayout hTieuChi = new HorizontalLayout();
	DateField dfStartDate = new DateField();
	DateField dfEndDate = new DateField();
	LayoutButtonSubmit layoutSubmit = new LayoutButtonSubmit("Xuất file","Hủy",FontAwesome.FILE_EXCEL_O,FontAwesome.REMOVE);
	
	TiepCongDanServiceUtil svTCD = new TiepCongDanServiceUtil();
	
	boolean validateSuccess = false;
	
	public WindowTieuChiXuatDanhSachDonDaXuLy() {
		try {
			setDefaultValue();
			buildLayout();
			configComponent();
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	private void setDefaultValue() throws SystemException {
		Calendar c = Calendar.getInstance();
	    c.setTime(new Date());
	    c.add(Calendar.DAY_OF_YEAR, -30);
	    
	    dfStartDate.setValue(c.getTime());
	    dfEndDate.setValue(new Date());
	    
	    cmbDateType.addItem("ngayxuly");
	    cmbDateType.setItemCaption("ngayxuly", "Ngày xử lý");
	    cmbDateType.addItem("ngaynhandon");
	    cmbDateType.setItemCaption("ngaynhandon", "Ngày nhận đơn");
	    cmbDateType.setNullSelectionAllowed(false);
	    cmbDateType.select("ngayxuly");
	}

	private void buildLayout() {
		vMainLayout.addComponents(hDateType,hTieuChi,layoutSubmit);
		
		Label lblDateType = new Label("<b>Xuất theo loại:</b>",ContentMode.HTML);
		lblDateType.setWidthUndefined();
		cmbDateType.setWidth("100%");
		hDateType.addComponents(lblDateType,cmbDateType);
		hDateType.setSpacing(true);
		hDateType.setExpandRatio(cmbDateType, 1.0f);
		hDateType.setWidth("100%");
		
		hTieuChi.addComponents(new Label("<b>Ngày bắt đầu:</b>",ContentMode.HTML),dfStartDate,new Label("<b>Ngày kết thúc:</b>",ContentMode.HTML),dfEndDate);
		hTieuChi.setSpacing(true);
		
		Label lblCanBoXuat = new Label(FontAwesome.USER.getHtml()+" <b>Cán bộ nhập kết quả xử lý</b>",ContentMode.HTML);
		
		lblCanBoXuat.setWidthUndefined();
		
		vMainLayout.setComponentAlignment(layoutSubmit, Alignment.MIDDLE_RIGHT);
		
		vMainLayout.setSpacing(true);
		vMainLayout.setMargin(true);
		
		this.setCaption("Lựa chọn tiêu chí xuất sổ đơn thư");
		this.setContent(vMainLayout);
		this.center();
		this.setModal(true);
	}

	private void configComponent() {
		layoutSubmit.getBtnCancel().addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
		
		layoutSubmit.getBtnSave().addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					validate();
					if(validateSuccess)
					{
						new DanhSachDonTiepNhanExcel((String)cmbDateType.getValue(), dfStartDate.getValue(), dfEndDate.getValue());
						close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void validate()
	{
		if(dfStartDate.getValue()!=null && dfEndDate.getValue()==null)
		{
			Notification.show("Vui lòng nhập ngày kết thúc",Type.WARNING_MESSAGE);
			dfStartDate.focus();
			validateSuccess = false;
			return;
		}
		if(dfStartDate.getValue()==null && dfEndDate.getValue()!=null)
		{
			Notification.show("Vui lòng nhập ngày bắt đầu",Type.WARNING_MESSAGE);
			dfEndDate.focus();
			validateSuccess = false;
			return;
		}
		validateSuccess = true;
	}

	public LayoutButtonSubmit getLayoutSubmit() {
		return layoutSubmit;
	}

	public void setLayoutSubmit(LayoutButtonSubmit layoutSubmit) {
		this.layoutSubmit = layoutSubmit;
	}
	public boolean isValidateSuccess() {
		return validateSuccess;
	}
	public void setValidateSuccess(boolean validateSuccess) {
		this.validateSuccess = validateSuccess;
	}
}
