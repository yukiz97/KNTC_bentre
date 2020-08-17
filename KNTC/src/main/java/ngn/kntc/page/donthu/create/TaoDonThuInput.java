package ngn.kntc.page.donthu.create;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;

import ngn.kntc.beans.DoiTuongBiKNTCBean;
import ngn.kntc.beans.DoiTuongDiKNTCBean;
import ngn.kntc.beans.DoiTuongUyQuyenBean;
import ngn.kntc.beans.DonThuBean;
import ngn.kntc.beans.SoTiepCongDanBean;
import ngn.kntc.beans.ThongTinDonThuBean;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.utils.DanhMucServiceUtil;
import ngn.kntc.utils.DonThuServiceUtil;
import ngn.kntc.utils.LiferayServiceUtil;
import ngn.kntc.utils.SessionUtil;

public class TaoDonThuInput extends TaoDonThuEvents{
	public TaoDonThuInput(int idDonThu)
	{
		try {
			this.themMoi = false;
			this.idDonThu = idDonThu;
			this.folderTmp = idDonThu+"";
			layoutNoiDungDonThu.setFolderTmp(folderTmp);
			hHeaderNguoiBiKNTC.setEnabled(false);
			hHeaderCoQuanDaGiaiQuyet.setEnabled(false);
			hHeaderNguoiUyQuyen.setEnabled(false);
			btnTiepNhanDonGianTiep.click();
			loadDataDonThu();
			disableComponentInEditMode();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public TaoDonThuInput(int idTiepCongDan,int idDonThu) {
		try {
			this.themMoi = false;
			this.idTiepCongDan = idTiepCongDan;
			this.tiepCongDan = true;
			this.folderTmp = idDonThu+"";
			layoutNoiDungDonThu.setFolderTmp(folderTmp);
			hHeaderNguoiBiKNTC.setEnabled(false);
			hHeaderCoQuanDaGiaiQuyet.setEnabled(false);
			hHeaderNguoiUyQuyen.setEnabled(false);
			SoTiepCongDanBean modelTCD = svTCD.getSoTiepCongDan(this.idTiepCongDan);
			if(modelTCD.getMaLanhDaoTiep()!=0)
			{
				btnLanhDaoTiep.click();
				cmbTenLanhDaoTiep.setValue(modelTCD.getMaLanhDaoTiep());
				cbTiepDinhKy.setValue(modelTCD.isTiepDinhKy());
				cbUyQuyenLanhDao.setValue(modelTCD.isUyQuyenLanhDao());
				if(modelTCD.isUyQuyenLanhDao())
				{
					cmbTenLanhDaoUyQuyen.setValue(modelTCD.getMaLanhDaoUyQuyen());
				}
			}
			else
			{
				btnTiepCongDan.click();
			}
			if(idDonThu!=0)
			{
				this.idDonThu = idDonThu;
				loadDataDonThu();
			}
			else
			{
				cbTiepCongDanKhongDon.setValue(true);
				/* Cơ quan đã giải quyết */
				if(modelTCD.getMaCoQuanDaGiaiQuyet()!=null)
				{
					cbHeaderCoQuanDaGiaiQuyet.setValue(true);
					layoutCoQuanDaGiaiQuyet.setFieldsCQDGQ(modelTCD);
				}

				/* Đối tượng bị KNTC */
				if(modelTCD.getIdNguoiBiKNTC()!=0)
				{
					cbHeaderNguoiBiKNTC.setValue(true);
					DoiTuongBiKNTCBean modelDTBiKNTC = svDonThu.getDoiTuongBiKNTC(modelTCD.getIdNguoiBiKNTC());
					layoutNguoiBiKNTC.getCmbLoaiNguoiKTNC().setValue(modelTCD.getLoaiNguoiBiKNTC());
					layoutNguoiBiKNTC.setFieldsBiKNTC(modelDTBiKNTC);
				}

				/* Đối tượng ủy quyền */
				if(modelTCD.getIdNguoiUyQuyen()!=0)
				{
					layoutNguoiUyQuyen.getCmbLoaiNguoiUyQuyen().setValue(modelTCD.getLoaiNguoiUyQuyen());
					cbHeaderNguoiUyQuyen.setValue(true);
					DoiTuongUyQuyenBean modelDTUyQuyen = svDonThu.getDoiTuongUyQuyen(modelTCD.getIdNguoiUyQuyen());
					layoutNguoiUyQuyen.setFieldsNguoiUyQuyen(modelDTUyQuyen);
				}
				
				layoutNoiDungDonThu.getCmbLoaiDonThu().setValue(modelTCD.getLoaiLinhVuc());
				layoutNoiDungDonThu.setListLinhVuc(svTCD.getLinhVucList(idTiepCongDan));
				layoutNoiDungDonThu.loadDisplayLinhVuc();
			}
			dfNgayTiepCongDan.setValue(modelTCD.getNgayTiepCongDan());
			layoutNguoiDiKNTC.getTxtNoiDungTiep().setValue(modelTCD.getNoiDungTiepCongDan());
			layoutNguoiDiKNTC.getTxtKetQuaTiep().setValue(modelTCD.getKetQuaTiepCongDan());
			loadDataChuTheDonThu(modelTCD, null);
			disableComponentInEditMode();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public TaoDonThuInput() {

	}
	
	public void loadThongTinDonThu() throws Exception
	{
		ThongTinDonThuBean model = svDonThu.getThongTinDonThu(idDonThu, SessionUtil.getOrgId());
		
		cmbNguonDonDen.select(model.getNguonDonDen());
		if(model.getMaCoQuanChuyenDen()!=null)
		{
			txtTenCoQuanChuyenDen.setValue(DanhMucServiceUtil.getDanhMuc(DanhMucTypeEnum.coquan.getName(), model.getMaCoQuanChuyenDen()).getName());
			txtTenCoQuanChuyenDen.setId(model.getMaCoQuanChuyenDen());
			txtSoVanBanChuyenDen.setValue(model.getSoVanBanDen());
			dfNgayPhatHanhVanBanChuyenDen.setValue(model.getNgayPhatHanhVanBanDen());
		}
	}
	
	public void loadDataDonThu() throws Exception
	{
		loadThongTinDonThu();
		DonThuBean modelDonThu = svDonThu.getDonThu(idDonThu);
		idGanVuViecLienThong=modelDonThu.getIdHoSoLienThong();
		if(this.idTiepCongDan==-1)
			loadDataChuTheDonThu(null, modelDonThu);
		/* Thông tin đơn */
		txtNguoiNhapDon.setValue(UserLocalServiceUtil.getUser(modelDonThu.getUserNhapDon()).getFirstName());
		txtTenCoQuanTiepNhan.setValue(OrganizationLocalServiceUtil.getOrganization(LiferayServiceUtil.getMasterOrgByUser(modelDonThu.getUserNhapDon())).getName());
		dfNgayNhapDon.setValue(modelDonThu.getNgayNhapDon());
		dfNgayNhanDon.setValue(modelDonThu.getNgayNhanDon());
		/* Nội dung đơn thư */
		layoutNoiDungDonThu.getCbDonKhongDuDKXL().setValue(modelDonThu.isDonKhongDuDieuKienXuLy());
		layoutNoiDungDonThu.getCmbLoaiDonThu().setValue(modelDonThu.getLoaiDonThu());
		layoutNoiDungDonThu.setListLinhVuc(svDonThu.getLinhVucList(idDonThu));
		layoutNoiDungDonThu.setListHoSoDinhKem(svDonThu.getDinhKemHoSoList(idDonThu));
		layoutNoiDungDonThu.loadDisplayDinhKem();
		layoutNoiDungDonThu.loadDisplayLinhVuc();
		layoutNoiDungDonThu.getTxtNoiDungDonThu().setValue(modelDonThu.getNoiDungDonThu());

		/* Cơ quan đã giải quyết */
		if(modelDonThu.getMaCoQuanDaGiaiQuyet()!=null)
		{
			cbHeaderCoQuanDaGiaiQuyet.setValue(true);
			layoutCoQuanDaGiaiQuyet.setFieldsCQDGQ(modelDonThu);
		}

		/* Đối tượng bị KNTC */
		if(modelDonThu.getMaDoiTuongBiKNTC()!=0)
		{
			cbHeaderNguoiBiKNTC.setValue(true);
			DoiTuongBiKNTCBean modelDTBiKNTC = svDonThu.getDoiTuongBiKNTC(modelDonThu.getMaDoiTuongBiKNTC());
			layoutNguoiBiKNTC.getCmbLoaiNguoiKTNC().setValue(modelDonThu.getLoaiNguoiBiKNTC());
			layoutNguoiBiKNTC.setFieldsBiKNTC(modelDTBiKNTC);
		}

		/* Đối tượng ủy quyền */
		if(modelDonThu.getMaDoiTuongUyQuyen()!=0)
		{
			layoutNguoiUyQuyen.getCmbLoaiNguoiUyQuyen().setValue(modelDonThu.getLoaiNguoiUyQuyen());
			cbHeaderNguoiUyQuyen.setValue(true);
			DoiTuongUyQuyenBean modelDTUyQuyen = svDonThu.getDoiTuongUyQuyen(modelDonThu.getMaDoiTuongUyQuyen());
			layoutNguoiUyQuyen.setFieldsNguoiUyQuyen(modelDTUyQuyen);
		}
	}

	public void loadDataChuTheDonThu(SoTiepCongDanBean modelTCD,DonThuBean modelDonThu) throws Exception
	{
		/* 
		 * type = 1: Tiếp công dân
		 * type = 2: Đơn thư
		 *  */
		List<DoiTuongDiKNTCBean> list = new ArrayList<DoiTuongDiKNTCBean>();
		if(modelTCD != null)
		{
			list = svTCD.getNguoiDaiDienTiepCongDan(modelTCD.getMaSoTiepCongDan());
			layoutNguoiDiKNTC.getCmbLoaiNguoiKTNC().setValue(modelTCD.getLoaiNguoiDiKNTC());
			switch (modelTCD.getLoaiNguoiDiKNTC()) {
			case 1:
				layoutNguoiDiKNTC.setFieldsNguoiDaiDien(svDonThu.getDoiTuongDiKNTC(list.get(0).getMaDoiTuong()));
				break;
			case 2:
				layoutNguoiDiKNTC.getTxtSoNguoi().setValue(modelTCD.getSoNguoiDiKNTC()+"");
				Map<Integer, DoiTuongDiKNTCBean> listNguoiDaiDien = new HashMap<Integer, DoiTuongDiKNTCBean>();
				int i = 0;
				for(DoiTuongDiKNTCBean model : list)
				{
					listNguoiDaiDien.put(++i, model);
				}
				layoutNguoiDiKNTC.setIndexList(i);
				layoutNguoiDiKNTC.setListNguoiDaiDien(listNguoiDaiDien);
				layoutNguoiDiKNTC.loadDisplayNguoiDaiDien();
				break;
			case 3:
				layoutNguoiDiKNTC.setFieldsNguoiDaiDien(svDonThu.getDoiTuongDiKNTC(list.get(0).getMaDoiTuong()));
				layoutNguoiDiKNTC.getTxtTenCoQuan().setValue(modelTCD.getTenCoQuanDiKNTC());
				layoutNguoiDiKNTC.getTxtDiaChiCoQuan().setValue(modelTCD.getDiaChiCoQuanDiKNTC());
				break;
			}
		}
		else if(modelTCD == null && modelDonThu !=null)
		{
			layoutNguoiDiKNTC.getCbDonNacDanh().setValue(modelDonThu.isDonNacDanh());
			list = DonThuServiceUtil.getNguoiDaiDienDonThu(modelDonThu.getMaDonThu());
			layoutNguoiDiKNTC.getCmbLoaiNguoiKTNC().setValue(modelDonThu.getLoaiNguoiDiKNTC());
			switch (modelDonThu.getLoaiNguoiDiKNTC()) {
			case 1:
				layoutNguoiDiKNTC.setFieldsNguoiDaiDien(svDonThu.getDoiTuongDiKNTC(list.get(0).getMaDoiTuong()));
				break;
			case 2:
				layoutNguoiDiKNTC.getTxtSoNguoi().setValue(modelDonThu.getSoNguoiDiKNTC()+"");
				Map<Integer, DoiTuongDiKNTCBean> listNguoiDaiDien = new HashMap<Integer, DoiTuongDiKNTCBean>();
				int i = 0;
				for(DoiTuongDiKNTCBean model : list)
				{
					listNguoiDaiDien.put(++i, model);
				}
				layoutNguoiDiKNTC.setIndexList(i);
				layoutNguoiDiKNTC.setListNguoiDaiDien(listNguoiDaiDien);
				layoutNguoiDiKNTC.loadDisplayNguoiDaiDien();
				break;
			case 3:
				layoutNguoiDiKNTC.setFieldsNguoiDaiDien(svDonThu.getDoiTuongDiKNTC(list.get(0).getMaDoiTuong()));
				layoutNguoiDiKNTC.getTxtTenCoQuan().setValue(modelDonThu.getTenCoQuanDiKNTC());
				layoutNguoiDiKNTC.getTxtDiaChiCoQuan().setValue(modelDonThu.getDiaChiCoQuanDiKNTC());
				break;
			}
		}
	}
	
	public void disableComponentInEditMode()
	{
		hPhuongThucTiepNhan.setVisible(false);
		/* Tiếp công dân */
		cbTiepCongDanKhongDon.setEnabled(false);
		if(this.idTiepCongDan!=-1)
			cmbNguonDonDen.setEnabled(false);
		/* Đơn thư */
		if((int)cmbNguonDonDen.getValue()==4)
			cmbNguonDonDen.setEnabled(false);
		layoutNguoiDiKNTC.getCbDonNacDanh().setEnabled(false);
		/* Người đi KNTC */
		layoutNguoiDiKNTC.getCmbLoaiNguoiKTNC().setEnabled(false);
		if((int)layoutNguoiDiKNTC.getCmbLoaiNguoiKTNC().getValue()!=2)
			layoutNguoiDiKNTC.getBtnSearchNguoiDaiDien().setVisible(false);
		// Nội dung đơn
		layoutNoiDungDonThu.getCbDonKhongDuDKXL().setEnabled(false);
		layoutNoiDungDonThu.getBtnKiemTraDonThu().setVisible(false);
		layoutNoiDungDonThu.getCmbLoaiDonThu().setEnabled(false);
		// Người bị KNTC
		layoutNguoiBiKNTC.getCmbLoaiNguoiKTNC().setEnabled(false);
		layoutNguoiBiKNTC.getBtnSearchNguoiBiKNTC().setVisible(false);
		// Người được ủy quyền
		layoutNguoiUyQuyen.getCmbLoaiNguoiUyQuyen().setEnabled(false);
		
		btnReset.setVisible(false);
	}
}
