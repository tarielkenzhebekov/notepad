class FileNewCommand implements Command {

    private ActionController controller;

    private Command saveChangesCommand;

    public FileNewCommand(ActionController controller) {
        this.controller = controller;
        saveChangesCommand = new SaveChangesCommand(controller);
    }

    @Override
    public void execute() {
        Viewer viewer = controller.getViewer();
        FileHandler fileHandler = controller.getFileHandler();
        
        saveChangesCommand.execute();

        viewer.clearTextArea();
        fileHandler.newFile(null);
        
        String fileName = fileHandler.getFileName();
        viewer.updateTitle(fileName);

        controller.stopAutoSave();
    }

}