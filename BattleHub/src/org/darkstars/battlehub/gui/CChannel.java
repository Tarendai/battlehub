/*
 * CChannel.java
 *
 * Created on 07 April 2007, 18:22
 */
package org.darkstars.battlehub.gui;

import java.net.MalformedURLException;
import org.darkstars.battlehub.LMain;
import org.darkstars.battlehub.helpers.ColourHelper;
import org.darkstars.battlehub.framework.CPlayer;
import org.darkstars.battlehub.gui.CChatLogFormatter;
import org.darkstars.battlehub.gui.CSmileyManager;
import org.darkstars.battlehub.framework.CEvent;
import org.darkstars.battlehub.framework.CStringHelper;
import org.darkstars.battlehub.gui.CUISettings;
import org.darkstars.battlehub.CUserSettings;
import org.darkstars.battlehub.framework.IModule;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TreeMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import org.darkstars.battlehub.CTextColours;
import org.darkstars.battlehub.framework.CEventTypes;
import org.darkstars.battlehub.framework.ICentralClass;
import org.darkstars.battlehub.framework.IChannelModel;
import org.darkstars.battlehub.framework.Misc;

/**
 *
 * @author  Tom
 */
public class CChannel extends javax.swing.JPanel implements IModule {

    public boolean scroll = false;
    public String lastsent = "";
    public String Channel = "";
    public LMain LM;
    public boolean inchannel = true;
    public static ImageIcon[] smallranks = null;
    public static TreeMap<String, ImageIcon> flags = new TreeMap<String, ImageIcon>();
    FileHandler txtLog = null;
    Logger logger = null;

    public static ImageIcon GetFlagIcon(String flag) {
        if (flag == null) {
            //
            return null;
        }

        if (flags.containsKey(flag)) {

            ImageIcon i = flags.get(flag);
            return i;
        } else {
            String path = LMain.GetAbsoluteLobbyFolderPathStatic() + "images/flags/" + flag + ".png";
            File f = new File(path);
            if (f.canRead()) {
                try {
                    ImageIcon i = new ImageIcon(f.toURI().toURL());
                    flags.put(flag, i);
                    return i;
                } catch (MalformedURLException ex) {
                    Logger.getLogger(CChannel.class.getName()).log(Level.SEVERE, null, ex);
                    return null;
                }
            } else {
                ImageIcon i = GetFlagIcon("unknown_flag");
                flags.put(flag, i);
                return i;
            }
        }
    }
    private IChannelModel cm;

    /**
     * Creates new form CChannel
     * @param cm
     */
    public CChannel(final IChannelModel cm) {
        this.cm = cm;
        chatpanel = new org.darkstars.battlehub.gui.CChatPanel();

        if (smallranks == null) {

            // load the smaller rank images
            smallranks = new ImageIcon[7];
            for (int i = 0; i < 7; i++) {
                String path = LMain.GetAbsoluteLobbyFolderPathStatic() +
                        "images/ranks_small/" + i + ".png";
                smallranks[i] = new ImageIcon(path); //createImageIcon(path);//contents[i].toURI ().toString ());
            }

        }
        SwingUtilities.invokeLater( new Runnable() {

            @Override
            public void run() {
                initComponents();
            }
        });
    }

