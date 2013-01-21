/*
 * CBattleInfo.java
 *
 * Created on 25 June 2006, 21:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.darkstars.battlehub;

import org.darkstars.battlehub.framework.IBattleInfo;
import org.darkstars.battlehub.framework.CPlayer;
import org.darkstars.battlehub.framework.CEvent;
import java.util.ArrayList;

/**
 *
 * @author AF
 */
public class CBattleInfo implements IBattleInfo {

    LMain LM;

    /**
     * Creates a new instance of CBattleInfo
     * @param L 
     */
    public CBattleInfo(LMain L) {
        LM = L;
    }
    //public String name="";
    private ArrayList<CPlayer> Players = new ArrayList<CPlayer>();
    private String mapname = "";
    private String modname = "";
    public String modhash = "";
    public String maphash = "";
    public boolean locked = false;
    public boolean isprivate = false;
    private boolean isladdergame = false;
    private CLadderProperties ladderproperties = null;
    public int maxplayers = 4;
    private boolean started;
    private boolean passworded;
    private String description = "";
    private CPlayer host = null;
    public int natType = 0;
    
    /**
     * If 0, no rank limit is set. If 1 or higher, only players with this rank
     * (or higher) can join the battle (Note: rank index 1 means seconds rank,
     * not the first one, since you can't limit game to players of the first
     * rank because that means game is open to all players and you don't have to limit it in that case).
     */
    public int rank;
    
    public int spectatorcount = 0;
    private int ID;
    private boolean active = false;
    public String ip = "localhost";
    private int port = 8452;
    public int type = 0;
    //public int startPos; // 0 = fixed, 1 = random, 2 = choose in game
    private String engine = "spring"; // assume the engine is spring for now

    @Override
    public boolean equals(Object i){
        //
        if(i.getClass() == CBattleInfo.class){
            CBattleInfo info = (CBattleInfo) i;
            return info.GetID() == this.GetID();
        }else{
            return super.equals(i);
        }
    }

    @Override
    public boolean isJoinable() {
        if (Main.chat_only_mode) {
            return false;
        }
        return org.darkstars.battlehub.CContentManager.SupportsEngine(engine);

    }

    @Override
    public String getEngine() {
        return engine;
    }

    @Override
    public void setEngine(String engine) {
        this.engine = engine;
    }

    @Override
    public String toString() {
        //
        return description;
    }

    @Override
    public boolean Active() {
        return active;
    }

    @Override
    public void SetActive(boolean isactive) {
        active = isactive;
    }

    private void Check() {
        if (Active()) {
            CEvent e = new CEvent(CEvent.BATTLEINFO_CHANGED);
            LMain.core.NewGUIEvent(e);
        }
    }

    @Override
    public ArrayList<CPlayer> GetPlayers() {
        //
        ArrayList<CPlayer> newArrayList = new ArrayList<CPlayer>();
        newArrayList.addAll(Players);
        return newArrayList;
    }

    @Override
    public String GetPlayerNames() {
        String s = "";
        boolean first = true;
        for (CPlayer p : Players) {
            //
            if (!first) {
                s += " ,";
            } else {
                first = false;
            }
            s += p.name;
        //
        }
        return s;
    }

    @Override
    public CPlayer GetHost() {
        return host;
    }

    @Override
    public void SetHost(CPlayer host) {
        this.host = host;
        AddPlayer(host);
    }

    @Override
    public void SetMap(String map) {
        if (mapname.equals(map) == false) {
            mapname = map;
            Check();
        }
    }

    @Override
    public String GetMap() {
        return mapname;
    }

    @Override
    public void SetMod(String mod) {
        if (modname.equals(mod) == false) {
            modname = mod;
            Check();
        }
    }

    @Override
    public String GetMod() {
        return modname;
    }

    @Override
    public void SetPassworded(boolean pass) {
        if (passworded != pass) {
            passworded = pass;
            Check();
        }
    }

    @Override
    public boolean IsPassworded() {
        return passworded;
    }

    @Override
    public void SetID(int i) {
        ID = i;
        Check();
    }

    @Override
    public int GetID() {
        return ID;
    }

    @Override
    public boolean IsIngame() {
        return started;
    }

    @Override
    public void SetIngame(boolean ingame) {
        if (started != ingame) {
            started = ingame;
            Check();
        }
    }

    @Override
    public void SetDescription(String desc) {
        if (desc.equalsIgnoreCase(description) == false) {
            description = desc;
            Check();
        }
    }

    @Override
    public String GetDescription() {
        return description;
    }

    @Override
    public void AddPlayer(CPlayer p) {
        if (Players.contains(p) == false) {
            Players.add(p);
            Check();
        }
    }

    @Override
    public void RemovePlayer(CPlayer p) {
        Players.remove(p);
        Check();
    }

    public boolean isladdergame() {
        return isladdergame;
    }

    public void setIsladdergame(boolean isladdergame) {
        this.isladdergame = isladdergame;
    }

    public CLadderProperties getLadderproperties() {
        if (isladdergame()) {
            return null;
        }
        return ladderproperties;
    }

    public void setLadderproperties(CLadderProperties ladderproperties) {
        this.ladderproperties = ladderproperties;
        setIsladdergame(true);
    }

    @Override
    public int GetPort() {
        return port;
    }

    @Override
    public void SetPort(int port) {
        this.port = port;
    }
}
