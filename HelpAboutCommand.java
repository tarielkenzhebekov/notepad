import javax.swing.JOptionPane;

public class HelpAboutCommand implements Command {

    @Override
    public void execute() {
        JOptionPane.showMessageDialog(null, "This app is developed by interns of the internlabs 5.0\n\n" +
                    "Quick commands for working in our application:\n\n" +
                    "\u2022 Ctrl+N - To open a new text editor\n" +
                    "\u2022 Ctrl+O - To open existing files\n" +
                    "\u2022 Ctrl+S - To save the file\n" +
                    "\u2022 Ctrl+Shift+S - To save the file in a specific location\n" +
                    "\u2022 Ctrl+W - To close the application\n" +
                    "\u2022 Ctrl+Z - To undo one action\n" +
                    "\u2022 Ctrl+Y - To redo one action\n" +
                    "\u2022 Ctrl+X - To cut\n" +
                    "\u2022 Ctrl+C - To copy\n" +
                    "\u2022 Ctrl+V - To paste\n" +
                    "\u2022 Ctrl+F - To find\n" +
                    "\u2022 Ctrl+H - To replace\n" +
                    "\u2022 Ctrl+A - To select all\n" +
                    "\u2022 F5 - Insert time\n\n" + 
                    "Developed by Team #7", "About", JOptionPane.INFORMATION_MESSAGE);
    }
}
