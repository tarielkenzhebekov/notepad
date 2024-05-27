class EditUndo implements Command {

    private Viewer viewer;

    public EditUndo(Viewer viewer) {
        this.viewer = viewer;
    }

    @Override
    public void execute() {
        if (viewer.canUndo()) {
            viewer.undo();
        }
        if (viewer.getText().isEmpty()) {
            if (viewer.canUndo()) {
                viewer.undo();
            }
        }
    }
}
