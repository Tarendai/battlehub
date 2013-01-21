/*
 * CTASServerProtocol.java
 *
 * Created on 10 June 2007, 01:23
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.darkstars.battlehub.protocol.tasserver;

import aflobby.CUnitSyncJNIBindings;
import org.darkstars.battlehub.CBattleInfo;
import org.darkstars.battlehub.battlemodels.CSpringBattleModel;
import org.darkstars.battlehub.CChannelPassword;
import org.darkstars.battlehub.framework.CEvent;
import org.darkstars.battlehub.battlemodels.CGlestBattleModel;
import org.darkstars.battlehub.framework.CPlayer;
import org.darkstars.battlehub.JRegView;
import org.darkstars.battlehub.LMain;
import org.darkstars.battlehub.Main;
import javax.swing.SwingUtilities;
import org.darkstars.battlehub.CTraffic;
import org.darkstars.battlehub.framework.CConnection;
import org.darkstars.battlehub.framework.CEventTypes;
import org.darkstars.battlehub.framework.IBattleInfo;
import org.darkstars.battlehub.framework.ICentralClass;
import org.darkstars.battlehub.framework.Misc;
import org.darkstars.battlehub.framework.protocol.tasserver.CBasicTASServerProtocol;
import org.darkstars.battlehub.gui.CView;
import org.darkstars.battlehub.gui.WarningWindow;

/**
 *
 * @author Tom
 */
public class CTASServerProtocol extends CBasicTASServerProtocol {

    private LMain LM;

    @Override
    public void Init(ICentralClass L) {
        LM = (LMain) L;
        super.Init(L);
    }

