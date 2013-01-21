/*
 * CBattleModel.java
 * 
 * Created on 22-Aug-2007, 22:14:44
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.darkstars.battlehub.battlemodels;

import org.darkstars.battlehub.CBattleInfo;
import org.darkstars.battlehub.CBattlePlayer;
import org.darkstars.battlehub.gui.CSpringBattleWindow;
import org.darkstars.battlehub.integration.CMeltraxLadder;
import org.darkstars.battlehub.framework.CPlayer;
import org.darkstars.battlehub.CSync;
import org.darkstars.battlehub.helpers.ColourHelper;
import org.darkstars.battlehub.LMain;
import org.darkstars.battlehub.framework.CEvent;
import org.darkstars.battlehub.CBaseModule;
import org.darkstars.battlehub.CUserSettings;
import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.darkstars.battlehub.framework.CEventTypes;
import org.darkstars.battlehub.framework.IBattleInfo;
import org.darkstars.battlehub.framework.ICentralClass;
import org.darkstars.battlehub.framework.Misc;


class CSpringTask extends Thread {

    public CSpringBattleWindow b;

    CSpringTask(CSpringBattleWindow battle) {
        b = battle;
    }
//    BufferedReader in = null;
//    PrintWriter out = null;

    @Override
    public void run() {
        
        b.battlemodel.GetMe().setReady(false);
        b.MyReadyStatusChanged();
        b.SendMyStatus();
        b.UpdateClientsNeeded = true;
//        SwingUtilities.invokeLater(doWorkRunnable);
        Process p;
        try {
            b.ingame = true;
            String sp = CUserSettings.GetValue("spring.command", "spring");
            p = Runtime.getRuntime().exec(sp + " script.txt"); //"settings.exe"); script.txt
            
            LMain.protocol.SetAway(false);
            LMain.protocol.SetIngame(true);
            
            InputStream out = p.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(out));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            
            p.waitFor();
            
        } catch (InterruptedException ex) {
            Logger.getLogger(CSpringTask.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            b.ingame = false;
            System.out.println(java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("CBattleWindow.SpringTask.VistaUACError"));
            ex.printStackTrace();
        }
        
//ch.status = Misc.setReadyStatusOfBattleStatus (b.ch.status,0);
//        boolean loop = true;
//        try {
//            in = new BufferedReader (new InputStreamReader (p.getInputStream ()));
//        } catch (Exception e) {
//            System.out.println ("ERROR E1="+e);
//            loop = false;
//        }
//        if(loop){
//            while (loop==true) {
//                try {
//                    this.sleep (100);
//                    String springOutput = in.readLine ();
//                    if (springOutput != null){// continue;
//
//                    System.out.println ("SPRING OUTPUT="+springOutput);
//                    //                    if (springOutput.equalsIgnoreCase("GAME OVER")) {
//                    //                        shutdown = true;
//                    //                        clientReference.shutdownSpring();
//                    //                        continue;
//                    //                    }
//                    }
//                } catch (Exception e) {
//                    System.out.println ("ERROR="+e);
//                    loop = false;
//                }
//                try{
//                    p.exitValue ();
//                    loop = false;
//                }catch(Exception e){
//                    //
//                }
//            }
//        }else{

        //b.MyReadyStatusChanged();
//        SwingUtilities.invokeLater( new Runnable() {
//            @Override
//            public void run() {
//                b.readyToggle.setSelected(false);
//                /*if(b.battlemodel.AmIHost()){
//                b.GameStartButton.setEnabled(true);
//                }*/
//            }
//        });
//        }
        LMain.protocol.SendTraffic("GETINGAMETIME");
//        System.out.println("loop ended");
        LMain.protocol.SetIngame(false);
//        b.ingame = false;
        // we keep it flagged as ingame untill the host sends his/her status
        // saying they're no longer ingame or we may end up relaunchign spring.'
        b.Redraw();
        
        if(b.info.isladdergame()){
            CMeltraxLadder.ReportGame(LMain.protocol.GetUsername(), LMain.protocol.Password());
        }
    }
}

/**
 *
 * @author Tom
 */
public class CSpringBattleModel extends CBaseModule implements IGUIBattleModel {
    CBattleInfo info;
    CSpringBattleWindow battlewindow;
    java.util.TreeMap<String, CBattlePlayer> players = new TreeMap<String, CBattlePlayer>();
    java.util.TreeMap<String, CBattlePlayer> AIplayers = new TreeMap<String, CBattlePlayer>();
    CBattlePlayer me = null;
    
    boolean locked = false;
    
    ArrayList<String> AIs = new ArrayList<String>();
    
//    ArrayList<Strin
    
    public CSpringBattleModel(){
    }
    
