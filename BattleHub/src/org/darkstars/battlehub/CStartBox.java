/*
 * CStartBox.java
 *
 * Created on 12 July 2007, 05:17
 */

package org.darkstars.battlehub;

import org.darkstars.battlehub.gui.borders.DefaultResizableBorder;
import org.darkstars.battlehub.gui.borders.ResizableBorder;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JComponent;

/**
 *
 * @author  Tom
 */
public class CStartBox extends javax.swing.JPanel {

    public CStartRectangleDrawer j;
    private int number = 0;

    /** Creates new form CStartBox
     * @param j 
     */
    public CStartBox(CStartRectangleDrawer j) {
        this.j = j;
        number = j.boxes.size();
        initComponents();
    }

    public int getNumber() {
        //
        return number;
    }
    
    public void setNumber(int n){
        number = n;
    }
    //Point p;
    //Point p2;

    //
    private int cursor;
    private Point startPos = null;

    private void didResized() {
        if (getParent() != null) {
            getParent().repaint();
            invalidate();
            ((JComponent) getParent ()).revalidate();
        }
    }

    //
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();

        setBorder(new DefaultResizableBorder(6));
        setMinimumSize(new java.awt.Dimension(67, 67));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                formMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });

        jButton1.setText("Delete");
        jButton1.setMargin(new java.awt.Insets(2, 6, 2, 6));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Box1", "Box2", "Box3", "Box4", "Box5", "Box6", "Box7", "Box8" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );
    }// </editor-fold>//GEN-END:initComponents

private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
    startPos = null;
}//GEN-LAST:event_formMouseReleased

private void formMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseExited
    setCursor (Cursor.getDefaultCursor ());
}//GEN-LAST:event_formMouseExited

private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
    ResizableBorder border = (ResizableBorder) getBorder ();
    setCursor (Cursor.getPredefinedCursor (border.getResizeCursor (evt)));
}//GEN-LAST:event_formMouseMoved

    private void jComboBox1ActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        number = this.jComboBox1.getSelectedIndex ();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton1ActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        j.jLayeredPane1.remove (this);
        j.boxes.remove (this);
        if (getParent () != null) {
            getParent ().repaint ();
            invalidate ();
            ((JComponent) getParent ()).revalidate ();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formMouseDragged (java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        if (startPos != null) {
            int dx = evt.getX ()-startPos.x;
            int dy = evt.getY ()-startPos.y;
            switch (cursor) {
                case Cursor.N_RESIZE_CURSOR:
                    setBounds (getX (), getY ()+dy, getWidth (), getHeight ()-dy);
                    didResized ();
                    break;
                case Cursor.S_RESIZE_CURSOR:
                    setBounds(getX(), getY(), getWidth(), getHeight()+dy); 
                    startPos = evt.getPoint(); 
                    didResized(); 
                    break;
                case Cursor.W_RESIZE_CURSOR: 
                   setBounds(getX()+dx, getY(), getWidth()-dx, getHeight()); 
                   didResized(); 
                   break; 
                case Cursor.E_RESIZE_CURSOR: 
                   setBounds(getX(), getY(), getWidth()+dx, getHeight()); 
                   startPos = evt.getPoint(); 
                   didResized(); 
                   break; 
                case Cursor.NW_RESIZE_CURSOR: 
                   setBounds(getX()+dx, getY()+dy, getWidth()-dx, getHeight()-dy); 
                   didResized(); 
                   break; 
                case Cursor.NE_RESIZE_CURSOR: 
                   setBounds(getX(), getY()+dy, getWidth()+dx, getHeight()-dy); 
                   startPos = new Point(evt.getX(), startPos.y); 
                   didResized(); 
                   break; 
                case Cursor.SW_RESIZE_CURSOR: 
                   setBounds(getX()+dx, getY(), getWidth()-dx, getHeight()+dy); 
                   startPos = new Point(startPos.x, evt.getY()); 
                   didResized(); 
                   break; 
                case Cursor.SE_RESIZE_CURSOR: 
                   setBounds(getX(), getY(), getWidth()+dx, getHeight()+dy); 
                   startPos = evt.getPoint(); 
                   didResized(); 
                   break; 
                case Cursor.MOVE_CURSOR: 
                   Rectangle bounds = getBounds(); 
                   bounds.translate(dx, dy); 
                   setBounds(bounds); 
                   didResized();
              }

            // cursor shouldn't change while dragging
            setCursor (Cursor.getPredefinedCursor (cursor));
        }
    }//GEN-LAST:event_formMouseDragged

    private void formMousePressed (java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        ResizableBorder border = (ResizableBorder) getBorder ();
        cursor = border.getResizeCursor (evt);
        startPos = evt.getPoint();//getLocationOnScreen();//getPoint ();
        /*p = evt.getPoint ();
        p2 = getLocation ();*/
    }//GEN-LAST:event_formMousePressed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox1;
    // End of variables declaration//GEN-END:variables
    
}