    @Override
    public void NewEvent(final CEvent e) {
        if (e.IsEvent("TASSERVER")) {

            String s = e.data[2];
            if (s.equals("*") == false) {

                if (Main.ignorespringversion == false) {
                    if (CUnitSyncJNIBindings.IsLoaded()) {
                        String myversion = CUnitSyncJNIBindings.GetSpringVersion();
                        if (myversion.equalsIgnoreCase(s) == false) {
                            SendTraffic("REQUESTUPDATEFILE Spring " + myversion);//
                        }
                    }
                }

            }
            CEvent e2 = new CEvent(CEventTypes.CONNECTED);
            LMain.core.NewGUIEvent(e2);
        } else if (e.IsEvent("OFFERFILE")) {
            //OFFERFILE options {filename} {url} {description}

            final String[] t = e.parameters.split("\t");
            System.out.println("OFFERFILE " + e.parameters + " || " + t[1]);
            javax.swing.SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    new WarningWindow("The server is offering us this file: <a href=\"" + t[1] + "\">" + t[1] + "</a><br><br><br>" + t[2],
                            "Server Offering File").setVisible(true);
                }
            });
            Disconnect();
        } else if (e.IsEvent("SERVERMSGBOX")) {
            //System.out.println("SERVERMSGBOX "+e.parameters);
            javax.swing.SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    new WarningWindow("Server Message: " + e.parameters,
                            "Server Message").setVisible(true);
                }
            });
        } else if (e.IsEvent("RING")) {
            String ringer = e.parameters;
            String s = "<img src=\"" + LMain.GetAbsoluteLobbyFolderPathStatic() + "/images/UI/alarm.gif\"></img><br><br><font face=\"Arial, Helvetica,Sans Serif\"> User: " + ringer + " wants your attention</font>";
            final WarningWindow w = new WarningWindow(s, ringer + " Ringing", true);
            java.awt.EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    w.setVisible(true);
                }
            });
        } else if (e.IsEvent("JOINFAILED")) {
            if (e.parameters.contains("Wrong key")) {
                javax.swing.SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        CChannelPassword c = new CChannelPassword(LM, e.data[1]);
                        c.setVisible(true);
                    }
                });
            } else {
                LMain.Toasts.AddMessage("ChannelJoin failed: " + e.parameters);
            }
        //Toasts.AddMessage (Misc.makeSentence (e.data,0));
        } else if (e.IsEvent("OPENBATTLE")) {

            // create a battleinfo structure and pass it to the battle window and open it.
            Integer i = new Integer(e.data[1].trim());
            IBattleInfo b = GetBattles().get(i);
            if (b != null) {
                CPlayer p = LMain.playermanager.GetPlayer(GetUsername());
                b.SetHost(p);

                if (b.getEngine().equals("spring")) {
                    b.SetPort(8452);
                    LM.battleModel = new CSpringBattleModel();
                } else if (b.getEngine().equals("glest")) {
                    b.SetPort(61357);
                    LM.battleModel = new CGlestBattleModel();
                } else if (b.getEngine().equals("wz2100")) {
                    b.SetPort(9999);
                    LM.battleModel = new org.darkstars.battlehub.battlemodels.CWarZoneBattleModel();
                }/* else if (b.getEngine().equals("ta3d")) {
                    b.SetPort(61357);
                    LM.battleModel = new CGlestBattleModel();
                }*/

                LMain.core.AddModule(LM.battleModel);

                LM.battleModel.Init(LM);
                LM.battleModel.Host(b);
            }
        } else if (e.IsEvent("JOINBATTLE")) {
            //if(!CUnitSyncJNIBindings.loaded){
            //    return;
            //}
            int i = Integer.parseInt(e.data[1]);
            CBattleInfo b = (CBattleInfo) GetBattles().get(i);
            // JOINBATTLE BATTLE_ID startingmetal startingenery maxunits startpos gameendcondition limitdgun diminishingMMs ghostedBuildings hashcode
            // JOINBATTLE BATTLE_ID hashcode
            /*b.metal = Integer.parseInt(e.data[2]);
            b.energy = Integer.parseInt(e.data[3]);
            b.maxunits = Integer.parseInt(e.data[4]);
            b.startPos = Integer.parseInt(e.data[5]);
            b.gameEndCondition = Integer.parseInt(e.data[6]);
            b.limitDGun = CBattleWindow.InttoBool(Integer.parseInt(e.data[7]));
            b.diminishingMMs = CBattleWindow.InttoBool(Integer.parseInt(e.data[8]));
            b.ghostedBuildings = CBattleWindow.InttoBool(Integer.parseInt(e.data[9]));*/
            b.modhash = e.data[2];

            if (b != null) {

                if (b.getEngine().equals("spring")) {
                    b.SetPort(8452);
                    LM.battleModel = new CSpringBattleModel();
                } else if (b.getEngine().equals("glest")) {
                    b.SetPort(61357);
                    LM.battleModel = new CGlestBattleModel();
                } else if (b.getEngine().equals("wz2100")) {
                    b.SetPort(9999);
                    LM.battleModel = new org.darkstars.battlehub.battlemodels.CWarZoneBattleModel();
                }

                LMain.core.AddModule(LM.battleModel);

                LM.battleModel.Init(LM);
                LM.battleModel.Join(b);
            }
        } else if (e.IsEvent("JOINEDBATTLE")) {
            //if(!CUnitSyncJNIBindings.loaded){
            //    return;
            //}
            Integer i = new Integer(e.data[1]);
            if (i != null) {
                CBattleInfo b = (CBattleInfo) GetBattles().get(i);
                if (b != null) {
                    CPlayer p = LMain.playermanager.GetPlayer(e.data[2]);
                    b.AddPlayer(p);
                }
            }
        } else if (e.IsEvent("LEFTBATTLE")) {
            //if(!CUnitSyncJNIBindings.loaded){
            //    return;
            //}
            Integer i = new Integer(e.data[1]);
            if (i != null) {
                CBattleInfo b = (CBattleInfo) GetBattles().get(i);
                if (b != null) {
                    CPlayer p = LMain.playermanager.GetPlayer(e.data[2]);
                    b.RemovePlayer(p);
                }
            }
        } else if (e.IsEvent("JOINBATTLEFAILED")) {
            LMain.Toasts.AddMessage(Misc.makeSentence(e.data, 0));
        } else if (e.IsEvent("OPENBATTLEFAILED")) {
            LMain.Toasts.AddMessage(Misc.makeSentence(e.data, 0));
        } else if (e.data[0].equalsIgnoreCase("BATTLEOPENED")) {
            // Add!!!!!
            //if(!CUnitSyncJNIBindings.loaded){
            //    return;
            //}
            if (e.data[1] == null) {
                System.out.println("e.data[1] == null for:" + Misc.makeSentence(e.data, 0));
                return;
            }
            /*Integer i = new Integer (e.data[1].trim ());// Integer.getInteger(e.data[1].trim());
            if(i == null){
            LM.Toasts.AddMessage ("i == null for battles \""+e.data[1].trim ()+" \" in ::\n"+Misc.makeSentence (e.data,0));
            return;
            }*/
            /*Integer i = new Integer (e.data[1].trim ());// Integer.getInteger(e.data[1].trim());
            if(i == null){
            LM.Toasts.AddMessage ("i == null for battles \""+e.data[1].trim ()+" \" in ::\n"+Misc.makeSentence (e.data,0));
            return;
            }*/
            int i = Integer.parseInt(e.data[1]);

            //  BATTLEOPENED BATTLE_ID type natType founder IP port maxplayers passworded rank maphash {mapname} {title} {modname}
            final CBattleInfo b = new CBattleInfo(LM);
            //if(e.data[2].equalsIgnoreCase ("1")) return;
            b.type = Integer.parseInt(e.data[2]);
            b.natType = Integer.parseInt(e.data[3]);
            CPlayer host = LMain.playermanager.GetPlayer(e.data[4]);
            b.SetHost(host);
            b.ip = e.data[5];
            b.SetPort(Integer.parseInt(e.data[6]));
            b.maxplayers = Integer.parseInt(e.data[7]);
            b.SetPassworded(Misc.strToBool(e.data[8]));
            b.rank = Integer.parseInt(e.data[9]);
            b.maphash = e.data[10];


            String[] data = Misc.makeSentence(e.data, 0).split("\t");

            b.SetDescription(data[1]);

            if (data[2].startsWith("glest")) {
                b.setEngine("glest");
            } else if (data[2].startsWith("wz2100")) {
                //
                b.setEngine("wz2100");
            } else if (data[2].startsWith("ta")) {
                //
                b.setEngine("ta");
            } else if (data[2].startsWith("ta3d")) {
                //
                b.setEngine("ta3d");
            } else {
                b.setEngine("spring");
            }


            String m = Misc.makeSentence(data[0].split(" "), 11);
            b.SetMap(m);
            b.SetMod(data[2]);

//            if(b.GetMod().startsWith("glest:")){
//                //
//                b.setEngine("glest");
//            }

            b.SetID(i);
            GetBattles().put(new Integer(i), b);
            CEvent ev = new CEvent(CEvent.ADDEDBATTLE);
            ev.object = b;
            LMain.core.NewEvent(ev);

        }
        if (e.IsEvent("BATTLECLOSED")) {
            //BATTLEOPENED
            // Add!!!!!
            //if(!CUnitSyncJNIBindings.loaded){
            //    return;
            //}
            if (e.data[1] == null) {
                return;
            }
            int k = Integer.parseInt(e.data[1].trim());
            GetBattles().remove(k); // BATTLE_ID username
        }
        super.NewEvent(e);
    }

    @Override
    public void NewGUIEvent(final CEvent e) {
        if (e.IsEvent(CEvent.DISCONNECT) || e.IsEvent(CEvent.DISCONNECTED)) {
            LMain.protocol = null;
        }else if (e.IsEvent(CEvent.TOGGLERAWTRAFFIC)) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    CTraffic traffic = (CTraffic) CConnection.getTraffic();
                    traffic.setVisible(!traffic.isVisible());
                }
            });
        }
        
        super.NewGUIEvent(e);
        
    }
    
    public CView GetRegisterView() {
        return new JRegView(LM);
    }
}
