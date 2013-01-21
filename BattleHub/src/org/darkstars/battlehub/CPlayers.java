/*
 * CPlayers.java
 *
 * Created on 28 May 2006, 12:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.darkstars.battlehub;

import org.darkstars.battlehub.framework.CPlayer;
import org.darkstars.battlehub.framework.CEvent;
import org.darkstars.battlehub.framework.IModule;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import org.darkstars.battlehub.framework.CCore;
import org.darkstars.battlehub.framework.CEventTypes;
import org.darkstars.battlehub.framework.ICentralClass;
import org.darkstars.battlehub.framework.Misc;

/**
 *
 * @author AF
 */

public class CPlayers implements IModule {
    public ArrayList<CPlayer> players;
    public ArrayList<String> activeplayers;
    public ArrayList<String> inactiveplayers;
    public TreeMap<String,CPrivateMsgWindow> pmwindows;
    public ICentralClass LM;
    
    /**
     * Creates a new instance of CPlayers
     */
    public CPlayers () {
        //
        players = new ArrayList<CPlayer>();
        activeplayers = new ArrayList<String>();
        inactiveplayers = new ArrayList<String>();
        pmwindows = new TreeMap<String,CPrivateMsgWindow>();
    }
    
    public  CPlayer GetPlayer (String name){
        synchronized(players){
            Iterator<CPlayer> i = players.iterator ();
            while(i.hasNext ()){
                CPlayer p = i.next ();
                if(p.name.equalsIgnoreCase (name)){
                    return p;
                }
            }
        }
        return null;
    }
    
    public String GetPlayerStatus (String name){
        
        CPlayer p = GetPlayer(name);
        if(p == null){
            return "-";
        } else {
            return p.GetStatushtml();
        }
    }
    
    public boolean GetPlayerInGame(String playername){
        CPlayer p = GetPlayer (playername);
        
        if(p == null){
            return false;
        }
        
        if(Misc.getInGameFromStatus (p.getStatus())==1){
            return true;
        }else{
            return false;
        }
    }
    
    @Override
    public void Update (){
    }
    
    
    @Override
    public void NewEvent (final CEvent e){
        if(e.IsEvent("ADDUSER")){
            activeplayers.add (e.GetData(1));
            if(inactiveplayers.contains (e.GetData(1))==false){
                inactiveplayers.remove (e.GetData(1));
                
                CPlayer p = new CPlayer (e.GetData(1));
                p.setCpu(e.data[3] + "Mhz");
                p.setCountry(e.data[2]);
                players.add (p);
                
                LM.GetCore().AddModule(p);
                CEvent ne = new CEvent(CEventTypes.NEWUSRADDED);
                ne.object = p;
                LM.GetCore().NewEvent(ne);
            }
        }else if(e.IsEvent("REMOVEUSER")){
            activeplayers.remove (e.GetData(1));
            inactiveplayers.add (e.GetData(1));
        }else if(e.IsEvent("SAIDPRIVATE")){
            if(this.pmwindows.containsKey (e.GetData(1))){
                pmwindows.get (e.data[1]).Open ();
                //Open ();
            }else{
                CPrivateMsgWindow privmsg = new CPrivateMsgWindow ();
                privmsg.Init (LM,GetPlayer (e.GetData(1)));
                LM.GetCore().AddModule(privmsg);
                pmwindows.put (e.GetData(1),privmsg);
                privmsg.Open ();
                //privmsg.NewEvent (e);
            }
        }
        
    }
    
    @Override
    public  void NewGUIEvent (final CEvent e){
        if(e.IsEvent (CEventTypes.OPENPRIVATEMSG)){
            if(e.FieldCount() == 1){
                return;
            }
            if(pmwindows.containsKey (e.GetData(1))){
                pmwindows.get (e.GetData(1)).Open ();
            }else{
                CPrivateMsgWindow privmsg = new CPrivateMsgWindow ();
                privmsg.Init (LM,GetPlayer (e.GetData(1)));
                LM.GetCore().AddModule(privmsg);
                pmwindows.put (e.GetData(1),privmsg);
                privmsg.Open ();
            }
        }
    }

    @Override
    public void Init(ICentralClass L) {
        LM =  L;
        L.GetCore().AddModule(this, CCore.RCVD_SERVER_TRAFFIC);
        L.GetCore().AddModule(this, CCore.LOBBY_GUI_EVENTS);
    }

    @Override
    public void OnRemove() {
    }

    @Override
    public void OnEvent(CEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void OnRemove(int channel) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}
