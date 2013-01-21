/*
 * JChannel.java
 * 98612769816789122
 * Created on 28 May 2006, 13:22
 */
package org.darkstars.battlehub;

import org.darkstars.battlehub.framework.IModule;
import org.darkstars.battlehub.gui.CChannel;
import org.darkstars.battlehub.framework.CPlayer;
import org.darkstars.battlehub.gui.CHostWizard;
import org.darkstars.battlehub.framework.CEvent;
import org.darkstars.battlehub.gui.CUISettings;
import org.darkstars.battlehub.CUserSettings;
import org.darkstars.battlehub.gui.CView;
import org.darkstars.battlehub.framework.BrowserLauncher;
import org.darkstars.battlehub.protocol.tasserver.CTASChannelModel;
import org.darkstars.battlehub.protocol.tasserver.ui.CChannelList;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import org.darkstars.battlehub.framework.CEventTypes;
import org.darkstars.battlehub.framework.Misc;
import org.darkstars.battlehub.framework.IChannelModel;
import org.darkstars.battlehub.framework.spring.CJNATest;
import org.darkstars.battlehub.gui.CChannelListingController;
import org.darkstars.battlehub.gui.CPlayerTablePanel;
import org.darkstars.battlehub.scripting.CScriptManager;
import org.jvnet.flamingo.common.ElementState;
import org.jvnet.flamingo.common.JCommandButton;
import org.jvnet.flamingo.common.JCommandButton.CommandButtonKind;
import org.jvnet.flamingo.common.JCommandButtonPanel;
import org.jvnet.flamingo.common.JIconPopupPanel;
import org.jvnet.flamingo.common.icon.ImageWrapperResizableIcon;
import org.jvnet.flamingo.common.icon.ResizableIcon;
import org.jvnet.lafwidget.LafWidget;
import org.jvnet.lafwidget.tabbed.DefaultTabPreviewPainter;

/**
 *
 * @author  Shade
 */
public class CChannelView extends CView {

    public boolean htmlaccess = false;
    CChannelList cChannelList = null;
    CChannelListingController clc = null;
    CSettings cSettings = null;
    CBattleList cBattleList = null;
    CHostWizard cHostWizard = null;
    JCommandButtonPanel mainPanel;
    JIconPopupPanel iconMainPanel;

    public CChannelView(LMain L) {

        setTitle("jChannelView");
        LM = L;

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                initComponents();
                Initialize();
            }
        });

        if (!Main.chat_only_mode) {

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {

                    cHostWizard = new CHostWizard(LM);
                    LMain.core.AddModule(cHostWizard);
                }
            });
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {

                    final boolean b = CUserSettings.GetValue("ui.channelview.battlelisttopposition", "true").equals("true");
                    cBattleList = new CBattleList(LM);
                    LMain.core.AddModule(cBattleList);


                    if (cHostWizard != null) {
                        tabpane.add("Host Game", cHostWizard);
                    }

                    if (b) {
                        tabpane.add("Battle List", cBattleList);
                        SplitPane.setDividerSize(0);
                    } else {

                        SplitPane.setResizeWeight(0.5);
                        SplitPane.setDividerSize(8);
                        SplitPane.setDividerLocation(0.7);
                        SplitPane.setBottomComponent(cBattleList);
                    }
                }
            });

        }

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                clc = new CChannelListingController();
                clc.Init(LM);
                tabpane.add("Channels", clc.getList());
                LMain.core.AddModule(clc);
            }
        });

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                cSettings = new CSettings(LM, false);
                tabpane.add("Settings", cSettings);
                LMain.core.AddModule(cSettings);
            }
        });


        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                //
                mainPanel = new JCommandButtonPanel(ElementState.TILE);
                mainPanel.addButtonGroup("General", 0);
                mainPanel.addButtonGroup("Content Download Sites", 1);
                mainPanel.addButtonGroup("Game Websites", 2);
                mainPanel.addButtonGroup("Other", 3);
            }
        });
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                iconMainPanel = new JIconPopupPanel(mainPanel, 1, 0);
                mainPanelContainer.add(iconMainPanel);
            }
        });
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                    URL url = this.getClass().getResource("/images/3rdpartyicons/darkstars.png");
                    ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));

                    JCommandButton b = new JCommandButton("darkstars.co.uk", ri);


                    b.setCommandButtonKind(CommandButtonKind.ACTION_ONLY);
                    b.setToolTipText("click");

                    b.addActionListener( new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            BrowserLauncher.openURL("http://www.darkstars.co.uk");
                        }
                    });

                    mainPanel.addButtonToGroup("General", b);
            }
        });

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                URL url = this.getClass().getResource("/images/tango/32x32/apps/help-browser.png");
                ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));

                JCommandButton b = new JCommandButton("help", ri);


                b.setCommandButtonKind(CommandButtonKind.ACTION_ONLY);
                b.setToolTipText("click");

                b.addActionListener( new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        LM.GetProtocol().JoinChannel("newbies", "");
                    }
                });

                mainPanel.addButtonToGroup("General", b);

            }
        });
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {


                URL url = this.getClass().getResource("/images/tango/32x32/status/dialog-error.png");
                ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));

                JCommandButton b = new JCommandButton("Report a bug", ri);


                b.setCommandButtonKind(CommandButtonKind.ACTION_ONLY);
                b.setToolTipText("click");
                //b.setEnabled(false);

                b.addActionListener(
                        new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                BrowserLauncher.openURL("http://battlehub.darkstars.co.uk/trac/newticket");

                            }
                        });

                mainPanel.addButtonToGroup("General", b);

            }
        });
        if (CUpdateHandler.uptodate == CUpdateHandler.versionStatus.outofdate) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {

                    URL url = this.getClass().getResource("/images/tango/32x32/status/software-update-available.png");
                    ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));

                    JCommandButton b = new JCommandButton("Updates are available", ri);


                    b.setCommandButtonKind(CommandButtonKind.ACTION_ONLY);
                    b.setToolTipText("click");
                    b.setEnabled(false);

