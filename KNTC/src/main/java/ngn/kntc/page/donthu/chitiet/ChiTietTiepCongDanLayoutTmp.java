package ngn.kntc.page.donthu.chitiet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import ngn.kntc.beans.DoiTuongBiKNTCBean;
import ngn.kntc.beans.DoiTuongDiKNTCBean;
import ngn.kntc.beans.DoiTuongUyQuyenBean;
import ngn.kntc.beans.DonThuBean;
import ngn.kntc.beans.GeneralStringStringBean;
import ngn.kntc.beans.HoSoDinhKemBean;
import ngn.kntc.beans.SoTiepCongDanBean;
import ngn.kntc.beans.ThongTinDonThuBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.enums.LoaiDonThuEnum;
import ngn.kntc.enums.LoaiNguoiBiKNTCEnum;
import ngn.kntc.enums.LoaiNguoiDiKNTCEnum;
import ngn.kntc.enums.LoaiNguoiUyQuyenEnum;
import ngn.kntc.enums.ThamQuyenGiaiQuyetEnum;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.page.donthu.create.TaoDonThuInput;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.SessionUtil;
import ngn.kntc.utils.TiepCongDanServiceUtil;
import ngn.kntc.utils.UploadServiceUtil;

import com.jarektoro.responsivelayout.ResponsiveRow;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class ChiTietTiepCongDanLayoutTmp extends VerticalLayout{
	HorizontalLayout hChiTiet = new HorizontalLayout();

	Button btnSua = new Button("Sửa thông tin tiếp công dân",FontAwesome.EDIT);

	VerticalLayout vThongTinTiepCongDan = new VerticalLayout();
	VerticalLayout vThongTinDoiTuong = new VerticalLayout();

	DonThuServiceUtil svDonThu = new DonThuServiceUtil();
	TiepCongDanServiceUtil svTCD = new TiepCongDanServiceUtil();

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	String chuaXacDinhValue = "<i style='color: gray'>Chưa xác định</i>";

	int idTCD = -1;
	SoTiepCongDanBean modelTCD;

	public ChiTietTiepCongDanLayoutTmp(int idTCD) {
		try {
			this.idTCD = idTCD;
			modelTCD = svTCD.getSoTiepCongDan(idTCD);

			buildLayout();
			configComponent();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void buildLayout() throws Exception {
		this.addComponent(btnSua);
		this.addComponent(hChiTiet);

		hChiTiet.addComponent(vThongTinTiepCongDan);
		hChiTiet.addComponent(vThongTinDoiTuong);
		
		btnSua.addStyleName(ValoTheme.BUTTON_PRIMARY);
		btnSua.addStyleName(ValoTheme.BUTTON_SMALL);
		
		vThongTinTiepCongDan.setWidth("100%");
		vThongTinTiepCongDan.setSpacing(true);
		
		vThongTinDoiTuong.setWidth("100%");
		vThongTinDoiTuong.setSpacing(true);

		hChiTiet.setSpacing(true);
		hChiTiet.setWidth("100%");

		this.setExpandRatio(hChiTiet, 1.0f);
		
		this.setComponentAlignment(btnSua, Alignment.MIDDLE_RIGHT);

		this.addStyleName("chitiet-info-layout");

		this.setSizeFull();
		this.setSpacing(true);
		this.setMargin(true);

		buildThongTinDonLayout();
		buildThongTinDoiTuongLayout();
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
				UI.getCurrent().addWindow(wdUpdate);
				donThu.getBtnSave().addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						if(donThu.isValidateSuccess())
						{
							try {
								modelTCD = svTCD.getSoTiepCongDan(idTCD);
								buildThongTinDonLayout();
								buildThongTinDoiTuongLayout();
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
			}
		});
	}

	private void buildThongTinDonLayout() throws Exception
	{
		vThongTinTiepCongDan.removeAllComponents();
		
		vThongTinTiepCongDan.addComponent(buildThongTinTiepCongDanBlock());
	}

	private void buildThongTinDoiTuongLayout() throws Exception
	{
		vThongTinDoiTuong.removeAllComponents();
		
		vThongTinDoiTuong.addComponent(ChiTietDonThuTCDGeneralFunction.buildThongTinDoiTuongDiKNTCBlock(false,null,modelTCD));
		if(modelTCD.getIdNguoiBiKNTC()!=0)
			vThongTinDoiTuong.addComponent(ChiTietDonThuTCDGeneralFunction.buildThongTinDoiTuongBiKNTCBlock(svDonThu.getDoiTuongBiKNTC(modelTCD.getIdNguoiBiKNTC()),modelTCD.getLoaiNguoiBiKNTC()));
		if(modelTCD.getIdNguoiUyQuyen()!=0)
			vThongTinDoiTuong.addComponent(ChiTietDonThuTCDGeneralFunction.buildThongTinDoiTuongUyQuyentBlock(svDonThu.getDoiTuongUyQuyen(modelTCD.getIdNguoiUyQuyen()),modelTCD.getLoaiNguoiUyQuyen()));
	}
	
	private VerticalLayout buildThongTinTiepCongDanBlock() throws Exception
	{	
		String ngayTiep = sdf.format(modelTCD.getNgayTiepCongDan());
		String nguoiTiep = UserLocalServiceUtil.getUser(modelTCD.getUserTCD()).getFirstName();
		
		String tiepCoDon = modelTCD.getMaDonThu()!=0?FontAwesome.CHECK.getHtml():FontAwesome.REMOVE.getHtml();
		
		List<Label> listThongTinTCD = new ArrayList<Label>();
		
		if(modelTCD.getMaLanhDaoTiep()!=0)
		{
			String loaiTiep = "Lãnh đạo "+(modelTCD.isTiepDinhKy()?"tiếp định kỳ":"tiếp đột xuất");
			String lanhDaoTiep = UserLocalServiceUtil.getUser(modelTCD.getMaLanhDaoTiep()).getFirstName();
			String lanhDaoUyQuyen = "<i style='color:gray'>Không ủy quyền</i>";
			
			if(modelTCD.isUyQuyenLanhDao())
			{
				lanhDaoUyQuyen=UserLocalServiceUtil.getUser(modelTCD.getMaLanhDaoUyQuyen()).getFirstName();
			}
			
			listThongTinTCD.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Loại", loaiTiep))));
			listThongTinTCD.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Lãnh đạo tiếp", lanhDaoTiep),new GeneralStringStringBean("Lãnh đạo ủy quyền", lanhDaoUyQuyen))));
		}
		else
		{
			String tiepThuongXuyenn = "Tiếp thường xuyên";
			
			listThongTinTCD.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Loại", tiepThuongXuyenn))));
		}
		
		listThongTinTCD.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Ngày tiếp", ngayTiep),new GeneralStringStringBean("Người tiếp", nguoiTiep))));
		listThongTinTCD.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Tiếp có đơn", tiepCoDon))));
	
		if(modelTCD.getMaDonThu()!=0)
		{
			String noiDungDonThu = svDonThu.getStringFieldValueOfDonThu("NoiDungDonThu", modelTCD.getMaDonThu());

			listThongTinTCD.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Nội dung đơn thư", noiDungDonThu))));
		}
		else
		{
			String noiDungTiep = modelTCD.getNoiDungTiepCongDan();
			String ketQuaTiep = modelTCD.getKetQuaTiepCongDan();
			
			listThongTinTCD.add(ChiTietDonThuTCDGeneralFunction.buildLabelThongTin(Arrays.asList(new GeneralStringStringBean("Nội dung tiếp", noiDungTiep),new GeneralStringStringBean("Kết quả tiếp", ketQuaTiep))));
		}
		
		return ChiTietDonThuTCDGeneralFunction.buildBlockThongTin("Thông tin tiếp công dân", listThongTinTCD,FontAwesome.PAPERCLIP.getHtml());
	}
}
