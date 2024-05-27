import java.nio.file.Path;

class FileOpenCommand implements Command {

    private ActionController controller;

    private Command saveChangesCommand;

    public FileOpenCommand(ActionController controller) {
        this.controller = controller;
        saveChangesCommand = new SaveChangesCommand(controller);
    }

    @Override
    public void execute() {
        FileHandler fileHandler = controller.getFileHandler();
        Viewer viewer = controller.getViewer();

        saveChangesCommand.execute();

        Path path = viewer.showOpenDialog();

        if (path == null) {
            return;
        }

        fileHandler.newFile(path);
        Result<String, Integer> result = fileHandler.readContentFromFile();

        if (result.isFail()) {
            if (result.getFail() == FileHandler.Status.FILE_IS_TOO_LARGE) {
                viewer.showFileIsTooLargeDialog();
            }
            return;
        }

        String content = result.getSuccess();
        viewer.updateTextArea(content);
        viewer.getTextArea().setCaretPosition(0);

        String fileName = fileHandler.getFileName();
        viewer.updateTitle(fileName);

        controller.startAutoSave();
    }
}