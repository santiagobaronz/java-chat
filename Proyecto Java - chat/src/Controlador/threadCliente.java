package Controlador;

import Vista.VentanaCliente;
import Vista.VentanaCliente;
import java.net.*;
import java.lang.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Santiago Baron Zuleta, Mauricio Sanchez, Sebastian Yepes
 */

public class threadCliente extends Thread {

    DataInputStream entrada;
    VentanaCliente vcli;

    public threadCliente(DataInputStream entrada, VentanaCliente vcli) throws IOException {
        this.entrada = entrada;
        this.vcli = vcli;
    }

    public void run() {
        String menser = "", amigo = "";
        int opcion = 0;
        while (true) {

            try {
                opcion = entrada.readInt();
                switch (opcion) {
                    case 1://mensage enviado
                        menser = entrada.readUTF();
                        System.out.println("ECO del servidor:" + menser);
                        vcli.mostrarMsg(menser);
                        break;
                    case 2://se agrega
                        menser = entrada.readUTF();
                        vcli.agregarUser(menser);
                        break;
                    case 3://mensage de amigo
                        amigo = entrada.readUTF();
                        menser = entrada.readUTF();
                        vcli.mensageAmigo(amigo, menser);
                        System.out.println("ECO del servidor:" + menser);
                        break;
                    case 4:
                        menser = entrada.readUTF();
                        vcli.enviarMensaje("He sido bloqueado de este chat ¡Adioooos!");
                        vcli.bloquear();
                        break;
                }
            } catch (IOException e) {
                System.out.println("Error en la comunicaci�n " + "Informaci�n para el usuario");
                break;
            }
        }
        System.out.println("se desconecto el servidor");
    }

}
