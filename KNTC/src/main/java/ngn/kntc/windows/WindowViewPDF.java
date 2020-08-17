package ngn.kntc.windows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class WindowViewPDF extends Window{
	private String filename;
	private Embedded embedded = new Embedded();
	private StreamResource resource;
	
	public WindowViewPDF(){
		buildLayout();
	}
	
	@SuppressWarnings("deprecation")
	private void buildLayout(){
		embedded.setSizeFull();
		embedded.setType(Embedded.TYPE_BROWSER);
        
		this.center();
		this.setSizeFull();
		this.setModal(true);
		this.setResizable(false);
		this.setContent(embedded);
	}
	
	public void loadData(ByteArrayOutputStream byteArrayOutputStream){
		StreamSource source = new StreamResource.StreamSource() {
			@Override
	        public InputStream getStream() {
		        try {
		        	byte[] bytes = byteArrayOutputStream.toByteArray();
		        	InputStream inputStream = new ByteArrayInputStream(bytes);
		        	return inputStream;
		        } catch (Exception e) {
		        	e.printStackTrace();
		        	return null;
		        }
	        }
		};
		resource = new StreamResource(source,filename);
		resource.setMIMEType("application/pdf");
		embedded.setSource(resource);
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
}
