package modelos;
import micelaneos.*;

public class ModeloSD {
    int capacidad;
    int bloquesLibres;
    Node[] bloques;
    List tabla;
      
    public ModeloSD(int bloquesLibres, Node[] nodos, List lista) {
        this.bloquesLibres = bloquesLibres;
        this.capacidad = bloquesLibres;
        bloques = nodos;
        tabla = lista;
    }

    public Node[] obtenerBloques() {
        return bloques;
    }
    
    public void borrarArchivo(String nombreArchivo) {
        for (int i = 0; i < capacidad; i++) {
            Node nodo = bloques[i];
            if(nodo.getFile() != null) {
                if(nodo.getFile().equals(nombreArchivo)) {
                    nodo.setFile(null);
                    nodo.setNext(-1);
                    bloquesLibres++;
                }
            }
        }
        NodoList siguienteNodo = tabla.getHead();
        while(siguienteNodo != null) {
            if(((String[])siguienteNodo.getValue())[0].equals(nombreArchivo)) {
                tabla.delete(siguienteNodo);
                break;
            }
            siguienteNodo = siguienteNodo.getpNext();
        }
    }

    public void crearArchivo(int numeroBloques, String nombreArchivo) throws Exception {
        if(bloquesLibres < numeroBloques) {
            throw new Exception(ExceptionMessage.notSpace);
        } else if(this.nombreExiste(nombreArchivo)) {
            throw new Exception(ExceptionMessage.sameName);
        } else {
            asignarBloques(numeroBloques, nombreArchivo);
        }
    }

    public void actualizarArchivo(String nombreArchivo, String nuevoNombreArchivo) throws Exception {
        if(this.nombreExiste(nuevoNombreArchivo)) {
            throw new Exception(ExceptionMessage.sameName);
        }
        NodoList siguienteNodo = tabla.getHead();
        while(siguienteNodo != null) {
            if(((String[])siguienteNodo.getValue())[0].equals(nombreArchivo)) {
                ((String[]) siguienteNodo.getValue())[0] = nuevoNombreArchivo;
                break;
            }
        }
        int i = inicioArchivo(nombreArchivo); 
        while(i != -1) {
            bloques[i].setFile(nuevoNombreArchivo);
            i = bloques[i].getNext();
        }
    }

    public boolean esArchivo(String nombreArchivo) {
        return obtenerBloques(nombreArchivo) > 0;
    }
    
    public int inicioArchivo(String nombreArchivo) {
        for (int i = 0; i < capacidad; i++) {
            if(bloques[i].getFile() != null) {
                if(bloques[i].getFile().equals(nombreArchivo)) return i;
            }
        }
        return -1;
    }
    
    private int obtenerBloques(String nombreArchivo) {
        int cantidad = 0;
        for (int i = 0; i < capacidad; i++) {
            String siguiente = bloques[i].getFile();
            if(siguiente != null && siguiente.equals(nombreArchivo)) {
                cantidad++;
            }
        }
        return cantidad;
    }
    
    private boolean nombreExiste(String nombreArchivo) {
        for (int i = 0; i < capacidad; i++) {
            String siguiente = bloques[i].getFile();
            if(siguiente != null && siguiente.equals(nombreArchivo)) {
                return true;
            }
        }
        return false;
    }
    
    private int siguienteBloqueLibre() {
        for (int i = 0; i < capacidad; i++) {
            String siguiente = bloques[i].getFile();
            if(siguiente == null) return i;
        }
        return -1;
    }

    private void asignarBloques(int numeroBloques, String nombreArchivo) {
        for (int i = 0; i < numeroBloques; i++) {
            int j = this.siguienteBloqueLibre();
            if(i == 0) {
                String[] array = {nombreArchivo, Integer.toString(j), Integer.toString(numeroBloques)};
                tabla.appendLast(array);
            }
            Node siguiente = bloques[j];
            siguiente.setFile(nombreArchivo);
            if(i != numeroBloques - 1) {
                int siguienteLibre = this.siguienteBloqueLibre();
                siguiente.setNext(siguienteLibre);
            }
            bloquesLibres--;
        }
    }

    public List getTabla() {
        return tabla;
    }
    
}
