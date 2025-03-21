/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package proyecto2;

import controladores.ControladorMenu;
import javax.swing.JTree;
import micelaneos.*;
import modelos.ModeloSD;
import vistas.VistaMenu;

/**
 *
 * @author DELL
 */
public class NicolaTomas2 {

    public static void main(String[] args) {
        ModeloSD hola = ModeloSD.readFromJson("files/sd.json");
        JTree jtree = JTreeFromJson.readJTreeFromJson("files/jtree.json");
        //++++++++++++++++++++++++++++
//        Node[] nodes = new Node[40];
//        for (int i = 0; i < 40; i++) {
//            nodes[i] = new Node();
//        }
//        List table = new List();
//        ModeloSD hola = new ModeloSD(30,30,nodes,table);
        //++++++++++++++++++++++++++++++
        
        
        ControladorMenu controaldor1 = new ControladorMenu(hola);
        
        VistaMenu vista = new VistaMenu(jtree);
        vista.setControlador(controaldor1);
        
        vista.actualizarTabla1();
        vista.actualizarTabla2();
       
    }
}
