/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package views;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.jsonwebtoken.Claims;
import javax.swing.JOptionPane;
import sistema.Cliente;
import sistema.createJWT;

/**
 *
 * @author henri
 */
public class editarAdmin extends javax.swing.JFrame {

    public Cliente cliente;
    public String authToken;
    public String ip;
    public int porta;
    public String id = "";
    public editarAdmin(String token,String ip,int porta) {
        initComponents();
        this.authToken = token;
        this.cliente = Cliente.getInstance(ip, porta);
        getDados();
    }

    public editarAdmin(String token,String ip,int porta,String id) {
        initComponents();
        this.authToken = token;

        this.cliente = Cliente.getInstance(ip, porta);
        this.ip = ip;
        this.id = id;
        this.porta = porta;
        getDados();
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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        btnEnviar = new javax.swing.JButton();
        btnVoltar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        adminCheck = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Email");

        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });

        btnEnviar.setText("Enviar");
        btnEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarActionPerformed(evt);
            }
        });

        btnVoltar.setText("voltar");
        btnVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoltarActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 153, 255));
        jLabel3.setText("Editar");

        jLabel4.setText("Nome");

        txtNome.setToolTipText("");
        txtNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeActionPerformed(evt);
            }
        });

        adminCheck.setText("Admin");
        adminCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminCheckActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnEnviar, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
                        .addComponent(btnVoltar, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(adminCheck, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
                            .addComponent(txtNome))))
                .addContainerGap(57, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(adminCheck)
                .addGap(84, 84, 84)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnVoltar)
                    .addComponent(btnEnviar))
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarActionPerformed
        ObjectNode retorno = Editar();
        boolean isError = retorno.get("error").asBoolean();
        if(isError){
            mostrarAviso(retorno.get("message").asText());
        }else{
            indexAdmin admin = new indexAdmin(authToken,ip,porta);
            this.dispose();
            admin.setVisible(true);
            admin.mostrarAviso(retorno.get("message").asText());
        }
    }//GEN-LAST:event_btnEnviarActionPerformed

    private void btnVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoltarActionPerformed
        indexAdmin admin = new indexAdmin(authToken,ip,porta);
        this.dispose();
        admin.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_btnVoltarActionPerformed

    private void txtNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeActionPerformed

    private void adminCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminCheckActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_adminCheckActionPerformed

//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(editarAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(editarAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(editarAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(editarAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new editarAdmin().setVisible(true);
//            }
//        });
//    }
    
public ObjectNode Editar() {
        ObjectNode respostaDoServidor;
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonData = objectMapper.createObjectNode();
        if(id.equals("")){
            String name = txtNome.getText();
            String email = txtEmail.getText();
            String password = null;
            boolean type= adminCheck.isSelected();
            jsonData.put("name", name);
            jsonData.put("email",email);
            jsonData.put("password",password);
            Claims claims = createJWT.verifyJwtToken(authToken);
            System.out.println(claims);
            jsonData.put("user_id", claims.get("user_id", String.class));
            jsonData.put("type",type);
            jsonData.put("token",authToken);

            ObjectNode jsonToSend = objectMapper.createObjectNode();
            jsonToSend.put("action", "edicao-usuario");
            jsonToSend.set("data", jsonData);


            // Envie os dados para o servidor (substitua 'DadosParaServidor' pelos dados reais)
            respostaDoServidor = cliente.enviarSolicitacao(jsonToSend);
        }else{
            String name = txtNome.getText();
            String email = txtEmail.getText();
            String password = null;
            boolean type= adminCheck.isSelected();
            jsonData.put("name", name);
            jsonData.put("email",email);
            jsonData.put("password",password);
            jsonData.put("user_id",id);
            jsonData.put("type",type);
            jsonData.put("token",authToken);

            ObjectNode jsonToSend = objectMapper.createObjectNode();
            jsonToSend.put("action", "edicao-usuario");
            jsonToSend.set("data", jsonData);


            // Envie os dados para o servidor (substitua 'DadosParaServidor' pelos dados reais)
            respostaDoServidor = cliente.enviarSolicitacao(jsonToSend);
        }
        return respostaDoServidor;
    }    
    
public void getDados(){
    if(id.equals("")){
        ObjectNode respostaDoServidor;
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonData = objectMapper.createObjectNode();
        jsonData.put("token", authToken);
        Claims claims = createJWT.verifyJwtToken(authToken);
        jsonData.put("user_id", claims.get("user_id", String.class));
        ObjectNode jsonToSend = objectMapper.createObjectNode();
        jsonToSend.put("action", "pedido-edicao-usuario");
        jsonToSend.set("data", jsonData);

        respostaDoServidor = cliente.enviarSolicitacao(jsonToSend);
        System.out.println(respostaDoServidor);

        boolean isError = respostaDoServidor.get("error").asBoolean();
        if(isError){
            mostrarAviso(respostaDoServidor.get("message").asText());
        }else{
                JsonNode dataNode = respostaDoServidor.get("data").path("user");
                
                txtNome.setText(dataNode.get("name").asText());
                txtEmail.setText(dataNode.get("email").asText());
                adminCheck.setSelected(dataNode.get("type").asBoolean());
        }
    }else{
        ObjectNode respostaDoServidor;
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonData = objectMapper.createObjectNode();
        jsonData.put("token", authToken);
        jsonData.put("user_id", id);
        ObjectNode jsonToSend = objectMapper.createObjectNode();
        jsonToSend.put("action", "pedido-edicao-usuario");
        jsonToSend.set("data", jsonData);

        respostaDoServidor = cliente.enviarSolicitacao(jsonToSend);
        System.out.println(respostaDoServidor);

        boolean isError = respostaDoServidor.get("error").asBoolean();
        if(isError){
            mostrarAviso(respostaDoServidor.get("message").asText());
        }else{
                JsonNode dataNode = respostaDoServidor.get("data").path("user");
                
                txtNome.setText(dataNode.get("name").asText());
                txtEmail.setText(dataNode.get("email").asText());
                adminCheck.setSelected(dataNode.get("type").asBoolean());
        }
    }
}
public void mostrarAviso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox adminCheck;
    private javax.swing.JButton btnEnviar;
    private javax.swing.JButton btnVoltar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtNome;
    // End of variables declaration//GEN-END:variables
}
