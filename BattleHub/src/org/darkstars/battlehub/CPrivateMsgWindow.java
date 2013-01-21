/*
 * CPrivateMsgWindow.java
 *
 * Created on 27 May 2006, 21:57
 */

package org.darkstars.battlehub;

import org.darkstars.battlehub.framework.ICentralClass;
import org.darkstars.battlehub.gui.CChannel;
import org.darkstars.battlehub.helpers.AePlayWave;
import org.darkstars.battlehub.framework.CPlayer;
import org.darkstars.battlehub.gui.CChatLogFormatter;
import org.darkstars.battlehub.framework.CEvent;
import org.darkstars.battlehub.gui.CUISettings;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import org.darkstars.battlehub.framework.IModule;
import org.darkstars.battlehub.framework.Misc;
import org.darkstars.battlehub.gui.CSmileyManager;

/**
 *
 * @author  AF
 */

public class CPrivateMsgWindow extends javax.swing.JFrame implements IModule{

    public LMain LM;
    public CPlayer player;
    public boolean opened = false;
    public java.awt.Point origin = new java.awt.Point();
    public String lastsent = "";
    FileHandler txtLog = null;
    Logger logger = null;
    private HashMap actions;

    /**
     * Creates new form CPrivateMsgWindow
     */
    public CPrivateMsgWindow() {
        
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run() {
                initComponents();
            }
        });
    }

    public void Init(ICentralClass L, CPlayer p) {
        //
        LM = (LMain) L;
        player = p;

        SwingUtilities.invokeLater( new Runnable() {

            @Override
            public void run() {
                setTitle(java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("CPrivateMessageWindow.PM_:") + player.name);
            }
        });

        if (Misc.getInGameFromStatus(player.getStatus()) == 1) {
            ingame = true;
        } else {
            ingame = false;
        }
        try {

            File log = new File(LMain.GetAbsoluteLobbyFolderPathStatic() + "logs/pm/");
            if (!log.canRead()) {
                log.mkdir();
            }
            txtLog = new FileHandler(LMain.GetAbsoluteLobbyFolderPathStatic() + "logs/pm/" + p.name + ".txt", true);
            txtLog.setFormatter(new CChatLogFormatter());
            logger = Logger.getLogger("privatemsg." + p.name);
            logger.setLevel(Level.INFO);
            logger.addHandler(txtLog);
            logger.info("\n");
            logger.info("\n");
            logger.info("logging started");
            /*CEvent e = new CEvent ();
            e.data = new String[2];
            e.data[1] = Channel;
            e.data[0] = "channel_release_events";
            e.a = this;
            LM.NewGUIEvent (e);*/
        } catch (IOException ex) {
            Logger.getLogger(CChannel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(CChannel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void SendMessage() {
        if (LMain.protocol.Connected()) {
            //
            SwingUtilities.invokeLater( new Runnable() {

                @Override
                public void run() {
                    //
                    String[] lines = messageInput.getText().split("\n");
                    for (int n = 0; n < lines.length; n++) {
                        String[] command = lines[n].split(" ");
                        //if(command[0].equalsIgnoreCase ("/me")){
                        //LM.protocol.SendTraffic ("SAYPRIVEX " + player.name + " " + Misc.makeSentence (command,1));
                        //}else{
                        LMain.protocol.SendTraffic("SAYPRIVATE " + player.name + " " + lines[n]);
                        String Msg = lines[n];
                        Msg = Misc.toHTML(Msg);
                        String time = "<font face=\"Arial, Helvetica, sans-serif\" size=\"3\" color=\"" + CTextColours.GetColor("timestamps", "#aaaaaa") + "\">[" + Misc.easyDateFormat("hh:mm:ss") + "] </font>";
                        if (jToggleButton1.isSelected()) {
                            if (lastsent.equalsIgnoreCase(LMain.protocol.GetUsername().trim()) == false) {//
                                Add("<font face=\"Arial, Helvetica, sans-serif\" size=\"3\"><b><i>" + LMain.protocol.GetUsername() + " " + java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("CPrivateMessageWindow._says:")+"</i></b><br>" + time + "<font face=\"Arial, Helvetica, sans-serif\" size=\"3\">" + Msg + "</font>");
                                lastsent = LMain.protocol.GetUsername().trim();
                            } else {//<b><i>
                                Add(time + "<font face=\"Arial, Helvetica, sans-serif\" size=\"3\">" + Msg + "</font>");
                            }
                        } else {//<b><i>
                            Add(time + "<font face=\"Arial, Helvetica, sans-serif\" size=\"3\">" + LMain.protocol.GetUsername() + java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("CPrivateMessageWindow._says:") + Msg + "</font>");
                        }
                        //logger.info("<"+LM.protocol.GetUsername()+"> "+Msg);
                        //}
                    }

                    messageInput.setText("");
                }
            });
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        messageInput = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        chatToolBar = new javax.swing.JToolBar();
        jToggleButton1 = new javax.swing.JToggleButton();
        chat = new org.darkstars.battlehub.gui.CChatPanel();

        setIconImage(CUISettings.GetWindowIcon());
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(200);
        jSplitPane1.setDividerSize(8);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(0.9);

        messageInput.setColumns(20);
        messageInput.setFont(new java.awt.Font("Arial", 0, 12));
        messageInput.setLineWrap(true);
        messageInput.setRows(2);
        messageInput.setNextFocusableComponent(jButton1);
        messageInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                messageInputKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(messageInput);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/darkstars/battlehub/languages"); // NOI18N
        jButton1.setText(bundle.getString("Send")); // NOI18N
        jButton1.setMargin(new java.awt.Insets(2, 8, 2, 8));
        jButton1.setNextFocusableComponent(messageInput);
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton1MousePressed(evt);
            }
        });

        chatToolBar.setFloatable(false);
        chatToolBar.setRollover(true);

        jToggleButton1.setFont(new java.awt.Font("Arial", 0, 11));
        jToggleButton1.setSelected(true);
        jToggleButton1.setText(bundle.getString("Accumulate_messages")); // NOI18N
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });
        chatToolBar.add(jToggleButton1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chatToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(chatToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1))
        );

        chatToolBar.add(CSmileyManager.GetSmileyPicker(messageInput));

        jSplitPane1.setBottomComponent(jPanel2);

        javax.swing.GroupLayout chatLayout = new javax.swing.GroupLayout(chat);
        chat.setLayout(chatLayout);
        chatLayout.setHorizontalGroup(
            chatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 411, Short.MAX_VALUE)
        );
        chatLayout.setVerticalGroup(
            chatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );

        jSplitPane1.setLeftComponent(chat);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 431, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 323, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void messageInputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_messageInputKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (evt.isShiftDown()) {
                return;
            }
            SendMessage();
        }
}//GEN-LAST:event_messageInputKeyPressed

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        java.awt.Point p = getLocation();
        setLocation(p.x + evt.getX() - origin.x, p.y + evt.getY() - origin.y);
    }//GEN-LAST:event_formMouseDragged

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        origin.x = evt.getX();
        origin.y = evt.getY();
    }//GEN-LAST:event_formMousePressed

    private void jButton1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MousePressed
        SendMessage();
    }//GEN-LAST:event_jButton1MousePressed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        accumulate = jToggleButton1.isSelected();
    }//GEN-LAST:event_jToggleButton1ActionPerformed
    
    boolean accumulate = true;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new CPrivateMsgWindow().setVisible(false);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void createActionTable(JTextComponent textComponent) {
        actions = new HashMap();
        Action[] actionsArray = textComponent.getActions();
        for (int i = 0; i < actionsArray.length; i++) {
            Action a = actionsArray[i];
            actions.put(a.getValue(Action.NAME), a);
        }
    }

    private Action getActionByName(String name) {
        return (Action) (actions.get (name));
    }

    public void Add(String s) {
        logger.info(s);
        chat.AddMessage(s);
    }

    public void Update() {
        //
    }

    public void Open() {
        Runnable doWorkRunnable = new Runnable() {

            @Override
            public void run() {
                if(!isVisible()){
                    setVisible(true);
                }
            }
        };
        SwingUtilities.invokeLater(doWorkRunnable);
    }

    public void Hide() {
        Runnable doWorkRunnable = new Runnable() {

            @Override
            public void run() {
                setVisible(false);
            }
        };
        SwingUtilities.invokeLater(doWorkRunnable);
    }
    public boolean ingame = false;

    public void NewEvent(CEvent e) {
        if (e.IsEvent("REMOVEUSER")) {
            if (e.data[1].equals(player.name)) {
                String msg = java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("CPrivateMessageWindow.user_disconnected_at_")+ Misc.easyDateFormat("hh:mm:ss");
                msg = "<font face=\"Arial, Helvetica, sans-serif\" size=\"3\" color=\"996699\">" + msg + "</b></font>";
                Add(msg);
            }
        } else if (e.data[0].equalsIgnoreCase("CLIENTBATTLESTATUS")) {
            if (player.name.equals(e.data[1])) {
                if (!ingame) {
                    if (Misc.getInGameFromStatus(Integer.parseInt(e.data[2])) == 1) {
                        Add("<font face=\"Arial, Helvetica, sans-serif\" size=\"3\" color=\"996699\"><b>"+java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("CPrivateMessageWindow.user_ingame_at_") + Misc.easyDateFormat("hh:mm:ss")+"</b></font>");
                        ingame = true;
                    }
                } else {
                    if (Misc.getInGameFromStatus(Integer.parseInt(e.data[2])) == 0) {
                        Add("<font face=\"Arial, Helvetica, sans-serif\" size=\"3\" color=\"996699\"><b>"+java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("CPrivateMessageWindow.user_left_game_at_") + Misc.easyDateFormat("hh:mm:ss")+"</b></font>");
                        ingame = false;
                    }
                }
            }
        } else if (e.IsEvent("SAIDPRIVATE")) {
            if (e.data[1].equalsIgnoreCase(player.name)) {
                Open();
                //URL r = getClass ().getResource ("/");
                //if(r != null){
                new AePlayWave(LMain.GetAbsoluteLobbyFolderPathStatic() + "sounds/question.wav").start();

                String time = CTextColours.getHTMLTimestamp();
                String Msg = CTextColours.getChatHTML(Misc.makeSentence(e.data, 2),false);
                String User = CTextColours.getUserChatHTML(e.data[1]);

                if (accumulate) {
                    if (lastsent.equalsIgnoreCase(User.trim()) == false) {
                        Add(User +" <br>"+ time + Msg);
                        lastsent = User.trim();
                    } else {
                        Add(time + Msg);
                    }
                } else {
                    Add(time + User + " " + Msg);
                }


                //}
                /*if(jToggleButton1.isSelected ()==true){
                if(lastsent==0){
                Add (e.data[1] + " Says:\n   " + Misc.makeSentence (e.data,2));
                lastsent=2;
                }else if(lastsent==1){
                Add (e.data[1] + " Says:\n   " +Misc.makeSentence (e.data,2));
                lastsent=2;
                }else{
                Add ("   " + Misc.makeSentence (e.data,2));
                lastsent=2;
                }
                }else{
                Add (player.name + " Says:\n   " +Misc.makeSentence (e.data,2));
                lastsent=2;
                }*/
            }
        } else if (e.data[0].equals("SAIDPRIVEX")) {
            if (e.data[1].equals(player.name)) {
                Open();
                Add(player.name + " " + Misc.makeSentence(e.data, 2));
            }
        } else if (e.data[0].equals("ACCEPTED")) {
            Add(java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("CPrivateMessageWindow.logged_in_at_") + Misc.easyDateFormat("hh:mm:ss"));
        } else if (e.IsEvent(CEvent.LOGGEDOUT) || e.IsEvent(CEvent.LOGOUT)) {
            Add(java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("CPrivateMessageWindow.logged_out_at_") + Misc.easyDateFormat("hh:mm:ss"));
        }
    }

    @Override
    public void NewGUIEvent(CEvent e) {
        if (e.data[0].equals("OPENPRIV")) {
            if (e.data[1] == null) {
                return;
            }
            if (e.data[1].equalsIgnoreCase(player.name)) {
                Open();
            }
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.darkstars.battlehub.gui.CChatPanel chat;
    private javax.swing.JToolBar chatToolBar;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JTextArea messageInput;
    // End of variables declaration//GEN-END:variables

    @Override
    public void Init(ICentralClass L) {
        //
        
    }

    @Override
    public void OnRemove() {
        //
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
