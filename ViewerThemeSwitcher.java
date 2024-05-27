import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;

public class ViewerThemeSwitcher implements Command {

    private String actionCommand;
    private Viewer viewer;

    public ViewerThemeSwitcher(Viewer viewer, String actionCommand) {
        this.viewer = viewer;
        this.actionCommand = actionCommand;
    }

    @Override
    public void execute() {
        JFrame frame = viewer.getFrame();

        try {
            switch (actionCommand){
                case "Nimbus":
                    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                    break;
                case "Metal":
                    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                    break;
                case "Motif":
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                    break;
                case "Windows":
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    break;
            }

            SwingUtilities.updateComponentTreeUI(frame);
            frame.setSize(800, 600);
            frame.validate();
            frame.repaint();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
