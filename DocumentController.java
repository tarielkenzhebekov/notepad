import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

class DocumentController implements DocumentListener {

    private FileHandler fileHandler;

    private Viewer viewer;

    private int lastHash;

    DocumentController(FileHandler fileHandler, Viewer viewer) {
        this.fileHandler = fileHandler;
        this.viewer = viewer;
    }
    
    @Override 
    public void insertUpdate(DocumentEvent event) {
        changedUpdate(event);
    }
    
    @Override 
    public void removeUpdate(DocumentEvent event) {
        changedUpdate(event);
    }
    
    @Override
    public void changedUpdate(DocumentEvent event) {
        String text = viewer.getTextAreaContent();
        int newHash = text.hashCode();
        
        if (lastHash != newHash) {
            fileHandler.setSavedFlag(false);
        } else if (fileHandler.isSaved()) {
            fileHandler.setSavedFlag(true);
            lastHash = text.hashCode();
        }

        String fileName = fileHandler.getFileName();
        viewer.updateTitle(fileName);
    }
}