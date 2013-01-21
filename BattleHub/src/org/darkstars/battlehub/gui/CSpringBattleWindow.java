/*
 * CBattleWindow.java
 *
 * Created on 05 June 2006, 23:05
 */
package org.darkstars.battlehub.gui;

import aflobby.CUnitSyncJNIBindings;
import java.awt.event.ActionEvent;
import org.darkstars.battlehub.helpers.ColourHelper;
import org.darkstars.battlehub.helpers.CImageLoading;
import org.darkstars.battlehub.framework.CPlayer;
import org.darkstars.battlehub.framework.CHolePuncher;
import org.darkstars.battlehub.framework.CEvent;
import org.darkstars.battlehub.battlemodels.IGUIBattleModel;
import org.darkstars.battlehub.protocol.tasserver.RectHandler;
import org.darkstars.battlehub.protocol.tasserver.RectEntry;
import org.darkstars.battlehub.gui.CUISettings;
import org.darkstars.battlehub.CUserSettings;
import org.darkstars.battlehub.gui.battle.CBooleanOptionsPanel;
import org.darkstars.battlehub.gui.battle.CListOptionsPanel;
import org.darkstars.battlehub.gui.battle.CSliderOptionsPanel;
import org.darkstars.battlehub.gui.battle.CStringOptionsPanel;
import org.darkstars.battlehub.gui.battle.ListItem;
import org.darkstars.battlehub.protocol.tasserver.TeamEntry;
import org.darkstars.battlehub.protocol.tasserver.TeamData;
import org.darkstars.battlehub.helpers.AlphanumComparator;
import org.darkstars.battlehub.framework.IModule;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.darkstars.battlehub.AllyData;
import org.darkstars.battlehub.AllyEntry;
import org.darkstars.battlehub.CBattleInfo;
import org.darkstars.battlehub.CBattlePlayer;
import org.darkstars.battlehub.CBattlePlayerTableModel;
import org.darkstars.battlehub.CLadderProperties;
import org.darkstars.battlehub.CStartRectangleDrawer;
import org.darkstars.battlehub.CSync;
import org.darkstars.battlehub.CVectorListModel;
import org.darkstars.battlehub.LMain;
import org.darkstars.battlehub.Main;
import org.darkstars.battlehub.OptionType;
import org.darkstars.battlehub.framework.ICentralClass;
import org.darkstars.battlehub.framework.Misc;
import org.jvnet.flamingo.common.JCommandButton;
import org.jvnet.flamingo.common.JCommandButton.CommandButtonKind;
import org.jvnet.flamingo.common.icon.ImageWrapperResizableIcon;
import org.jvnet.flamingo.common.icon.ResizableIcon;
import org.jvnet.flamingo.ribbon.JRibbon;
import org.jvnet.flamingo.ribbon.JRibbonBand;
import org.jvnet.flamingo.ribbon.RibbonElementPriority;
import org.jvnet.flamingo.ribbon.RibbonTask;

/**
 *
 * @author  AF
 */
class CBattleTask extends TimerTask {

    public CSpringBattleWindow c;

    CBattleTask(CSpringBattleWindow jc) {
        c = jc;
    }

    @Override
    public void run() {
        if (c.UpdateClientsNeeded) {
            c.UpdateClientsNeeded = false;
            c.DoUpdateBattleClients();
        }
    }
}

public class CSpringBattleWindow extends javax.swing.JFrame implements IModule {

    public IGUIBattleModel battlemodel = null;
    public LMain LM;
    public java.awt.Point origin = new java.awt.Point();
    public CBattleInfo info;
    public Integer CurrentID = null;
    public String lastsent = java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("n/a");
    public int MyUDPPort = -1;
    public boolean UpdateClientsNeeded = false;
    public boolean active = false;
    private CBattleTask jbt;
    public Timer tjb;
    public Process p;
    public boolean ingame = false;
    //private CBattleCommandHandler cmdhandler = null;
    private CVectorListModel RestrictedUnits = new CVectorListModel();
    private CVectorListModel nonRestrictedUnits = new CVectorListModel();
    private CVectorListModel AIListModel = new CVectorListModel();
    private CVectorListModel factionListModel = new CVectorListModel();
    private CVectorListModel aisListModel = new CVectorListModel();
    public CStartRectangleDrawer startrect;
    private CBattlePlayerTableModel playertablemodel = null;
    private JCommandButton startButton;
    private JCommandButton lockButton;
    private JCommandButton exitButton;
    private JCommandButton readyButton;
    private JRibbon ribbon;
    private JRibbonBand gameActions;
    private JRibbonBand myTeamActions;
    private JRibbonBand gameOptions;
    private JRibbonBand otherGameActions;
    private JRibbonBand aiBand;
    JCommandButton gameOptionsButton;
    JCommandButton addAIButton;
    JCommandButton modifyAIButton;
    JCommandButton changeMapButton;
    JCommandButton restrictedUnitsButton;
    JCommandButton scriptButton;
    JCommandButton spectatorButton;
    JCommandButton fixTeamsButton;
    JCommandButton forceFFAButton;

