/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab.pkg6.file;

import java.awt.Frame;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

/**
 *
 * @author janinadiaz
 */
public class GUI_fonts extends JDialog{
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

    private final Integer[] SIZES  = {8,9,10,11,12,14,16,18,20,22,24,28,32,36,48,72};

    public GUI_fonts(Frame owner, JTextPane textPane) {
        super(owner, "Fuente", true);
        this.textPane = textPane;
        pack();
        setLocationRelativeTo(owner);
        setResizable(false);
    }
    
}
