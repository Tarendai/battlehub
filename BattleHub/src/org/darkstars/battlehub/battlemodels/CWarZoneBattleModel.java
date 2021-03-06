/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.darkstars.battlehub.battlemodels;

import org.darkstars.battlehub.CBattleInfo;
import org.darkstars.battlehub.LMain;
import org.darkstars.battlehub.gui.CBasicBattleWindow;
import org.darkstars.battlehub.framework.CPlayer;
import org.darkstars.battlehub.framework.CEvent;
import org.darkstars.battlehub.CBaseModule;
import org.darkstars.battlehub.CUserSettings;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.darkstars.battlehub.framework.IBattleInfo;
import org.darkstars.battlehub.framework.Misc;


class CWarZoneTask extends Thread {

    public CWarZoneBattleModel b;
    

    CWarZoneTask(CWarZoneBattleModel battle) {
        b = battle;
    }

    @Override
    @SuppressWarnings("static-access")
    public void run() {
        
        Process p;
        try {
            //b.ingame = true;
            String sp = CUserSettings.GetValue("wz2100.command", "warzone2100 ");
            if(b.AmIHost()){
                sp += " --host";
            }else{
                sp += " --join ";
                sp += b.GetInfo().ip;
            }
            
            p = Runtime.getRuntime().exec(sp);
            
            sleep(3000);
        
        
            b.LM.protocol.SetAway(false);
            b.LM.protocol.SetIngame(true);

            InputStream out = p.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(out));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            try {
                p.waitFor();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } catch ( InterruptedException ex ) {
            Logger.getLogger(CWarZoneTask.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            //b.ingame = false;
            System.out.println(java.util.ResourceBundle.getBundle("aflobby/languages").getString("CBattleWindow.SpringTask.VistaUACError"));
            ex.printStackTrace();
            return;
        }

        b.LM.protocol.SetIngame(false);
//        }
        //b.LM.protocol.SendTraffic("GETINGAMETIME");
//        System.out.println("loop ended");
        //b.LM.protocol.SetIngame(false);
        b.Exit();
//        b.ingame = false;

        
        //if(b.info.isladdergame()){
        //    CMeltraxLadder.ReportGame(b.LM.protocol.GetUsername(), b.LM.protocol.Password());
        //}
    }
}

/**
 *
 * @author tarendai
 */
public class CWarZoneBattleModel extends CBaseModule implements IBattleModel {

    private CBattleInfo info = null;
    private CBasicBattleWindow battlewindow = null;
    private boolean ingame = false;
    
    public CWarZoneBattleModel(){
        
    }
    
    @Override
    public void NewEvent(final CEvent e){
        //
        if (e.IsEvent("CLIENTSTATUS")) {
            if (!AmIHost()) {
                if (info != null) {
                    if (e.data[1].equalsIgnoreCase(info.GetHost().name)) {
                        int status = Integer.parseInt(e.data[2]);
                        if (Misc.getInGameFromStatus(status) > 0) {
                            if (ingame == false) {
                                Start();
                            }
                        } else {
                            ingame = false;
                        }
                    }
                }
            }
        } else if (e.IsEvent("FORCEQUITBATTLE")) {
            LMain.Toasts.AddMessage(java.util.ResourceBundle.getBundle("aflobby/languages").getString("CBattleWindow.you_were_kicked_from_the_battle"));
            Exit();
        } else if (e.data[0].equalsIgnoreCase("BATTLECLOSED")) {
            if(info != null){
                if (info.GetID() == Integer.parseInt(e.data[1])) {
                    LMain.Toasts.AddMessage(java.util.ResourceBundle.getBundle("aflobby/languages").getString("CBattleWindow.Battle_closed"));
                    Exit();
                }
            }
        } else if (e.IsEvent("LEFTBATTLE")) {
            //LEFTBATTLE BATTLE_ID username
            if (Integer.parseInt(e.data[1]) == info.GetID()) {
                battlewindow.RemovePlayer(e.data[2]);
            }
        } else if (e.IsEvent("JOINEDBATTLE")) {
            int i = info.GetID();
            int j = Integer.parseInt(e.data[1]);
            if (i == j) {
                //JOINEDBATTLE BATTLE_ID username
                CPlayer p = LMain.playermanager.GetPlayer(e.data[2]);
                battlewindow.AddPlayer(p);
            }
        }
        battlewindow.NewEvent(e);
    }

    @Override
    public ArrayList<String> GetPlayerList() {
        return null;
    }

    @Override
    public String GetHost() {
        return info.GetHost().name;
    }

    /**
     * 
     * @return a boolean representing wether you are the host of this battle
     */
    @Override
    public boolean AmIHost(){
        
        if(info == null){
            return false;
        }
        
        return GetHost().equalsIgnoreCase(LMain.protocol.GetUsername());
    }

    @Override
    public void Host(IBattleInfo i) {
        info = (CBattleInfo) i;
        
        battlewindow = new CBasicBattleWindow(LM,this);
        battlewindow.Init(LM);
        
        CPlayer p = LMain.playermanager.GetPlayer(LMain.protocol.GetUsername());
        
        battlewindow.AddPlayer(p);
        
    }

    @Override
    public void Join(IBattleInfo i) {
        info = (CBattleInfo) i;
        
        battlewindow = new CBasicBattleWindow(LM,this);
        battlewindow.Init(LM);
        
        for(CPlayer p : i.GetPlayers()){
            battlewindow.AddPlayer(p);
        }
        
    }

    @Override
    public void Exit() {
        LMain.protocol.SendTraffic("LEAVEBATTLE");
        
        if (info != null) {
            info.SetActive(false);
            info = null;
        }
        battlewindow.dispose();
        battlewindow = null;
        
        LMain.core.RemoveModule(LM.battleModel);
        LM.battleModel = null;
    }

    @Override
    public CBattleInfo GetInfo() {
        return info;
    }

    @Override
    public boolean Start() {
        
        CWarZoneTask g = new CWarZoneTask(this);
        g.start();
        
        return true;
    }
    
    @Override
    public void SetLocked(boolean locked) {
        if(info.locked != locked){
            info.locked = locked;
            SendUpdateBattle();
        }
    }

    @Override
    public boolean IsLocked() {
        return info.locked;
    }
    
    @Override
    public void SendUpdateBattle() {
        //* UPDATEBATTLEINFO BATTLE_ID SpectatorCount locked maphash {mapname}
        String s = "UPDATEBATTLEINFO 0 ";
        
        if(IsLocked()){
            s += "1 ";
        }else{
            s += "0 ";
        }
        
        s += "0 nomap";

        LMain.protocol.SendTraffic(s);
    }

    @Override
    public void SendChatMessage(String message) {
        LMain.protocol.SendTraffic("SAYBATTLE  " + message);
    }

    @Override
    public void SendChatActionMessage(String message) {
        LMain.protocol.SendTraffic("SAYBATTLEEX  " + message);
    }

    @Override
    public Map<String, Object> GetOptions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object GetOption(String option) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean HasOption(String option) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void PutOption(String option, Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void RemoveOption(String option) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void RemoveAllOptions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    

}
