package org.darkstars.battlehub;

import org.darkstars.battlehub.helpers.CFSHelper;
import org.darkstars.battlehub.protocol.tasserver.RectHandler;
import org.darkstars.battlehub.protocol.tasserver.RectEntry;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

/*
 * CStartRectangleDrawer.java
 *
 * Created on 10 July 2007, 23:28
 */
import org.darkstars.battlehub.gui.CSpringBattleWindow;

/**
 *
 * @author  Tom
 */
public class CStartRectangleDrawer extends javax.swing.JPanel {
    JFileChooser fc = new JFileChooser();
//    JFileChooser imgfc = new JFileChooser();
    
    public ArrayList<CStartBox> boxes = new ArrayList<CStartBox>();
    CSpringBattleWindow b = null;
    
    class FileExtFilter extends javax.swing.filechooser.FileFilter {
        String extension = "";
        FileExtFilter(String ext){
            extension = ext;
        }
        @Override
        public boolean accept(File file) {
            String filename = file.getName();
            return filename.endsWith(extension);
        }
        
        @Override
        public String getDescription() {
            return "*"+extension;
        }
    }
    
    /** Creates new form CStartRectangleDrawer 
     * @param b - a reference to the battlewindow GUI
     */
    public CStartRectangleDrawer(CSpringBattleWindow b) {
        this.b = b;
        initComponents();
        fc.addChoosableFileFilter(new FileExtFilter(".sbox"));
        fc.setCurrentDirectory(null);
        
//        imgfc.addChoosableFileFilter(new FileExtFilter(".jpg"));
//        imgfc.setCurrentDirectory(null);
    }
    BufferedImage bi=null;
    public void SetMinimapImage(final ImageIcon i, final BufferedImage b) {
        bi = b;
        Runnable doWorkRunnable = new Runnable() {

            @Override
            public void run() {
                ImageIcon oldic = (ImageIcon) Minimap.getIcon();
                Minimap.setIcon(new ImageIcon(i.getImage().getScaledInstance(Minimap.getWidth(), Minimap.getHeight(), Image.SCALE_SMOOTH)));
                if(oldic != null){
                    oldic.getImage().flush();
                    oldic = null;
                }
            }
        };
        SwingUtilities.invokeLater(doWorkRunnable);
    }
    /*
     * Only to be called in the GUI thread.
     * This function removes all the boxes from the drawing view.
     * It doesnt actually gt rid fo any boxes already placed so 
     * it wont affect other players.
     */
    public void ClearBoxes(){
        for(CStartBox c : boxes){
            jLayeredPane1.remove (c);
        }
        boxes.clear ();
        jLayeredPane1.validate ();
        jLayeredPane1.repaint ();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        Minimap = new javax.swing.JLabel();
        AddButton = new javax.swing.JButton();
        ClearButton = new javax.swing.JButton();
        UpdateButton = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        SaveButton = new javax.swing.JButton();
        LoadButton = new javax.swing.JButton();

        jLayeredPane1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jLayeredPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLayeredPane1MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLayeredPane1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLayeredPane1MouseReleased(evt);
            }
        });
        jLayeredPane1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jLayeredPane1MouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jLayeredPane1MouseMoved(evt);
            }
        });

        Minimap.setFocusable(false);
        Minimap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                MinimapMouseReleased(evt);
            }
        });
        Minimap.setBounds(0, 0, 360, 360);
        jLayeredPane1.add(Minimap, javax.swing.JLayeredPane.DEFAULT_LAYER);

        AddButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/list-add.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/darkstars/battlehub/languages"); // NOI18N
        AddButton.setText(bundle.getString("CStartRectangleDrawer.AddButton.text")); // NOI18N
        AddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddButtonActionPerformed(evt);
            }
        });

        ClearButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/edit-clear.png"))); // NOI18N
        ClearButton.setText(bundle.getString("CStartRectangleDrawer.ClearButton.text")); // NOI18N
        ClearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearButtonActionPerformed(evt);
            }
        });

        UpdateButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/document-save.png"))); // NOI18N
        UpdateButton.setText(bundle.getString("CStartRectangleDrawer.UpdateButton.text")); // NOI18N
        UpdateButton.setMargin(new java.awt.Insets(2, 8, 2, 8));
        UpdateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateButtonActionPerformed(evt);
            }
        });

        jToggleButton1.setText(bundle.getString("CStartRectangleDrawer.jToggleButton1.text")); // NOI18N
        jToggleButton1.setEnabled(false);

        jToggleButton2.setText(bundle.getString("CStartRectangleDrawer.jToggleButton2.text")); // NOI18N
        jToggleButton2.setEnabled(false);

        SaveButton.setText(bundle.getString("CStartRectangleDrawer.SaveButton.text")); // NOI18N
        SaveButton.setEnabled(false);
        SaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveButtonActionPerformed(evt);
            }
        });

        LoadButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/document-open.png"))); // NOI18N
        LoadButton.setText(bundle.getString("CStartRectangleDrawer.LoadButton.text")); // NOI18N
        LoadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoadButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToggleButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
                    .addComponent(UpdateButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(AddButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SaveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ClearButton))
                    .addComponent(LoadButton))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {AddButton, ClearButton, LoadButton, SaveButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(AddButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ClearButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SaveButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LoadButton))
                    .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jToggleButton1)
                    .addComponent(jToggleButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UpdateButton)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