    public void Init(LMain L, final String channel) {

        Init(L);

        Channel = channel;
        inchannel = true;

        SwingUtilities.invokeLater( new Runnable() {

            @Override
            public void run() {
                if (Channel.equalsIgnoreCase("main") ||
                        Channel.equalsIgnoreCase("news") ||
                        Channel.equalsIgnoreCase("sy") ||
                        Channel.equalsIgnoreCase("newbies")) {
                    channelPrivateLabel.setText("public channel");
                }
            }
        });

        try {

            File log = new File(LMain.GetAbsoluteLobbyFolderPathStatic() + "logs/");
            if (!log.canRead()) {
                log.mkdir();
            }

            txtLog = new FileHandler(LMain.GetAbsoluteLobbyFolderPathStatic() +
                    "logs/" + channel + ".txt", true);
            txtLog.setFormatter(new CChatLogFormatter());
            logger = Logger.getLogger("channel." + channel);
            logger.setLevel(Level.INFO);
            logger.addHandler(txtLog);

            Date today;
            String dateOut;
            DateFormat formatter;

            formatter = DateFormat.getDateTimeInstance(DateFormat.FULL,
                    DateFormat.LONG);
            today = new Date();
            dateOut = formatter.format(today);

            logger.info("logging started at " + dateOut + " " + Locale.getDefault());

        } catch (IOException ex) {
            Logger.getLogger(CChannel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(CChannel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public static ImageIcon botimg = null;

    class PlayerListRenderer extends JLabel implements ListCellRenderer {

        public PlayerListRenderer() {
            setOpaque(true);
            setHorizontalAlignment(LEFT);
            setVerticalAlignment(TOP);



            if (CChannel.botimg == null) {
                URL h = this.getClass().getResource("/images/bot/32.png");
                CChannel.botimg = new ImageIcon(
                        new ImageIcon(h).getImage().getScaledInstance(20, 20, 0));
            }
        }

        /*
         * This method finds the image and text corresponding
         * to the selected value and returns the label, set up
         * to display the text and image.
         */
        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {

            //Get the selected index. (The index param isn't
            //always valid, so just use the value.)
            String selectedValue = (String) value;

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());

                if (LMain.playermanager.GetPlayerInGame(selectedValue) == false) {
                    setForeground(list.getForeground());
                } else {
                    setForeground(new Color(181, 203, 255));
                }
            }

            CPlayer p = LMain.playermanager.GetPlayer(selectedValue);

            if (p != null) {
                ImageIcon icon = null;

                if (Misc.getAccessFromStatus(p.getStatus()) > 0) {
                    icon = CSmileyManager.adminimg;
                    icon.setImageObserver(list);

                } else if (Misc.getBotModeFromStatus(p.getStatus())) {
                    icon = CChannel.botimg;
                    icon.setImageObserver(list);

                } else {
                    //Set the icon and text.  If icon was null, say so.
                    icon = CSmileyManager.rank_images[p.rank];
                    icon.setImageObserver(list);
                }

                setIcon(icon);

            }

            String t = selectedValue;
            if (LMain.playermanager.GetPlayerInGame(selectedValue)) {
                t += " (ingame)";
            }
            setText(t);
            setFont(list.getFont());

            return this;
        }
    }

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     *  
     * @param path 
     * @return 
     */
    protected ImageIcon createImageIcon(String path) {
        if (path == null) {
            return null;
        }
        return new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(
                18, 18, Image.SCALE_REPLICATE));

    }

    class getinputtext implements Runnable {

        public String text = "";

        @Override
        public void run() {
            text = messageinput.getText();
        }
    }

    private void SendMessage() {

        String[] lines;
        if (EventQueue.isDispatchThread()) {
            lines = messageinput.getText().split("\n");
        } else {
            getinputtext getTextFieldText = new getinputtext();
            try {
                SwingUtilities.invokeAndWait(getTextFieldText);
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
                return;
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                return;
            }
            lines = getTextFieldText.text.split("\n");
        }

        for (int n = 0; n < lines.length; n++) {
            String k = lines[n];
            if (k.trim().equalsIgnoreCase("")) {
                continue;
            }
            if (LMain.command_handler.ExecuteCommand(lines[n])) {
                continue;
            }
            String[] command = k.split(" ");
            if (k.length() > 1020) {
                LMain.Toasts.AddMessage("error: a message cannot contain in" +
                        " excess of 1,024 characters, the message was blocked" +
                        " to prevent a server ban");
            }
            if (command[0].equalsIgnoreCase("/me")) {
                cm.SayAction(Misc.makeSentence(command, 1));
            } else {
                cm.Say(k);
            }
        }
        Runnable doWorkRunnable = new Runnable() {

            @Override
            public void run() {
                messageinput.setText("");
                messageinput.grabFocus();
            }
        };
        SwingUtilities.invokeLater(doWorkRunnable);
    }

    public void ExitChannel() {

        cm.Exit("user left channel");
        inchannel = false;

        ClearPlayers();

    }

    public void AddMessage(final String s) {
        AddMessage(s, true);
    }

    public void AddMessage(final String s, final boolean urls) {

        Runnable doWorkRunnable = new Runnable() {

            @Override
            public void run() {
                if (chatpanel != null) {
                    chatpanel.AddMessage(s, urls);
                }
            }
        };
        SwingUtilities.invokeLater(doWorkRunnable);

    }

    public void SetMessageWindow(final String s) {
        if (!inchannel) {
            return;
        }
        chatpanel.SetContents(s);
    }

    @Override
    public void Update() {
        //
    }

    @Override
    public void NewEvent(final CEvent e) {
        if (e.IsEvent("SAID")) {
            if (e.data[1].equalsIgnoreCase(Channel)) {
                // Add!!!!!
                final String time = CTextColours.getHTMLTimestamp();
                final String Msg = CTextColours.getChatHTML(Misc.makeSentence(e.data, 3), false);
                final String User = CTextColours.getUserChatHTML(e.data[2]);
                logger.info(CStringHelper.getTimestamp() + "<" + e.data[2] + "> " + Misc.makeSentence(e.data, 3));


                Runnable doWorkRunnable = new Runnable() {

                    @Override
                    public void run() {
                        if (MultiLineMenuItem.isSelected()) {
                            if (lastsent.equalsIgnoreCase(User.trim()) == false) {
                                AddMessage(User + "<br>" + time + Msg);
                                lastsent = User.trim();
                            } else {
                                AddMessage(time + Msg);
                            }
                        } else {
                            AddMessage(time + User + Msg);
                        }
                        if (!chatpanel.isShowing()) {
                            CEvent e2 = new CEvent(CEventTypes.CHANNELUNREAD + " " + Channel);
                            LMain.core.NewGUIEvent(e2);
                        }
                    }
                };
                SwingUtilities.invokeLater(doWorkRunnable);
            }
        } else if (e.IsEvent("SAIDEX")) {
            if (e.data[1].equalsIgnoreCase(Channel)) {
                // Add!!!!! SAIDEX main w00t heheh
                String time = CTextColours.getHTMLTimestamp();
                final String msg = CTextColours.getChatHTML(e.data[2] + " " +
                        Misc.makeSentence(e.data, 3), true);
                AddMessage(time + msg);
                logger.info(CStringHelper.getTimestamp() + "*" + e.data[2] + " " +
                        Misc.makeSentence(e.data, 3));
                lastsent = e.data[2];
                Runnable doWorkRunnable = new Runnable() {

                    @Override
                    public void run() {
                        if (!isShowing()) {
                            CEvent e2 = new CEvent(CEvent.CHANNELUNREAD + " " + Channel);
                            LMain.core.NewGUIEvent(e2);
                        }
                    }
                };
                SwingUtilities.invokeLater(doWorkRunnable);
            }
        } else if (e.IsEvent("LEFT")) {
            // Add!!!!!
            if (e.data[1].equalsIgnoreCase(Channel)) {
                if (e.data[2].equalsIgnoreCase(LMain.protocol.GetUsername())) {
                    logger.info("Logging Ended");
                    logger.info("\n");
                } else {
                    logger.info(e.data[2] + " left channel: " +
                            Misc.makeSentence(e.data, 3));
                    lastsent = "";
                }
                RemovePlayer(e.data[2], Misc.makeSentence(e.data, 3));
            }
        //lastsent="";
        } else if (e.IsEvent("JOINED")) {
            if (e.data[1].equalsIgnoreCase(Channel)) {
                logger.info("User " + e.data[2] + " joined");
                lastsent = "";
                AddPlayer(e.data[2], false);
            }
        //lastsent="";
        } else if (e.IsEvent("CLIENTS")) {
            // Add!!!!!
            if (e.data[1].equalsIgnoreCase(Channel)) {
                for (int c = 2; c < e.data.length; c++) {
                    //listModel.addElement(e.data[c]);
                    AddPlayer(e.data[c], true);
                }
            }
        } else if (e.IsEvent("CHANNELTOPIC")) {
            // Add!!!!!
            if (e.data[1].equalsIgnoreCase(Channel)) {
                //Integer x = new Integer(e.data[3]);
                Date topicset = new Date(Long.parseLong(e.data[3]));
                String topic = Misc.toHTML(Misc.makeSentence(e.data, 4));
                topic = topic.replaceAll("\\\\n", "<br>");
                topic = org.darkstars.battlehub.gui.CChatPanel.ParseChatText(topic);
                topic = "<font face=\"Bitstream Vera Sans Mono, Andale Mono, " +
                        "Consolas, Courier New, Courier, Monospace\" size=\"3\">" +
                        "Topic : <br>" + topic + "<br> set by :" + e.data[2] +
                        " on " + topicset.toString() + "</font><br>";
                final String finalTopic = topic;
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        if (chatpanel != null) {
                            chatpanel.AddRawHTML(finalTopic);
                        }
                    }
                });

                logger.info("TOPIC: " + Misc.makeSentence(e.data, 4));
                lastsent = "";
            }
        //lastsent="";
        } else if (e.IsEvent("CHANNELMESSAGE")) {
            // Add!!!!!
            if (e.data[1].equalsIgnoreCase(Channel)) {
                logger.info("ChannelMessage : " + Misc.makeSentence(e.data, 2));
                AddMessage("<font face=\"Arial, Helvetica, sans-serif\" " +
                        "size=\"3\" color=\"" + ColourHelper.ColourToHex(Color.BLUE) + "\"><b>ChannelMessage : " + Misc.toHTML(Misc.makeSentence(e.data, 2)) + "</b></font>");
            }
        } else if (e.data[0].equalsIgnoreCase("FORCELEAVECHANNEL")) {
            if (e.data[1].equalsIgnoreCase(Channel)) {
                //if(e.data[2].equalsIgnoreCase (e.connection.username)){
                // We've been kicked, oh noes!!!!! Close the window and show a message in the message window!!!!'
                logger.info("You got kicked from " + this.Channel + " reason:: " + Misc.makeSentence(e.data, 3));
                LMain.Toasts.AddMessage("You got kicked from " + this.Channel +
                        " reason:: " + Misc.makeSentence(e.data, 3));
                ExitChannel();
                logger.info("Logging Ended");
                logger.info("\n");
            //}else{
            //    // Thank god, it was a different user who got kicked! Display a nice blue message to celebrate
            //    AddMessage ("<font face=\"Arial, Helvetica, sans-serif\" size=\"3\" color=\"333399\"><b>User got kicked! : " +e.data[2]+" reason: "+Misc.makeSentence (e.data,3)+"</b></font>");
            //}
            }
        }
    }

    void RemovePlayer(final String player, final String reason) {

        //final 
        Runnable doWorkRunnable = new Runnable() {

            @Override
            public void run() {
                CPlayer p = LMain.playermanager.GetPlayer(player);
                playerTablePanel.RemovePlayer(p);

                if (FilJoin.isSelected() == false) {
                    String r = "";
                    if (reason.equalsIgnoreCase("") == false) {
                        r = " reason: " + reason;
                    }
                    AddMessage("<font face=\"Arial, Helvetica, sans-serif\" " +
                            "size=\"3\" color=\"996699\"><b> " + player + " has" +
                            " Left #" + Channel + " " + r + "</b></font>", false);
                }

            }
        };
        SwingUtilities.invokeLater(doWorkRunnable);
    }

    void ClearPlayers() {
        Runnable doWorkRunnable = new Runnable() {

            @Override
            public void run() {
                playerTablePanel.ClearPlayers();
            //jList1.removeAll ();
            }            //listModel.clear ();
        };
        SwingUtilities.invokeLater(doWorkRunnable);
    }

    void AddPlayer(final String player, final boolean JustJoined) {
        final CPlayer p = LMain.playermanager.GetPlayer(player);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                playerTablePanel.AddPlayer(p);
            }
        });
        
        if (!JustJoined) {
            String tflag = " ";
            if (Boolean.valueOf(CUserSettings.GetValue("ui.channelview.userjoinflags", "false"))) {
                tflag = p.GetFlagHTML(LM)+ " &nbsp; ";
            }
            if (Boolean.valueOf(CUserSettings.GetValue("ui.channelview.userjoinranks", "false"))) {
                tflag += p.GetRankHTML(LM);
            }
            final String flag = tflag;
            
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    if (FilJoin.isSelected() == false) {
                        AddMessage("<font face=\"Arial, Helvetica, " +
                                "sans-serif\" size=\"3\" color=\"00AA22\">" +
                                "<b> " + flag + " &nbsp; " + player + " has Joined" +
                                " #" + Channel + "</b></font>", false);
                    }
                }
            });
        }

    }
    //}
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        optionsPopupMenu = new javax.swing.JPopupMenu();
        MultiLineMenuItem = new javax.swing.JCheckBoxMenuItem();
        ClearChatMenuItem = new javax.swing.JMenuItem();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        messageinput = new javax.swing.JTextArea();
        jButton3 = new javax.swing.JButton();
        chatToolBar = new javax.swing.JToolBar();
        JAutoScrollToggle = new javax.swing.JToggleButton();
        FilJoin = new javax.swing.JToggleButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        chatpanel = new org.darkstars.battlehub.gui.CChatPanel();
        playerTablePanel = new CPlayerTablePanel(LM);
        jPanel1 = new javax.swing.JPanel();
        channelPrivateLabel = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        MultiLineMenuItem.setSelected(Boolean.parseBoolean(CUserSettings.GetValue("ui.chat.multiline", "true")));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/darkstars/battlehub/languages"); // NOI18N
        MultiLineMenuItem.setText(bundle.getString("CChannel.MultiLineMenuItem.text")); // NOI18N
        MultiLineMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/UI/multi_line.gif"))); // NOI18N
        MultiLineMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MultiLineMenuItemActionPerformed(evt);
            }
        });
        optionsPopupMenu.add(MultiLineMenuItem);

        ClearChatMenuItem.setText(bundle.getString("CChannel.ClearChatMenuItem.text")); // NOI18N
        ClearChatMenuItem.setComponentPopupMenu(optionsPopupMenu);
        ClearChatMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearChatMenuItemActionPerformed(evt);
            }
        });
        optionsPopupMenu.add(ClearChatMenuItem);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });

        jSplitPane2.setDividerLocation(jSplitPane2.getHeight()-200);
        jSplitPane2.setDividerSize(8);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.setResizeWeight(0.95);
        jSplitPane2.setOneTouchExpandable(true);

        jScrollPane3.setBackground(new java.awt.Color(255, 255, 255));

        messageinput.setColumns(20);
        messageinput.setFont(CUISettings.GetFont(12,false));
        messageinput.setLineWrap(true);
        messageinput.setRows(1);
        messageinput.setWrapStyleWord(true);
        messageinput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                messageinputKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(messageinput);

        jButton3.setFont(CUISettings.GetFont(12,false));
        jButton3.setText(bundle.getString("CChannel.jButton3.text")); // NOI18N
        jButton3.setMargin(new java.awt.Insets(3, 6, 3, 6));
        jButton3.setNextFocusableComponent(messageinput);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        chatToolBar.setRollover(true);

        JAutoScrollToggle.setFont(CUISettings.GetFont(12,false));
        JAutoScrollToggle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/UI/downarrow.gif"))); // NOI18N
        JAutoScrollToggle.setSelected(true);
        JAutoScrollToggle.setText(bundle.getString("CChannel.JAutoScrollToggle.text")); // NOI18N
        JAutoScrollToggle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                JAutoScrollToggleMousePressed(evt);
            }
        });
        JAutoScrollToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JAutoScrollToggleActionPerformed(evt);
            }
        });
        chatToolBar.add(JAutoScrollToggle);

        FilJoin.setFont(CUISettings.GetFont(12,false));
        FilJoin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/UI/filter.gif"))); // NOI18N
        FilJoin.setText(bundle.getString("CChannel.FilJoin.text")); // NOI18N
        chatToolBar.add(FilJoin);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(chatToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(chatToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        chatToolBar.add(CSmileyManager.GetSmileyPicker(messageinput));

        jSplitPane2.setRightComponent(jPanel2);

        jSplitPane1.setDividerSize(8);
        jSplitPane1.setResizeWeight(0.98);
        jSplitPane1.setAutoscrolls(true);
        jSplitPane1.setContinuousLayout(true);
        jSplitPane1.setOneTouchExpandable(true);

        javax.swing.GroupLayout chatpanelLayout = new javax.swing.GroupLayout(chatpanel);
        chatpanel.setLayout(chatpanelLayout);
        chatpanelLayout.setHorizontalGroup(
            chatpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 442, Short.MAX_VALUE)
        );
        chatpanelLayout.setVerticalGroup(
            chatpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 198, Short.MAX_VALUE)
        );

        jSplitPane1.setLeftComponent(chatpanel);

        javax.swing.GroupLayout playerTablePanelLayout = new javax.swing.GroupLayout(playerTablePanel);
        playerTablePanel.setLayout(playerTablePanelLayout);
        playerTablePanelLayout.setHorizontalGroup(
            playerTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        playerTablePanelLayout.setVerticalGroup(
            playerTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 198, Short.MAX_VALUE)
        );

        jSplitPane1.setRightComponent(playerTablePanel);

        jSplitPane2.setLeftComponent(jSplitPane1);

        channelPrivateLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        channelPrivateLabel.setText(bundle.getString("CChannel.channelPrivateLabel.text")); // NOI18N

        jButton2.setFont(CUISettings.GetFont(12,false));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/UI/comment_new.gif"))); // NOI18N
        jButton2.setText(bundle.getString("CChannel.jButton2.text")); // NOI18N
        jButton2.setToolTipText(bundle.getString("CChannel.jButton2.toolTipText")); // NOI18N
        jButton2.setFocusPainted(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/categories/preferences-system.png"))); // NOI18N
        jButton5.setText(bundle.getString("CChannel.jButton5.text")); // NOI18N
        jButton5.setToolTipText(bundle.getString("CChannel.jButton5.toolTipText")); // NOI18N
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton5MousePressed(evt);
            }
        });
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton1.setFont(CUISettings.GetFont(12,false));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/system-log-out.png"))); // NOI18N
        jButton1.setText(bundle.getString("CChannel.jButton1.text")); // NOI18N
        jButton1.setToolTipText(bundle.getString("CChannel.jButton1.toolTipText")); // NOI18N
        jButton1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 0, 0), 2, true));
        jButton1.setFocusPainted(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(channelPrivateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton2, jButton5});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
            .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
            .addComponent(channelPrivateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {channelPrivateLabel, jButton1, jButton2, jButton5});

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() | java.awt.Font.BOLD, jLabel1.getFont().getSize()+3));
        jLabel1.setText("#"+Channel + " channel");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // </editor-fold>
    // </editor-fold>
