/*
 * LMain.java
 *
 * Created on 07 January 2008, 11:51
 */
package org.darkstars.battlehub;

import javax.swing.JComponent;
import javax.swing.JLabel;
import org.darkstars.battlehub.battlemodels.IBattleModel;
import org.darkstars.battlehub.framework.CEvent;
import org.darkstars.battlehub.gui.CUISettings;
import org.darkstars.battlehub.CUserSettings;
import org.darkstars.battlehub.gui.CView;
import org.darkstars.battlehub.contentdownloader.CContentInterface;
import org.darkstars.battlehub.framework.IModule;
import org.darkstars.battlehub.protocol.tasserver.ui.CAgree;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.darkstars.battlehub.framework.CConnection;
import org.darkstars.battlehub.framework.CCore;
import org.darkstars.battlehub.framework.CErrorWindow;
import org.darkstars.battlehub.framework.CEventTypes;
import org.darkstars.battlehub.framework.ICentralClass;
import org.darkstars.battlehub.framework.Misc;
import org.darkstars.battlehub.framework.protocol.IProtocol;
import org.darkstars.battlehub.gui.C3PanelContainer;
import org.darkstars.battlehub.gui.C3PanelContainer.EContainerLocation;
import org.darkstars.battlehub.gui.CContainerWindow;

class UpdateTask extends TimerTask {

    public LMain LM;

    UpdateTask(LMain L) {
        LM = L;
    }

    @Override
    public void run() {
        //if(LM.updatedone){
        LMain.core.Update();
    //}
    }
}

/**
 * While this class inherits from JFrame I do not use it as such and rely on a 
 * member variable JFrame. I would change this but its problematic as it would 
 * require lots of tedious work to make netbeans happy, but it needs to be done 
 * and soonish
 * 
 * @author  tarendai-std
 */
public class LMain extends javax.swing.JFrame implements IModule, ICentralClass{

    public static boolean Registering = false;
    public static boolean timerdo;
    public static boolean pageview = false;
    public static CToast Toasts = null;
    public static CPlayers playermanager;
    public static CAgree agree;
    public static CContentInterface cci;
    private static ArrayList<CView> views = new ArrayList<CView>();
    public static CUserCommandHandler command_handler;
    public static CUserSettings UserSettings;
    public IBattleModel battleModel;
    private static javax.swing.JColorChooser ColorChooser = null;
    
    public static CCore core = new CCore();
    
    @Deprecated
    public static IProtocol protocol;
    
    public CContainerWindow mainWindow;
    public C3PanelContainer mainContainer;
    
    /**
     * Creates new form LMain
     */
    public LMain() {
        
        SwingUtilities.invokeLater( new Runnable() {

            @Override
            public void run() {
                mainWindow = new CContainerWindow();
                mainContainer = new C3PanelContainer();
                mainWindow.SetGUIObject(mainContainer);

                JLabel lobbyLabel = new javax.swing.JLabel();

                lobbyLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/UI/BattlehubWide.png")));
                
                mainContainer.AddObject(lobbyLabel, EContainerLocation.topLeft);
                CTraffic traffic = new CTraffic();
                CConnection.setTraffic(traffic);
                
                
                
            }
        });
        agree = new CAgree();
        Toasts = new CToast();
        //initComponents();
        

        core.AddModule(this, CCore.UI_OBJECT_MANAGEMENT);
        core.AddModule(this, CCore.RCVD_SERVER_TRAFFIC);
        core.AddModule(this, CCore.LOBBY_GUI_EVENTS);

        playermanager = new CPlayers();
        playermanager.Init(this);

        command_handler = new CUserCommandHandler();
        command_handler.Init(this);
        //core.AddModule(command_handler);


        UserSettings = new CUserSettings();
        

        // test glest support
        if (org.darkstars.battlehub.CUserSettings.GetValue("glest.command", "foobar").equals("foobar") == false) {
            CContentManager.installed_engines.add("glest");
        }
    }