private void jLayeredPane1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLayeredPane1MouseReleased
    
}//GEN-LAST:event_jLayeredPane1MouseReleased

private void jLayeredPane1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLayeredPane1MouseExited
    
}//GEN-LAST:event_jLayeredPane1MouseExited

private void jLayeredPane1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLayeredPane1MousePressed
        
}//GEN-LAST:event_jLayeredPane1MousePressed

private void jLayeredPane1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLayeredPane1MouseMoved
}//GEN-LAST:event_jLayeredPane1MouseMoved
    
private void jLayeredPane1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLayeredPane1MouseDragged

}//GEN-LAST:event_jLayeredPane1MouseDragged
    
private void UpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateButtonActionPerformed
        //REMOVESTARTRECT allyno
        // remove all starting boxes
        ArrayList<RectEntry> rl = RectHandler.getRectList ();
        for(RectEntry r : rl){
            b.LM.protocol.SendTraffic ("REMOVESTARTRECT "+r.getNumAlly ());
        }
        RectHandler.clearRects ();
        
        // calculate and add the new starting boxes to recthandler
        int highest = -1;
        java.awt.Dimension td = jLayeredPane1.getSize();
        for(int i = 0; i < boxes.size (); i++){
            CStartBox c = boxes.get (i);
            //if(c.getNumber () > highest){
                highest = c.getNumber ();
                
                java.awt.Point p = c.getLocation ();
                java.awt.Dimension d = c.getSize ();
                RectHandler.SetRect (highest,
                    (int)(p.getX ()*(200/td.getWidth ())),
                    (int)(p.getY ()*(200/td.getHeight ())),
                    (int)(p.getX ()*(200/td.getWidth ()))+(int)(d.getWidth ()*(200/td.getWidth ())),
                    (int)(p.getY ()*(200/td.getHeight ()))+(int)(d.getHeight ()*(200/td.getHeight ()))
                    );
//            }
            //RectHandler.
        }
        // update the lobby server with the enw rectangles
        //ADDSTARTRECT allyno left top right bottom
        rl = RectHandler.getRectList ();
        for(RectEntry r : rl){
            b.LM.protocol.SendTraffic ("ADDSTARTRECT "+r.getNumAlly ()+" "+r.getStartRecLeft ()+" "+r.getStartRecTop ()+" "+r.getStartRecRight ()+" "+r.getStartRecBottom ());
        }
        b.MapChange ();
}//GEN-LAST:event_UpdateButtonActionPerformed
    
    private void ClearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearButtonActionPerformed
        ClearBoxes();
}//GEN-LAST:event_ClearButtonActionPerformed
    
    private void AddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddButtonActionPerformed
        CStartBox c = new CStartBox (this);
        c.setBounds (10,10, 90, 90);
        jLayeredPane1.add (c,new Integer (1));
        jLayeredPane1.validate ();
        boxes.add (c);
}//GEN-LAST:event_AddButtonActionPerformed

    private void LoadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoadButtonActionPerformed
        fc.setDialogTitle("open a saved set of starting boxes");
        
        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            // load the boxes
            File file = fc.getSelectedFile();
            String boxfile = CFSHelper.GetFileContents(file);
            RectHandler.LoadBoxes(boxfile);
            
            // remove all starting boxes
            ArrayList<RectEntry> rl = RectHandler.getRectList ();
            for(RectEntry r : rl){
                b.LM.protocol.SendTraffic ("REMOVESTARTRECT "+r.getNumAlly ());
            }
            RectHandler.clearRects ();
            
            //ADDSTARTRECT allyno left top right bottom
            rl = RectHandler.getRectList ();
            for(RectEntry r : rl){
                b.LM.protocol.SendTraffic ("ADDSTARTRECT "+r.getNumAlly ()+" "+r.getStartRecLeft ()+" "+r.getStartRecTop ()+" "+r.getStartRecRight ()+" "+r.getStartRecBottom ());
            }
            b.MapChange ();
            
            
            // clear all the boxes out of the UI
            ClearBoxes();
            
            java.awt.Dimension td = jLayeredPane1.getSize ();
            for(RectEntry r : rl){
                CStartBox c = new CStartBox (this);
                //setBounds(int x, int y, int width, int height)
                c.setBounds ((int)(r.getStartRecLeft()/(200*td.getWidth())),
                        (int)(r.getStartRecTop()/(200*td.getHeight())),
                        (int)((r.getStartRecRight()-r.getStartRecLeft())/(200*td.getWidth())),
                        (int)((r.getStartRecBottom()-r.getStartRecTop())/(200*td.getHeight()))
                        );
                
                c.setNumber(r.getNumAlly());
                
                jLayeredPane1.add (c,new Integer (1));
                jLayeredPane1.validate ();
                boxes.add (c);
            }
            
            // update the lobby server with the enw rectangles
            
        } //else {
        //    log.append("Open command cancelled by user." + newline);
        //}
}//GEN-LAST:event_LoadButtonActionPerformed

    private void MinimapMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MinimapMouseReleased
        
        CStartBox c = new CStartBox (this);
        c.setBounds (Math.min(evt.getX(),Minimap.getWidth()-90),Math.min(evt.getY(),Minimap.getHeight()-90), 90, 90);
        jLayeredPane1.add (c,new Integer (1));
        jLayeredPane1.validate ();
        boxes.add (c);
    }//GEN-LAST:event_MinimapMouseReleased

    private void SaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveButtonActionPerformed
        fc.setDialogTitle("Save a set of starting boxes");
        
        int returnVal = fc.showSaveDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String s = "";
            int highest = -1;
            java.awt.Dimension td = jLayeredPane1.getSize();
            for(int i = 0; i < boxes.size (); i++){
                CStartBox c = boxes.get (i);
                if(c.getNumber () > highest){
                    highest = c.getNumber ();
                    java.awt.Point p = c.getLocation ();
                    java.awt.Dimension d = c.getSize ();
                    /*RectHandler.SetRect (highest,
                        (int)(p.getX ()*(200/td.getWidth ())),
                        (int)(p.getY ()*(200/td.getHeight ())),
                        (int)(p.getX ()*(200/td.getWidth ()))+(int)(d.getWidth ()*(200/td.getWidth ())),
                        (int)(p.getY ()*(200/td.getHeight ()))+(int)(d.getHeight ()*(200/td.getHeight ()))
                        );*/
                }
                //RectHandler.
            }
        }
}//GEN-LAST:event_SaveButtonActionPerformed
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddButton;
    private javax.swing.JButton ClearButton;
    private javax.swing.JButton LoadButton;
    private javax.swing.JLabel Minimap;
    private javax.swing.JButton SaveButton;
    private javax.swing.JButton UpdateButton;
    public javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    // End of variables declaration//GEN-END:variables
    
}