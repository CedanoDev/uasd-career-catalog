package com.uasd.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
     // Configuración de conexión para Docker
     private static final String URL = "jdbc:mysql://db:3306/uasd_catalog";
     private static final String USUARIO = "uasd_user";
     private static final String PASSWORD = "uasd_password";
     private static Connection conexion = null;

      
    //Constructor privado para evitar instanciación directa (patrón Singleton)
     
    private ConexionDB() {
    }

     /**
     * Obtiene una conexión activa a la base de datos.
     * Si no existe una conexión, la crea. Si existe pero está cerrada, crea una nueva.
     * 
     * @return Connection objeto de conexión a la base de datos
     * @throws SQLException si ocurre un error al conectar
     */
     public static Connection getConexion() throws SQLException {
        try {
            // Verificar si la conexión es nula o está cerrada
            if (conexion == null || conexion.isClosed()) {
                // Cargar el driver de MySQL (opcional en JDBC 4.0+, pero recomendado)
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                // Establecer la conexión
                conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
                System.out.println("Conexión a la base de datos establecida exitosamente.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver de MySQL no encontrado.");
            throw new SQLException("Driver de MySQL no encontrado", e);
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            throw e;
        }
        
        return conexion;
    }
     
     //Cierra la conexión a la base de datos si está abierta.
     public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión a la base de datos cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
     /**
     * Prueba la conexión a la base de datos.
     * Útil para verificar que la configuración es correcta.
     * 
     * @return true si la conexión es exitosa, false en caso contrario
     */
     public static boolean probarConexion() {
        try {
            Connection conn = getConexion();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Fallo en la prueba de conexión: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Método main para pruebas unitarias de la conexión
     */
    public static void main(String[] args) {
        System.out.println("=== PRUEBA DE CONEXIÓN A LA BASE DE DATOS ===\n");
        
        try {
            // Probar la conexión
            Connection conn = ConexionDB.getConexion();
            
            if (conn != null && !conn.isClosed()) {
                System.out.println("Conexión exitosa a: " + URL);
                System.out.println("Base de datos: uasd_catalog");
                System.out.println("Usuario: " + USUARIO);
                
                // Obtener información del servidor
                System.out.println("\n--- Información del Servidor ---");
                System.out.println("Producto: " + conn.getMetaData().getDatabaseProductName());
                System.out.println("Versión: " + conn.getMetaData().getDatabaseProductVersion());
            }
            
        } catch (SQLException e) {
            System.err.println("\nError de conexión:");
            System.err.println("   Mensaje: " + e.getMessage());
            System.err.println("   Código SQL: " + e.getSQLState());
        } finally {
            // Cerrar la conexión
            ConexionDB.cerrarConexion();
        }
        
        System.out.println("\n=== FIN DE LA PRUEBA ===");
    }
}
        
