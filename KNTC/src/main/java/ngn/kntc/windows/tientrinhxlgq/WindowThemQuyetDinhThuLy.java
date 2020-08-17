package ngn.kntc.windows.tientrinhxlgq;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ngn.kntc.beans.QuaTrinhXuLyGiaiQuyetBean;
import ngn.kntc.beans.QuyetDinhThuLyBean;
import ngn.kntc.lienthongttcp.LienThongQuyetDinhThuLy;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.modules.KNTCProps;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.LiferayServiceUtil;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;
import ngn.kntc.utils.SessionUtil;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class WindowThemQuyetDinhThuLy extends WindowTienTrinhXuLy{
	private TextField txtCoQuanThuLy = new TextField("Cơ quan thụ lý"+DonThuModule.requiredMark);
	private DateField dfNgayThuLy = new DateField("Ngày thụ lý"+DonThuModule.requiredMark,new Date());
	private DateField dfHanGiaiQuyet = new DateField("Hạn Giải Quyết"+DonThuModule.requiredMark);
	private ComboBox cmbNguoiDuyetDon = new ComboBox("Cán bộ duyệt đơn"+DonThuModule.requiredMark);
	QuyetDinhThuLyBean modelQDTL = new QuyetDinhThuLyBean();

	KNTCProps props = new KNTCProps();

	private QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh = new QuaTrinhXuLyGiaiQuyetServiceUtil();
	private DonThuServiceUtil svDonThu = new DonThuServiceUtil();

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private LienThongQuyetDinhThuLy lienThongQDTL = new LienThongQuyetDinhThuLy();

	private boolean validateSuccess = false;
	private int idDonThu;

	public WindowThemQuyetDinhThuLy(int idDonThu) {
		this.idDonThu = idDonThu;
		buildLayout();
		configComponent();
	}

	public WindowThemQuyetDinhThuLy(QuyetDinhThuLyBean quyetDinhThuLyBeanInput,int idDonThu) {
		this.idDonThu = idDonThu;
		buildLayout();
		configComponent();
		modelQDTL = quyetDinhThuLyBeanInput;
		if(modelQDTL.getLinkFileDinhKem()!=null)
		{
			dfNgayThuLy.setValue(modelQDTL.getNgayThuLy());
			dfHanGiaiQuyet.setValue(modelQDTL.getHanGiaiQuyet());
			cmbNguoiDuyetDon.setValue(modelQDTL.getCanBoDuyet());
			attachFile.setFileNameNew(modelQDTL.getLinkFileDinhKem());
			attachFile.setFileNameOld(modelQDTL.getTenFileDinhKem());
			attachFile.showListAttachFiles();
		}
	}

	@SuppressWarnings("static-access")
	public void buildLayout(){
		super.buildLayout();
		frmLayout.addComponent(txtCoQuanThuLy);
		frmLayout.addComponent(dfNgayThuLy);
		frmLayout.addComponent(dfHanGiaiQuyet);
		frmLayout.addComponent(cmbNguoiDuyetDon);
		frmLayout.addComponent(attachFile);

		attachFile.setNameFolderTMP(File.separator+idDonThu);

		txtCoQuanThuLy.setCaptionAsHtml(true);
		dfNgayThuLy.setCaptionAsHtml(true);
		cmbNguoiDuyetDon.setCaptionAsHtml(true);
		dfHanGiaiQuyet.setCaptionAsHtml(true);

		try {
			List<User> listLanhDao = LiferayServiceUtil.returnListLanhDao();

			for(User user: listLanhDao)
			{
				long idUser = user.getUserId();
				String tenUser = user.getFirstName();

				cmbNguoiDuyetDon.addItem(idUser);
				cmbNguoiDuyetDon.setItemCaption(idUser, tenUser);
			}
			cmbNguoiDuyetDon.setNullSelectionAllowed(false);
			cmbNguoiDuyetDon.select(listLanhDao.get(0).getUserId());
			
			txtCoQuanThuLy.setValue(OrganizationLocalServiceUtil.getOrganization(SessionUtil.getOrgId()).getName());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		txtCoQuanThuLy.setEnabled(false);

		int intHanXuLy = 0;
		Calendar car = Calendar.getInstance();
		Date hanTraLoi;
		try {
			switch (svDonThu.getDonThu(idDonThu).getLoaiDonThu()) {
			case 1:
				intHanXuLy = Integer.parseInt(props.getProperty("han.xu.ly.khieu.nai"));
				car.add(car.DATE, +intHanXuLy);
				hanTraLoi = car.getTime();
				dfHanGiaiQuyet.setValue(hanTraLoi);
				dfHanGiaiQuyet.setCaption("Hạn giải quyết (Khiếu nại)");
				break;
			case 2:
				intHanXuLy = Integer.parseInt(props.getProperty("han.xu.ly.to.cao"));
				car.add(car.DATE, +intHanXuLy);
				hanTraLoi = car.getTime();
				dfHanGiaiQuyet.setValue(hanTraLoi);
				dfHanGiaiQuyet.setCaption("Hạn giải quyết (tố cáo)");
				break;
			case 3:
				intHanXuLy = Integer.parseInt(props.getProperty("han.xu.ly.phan.anh"));
				car.add(car.DATE, +intHanXuLy);
				hanTraLoi = car.getTime();
				dfHanGiaiQuyet.setValue(hanTraLoi);
				dfHanGiaiQuyet.setCaption("Hạn giải quyết (phản ánh)");
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.setCaption("Thêm quyết định thụ lý");
		this.setWidth("350px");

		attachFile.setCaption("Tập đính kèm");

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
						modelQDTL.setMaCoQuanThuLy(SessionUtil.getOrgId()+"");
						modelQDTL.setNgayThuLy(dfNgayThuLy.getValue());
						modelQDTL.setHanGiaiQuyet(dfHanGiaiQuyet.getValue());
						modelQDTL.setCanBoDuyet(cmbNguoiDuyetDon.getItemCaption((Long)cmbNguoiDuyetDon.getValue()));
						modelQDTL.setTenFileDinhKem(attachFile.getFileNameOld());
						modelQDTL.setLinkFileDinhKem(attachFile.getFileNameNew());
						modelQDTL.setNgayTao(new Date());
						modelQDTL.setMaDonThu(idDonThu);

						QuaTrinhXuLyGiaiQuyetBean model = new QuaTrinhXuLyGiaiQuyetBean();

						model.setNoiDung(svQuaTrinh.returnStringCanBoNhap(SessionUtil.getUserId(), "đã nhập quyết định thụ lý","<span style='color: #a2271e'>Ngày thụ lý:</span> "+sdf.format(modelQDTL.getNgayThuLy())+" <span style='color: #a2271e'>- Hạn giải quyết:</span> "+sdf.format(modelQDTL.getHanGiaiQuyet())));

						model.setUserNhap(SessionUtil.getUserId());
						model.setNgayDang(new Date());
						model.setMaDonThu(idDonThu);
						if(attachFile.getFileNameNew()!=null)
						{
							model.setTenFileDinhKem(attachFile.getFileNameOld());
							model.setLinkFileDinhKem(attachFile.getFileNameNew());
						}
						model.setHeThongTao(true);

						int idQuaTrinh = svQuaTrinh.insertQuaTrinhXLGQ(model);
						svQuaTrinh.insertQuyetDinhThuLy(modelQDTL);
						//lienThongQDTL.executeLienThong(modelQDTL, idDonThu);
						new LienThongQuyetDinhThuLy().executeLienThong(idDonThu, SessionUtil.getOrgId());
						DonThuModule.insertThongBao(idDonThu, idQuaTrinh);
						svDonThu.updateDonThuDate("NgayThuLy", modelQDTL.getNgayThuLy(),idDonThu);
						svDonThu.updateDonThuDate("NgayHanGiaiQuyet", modelQDTL.getHanGiaiQuyet(),idDonThu);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	public void validateFormSubmit()
	{
		if(dfNgayThuLy.isEmpty())
		{
			Notification.show("Vui lòng chọn ngày thụ lý",Type.WARNING_MESSAGE);
			dfNgayThuLy.focus();
			validateSuccess = false;
			return;
		}
		if(dfHanGiaiQuyet.isEmpty())
		{
			Notification.show("Vui lòng chọn hạn giải quyết",Type.WARNING_MESSAGE);
			dfHanGiaiQuyet.focus();
			validateSuccess = false;
			return;
		}
		if(cmbNguoiDuyetDon.isEmpty())
		{
			Notification.show("Vui lòng nhập vào cán bộ duyệt",Type.WARNING_MESSAGE);
			cmbNguoiDuyetDon.focus();
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

	public KNTCProps getProps() {
		return props;
	}

	public void setProps(KNTCProps props) {
		this.props = props;
	}

	public QuyetDinhThuLyBean getQuyetDinhThuLyBean() {
		return modelQDTL;
	}
	public void setQuyetDinhThuLyBean(QuyetDinhThuLyBean quyetDinhThuLyBean) {
		this.modelQDTL = quyetDinhThuLyBean;
	}
	public boolean isValidateSuccess() {
		return validateSuccess;
	}
	public void setValidateSuccess(boolean validateSuccess) {
		this.validateSuccess = validateSuccess;
	}
}
