package ngn.kntc.windows.tientrinhxlgq;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ngn.kntc.attachfile.FormAttachFile;
import ngn.kntc.beans.DanhMucBean;
import ngn.kntc.beans.KetQuaXuLyBean;
import ngn.kntc.beans.LuanChuyenBean;
import ngn.kntc.beans.QuaTrinhXuLyGiaiQuyetBean;
import ngn.kntc.beans.ThongTinDonThuBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.enums.UserRole;
import ngn.kntc.lienthongttcp.LienThongKetQuaXuLy;
import ngn.kntc.lienthongttcp.LienThongKetQuaXuLy;
import ngn.kntc.lienthongttcp.LienThongVuViec;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.modules.LayoutButtonSubmit;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.LiferayServiceUtil;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;
import ngn.kntc.utils.SessionUtil;
import ngn.kntc.utils.UploadServiceUtil;
import ngn.kntc.windows.WindowChonCoQuan;
import ngn.kntc.windows.WindowChuyenDonThu;

import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;

public class WindowThemKetQuaXuLyTmp extends Window{
	VerticalLayout vMain = new VerticalLayout();

	ResponsiveLayout rslMain = new ResponsiveLayout();

	ComboBox cmbCanBoXuLy = new ComboBox();
	ComboBox cmbCanBoDuyet = new ComboBox();
	ComboBox cmbHuongXuLy = new ComboBox();
	DateField dfNgayXuLy = new DateField();
	TextField txtCoQuanXuLy = new TextField();
	HorizontalLayout hMainCoQuanXuLyTiep = new HorizontalLayout();
	HorizontalLayout hCoQuanXuLyTiep = new HorizontalLayout();
	TextField txtCoQuanXuLyTiep = new TextField();
	Button btnCoQuanXuLyTiep = new Button("",FontAwesome.SEARCH_PLUS);

	HorizontalLayout hCoQuanChuyen = new HorizontalLayout();
	TextField txtCoQuanChuyen = new TextField();
	Button btnCoQuanChuyen = new Button("",FontAwesome.SEARCH_PLUS);
	TextField txtSoVanBanDi = new TextField();
	DateField dfNgayChyen = new DateField();

	ResponsiveRow rowChuyenCoQuan = buildRowChuyenCoQuan();
	ResponsiveRow rowSoVanBan = buildRowSoHieu();

	TextArea txtNoiDungXuLy = new TextArea();
	FormAttachFile attachFile = new FormAttachFile();

	LayoutButtonSubmit layoutSubmit = new LayoutButtonSubmit();

	DanhMucServiceUtil svDanhMuc = new DanhMucServiceUtil();
	QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh = new QuaTrinhXuLyGiaiQuyetServiceUtil();
	DonThuServiceUtil svDonThu = new DonThuServiceUtil();
	UploadServiceUtil svUpload = new UploadServiceUtil();

	WindowChuyenDonThu wdChuyenDon = null;

	private KetQuaXuLyBean modelKQXL = new KetQuaXuLyBean();
	private Map<String,Integer> listAllLienThongId;

	private String idCQXL = SessionUtil.getLienThongOrgId();
	private String idCQXLTiep = SessionUtil.getLienThongOrgId();
	private int orgChuyen = -1;

	private int idDonThu = -1;

