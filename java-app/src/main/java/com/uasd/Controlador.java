package com.uasd;

import java.sql.*;
import javax.swing.JTable;
import javax.swing.table.*;

public class Controlador {
    private String query;
    private Connect conexion;
    private int filaFacultad;
    private DefaultTableModel modelo = new DefaultTableModel();
    
    public Controlador(){
        conexion = new Connect();
    }
    
    private void modificarTabla(JTable tabla, String[] columnas){
        modelo.setColumnCount(0);
        modelo.setRowCount(0);
        for(String columna : columnas){
            modelo.addColumn(columna);
        }
        
        tabla.setModel(modelo);
        TableRowSorter<TableModel> ordenarTabla = new TableRowSorter<>(modelo);
        tabla.setRowSorter(ordenarTabla);
    }
    
    public void mostrarFacultades(frameCatalogoUASD frame) throws SQLException, ClassNotFoundException{
        frame.btnMostrarFacultades.setVisible(false);
        frame.btnMostrarEscuelas.setVisible(false);
        modificarTabla(frame.tabla, new String[]{"Nombre de Facultades"});
        query = "Select nombre from facultades";
        conexion.login();
        PreparedStatement stmt = conexion.database.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            modelo.addRow(new Object[]{
                rs.getString("nombre")
            });
        }
        stmt.close();
        conexion.logoff();
    }
    
    public void mostrarEscuelas(frameCatalogoUASD frame) throws SQLException, ClassNotFoundException{
        frame.btnMostrarFacultades.setVisible(true);
        frame.btnMostrarEscuelas.setVisible(true);
        int filaSeleccionada = (frame.tabla.getColumnName(0).equals("Nombre de Facultades"))?frame.tabla.getSelectedRow():filaFacultad;
        filaFacultad = filaSeleccionada;
        modificarTabla(frame.tabla, new String[]{"Nombre de Escuelas"});
        query = "Select nombre from escuela where facultad_id = ?";
        conexion.login();
        PreparedStatement stmt = conexion.database.prepareStatement(query);
        stmt.setInt(1, filaSeleccionada);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            modelo.addRow(new Object[]{
                rs.getString("nombre")
            });
        }
        stmt.close();
        conexion.logoff();
    }
    
    public void mostrarCarreras(frameCatalogoUASD frame) throws SQLException, ClassNotFoundException{
        frame.btnMostrarFacultades.setVisible(true);
        frame.btnMostrarEscuelas.setVisible(true);
        int filaSeleccionada = frame.tabla.getSelectedRow();
        modificarTabla(frame.tabla, new String[]{"Codigo", "Carrera"});
        query = "Select codigo_plan, nombre from carrera where escuela_id = ?";
        conexion.login();
        PreparedStatement stmt = conexion.database.prepareStatement(query);
        stmt.setInt(1, filaSeleccionada);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            modelo.addRow(new Object[]{
                rs.getString("codigo_plan"),
                rs.getString("nombre")
            });
        }
        stmt.close();
        conexion.logoff();
    }
}
