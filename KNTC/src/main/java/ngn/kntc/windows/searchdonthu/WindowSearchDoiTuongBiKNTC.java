package ngn.kntc.windows.searchdonthu;

import java.util.List;

import ngn.kntc.beans.DoiTuongBiKNTCBean;
import ngn.kntc.modules.DonThuModule;
import ngn.kntc.utils.DonThuServiceUtil;

import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class WindowSearchDoiTuongBiKNTC extends WindowSearchDoiTuong{
	DonThuServiceUtil svDonThu = new DonThuServiceUtil();
	
	int idDoiTuong = -1;
	
	final String STT = "STT";
	final String HOTEN = "Họ tên";
	final String SODINHDANH = "Số định danh";
	final String NOICONGTAC = "Nơi công tác";
	final String CHUCVU = "Chức vụ";
	final String TENCOQUAN = "Tên cơ quan";
	final String DIACHI = "Địa chỉ chi tiết";
	final String CHON = "Chọn";
	
	int loaiDoiTuong = -1;
	
	public WindowSearchDoiTuongBiKNTC(int loaiDoiTuong) {
		this.loaiDoiTuong = loaiDoiTuong;
		configComponent();
		buildTable();
		
		this.setCaption(FontAwesome.SEARCH.getHtml()+" Tìm kiếm thông tin đối tượng bị khiếu tố");
	}

	private void configComponent() {
		btnSearch.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					loadData(txtSearch.getValue().trim());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void buildTable() {
		super.buildTable();
		
		container.addContainerProperty(STT, Integer.class, null);
		if(loaiDoiTuong==1)
		{
			container.addContainerProperty(HOTEN, String.class, null);
			container.addContainerProperty(SODINHDANH, String.class, null);
			container.addContainerProperty(NOICONGTAC, String.class, null);
			container.addContainerProperty(CHUCVU, String.class, null);
			container.addContainerProperty(DIACHI, Label.class, null);
			container.addContainerProperty(CHON, Button.class, null);
		}
		else if(loaiDoiTuong==2)
		{
			container.addContainerProperty(TENCOQUAN, String.class, null);
			container.addContainerProperty(DIACHI, Label.class, null);
			container.addContainerProperty(CHON, Button.class, null);
		}
		
		tblDanhSach.setColumnAlignment(STT, Align.CENTER);
		tblDanhSach.setColumnAlignment(CHON, Align.CENTER);

		tblDanhSach.setContainerDataSource(container);
	}
	
	@SuppressWarnings("unchecked")
	public void loadData(String keyWord) throws Exception
	{
		container.removeAllItems();
		List<DoiTuongBiKNTCBean> listDoiTuong = svDonThu.searchNguoiBiKNTC(keyWord,loaiDoiTuong);
		int i = 0;
		for(DoiTuongBiKNTCBean model : listDoiTuong)
		{
			Button btnChon = new Button("",FontAwesome.CHECK_CIRCLE);
			btnChon.addStyleName(ValoTheme.BUTTON_PRIMARY);
			btnChon.setId("-1");
			listButton.add(btnChon);
			
			Item item = container.addItem(++i);
			item.getItemProperty(STT).setValue(i);
			if(loaiDoiTuong==1)
			{
				item.getItemProperty(HOTEN).setValue(model.getHoTen());
				item.getItemProperty(SODINHDANH).setValue(model.getSoDinhDanhCaNhan());
				item.getItemProperty(NOICONGTAC).setValue(model.getNoiCongTac());
				item.getItemProperty(CHUCVU).setValue(model.getChucVu());
				item.getItemProperty(DIACHI).setValue(new Label(DonThuModule.returnDiaChiChiTiet(model.getDiaChiChiTiet(), model.getMaTinh(), model.getMaHuyen(), model.getMaXa())));
			}
			else
			{
				item.getItemProperty(TENCOQUAN).setValue(model.getTenCoQuanToChuc());
				item.getItemProperty(DIACHI).setValue(new Label(DonThuModule.returnDiaChiChiTiet(model.getDiaChiChiTietCoQuan(), model.getMaTinhCoQuan(), model.getMaHuyenCoQuan(), model.getMaXaCoQuan())));
			}
			item.getItemProperty(CHON).setValue(btnChon);
		
			btnChon.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					if(btnChon.getId()=="-1")
					{
						idDoiTuong = model.getMaDoiTuong();
						btnChon.setId(model.getMaDoiTuong()+"");
						btnChon.addStyleName(ValoTheme.BUTTON_DANGER);
						btnChon.setIcon(FontAwesome.REMOVE);
						btnOk.setEnabled(true);
						
						for(Button btn : listButton)
						{
							btn.setEnabled(false);
						}
						btnChon.setEnabled(true);
					}
					else
					{
						idDoiTuong = -1;
						btnChon.setId("-1");
						btnChon.removeStyleName(ValoTheme.BUTTON_DANGER);
						btnChon.setIcon(FontAwesome.CHECK_CIRCLE);
						btnOk.setEnabled(false);
						for(Button btn : listButton)
						{
							btn.setEnabled(true);
						}
					}
				}
			});
		}
		tblDanhSach.setContainerDataSource(container);
		this.center();
	}

	public int getIdDoiTuong() {
		return idDoiTuong;
	}

	public void setIdDoiTuong(int idDoiTuong) {
		this.idDoiTuong = idDoiTuong;
	}
}
