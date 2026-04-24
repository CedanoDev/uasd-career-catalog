package com.uasd;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

public class Main extends JFrame {

    // ==================== CONEXION ====================
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/uasd_catalog";
    private static final String USUARIO = "uasd_user";
    private static final String PASSWORD = "uasd_password";
    
    private JComboBox<String> comboFacultad;
    private JTextField txtBuscar;
    private JButton btnBuscar, btnTodas;
    private JTable tablaCarreras;
    private DefaultTableModel modeloTabla;

    public Main() {
        setTitle("Catalogo Oficial de Carreras - UASD");
        setSize(1300, 780);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ==================== PANEL SUPERIOR ====================
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 15));
        panelSuperior.setBackground(new Color(0, 51, 102));

        JLabel titulo = new JLabel("Catalogo de Carreras UASD");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);

        comboFacultad = new JComboBox<>();
        comboFacultad.setPreferredSize(new Dimension(380, 40));

        txtBuscar = new JTextField(25);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        btnBuscar = new JButton("Buscar");
        btnTodas = new JButton("Todas las Carreras");
        
        styleButton(btnBuscar, new Color(0, 153, 76));
        styleButton(btnTodas, new Color(0, 102, 204));
        
        panelSuperior.add(titulo);
        panelSuperior.add(comboFacultad);
        panelSuperior.add(txtBuscar);
        panelSuperior.add(btnBuscar);
        panelSuperior.add(btnTodas);
       
        // ==================== TABLA SOLO LECTURA ====================
        String[] columnas = {"ID", "Facultad", "Escuela", "Codigo", "Nombre de la Carrera"};

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaCarreras = new JTable(modeloTabla);
        tablaCarreras.setRowHeight(34);
        tablaCarreras.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaCarreras.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaCarreras.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCarreras.getTableHeader().setReorderingAllowed(false);
        tablaCarreras.setAutoCreateRowSorter(true);
        tablaCarreras.setGridColor(new Color(230, 230, 230));
        tablaCarreras.setShowGrid(true);
        tablaCarreras.setSelectionBackground(new Color(0, 102, 204));
        tablaCarreras.setSelectionForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(tablaCarreras);

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // Cargar datos iniciales
        cargarFacultades();
        cargarTodasLasCarreras();

        // ==================== EVENTOS ====================
        btnBuscar.addActionListener(e -> buscar());
        txtBuscar.addActionListener(e -> buscar());
        txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { buscar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { buscar(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { buscar(); }
        });

        btnTodas.addActionListener(e -> {
            txtBuscar.setText("");           
            cargarTodasLasCarreras();
        });

        comboFacultad.addActionListener(e -> {
            String facultadSeleccionada = (String) comboFacultad.getSelectedItem();
            if (facultadSeleccionada != null && !facultadSeleccionada.equals("Todas las Facultades")) {
                filtrarPorFacultad(facultadSeleccionada);
            } else {
                cargarTodasLasCarreras();
            }
        });
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
    }

    // ==================== MÉTODOS ====================

    private void cargarFacultades() {
        comboFacultad.addItem("Todas las Facultades");
        String sql = "SELECT DISTINCT nombre FROM facultades ORDER BY nombre";
        try (Connection conn = DriverManager.getConnection(URL, USUARIO, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                comboFacultad.addItem(rs.getString("nombre"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando facultades: " + e.getMessage());
        }
    }

    private void cargarTodasLasCarreras() {
        modeloTabla.setRowCount(0);
        String sql = """
            SELECT c.id, f.nombre AS facultad, e.nombre AS escuela, 
                   c.codigo_plan, c.nombre AS carrera
            FROM facultades f
            JOIN escuelas e ON e.facultad_id = f.id
            JOIN carreras c ON c.escuela_id = e.id
            ORDER BY f.nombre, e.nombre, c.nombre
            """;

        try (Connection conn = DriverManager.getConnection(URL, USUARIO, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                modeloTabla.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("facultad"),
                    rs.getString("escuela"),
                    rs.getString("codigo_plan"),
                    rs.getString("carrera")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar carreras: " + e.getMessage());
        }
    }

    private void filtrarPorFacultad(String facultad) {
        modeloTabla.setRowCount(0);
        String sql = """
            SELECT c.id, f.nombre AS facultad, e.nombre AS escuela, 
                   c.codigo_plan, c.nombre AS carrera
            FROM facultades f
            JOIN escuelas e ON e.facultad_id = f.id
            JOIN carreras c ON c.escuela_id = e.id
            WHERE f.nombre = ?
            ORDER BY e.nombre, c.nombre
            """;

        try (Connection conn = DriverManager.getConnection(URL, USUARIO, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, facultad);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                modeloTabla.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("facultad"),
                    rs.getString("escuela"),
                    rs.getString("codigo_plan"),
                    rs.getString("carrera")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al filtrar por facultad: " + e.getMessage());
        }
    }

    private void buscar() {
        String texto = txtBuscar.getText().trim();
        if (texto.isEmpty()) {
            cargarTodasLasCarreras();
            return;
        }

        modeloTabla.setRowCount(0);
        String sql = """
            SELECT c.id, f.nombre AS facultad, e.nombre AS escuela, 
                   c.codigo_plan, c.nombre AS carrera
            FROM facultades f
            JOIN escuelas e ON e.facultad_id = f.id
            JOIN carreras c ON c.escuela_id = e.id
            WHERE f.nombre LIKE ? OR e.nombre LIKE ? OR c.nombre LIKE ? OR c.codigo_plan LIKE ?
            ORDER BY f.nombre, e.nombre
            """;

        try (Connection conn = DriverManager.getConnection(URL, USUARIO, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String like = "%" + texto + "%";
            pstmt.setString(1, like);
            pstmt.setString(2, like);
            pstmt.setString(3, like);
            pstmt.setString(4, like);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                modeloTabla.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("facultad"),
                    rs.getString("escuela"),
                    rs.getString("codigo_plan"),
                    rs.getString("carrera")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en búsqueda: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
