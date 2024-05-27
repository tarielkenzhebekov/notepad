public class OpenFontDialogCommand implements Command {

    private Viewer viewer;

    public OpenFontDialogCommand(Viewer viewer) {
        this.viewer = viewer;
    }

    @Override
    public void execute() {
        viewer.showFontDialog();
    } 
}
