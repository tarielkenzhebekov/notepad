import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;

public class CursorController implements CaretListener {

    private Viewer viewer;

    public CursorController(Viewer viewer){
        this.viewer = viewer;
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        JTextArea jTextArea = viewer.getTextArea();
        int caretPosition = jTextArea.getCaretPosition();
        try {
            int lineOfOffset = jTextArea.getLineOfOffset(caretPosition);
            int stringNumber = lineOfOffset + 1;
            int lineStartOffset = jTextArea.getLineStartOffset(lineOfOffset);
            int columnInLine = caretPosition - lineStartOffset;
            int columnNumber = columnInLine + 1;
            String cursorPosition = "String " + stringNumber + ", " + "column " + columnNumber;
            viewer.setDataToStatusBar(cursorPosition);
        } catch (BadLocationException ble) {
            System.out.println("Error: " + ble);;
        }
    }
}
