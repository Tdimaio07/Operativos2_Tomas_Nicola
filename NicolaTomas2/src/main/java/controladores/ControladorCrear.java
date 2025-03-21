/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controladores;
import vistas.*;
/**
 *
 * @author DELL
 */
public class ControladorCrear {
    private VistaCrear vista;

    public ControladorCrear(VistaCrear vista) {
        this.vista = vista;
    }
    
    public int obtenerBloques(){
        return Integer.parseInt(vista.getParametros()[1]);
    }
    public String obtenerNombre(){
        return vista.getParametros()[0];
    }
    public boolean obtenerValidación(){
        return vista.inputValido();
    }
    public boolean obtenerEstado(){
        return vista.isVisible();
    }
}
