import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class FontTextFieldFocusListener implements FocusListener {

    private FontDialog dialog;

    public FontTextFieldFocusListener(FontDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {
        dialog.updateExampleLabel();
    }
}