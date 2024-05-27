public class ViewStatusBarCommand implements Command {

    private Viewer viewer;

    public ViewStatusBarCommand(Viewer viewer) {
        this.viewer = viewer;
    }

    @Override
    public void execute() {
        viewer.showStatusBar();
    }
}
