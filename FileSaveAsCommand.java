import java.nio.file.Path;
import java.nio.file.Files;

class FileSaveAsCommand implements Command {

    private ActionController controller;

    public FileSaveAsCommand(ActionController controller) {
        this.controller = controller;
    }

    @Override
    public void execute() {
        FileHandler fh = controller.getFileHandler();
        Viewer viewer = controller.getViewer();

        controller.stopAutoSave();
        
        Path path = viewer.showSaveDialog();

        if (path == null) {
            return;
        }
        
        loop:
        while (Files.exists(path)) {
            int optionResult = viewer.showFileAlreadyExistsDialog();
            
            switch (optionResult) {
            case 0:
                break loop;
            case 1:
                path = viewer.showSaveDialog();

                if (path == null) {
                    return;
                }
                break;
            default:
                return;
            }
        }

        if (path == null) {
            return;
        }

        fh.newFile(path);
        
        Command command = new FileSaveCommand(controller); 
        command.execute();

        controller.startAutoSave();
    }
}