package ngn.kntc.windows.tientrinhxlgq;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ngn.kntc.beans.DanhMucBean;
import ngn.kntc.beans.KetQuaXuLyBean;
import ngn.kntc.beans.QuaTrinhXuLyGiaiQuyetBean;
import ngn.kntc.beans.ThongTinDonThuBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.SessionUtil;
import ngn.kntc.windows.WindowChonCoQuan;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class WindowThemKetQuaXuLy extends WindowTienTrinhXuLy{
	/* Nội dung xử lý */
	private Label lblNoiDungCaption = new Label();
	private RichTextArea txtTomTat = new RichTextArea("Tóm tắt nội dung"+DonThuModule.requiredMark);
	private DateField dfNgayXuLy = new DateField("Ngày xử lý"+DonThuModule.requiredMark,new Date());
	private HorizontalLayout hCanBo = new HorizontalLayout();
	private TextField txtCanBoXuLy = new TextField("Xử lý"+DonThuModule.requiredMark);
	private Button btnCanBoXuLy = new Button(FontAwesome.SEARCH_PLUS);
	private TextField txtCanBoDuyet = new TextField("Duyệt"+DonThuModule.requiredMark);
	private Button btnCanBoDuyet = new Button(FontAwesome.SEARCH_PLUS);
	/* Hướng xử lý */
	private Label lblHuongXuLyCaption = new Label();
	private ComboBox cmbHuongXuLy = new ComboBox("Hướng xử lý"+DonThuModule.requiredMark);
	private HorizontalLayout hCQXL = new HorizontalLayout();
	private RichTextArea txtNoiDungXuLy = new RichTextArea("Nội dung xử lý"+DonThuModule.requiredMark);
	private Label lblCQXL = new Label();
	private Button btnCQXL = new Button("",FontAwesome.SEARCH);
	private HorizontalLayout hCQXLTiep = new HorizontalLayout();
	private Label lblCQXLTiep = new Label();
	private Button btnCQXLTiep = new Button("",FontAwesome.SEARCH);
	private KetQuaXuLyBean modelKQXL = new KetQuaXuLyBean();

	private String idCQXL = null;
	private String idCQXLTiep = null;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	private DanhMucServiceUtil svDanhMuc = new DanhMucServiceUtil();
	private DonThuServiceUtil svDonThu = new DonThuServiceUtil();

	private boolean validateSuccess = false;

	private int idDonThu;

	public WindowThemKetQuaXuLy(int idDonThu) {
		this.idDonThu = idDonThu;
		buildLayout();
		configComponent();
	}

	public WindowThemKetQuaXuLy(KetQuaXuLyBean ketQuaXuLyBeanInput,int idDonThu) {
		this.idDonThu = idDonThu;
		buildLayout();
		configComponent();
		this.modelKQXL = ketQuaXuLyBeanInput;
		if(ketQuaXuLyBeanInput.getLinkFileDinhKem()!=null)
		{
			txtTomTat.setValue(modelKQXL.getTomTatNoiDung());
			dfNgayXuLy.setValue(modelKQXL.getNgayXuLy());
			txtCanBoXuLy.setValue(modelKQXL.getCanBoXuLy());
			txtCanBoDuyet.setValue(modelKQXL.getCanBoDuyet()!=null?modelKQXL.getCanBoDuyet():"");
			try {
				idCQXL = modelKQXL.getMaCQXL();
				lblCQXL.setValue(DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.coquan.getName(),modelKQXL.getMaCQXL()).getName());
				idCQXLTiep = modelKQXL.getMaCQXLTiep();
				lblCQXLTiep.setValue(DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.coquan.getName(),modelKQXL.getMaCQXLTiep()).getName());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			cmbHuongXuLy.setValue(modelKQXL.getMaHuongXuLy());
			txtNoiDungXuLy.setValue(modelKQXL.getNoiDungXuLy());
			attachFile.setFileNameNew(modelKQXL.getLinkFileDinhKem());
			attachFile.setFileNameOld(modelKQXL.getTenFileDinhKem());
			attachFile.showListAttachFiles();
		}
	}

	public void buildLayout() {
		super.buildLayout();
		frmLayout.addComponent(lblNoiDungCaption);
		frmLayout.addComponent(txtTomTat);
		frmLayout.addComponent(dfNgayXuLy);
		frmLayout.addComponent(hCanBo);
		frmLayout.addComponent(lblHuongXuLyCaption);
		frmLayout.addComponent(hCQXL);
		frmLayout.addComponent(hCQXLTiep);
		frmLayout.addComponent(cmbHuongXuLy);
		frmLayout.addComponent(txtNoiDungXuLy);
		frmLayout.addComponent(attachFile);
		
		txtTomTat.setCaptionAsHtml(true);
		dfNgayXuLy.setCaptionAsHtml(true);
		hCanBo.setCaptionAsHtml(true);
		hCQXL.setCaptionAsHtml(true);
		hCQXLTiep.setCaptionAsHtml(true);
		cmbHuongXuLy.setCaptionAsHtml(true);
		txtNoiDungXuLy.setCaptionAsHtml(true);
		txtCanBoDuyet.setCaptionAsHtml(true);
		txtCanBoXuLy.setCaptionAsHtml(true);
		
		attachFile.setNameFolderTMP(File.separator+idDonThu);

		dfNgayXuLy.setDateFormat("dd-MM-yyyy");

		frmLayout.setMargin(new MarginInfo(false,true,true,false));

		frmLayout.setWidth("100%");

		//this.setWidth("750px");
		this.setSizeFull();
		this.setCaption("Thêm kết quả xử lý");

		buildLayoutNoiDungXuLy();
		buildLayoutHuongXuLy();
	}

	public void buildLayoutNoiDungXuLy()
	{
		/* Caption */
		lblNoiDungCaption.setCaption("<b style='font-size: 15px !important;color: #f13838 !important;'>NỘI DUNG XỬ LÝ:</b>");
		lblNoiDungCaption.setCaptionAsHtml(true);
		/* Cán bộ */
		VerticalLayout vTmp1 = new VerticalLayout();
		VerticalLayout vTmp2 = new VerticalLayout();

		vTmp1.addComponent(btnCanBoXuLy);
		vTmp2.addComponent(btnCanBoDuyet);
		vTmp1.setCaption("");
		vTmp2.setCaption("");
/*		vTmp1.setWidth("36px");
		vTmp2.setWidth("36px");
*/
		btnCanBoDuyet.setCaption("");
		txtCanBoXuLy.setWidth("100%");
		txtCanBoDuyet.setWidth("100%");

		hCanBo.addComponents(txtCanBoXuLy,vTmp1,txtCanBoDuyet,vTmp2);
		hCanBo.setExpandRatio(txtCanBoXuLy, 1.0f);
		hCanBo.setExpandRatio(txtCanBoDuyet, 1.0f);

		hCanBo.setCaption("Cán bộ");
		hCanBo.setSpacing(true);
		hCanBo.setWidth("100%");
		/* Nội dung */
		txtTomTat.setWidth("100%");
	}

	private void buildLayoutHuongXuLy()
	{
		/* Caption */
		lblHuongXuLyCaption.setCaption("<b style='font-size: 15px !important;color: #f13838 !important;'>HƯỚNG XỬ LÝ:</b>");
		lblHuongXuLyCaption.setCaptionAsHtml(true);
		/* CQXL */
		hCQXL.addComponents(lblCQXL,btnCQXL);
		hCQXL.setSpacing(true);
		hCQXL.setCaption("Cơ quan xử lý"+DonThuModule.requiredMark);

		hCQXLTiep.addComponents(lblCQXLTiep,btnCQXLTiep);
		hCQXLTiep.setSpacing(true);
		hCQXLTiep.setCaption("Cơ quan xử lý tiếp"+DonThuModule.requiredMark);
		/* Hướng xử lý */
		List<DanhMucBean> listHuongXuLy;
		try {
			listHuongXuLy = svDanhMuc.getDanhMucList(DanhMucTypeEnum.huongxuly.getName(),DanhMucTypeEnum.huongxuly.getIdType());

			for(DanhMucBean model : listHuongXuLy)
			{
				cmbHuongXuLy.addItem(model.getIntMa());
				cmbHuongXuLy.setItemCaption(model.getIntMa(), model.getName());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		cmbHuongXuLy.setNullSelectionAllowed(false);
		cmbHuongXuLy.setTextInputAllowed(false);
		cmbHuongXuLy.setValue(1);
		/* Nội dung */
		txtNoiDungXuLy.setWidth("100%");
	}

	public void configComponent() {
		super.configComponent();
		btnOk.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				validateFormSubmit();
				if(validateSuccess)
				{
					try {
						modelKQXL.setTomTatNoiDung(txtTomTat.getValue());
						modelKQXL.setNgayXuLy(dfNgayXuLy.getValue());
						modelKQXL.setCanBoDuyet(!txtCanBoDuyet.isEmpty()?txtCanBoDuyet.getValue():null);
						modelKQXL.setMaHuongXuLy((int)cmbHuongXuLy.getValue());
						modelKQXL.setCanBoXuLy(txtCanBoXuLy.getValue());
						modelKQXL.setNoiDungXuLy(txtNoiDungXuLy.getValue());
						modelKQXL.setMaCQXL(idCQXL);
						modelKQXL.setMaCQXLTiep(idCQXLTiep);
						modelKQXL.setTenFileDinhKem(attachFile.getFileNameOld());
						modelKQXL.setLinkFileDinhKem(attachFile.getFileNameNew());
						modelKQXL.setNgayTao(new Date());
						modelKQXL.setMaDonThu(idDonThu);
						modelKQXL.setOrgTao(SessionUtil.getMasterOrgId());

						QuaTrinhXuLyGiaiQuyetBean model = new QuaTrinhXuLyGiaiQuyetBean();

						model.setNoiDung(svQuaTrinh.returnStringCanBoNhap(SessionUtil.getUserId(), "đã nhập kết quả xử lý","<span style='color: #a2271e'>Hướng xử lý:</span> "+DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.huongxuly.getName(), modelKQXL.getMaHuongXuLy()).getName()+" - <span style='color: #a2271e'>Ngày xử lý:</span> "+sdf.format(modelKQXL.getNgayXuLy())));

						model.setUserNhap(SessionUtil.getUserId());
						model.setNgayDang(new Date());
						model.setMaDonThu(idDonThu);
						if(attachFile.getFileNameNew()!=null)
						{
							model.setTenFileDinhKem(attachFile.getFileNameOld());
							model.setLinkFileDinhKem(attachFile.getFileNameNew());
						}
						model.setHeThongTao(true);
						
						ThongTinDonThuBean modelThongTinDon = svDonThu.getThongTinDonThu(idDonThu, SessionUtil.getMasterOrgId());
						svDonThu.updateThongTinDonThuDate("NgayHoanThanhXuLy", new Date(), modelThongTinDon.getMaThongTinDon());
						
						int idQuaTrinh = svQuaTrinh.insertQuaTrinhXLGQ(model);
						svQuaTrinh.insertKetQuaXuLy(modelKQXL);
						DonThuModule.insertThongBao(idDonThu, idQuaTrinh);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		btnCQXL.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				WindowChonCoQuan wdCoQuan = new WindowChonCoQuan();
				UI.getCurrent().addWindow(wdCoQuan);
				wdCoQuan.getBtnOk().addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						idCQXL = wdCoQuan.getCoQuanID();
						try {
							lblCQXL.setCaption(DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.coquan.getName(),idCQXL).getName());
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				});

			}
		});

		btnCQXLTiep.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				WindowChonCoQuan wdCoQuan = new WindowChonCoQuan();
				UI.getCurrent().addWindow(wdCoQuan);
				wdCoQuan.getBtnOk().addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						idCQXLTiep = wdCoQuan.getCoQuanID();
						try {
							lblCQXLTiep.setCaption(DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.coquan.getName(),idCQXLTiep).getName());
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				});

			}
		});
	}

	public void validateFormSubmit()
	{
		if(txtTomTat.isEmpty())
		{
			Notification.show("Vui lòng nhập vào tóm tắt nội dung",Type.WARNING_MESSAGE);
			validateSuccess = false;
			return;
		}
		if(txtCanBoXuLy.isEmpty())
		{
			Notification.show("Vui lòng nhập vào cán bộ xử lý",Type.WARNING_MESSAGE);
			txtCanBoXuLy.focus();
			validateSuccess = false;
			return;
		}
		if(txtCanBoDuyet.isEmpty())
		{
			Notification.show("Vui lòng nhập vào cán bộ duyệt",Type.WARNING_MESSAGE);
			txtCanBoDuyet.focus();
			validateSuccess = false;
			return;
		}
		if(dfNgayXuLy.isEmpty())
		{
			Notification.show("Vui lòng nhập vào ngày xử lý",Type.WARNING_MESSAGE);

			dfNgayXuLy.focus();
			validateSuccess = false;
			return;
		}
		if(idCQXL==null)
		{
			Notification.show("Vui lòng chọn cơ quan xử lý",Type.WARNING_MESSAGE);
			btnCQXL.focus();
			validateSuccess = false;
			return;
		}
		if(idCQXLTiep==null)
		{
			Notification.show("Vui lòng chọn cơ quan xử lý tiếp",Type.WARNING_MESSAGE);
			btnCQXLTiep.focus();
			validateSuccess = false;
			return;
		}
		if(txtNoiDungXuLy.isEmpty())
		{
			Notification.show("Vui lòng nhập vào nội dung xử lý",Type.WARNING_MESSAGE);
			validateSuccess = false;
			return;
		}
		if(attachFile.getFileNameNew()==null)
		{
			Notification.show("Vui lòng thêm file đính kèm",Type.WARNING_MESSAGE);
			validateSuccess = false;
			return;
		}
		validateSuccess = true;
	}

	public KetQuaXuLyBean getKetQuaXuLy() {
		return modelKQXL;
	}
	public void setKetQuaXuLy(KetQuaXuLyBean ketQuaXuLy) {
		this.modelKQXL = ketQuaXuLy;
	}
	public boolean isValidateSuccess() {
		return validateSuccess;
	}
	public void setValidateSuccess(boolean validateSuccess) {
		this.validateSuccess = validateSuccess;
	}
}
