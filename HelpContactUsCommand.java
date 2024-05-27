import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class HelpContactUsCommand implements Command {

    @Override
    public void execute() {
        try {
            Desktop.getDesktop().browse(new URI("https://t.me/bdcofXjTcvQxZWY6"));
        } catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }
}
