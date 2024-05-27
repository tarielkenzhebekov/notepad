import java.awt.Font;
import java.awt.Graphics;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class FilePrintCommand implements Printable, Command {

    private final ActionController controller;

    private Command saveCommand;

    private String data;

    private Font font;

    public FilePrintCommand(ActionController controller) {
        this.controller = controller;
        saveCommand = new SaveChangesCommand(controller);
    }

    public int print(Graphics g, PageFormat pf, int pageIndex) {
        List<String> textForPrint = new ArrayList<>();
        FontMetrics metrics = g.getFontMetrics(font);
        int lineHeight = metrics.getHeight();
        int pageWidth = (int) pf.getImageableWidth() - 100;
        String[] lines = data.split("\n");
        StringBuilder lineBuilder = new StringBuilder();

        for (String line : lines) {
            String[] words = line.split("(?<=\\s)|(?=\\s)");

            for (String word : words) {
                int buildingLineWidth = metrics.stringWidth(lineBuilder.toString() + " " + word);

                if (buildingLineWidth <= pageWidth) {
                    lineBuilder.append(word);
                } else {
                    textForPrint.add(lineBuilder.toString());
                    lineBuilder.setLength(0);
                    
                    int wordWidth = metrics.stringWidth(word);
                    while (wordWidth > pageWidth) {
                        int trimIndex = Math.max((pageWidth * word.length()) / wordWidth, 1);
                        String wordPartForPrint = word.substring(0, trimIndex);
                        textForPrint.add(wordPartForPrint);
                        word = word.substring(trimIndex);
                        wordWidth = metrics.stringWidth(word);
                    }
                    lineBuilder.append(word);

                }
            }
            textForPrint.add(lineBuilder.toString());
            lineBuilder.setLength(0);
        }

        int linesPerPage = ((int) pf.getImageableHeight() - 100) / lineHeight;
        int numBreaks = (textForPrint.size() - 1) / linesPerPage;
        int[] pageBreaks = new int[numBreaks];

        for (int i = 0; i < numBreaks; i++) {
            pageBreaks[i] = (i + 1) * linesPerPage;
        }

        if (pageIndex > pageBreaks.length) {
            return NO_SUCH_PAGE;
        }

        g.setFont(font);
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        int x = 50;
        int y = 50;
        int start = (pageIndex == 0) ? 0 : pageBreaks[pageIndex - 1];
        int end = (pageIndex == pageBreaks.length) ? textForPrint.size() : pageBreaks[pageIndex];

        for (int i = start; i < end; i++) {
            y += lineHeight;
            g.drawString(textForPrint.get(i), x, y);
        }
        return PAGE_EXISTS;
    }

    public void printDocument() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);

        boolean ok = job.printDialog();
        if (ok) {            
            JButton abortButton = new JButton("Cancel");
            
            JOptionPane optionPane = new JOptionPane("Printing document ... ", 
                                                     JOptionPane.QUESTION_MESSAGE, 
                                                     JOptionPane.CANCEL_OPTION, 
                                                     null, 
                                                     new Object[]{abortButton},
                                                     abortButton);

            JFrame frame = controller.getViewer().getFrame();
            JDialog dialog = new JDialog(frame);
            dialog.setContentPane(optionPane);
            dialog.pack();
            dialog.setLocationRelativeTo(frame);
            dialog.setResizable(false);

            Runnable printRunnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        dialog.setVisible(true);
                        
                        job.print();
                        
                        dialog.dispose();
                        JOptionPane.showMessageDialog(frame, "The document has been printed!");
                    } catch (PrinterException printerException) {
                        System.out.println("Error: " + printerException);
                    }
                }
            };
            
            Thread thread = new Thread(printRunnable);
            
            abortButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    job.cancel();
                    dialog.setVisible(false);
                }
            });

            thread.start();
         }
    }

    @Override
    public void execute() {
        saveCommand.execute();

        if (controller.getFileHandler().getPath() == null) {
            return;
        }

        if (controller.getFileHandler().isSaved() == false) {
            return;
        }

        Viewer viewer = controller.getViewer();
        data = viewer.getTextAreaContent();
        font = viewer.getTextArea().getFont();
        
        controller.stopAutoSave();
        printDocument();
        controller.startAutoSave();
    }
}