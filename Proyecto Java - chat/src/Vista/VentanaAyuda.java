/*
 * VentanaAyuda.java
 *
 * Created on 23 de marzo de 2008, 17:32
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package Vista;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.lang.*;
import java.util.*;
import java.net.URL;

/**
 *
 * @author Santiago Baron Zuleta, Mauricio Sanchez, Sebastian Yepes
 */
public class VentanaAyuda extends JFrame {

    /**
     * Creates a new instance of VentanaAyuda
     */
    JScrollPane panelPrincipal;
    JEditorPane html;

    public VentanaAyuda() {
        super("Ventana de Ayuda :");
        setSize(600, 700);
        setLocation(450, 0);
        panelPrincipal = new JScrollPane();

        try {
            URL url = getClass().getResource("index.html");
            html = new JEditorPane(url);
            html.setEditable(false);
            setVisible(true);

        } catch (Exception e) {
            e.getMessage();
        }

        JViewport jv = panelPrincipal.getViewport();
        jv.add(html);

        setContentPane(panelPrincipal);
    }

}
