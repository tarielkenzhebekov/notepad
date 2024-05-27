class EditRedo implements Command {

    private Viewer viewer;

    public EditRedo(Viewer viewer) {
        this.viewer = viewer;
    }

    @Override
    public void execute() {
        if (viewer.canRedo()) {
            viewer.redo();
        }
        if (viewer.getText().isEmpty()) {
            if (viewer.canRedo()) {
                viewer.redo();
            }
        }
    }
}
