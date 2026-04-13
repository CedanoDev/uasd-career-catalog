package com.uasd;

import java.sql.*;

public class Connect {
        private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://db:3306/uasd_catalog";
    private static final String USER = "uasd_user";
    private static final String PASS = "uasd_password";

    private Connection database = null;

    // Conectar
    public void login() throws ClassNotFoundException {
        try {
            Class.forName(JDBC_DRIVER);
            database = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("✅ Conexión establecida.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Cerrar conexión
    public void logoff() throws SQLException {
        if (database != null && !database.isClosed()) {
            database.close();
            System.out.println("🔒 Conexión cerrada.");
        }
    }
}
