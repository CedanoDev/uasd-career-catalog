package com.uasd;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void mostrarVentana(JFrame ventana) {
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(400, 300);
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }
    public static void main(String[] args) {
        System.out.println("Hola desde Main");
        //mostrarVentana(new frameCatalogoUASD());
        SwingUtilities.invokeLater(() -> {
    new frameCatalogoUASD().setVisible(true);
});

    }
}