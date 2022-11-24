/*
 * Server.java
 *
 * Created on 21 de marzo de 2008, 12:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package Vista;

/**
 *
 * @author Santiago Baron Zuleta, Mauricio Sanchez, Sebastian Yepes
 */
import Controlador.threadServidor;
import Modelo.Cliente;
import Vista.VentanaCliente;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class Servidor extends JFrame implements ActionListener {

    // DECLARAMOS ELEMENTOS
    JTextArea txaMostrar;
    JButton butBlq;
    JTextField userBlq;

    public Servidor() {
        super("Consola servidor");
        txaMostrar = new JTextArea();
        setLocationRelativeTo(null);
        userBlq = new JTextField(30);
        butBlq = new JButton("Bloquear");
        butBlq.addActionListener(this);
        
        txaMostrar = new JTextArea();
        txaMostrar.setColumns(25);

        txaMostrar.setEditable(false);
        txaMostrar.setForeground(Color.BLUE);
        txaMostrar.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 3, 3, new Color(25, 10, 80)));

        JPanel panAbajo = new JPanel();
        panAbajo.setLayout(new BorderLayout());
        panAbajo.add(new JLabel("  Ingrese usuario a bloquear:  "), BorderLayout.WEST);
        panAbajo.add(userBlq, BorderLayout.CENTER);
        panAbajo.add(butBlq, BorderLayout.EAST);
        
        JPanel panArriba = new JPanel();
        panArriba.setLayout(new BorderLayout());
        panArriba.add(new JScrollPane(txaMostrar), BorderLayout.CENTER);
        
        JPanel panCentral = new JPanel();
        panCentral.setLayout(new BorderLayout());
        panCentral.add(panAbajo, BorderLayout.SOUTH);
        panCentral.add(panArriba,BorderLayout.NORTH);

        setLayout(new BorderLayout());
        add(panAbajo, BorderLayout.SOUTH);
        add(panArriba, BorderLayout.CENTER);

        userBlq.requestFocus();//pedir el focus	
        
        setSize(450, 430);
        setLocation(120, 90);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    public void mostrar(String msg) {
        txaMostrar.append(msg + "\n");
    }
    
    public String usuarioBloquear = "";
    
    public void actionPerformed(ActionEvent e) {
        usuarioBloquear = this.userBlq.getText();
    }

    public void runServer() {
        ServerSocket serv = null;//para comunicacion
        ServerSocket serv2 = null;//para enviar mensajes
        boolean listening = true;
        try {
            serv = new ServerSocket(8081);
            serv2 = new ServerSocket(8082);
            mostrar(".::Servidor activo :");
            while (listening) {
                Socket sock = null, sock2 = null;
                try {
                    mostrar("Esperando Usuarios");
                    sock = serv.accept();
                    sock2 = serv2.accept();
                } catch (IOException e) {
                    mostrar("Accept failed: " + serv + ", " + e.getMessage());
                    continue;
                }
                threadServidor user = new threadServidor(sock, sock2, this);
                user.start();
            }

        } catch (IOException e) {
            mostrar("error :" + e);
        }
    }
    public static void main(String abc[]) throws IOException {
        Servidor ser = new Servidor();
        ser.runServer();
    }


}