	boolean validateSuccess = false;
	LienThongKetQuaXuLy lienThongKQXL = new LienThongKetQuaXuLy();

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	public WindowThemKetQuaXuLyTmp(int idDonThu) {
		try {
			this.idDonThu = idDonThu;
			buildLayout();
			confiComponent();
			loadDefaultValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadDefaultValue() throws Exception {
		/* Hướng xử lý */
		List<DanhMucBean> listHuongXuLy = svDanhMuc.getDanhMucList(DanhMucTypeEnum.huongxuly.getName(),DanhMucTypeEnum.huongxuly.getIdType());

		for(DanhMucBean model : listHuongXuLy)
		{
			cmbHuongXuLy.addItem(model.getIntMa());
			cmbHuongXuLy.setItemCaption(model.getIntMa(), model.getName());
		}
		cmbHuongXuLy.setNullSelectionAllowed(false);
		cmbHuongXuLy.setValue(1);	

		List<User> listUser = UserLocalServiceUtil.getOrganizationUsers(SessionUtil.getOrgId());

		for(User user : listUser)
		{
			if(LiferayServiceUtil.isUserHasRole(user.getUserId(), UserRole.TRUONGPHONG.getName()))
			{
				cmbCanBoDuyet.addItem(user.getUserId());
				cmbCanBoDuyet.setItemCaption(user.getUserId(), user.getFirstName()+" - "+user.getJobTitle());
				cmbCanBoDuyet.select(user.getUserId());
			}
			else
			{
				cmbCanBoXuLy.addItem(user.getUserId());
				cmbCanBoXuLy.setItemCaption(user.getUserId(), user.getFirstName());

				if(user.getUserId()==SessionUtil.getUserId())
					cmbCanBoXuLy.select(SessionUtil.getUserId());
			}
		}

		cmbCanBoXuLy.setNullSelectionAllowed(false);
		cmbCanBoDuyet.setNullSelectionAllowed(false);

		dfNgayChyen.setValue(new Date());
		dfNgayXuLy.setValue(new Date());

		txtCoQuanXuLy.setValue(LiferayServiceUtil.getUserOrgName(SessionUtil.getUserId()));
		txtCoQuanXuLy.setEnabled(false);
		hMainCoQuanXuLyTiep.setVisible(false);
		txtCoQuanXuLyTiep.setEnabled(false);
		txtCoQuanChuyen.setEnabled(false);
	}

	private void buildLayout() {
		vMain.addComponent(rslMain);
		vMain.addComponent(layoutSubmit);

		rslMain.addRow(buildRowCanBo());
		rslMain.addRow(buildRowHuongXuLy());
		rslMain.addRow(buildRowCoQuan());
		rslMain.addRow(rowChuyenCoQuan);
		rslMain.addRow(rowSoVanBan);
		rslMain.addComponent(DonThuModule.buildFormLayoutSingle("Nội dung xử lý"+DonThuModule.requiredMark, txtNoiDungXuLy,"130px"));
		rslMain.addComponent(DonThuModule.buildFormLayoutSingle("Tệp đính kèm"+DonThuModule.requiredMark, attachFile,"130px"));

		txtNoiDungXuLy.setWidth("100%");
		rowChuyenCoQuan.setVisible(false);
		rowSoVanBan.setVisible(false);

		vMain.setComponentAlignment(layoutSubmit, Alignment.MIDDLE_RIGHT);

		vMain.setSpacing(true);
		vMain.setMargin(true);

		this.setContent(vMain);
		this.setCaption(FontAwesome.PLUS.getHtml()+" Thêm kết quả xử lý cho đơn thư");
		this.setCaptionAsHtml(true);
		this.setWidth("1000px");
		this.setModal(true);
		this.center();

		buildAttachFile();
		setTypeFileAccept();
		attachFile.setNameFolderTMP(File.separator+idDonThu);
	}

	private void confiComponent() {
		cmbHuongXuLy.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				boolean isChuyen = (int)cmbHuongXuLy.getValue()==5?true:false;

				rowChuyenCoQuan.setVisible(isChuyen);
				rowSoVanBan.setVisible(isChuyen);

				if(isChuyen)
				{
					wdChuyenDon = new WindowChuyenDonThu(idDonThu);
					listAllLienThongId = wdChuyenDon.getListAllLienThongId();
					hMainCoQuanXuLyTiep.setVisible(true);
				}
				else
				{
					hMainCoQuanXuLyTiep.setVisible(false);
					txtCoQuanXuLyTiep.setValue("");
					orgChuyen = -1;
					txtCoQuanChuyen.setValue("");
					idCQXLTiep = SessionUtil.getLienThongOrgId();
				}
			}
		});

