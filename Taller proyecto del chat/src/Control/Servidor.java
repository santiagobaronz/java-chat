package Control;

// Configurar un servidor que reciba una conexión de un cliente, envíe
// una cadena al cliente y cierre la conexión.
import Vista.ChatVentana;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Properties;
import javax.swing.*;

public class Servidor extends JFrame {

    private JTextField campoIntroducir;
    private JTextArea areaPantalla;
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;
    private ServerSocket servidor;
    private Socket conexion;
    private int contador = 1;


    // configurar GUI
    public Servidor() {


    } // fin del constructor de Servidor

    // configurar y ejecutar el servidor 
    public void ejecutarServidor() {
        // configurar servidor para que reciba conexiones; procesar las conexiones
        
        Propiedades props = new Propiedades();
        String[] propiedades = props.getProperties();
        int server_port = Integer.parseInt(propiedades[0]);
        
        
        try {

            // Paso 1: crear un objeto ServerSocket.
            servidor = new ServerSocket(server_port, 100);
            ChatVentana vista1 = new ChatVentana("Chat del servidor");
            vista1.setVisible(true);

            areaPantalla = vista1.campoPantalla;
            campoIntroducir = vista1.campoTexto;
            campoIntroducir.addActionListener(
                    new ActionListener() {

                // enviar mensaje al cliente
                public void actionPerformed(ActionEvent evento) {
                    enviarDatos(evento.getActionCommand());
                    campoIntroducir.setText("");
                }
            }
            );

            while (true) {

                try {
                    esperarConexion(); // Paso 2: esperar una conexión.
                    obtenerFlujos();        // Paso 3: obtener flujos de entrada y salida.
                    procesarConexion(); // Paso 4: procesar la conexión.
                } // procesar excepción EOFException cuando el cliente cierre la conexión 
                catch (EOFException excepcionEOF) {
                    System.err.println("El servidor terminó la conexión");
                } finally {
                    cerrarConexion();   // Paso 5: cerrar la conexión.
                    ++contador;
                }

            } // fin de instrucción while

        } // fin del bloque try
        // procesar problemas con E/S
        catch (IOException excepcionES) {
            excepcionES.printStackTrace();
        }

    } // fin del método ejecutarServidor

    // esperar que la conexión llegue, después mostrar información de la conexión
    private void esperarConexion() throws IOException {
        mostrarMensaje("Esperando una conexión\n");
        conexion = servidor.accept(); // permitir al servidor aceptar la conexión            
        mostrarMensaje("Conexión " + contador + " recibida de: "
                + conexion.getInetAddress().getHostName());
    }

    // obtener flujos para enviar y recibir datos
    private void obtenerFlujos() throws IOException {
        // establecer flujo de salida para los objetos
        salida = new ObjectOutputStream(conexion.getOutputStream());
        salida.flush(); // vaciar búfer de salida para enviar información de encabezado

        // establecer flujo de entrada para los objetos
        entrada = new ObjectInputStream(conexion.getInputStream());

        mostrarMensaje("\nSe recibieron los flujos de E/S\n");
    }

    // procesar la conexión con el cliente
    private void procesarConexion() throws IOException {
        // enviar mensaje de conexión exitosa al cliente
        String mensaje = "Conexión exitosa";
        enviarDatos(mensaje);

        // habilitar campoIntroducir para que el usuario del servidor pueda enviar mensajes
        establecerCampoTextoEditable(true);

        do { // procesar los mensajes enviados por el cliente

            // leer el mensaje y mostrarlo en pantalla
            try {
                mensaje = (String) entrada.readObject();
                mostrarMensaje("\n" + mensaje);
            } // atrapar problemas que pueden ocurrir al tratar de leer del cliente
            catch (ClassNotFoundException excepcionClaseNoEncontrada) {
                mostrarMensaje("\nSe recibió un tipo de objeto desconocido");
            }

        } while (!mensaje.equals("CLIENTE>>> TERMINAR"));

    } // fin del método procesarConexion

    // cerrar flujos y socket
    private void cerrarConexion() {
        mostrarMensaje("\nFinalizando la conexión\n");
        establecerCampoTextoEditable(false); // deshabilitar campoIntroducir

        try {
            salida.close();
            entrada.close();
            conexion.close();
        } catch (IOException excepcionES) {
            excepcionES.printStackTrace();
        }
    }

    // enviar mensaje al cliente
    private void enviarDatos(String mensaje) {
        // enviar objeto al cliente
        try {
            salida.writeObject("SERVIDOR>>> " + mensaje);
            salida.flush();
            mostrarMensaje("\nSERVIDOR>>> " + mensaje);
        } // procesar problemas que pueden ocurrir al enviar el objeto
        catch (IOException excepcionES) {
            areaPantalla.append("\nError al escribir objeto");
        }
    }

    // método utilitario que es llamado desde otros subprocesos para manipular a
    // areaPantalla en el subproceso despachador de eventos
    private void mostrarMensaje(final String mensajeAMostrar) {
        // mostrar mensaje del subproceso de ejecución despachador de eventos
        SwingUtilities.invokeLater(
                new Runnable() {  // clase interna para asegurar que la GUI se actualice apropiadamente

            public void run() // actualiza areaPantalla
            {
                areaPantalla.append(mensajeAMostrar);
                areaPantalla.setCaretPosition(
                        areaPantalla.getText().length());
            }

        } // fin de la clase interna

        ); // fin de la llamada a SwingUtilities.invokeLater
    }

    // método utilitario que es llamado desde otros subprocesos para manipular a 
    // campoIntroducir en el subproceso despachador de eventos
    private void establecerCampoTextoEditable(final boolean editable) {
        // mostrar mensaje del subproceso de ejecución despachador de eventos
        SwingUtilities.invokeLater(
                new Runnable() {  // clase interna para asegurar que la GUI se actualice apropiadamente

            public void run() // establece la capacidad de modificar a campoIntroducir
            {
                campoIntroducir.setEditable(editable);
            }

        } // fin de la clase interna

        ); // fin de la llamada a SwingUtilities.invokeLater
    }

    public static void main(String args[]) {
        Servidor aplicacion = new Servidor();
        aplicacion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        aplicacion.ejecutarServidor();
    }

}  // fin de la clase Servidor
