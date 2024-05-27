public class ViewOriginalScaleCommand implements Command{

    private Viewer viewer;

    public ViewOriginalScaleCommand(Viewer viewer) {
        this.viewer = viewer;
    }

    @Override
    public void execute() {
        int defaultFontSize = viewer.getDefaultFontSize();
        viewer.setFontSize(defaultFontSize);
        String scaleValue = defaultFontSize + (100 - defaultFontSize) + "%";
        viewer.setDataToStatusBar(scaleValue);
        viewer.setCurrentFontSize(defaultFontSize);
    }
}
