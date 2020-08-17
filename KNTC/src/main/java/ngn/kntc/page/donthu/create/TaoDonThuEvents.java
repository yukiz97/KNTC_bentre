package ngn.kntc.page.donthu.create;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.vaadin.dialogs.ConfirmDialog;

import ngn.kntc.UI.kntcUI;
import ngn.kntc.beans.DoiTuongBiKNTCBean;
import ngn.kntc.beans.DoiTuongDiKNTCBean;
import ngn.kntc.beans.DoiTuongUyQuyenBean;
import ngn.kntc.beans.DonThuBean;
import ngn.kntc.beans.HoSoDinhKemBean;
import ngn.kntc.beans.LuanChuyenBean;
import ngn.kntc.beans.QuaTrinhXuLyGiaiQuyetBean;
import ngn.kntc.beans.SoTiepCongDanBean;
import ngn.kntc.beans.ThongTinDonThuBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.enums.LoaiQuanLy;
import ngn.kntc.lienthongttcp.LienThongVuViec;
import ngn.kntc.lienthongttcp.LienThongVuViec;
import ngn.kntc.page.donthu.create.sublayout.NguoiUyQuyenLayout;
import ngn.kntc.ttcp.LTTTCP;
import ngn.kntc.ttcp.model.AttachmentType;
import ngn.kntc.ttcp.model.CoQuanDaGiaiQuyetType;
import ngn.kntc.ttcp.model.NguoiBiKNTCType;
import ngn.kntc.ttcp.model.NguoiKNTC;
import ngn.kntc.ttcp.model.NguoiKNTCType;
import ngn.kntc.ttcp.model.NguoiUyQuyenType;
import ngn.kntc.ttcp.model.NguonDonDenType;
import ngn.kntc.ttcp.model.PhanLoaiDonType;
import ngn.kntc.ttcp.model.VuViecType;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.SessionUtil;
import ngn.kntc.utils.UploadServiceUtil;
import ngn.kntc.views.tiepnhan.ViewSoTiepCongDan;
import ngn.kntc.views.tiepnhan.ViewTiepCongDan;
import ngn.kntc.views.xulydon.ViewXuLyDonDaCoKetQua;
import ngn.kntc.views.xulydon.ViewXuLyDonDaTiepNhan;
import ngn.kntc.windows.WindowChonCoQuan;
import ngn.kntc.windows.searchdonthu.WindowSearchDonThu;
import ngn.kntc.windows.searchdonthu.WindowSearchTiepCongDan;
import ngn.kntc.windows.tientrinhxlgq.WindowThemKetQuaXuLy;
import ngn.kntc.windows.tientrinhxlgq.WindowThemKetQuaXuLyTmp;

import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;

public class TaoDonThuEvents extends TaoDonThuDesign{
	public TaoDonThuEvents() {
		phuongThucEvents();
		phuongThucTCDEvents();
		phuongThucTiepNhanGianTiepEvents();
		mainButtonEvents();
	}

