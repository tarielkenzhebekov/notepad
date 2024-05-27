import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class DocumentNumberFilter extends DocumentFilter {

    @Override
    public void insertString(FilterBypass fb, 
                             int offs, 
                             String str, 
                             AttributeSet a) 
        throws BadLocationException {

        if (isUnsignedInteger(str)) {
            super.insertString(fb, offs, str, a);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offs,
                        int length, 
                        String str, AttributeSet a)
        throws BadLocationException {

        if (isUnsignedInteger(str)) {
            super.insertString(fb, offs, str, a);
        }
    }

    private boolean isUnsignedInteger(String str) {
        Integer i;
        try {
            i = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }

        if (i < 0) {
            return false;
        }

        return true;
    }
}
