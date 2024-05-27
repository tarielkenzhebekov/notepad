class EditEnableAndDisableEditItems implements Command {

    private Viewer viewer;

    public EditEnableAndDisableEditItems(Viewer viewer) {
        this.viewer = viewer;
    }

    @Override
    public void execute() {
        viewer.enableAndDisableEditItems();
    }
}
