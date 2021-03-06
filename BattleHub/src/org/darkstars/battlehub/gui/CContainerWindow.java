/*
 * CContainerWindow.java
 *
 * Created on 13 June 2008, 15:46
 */
package org.darkstars.battlehub.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 *
 * @author  AF-Standard
 */
public class CContainerWindow extends javax.swing.JFrame implements IGUIContainer {

    /** Creates new form CContainerWindow */
    public CContainerWindow() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                initComponents();
            }
        });

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        containerPanel = new org.darkstars.battlehub.gui.CContainerPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout containerPanelLayout = new javax.swing.GroupLayout(containerPanel);
        containerPanel.setLayout(containerPanelLayout);
        containerPanelLayout.setHorizontalGroup(
            containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        containerPanelLayout.setVerticalGroup(
            containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 309, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(containerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(containerPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.darkstars.battlehub.gui.CContainerPanel containerPanel;
    // End of variables declaration//GEN-END:variables
    
    @Override
    public void SetGUIObject(final JComponent c) {
        System.out.println("added panel window");
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                containerPanel.SetGUIObject(c);
            }
        });
    }

    @Override
    public void RemoveGUIObjects() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                containerPanel.RemoveGUIObjects();
            }
        });
    }
    
    public boolean fullscreen = false;
    Dimension origSize;
    public void ToggleFullScreen(){
        //
        fullscreen = !fullscreen;
        if(fullscreen){
            
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    origSize = getSize();
                    Toolkit tk = Toolkit.getDefaultToolkit();
                    int xSize = ((int) tk.getScreenSize().getWidth());
                    int ySize = ((int) tk.getScreenSize().getHeight());
                    setVisible(false);
                    dispose();
                    setUndecorated(true);
                    setSize(xSize,ySize);
                    setVisible(true);
                    
                }
            });
        } else{
            //
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    setVisible(false);
                    dispose();
                    setUndecorated(false);
                    setSize(origSize);
                    setVisible(true);
                    
                    
                }
            });
        }
    }
}
