package com.uasd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;

public class Main {
    public static void mostrarVentana(JFrame ventana) {
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //ventana.setSize(400, 300);
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }
    public static void main(String[] args) {

        System.out.println("--- INICIANDO PRUEBA DE CATÁLOGO UASD ---");

        try (Connection conn = ConexionDB.getConexion()) {
            System.out.println("✅ Conexión establecida.");

            // Consulta para ver las carreras de Informática (ID 11 según tu captura)
            String query = "SELECT codigo_plan, nombre FROM carreras WHERE escuela_id = 11";

            try (Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query)) {

                System.out.println("\nCarreras encontradas en la Escuela de Informática:");
                System.out.println("-------------------------------------------------");

                while (rs.next()) {
                    System.out.println(
                            "Código: " + rs.getString("codigo_plan") + " | Carrera: " + rs.getString("nombre"));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error en la prueba: " + e.getMessage());
        } finally {
            ConexionDB.cerrarConexion();
        }
        System.out.println("Hola desde Main");
        mostrarVentana(new frameCatalogoUASD());
    }
}