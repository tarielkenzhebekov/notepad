import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

class ActionController implements ActionListener {

    private final Viewer viewer;

    private FileHandler fileHandler;

    private Map<String, Command> commands;
    
    private Thread autoSaveThread;

    public ActionController(Viewer viewer) {
        this.viewer = viewer;
        fileHandler = new FileHandler();
        autoSaveThread = null;

        commands = initializeCommands();
    }

    private Map<String, Command> initializeCommands() {
        Map<String, Command> commands = new HashMap<>();

        commands.put("FileNew", new FileNewCommand(this));
        commands.put("FileOpen", new FileOpenCommand(this));
        commands.put("FileSave", new FileSaveCommand(this));
        commands.put("FileSaveAs", new FileSaveAsCommand(this));
        commands.put("FilePrint", new FilePrintCommand(this));
        commands.put("FileClose", new FileCloseCommand(this));

        commands.put("ViewIncrease", new ViewIncreaseCommand(viewer));
        commands.put("ViewDecrease", new ViewDecreaseCommand(viewer));
        commands.put("ViewOriginalScale", new ViewOriginalScaleCommand(viewer));
        commands.put("ViewStatusBar", new ViewStatusBarCommand(viewer));
        commands.put("ViewNimbus", new ViewerThemeSwitcher(viewer, "Nimbus"));
        commands.put("ViewMetal", new ViewerThemeSwitcher(viewer, "Metal"));
        commands.put("ViewMotif", new ViewerThemeSwitcher(viewer, "Motif"));
        commands.put("ViewWindows", new ViewerThemeSwitcher(viewer, "Windows"));

        commands.put("FontDialog", new OpenFontDialogCommand(viewer));
        commands.put("FontDialogOk", new FontOkCommand(viewer));
        commands.put("FontDialogCancel", new FontCancelCommand(viewer));

        commands.put("EditUndo", new EditUndo(viewer));
        commands.put("EditRedo", new EditRedo(viewer));
        commands.put("EditCopy", new EditCopy(viewer));
        commands.put("EditCut", new EditCut(viewer));
        commands.put("EditPaste", new EditPaste(viewer));
        commands.put("EditClear", new EditClear(viewer));
        commands.put("EditFind", new EditFindDialog(viewer));
        commands.put("EditReplace", new EditReplace(viewer));
        commands.put("EditReplaceShow", new EditReplaceDialog(viewer));
        commands.put("EditSelectAll", new EditSelectAll(viewer));
        commands.put("EditTimeAndDate", new EditTimeAndDate(viewer));
        commands.put("Edit", new EditEnableAndDisableEditItems(viewer));

        commands.put("HelpAbout", new HelpAboutCommand());
        commands.put("HelpContactUs", new HelpContactUsCommand());

        return commands;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String actionCommand = event.getActionCommand();
        Command command = commands.get(actionCommand);

        if (command == null) {
            return;
        }

        command.execute();
    }

    public Viewer getViewer() {
        return viewer;
    }

    public FileHandler getFileHandler() {
        return fileHandler;
    }

    public void startAutoSave() {
        if (autoSaveThread == null) {
            Runnable autoSave = new AutoSave(this);
            autoSaveThread = new Thread(autoSave, "Auto-Save-Thread");
    
            autoSaveThread.start();
        } else {
            stopAutoSave();
            startAutoSave();
        }
    }

    public synchronized void stopAutoSave() {
        if (autoSaveThread != null) {
            autoSaveThread.interrupt();
            autoSaveThread = null;
        }
    }
}
