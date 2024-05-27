class EditSelectAll implements Command {

    private Viewer viewer;

    public EditSelectAll(Viewer viewer) {
        this.viewer = viewer;
    }

    @Override
    public void execute() {
        viewer.selectAllTextArea();
    }
}
