package ngn.kntc.modules;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
public class KNTCProps {
	static Properties prop=new Properties();
	String fileName="config.properties";
	
	public KNTCProps() {
		InputStream inputStream=getClass().getClassLoader().getResourceAsStream(fileName);
		try {
			prop.load(inputStream);
			System.out.println("Connect success config properties");
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Connect fail config properties");
		}
	}
	
	public static String getProperty(String key){
		return prop.getProperty(key);
	}
}
