import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;

public class MouseListener extends MouseAdapter {

    private ActionController controller;

    private JMenuItem editMenuItem;

    public MouseListener(ActionController controller, JMenuItem editMenuItem) {
        this.controller = controller;
        this.editMenuItem = editMenuItem;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        ActionEvent actionEvent = new ActionEvent(editMenuItem, ActionEvent.ACTION_PERFORMED, "Edit");
        controller.actionPerformed(actionEvent);
    }
}
