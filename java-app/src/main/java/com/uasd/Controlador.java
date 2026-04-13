package com.uasd;

import java.sql.*;

public class Controlador {
    private String query;
    private Connect conexion;
    
    public Controlador(){
        conexion = new Connect();
    }
    
    public void mostrarFacultades(frameCatalogoUASD frame){}
    
    public void mostrarEscuelas(frameCatalogoUASD frame){}
    
    public void mostrarCarreras(frameCatalogoUASD frame){}
}