// </editor-fold>
    //Point prevsize = new Point();
private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
            // TODO add your handling code here:
            /*int new_width = getWidth();
            int new_height = getHeight();
            prevsize.setLocation(new_width, new_height);*/
}//GEN-LAST:event_formComponentResized

    private void JAutoScrollToggleActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JAutoScrollToggleActionPerformed
        chatpanel.SetAutoScroll(JAutoScrollToggle.isSelected());
    }//GEN-LAST:event_JAutoScrollToggleActionPerformed

    private void jButton2ActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String s = org.darkstars.battlehub.CUserSettings.GetStartScript();
        if (s.trim().equals("")) {
            s += System.getProperty("line.separator");
        }
        s += "/join ";
        s += Channel;
        org.darkstars.battlehub.CUserSettings.SaveStartScript(s);
        AddMessage("<font face=\"Arial, Helvetica, sans-serif\" size=\"3\" " +
                "color=\"00AA22\"><b> channel added to startup list</b></font>", false);

        CEvent e = new CEvent(CEvent.LOGONSCRIPTCHANGE);
        LMain.core.NewGUIEvent(e);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void formComponentShown (java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        CEvent e = new CEvent(CEvent.CHANNELREAD + " " + Channel);
        if (LM != null) {
            LMain.core.NewGUIEvent(e);
        }
    }//GEN-LAST:event_formComponentShown

    private void messageinputKeyPressed (java.awt.event.KeyEvent evt) {//GEN-FIRST:event_messageinputKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (evt.isShiftDown()) {
                return;
            }
            SendMessage();
        }
    }//GEN-LAST:event_messageinputKeyPressed

    private void JAutoScrollToggleMousePressed (java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JAutoScrollToggleMousePressed
//
        /*if(JAutoScrollToggle.isSelected ()){
        jast.schedule (jas,
        1000,        //initial delay
        100);  //subsequent rate
        } else{
        jast.cancel ();
        }*/
    }//GEN-LAST:event_JAutoScrollToggleMousePressed

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
        messageinput.requestFocus();
    }//GEN-LAST:event_formFocusGained

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MousePressed
        optionsPopupMenu.show(jButton5, jButton5.getHeight(), 0);
    }//GEN-LAST:event_jButton5MousePressed

    private void ClearChatMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearChatMenuItemActionPerformed
        chatpanel.ClearContents();
    }//GEN-LAST:event_ClearChatMenuItemActionPerformed

    private void MultiLineMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MultiLineMenuItemActionPerformed
        CUserSettings.PutValue("ui.chat.multiline",
                String.valueOf(MultiLineMenuItem.isSelected()));
    }//GEN-LAST:event_MultiLineMenuItemActionPerformed

private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    SendMessage();
}//GEN-LAST:event_jButton3ActionPerformed

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    ExitChannel();
}//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem ClearChatMenuItem;
    private javax.swing.JToggleButton FilJoin;
    private javax.swing.JToggleButton JAutoScrollToggle;
    private javax.swing.JCheckBoxMenuItem MultiLineMenuItem;
    private javax.swing.JLabel channelPrivateLabel;
    private javax.swing.JToolBar chatToolBar;
    private org.darkstars.battlehub.gui.CChatPanel chatpanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTextArea messageinput;
    private javax.swing.JPopupMenu optionsPopupMenu;
    private org.darkstars.battlehub.gui.CPlayerTablePanel playerTablePanel;
    // End of variables declaration//GEN-END:variables
    @Override
    public void Init(ICentralClass L) {
        LM = (LMain) L;
    }

    @Override
    public void NewGUIEvent(CEvent e) {
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