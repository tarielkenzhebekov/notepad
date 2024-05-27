import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.undo.UndoManager;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.nio.file.Path;

class Viewer {

    private static final String APP_NAME = "Notepad MVC";

    private static final String TITLE_SEPARATOR = " - ";

    private final JFrame frame;

    private final JTextArea textArea;

    private final JFileChooser fileChooser;

    private final UndoManager undoManager;

    private JMenu editMenu;

    private JMenuItem undoDocument;

    private JMenuItem redoDocument;

    private JMenuItem copyDocument;

    private JMenuItem cutDocument;

    private JMenuItem clearDocument;

    private JMenuItem findDocument;

    private JMenuItem replaceDocument;

    private JMenuItem selectAllDocument;

    private static final float DEFAULT_FONT_SIZE = 30;

    private final JPanel statusBar;

    private final JLabel scaleValue;

    private final JLabel cursorLocation;

    private JLabel charset;

    private ReplaceDialog replaceDialog;

    private final FontDialog fontDialog;

    private int currentFontSize;

    private JCheckBoxMenuItem checkBoxMenuItem;
    
    private ActionController actionController;

    private DocumentController documentController;

    public Viewer() {
        actionController = new ActionController(this);
        CursorController cursorController = new CursorController(this);
        WindowController windowController = new WindowController(actionController);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        fileChooser = new JFileChooser();

        frame = new JFrame();

        JMenuBar menuBar = getMenuBar(actionController);
        frame.setJMenuBar(menuBar);

        MouseListener mouseListener = new MouseListener(actionController, editMenu);
        editMenu.addMouseListener(mouseListener);

        textArea = new JTextArea();
        Font font = textArea.getFont().deriveFont(DEFAULT_FONT_SIZE);
        textArea.setFont(font);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.addCaretListener(cursorController);

        fontDialog = new FontDialog(frame, actionController, font);

        FileHandler fileHandler = actionController.getFileHandler();
        documentController = new DocumentController(fileHandler, this);
        String fileName = fileHandler.getFileName();
        updateTitle(fileName);
        textArea.getDocument().addDocumentListener(documentController);

        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane);
        frame.setIconImage(getScaledImageIcon("icons/notepad.png").getImage());

        scaleValue = new JLabel("100%");
        cursorLocation = new JLabel("String 1, column 1");
        cursorLocation.setBorder(new EmptyBorder(0, 30, 0, 30));
        charset = new JLabel("UTF-8");
        charset.setBorder(new EmptyBorder(0, 30, 0, 30));

        statusBar = new JPanel();
        statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
        Dimension preferredSize = statusBar.getPreferredSize();
        preferredSize.height = 30;
        statusBar.setPreferredSize(preferredSize);
        statusBar.add(cursorLocation);
        statusBar.add(Box.createHorizontalGlue());
        statusBar.add(scaleValue);
        statusBar.add(Box.createHorizontalGlue());
        statusBar.add(charset);
        frame.add(statusBar, BorderLayout.SOUTH);

        frame.setSize(800, 600);
        frame.setMinimumSize(new Dimension(500, 500));
        frame.setLocation(screenSize.width / 10, screenSize.height / 10);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(windowController);
        frame.setVisible(true);

