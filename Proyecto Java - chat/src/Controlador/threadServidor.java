package Controlador;

/*
 * threadServidor.java
 *
 * Created on 23 de marzo de 2008, 19:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
import Vista.Servidor;
import Vista.VentanaCliente;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author Santiago Baron Zuleta, Mauricio Sanchez, Sebastian Yepes
 */
public class threadServidor extends Thread {

    Socket scli = null;
    Socket scli2 = null;
    DataInputStream entrada = null;
    DataOutputStream salida = null;
    DataOutputStream salida2 = null;
    public static Vector<threadServidor> clientesActivos = new Vector();
    String nameUser;
    Servidor serv;

    public threadServidor(Socket scliente, Socket scliente2, Servidor serv) {
        scli = scliente;
        scli2 = scliente2;
        this.serv = serv;
        nameUser = "";
        clientesActivos.add(this);
        serv.mostrar("cliente agregado: " + this);
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String name) {
        nameUser = name;
    }

    public void run() {
        serv.mostrar(".::Esperando Mensajes :");

        try {
            entrada = new DataInputStream(scli.getInputStream());
            salida = new DataOutputStream(scli.getOutputStream());
            salida2 = new DataOutputStream(scli2.getOutputStream());
            this.setNameUser(entrada.readUTF());
            enviaUserActivos();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int opcion = 0, numUsers = 0;
        String amigo = "", mencli = "", userBloquear = "";

        while (true) {
            try {

                String name_to_block = serv.usuarioBloquear;

                if (name_to_block != "") {
                    opcion = 4;
                } else {
                    opcion = entrada.readInt();
                }

                switch (opcion) {
                    case 1://envio de mensage a todos
                        mencli = entrada.readUTF();
                        serv.mostrar(getNameUser() + ": " + mencli);
                        enviaMsg(mencli);
                        break;
                    case 2://envio de lista de activos
                        numUsers = clientesActivos.size();
                        salida.writeInt(numUsers);
                        for (int i = 0; i < numUsers; i++) {
                            salida.writeUTF(clientesActivos.get(i).nameUser);
                        }
                        break;
                    case 3: // envia mensage a uno solo
                        amigo = entrada.readUTF();//captura nombre de amigo
                        mencli = entrada.readUTF();//mensage enviado
                        enviaMsg(amigo, mencli);
                        break;
                    case 4: // Bloquea al usuario
                        userBloquear = name_to_block;
                        serv.mostrar(name_to_block);
                        serv.mostrar("Se ha bloqueado al usuario: " + name_to_block);
                        bloquearUsuario(userBloquear);
                        serv.usuarioBloquear = name_to_block = "";
                        opcion = 0;
                        break;
                }
            } catch (IOException e) {
                System.out.println("El cliente termino la conexion");
                break;
            }
        }
        serv.mostrar("Se removio un usuario: ");
        clientesActivos.removeElement(this);

        try {
            serv.mostrar("Se desconecto un usuario");
            scli.close();
        } catch (Exception et) {
            serv.mostrar("no se puede cerrar el socket");
        }
    }

    public void bloquearUsuario(String usuario) throws IOException {
        threadServidor user = null;
        for (int i = 0; i < clientesActivos.size(); i++) {
            try {
                if (clientesActivos.get(i).getNameUser().equals(usuario)) {
                    user = clientesActivos.get(i);
                    user.salida2.writeInt(4);//opcion de mensage 
                    user.salida2.writeUTF(usuario);
                    serv.usuarioBloquear = "";
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void enviaMsg(String mencli2) {
        threadServidor user = null;
        for (int i = 0; i < clientesActivos.size(); i++) {
            serv.mostrar("MENSAJE DEVUELTO:" + mencli2);
            try {
                user = clientesActivos.get(i);
                user.salida2.writeInt(1);//opcion de mensage 
                user.salida2.writeUTF("" + this.getNameUser() + ": " + mencli2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void enviaUserActivos() {
        threadServidor user = null;
        for (int i = 0; i < clientesActivos.size(); i++) {
            try {
                user = clientesActivos.get(i);
                if (user == this) {
                    continue;//ya se lo envie
                }
                user.salida2.writeInt(2);//opcion de agregar 
                user.salida2.writeUTF(this.getNameUser());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void enviaMsg(String amigo, String mencli) {
        threadServidor user = null;
        for (int i = 0; i < clientesActivos.size(); i++) {
            try {
                user = clientesActivos.get(i);
                if (user.nameUser.equals(amigo)) {
                    user.salida2.writeInt(3);//opcion de mensage amigo   
                    user.salida2.writeUTF(this.getNameUser());
                    user.salida2.writeUTF("" + this.getNameUser() + ": " + mencli);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
