package lab.pkg6.file;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.Color;
import java.io.*;
import org.apache.poi.xwpf.usermodel.*;

public class FileManager {

    public void guardar(JTextPane textPane, String ruta)
            throws IOException, BadLocationException {

        StyledDocument doc = textPane.getStyledDocument();
        String textoCompleto = doc.getText(0, doc.getLength());

        XWPFDocument docx = new XWPFDocument();
        XWPFParagraph parrafo = docx.createParagraph();

        int i = 0;
        while (i < textoCompleto.length()) {

            AttributeSet attrs = doc.getCharacterElement(i).getAttributes();
            String fuente = StyleConstants.getFontFamily(attrs);
            int tamano = StyleConstants.getFontSize(attrs);
            boolean negrita = StyleConstants.isBold(attrs);
            boolean cursiva = StyleConstants.isItalic(attrs);
            boolean subray = StyleConstants.isUnderline(attrs);
            Color color = StyleConstants.getForeground(attrs);

            int inicio = i;
            while (i < textoCompleto.length()) {
                AttributeSet a2 = doc.getCharacterElement(i).getAttributes();
                if (!StyleConstants.getFontFamily(a2).equals(fuente)
                        || StyleConstants.getFontSize(a2) != tamano
                        || StyleConstants.isBold(a2) != negrita
                        || StyleConstants.isItalic(a2) != cursiva
                        || StyleConstants.isUnderline(a2) != subray
                        || !StyleConstants.getForeground(a2).equals(color)) {
                    break;
                }
                i++;
            }

            String segmento = textoCompleto.substring(inicio, i);

            String[] lineas = segmento.split("\n", -1);
            for (int l = 0; l < lineas.length; l++) {
                if (l > 0) {
                    parrafo = docx.createParagraph();
                }
                if (!lineas[l].isEmpty()) {
                    XWPFRun run = parrafo.createRun();
                    run.setFontFamily(fuente);
                    run.setFontSize(tamano);
                    run.setBold(negrita);
                    run.setItalic(cursiva);
                    if (subray) {
                        run.setUnderline(UnderlinePatterns.SINGLE);
                    }
                    run.setColor(String.format("%02X%02X%02X",
                            color.getRed(), color.getGreen(), color.getBlue()));
                    run.setText(lineas[l]);
                }
            }
        }

        try (FileOutputStream fos = new FileOutputStream(ruta)) {
            docx.write(fos);
        }
        docx.close();
    }

    public void abrir(JTextPane textPane, String ruta) throws IOException {
        FileInputStream fis = new FileInputStream(ruta);
        XWPFDocument docx = new XWPFDocument(fis);

        textPane.setText("");
        StyledDocument doc = textPane.getStyledDocument();

        for (XWPFParagraph parrafo : docx.getParagraphs()) {
            for (XWPFRun run : parrafo.getRuns()) {
                String texto = run.getText(0);
                if (texto == null) {
                    continue;
                }

                SimpleAttributeSet attrs = new SimpleAttributeSet();

                String fuente = run.getFontName();
                if (fuente != null) {
                    StyleConstants.setFontFamily(attrs, fuente);
                }

                int tamano = run.getFontSize();
                if (tamano > 0) {
                    StyleConstants.setFontSize(attrs, tamano);
                }

                StyleConstants.setBold(attrs, run.isBold());
                StyleConstants.setItalic(attrs, run.isItalic());
                StyleConstants.setUnderline(attrs,
                        run.getUnderline() != UnderlinePatterns.NONE);

                String colorHex = run.getColor();
                if (colorHex != null && !colorHex.equalsIgnoreCase("auto")) {
                    try {
                        StyleConstants.setForeground(attrs,
                                Color.decode("#" + colorHex));
                    } catch (NumberFormatException ignored) {
                    }
                }

                try {
                    doc.insertString(doc.getLength(), texto, attrs);
                } catch (BadLocationException e) {
                    throw new IOException("Error insertando texto: " + e.getMessage());
                }
            }
            try {
                doc.insertString(doc.getLength(), "\n", new SimpleAttributeSet());
            } catch (BadLocationException e) {
                throw new IOException("Error insertando salto: " + e.getMessage());
            }
        }

        fis.close();
        docx.close();
    }
}