    public void start() {

        SwingUtilities.invokeLater( new Runnable() {

            @Override
            public void run() {
                mainWindow.setTitle("BattleHub "+CUpdateChecker.version);
                mainWindow.setIconImage(CUISettings.GetWindowIcon());
                mainWindow.setVisible(true);
                mainWindow.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent evt) {
                        formWindowClosing(evt);
                    }
                    @Override
                    public void windowOpened(java.awt.event.WindowEvent evt) {
                        formWindowOpened(evt);
                    }
                });
            }
        });
        
        


        //protocol = new CTASServerProtocol ();
        //protocol.Init (this);

        new Timer().schedule(new UpdateTask(this),
                10, //initial delay
                30);  //subsequent rate

        Toasts.Init(this);
        agree.Init(this);
        
        final LMain LM = this;
        SwingUtilities.invokeLater( new Runnable() {

            @Override
            public void run() {
                
                if (Boolean.valueOf(CUserSettings.GetValue("autologin", "false"))) {
                    CLoginProgress l = new CLoginProgress(LM);
                    AddView(l, true);
                } else {
                    CSplashScreen jk = new CSplashScreen(LM);
                    AddView(jk, true);
                }
                ChangeSize(860, 631);

            }
        });

        //DoValidate();
    }

    void ChangeSize(int x, int y) {

        final int dx = Math.max(x, 500);
        final int dy = Math.max(y, 500);
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run() {
                mainWindow.setBounds(getLocation().x, getLocation().y, dx, dy);
            }
        });
        ValidateNow();
    }
    
    public Color lastColor = Color.blue;

    @SuppressWarnings("static-access")
    public Color ShowColorDialog(Component component, String title) {
        //
        lastColor = ColorChooser.showDialog(component, title, lastColor);
        return lastColor;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(CUpdateChecker.version);
        setIconImage(CUISettings.GetWindowIcon());
        setMinimumSize(new java.awt.Dimension(600, 500));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 657, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 514, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        CUserSettings.PutValue("mainwindow.size.x", String.valueOf(mainWindow.getWidth()));
        CUserSettings.PutValue("mainwindow.size.y", String.valueOf(mainWindow.getHeight()));
        
        CUserSettings.PutValue("mainwindow.size.extendedstate", String.valueOf(mainWindow.getExtendedState()));
        
        mainWindow.setVisible(false);
        Shutdown();
    }//GEN-LAST:event_formWindowClosing

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        final LMain LM = this;
        final int x = Integer.valueOf(CUserSettings.GetValue("mainwindow.size.x","600"));
        final int y = Integer.valueOf(CUserSettings.GetValue("mainwindow.size.y","600"));
        
        final int extendedState = Integer.valueOf(CUserSettings.GetValue("mainwindow.size.extendedstate",String.valueOf(Frame.NORMAL)));
        SwingUtilities.invokeLater( new Runnable() {

            @Override
            public void run() {
                if (Boolean.valueOf(CUserSettings.GetValue("autologin", "false"))) {
                    CLoginProgress l = new CLoginProgress(LM);
                    AddView(l, true);
                } else {
                    CSplashScreen jk = new CSplashScreen(LM);
                    AddView(jk, true);
                }
                if(extendedState != Frame.MAXIMIZED_BOTH){
                    ChangeSize(x, y);
                }

            }
        });
        SwingUtilities.invokeLater( new Runnable() {

            @Override
            public void run() {
                if(extendedState != Frame.ICONIFIED){
                    mainWindow.setExtendedState(extendedState);
                }
            }
        });
    }//GEN-LAST:event_formWindowOpened

    @Override
    public void Update() {
        //
    }

    @Override
    public void NewEvent(CEvent e) {
        if (e.IsEvent("AGREEMENT")) {
            agree.AddLine(Misc.makeSentence(e.data, 1));
        } else if (e.IsEvent("AGREEMENTEND")) {
            SwingUtilities.invokeLater( new Runnable() {

                @Override
                public void run() {
                    agree.setVisible(true);
                }
            });
        } else if (e.IsEvent("BROADCAST")) {
            LMain.Toasts.AddMessage(Misc.makeSentence(e.data, 1));
        } else if (e.IsEvent(CEventTypes.DISCONNECTED)) {
            SetFocus("LoginPanel");
        }
    }

    @Override
    public void NewGUIEvent(CEvent e) {
        if (e.IsEvent(CEventTypes.LOGGEDOUT) || e.IsEvent(CEventTypes.LOGOUT)) {
            SetFocus("LoginPanel");
        } else if (e.IsEvent("UI_TOGGLE_FULLSCREEN")){
            //
            mainWindow.ToggleFullScreen();
        }
    }


    @Deprecated
    public boolean ViewExists(String s) {
        //
        Iterator<CView> i = views.iterator();
        while (i.hasNext()) {
            CView n = i.next();
            if (n.getTitle().equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    public void AddView(final CView v, final boolean setfocus) {

        core.AddModule(v);
        views.add(v);

        if (setfocus) {
            System.out.println("adding view and setting focus");
            SetFocus(v);
        }

    }

    @Deprecated
    public void RemoveView(final CView v) {

        core.RemoveModule(v);
        views.remove(v);

        if (v == current) {
            SwingUtilities.invokeLater( new Runnable() {

                @Override
                public void run() {
                    //mainWindow.RemoveGUIObjects();
                    //container.remove((Component) v);
                }
            });

            SwingUtilities.invokeLater( new Runnable() {

                @Override
                public void run() {
                    validate();
                }
            });
        }
    }

    @Deprecated
    public void RemoveView(String s) {
        //synchronized(views){
        for (int i = 0; i < views.size(); i++) {
            final CView v = views.get(i);
            if (v.getTitle().equals(s)) {
                RemoveView(v);

                //decrement so that we then get incremented and handle the same index again
                i--;

                break;
            }
        }

    }
    public CView current = null;

    @Deprecated
    public void SetFocus(final CView v) {


        if (views.contains(v)) {
            current = v;

            SwingUtilities.invokeLater( new Runnable() {

                @Override
                public void run() {
                    v.setVisible(true);
                    mainContainer.AddObject(v, EContainerLocation.middle);
                    
                    //container.removeAll();
                    //container.add(current);
                }
            });

//            SwingUtilities.invokeLater( new Runnable() {
//
//                @Override
//                public void run() {
//                    container.validate();
//                    container.repaint();
//                }
//            });

        } else {
            System.out.println("hmmm BOOO");
        }

    }

    @Deprecated
    public void SetFocus(String s) {
        Iterator<CView> i = views.iterator();
        while (i.hasNext()) {
            CView n = i.next();
            if (n == null) {
                System.out.println(java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("LMain.n_==_null_in_LMain"));
                continue;
            }
            if (n.getTitle().equalsIgnoreCase(s)) {
                SetFocus(n);
                return;
            }
        }
    }

    @Deprecated
    public void ValidateNow() {
        SwingUtilities.invokeLater( new Runnable() {

            @Override
            public void run() { /*jPanel1.*/

                mainWindow.validate();
            }
        });
    }

    /**
     * This function is here in order to support the IModule interface. Because
     * this class is somewhat old and more of a kludge than a design decisions,
     * one should never call this function.
     * 
     * In time this class will be replaced with a cleaner pice of code or the
     * nature of this function will change, probably because the parameter will
     * change to an instance of CCore.
     * 
     * @param L is a reference to the LMain class instance, which is pointless
     * because we have the 'this' keyword tot hat job here!
     */
    @Override
    public void Init(ICentralClass L) {
        //
    }

    @Override
    public void OnRemove() {
        //
    }
    
    @Override
    public CCore GetCore() {
        return core;
    }

    @Override
    public IProtocol GetProtocol() {
        return protocol;
    }
    
    @Override
    public void Shutdown() {
        if(GetProtocol() != null){
            GetProtocol().Disconnect();
        }
        GetCore().RemoveAllModules();

        System.exit(0);
    }
    
    public static String GetAbsoluteRootPath() {
        return GetAbsoluteLobbyFolderPathStatic().replaceAll("lobby/battlehub/", "");
    }

    @Override
    public String GetAbsoluteLobbyFolderPath() {
        //
        return GetAbsoluteLobbyFolderPathStatic();
    }
    
    public static String GetAbsoluteLobbyFolderPathStatic() {
        if (!Misc.dev_environment) {
            try {
                // Get current classloader
                URL u = LMain.class.getProtectionDomain().getCodeSource().getLocation();
                File f = new File(u.toURI());
                String s = f.getParent().replaceAll("\\\\", "/") + "/lobby/battlehub/";
                if (!Misc.isWindows()) {
                    s = "/" + s;
                }
                s = s.replaceAll("%20", " ");
                //System.out.println(f.getParent());
                return s;
            } catch (URISyntaxException ex) {
                Logger.getLogger(Misc.class.getName()).log(Level.SEVERE, null, ex);
                CErrorWindow ew = new CErrorWindow(ex);
            }
            return null;
        } else {
            File f2 = new File("lobby/battlehub/");
            try {
                //pathSeparator = "/";
                //            try {
                String s = f2.toURI().toURL().toString().substring(6);
                if (Misc.isWindows() == false) {
                    s = "/" + s;
                }
                s = s.replaceAll("%20", " ");
                s = s.replaceAll("\\\\", "/");
                if (!Misc.isWindows()) {
                    s = "/" + s;
                }

                //System.out.println("\"" + s + "\"");
                return s;
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
                CErrorWindow ew = new CErrorWindow(ex);
            }
            return "";
        }
    /*
    File f = new File("lobby/aflobby/");
    try {
    //pathSeparator = "/";
    //            try {
    String s =  f.toURI().toURL().toString().substring(6).replaceAll("%20", " ");
    if(isWindows()==false){
    s = "/"+s;
    }
    if((s.endsWith("/")==false)&&(s.endsWith("\\")==false)){
    s +="/";
    }
    return s;
    } catch (MalformedURLException ex) {
    ex.printStackTrace();
    }
    return "";*/
    }

    @Override
    public void OnEvent(CEvent e) {
        if(e.GetMessageChannel() == CCore.UI_OBJECT_MANAGEMENT){
            if(e.IsEvent("SETMIDDLECONTENT")){
                //
                mainContainer.AddObject((JComponent) e.object,EContainerLocation.middle);
            } else if(e.IsEvent("SETMIDDLECONTENTBYNAME")){
                //
                SetFocus(e.GetData(1));
            }
        }
    }

    @Override
    public void OnRemove(int channel) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    

    
}
