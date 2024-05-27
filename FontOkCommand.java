public class FontOkCommand implements Command {
    
    private Viewer viewer;

    public FontOkCommand(Viewer viewer) {
        this.viewer = viewer;
    }

    @Override
    public void execute() {
        viewer.hideFontDialog();
        
        java.awt.Font newFont = viewer.getFontDialog().getFontFromTextFields();
        viewer.getTextArea().setFont(newFont);
    }
}
