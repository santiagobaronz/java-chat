/*
 * Cliente.java
 *
 * Created on 21 de marzo de 2008, 12:11 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package Modelo;

import Controlador.threadCliente;
import Vista.VentanaCliente;
import java.io.*;
import java.net.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Santiago Baron Zuleta, Mauricio Sanchez, Sebastian Yepes
 */
public class Cliente {

    public static String IP_SERVER;
    VentanaCliente vent;
    DataInputStream entrada = null;
    DataOutputStream salida = null;
    DataInputStream entrada2 = null;
    Socket comunication = null;//para la comunicacion
    Socket comunication2 = null;//para recivir msg

    String nomCliente;
    private String user;

    /**
     * Creates a new instance of Cliente
     */
    public Cliente(VentanaCliente vent) throws IOException {
        this.vent = vent;
    }

    public Cliente() {
    }

    public void conexion(String user) throws IOException {
        try {
            comunication = new Socket(Cliente.IP_SERVER, 8081);
            comunication2 = new Socket(Cliente.IP_SERVER, 8082);
            entrada = new DataInputStream(comunication.getInputStream());
            salida = new DataOutputStream(comunication.getOutputStream());
            entrada2 = new DataInputStream(comunication2.getInputStream());
            nomCliente = user;
            vent.setNombreUser(user);
            salida.writeUTF(nomCliente);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "El servidor en esa ip servidor no esta levantado");
        }
        new threadCliente(entrada2, vent).start();
    }

    public String getNombre() {
        return nomCliente;
    }

    public Vector<String> pedirUsuarios() {
        Vector<String> users = new Vector();
        try {
            salida.writeInt(2);
            int numUsers = entrada.readInt();
            for (int i = 0; i < numUsers; i++) {
                users.add(entrada.readUTF());
            }
        } catch (IOException ex) {
            System.err.println("Ha ocurrido un error");
        }
        return users;
    }

    public void flujo(String mens) {
        try {
            System.out.println(getNombre() + ":"
                    + mens);
            salida.writeInt(1);
            salida.writeUTF(mens);
        } catch (IOException e) {
            System.out.println("error...." + e);
        }
    }

    public void flujo(String amigo, String mens) {
        try {
            System.out.println(getNombre() + ":"
                    + mens);
            salida.writeInt(3);//opcion de mensage a amigo
            salida.writeUTF(amigo);
            salida.writeUTF(mens);
        } catch (IOException e) {
            System.out.println("error...." + e);
        }
    }

}
