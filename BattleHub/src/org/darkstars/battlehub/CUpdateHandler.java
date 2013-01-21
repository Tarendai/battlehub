/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.darkstars.battlehub;

import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.darkstars.battlehub.framework.CUnZipper;
import org.darkstars.battlehub.framework.Misc;
import org.darkstars.battlehub.framework.downloader.CDownloadFile;
import org.darkstars.battlehub.framework.downloader.IDownloaderListener;

/**
 *
 * @author AF-Standard
 */
public class CUpdateHandler implements IDownloaderListener {

    CDownloadFile d;
    public static versionStatus uptodate = versionStatus.unknown;

    public CUpdateHandler() {
        //if(true){
        uptodate = IsUpdateAvailable();
//        if (uptodate == versionStatus.outofdate) {
//            //(Component parentComponent,Object message, String title, int optionType, int messageType) 
//            int i = JOptionPane.showConfirmDialog(
//                    null,
//                    "A new Update is available, would you like to download and install it? (Its reccomended you run the standalone updater if theres an update available)",
//                    "Update",
//                    JOptionPane.YES_NO_OPTION,
//                    JOptionPane.WARNING_MESSAGE);
//            if (i == JOptionPane.YES_OPTION) {
//                //
//                try {
//                    //
//                    d = new CDownloadFile("http://www.darkstars.co.uk/ahem/battlehub.zip", File.createTempFile("battlehubupdate", ".zip.tmp"));
//                    d.RegisterListener(this);
//                    d.StartDownload();
//                } catch (IOException ex) {
//                    Logger.getLogger(CUpdateHandler.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        } else {
//            JOptionPane.showMessageDialog(null,
//                "You have the latest build available",
//                "Up to date",
//                JOptionPane.INFORMATION_MESSAGE);
//        }

    }

    @Override
    public void DownloadFinished(File f) {
        //
        SwingUtilities.invokeLater( new Runnable() {

            @Override
            public void run() {
                d.setVisible(false);
                d = null;
            }
        });
        File arp = new File(LMain.GetAbsoluteRootPath());
        CUnZipper.unzipArchive(f, arp);
        JOptionPane.showMessageDialog(null,
                "Update has been downloaded and extracted, please restart the lobby for changes to take effect",
                "Update Complete",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static String version = "Beta 5.3";
    /**
     * The numerical version number of aflobby
     */
    public static int versionNumber = 59;

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

    public versionStatus IsUpdateAvailable() {
        String s = Misc.getURLContent("http://battlehub.darkstars.co.uk/tversion.txt", "cuckoo");

        versionStatus uptodate2;

        if (s.equalsIgnoreCase("cuckoo")) {
            //t = "Unable to perform update check, darkstars.co.uk may be down";
            uptodate2 = versionStatus.unknown;
        } else {
            //String tc = String.valueOf ((char)127);
            String[] lines = s.split("@@");

            int v = Integer.parseInt(lines[0].trim());

            if (versionNumber < v) {
                uptodate2 = versionStatus.outofdate;

            //t = "A new version of aflobby is available:  "+lines[1];

            } else if (v < versionNumber) {
                //t = "Goodness Gracious That versions unreleased!";
                uptodate2 = versionStatus.experimental;
            } else {
                //t = "Your copy of AFLobby is upto date";
                uptodate2 = versionStatus.uptodate;
            }
        }
        return uptodate2;
    }
}