	private void phuongThucEvents()
	{
		btnTiepCongDan.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if(!tiepCongDan || lanhDaoTiep)
				{
					btnTiepCongDan.removeStyleName("btn-phuongthuc-deactive");
					btnTiepCongDan.addStyleName("btn-phuongthuc-active");
					btnLanhDaoTiep.removeStyleName("btn-phuongthuc-active");
					btnLanhDaoTiep.addStyleName("btn-phuongthuc-deactive");
					btnTiepNhanDonGianTiep.removeStyleName("btn-phuongthuc-active");
					btnTiepNhanDonGianTiep.addStyleName("btn-phuongthuc-deactive");

					if(cbTiepCongDanKhongDon.getValue())
					{
						vNoiDungDonThu.setVisible(false);
						vCoQuanDaGiaiQuyet.setVisible(false);
						vNguoiBiKNTC.setVisible(false);
						vNguoiUyQuyen.setVisible(false);
						rowTiepCongDan2.setVisible(false);
					}
					else
					{
						rowTiepCongDan2.setVisible(true);
					}

					rowTiepCongDan1.setVisible(true);
					rowTiepCongDan3.setVisible(false);
					rowTiepCongDan4.setVisible(false);
					rowTiepNhanDon1.setVisible(false);
					rowTiepNhanDon2.setVisible(false);
					rowTiepNhanDon3.setVisible(false);
					rowTiepNhanDon4.setVisible(false);
					layoutNguoiDiKNTC.getRowNacDanh().setVisible(false);
					layoutNguoiDiKNTC.getCbDonNacDanh().setValue(false);

					tiepCongDan = true;
					lanhDaoTiep = false;
				}
			}
		});

		btnLanhDaoTiep.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if(!tiepCongDan || !lanhDaoTiep)
				{
					btnLanhDaoTiep.removeStyleName("btn-phuongthuc-deactive");
					btnLanhDaoTiep.addStyleName("btn-phuongthuc-active");
					btnTiepCongDan.removeStyleName("btn-phuongthuc-active");
					btnTiepCongDan.addStyleName("btn-phuongthuc-deactive");
					btnTiepNhanDonGianTiep.removeStyleName("btn-phuongthuc-active");
					btnTiepNhanDonGianTiep.addStyleName("btn-phuongthuc-deactive");

					if(cbTiepCongDanKhongDon.getValue())
					{
						vNoiDungDonThu.setVisible(false);
						vCoQuanDaGiaiQuyet.setVisible(false);
						vNguoiBiKNTC.setVisible(false);
						vNguoiUyQuyen.setVisible(false);
						rowTiepCongDan2.setVisible(false);
					}
					else
					{
						rowTiepCongDan2.setVisible(true);
					}

					rowTiepCongDan1.setVisible(true);
					rowTiepNhanDon1.setVisible(false);
					rowTiepNhanDon2.setVisible(false);
					rowTiepNhanDon3.setVisible(false);
					rowTiepNhanDon4.setVisible(false);

					rowTiepCongDan3.setVisible(true);
					rowTiepCongDan4.setVisible(true);

					layoutNguoiDiKNTC.getRowNacDanh().setVisible(false);
					layoutNguoiDiKNTC.getCbDonNacDanh().setValue(false);

					tiepCongDan = true;
					lanhDaoTiep = true;
				}
			}
		});

		btnTiepNhanDonGianTiep.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if(tiepCongDan)
				{
					btnTiepCongDan.removeStyleName("btn-phuongthuc-active");
					btnTiepCongDan.addStyleName("btn-phuongthuc-deactive");
					btnLanhDaoTiep.removeStyleName("btn-phuongthuc-active");
					btnLanhDaoTiep.addStyleName("btn-phuongthuc-deactive");
					btnTiepNhanDonGianTiep.removeStyleName("btn-phuongthuc-deactive");
					btnTiepNhanDonGianTiep.addStyleName("btn-phuongthuc-active");

					rowTiepCongDan1.setVisible(false);
					rowTiepCongDan2.setVisible(false);
					rowTiepCongDan3.setVisible(false);
					rowTiepCongDan4.setVisible(false);
					rowTiepNhanDon1.setVisible(true);
					rowTiepNhanDon2.setVisible(true);

					vNoiDungDonThu.setVisible(true);
					vCoQuanDaGiaiQuyet.setVisible(true);
					vNguoiBiKNTC.setVisible(true);
					vNguoiUyQuyen.setVisible(true);
					layoutNguoiDiKNTC.getRowNacDanh().setVisible(true);
					layoutNguoiDiKNTC.getCbDonNacDanh().setValue(false);

					if((int)cmbNguonDonDen.getValue()==4)
					{
						rowTiepNhanDon3.setVisible(true);
						rowTiepNhanDon4.setVisible(true);
					}

					tiepCongDan = false;
					lanhDaoTiep = false;
				}
			}
		});
	}

	public void phuongThucTCDEvents()
	{
		cbTiepCongDanKhongDon.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(cbTiepCongDanKhongDon.getValue())
				{
					//vNoiDungDonThu.setVisible(false);
					//vCoQuanDaGiaiQuyet.setVisible(false);
					//vNguoiBiKNTC.setVisible(false);
					//vNguoiUyQuyen.setVisible(false);
					rowTiepCongDan2.setVisible(false);

					layoutNoiDungDonThu.getRowKiemTra().setVisible(false);
					layoutNoiDungDonThu.getRowNoiDung().setVisible(false);
					layoutNoiDungDonThu.getRowBtnDinhKem().setVisible(false);
					layoutNoiDungDonThu.getRowDisplayDinhKem().setVisible(false);

					layoutNoiDungDonThu.getBtnChonLinhVuc().setCaption("Thêm lĩnh vực");

					lblHeaderNoiDungDonThu.setVisible(false);

					layoutNguoiDiKNTC.getRowKetQuaTiep().setVisible(true);
				}
				else
				{
					vNoiDungDonThu.setVisible(true);
					vCoQuanDaGiaiQuyet.setVisible(true);
					vNguoiBiKNTC.setVisible(true);
					vNguoiUyQuyen.setVisible(true);
					rowTiepCongDan2.setVisible(true);

					layoutNoiDungDonThu.getRowKiemTra().setVisible(true);
					layoutNoiDungDonThu.getRowNoiDung().setVisible(true);
					layoutNoiDungDonThu.getRowBtnDinhKem().setVisible(true);
					layoutNoiDungDonThu.getRowDisplayDinhKem().setVisible(true);

					lblHeaderNoiDungDonThu.setVisible(true);

					layoutNoiDungDonThu.getBtnChonLinhVuc().setCaption("Thêm lĩnh vực của đơn thư");

					layoutNguoiDiKNTC.getRowKetQuaTiep().setVisible(false);
				}
			}
		});
		cbUyQuyenLanhDao.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				cmbTenLanhDaoUyQuyen.setEnabled(cbUyQuyenLanhDao.getValue());
			}
		});
		layoutNguoiDiKNTC.getBtnLayThongTinTiepCongDan().addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				String[] arrChuTheDonThu = null;

				if((int)layoutNguoiDiKNTC.getCmbLoaiNguoiKTNC().getValue()!=2)
				{
					if(!layoutNguoiDiKNTC.getTxtHoTen().getValue().trim().isEmpty())
					{
						arrChuTheDonThu = new String[1];
						arrChuTheDonThu[0] = layoutNguoiDiKNTC.getTxtHoTen().getValue().trim();
					}
				}
				else
				{
					int i = 0;
					Map<Integer, DoiTuongDiKNTCBean> listNguoiDaiDien = layoutNguoiDiKNTC.getListNguoiDaiDien();
					if(listNguoiDaiDien.size()>0)
					{
						arrChuTheDonThu = new String[listNguoiDaiDien.size()];
						Iterator<Integer> itr = listNguoiDaiDien.keySet().iterator();
						while(itr.hasNext())
						{
							arrChuTheDonThu[i++] =" "+listNguoiDaiDien.get(itr.next()).getHoTen()+" ";
						}
					}
				}

				WindowSearchTiepCongDan wdLayThongTinTCD = new WindowSearchTiepCongDan();
				try {
					wdLayThongTinTCD.loadData(arrChuTheDonThu);
				} catch (Exception e) {
					e.printStackTrace();
				}
				UI.getCurrent().addWindow(wdLayThongTinTCD);

				wdLayThongTinTCD.getBtnOk().addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						wdLayThongTinTCD.close();
						try {
							if(wdLayThongTinTCD.isGanVuViec())
							{
								idGanVuViec = wdLayThongTinTCD.getTiepCongDan().getMaSoTiepCongDan()+"";
							}
							if(wdLayThongTinTCD.isLayThongTin())
							{
								SoTiepCongDanBean modelTmp = wdLayThongTinTCD.getTiepCongDan();

								List<String> listLinhVucTmp = svTCD.getLinhVucList(modelTmp.getMaSoTiepCongDan());
								layoutNoiDungDonThu.getCmbLoaiDonThu().setValue(modelTmp.getLoaiLinhVuc());
								layoutNoiDungDonThu.setListLinhVuc(listLinhVucTmp);
								layoutNoiDungDonThu.loadDisplayLinhVuc();
								layoutNguoiDiKNTC.getTxtNoiDungTiep().setValue(modelTmp.getNoiDungTiepCongDan());
								layoutNguoiDiKNTC.getTxtKetQuaTiep().setValue(modelTmp.getKetQuaTiepCongDan());

								if(modelTmp.getMaCoQuanDaGiaiQuyet()!=null)
								{
									cbHeaderCoQuanDaGiaiQuyet.setValue(true);

									layoutCoQuanDaGiaiQuyet.setFieldsCQDGQ(modelTmp);
								}
								if(modelTmp.getIdNguoiBiKNTC()!=0)
								{
									DoiTuongBiKNTCBean modelDoiTuongBiKNTCBean = svDonThu.getDoiTuongBiKNTC(modelTmp.getIdNguoiBiKNTC());
									
									cbHeaderNguoiBiKNTC.setValue(true);
									layoutNguoiBiKNTC.getCmbLoaiNguoiKTNC().setValue(modelTmp.getLoaiNguoiBiKNTC());
									layoutNguoiBiKNTC.setFieldsBiKNTC(modelDoiTuongBiKNTCBean);
								}
								if(modelTmp.getIdNguoiUyQuyen()!=0)
								{
									DoiTuongUyQuyenBean modelDoituongUyQuyen = svDonThu.getDoiTuongUyQuyen(modelTmp.getIdNguoiUyQuyen());
									
									cbHeaderNguoiUyQuyen.setValue(true);
									layoutNguoiUyQuyen.getCmbLoaiNguoiUyQuyen().setValue(modelTmp.getLoaiNguoiUyQuyen());
									layoutNguoiUyQuyen.setFieldsNguoiUyQuyen(modelDoituongUyQuyen);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
	}

	public void phuongThucTiepNhanGianTiepEvents()
	{
		cmbNguonDonDen.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if((int)cmbNguonDonDen.getValue()==4)
				{
					rowTiepNhanDon3.setVisible(true);
					rowTiepNhanDon4.setVisible(true);
				}
				else
				{
					rowTiepNhanDon3.setVisible(false);
					rowTiepNhanDon4.setVisible(false);
				}
			}
		});

		btnTimKiemCoQuanChuyenDen.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				WindowChonCoQuan wdChonCoQuan = new WindowChonCoQuan();
				UI.getCurrent().addWindow(wdChonCoQuan);

				wdChonCoQuan.getBtnOk().addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						String coQuanId = wdChonCoQuan.getCoQuanID();
						try {
							txtTenCoQuanChuyenDen.setValue(DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.coquan.getName(), coQuanId).getName());
							txtTenCoQuanChuyenDen.setId(coQuanId);
						} catch (ReadOnlyException | SQLException e) {
							e.printStackTrace();
						}
					}
				});
			}
		});

		layoutNguoiDiKNTC.getCbDonNacDanh().addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				layoutNoiDungDonThu.getCbDonKhongDuDKXL().setValue(layoutNguoiDiKNTC.getCbDonNacDanh().getValue());
			}
		});

		layoutNoiDungDonThu.getBtnKiemTraDonThu().addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					String[] arrChuTheDonThu = null;

					if((int)layoutNguoiDiKNTC.getCmbLoaiNguoiKTNC().getValue()!=2)
					{
						if(!layoutNguoiDiKNTC.getTxtHoTen().getValue().trim().isEmpty())
						{
							arrChuTheDonThu = new String[1];
							arrChuTheDonThu[0] = layoutNguoiDiKNTC.getTxtHoTen().getValue().trim();
						}
					}
					else
					{
						int i = 0;
						Map<Integer, DoiTuongDiKNTCBean> listNguoiDaiDien = layoutNguoiDiKNTC.getListNguoiDaiDien();
						if(listNguoiDaiDien.size()>0)
						{
							arrChuTheDonThu = new String[listNguoiDaiDien.size()];
							Iterator<Integer> itr = listNguoiDaiDien.keySet().iterator();
							while(itr.hasNext())
							{
								arrChuTheDonThu[i++] =" "+listNguoiDaiDien.get(itr.next()).getHoTen()+" ";
							}
						}
					}
					WindowSearchDonThu wdSearchDonThu = new WindowSearchDonThu();
					UI.getCurrent().addWindow(wdSearchDonThu);
					wdSearchDonThu.loadData(arrChuTheDonThu,layoutNoiDungDonThu.getTxtNoiDungDonThu().getValue().trim());
					wdSearchDonThu.getTxtNoiDungDonThu().setValue(layoutNoiDungDonThu.getTxtNoiDungDonThu().getValue().trim());
					wdSearchDonThu.getBtnOk().addClickListener(new ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
							try {
								if(wdSearchDonThu.isGanVuViec())
								{
									idGanVuViec = wdSearchDonThu.getDonThu().getMaDonThu()+"";
									idGanVuViecLienThong = wdSearchDonThu.getDonThu().getIdHoSoLienThong();
								}
								if(wdSearchDonThu.isLayThongTin())
								{
									DonThuBean modelTmp = wdSearchDonThu.getDonThu();

									List<String> listLinhVucTmp = svDonThu.getLinhVucList(modelTmp.getMaDonThu());
									layoutNoiDungDonThu.getCmbLoaiDonThu().setValue(modelTmp.getLoaiDonThu());
									layoutNoiDungDonThu.setListLinhVuc(listLinhVucTmp);
									layoutNoiDungDonThu.loadDisplayLinhVuc();
									layoutNoiDungDonThu.getTxtNoiDungDonThu().setValue(modelTmp.getNoiDungDonThu());

									if(modelTmp.getMaCoQuanDaGiaiQuyet()!=null)
									{
										cbHeaderCoQuanDaGiaiQuyet.setValue(true);
										layoutCoQuanDaGiaiQuyet.setFieldsCQDGQ(modelTmp);
									}
									if(modelTmp.getMaDoiTuongBiKNTC()!=0)
									{
										DoiTuongBiKNTCBean modelDoiTuongBiKNTCBean = svDonThu.getDoiTuongBiKNTC(modelTmp.getMaDoiTuongBiKNTC());
										
										cbHeaderNguoiBiKNTC.setValue(true);
										layoutNguoiBiKNTC.getCmbLoaiNguoiKTNC().setValue(modelTmp.getLoaiNguoiBiKNTC());
										layoutNguoiBiKNTC.setFieldsBiKNTC(modelDoiTuongBiKNTCBean);
									}
									if(modelTmp.getMaDoiTuongUyQuyen()!=0)
									{
										DoiTuongUyQuyenBean modelDoituongUyQuyen = svDonThu.getDoiTuongUyQuyen(modelTmp.getMaDoiTuongUyQuyen());
										
										cbHeaderNguoiUyQuyen.setValue(true);
										layoutNguoiUyQuyen.getCmbLoaiNguoiUyQuyen().setValue(modelTmp.getLoaiNguoiUyQuyen());
										layoutNguoiUyQuyen.setFieldsNguoiUyQuyen(modelDoituongUyQuyen);
									}
								}
								wdSearchDonThu.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}

	public void mainButtonEvents()
	{
		btnSave.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					validateForm();
					if(validateSuccess)
					{
						if(themMoi)
							saveFunction();
						else
							updateFunction();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		btnReset.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				kntcUI.getCurrent().getNavigator().navigateTo(ViewTiepCongDan.NAME);
			}
		});

		btnClose.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				kntcUI.getCurrent().getNavigator().navigateTo(ViewSoTiepCongDan.NAME);
			}
		});
	}

	private void saveFunction() throws Exception {
		int idGenDoiTuongBiKNTC = -1;
		int idGenDoiTuongUyQuyen = -1;
		int idGenDonThu = -1;
		int idGenSoTiepCongDan = -1;

		/* Insert đối tượng bị KNTC */
		if(cbHeaderNguoiBiKNTC.getValue())
		{
			DoiTuongBiKNTCBean modelDoiTuongBiKNTC = layoutNguoiBiKNTC.getValueNguoiBiKNTC();
			if(modelDoiTuongBiKNTC.getMaDoiTuong()==0)
				idGenDoiTuongBiKNTC = svDonThu.insertDoiTuongBiKNTC(modelDoiTuongBiKNTC);
			else
			{
				svDonThu.updateDoiTuongBiKNTC(modelDoiTuongBiKNTC);
				idGenDoiTuongBiKNTC = modelDoiTuongBiKNTC.getMaDoiTuong();
			}
		}
		/* Insert đối tượng ủy quyền */
		if(cbHeaderNguoiUyQuyen.getValue())
		{
			DoiTuongUyQuyenBean modelDoiTuongUyQuyen = layoutNguoiUyQuyen.getValueNguoiUyQuyen();
			idGenDoiTuongUyQuyen = svDonThu.insertDoiTuongUyQuyen(modelDoiTuongUyQuyen);
		}
		
		if(!tiepCongDan || (tiepCongDan && !cbTiepCongDanKhongDon.getValue()))
		{
			/* Insert đơn thư */
			DonThuBean modelDonThu = getValueDonThu(idGenDoiTuongBiKNTC,idGenDoiTuongUyQuyen);
			
			idGenDonThu = svDonThu.insertDonThu(modelDonThu);
			
			/* Insert thông tin đơn thư */
			ThongTinDonThuBean modelThongTinDon = getValueThongTinDonInsert(idGenDonThu);
			svDonThu.insertThongTinDon(modelThongTinDon);
			
			/* Lĩnh vực đơn thư */
			insertLinhVucDonThu(idGenDonThu, layoutNoiDungDonThu.getListLinhVuc());

			/* Hồ sơ đính kèm */
			insertHoSoDinhKem(idGenDonThu, layoutNoiDungDonThu.getListHoSoDinhKem());

			if(idDonThu==-1)
			{
				File old=new File(UploadServiceUtil.getAbsolutePath()+File.separator+UploadServiceUtil.getFolderNameDonThu()+File.separator+folderTmp);
				File rename=new File(UploadServiceUtil.getAbsolutePath()+File.separator+UploadServiceUtil.getFolderNameDonThu()+File.separator+idGenDonThu);
				old.renameTo(rename);
			}

			QuaTrinhXuLyGiaiQuyetBean modelQT = new QuaTrinhXuLyGiaiQuyetBean();
			modelQT.setHeThongTao(true);
			modelQT.setMaDonThu(idGenDonThu);
			modelQT.setNgayDang(new Date());
			modelQT.setUserNhap(SessionUtil.getUserId());
			modelQT.setNoiDung(svQuaTrinh.returnStringCanBoNhap(SessionUtil.getUserId(), " đã nhập đơn đơn thư",null));
			svQuaTrinh.insertQuaTrinhXLGQ(modelQT);

			LuanChuyenBean model = new LuanChuyenBean();
			model.setIdDonThu(idGenDonThu);
			model.setIdUserChuyen(SessionUtil.getUserId());
			model.setIdUserNhan(SessionUtil.getUserId());
			model.setPhanCong(true);
			model.setNgayChuyen(new Date());

			svQuaTrinh.insertLuanChuyen(model);
			
		}
		if(tiepCongDan)
		{
			/* Insert tiếp công dân */
			idGenSoTiepCongDan = svTCD.insertSoTiepCongDan(getValueTiepCongDan(idGenDonThu,idGenDoiTuongBiKNTC,idGenDoiTuongUyQuyen));

			/* Lĩnh vực đơn thư */
			insertLinhVucTCD(idGenSoTiepCongDan, layoutNoiDungDonThu.getListLinhVuc());
		}

		/* Insert đối tượng đi KNTC */
		insertNguoiDaiDien(idGenSoTiepCongDan, idGenDonThu);
		if(!tiepCongDan || (tiepCongDan && !cbTiepCongDanKhongDon.getValue()))
		{
			//lienThongVuViec.executeLienThong(idGanVuViecLienThong,"save");
			new LienThongVuViec().executeLienThong(idGenDonThu,SessionUtil.getOrgId(),idGanVuViecLienThong,"save");
		}
		Notification.show("Thêm mới thành công",Type.TRAY_NOTIFICATION);
		kntcUI.getCurrent().setMenuCountValue(LoaiQuanLy.donvi.getType());

		if(!tiepCongDan || (tiepCongDan && !cbTiepCongDanKhongDon.getValue()))
		{
			if(!layoutNoiDungDonThu.getCbDonKhongDuDKXL().getValue())
			{
				int idDonThiTmp = idGenDonThu;
				ConfirmDialog.show(UI.getCurrent(),"Thông báo","Bạn có muốn nhập kết quả xử lý","Đồng ý","Hủy",new ConfirmDialog.Listener() {

					@Override
					public void onClose(ConfirmDialog dialog) {
						if(dialog.isConfirmed())
						{
							WindowThemKetQuaXuLyTmp wdKQXL = new WindowThemKetQuaXuLyTmp(idDonThiTmp);
							wdKQXL.setClosable(false);
							UI.getCurrent().addWindow(wdKQXL);

							wdKQXL.getLayoutSubmit().getBtnSave().addClickListener(new ClickListener() {

								@Override
								public void buttonClick(ClickEvent event) {
									if(wdKQXL.isValidateSuccess())
									{
										wdKQXL.close();
										try {
											kntcUI.getCurrent().setMenuCountValue(LoaiQuanLy.donvi.getType());
											Notification.show("Thêm thành công kết quả xử lý",Type.TRAY_NOTIFICATION);
											kntcUI.getCurrent().getNavigator().navigateTo(ViewXuLyDonDaCoKetQua.NAME);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}
							});

							wdKQXL.getLayoutSubmit().getBtnCancel().addClickListener(new ClickListener() {

								@Override
								public void buttonClick(ClickEvent event) {
									kntcUI.getCurrent().getNavigator().navigateTo(ViewXuLyDonDaTiepNhan.NAME);
								}
							});
						}
						else
						{
							kntcUI.getCurrent().getNavigator().navigateTo(ViewXuLyDonDaTiepNhan.NAME);
						}
					}
				});
			}
			else
			{
				kntcUI.getCurrent().getNavigator().navigateTo(ViewXuLyDonDaTiepNhan.NAME);
			}
		}
		else if(tiepCongDan)
		{
			kntcUI.getCurrent().getNavigator().navigateTo(ViewSoTiepCongDan.NAME);
		}
	}

	private void updateFunction() throws Exception {
		/* update đối tượng bị KNTC */
		if(cbHeaderNguoiBiKNTC.getValue())
		{
			DoiTuongBiKNTCBean modelDoiTuongBiKNTC = layoutNguoiBiKNTC.getValueNguoiBiKNTC();
			svDonThu.updateDoiTuongBiKNTC(modelDoiTuongBiKNTC);
		}

		/* update đối tượng ủy quyền */
		if(cbHeaderNguoiUyQuyen.getValue())
		{
			DoiTuongUyQuyenBean modelDoiTuongUyQuyen = layoutNguoiUyQuyen.getValueNguoiUyQuyen();
			svDonThu.updateDoiTuongUyQuyen(modelDoiTuongUyQuyen);
		}
		if(!tiepCongDan || (tiepCongDan && !cbTiepCongDanKhongDon.getValue()))
		{
			//update đơn thư 
			DonThuBean modelDonThu = getValueDonThu(-1,-1);
			modelDonThu.setMaDonThu(this.idDonThu);
			svDonThu.updateDonThu(modelDonThu);
			new LienThongVuViec().executeLienThong(this.idDonThu,SessionUtil.getOrgId(),null,"update");

			// Lĩnh vực đơn thư 
			insertLinhVucDonThu(this.idDonThu, layoutNoiDungDonThu.getListLinhVuc());

			// Hồ sơ đính kèm 
			insertHoSoDinhKem(this.idDonThu, layoutNoiDungDonThu.getListHoSoDinhKem());

			QuaTrinhXuLyGiaiQuyetBean modelQT = new QuaTrinhXuLyGiaiQuyetBean();
			modelQT.setHeThongTao(true);
			modelQT.setMaDonThu(this.idDonThu);
			modelQT.setNgayDang(new Date());
			modelQT.setUserNhap(SessionUtil.getUserId());
			modelQT.setNoiDung(svQuaTrinh.returnStringCanBoNhap(SessionUtil.getUserId(), " đã cập nhật đơn thư",null));
			svQuaTrinh.insertQuaTrinhXLGQ(modelQT);
		}
		if(this.idTiepCongDan!=-1)
		{
			// Insert tiếp công dân 
			SoTiepCongDanBean modelTCD = getValueTiepCongDan(-1,-1,-1);
			modelTCD.setMaSoTiepCongDan(this.idTiepCongDan);
			svTCD.updateSoTiepCongDan(modelTCD);

			/* Lĩnh vực đơn thư */
			insertLinhVucTCD(idTiepCongDan, layoutNoiDungDonThu.getListLinhVuc());
		}

		// Insert đối tượng đi KNTC 
		insertNguoiDaiDien(this.idTiepCongDan, this.idDonThu);

		Notification.show("Chỉnh sửa thành công",Type.TRAY_NOTIFICATION);
	}

	public ThongTinDonThuBean getValueThongTinDonInsert(int idDonThu)
	{
		ThongTinDonThuBean modelThongTinDon = new ThongTinDonThuBean();

		int nguonDonDen = 0;
		if(!tiepCongDan)
			nguonDonDen = (int)cmbNguonDonDen.getValue();
		else
			nguonDonDen = 6;

		modelThongTinDon.setNguonDonDen(nguonDonDen);

		/* Cơ quan chuyển đến */
		if((int)cmbNguonDonDen.getValue()==4)
		{
			String maCoQuanChuyenDen = txtTenCoQuanChuyenDen.getId();
			String soVanBanDen = txtSoVanBanChuyenDen.getValue().trim();
			Date ngayPhatHanhVanBan = dfNgayPhatHanhVanBanChuyenDen.getValue();

			modelThongTinDon.setMaCoQuanChuyenDen(maCoQuanChuyenDen);
			modelThongTinDon.setSoVanBanDen(soVanBanDen == "" ? null : soVanBanDen);
			modelThongTinDon.setNgayPhatHanhVanBan(ngayPhatHanhVanBan);
		}

		modelThongTinDon.setOrgChuyen(SessionUtil.getOrgId());
		modelThongTinDon.setOrgNhan(SessionUtil.getOrgId());
		modelThongTinDon.setNgayNhan(new Date());
		modelThongTinDon.setMaDonThu(idDonThu);

		return modelThongTinDon;
	}

	public DonThuBean getValueDonThu(int idDoiTuongBiKNTC, int idDoiTuongUyQuyen)
	{
		DonThuBean model = new DonThuBean();

		/* Thông tin đơn thư */

		Date ngayNhanDon = null;
		Date ngayNhapDon = dfNgayNhapDon.getValue();

		if(tiepCongDan)
			ngayNhanDon = dfNgayTiepCongDan.getValue();
		else
			ngayNhanDon = dfNgayNhanDon.getValue();

		model.setUserNhapDon(SessionUtil.getUserId());
		model.setNgayNhanDon(ngayNhanDon);
		model.setNgayNhapDon(ngayNhapDon);

		/* Người đi KNTC */
		int loaiNguoiDiKNTC = (int)layoutNguoiDiKNTC.getCmbLoaiNguoiKTNC().getValue();
		int soNguoiDiKNTC = 1;
		int soNguoiDaiDien = 1;
		boolean donNacDanh = layoutNguoiDiKNTC.getCbDonNacDanh().getValue();

		if(loaiNguoiDiKNTC==2)
		{
			soNguoiDiKNTC = Integer.parseInt(layoutNguoiDiKNTC.getTxtSoNguoi().getValue());
			soNguoiDaiDien = layoutNguoiDiKNTC.getListNguoiDaiDien().size();
		} 
		else if(loaiNguoiDiKNTC==3)
		{
			String tenCoQuanDiKNTC = layoutNguoiDiKNTC.getTxtTenCoQuan().getValue().trim();
			String diaChiCoQuanDiKNTC = layoutNguoiDiKNTC.getTxtDiaChiCoQuan().getValue().trim();

			model.setTenCoQuanDiKNTC(tenCoQuanDiKNTC);
			model.setDiaChiCoQuanDiKNTC(diaChiCoQuanDiKNTC == "" ? null : diaChiCoQuanDiKNTC);
		}
		model.setLoaiNguoiDiKNTC(loaiNguoiDiKNTC);
		model.setSoNguoiDiKNTC(soNguoiDiKNTC);
		model.setSoNguoiDaiDien(soNguoiDaiDien);
		model.setDonNacDanh(donNacDanh);

		/* Nội dung đơn thư */
		boolean donKhongDuDKXL = layoutNoiDungDonThu.getCbDonKhongDuDKXL().getValue();
		String noiDungDonThu = layoutNoiDungDonThu.getTxtNoiDungDonThu().getValue().trim();
		int loaiDonThu = (int)layoutNoiDungDonThu.getCmbLoaiDonThu().getValue();

		model.setDonKhongDuDieuKienXuLy(donKhongDuDKXL);
		model.setNoiDungDonThu(noiDungDonThu);
		model.setLoaiDonThu(loaiDonThu);


		/* Cơ quan đã giải quyết */
		if(cbHeaderCoQuanDaGiaiQuyet.getValue())
		{
			String tenCoQuanDaGiaiQuyet = layoutCoQuanDaGiaiQuyet.getTxtTenCoQuan().getId();
			String soKyhieu = !layoutCoQuanDaGiaiQuyet.getTxtSoKyHieu().isEmpty()? layoutCoQuanDaGiaiQuyet.getTxtSoKyHieu().getValue().trim():null;
			int lanGiaiQuyet = Integer.parseInt(layoutCoQuanDaGiaiQuyet.getTxtLanGiaiQuyet().getValue().trim());
			Date ngayBanHanhQDGQ = layoutCoQuanDaGiaiQuyet.getDfNgayBanHanh().getValue();
			int loaiQuyetDinhGiaiQuyet = (int)layoutCoQuanDaGiaiQuyet.getCmbLoaiQuyetDinh().getValue();
			String noiDungQuyetDinh  = !layoutCoQuanDaGiaiQuyet.getTxtNoiDungQuyetDinh().isEmpty() ? layoutCoQuanDaGiaiQuyet.getTxtNoiDungQuyetDinh().getValue().trim():null;

			model.setMaCoQuanDaGiaiQuyet(tenCoQuanDaGiaiQuyet);
			model.setSoKyHieuVanBanGiaiQuyet(soKyhieu);
			model.setLanGiaiQuyet(lanGiaiQuyet);
			model.setNgayBanHanhQDGQ(ngayBanHanhQDGQ);
			model.setLoaiQuyetDinhGiaiQuyet(loaiQuyetDinhGiaiQuyet);
			model.setTomTatNoiDungGiaiQuyet(noiDungQuyetDinh);
		}
		/* Người bị KNTC */
		if(cbHeaderNguoiBiKNTC.getValue())
		{
			int loaiNguoiBiKNTC = (int)layoutNguoiBiKNTC.getCmbLoaiNguoiKTNC().getValue();

			model.setLoaiNguoiBiKNTC(loaiNguoiBiKNTC);
			model.setMaDoiTuongBiKNTC(idDoiTuongBiKNTC);
		}

		/* Người ủy quyền */
		if(cbHeaderNguoiUyQuyen.getValue())
		{
			int loaiNguoiUyQuyen = (int)layoutNguoiUyQuyen.getCmbLoaiNguoiUyQuyen().getValue();

			model.setLoaiNguoiUyQuyen(loaiNguoiUyQuyen);
			model.setMaDoiTuongUyQuyen(idDoiTuongUyQuyen);
		}

		if(idGanVuViec!="")
		{
			model.setGanVuViec(idGanVuViec);
		}

		return model;
	}

	public SoTiepCongDanBean getValueTiepCongDan(int idDonThu,int idDoiTuongBiKNTC, int idDoiTuongUyQuyen)
	{
		SoTiepCongDanBean model = new SoTiepCongDanBean();
		int loaiNguoiDiKNTC = (int)layoutNguoiDiKNTC.getCmbLoaiNguoiKTNC().getValue();
		int soNguoiDiKNTC = 1;
		int soNguoiDaiDien = 1;
		Date ngayTiepCongDan = dfNgayTiepCongDan.getValue();

		model.setMaDonThu(idDonThu);

		if(loaiNguoiDiKNTC==2)
		{
			soNguoiDiKNTC = Integer.parseInt(layoutNguoiDiKNTC.getTxtSoNguoi().getValue());
			soNguoiDaiDien = layoutNguoiDiKNTC.getListNguoiDaiDien().size();
		} 
		else if(loaiNguoiDiKNTC==3)
		{
			String tenCoQuanDiKNTC = layoutNguoiDiKNTC.getTxtTenCoQuan().getValue().trim();
			String diaChiCoQuanDiKNTC = layoutNguoiDiKNTC.getTxtDiaChiCoQuan().getValue().trim();

			model.setTenCoQuanDiKNTC(tenCoQuanDiKNTC);
			model.setDiaChiCoQuanDiKNTC(diaChiCoQuanDiKNTC == "" ? null : diaChiCoQuanDiKNTC);
		}

		model.setLoaiNguoiDiKNTC(loaiNguoiDiKNTC);
		model.setSoNguoiDiKNTC(soNguoiDiKNTC);
		model.setSoNguoiDaiDien(soNguoiDaiDien);

		int loaiLinhVuc = (int)layoutNoiDungDonThu.getCmbLoaiDonThu().getValue();

		model.setLoaiLinhVuc(loaiLinhVuc);

		if(cbTiepCongDanKhongDon.getValue())
		{
			String ketQuaTiepCongDan = layoutNguoiDiKNTC.getTxtKetQuaTiep().getValue().trim();
			String noiDungTiepCongDan = layoutNguoiDiKNTC.getTxtNoiDungTiep().getValue().trim();
			model.setKetQuaTiepCongDan(ketQuaTiepCongDan);
			model.setNoiDungTiepCongDan(noiDungTiepCongDan);
			if(idGanVuViec!="")
			{
				model.setGanVuViec(idGanVuViec);
			}
		}
		if(lanhDaoTiep)
		{
			model.setMaLanhDaoTiep((long)cmbTenLanhDaoTiep.getValue());
			model.setUyQuyenLanhDao(cbUyQuyenLanhDao.getValue());
			model.setTiepDinhKy(cbTiepDinhKy.getValue());
			if(cbUyQuyenLanhDao.getValue())
				model.setMaLanhDaoUyQuyen((long)cmbTenLanhDaoUyQuyen.getValue());
		}
		/* Cơ quan đã giải quyết */
		if(cbHeaderCoQuanDaGiaiQuyet.getValue())
		{
			String tenCoQuanDaGiaiQuyet = layoutCoQuanDaGiaiQuyet.getTxtTenCoQuan().getId();
			String soKyhieu = !layoutCoQuanDaGiaiQuyet.getTxtSoKyHieu().isEmpty()? layoutCoQuanDaGiaiQuyet.getTxtSoKyHieu().getValue().trim():null;
			int lanGiaiQuyet = Integer.parseInt(layoutCoQuanDaGiaiQuyet.getTxtLanGiaiQuyet().getValue().trim());
			Date ngayBanHanhQDGQ = layoutCoQuanDaGiaiQuyet.getDfNgayBanHanh().getValue();
			int loaiQuyetDinhGiaiQuyet = (int)layoutCoQuanDaGiaiQuyet.getCmbLoaiQuyetDinh().getValue();
			String noiDungQuyetDinh  = !layoutCoQuanDaGiaiQuyet.getTxtNoiDungQuyetDinh().isEmpty() ? layoutCoQuanDaGiaiQuyet.getTxtNoiDungQuyetDinh().getValue().trim():null;

			model.setMaCoQuanDaGiaiQuyet(tenCoQuanDaGiaiQuyet);
			model.setSoKyHieuVanBanDen(soKyhieu);
			model.setLanGiaiQuyet(lanGiaiQuyet);
			model.setNgayBanHanhQDGQ(ngayBanHanhQDGQ);
			model.setLoaiQuyetDinhGiaiQuyet(loaiQuyetDinhGiaiQuyet);
			model.setTomTatNoiDungGiaiQuyet(noiDungQuyetDinh);
		}
		/* Người bị KNTC */
		if(cbHeaderNguoiBiKNTC.getValue())
		{
			int loaiNguoiBiKNTC = (int)layoutNguoiBiKNTC.getCmbLoaiNguoiKTNC().getValue();

			model.setLoaiNguoiBiKNTC(loaiNguoiBiKNTC);
			model.setIdNguoiBiKNTC(idDoiTuongBiKNTC);
		}

		/* Người ủy quyền */
		if(cbHeaderNguoiUyQuyen.getValue())
		{
			int loaiNguoiUyQuyen = (int)layoutNguoiUyQuyen.getCmbLoaiNguoiUyQuyen().getValue();

			model.setLoaiNguoiUyQuyen(loaiNguoiUyQuyen);
			model.setIdNguoiUyQuyen(idDoiTuongUyQuyen);
		}
		model.setUserTCD(SessionUtil.getUserId());
		model.setNgayTiepCongDan(ngayTiepCongDan);

		return model;
	}

	private void insertNguoiDaiDien(int idSoTiepCongDan,int idDonThu) throws Exception
	{
		List<DoiTuongDiKNTCBean> listDoiTuongDiKNTC = new ArrayList<DoiTuongDiKNTCBean>();
		int idGenDoiTuongDiKNTC = 0; 
		/* Insert đối tượng đi KNTC */
		svDonThu.deleteNguoiDaiDienDonThu(idDonThu);
		svTCD.deleteNguoiDaiDienTCD(idSoTiepCongDan);
		if((int)layoutNguoiDiKNTC.getCmbLoaiNguoiKTNC().getValue()!=2)
		{
			DoiTuongDiKNTCBean model = layoutNguoiDiKNTC.getValueNguoiDiKNTC();
			listDoiTuongDiKNTC.add(model);
			if(model.getMaDoiTuong()==0)
				idGenDoiTuongDiKNTC = svDonThu.insertDoiTuongDiKNTC(model);
			else
			{
				svDonThu.updateDoiTuongDiKNTC(model);
				idGenDoiTuongDiKNTC = model.getMaDoiTuong();
			}
			if(idSoTiepCongDan!=-1)
			{
				svDonThu.insertNguoiDaiDienTiepCongDan(idSoTiepCongDan, idGenDoiTuongDiKNTC);
				if(idDonThu!=-1)
					svDonThu.insertNguoiDaiDienDonThu(idDonThu, idGenDoiTuongDiKNTC);
			}
			else
			{
				SoTiepCongDanBean modelTCD= svTCD.getSoTiepCongDanOnDonThu(idDonThu);
				if(modelTCD!=null)
				{
					svTCD.deleteNguoiDaiDienTCD(modelTCD.getMaSoTiepCongDan());
					svDonThu.insertNguoiDaiDienTiepCongDan(modelTCD.getMaSoTiepCongDan(), idGenDoiTuongDiKNTC);
				}
				svDonThu.insertNguoiDaiDienDonThu(idDonThu, idGenDoiTuongDiKNTC);
			}
		}
		else
		{
			SoTiepCongDanBean modelTCD= svTCD.getSoTiepCongDanOnDonThu(idDonThu);
			if(modelTCD!=null)
			{
				svTCD.deleteNguoiDaiDienTCD(modelTCD.getMaSoTiepCongDan());
			}
			Map<Integer, DoiTuongDiKNTCBean> listNguoiDaiDien = layoutNguoiDiKNTC.getListNguoiDaiDien();
			Iterator<Integer> itr = listNguoiDaiDien.keySet().iterator();
			while(itr.hasNext())
			{
				DoiTuongDiKNTCBean model = listNguoiDaiDien.get(itr.next());
				listDoiTuongDiKNTC.add(model);
				if(model.getMaDoiTuong()==0)
					idGenDoiTuongDiKNTC = svDonThu.insertDoiTuongDiKNTC(model);
				else
				{
					svDonThu.updateDoiTuongDiKNTC(model);
					idGenDoiTuongDiKNTC = model.getMaDoiTuong();
				}
				if(idSoTiepCongDan!=-1)
				{
					svDonThu.insertNguoiDaiDienTiepCongDan(idSoTiepCongDan, idGenDoiTuongDiKNTC);
					if(idDonThu!=-1)
						svDonThu.insertNguoiDaiDienDonThu(idDonThu, idGenDoiTuongDiKNTC);
				}
				else
				{
					if(modelTCD!=null)
					{
						svDonThu.insertNguoiDaiDienTiepCongDan(modelTCD.getMaSoTiepCongDan(), idGenDoiTuongDiKNTC);
					}
					svDonThu.insertNguoiDaiDienDonThu(idDonThu, idGenDoiTuongDiKNTC);
				}
			}
		}
	}

	public void insertLinhVucDonThu(int idDonThu, List<String> listLinhVuc) throws SQLException
	{
		svDonThu.deleteLinhVucDonThu(idDonThu);
		for(String id : listLinhVuc)
		{
			svDonThu.insertLinhVucDonThu(idDonThu, id);
		}
	}

	public void insertLinhVucTCD(int idSoTCD, List<String> listLinhVuc) throws SQLException
	{
		svTCD.deleteLinhVucTCD(idSoTCD);
		for(String id : listLinhVuc)
		{
			svTCD.insertLinhVucTCD(idSoTCD, id);
		}
	}

	private void insertHoSoDinhKem(int idHoSo,List<HoSoDinhKemBean> listHoSoDinhKem) throws SQLException
	{
		if(!listHoSoDinhKem.isEmpty()){
			for(HoSoDinhKemBean model : listHoSoDinhKem)
			{
				if(model.getMaDinhKem()==0)
				{
					svDonThu.insertHoSoDinhKem(model, idHoSo);
				}
				else
				{
					//svDanhMuc.updateDinhKemHoSo(model);
				}
			}
		}
	}

	public void validateForm()
	{
		if(!layoutNguoiDiKNTC.validateForm())
		{
			validateSuccess = false;
			return;
		}
		if(cbHeaderNguoiBiKNTC.getValue())
		{
			if(!layoutNguoiBiKNTC.validateForm())
			{
				validateSuccess = false;
				return;
			}
		}
		if(cbHeaderCoQuanDaGiaiQuyet.getValue())
		{
			if(!layoutCoQuanDaGiaiQuyet.validateForm())
			{
				validateSuccess = false;
				return;
			}
		}
		if(cbHeaderNguoiUyQuyen.getValue())
		{
			if(!layoutNguoiUyQuyen.validateForm())
			{
				validateSuccess = false;
				return;
			}
		}
		if(!tiepCongDan || (tiepCongDan && !cbTiepCongDanKhongDon.getValue()))
		{
			if(!layoutNoiDungDonThu.validateForm())
			{
				validateSuccess = false;
				return;
			}
			if(dfNgayNhanDon.isEmpty())
			{
				Notification.show("Vui lòng nhập vào ngày nhận đơn",Type.WARNING_MESSAGE);
				dfNgayNhanDon.focus();
				validateSuccess = false;
				return;
			}
			if(dfNgayNhapDon.isEmpty())
			{
				Notification.show("Vui lòng nhập vào ngày nhập đơn",Type.WARNING_MESSAGE);
				dfNgayNhapDon.focus();
				validateSuccess = false;
				return;
			}
			if((int)cmbNguonDonDen.getValue()==4)
			{
				if(txtTenCoQuanChuyenDen.isEmpty())
				{
					Notification.show("Vui lòng chọn cơ quan chuyển đến",Type.WARNING_MESSAGE);
					btnTimKiemCoQuanChuyenDen.focus();
					validateSuccess = false;
					return;
				}
				if(txtSoVanBanChuyenDen.isEmpty())
				{
					Notification.show("Vui lòng nhập vào số văn bản đến",Type.WARNING_MESSAGE);
					txtSoVanBanChuyenDen.focus();
					validateSuccess = false;
					return;
				}
				if(dfNgayPhatHanhVanBanChuyenDen.isEmpty())
				{
					Notification.show("Vui lòng chọn ngày phát hành văn bản đến",Type.WARNING_MESSAGE);
					dfNgayPhatHanhVanBanChuyenDen.focus();
					validateSuccess = false;
					return;
				}
			}
		}
		if(tiepCongDan)
		{
			if(cbTiepCongDanKhongDon.getValue() && layoutNguoiDiKNTC.getTxtKetQuaTiep().isEmpty())
			{
				Notification.show("Vui lòng nhập vào kết quả tiếp công dân",Type.WARNING_MESSAGE);
				validateSuccess = false;
				return;
			}
			if(dfNgayTiepCongDan.isEmpty())
			{
				Notification.show("Vui lòng nhập vào ngày tiếp công dân",Type.WARNING_MESSAGE);
				dfNgayTiepCongDan.focus();
				validateSuccess = false;
				return;
			}
			if(lanhDaoTiep)
			{
				if(cmbTenLanhDaoTiep.getValue()==null)
				{
					Notification.show("Vui lòng chọn lãnh đạo tiếp",Type.WARNING_MESSAGE);
					cmbTenLanhDaoTiep.focus();
					validateSuccess = false;
					return;
				}
				if(cbUyQuyenLanhDao.getValue())
				{
					if(cmbTenLanhDaoUyQuyen.getValue()==null)
					{
						Notification.show("Vui lòng chọn lãnh đạo ủy quyền",Type.WARNING_MESSAGE);
						cmbTenLanhDaoUyQuyen.focus();
						validateSuccess = false;
						return;
					}
					if((long)cmbTenLanhDaoTiep.getValue()==(long)cmbTenLanhDaoUyQuyen.getValue())
					{
						Notification.show("Lãnh đạo tiếp và lãnh đạo ủy quyền không được giống nhau",Type.WARNING_MESSAGE);
						cmbTenLanhDaoTiep.focus();
						validateSuccess = false;
						return;
					}
				}
			}
		}
		validateSuccess = true;
	}
}
