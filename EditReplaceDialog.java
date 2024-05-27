class EditReplaceDialog implements Command {

    private Viewer viewer;

    public EditReplaceDialog(Viewer viewer) {
        this.viewer = viewer;
    }

    @Override
    public void execute() {
        viewer.showReplaceDialog();
    }
}
