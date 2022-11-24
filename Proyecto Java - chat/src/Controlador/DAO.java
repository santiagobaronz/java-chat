/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Santiago Baron Zuleta, Mauricio Sanchez, Sebastian Yepes
 */
public class DAO {

    private Statement consulta = null;
    private String db = "segundoparcial_db";
    private String url = "jdbc:mysql://localhost/";
    private String user = "root";
    private String password = "";
    private String driver = "com.mysql.cj.jdbc.Driver";
    private Connection cx;

    public DAO() {

    }

    public Connection conectar() {

        try {
            Class.forName(driver);
            cx = DriverManager.getConnection(url + db, user, password);
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("No se conecto a base de datos" + ex);
        }

        return cx;

    }

    public void desconectar() {
        try {
            cx.close();
        } catch (SQLException ex) {
            System.out.println("No se pudo conectar");
        }
    }

    public boolean validarUsuario(String user) {
        boolean comprobacion = false;

        String sql = "SELECT * FROM users WHERE usuario='" + user + "'";

        try {
            Connection cn = this.conectar();
            PreparedStatement pst = cn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                comprobacion = true;
            } else {
                comprobacion = false;
                JOptionPane.showMessageDialog(null, "El usuario: " + user + " no existe");
            }

            cn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "La verificación no se ha completado");
        }

        return comprobacion;
    }

}
