import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class DocumentSizeFilter extends DocumentFilter {

    private final DocumentNumberFilter documentNumberFilter;

    int maxCharacters;

    public DocumentSizeFilter(DocumentNumberFilter documentNumberFilter, int maxChars) {
        this.documentNumberFilter = documentNumberFilter;
        maxCharacters = maxChars;
    }

    @Override
    public void insertString(FilterBypass fb, int offs,
                             String str, AttributeSet a)
        throws BadLocationException {
 
        

        if ((fb.getDocument().getLength() + str.length()) <= maxCharacters) {
            documentNumberFilter.insertString(fb, offs, str, a);
        }
    }
    
    @Override
    public void replace(FilterBypass fb, int offs,
                        int length, 
                        String str, AttributeSet a)
        throws BadLocationException {

        if ((fb.getDocument().getLength() + str.length()
             - length) <= maxCharacters) {
            documentNumberFilter.insertString(fb, offs, str, a);
        }
    }

}