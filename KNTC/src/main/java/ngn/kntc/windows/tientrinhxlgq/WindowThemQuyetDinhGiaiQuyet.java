package ngn.kntc.windows.tientrinhxlgq;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ngn.kntc.beans.DanhMucBean;
import ngn.kntc.beans.DoiTuongBiXuLyHanhChinhBean;
import ngn.kntc.beans.QuaTrinhXuLyGiaiQuyetBean;
import ngn.kntc.beans.QuyetDinhGiaiQuyetBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.enums.HinhThucXuPhatHanhChinhEnum;
import ngn.kntc.enums.LoaiDonThuEnum;
import ngn.kntc.enums.LoaiKetQuaGiaiQuyetEnum;
import ngn.kntc.lienthongttcp.LienThongQuyetDinhGiaiQuyet;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;
import ngn.kntc.utils.SessionUtil;
import ngn.kntc.windows.WindowChonCoQuan;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class WindowThemQuyetDinhGiaiQuyet extends WindowTienTrinhXuLy{
	/* Nội dung quyết định */
	private Label lblNoiDungCaption = new Label();
	private HorizontalLayout hCQBH = new HorizontalLayout();
	private Label lblCQBH = new Label();
	private Button btnCQBH = new Button(FontAwesome.SEARCH);
	private DateField dfNgayBanHanh = new DateField("Ngày ban hành"+DonThuModule.requiredMark,new Date());
	private RichTextArea txtTomTatNoiDung = new RichTextArea("Tóm tắt nội dung"+DonThuModule.requiredMark);
	private ComboBox cmbLoaiKetQuaGiaiQuyet = new ComboBox("Loại kết quả"+DonThuModule.requiredMark);
	private ComboBox cmbHinhThucGiaiQuyet = new ComboBox("Hình thức giải quyết"+DonThuModule.requiredMark);
	private CheckBox cbQuyenKhoiKien = new CheckBox("Quyền khởi kiện");
	/* Bồi thường thiệt hại */
	private Label lblBoiThuongThietHaiCaption = new Label();
	private HorizontalLayout hThuHoi = new HorizontalLayout();
	private TextField txtThuHoiTien = new TextField("Tiền (vnđ)");
	private TextField txtThuHoiDatO = new TextField("Đất ở (m²)");
	private TextField txtThuHoiDatSX = new TextField("Đất sản xuất (m²)");
	private HorizontalLayout hTraLai = new HorizontalLayout();
	private TextField txtTraLaiTien = new TextField("Tiền (vnđ)");
	private TextField txtTraLaiDatO = new TextField("Đất ở (m²)");
	private HorizontalLayout hSoNguoi = new HorizontalLayout();
	private TextField txtTraLaiDatSX = new TextField("Đất sản xuất (m²)");
	private TextField txtSoNguoiHuong = new TextField("hưởng");
	private TextField txtXuPhatHanhChinh = new TextField("Xử phạt hành chính (vnđ)");
	private TextField txtSoNguoiDieuTra = new TextField("điều tra");
	private List<DoiTuongBiXuLyHanhChinhBean> listDoiTuongBiXLHC = new ArrayList<DoiTuongBiXuLyHanhChinhBean>();

	int countComponent = 0;
	Map<Integer, HorizontalLayout> map = new HashMap<Integer, HorizontalLayout>();

	private VerticalLayout vDoiTuongBiXLHC = new VerticalLayout();
	private VerticalLayout vCaNhanXuPhatHanhChinh = new VerticalLayout();
	private VerticalLayout vCoQuanXuPhatHanhChinh = new VerticalLayout();
	private Button btnAddCaNhan = new Button("Thêm cá nhân",FontAwesome.PLUS_SQUARE);
	private Button btnAddCoQuan = new Button("Thêm cơ quan / tổ chức",FontAwesome.PLUS_SQUARE);

	private QuyetDinhGiaiQuyetBean modelQDGQ = new QuyetDinhGiaiQuyetBean();
	String maCQBH = null;

	private DonThuServiceUtil svDonThu = new DonThuServiceUtil();
	private QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh = new QuaTrinhXuLyGiaiQuyetServiceUtil();
	private DanhMucServiceUtil svDanhMuc = new DanhMucServiceUtil();

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private LienThongQuyetDinhGiaiQuyet lienThongQDGQ = new LienThongQuyetDinhGiaiQuyet();

	private boolean validateSuccess = false;
	private int idDonThu;
	private int loaiDonThu;

	public WindowThemQuyetDinhGiaiQuyet(int idDonThu) {
		this.idDonThu = idDonThu;
		buildLayout();
		configComponent();
	}

	public WindowThemQuyetDinhGiaiQuyet(QuyetDinhGiaiQuyetBean quyetDinhGiaiQuyetInput,List<DoiTuongBiXuLyHanhChinhBean> doiTuongBiXuLyHanhChinhInput,int idDonThu) {
		this.idDonThu = idDonThu;
		buildLayout();
		configComponent();
		this.modelQDGQ=quyetDinhGiaiQuyetInput;
		if(modelQDGQ.getLinkFileDinhKem()!=null)
		{
			try {
				lblCQBH.setValue(DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.coquan.getName(),modelQDGQ.getMaCoQuanBanHanh()).getName());
				maCQBH = modelQDGQ.getMaCoQuanBanHanh();
				dfNgayBanHanh.setValue(modelQDGQ.getNgayBanHanh());
				attachFile.setFileNameNew(modelQDGQ.getLinkFileDinhKem());
				attachFile.setFileNameOld(modelQDGQ.getTenFileDinhKem());
				attachFile.showListAttachFiles();
				txtTomTatNoiDung.setValue(modelQDGQ.getTomTatNoiDung());
				cmbLoaiKetQuaGiaiQuyet.setValue(modelQDGQ.getLoaiKetQuaGiaiQuyet());
				cmbHinhThucGiaiQuyet.setValue(modelQDGQ.getHinhThucGiaiQuyet());
				cbQuyenKhoiKien.setValue(modelQDGQ.isQuyenKhoiKien());
				txtThuHoiTien.setValue(modelQDGQ.getThuHoiTien()!=0?String.valueOf(modelQDGQ.getThuHoiTien()):"");
				txtThuHoiDatO.setValue(modelQDGQ.getThuHoiDatO()!=0?String.valueOf(modelQDGQ.getThuHoiDatO()):"");
				txtThuHoiDatSX.setValue(modelQDGQ.getThuHoiDatSX()!=0?String.valueOf(modelQDGQ.getThuHoiDatSX()):"");
				txtTraLaiTien.setValue(modelQDGQ.getTraLaiTien()!=0?String.valueOf(modelQDGQ.getTraLaiTien()):"");
				txtTraLaiDatO.setValue(modelQDGQ.getTraLaiDatO()!=0?String.valueOf(modelQDGQ.getTraLaiDatO()):"");
				txtTraLaiDatSX.setValue(modelQDGQ.getTraLaiDatSX()!=0?String.valueOf(modelQDGQ.getTraLaiDatSX()):"");
				txtSoNguoiHuong.setValue(modelQDGQ.getSoNguoiDuocTraQuyenLoi()!=0?String.valueOf(modelQDGQ.getSoNguoiDuocTraQuyenLoi()):"");
				txtSoNguoiDieuTra.setValue(modelQDGQ.getSoNguoiChuyenDieuTra()!=0?String.valueOf(modelQDGQ.getSoNguoiChuyenDieuTra()):"");
				txtXuPhatHanhChinh.setValue(modelQDGQ.getXuPhatHanhChinh()!=0?String.valueOf(modelQDGQ.getXuPhatHanhChinh()):"");
				/*for(DoiTuongBiXuLyHanhChinhBean model : doiTuongBiXuLyHanhChinhInput)
				{
					HorizontalLayout hTemp = buildLayoutForDTXLHC();
					map.put(++countComponent, hTemp);
					vDoiTuongXuPhatHanhChinh.addComponent(map.get(countComponent));
					TextField tmp = (TextField)map.get(countComponent).getComponent(0);
					tmp.setValue(model.getTen());
					TextField tmp1 = (TextField)map.get(countComponent).getComponent(1);
					tmp1.setValue(model.getChucVu());
					TextField tmp2 = (TextField)map.get(countComponent).getComponent(2);
					tmp2.setValue(model.getHinhThucXuLy());
					Button btnXoa = (Button)map.get(countComponent).getComponent(3);
					btnXoa.addClickListener(new ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
							map.remove(countComponent);
							vDoiTuongXuPhatHanhChinh.removeComponent(hTemp);
						}
					});
				}*/
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void buildLayout() {
		try {
			this.loaiDonThu = svDonThu.getLoaiDonThu(idDonThu);
		} catch (Exception e) {
			e.printStackTrace();
		}

		super.buildLayout();
		frmLayout.addComponent(lblNoiDungCaption);
		frmLayout.addComponent(hCQBH);
		frmLayout.addComponent(dfNgayBanHanh);
		frmLayout.addComponent(txtTomTatNoiDung);
		frmLayout.addComponent(cmbLoaiKetQuaGiaiQuyet);
		frmLayout.addComponent(cmbHinhThucGiaiQuyet);
		frmLayout.addComponent(cbQuyenKhoiKien);
		frmLayout.addComponent(lblBoiThuongThietHaiCaption);
		frmLayout.addComponent(hThuHoi);
		frmLayout.addComponent(hTraLai);
		frmLayout.addComponent(hSoNguoi);
		frmLayout.addComponent(txtXuPhatHanhChinh);
		frmLayout.addComponent(vDoiTuongBiXLHC);
		frmLayout.addComponent(attachFile);

		attachFile.setNameFolderTMP(File.separator+idDonThu);

		hCQBH.setCaptionAsHtml(true);
		dfNgayBanHanh.setCaptionAsHtml(true);
		txtTomTatNoiDung.setCaptionAsHtml(true);
		cmbLoaiKetQuaGiaiQuyet.setCaptionAsHtml(true);
		cmbHinhThucGiaiQuyet.setCaptionAsHtml(true);

		attachFile.setCaption("Tệp đính kèm");

		frmLayout.setMargin(new MarginInfo(false,true,false,false));

		this.setCaption("Thêm quyết định giải quyết");
		//this.setWidth("750px");
		this.setSizeFull();

		buildNoiDung();
		buildBoiThuongThietHai();
	}

	private void buildNoiDung()
	{
		/* Caption */
		lblNoiDungCaption.setCaption("<b style='font-size: 15px !important;color: #f13838 !important;'>NỘI DUNG QUYẾT ĐỊNH:</b>");
		lblNoiDungCaption.setCaptionAsHtml(true);
		/* Cơ quan ban hành */
		hCQBH.addComponents(lblCQBH,btnCQBH);
		hCQBH.setSpacing(true);
		hCQBH.setCaption("Cơ quan ban hành"+DonThuModule.requiredMark);
		/* Nội dung */
		txtTomTatNoiDung.setWidth("100%");
		/* List hình thức  */
		List<DanhMucBean> listHinhThuc;
		try {
			listHinhThuc = svDanhMuc.getDanhMucList(DanhMucTypeEnum.hinhthucgiaiquyet.getName(), DanhMucTypeEnum.hinhthucgiaiquyet.getIdType());
			for(DanhMucBean model : listHinhThuc)
			{
				cmbHinhThucGiaiQuyet.addItem(model.getIntMa());
				cmbHinhThucGiaiQuyet.setItemCaption(model.getIntMa(), model.getName());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		cmbHinhThucGiaiQuyet.setNullSelectionAllowed(false);
		cmbHinhThucGiaiQuyet.setValue(1);
		/* List Loại kết quả */
		List<Integer> arrayLoaiKetQuaKhieuNai = new ArrayList<Integer>();
		arrayLoaiKetQuaKhieuNai.add(LoaiKetQuaGiaiQuyetEnum.khieunaidungmotphan.getType());
		arrayLoaiKetQuaKhieuNai.add(LoaiKetQuaGiaiQuyetEnum.khieunaidungtoanbo.getType());
		arrayLoaiKetQuaKhieuNai.add(LoaiKetQuaGiaiQuyetEnum.khieunaisaitoanbo.getType());

		List<Integer> arrayLoaiKetQuaToCao = new ArrayList<Integer>();
		arrayLoaiKetQuaToCao.add(LoaiKetQuaGiaiQuyetEnum.tocaodung.getType());
		arrayLoaiKetQuaToCao.add(LoaiKetQuaGiaiQuyetEnum.tocaodungmotphan.getType());
		arrayLoaiKetQuaToCao.add(LoaiKetQuaGiaiQuyetEnum.tocaosai.getType());
		
		List<DanhMucBean> listLoaiKetQua;
		try {
			listLoaiKetQua = svDanhMuc.getDanhMucList(DanhMucTypeEnum.loaiquyetdinh.getName(), DanhMucTypeEnum.loaiquyetdinh.getIdType());
			for(DanhMucBean model : listLoaiKetQua)
			{
				if(model.getIntMa()==LoaiKetQuaGiaiQuyetEnum.chuyencoquandieutra.getType())
				{
					cmbLoaiKetQuaGiaiQuyet.addItem(model.getIntMa());
					cmbLoaiKetQuaGiaiQuyet.setItemCaption(model.getIntMa(), model.getName());
					continue;
				}
				if((this.loaiDonThu == LoaiDonThuEnum.khieunai.getType() && arrayLoaiKetQuaKhieuNai.contains(Integer.valueOf(model.getIntMa()))) || (this.loaiDonThu == LoaiDonThuEnum.tocao.getType() && arrayLoaiKetQuaToCao.contains(Integer.valueOf(model.getIntMa()))))
				{
					System.out.println(loaiDonThu+"=="+model.getName());
					cmbLoaiKetQuaGiaiQuyet.addItem(model.getIntMa());
					cmbLoaiKetQuaGiaiQuyet.setItemCaption(model.getIntMa(), model.getName());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		cmbLoaiKetQuaGiaiQuyet.setNullSelectionAllowed(false);
		cmbLoaiKetQuaGiaiQuyet.setValue(1);
	}

	public void buildBoiThuongThietHai()
	{
		/* Caption */
		lblBoiThuongThietHaiCaption.setCaption("<b style='font-size: 15px !important;color: #f13838 !important;'>KẾT QUẢ GIẢI QUYẾT:</b>");
		lblBoiThuongThietHaiCaption.setCaptionAsHtml(true);

		vDoiTuongBiXLHC.addComponents(btnAddCaNhan,vCaNhanXuPhatHanhChinh,btnAddCoQuan,vCoQuanXuPhatHanhChinh);
		vDoiTuongBiXLHC.setCaption("Xử lý hành chính");
		btnAddCaNhan.addStyleName(ValoTheme.BUTTON_PRIMARY);
		btnAddCoQuan.addStyleName(ValoTheme.BUTTON_PRIMARY);

		vDoiTuongBiXLHC.setSpacing(true);

		hThuHoi.addComponents(txtThuHoiTien,txtThuHoiDatO,txtThuHoiDatSX);
		hThuHoi.setSpacing(true);
		hThuHoi.setCaption("Thu hồi");

		hTraLai.addComponents(txtTraLaiTien,txtTraLaiDatO,txtTraLaiDatSX);
		hTraLai.setSpacing(true);
		hTraLai.setCaption("Trả lại");

		hSoNguoi.addComponents(txtSoNguoiHuong,txtSoNguoiDieuTra);
		hSoNguoi.setSpacing(true);
		hSoNguoi.setCaption("Số người");
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
						modelQDGQ.setMaCoQuanBanHanh(maCQBH);
						modelQDGQ.setNgayBanHanh(dfNgayBanHanh.getValue());
						modelQDGQ.setTomTatNoiDung(txtTomTatNoiDung.getValue());
						modelQDGQ.setLoaiKetQuaGiaiQuyet((int)cmbLoaiKetQuaGiaiQuyet.getValue());
						modelQDGQ.setHinhThucGiaiQuyet((int)cmbHinhThucGiaiQuyet.getValue());
						modelQDGQ.setThuHoiTien((!txtThuHoiTien.isEmpty())?Integer.parseInt(txtThuHoiTien.getValue()):0);
						modelQDGQ.setThuHoiDatO(!txtThuHoiDatO.isEmpty()?Integer.parseInt(txtThuHoiDatO.getValue()):0);
						modelQDGQ.setThuHoiDatSX(!txtThuHoiDatSX.isEmpty()?Integer.parseInt(txtThuHoiDatSX.getValue()):0);
						modelQDGQ.setTraLaiTien(!txtTraLaiTien.isEmpty()?Integer.parseInt(txtTraLaiTien.getValue()):0);
						modelQDGQ.setTraLaiDatO(!txtTraLaiDatO.isEmpty()?Integer.parseInt(txtTraLaiDatO.getValue()):0);
						modelQDGQ.setTraLaiDatSX(!txtTraLaiDatSX.isEmpty()?Integer.parseInt(txtTraLaiDatSX.getValue()):0);
						modelQDGQ.setQuyenKhoiKien(cbQuyenKhoiKien.getValue());
						modelQDGQ.setSoNguoiDuocTraQuyenLoi(!txtSoNguoiHuong.isEmpty()?Integer.parseInt(txtSoNguoiHuong.getValue()):0);
						modelQDGQ.setSoNguoiChuyenDieuTra(!txtSoNguoiDieuTra.isEmpty()?Integer.parseInt(txtSoNguoiDieuTra.getValue()):0);
						modelQDGQ.setXuPhatHanhChinh(!txtXuPhatHanhChinh.isEmpty()?Integer.parseInt(txtXuPhatHanhChinh.getValue()):0);
						modelQDGQ.setTenFileDinhKem(attachFile.getFileNameOld());
						modelQDGQ.setLinkFileDinhKem(attachFile.getFileNameNew());
						modelQDGQ.setNgayTao(new Date());
						modelQDGQ.setIdDonThu(idDonThu);
						modelQDGQ.setOrgTao(SessionUtil.getOrgId());

						QuaTrinhXuLyGiaiQuyetBean model = new QuaTrinhXuLyGiaiQuyetBean();
						String strLoaiKetQua = DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.loaiquyetdinh.getName(), modelQDGQ.getLoaiKetQuaGiaiQuyet()).getName();
						String strHinhThucGiaiQuyet = DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.hinhthucgiaiquyet.getName(), modelQDGQ.getHinhThucGiaiQuyet()).getName();

						model.setNoiDung(svQuaTrinh.returnStringCanBoNhap(SessionUtil.getUserId(), "đã nhập quyết định giải quyết","<span style='color: #a2271e'>Loại kết quả:</span> "+strLoaiKetQua+" - "+" <span style='color: #a2271e'>Hình thức giải quyết:</span> "+strHinhThucGiaiQuyet+" - <span style='color: #a2271e'>Ngày ban hành:</span> "+sdf.format(modelQDGQ.getNgayBanHanh())));

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
						int idQuyetDinh = svQuaTrinh.insertQuyetDinhGiaiQuyet(modelQDGQ);

						DonThuModule.insertThongBao(idDonThu, idQuaTrinh);

						Iterator <Integer> itr = map.keySet().iterator();
						while (itr.hasNext()){
							DoiTuongBiXuLyHanhChinhBean modelBiXLHC = new DoiTuongBiXuLyHanhChinhBean();
							HorizontalLayout hTemp = map.get(itr.next());
							int componentCount = hTemp.getComponentCount();
							ComboBox cmbHinhThucXuLy = (ComboBox)hTemp.getComponent(componentCount-2);
							String hinhThucXuLy = cmbHinhThucXuLy.getItemCaption(cmbHinhThucXuLy.getValue());
							if(componentCount==4)
							{
								modelBiXLHC.setTenDoiTuong(((TextField)hTemp.getComponent(0)).getValue());
								modelBiXLHC.setChucVu(((TextField)hTemp.getComponent(1)).getValue());
								modelBiXLHC.setHinhThucXuLy(hinhThucXuLy);
								modelBiXLHC.setCaNhan(true);
							}
							else if(componentCount==3)
							{
								modelBiXLHC.setTenDoiTuong(((TextField)hTemp.getComponent(0)).getValue());
								modelBiXLHC.setHinhThucXuLy(hinhThucXuLy);
								modelBiXLHC.setCaNhan(false);
							}
							modelBiXLHC.setMaQuyetDinhGiaiQuyet(idQuyetDinh);
							if(modelBiXLHC.getTenDoiTuong()!="" && modelBiXLHC.getTenDoiTuong()!=null)
								svQuaTrinh.insertDoiTuongBiXLHC(modelBiXLHC);
						}
						modelQDGQ.setMaQuyetDinhGiaiQuyet(idQuyetDinh);
						//lienThongQDGQ.executeLienThong(modelQDGQ,idDonThu);
						new LienThongQuyetDinhGiaiQuyet().executeLienThong(idDonThu, SessionUtil.getOrgId());
						svDonThu.updateDonThuDate("NgayHoanThanhGiaiQuyet", modelQDGQ.getNgayBanHanh(), idDonThu);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		btnAddCaNhan.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				HorizontalLayout hTemp = buildLayoutCaNhanBiXuLy();
				map.put(++countComponent, hTemp);
				vCaNhanXuPhatHanhChinh.addComponent(map.get(countComponent));
				Button btnXoa = (Button)map.get(countComponent).getComponent(3);
				btnXoa.addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						map.remove(countComponent);
						vCaNhanXuPhatHanhChinh.removeComponent(hTemp);
					}
				});
			}
		});
		btnAddCoQuan.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				HorizontalLayout hTemp = buildLayoutCoQuanBiXuLy();
				map.put(++countComponent, hTemp);
				vCoQuanXuPhatHanhChinh.addComponent(map.get(countComponent));
				Button btnXoa = (Button)map.get(countComponent).getComponent(2);
				btnXoa.addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						map.remove(countComponent);
						vCoQuanXuPhatHanhChinh.removeComponent(hTemp);
					}
				});
			}
		});
		btnCQBH.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				WindowChonCoQuan wdCoQuan = new WindowChonCoQuan();
				UI.getCurrent().addWindow(wdCoQuan);
				wdCoQuan.getBtnOk().addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						maCQBH = wdCoQuan.getCoQuanID();
						try {
							lblCQBH.setValue(DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.coquan.getName(), maCQBH).getName());
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
	}
	public HorizontalLayout buildLayoutCaNhanBiXuLy()
	{
		HorizontalLayout hLayout = new HorizontalLayout();
		Button btnXoa = new Button(FontAwesome.MINUS);
		hLayout.addComponent(new TextField("Họ tên"));
		hLayout.addComponent(new TextField("Chức vụ"));
		hLayout.addComponent(returnComboboxXuPhatHanhChinh());
		hLayout.addComponent(btnXoa);
		hLayout.setSpacing(true);

		btnXoa.setCaption("");
		btnXoa.addStyleName(ValoTheme.BUTTON_DANGER);
		btnXoa.addStyleName(ValoTheme.BUTTON_SMALL);
		btnXoa.addStyleName("btn-donthu-form-remove-nguoibixlhc");

		return hLayout;
	}

	public HorizontalLayout buildLayoutCoQuanBiXuLy()
	{
		HorizontalLayout hLayout = new HorizontalLayout();
		Button btnXoa = new Button(FontAwesome.MINUS);
		hLayout.addComponent(new TextField("Tên cơ quan / tổ chức"));
		hLayout.addComponent(returnComboboxXuPhatHanhChinh());
		hLayout.addComponent(btnXoa);
		hLayout.setSpacing(true);

		btnXoa.setCaption("");
		btnXoa.addStyleName(ValoTheme.BUTTON_DANGER);
		btnXoa.addStyleName(ValoTheme.BUTTON_SMALL);
		btnXoa.addStyleName("btn-donthu-form-remove-nguoibixlhc");

		return hLayout;
	}

	public ComboBox returnComboboxXuPhatHanhChinh()
	{
		ComboBox cbXPHC = new ComboBox("Hình thức xử lý");
		cbXPHC.setNullSelectionAllowed(false);

		for(HinhThucXuPhatHanhChinhEnum e : HinhThucXuPhatHanhChinhEnum.values())
		{
			cbXPHC.addItem(e.getType());
			cbXPHC.setItemCaption(e.getType(), e.getName());
		}
		cbXPHC.select(1);

		return cbXPHC;
	}

	public void validateFormSubmit()
	{
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
			if(!txtSoNguoiDieuTra.isEmpty())
				Integer.parseInt(txtSoNguoiDieuTra.getValue());
			if(!txtSoNguoiHuong.isEmpty())
				Integer.parseInt(txtSoNguoiHuong.getValue());
		}catch(Exception e){
			Notification.show("Số người được hưởng và chuyển điều tra phải là kiểu số",Type.WARNING_MESSAGE);
			validateSuccess = false;
			return;
		}
		try{
			if(!txtXuPhatHanhChinh.isEmpty())
				Integer.parseInt(txtXuPhatHanhChinh.getValue());
		}catch(Exception e){
			Notification.show("Số tiền xử phạt hành chính phải là kiểu số",Type.WARNING_MESSAGE);
			validateSuccess = false;
			return;
		}
		if(maCQBH==null)
		{
			Notification.show("Vui lòng chọn cơ quan / tổ chức ban hành quyết định",Type.WARNING_MESSAGE);
			btnCQBH.focus();
			validateSuccess = false;
			return;
		}
		if(dfNgayBanHanh.isEmpty())
		{
			Notification.show("Vui lòng chọn ngày ban hành quyết định",Type.WARNING_MESSAGE);
			dfNgayBanHanh.focus();
			validateSuccess = false;
			return;
		}
		if(txtTomTatNoiDung.isEmpty())
		{
			Notification.show("Vui lòng nhập vào tóm tắt nội dung giải quyết",Type.WARNING_MESSAGE);
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

	public QuyetDinhGiaiQuyetBean getQuyetDinhGiaiQuyetBean() {
		return modelQDGQ;
	}

	public void setQuyetDinhGiaiQuyetBean(
			QuyetDinhGiaiQuyetBean quyetDinhGiaiQuyetBean) {
		this.modelQDGQ = quyetDinhGiaiQuyetBean;
	}

	public List<DoiTuongBiXuLyHanhChinhBean> getListDoiTuongBiXLHC() {
		return listDoiTuongBiXLHC;
	}

	public void setListDoiTuongBiXLHC(
			List<DoiTuongBiXuLyHanhChinhBean> listDoiTuongBiXLHC) {
		this.listDoiTuongBiXLHC = listDoiTuongBiXLHC;
	}
	public boolean isValidateSuccess() {
		return validateSuccess;
	}
	public void setValidateSuccess(boolean validateSuccess) {
		this.validateSuccess = validateSuccess;
	}
}
