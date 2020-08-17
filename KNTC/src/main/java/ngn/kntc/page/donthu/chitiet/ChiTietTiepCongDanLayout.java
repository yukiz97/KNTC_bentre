package ngn.kntc.page.donthu.chitiet;

import java.text.SimpleDateFormat;
import java.util.List;

import ngn.kntc.beans.DoiTuongDiKNTCBean;
import ngn.kntc.beans.SoTiepCongDanBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.enums.LoaiNguoiDiKNTCEnum;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.page.donthu.create.TaoDonThuInput;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.TiepCongDanServiceUtil;

import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ChiTietTiepCongDanLayout extends ResponsiveLayout{
	int idTCD;
	SoTiepCongDanBean modelTCD;
	Button btnSua = new Button("Sửa thông tin tiếp công dân");
	VerticalLayout vTiepCongDan = new VerticalLayout();
	Label lblCaptionDonTiepDan = new Label("<b style='font-size: 25px; color: #da392d;'>THÔNG TIN TIẾP CÔNG DÂN</b>",ContentMode.HTML);

	VerticalLayout vNguoiDaiDien = new VerticalLayout();
	Label lblCaptionNguoiDaiDien = new Label("<b style='font-size: 25px; color: #da392d;'>THÔNG TIN NGƯỜI ĐẠI DIỆN</b>",ContentMode.HTML);
	
	DonThuServiceUtil svDonThu = new DonThuServiceUtil();
	TiepCongDanServiceUtil svTCD = new TiepCongDanServiceUtil();
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	public ChiTietTiepCongDanLayout(int idTCD) throws Exception {
		this.idTCD = idTCD;
		
		ResponsiveRow row = new ResponsiveRow();
		
		row.addColumn().withComponent(vTiepCongDan).withDisplayRules(12,12,6,5);
		row.addColumn().withComponent(vNguoiDaiDien).withDisplayRules(12,12,6,7);
		
		HorizontalLayout hTmp = new HorizontalLayout(btnSua);
		hTmp.setComponentAlignment(btnSua, Alignment.MIDDLE_RIGHT);
		hTmp.addStyleName("hLayout-donthu-chitiet-margin");
		
		hTmp.setWidth("100%");
		
		btnSua.addStyleName("btn-donthu-thongtinquatrinhxlgq-mainbutton");
		btnSua.addStyleName("btn-donthu-thongtinquatrinhxlgq-mainbutton-tientrinh");
		
		this.addComponent(hTmp);
		this.addRow(row);
		this.setWidth("100%");
		
		buildLayoutTiepCongdan();
		buildLayoutNguoiDaiDien();
		configComponent();
	}
	
	private void configComponent() {
		btnSua.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Window wdUpdate = new Window();
				TaoDonThuInput donThu = new TaoDonThuInput(modelTCD.getMaSoTiepCongDan(),modelTCD.getMaDonThu());
				wdUpdate.setContent(donThu);
				wdUpdate.setSizeFull();
				wdUpdate.setModal(true);
				wdUpdate.setCaption("Sửa thông tin tiếp công dân");
				donThu.getBtnSave().addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						if(donThu.isValidateSuccess())
						{
							try {
								buildLayoutTiepCongdan();
								buildLayoutNguoiDaiDien();
								wdUpdate.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				});
				donThu.getBtnClose().addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						wdUpdate.close();
					}
				});
				UI.getCurrent().addWindow(wdUpdate);
			}
		});
	}

	public void buildLayoutTiepCongdan() throws Exception
	{
		vTiepCongDan.removeAllComponents();
		modelTCD = svTCD.getSoTiepCongDan(idTCD);
		vTiepCongDan.addComponent(lblCaptionDonTiepDan);
		String loaiChuThe = "";
		
		for(LoaiNguoiDiKNTCEnum e : LoaiNguoiDiKNTCEnum.values())
		{
			if(e.getType()== modelTCD.getLoaiNguoiDiKNTC())
				loaiChuThe = e.getName();
		}
		if(modelTCD.getMaLanhDaoTiep()!=0)
		{
			ResponsiveRow rowtmp = new ResponsiveRow();
			rowtmp.addColumn().withComponent(buildLabelDisplay("Tiếp định kỳ: ", modelTCD.isTiepDinhKy()?FontAwesome.CHECK.getHtml():FontAwesome.REMOVE.getHtml(), "left")).withDisplayRules(12, 12, 6, 6);
			rowtmp.addColumn().withComponent(buildLabelDisplay("Ủy quyền: ", modelTCD.isUyQuyenLanhDao()?FontAwesome.CHECK.getHtml():FontAwesome.REMOVE.getHtml(), "right")).withDisplayRules(12, 12, 6, 6);
			vTiepCongDan.addComponent(rowtmp);
			ResponsiveRow row0 = new ResponsiveRow();
			row0.addColumn().withComponent(buildLabelDisplay("Lãnh đạo tiếp: ", UserLocalServiceUtil.getUser(modelTCD.getMaLanhDaoTiep()).getFirstName(), "left")).withDisplayRules(12, 12, 6, 6);
			if(modelTCD.isUyQuyenLanhDao())
				row0.addColumn().withComponent(buildLabelDisplay("Lãnh đạo ủy quyền: ", UserLocalServiceUtil.getUser(modelTCD.getMaLanhDaoUyQuyen()).getFirstName(), "right")).withDisplayRules(12, 12, 6, 6);
			vTiepCongDan.addComponent(row0);
		}
		
		ResponsiveRow row1 = new ResponsiveRow();
		row1.addColumn().withComponent(buildLabelDisplay("Số người khiếu tố: ", modelTCD.getSoNguoiDiKNTC()+"", "left")).withDisplayRules(12, 12, 6, 8);
		row1.addColumn().withComponent(buildLabelDisplay("Số người đại diện: ", modelTCD.getSoNguoiDaiDien()+"", "right")).withDisplayRules(12, 12, 6, 4);
		
		ResponsiveRow row2 = new ResponsiveRow();
		row2.addColumn().withComponent(buildLabelDisplay("Người tiếp: ", UserLocalServiceUtil.getUser(modelTCD.getUserTCD()).getFirstName(), "left")).withDisplayRules(12, 12, 8, 8);
		row2.addColumn().withComponent(buildLabelDisplay("Ngày tiếp: ", sdf.format(modelTCD.getNgayTiepCongDan()), "right")).withDisplayRules(12, 12, 8, 4);
		
		vTiepCongDan.addComponent(buildLabelDisplay("Loại chủ thể: ", loaiChuThe, "left"));
		vTiepCongDan.addComponent(row1);
		vTiepCongDan.addComponent(buildLabelDisplay("Nội dung tiếp: ", modelTCD.getNoiDungTiepCongDan()==null?"Tiếp công dân có đơn":modelTCD.getNoiDungTiepCongDan(), "left"));
		vTiepCongDan.addComponent(buildLabelDisplay("Kết quả tiếp: ", modelTCD.getKetQuaTiepCongDan()==null?"Tiếp công dân có đơn":modelTCD.getKetQuaTiepCongDan(), "left"));
		vTiepCongDan.addComponent(row2);
		
		vTiepCongDan.setMargin(true);
		vTiepCongDan.setWidth("100%");
		vTiepCongDan.addStyleName("vLayout-thongtin-tcd");
	}
	
	public void buildLayoutNguoiDaiDien() throws Exception
	{
		List<DoiTuongDiKNTCBean> list = svTCD.getNguoiDaiDienTiepCongDan(idTCD);
		vNguoiDaiDien.removeAllComponents();
		vNguoiDaiDien.addComponent(lblCaptionNguoiDaiDien);
		if(modelTCD.getLoaiNguoiDiKNTC()==3)
		{
			ResponsiveRow row0 = new ResponsiveRow();
			row0.addColumn().withComponent(buildLabelDisplay("Tên cơ quan: ", modelTCD.getTenCoQuanDiKNTC(), "left")).withDisplayRules(12, 12, 8, 6);
			row0.addColumn().withComponent(buildLabelDisplay("Địa chỉ cơ quan: ", modelTCD.getDiaChiCoQuanDiKNTC(), "left")).withDisplayRules(12, 12, 4, 6);
			row0.addStyleName("row-nguoidaidien-congty");
			vNguoiDaiDien.addComponent(row0);
		}
		for(DoiTuongDiKNTCBean modelTmp : list)
		{
			vNguoiDaiDien.addComponent(buildLayoutNguoiDaiDienSingle(modelTmp));
		}
		
		vNguoiDaiDien.setMargin(true);
		vNguoiDaiDien.setSpacing(true);
		vNguoiDaiDien.setWidth("100%");
		vNguoiDaiDien.addStyleName("vLayout-nguoidaidien-tcd");
	}
	
	public VerticalLayout buildLayoutNguoiDaiDienSingle(DoiTuongDiKNTCBean modelTmp) throws Exception
	{
		VerticalLayout vNguoiDaiDienTmp = new VerticalLayout();
		
		ResponsiveRow row1 = new ResponsiveRow();
		row1.addColumn().withComponent(buildLabelDisplay("Họ tên: ", modelTmp.getHoTen(), "left")).withDisplayRules(12, 12, 8, 8);
		row1.addColumn().withComponent(buildLabelDisplay("Giới tính: ", modelTmp.getGioiTinh()==1?"Nam":"Nữ", "left")).withDisplayRules(12, 12, 4, 4);
		
		ResponsiveRow row2 = new ResponsiveRow();
		row2.addColumn().withComponent(buildLabelDisplay("Số định danh: ", modelTmp.getSoDinhDanhCaNhan(), "left")).withDisplayRules(12, 12, 8, 4);
		row2.addColumn().withComponent(buildLabelDisplay("Nơi cấp: ", modelTmp.getNoiCapSoDinhDanh(), "left")).withDisplayRules(12, 12, 4, 4);
		row2.addColumn().withComponent(buildLabelDisplay("Ngày cấp: ",modelTmp.getNgayCapSoDinhDanh()!=null? sdf.format(modelTmp.getNgayCapSoDinhDanh()):"", "left")).withDisplayRules(12, 12, 4, 4);
		
		ResponsiveRow row3 = new ResponsiveRow();
		row3.addColumn().withComponent(buildLabelDisplay("Quốc tịch: ", DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.quoctich.getName(), modelTmp.getMaQuocTich()).getName(), "left")).withDisplayRules(12, 12, 8, 8);
		row3.addColumn().withComponent(buildLabelDisplay("Dân tộc: ", DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.dantoc.getName(), modelTmp.getMaDanToc()).getName(),"left")).withDisplayRules(12, 12, 4, 4);
		

		vNguoiDaiDienTmp.addComponent(row1);
		vNguoiDaiDienTmp.addComponent(row2);
		vNguoiDaiDienTmp.addComponent(row3);
		vNguoiDaiDienTmp.addComponent(buildLabelDisplay("Địa chỉ chi tiết: ", DonThuModule.returnDiaChiChiTiet(modelTmp.getDiaChiChiTiet(), modelTmp.getMaTinh(), modelTmp.getMaHuyen(), modelTmp.getMaXa()), "left"));
		vNguoiDaiDienTmp.setWidth("100%");
		vNguoiDaiDienTmp.addStyleName("vLayout-nguoidaidien-single-tcd");
		
		return vNguoiDaiDienTmp;
	}
	
	public Label buildLabelDisplay(String caption,String value,String align)
	{
		Label lblReturn = new Label("",ContentMode.HTML);
		caption = "<b style='color: #0f50a2'>"+caption+"</b>";
		if(value==null)
		{
			value="";
		}
		
		lblReturn.setValue("<div style='text-align: "+align+"'>"+caption+value+"</div>");
		
		return lblReturn;
	}
}
