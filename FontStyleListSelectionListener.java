import javax.swing.event.ListSelectionListener;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

public class FontStyleListSelectionListener implements ListSelectionListener {
    
    private final FontDialog dialog;

    public FontStyleListSelectionListener(FontDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        
        ListSelectionModel model = (ListSelectionModel) e.getSource();

        int indx = model.getMinSelectionIndex();

        if (indx == -1) {
            return;
        }

        dialog.updateFontSyteTextField();
        dialog.updateExampleLabel();
    }
}
