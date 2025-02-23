/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package boletos.presentacion;

import boletos.control.ControlComprarBoletos;
import boletos.control.ControlAgregarSaldo;
import boletos.control.ControlVenderBoletos;
import boletos.dtos.UsuarioDTO;
import boletos.persistencia.BoletosDAO;
import boletos.persistencia.ConexionBD;
import boletos.persistencia.UsuariosDAO;



public class MenuPrincipal extends javax.swing.JFrame {
    UsuarioDTO usuarioActual;
    /**
     * Creates new form MenuPrincipal
     */
    public MenuPrincipal(UsuarioDTO usuarioActual) {
        this.usuarioActual = usuarioActual;
        setLocationRelativeTo(null);
        initComponents();
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
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnAgregarSaldo = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        btnComprar = new javax.swing.JButton();
        btnMisBoletos = new javax.swing.JButton();
        btnActualizarDatos = new javax.swing.JButton();
        btnHistorial = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("MENU PRINCIPAL");

        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        btnAgregarSaldo.setBackground(new java.awt.Color(204, 204, 204));
        btnAgregarSaldo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnAgregarSaldo.setText("AGREGAR SALDO");
        btnAgregarSaldo.setToolTipText("");
        btnAgregarSaldo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnAgregarSaldo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgregarSaldo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarSaldoActionPerformed(evt);
            }
        });

        btnSalir.setText("SALIR");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        btnComprar.setBackground(new java.awt.Color(204, 204, 204));
        btnComprar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnComprar.setText("COMPRAR");
        btnComprar.setToolTipText("");
        btnComprar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnComprar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnComprar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnComprarActionPerformed(evt);
            }
        });

        btnMisBoletos.setBackground(new java.awt.Color(204, 204, 204));
        btnMisBoletos.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnMisBoletos.setText("MIS BOLETOS");
        btnMisBoletos.setToolTipText("");
        btnMisBoletos.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnMisBoletos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMisBoletos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMisBoletosActionPerformed(evt);
            }
        });

        btnActualizarDatos.setBackground(new java.awt.Color(204, 204, 204));
        btnActualizarDatos.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnActualizarDatos.setText("ACTUALIZAR DATOS");
        btnActualizarDatos.setToolTipText("");
        btnActualizarDatos.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnActualizarDatos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActualizarDatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarDatosActionPerformed(evt);
            }
        });

        btnHistorial.setBackground(new java.awt.Color(204, 204, 204));
        btnHistorial.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnHistorial.setText("HISTORIAL");
        btnHistorial.setToolTipText("");
        btnHistorial.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnHistorial.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHistorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistorialActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
            .addComponent(btnComprar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnMisBoletos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnAgregarSaldo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnActualizarDatos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnHistorial, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnSalir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnComprar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnMisBoletos, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(btnAgregarSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnActualizarDatos, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnHistorial, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                .addComponent(btnSalir)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnComprarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnComprarActionPerformed
        ConexionBD manejadorConexiones = new ConexionBD();
        BoletosDAO boletosDAO = new BoletosDAO(manejadorConexiones);
        ControlComprarBoletos control = new ControlComprarBoletos(boletosDAO, this, usuarioActual);
        control.iniciarCasoUso();
        this.dispose();
        
    }//GEN-LAST:event_btnComprarActionPerformed

    private void btnMisBoletosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMisBoletosActionPerformed
        ConexionBD manejadorConexiones = new ConexionBD();
        BoletosDAO boletosDAO = new BoletosDAO(manejadorConexiones);
        ControlVenderBoletos control = new ControlVenderBoletos(this, boletosDAO,usuarioActual);
        control.iniciarCasoUso();
        this.dispose();
    }//GEN-LAST:event_btnMisBoletosActionPerformed

    private void btnActualizarDatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarDatosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnActualizarDatosActionPerformed

    private void btnHistorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistorialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnHistorialActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnAgregarSaldoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarSaldoActionPerformed
        ConexionBD manejadorConexiones = new ConexionBD();
        UsuariosDAO usuariosDAO = new UsuariosDAO(manejadorConexiones);
        ControlAgregarSaldo control = new ControlAgregarSaldo(usuariosDAO, this, usuarioActual);
        control.iniciarCasoUso();
        this.dispose();
        
    }//GEN-LAST:event_btnAgregarSaldoActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizarDatos;
    private javax.swing.JButton btnAgregarSaldo;
    private javax.swing.JButton btnComprar;
    private javax.swing.JButton btnHistorial;
    private javax.swing.JButton btnMisBoletos;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