        undoManager = new UndoManager();
        textArea.getDocument().addUndoableEditListener(undoManager);
    }

    private JMenuBar getMenuBar(ActionController actionController) {
        JMenu fileMenu = getFileMenu(actionController);
        JMenu viewMenu = getViewMenu(actionController);
        JMenu formatMenu = getFormatMenu(actionController);
        JMenu helpMenu = getHelpMenu(actionController);
        editMenu = getEditMenu(actionController);


        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        menuBar.add(formatMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private JMenu getFileMenu(ActionController actionController) {
        JMenuItem newDocument = new JMenuItem("New");
        newDocument.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        newDocument.addActionListener(actionController);
        newDocument.setActionCommand("FileNew");
        newDocument.setIcon(getScaledImageIcon("icons/new.png"));

        JMenuItem openDocument = new JMenuItem("Open");
        openDocument.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        openDocument.addActionListener(actionController);
        openDocument.setActionCommand("FileOpen");
        openDocument.setIcon(getScaledImageIcon("icons/open.png"));

        JMenuItem saveDocument = new JMenuItem("Save");
        saveDocument.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        saveDocument.addActionListener(actionController);
        saveDocument.setActionCommand("FileSave");
        saveDocument.setIcon(getScaledImageIcon("icons/save.png"));

        JMenuItem saveAsDocument = new JMenuItem("Save As");
        saveAsDocument.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.SHIFT_DOWN_MASK + InputEvent.CTRL_DOWN_MASK));
        saveAsDocument.addActionListener(actionController);
        saveAsDocument.setActionCommand("FileSaveAs");
        saveAsDocument.setIcon(getScaledImageIcon("icons/save_as.png"));

        JMenuItem printDocument = new JMenuItem("Print");
        printDocument.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
        printDocument.addActionListener(actionController);
        printDocument.setActionCommand("FilePrint");
        printDocument.setIcon(getScaledImageIcon("icons/print.png"));

        JMenuItem closeProgramm = new JMenuItem("Close");
        closeProgramm.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
        closeProgramm.addActionListener(actionController);
        closeProgramm.setActionCommand("FileClose");
        closeProgramm.setIcon(getScaledImageIcon("icons/exit.png"));

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        fileMenu.add(newDocument);
        fileMenu.add(openDocument);
        fileMenu.add(saveDocument);
        fileMenu.add(saveAsDocument);
        fileMenu.add(new JSeparator());
        fileMenu.add(printDocument);
        fileMenu.add(new JSeparator());
        fileMenu.add(closeProgramm);

        return fileMenu;
    }

    private JMenu getEditMenu(ActionController actionController) {

        undoDocument = new JMenuItem("Undo");
        undoDocument.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        undoDocument.addActionListener(actionController);
        undoDocument.setActionCommand("EditUndo");
        undoDocument.setIcon(getScaledImageIcon("icons/undo.png"));

        redoDocument = new JMenuItem("Redo");
        redoDocument.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
        redoDocument.addActionListener(actionController);
        redoDocument.setActionCommand("EditRedo");
        redoDocument.setIcon(getScaledImageIcon("icons/redo.png"));

        copyDocument = new JMenuItem("Copy");
        copyDocument.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        copyDocument.addActionListener(actionController);
        copyDocument.setActionCommand("EditCopy");
        copyDocument.setIcon(getScaledImageIcon("icons/copy.png"));

        cutDocument = new JMenuItem("Cut");
        cutDocument.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        cutDocument.addActionListener(actionController);
        cutDocument.setActionCommand("EditCut");
        cutDocument.setIcon(getScaledImageIcon("icons/cut.png"));

        JMenuItem pasteDocument = new JMenuItem("Paste");
        pasteDocument.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
        pasteDocument.addActionListener(actionController);
        pasteDocument.setActionCommand("EditPaste");
        pasteDocument.setIcon(getScaledImageIcon("icons/paste.png"));

        clearDocument = new JMenuItem("Clear");
        clearDocument.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        clearDocument.addActionListener(actionController);
        clearDocument.setActionCommand("EditClear");
        clearDocument.setIcon(getScaledImageIcon("icons/delete.png"));

        findDocument = new JMenuItem("Find");
        findDocument.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
        findDocument.addActionListener(actionController);
        findDocument.setActionCommand("EditFind");
        findDocument.setIcon(getScaledImageIcon("icons/search.png"));

        replaceDocument = new JMenuItem("Replace");
        replaceDocument.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK));
        replaceDocument.addActionListener(actionController);
        replaceDocument.setActionCommand("EditReplaceShow");
        replaceDocument.setIcon(getScaledImageIcon("icons/replace.png"));

        selectAllDocument = new JMenuItem("SelectAll");
        selectAllDocument.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
        selectAllDocument.addActionListener(actionController);
        selectAllDocument.setActionCommand("EditSelectAll");
        selectAllDocument.setIcon(getScaledImageIcon("icons/select_all.png"));

        JMenuItem timeAndDateDocument = new JMenuItem("Time and date");
        timeAndDateDocument.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        timeAndDateDocument.addActionListener(actionController);
        timeAndDateDocument.setActionCommand("EditTimeAndDate");
        timeAndDateDocument.setIcon(getScaledImageIcon("icons/clock.png"));

        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic('E');
        editMenu.add(undoDocument);
        editMenu.add(redoDocument);
        editMenu.add(new JSeparator());
        editMenu.add(cutDocument);
        editMenu.add(copyDocument);
        editMenu.add(pasteDocument);
        editMenu.add(clearDocument);
        editMenu.add(new JSeparator());
        editMenu.add(findDocument);
        editMenu.add(replaceDocument);
        editMenu.add(new JSeparator());
        editMenu.add(selectAllDocument);
        editMenu.add(timeAndDateDocument);
        return editMenu;
    }

    private JMenu getViewMenu(ActionController actionController) {
        JMenuItem increase = new JMenuItem("Increase");
        increase.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, ActionEvent.CTRL_MASK));
        increase.addActionListener(actionController);
        increase.setActionCommand("ViewIncrease");
        increase.setIcon(getScaledImageIcon("icons/zoom_in.png"));

        JMenuItem decrease = new JMenuItem("Decrease");
        decrease.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, ActionEvent.CTRL_MASK));
        decrease.addActionListener(actionController);
        decrease.setActionCommand("ViewDecrease");
        decrease.setIcon(getScaledImageIcon("icons/zoom_out.png"));

        JMenuItem defaultScale = new JMenuItem("Original scale");
        defaultScale.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, ActionEvent.CTRL_MASK));
        defaultScale.addActionListener(actionController);
        defaultScale.setActionCommand("ViewOriginalScale");
        defaultScale.setIcon(getScaledImageIcon("icons/default_scale.png"));

        checkBoxMenuItem = new JCheckBoxMenuItem("Status Bar", true);
        checkBoxMenuItem.addActionListener(actionController);
        checkBoxMenuItem.setActionCommand("ViewStatusBar");

        JRadioButton themeSwitcher = new JRadioButton("Nimbus");
        themeSwitcher.addActionListener(actionController);
        themeSwitcher.setActionCommand("ViewNimbus");

        JRadioButton themeSwitcher2 = new JRadioButton("Default");
        themeSwitcher2.addActionListener(actionController);
        themeSwitcher2.setActionCommand("ViewMetal");

        JRadioButton themeSwitcher3 = new JRadioButton("Motif");
        themeSwitcher3.addActionListener(actionController);
        themeSwitcher3.setActionCommand("ViewMotif");

        JRadioButton themeSwitcher4 = new JRadioButton("Windows");
        themeSwitcher4.addActionListener(actionController);
        themeSwitcher4.setActionCommand("ViewWindows");

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(themeSwitcher);
        buttonGroup.add(themeSwitcher2);
        buttonGroup.add(themeSwitcher3);
        buttonGroup.add(themeSwitcher4);

        JMenu setTheme = new JMenu("Set theme");
        setTheme.add(themeSwitcher);
        setTheme.add(themeSwitcher2);
        setTheme.add(themeSwitcher3);
        setTheme.add(themeSwitcher4);

        JMenu scale = new JMenu("Scale");
        scale.add(increase);
        scale.add(decrease);
        scale.add(defaultScale);

        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic('V');
        viewMenu.add(scale);
        viewMenu.add(checkBoxMenuItem);
        viewMenu.add(setTheme);

        return viewMenu;
    }

    private JMenu getFormatMenu(ActionController actionController) {
        JMenuItem Font = new JMenuItem("Font");
        Font.addActionListener(actionController);
        Font.setActionCommand("FontDialog");

        JMenu formatMenu = new JMenu("Format");
        formatMenu.add(Font);

        return formatMenu;
    }

    private JMenu getHelpMenu(ActionController actionController) {

        JMenu helpMenu = new JMenu("Help");

        JMenuItem contactUsMenuItem = new JMenuItem("Contact us");
        contactUsMenuItem.addActionListener(actionController);
        contactUsMenuItem.setActionCommand("HelpContactUs");

        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(actionController);
        aboutMenuItem.setActionCommand("HelpAbout");

        helpMenu.add(contactUsMenuItem);
        helpMenu.add(aboutMenuItem);

        return helpMenu;
    }

    public String getTextAreaContent() {
        return textArea.getText();
    }

    private ImageIcon getScaledImageIcon(String path) {
        return new ImageIcon(new ImageIcon(getClass().getResource(path)).getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
    }

    public Path showOpenDialog() {
        int state = fileChooser.showOpenDialog(frame);

        Path path = null;
        if (state == JFileChooser.APPROVE_OPTION) {
            path = fileChooser.getSelectedFile().toPath();
        }

        return path;
    }

    public Path showSaveDialog() {
        int state = fileChooser.showSaveDialog(frame);

        Path path = null;
        if (state == JFileChooser.APPROVE_OPTION) {
            path = fileChooser.getSelectedFile().toPath();
        }

        return path;
    }

    public int getDefaultFontSize() {
        return (int) DEFAULT_FONT_SIZE;
    }

    public void setFontSize(int size) {
        Font fontWithNewSize = new Font(textArea.getFont().getFontName(), textArea.getFont().getStyle(), size);
        textArea.setFont(fontWithNewSize);
    }

    public void showStatusBar() {
        if (checkBoxMenuItem.isSelected()) {
            statusBar.setVisible(true);
        } else {
            statusBar.setVisible(false);
        }
    }

    public void setDataToStatusBar(String data) {
        if (data.endsWith("%")) {
            scaleValue.setText(data);
        } else cursorLocation.setText(data);
    }
    
    public int showFileAlreadyExistsDialog() {
        String options[] = {"OK", "CANCEL"};
        int optionResult = JOptionPane.showOptionDialog(frame,
                                                        "Do you want to overwrite it?",
                                                        "File already exists!",
                                                        JOptionPane.YES_NO_OPTION,
                                                        JOptionPane.QUESTION_MESSAGE,
                                                        null,
                                                        options,
                                                        options[1]);

        return optionResult;
    }
            
    public int showExitWithoutSavingDialog() {
        String options[] = {"Save", "Don`t save", "Cancel"};
        int optionResult = JOptionPane.showOptionDialog(frame,
                                                        "Changes are not saved. Do you want to save them?",
                                                        "File not saved",
                                                        JOptionPane.YES_NO_CANCEL_OPTION,
                                                        JOptionPane.WARNING_MESSAGE,
                                                        null,
                                                        options,
                                                        options[0]);

        return optionResult;        
    }

    public int showFileIsTooLargeDialog() {
        String options[] = {"OK"};
        int optionResult = JOptionPane.showOptionDialog(frame,
                                                        "This file is too large!",
                                                        "File is too large",
                                                        JOptionPane.DEFAULT_OPTION,
                                                        JOptionPane.WARNING_MESSAGE,
                                                        null,
                                                        options,
                                                        options[0]);

        return optionResult;
    }

    public void selectAllTextArea() {
        textArea.selectAll();
    }

    public String getSelectedText() {
        return textArea.getSelectedText();
    }

    public void insert(String str, int pos) {
        textArea.insert(str, pos);
    }

    public int getCaretPosition() {
        return textArea.getCaretPosition();
    }

    public boolean canUndo() {
        return undoManager.canUndo();
    }

    public void undo() {
        undoManager.undo();
    }

    public boolean canRedo() {
        return undoManager.canRedo();
    }

    public void redo() {
        undoManager.redo();
    }

    public void cutTextArea() {
        textArea.getSelectedText();
        textArea.cut();
    }

    public void showReplaceDialog() {
        if (replaceDialog == null) {
            replaceDialog = new ReplaceDialog(actionController);
        }

        int pointX = frame.getLocation().x + frame.getWidth() / 3;
        int pointY = frame.getLocation().y + frame.getHeight() / 3;
        replaceDialog.setLocation(pointX, pointY);
        replaceDialog.setVisible(true);
    }

    public void closeReplaceDialog() {
        replaceDialog.close();
    }

    public String getSearchWord() {
        return replaceDialog.getSearchWord();
    }

    public String getReplacementWord() {
        return replaceDialog.getReplacementWord();
    }

    public String getText() {
        return textArea.getText();
    }

    public void updateTextArea(String content) {
        textArea.getDocument().removeDocumentListener(documentController);

        textArea.setText(content);
        
        textArea.getDocument().addDocumentListener(documentController);
    }

    public void showFindDialog() {
        new FindDialog(frame, textArea);
    }

    public JFrame getFrame() {
        return frame;
    }

    public String removeWhiteSpaceAndNewLine(String text) {
        return text.replaceAll("\\s+|\n", "");
    }

    public void enableAndDisableEditItems() {
        if (getSelectedText() != null) {
            cutDocument.setEnabled(true);
            copyDocument.setEnabled(true);
            clearDocument.setEnabled(true);
        } else {
            cutDocument.setEnabled(false);
            copyDocument.setEnabled(false);
            clearDocument.setEnabled(false);
        }

        if (canRedo()) {
            redoDocument.setEnabled(true);
        } else {
            redoDocument.setEnabled(false);
        }
        if (canUndo()) {
            undoDocument.setEnabled(true);
        } else {
            undoDocument.setEnabled(false);
        }

        if (removeWhiteSpaceAndNewLine(getText()).equals("")) {
            findDocument.setEnabled(false);
            replaceDocument.setEnabled(false);
            selectAllDocument.setEnabled(false);
        } else {
            findDocument.setEnabled(true);
            replaceDocument.setEnabled(true);
            selectAllDocument.setEnabled(true);
        }
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public void hideFontDialog() {
        fontDialog.hideDialog();
    }

    public void showFontDialog() {
        Font currentFont = textArea.getFont();
        fontDialog.showDialog(currentFont);
        setDataToStatusBar("100%");
        currentFontSize = textArea.getFont().getSize();
    }

    public FontDialog getFontDialog() {
        return fontDialog;
    }

    public void clearTextArea() {
        textArea.setText("");
    }

    public void updateTitle(String fileName) {
        String newTitle = APP_NAME 
                          + TITLE_SEPARATOR 
                          + fileName;

        frame.setTitle(newTitle);
    }
    public int getCurrentFontSize(){
        if (currentFontSize == 0){
            currentFontSize =(int) DEFAULT_FONT_SIZE;
        }
        return currentFontSize;
    }

    public void setCurrentFontSize(int size){
        currentFontSize = size;
    }

}
