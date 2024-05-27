class EditReplace implements Command {

    private Viewer viewer;

    public EditReplace(Viewer viewer) {
        this.viewer = viewer;
    }

    @Override
    public void execute() {
        String search = viewer.getSearchWord();
        String replace = viewer.getReplacementWord();
        String txt = viewer.getText();

        if (checkSpaces(search) || checkSpaces(replace)) {
            return;
        }

        if (txt.contains(search)) {
            viewer.updateTextArea(txt.replaceAll("\\b" + search + "\\b", replace));
            viewer.closeReplaceDialog();
            return;
        }
    }

    private boolean checkSpaces(String word) {
        int count = word.length() - word.replace(" ", "").length();
        return count == word.length();
    }
}
