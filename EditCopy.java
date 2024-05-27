import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;

class EditCopy implements Command {

    private Viewer viewer;

    public EditCopy(Viewer viewer) {
        this.viewer = viewer;
    }

    @Override
    public void execute() {
        String selectedText = viewer.getSelectedText();

        if (selectedText != null) {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection selection = new StringSelection(selectedText);
            clipboard.setContents(selection, null);
        }
    }
}
