/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.NorthernSec.NorthernMail.GUI;

import com.NorthernSec.NorthernMail.Exceptions.InvalidMailException;
import com.NorthernSec.NorthernMail.Exceptions.InvalidSignatureException;
import com.NorthernSec.NorthernMail.Objects.Mail.DecryptedMail;
import com.NorthernSec.NorthernMail.Objects.Mail.Mail;
import com.NorthernSec.NorthernMail.client.ConfigReader;
import com.NorthernSec.NorthernMail.client.KeyManager;
import com.NorthernSec.NorthernMail.client.MailClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Snorelax
 */
public class MainPanel extends javax.swing.JFrame {

    /**
     * Creates new form ConfigPanle
     */
    public MainPanel() {
        initComponents();
        init();
        fetchMails();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBox1 = new javax.swing.JCheckBox();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblMails = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        jCheckBox1.setText("jCheckBox1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jSplitPane1.setDividerLocation(350);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        tblMails.setModel(new DefaultTableModel());
        jScrollPane1.setViewportView(tblMails);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        jSplitPane1.setRightComponent(jScrollPane2);

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
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
            java.util.logging.Logger.getLogger(MainPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainPanel().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTable tblMails;
    // End of variables declaration//GEN-END:variables

    // VARIABLES
    private MailClient mc;
    private ConfigReader cr;
    private KeyManager keyMan;
    private DecryptedMail[] mails;
    
    // FUNCTIONS
    private void init(){
        cr=new ConfigReader();
        try {
            final JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new FileNameExtensionFilter("NorthernMail Configuration (*.nmc)", "nmc"));
            int returnVal = fc.showOpenDialog(jMenu1);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                cr.load("/home/snorelax/NorthernMail.nmc");
                mc=new MailClient(cr.getConf());
                keyMan = cr.getConf().getKeyManager();
            }
        } catch (IOException ex) {
            Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void fetchMails(){
        try {
            //Checks
            if(cr == null || mc == null || keyMan == null){return;}
            //Variables
            DefaultTableModel dtm = new DefaultTableModel();
            dtm.setColumnIdentifiers(new String[]{"Sender","Subject","Date"});
            //Fetching
            mc.connect(cr.getConf().getHost(), cr.getConf().getPort());
            for (Mail mail:mc.fetchMail()){
                try {
                    DecryptedMail m = mail.decrypt(keyMan);
                    dtm.addRow(new Object[]{m.getSender(), m.getSubject(), "TODO"});
                    System.out.println(mail.decrypt(keyMan).getSubject());
                } catch (InvalidMailException ex) {
                } catch (InvalidSignatureException ex) {
                    DecryptedMail m = ex.getMail();
                    dtm.addRow(new Object[]{"*Corrupted Signature*", m.getSubject(), "TODO"});
                }
            }
            tblMails.setModel(dtm);
        } catch (IOException ex) {
            Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
