    package lab.pkg6.file;

    import javax.swing.*;
    import javax.swing.text.*;
    import java.awt.*;
    import java.awt.event.*;
    import java.io.*;

    public class GUI extends JFrame {

        private JTextPane textPane;
        private Lab6File lab6File;
        private String rutaActual = null;

        // Botones de archivo
        private JButton btnNuevo, btnAbrir, btnGuardar;

        // Botones de formato directo
        private JButton btnNegrita, btnCursiva, btnSubrayado;
        private JButton btnFuente;  // abre GUI_fonts
        private JButton btnColor;   // abre JColorChooser

        // Panel de colores usados
        private JPanel panelColores;
        private int indexColor = 0;

        public GUI() {
            lab6File = new Lab6File();
            initComponents();
        }

        private void initComponents() {
            setTitle("Editor de Texto");
            setSize(900, 600);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLayout(new BorderLayout());

            // ── TOOLBAR ──────────────────────────────────────────────────
            JToolBar toolbar = new JToolBar();
            toolbar.setFloatable(false);

            btnNuevo   = new JButton("Nuevo");
            btnAbrir   = new JButton("Abrir");
            btnGuardar = new JButton("Guardar");

            btnNegrita   = new JButton("N");
            btnCursiva   = new JButton("K");
            btnSubrayado = new JButton("S");
            btnFuente    = new JButton("Fuente...");
            btnColor     = new JButton("Color");

            btnNegrita.setFont(new Font("Arial", Font.BOLD, 12));
            btnCursiva.setFont(new Font("Arial", Font.ITALIC, 12));

            toolbar.add(btnNuevo);
            toolbar.add(btnAbrir);
            toolbar.add(btnGuardar);
            toolbar.addSeparator();
            toolbar.add(btnNegrita);
            toolbar.add(btnCursiva);
            toolbar.add(btnSubrayado);
            toolbar.addSeparator();
            toolbar.add(btnFuente);
            toolbar.add(btnColor);

            // ── PANEL COLORES USADOS ─────────────────────────────────────
            JPanel panelDerecho = new JPanel(new BorderLayout());
            panelDerecho.add(new JLabel("Colores utilizados:"), BorderLayout.NORTH);
            panelColores = new JPanel(new GridLayout(3, 4, 2, 2));
            panelColores.setPreferredSize(new Dimension(180, 80));
            for (int i = 0; i < 12; i++) {
                JButton btn = new JButton();
                btn.setBackground(Color.WHITE);
                btn.setOpaque(true);
                btn.setBorderPainted(true);
                panelColores.add(btn);
            }
            panelDerecho.add(panelColores, BorderLayout.CENTER);
            panelDerecho.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

            // ── PANEL SUPERIOR ───────────────────────────────────────────
            JPanel panelSuperior = new JPanel(new BorderLayout());
            panelSuperior.add(toolbar, BorderLayout.CENTER);
            panelSuperior.add(panelDerecho, BorderLayout.EAST);

            // ── AREA DE TEXTO ────────────────────────────────────────────
            textPane = new JTextPane();
            JScrollPane scroll = new JScrollPane(textPane);

            add(panelSuperior, BorderLayout.NORTH);
            add(scroll, BorderLayout.CENTER);

            // ── LISTENERS ────────────────────────────────────────────────
            btnNuevo.addActionListener(e -> nuevo());
            btnAbrir.addActionListener(e -> abrir());
            btnGuardar.addActionListener(e -> guardar());

            btnNegrita.addActionListener(e -> toggleFormato(StyleConstants.Bold));
            btnCursiva.addActionListener(e -> toggleFormato(StyleConstants.Italic));
            btnSubrayado.addActionListener(e -> toggleFormato(StyleConstants.Underline));

            // Abre GUI_fonts como dialogo modal
            btnFuente.addActionListener(e ->
                new GUI_fonts((Frame) SwingUtilities.getWindowAncestor(textPane), textPane)
                    .setVisible(true)
            );

            btnColor.addActionListener(e -> elegirColor());

            setLocationRelativeTo(null);
            setVisible(true);
        }

        // ── ARCHIVO ──────────────────────────────────────────────────────

        private void nuevo() {
            textPane.setText("");
            rutaActual = null;
            setTitle("Editor de Texto - Nuevo");
        }

        private void abrir() {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    rutaActual = chooser.getSelectedFile().getAbsolutePath();
                    lab6File.abrir(textPane, rutaActual);
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
                    if (!rutaActual.endsWith(".docx"))
                        rutaActual += ".docx";
                } else return;
            }
            try {
                lab6File.guardar(textPane, rutaActual);
                JOptionPane.showMessageDialog(this, "Guardado correctamente.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage());
            }
        }

        // ── FORMATO ──────────────────────────────────────────────────────

        // Toggle generico para negrita, cursiva y subrayado
        private void toggleFormato(Object key) {
            StyledDocument doc = textPane.getStyledDocument();
            int inicio = textPane.getSelectionStart();
            int fin    = textPane.getSelectionEnd();
            if (inicio == fin) return;

            Element elem = doc.getCharacterElement(inicio);
            AttributeSet as = elem.getAttributes();

            SimpleAttributeSet attr = new SimpleAttributeSet();
            if (key == StyleConstants.Bold)
                StyleConstants.setBold(attr, !StyleConstants.isBold(as));
            else if (key == StyleConstants.Italic)
                StyleConstants.setItalic(attr, !StyleConstants.isItalic(as));
            else if (key == StyleConstants.Underline)
                StyleConstants.setUnderline(attr, !StyleConstants.isUnderline(as));

            doc.setCharacterAttributes(inicio, fin - inicio, attr, false);
        }

        private void elegirColor() {
            Color color = JColorChooser.showDialog(this, "Elegir color", Color.BLACK);
            if (color == null) return;

            int inicio = textPane.getSelectionStart();
            int fin    = textPane.getSelectionEnd();
            if (inicio != fin) {
                SimpleAttributeSet attr = new SimpleAttributeSet();
                StyleConstants.setForeground(attr, color);
                textPane.getStyledDocument()
                        .setCharacterAttributes(inicio, fin - inicio, attr, false);
            }
            agregarColorUsado(color);
        }

        private void agregarColorUsado(Color color) {
            int idx = indexColor % panelColores.getComponentCount();
            JButton btn = (JButton) panelColores.getComponent(idx);
            btn.setBackground(color);

            for (ActionListener al : btn.getActionListeners())
                btn.removeActionListener(al);

            btn.addActionListener(e -> aplicarColorDirecto(color));
            indexColor++;
            panelColores.revalidate();
            panelColores.repaint();
        }

        private void aplicarColorDirecto(Color color) {
            int inicio = textPane.getSelectionStart();
            int fin    = textPane.getSelectionEnd();
            if (inicio == fin) return;

            SimpleAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setForeground(attr, color);
            textPane.getStyledDocument()
                    .setCharacterAttributes(inicio, fin - inicio, attr, false);
        }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> new GUI());
        }
    }