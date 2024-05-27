import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Position.Bias;

public class FontDialog {

    private final JFrame parentFrame;

    private final JDialog dialog;

    private JTextField fontNameTextFieald;

    private JTextField fontStyleTextField;

    private JTextField fontSizeTextField;

    private JList<String> fontNameList;
    
    private JList<String> fontStyleList;

    private JList<Integer> fontSizeList;

    private JLabel exampleLabel;

    private JCheckBox checkBox;

    private FontNameListSelectionListener fontNameListSelectionListener;

    private FontStyleListSelectionListener fontStyleListSelectionListener;

    private FontSizeListSelectionListener fontSizeListSelectionListener;

    public FontDialog(JFrame frame, ActionController controller, Font initialFont) {
        parentFrame = frame;

        JPanel gridPane = initializeGridPane(initialFont);
        
        JPanel flowPane = new JPanel();
        FlowLayout flowLayout = new FlowLayout(FlowLayout.RIGHT); 
        flowPane.setLayout(flowLayout);

        JButton buttonOk = createButton("Ok", controller, "FontDialogOk");
        JButton buttonCancel = createButton("Cancel", controller, "FontDialogCancel");

        flowPane.add(buttonOk);
        flowPane.add(buttonCancel);

        JPanel root = new JPanel();
        BoxLayout boxLayout = new BoxLayout(root, BoxLayout.Y_AXIS);
        root.setLayout(boxLayout);

        root.add(gridPane);
        root.add(flowPane);

        dialog = new JDialog(parentFrame, "Font", true);

        dialog.add(root);
        dialog.pack();
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
    }

