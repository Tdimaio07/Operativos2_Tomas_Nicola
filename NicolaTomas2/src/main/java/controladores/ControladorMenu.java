package controladores;
import javax.swing.JTree;
import micelaneos.JTreeToJson;
import micelaneos.List;
import modelos.ModeloSD;
public class ControladorMenu {
    ModeloSD sistemaDeArchivos;

    public ControladorMenu(ModeloSD sistemaDeArchivos) {
        this.sistemaDeArchivos = sistemaDeArchivos;
    }

    public void actualizarArchivo(String nombreViejo, String nombreNuevo) throws Exception {
        sistemaDeArchivos.actualizarArchivo(nombreViejo, nombreNuevo);
    }

    public void borrarArchivo(String nombreArchivo) {
        sistemaDeArchivos.borrarArchivo(nombreArchivo);
    }

    public void crearArchivo(int numero, String nombre) throws Exception {
        sistemaDeArchivos.crearArchivo(numero, nombre);
    }

    public boolean esArchivo(String nombre) {
        return sistemaDeArchivos.esArchivo(nombre);
    }

    public String[][] obtenerBloques() {
        String[][] salida = new String[10][3];
        int contador = 1;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 3; j++) {
                try {
                    salida[i][j] = Integer.toString(contador) + ": " 
                        + sistemaDeArchivos.obtenerBloques()[contador - 1].getFile().split("/")[sistemaDeArchivos.obtenerBloques()[contador - 1].getFile().split("/").length - 1];
                } catch (Exception e) {
                    salida[i][j] = Integer.toString(contador) + ": Libre";
                }
                contador++;
            }
        }
        return salida;
    }
    public List obtenerTabla(){
        return sistemaDeArchivos.getTabla();
    }
    
    public void guardarSD(){
        sistemaDeArchivos.saveAsJson("files/sd.json");
    }
    
    public void guardarJTree(JTree jtree){
        JTreeToJson.saveJTreeToJson("files/jtree.json", jtree);
    }
}

