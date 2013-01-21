/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.darkstars.battlehub;

import org.darkstars.battlehub.framework.CErrorWindow;
import org.darkstars.battlehub.framework.CEvent;
import org.darkstars.battlehub.framework.CEventTypes;

/**
 *
 * @author AF-Standard
 */
public class CLoginTask extends Thread {

    public LMain LM;
    public String username;
    public String password;
    public String server;
    public int port;
    CChannelView CV;
    CLoginProgress lp;
    public boolean sha=false;

    CLoginTask(LMain L,boolean sha_hash) {
        LM = L;
        sha = sha_hash;
    }

    @Override
    public void run() {
        CErrorWindow.username = username;
        if (!Main.chat_only_mode) {
            String s = CUserSettings.GetValue("springpath", "");
            if(s.equals("")==false){
                CSync.Setup(LM);
            }
        }
        
        CEvent e = new CEvent(CEventTypes.LOGINPROGRESS + " " + java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("CLoginTask._75_Connecting"));
        LM.GetCore().NewGUIEvent(e);

        CV = new CChannelView(LM);
        LM.AddView(CV, false);
//        {
//            String url = "http://www.darkstars.co.uk/ahem/cheyenne/?tag="+username+"."+CUpdateHandler.versionNumber;
//            Misc.getURLContent(url, "");
//        }
        if (LM.GetProtocol().Connect(server, port)) {
            CEvent e2 = new CEvent(CEventTypes.LOGINPROGRESS + " 78 Connected, Logging in");
            LM.GetCore().NewGUIEvent(e2);
            LM.GetProtocol().Login(username, password,sha);
        } else {
            //LM.Toasts.AddMessage("error could not connect");
            CEvent e2 = new CEvent(CEventTypes.FAILEDCONNECTION);
            LM.GetCore().NewEvent(e2);
            LM.RemoveView(CV);
        }
    }
}