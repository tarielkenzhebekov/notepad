import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class WindowController implements WindowListener {

    private final ActionController controller;

    public WindowController(ActionController controller){
        this.controller = controller;
    }

    @Override
    public void windowClosing(WindowEvent event) {
        Command command = new FileCloseCommand(controller);
        command.execute();
    }

    @Override
    public void windowOpened(WindowEvent event) {

    }

    @Override
    public void windowClosed(WindowEvent event) {

    }

    @Override
    public void windowIconified(WindowEvent event) {

    }

    @Override
    public void windowDeiconified(WindowEvent event) {

    }

    @Override
    public void windowActivated(WindowEvent event) {

    }

    @Override
    public void windowDeactivated(WindowEvent event) {

    }
}
