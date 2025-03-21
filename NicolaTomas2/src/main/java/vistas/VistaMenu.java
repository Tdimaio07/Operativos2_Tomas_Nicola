/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vistas;
import modelos.ModeloSD;
import controladores.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import micelaneos.NodoList;

public class VistaMenu extends javax.swing.JFrame {
    ControladorMenu controlador;
    /**
     * Creates new form VistaMenu
     */
    public VistaMenu() {
        initComponents();
        this.setVisible(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
    }

    public ControladorMenu getControlador() {
        return controlador;
    }

    public void setControlador(ControladorMenu controlador) {
        this.controlador = controlador;
    }
    
    public void actualizarTabla1() {

    DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
    for (int i = 0; i < modelo.getRowCount(); i++) {
        modelo.removeRow(i);
    }

    NodoList siguienteNodo = controlador.obtenerTabla().getHead();
    while (siguienteNodo != null) {
        String[] nuevo = new String[3];

        int i = ((String[]) siguienteNodo.getValue())[0].split("/").length - 1;
        nuevo[0] = ((String[]) siguienteNodo.getValue())[0].split("/")[i];

        nuevo[1] = ((String[]) siguienteNodo.getValue())[1];
        nuevo[2] = ((String[]) siguienteNodo.getValue())[2];
        modelo.addRow(nuevo);
        siguienteNodo = siguienteNodo.getpNext();
    }
}

public void actualizarTabla2() {
    String[][] bloques = controlador.obtenerBloques();
    for (int fila = 0; fila < bloques.length; fila++) {
        for (int columna = 0; columna < bloques[fila].length; columna++) {
            jTable2.setValueAt(bloques[fila][columna], fila, columna);
        }
    }
}

public void actualizarArbol(String nombre) {
    DefaultTreeModel modelo = (DefaultTreeModel) jTree1.getModel();
    TreePath rutaSeleccionada = jTree1.getSelectionPath();
    if (rutaSeleccionada == null) {
        JOptionPane.showMessageDialog(this, "Seleccione un nodo para renombrar");
        return;
    }
    DefaultMutableTreeNode nodoSeleccionado = (DefaultMutableTreeNode) rutaSeleccionada.getLastPathComponent();
    DefaultMutableTreeNode nodoPadre = (DefaultMutableTreeNode) nodoSeleccionado.getParent();

    // Verificar si ya existe un nodo con el mismo nombre entre los hermanos
    boolean nodoExiste = false;
    for (int i = 0; i < nodoPadre.getChildCount(); i++) {
        DefaultMutableTreeNode nodoHijo = (DefaultMutableTreeNode) nodoPadre.getChildAt(i);
        if (nombre.equals(nodoHijo.getUserObject().toString())) {
            nodoExiste = true;
            break;
        }
    }

    if (nodoExiste) {
        JOptionPane.showMessageDialog(this, "Ya existe un nodo con el mismo nombre");
        return;
    }

    try {
        if (controlador.esArchivo(this.obtenerRutaComoString(rutaSeleccionada))) {
            nombre = nombre + ".txt";
            String nuevaRuta = obtenerRutaComoString(rutaSeleccionada.getParentPath()) + "/" + nombre;

            System.out.println("Ruta del nuevo nodo antes de renombrar: " + nuevaRuta);
            controlador.actualizarArchivo(this.obtenerRutaComoString(rutaSeleccionada), nuevaRuta);
        }
        nodoSeleccionado.setUserObject(nombre);
        modelo.nodeChanged(nodoSeleccionado);
        modelo.reload(nodoSeleccionado);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
    }
}

private String obtenerRutaComoString(TreePath ruta) {
    Object[] nodos = ruta.getPath();
    StringBuilder rutaString = new StringBuilder();

    for (Object nodo : nodos) {
        rutaString.append(nodo.toString()).append("/");
    }

    // Eliminar la barra al final
    if (rutaString.length() > 0) {
        rutaString.setLength(rutaString.length() - 1);
    }

    return rutaString.toString();
}

private void eliminarNodoYHijos(DefaultMutableTreeNode nodo) {
    // Eliminar de forma recursiva todos los nodos hijos
    while (nodo.getChildCount() > 0) {
        DefaultMutableTreeNode nodoHijo = (DefaultMutableTreeNode) nodo.getFirstChild();
        eliminarNodoYHijos(nodoHijo);
        nodo.remove(nodoHijo);
    }

    // Eliminar el archivo asociado a este nodo
    TreePath ruta = new TreePath(nodo.getPath());
    controlador.borrarArchivo(this.obtenerRutaComoString(ruta));
}

public void crearNodoArbol(String nombre, int num) {
    DefaultTreeModel modelo = (DefaultTreeModel) jTree1.getModel();
    TreePath rutaSeleccionada = jTree1.getSelectionPath();
    DefaultMutableTreeNode nodoSeleccionado;
    if (rutaSeleccionada != null) {
        nodoSeleccionado = (DefaultMutableTreeNode) rutaSeleccionada.getLastPathComponent();
    } else {
        nodoSeleccionado = (DefaultMutableTreeNode) modelo.getRoot();
    }

    boolean nodoExiste = false;
    for (int i = 0; i < nodoSeleccionado.getChildCount(); i++) {
        DefaultMutableTreeNode nodoHijo = (DefaultMutableTreeNode) nodoSeleccionado.getChildAt(i);
        if (nombre.equals(nodoHijo.getUserObject().toString())) {
            nodoExiste = true;
            break;
        }
    }

    if (!nodoExiste) {
        try {
            if (controlador.esArchivo(this.obtenerRutaComoString(new TreePath(nodoSeleccionado.getPath())))) {
                JOptionPane.showMessageDialog(this, "Es un archivo");
                return;
            }
            if (num > 0) {
                nombre = nombre + ".txt";
                TreePath rutaPadre = new TreePath(nodoSeleccionado.getPath());
                String nuevaRuta = obtenerRutaComoString(rutaPadre) + "/" + nombre;

                System.out.println("Ruta del nuevo nodo antes de agregar: " + nuevaRuta);
                controlador.crearArchivo(num, nuevaRuta);
            }
            DefaultMutableTreeNode nuevoNodo = new DefaultMutableTreeNode(nombre);
            nodoSeleccionado.add(nuevoNodo);
            modelo.reload(nodoSeleccionado);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            e.printStackTrace();
        }
    } else {
        JOptionPane.showMessageDialog(this, "No pueden existir 2 archivos iguales");
    }
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        crear = new javax.swing.JButton();
        editar = new javax.swing.JButton();
        borrar = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("C:");
        jTree1.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane1.setViewportView(jTree1);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 180, 350));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 230, 400, 130));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Bloques", "Inicio"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable1);

        jPanel1.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 80, 400, 130));

        jToolBar1.setRollover(true);

        crear.setText("Crear");
        crear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                crearActionPerformed(evt);
            }
        });
        jToolBar1.add(crear);

        editar.setText("Editar");
        editar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editarActionPerformed(evt);
            }
        });
        jToolBar1.add(editar);

        borrar.setText("Borrar");
        borrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarActionPerformed(evt);
            }
        });
        jToolBar1.add(borrar);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Administrador", "Usuario" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jComboBox1);

        jPanel1.add(jToolBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, 260, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 683, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void crearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_crearActionPerformed
        // TODO add your handling code here:
        ControladorCrear controlador2 = new ControladorCrear (new VistaCrear(this));
    }//GEN-LAST:event_crearActionPerformed

    private void editarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarActionPerformed
        // TODO add your handling code here:
        String nombre = JOptionPane.showInputDialog("nuevo nombre");
          if(!nombre.isBlank()){
              this.actualizarArbol(nombre);
              this.actualizarTabla1();
              this.actualizarTabla2();
          }else{
              JOptionPane.showMessageDialog(rootPane, "no puede estar vacio");
          }
    }//GEN-LAST:event_editarActionPerformed

    private void borrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarActionPerformed
        // TODO add your handling code here:
        TreePath rutaSeleccionada = jTree1.getSelectionPath();
        if (rutaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un nodo para eliminar");
            return;
        }

        // Obtener el nodo seleccionado
        DefaultTreeModel modelo = (DefaultTreeModel) jTree1.getModel();
        DefaultMutableTreeNode nodoSeleccionado = (DefaultMutableTreeNode) rutaSeleccionada.getLastPathComponent();

        // Eliminar recursivamente todos los nodos hijos y sus archivos
        eliminarNodoYHijos(nodoSeleccionado);

        // Remover el nodo del JTree
        modelo.removeNodeFromParent(nodoSeleccionado);

        // Actualizar las tablas
        this.actualizarTabla1();
        this.actualizarTabla2();

    }//GEN-LAST:event_borrarActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
        if(jComboBox1.getSelectedIndex() == 0){
            borrar.setEnabled(true);
            crear.setEnabled(true);
            editar.setEnabled(true);
        }else{
            borrar.setEnabled(false);
            crear.setEnabled(false);
            editar.setEnabled(false);
            
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VistaMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VistaMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VistaMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VistaMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VistaMenu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton borrar;
    private javax.swing.JButton crear;
    private javax.swing.JButton editar;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables
}
