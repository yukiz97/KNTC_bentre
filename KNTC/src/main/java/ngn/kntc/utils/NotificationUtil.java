package ngn.kntc.utils;

import com.vaadin.server.Page;

import ngn.kntc.UI.kntcUI;

public class NotificationUtil {
	public static enum Id{;
		public static final String sumtcd="sumtcd";
		public static final String tiepcongdan="tiepcongdan";
		public static final String sumxl="sumxl";
		public static final String datiepnhan="datiepnhan";
		public static final String donvikhacchuyenden="donvikhacchuyenden";
		public static final String dacokq="dacokq";
		public static final String sumtl="sumtl";
		public static final String canthuly="canthuly";
		public static final String dathuly="dathuly";
		public static final String sumgq="sumgq";
		public static final String cangiaiquyet="cangiaiquyet";
		public static final String dangthihanh="dangthihanh";
		public static final String sumketthuc="sumketthuc";
		public static final String daketthuc="daketthuc";
		public static final String phuctapkeodai="phuctapkeodai";
		public static final String donduocrut="donduocrut";
		public static final String sumchuyen="sumchuyen";
		public static final String chuyenchuagq="chuyenchuagq";
		public static final String chuyendagq="chuyendagq";
		public static final String chuyenluutru="chuyenluutru";
		public static final String sumthammuu="sumthammuu";
		public static final String thammuuchuagq="thammuuchuagq";
		public static final String thammuudagq="thammuudagq";
	}

	public static void setThongbao(int count){
		kntcUI.getCurrent().getFormHead().getMenuItemNotification().setText(String.valueOf(count));
	}

	public static void setNotification(String id,int count) {
		//Page.getCurrent().getJavaScript().execute("$('#"+id+"').html('"+count+"');");
		Page.getCurrent().getJavaScript().execute("document.getElementById('"+id+"').innerHTML = '"+count+"'");
	}

}
