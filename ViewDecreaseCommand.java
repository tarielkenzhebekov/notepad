public class ViewDecreaseCommand implements Command {
    private Viewer viewer;
    
    public ViewDecreaseCommand(Viewer viewer) {
        this.viewer = viewer;
    }

    @Override
    public void execute() {
        int size = viewer.getCurrentFontSize();

        int fontSizeChangeStep = (int) (size * 0.1);

        if (fontSizeChangeStep == 0){
            fontSizeChangeStep++;
        }

        int currentFontSize = viewer.getTextArea().getFont().getSize();
        int decreasedFontSize = currentFontSize - fontSizeChangeStep;
        int scale = 100 + ((decreasedFontSize - size) / fontSizeChangeStep) * 10;

        if (scale >= 10) {
            viewer.setFontSize(decreasedFontSize);
            String scaleValue = scale + "%";
            viewer.setDataToStatusBar(scaleValue);
        }
    }
}
