package ngn.kntc.page.home;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ngn.kntc.beans.LienThongMaVuViecAndMaQuyetDinhAndMaThiHanhBean;
import ngn.kntc.beans.LienThongMaVuViecAndMaQuyetDinhBean;
import ngn.kntc.lienthongttcp.LienThongLocalServicesUtil;

import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class TinhTrangLienThongLayout extends VerticalLayout{
	Label lblMainCaption = new Label("Tình trạng liên thông",ContentMode.HTML);
	Label lblSubCaption = new Label("+ Xem tình trạng liên thông đơn thư, kết quả xử lý, quyết định thụ lý, quyết định giải quyết và thi hành giải quyết của phần mềm và cơ sở dữ liệu thanh tra chính phủ",ContentMode.HTML);
	DateField dfNgayXem = new DateField("",new Date());
	
	VerticalLayout vDisplayResult = new VerticalLayout();
	
	String styleNumberCount = "display: inline-block; background: #398ed2; padding: 0px 7px; font-weight: bold; color: #ffff; border-radius: 50%; box-shadow: 0px 0px 1px 1px #e0e0e0;";
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	LienThongLocalServicesUtil svLienThong = new LienThongLocalServicesUtil();
	
	public TinhTrangLienThongLayout() {
		buildLayout();
		configComponent();
	}

	private void buildLayout() {
		this.addComponent(lblMainCaption);
		this.addComponent(lblSubCaption);
		this.addComponent(dfNgayXem);
		this.addComponent(vDisplayResult);
		
		lblMainCaption.addStyleName("lbl-caption-main");
		
		this.setWidth("100%");
		this.setMargin(new MarginInfo(false,true,true,true));
		this.setSpacing(true);
		
		buildLayoutDisplay();
	}

	private void configComponent() {
		dfNgayXem.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				try {
					loadData();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	private void buildLayoutDisplay()
	{
		vDisplayResult.setWidth("100%");
		vDisplayResult.setSpacing(true);
		
		try {
			loadData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadData() throws SQLException
	{
		vDisplayResult.removeAllComponents();
		
		vDisplayResult.addComponent(buildResultVuViec());
		vDisplayResult.addComponent(buildResultKQXL());
		vDisplayResult.addComponent(buildResultQDTL());
		vDisplayResult.addComponent(buildResultQDGQ());
		vDisplayResult.addComponent(buildResultTHGQ());
	}

	private ResponsiveLayout buildResultVuViec() throws SQLException {
		List<String> list = svLienThong.getDonThuLienThongByDate(dfNgayXem.getValue());
		ResponsiveLayout vTmp = new ResponsiveLayout();
		Label lblTomTat = new Label("<b style='font-size: 16px; margin-bottom: 7px;'>Tổng số đơn thư:</b> <span style='"+styleNumberCount+"'>"+list.size()+"</span>",ContentMode.HTML);
		
		vTmp.addComponent(lblTomTat);
		int i = 1;
		String date = sdf.format(dfNgayXem.getValue());
		for(String id : list)
		{
			Label lblSTT = new Label("<b>STT:</b> "+i++,ContentMode.HTML);
			Label lblMaVuViec = new Label("<b>Mã vụ việc:</b> "+id,ContentMode.HTML);
			Label lblNgayCapNhat= new Label("<b>Ngày cập nhật:</b> "+date,ContentMode.HTML);
			
			ResponsiveRow rowTmp = new ResponsiveRow();
			rowTmp.addColumn().withComponent(lblSTT).withDisplayRules(12, 12, 2, 2);
			rowTmp.addColumn().withComponent(lblMaVuViec).withDisplayRules(12, 12, 8, 8);
			rowTmp.addColumn().withComponent(lblNgayCapNhat).withDisplayRules(12, 12, 2, 2);
			
			vTmp.addRow(rowTmp);
		}
		
		vTmp.setWidth("100%");
		vTmp.addStyleName("block-tinhtrang-lienthong");
		
		return vTmp;
	}
	
	private ResponsiveLayout buildResultKQXL() throws SQLException {
		List<String> list = svLienThong.getKQXLLienThongByDate(dfNgayXem.getValue());

		ResponsiveLayout vTmp = new ResponsiveLayout();
		Label lblTomTat = new Label("<b style='font-size: 16px; margin-bottom: 7px;'>Tổng số kết quả xử lý:</b> <span style='"+styleNumberCount+"'>"+list.size()+"</span>",ContentMode.HTML);
		
		vTmp.addComponent(lblTomTat);
		
		int i = 1;
		String date = sdf.format(dfNgayXem.getValue());
		for(String id : list)
		{
			Label lblSTT = new Label("<b>STT:</b> "+i++,ContentMode.HTML);
			Label lblMaVuViec = new Label("<b>Mã vụ việc cập nhật kết quả xử lý:</b> "+id,ContentMode.HTML);
			Label lblNgayCapNhat= new Label("<b>Ngày cập nhật:</b> "+date,ContentMode.HTML);
			
			ResponsiveRow rowTmp = new ResponsiveRow();
			rowTmp.addColumn().withComponent(lblSTT).withDisplayRules(12, 12, 2, 2);
			rowTmp.addColumn().withComponent(lblMaVuViec).withDisplayRules(12, 12, 8, 8);
			rowTmp.addColumn().withComponent(lblNgayCapNhat).withDisplayRules(12, 12, 2, 2);
			
			vTmp.addRow(rowTmp);
		}
		
		vTmp.setWidth("100%");
		vTmp.addStyleName("block-tinhtrang-lienthong");
		
		return vTmp;
	}
	
	private ResponsiveLayout buildResultQDTL() throws SQLException {
		List<String> list = svLienThong.getQDTLLienThongByDate(dfNgayXem.getValue());
		
		ResponsiveLayout vTmp = new ResponsiveLayout();
		Label lblTomTat = new Label("<b style='font-size: 16px; margin-bottom: 7px;'>Tổng số quyết định thụ lý:</b> <span style='"+styleNumberCount+"'>"+list.size()+"</span>",ContentMode.HTML);
		
		vTmp.addComponent(lblTomTat);
		
		int i = 1;
		String date = sdf.format(dfNgayXem.getValue());
		for(String id : list)
		{
			Label lblSTT = new Label("<b>STT:</b> "+i++,ContentMode.HTML);
			Label lblMaVuViec = new Label("<b>Mã vụ việc cập nhật quyết định thụ lý:</b> "+id,ContentMode.HTML);
			Label lblNgayCapNhat= new Label("<b>Ngày cập nhật:</b> "+date,ContentMode.HTML);
			
			ResponsiveRow rowTmp = new ResponsiveRow();
			rowTmp.addColumn().withComponent(lblSTT).withDisplayRules(12, 12, 2, 2);
			rowTmp.addColumn().withComponent(lblMaVuViec).withDisplayRules(12, 12, 8, 8);
			rowTmp.addColumn().withComponent(lblNgayCapNhat).withDisplayRules(12, 12, 2, 2);
			
			vTmp.addRow(rowTmp);
		}
		
		vTmp.setWidth("100%");
		vTmp.addStyleName("block-tinhtrang-lienthong");
		
		return vTmp;
	}
	
	private ResponsiveLayout buildResultQDGQ() throws SQLException {
		List<LienThongMaVuViecAndMaQuyetDinhBean> list = svLienThong.getQDGQLienThongByDate(dfNgayXem.getValue());

		ResponsiveLayout vTmp = new ResponsiveLayout();
		Label lblTomTat = new Label("<b style='font-size: 16px; margin-bottom: 7px;'>Tổng số quyết định giải quyết:</b> <span style='"+styleNumberCount+"'>"+list.size()+"</span>",ContentMode.HTML);
		
		vTmp.addComponent(lblTomTat);
		
		int i = 1;
		String date = sdf.format(dfNgayXem.getValue());
		
		for(LienThongMaVuViecAndMaQuyetDinhBean model : list)
		{
			Label lblSTT = new Label("<b>STT:</b> "+i++,ContentMode.HTML);
			Label lblMaVuViec = new Label("<b>Mã vụ việc:</b> "+model.getIdVuViec(),ContentMode.HTML);
			Label lblMaQuyetDinh = new Label("<b>Mã quyết định:</b> "+model.getIdQuyetDinh(),ContentMode.HTML);
			Label lblNgayCapNhat= new Label("<b>Ngày cập nhật:</b> "+date,ContentMode.HTML);
			
			ResponsiveRow rowTmp = new ResponsiveRow();
			rowTmp.addColumn().withComponent(lblSTT).withDisplayRules(12, 12, 2, 2);
			rowTmp.addColumn().withComponent(lblMaVuViec).withDisplayRules(12, 12, 4, 4);
			rowTmp.addColumn().withComponent(lblMaQuyetDinh).withDisplayRules(12, 12, 4, 4);
			rowTmp.addColumn().withComponent(lblNgayCapNhat).withDisplayRules(12, 12, 2, 2);
			
			vTmp.addRow(rowTmp);
		}
		
		vTmp.setWidth("100%");
		vTmp.addStyleName("block-tinhtrang-lienthong");
		
		return vTmp;
	}
	
	private ResponsiveLayout buildResultTHGQ() throws SQLException {
		List<LienThongMaVuViecAndMaQuyetDinhAndMaThiHanhBean> list = svLienThong.getTHGQLienThongByDate(dfNgayXem.getValue());
		
		ResponsiveLayout vTmp = new ResponsiveLayout();
		Label lblTomTat = new Label("<b style='font-size: 16px; margin-bottom: 7px;'>Tổng số thi hành giải quyết:</b> <span style='"+styleNumberCount+"'>"+list.size()+"</span>",ContentMode.HTML);
		
		vTmp.addComponent(lblTomTat);
		
		int i = 1;
		String date = sdf.format(dfNgayXem.getValue());
		
		for(LienThongMaVuViecAndMaQuyetDinhAndMaThiHanhBean model : list)
		{
			Label lblSTT = new Label("<b>STT:</b> "+i++,ContentMode.HTML);
			Label lblMaVuViec = new Label("<b>Mã vụ việc:</b> "+model.getIdVuViec(),ContentMode.HTML);
			Label lblMaQuyetDinh = new Label("<b>Mã quyết định:</b> "+model.getIdQuyetDinh(),ContentMode.HTML);
			Label lblMaThiHanh = new Label("<b>Mã thi hành:</b> "+model.getIdThiHanh(),ContentMode.HTML);
			Label lblNgayCapNhat= new Label("<b>Ngày cập nhật:</b> "+date,ContentMode.HTML);
			
			ResponsiveRow rowTmp = new ResponsiveRow();
			rowTmp.addColumn().withComponent(lblSTT).withDisplayRules(12, 12, 1, 1);
			rowTmp.addColumn().withComponent(lblMaVuViec).withDisplayRules(12, 12, 3, 3);
			rowTmp.addColumn().withComponent(lblMaQuyetDinh).withDisplayRules(12, 12, 3, 3);
			rowTmp.addColumn().withComponent(lblMaThiHanh).withDisplayRules(12, 12, 3, 3);
			rowTmp.addColumn().withComponent(lblNgayCapNhat).withDisplayRules(12, 12, 2, 2);
			
			vTmp.addRow(rowTmp);
		}
		
		vTmp.setWidth("100%");
		vTmp.addStyleName("block-tinhtrang-lienthong");
		
		return vTmp;
	}
}
