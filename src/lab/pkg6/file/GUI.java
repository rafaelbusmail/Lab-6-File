package lab.pkg6.file;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class GUI extends JFrame {

    private JTextPane textPane;
    private FileManager fileManager;
    private String rutaActual = null;

    private JButton btnNuevo, btnAbrir, btnGuardar;
    private JToggleButton btnNegrita, btnCursiva, btnSubrayado;
    private JButton btnColor;

    private JComboBox<String> comboFuente;
    private JComboBox<Integer> comboTamano;

    private JPanel panelColores;
    private int indexColor = 0;

    private boolean updatingControls = false;

    public GUI() {
        fileManager = new FileManager();
        initComponents();
    }

    private void initComponents() {
        setTitle("Editor de Texto");
        setSize(960, 640);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setRollover(true);

        btnNuevo = new JButton("Nuevo");
        btnAbrir = new JButton("Abrir");
        btnGuardar = new JButton("Guardar");

        btnNegrita = new JToggleButton("N");
        btnCursiva = new JToggleButton("K");
        btnSubrayado = new JToggleButton("S");

        btnNegrita.setFont(new Font("Arial", Font.BOLD, 12));
        btnCursiva.setFont(new Font("Arial", Font.ITALIC, 12));
        btnNegrita.setPreferredSize(new Dimension(32, 26));
        btnCursiva.setPreferredSize(new Dimension(32, 26));
        btnSubrayado.setPreferredSize(new Dimension(32, 26));
        btnNegrita.setFocusable(false);
        btnCursiva.setFocusable(false);
        btnSubrayado.setFocusable(false);

        toolbar.add(btnNuevo);
        toolbar.add(btnAbrir);
        toolbar.add(btnGuardar);
        toolbar.addSeparator();
        toolbar.add(btnNegrita);
        toolbar.add(btnCursiva);
        toolbar.add(btnSubrayado);

        // Panel izquierdo: Fuente / Tamaño / Color
        JPanel panelIzquierdo = new JPanel(new GridBagLayout());
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));

        GridBagConstraints gLbl = new GridBagConstraints();
        gLbl.anchor = GridBagConstraints.WEST;
        gLbl.insets = new Insets(1, 2, 1, 6);
        gLbl.gridx = 0;

        GridBagConstraints gCtrl = new GridBagConstraints();
        gCtrl.fill = GridBagConstraints.HORIZONTAL;
        gCtrl.weightx = 1.0;
        gCtrl.insets = new Insets(1, 0, 1, 2);
        gCtrl.gridx = 1;

        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        comboFuente = new JComboBox<>(fonts);
        comboFuente.setSelectedItem("Arial");
        comboFuente.setPreferredSize(new Dimension(180, 22));

        Integer[] sizes = {8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 28, 32, 36, 48, 64, 72, 92};
        comboTamano = new JComboBox<>(sizes);
        comboTamano.setSelectedItem(14);
        comboTamano.setEditable(true);
        comboTamano.setPreferredSize(new Dimension(180, 22));

        btnColor = new JButton("Color");
        btnColor.setFocusable(false);
        btnColor.setMargin(new Insets(1, 6, 1, 6));

        gLbl.gridy = 0;
        gCtrl.gridy = 0;
        panelIzquierdo.add(new JLabel("Fuente"), gLbl);
        panelIzquierdo.add(comboFuente, gCtrl);

        gLbl.gridy = 1;
        gCtrl.gridy = 1;
        panelIzquierdo.add(new JLabel("Tamaño"), gLbl);
        panelIzquierdo.add(comboTamano, gCtrl);

        gLbl.gridy = 2;
        gCtrl.gridy = 2;
        panelIzquierdo.add(new JLabel("Color"), gLbl);
        panelIzquierdo.add(btnColor, gCtrl);

        
        JPanel panelDerecho = new JPanel(new BorderLayout(0, 2));
        panelDerecho.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
        panelDerecho.add(new JLabel("Colores utilizados:"), BorderLayout.NORTH);

        panelColores = new JPanel(new GridLayout(3, 8, 2, 2));
        for (int i = 0; i < 24; i++) {
            JPanel slot = crearSlotColor(Color.WHITE);
            panelColores.add(slot);
        }
        panelDerecho.add(panelColores, BorderLayout.CENTER);

        JPanel panelControles = new JPanel();
        panelControles.setLayout(new BoxLayout(panelControles, BoxLayout.X_AXIS));
        panelControles.add(panelIzquierdo);
        panelControles.add(panelDerecho);
        panelControles.add(Box.createHorizontalGlue());

        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.add(toolbar, BorderLayout.NORTH);
        panelNorte.add(panelControles, BorderLayout.CENTER);

        textPane = new JTextPane();
        textPane.setBackground(Color.WHITE);
        textPane.setMargin(new Insets(10, 12, 10, 12));
        JScrollPane scroll = new JScrollPane(textPane);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(panelNorte, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        btnNuevo.addActionListener(e -> nuevo());
        btnAbrir.addActionListener(e -> abrir());
        btnGuardar.addActionListener(e -> guardar());

        comboFuente.addActionListener(e -> {
            if (!updatingControls) {
                aplicarAtributo(StyleConstants.FontFamily, comboFuente.getSelectedItem());
            }
        });

        comboTamano.addActionListener(e -> {
            if (!updatingControls) {
                aplicarTamano();
            }
        });

        btnNegrita.addActionListener(e -> {
            if (!updatingControls) {
                aplicarAtributo(StyleConstants.Bold, btnNegrita.isSelected());
            }
        });

        btnCursiva.addActionListener(e -> {
            if (!updatingControls) {
                aplicarAtributo(StyleConstants.Italic, btnCursiva.isSelected());
            }
        });

        btnSubrayado.addActionListener(e -> {
            if (!updatingControls) {
                aplicarAtributo(StyleConstants.Underline, btnSubrayado.isSelected());
            }
        });

        btnColor.addActionListener(e -> elegirColor());

        textPane.addCaretListener(e -> actualizarToolbar());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel crearSlotColor(Color color) {
        JPanel slot = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        slot.setBackground(color);
        slot.setPreferredSize(new Dimension(26, 20));
        slot.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        slot.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return slot;
    }

    private void actualizarToolbar() {
        updatingControls = true;
        try {
            int pos = textPane.getCaretPosition();
            StyledDocument doc = textPane.getStyledDocument();
            Element elem = doc.getCharacterElement(pos > 0 ? pos - 1 : pos);
            AttributeSet a = elem.getAttributes();

            btnNegrita.setSelected(StyleConstants.isBold(a));
            btnCursiva.setSelected(StyleConstants.isItalic(a));
            btnSubrayado.setSelected(StyleConstants.isUnderline(a));

            String fam = StyleConstants.getFontFamily(a);
            if (fam != null) {
                comboFuente.setSelectedItem(fam);
            }
            comboTamano.setSelectedItem(StyleConstants.getFontSize(a));
        } finally {
            updatingControls = false;
        }
    }

    private void aplicarAtributo(Object key, Object value) {
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        if (key == StyleConstants.FontFamily) {
            StyleConstants.setFontFamily(attrs, (String) value);
        } else if (key == StyleConstants.Bold) {
            StyleConstants.setBold(attrs, (Boolean) value);
        } else if (key == StyleConstants.Italic) {
            StyleConstants.setItalic(attrs, (Boolean) value);
        } else if (key == StyleConstants.Underline) {
            StyleConstants.setUnderline(attrs, (Boolean) value);
        } else if (key == StyleConstants.Foreground) {
            StyleConstants.setForeground(attrs, (Color) value);
        }
        aplicarAlDocumento(attrs);
    }

    private void aplicarTamano() {
        try {
            int sz = Integer.parseInt(comboTamano.getSelectedItem().toString().trim());
            if (sz < 1 || sz > 500) {
                return;
            }
            SimpleAttributeSet attrs = new SimpleAttributeSet();
            StyleConstants.setFontSize(attrs, sz);
            aplicarAlDocumento(attrs);
        } catch (NumberFormatException ignore) {
        }
    }

    private void aplicarAlDocumento(SimpleAttributeSet attrs) {
        int s = textPane.getSelectionStart();
        int e = textPane.getSelectionEnd();
        StyledDocument doc = textPane.getStyledDocument();
        if (s != e) {
            doc.setCharacterAttributes(s, e - s, attrs, false);
        } else {
            textPane.getInputAttributes().addAttributes(attrs);
        }
        textPane.requestFocusInWindow();
    }

    private void elegirColor() {
        Color color = JColorChooser.showDialog(this, "Elegir color", Color.BLACK);
        if (color == null) {
            return;
        }
        aplicarAtributo(StyleConstants.Foreground, color);
        agregarColorUsado(color);
    }

    private void agregarColorUsado(Color color) {
        int idx = indexColor % panelColores.getComponentCount();
        JPanel slot = (JPanel) panelColores.getComponent(idx);

        slot.setBackground(color);


        for (MouseListener ml : slot.getMouseListeners()) {
            slot.removeMouseListener(ml);
        }
        slot.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                aplicarAtributo(StyleConstants.Foreground, color);
            }
        });

        slot.repaint();
        indexColor++;
        panelColores.revalidate();
        panelColores.repaint();
    }

    private void nuevo() {
        textPane.setText("");
        textPane.setDocument(new DefaultStyledDocument());
        rutaActual = null;
        setTitle("Editor de Texto - Nuevo");
    }

    private void abrir() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                rutaActual = chooser.getSelectedFile().getAbsolutePath();
                fileManager.abrir(textPane, rutaActual);
                setTitle("Editor de Texto - " + chooser.getSelectedFile().getName());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al abrir: " + e.getMessage());
            }
        }
    }

    private void guardar() {
        if (rutaActual == null) {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                rutaActual = chooser.getSelectedFile().getAbsolutePath();
                if (!rutaActual.endsWith(".docx")) {
                    rutaActual += ".docx";
                }
            } else {
                return;
            }
        }
        try {
            fileManager.guardar(textPane, rutaActual);
            JOptionPane.showMessageDialog(this, "Guardado correctamente.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage());
        }
    }
}
