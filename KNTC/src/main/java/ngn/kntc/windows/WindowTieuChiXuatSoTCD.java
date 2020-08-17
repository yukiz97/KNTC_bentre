package ngn.kntc.windows;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ngn.kntc.beans.SoTiepCongDanBean;
import ngn.kntc.modules.LayoutButtonSubmit;
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

public class WindowTieuChiXuatSoTCD extends Window{
	VerticalLayout vMainLayout = new VerticalLayout();
	
	HorizontalLayout hTieuChi = new HorizontalLayout();
	DateField dfStartDate = new DateField();
	DateField dfEndDate = new DateField();
	HorizontalLayout hCanBo = new HorizontalLayout();
	ComboBox cmbCanBo = new ComboBox();
	LayoutButtonSubmit layoutSubmit = new LayoutButtonSubmit("Xuất file","Hủy",FontAwesome.FILE_EXCEL_O,FontAwesome.REMOVE);
	
	TiepCongDanServiceUtil svTCD = new TiepCongDanServiceUtil();
	
	boolean validateSuccess = false;
	
	public WindowTieuChiXuatSoTCD() {
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
	    
	    List<User> listUser = UserLocalServiceUtil.getOrganizationUsers(SessionUtil.getOrgId());
	    long all = 0;
	    cmbCanBo.addItem(all);
	    cmbCanBo.setItemCaption(all, "Tất cả");
	    for(User user : listUser)
	    {
	    	cmbCanBo.addItem(user.getUserId());
	    	cmbCanBo.setItemCaption(user.getUserId(), user.getFirstName());
	    }
	    cmbCanBo.setNullSelectionAllowed(false);
	    cmbCanBo.select(all);
	}

	private void buildLayout() {
		vMainLayout.addComponents(hTieuChi,hCanBo,layoutSubmit);
		
		hTieuChi.addComponents(new Label("<b>Ngày bắt đầu:</b>",ContentMode.HTML),dfStartDate,new Label("<b>Ngày kết thúc:</b>",ContentMode.HTML),dfEndDate);
		hTieuChi.setSpacing(true);
		
		Label lblCanBoXuat = new Label(FontAwesome.USER.getHtml()+" <b>Cán bộ xuất</b>",ContentMode.HTML);
		hCanBo.addComponents(lblCanBoXuat,cmbCanBo);
		
		lblCanBoXuat.setWidthUndefined();
		cmbCanBo.setWidth("100%");
		
		hCanBo.setExpandRatio(cmbCanBo, 1.0f);
		hCanBo.setWidth("100%");
		hCanBo.setSpacing(true);
		
		vMainLayout.setComponentAlignment(layoutSubmit, Alignment.MIDDLE_RIGHT);
		
		vMainLayout.setSpacing(true);
		vMainLayout.setMargin(true);
		
		this.setCaption("Lựa chọn tiêu chí xuất sổ tiếp công dân");
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
						new SoTCDExcel(dfStartDate.getValue(), dfEndDate.getValue(), (long)cmbCanBo.getValue());
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