    /**
     * Creates new form CBattleWindow
     * @param L
     */
    public CSpringBattleWindow(LMain L) {

        LM = L;


        playertablemodel = new CBattlePlayerTableModel(LM);


        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                initComponents();
            }
        });

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                ribbon = new JRibbon();
                //

                {
                    URL url = this.getClass().getResource("/images/tango/32x32/devices/input-gaming.png");
                    ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));
                    gameActions = new JRibbonBand("Game Actions", ri);
                }
                {
                    //
                    URL url = this.getClass().getResource("/images/tango/32x32/apps/system-users.png");
                    ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));
                    myTeamActions = new JRibbonBand("My Team Actions", ri);
                }

                {
                    URL url = this.getClass().getResource("/images/tango/32x32/categories/applications-accessories.png");
                    ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));
                    otherGameActions = new JRibbonBand("Other Game Actions", ri);
                }

                {
                    URL url = this.getClass().getResource("/images/tango/32x32/apps/preferences-system-session.png");
                    ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));
                    gameOptions = new JRibbonBand("Game Options", ri);
                }

                {
                    //
                    URL url = this.getClass().getResource("/images/tango/32x32/apps/utilities-terminal.png");
                    ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));
                    aiBand = new JRibbonBand("AI Options", ri);
                }


                RibbonTask battleOptions = new RibbonTask();

                battleOptions.addBand(gameOptions);
                battleOptions.addBand(aiBand);



                RibbonTask battleActions = new RibbonTask();
                battleActions.addBand(gameActions);
                battleActions.addBand(myTeamActions);
                battleActions.addBand(otherGameActions);

                otherGameActions.addPanel(jPanel3);
                myTeamActions.addPanel(myTeamPanel);

                //org.jvnet.flamingo.svg.SvgBatikResizableIcon.getSvgIcon(url, new Dimension(64,64));



                {
                    URL url = this.getClass().getResource("/images/tango/32x32/categories/applications-games.png");
                    ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));
                    startButton = new JCommandButton("Start Game", ri);
                }
                //
                {
                    URL url = this.getClass().getResource("/images/tango/32x32/actions/system-lock-screen.png");
                    ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));
                    lockButton = new JCommandButton("Lock Game", ri);
                }

                {
                    URL url = this.getClass().getResource("/images/tango/32x32/actions/system-log-out.png");
                    ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));
                    exitButton = new JCommandButton("Exit Game", ri);
                }

                {
                    URL url = this.getClass().getResource("/images/tango/32x32/actions/system-shutdown-red.png");
                    ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));
                    readyButton = new JCommandButton("Not Ready", ri);
                }

                {
                    URL url = this.getClass().getResource("/images/tango/32x32/apps/system-users.png");
                    ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));
                    spectatorButton = new JCommandButton("Player Mode", ri);
                }

                {
                    URL url = this.getClass().getResource("/images/tango/32x32/mimetypes/text-x-generic.png");
                    ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));
                    forceFFAButton = new JCommandButton("Force FFA", ri);
                }

                {
                    URL url = this.getClass().getResource("/images/tango/32x32/mimetypes/text-x-generic.png");
                    ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));
                    fixTeamsButton = new JCommandButton("Fix Teams", ri);
                }

                {
                    URL url = this.getClass().getResource("/images/tango/32x32/actions/document-properties.png");
                    ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));

                    scriptButton = new JCommandButton("View Game Script", ri);
                    scriptButton.setCommandButtonKind(CommandButtonKind.POPUP_ONLY);

                    scriptButton.addPopupActionListener(new PanelPopupActionListener(scriptPanel));
                }

                {
                    //
                    URL url = this.getClass().getResource("/images/tango/32x32/actions/document-properties.png");
                    ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));

                    gameOptionsButton = new JCommandButton("Game Options", ri);
                    gameOptionsButton.setCommandButtonKind(CommandButtonKind.POPUP_ONLY);

                    gameOptionsButton.addPopupActionListener(new PanelPopupActionListener(gameOptionsPanel));
                }

                {
                    URL url = this.getClass().getResource("/images/tango/32x32/apps/utilities-terminal.png");
                    ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));

                    addAIButton = new JCommandButton("Add AIs", ri);
                    addAIButton.setCommandButtonKind(CommandButtonKind.POPUP_ONLY);

                    addAIButton.addPopupActionListener(new PanelPopupActionListener(addAIPanel));
                }

                {
                    URL url = this.getClass().getResource("/images/tango/32x32/actions/document-properties.png");
                    ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));

                    modifyAIButton = new JCommandButton("Modify AIs", ri);
                    modifyAIButton.setCommandButtonKind(CommandButtonKind.POPUP_ONLY);

                    modifyAIButton.addPopupActionListener(new PanelPopupActionListener(modifyAIPanel));
                }

                {
                    URL url = this.getClass().getResource("/images/tango/32x32/actions/system-search.png");
                    ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));

                    changeMapButton = new JCommandButton("Map Browser", ri);
                    changeMapButton.setCommandButtonKind(CommandButtonKind.POPUP_ONLY);

                    changeMapButton.addPopupActionListener(new PanelPopupActionListener(mapPanel));
                }

                {
                    URL url = this.getClass().getResource("/images/tango/32x32/actions/mail-mark-not-junk.png");
                    ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));

                    restrictedUnitsButton = new JCommandButton("Restricted Units", ri);
                    restrictedUnitsButton.setCommandButtonKind(CommandButtonKind.POPUP_ONLY);
                    restrictedUnitsButton.addPopupActionListener(new PanelPopupActionListener(restrictedUnitsPanel));
                }

                startButton.addActionListener(new java.awt.event.ActionListener() {

                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        battlemodel.Start();
                    }
                });

                lockButton.addActionListener(new java.awt.event.ActionListener() {

                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        boolean locked = !battlemodel.IsLocked();
                        battlemodel.SetLocked(locked);
                        if (locked) {
                            lockButton.setText("Unlock game");

                        } else {
                            lockButton.setText("Lock game");
                        }
                    }
                });

                exitButton.addActionListener(new java.awt.event.ActionListener() {

                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        battlemodel.Exit();
                    }
                });

                myTeamActions.addGalleryButton(readyButton, RibbonElementPriority.TOP);

                readyButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean toggle = !battlemodel.GetMe().isReady();
                        battlemodel.GetMe().setReady(toggle);

                        SendMyStatus();
                        MyReadyStatusChanged();
                    }
                });

                //

                myTeamActions.addGalleryButton(spectatorButton, RibbonElementPriority.TOP);

                spectatorButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean toggle = !battlemodel.GetMe().isSpec();
                        battlemodel.GetMe().setSpectator(toggle);

                        SendMyStatus();
                        MySpectatorStatusChanged();
                    }
                });
                /*
                 * 
                 */

                gameActions.addGalleryButton(startButton, RibbonElementPriority.TOP);
                //gameActions.addPanel(jPanel1);
                gameActions.addGalleryButton(lockButton, RibbonElementPriority.TOP);
                gameActions.addGalleryButton(exitButton, RibbonElementPriority.TOP);

                gameOptions.addGalleryButton(gameOptionsButton, RibbonElementPriority.MEDIUM);
                aiBand.addGalleryButton(addAIButton, RibbonElementPriority.MEDIUM);
                aiBand.addGalleryButton(modifyAIButton, RibbonElementPriority.MEDIUM);
                gameOptions.addGalleryButton(changeMapButton, RibbonElementPriority.MEDIUM);

                


                gameOptions.addGalleryButton(restrictedUnitsButton, RibbonElementPriority.LOW);
                gameOptions.addGalleryButton(scriptButton, RibbonElementPriority.LOW);


                ribbon.addTask("Battle Actions", battleActions);
                ribbon.addTask("Battle Options", battleOptions);

                ribbonPanel.add(ribbon);
            }
        });


        jbt = new CBattleTask(this);
        tjb = new Timer();



        //cmdhandler = new CBattleCommandHandler(LM, this, LMain.command_handler);
        //jas = new JAutoDownScroll (BattleMessages);
        //jast = new Timer ();
        Runnable doWorkRunnable2 = new Runnable() {

            @Override
            public void run() {

                playerBattleTable.getColumnModel().getColumn(0).setMaxWidth(50);
                playerBattleTable.getColumnModel().getColumn(1).setMaxWidth(50);
                playerBattleTable.getColumnModel().getColumn(2).setMaxWidth(50);
                playerBattleTable.getColumnModel().getColumn(4).setMaxWidth(30);
                playerBattleTable.getColumnModel().getColumn(5).setMaxWidth(50);
                playerBattleTable.getColumnModel().getColumn(6).setMaxWidth(60);
                playerBattleTable.getColumnModel().getColumn(7).setMaxWidth(30);
                playerBattleTable.getColumnModel().getColumn(8).setMaxWidth(30);
                playerBattleTable.getColumnModel().getColumn(9).setMaxWidth(30);
                playerBattleTable.getColumnModel().getColumn(10).setMaxWidth(40);

                setLocationRelativeTo(null); // center the window on the screen
                requestFocusInWindow();
            }
        };
        SwingUtilities.invokeLater(doWorkRunnable2);

    //this.ColourWheel.getSelectionModel().addChangeListener(this);
    }

    public void MyReadyStatusChanged() {
        //
        boolean ready = battlemodel.GetMe().isReady();
        if (ready) {
            //
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    URL url = this.getClass().getResource("/images/tango/32x32/actions/system-shutdown-green.png");
                    ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));
                    readyButton.setIcon(ri);
                    readyButton.setText("Ready");
                }
            });
        } else {

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    URL url = this.getClass().getResource("/images/tango/32x32/actions/system-shutdown-red.png");
                    ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));
                    readyButton.setIcon(ri);
                    readyButton.setText("Not Ready");
                }
            });
        }
    }

    public void MySpectatorStatusChanged() {
        //
        boolean spec = battlemodel.GetMe().isSpec();
        if (spec) {
            //
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    URL url = this.getClass().getResource("/images/tango/32x32/actions/system-search.png");
                    ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));
                    spectatorButton.setIcon(ri);
                    spectatorButton.setText("Spectator Mode");
                }
            });
        } else {

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    URL url = this.getClass().getResource("/images/tango/32x32/apps/system-users.png");
                    ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));
                    spectatorButton.setIcon(ri);
                    spectatorButton.setText("Player Mode");
                }
            });
        }
    }

    public void Init() {
    }

    public void SetBattleTitle(final String s) {
        //
        Runnable doWorkRunnable = new Runnable() {

            @Override
            public void run() {
                setTitle(s);
            }
        };

        SwingUtilities.invokeLater(doWorkRunnable);
    }

    /**
     * 
     * @param player 
     */
    public void AddPlayer(CBattlePlayer player) {
        //
        playertablemodel.AddPlayer(player);

        Redraw();
        String flag = " ";

        if (Boolean.valueOf(CUserSettings.GetValue("ui.channelview.userjoinflags", "false"))) {
            flag = Misc.toHTML(player.getPlayerdata().GetFlagHTML(LM));
        }

        if (Boolean.valueOf(CUserSettings.GetValue("ui.channelview.userjoinranks", "false"))) {
            flag += " " + Misc.toHTML(player.getPlayerdata().GetRankHTML(LM));
        }

        String name = player.getPlayername();
        String msg = java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("CBattleWindow.joined_the_battle");
        AddMessage("<font face=\"Arial, Helvetica, sans-serif\" size=\"3\">" + flag + " " + name + " " + msg + "</font>");
    }

    public void RemovePlayer(CBattlePlayer p) {
        //
        playertablemodel.RemovePlayer(p);
        AddMessage("<font face=\"Arial, Helvetica, sans-serif\" size=\"3\">" + p.getPlayername() + java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("CBattleWindow.left_the_battle") + "</font>");
    }

    public void JoinBattle(CBattleInfo jinfo) {
        battlemodel = (IGUIBattleModel) LM.battleModel;

        active = true;
        info = jinfo;

        final ArrayList<Object> options = new ArrayList<Object>();

        final CBooleanOptionsPanel b = new CBooleanOptionsPanel(
                battlemodel,
                "GAME/DiminishingMMs",
                false,
                "Diminishing metal maker returns",
                " As you build more" +
                " metalmakers, their metal output drops. Metal maker" +
                " efficiency drops over time as a result as more " +
                "metalmakers are built.",
                false);
        final CBooleanOptionsPanel b2 = new CBooleanOptionsPanel(
                battlemodel,
                "GAME/LimitDGun",
                false,
                "Limited Dgun range",
                "Limits the dgunning to within a certain radius around your" +
                " starting position to prevent offensive commander pushes.",
                false);
        final CBooleanOptionsPanel b3 = new CBooleanOptionsPanel(
                battlemodel,
                "GAME/GhostedBuildings",
                true,
                "Ghosted Buildings",
                "When an enemy building goes out of view, a ghost appears" +
                " showing its location and unit type afterwards.",
                false);
        LMain.core.AddModule(b);
        LMain.core.AddModule(b2);
        LMain.core.AddModule(b3);

        final CSliderOptionsPanel sp1 = new CSliderOptionsPanel(
                battlemodel,
                "GAME/StartEnergy",
                1000,
                500,
                10000,
                500,
                "Initial Starting Energy",
                "The amount of energy given to your commander at the start of" +
                " the game to build things with.",
                false);

        final CSliderOptionsPanel sp2 = new CSliderOptionsPanel(
                battlemodel,
                "GAME/StartMetal",
                1000,
                500,
                10000,
                500,
                "Initial Starting Metal",
                "The amount of metal given to your commander at the start of" +
                " the game to build things with.",
                false);

        final CSliderOptionsPanel sp3 = new CSliderOptionsPanel(
                battlemodel,
                "GAME/MaxUnits",
                1000,
                500,
                10000,
                500,
                "Unit Limit",
                "The maximum number of units that can be built in a single" +
                " game.",
                false);

        {
            ArrayList<ListItem> items =
                    new ArrayList<ListItem>();

            ListItem y = new ListItem();
            y.key = "0";
            y.name = "Destroy All";
            y.description = "Annihilate all enemy units to win.";

            items.add(y);

            ListItem y1 = new ListItem();
            y1.key = "1";
            y1.name = "Commander Ends";
            y1.description = "A deathmatch where players kill eachother off by" +
                    " destroying their commanders.. Loosing your commander" +
                    " looses you the game.";

            items.add(y1);

            ListItem y2 = new ListItem();
            y2.key = "1";
            y2.name = "Lineage";
            y2.description = "Loosing your commander" +
                    " looses you the game. The difference here is that when" +
                    " your stuff dies so does everything you gave to your" +
                    " allies, and anything built with those shared units.";

            items.add(y2);


            final CListOptionsPanel lo = new CListOptionsPanel(
                    battlemodel,
                    "GAME/GameMode",
                    "0",
                    items,
                    "Victory Condition",
                    "Determines how the game is to be won.",
                    false);
            options.add(lo);
        }

        {
            ArrayList<ListItem> items =
                    new ArrayList<ListItem>();

            ListItem y = new ListItem();
            y.key = "0";
            y.name = "Fixed";
            y.description = "Places players at predefined starting positions" +
                    " based on their ordering.";

            items.add(y);

            ListItem y1 = new ListItem();
            y1.key = "1";
            y1.name = "Random";
            y1.description = "Randomly places you on a predefined start " +
                    "position.";

            items.add(y1);

            ListItem y2 = new ListItem();
            y2.key = "2";
            y2.name = "Choose Ingame";
            y2.description = "When ingame you can choose where on the map you" +
                    " want to start by placing markers. This can be confine to" +
                    " a startign rectangle/box";

            items.add(y2);


            final CListOptionsPanel lo = new CListOptionsPanel(
                    battlemodel,
                    "GAME/StartPosType",
                    "0",
                    items,
                    "Starting Positions",
                    "Determines where and how on the map your units start the game.",
                    false);
            options.add(lo);
        }

        LMain.core.AddModule(sp1);
        LMain.core.AddModule(sp2);
        LMain.core.AddModule(sp3);

        CSync.SetWorkingMod(info.GetMod());
        for (CPlayer player : info.GetPlayers()) {
            //
            CBattlePlayer pl = new CBattlePlayer();

            pl.setBattlestatus(0);
            pl.setColor(Color.BLUE);
            pl.setPlayerdata(player);
            pl.setPlayername(player.name);
            if (player.name.equalsIgnoreCase(LMain.protocol.GetUsername())) {
                battlemodel.SetMe(pl);
            }
            battlemodel.GetPlayers().put(player.name, pl);
            playertablemodel.AddPlayer(pl);
        }

        for (Object i : options) {
            LMain.core.AddModule((IModule) i);
        }
        String sk;
        if (Misc.isWindows()) {
            sk = CUnitSyncJNIBindings.SearchVFS("AI/Bot-libs/*.dll");
        } else if (Misc.isMacOS()) {
            sk = CUnitSyncJNIBindings.SearchVFS("AI/Bot-libs/*.dylib");
        } else {
            // if(Misc.isLinux()){
            sk = CUnitSyncJNIBindings.SearchVFS("AI/Bot-libs/*.so");
        }
        String sk2 = CUnitSyncJNIBindings.SearchVFS("AI/Bot-libs/*.jar");

        final String[] ailist1 = sk.split(",");
        final String[] ailist2 = sk.split(",");

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                startButton.setEnabled(false);
                lockButton.setEnabled(false);

                MapList.removeAll();
                Object[] m = CSync.map_names.values().toArray().clone();
                Arrays.sort(m, new AlphanumComparator());
                MapList.setListData(m);

                AIListModel.clear();
                for (String s : ailist1) {
                    String s2 = s.replaceAll("\\\\", "/");
                    int idx = s2.lastIndexOf("/");
                    if (idx != -1) {
                        s2 = s2.substring(idx + 1);
                    }
                    AIListModel.addElement(s2);//.substring(12));
                }
                for (String s : ailist2) {
                    String s2 = s.replaceAll("\\\\", "/");
                    int idx = s2.lastIndexOf("/");
                    if (idx != -1) {
                        s2 = s2.substring(idx + 1);
                    }
                    AIListModel.addElement(s2);//.substring(12));
                }

                RaceCombo.removeAllItems();
                factionListModel.clear();

                for (int i = 0; i < CSync.sidecount; i++) {
                    String r = CSync.GetSideName(i);
                    RaceCombo.addItem(r);
                    factionListModel.addElement(r);
                }
                // do ladder stuff
                if (info.isladdergame()) {
                    //
                    SetupLadderControls();
                }


                OptionsPanel.add(b);
                OptionsPanel.add(b2);
                OptionsPanel.add(b3);

                OptionsPanel.add(sp1);
                OptionsPanel.add(sp2);
                OptionsPanel.add(sp3);

                for (Object i : options) {
                    OptionsPanel.add((Component) i);
                }


            }
        });


        /*
        Runnable doWorkRunnable = new Runnable() {
        public void run() {
        for (int i = 0; i < CSync.sidecount; i++) {
        RaceCombo.addItem(CSync.GetSideName(i));
        }
        MapList.removeAll();
        MapList.setListData(CSync.map_names.values().toArray());
        }
        };
        SwingUtilities.invokeLater(doWorkRunnable);*/

        MapChange();

        CurrentID = jinfo.GetID();
        info.SetActive(true);
        SetControls(info);
        SetHostControls(false);
        //SendMyStatus ();//###@
        //(Component) CSync.map_names.get(i));
        Redraw();
    }

    public void SetupLadderControls() {
        CLadderProperties lp = info.getLadderproperties();

        // the ladder onyl supports 2 ally teams so empty th combobox and insert 2 entries
        MyAllyTeamCombo.removeAllItems();
        MyAllyTeamCombo.addItem("Ally Team 1");
        MyAllyTeamCombo.addItem("Ally Team 2");
    /*if(lp.startpos != -1){
    StartPosCombo.setSelectedIndex(lp.startpos);
    StartPosCombo.setEnabled(false);
    }
    if(lp.gamemode != -1){
    GameEndCombo.setSelectedIndex(lp.gamemode);
    GameEndCombo.setEnabled(false);
    }
    int min=1000,max=10000;
    if(lp.maxmetal != -1){
    max = lp.maxmetal;
    StartingMetalSlider.setMaximum(max);
    //StartingMetalSlider.setEnabled(false);
    }
    if(lp.minmetal != -1){
    min = lp.minmetal;
    StartingMetalSlider.setMinimum(min);
    //StartingMetalSlider.setEnabled(false);
    }
    StartingMetalSpinner.setModel(new SpinnerNumberModel(min,min,max,500));
    min = 500;
    max = 10000;
    if(lp.maxenergy != -1){
    max = lp.maxenergy;
    StartingEnergySlider.setMaximum(max);
    //StartingEnergySlider.setEnabled(false);
    }
    if(lp.minenergy != -1){
    min = lp.minenergy;
    StartingEnergySlider.setMinimum(min);
    //StartingEnergySlider.setEnabled(false);
    }
    StartingEnergySpinner.setModel(new SpinnerNumberModel(min,min,max,500));
    min = 50;
    max = 5000;
    if(lp.maxunits != -1){
    max = lp.maxunits;
    MaxUnitsSlider.setMaximum(max);
    //MaxUnitsSlider.setEnabled(false);
    }
    if(lp.minunits != -1){
    min = lp.minunits;
    MaxUnitsSlider.setMinimum(min);
    //MaxUnitsSlider.setEnabled(false);
    }
    MaxUnitsSpinner.setModel(new SpinnerNumberModel(Math.max(min, 500),min,max,500));
     */
    /*if(lp.dgun != -1){
    LimitDGunCheck.setSelected(lp.dgun == 1);
    LimitDGunCheck.setEnabled(false);
    }
    if(lp.diminish != -1){
    DiminMMCheck.setSelected(lp.diminish == 1);
    DiminMMCheck.setEnabled(false);
    }
    if(lp.ghost != -1){
    GhostedCheck.setSelected(lp.ghost == 1);
    GhostedCheck.setEnabled(false);
    }*/
    }

    public void LeaveBattle() {

        active = false;


        RectHandler.clearRects();

        CEvent e = new CEvent(CEvent.EXITEDBATTLE);
        LMain.core.NewGUIEvent(e);

        if (tjb != null) {
            tjb.cancel();
            tjb = null;
        }

        if (battlemodel != null) {
            battlemodel.Exit();
            battlemodel = null;
        }


        Runnable doWorkRunnable = new Runnable() {

            @Override
            public void run() {
                setVisible(false);
                dispose();
            }
        };

        SwingUtilities.invokeLater(doWorkRunnable);
    }

    public void HostGame(CBattleInfo jinfo) {
        battlemodel = (IGUIBattleModel) LM.battleModel;
        active = true;
        info = jinfo;
        final ArrayList<Object> options = new ArrayList<Object>();

        final CBooleanOptionsPanel b1 = new CBooleanOptionsPanel(
                battlemodel,
                "GAME/DiminishingMMs",
                false,
                "Diminishing metal maker returns",
                " As you build more" +
                " metalmakers, their metal output drops. Metal maker" +
                " efficiency drops over time as a result as more " +
                "metalmakers are built.",
                false);
        final CBooleanOptionsPanel b2 = new CBooleanOptionsPanel(
                battlemodel,
                "GAME/LimitDGun",
                false,
                "Limited Dgun range",
                "Limits the dgunning to within a certain radius around your" +
                " starting position to prevent offensive commander pushes.",
                false);
        final CBooleanOptionsPanel b3 = new CBooleanOptionsPanel(
                battlemodel,
                "GAME/GhostedBuildings",
                true,
                "Ghosted Buildings",
                "When an enemy building goes out of view, a ghost appears" +
                " showing its location and unit type afterwards.",
                false);
        options.add(b1);
        options.add(b2);
        options.add(b3);

        final CSliderOptionsPanel sp1 = new CSliderOptionsPanel(
                battlemodel,
                "GAME/StartEnergy",
                1000,
                500,
                10000,
                500,
                "Initial Starting Energy",
                "The amount of energy given to your commander at the start of" +
                " the game to build things with.",
                false);

        final CSliderOptionsPanel sp2 = new CSliderOptionsPanel(
                battlemodel,
                "GAME/StartMetal",
                1000,
                500,
                10000,
                500,
                "Initial Starting Metal",
                "The amount of metal given to your commander at the start of" +
                " the game to build things with.",
                false);

        final CSliderOptionsPanel sp3 = new CSliderOptionsPanel(
                battlemodel,
                "GAME/MaxUnits",
                1000,
                500,
                10000,
                500,
                "Unit Limit",
                "The maximum number of units that can be built in a single" +
                " game.",
                false);

        options.add(sp1);
        options.add(sp2);
        options.add(sp3);

        {
            ArrayList<ListItem> items =
                    new ArrayList<ListItem>();

            ListItem y = new ListItem();
            y.key = "0";
            y.name = "Destroy All";
            y.description = "Annihilate all enemy units to win.";

            items.add(y);

            ListItem y1 = new ListItem();
            y1.key = "1";
            y1.name = "Commander Ends";
            y1.description = "A deathmatch where players kill eachother off by" +
                    " destroying their commanders.. Loosing your commander" +
                    " looses you the game.";

            items.add(y1);

            ListItem y2 = new ListItem();
            y2.key = "1";
            y2.name = "Lineage";
            y2.description = "Loosing your commander" +
                    " looses you the game. The difference here is that when" +
                    " your stuff dies so does everything you gave to your" +
                    " allies, and anything built with those shared units.";

            items.add(y2);


            final CListOptionsPanel lo = new CListOptionsPanel(
                    battlemodel,
                    "GAME/GameMode",
                    "0",
                    items,
                    "Victory Condition",
                    "Determines how the game is to be won.",
                    false);
            options.add(lo);
        }

        {
            ArrayList<ListItem> items =
                    new ArrayList<ListItem>();

            ListItem y = new ListItem();
            y.key = "0";
            y.name = "Fixed";
            y.description = "Places players at predefined starting positions" +
                    " based on their ordering.";

            items.add(y);

            ListItem y1 = new ListItem();
            y1.key = "1";
            y1.name = "Random";
            y1.description = "Randomly places you on a predefined start " +
                    "position.";

            items.add(y1);

            ListItem y2 = new ListItem();
            y2.key = "2";
            y2.name = "Choose Ingame";
            y2.description = "When ingame you can choose where on the map you" +
                    " want to start by placing markers. This can be confine to" +
                    " a startign rectangle/box";

            items.add(y2);


            final CListOptionsPanel lo = new CListOptionsPanel(
                    battlemodel,
                    "GAME/StartPosType",
                    "0",
                    items,
                    "Starting Positions",
                    "Determines where and how on the map your units start the game.",
                    false);
            options.add(lo);
        }


        CSync.SetWorkingMod(info.GetMod());

        int modoptioncount = CUnitSyncJNIBindings.GetModOptionCount();
        for (int i = 0; i < modoptioncount; i++) {
            int type = CUnitSyncJNIBindings.GetOptionType(i);
            String key = "game/modoptions/" + CUnitSyncJNIBindings.GetOptionKey(i);
            String description = CUnitSyncJNIBindings.GetOptionDesc(i);
            String name = CUnitSyncJNIBindings.GetOptionName(i);
            if (type == OptionType.opt_bool) {
                //
                boolean defValue = CUnitSyncJNIBindings.GetOptionBoolDef(i) == 1;
                final CBooleanOptionsPanel o = new CBooleanOptionsPanel(
                        battlemodel,
                        key,
                        defValue,
                        name,
                        description,
                        false);
                options.add(o);
            } else if (type == OptionType.opt_list) {
                ArrayList<ListItem> items = new ArrayList<ListItem>();

                int listoptcount = CUnitSyncJNIBindings.GetOptionListCount(i);
                for (int n = 0; n < listoptcount; n++) {
                    //
                    ListItem lim = new ListItem();
                    lim.key = CUnitSyncJNIBindings.GetOptionListItemKey(i, n);
                    lim.name = CUnitSyncJNIBindings.GetOptionListItemName(i, n);
                    lim.description = CUnitSyncJNIBindings.GetOptionListItemDesc(i, n);

                    items.add(lim);
                }

                String defValue = CUnitSyncJNIBindings.GetOptionListDef(i);

                CListOptionsPanel o = new CListOptionsPanel(
                        battlemodel,
                        key,
                        defValue,
                        items,
                        name,
                        description,
                        false);

                options.add(o);

            } else if (type == OptionType.opt_number) {
                //
                int defValue = (int) CUnitSyncJNIBindings.GetOptionNumberDef(i);
                int min = (int) CUnitSyncJNIBindings.GetOptionNumberMin(i);
                int max = (int) CUnitSyncJNIBindings.GetOptionNumberMax(i);
                int step = (int) CUnitSyncJNIBindings.GetOptionNumberMin(i);
                CSliderOptionsPanel o = new CSliderOptionsPanel(
                        battlemodel,
                        key,
                        defValue,
                        min,
                        max,
                        step,
                        name,
                        description,
                        false);

                options.add(o);
            } else if (type == OptionType.opt_string) {
                //
                String defValue = CUnitSyncJNIBindings.GetOptionStringDef(i);
                int maxlength = CUnitSyncJNIBindings.GetOptionStringMaxLen(i);
                CStringOptionsPanel o = new CStringOptionsPanel(
                        battlemodel,
                        key,
                        defValue,
                        maxlength,
                        name,
                        description,
                        false);
            } else if (type == OptionType.opt_error) {
                // bah
            }
        }

        for (Object i : options) {
            LMain.core.AddModule((IModule) i);
        }

        String sk;
        if (Misc.isWindows()) {
            sk = CUnitSyncJNIBindings.SearchVFS("AI/Bot-libs/*.dll");
        } else if (Misc.isMacOS()) {
            sk = CUnitSyncJNIBindings.SearchVFS("AI/Bot-libs/*.dylib");
        } else {
            // if(Misc.isLinux()){
            sk = CUnitSyncJNIBindings.SearchVFS("AI/Bot-libs/*.so");
        }
        String sk2 = CUnitSyncJNIBindings.SearchVFS("AI/Bot-libs/*.jar");

        final String[] ailist1 = sk.split(",");
        final String[] ailist2 = sk.split(",");

        Runnable doWorkRunnable = new Runnable() {

            @Override
            public void run() {

                //
                gameOptions.addGalleryButton(forceFFAButton, RibbonElementPriority.MEDIUM);
                gameOptions.addGalleryButton(fixTeamsButton, RibbonElementPriority.MEDIUM);

                fixTeamsButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // FIX TEAMS!
                        int i = 0;
                        for (CBattlePlayer bp : battlemodel.GetPlayers().values()) {
                            bp.setTeamNo(i);
                            LM.GetProtocol().SendTraffic("FORCETEAMNO " + bp.getPlayername() + " " + i);
                            i++;
                        }
                    }
                });

                forceFFAButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // FORCE FFA!
                        int i = 0;
                        for (CBattlePlayer bp : battlemodel.GetPlayers().values()) {
                            bp.setAllyNo(i);
                            LM.GetProtocol().SendTraffic("FORCEALLYNO " + bp.getPlayername() + " " + i);
                            i++;
                        }
                    }
                });
                
                startButton.setEnabled(true);
                lockButton.setEnabled(true);
                AIListModel.clear();

                for (String s : ailist1) {
                    String s2 = s.replaceAll("\\\\", "/");
                    int idx = s2.lastIndexOf("/");
                    if (idx != -1) {
                        s2 = s2.substring(idx + 1);
                    }
                    AIListModel.addElement(s2);
                }

                for (String s : ailist2) {
                    String s2 = s.replaceAll("\\\\", "/");
                    int idx = s2.lastIndexOf("/");
                    if (idx != -1) {
                        s2 = s2.substring(idx + 1);
                    }
                    AIListModel.addElement(s2);
                }

                RaceCombo.removeAllItems();
                factionListModel.clear();

                for (int i = 0; i < CSync.sidecount; i++) {
                    String r = CSync.GetSideName(i);
                    RaceCombo.addItem(r);
                    factionListModel.addElement(r);
                }

                // do ladder stuff
                if (info.isladdergame()) {
                    //
                    SetupLadderControls();
                }

                for (Object i : options) {
                    OptionsPanel.add((Component) i);
                }

            }
        };

        SwingUtilities.invokeLater(doWorkRunnable);

        battlemodel.GetMe().setColor(Color.BLUE);
        battlemodel.GetMe().setBattlestatus(0);
        battlemodel.GetMe().setSpectator(false);
        battlemodel.GetMe().setPlayername(LMain.protocol.GetUsername());
        battlemodel.GetMe().setPlayerdata(LMain.playermanager.GetPlayer(LMain.protocol.GetUsername()));
        battlemodel.GetPlayers().put(LMain.protocol.GetUsername(), battlemodel.GetMe());
        info.SetMap(CUserSettings.GetValue("battle.lastmap", "SmallDivide.smf"));

        playertablemodel.AddPlayer(battlemodel.GetMe());
        final CSpringBattleWindow bw = this;

        Runnable doWorkRunnable2 = new Runnable() {

            @Override
            public void run() {
                MapList.removeAll();
                Object[] m = CSync.map_names.values().toArray().clone();
                Arrays.sort(m, new AlphanumComparator());
                MapList.setListData(m);
//                MapList.removeAll();
//                MapList.setListData(CSync.map_names.values().toArray().clone());
                ArrayList<String> a = CSync.GetUnitList();
                //if(a.isEmpty ()==false){
//                    RestrictedUnitsCombo.removeAllItems ();
                for (String s : a) {
                    nonRestrictedUnits.addElement(s);
                }
                startrect = new CStartRectangleDrawer(bw);

                JCommandButton startRectButton;
                {
                    startRectButton = new JCommandButton("Starting Rectanges", org.jvnet.flamingo.utils.FlamingoUtilities.getRibbonBandExpandIcon());
                    startRectButton.setCommandButtonKind(CommandButtonKind.POPUP_ONLY);
                    startRectButton.addPopupActionListener(new PanelPopupActionListener(startrect));
                }
                gameActions.addGalleryButton(startRectButton, RibbonElementPriority.MEDIUM);
            //gameOptionsPanel.add(java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("CBattleWindow.StartingBoxesTabTitle"), startrect);
//                }
            }
        };
        SwingUtilities.invokeLater(doWorkRunnable2);

        MapChange();

        CurrentID = jinfo.GetID();
        info.SetActive(true);
        SetControls(info);
        SetHostControls(true);
        battlemodel.CheckSync();
        //SendMyStatus ();
        tjb = new Timer();
        jbt = new CBattleTask(this);
        tjb.schedule(jbt, 1000, 1000); //subsequent rate
        Redraw();
    //
    }

    public void SetHostControls(final boolean host) {
        Runnable doWorkRunnable = new Runnable() {

            @Override
            public void run() {
                setVisible(true);
                SetBattleTitle(info.GetDescription() + "::" + info.GetMod());

                /*StartingEnergySlider.setEnabled(host);
                StartingEnergySpinner.setEnabled(host);
                StartingMetalSlider.setEnabled(host);
                StartingMetalSpinner.setEnabled(host);
                MaxUnitsSlider.setEnabled(host);
                MaxUnitsSpinner.setEnabled(host);*/
                //StartPosCombo.setEnabled(host);
                //GameEndCombo.setEnabled(host);
                //LimitDGunCheck.setEnabled(host);
                //DiminMMCheck.setEnabled(host);
                //GhostedCheck.setEnabled(host);

                UnRestrictAllUnitsButton.setEnabled(host);
                UnRestrictUnitButton.setEnabled(host);
                RestrictUnitButton.setEnabled(host);
            //HostButtonsPanel.setVisible(host);
            }
        };
        SwingUtilities.invokeLater(doWorkRunnable);

    //
    }

    void SetControls(final CBattleInfo i) {
        CBattlePlayer me = battlemodel.GetMe();
        if (me != null) {
            me.setTeamNo(1); //mybattlestatus = Misc.setTeamNoOfBattleStatus (mybattlestatus,1);
            me.setSpectator(false); //mybattlestatus = Misc.setModeOfBattleStatus (mybattlestatus,1);
            me.setReady(false); //mybattlestatus = Misc.setReadyStatusOfBattleStatus (mybattlestatus,0);
        }
    /*Runnable doWorkRunnable = new Runnable() {
    public void run() {
    /*StartingEnergySlider.getModel().setValue(info.energy);
    StartingEnergySpinner.getModel().setValue(info.energy);
    StartingMetalSlider.getModel().setValue(info.metal);
    StartingMetalSpinner.getModel().setValue(info.metal);
    MaxUnitsSlider.getModel().setValue(info.maxunits);
    MaxUnitsSpinner.getModel().setValue(info.maxunits);*/
    //StartPosCombo.setSelectedIndex(i.startPos);
    //GameEndCombo.setSelectedIndex(i.gameEndCondition);
    //LimitDGunCheck.setSelected(info.limitDGun);
    //DiminMMCheck.setSelected(info.diminishingMMs);
    //GhostedCheck.setSelected(info.ghostedBuildings);
            /*}
    };
    SwingUtilities.invokeLater(doWorkRunnable);*/
    }

    public int GetSpectatorCount() {
        int c = 0;
        Iterator<CBattlePlayer> i = battlemodel.GetPlayers().values().iterator();
        while (i.hasNext()) {
            CBattlePlayer pl = i.next();
            if (pl.isSpec()) {
                c++;
            }
        }
        return c;
    }

    public void SetTeamColor(Color c) {
        battlemodel.GetMe().setColor(c); //mycolour = c;
    }

    /*public void setStartingMetal(final int m) {
    info.metal = m;
    Runnable doWorkRunnable = new Runnable() {
    public void run() {
    StartingMetalSlider.getModel().setValue(m);
    StartingMetalSpinner.getModel().setValue(m);
    }
    };
    SwingUtilities.invokeLater(doWorkRunnable);
    }
    public void setStartingEnergy(final int e) {
    info.energy = e;
    Runnable doWorkRunnable = new Runnable() {
    public void run() {
    StartingEnergySlider.getModel().setValue(e);
    StartingEnergySpinner.getModel().setValue(e);
    }
    };
    SwingUtilities.invokeLater(doWorkRunnable);
    }
    public void setMaxUnits(final int u) {
    info.maxunits = u;
    Runnable doWorkRunnable = new Runnable() {
    public void run() {
    MaxUnitsSlider.getModel().setValue(u);
    MaxUnitsSpinner.getModel().setValue(u);
    }
    };
    SwingUtilities.invokeLater(doWorkRunnable);
    }*/

    /*public void setStartPosType(final int s) {
    info.startPos = s;
    Runnable doWorkRunnable = new Runnable() {
    public void run() {
    StartPosCombo.setSelectedIndex(s);
    }
    };
    SwingUtilities.invokeLater(doWorkRunnable);
    }
    public void setGameEnd(final int s) {
    info.gameEndCondition = s;
    Runnable doWorkRunnable = new Runnable() {
    public void run() {
    GameEndCombo.setSelectedIndex(s);
    }
    };
    SwingUtilities.invokeLater(doWorkRunnable);
    }
    public void setLimitDGUN(final boolean b) {
    info.limitDGun = b;
    Runnable doWorkRunnable = new Runnable() {
    public void run() {
    LimitDGunCheck.getModel().setSelected(b);
    }
    };
    SwingUtilities.invokeLater(doWorkRunnable);
    }
    public void setDiminMM(final boolean b) {
    info.diminishingMMs = b;
    Runnable doWorkRunnable = new Runnable() {
    public void run() {
    DiminMMCheck.getModel().setSelected(b);
    }
    };
    SwingUtilities.invokeLater(doWorkRunnable);
    }
    public void setGhostedBuildings(final boolean b) {
    info.ghostedBuildings = b;
    Runnable doWorkRunnable = new Runnable() {
    public void run() {
    GhostedCheck.getModel().setSelected(b);
    }
    };
    SwingUtilities.invokeLater(doWorkRunnable);
    }*/
    int BooltoInt(boolean b) {
        if (b == true) {
            return 1;
        } else {
            return 0;
        }
    }

    public static boolean InttoBool(int i) {
        if (i == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void UpdateBattle() {
        battlemodel.CheckSync();
    }

    /**
     * Is called when the minimap changes, it redraws and sets the new minimap
     * image and tooltip, including starting rectangles
     *
     */
    public void MapChange() {
        final String s = info.GetMap();
        SwingUtilities.invokeLater( new Runnable() {

            @Override
            public void run() {
                minimapLabel.setText("loading minimap " + s);
                minimapLabel.setIcon(null);
            }
        });

        SwingWorker worker = new SwingWorker<BufferedImage, Void>() {

            boolean hasmap = true;
            String desc = "";

            @Override
            public BufferedImage doInBackground() {
                String q = s;
                if (CSync.HasMap(s) == false) {
                    hasmap = false;
                    return null;
                //URL r =
                //jLabel7.setIcon (null);
                //q = "Classpath: /aflobby/NoMap.JPG";
                } else {
                    //desc = CUnitSyncJNIBindings.GetMapInfo(s);
                    q = Misc.GetMinimapPath(LM) + s.toLowerCase() + ".jpg";
                }
                CImageLoading il = new CImageLoading();
                BufferedImage newest = il.newestLoadImage(q); //(BufferedImage)
                if (newest == null) {
                    return null;
                }
                //.getScaledInstance(196,196,Image.SCALE_SMOOTH);
//Image a = Toolkit.getDefaultToolkit().getImage(getClass().getResource("file://C:/program files/Spring/lobby/aflobby/maps/"+s+".jpg"));
                // draw out starting rectangles
                Graphics2D g = newest.createGraphics(); //getGraphics ();//createGraphics ();
                g.scale(2.58, 2.58);
                //g.scale (2,2);
                ///* = is.getGraphics ();
                Color c1 = new Color(255, 255, 255, 50);
                Color c2 = new Color(255, 255, 255, 150); // = new Color();
                ArrayList<RectEntry> a = RectHandler.getRectList();
                Iterator<RectEntry> it = a.iterator();
                while (it.hasNext()) {
                    RectEntry r = it.next();
                    //g.setColor (Color.MAGENTA);
                    int x = 1 + r.getStartRecLeft();
                    int y = 1 + r.getStartRecTop();
                    int w = r.getStartRecRight() - r.getStartRecLeft();
                    int h = r.getStartRecBottom() - r.getStartRecTop();
                    //x = (x/200)*512;
                    //y = (y/200)*512;
                    //w = (w/200)*512;
                    //h = (h/200)*512;
                    g.setColor(c1);
                    g.fillRect(x, y, w, h);
                    g.setColor(c2);
                    g.drawRect(x, y, w, h);
                    g.drawString(String.valueOf(r.getNumAlly() + 1), x + (w / 2) - 5, y + (h / 2) + 5);
                //System.out.println (x+" "+y+" "+w+" "+h);
                //g.drawRect (1+(r.getStartRecLeft ()*(200/196)),1+r.getStartRecTop ()*(200/196),(r.getStartRecRight ()-r.getStartRecLeft ())*(200/196),(r.getStartRecTop ()-r.getStartRecBottom ())*(200/196));
                //System.out.println ((r.getStartRecLeft ()*(200/196))+" "+r.getStartRecTop ()+" "+(r.getStartRecRight ()-r.getStartRecLeft ())+" "+(r.getStartRecBottom ()-r.getStartRecTop ()));
                } //*/
                g.dispose();
                return newest;
            }

            @Override
            public void done() {
                if (!hasmap) {
                    AddMessage(java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("CBattleWindow.dontHaveMapPrefix") + s + java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("CBattleWindow.dontHaveMapSuffix"));
                } else {
                    minimapLabel.setText(s+"\n"+desc);
                }
                try {
                    BufferedImage i = get();
                    if (i != null) {
                        Image is = i.getScaledInstance(256, 256, Image.SCALE_SMOOTH);
                        ImageIcon ic = new ImageIcon(is);
                        ImageIcon oldic = (ImageIcon) minimapLabel.getIcon();
                        minimapLabel.setIcon(ic);
                        if (oldic != null) {
                            oldic.getImage().flush();
                            oldic = null;
                        }
                    }
                } catch (ExecutionException ex) {
                    ex.printStackTrace();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        };
        worker.execute();

        if (startrect != null) {
            SwingWorker worker2 = new SwingWorker<BufferedImage, Void>() {

                boolean hasmap = true;

                @Override
                public BufferedImage doInBackground() {
                    String q = s;
                    if (CSync.HasMap(s) == false) {
                        hasmap = false;
                        return null;
                    //URL r =
                    //jLabel7.setIcon (null);
                    //q = "Classpath: /aflobby/NoMap.JPG";
                    } else {
                        q = Misc.GetMinimapPath(LM) + s.toLowerCase() + ".jpg";
                    }

                    CImageLoading il = new CImageLoading();
                    BufferedImage newest = il.newestLoadImage(q); //(BufferedImage)
                    if (newest == null) {
                        return null;
                    }
                    //.getScaledInstance(196,196,Image.SCALE_SMOOTH);
                    //Image a = Toolkit.getDefaultToolkit().getImage(getClass().getResource("file://C:/program files/Spring/lobby/aflobby/maps/"+s+".jpg"));
                    // draw out starting rectangles
                    return newest;
                }

                @Override
                public void done() {
                    try {
                        BufferedImage i = get();
                        if (i != null) {
                            if (startrect != null) {
                                //Image is = i.getScaledInstance(512, 512, Image.SCALE_SMOOTH);
                                ImageIcon ic = new ImageIcon(i);
                                startrect.SetMinimapImage(ic, i);
                            }
                        }
                    } catch (ExecutionException ex) {
                        ex.printStackTrace();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            };
            worker2.execute();
        }
    }

    public void ChangeMap() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                String s = (String) MapList.getSelectedValue();
                String currentMap = info.GetMap();
                if (!s.equalsIgnoreCase(currentMap)) {
                    info.SetMap(s);
                    CUserSettings.PutValue("battle.lastmap", s);
                    info.maphash = CSync.GetMapHash(s);
                    battlemodel.SendUpdateBattle();
                    MapChange();
                }
            }
        });
    }

    private static String getMissingZeros(int length) {
        String zeroString = "";

        for (int i = 0; i < (7 - length); i++) {
            zeroString += "0";
        }

        return zeroString;
    }
    public int numTeams = 0;
    public int numAllies = 0;

    public String GetScript(boolean path) {
        String s = "";
        if (info != null) {
            try {
                //////
                List<CBattlePlayer> a = new ArrayList<CBattlePlayer>();
                List<CBattlePlayer> btemp = battlemodel.GetAllPlayers();

                for (CBattlePlayer z : btemp) {
                    CBattlePlayer r = new CBattlePlayer(z);
                    a.add(r);
                }
                AllyData ad = new AllyData(a);
                TeamData td = new TeamData(a);
                //////////
                //GroupData(Orderedplayers);
                s = "[GAME]\n" + "{\n";
                s += "\tMapname=" + info.GetMap() + ";\n";


                String m = info.GetMod();
                if (m == null) {
                    throw new NullPointerException();
                }

                String an = CSync.mod_name_archive.get(m);
                assert (an != null);

                s += "\tGameType=" + an + ";\n";


                if (this.info.GetHost().name.equalsIgnoreCase(LMain.protocol.GetUsername())) {
                    s += "\tHostIP=localhost;\n";
                } else {
                    s += "\tHostIP=" + info.ip + ";\n";
                }

                s += "\tHostPort=" + info.GetPort() + ";\n\n";

                if(path){
                    s += "\t";
                }
                // mod options

                Map<String, Object> options = battlemodel.GetOptions();

                Map<String, Object> mapOptions = new TreeMap<String, Object>();
                Map<String, Object> modOptions = new TreeMap<String, Object>();
                Map<String, Object> generalOptions = new TreeMap<String, Object>();

                for (String k2 : options.keySet()) {
                    String k = k2.toLowerCase();
                    k.replaceAll("\\\\", "/");

                    if (k.startsWith("game/mapoptions/")) {
                        //
                        mapOptions.put(k.substring(16), options.get(k2));
                    } else if (k.startsWith("game/modoptions/")) {
                        //
                        modOptions.put(k.substring(16), options.get(k2));
                    } else if (k.startsWith("game/")) {
                        //
                        generalOptions.put(k.substring(5), options.get(k2));
                    } else {
                        generalOptions.put(k + "erm", options.get(k2));
                    }
                }

                if (!generalOptions.isEmpty()) {
                    //
                    for (String k : generalOptions.keySet()) {
                        Object v = generalOptions.get(k);
                        String value = "";

                        if ((v.getClass() == boolean.class) || (v.getClass() == Boolean.class)) {
                            value = "" + ((v.toString().equals("true")) ? 1 : 0);
                        } else {
                            value = v.toString();
                        }

                        s += "\t" + k + "=" + value + ";\n";
                    }
                }

                if (!modOptions.isEmpty()) {
                    //
                    s += "\t[modoptions]\n";
                    s += "\t{\n";

                    for (String k : modOptions.keySet()) {
                        s += "\t\t" + k + "=" + modOptions.get(k) + ";\n";
                    }

                    s += "\t}\n\n";
                }

                if (!mapOptions.isEmpty()) {
                    //
                    s += "\t[mapoptions]\n";
                    s += "\t{\n";

                    for (String k : mapOptions.keySet()) {
                        s += "\t\t" + k + "=" + mapOptions.get(k) + ";\n";
                    }

                    s += "\t}\n\n";
                }



                for (int k = 0; k < info.GetPlayers().size(); k++) {
                    String z = info.GetPlayers().get(k).name;
                    if (z.equalsIgnoreCase(LMain.protocol.GetUsername())) {
                        s += "\tmyplayernum=" + k + ";\n\n";
                    }
                }

                s += "\tnumplayers=" + battlemodel.GetPlayers().size() + ";\n";
                s += "\tnumteams=" + td.getTeamCount() + ";\n";
                s += "\tnumallyteams=" + ad.getAllyCount() + ";\n";

                s += "\n";

                // PLAYERS
                for (int i = 0; i < info.GetPlayers().size(); i++) {

                    String z = info.GetPlayers().get(i).name;
                    CBattlePlayer u = battlemodel.GetPlayers().get(z);

                    s += "\t[player" + i + "]\n";
                    s += "\t{\n";
                    s += "\t\tname=" + u.getPlayername() + ";\n";
                    s += "\t\tspectator=" + BooltoInt(u.isSpec()) + ";\n";
                    s += "\t\tcountrycode=" + u.getPlayerdata().getCountry() + ";\n";
                    s += "\t\trank=" + u.getPlayerdata().rank + ";\n";

                    if (!u.isSpec()) {
                        s += "\t\tteam=" + td.GetTeamNo(Misc.getTeamNoFromBattleStatus(u.getBattlestatus())) + ";\n";
                    }

                    s += "\t}\n";
                }

                // TEAMS
                s += "\n";
                //////
                TreeMap<Integer, TeamEntry> orderedTeams = td.getTeamList();
                for (int i = 0; i < td.getTeamCount(); i++) {
                    s += "\t[team" + i + "]\n";
                    s += "\t{\n";


                    int currentKey = orderedTeams.firstKey();
                    TeamEntry te = orderedTeams.remove(currentKey);

                    String ai = te.getAI();

                    if (ai.equalsIgnoreCase("") == false) {
                        CBattlePlayer u = battlemodel.GetPlayers().get(te.getAIOwner());
                        s += "\t\tteamleader=";
                        s += td.GetTeamNo(Misc.getTeamNoFromBattleStatus(u.getBattlestatus()));
                        s += ";\n";
                    } else {
                        s += "\t\tteamleader=" + te.getTeamLeader() + ";\n";
                    }

                    s += "\t\tallyteam=" + te.getAllyNo() + ";\n";


                    if (ai.equalsIgnoreCase("") == false) {
                        s += "\t\taidll=AI/Bot-libs/" + ai + ";\n";
                    } else {
                        s += "\t\t// no AI\n";
                    }

                    Color col = te.getTeamColor();
                    String redCol = "" + (col.getRed() / 255d);
                    String greenCol = "" + (col.getGreen() / 255d);
                    String blueCol = "" + (col.getBlue() / 255d);

                    if (redCol.length() > 7) {
                        redCol = redCol.substring(0, 7);
                    }
                    if (greenCol.length() > 7) {
                        greenCol = greenCol.substring(0, 7);
                    }
                    if (blueCol.length() > 7) {
                        blueCol = blueCol.substring(0, 7);
                    }
                    redCol += getMissingZeros(redCol.length());
                    greenCol += getMissingZeros(greenCol.length());
                    blueCol += getMissingZeros(blueCol.length());

                    s += "\t\tRGBColor=" + redCol + " " + greenCol + " " + blueCol + ";\n";
                    s += "\t\tSide=" + CSync.GetSideName(te.getSide()) + ";\n"; //JUnitSync.GetSideName (te.getSide ())
                    s += "\t\tHandicap=" + te.getHandicap() + ";\n";

                    s += "\t}\n";
                }

                // ALLYS
                TreeMap<Integer, AllyEntry> orderedAllys = ad.getAllyList();
                for (int i = 0; i < ad.getAllyCount(); i++) {
                    s += "\t[ALLYTEAM" + i + "]\n";
                    s += "\t{\n";

                    int currentKey = orderedAllys.firstKey();
                    AllyEntry te = orderedAllys.remove(currentKey);

                    s += "\t\tNumAllies=" + 0 + ";\n";
                    RectEntry re = RectHandler.getRectForAlly(ad.untransform(i));
                    if (re != null) {
                        String[] data = re.getRectDataStr();

                        s += "\t\tStartRectLeft=" + data[0] + ";\n";
                        s += "\t\tStartRectTop=" + data[1] + ";\n";
                        s += "\t\tStartRectRight=" + data[2] + ";\n";
                        s += "\t\tStartRectBottom=" + data[3] + ";\n";
                    }
                    s += "\t}\n";
                }

                s += "\n";
                s += "\tNumRestrictions=" + RestrictedUnits.getSize() + ";\n";
                s += "\t[RESTRICT]\n";
                s += "\t{\n";
                for (int i = 0; i < RestrictedUnits.getSize(); ++i) {
                    s += "\t\tUnit" + i + "=" + RestrictedUnits.get(i) + ";\n";
                    s += "\t\tLimit" + i + "=0;\n";
                }
                s += "\t}\n";
                s += "}";
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            s = "n/a";
        }
        return s;
    }

    private void SendMessage() {
        Runnable doWorkRunnable = new Runnable() {

            @Override
            public void run() {

                String[] lines = UserMessage.getText().split("\n");

                for (int n = 0; n < lines.length; n++) {

                    String k = lines[n];

//                    if (cmdhandler.IsCommand(k)) {
//                        cmdhandler.ExecuteCommand(k);
//                        continue;
//                    }

                    String[] command = k.split(" ");

                    if (command[0].equalsIgnoreCase("/me")) {
                        battlemodel.SendChatActionMessage(Misc.makeSentence(command, 1));
                    } else {
                        battlemodel.SendChatMessage(k);
                    }
                }

                UserMessage.setText("");
                UserMessage.requestFocus();
            }
        };

        SwingUtilities.invokeLater(doWorkRunnable);
    }

    public void SendDisabledUnits() {

        String s = "DISABLEUNITS";

        for (int i = 0; i < RestrictedUnits.getSize(); i++) {
            String a = (String) RestrictedUnits.get(i);
            s += " " + a;
        }

        LMain.protocol.SendTraffic(s);
    }

    public void UpdateMyUIControls() {

        Runnable doWorkRunnable = new Runnable() {

            @Override
            public void run() {
                cansend = false;

                CBattlePlayer me = battlemodel.GetMe();

                MyTeamCombo.setSelectedIndex(me.getTeamNo());
                MyAllyTeamCombo.setSelectedIndex(me.getAllyNo());
                RaceCombo.setSelectedIndex(me.getSideNumber());
                ColourPanel.setBackground(me.getColor());

                cansend = true;
            }
        };

        SwingUtilities.invokeLater(doWorkRunnable);

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        GameSpeedLockCheck = new javax.swing.JCheckBox();
        restrictedUnitsPanel = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        disabledUnitsList = new javax.swing.JList();
        UnRestrictUnitButton = new javax.swing.JButton();
        UnRestrictAllUnitsButton = new javax.swing.JButton();
        RestrictUnitButton = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        enabledUnitsList = new javax.swing.JList();
        mapPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        MapList = new javax.swing.JList();
        ChMapButton = new javax.swing.JButton();
        MapPickerLabel = new javax.swing.JLabel();
        reload_maplist = new javax.swing.JButton();
        scriptPanel = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        ScriptPreview = new javax.swing.JTextArea();
        jButton4 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        gameOptionsPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        OptionsPanel = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        modifyAIPanel = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        RemoveAIButton = new javax.swing.JButton();
        UpdateAIButton = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        AIColourPanel = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        EditAIFactionList = new javax.swing.JList();
        jScrollPane10 = new javax.swing.JScrollPane();
        modifyAIsList = new javax.swing.JList();
        jScrollPane11 = new javax.swing.JScrollPane();
        editAIAllyList = new javax.swing.JList();
        myTeamPanel = new javax.swing.JPanel();
        MyTeamCombo = new javax.swing.JComboBox();
        RaceCombo = new javax.swing.JComboBox();
        MyAllyTeamCombo = new javax.swing.JComboBox();
        ColourButton = new javax.swing.JButton();
        ColourPanel = new javax.swing.JPanel();
        addAIPanel = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        AddAIButton = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox();
        NewAIName = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        AddAIList = new javax.swing.JList();
        jScrollPane8 = new javax.swing.JScrollPane();
        AddAIFactionList = new javax.swing.JList();
        ribbonPanel = new javax.swing.JPanel();
        chatToolBar = new javax.swing.JToolBar();
        jToggleButton3 = new javax.swing.JToggleButton();
        JAutoScrollToggle = new javax.swing.JToggleButton();
        sayButton = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        playerBattleTable = new javax.swing.JTable();
        Chat = new org.darkstars.battlehub.gui.CChatPanel();
        minimapLabel = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        UserMessage = new javax.swing.JTextArea();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/darkstars/battlehub/languages"); // NOI18N
        GameSpeedLockCheck.setText(bundle.getString("CBattleWindow.")); // NOI18N
        GameSpeedLockCheck.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        GameSpeedLockCheck.setMargin(new java.awt.Insets(0, 0, 0, 0));
        GameSpeedLockCheck.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                GameSpeedLockCheckStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(GameSpeedLockCheck)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(GameSpeedLockCheck)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        disabledUnitsList.setFont(CUISettings.GetFont(12,false));
        disabledUnitsList.setModel(RestrictedUnits);
        disabledUnitsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane6.setViewportView(disabledUnitsList);

        UnRestrictUnitButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/list-remove.png"))); // NOI18N
        UnRestrictUnitButton.setText(bundle.getString("CBattleWindow.UnRestrict_Unit")); // NOI18N
        UnRestrictUnitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UnRestrictUnitButtonActionPerformed(evt);
            }
        });

        UnRestrictAllUnitsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/edit-clear.png"))); // NOI18N
        UnRestrictAllUnitsButton.setText(bundle.getString("CBattleWindow.UnRestrict_All_Units")); // NOI18N
        UnRestrictAllUnitsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UnRestrictAllUnitsButtonActionPerformed(evt);
            }
        });

        RestrictUnitButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/list-add.png"))); // NOI18N
        RestrictUnitButton.setText(bundle.getString("CBattleWindow.Restrict_Unit")); // NOI18N
        RestrictUnitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RestrictUnitButtonActionPerformed(evt);
            }
        });

        enabledUnitsList.setModel(nonRestrictedUnits);
        jScrollPane5.setViewportView(enabledUnitsList);

        javax.swing.GroupLayout restrictedUnitsPanelLayout = new javax.swing.GroupLayout(restrictedUnitsPanel);
        restrictedUnitsPanel.setLayout(restrictedUnitsPanelLayout);
        restrictedUnitsPanelLayout.setHorizontalGroup(
            restrictedUnitsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(restrictedUnitsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(restrictedUnitsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(restrictedUnitsPanelLayout.createSequentialGroup()
                        .addComponent(UnRestrictAllUnitsButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(restrictedUnitsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(RestrictUnitButton, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                            .addComponent(UnRestrictUnitButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, restrictedUnitsPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        restrictedUnitsPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jScrollPane5, jScrollPane6});

        restrictedUnitsPanelLayout.setVerticalGroup(
            restrictedUnitsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(restrictedUnitsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(restrictedUnitsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane5)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RestrictUnitButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(restrictedUnitsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(UnRestrictUnitButton)
                    .addComponent(UnRestrictAllUnitsButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mapPanel.setPreferredSize(new java.awt.Dimension(800, 600));

        MapList.setFont(CUISettings.GetFont(12,false));
        MapList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        MapList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                MapListValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(MapList);

        ChMapButton.setFont(CUISettings.GetFont(12,false));
        ChMapButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/categories/applications-internet.png"))); // NOI18N
        ChMapButton.setText(bundle.getString("CBattleWindow.Change_Map")); // NOI18N
        ChMapButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChMapButtonActionPerformed(evt);
            }
        });

        MapPickerLabel.setText(bundle.getString("CBattleWindow.Select_a_map_from_below")); // NOI18N
        MapPickerLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        MapPickerLabel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        reload_maplist.setFont(CUISettings.GetFont(12,false));
        reload_maplist.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/view-refresh.png"))); // NOI18N
        reload_maplist.setText(bundle.getString("CBattleWindow.Reload_Maplist")); // NOI18N
        reload_maplist.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        reload_maplist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reload_maplistActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mapPanelLayout = new javax.swing.GroupLayout(mapPanel);
        mapPanel.setLayout(mapPanelLayout);
        mapPanelLayout.setHorizontalGroup(
            mapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mapPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(MapPickerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mapPanelLayout.createSequentialGroup()
                        .addComponent(reload_maplist, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ChMapButton, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
                .addContainerGap())
        );
        mapPanelLayout.setVerticalGroup(
            mapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mapPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
                    .addGroup(mapPanelLayout.createSequentialGroup()
                        .addComponent(MapPickerLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                        .addGap(41, 41, 41)
                        .addGroup(mapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(reload_maplist, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ChMapButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        mapPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {ChMapButton, reload_maplist});

        scriptPanel.setEnabled(Main.dev_environment);
        scriptPanel.setMinimumSize(new java.awt.Dimension(300, 300));

        ScriptPreview.setColumns(20);
        ScriptPreview.setEditable(false);
        ScriptPreview.setFont(new java.awt.Font("Arial", 0, 13));
        ScriptPreview.setLineWrap(true);
        ScriptPreview.setRows(5);
        ScriptPreview.setText("Please press refresh below"); // NOI18N
        ScriptPreview.setWrapStyleWord(true);
        jScrollPane12.setViewportView(ScriptPreview);

        jButton4.setFont(CUISettings.GetFont(12,false));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/view-refresh.png"))); // NOI18N
        jButton4.setText(bundle.getString("CBattleWindow.refresh_debug_script")); // NOI18N
        jButton4.setBorderPainted(false);
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton4MouseReleased(evt);
            }
        });

        jLabel8.setText(bundle.getString("CBattleWindow.debugScriptNote")); // NOI18N

        javax.swing.GroupLayout scriptPanelLayout = new javax.swing.GroupLayout(scriptPanel);
        scriptPanel.setLayout(scriptPanelLayout);
        scriptPanelLayout.setHorizontalGroup(
            scriptPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scriptPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(scriptPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, scriptPanelLayout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        scriptPanelLayout.setVerticalGroup(
            scriptPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, scriptPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(scriptPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE))
                .addContainerGap())
        );

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        OptionsPanel.setLayout(new javax.swing.BoxLayout(OptionsPanel, javax.swing.BoxLayout.PAGE_AXIS));
        OptionsPanel.add(jSeparator2);

        jScrollPane1.setViewportView(OptionsPanel);

        javax.swing.GroupLayout gameOptionsPanelLayout = new javax.swing.GroupLayout(gameOptionsPanel);
        gameOptionsPanel.setLayout(gameOptionsPanelLayout);
        gameOptionsPanelLayout.setHorizontalGroup(
            gameOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gameOptionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 587, Short.MAX_VALUE)
                .addContainerGap())
        );
        gameOptionsPanelLayout.setVerticalGroup(
            gameOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gameOptionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel10.setFont(jLabel10.getFont().deriveFont(jLabel10.getFont().getSize()+6f));
        jLabel10.setText("Modify an AI"); // NOI18N

        jLabel11.setText("AI to edit:"); // NOI18N

        jLabel12.setText("AI Race:"); // NOI18N

        jLabel13.setText("AI Ally:"); // NOI18N

        RemoveAIButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/edit-delete.png"))); // NOI18N
        RemoveAIButton.setText("Remove This AI"); // NOI18N
        RemoveAIButton.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 0, 0), 1, true));
        RemoveAIButton.setEnabled(false);
        RemoveAIButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveAIButtonActionPerformed(evt);
            }
        });

        UpdateAIButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/document-save.png"))); // NOI18N
        UpdateAIButton.setText("Update AI"); // NOI18N
        UpdateAIButton.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 204, 0), 1, true));
        UpdateAIButton.setEnabled(false);
        UpdateAIButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateAIButtonActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/UI/colourpickerwheel.png"))); // NOI18N
        jButton3.setText("Change Colour"); // NOI18N
        jButton3.setEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        AIColourPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        javax.swing.GroupLayout AIColourPanelLayout = new javax.swing.GroupLayout(AIColourPanel);
        AIColourPanel.setLayout(AIColourPanelLayout);
        AIColourPanelLayout.setHorizontalGroup(
            AIColourPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 126, Short.MAX_VALUE)
        );
        AIColourPanelLayout.setVerticalGroup(
            AIColourPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 45, Short.MAX_VALUE)
        );

        EditAIFactionList.setModel(factionListModel);
        jScrollPane9.setViewportView(EditAIFactionList);

        modifyAIsList.setModel(aisListModel);
        modifyAIsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                modifyAIsListValueChanged(evt);
            }
        });
        jScrollPane10.setViewportView(modifyAIsList);

        editAIAllyList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Ally Team 1", "Ally Team 2", "Ally Team 3", "Ally Team 4", "Ally Team 5", "Ally Team 6", "Ally Team 7", "Ally Team 8", "Ally Team 9", "Ally Team 10", "Ally Team 11", "Ally Team 12", "Ally Team 13", "Ally Team 14", "Ally Team 15", "Ally Team 16" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane11.setViewportView(editAIAllyList);

        javax.swing.GroupLayout modifyAIPanelLayout = new javax.swing.GroupLayout(modifyAIPanel);
        modifyAIPanel.setLayout(modifyAIPanelLayout);
        modifyAIPanelLayout.setHorizontalGroup(
            modifyAIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(modifyAIPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(modifyAIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(modifyAIPanelLayout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE))
                    .addGroup(modifyAIPanelLayout.createSequentialGroup()
                        .addGroup(modifyAIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13))
                        .addGap(11, 11, 11)
                        .addGroup(modifyAIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, modifyAIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(modifyAIPanelLayout.createSequentialGroup()
                            .addComponent(RemoveAIButton, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(UpdateAIButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(modifyAIPanelLayout.createSequentialGroup()
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(AIColourPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        modifyAIPanelLayout.setVerticalGroup(
            modifyAIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(modifyAIPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(modifyAIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(modifyAIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(modifyAIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(modifyAIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(modifyAIPanelLayout.createSequentialGroup()
                        .addComponent(AIColourPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(modifyAIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(RemoveAIButton, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(UpdateAIButton, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(modifyAIPanelLayout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                        .addGap(32, 32, 32)))
                .addContainerGap())
        );

        modifyAIPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {AIColourPanel, jButton3});

        modifyAIPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {RemoveAIButton, UpdateAIButton});

        MyTeamCombo.setFont(CUISettings.GetFont(12,false));
        MyTeamCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Team 1", "Team 2", "Team 3", "Team 4", "Team 5", "Team 6", "Team 7", "Team 8", "Team 9", "Team 10", "Team 11", "Team 12", "Team 13", "Team 14", "Team 15", "Team 16" }));
        MyTeamCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MyTeamComboActionPerformed(evt);
            }
        });

        RaceCombo.setFont(CUISettings.GetFont(12,false));
        RaceCombo.setMaximumSize(new java.awt.Dimension(70, 25));
        RaceCombo.setPreferredSize(new java.awt.Dimension(120, 25));
        RaceCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RaceComboActionPerformed(evt);
            }
        });

        MyAllyTeamCombo.setFont(CUISettings.GetFont(12,false));
        MyAllyTeamCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ally Team 1", "Ally Team 2", "Ally Team 3", "Ally Team 4", "Ally Team 5", "Ally Team 6", "Ally Team 7", "Ally Team 8", "Ally Team 9", "Ally Team 10", "Ally Team 11", "Ally Team 12", "Ally Team 13", "Ally Team 14", "Ally Team 15", "Ally Team 16" }));
        MyAllyTeamCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MyAllyTeamComboActionPerformed(evt);
            }
        });

        ColourButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/UI/colourpickerwheel16.png"))); // NOI18N
        ColourButton.setText("Change Colour"); // NOI18N
        ColourButton.setMargin(new java.awt.Insets(2, 6, 2, 6));
        ColourButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ColourButtonActionPerformed(evt);
            }
        });

        ColourPanel.setBackground(Color.decode(CUserSettings.GetValue("lastbattle.colour", String.valueOf(Color.blue.getRGB()))));
        ColourPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        javax.swing.GroupLayout ColourPanelLayout = new javax.swing.GroupLayout(ColourPanel);
        ColourPanel.setLayout(ColourPanelLayout);
        ColourPanelLayout.setHorizontalGroup(
            ColourPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 74, Short.MAX_VALUE)
        );
        ColourPanelLayout.setVerticalGroup(
            ColourPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout myTeamPanelLayout = new javax.swing.GroupLayout(myTeamPanel);
        myTeamPanel.setLayout(myTeamPanelLayout);
        myTeamPanelLayout.setHorizontalGroup(
            myTeamPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, myTeamPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(myTeamPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(MyAllyTeamCombo, 0, 0, Short.MAX_VALUE)
                    .addComponent(MyTeamCombo, 0, 80, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(myTeamPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(myTeamPanelLayout.createSequentialGroup()
                        .addComponent(ColourButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ColourPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(RaceCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        myTeamPanelLayout.setVerticalGroup(
            myTeamPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(myTeamPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(myTeamPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(MyTeamCombo, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(RaceCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(myTeamPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ColourPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(myTeamPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(MyAllyTeamCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(ColourButton)))
                .addContainerGap())
        );

        myTeamPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {MyAllyTeamCombo, MyTeamCombo, RaceCombo});

        jLabel9.setFont(jLabel9.getFont().deriveFont(jLabel9.getFont().getSize()+6f));
        jLabel9.setText("Add an AI"); // NOI18N

        AddAIButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/list-add.png"))); // NOI18N
        AddAIButton.setText("Add AI"); // NOI18N
        AddAIButton.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 0), 1, true));
        AddAIButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddAIButtonActionPerformed(evt);
            }
        });

        jLabel14.setText("Choose an AI:"); // NOI18N

        jLabel15.setText("Pick a name:"); // NOI18N

        jLabel17.setText("AI Race:"); // NOI18N

        jLabel18.setText("Difficulty:"); // NOI18N

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Easy", "Moderate", "Difficult" }));
        jComboBox5.setEnabled(false);

        jLabel16.setText("(no spaces in AI names)"); // NOI18N

        AddAIList.setModel(AIListModel);
        jScrollPane7.setViewportView(AddAIList);

        AddAIFactionList.setModel(factionListModel);
        jScrollPane8.setViewportView(AddAIFactionList);

        javax.swing.GroupLayout addAIPanelLayout = new javax.swing.GroupLayout(addAIPanel);
        addAIPanel.setLayout(addAIPanelLayout);
        addAIPanelLayout.setHorizontalGroup(
            addAIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addAIPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addAIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(addAIPanelLayout.createSequentialGroup()
                        .addGroup(addAIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel17)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(addAIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(addAIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(NewAIName, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE))))
                    .addComponent(AddAIButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addGroup(addAIPanelLayout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addComponent(jComboBox5, 0, 141, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addContainerGap())
        );

        addAIPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {NewAIName, jComboBox5, jScrollPane7, jScrollPane8});

        addAIPanelLayout.setVerticalGroup(
            addAIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addAIPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addAIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(addAIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(NewAIName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addAIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addAIPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AddAIButton, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("BattleName :: Server"); // NOI18N
        setExtendedState(MAXIMIZED_BOTH);
        setIconImage(CUISettings.GetWindowIcon());
        setMinimumSize(new java.awt.Dimension(600, 600));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        ribbonPanel.setLayout(new java.awt.BorderLayout());

        chatToolBar.setFloatable(false);

        jToggleButton3.setFont(CUISettings.GetFont(12,false));
        jToggleButton3.setSelected(true);
        jToggleButton3.setText(bundle.getString("CBattleWindow.Accumulate_Messages")); // NOI18N
        jToggleButton3.setMaximumSize(new java.awt.Dimension(105, 21));
        chatToolBar.add(jToggleButton3);

        JAutoScrollToggle.setFont(CUISettings.GetFont(12,false));
        JAutoScrollToggle.setSelected(true);
        JAutoScrollToggle.setText(bundle.getString("CBattleWindow.AutoScroll")); // NOI18N
        JAutoScrollToggle.setMaximumSize(new java.awt.Dimension(65, 21));
        JAutoScrollToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JAutoScrollToggleActionPerformed(evt);
            }
        });
        chatToolBar.add(JAutoScrollToggle);

        sayButton.setText(bundle.getString("CBattleWindow.Say")); // NOI18N
        sayButton.setMargin(new java.awt.Insets(2, 6, 2, 6));
        sayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sayButtonActionPerformed(evt);
            }
        });

        jSplitPane1.setDividerLocation(220);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(0.5);
        jSplitPane1.setOneTouchExpandable(true);

        playerBattleTable.setModel(playertablemodel);
        playerBattleTable.setRowHeight(20);
        playerBattleTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        playerBattleTable.setShowHorizontalLines(false);
        playerBattleTable.setShowVerticalLines(false);
        jScrollPane2.setViewportView(playerBattleTable);

        jSplitPane1.setTopComponent(jScrollPane2);

        javax.swing.GroupLayout ChatLayout = new javax.swing.GroupLayout(Chat);
        Chat.setLayout(ChatLayout);
        ChatLayout.setHorizontalGroup(
            ChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 295, Short.MAX_VALUE)
        );
        ChatLayout.setVerticalGroup(
            ChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 48, Short.MAX_VALUE)
        );

        jSplitPane1.setRightComponent(Chat);

        minimapLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        minimapLabel.setText(bundle.getString("CBattleWindow.map")); // NOI18N
        minimapLabel.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        minimapLabel.setAlignmentX(0.5F);
        minimapLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        minimapLabel.setMaximumSize(new java.awt.Dimension(260, 270));
        minimapLabel.setMinimumSize(new java.awt.Dimension(260, 270));
        minimapLabel.setPreferredSize(new java.awt.Dimension(260, 270));
        minimapLabel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        UserMessage.setColumns(20);
        UserMessage.setLineWrap(true);
        UserMessage.setRows(2);
        UserMessage.setWrapStyleWord(true);
        UserMessage.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                UserMessageKeyPressed(evt);
            }
        });
        jScrollPane4.setViewportView(UserMessage);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ribbonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 581, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sayButton, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(chatToolBar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(minimapLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(ribbonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(minimapLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chatToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sayButton, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        chatToolBar.add(CSmileyManager.GetSmileyPicker(UserMessage));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void UnRestrictAllUnitsButtonActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UnRestrictAllUnitsButtonActionPerformed
        LMain.protocol.SendTraffic("ENABLEALLUNITS");
        Object[] elements = RestrictedUnits.toArray();
        for (Object o : elements) {
            nonRestrictedUnits.addElement(o);
        }
        RestrictedUnits.clear();
    }//GEN-LAST:event_UnRestrictAllUnitsButtonActionPerformed

    private void UnRestrictUnitButtonActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UnRestrictUnitButtonActionPerformed
        String s = (String) disabledUnitsList.getSelectedValue();

        RestrictedUnits.removeElement(s);
        nonRestrictedUnits.addElement(s);

        SendDisabledUnits();
    }//GEN-LAST:event_UnRestrictUnitButtonActionPerformed

    private void RestrictUnitButtonActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RestrictUnitButtonActionPerformed
        String s = (String) enabledUnitsList.getSelectedValue();

        RestrictedUnits.addElement(s);
        nonRestrictedUnits.removeElement(s);

        SendDisabledUnits();
    }//GEN-LAST:event_RestrictUnitButtonActionPerformed

    private void JAutoScrollToggleActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JAutoScrollToggleActionPerformed
        Chat.SetAutoScroll(JAutoScrollToggle.isSelected());
    }//GEN-LAST:event_JAutoScrollToggleActionPerformed

    private void reload_maplistActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reload_maplistActionPerformed
        CSync.RefreshUnitSync();
    }//GEN-LAST:event_reload_maplistActionPerformed

    private void MapListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_MapListValueChanged
        final String s = (String) MapList.getSelectedValue();

        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run() {
                MapPickerLabel.setText("loading minimap " + s);
                MapPickerLabel.setIcon(null);
            }
        });

        SwingWorker worker = new SwingWorker<ImageIcon, Void>() {

            @Override
            public ImageIcon doInBackground() {
                CImageLoading il = new CImageLoading();
                Image newest = il.olderLoadImage(Misc.GetMinimapPath(LM) + s.toLowerCase() + ".jpg");
                return new ImageIcon(newest.getScaledInstance(400, 400, Image.SCALE_SMOOTH));
            }

            @Override
            public void done() {
                try {
                    MapPickerLabel.setIcon(get());
                } catch (ExecutionException ex) {
                    ex.printStackTrace();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                MapPickerLabel.setText("");
            }
        };
        worker.execute();
    }//GEN-LAST:event_MapListValueChanged

    private void jButton4MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseReleased
        ScriptPreview.setText(GetScript(false));
    }//GEN-LAST:event_jButton4MouseReleased

    /*public void stateChanged(ChangeEvent e) {
    if(this.info == null) return;
    if(ColourWheel == e.getSource()){
    Color c = ColourWheel.getColor();
    if(!c.equals(mycolour)){
    mycolour = c;
    SendMyStatus();
    }
    }
    }*/
    private void GameSpeedLockCheckStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_GameSpeedLockCheckStateChanged
        if (battlemodel.AmIHost()) {
            //info. = GameSpeedLockCheck.isSelected();
            //UpdateBattleClients();
        }
    }//GEN-LAST:event_GameSpeedLockCheckStateChanged

    //private boolean wipenext=false;
    private void AddAIButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddAIButtonActionPerformed
        //ADDBOT name battlestatus teamcolor {AIDLL}
        String s = "ADDBOT ";
        String name = NewAIName.getText().replaceAll(" ", "");
        if (name.equals("")) {
            return;
        }
        s += name + " ";
        CBattlePlayer bp = new CBattlePlayer();
        bp.setSpectator(false);
        bp.setTeamNo(battlemodel.GetFirstFreeTeam());
        bp.setAllyNo(battlemodel.GetFirstFreeAlly());
        bp.setSide(AddAIFactionList.getSelectedIndex());
        s += bp.getBattlestatus() + " ";
        s += ColourHelper.ColorToInteger(Color.BLUE).intValue() + " ";
        s += (String) AddAIList.getSelectedValue();
        LMain.protocol.SendTraffic(s);
    }//GEN-LAST:event_AddAIButtonActionPerformed

    private void RaceComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RaceComboActionPerformed
        if (battlemodel.GetMe() == null) {
            return;
        }
        int i = RaceCombo.getSelectedIndex();
        battlemodel.GetMe().setSide(i); //mybattlestatus = Misc.setSideOfBattleStatus (mybattlestatus,i);
        SendMyStatus();
    }//GEN-LAST:event_RaceComboActionPerformed

    private void MyAllyTeamComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MyAllyTeamComboActionPerformed
        int i = MyAllyTeamCombo.getSelectedIndex();
        battlemodel.GetMe().setAllyNo(i); //mybattlestatus = Misc.setAllyNoOfBattleStatus (mybattlestatus,i);
        SendMyStatus();
    }//GEN-LAST:event_MyAllyTeamComboActionPerformed

    private void MyTeamComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MyTeamComboActionPerformed
        int i = MyTeamCombo.getSelectedIndex();
        battlemodel.GetMe().setTeamNo(i); //mybattlestatus = Misc.setTeamNoOfBattleStatus (mybattlestatus,i);
        SendMyStatus();
    }//GEN-LAST:event_MyTeamComboActionPerformed

    private void RemoveAIButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveAIButtonActionPerformed
        String ainame = (String) modifyAIsList.getSelectedValue();
        LMain.protocol.SendTraffic("REMOVEBOT " + ainame);
}//GEN-LAST:event_RemoveAIButtonActionPerformed

    private void UpdateAIButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateAIButtonActionPerformed
        String ainame = (String) modifyAIsList.getSelectedValue();
        CBattlePlayer bp = battlemodel.GetAIPlayers().get(ainame);
        if (bp == null) {
            return;
        }
        bp.setColor(aicolour);
        bp.setSide(EditAIFactionList.getSelectedIndex());
        bp.setAllyNo(editAIAllyList.getSelectedIndex());
        // UPDATEBOT name battlestatus teamcolor
        String s = "UPDATEBOT " + ainame;
        s += " " + bp.getBattlestatus();
        s += " " + ColourHelper.ColorToInteger(bp.getColor()).intValue();
        LMain.protocol.SendTraffic(s);
}//GEN-LAST:event_UpdateAIButtonActionPerformed

    private void ChMapButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChMapButtonActionPerformed
        if (battlemodel.AmIHost()) {
            // change map!
            ChangeMap();
        } else {
            // suggest we change the map!
            String s = (String) MapList.getSelectedValue();
            s = java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("CBattleWindow.suggests_this_map:_") + s;
            LMain.protocol.SendTraffic("SAYBATTLEEX " + s);
        //CUserSettings.PutValue("UI.lasthosted.map", s);
        }
    }//GEN-LAST:event_ChMapButtonActionPerformed
    boolean closing = false;

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        if (!closing) {
            LeaveBattle();
            closing = true;
        }
    }//GEN-LAST:event_formWindowClosed

    private void UserMessageKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_UserMessageKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (evt.isShiftDown()) {
                return;
            }
            SendMessage();
        }
    }//GEN-LAST:event_UserMessageKeyPressed

    private void ColourButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ColourButtonActionPerformed
        // TODO add your handling code here:
        Color c = LM.ShowColorDialog(this, "Pick a Colour");
        if (c == null) {
            return;
        }
        ColourPanel.setBackground(c);
        battlemodel.GetMe().setColor(c);
        CUserSettings.PutValue("lastbattle.colour", String.valueOf(c.getRGB()));
        colorchange = true;
    }//GEN-LAST:event_ColourButtonActionPerformed
    Color aicolour = Color.black;
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Color c = LM.ShowColorDialog(this, "Pick a Colour");
        if (c == null) {
            return;
        } else {
            aicolour = c;
        }
        AIColourPanel.setBackground(c);
}//GEN-LAST:event_jButton3ActionPerformed

    private void sayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sayButtonActionPerformed
        SendMessage();
}//GEN-LAST:event_sayButtonActionPerformed

