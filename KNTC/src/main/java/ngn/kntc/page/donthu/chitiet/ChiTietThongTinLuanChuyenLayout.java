package ngn.kntc.page.donthu.chitiet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ngn.kntc.beans.LuanChuyenBean;
import ngn.kntc.enums.UserRole;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.utils.LiferayServiceUtil;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;

import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.liferay.portal.model.Organization;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ChiTietThongTinLuanChuyenLayout extends VerticalLayout{
	Label lblMainCaption = new Label("THÔNG TIN LUÂN CHUYỂN",ContentMode.HTML);
	ComboBox cmbGroupBy = new ComboBox();
	VerticalLayout vDisplay = new VerticalLayout();
	
	private QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh = new QuaTrinhXuLyGiaiQuyetServiceUtil();
	private int idDonThu;
	
	public ChiTietThongTinLuanChuyenLayout(int idDonThu) {
		try {
			this.idDonThu = idDonThu;
			buildLayout();
			configComponent();
			buildDisplayLuanChuyen(2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void buildLayout() throws Exception {
		this.addComponent(lblMainCaption);
		this.addComponent(buildComboBoxLayout());
		this.addComponent(vDisplay);

		cmbGroupBy.setWidth("300px");
		vDisplay.setWidth("100%");
		vDisplay.setSpacing(true);
		
		cmbGroupBy.addStyleName(ValoTheme.COMBOBOX_SMALL);

		lblMainCaption.addStyleName("lbl-donthu-maincaption-thongtinluanchuyen");

		this.setSpacing(true);
		this.setMargin(true);
		this.setWidth("100%");
	}

	private void configComponent() {
		cmbGroupBy.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				try {
					buildDisplayLuanChuyen((int)cmbGroupBy.getValue());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private HorizontalLayout buildComboBoxLayout()
	{
		HorizontalLayout hTemp = DonThuModule.buildFormLayoutSingle("Hiển thị thông tin luân chuyển theo", cmbGroupBy, null);
		hTemp.setWidth("100%");
		cmbGroupBy.setWidth("100%");

		cmbGroupBy.addItem(1);
		cmbGroupBy.setItemCaption(1, "Theo dòng thời gian");
		cmbGroupBy.addItem(2);
		cmbGroupBy.setItemCaption(2, "Theo loại luân chuyển");

		cmbGroupBy.select(2);

		cmbGroupBy.setNullSelectionAllowed(false);

		return hTemp;
	}
	private void buildDisplayLuanChuyen(int type) throws Exception
	{
		/*
		 * type = 1: theo dong thời gian
		 * type = 2: theo loại luân chuyển
		 * */
		vDisplay.removeAllComponents();
		
		if(type==1)
		{
			Label lblCaption = new Label("Thông tin luân chuyển theo thời gian",ContentMode.HTML);
			lblCaption.addStyleName("lbl-donthu-thongtinluanchuyen-subcaption");
			vDisplay.addComponent(lblCaption);
			vDisplay.addComponent(buildLayoutDisplayLuanChuyen(svQuaTrinh.getLuanChuyenList(idDonThu, 0)));
		}
		else
		{
			List<LuanChuyenBean> listPhanCong = svQuaTrinh.getLuanChuyenList(idDonThu, 1);
			List<LuanChuyenBean> listLanhDao= new ArrayList<LuanChuyenBean>();
			List<LuanChuyenBean> listChuyen = svQuaTrinh.getLuanChuyenList(idDonThu, 2);
			
			for(LuanChuyenBean model : listPhanCong)
			{
				if((LiferayServiceUtil.isUserHasRole(model.getIdUserNhan(), UserRole.LANHDAO.getName()) || LiferayServiceUtil.isUserHasRole(model.getIdUserNhan(), UserRole.TRUONGPHONG.getName())) && LiferayServiceUtil.getMasterOrgByUser(model.getIdUserChuyen()) == LiferayServiceUtil.getMasterOrgByUser(model.getIdUserNhan()))
				{
					listLanhDao.add(model);
				}
			}
			listPhanCong.removeAll(listLanhDao);
			Label lblCaptionLanhDao = new Label("Thông tin luân chuyển - trình lãnh đạo",ContentMode.HTML);
			lblCaptionLanhDao.addStyleName("lbl-donthu-thongtinluanchuyen-subcaption");
			vDisplay.addComponent(lblCaptionLanhDao);
			if(!listLanhDao.isEmpty())
			{
				vDisplay.addComponent(buildLayoutDisplayLuanChuyen(listLanhDao));
			}
			Label lblCaptionPhanCong = new Label("Thông tin luân chuyển - phân công",ContentMode.HTML);
			vDisplay.addComponent(lblCaptionPhanCong);
			vDisplay.addComponent(buildLayoutDisplayLuanChuyen(listPhanCong));
			lblCaptionPhanCong.addStyleName("lbl-donthu-thongtinluanchuyen-subcaption");
			
			Label lblCaptionChuyen = new Label("Thông tin luân chuyển - chuyển đơn thư",ContentMode.HTML);
			vDisplay.addComponent(lblCaptionChuyen);
			vDisplay.addComponent(buildLayoutDisplayLuanChuyen(listChuyen));
			lblCaptionChuyen.addStyleName("lbl-donthu-thongtinluanchuyen-subcaption");
			
		}
	}

	private ResponsiveLayout buildLayoutDisplayLuanChuyen(List<LuanChuyenBean> listLuanChuyen) throws Exception
	{
		ResponsiveLayout rsl = new ResponsiveLayout();
		List<Long> listOrgChuyenDaHienThi = new ArrayList<Long>();
		for(LuanChuyenBean model : listLuanChuyen)
		{
			String userChuyen = UserLocalServiceUtil.getUser(model.getIdUserChuyen()).getFirstName();
			String userNhan = "";
			String luanChuyenType = "";
			String ngayChuyen = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(model.getNgayChuyen());
			String rowStyle="";
			if(model.isPhanCong())
			{
				if(model.getIdUserNhan()==model.getIdUserChuyen())
					continue;
				userNhan = UserLocalServiceUtil.getUser(model.getIdUserNhan()).getFirstName();
				if((LiferayServiceUtil.isUserHasRole(model.getIdUserNhan(), UserRole.LANHDAO.getName()) || LiferayServiceUtil.isUserHasRole(model.getIdUserNhan(), UserRole.TRUONGPHONG.getName())) && LiferayServiceUtil.getMasterOrgByUser(model.getIdUserChuyen()) == LiferayServiceUtil.getMasterOrgByUser(model.getIdUserNhan()))
				{
					luanChuyenType = "Đã trình đơn cho";
					rowStyle = "row-donthu-thongtinluanchuyen-display-trinhlanhdao";
				}
				else
				{
					luanChuyenType = "Đã phân công cho";
					rowStyle = "row-donthu-thongtinluanchuyen-display-phancong";
				}
			}
			else
			{
				Organization orgDaChuyen= OrganizationLocalServiceUtil.getUserOrganizations(model.getIdUserNhan()).get(0);
				if(listOrgChuyenDaHienThi.contains(Long.valueOf(orgDaChuyen.getOrganizationId())))
					continue;
				luanChuyenType = "Đã chuyển cho";
				userNhan = orgDaChuyen.getName();
				listOrgChuyenDaHienThi.add(orgDaChuyen.getOrganizationId());
				rowStyle = "row-donthu-thongtinluanchuyen-display-chuyen";		
			}

			Label first = new Label("<span style='color: #0047b3'>"+userChuyen+"</span>",ContentMode.HTML);
			Label second = new Label("<span style='color: #f19206'>"+luanChuyenType+"</span>",ContentMode.HTML);
			Label third = new Label("<span style='color: #0047b3'>"+userNhan+"</span>",ContentMode.HTML);
			Label four = new Label("<span style='color: #717780'>"+ngayChuyen+"</span>",ContentMode.HTML);

			first.addStyleName("lbl-donthu-thongtinluanchuyen-detail");
			second.addStyleName("lbl-donthu-thongtinluanchuyen-detail");
			third.addStyleName("lbl-donthu-thongtinluanchuyen-detail");
			four.addStyleName("lbl-donthu-thongtinluanchuyen-detail");
			ResponsiveRow row = new ResponsiveRow();
			row.addColumn().withComponent(first).withDisplayRules(12, 12, 5, 4);
			row.addColumn().withComponent(second).withDisplayRules(12, 12, 2, 2);
			row.addColumn().withComponent(third).withDisplayRules(12, 12, 5, 4);
			row.addColumn().withComponent(four).withDisplayRules(12, 12, 5, 2);
			row.addStyleName("row-donthu-thongtinluanchuyen-display");
			row.addStyleName(rowStyle);

			rsl.addRow(row);
		}

		rsl.addStyleName("animated flipInX");
		rsl.addStyleName("rsl-donthu-thongtinluanchuyen-block");
		rsl.setWidth("100%");

		return rsl;
	}
}