//                    b.addActionListener(new ActionListener() {
//
//                        @Override
//                        public void actionPerformed(ActionEvent e) {
//                            CUpdateHandler c = new CUpdateHandler();
//                        }
//                    });




                    mainPanel.addButtonToGroup("General", b);
                }
            });
        }


        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                URL url = this.getClass().getResource("/images/tango/32x32/actions/system-log-out.png");
                ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));

                JCommandButton b = new JCommandButton("Logout", ri);


                b.setCommandButtonKind(CommandButtonKind.ACTION_ONLY);
                b.setToolTipText("click");

                b.addActionListener(
                        new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                //
                                CEvent event = new CEvent(CEventTypes.LOGOUT);
                                LM.GetCore().NewGUIEvent(event);
                            }
                        });

                mainPanel.addButtonToGroup("General", b);

            }
        });
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                // file sites


                URL url = this.getClass().getResource("/images/3rdpartyicons/springfiles.png");
                ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));

                JCommandButton b = new JCommandButton("JJ Spring Files 2", ri);


                b.setCommandButtonKind(CommandButtonKind.ACTION_ONLY);
                b.setToolTipText("click");

                b.addActionListener(
                        new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                //
                                BrowserLauncher.openURL("http://spring.jobjol.nl");

                            }
                        });

                mainPanel.addButtonToGroup("Content Download Sites", b);

            }
        });
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                URL url = this.getClass().getResource("/images/3rdpartyicons/darkstars.png");
                ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));

                JCommandButton b = new JCommandButton("Darkstars Files", ri);


                b.setCommandButtonKind(CommandButtonKind.ACTION_ONLY);
                b.setToolTipText("click");

                b.addActionListener(
                        new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                //
                                BrowserLauncher.openURL("http://www.darkstars.co.uk/downloads/");

                            }
                        });

                mainPanel.addButtonToGroup("Content Download Sites", b);

            }
        });
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                URL url = this.getClass().getResource("/images/tango/32x32/actions/system-log-out.png");
                ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));

                JCommandButton b = new JCommandButton("Foreboding Angel", ri);


                b.setCommandButtonKind(CommandButtonKind.ACTION_ONLY);
                b.setToolTipText("click");

                b.addActionListener(
                        new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                //
                                BrowserLauncher.openURL("http://evolutionrts.info/maps/");

                            }
                        });

                mainPanel.addButtonToGroup("Content Download Sites", b);

            }
        });
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                URL url = this.getClass().getResource("/images/tango/32x32/actions/system-log-out.png");
                ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));

                JCommandButton b = new JCommandButton("Spring Portal", ri);


                b.setCommandButtonKind(CommandButtonKind.ACTION_ONLY);
                b.setToolTipText("click");

                b.addActionListener(
                        new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                //
                                BrowserLauncher.openURL("http://spring-portal.com");

                            }
                        });

                mainPanel.addButtonToGroup("Content Download Sites", b);

            }
        });
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                URL url = this.getClass().getResource("/images/tango/32x32/actions/system-log-out.png");
                ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));

                JCommandButton b = new JCommandButton("TAS Downloads", ri);


                b.setCommandButtonKind(CommandButtonKind.ACTION_ONLY);
                b.setToolTipText("click");

                b.addActionListener(
                        new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                //
                                BrowserLauncher.openURL("http://www.tasdownloads.com/");

                            }
                        });

                mainPanel.addButtonToGroup("Content Download Sites", b);


            }
        });

        // games sites
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {



                URL url = this.getClass().getResource("/images/3rdpartyicons/spring.png");
                ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));

                JCommandButton b = new JCommandButton("Spring Engine", ri);


                b.setCommandButtonKind(CommandButtonKind.ACTION_ONLY);
                b.setToolTipText("click");

                b.addActionListener(
                        new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                //
                                BrowserLauncher.openURL("http://spring.clan-sy.com");

                            }
                        });

                mainPanel.addButtonToGroup("Game Websites", b);

            }
        });
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                URL url =
                        this.getClass().getResource("/images/3rdpartyicons/s44.png");
                ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));

                JCommandButton b = new JCommandButton("Spring 1944", ri);


                b.setCommandButtonKind(CommandButtonKind.ACTION_ONLY);
                b.setToolTipText("click");

                b.addActionListener(
                        new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                BrowserLauncher.openURL("http://spring1944.com/s44/");
                            }
                        });

                mainPanel.addButtonToGroup("Game Websites", b);

            }
        });
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                URL url =
                        this.getClass().getResource("/images/3rdpartyicons/ca.png");
                ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));

                JCommandButton b = new JCommandButton("Complete Annihilation", ri);

                b.setCommandButtonKind(CommandButtonKind.ACTION_ONLY);
                b.setToolTipText("click");

                b.addActionListener(
                        new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                BrowserLauncher.openURL("http://www.caspring.org");

                            }
                        });

                mainPanel.addButtonToGroup("Game Websites", b);

            }
        });
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                URL url = this.getClass().getResource("/images/3rdpartyicons/evolution.png");
                ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));

                JCommandButton b = new JCommandButton("Evolution RTS", ri);


                b.setCommandButtonKind(CommandButtonKind.ACTION_ONLY);
                b.setToolTipText("click");

                b.addActionListener(
                        new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                BrowserLauncher.openURL("http://www.evolutionrts.info/");
                            }
                        });

                mainPanel.addButtonToGroup("Game Websites", b);

            }
        });
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                URL url = this.getClass().getResource("/images/3rdpartyicons/swiw.png");
                ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));

                JCommandButton b = new JCommandButton("StarWars Imperial Winter", ri);

                b.setCommandButtonKind(CommandButtonKind.ACTION_ONLY);
                b.setToolTipText("click");

                b.addActionListener(
                        new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                BrowserLauncher.openURL("http://www.imperialwinter.com");
                            }
                        });

                mainPanel.addButtonToGroup("Game Websites", b);

            }
        });
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                URL url = this.getClass().getResource(
                        "/images/tango/32x32/actions/view-fullscreen.png");
                ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));

                JCommandButton b = new JCommandButton("Toggle Fullscreen", ri);


                b.setCommandButtonKind(CommandButtonKind.ACTION_ONLY);
                b.setToolTipText("click");

                b.addActionListener(
                        new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                CEvent event = new CEvent("UI_TOGGLE_FULLSCREEN");
                                LM.GetCore().NewGUIEvent(event);
                            }
                        });

                mainPanel.addButtonToGroup("Other", b);

            }
        });
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                URL url = this.getClass().getResource(
                        "/images/tango/32x32/actions/view-fullscreen.png");
                ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));

                JCommandButton b = new JCommandButton("Aegis API test Load", ri);


                b.setCommandButtonKind(CommandButtonKind.ACTION_ONLY);
                b.setToolTipText("click");

                b.addActionListener(
                        new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {

                                CScriptManager.GetCallbacks(LM);
                            }
                        });

                mainPanel.addButtonToGroup("Other", b);


            //
            }
        });
        
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                URL url = this.getClass().getResource(
                        "/images/tango/32x32/actions/view-fullscreen.png");
                ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));

                JCommandButton b = new JCommandButton("JNA test Load", ri);


                b.setCommandButtonKind(CommandButtonKind.ACTION_ONLY);
                b.setToolTipText("click");

                b.addActionListener(
                        new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {

                                //
                                String usyncpath = CUserSettings.GetValue("unitsyncpath");
                                CJNATest jnat = new CJNATest(usyncpath);
                            }
                        });

                mainPanel.addButtonToGroup("Other", b);


            //
            }
        });
    }

    @Override
    public void OnRemove() {

        if (cSettings != null) {
            LMain.core.RemoveModule(cSettings);
            cSettings =
                    null;
        }

        if (cBattleList != null) {
            LMain.core.RemoveModule(cBattleList);
            cBattleList = null;
        }

        for (CChannel c : this.channels.values()) {
            LMain.core.RemoveModule(c);
        }

        channels.clear();

    }

    @Override
    public void Initialize() {
        UserTitle = "Main lobby page";
    }

    private void AddPlayer(CPlayer cPlayer) {
        playerTablePanel.AddPlayer(cPlayer);
    }

    public void RemoveChannel(String channelname, final CChannel ui) {

        this.channelsModels.remove(channelname);

        this.channels.remove(channelname);

        if (ui != null) {
            SwingUtilities.invokeLater( new Runnable() {

                @Override
                public void run() {
                    tabpane.remove(ui);
                }
            });
        }

    }
    //public javax.swing.JScrollPane jScrollPane1aaaa;
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabplacementGroup = new javax.swing.ButtonGroup();
        SplitPane = new javax.swing.JSplitPane();
        tabpane = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        StatusCombo = new javax.swing.JComboBox();
        ingame_label = new javax.swing.JLabel();
        ladderLabel = new javax.swing.JLabel();
        WelcomeLabel = new javax.swing.JLabel();
        SearchBox = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        next_rank_ProgressBar = new javax.swing.JProgressBar();
        next_rank_label = new javax.swing.JLabel();
        mainPanelContainer = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        FServerMessages = new javax.swing.JEditorPane();
        jButton5 = new javax.swing.JButton();
        playerTablePanel =  new CPlayerTablePanel(LM);
        BottomPanel = new javax.swing.JPanel();

        setFont(CUISettings.GetFont(12,false));
        setMaximumSize(new java.awt.Dimension(2840, 2560));
        setMinimumSize(new java.awt.Dimension(840, 570));
        setPreferredSize(new java.awt.Dimension(840, 570));

        SplitPane.setDividerSize(8);
        SplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        SplitPane.setResizeWeight(1.0);
        SplitPane.setOneTouchExpandable(true);

        tabpane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        tabpane.setTabPlacement(Integer.parseInt(CUserSettings.GetValue("UI.toptabpane.placement", ""+javax.swing.JTabbedPane.TOP)));
        tabpane.setMaximumSize(new java.awt.Dimension(83000, 530000));
        tabpane.setMinimumSize(new java.awt.Dimension(400, 300));
        tabpane.setPreferredSize(new java.awt.Dimension(840, 570));
        tabpane.putClientProperty(
            LafWidget.TABBED_PANE_PREVIEW_PAINTER,
            new DefaultTabPreviewPainter());

        jPanel7.setFont(CUISettings.GetFont(12,false));

        StatusCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Online", "Away" }));
        StatusCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                StatusComboItemStateChanged(evt);
            }
        });
        StatusCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StatusComboActionPerformed(evt);
            }
        });

        ingame_label.setFont(ingame_label.getFont().deriveFont(ingame_label.getFont().getSize()+1f));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/darkstars/battlehub/languages"); // NOI18N
        ingame_label.setText(bundle.getString("CChannelView.ingame_label.text")); // NOI18N

        ladderLabel.setFont(ladderLabel.getFont().deriveFont(ladderLabel.getFont().getSize()+1f));
        ladderLabel.setText("Not registered to the ladder"); // NOI18N

        WelcomeLabel.setFont(CUISettings.GetFont(24,true));
        WelcomeLabel.setText("Welcome "+LM.protocol.GetUsername() + "!");

        SearchBox.setText(bundle.getString("CChannelView.SearchBox.text")); // NOI18N
        SearchBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                SearchBoxFocusGained(evt);
            }
        });
        SearchBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                SearchBoxKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                SearchBoxKeyTyped(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/system-search.png"))); // NOI18N
        jButton3.setText(bundle.getString("CChannelView.jButton3.text_1")); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        next_rank_label.setFont(next_rank_label.getFont().deriveFont(next_rank_label.getFont().getSize()+1f));
        next_rank_label.setText(bundle.getString("CChannelView.next_rank_label.text")); // NOI18N

        mainPanelContainer.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        mainPanelContainer.setLayout(new javax.swing.BoxLayout(mainPanelContainer, javax.swing.BoxLayout.LINE_AXIS));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(mainPanelContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 813, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(next_rank_ProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 813, Short.MAX_VALUE)
                            .addComponent(next_rank_label, javax.swing.GroupLayout.DEFAULT_SIZE, 813, Short.MAX_VALUE)
                            .addComponent(ingame_label, javax.swing.GroupLayout.DEFAULT_SIZE, 813, Short.MAX_VALUE)
                            .addComponent(WelcomeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 813, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(ladderLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
                        .addGap(349, 349, 349))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(SearchBox, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(StatusCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(WelcomeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ingame_label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(next_rank_label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(next_rank_ProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ladderLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(SearchBox, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                    .addComponent(StatusCombo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainPanelContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel7Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {SearchBox, jButton3});

        tabpane.addTab(bundle.getString("CChannelView.jPanel7.TabConstraints.tabTitle_1"), new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/places/start-here.png")), jPanel7); // NOI18N

        FServerMessages.setEditable(false);
        jScrollPane4.setViewportView(FServerMessages);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/status/network-idle.png"))); // NOI18N
        jButton5.setText(bundle.getString("CChannelView.jButton5.text")); // NOI18N
        jButton5.setMargin(new java.awt.Insets(2, 6, 2, 6));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout playerTablePanelLayout = new javax.swing.GroupLayout(playerTablePanel);
        playerTablePanel.setLayout(playerTablePanelLayout);
        playerTablePanelLayout.setHorizontalGroup(
            playerTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 370, Short.MAX_VALUE)
        );
        playerTablePanelLayout.setVerticalGroup(
            playerTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 497, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(playerTablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(playerTablePanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE)))
                .addContainerGap())
        );

        tabpane.addTab(bundle.getString("CChannelView.jPanel2.TabConstraints.tabTitle"), new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/places/network-server.png")), jPanel2); // NOI18N

        SplitPane.setLeftComponent(tabpane);

        javax.swing.GroupLayout BottomPanelLayout = new javax.swing.GroupLayout(BottomPanel);
        BottomPanel.setLayout(BottomPanelLayout);
        BottomPanelLayout.setHorizontalGroup(
            BottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 838, Short.MAX_VALUE)
        );
        BottomPanelLayout.setVerticalGroup(
            BottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        SplitPane.setRightComponent(BottomPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(SplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 840, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(SplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void StatusComboItemStateChanged (java.awt.event.ItemEvent evt) {//GEN-FIRST:event_StatusComboItemStateChanged
        if (LMain.protocol == null) {
            return;
        }

        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            if (StatusCombo.getSelectedIndex() == 0) {
                // ONLINE
                LMain.protocol.SetAway(false);
                LMain.protocol.SetIngame(false);
            } else if (StatusCombo.getSelectedIndex() == 1) {
                // AWAY
                LMain.protocol.SetAway(true);
                LMain.protocol.SetIngame(false);
            }

        }
    }//GEN-LAST:event_StatusComboItemStateChanged

    private void StatusComboActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StatusComboActionPerformed
        /*if(LM.connection == null){
        return;
        }
        if(StatusCombo.getSelectedIndex ()==0){// ONLINE
        status = Misc.setAwayBitToStatus (status,0);
        status = Misc.setInGameToStatus (status,0);
        LM.connection.SendLine ("MYSTATUS " + status);
        }else if(StatusCombo.getSelectedIndex ()==1){// AWAY
        status = Misc.setAwayBitToStatus (status,1);
        status = Misc.setInGameToStatus (status,0);
        LM.connection.SendLine ("MYSTATUS " + status);
        }*/
    }//GEN-LAST:event_StatusComboActionPerformed
    public boolean search_changed = false;

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed


        String s = SearchBox.getText();
        String url = "http://www.darkstars.co.uk/downloads/search.php?search=" +
                Misc.toHTML(s) + "/";
        BrowserLauncher.openURL(url);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void SearchBoxFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_SearchBoxFocusGained
        SearchBox.setSelectionStart(0);
        SearchBox.setSelectionEnd(SearchBox.getText().length());
    }//GEN-LAST:event_SearchBoxFocusGained

    private void SearchBoxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SearchBoxKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_SearchBoxKeyTyped

    private void SearchBoxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SearchBoxKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String s = SearchBox.getText();
            String url = "http://www.unknown-files.net/spring/search/" + Misc.toHTML(s) + "/";
            BrowserLauncher.openURL(url);
        }
    }//GEN-LAST:event_SearchBoxKeyReleased

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        CEvent e = new CEvent(CEventTypes.TOGGLERAWTRAFFIC);
        LMain.core.NewGUIEvent(e);
    }//GEN-LAST:event_jButton5ActionPerformed
    boolean started = false;
    int fredrawcounter = 80;
    ArrayList<String> startupcommands = new ArrayList<String>();

    /**
     * The Update() Routine
     */
    @Override
    public void Update() {
        fredrawcounter--;
        //if ((fredrawcounter == 2) || (fredrawcounter == 4)) {
        if (startupcommands.isEmpty() == false) {

            String f = startupcommands.get(0);
            //System.out.println(f);
            startupcommands.remove(0);
            LMain.command_handler.ExecuteCommand(f);
        }

        if ((newchannels) && (channels != null)) {
            //
            if (channels.isEmpty() == false) {
                Runnable doWorkRunnable = new Runnable() {

                    @Override
                    public void run() {
                        synchronized (channels) {
                            Iterator<CChannel> i = channels.values().iterator();
                            while (i.hasNext()) {
                                CChannel c = i.next();
                                if (tabpane.indexOfComponent(c) == -1) {
                                    tabpane.add("#" + c.Channel, c);
                                }

                            }
                        }



                    }
                };
                SwingUtilities.invokeLater(doWorkRunnable);
            }
//tabpane.add ("#"+e.data[1],c1);
        }
    //}

    //if(fredrawcounter == 0){
    //    fredrawcounter = 4;
    //}
        /*for (CChannel cc : channels.values()) {
    cc.Update();
    }*/


    }

    @Override
    public void ValidatePanel() {
        Runnable doWorkRunnable = new Runnable() {

            @Override
            public void run() {
                validate();
            }
        };
        SwingUtilities.invokeLater(doWorkRunnable);
    }
    boolean Event_avail = true;
    public TreeMap<String, String> channeltemp = new TreeMap<String, String>();
    boolean newchannels = false;
    public TreeMap<String, CChannel> channels = new TreeMap<String, CChannel>();
    TreeMap<String, IChannelModel> channelsModels = new TreeMap<String, IChannelModel>();
    boolean refreshing_channels = false;

    /**
     * Class notification routine
     * @param e
     */
    @Override
    public void NewGUIEvent(final CEvent e) {
        //
        if (e.IsEvent(CEvent.LOGOUT) || e.IsEvent(CEvent.LOGGEDOUT) || e.IsEvent(CEvent.DISCONNECT) || e.IsEvent(CEvent.DISCONNECTED)) {
            Runnable doWorkRunnable = new Runnable() {

                @Override
                public void run() {
                    ClearPlayers();
                    tabpane.removeAll();
                }
            };
            SwingUtilities.invokeLater(doWorkRunnable);
            LM.RemoveView(this);
            if (e.IsEvent(CEventTypes.DISCONNECTED)) {
                LMain.Toasts.AddMessage("You've been disconnected");
            }



        } else if (e.IsEvent(CEventTypes.CHANNELUNREAD)) {
            final String s = "#" + e.data[1];
            Runnable doWorkRunnable = new Runnable() {

                @Override
                public void run() {
                    for (int i = 0; i <
                            tabpane.getTabCount(); i++) {
                        String q = tabpane.getTitleAt(i);
                        if (q.equalsIgnoreCase(s)) {
                            tabpane.setIconAt(i, new javax.swing.ImageIcon(getClass().getResource(
                                    "/images/UI/comment_new.gif")));
                            break;

                        }




                    }


                }
            };
            SwingUtilities.invokeLater(doWorkRunnable);

        } else if (e.IsEvent(CEventTypes.CHANNELREAD)) {
            final String s = "#" + e.data[1];
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    for (int i = 0; i <
                            tabpane.getTabCount(); i++) {
                        String q = tabpane.getTitleAt(i);
                        if (q.equalsIgnoreCase(s)) {
                            //




                            tabpane.setIconAt(i, null);
                            break;

                        }


                    }
                }
            });

        } else if (e.IsEvent(CEventTypes.CHANNELCLOSED)) {
            final String t1 = e.data[1];
            Runnable doWorkRunnable = new Runnable() {

                @Override
                public void run() {
                    CChannel c = channels.get(t1);
                    if (c != null) {
                        synchronized (channels) {
                            channels.remove(t1);
                        }





                        LMain.core.RemoveModule(c);
                        tabpane.remove(c);
                    }

                }
            };
            SwingUtilities.invokeLater(doWorkRunnable);
        }

    }

    @Override
    public void NewEvent(final CEvent e) {
        if (e.IsEvent("JOIN")) {

            CTASChannelModel cm = new CTASChannelModel();

            cm.SetName(e.data[1]);
            LMain.core.AddModule((IModule) cm);

            cm.Init(LM);



            cm.DoChannelViewStuff(this);

            synchronized (channelsModels) {
                channelsModels.put(e.data[1], (IChannelModel) cm);
            }

            /**/
            newchannels = true;

        } else if (e.IsEvent(CEventTypes.NEWUSRADDED)) {
            AddPlayer((CPlayer) e.object);
        } else if (e.IsEvent("ACCEPTED")) {

            LMain.protocol.SendTraffic("GETINGAMETIME");
//            SwingWorker worker =
//                    new SwingWorker<Boolean, Void>() {
//
//                        @Override
//                        public Boolean doInBackground() {
//                            boolean b = CMeltraxLadder.AccountExists(LMain.protocol.GetUsername());
//                            return b;
//                        }
//
//                        @Override
//                        public void done() {
//                            try {
//                                if (get()) {
//                                    ladderLabel.setText("Account is registered on the ladder");
//                                }
//
//                            } catch (ExecutionException ex) {
//                                ex.printStackTrace();
//                            } catch (InterruptedException ex) {
//                                ex.printStackTrace();
//                            }
//
//                            WelcomeLabel.setText("Welcome " + LMain.protocol.GetUsername());
//                        }
//                    };
//            worker.execute();
        } else if (e.IsEvent("LOGININFOEND")) {
            LM.SetFocus(this);

            String s = org.darkstars.battlehub.CUserSettings.GetStartScript();
            if (s.equals("") == false) {
                String[] lines = s.split("\n");
                for (int h = 0; h <
                        lines.length; ++h) {
                    //System.out.println(lines[h]);


                    startupcommands.add(lines[h]);
                }

            }


            Runnable doWorkRunnable = new Runnable() {

                @Override
                public void run() {
                    playerTablePanel.ClearPlayers();

                }
            };
            SwingUtilities.invokeLater(doWorkRunnable);

            Runnable doWorkRunnable2 = new Runnable() {

                @Override
                public void run() {
                    ArrayList<String> list = new ArrayList<String>();
                    synchronized (LMain.playermanager.activeplayers) {
                        list.addAll(LMain.playermanager.activeplayers);
                    }

                    for (String o : list) {
                        CPlayer p = LMain.playermanager.GetPlayer(o);
                        playerTablePanel.AddPlayer(p);
                    }

                }
            };
            SwingUtilities.invokeLater(doWorkRunnable2);

        } else if (e.IsEvent("REMOVEUSER")) {
            RemovePlayer(e.data[1]);
        } else if (e.IsEvent("CLIENTSTATUS")) {
            if (e.data[1].equalsIgnoreCase(LMain.protocol.GetUsername())) {
                final int status = Integer.parseInt(e.data[2]);
                Runnable doWorkRunnable = new Runnable() {

                    @Override
                    public void run() {
                        if (Misc.getAwayBitFromStatus(status) == 1) {
                            // AWAY


                            StatusCombo.setSelectedIndex(1);
                        } else {
                            // NORMAL ONLINE


                            StatusCombo.setSelectedIndex(0);
                        }





                    }
                };
                SwingUtilities.invokeLater(doWorkRunnable);
            } //Iterator<CChannel> i = channels.values ().iterator ();
//if(e.data[0].equalsIgnoreCase (""))

        } else if (e.IsEvent("DENIED")) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    Collection co = channels.values();
                    @SuppressWarnings(value = "unchecked")
                    Iterator<org.darkstars.battlehub.gui.CChannel> i = co.iterator();
                    while (i.hasNext()) {
                        CChannel cn = i.next();
                        if (cn == null) {
                            continue;

                        }

                        tabpane.remove(cn);
                    }



                    channels.clear();
                }
            });
            LM.RemoveView(getTitle());
        } else if (e.IsEvent("MOTD")) {
            AddMOTD(Misc.makeSentence(e.data, 1));
        } else if (e.IsEvent("SERVERMSG") || e.IsEvent("SERVERMSGBOX")) {
            AddMOTD(Misc.makeSentence(e.data, 1));
            if (Misc.makeSentence(e.data, 0).startsWith("SERVERMSG Your in-game time is")) {


                Runnable doWorkRunnable = new Runnable() {

                    @Override
                    public void run() {
                        int minutes = Integer.parseInt(e.data[5]);

                        ingame_label.setText("Your ingame time is " + MinutesToString(minutes));

                        float percentage = 0;
                        int minutes_left = 0;
                        if (minutes < 5 * 60) {
                            // newbie rank
                            minutes_left = (5 * 60) - minutes;
                            percentage =
                                    minutes_left / (5.0f * 60.0f);
                        } else if (minutes < 15 * 60) {
                            //
                            minutes_left = (15 * 60) - minutes;
                            percentage =
                                    minutes_left / (15.0f * 60.0f);
                        } else if (minutes < 30 * 60) {
                            //
                            minutes_left = (30 * 60) - minutes;
                            percentage =
                                    minutes_left / (30.0f * 60.0f);
                        } else if (minutes < 100 * 60) {
                            //
                            minutes_left = (100 * 60) - minutes;
                            percentage =
                                    minutes_left / (100.0f * 60.0f);
                        } else if (minutes < 300 * 60) {
                            //
                            minutes_left = (300 * 60) - minutes;
                            percentage =
                                    minutes_left / (300.0f * 60.0f);
                        } else if (minutes < 1000 * 60) {
                            //
                            minutes_left = (1000 * 60) - minutes;
                            percentage =
                                    minutes_left / (1000.0f * 60.0f);
                        }// else {
//                            //
//                            minutes_left = -1;
//                            percentage = -1;
//                        }

                        next_rank_ProgressBar.setValue(100 - (int) (percentage * 100));
                        {
////////////////////////
                            next_rank_label.setText("You will reach the next rank after playing for " + MinutesToString(minutes_left));//s2);

                        }





                    }
                };
                SwingUtilities.invokeLater(doWorkRunnable);
            }
//FServerMessages.setCaretPosition(FServerMessages.getText().length());
        }

    }

    public String MinutesToString(
            int minutes) {

        //Calendar c2 = Calendar.getInstance();
        //c2.clear();
        //c2.setTimeInMillis(minutes * 1000);



        int minutes1 = minutes % 60;


        int hours1 = (minutes / 60) % 24;
        int days1 =
                ((minutes /
                60) / 24) % 7;
        int weeks1 = ((minutes / 60) / 24) / 7;


        String s2 = "";
        if (weeks1 > 0) {
            s2 += weeks1 + " week";
            if (weeks1 > 1) {
                s2 += "s";
            }

        }

        if (days1 > 0) {
            s2 += ", " + days1 + " day";
            if (days1 > 1) {
                s2 += "s";
            }

        }

        if (hours1 > 0) {
            s2 += " " + hours1 + " hour";
            if (hours1 > 1) {
                s2 += "s";
            }

        }

        if (minutes1 > 0) {
            s2 += " " + minutes1 + " minute";
            if (minutes1 > 1) {
                s2 += "s";
            }

        }
        return s2;
    }
    String MOTD = "";

    void AddMOTD(String s) {
        synchronized (MOTD) {
            MOTD += s + "\n";
        }

        Runnable doWorkRunnable = new Runnable() {

            @Override
            public void run() {
                FServerMessages.setText(MOTD);
            }
        };
        SwingUtilities.invokeLater(doWorkRunnable);
    }

    void AddPlayer(String player) {
        if (player.equals(
                "")) {
            return;
        } else if (player == null) {
            return;
        }

        playerTablePanel.AddPlayer(
                LMain.playermanager.GetPlayer(player));

        search_changed =
                true;
    }

    void RemovePlayer(String player) {
        if (player.equals(
                "")) {
            return;
        } else if (player == null) {
            return;
        }

        playerTablePanel.RemovePlayer(
                LMain.playermanager.GetPlayer(player));
        search_changed =
                true;





    }

    void ClearPlayers() {
        this.playerTablePanel.ClearPlayers();
    }
    String playersearchstring = "";

    /**
     * 
     */
    public class TblRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable jTable, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            super.getTableCellRendererComponent(jTable, value,
                    isSelected, hasFocus, row, col);

            String tvalue = (String) jTable.getValueAt(row, 1);
            if (tvalue != null) {

                if (tvalue.startsWith("Ingame")) {

                    setBackground(new Color(215, 240, 247));
                } else if (tvalue.startsWith("Locked")) {

                    setBackground(new Color(242, 242, 242));

                } else if (tvalue.startsWith("Full")) {

                    setBackground(new Color(221, 221, 221));
                } else if (tvalue.contains("passworded")) {
                    setBackground(new Color(255, 235, 166));
                } else {
                    setBackground(Color.white);
                }
                if (isSelected) {


                    setForeground(Color.red);


                    setFont(CUISettings.GetFont(12, true));
                } else {
                    setForeground(Color.BLACK);
                }
            /*
             *else if(b.locked){
            s += "Locked";
            }else if(b.maxplayers == b.GetPlayerNames ().size ()){
            s+= "Full";
            }else{
            s += "Open";
            }
            if(b.IsPassworded ()){
            s += " & passworded";//<HTML><body bgcolor=\"FFDD00\"> <font color=\"#000000\">"+"&nbsp;";
            }
             */

            /*if( val>valMin )
            setBackground (Color.yellow);
            if( val>valMax)
            setBackground (Color.red);*/
            } else {
                setBackground(Color.white);
            }
            return this;
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BottomPanel;
    private javax.swing.JEditorPane FServerMessages;
    private javax.swing.JTextField SearchBox;
    private javax.swing.JSplitPane SplitPane;
    private javax.swing.JComboBox StatusCombo;
    private javax.swing.JLabel WelcomeLabel;
    private javax.swing.JLabel ingame_label;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel ladderLabel;
    private javax.swing.JPanel mainPanelContainer;
    private javax.swing.JProgressBar next_rank_ProgressBar;
    private javax.swing.JLabel next_rank_label;
    private org.darkstars.battlehub.gui.CPlayerTablePanel playerTablePanel;
    public javax.swing.JTabbedPane tabpane;
    private javax.swing.ButtonGroup tabplacementGroup;
    // End of variables declaration//GEN-END:variables
}