import java.text.SimpleDateFormat;
import java.util.Date;

class EditTimeAndDate implements Command {

    private Viewer viewer;

    public EditTimeAndDate(Viewer viewer) {
        this.viewer = viewer;
    }

    @Override
    public void execute() {
        viewer.insert(new SimpleDateFormat("HH:mm:ss  dd.MM.yyyy")
                .format(new Date()), viewer.getCaretPosition());
    }
}
