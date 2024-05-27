class EditCut implements Command {

    private Viewer viewer;

    public EditCut(Viewer viewer) {
        this.viewer = viewer;
    }

    @Override
    public void execute() {
        viewer.cutTextArea();
    }
}
