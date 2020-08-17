package ngn.kntc.modules;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class LayoutButtonSubmit extends HorizontalLayout{
	private Button btnSave = new Button("Lưu",FontAwesome.SAVE);
	private Button btnCancel = new Button("Đóng",FontAwesome.REMOVE);

	public LayoutButtonSubmit() {
		buildGeneral();
	}

	public LayoutButtonSubmit(String strCaptionSave,String strCaptionCancel) {
		btnSave.setCaption(strCaptionSave);
		btnCancel.setCaption(strCaptionCancel);
		
		buildGeneral();
	}

	public LayoutButtonSubmit(String strCaptionSave,String strCaptionCancel,FontAwesome fontSave,FontAwesome fontCancel) {
		btnSave.setCaption(strCaptionSave);
		btnCancel.setCaption(strCaptionCancel);
		btnSave.setIcon(fontSave);
		btnCancel.setIcon(fontCancel);
		
		buildGeneral();
	}

	private void buildGeneral()
	{
		this.addComponents(btnSave,btnCancel);
		
		btnSave.addStyleName(ValoTheme.BUTTON_PRIMARY);
		btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
		
		this.setSpacing(true);
	}

	public Button getBtnSave() {
		return btnSave;
	}

	public void setBtnSave(Button btnSave) {
		this.btnSave = btnSave;
	}

	public Button getBtnCancel() {
		return btnCancel;
	}

	public void setBtnCancel(Button btnCancel) {
		this.btnCancel = btnCancel;
	}
}
