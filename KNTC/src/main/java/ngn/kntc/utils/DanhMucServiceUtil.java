package ngn.kntc.utils;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.ComboBox;

import ngn.kntc.beans.DanhMucBean;
import ngn.kntc.databases.DatabaseServices;
import ngn.kntc.enums.DanhMucTypeEnum;
import ngn.kntc.enums.LoaiDonThuEnum;

public class DanhMucServiceUtil implements Serializable{
	public List<DanhMucBean> getTinhThanhList() throws SQLException{
		List<DanhMucBean> list=new ArrayList<DanhMucBean>();
		Connection cn=DatabaseServices.getConnection();
		String sql="select * from "+DanhMucTypeEnum.diagioi.getName()+" WHERE length(ma) = 3";
		ResultSet rs=cn.createStatement().executeQuery(sql);
		while(rs.next()){
			DanhMucBean model=new DanhMucBean();
			model.setStrMa(rs.getString("ma"));
			model.setName(rs.getString("ten"));
			list.add(model);
		}
		rs.close();
		cn.close();
		return list;
	}

	public List<DanhMucBean> getQuanHuyenList(String MaTT) throws SQLException{
		List<DanhMucBean> list=new ArrayList<DanhMucBean>();
		Connection cn=DatabaseServices.getConnection();
		String sql="SELECT * FROM "+DanhMucTypeEnum.diagioi.getName()+" WHERE LEFT(ma,3) = ? AND LENGTH(ma) = 5";
		PreparedStatement st=cn.prepareStatement(sql);
		st.setString(1, MaTT);
		ResultSet rs=st.executeQuery();
		while(rs.next()){
			DanhMucBean model=new DanhMucBean();
			model.setStrMa(rs.getString("ma"));
			model.setName(rs.getString("ten"));
			list.add(model);
		}
		rs.close();
		cn.close();
		return list;
	}

	public List<DanhMucBean> getPhuongXaList(String MaQH) throws SQLException{
		List<DanhMucBean> list=new ArrayList<DanhMucBean>();
		Connection cn=DatabaseServices.getConnection();
		String sql="SELECT * FROM "+DanhMucTypeEnum.diagioi.getName()+" WHERE LEFT(ma,5) = ? AND LENGTH(ma) = 7";
		PreparedStatement st=cn.prepareStatement(sql);
		st.setString(1, MaQH);
		ResultSet rs=st.executeQuery();
		while(rs.next()){
			DanhMucBean model=new DanhMucBean();
			model.setStrMa(rs.getString("ma"));
			model.setName(rs.getString("ten"));
			list.add(model);
		}
		rs.close();
		cn.close();
		return list;
	}

	public List<DanhMucBean> getLinhVucList(int typeDonThu) throws SQLException
	{
		String strLinhVucSQL = returnLinhVucStringSQL(typeDonThu);
		
		List<DanhMucBean> list = new ArrayList<DanhMucBean>();
		Connection con = DatabaseServices.getConnection();
		
		String sql = "SELECT * from "+DanhMucTypeEnum.linhvuc.getName()+" WHERE 1 = 1";
		if(strLinhVucSQL!=null)
		{
			sql+=" AND ma IN ("+strLinhVucSQL+")";
		}
		else if(strLinhVucSQL==null && typeDonThu == LoaiDonThuEnum.phananhkiennghi.getType()){
			sql+=" AND ma LIKE 'PA%' AND length(ma) > 2";
		}
		sql+=" ORDER BY length(ma) ASC";
		System.out.println(sql);
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			DanhMucBean model = new DanhMucBean();
			model.setStrMa(rs.getString("ma"));
			model.setName(rs.getString("ten"));
			list.add(model);
		}
		rs.close();
		con.close();