    /**
     * 
     * @param L
     */
    @Override
    public void Init(final ICentralClass LM){
        //
        this.LM = (LMain) LM;
        battlewindow = new CSpringBattleWindow((LMain) LM);
        
        //if()
    }

    /**
     * Sets the battle window proprty used to pass data and handle events
     * 
     * @param b an instance of CBattleWindow
     */
    @Override
    public void SetBattleWindow(CSpringBattleWindow b) {
        battlewindow = b;
    }

    @Override
    public void Update(){
        if(battlewindow != null){
            battlewindow.Update();
        }
    }
    
    @Override
    public void NewEvent(final CEvent e){
         if (e.IsEvent("UPDATEBATTLEINFO")) {
//            if(!CUnitSyncJNIBindings.loaded){
//                return;
//            }
            // UPDATEBATTLEINFO BATTLE_ID SpectatorCount locked maphash {mapname}
            int i = Integer.parseInt(e.data[1].trim());
            
            CBattleInfo b = (CBattleInfo) LMain.protocol.GetBattles().get(i);
            
            if (b != null) {
                boolean changed = false;
                if(!b.maphash.equalsIgnoreCase(e.data[4])){
                    b.SetMap(Misc.makeSentence(e.data, 5));
                    b.maphash = e.data[4]; //;//s[1];
                    battlewindow.MapChange();
                    changed = true;
                }
                boolean tempLocked = CSpringBattleWindow.InttoBool(Integer.valueOf(e.data[3]));
                if(b.locked != tempLocked){
                    b.locked = tempLocked;
                    changed = true;
                }
                int specCountTemp = Integer.valueOf(e.data[2]);
                if(b.spectatorcount != specCountTemp){
                    b.spectatorcount = specCountTemp;
                    changed = true;
                }
                if(changed){
                    battlewindow.UpdateBattle();
                }
            }
            //
        } else if (e.IsEvent("JOINEDBATTLE")) {
//            if(!CUnitSyncJNIBindings.loaded){
//                return;
//            }
            
            int i = Integer.parseInt(e.data[1]);
            if (i == info.GetID()) {
                CBattlePlayer pl = new CBattlePlayer();
                pl.setPlayername(e.data[2]);
                pl.setBattlestatus(0);
                pl.setPlayerdata(LMain.playermanager.GetPlayer(e.data[2]));
                pl.setColor(Color.BLUE);
                GetPlayers().put(e.data[2], pl);
                
                battlewindow.AddPlayer(pl);
                
                if(e.data[2].equals(LMain.protocol.GetUsername())){
                    //
                    SetMe(pl);
                }
//                b.AddPlayer(e.data[2]);
            }
            
        } else if (e.IsEvent("CLIENTBATTLESTATUS")) {
            
            // check if this user is already in the battle
            // if so then update details, else create a new CBattlePlayer object and put it in the treemap

            CBattlePlayer player = GetPlayer(e.data[1]);
            assert(player != null);

                
            final int bstatus = Integer.parseInt(e.data[2]);
            final Color c = ColourHelper.IntegerToColor(Integer.parseInt(e.data[3]));

            player.setBattlestatus(bstatus);
            player.setColor(c);


            if(player == GetMe()){
                battlewindow.UpdateMyUIControls();
            }

            if(AmIHost()){
                SendUpdateBattle();
            }

            battlewindow.Redraw();
        } else if (e.IsEvent("HOSTPORT")) {
            info.SetPort(Integer.parseInt(e.data[1]));
        } else if (e.IsEvent("CLIENTIPPORT")) {
            //  * CLIENTIPPORT username ip port
            GetPlayer(e.data[1]).port = Integer.parseInt(e.data[3]);

        } else if (e.IsEvent("REQUESTBATTLESTATUS")) {
            
            // send MYBATTLESTATUS
            
            int firstteam = GetFirstFreeTeam();
            int firstally = GetFirstFreeAlly();

            me.setAllyNo(firstally);
            me.setTeamNo(firstteam);
            me.setSpectator(false);
            me.setReady(false);
            
            battlewindow.cansend = true;
            
            battlewindow.SendMyStatus();
            
        } else if (e.IsEvent("UPDATEBOT")) {
            //UPDATEBOT BATTLE_ID name battlestatus teamcolor
            
            CBattlePlayer bp = GetAIPlayers().get(e.data[2]);
            
            if (bp == null) {
                return;
            }
            
            bp.setBattlestatus(Integer.valueOf(e.data[3]));
            bp.setColor(ColourHelper.IntegerToColor(Integer.valueOf(e.data[4])));
            battlewindow.Redraw();
            
        } else if (e.IsEvent("LEFTBATTLE")) {
            //LEFTBATTLE BATTLE_ID username
            
            if (Integer.parseInt(e.data[1]) == info.GetID()) {
                
                CBattlePlayer player = GetPlayer(e.data[2]);
                GetPlayers().remove(e.data[2]);

                if (info != null) {
                    if (info.Active()) {
                        SendUpdateBattle();
                        //Redraw();
                        battlewindow.Redraw();
                    }
                }
            }
            
        } else if (e.IsEvent("FORCEQUITBATTLE")) {
            LMain.Toasts.AddMessage(java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("CBattleWindow.you_were_kicked_from_the_battle"));
            battlewindow.LeaveBattle();
            
        } else if (e.IsEvent("BATTLECLOSED")) {
            if (info.GetID() == Integer.parseInt(e.data[1])) {
                LMain.Toasts.AddMessage(java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("CBattleWindow.Battle_closed"));
                battlewindow.LeaveBattle();
            }
            
        } else if (e.IsEvent("SETSCRIPTTAGS")){
            
            options.clear();
            String[] pairs = e.parameters.split("\t");
            
            for(int i = 0; i< pairs.length; i++){
                
                String[] pair = pairs[i].split("=");
                
                String key = pair[0];
                key = key.replaceAll("\\\\", "/");
                String value = pair[1];
                
                options.put(key, value);
                
            }
        }

    }
    
