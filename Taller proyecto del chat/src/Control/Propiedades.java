/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import javax.swing.JOptionPane;


/**
 *
 * @author Santiago Baron Zuleta, Claudia Tatiana Ospina Castiblanco, Santiago
 * Morales Ariza
 */
public class Propiedades {
    
    String[] properties = null;
    
    public String[] getProperties(){
    try ( InputStream lectura = new FileInputStream("props.properties")) {
            Properties propiedades = new Properties();
            propiedades.load(lectura);
            
            String server_port_properties = propiedades.getProperty("SERVER_PORT");
            String client_ip_properties = propiedades.getProperty("CLIENT_IP");
            
            String[] propertiesToReturn = {server_port_properties, client_ip_properties};
            return propertiesToReturn;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se pudo leer el archivo " + e);
        }
        return properties;
    }
    

    
}
