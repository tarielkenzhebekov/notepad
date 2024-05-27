class EditFindDialog implements Command {

    private Viewer viewer;

    public EditFindDialog(Viewer viewer) {
        this.viewer = viewer;
    }

    @Override
    public void execute() {
        viewer.showFindDialog();
    }
}