		btnCoQuanXuLyTiep.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				WindowChonCoQuan wdCoQuan = new WindowChonCoQuan();
				UI.getCurrent().addWindow(wdCoQuan);
				wdCoQuan.getBtnOk().addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						idCQXLTiep = wdCoQuan.getCoQuanID();
						if(listAllLienThongId.containsKey(idCQXLTiep))
						{
							orgChuyen = listAllLienThongId.get(idCQXLTiep);
							try {
								String orgName = OrganizationLocalServiceUtil.getOrganization(orgChuyen).getName();
								txtCoQuanChuyen.setValue(orgName);
							} catch (PortalException e) {
								e.printStackTrace();
							} catch (SystemException e) {
								e.printStackTrace();
							}
						}

						try {
							txtCoQuanXuLyTiep.setValue(DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.coquan.getName(),idCQXLTiep).getName());
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				});

			}
		});

		btnCoQuanChuyen.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().addWindow(wdChuyenDon);

				wdChuyenDon.getBtnOk().addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						orgChuyen = wdChuyenDon.getOrgSelectedId();
						String orgName;
						try {
							orgName = OrganizationLocalServiceUtil.getOrganization(orgChuyen).getName();
							txtCoQuanChuyen.setValue(orgName);
						} catch (PortalException | SystemException e) {
							// TODO Auto-generated catch block
						}

						wdChuyenDon.close();
					}
				});
			}
		});

		layoutSubmit.getBtnSave().addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				validateFormSubmit();
				if(validateSuccess)
				{
					try {
						String valueTomTatNoiDung = svDonThu.getDonThu(idDonThu).getNoiDungDonThu();

						modelKQXL.setTomTatNoiDung(valueTomTatNoiDung);
						modelKQXL.setNgayXuLy(dfNgayXuLy.getValue());
						modelKQXL.setCanBoDuyet(cmbCanBoDuyet.getItemCaption(cmbCanBoDuyet.getValue()));
						modelKQXL.setMaHuongXuLy((int)cmbHuongXuLy.getValue());
						modelKQXL.setCanBoXuLy(cmbCanBoXuLy.getItemCaption(cmbCanBoXuLy.getValue()));
						modelKQXL.setNoiDungXuLy(txtNoiDungXuLy.getValue());
						modelKQXL.setMaCQXL(idCQXL);
						modelKQXL.setMaCQXLTiep(idCQXLTiep);
						modelKQXL.setTenFileDinhKem(attachFile.getFileNameOld());
						modelKQXL.setLinkFileDinhKem(attachFile.getFileNameNew());
						modelKQXL.setNgayTao(new Date());
						modelKQXL.setMaDonThu(idDonThu);
						modelKQXL.setOrgTao(SessionUtil.getOrgId());

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

						ThongTinDonThuBean modelThongTinDon = svDonThu.getThongTinDonThu(idDonThu, SessionUtil.getOrgId());
						svDonThu.updateThongTinDonThuDate("NgayHoanThanhXuLy", new Date(), modelThongTinDon.getMaThongTinDon());

						int idQuaTrinh = svQuaTrinh.insertQuaTrinhXLGQ(model);
						svQuaTrinh.insertKetQuaXuLy(modelKQXL);
						//lienThongKQXL.executeLienThong(modelKQXL, idDonThu);
						new LienThongKetQuaXuLy().executeLienThong(idDonThu, SessionUtil.getOrgId());
						DonThuModule.insertThongBao(idDonThu, idQuaTrinh);

						//update thẩm quyền giải quyết
						String coQuanCodeType = idCQXLTiep.substring(0,1);
						svDonThu.updateDonThuStringField("ThamQuyenGiaiQuyet", coQuanCodeType, idDonThu);

						//nếu chuyển đơn
						if((int)cmbHuongXuLy.getValue()==5 && orgChuyen!=-1)
						{
							List<Long> list = new ArrayList<Long>();
							List<User> listUserRecive = UserLocalServiceUtil.getOrganizationUsers(orgChuyen);
							for(User user : listUserRecive)
							{
								list.add(user.getUserId());
							}
							for(Long n : list)
							{									
								LuanChuyenBean modelLuanChuyen = new LuanChuyenBean();
								modelLuanChuyen.setIdDonThu(idDonThu);
								modelLuanChuyen.setIdUserChuyen(SessionUtil.getUserId());
								modelLuanChuyen.setIdUserNhan(n);
								modelLuanChuyen.setPhanCong(false);
								modelLuanChuyen.setNgayChuyen(new Date());

								svQuaTrinh.insertLuanChuyen(modelLuanChuyen);

							}
							Notification.show("Đã chuyển đơn thư đến "+OrganizationLocalServiceUtil.getOrganization(orgChuyen).getName(),Type.TRAY_NOTIFICATION);

							QuaTrinhXuLyGiaiQuyetBean modelQT = new QuaTrinhXuLyGiaiQuyetBean();
							modelQT.setMaDonThu(idDonThu);
							modelQT.setNoiDung(svQuaTrinh.returnStringChuyenDon(SessionUtil.getUserId(), orgChuyen));
							modelQT.setHeThongTao(true);
							modelQT.setNgayDang(new Date());
							modelQT.setUserNhap(SessionUtil.getUserId());

							ThongTinDonThuBean modelThongTin = new ThongTinDonThuBean();
							modelThongTin.setNguonDonDen(4);
							modelThongTin.setMaCoQuanChuyenDen(LiferayServiceUtil.getOrgCustomFieldValue(SessionUtil.getOrgId(), "OrgLienThongId"));
							modelThongTin.setSoVanBanDen(txtSoVanBanDi.getValue().trim());
							modelThongTin.setNgayPhatHanhVanBan(dfNgayChyen.getValue());
							modelThongTin.setOrgChuyen(SessionUtil.getOrgId());
							modelThongTin.setOrgNhan(orgChuyen);
							modelThongTin.setNgayNhan(new Date());
							modelThongTin.setMaDonThu(idDonThu);
							try {
								int idQuaTrinhChuyenDon = svQuaTrinh.insertQuaTrinhXLGQ(modelQT);
								svDonThu.insertThongTinDon(modelThongTin);
								DonThuModule.insertThongBao(idDonThu, idQuaTrinhChuyenDon);
								String idLienThongGanVuViec = null;
								String ganVuViec = svDonThu.getDonThu(idDonThu).getGanVuViec();
								if(ganVuViec!=null)
								{
									idLienThongGanVuViec = svDonThu.getDonThu(Integer.parseInt(ganVuViec)).getIdHoSoLienThong();
								}
								new LienThongVuViec().executeLienThong(idDonThu, orgChuyen, idLienThongGanVuViec, "save");
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					}catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		});

		layoutSubmit.getBtnCancel().addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
	}

	private ResponsiveRow buildRowCanBo()
	{
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Cán bộ xử lý"+DonThuModule.requiredMark, cmbCanBoXuLy,"130px")).withDisplayRules(12, 12, 6, 6);
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Cán bộ duyệt"+DonThuModule.requiredMark, cmbCanBoDuyet,"110px")).withDisplayRules(12, 12, 6, 6);

		cmbCanBoXuLy.setWidth("100%");
		cmbCanBoDuyet.setWidth("100%");

		return row;
	}

	private ResponsiveRow buildRowHuongXuLy()
	{
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Hướng xử lý"+DonThuModule.requiredMark, cmbHuongXuLy,"130px")).withDisplayRules(12, 12, 6, 6);
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Ngày xử lý"+DonThuModule.requiredMark, dfNgayXuLy,"110px")).withDisplayRules(12, 12, 6, 6);

		cmbHuongXuLy.setWidth("100%");
		dfNgayXuLy.setWidth("100%");

		return row;
	}

	private ResponsiveRow buildRowCoQuan()
	{
		hMainCoQuanXuLyTiep = DonThuModule.buildFormLayoutSingle("Cơ quan xử lý tiếp"+DonThuModule.requiredMark, hCoQuanXuLyTiep,"110px");
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Cơ quan xử lý"+DonThuModule.requiredMark, txtCoQuanXuLy,"130px")).withDisplayRules(12, 12, 6, 6);
		row.addColumn().withComponent(hMainCoQuanXuLyTiep).withDisplayRules(12, 12, 6, 6);

		txtCoQuanXuLy.setWidth("100%");
		hCoQuanXuLyTiep.addComponents(txtCoQuanXuLyTiep,btnCoQuanXuLyTiep);

		txtCoQuanXuLyTiep.setWidth("100%");
		txtCoQuanXuLyTiep.setEnabled(false);

		hCoQuanXuLyTiep.setExpandRatio(txtCoQuanXuLyTiep, 1.0f);

		hCoQuanXuLyTiep.setWidth("100%");
		hCoQuanXuLyTiep.setSpacing(true);

		return row;
	}

	private ResponsiveRow buildRowChuyenCoQuan()
	{
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Cơ quan chuyển", hCoQuanChuyen,"130px")).withDisplayRules(12, 12, 6, 12);

		hCoQuanChuyen.addComponents(txtCoQuanChuyen,btnCoQuanChuyen);


		txtCoQuanChuyen.setWidth("100%");
		txtCoQuanChuyen.setEnabled(false);

		hCoQuanChuyen.setExpandRatio(txtCoQuanChuyen, 1.0f);

		hCoQuanChuyen.setWidth("100%");
		hCoQuanChuyen.setSpacing(true);

		return row;
	}

	private ResponsiveRow buildRowSoHieu()
	{
		ResponsiveRow row = new ResponsiveRow();
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Số hiệu"+DonThuModule.requiredMark, txtSoVanBanDi,"130px")).withDisplayRules(12, 12, 6, 6);
		row.addColumn().withComponent(DonThuModule.buildFormLayoutSingle("Ngày chuyển"+DonThuModule.requiredMark, dfNgayChyen,"110px")).withDisplayRules(12, 12, 6, 6);

		txtSoVanBanDi.setWidth("100%");
		dfNgayChyen.setWidth("100%");

		return row;
	}

	private void setTypeFileAccept(){
		List<String> typeFileAcept = new ArrayList<String>();
		try {
			typeFileAcept = svUpload.getTypeAccessedList();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		attachFile.setTypeAccept(typeFileAcept);
	}

	private void buildAttachFile(){
		attachFile.setBaseDirectory(UploadServiceUtil.getAbsolutePath());
		attachFile.setPathFolderUpload(UploadServiceUtil.getFolderNameDonThu());
		attachFile.setMaxFileSize(svUpload.getMaxSize());

		attachFile.setMakeFolderTMP(true);
		//attachFile.setNameFolderTMP(File.separator + new Random().nextInt(1000000));
	}

	public void validateFormSubmit()
	{
		if(cmbCanBoXuLy.getValue()==null)
		{
			Notification.show("Vui lòng chọn cán bộ xử lý",Type.WARNING_MESSAGE);
			cmbCanBoXuLy.focus();
			validateSuccess = false;
			return;
		}
		if(cmbCanBoDuyet.getValue()==null)
		{
			Notification.show("Vui lòng chọn cán bộ duyệt",Type.WARNING_MESSAGE);
			cmbCanBoDuyet.focus();
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
		if((int)cmbHuongXuLy.getValue()==5)
		{
			if(idCQXLTiep==null)
			{
				Notification.show("Vui lòng chọn cơ quan xử lý tiếp",Type.WARNING_MESSAGE);
				btnCoQuanXuLyTiep.focus();
				validateSuccess = false;
				return;
			}

			if(txtSoVanBanDi.isEmpty())
			{
				Notification.show("Vui lòng nhập vào số hiệu văn bản đi",Type.WARNING_MESSAGE);
				txtSoVanBanDi.focus();
				validateSuccess = false;
				return;
			}
			if(dfNgayChyen.isEmpty())
			{
				Notification.show("Vui lòng nhập vào ngày chuyển đơn",Type.WARNING_MESSAGE);

				dfNgayChyen.focus();
				validateSuccess = false;
				return;
			}
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
