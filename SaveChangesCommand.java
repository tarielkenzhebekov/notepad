public class SaveChangesCommand implements Command {

    private ActionController controller;

    private Command saveCommand;

    public SaveChangesCommand(ActionController controller) {
        this.controller = controller;
        saveCommand = new FileSaveCommand(controller);
    }

    @Override
    public void execute() {
        FileHandler fileHandler = controller.getFileHandler();
        Viewer viewer = controller.getViewer();
                
        loop:
        while (! fileHandler.isSaved()) {
            int optionResult = viewer.showExitWithoutSavingDialog();
            
            switch (optionResult) {
            case 0:
                saveCommand.execute();

                if (fileHandler.getPath() == null) {
                    break;
                }
                break;
            case 1:
                break loop;
            case 2:
                return;
            default:
                return;
            }
        }
    }
}
