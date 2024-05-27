public class ViewIncreaseCommand implements Command {
    private Viewer viewer;
    
    public ViewIncreaseCommand(Viewer viewer) {
        this.viewer = viewer;
    }

    @Override
    public void execute() {
        int size = viewer.getCurrentFontSize();

        int fontSizeChangeStep = (int) (size * 0.1);

        if (fontSizeChangeStep == 0) {
            fontSizeChangeStep++;
        }

        int currentFontSize = viewer.getTextArea().getFont().getSize();
        int increasedFontSize = currentFontSize + fontSizeChangeStep;
        int scale = 100 + ((increasedFontSize - size) / fontSizeChangeStep) * 10;

        if (scale <= 500) {
            viewer.setFontSize(increasedFontSize);
            String scaleValue = scale + "%";
            viewer.setDataToStatusBar(scaleValue);
        }
    }
}
