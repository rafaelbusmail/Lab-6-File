/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab.pkg6.file;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author janinadiaz
 */
public class GUI_fonts extends JDialog {

    private JList<String> fontFamilyList;
    private JList<String> fontStyleList;
    private JList<Integer> fontSizeList;
    private JTextField fontFamilyField;
    private JTextField fontSizeField;
    private JLabel previewLabel;
    private JPanel previewPanel;
    private JButton btnAceptar;
    private JButton btnCancelar;
    private JTextPane textPane;
    private final String[] STYLES = {"Normal", "Negrita", "Cursiva", "Negrita Cursiva"};

    private final Integer[] SIZES = {8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 28, 32, 36, 48, 72};

    public GUI_fonts(Frame owner, JTextPane textPane) {
        super(owner, "Fuente", true);
        this.textPane = textPane;
        pack();
        initComponents();
        setupLayout();
        setupListeners();
        cargarValoresActuales();
        setLocationRelativeTo(owner);
        setResizable(false);
    }

    private void initComponents() {
        fontFamilyField = new JTextField(20);
        fontFamilyField.setToolTipText("Escriba para filtrar fuentes");
        String[] sysfonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontFamilyList = new JList<>(sysfonts);
        fontFamilyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fontFamilyList.setVisibleRowCount(8);
        fontStyleList = new JList<>(STYLES);
        fontStyleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fontStyleList.setVisibleRowCount(8);
        fontStyleList.setSelectedIndex(0);
        fontSizeField = new JTextField(4);
        fontSizeField.setText("14");
        fontSizeField.setToolTipText("Escriba o seleccione el tamaño");
        fontSizeList = new JList<>(SIZES);
        fontSizeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fontSizeList.setVisibleRowCount(8);
        fontSizeList.setSelectedValue(14, true);

        previewLabel = new JLabel("AaBbYyZz 123");
        previewLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        previewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        previewLabel.setForeground(Color.BLACK);

        btnAceptar = new JButton("Aceptar");
        btnCancelar = new JButton("Cancelar");
    }

    private void setupLayout() {
        JPanel main = new JPanel(new BorderLayout(8, 8));
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBorder(BorderFactory.createTitledBorder("Vista Previa"));
        previewPanel.setPreferredSize(new Dimension(430, 70));
        previewPanel.add(previewLabel, BorderLayout.CENTER);

        JPanel cols = new JPanel(new GridLayout(1, 3, 8, 0));

        JPanel col1 = new JPanel(new BorderLayout(0, 4));
        col1.add(new JLabel("Fuente:"), BorderLayout.NORTH);
        col1.add(fontFamilyField, BorderLayout.CENTER);
        JScrollPane spFuentes = new JScrollPane(fontFamilyList);
        spFuentes.setPreferredSize(new Dimension(160, 140));
        col1.add(spFuentes, BorderLayout.SOUTH);

        JPanel col2 = new JPanel(new BorderLayout(0, 4));
        col2.add(new JLabel("Estilo:"), BorderLayout.NORTH);
        col2.add(new JScrollPane(fontStyleList), BorderLayout.CENTER);

        JPanel col3 = new JPanel(new BorderLayout(0, 4));
        col3.add(new JLabel("Tamaño:"), BorderLayout.NORTH);
        col3.add(fontSizeField, BorderLayout.CENTER);
        JScrollPane spSizes = new JScrollPane(fontSizeList);
        spSizes.setPreferredSize(new Dimension(60, 140));
        col3.add(spSizes, BorderLayout.SOUTH);
        cols.add(col1);
        cols.add(col2);
        cols.add(col3);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(btnAceptar);
        btnPanel.add(btnCancelar);

        main.add(previewPanel, BorderLayout.NORTH);
        main.add(cols, BorderLayout.CENTER);
        main.add(btnPanel, BorderLayout.SOUTH);

        setContentPane(main);
        getRootPane().setDefaultButton(btnAceptar);

    }

