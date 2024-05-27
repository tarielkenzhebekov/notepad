import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

class EditPaste implements Command {

    private Viewer viewer;

    public EditPaste(Viewer viewer) {
        this.viewer = viewer;
    }

    @Override
    public void execute() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        try {
            Transferable transferable = clipboard.getContents(this);

            if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String clipboardText = (String) transferable.getTransferData(DataFlavor.stringFlavor);

                viewer.insert(clipboardText, viewer.getCaretPosition());
            }
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
    }
}
