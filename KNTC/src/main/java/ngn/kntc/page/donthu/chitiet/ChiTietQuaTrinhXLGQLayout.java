package ngn.kntc.page.donthu.chitiet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import ngn.kntc.beans.DonThuBean;
import ngn.kntc.beans.KetQuaXuLyBean;
import ngn.kntc.beans.QuaTrinhXuLyGiaiQuyetBean;
import ngn.kntc.beans.QuyetDinhGiaiQuyetBean;
import ngn.kntc.beans.QuyetDinhThuLyBean;
import ngn.kntc.beans.ThiHanhQuyetDinhBean;
import ngn.kntc.beans.ThongTinDonThuBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.enums.TinhTrangDonThu;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.QuaTrinhXuLyGiaiQuyetServiceUtil;
import ngn.kntc.utils.SessionUtil;
import ngn.kntc.utils.UploadServiceUtil;
import ngn.kntc.windows.WindowChuyenDonThu;
import ngn.kntc.windows.WindowEditorBieuMau;
import ngn.kntc.windows.WindowLapBieuMau;
import ngn.kntc.windows.WindowPhanCong;
import ngn.kntc.windows.WindowThemQuaTrinhXLGQ;
import ngn.kntc.windows.WindowTrinhLanhDao;
import ngn.kntc.windows.tientrinhxlgq.WindowShowKetQuaXuLy;
import ngn.kntc.windows.tientrinhxlgq.WindowShowQuyetDinhGiaiQuyet;
import ngn.kntc.windows.tientrinhxlgq.WindowShowQuyetDinhThuLy;
import ngn.kntc.windows.tientrinhxlgq.WindowShowThiHanhGiaiQuyet;
import ngn.kntc.windows.tientrinhxlgq.WindowThemKetQuaXuLy;
import ngn.kntc.windows.tientrinhxlgq.WindowThemKetQuaXuLyTmp;
import ngn.kntc.windows.tientrinhxlgq.WindowThemQuyetDinhGiaiQuyet;
import ngn.kntc.windows.tientrinhxlgq.WindowThemQuyetDinhThuLy;
import ngn.kntc.windows.tientrinhxlgq.WindowThemThiHanhGiaiQuyet;
import ngn.kntc.windows.tientrinhxlgq.WindowThemVanBan;
import ngn.kntc.windows.tientrinhxlgq.WindowViewVanBanXuLyGiaiQuyet;

