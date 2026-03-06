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
import javax.swing.*;

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

}
