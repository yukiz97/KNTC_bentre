package ngn.kntc.page.donthu.danhsach;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class test {
	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		List<beantest> list = new ArrayList<beantest>();

		beantest model1 = new test().new beantest();
		model1.setDateCreate(new Date(2019, 10, 15));
		model1.setName("Model 1");

		beantest model2 = new test().new beantest();
		model2.setDateCreate(new Date(2019, 11, 23));
		model2.setName("Model 2");

		beantest model3 = new test().new beantest();
		model3.setDateCreate(new Date(2019, 8, 7));
		model3.setName("Model 3");

		list.add(model1);
		list.add(model2);
		list.add(model3);

		Collections.sort(list);
		
		for(beantest model : list)
		{
			System.out.println(model.getName()+" -- "+sdf.format(model.getDateCreate()));
		}
	}

	public class beantest implements Comparable<beantest>{
		Date dateCreate;
		String name;
		public Date getDateCreate() {
			return dateCreate;
		}
		public void setDateCreate(Date dateCreate) {
			this.dateCreate = dateCreate;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		@Override
		public int compareTo(beantest o) {
			return this.getDateCreate().compareTo(o.getDateCreate());
		}
	}
}