private void modifyAIsListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_modifyAIsListValueChanged

    String name = (String) modifyAIsList.getSelectedValue();
    if (name == null) {
        return;
    }

    CBattlePlayer bp = battlemodel.GetAIPlayers().get(name);

    if (bp == null) {
        return;
    }

    editAIAllyList.setSelectedIndex(bp.getAllyNo());

    aicolour = bp.getColor();

    int side = bp.getSideNumber();
    EditAIFactionList.setSelectedIndex(side);
}//GEN-LAST:event_modifyAIsListValueChanged
    //}
    public void AddMessage(final String s) {
        Chat.AddMessage(s); // + "<br>");
    }
    public boolean scroll = false;
    boolean colorchange = false;
    int scrollcount = 10;

    @Override
    public void Update() {
        scrollcount--;

        if (scrollcount < 1) {
            scrollcount = 10;

            if (colorchange) {
                colorchange = false;
                this.SendMyStatus();
            }

            if (info.natType == 1) {
                if (ingame == false) {
                    if (!CHolePuncher.SendRequest(LMain.protocol.GetUsername())) {
                        LMain.Toasts.AddMessage(java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("CBattleWindow.Holepunching_failed"));
                        return; // holepunching failed we dont start
                    }
                }
            }

        }
    }

    @Override
    public void NewEvent(final CEvent e) {

        if (info == null) {
            return;
        }

        if (!active) {
            return;
        }

        if (e.IsEvent("SAIDBATTLE")) {

            String Msg = Misc.toHTML(Misc.makeSentence(e.data, 2));
            String User = e.data[1];

            if (lastsent.equalsIgnoreCase(User)) {

                AddMessage("<font face=\"Arial, Helvetica, sans-serif\" size=\"3\">&nbsp;&nbsp;&nbsp;" + Msg + "</font>");

            } else if (lastsent.equalsIgnoreCase("")) {
                AddMessage("<font face=\"Arial, Helvetica, sans-serif\" size=\"3\"><b><i>" + User + java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("CBattleWindow.Says") + " " + Msg + "</font>");
                lastsent = User;
            } else if (lastsent.equalsIgnoreCase(User) == false) {
                AddMessage("<font face=\"Arial, Helvetica, sans-serif\" size=\"3\"><b><i>" + User + java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("CBattleWindow.Says") + " " + Msg + "</font>");
                lastsent = User;
            } //
        } else if (e.IsEvent("REMOVEBOT")) {

            //REMOVEBOT BATTLE_ID name

            CBattlePlayer ai = battlemodel.GetAI(e.data[2]);
            playertablemodel.RemovePlayer(ai);

            battlemodel.RemoveAI(e.data[2]);

            Runnable doWorkRunnable = new Runnable() {

                @Override
                public void run() {

                    //if (MyAIsCombo.getItemCount() == 1) {
                    //     MyAIsCombo.addItem(" ");
                    //}

                    aisListModel.removeElement(e.data[2]);

                    if (aisListModel.getSize() < 1) {
                        editAIAllyList.setEnabled(false);
                        EditAIFactionList.setEnabled(false);
                        RemoveAIButton.setEnabled(false);
                        UpdateAIButton.setEnabled(false);
                        jButton3.setEnabled(false);
                    }

                }
            };
            SwingUtilities.invokeLater(doWorkRunnable);

            //info.GetPlayerNames ().add (e.data[2]);
            AddMessage("<font face=\"Arial, Helvetica, sans-serif\" size=\"3\"><b>" + e.data[2] + " AI removed by " + e.data[2] + "</b></font>");

            battlemodel.SendUpdateBattle();
            Redraw();

        } else if (e.IsEvent("ADDBOT")) {

            //ADDBOT BATTLE_ID name owner battlestatus teamcolor {AIDLL}

            CBattlePlayer pl = new CBattlePlayer();

            pl.setPlayername(e.data[2]);
            pl.setAIOwner(e.data[3]);
            pl.setBattlestatus(Integer.parseInt(e.data[4]));
            pl.setPlayerdata(LMain.playermanager.GetPlayer(e.data[3]));
            pl.setColor(ColourHelper.IntegerToColor(Integer.parseInt(e.data[5])));

            String t = Misc.makeSentence(e.data, 6).replaceAll("\t", "");
            pl.setAI(t);
            pl.setSpectator(false);

            battlemodel.AddAI(e.data[2], pl);

            playertablemodel.AddPlayer(pl);


            String flag;

            if (CUserSettings.GetValue("ui.channelview.userjoinflags", "true").equals("false")) {
                flag = "";
            } else {
                flag = pl.getPlayerdata().GetFlagHTML(LM);
            }

            AddMessage("<font face=\"Arial, Helvetica, sans-serif\" size=\"3\"><b>" + flag + " AI added by " + e.data[2] + "</b></font>");

            if (e.data[3].equalsIgnoreCase(LMain.protocol.GetUsername()) || battlemodel.AmIHost()) {
                //
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {

                        aisListModel.addElement(e.data[2]);

                        editAIAllyList.setEnabled(true);
                        EditAIFactionList.setEnabled(true);
                        RemoveAIButton.setEnabled(true);
                        UpdateAIButton.setEnabled(true);
                        jButton3.setEnabled(true);

                    }
                });
            }


            battlemodel.SendUpdateBattle();
            Redraw();

        } else if (e.IsEvent("DISABLEUNITS")) {

            Runnable doWorkRunnable = new Runnable() {

                @Override
                public void run() {

                    for (int i = 1; i < e.data.length; i++) {
                        if (RestrictedUnits.contains(e.data[i]) == false) {
                            RestrictedUnits.addElement(e.data[i]);
                        }
                    }

                }
            };

            SwingUtilities.invokeLater(doWorkRunnable);

        } else if (e.IsEvent("ENABLEUNITS")) {
            //ENABLEALLUNITS
            Runnable doWorkRunnable = new Runnable() {

                @Override
                public void run() {

                    for (int i = 1; i < e.data.length; i++) {
                        RestrictedUnits.removeElement(e.data[i]);
                    }

                }
            };

            SwingUtilities.invokeLater(doWorkRunnable);

        } else if (e.IsEvent("ENABLEALLUNITS")) {

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    RestrictedUnits.clear();
                }
            });

        } else if (e.IsEvent("SAIDBATTLEEX")) {
            AddMessage("<font face=\"Arial, Helvetica, sans-serif\" size=\"3\" color=\"3322FF\"><b> " + Misc.makeSentence(e.data, 1) + "</b></font>");
            lastsent = e.data[2];

        } else if (e.IsEvent("UDPSOURCEPORT")) {
            MyUDPPort = Integer.parseInt(e.data[1]);

        } else if (e.IsEvent("CLIENTSTATUS")) {

            if (!battlemodel.AmIHost()) {
                if (info != null) {

                    if (e.data[1].equalsIgnoreCase(info.GetHost().name)) {

                        int status = Integer.parseInt(e.data[2]);
                        if (Misc.getInGameFromStatus(status) > 0) {
                            if (ingame == false) {
                                if (battlemodel.GetMe().isSpec() || battlemodel.GetMe().isReady()) {
                                    battlemodel.Start();
                                }
                            }
                        } else {
                            ingame = false;
                        }
                    }
                }
            }

        } else if (e.IsEvent("UPDATEBATTLEINFO")) {
            // UPDATEBATTLEINFO BATTLE_ID SpectatorCount locked maphash {mapname}

//            if (!battlemodel.AmIHost()) {
//                if (Integer.parseInt(e.data[1]) == info.GetID()) {
//                    this.MapChange();
//                }
//            }
            // 3

            boolean tlocked = (Integer.parseInt(e.data[3]) == 1);
            if (tlocked) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        lockButton.setText("Unlock game");
                    }
                });


            } else {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        lockButton.setText("Lock game");
                    }
                });

            }
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    lockButton.validate();
                }
            });


        } else if (e.IsEvent("ADDSTARTRECT")) {
            // ADDSTARTRECT allyno left top right bottom

            int allynum = Integer.parseInt(e.data[1]);


            RectHandler.SetRect(allynum, Integer.parseInt(e.data[2]), Integer.parseInt(e.data[3]), Integer.parseInt(e.data[4]), Integer.parseInt(e.data[5]));
            MapChange();

        } else if (e.IsEvent("REMOVESTARTRECT")) {
            // REMOVESTARTRECT allyno

            RectHandler.RemoveRect(Integer.parseInt(e.data[1]));
            MapChange();

        }
    }
    public boolean cansend = false;

    public void SendMyStatus() {

        if (!cansend) {
            return;
        }

        // check sync again
        battlemodel.CheckSync();

        //MYBATTLESTATUS battlestatus myteamcolor

        String s = "MYBATTLESTATUS ";
        s += battlemodel.GetMe().getBattlestatus() + " ";

        int color = ColourHelper.ColorToInteger(battlemodel.GetMe().getColor()).intValue();
        s += color;

        if (LMain.protocol != null) {
            LMain.protocol.SendTraffic(s);
        }
    }

    public void UpdateBattleClients() {
        this.UpdateClientsNeeded = true;
    }

    public void DoUpdateBattleClients() {
        //UPDATEBATTLEDETAILS startingmetal startingenergy maxunits startpos gameendcondition limitdgun diminishingMMs ghostedBuildings

        String s = "SETSCRIPTTAGS ";
        /*= "UPDATEBATTLEDETAILS ";
        s += info.metal + " ";
        s += info.energy + " ";
        s += info.maxunits + " ";
        s += info.startPos + " ";
        s += info.gameEndCondition + " ";
        s +=  + " ";
        s +=  + " ";
        s += ;*/

        /*String pair = "GAME/StartMetal="+info.metal;
        s = pair;
        
        pair = "\tGAME/StartEnergy="+info.energy;
        s += pair;
        
        pair = "\tGAME/MaxUnits="+info.maxunits;
        s += pair;*/

        //pair = "\tGAME/StartPosType="+info.startPos;
        //s += pair;


        /*pair = "\tGAME/LimitDGun="+BooltoInt(info.limitDGun);
        s += pair;
        
        pair = "\tGAME/DiminishingMMs="+BooltoInt(info.diminishingMMs);
        s += pair;
        
        pair = "\tGAME/GhostedBuildings="+BooltoInt(info.ghostedBuildings);
        s += pair;*/

        Map<String, Object> options = battlemodel.GetOptions();

        for (String key : options.keySet()) {
            //
            Object v = options.get(key);

            String value = "";
            if ((v.getClass() == boolean.class) || (v.getClass() == Boolean.class)) {
                value = "" + ((v.toString().equals("true")) ? 1 : 0);
            } else {
                value = v.toString();
            }

            key = key.replaceAll("/", "\\\\");

            String pair = "\t" + key + "=" + value;
            s += pair;
        }




        //s += pair+"\t";


        LMain.protocol.SendTraffic(s);
        UpdateClientsNeeded = false;
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public void NewGUIEvent(final CEvent e) {

        if (e.IsEvent(CEvent.LOGGEDOUT) || e.IsEvent(CEvent.LOGOUT) || e.IsEvent(CEvent.DISCONNECTED) || e.IsEvent(CEvent.DISCONNECT)) {

            Runnable doWorkRunnable = new  

                  Runnable() {













                    
                
            
            
            
            
               
              
            
            
        
    

    @ 
     Override 
          

            public  void run() {
                dispose();
            }
        };


    
     SwingUtilities.invokeLater 
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      

       
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      

      ( doWorkRunnable ) 
           ;

    }

       
          else if (e.IsEvent(CEvent.CONTENTREFRESH)) {
        
            MapList.removeAll();
            Object[] m = CSync.map_names.values().toArray().clone();
            Arrays.sort(m, new AlphanumComparator());
            MapList.setListData(m);
            battlemodel.CheckSync();
        }
    }

    public void Redraw() {
        
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run() {
                playertablemodel.fireTableDataChanged();
            }
        });
        
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AIColourPanel;
    private javax.swing.JButton AddAIButton;
    private javax.swing.JList AddAIFactionList;
    private javax.swing.JList AddAIList;
    private javax.swing.JButton ChMapButton;
    private org.darkstars.battlehub.gui.CChatPanel Chat;
    private javax.swing.JButton ColourButton;
    private javax.swing.JPanel ColourPanel;
    private javax.swing.JList EditAIFactionList;
    private javax.swing.JCheckBox GameSpeedLockCheck;
    private javax.swing.JToggleButton JAutoScrollToggle;
    private javax.swing.JList MapList;
    private javax.swing.JLabel MapPickerLabel;
    private javax.swing.JComboBox MyAllyTeamCombo;
    private javax.swing.JComboBox MyTeamCombo;
    private javax.swing.JTextField NewAIName;
    private javax.swing.JPanel OptionsPanel;
    private javax.swing.JComboBox RaceCombo;
    private javax.swing.JButton RemoveAIButton;
    private javax.swing.JButton RestrictUnitButton;
    private javax.swing.JTextArea ScriptPreview;
    private javax.swing.JButton UnRestrictAllUnitsButton;
    private javax.swing.JButton UnRestrictUnitButton;
    private javax.swing.JButton UpdateAIButton;
    private javax.swing.JTextArea UserMessage;
    private javax.swing.JPanel addAIPanel;
    private javax.swing.JToolBar chatToolBar;
    private javax.swing.JList disabledUnitsList;
    private javax.swing.JList editAIAllyList;
    private javax.swing.JList enabledUnitsList;
    private javax.swing.JPanel gameOptionsPanel;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JPanel mapPanel;
    private javax.swing.JLabel minimapLabel;
    private javax.swing.JPanel modifyAIPanel;
    private javax.swing.JList modifyAIsList;
    private javax.swing.JPanel myTeamPanel;
    private javax.swing.JTable playerBattleTable;
    private javax.swing.JButton reload_maplist;
    private javax.swing.JPanel restrictedUnitsPanel;
    private javax.swing.JPanel ribbonPanel;
    private javax.swing.JButton sayButton;
    private javax.swing.JPanel scriptPanel;
    // End of variables declaration//GEN-END:variables

    /**
     * 
     * @param L
     */
    @Override
    public void Init(final ICentralClass LM){
        //
        this.LM = (LMain) LM;
    }

    @Override
    public void OnRemove() {
        //
        this.battlemodel = null;
        LM = null;

        SwingUtilities.invokeLater(new  

              Runnable() {
                @Override
            public void run() {
                setVisible(false);
            }
        });
        SwingUtilities.invokeLater(new  

              Runnable() {





                
            
        
    

@Override
            public void run() {
                dispose();
            }
        });
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
