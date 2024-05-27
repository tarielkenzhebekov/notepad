class FileSaveCommand implements Command {

    private ActionController controller;

    public FileSaveCommand(ActionController controller) {
        this.controller = controller;
    }

    @Override
    public void execute() {
        FileHandler fileHandler = controller.getFileHandler();
        Viewer viewer = controller.getViewer();
        String content = viewer.getTextAreaContent();
        
        if (fileHandler.getPath() == null) {
            Command command = new FileSaveAsCommand(controller);
            command.execute();
            
            if (fileHandler.getPath() == null) {
                return;
            }
        }

        byte[] byteArray = content.getBytes();
        fileHandler.writeByteArrayToFile(byteArray);

        String fileName = fileHandler.getFileName();
        viewer.updateTitle(fileName);
    }
}