    @Override
    public void NewGUIEvent(final CEvent e){
        if(e.IsEvent(CEventTypes.BATTLEINFO_CHANGED)){
            battlewindow.Redraw();
        }

        
    }
    
    /**
     * 
     * @return An ArrayList containing copies of all the players and AIs in the correct order
     */
    @Override
    public List<CBattlePlayer> GetAllPlayers() {
        
        ArrayList<CBattlePlayer> a = new ArrayList<CBattlePlayer>();
        for (CPlayer p : info.GetPlayers()) {
            String z = p.name;
            CBattlePlayer q = GetPlayers().get(z);
            if(q == null) continue;
            CBattlePlayer r = new CBattlePlayer(q);
            a.add(r);
        }
        
        for (String z : AIs) {
            CBattlePlayer q = GetAIPlayers().get(z);
            if(q == null) continue;
            CBattlePlayer r = new CBattlePlayer(q);
            a.add(r);
        }
        return a;
    }

    @Override
    public TreeMap<String, CBattlePlayer> GetPlayers() {
        return players;
    }

    @Override
    public TreeMap<String, CBattlePlayer> GetAIPlayers() {
        return AIplayers;
    }

    @Override
    public CBattlePlayer GetPlayer(String player) {
        return players.get(player);
    }

    /**
     * Retrieves an AI battleplayer by name
     * @param ai - the name of the AI
     * @return the battleplayer object representing the AI or null
     */
    @Override
    public CBattlePlayer GetAI(String ai) {
        return AIplayers.get(ai);
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @return the username of the host
     */
    @Override
    public String GetHost() {
        if(info == null) return "";
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

    /**
     * 
     * @return an ordered ArrayList of nonAI players 
     */
    @Override
    public ArrayList<String> GetPlayerList() {
        ArrayList<CPlayer> tempPlayers = info.GetPlayers();
        ArrayList<String> names = new ArrayList<String>();
        for(CPlayer p : tempPlayers){
            //
            names.add(p.name);
        }
        return names;
    }

    /**
     * 
     * @return an ordered ArrayList of AIs
     */
    @Override
    public ArrayList<String> GetAIList() {
        return AIs;
    }

    @Override
    public void RemoveAI(String name) {
        AIs.remove(name);
        AIplayers.remove(name);
    }

    @Override
    public void AddAI(String name, CBattlePlayer p) {
        AIplayers.put(name, p);
        AIs.add(name);
    }

    @Override
    public CBattlePlayer GetMe() {
        return me;
    }

    @Override
    public void SetMe(CBattlePlayer p) {
        me = p;
    }

    @Override
    public int GetFirstFreeTeam() {
//        if(AmIHost()){
//            return 0;
//        }
        int firstteam = 0;
        List<CBattlePlayer> a = GetAllPlayers();
        Iterator<CBattlePlayer> i = a.iterator();
        while (i.hasNext()) {
            CBattlePlayer pl = i.next();
            if (pl.isSpec()) {
                continue;
            }
//            if (pl.getPlayername().equals(LM.protocol.GetUsername())) {
////                if(pl.getAI().isEmpty()){
//                    break;
////                }
//            }
            if (pl.getTeamNo() == firstteam) {
                firstteam++;
                i = a.iterator();
            }
        }
        return firstteam;
    }

    @Override
    public int GetFirstFreeAlly() {
//        if(AmIHost()){
//            return 0;
//        }
        int firstally = 0;
        List<CBattlePlayer> a = GetAllPlayers();
        Iterator<CBattlePlayer> i = a.iterator();
        while (i.hasNext()) {
            CBattlePlayer pl = i.next();
            if (pl.isSpec()) {
                continue;
            }
//            if (pl.getPlayername().equals(LM.protocol.GetUsername())) {
//                break;
//            }
            if (pl.getAllyNo() == firstally) {
                firstally++;
                i = a.iterator();
            }
        }
        return firstally;
    }
    
    @Override
    public void CheckSync() {
        int synced = 1;
        if (!AmIHost()) {
            String m = info.GetMod();
            String mh = info.modhash;
            String mh2 = CSync.GetModHashbyName(m);
            if (info == null) {
                synced = 0;
                System.out.println(java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("CBattleWindow.mhinfo_==_null"));
            } else if (m == null) {
                synced = 0;
                System.out.println(java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("CBattleWindow.mh2_==_null"));
            } else if (mh == null) {
                synced = 0;
                System.out.println(java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("CBattleWindow.mh_==_null"));
            } else if (mh2 == null) {
                synced = 0;
                System.out.println(java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("CBattleWindow.mh2_==_null"));
            } else if (mh2.equals("")) {
                synced = 2;
                //System.out.println ("mh2 : "+mh2+" and mh : " + mh);
            }
        }
        GetMe().SetSync(synced);
//        me.setBattlestatus(Misc.setSyncOfBattleStatus(me.getBattlestatus(), synced));
    }

    @Override
    public void Host(IBattleInfo i) {
        info = (CBattleInfo) i;
        me = new CBattlePlayer();
        LMain.core.AddModule(battlewindow);
        battlewindow.HostGame(info);
    }

    @Override
    public void Join(IBattleInfo i) {
        info = (CBattleInfo) i;
        me = new CBattlePlayer();
        LMain.core.AddModule(battlewindow);
        battlewindow.JoinBattle(info);
    }

    @Override
    public void Exit() {
        LMain.protocol.SendTraffic("LEAVEBATTLE");
        GetPlayers().clear();
        
        if (info != null) {
            info.SetActive(false);
            info = null;
        }
        
        LMain.core.RemoveModule(this);
        LMain.core.RemoveModule(battlewindow);
        
        LM.battleModel = null;
        battlewindow = null;
        
    }

    @Override
    public CBattleInfo GetInfo() {
        return info;
    }

    @Override
    public boolean Start() {
        String s = battlewindow.GetScript(false);
        //if(Misc.isWindows()){
        // write out script
        try {
            PrintStream out = new PrintStream(new BufferedOutputStream(new FileOutputStream("script.txt", false))); //"file:"springpath.getText()+
            out.print(s);
            out.close();
            new CSpringTask(battlewindow).start();
            return true;
            //java.awt.EventQueue.invokeLater (new CSpringTask (this));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
        if(AmIHost()){
            //* UPDATEBATTLEINFO BATTLE_ID SpectatorCount locked maphash {mapname}
            String s = "UPDATEBATTLEINFO ";
            //s += info.GetID() + " ";
            info.spectatorcount = battlewindow.GetSpectatorCount();
            s += info.spectatorcount + " ";
            if(IsLocked()){
                s += "1 ";
            }else{
                s += "0 ";
            }
            s += info.maphash + " ";
            String m = info.GetMap();
            s += m;
            LMain.protocol.SendTraffic(s);
        }
    }

    @Override
    public void SendChatMessage(String message) {
        LMain.protocol.SendTraffic("SAYBATTLE  " + message);
    }

    @Override
    public void SendChatActionMessage(String message) {
        LMain.protocol.SendTraffic("SAYBATTLEEX  " + message);
    }

    private Map<String, Object> options = new TreeMap<String, Object>();
    
    @Override
    public Map<String, Object> GetOptions() {
        return options;
    }

    @Override
    public Object GetOption(String option) {
        return options.get(option);
    }

    @Override
    public boolean HasOption(String option) {
        return options.containsKey(option);
    }

    @Override
    public void PutOption(String option, Object value) {
        if(value == null){
            return;
        }
        Object o = options.get(option);
        
        if(o != null){
            String v = "";
            if((o.getClass() == boolean.class)||(o.getClass() == Boolean.class)){
                v = ""+((o.toString().equals("true"))? 1 : 0);
            }else{
                v = o.toString();
            }
            if(v.equals(value)){
                return;
            }
        }
        options.put(option, value);
        if(battlewindow != null){
            battlewindow.UpdateBattleClients();
        }
    }

    @Override
    public void RemoveOption(String option) {
        if(options.containsKey(option)){
            options.remove(option);
            if(battlewindow != null){
                battlewindow.UpdateBattleClients();
            }
        }
    }

    @Override
    public void RemoveAllOptions() {
        options.clear();
        if(battlewindow != null){
            battlewindow.UpdateBattleClients();
        }
    }
    //
}