import com.liferay.portal.service.UserLocalServiceUtil;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ChiTietQuaTrinhXLGQLayout extends VerticalLayout {
	private HorizontalLayout hMainButton = new HorizontalLayout();

	private Panel pnlButton = new Panel();
	private Button btnTaoBieuMau = new Button("Tạo biểu mẫu",FontAwesome.FILE_WORD_O);
	private Button btnThemKetQuaXuLy = new Button("Thêm kết quả xử lý",FontAwesome.PLUS);
	private Button btnThemQuyetDinhThuLy = new Button("Thêm quyết định thụ lý",FontAwesome.PLUS);
	private Button btnThemQuyetDinhGiaiQuyet = new Button("Thêm quyết định giải quyết",FontAwesome.PLUS);
	private Button btnThemThiHanhGiaiQuyet = new Button("Thêm kết quả thi hành giải quyết",FontAwesome.PLUS);
	private Button btnKetThucHoSo = new Button("Kết thúc hồ sơ",FontAwesome.CHAIN);
	private Button btnPhucTapKeoDai = new Button("Đánh dấu vụ việc phúc tạp, kéo dài",FontAwesome.BOOKMARK);
	private Button btnRutDon = new Button("Rút đơn",FontAwesome.CROP);
	
	private Button btnThemQuaTrinhXLGQ = new Button("Thêm quá trình xử lý, giải quyết",FontAwesome.PLUS);
	private Button btnThemVanBan = new Button("Thêm văn bản",FontAwesome.PLUS);
	private Button btnThamMuuGiaiQuyet = new Button("Tham mưu giải quyết",FontAwesome.USER_PLUS);
	private Button btnTrinhLanhDao = new Button("Trình lãnh đạo",FontAwesome.INFO);

	private VerticalLayout vTomTat = new VerticalLayout();
	private Button btnXemTomTatQuaTrinhHeThong = new Button("Xem");
	private Button btnXemTomTatQuaTrinhNguoiDung = new Button("Xem");
	private Button btnXemTomTatTongSoVanBan = new Button("Xem");
	private Button btnXemTomTatSoFileDinhKem = new Button("Xem");

	private HorizontalLayout hTienTrinhGiaiQuyet = new HorizontalLayout();
	private Button btnFlowKetQuaXuLy = new Button("Kết quả xử lý");
	private Button btnFlowQuyetDinhThuLy = new Button("Quyết định thụ lý");
	private Button btnFlowQuyetDinhGiaiQuyet = new Button("Quyết định giải quyết");
	private Button btnFlowKetQuaThiHanhQuyetDinh = new Button("Kết quả thi hành quyết định");

	private HorizontalLayout hMainTienTrinhFilter = new HorizontalLayout();

	private HorizontalLayout hMainTienTrinh = new HorizontalLayout();
	private HorizontalLayout hFilter = new HorizontalLayout();
	private ComboBox cmbFilterLoaiHienThi = new ComboBox();
	private ComboBox cmbFilterHienThiDinhKem = new ComboBox();

	private Table tblQuaTrinhXLGQ = new Table();
	private IndexedContainer container = new IndexedContainer();

	private final String STT = "STT";
	private final String NGUOICAPNHAT = "Người cập nhật";
	private final String NOIDUNG = "Nội dung";
	private final String NGAYCAPNHAT = "Ngày cập nhật";
	private final String DINHKEM = "Đính kèm";

	private QuaTrinhXuLyGiaiQuyetServiceUtil svQuaTrinh = new QuaTrinhXuLyGiaiQuyetServiceUtil();
	private DonThuServiceUtil svDonThu = new DonThuServiceUtil();

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	private int idDonThu;

	public ChiTietQuaTrinhXLGQLayout(int idDonThu) {
		this.idDonThu = idDonThu;
		try {
			buildLayout();
			configComponent();
			loadData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void buildLayout() throws Exception {
		this.addComponent(pnlButton);
		this.addComponent(vTomTat);
		this.addComponent(hMainTienTrinhFilter);
		this.addComponent(hTienTrinhGiaiQuyet);
		this.addComponent(tblQuaTrinhXLGQ);

		//this.setComponentAlignment(hMainButton, Alignment.MIDDLE_RIGHT);
		//this.setComponentAlignment(hFilter, Alignment.MIDDLE_RIGHT);

		this.setWidth("100%");
		this.setSpacing(true);
		this.setMargin(true);
		
		buildTomTat();
		buildTable();
		buildLayoutMainButton();
		buildTienTrinhGiaiQuyet();
		buildLayoutMainTienTrinhFilter();
		loadTienTrinhFlowDisplay();
		loadFunctionPermission();
	}

	private void configComponent() {
		btnTaoBieuMau.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				WindowLapBieuMau wdBieuMau = new WindowLapBieuMau(idDonThu);
				UI.getCurrent().addWindow(wdBieuMau);
			}
		});

		btnThemQuaTrinhXLGQ.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				WindowThemQuaTrinhXLGQ wdThemQuaTrinh = new WindowThemQuaTrinhXLGQ(idDonThu);
				UI.getCurrent().addWindow(wdThemQuaTrinh);

				wdThemQuaTrinh.getBtnSubmit().addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						if(wdThemQuaTrinh.isValidateSucccess())
						{
							wdThemQuaTrinh.close();
							try {
								loadData();
								buildTomTat();
								Notification.show("Thêm thành công quá trình xử lý, giải quyết",Type.TRAY_NOTIFICATION);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				});
			}
		});

		btnThemKetQuaXuLy.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				//WindowThemKetQuaXuLy wdKQXL = new WindowThemKetQuaXuLy(idDonThu);
				
				WindowThemKetQuaXuLyTmp wdKQXL = new WindowThemKetQuaXuLyTmp(idDonThu);
				
				UI.getCurrent().addWindow(wdKQXL);

				wdKQXL.getLayoutSubmit().getBtnSave().addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						if(wdKQXL.isValidateSuccess())
						{
							wdKQXL.close();
							try {
								buildTomTat();
								loadData();
								loadTienTrinhFlowDisplay();
								loadFunctionPermission();
								Notification.show("Thêm thành công kết quả xử lý",Type.TRAY_NOTIFICATION);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				});
			}
		});

		btnThemQuyetDinhThuLy.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				WindowThemQuyetDinhThuLy wdQDTL = new WindowThemQuyetDinhThuLy(idDonThu);
				UI.getCurrent().addWindow(wdQDTL);

				wdQDTL.getBtnOk().addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						if(wdQDTL.isValidateSuccess())
						{
							wdQDTL.close();

							try {
								buildTomTat();
								loadData();
								loadTienTrinhFlowDisplay();
								loadFunctionPermission();
								Notification.show("Thêm thành công quyết định thụ lý",Type.TRAY_NOTIFICATION);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				});
			}
		});

		btnThemQuyetDinhGiaiQuyet.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				WindowThemQuyetDinhGiaiQuyet wdQDGQ = new WindowThemQuyetDinhGiaiQuyet(idDonThu);
				UI.getCurrent().addWindow(wdQDGQ);

				wdQDGQ.getBtnOk().addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						if(wdQDGQ.isValidateSuccess())
						{
							wdQDGQ.close();

							try {
								buildTomTat();
								loadData();
								loadTienTrinhFlowDisplay();
								loadFunctionPermission();
								Notification.show("Thêm thành công quyết định giải quyết, giải quyết",Type.TRAY_NOTIFICATION);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				});
			}
		});

		btnThemThiHanhGiaiQuyet.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				WindowThemThiHanhGiaiQuyet wdTHGQ = new WindowThemThiHanhGiaiQuyet(idDonThu);
				UI.getCurrent().addWindow(wdTHGQ);

				wdTHGQ.getBtnOk().addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						if(wdTHGQ.isValidateSuccess())
						{
							wdTHGQ.close();

							try {
								buildTomTat();
								loadData();
								loadTienTrinhFlowDisplay();
								loadFunctionPermission();
								Notification.show("Thêm thành công một kết quả thi hành quyết định",Type.TRAY_NOTIFICATION);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				});
			}
		});

		btnKetThucHoSo.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog.show(UI.getCurrent(),"Thông báo","Bạn muốn kết thúc hồ sơ này?","Đồng ý","Hủy",new ConfirmDialog.Listener() {

					@Override
					public void onClose(ConfirmDialog dialog) {
						if(dialog.isConfirmed())
						{
							try {
								svDonThu.updateDonThuStatus(TinhTrangDonThu.ketthuchoso.getType(), idDonThu);

								QuaTrinhXuLyGiaiQuyetBean model = new QuaTrinhXuLyGiaiQuyetBean();

								model.setNoiDung(svQuaTrinh.returnStringCanBoNhap(SessionUtil.getUserId(), "đã kết thúc hồ sơ",null));

								model.setUserNhap(SessionUtil.getUserId());
								model.setNgayDang(new Date());
								model.setMaDonThu(idDonThu);
								model.setHeThongTao(true);

								int idQuaTrinh = svQuaTrinh.insertQuaTrinhXLGQ(model);
								DonThuModule.insertThongBao(idDonThu, idQuaTrinh);

								loadFunctionPermission();
								loadData();
								buildTomTat();

								Notification.show("Đã kết thúc hồ sơ thành công",Type.TRAY_NOTIFICATION);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}						
					}
				});
			}
		});
		
		btnRutDon.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog.show(UI.getCurrent(),"Thông báo","Bạn muốn rút đơn này?","Đồng ý","Hủy",new ConfirmDialog.Listener() {

					@Override
					public void onClose(ConfirmDialog dialog) {
						if(dialog.isConfirmed())
						{
							try {
								svDonThu.updateDonThuStatus(TinhTrangDonThu.rutdon.getType(), idDonThu);

								QuaTrinhXuLyGiaiQuyetBean model = new QuaTrinhXuLyGiaiQuyetBean();

								model.setNoiDung(svQuaTrinh.returnStringCanBoNhap(SessionUtil.getUserId(), "đã xác nhận rút đơn",null));

								model.setUserNhap(SessionUtil.getUserId());
								model.setNgayDang(new Date());
								model.setMaDonThu(idDonThu);
								model.setHeThongTao(true);

								int idQuaTrinh = svQuaTrinh.insertQuaTrinhXLGQ(model);
								DonThuModule.insertThongBao(idDonThu, idQuaTrinh);

								loadFunctionPermission();
								loadData();
								buildTomTat();

								Notification.show("Đã rút đơn thành công, đơn được quản lý trong mục rút đơn",Type.TRAY_NOTIFICATION);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}						
					}
				});
			}
		});

		btnPhucTapKeoDai.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog.show(UI.getCurrent(),"Thông báo","Bạn muốn đánh dấu đơn này có vụ việc kéo dài?","Đồng ý","Hủy",new ConfirmDialog.Listener() {

					@Override
					public void onClose(ConfirmDialog dialog) {
						if(dialog.isConfirmed())
						{
							try {
								svDonThu.updateDonThuStatus(TinhTrangDonThu.vuviecphuctap.getType(), idDonThu);

								QuaTrinhXuLyGiaiQuyetBean model = new QuaTrinhXuLyGiaiQuyetBean();

								model.setNoiDung(svQuaTrinh.returnStringCanBoNhap(SessionUtil.getUserId(), "đã đánh dấu đơn thư thành vụ việc phức tạp kéo dài",null));

								model.setUserNhap(SessionUtil.getUserId());
								model.setNgayDang(new Date());
								model.setMaDonThu(idDonThu);
								model.setHeThongTao(true);

								int idQuaTrinh = svQuaTrinh.insertQuaTrinhXLGQ(model);
								DonThuModule.insertThongBao(idDonThu, idQuaTrinh);

								loadFunctionPermission();
								loadData();
								buildTomTat();

								Notification.show("Đã đánh dấu vụ việc phức tạp kéo dài, đơn được quản lý trong mục đơn phức tạp kéo dài",Type.TRAY_NOTIFICATION);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				});
			}
		});

		btnThemVanBan.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				WindowThemVanBan wdThemVanBan = new WindowThemVanBan(idDonThu);
				UI.getCurrent().addWindow(wdThemVanBan);

				wdThemVanBan.getLayoutSubmit().getBtnSave().addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						if(wdThemVanBan.isValidateSuccess())
						{
							try {
								buildTomTat();
								loadData();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				});
			}
		});

		cmbFilterHienThiDinhKem.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				try {
					loadData();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		cmbFilterLoaiHienThi.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				try {
					loadData();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		btnThamMuuGiaiQuyet.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				WindowPhanCong wdPhanCong = new WindowPhanCong(idDonThu);
				UI.getCurrent().addWindow(wdPhanCong);

				wdPhanCong.getBtnOk().addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						try {
							buildTomTat();
							loadData();
							loadFunctionPermission();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});

		btnTrinhLanhDao.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				WindowTrinhLanhDao wdTrinhLanhDao = new WindowTrinhLanhDao(idDonThu);
				UI.getCurrent().addWindow(wdTrinhLanhDao);

				wdTrinhLanhDao.getBtnOk().addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						try {
							buildTomTat();
							loadData();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		btnFlowKetQuaXuLy.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().addWindow(new WindowShowKetQuaXuLy(idDonThu));
			}
		});

		btnFlowQuyetDinhThuLy.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().addWindow(new WindowShowQuyetDinhThuLy(idDonThu));
			}
		});

		btnFlowQuyetDinhGiaiQuyet.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().addWindow(new WindowShowQuyetDinhGiaiQuyet(idDonThu));
			}
		});
		btnFlowKetQuaThiHanhQuyetDinh.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					int maQuyetDinh = svQuaTrinh.getQuyetDinhGiaiQuyet(idDonThu).getMaQuyetDinhGiaiQuyet();
					UI.getCurrent().addWindow(new WindowShowThiHanhGiaiQuyet(maQuyetDinh));
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});

		btnXemTomTatQuaTrinhHeThong.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				cmbFilterHienThiDinhKem.select(1);
				cmbFilterLoaiHienThi.select(2);
			}
		});

		btnXemTomTatQuaTrinhNguoiDung.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				cmbFilterHienThiDinhKem.select(1);
				cmbFilterLoaiHienThi.select(3);
			}
		});

		btnXemTomTatSoFileDinhKem.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				cmbFilterHienThiDinhKem.select(2);
				cmbFilterLoaiHienThi.select(1);
			}
		});

		btnXemTomTatTongSoVanBan.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				WindowViewVanBanXuLyGiaiQuyet wdViewVanBan = new WindowViewVanBanXuLyGiaiQuyet(idDonThu);

				UI.getCurrent().addWindow(wdViewVanBan);
			}
		});
	}

	private void buildTable()
	{
		tblQuaTrinhXLGQ.setPageLength(tblQuaTrinhXLGQ.getPageLength());
		tblQuaTrinhXLGQ.setWidth("100%");
		tblQuaTrinhXLGQ.setImmediate(true);
		tblQuaTrinhXLGQ.addStyleName("table-vanban");

		tblQuaTrinhXLGQ.setColumnAlignment(STT, Align.CENTER);
		tblQuaTrinhXLGQ.setColumnAlignment(NGUOICAPNHAT, Align.CENTER);
		tblQuaTrinhXLGQ.setColumnAlignment(NGAYCAPNHAT, Align.CENTER);
		
		tblQuaTrinhXLGQ.setColumnWidth(DINHKEM, 150);

		container.addContainerProperty(STT, Integer.class, null);
		container.addContainerProperty(NGUOICAPNHAT, String.class, null);
		container.addContainerProperty(NOIDUNG, Label.class, null);
		container.addContainerProperty(NGAYCAPNHAT, String.class, null);
		container.addContainerProperty(DINHKEM, Button.class, null);
	}

	private void buildTomTat() throws SQLException
	{
		vTomTat.removeAllComponents();
		String styleLabel = "color:#F44336";

		Label lblTomTatQuaTrinhHeThong = new Label("+ Số quá trình xử lý, giải quyết của hệ thống:  <b style='"+styleLabel+"'>"+svQuaTrinh.countQuaTrinhXuLyGiaiQuyet(1,idDonThu)+"</b>",ContentMode.HTML);
		HorizontalLayout hQuaTrinhHeThong = new HorizontalLayout(lblTomTatQuaTrinhHeThong,btnXemTomTatQuaTrinhHeThong);

		Label lblTomTatQuaTrinhNguoiDung = new Label("+ Số quá trình xử lý, giải quyết của người dùng:  <b style='"+styleLabel+"'>"+svQuaTrinh.countQuaTrinhXuLyGiaiQuyet(2,idDonThu)+"</b>",ContentMode.HTML);
		HorizontalLayout hQuaTrinhNguoiDung = new HorizontalLayout(lblTomTatQuaTrinhNguoiDung,btnXemTomTatQuaTrinhNguoiDung);

		Label lblTomTatTongSoVanBan = new Label("+ Tổng số văn bản:  <b style='"+styleLabel+"'>"+svQuaTrinh.countVanBanXuLyGiaiQuyetOfDonThu(idDonThu)+"</b>",ContentMode.HTML);
		HorizontalLayout hQuaTrinhTongSoVanBan = new HorizontalLayout(lblTomTatTongSoVanBan,btnXemTomTatTongSoVanBan);

		Label lblTomTatSoFileDinhKem = new Label("+ Số file đính kèm:  <b style='"+styleLabel+"'>"+svQuaTrinh.countQuaTrinhXuLyGiaiQuyet(3,idDonThu)+"</b>",ContentMode.HTML);
		HorizontalLayout hSoTepDinhKem = new HorizontalLayout(lblTomTatSoFileDinhKem,btnXemTomTatSoFileDinhKem);

		btnXemTomTatQuaTrinhHeThong.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		btnXemTomTatQuaTrinhHeThong.addStyleName("btn-donthu-xem-tomtat");
		btnXemTomTatQuaTrinhNguoiDung.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		btnXemTomTatQuaTrinhNguoiDung.addStyleName("btn-donthu-xem-tomtat");
		btnXemTomTatSoFileDinhKem.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		btnXemTomTatSoFileDinhKem.addStyleName("btn-donthu-xem-tomtat");
		btnXemTomTatTongSoVanBan.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		btnXemTomTatTongSoVanBan.addStyleName("btn-donthu-xem-tomtat");

		vTomTat.addComponent(new Label("<b style='font-size: 18px; color: #ff1717;'>Tóm tắt</b>",ContentMode.HTML));
		vTomTat.addComponent(new Label("<b style='font-size: 16px; color: #295ef7;'>Tổng số quá trình xử lý, giải quyết:</b> <b style='"+styleLabel+"'>"+svQuaTrinh.countQuaTrinhXuLyGiaiQuyet(0,idDonThu)+"</b>",ContentMode.HTML));
		vTomTat.addComponent(hQuaTrinhHeThong);
		vTomTat.addComponent(hQuaTrinhNguoiDung);
		vTomTat.addComponent(hSoTepDinhKem);
		vTomTat.addComponent(hQuaTrinhTongSoVanBan);

		hQuaTrinhHeThong.setSpacing(true);
		hQuaTrinhNguoiDung.setSpacing(true);
		hSoTepDinhKem.setSpacing(true);
		hQuaTrinhTongSoVanBan.setSpacing(true);

		vTomTat.addStyleName("v-donthu-quatrinhxlgq-tomtat");
	}

	private void buildLayoutMainButton()
	{
		hMainButton.addComponents(btnTaoBieuMau,btnThemQuaTrinhXLGQ,btnThemVanBan,btnThamMuuGiaiQuyet,btnTrinhLanhDao);

		btnTaoBieuMau.addStyleName("btn-donthu-thongtinquatrinhxlgq-mainbutton");
		btnThemQuaTrinhXLGQ.addStyleName("btn-donthu-thongtinquatrinhxlgq-mainbutton");
		btnThemVanBan.addStyleName("btn-donthu-thongtinquatrinhxlgq-mainbutton");
		btnThamMuuGiaiQuyet.addStyleName("btn-donthu-thongtinquatrinhxlgq-mainbutton");
		btnTrinhLanhDao.addStyleName("btn-donthu-thongtinquatrinhxlgq-mainbutton");

		btnTaoBieuMau.addStyleName("btn-donthu-thongtinquatrinhxlgq-mainbutton-chuyen");
		btnThemQuaTrinhXLGQ.addStyleName("btn-donthu-thongtinquatrinhxlgq-mainbutton-themqtxlgq");
		btnThemVanBan.addStyleName("btn-donthu-thongtinquatrinhxlgq-mainbutton-themqtxlgq");
		btnThamMuuGiaiQuyet.addStyleName("btn-donthu-thongtinquatrinhxlgq-mainbutton-phancong");
		btnTrinhLanhDao.addStyleName("btn-donthu-thongtinquatrinhxlgq-mainbutton-trinhlanhdao");

		btnTaoBieuMau.addStyleName(ValoTheme.BUTTON_SMALL);
		btnThemQuaTrinhXLGQ.addStyleName(ValoTheme.BUTTON_SMALL);
		btnThemVanBan.addStyleName(ValoTheme.BUTTON_SMALL);
		btnTrinhLanhDao.addStyleName(ValoTheme.BUTTON_SMALL);
		btnThamMuuGiaiQuyet.addStyleName(ValoTheme.BUTTON_SMALL);
		
		hMainButton.setSpacing(true);
		hMainButton.setMargin(true);

		pnlButton.setContent(hMainButton);
		pnlButton.setVisible(false);
	}

	private void buildTienTrinhGiaiQuyet() {
		hTienTrinhGiaiQuyet.addComponents(btnFlowKetQuaXuLy,btnFlowQuyetDinhThuLy,btnFlowQuyetDinhGiaiQuyet,btnFlowKetQuaThiHanhQuyetDinh);

		btnFlowKetQuaXuLy.setWidth("100%");
		btnFlowQuyetDinhThuLy.setWidth("100%");
		btnFlowQuyetDinhGiaiQuyet.setWidth("100%");
		btnFlowKetQuaThiHanhQuyetDinh.setWidth("100%");

		btnFlowKetQuaXuLy.addStyleName("btn-flow-giaiquyet");
		btnFlowQuyetDinhThuLy.addStyleName("btn-flow-giaiquyet");
		btnFlowQuyetDinhGiaiQuyet.addStyleName("btn-flow-giaiquyet");
		btnFlowKetQuaThiHanhQuyetDinh.addStyleName("btn-flow-giaiquyet");

		btnFlowKetQuaXuLy.setEnabled(false);
		btnFlowQuyetDinhThuLy.setEnabled(false);
		btnFlowQuyetDinhGiaiQuyet.setEnabled(false);
		btnFlowKetQuaThiHanhQuyetDinh.setEnabled(false);
		
		btnFlowKetQuaXuLy.addStyleName(ValoTheme.BUTTON_SMALL);
		btnFlowQuyetDinhThuLy.addStyleName(ValoTheme.BUTTON_SMALL);
		btnFlowQuyetDinhGiaiQuyet.addStyleName(ValoTheme.BUTTON_SMALL);
		btnFlowKetQuaThiHanhQuyetDinh.addStyleName(ValoTheme.BUTTON_SMALL);

		hTienTrinhGiaiQuyet.setSpacing(true);
		hTienTrinhGiaiQuyet.setWidth("100%");
	}

	private void buildLayoutMainTienTrinhFilter()
	{
		hMainTienTrinhFilter.addComponents(hMainTienTrinh,hFilter);

		hMainTienTrinh.addComponents(btnThemKetQuaXuLy,btnThemQuyetDinhThuLy,btnThemQuyetDinhGiaiQuyet,btnThemThiHanhGiaiQuyet,btnPhucTapKeoDai,btnKetThucHoSo,btnRutDon);

		btnThemKetQuaXuLy.addStyleName("btn-donthu-thongtinquatrinhxlgq-mainbutton");
		btnThemQuyetDinhThuLy.addStyleName("btn-donthu-thongtinquatrinhxlgq-mainbutton");
		btnThemQuyetDinhGiaiQuyet.addStyleName("btn-donthu-thongtinquatrinhxlgq-mainbutton");
		btnThemThiHanhGiaiQuyet.addStyleName("btn-donthu-thongtinquatrinhxlgq-mainbutton");
		btnKetThucHoSo.addStyleName("btn-donthu-thongtinquatrinhxlgq-mainbutton");
		btnPhucTapKeoDai.addStyleName("btn-donthu-thongtinquatrinhxlgq-mainbutton");
		btnRutDon.addStyleName("btn-donthu-thongtinquatrinhxlgq-mainbutton");

		btnThemKetQuaXuLy.addStyleName("btn-donthu-thongtinquatrinhxlgq-mainbutton-tientrinh");
		btnThemQuyetDinhThuLy.addStyleName("btn-donthu-thongtinquatrinhxlgq-mainbutton-tientrinh");
		btnThemQuyetDinhGiaiQuyet.addStyleName("btn-donthu-thongtinquatrinhxlgq-mainbutton-tientrinh");
		btnThemThiHanhGiaiQuyet.addStyleName("btn-donthu-thongtinquatrinhxlgq-mainbutton-tientrinh");
		btnKetThucHoSo.addStyleName("btn-donthu-thongtinquatrinhxlgq-mainbutton-tientrinh-ketthuc");
		btnPhucTapKeoDai.addStyleName("btn-donthu-thongtinquatrinhxlgq-mainbutton-tientrinh-ketthuc");
		btnRutDon.addStyleName("btn-donthu-thongtinquatrinhxlgq-mainbutton-tientrinh-rutdon");
		
		btnThemKetQuaXuLy.addStyleName(ValoTheme.BUTTON_SMALL);
		btnThemQuyetDinhThuLy.addStyleName(ValoTheme.BUTTON_SMALL);
		btnThemQuyetDinhGiaiQuyet.addStyleName(ValoTheme.BUTTON_SMALL);
		btnThemThiHanhGiaiQuyet.addStyleName(ValoTheme.BUTTON_SMALL);
		btnKetThucHoSo.addStyleName(ValoTheme.BUTTON_SMALL);
		btnPhucTapKeoDai.addStyleName(ValoTheme.BUTTON_SMALL);
		btnRutDon.addStyleName(ValoTheme.BUTTON_SMALL);
		
		hMainTienTrinh.setSpacing(true);

		hFilter.addComponents(new Label("<b style='font-size: 13px'>Bộ lọc: </b>",ContentMode.HTML),cmbFilterLoaiHienThi,cmbFilterHienThiDinhKem);

		hFilter.setSpacing(true);
		hFilter.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);

		cmbFilterHienThiDinhKem.setNullSelectionAllowed(false);
		cmbFilterLoaiHienThi.setNullSelectionAllowed(false);

		cmbFilterLoaiHienThi.addItem(1);
		cmbFilterLoaiHienThi.setItemCaption(1, "Tất cả");
		cmbFilterLoaiHienThi.addItem(2);
		cmbFilterLoaiHienThi.setItemCaption(2, "Hệ thống");
		cmbFilterLoaiHienThi.addItem(3);
		cmbFilterLoaiHienThi.setItemCaption(3, "Người dùng");
		cmbFilterLoaiHienThi.select(1);

		cmbFilterHienThiDinhKem.addItem(1);
		cmbFilterHienThiDinhKem.setItemCaption(1, "Tất cả");
		cmbFilterHienThiDinhKem.addItem(2);
		cmbFilterHienThiDinhKem.setItemCaption(2, "Có đính kèm");
		cmbFilterHienThiDinhKem.addItem(3);
		cmbFilterHienThiDinhKem.setItemCaption(3, "Không có đính kèm");
		cmbFilterHienThiDinhKem.select(1);
		
		cmbFilterLoaiHienThi.addStyleName(ValoTheme.COMBOBOX_SMALL);
		cmbFilterHienThiDinhKem.addStyleName(ValoTheme.COMBOBOX_SMALL);

		hMainTienTrinhFilter.setComponentAlignment(hFilter, Alignment.MIDDLE_RIGHT);

		hMainTienTrinhFilter.setWidth("100%");
	}

	private void loadFunctionPermission() throws Exception
	{
		btnRutDon.setVisible(false);
		btnPhucTapKeoDai.setVisible(false);
		btnThemVanBan.setVisible(false);
		btnThemKetQuaXuLy.setVisible(false);
		btnThemQuyetDinhThuLy.setVisible(false);
		btnThemQuyetDinhGiaiQuyet.setVisible(false);
		btnThemThiHanhGiaiQuyet.setVisible(false);
		btnKetThucHoSo.setVisible(false);
		btnThemQuaTrinhXLGQ.setVisible(false);
		btnThamMuuGiaiQuyet.setVisible(false);
		btnTrinhLanhDao.setVisible(false);
		DonThuBean model = svDonThu.getDonThu(idDonThu);

		if(model.getStatus()==TinhTrangDonThu.ketthuchoso.getType())
		{
			pnlButton.setVisible(true);
			btnTaoBieuMau.setVisible(true);
			btnPhucTapKeoDai.setVisible(true);
			btnThemThiHanhGiaiQuyet.setVisible(false);

			btnPhucTapKeoDai.setCaption("Đánh dấu vụ việc phúc tạp, kéo dài");
			btnPhucTapKeoDai.setIcon(FontAwesome.BOOKMARK);
			btnPhucTapKeoDai.setEnabled(true);
		}
		else if(model.getStatus()==TinhTrangDonThu.rutdon.getType())
		{
			hMainTienTrinh.setVisible(false);
		}
		else
		{
			if(!model.isDonKhongDuDieuKienXuLy())
			{
				if(!SessionUtil.isLanhDaoDonVi() && !SessionUtil.isTruongPhong())
				{
					btnTrinhLanhDao.setVisible(true);
				}
				else
				{
					btnThemQuaTrinhXLGQ.setCaption("Thêm ý kiến lãnh đạo");
				}
					
				pnlButton.setVisible(true);
				btnThemQuaTrinhXLGQ.setVisible(true);
				btnThemVanBan.setVisible(true);
				btnThemQuaTrinhXLGQ.setVisible(true);
				KetQuaXuLyBean modelKQXL = svQuaTrinh.getKetQuaXuLy(idDonThu, SessionUtil.getOrgId());
				if(modelKQXL != null)
				{
					/* Thụ lý */
					if(modelKQXL.getMaHuongXuLy()==4)
					{
						QuyetDinhThuLyBean modelQDTL = svQuaTrinh.getQuyetDinhThuLy(idDonThu);
						if(modelQDTL != null)
						{
							btnThamMuuGiaiQuyet.setVisible(true);
							QuyetDinhGiaiQuyetBean modelQDGQ = svQuaTrinh.getQuyetDinhGiaiQuyet(idDonThu);
							if(modelQDGQ != null)
							{
								if(model.getStatus()!=TinhTrangDonThu.vuviecphuctap.getType())
								{
									btnThemThiHanhGiaiQuyet.setVisible(true);
								}
								btnKetThucHoSo.setVisible(true);
							}
							else
							{
								btnThemQuyetDinhGiaiQuyet.setVisible(true);
								btnKetThucHoSo.setVisible(false);
							}
						}
						else
						{
							btnThemQuyetDinhThuLy.setVisible(true);
						}
					}
				}
				else
				{
					btnThemKetQuaXuLy.setVisible(true);
				}
			}
			if(!svQuaTrinh.checkIfDonViDaChuyenDonThu(idDonThu, SessionUtil.getOrgId()))
			{
				btnRutDon.setVisible(true);
			}
		}
		if(model.getStatus()==TinhTrangDonThu.vuviecphuctap.getType())
		{
			btnPhucTapKeoDai.setVisible(true);
			btnPhucTapKeoDai.setCaption("Đã đánh dấu vụ việc phức tạp, kéo dài");
			btnPhucTapKeoDai.setIcon(FontAwesome.CHECK);
			btnPhucTapKeoDai.setEnabled(false);
		}
		
		ThongTinDonThuBean modelThongTinDonTmp = svDonThu.getThongTinDonThu(idDonThu, SessionUtil.getOrgId());
		if(modelThongTinDonTmp.getMaThongTinDon()==0)
			hMainTienTrinh.setVisible(false);
		
		if(svQuaTrinh.isLanhDaoCuaCoQuanThamMuu(idDonThu))
		{
			btnThamMuuGiaiQuyet.setVisible(true);
		}
	}

	@SuppressWarnings("unchecked")
	private void loadData() throws Exception
	{
		List<QuaTrinhXuLyGiaiQuyetBean> listQuaTrinh = svQuaTrinh.getQuaTrinhXLGQList(idDonThu,(int)cmbFilterLoaiHienThi.getValue(),(int)cmbFilterHienThiDinhKem.getValue());

		int i = 0;
		container.removeAllItems();
		for(QuaTrinhXuLyGiaiQuyetBean model : listQuaTrinh)
		{
			Button btnDownload = new Button();

			Item item = container.addItem(++i);
			item.getItemProperty(STT).setValue(i);
			item.getItemProperty(NGUOICAPNHAT).setValue(UserLocalServiceUtil.getUser(model.getUserNhap()).getFirstName());
			item.getItemProperty(NOIDUNG).setValue(new Label(model.getNoiDung(),ContentMode.HTML));
			item.getItemProperty(NGAYCAPNHAT).setValue(sdf.format(model.getNgayDang()));

			if(model.getTenFileDinhKem()!=null)
			{
				btnDownload = new Button("",FontAwesome.DOWNLOAD);
				btnDownload.setCaption(model.getTenFileDinhKem());
				btnDownload.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
				Resource source = new StreamResource(new StreamSource() {

					@Override
					public InputStream getStream() {
						String directory = UploadServiceUtil.getAbsolutePath()+File.separator+UploadServiceUtil.getFolderNameDonThu()+File.separator+idDonThu+File.separator+model.getLinkFileDinhKem();
						File file = new File(directory);
						try {
							return new FileInputStream(file);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
							return null;
						}
					}
				}, model.getTenFileDinhKem());
				btnDownload.setCaption(model.getTenFileDinhKem());
				btnDownload.setDescription(model.getTenFileDinhKem());

				String fileExt = "";
				int lastIndexOf = model.getTenFileDinhKem().lastIndexOf(".");
				if (lastIndexOf == -1) {
					fileExt = ""; // empty extension
				}
				fileExt = model.getTenFileDinhKem().substring(lastIndexOf+1);
				System.out.println(fileExt+"------");

				if(fileExt.equalsIgnoreCase("pdf"))
				{
					btnDownload.addClickListener(new ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
							Window window = new Window();
							window.setWidth("90%");
							window.setHeight("90%");
							window.setCaption(model.getTenFileDinhKem());
							BrowserFrame e = new BrowserFrame("PDF File",source);
							//BrowserFrame e = new BrowserFrame("PDF File",new ExternalResource("http://demo.vaadin.com/sampler/"));
							e.setWidth("100%");
							e.setHeight("100%");
							window.setContent(e);
							window.center();
							window.setModal(true);
							UI.getCurrent().addWindow(window);	
						}
					});
				}
				else
				{
					FileDownloader downloader = new FileDownloader(source);
					downloader.extend(btnDownload);
				}

				item.getItemProperty(DINHKEM).setValue(btnDownload);
			}
		}
		tblQuaTrinhXLGQ.setContainerDataSource(container);
	}

	public void loadTienTrinhFlowDisplay() throws SQLException
	{
		List<KetQuaXuLyBean> listKQXL = svQuaTrinh.getKetQuaXuLyList(idDonThu);
		if(!listKQXL.isEmpty())
		{
			btnFlowKetQuaXuLy.setEnabled(true);
			btnFlowKetQuaXuLy.addStyleName("btn-flow-giaiquyet-KQXL");
		}
		QuyetDinhThuLyBean modelQDTL = svQuaTrinh.getQuyetDinhThuLy(idDonThu);
		if(modelQDTL!=null)
		{
			btnFlowQuyetDinhThuLy.setEnabled(true);
			btnFlowQuyetDinhThuLy.addStyleName("btn-flow-giaiquyet-QDTL");
		}
		QuyetDinhGiaiQuyetBean modelQDXL = svQuaTrinh.getQuyetDinhGiaiQuyet(idDonThu);
		if(modelQDXL!=null)
		{
			btnFlowQuyetDinhGiaiQuyet.setEnabled(true);
			btnFlowQuyetDinhGiaiQuyet.addStyleName("btn-flow-giaiquyet-QDGQ");

			List<ThiHanhQuyetDinhBean> listModelTHQD = svQuaTrinh.getThiHanhGiaiQuyetList(modelQDXL.getMaQuyetDinhGiaiQuyet());
			if(!listModelTHQD.isEmpty())
			{
				btnFlowKetQuaThiHanhQuyetDinh.setEnabled(true);
				btnFlowKetQuaThiHanhQuyetDinh.addStyleName("btn-flow-giaiquyet-THQD");
			}
		}
	}
}
