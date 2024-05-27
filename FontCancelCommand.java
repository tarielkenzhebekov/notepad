public class FontCancelCommand implements Command {
    
    private Viewer viewer;

    public FontCancelCommand(Viewer viewer) {
        this.viewer = viewer;
    }

    @Override
    public void execute() {
        viewer.hideFontDialog();
    }
}
