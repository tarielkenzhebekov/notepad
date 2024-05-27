import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class FontCheckBoxChangeListener implements ChangeListener {
    
    private FontDialog dialog;

    public FontCheckBoxChangeListener(FontDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        dialog.updateExampleLabel();
    }
}
