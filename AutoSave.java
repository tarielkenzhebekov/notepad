public class AutoSave implements Runnable {

    private FileHandler fileHandler;

    private Command saveCommand;

    public AutoSave(ActionController controller) {
        fileHandler = controller.getFileHandler();
        saveCommand = new FileSaveCommand(controller);
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(1_000 * 15);
                if (! fileHandler.isSaved()) {
                    saveCommand.execute();
                }
            }
        } catch (InterruptedException ie) {
            fileHandler = null;
            saveCommand = null;
            return;
        }
    }
}
