/*
 * CUpdateChecker.java
 *
 * Created on 12 February 2007, 01:19
 *
 */

package org.darkstars.battlehub;

import org.darkstars.battlehub.gui.WarningWindow;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import org.darkstars.battlehub.framework.Misc;

/**
 *
 * @author Tom
 */
public class CUpdateChecker extends Thread{
    public static String version = "Beta 5";
    
    /**
     * The numerical version number of aflobby
     */
    public static int versionNumber = 56;
    
    /**
     * An enum describing the current status of this version of AFLobby and
     * how upto date it is. Used to determine whether this AFLobby is
     * experimental, out of date, or current
     */
    public enum versionStatus {
        uptodate,
        outofdate,
        unknown,
        experimental
    }
    
    private static CUpdateHandler.versionStatus uptodate = CUpdateHandler.versionStatus.unknown;
    
    public static CUpdateHandler.versionStatus IsUpToDate(){
        return uptodate;
    }
    
    JLabel label;
    
    /**
     * Creates a new instance of CUpdateChecker
     * @param l 
     */
    public CUpdateChecker (JLabel l) {
        label = l;
    }
    
    @Override
    public void run () {
        CUpdateHandler c = new CUpdateHandler();
        uptodate = c.IsUpdateAvailable();
        
        //String s = Misc.getURLContent ("http://tarendai.googlepages.com/AFLVersion.txt","cuckoo");

        final ImageIcon i;
        
        String t = "";
        if(uptodate == CUpdateHandler.versionStatus.unknown){//s.equalsIgnoreCase ("cuckoo")){
            t = "Unable to perform update check, darkstars.co.uk may be down";
            i = null;
        }else{
            //String tc = String.valueOf ((char)127);
            //String[] lines = s.split ("@@");
            
            //int v = Integer.parseInt (lines[0]);
            
            if(CUpdateHandler.versionStatus.outofdate == uptodate){//versionNumber < v){
                //uptodate = versionStatus.outofdate;
                i = new ImageIcon (getClass ().getResource ("/images/UI/alert.png"));
                
                t = "A new version of BattleHub is available please run the installer/updater";//+lines[1];
                //final String wt = Misc.makeSentence (lines,2);
                //final String wttitle = "Update Available " + lines[1];
//                javax.swing.SwingUtilities.invokeLater (new Runnable () {
//                    @Override
//                    public void run () {
//                        new WarningWindow (wt,
//                            wttitle
//                            ).setVisible (true);
//                    }
//                });

            } else if (CUpdateHandler.versionStatus.experimental == uptodate){//v < versionNumber){
                i = null;
                t = "Goodness Gracious That versions unreleased!";
                //uptodate = versionStatus.experimental;
            } else {
                i = null;
                t = "Your copy of BattleHub is upto date";
                //uptodate = versionStatus.uptodate;
            }
        }
        
        final String t2 = t;
        
        
        Runnable doWorkRunnable = new Runnable () {
            @Override
            public void run () {
                label.setText (t2);
                label.setIcon (i);
            }
        };
        SwingUtilities.invokeLater (doWorkRunnable);
    }
    
}
