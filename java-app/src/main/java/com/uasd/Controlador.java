package com.uasd;

import java.sql.*;
import javax.swing.JTable;
import javax.swing.table.*;

public class Controlador {
    private String query;
    private int filaFacultad;
    private DefaultTableModel modelo;
    private Connect conexion;
    
    public Controlador(){
        modelo = new DefaultTableModel();
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
        frame.btnMostrarEscuelas.setVisible(false);
        int filaSeleccionada = (frame.tabla.getColumnName(0).equals("Nombre de Facultades"))?frame.tabla.getSelectedRow():filaFacultad;
        filaFacultad = filaSeleccionada;
        modificarTabla(frame.tabla, new String[]{"ID", "Nombre de Escuelas"});
        frame.tabla.removeColumn(frame.tabla.getColumnModel().getColumn(0));
        query = "Select id, nombre from escuelas where facultad_id = ?";
        conexion.login();
        PreparedStatement stmt = conexion.database.prepareStatement(query);
        stmt.setInt(1, filaSeleccionada + 1);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            modelo.addRow(new Object[]{
                rs.getInt("id"),
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
        int idEscuela = (int)modelo.getValueAt(filaSeleccionada, 0);
        modificarTabla(frame.tabla, new String[]{"Codigo", "Carrera"});
        query = "Select codigo_plan, nombre from carreras where escuela_id = ?";
        conexion.login();
        PreparedStatement stmt = conexion.database.prepareStatement(query);
        stmt.setInt(1, idEscuela);
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