    private JPanel initializeGridPane(Font initialFont) {
        JLabel fontNameLabel = new JLabel("Font:");
        JLabel fontStyleLabel = new JLabel("Style:");
        JLabel fontSizeLabel = new JLabel("Size:");

        fontNameTextFieald = creaTextField();
        fontStyleTextField = creaTextField();
        fontSizeTextField = creaTextField();

        DocumentNumberFilter documentNumberFilter = new DocumentNumberFilter();
        DocumentSizeFilter documentSizeFilter = new DocumentSizeFilter(documentNumberFilter, 2);
        ((AbstractDocument) fontSizeTextField.getDocument()).setDocumentFilter(documentSizeFilter);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontNames = ge.getAvailableFontFamilyNames();
        
        String[] fontStyles = {"Plain", "Bold", "Italic", "Bold Italic"};

        int numberOfElements = 25;
        int fontSize = 8;
        Integer[] fontSizes = generateFontSizesForJList(fontSize, numberOfElements);
        
        fontNameList = createList(fontNames);
        fontStyleList = createList(fontStyles);
        fontSizeList = createList(fontSizes);

        int width = 200;
        int height = 200;
        JScrollPane scrolledFontNameList = createScrolledList(fontNameList, width, height);
        width = 100;
        JScrollPane scrolledFontStyleList = createScrolledList(fontStyleList, width, height);
        width = 50;
        JScrollPane scrolledFontSizeList = createScrolledList(fontSizeList, width, height);

        fontNameListSelectionListener = new FontNameListSelectionListener(this);
        fontStyleListSelectionListener = new FontStyleListSelectionListener(this);
        fontSizeListSelectionListener = new FontSizeListSelectionListener(this);
        
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        TitledBorder titledBorder = new TitledBorder(border);
        titledBorder.setTitle("Example");

        exampleLabel = new JLabel("AaBbYyZz");
        exampleLabel.setFont(initialFont);
        
        GridBagLayout gbl = new GridBagLayout();
        JPanel example = new JPanel(gbl);
        
        Dimension examplePrefferedSize = new Dimension(300, 100);
        example.setPreferredSize(examplePrefferedSize);
        example.add(exampleLabel);
        example.setBorder(titledBorder);
        
        checkBox = new JCheckBox("Underline");
        checkBox.addChangeListener(new FontCheckBoxChangeListener(this));

        JPanel gridPane = new JPanel();
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        gridPane.setLayout(gridBagLayout);

        Insets insets = new Insets(3, 3, 3, 3);
        gridBagConstraints.insets = insets;

        gridBagConstraints.gridx = 0;        
        gridBagConstraints.gridy = 0;

        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        gridPane.add(fontNameLabel, gridBagConstraints);

        gridBagConstraints.gridx++;
        gridPane.add(fontStyleLabel, gridBagConstraints);

        gridBagConstraints.gridx++;
        gridPane.add(fontSizeLabel, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;

        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        gridPane.add(fontNameTextFieald, gridBagConstraints);

        gridBagConstraints.gridx++;
        gridPane.add(fontStyleTextField, gridBagConstraints);

        gridBagConstraints.gridx++;
        gridPane.add(fontSizeTextField, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;

        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridPane.add(scrolledFontNameList, gridBagConstraints);

        gridBagConstraints.gridx++;
        gridPane.add(scrolledFontStyleList, gridBagConstraints);

        gridBagConstraints.gridx++;
        gridPane.add(scrolledFontSizeList, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;

        gridBagConstraints.gridwidth = 3;
        gridPane.add(example, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;

        gridBagConstraints.gridwidth = 1;
        gridPane.add(checkBox, gridBagConstraints);

        return gridPane;
    }

    private JTextField creaTextField() {
        JTextField textField = new JTextField();

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateExampleLabel();
            }
        };

        textField.addActionListener(actionListener);

        FontTextFieldFocusListener fieldFocusListener = new FontTextFieldFocusListener(this);
        textField.addFocusListener(fieldFocusListener);

        return textField;
    }

    private JButton createButton(String text, ActionController controller, String actionCommand) {
        JButton button = new JButton(text);

        button.addActionListener(controller);
        button.setActionCommand(actionCommand);

        Dimension d = button.getPreferredSize();
        d.width = 80;
        button.setPreferredSize(d);
    
        return button;
    }

    private Integer[] generateFontSizesForJList(int initialFontSize, int numberOfElements) {
        Integer[] fontSizes = new Integer[numberOfElements];

        int fontSize = initialFontSize;
        for (int i = 0; i < numberOfElements; i++) {
            fontSizes[i] = fontSize;
            if (8 <= fontSize && fontSize < 12) {
                fontSize++;
            } else {
                fontSize = fontSize + 2;
            }
        }

        return fontSizes;
    }

    private <T> JList<T> createList(T[] data) {        
        JList<T> list = new JList<>(data);

        list.setSelectedIndex(0);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrolledList = new JScrollPane(list);

        Dimension d = new Dimension(200, 200); 

        scrolledList.setPreferredSize(d);

        return list;
    }

    private <T> JScrollPane createScrolledList(JList<T> list, int width, int heigth) {
        JScrollPane scrolledList = new JScrollPane(list);

        Dimension d = new Dimension(width, heigth);
        scrolledList.setPreferredSize(d);

        return scrolledList;
    }

    public void hideDialog() {
        dialog.setVisible(false);
    }

    public void showDialog(Font currentFont) {
        int x = parentFrame.getX();
        int y = parentFrame.getY();

        setCurrentFontForExampleLabel(currentFont);
        
        addListenerToList(fontNameList, fontNameListSelectionListener);
        addListenerToList(fontStyleList, fontStyleListSelectionListener);
        addListenerToList(fontSizeList, fontSizeListSelectionListener);

        dialog.setLocation(x + 150, y + 150);
        dialog.setVisible(true);

        removeListenerFromList(fontNameList, fontNameListSelectionListener);
        removeListenerFromList(fontStyleList, fontStyleListSelectionListener);
        removeListenerFromList(fontSizeList, fontSizeListSelectionListener);
    }

    private <T> void addListenerToList(JList<T> list, javax.swing.event.ListSelectionListener listener) {
        list.getSelectionModel().addListSelectionListener(listener);
    }

    private <T> void removeListenerFromList(JList<T> list, javax.swing.event.ListSelectionListener listener) {
        list.getSelectionModel().removeListSelectionListener(listener);
    }

    private void setCurrentFontForExampleLabel(Font font) {
        String fontName = font.getName();
        int index = fontNameList.getNextMatch(fontName, 0, Bias.Forward);
        fontNameList.setSelectedIndex(index);

        String fontStyle = "";
        if (font.isBold() && font.isItalic()) {
            fontStyle = "Bold Italic";
        } else if (font.isBold()) {
            fontStyle = "Bold";
        } else if (font.isItalic()) {
            fontStyle = "Italic";
        } else {
            fontStyle = "Plain";
        }

        index = fontStyleList.getNextMatch(fontStyle, 0, Bias.Forward);
        fontStyleList.setSelectedIndex(index);

        String fontSize = String.valueOf(font.getSize());

        index = fontSizeList.getNextMatch(fontSize, 0, Bias.Forward);
        fontSizeList.setSelectedIndex(index);
        
        Map<TextAttribute, ?> attributes = font.getAttributes();
        if (attributes.get(TextAttribute.UNDERLINE) == TextAttribute.UNDERLINE_ON) {
            checkBox.setSelected(true);
        } else {
            checkBox.setSelected(false);
        }
        
        updateFontNameTextField();
        updateFontSyteTextField();
        updateFontSizeTextField(fontSize);

        updateExampleLabel(font);
    }

    public void updateFontNameTextField() {
        String fontName = fontNameList.getSelectedValue();
        fontNameTextFieald.setText(fontName);
    }

    public void updateFontSyteTextField() {
        String fontStyle = fontStyleList.getSelectedValue();
        fontStyleTextField.setText(fontStyle);
    }

    public void updateFontSizeTextField(String value) {
        try {
            Document doc = fontSizeTextField.getDocument();
            doc.remove(0, doc.getLength());
            doc.insertString(0, value, null);
        } catch (BadLocationException ble) {
            System.out.println(ble);
        }
    }

    public void updateFontSizeTextField() {
        int fontSize = fontSizeList.getSelectedValue();
        try {
            Document doc = fontSizeTextField.getDocument();
            doc.remove(0, doc.getLength());
            doc.insertString(0, String.valueOf(fontSize), null);
        } catch (BadLocationException ble) {
            System.out.println(ble);
        }
    }

    public void updateExampleLabel() {
        Font font = getFontFromTextFields();

        if (font == null) {
            return;
        }

        exampleLabel.setFont(font);
    }

    private void updateExampleLabel(Font font) {
        if (font == null) {
            return;
        }

        exampleLabel.setFont(font);
    }

    public Font getFontFromTextFields() {
        String fontName = fontNameTextFieald.getText();
        
        String fontStyle = fontStyleTextField.getText();
        int styleID = -1;
        
        switch (fontStyle) {
        case "Bold":
            styleID = Font.BOLD;
            break;
        case "Italic":
            styleID = Font.ITALIC;
            break;
        case "Plain":
            styleID = Font.PLAIN;
            break;
        case "Bold Italic":
            styleID = Font.BOLD | Font.ITALIC;
            break;
        default:
            styleID = Font.PLAIN;
            break;
        }
        
        int fontSize = -1;
        try {
            fontSize = Integer.parseInt(fontSizeTextField.getText());
        } catch (NumberFormatException pr) {
            System.out.println(pr);
            return null;
        }

        Font font = new Font(fontName, styleID, fontSize);
        Map<TextAttribute, Object> attributes = new HashMap<>();

        if (checkBox.isSelected()) {
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        } else {
            attributes.put(TextAttribute.UNDERLINE, -1);
        }
        font = font.deriveFont(attributes);

        return font;
    }
}