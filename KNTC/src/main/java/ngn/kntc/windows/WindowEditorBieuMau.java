package ngn.kntc.windows;

import org.vaadin.openesignforms.ckeditor.CKEditorConfig;
import org.vaadin.openesignforms.ckeditor.CKEditorTextField;
import org.vaadin.openesignforms.ckeditor.widgetset.client.ui.CKEditor;

import ngn.kntc.modules.LayoutButtonSubmit;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

public class WindowEditorBieuMau extends Window{
	VerticalLayout vMain = new VerticalLayout();
	
	LayoutButtonSubmit hSubmit = new LayoutButtonSubmit("Lưu chỉnh sửa","Hủy");
	
	CKEditorConfig config = new CKEditorConfig();
	CKEditorTextField txtEditor;
	
	public WindowEditorBieuMau() {
		
		config.useCompactTags();
		config.disableElementsPath();
		config.setResizeDir(CKEditorConfig.RESIZE_DIR.HORIZONTAL);
		config.disableSpellChecker();
		config.setWidth("100%");
		
		txtEditor = new CKEditorTextField(config);
		
		vMain.addComponent(txtEditor);
		vMain.addComponent(hSubmit);

		vMain.setExpandRatio(txtEditor, 1.0f);
		vMain.setComponentAlignment(hSubmit, Alignment.MIDDLE_RIGHT);
		
		txtEditor.setWidth("21.5cm");
		txtEditor.setHeight("700px");
		
		vMain.setWidth("100%");
		vMain.setMargin(true);
		vMain.setSpacing(true);
		
		this.setContent(vMain);
		this.setCaption("Edit");
		this.setModal(true);
		
		configComponent();
	}

	private void configComponent() {
		hSubmit.getBtnCancel().addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
	}
	
	public CKEditorTextField getTxtEditor() {
		return txtEditor;
	}

	public void setTxtEditor(CKEditorTextField txtEditor) {
		this.txtEditor = txtEditor;
	}

	public LayoutButtonSubmit gethSubmit() {
		return hSubmit;
	}

	public void sethSubmit(LayoutButtonSubmit hSubmit) {
		this.hSubmit = hSubmit;
	}
	
}
