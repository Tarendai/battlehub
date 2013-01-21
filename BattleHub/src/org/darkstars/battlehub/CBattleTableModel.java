/*
 * CBattleTableModel.java
 *
 * Created on 17 September 2006, 02:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.darkstars.battlehub;

import java.awt.Dimension;
import java.net.URL;
import java.text.DecimalFormat;
import org.darkstars.battlehub.framework.CPlayer;
import org.darkstars.battlehub.gui.CSmileyManager;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import org.darkstars.battlehub.gui.CChannel;
import org.jvnet.flamingo.common.icon.ImageWrapperResizableIcon;
import org.jvnet.flamingo.common.icon.ResizableIcon;

/**
 *
 * @author AF
 */
public class CBattleTableModel extends AbstractTableModel {

    class battle_comp implements Comparator {

        @Override
        public int compare(Object a, Object b) {
            try {
                int as;
                int bs;
                if (((CBattleInfo) a).IsIngame()) {
                    as = 0;
                } else if (((CBattleInfo) a).IsPassworded()) {
                    as = 1;
                } else {
                    as = 2;
                }
                if (((CBattleInfo) b).IsIngame()) {
                    bs = 0;
                } else if (((CBattleInfo) b).IsPassworded()) {
                    bs = 1;
                } else {
                    bs = 2;
                }
                if (as == bs) {
                    return ((CBattleInfo) a).GetMod().compareTo(((CBattleInfo) b).GetMod());
                }
                if (as > bs) {
                    return 0;
                } else {
                    return 1;
                }
                /*int i1=((CBattleInfo)a).GetID ();
                int i2 = ((CBattleInfo)b).GetID();
                if(i1 < i2){
                return 1;
                }else{
                return 0;
                }*/
            } catch (Exception e) {
                return 0;
            }
        }
    }
    
    DecimalFormat twoPlaces = new DecimalFormat("0.00");
    LMain LM;
    //CChannelView cv;
    private String[] columns = {
        java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("CBattleTableModel.Description"),
        " ",
        " ",
        java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("CBattleTableModel.Map"),
        java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("CBattleTableModel.Mod"),
        java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("CBattleTableModel.Host"),
        " ",
        "avg rank",
        java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("CBattleTableModel.Players"),
        "ID",
        " ",
        "NAT"
    };
    
    ResizableIcon warningTriangle = null;
    public ArrayList<CBattleInfo> battles = new ArrayList<CBattleInfo>();

    CBattleTableModel(LMain L) {
        LM = L;
        CSmileyManager.Init();
    }

    @Override
    public int getRowCount() {
        return battles.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }


    @Override
    public String getColumnName(int col) {
        return columns[col];
    }

    CBattleInfo GetBattleAt(int row) {
        //synchronized(battles){
        if (row < battles.size()) {
            return battles.get(row); //
        } else {
            return null;
        }
        //}
    }