    private void filtrarFuentes(String query) {
        String[] all = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        if (query == null || query.trim().isEmpty()) {
            fontFamilyList.setListData(all);
            return;
        }

        String q = query.toLowerCase();
        java.util.List<String> filtered = new java.util.ArrayList<>();
        for (String f : all) {
            if (f.toLowerCase().contains(q)) {
                filtered.add(f);
            }
        }

        fontFamilyList.setListData(filtered.toArray(new String[0]));
    }

    private void actualizarPreview() {
        String family = fontFamilyList.getSelectedValue();
        if (family == null) {
            return;
        }
        previewLabel.setFont(new Font(family, estiloAWT(), tamano()));
    }

    private int tamano() {
        try {
            int sz = Integer.parseInt(fontSizeField.getText().trim());
            return Math.max(6, Math.min(500, sz));
        } catch (NumberFormatException e) {
            return 14;
        }
    }

    private void setupListeners() {

        fontFamilyField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarFuentes(fontFamilyField.getText());
            }
        });

        fontFamilyList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                actualizarPreview();
            }
        });

        fontStyleList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                actualizarPreview();
            }
        });

        fontSizeList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Integer sz = fontSizeList.getSelectedValue();
                if (sz != null) {
                    fontSizeField.setText(String.valueOf(sz));
                    actualizarPreview();
                }
            }
        });

        fontSizeField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                actualizarPreview();
            }
        });

        btnAceptar.addActionListener(e -> {
            aplicarFormato();
            dispose();
        });

        btnCancelar.addActionListener(e -> dispose());
    }

    private int estiloAWT() {
        String sel = fontStyleList.getSelectedValue();
        if (sel == null) {
            return Font.PLAIN;
        }
        switch (sel) {
            case "Negrita":
                return Font.BOLD;
            case "Cursiva":
                return Font.ITALIC;
            case "Negrita Cursiva":
                return Font.BOLD | Font.ITALIC;
            default:
                return Font.PLAIN;
        }
    }

    private void aplicarFormato() {
        String family = fontFamilyList.getSelectedValue();
        if (family == null) {
            return;
        }

        int awtStyle = estiloAWT();
        int size = tamano();

        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attrs, family);
        StyleConstants.setFontSize(attrs, size);
        StyleConstants.setBold(attrs, (awtStyle & Font.BOLD) != 0);
        StyleConstants.setItalic(attrs, (awtStyle & Font.ITALIC) != 0);

        int s = textPane.getSelectionStart();
        int e = textPane.getSelectionEnd();
        StyledDocument doc = textPane.getStyledDocument();

        if (s != e) {
            doc.setCharacterAttributes(s, e - s, attrs, false);
        } else {
            textPane.getInputAttributes().addAttributes(attrs);
        }

    }

    private void cargarValoresActuales() {
        int pos = textPane.getCaretPosition();
        StyledDocument doc = textPane.getStyledDocument();
        Element elem = doc.getCharacterElement(pos > 0 ? pos - 1 : pos);
        AttributeSet a = elem.getAttributes();

        String fam = StyleConstants.getFontFamily(a);
        if (fam != null) {
            fontFamilyList.setSelectedValue(fam, true);
            fontFamilyField.setText(fam);
        }

        int sz = StyleConstants.getFontSize(a);
        fontSizeField.setText(String.valueOf(sz));
        fontSizeList.setSelectedValue(sz, true);


        boolean bold = StyleConstants.isBold(a);
        boolean italic = StyleConstants.isItalic(a);
        if (bold && italic) {
            fontStyleList.setSelectedValue("Negrita Cursiva", true);
        } else if (bold) {
            fontStyleList.setSelectedValue("Negrita", true);
        } else if (italic) {
            fontStyleList.setSelectedValue("Cursiva", true);
        } else {
            fontStyleList.setSelectedValue("Normal", true);
        }

        actualizarPreview();
    }

}
