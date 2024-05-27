import javax.swing.JTextField;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFrame;

public class ReplaceDialog {

    private JDialog replaceDialog;
    private JTextField searchTextField;
    private JTextField replaceWithTextField;

    public ReplaceDialog(ActionController controller) {
        replaceDialog = new JDialog(); //
        replaceDialog.setTitle("Word replacement");

        JLabel wordLabel = new JLabel("Find");
        wordLabel.setSize(wordLabel.getPreferredSize().width, wordLabel.getPreferredSize().height);
        wordLabel.setLocation(20, 20);
        replaceDialog.add(wordLabel);

        searchTextField = new JTextField();
        searchTextField.setSize(100, 20);
        searchTextField.setLocation(10, 50);
        replaceDialog.add(searchTextField);

        JLabel replacementWordLabel = new JLabel("Replace to");
        replacementWordLabel.setSize(replacementWordLabel.getPreferredSize().width,
                replacementWordLabel.getPreferredSize().height);
        replacementWordLabel.setLocation(160, 20);
        replaceDialog.add(replacementWordLabel);


        replaceWithTextField = new JTextField();
        replaceWithTextField.setSize(100, 20);
        replaceWithTextField.setLocation(160, 50);
        replaceDialog.add(replaceWithTextField);

        JButton button = new JButton("Replace");
        button.setSize(80, 20);
        button.setLocation(100, 100);
        button.addActionListener(controller);
        button.setActionCommand("EditReplace");
        replaceDialog.add(button);

        replaceDialog.setLayout(null);
        replaceDialog.setSize(300, 200);
        replaceDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        replaceDialog.setModal(true);
        replaceDialog.setResizable(false);

    }

    public String getSearchWord() {
        return searchTextField.getText();
    }

    public String getReplacementWord() {
        return replaceWithTextField.getText();
    }

    public void close() {
        replaceDialog.dispose();
        searchTextField.setText("");
        replaceWithTextField.setText("");
    }

    public void setLocation(int x, int y) {
        replaceDialog.setLocation(x, y);
    }

    public void setVisible(boolean visible) {
        replaceDialog.setVisible(visible);
    }
}