		return list;
	}

	public List<String> getLinhVucSubList(String idParent) throws SQLException
	{
		String strLinhVucSQL = returnLinhVucStringSQL(LoaiDonThuEnum.khieunai.getType());

		List<String> list = new ArrayList<String>();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT ma from "+DanhMucTypeEnum.linhvuc.getName()+" WHERE ma LIKE '"+idParent+"%' AND ma IN ("+strLinhVucSQL+")";
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			list.add(rs.getString("ma"));
		}
		rs.close();
		con.close();

		return list;
	}

	public DanhMucBean getLinhVuc(String id) throws SQLException
	{
		DanhMucBean model = new DanhMucBean();
		Connection con = DatabaseServices.getConnection();
		String sql = "SELECT * FROM "+DanhMucTypeEnum.linhvuc.getName()+" WHERE ma = '"+id+"'";
		ResultSet rs = con.createStatement().executeQuery(sql);
		while(rs.next())
		{
			model.setStrMa(rs.getString("ma"));
			model.setName(rs.getString("ten"));
		}
		rs.close();
		con.close();
		return model;
	}

	public List<DanhMucBean> getDanhMucList(String danhMucType,String IdType) throws SQLException
	{
		List<DanhMucBean> list = new ArrayList<DanhMucBean>();
		Connection cn=DatabaseServices.getConnection();
		String sql="SELECT * FROM "+danhMucType;
		PreparedStatement st=cn.prepareStatement(sql);
		ResultSet rs=st.executeQuery();
		while(rs.next()){
			DanhMucBean model = new DanhMucBean();
			if(IdType == "Integer")
				model.setIntMa(rs.getInt("ma"));
			else if(IdType == "String")
				model.setStrMa(rs.getString("ma"));
			model.setName(rs.getString("ten"));
			list.add(model);
		}
		rs.close();
		cn.close();
		return list;
	}

	public static DanhMucBean getDanhMuc(String danhMucType,int maDanhMuc) throws SQLException{
		DanhMucBean model=new DanhMucBean();
		Connection cn=DatabaseServices.getConnection();
		String sql="SELECT * FROM "+danhMucType+" WHERE ma = "+maDanhMuc;
		PreparedStatement st=cn.prepareStatement(sql);
		ResultSet rs=st.executeQuery();
		while(rs.next()){
			model.setIntMa(rs.getInt("ma"));
			model.setName(rs.getString("ten"));
		}
		rs.close();
		cn.close();
		return model;
	}

	public static DanhMucBean getDanhMuc(String danhMucType,String maDanhMuc) throws SQLException{
		DanhMucBean model=new DanhMucBean();
		Connection cn=DatabaseServices.getConnection();
		String sql="SELECT * FROM "+danhMucType+" WHERE ma = '"+maDanhMuc+"'";
		PreparedStatement st=cn.prepareStatement(sql);
		ResultSet rs=st.executeQuery();
		while(rs.next()){
			model.setStrMa(rs.getString("ma"));
			model.setName(rs.getString("ten"));
		}
		rs.close();
		cn.close();
		return model;
	}

	public void setValueDefaultComboboxDiaGioi(ComboBox cmbTinhThanh,ComboBox cmbQuanHuyen,ComboBox cmbPhuongXa,String defaultTinhThanh) throws SQLException
	{
		List<DanhMucBean> list = getTinhThanhList();

		for(DanhMucBean model : list)
		{
			cmbTinhThanh.addItem(model.getStrMa());
			cmbTinhThanh.setItemCaption(model.getStrMa(), model.getName());
		}

		cmbTinhThanh.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty().getValue()==null)
				{
					cmbQuanHuyen.setValue(null);
					cmbQuanHuyen.removeAllItems();
					return;
				}
				String matt=event.getProperty().getValue().toString();
				String maqh = null;
				String mapx = null;
				List<DanhMucBean> quanhuyen;
				List<DanhMucBean> phuongxa;
				try {
					cmbQuanHuyen.removeAllItems();
					quanhuyen= getQuanHuyenList(matt);
					for(DanhMucBean index: quanhuyen){
						cmbQuanHuyen.addItem(index.getStrMa());
						cmbQuanHuyen.setItemCaption(index.getStrMa(), index.getName());
						maqh=index.getStrMa();
					}
					cmbPhuongXa.removeAllItems();
					phuongxa= getPhuongXaList(maqh);
					for(DanhMucBean index : phuongxa){
						cmbPhuongXa.addItem(index.getStrMa());
						cmbPhuongXa.setItemCaption(index.getStrMa(),index.getName());
						mapx = index.getStrMa();
					}
					cmbQuanHuyen.select(maqh);
					cmbPhuongXa.select(mapx);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		cmbQuanHuyen.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				String maqh=(String)cmbQuanHuyen.getValue();
				List<DanhMucBean> phuongxa;
				try {
					cmbPhuongXa.removeAllItems();
					phuongxa= getPhuongXaList(maqh);
					for(DanhMucBean index : phuongxa){
						cmbPhuongXa.addItem(index.getStrMa());
						cmbPhuongXa.setItemCaption(index.getStrMa(),index.getName());
						cmbPhuongXa.setValue(index.getStrMa());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		//cmbTinhThanh.select(defaultTinhThanh);
	}

	public void setValueDefaultForComboboxDanhMuc(ComboBox cmb,String typeDanhMuc,String idTypeDanhMuc) throws SQLException
	{
		List<DanhMucBean> list = getDanhMucList(typeDanhMuc, idTypeDanhMuc);
		for(DanhMucBean model : list)
		{
			if(idTypeDanhMuc=="Integer")
			{
				cmb.addItem(model.getIntMa());
				cmb.setItemCaption(model.getIntMa(), model.getName());
			}
			else if(idTypeDanhMuc == "String")
			{
				cmb.addItem(model.getStrMa());
				cmb.setItemCaption(model.getStrMa(), model.getName());
			}
		}
	}
	
	public String returnLinhVucStringSQL(int type)
	{
		List<String> listLinhVucLastChild = new ArrayList<String>();
		switch (type) {
		case 1:
			Collections.addAll(listLinhVucLastChild, "KN.01.01.01", "KN.01.01.02", "KN.01.01.08", "KN.01.02.02", "KN.01.02.03", "KN.01.02.08", "KN.01.02.15", "KN.01.02.16", "KN.01.02.18", "KN.01.03","KN.01.05");
			Collections.addAll(listLinhVucLastChild, "KN.02.01","KN.02.02","KN.02.03","KN.02.04","KN.02.09");
			Collections.addAll(listLinhVucLastChild, "KN.03.01","KN.03.02","KN.05");
			break;
		case 2:
			Collections.addAll(listLinhVucLastChild, "TC.01.01.01", "TC.01.01.02", "TC.01.01.03", "TC.01.01.04", "TC.01.01.05", "TC.01.01.06","TC.01.02","TC.01.07");
			Collections.addAll(listLinhVucLastChild, "TC.02.01","TC.02.02","TC.02.09");
			Collections.addAll(listLinhVucLastChild, "TC.03.01","TC.03.02","TC.09");
			break;
		case 3:
			return null;
		}
		
		List<String> listNewElement = new ArrayList<String>();
		for(String linhVuc : listLinhVucLastChild)
		{
			String[] strTmp = linhVuc.split("\\.");
			String linhVucTmp = "";
			
			if(type==LoaiDonThuEnum.khieunai.getType())
				linhVucTmp="KN";
			if(type==LoaiDonThuEnum.tocao.getType())
				linhVucTmp="TC";
			for (int i = 1; i < strTmp.length; i++) {
				linhVucTmp+="."+strTmp[i];
				if(!listLinhVucLastChild.contains(String.valueOf(linhVucTmp)) || !listNewElement.contains(String.valueOf(linhVucTmp)))
					listNewElement.add(linhVucTmp);
			}
		}
		listLinhVucLastChild.addAll(listNewElement);
		String strLinhVucSQL = "";
		for(String linhvuc : listLinhVucLastChild)
		{
			strLinhVucSQL+="'"+linhvuc+"',";
		}
		return strLinhVucSQL = strLinhVucSQL.substring(0,strLinhVucSQL.length()-1);
	}
}
