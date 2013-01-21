/*
 * CBattleTableModel.java
 *
 * Created on 17 September 2006, 02:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.darkstars.battlehub.gui;


import org.darkstars.battlehub.framework.CPlayer;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;
import org.darkstars.battlehub.framework.Misc;

/**
 *
 * @author AF
 */
public class CPlayerTableModel extends AbstractTableModel {

    
    private String[] columns = {" "," "," ","Username","Status"};
    public ArrayList<CPlayer> players = new ArrayList<CPlayer>();

    public CPlayerTableModel() {
        CSmileyManager.Init();
    }

    public void clear(){
        players.clear();
        fireTableDataChanged();
    }
    
    @Override
    public int getRowCount() {
        return players.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }


    @Override
    public String getColumnName(int col) {
        return columns[col];
    }

    CPlayer GetPlayerAt(int row) {
        //synchronized(battles){
        if (row < players.size()) {
            return players.get(row); //
        } else {
            return null;
        }
        //}
    }

    @Override
    public Object getValueAt(int row, int col) {

        if (row > players.size()) {
            return "";
        }

        CPlayer b = GetPlayerAt(row);
        if (b == null) {
            return "";
        }

        switch (col) {
            case 0:
                return CChannel.GetFlagIcon(b.getCountry());
            case 1:
                return CChannel.smallranks[b.rank];
            case 2:
                if(Misc.getBotModeFromStatus(b.getStatus())){
                    return CChannel.botimg;
                }else if (Misc.getAccessFromStatus(b.getStatus())>0){
                    return CSmileyManager.adminimg;
                }else{
                    return null;
                }
            case 3:
                return b.name;
            case 4:
                return b.GetStatushtml();
            default:
                break;
        }

        return "";
    }

    /**
     * 
     * @param p
     */
    public void AddPlayer(CPlayer p) {
        if(p == null){
            return;
        }
        synchronized(players){
            if(players.contains(p)){
                return;
            }

            players.add(p);
        }
        fireTableDataChanged();
    }

    public void RemovePlayer(CPlayer b) {
        synchronized (players) {
            Iterator<CPlayer> i = players.iterator();
            while(i.hasNext()){
                CPlayer ba = i.next();
                if(ba == b){
                    i.remove();
                    break;
                }
            }
        }
        fireTableDataChanged();
//        synchronized (battles) {
//            battles.remove(b);
//        }
//        fireTableDataChanged();
//        return b;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
    @Override
    public Class getColumnClass(int column) {
        if(column <3){
            return ImageIcon.class;
        }else{
            return String.class;
        }
        /*Class returnValue;
        if ((column >= 0) && (column < getColumnCount())) {
            returnValue = getValueAt(0, column).getClass();
        } else {
            returnValue = Object.class;
        }
        return returnValue;*/
     }

}