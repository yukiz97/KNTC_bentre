package ngn.kntc.windows.tientrinhxlgq;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ngn.kntc.beans.QuaTrinhXuLyGiaiQuyetBean;
import ngn.kntc.beans.ThiHanhQuyetDinhBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.lienthongttcp.LienThongThiHanhGiaiQuyet;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.SessionUtil;
import ngn.kntc.windows.WindowChonCoQuan;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class WindowThemThiHanhGiaiQuyet extends WindowTienTrinhXuLy{
	private HorizontalLayout hCQBH = new HorizontalLayout();
	private Label lblCQTH = new Label();
	private Button btnCQTH = new Button(FontAwesome.SEARCH);
	private DateField dfNgayThiHanh = new DateField("Ngày thi hành"+DonThuModule.requiredMark,new Date());
	private HorizontalLayout hThuHoi = new HorizontalLayout();
	private TextField txtThuHoiTien = new TextField("Tiền (vnđ)");
	private TextField txtThuHoiDatO = new TextField("Đất ở (m²)");
	private TextField txtThuHoiDatSX = new TextField("Đất sản xuất (m²)");
	private HorizontalLayout hTraLai = new HorizontalLayout();
	private TextField txtTraLaiTien = new TextField("Tiền (vnđ)");
	private TextField txtTraLaiDatO = new TextField("Đất ở (m²)");
	private TextField txtTraLaiDatSX = new TextField("Đất sản xuất (m²)");
	private HorizontalLayout hSoNguoi = new HorizontalLayout();
	private TextField txtSoCaNhanBiXuLy = new TextField("Số cá nhân");
	private TextField txtSoTapTheBiXuLy = new TextField("Số tập thể");
	private TextField txtTaiSanQuyRaTien = new TextField("Tài sản quy ra tiền (vnđ)");
	private TextField txtSoTienNopPhat = new TextField("Số tiền nộp phạt (vnđ)");
	private TextField txtSoDoiTuongKhoiTo = new TextField("Số đối tượng khởi tố");
	String maCQTH;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private LienThongThiHanhGiaiQuyet lienThongTHGQ = new LienThongThiHanhGiaiQuyet();

	private ThiHanhQuyetDinhBean modelTHGQ = new ThiHanhQuyetDinhBean();
	
	private int idDonThu;
	private boolean validateSuccess = false;
	
	public WindowThemThiHanhGiaiQuyet(int idDonThu) {
		this.idDonThu = idDonThu;
		buildLayout();
		configComponent();
	}

	public WindowThemThiHanhGiaiQuyet(ThiHanhQuyetDinhBean thiHanhQuyetDinhInput,int idDonThu) {
		this.idDonThu = idDonThu;
		buildLayout();
		configComponent();
		modelTHGQ = thiHanhQuyetDinhInput;
		if(modelTHGQ.getMaCoQuanThiHanh()!=null)
		{
			try {
				lblCQTH.setValue(DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.coquan.getName(),modelTHGQ.getMaCoQuanThiHanh()).getName());
				maCQTH = modelTHGQ.getMaCoQuanThiHanh();
				dfNgayThiHanh.setValue(modelTHGQ.getNgayCapNhat());
				txtThuHoiTien.setValue(modelTHGQ.getThuHoiTien()!=0?String.valueOf(modelTHGQ.getThuHoiTien()):"");
				txtThuHoiDatO.setValue(modelTHGQ.getThuHoiDatO()!=0?String.valueOf(modelTHGQ.getThuHoiDatO()):"");
				txtThuHoiDatSX.setValue(modelTHGQ.getThuHoiDatSX()!=0?String.valueOf(modelTHGQ.getThuHoiDatSX()):"");
				txtTraLaiTien.setValue(modelTHGQ.getTraLaiTien()!=0?String.valueOf(modelTHGQ.getTraLaiTien()):"");
				txtTraLaiDatO.setValue(modelTHGQ.getTraLaiDatO()!=0?String.valueOf(modelTHGQ.getTraLaiDatO()):"");
				txtTraLaiDatSX.setValue(modelTHGQ.getTraLaiDatSX()!=0?String.valueOf(modelTHGQ.getTraLaiDatSX()):"");
				txtSoCaNhanBiXuLy.setValue(modelTHGQ.getSoNguoiBiXuLy()!=0?String.valueOf(modelTHGQ.getSoNguoiBiXuLy()):"");
				txtSoTapTheBiXuLy.setValue(modelTHGQ.getSoTapTheBiXuLy()!=0?String.valueOf(modelTHGQ.getSoTapTheBiXuLy()):"");
				txtTaiSanQuyRaTien.setValue(modelTHGQ.getTaiSanQuyRaTien()!=0?String.valueOf(modelTHGQ.getTaiSanQuyRaTien()):"");
				txtSoTienNopPhat.setValue(modelTHGQ.getSoTienNopPhat()!=0?String.valueOf(modelTHGQ.getSoTienNopPhat()):"");
				txtSoDoiTuongKhoiTo.setValue(modelTHGQ.getSoDoiTuongBiKhoiTo()!=0?String.valueOf(modelTHGQ.getSoDoiTuongBiKhoiTo()):"");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void buildLayout() {
		super.buildLayout();
		frmLayout.addComponent(hCQBH);
		frmLayout.addComponent(dfNgayThiHanh);
		frmLayout.addComponent(hThuHoi);
		frmLayout.addComponent(hTraLai);
		frmLayout.addComponent(hSoNguoi);
		frmLayout.addComponent(txtTaiSanQuyRaTien);
		frmLayout.addComponent(txtSoTienNopPhat);
		frmLayout.addComponent(txtSoDoiTuongKhoiTo);
		//frmLayout.addComponent(attachFile);
		
		dfNgayThiHanh.setCaptionAsHtml(true);
		hCQBH.setCaptionAsHtml(true);

		hCQBH.addComponents(lblCQTH,btnCQTH);
		hCQBH.setSpacing(true);
		hCQBH.setCaption("Cơ quan thi hành"+DonThuModule.requiredMark);

		hThuHoi.addComponents(txtThuHoiTien,txtThuHoiDatO,txtThuHoiDatSX);
		hThuHoi.setSpacing(true);
		hThuHoi.setCaption("Thu hồi");

		hTraLai.addComponents(txtTraLaiTien,txtTraLaiDatO,txtTraLaiDatSX);
		hTraLai.setSpacing(true);
		hTraLai.setCaption("Trả lại");

		hSoNguoi.addComponents(txtSoCaNhanBiXuLy,txtSoTapTheBiXuLy);
		hSoNguoi.setSpacing(true);
		hSoNguoi.setCaption("Bị xử lý");

		this.setCaption("Thêm quyết định giải quyết");

		attachFile.setCaption("Tệp đính kèm");

		frmLayout.setMargin(new MarginInfo(true,true,false,false));

		this.setWidth("650px");
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
						int idQDGQ = svQuaTrinh.getQuyetDinhGiaiQuyet(idDonThu).getMaQuyetDinhGiaiQuyet();
						/* Kết quả xử lý bean */
						modelTHGQ.setMaCoQuanThiHanh(maCQTH);
						modelTHGQ.setNgayCapNhat(dfNgayThiHanh.getValue());
						modelTHGQ.setThuHoiTien((!txtThuHoiTien.isEmpty())?Integer.parseInt(txtThuHoiTien.getValue()):0);
						modelTHGQ.setThuHoiDatO(!txtThuHoiDatO.isEmpty()?Integer.parseInt(txtThuHoiDatO.getValue()):0);
						modelTHGQ.setThuHoiDatSX(!txtThuHoiDatSX.isEmpty()?Integer.parseInt(txtThuHoiDatSX.getValue()):0);
						modelTHGQ.setTraLaiTien(!txtTraLaiTien.isEmpty()?Integer.parseInt(txtTraLaiTien.getValue()):0);
						modelTHGQ.setTraLaiDatO(!txtTraLaiDatO.isEmpty()?Integer.parseInt(txtTraLaiDatO.getValue()):0);
						modelTHGQ.setTraLaiDatSX(!txtTraLaiDatSX.isEmpty()?Integer.parseInt(txtTraLaiDatSX.getValue()):0);
						modelTHGQ.setSoNguoiBiXuLy(!txtSoCaNhanBiXuLy.isEmpty()?Integer.parseInt(txtSoCaNhanBiXuLy.getValue()):0);
						modelTHGQ.setSoTapTheBiXuLy(!txtSoTapTheBiXuLy.isEmpty()?Integer.parseInt(txtSoTapTheBiXuLy.getValue()):0);
						modelTHGQ.setSoTienNopPhat(!txtSoTienNopPhat.isEmpty()?Integer.parseInt(txtSoTienNopPhat.getValue()):0);
						modelTHGQ.setTaiSanQuyRaTien(!txtTaiSanQuyRaTien.isEmpty()?Integer.parseInt(txtTaiSanQuyRaTien.getValue()):0);
						modelTHGQ.setSoDoiTuongBiKhoiTo(!txtSoDoiTuongKhoiTo.isEmpty()?Integer.parseInt(txtSoDoiTuongKhoiTo.getValue()):0);
						modelTHGQ.setNgayTao(new Date());
						modelTHGQ.setIdQuyetDinhGiaiQuyet(idQDGQ);
						modelTHGQ.setOrgTao(SessionUtil.getOrgId());
						
						/* Thêm vào quá trình xlgq */
						QuaTrinhXuLyGiaiQuyetBean model = new QuaTrinhXuLyGiaiQuyetBean();

						model.setNoiDung(svQuaTrinh.returnStringCanBoNhap(SessionUtil.getUserId(), "đã nhập một kết quả thi hành quyết định", "<span style='color: #a2271e'>Ngày thi hành:</span> "+sdf.format(modelTHGQ.getNgayCapNhat())));

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
						int idThiHanh = svQuaTrinh.insertThiHanhGiaiQuyet(modelTHGQ);
						DonThuModule.insertThongBao(idDonThu, idQuaTrinh);
						modelTHGQ.setIdThiHanhQuyetDinh(idThiHanh);
						//lienThongTHGQ.executeLienThong(modelTHGQ, idDonThu);
						new LienThongThiHanhGiaiQuyet().executeLienThong(idDonThu, idQDGQ, SessionUtil.getOrgId());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		btnCQTH.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				WindowChonCoQuan wdCoQuan = new WindowChonCoQuan();
				UI.getCurrent().addWindow(wdCoQuan);
				wdCoQuan.getBtnOk().addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						maCQTH = wdCoQuan.getCoQuanID();
						try {
							lblCQTH.setValue(DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.coquan.getName(),maCQTH).getName());
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
	}

	public ThiHanhQuyetDinhBean getThiHanhQuyetDinhBean() {
		return modelTHGQ;
	}

	public void setThiHanhQuyetDinhBean(ThiHanhQuyetDinhBean thiHanhQuyetDinhBean) {
		this.modelTHGQ = thiHanhQuyetDinhBean;
	}

	@Override
	public void validateFormSubmit() {
		try{
			if(!txtThuHoiTien.isEmpty())
				Integer.parseInt(txtThuHoiTien.getValue());
			if(!txtThuHoiDatO.isEmpty())
				Integer.parseInt(txtThuHoiDatO.getValue());
			if(!txtThuHoiDatSX.isEmpty())
				Integer.parseInt(txtThuHoiDatSX.getValue());
			if(!txtTraLaiTien.isEmpty())
				Integer.parseInt(txtTraLaiTien.getValue());
			if(!txtTraLaiDatO.isEmpty())
				Integer.parseInt(txtTraLaiDatO.getValue());
			if(!txtTraLaiDatSX.isEmpty())
				Integer.parseInt(txtTraLaiDatSX.getValue());
		}catch(Exception e){
			Notification.show("Tất cả dữ liệu thu hồi và trả phải là kiểu số",Type.WARNING_MESSAGE);
			validateSuccess = false;
			return;
		}
		try{
			if(!txtSoCaNhanBiXuLy.isEmpty())
				Integer.parseInt(txtSoCaNhanBiXuLy.getValue());
			if(!txtSoTapTheBiXuLy.isEmpty())
				Integer.parseInt(txtSoTapTheBiXuLy.getValue());
		}catch(Exception e){
			Notification.show("Số cá nhân và tập thể bị xử lý phải là kiểu số",Type.WARNING_MESSAGE);
			validateSuccess = false;
			return;
		}
		try{
			if(!txtTaiSanQuyRaTien.isEmpty())
				Integer.parseInt(txtTaiSanQuyRaTien.getValue());
		}catch(Exception e){
			Notification.show("Số tiền từ tài sản quy ra phải là kiểu số",Type.WARNING_MESSAGE);
			validateSuccess = false;
			return;
		}
		try{
			if(!txtSoTienNopPhat.isEmpty())
				Integer.parseInt(txtSoTienNopPhat.getValue());
		}catch(Exception e){
			Notification.show("Số tiền nộp phạt phải là kiểu số",Type.WARNING_MESSAGE);
			validateSuccess = false;
			return;
		}
		try{
			if(!txtSoDoiTuongKhoiTo.isEmpty())
				Integer.parseInt(txtSoDoiTuongKhoiTo.getValue());
		}catch(Exception e){
			Notification.show("Số tiền đối tượng bị phải là kiểu số",Type.WARNING_MESSAGE);
			validateSuccess = false;
			return;
		}
		if(maCQTH==null)
		{
			Notification.show("Vui lòng chọn cơ quan thi hành quyết định",Type.WARNING_MESSAGE);
			btnCQTH.focus();
			validateSuccess = false;
			return;
		}
		if(dfNgayThiHanh.isEmpty())
		{
			Notification.show("Vui lòng chọn ngày thi hành quyết định",Type.WARNING_MESSAGE);
			dfNgayThiHanh.focus();
			validateSuccess = false;
			return;
		}	
		validateSuccess = true;
	}

	public boolean isValidateSuccess() {
		return validateSuccess;
	}
	public void setValidateSuccess(boolean validateSuccess) {
		this.validateSuccess = validateSuccess;
	}
}
