class EditClear implements Command {

    private Viewer viewer;

    public EditClear(Viewer viewer) {
        this.viewer = viewer;
    }

    @Override
    public void execute() {
        viewer.cutTextArea();
    }
}
