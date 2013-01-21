/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.darkstars.battlehub.gui;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import org.jvnet.flamingo.common.PopupMenuListener;

/**
 *
 * @author AF-Standard
 */
public class PanelPopupActionListener implements PopupMenuListener {

    public PanelPopupActionListener(JComponent panel) {
        //
        p = panel;
    }
    JComponent p;

    @Override
    public void menuAboutToShow(JPopupMenu popupMenu) {
        popupMenu.add(p);
    }
}