    @Override
    public Object getValueAt(int row, int col) {

        if (row > battles.size()) {
            return "";
        }

        CBattleInfo b = GetBattleAt(row);
        if (b == null) {
            return "";
        }

        switch (col) {
            case 0:
            {
                return b;
            }
            case 1:
            {
                if (b.IsIngame()) {
                    return CSmileyManager.ingame;
                } else if (b.locked) {
                    return CSmileyManager.locked;
                } else if (b.maxplayers == b.GetPlayers().size()) {
                    return CSmileyManager.full;
                } else if (b.isladdergame()){
                    return CSmileyManager.lock;
                } else {
                    return CSmileyManager.open;
                }                
            }
            case 2:
            {
                //
                if(b.IsPassworded()){
                    return CSmileyManager.lock;
                } else{
                     return new ImageIcon();
                }
            }
            case 3:
            {
                String t = b.GetMap();
                if (t.endsWith(".smf") || t.endsWith(".sm3")) {
                    t = t.substring(0, t.length() - 4);
                }
                return t;
            }
            case 4:
            {
                String modname = b.GetMod();

                // please thank springlobby for this little block
                //{
                modname = modname.replace("Absolute Annihilation", "AA");
                modname = modname.replace("Complete Annihilation", "CA");
                modname = modname.replace("Balanced Annihilation", "BA");
                modname = modname.replace("Expand and Exterminate", "EE");
                modname = modname.replace("War Evolution", "WarEvo");
                modname = modname.replace("TinyComm", "TC");
                modname = modname.replace("BETA", "b");
                modname = modname.replace("Public Alpha", "pa");
                modname = modname.replace("Public Beta", "pb");
                modname = modname.replace("Public", "p");
                modname = modname.replace("Alpha", "a");
                //}
                return modname;
            }
            case 5:
            {
                return b.GetHost().name;
            }
            
            case 6:
            {
                ArrayList<CPlayer> battlePlayers = b.GetPlayers();
                int total = 0;
                for(CPlayer p : battlePlayers){
                    if(!p.isBot()){
                        total += p.rank;
                    }
                }
                total /= battlePlayers.size();
                return CSmileyManager.rank_images[total];
            }
            case 7:
            {
                ArrayList<CPlayer> battlePlayers = b.GetPlayers();
                if(battlePlayers.size() == 1){
                    if(battlePlayers.get(0).isBot()){
                        return "-";
                    }
                }
                float total = 0;
                for(CPlayer p : battlePlayers){
                    if(!p.isBot()){
                        total += p.rank+1;
                    }
                }
                total /= battlePlayers.size();
                return twoPlaces.format(total);
            }
            case 8:
            {
                String q = "(";
                q += (b.GetPlayers().size() - b.spectatorcount) + "+" + b.spectatorcount;
                q += "/" + b.maxplayers + ")";
                q += b.GetPlayerNames();
                return q;
            }
            case 9:
            {
                return ""+b.GetID();
            }
            case 10:
            {
                return CChannel.GetFlagIcon(b.GetHost().getCountry());
            }
            case 11:
            {
                if(b.natType > 0){
                    //
                    if(warningTriangle == null){
                        //
                        URL url = getClass().getResource("/images/tango/16x16/status/weather-severe-alert.png");
                        warningTriangle = ImageWrapperResizableIcon.getIcon(url, new Dimension(16, 16));
                    }
                    return warningTriangle;
                } else {
                    return null;
                }
            }
            default:
            {
                break;
            }
        }

        return "";
    }

    public void AddBattle(CBattleInfo b) {
        if(battles.contains(b)){
            return;
        }
        
        battles.add(b);
        Runnable doWorkRunnable = new Runnable() {
            @Override
            public void run() {
                fireTableDataChanged();
            }
        };

        SwingUtilities.invokeLater(doWorkRunnable);
    }

    public void RemoveBattle(CBattleInfo b) {
        synchronized (battles) {
            Iterator<CBattleInfo> i = battles.iterator();
            while(i.hasNext()){
                CBattleInfo ba = i.next();
                if(ba == b){
                    i.remove();
                }
            }
        }
        Runnable doWorkRunnable = new Runnable() {
            @Override
            public void run() {
                fireTableDataChanged();
            }
        };

        SwingUtilities.invokeLater(doWorkRunnable);
//        synchronized (battles) {
//            battles.remove(b);
//        }
//        fireTableDataChanged();
//        return b;
    }
    
    public void RemoveBattle(int idx) {
        synchronized (battles) {
            Iterator<CBattleInfo> i = battles.iterator();
            while(i.hasNext()){
                CBattleInfo b = i.next();
                if(b.GetID() == idx){
                    i.remove();
                }
            }
        }
        Runnable doWorkRunnable = new Runnable() {
            @Override
            public void run() {
                fireTableDataChanged();
            }
        };

        SwingUtilities.invokeLater(doWorkRunnable);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
    @Override
    public Class getColumnClass(int column) {
        Class returnValue;
        if(column == 11){
            return Icon.class;
        }
        if ((column >= 0) && (column < getColumnCount())) {
            returnValue = getValueAt(0, column).getClass();
        } else {
            returnValue = Object.class;
        }
        return returnValue;
     }

}