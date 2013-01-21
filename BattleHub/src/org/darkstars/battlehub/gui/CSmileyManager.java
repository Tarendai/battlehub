/*
 * CSmileyManager.java
 *
 * Created on 29-Sep-2007, 13:03:45
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.darkstars.battlehub.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import org.darkstars.battlehub.LMain;
import org.jvnet.flamingo.common.ElementState;
import org.jvnet.flamingo.common.JCommandButton;
import org.jvnet.flamingo.common.JCommandButton.CommandButtonKind;
import org.jvnet.flamingo.common.JCommandButtonPanel;
import org.jvnet.flamingo.common.JIconPopupPanel;
import org.jvnet.flamingo.common.icon.ImageWrapperResizableIcon;
import org.jvnet.flamingo.common.icon.ResizableIcon;

/**
 *
 * @author AF-StandardUsr
 */
public class CSmileyManager {

    public static Integer[] intArray = null;
    public static ResizableIcon[] smiley_images = null;
    public static ResizableIcon[] small_smiley_images = null;
    public static String[] smileys = null;
    public static boolean loaded = false;
    public static ImageIcon[] rank_images = null;
    
    public static ImageIcon ingame = null;
    public static ImageIcon full = null;
    public static ImageIcon open = null;
    public static ImageIcon locked = null;
    
    public static ImageIcon lock = null;
    public static ImageIcon adminimg = null;

    public synchronized static void Init() {

        if(loaded){
            return;
        }
        
        if(ingame == null){
            String path = LMain.GetAbsoluteLobbyFolderPathStatic() +
                        "images/UI/ingame.png";
            ingame = new ImageIcon(path);
        }
        
        if(full == null){
            String path = LMain.GetAbsoluteLobbyFolderPathStatic() +
                        "images/UI/full.png";
            full = new ImageIcon(path);
        }
        
        if(open == null){
            String path = LMain.GetAbsoluteLobbyFolderPathStatic() +
                        "images/UI/open.png";
            open = new ImageIcon(path);
        }
        
        if(locked == null){
            String path = LMain.GetAbsoluteLobbyFolderPathStatic() +
                        "images/UI/locked.png";
            locked = new ImageIcon(path);
        }
        
        if(lock == null){
            String path = LMain.GetAbsoluteLobbyFolderPathStatic() +
                        "images/UI/lock.png";
            lock = new ImageIcon(path);
        }
        
        if(rank_images == null){
            //
            rank_images = new ImageIcon[7];
            
            for (int i = 0; i < 7; i++) {
                String path = LMain.GetAbsoluteLobbyFolderPathStatic() +
                        "images/ranks/" + i + ".png";
                rank_images[i] = new ImageIcon(path);
            }
        }
        if (smileys == null) {
            String path = LMain.GetAbsoluteLobbyFolderPathStatic() + "images/smileys/";
            File f = new File(path);
            File[] contents = f.listFiles();
            
            
            smileys = new String[contents.length];
            intArray = new Integer[smileys.length];
            smiley_images = new ResizableIcon[smileys.length];
            small_smiley_images = new ResizableIcon[smileys.length];
            
            //synchronized(smileys){
            for (int i = 0; i < contents.length; i++) {
                
                try {
                    String c = contents[i].getName();
                    smileys[i] = c.substring(0, c.length() - 4);
                    intArray[i] = new Integer(i);

                    String path2 = LMain.GetAbsoluteLobbyFolderPathStatic() + "images/smileys/" + smileys[i] + ".gif";
                    File f2 = new File(path2);
                    URL url = f2.toURI().toURL();
                    ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(32, 32));
                    smiley_images[i] = ri; //new ImageIcon();

                    small_smiley_images[i] = ri;
                    //if (smiley_images[i] != null) {
                    //    smiley_images[i].setDescription(smileys[i]);
                    //}
                } catch (MalformedURLException ex) {
                    Logger.getLogger(CSmileyManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                //if (smiley_images[i] != null) {
                //    smiley_images[i].setDescription(smileys[i]);
                //}
            }
        }
        if(adminimg == null){
            URL h = CSmileyManager.class.getResource("/images/admin.png");
            adminimg = new ImageIcon(h);
        }
        loaded = true;
    }
    
    public static int SmileyCount(){
        return smileys.length;
    }
    
    public static JCommandButton GetSmileyButton(final String smiley, final ResizableIcon i, final JTextArea textArea){

        if(i == null){
            return null;
        }
        if(smiley == null){
            return null;
        }
        JCommandButton b = new JCommandButton(smiley, i);
        b.setCommandButtonKind(CommandButtonKind.ACTION_ONLY);
        b.addActionListener( new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.insert(":"+smiley+":", textArea.getCaretPosition());
            }
            
        });
        return b;
    }
    
    public static  JIconPopupPanel GetSmileyPanel( JTextArea textArea){
        //
        Init();
        JCommandButtonPanel panel = new JCommandButtonPanel(ElementState.ORIG);
        panel.addButtonGroup("Smileys", 0);
        for(int i = 0; i < smileys.length; i++){
            String s = smileys[i];
            ResizableIcon icon = smiley_images[i];
            //
            if(icon != null){
                JCommandButton b = GetSmileyButton(s,icon,textArea);
                panel.addButtonToGroup("Smileys", b);
            }
        }
        JIconPopupPanel ipanel = new JIconPopupPanel(panel,4,3);
        return ipanel;
    }
    
    public static JCommandButton GetSmileyPicker(JTextArea textArea){
        //
        URL url = CSmileyManager.class.getResource("/images/tango/32x32/emotes/face-grin.png");
        ResizableIcon i = ImageWrapperResizableIcon.getIcon(url, new Dimension(32, 32));
        JCommandButton button = new JCommandButton("Smileys",i);
        button.setCommandButtonKind(CommandButtonKind.POPUP_ONLY);
        button.addPopupActionListener(new PanelPopupActionListener(GetSmileyPanel(textArea)));
        button.setState(ElementState.MEDIUM, true);
        return button;
    }
}
