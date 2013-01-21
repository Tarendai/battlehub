/*
 * JTraffic.java
 *
 * Created on 29 May 2006, 14:57
 */

package org.darkstars.battlehub;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import org.darkstars.battlehub.gui.CUISettings;
import javax.swing.SwingUtilities;
import org.darkstars.battlehub.framework.CConnection;
import org.darkstars.battlehub.framework.ITrafficListener;

/**
 *
 * @author  AF
 */

public class CTraffic extends javax.swing.JFrame implements ClipboardOwner, ITrafficListener {
    /**
     * A reference to the current connection class. This is used to send raw
     * protocol data from the textbox. It is initialized in the call to Init()
     */
    public CConnection jc = null;

    /**
     * Creates a JTraffic form and initializes its UI components.
     * 
     * Call Init() afterwards or sending data usign the window will cause a null
     * pointer exception at runtime.
     */
    public CTraffic () {
        initComponents ();
    }
    /**
     * Initializes the traffic windows logic
     * @param c - the active connection class
     */
    @Override
    public void Init (CConnection c){
        jc = c;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setTitle("Raw Traffic");
        setIconImage(CUISettings.GetWindowIcon());

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() | java.awt.Font.BOLD, jLabel1.getFont().getSize()+3));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/22x22/places/network-server.png"))); // NOI18N
        jLabel1.setText("Raw Protocol Traffic");

        jTextArea1.setEditable(false);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setWrapStyleWord(true);
        jTextArea1.setDoubleBuffered(true);
        jScrollPane1.setViewportView(jTextArea1);

        jButton1.setText("Send");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton1MousePressed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/edit-copy.png"))); // NOI18N
        jButton2.setText("Copy Traffic To Clipboard");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/edit-clear.png"))); // NOI18N
        jButton3.setText("Clear Traffic");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton1, jTextField1});

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void jButton1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MousePressed
        Runnable doWorkRunnable = new Runnable () {
            @Override
            public void run () {
                String n = jTextField1.getText ();
                jc.SendLine (n);
                Add (n);
                jTextField1.setText ("");
            }
            
        };
        SwingUtilities.invokeLater (doWorkRunnable);
    }//GEN-LAST:event_jButton1MousePressed

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    Clipboard clipboard = getToolkit ().getSystemClipboard ();
    String errorText = jTextArea1.getText ();

    StringSelection fieldContent = new StringSelection (errorText);
    clipboard.setContents (fieldContent, this);
}//GEN-LAST:event_jButton2ActionPerformed

private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    jTextArea1.setText("");
}//GEN-LAST:event_jButton3ActionPerformed
    
    /**
     * Appends a new comamnd to the traffic window text. Use this to add traffic
     * as its recieved for display to the user.
     * 
     * @param s - the commands to be apended to the traffic view
     */
    @Override
    public void Add (String s){
        final String t = s;
        SwingUtilities.invokeLater ( new Runnable () {
            @Override
            public void run () {
                //if(jTextArea1.isShowing ()){
                    jTextArea1.append (t+"\n");
                    jTextArea1.setCaretPosition (jTextArea1.getDocument().getLength()-1);
                //}
            }
        });
        
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        //
    }
    
}
