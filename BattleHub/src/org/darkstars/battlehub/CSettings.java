/*
 * CSettings.java
 *
 * Created on 14 September 2007, 09:47
 */
package org.darkstars.battlehub;

import org.darkstars.battlehub.integration.CMeltraxLadder;
import org.darkstars.battlehub.framework.CEvent;
import org.darkstars.battlehub.gui.CUISettings;
import org.darkstars.battlehub.CUserSettings;
import org.darkstars.battlehub.gui.CView;
import java.awt.Color;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.darkstars.battlehub.framework.Misc;
import org.darkstars.battlehub.gui.CPathsChooser;
import org.jvnet.substance.skin.SkinInfo;

/**
 * This panel class contains settings and options controls for the user to modify
 * the lobby with, such as highlighted keywords in chat or tab row
 * placements.
 * @author  AF-StandardUsr
 */
public class CSettings extends CView {

    CVectorListModel looknfeelsListModel;
    public CSettings() {
        this.setTitle("CSettings");
        looknfeelsListModel = new CVectorListModel();
        

        Runnable doWorkRunnable1 = new Runnable() {

            @Override
            public void run() {
                initComponents();
                
                
            }
        };
        SwingUtilities.invokeLater(doWorkRunnable1);
        
        
        SwingUtilities.invokeLater( new Runnable() {

            @Override
            public void run() {
                //setVisible(true);
                UIManager.LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
                
                for (int i=0; i<looks.length; i++) {

                    looknfeelsListModel.addElement(looks[i].getName());
                }
                Vector<SkinInfo> vals = new Vector(org.jvnet.substance.SubstanceLookAndFeel.getAllSkins().values());
                //Set<String> keys = o.keySet();
                for(SkinInfo s : vals){
                    //
                    looknfeelsListModel.addElement(s.getDisplayName());
                }
                autologincheckbox.setVisible(true);
                backButton.setVisible(false);
                String[] strings = CUserSettings.GetValue("ui.texthighlights").split(",");
                CVectorListModel v = (CVectorListModel) Highlights.getModel();
                for (String s : strings) {
                    v.addElement(s);
                }
            }
        });
    }

    /** Creates a new CSettings form. This form can be displayed on tis own as
     * other CView classes in the main window or it can be embedded inside
     * another component as a panel.
     *
     * If embedding inside a panel add it to LMain event handling by using
     * AddNonDispView() instead of AddView()
     *
     * @param LM - a reference to the LMain object used as the root object in
     * the lobby heirarchy.
     *
     * @param fullview - a boolean representing wether this form will be shown
     * inside another form or as a complete page. A true value will show a "back
     * to menu" button that changes the current view to the splash screen. This
     * could cause errors if set to true if displayed inside another view such
     * as inside a tab control
     */
    public CSettings(LMain LM, final boolean fullview) {
        this.LM = LM;
        setTitle("CSettings");
        
        looknfeelsListModel = new CVectorListModel();

        //Runnable doWorkRunnable1 = new Runnable() {

        //    @Override
        //    public void run() {
                initComponents();
                
                
        //    }
        //};
        //SwingUtilities.invokeLater(doWorkRunnable1);
        //Runnable doWorkRunnable1 = new Runnable() {
        //    public void run() {
        //initComponents();
        //    }
        //};
        //SwingUtilities.invokeLater(doWorkRunnable1);


        Runnable doWorkRunnable = new Runnable() {

            @Override
            public void run() {
                //setVisible(true);
                
                UIManager.LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
                
                for (int i=0; i<looks.length; i++) {

                    looknfeelsListModel.addElement(looks[i].getName());
                }
                
                autologincheckbox.setVisible(!fullview);
                backButton.setVisible(fullview);
                String[] strings = CUserSettings.GetValue("ui.texthighlights").split(",");
                CVectorListModel v = (CVectorListModel) Highlights.getModel();
                for (String s : strings) {
                    v.addElement(s);
                }
            }
        };
        SwingUtilities.invokeLater(doWorkRunnable);
    }

    /**
     * Retrieves the chat word highlights from the UI and saves them to the
     * settings class
     */
    public void SetHighlights() {
        String h = "";
        ListModel m = Highlights.getModel();
        for (int i = 0; i < m.getSize(); i++) {
            String s = (String) m.getElementAt(i);
            if (s == null) {
                continue;
            }
            if (s.trim().equals("")) {
                continue;
            }
            h += s;
            if (i + 1 != m.getSize()) {
                h += ",";
            }
        }
        CUserSettings.PutValue("ui.texthighlights", h);
    }

    /**
     * Handles new GUI Events dispatched from the LMain class.
     * @param e - a gui CEvent object 
     */
    @Override
    public void NewGUIEvent(final CEvent e) {
        // do GUI stuff
        if (e.IsEvent(CEvent.LOGONSCRIPTCHANGE)) {
            Runnable doWorkRunnable = new Runnable() {

                @Override
                public void run() {
                    String s = org.darkstars.battlehub.CUserSettings.GetStartScript();
                    CLogonScriptText.setText(s);
                }
            };
            SwingUtilities.invokeLater(doWorkRunnable);
        }
    }

    public Color GetTextColour(String t, Color def) {
        if (t != null) {
            if (t.equals("") == false) {
                return Color.decode(t);
            }
        }
        return def;
    }

    public void RedrawTextPreview() {
        // draw the text preview
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        settingsTabPane = new javax.swing.JTabbedPane();
        InitialPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jButton20 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        FilePathsPanel = new javax.swing.JPanel();
        internalFilePanel = new javax.swing.JPanel();
        OnLoginPanel = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        CLogonScriptText = new javax.swing.JTextPane();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        autologincheckbox = new javax.swing.JCheckBox();
        InterfacePane = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jLabel12 = new javax.swing.JLabel();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jRadioButton7 = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        BattlePositionCheckBox = new javax.swing.JCheckBox();
        HighlightPane = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        Highlights = new javax.swing.JList();
        jLabel14 = new javax.swing.JLabel();
        NewHighlightEntry = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        chatPanel = new javax.swing.JPanel();
        jCheckBox3 = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        chatTextLabel = new javax.swing.JLabel();
        chatTextColourButton = new javax.swing.JButton();
        actionChatTextColourButton = new javax.swing.JButton();
        actionChatTextLabel = new javax.swing.JLabel();
        usernamesColourButton = new javax.swing.JButton();
        usernamesLabel = new javax.swing.JLabel();
        timestampsColourButton = new javax.swing.JButton();
        timestampsLabel = new javax.swing.JLabel();
        jButton27 = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        jButton28 = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        jButton29 = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        jButton30 = new javax.swing.JButton();
        jLabel35 = new javax.swing.JLabel();
        jButton31 = new javax.swing.JButton();
        jLabel36 = new javax.swing.JLabel();
        jButton32 = new javax.swing.JButton();
        jLabel37 = new javax.swing.JLabel();
        jButton33 = new javax.swing.JButton();
        jLabel38 = new javax.swing.JLabel();
        jButton34 = new javax.swing.JButton();
        jLabel39 = new javax.swing.JLabel();
        LadderPane = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        ladderPassBox = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        backButton = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();

        jList1.setModel(looknfeelsListModel);
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
                .addContainerGap())
        );

        settingsTabPane.setFont(CUISettings.GetFont(12,false));

        jLabel7.setFont(jLabel7.getFont().deriveFont(jLabel7.getFont().getSize()+7f));
        jLabel7.setText("<html>Please Choose an option below, changes are saved automatically"); // NOI18N

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/apps/system-file-manager.png"))); // NOI18N
        jButton9.setText("Set up Paths"); // NOI18N
        jButton9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jLabel8.setText("<html>Set up the location of game engines"); // NOI18N

        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/apps/utilities-terminal.png"))); // NOI18N
        jButton12.setText("Initial Commands"); // NOI18N
        jButton12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jLabel9.setText("<html>Set up the lobby to join channels, message people, and do other things when you log in"); // NOI18N

        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/apps/preferences-system-windows.png"))); // NOI18N
        jButton13.setText("Interface Settings"); // NOI18N
        jButton13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/UI/colourpickerwheel16.png"))); // NOI18N
        jButton14.setText("Chat Highlighting"); // NOI18N
        jButton14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/categories/applications-games.png"))); // NOI18N
        jButton15.setText("Ladder Settings"); // NOI18N
        jButton15.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton16.setText("Spring Settings"); // NOI18N
        jButton16.setEnabled(CUserSettings.GetValue("settings.command", "").equals("")==false);
        jButton16.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jLabel10.setText("<html>Change themes, re-arrange tabs, the battle list, and customize other controls"); // NOI18N

        jLabel11.setText("<html>Set up and control ladder game integration, to play against other ranked players"); // NOI18N

        jLabel13.setText("<html>Change springs graphics, screen resolution, and other engine related settings"); // NOI18N

        jLabel16.setText("<html>Highlight words and phrases in conversations in different colours"); // NOI18N

        jButton20.setText("First Run Wizard"); // NOI18N
        jButton20.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        jLabel23.setText("<html>Re-run the initial startup wizard"); // NOI18N

        javax.swing.GroupLayout InitialPanelLayout = new javax.swing.GroupLayout(InitialPanel);
        InitialPanel.setLayout(InitialPanelLayout);
        InitialPanelLayout.setHorizontalGroup(
            InitialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InitialPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(InitialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, InitialPanelLayout.createSequentialGroup()
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 248, Short.MAX_VALUE))
                    .addComponent(jLabel7)
                    .addGroup(InitialPanelLayout.createSequentialGroup()
                        .addGroup(InitialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(InitialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)
                            .addComponent(jLabel16)
                            .addComponent(jLabel11)
                            .addComponent(jLabel13)
                            .addComponent(jLabel23))))
                .addContainerGap())
        );
        InitialPanelLayout.setVerticalGroup(
            InitialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InitialPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(InitialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton9)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(InitialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton12)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(InitialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton13)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(InitialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton14)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(InitialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton15)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(InitialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton16)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(InitialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton20)
                    .addComponent(jLabel23))
                .addGap(107, 107, 107))
        );

        InitialPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton12, jButton13, jButton14, jButton15, jButton16, jButton20, jButton9, jLabel10, jLabel11, jLabel13, jLabel16, jLabel23, jLabel8, jLabel9});

        settingsTabPane.addTab("Overview", InitialPanel);

        FilePathsPanel.setEnabled(!Misc.isWindows());

        internalFilePanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout FilePathsPanelLayout = new javax.swing.GroupLayout(FilePathsPanel);
        FilePathsPanel.setLayout(FilePathsPanelLayout);
        FilePathsPanelLayout.setHorizontalGroup(
            FilePathsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(internalFilePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 616, Short.MAX_VALUE)
        );
        FilePathsPanelLayout.setVerticalGroup(
            FilePathsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(internalFilePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
        );

        internalFilePanel.add(new CPathsChooser());

        settingsTabPane.addTab("File Paths", new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/apps/system-file-manager.png")), FilePathsPanel); // NOI18N

        OnLoginPanel.setFont(CUISettings.GetFont(12,false));
        OnLoginPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                OnLoginPanelComponentShown(evt);
            }
        });

        CLogonScriptText.setFont(CUISettings.GetFont(12,false));
        CLogonScriptText.setText(CUserSettings.GetStartScript());
        CLogonScriptText.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                CLogonScriptTextComponentHidden(evt);
            }
            public void componentShown(java.awt.event.ComponentEvent evt) {
                CLogonScriptTextComponentShown(evt);
            }
        });
        jScrollPane7.setViewportView(CLogonScriptText);

        jButton2.setFont(CUISettings.GetFont(12,false));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/document-save.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/darkstars/battlehub/languages"); // NOI18N
        jButton2.setText(bundle.getString("CChannelView.jButton2.text")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(CUISettings.GetFont(12,false));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/edit-undo.png"))); // NOI18N
        jButton3.setText(bundle.getString("CChannelView.jButton3.text")); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel20.setFont(CUISettings.GetFont(12,false));
        jLabel20.setText(bundle.getString("CChannelView.jLabel20.text")); // NOI18N

        autologincheckbox.setSelected(Boolean.valueOf(CUserSettings.GetValue("autologin", "false")));
        autologincheckbox.setText("Auto Login"); // NOI18N
        autologincheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autologincheckboxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout OnLoginPanelLayout = new javax.swing.GroupLayout(OnLoginPanel);
        OnLoginPanel.setLayout(OnLoginPanelLayout);
        OnLoginPanelLayout.setHorizontalGroup(
            OnLoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OnLoginPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(OnLoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, OnLoginPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(OnLoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(autologincheckbox, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        OnLoginPanelLayout.setVerticalGroup(
            OnLoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, OnLoginPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(autologincheckbox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(OnLoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(OnLoginPanelLayout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3))
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE))
                .addContainerGap())
        );

        settingsTabPane.addTab("On Login", new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/apps/utilities-terminal.png")), OnLoginPanel); // NOI18N

        buttonGroup2.add(jRadioButton1);
        jRadioButton1.setSelected(CUserSettings.GetValue("UI.toptabpane.placement", "" + javax.swing.JTabbedPane.TOP).equalsIgnoreCase("" + javax.swing.JTabbedPane.TOP));
        jRadioButton1.setText("Top"); // NOI18N
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        buttonGroup2.add(jRadioButton2);
        jRadioButton2.setSelected(CUserSettings.GetValue("UI.toptabpane.placement", "" + javax.swing.JTabbedPane.TOP).equalsIgnoreCase("" + javax.swing.JTabbedPane.BOTTOM));
        jRadioButton2.setText("Bottom"); // NOI18N
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        buttonGroup2.add(jRadioButton3);
        jRadioButton3.setSelected(CUserSettings.GetValue("UI.toptabpane.placement", "" + javax.swing.JTabbedPane.TOP).equalsIgnoreCase("" + javax.swing.JTabbedPane.LEFT));
        jRadioButton3.setText("Left"); // NOI18N
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });

        buttonGroup2.add(jRadioButton4);
        jRadioButton4.setSelected(CUserSettings.GetValue("UI.toptabpane.placement", "" + javax.swing.JTabbedPane.TOP).equalsIgnoreCase("" + javax.swing.JTabbedPane.RIGHT));
        jRadioButton4.setText("Right"); // NOI18N
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton4ActionPerformed(evt);
            }
        });

        jLabel12.setText("Main lobby tab control orientation"); // NOI18N

        buttonGroup1.add(jRadioButton5);
        jRadioButton5.setSelected(CUserSettings.GetValue("looknfeel", UIManager.getSystemLookAndFeelClassName()).contains("substance"));
        jRadioButton5.setText("Use Substance theme"); // NOI18N
        jRadioButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton5ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton6);
        jRadioButton6.setSelected(CUserSettings.GetValue("looknfeel", UIManager.getSystemLookAndFeelClassName()).equals(UIManager.getSystemLookAndFeelClassName()));
        jRadioButton6.setText("Use Native OS theme"); // NOI18N
        jRadioButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton6ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton7);
        jRadioButton7.setSelected(CUserSettings.GetValue("looknfeel", UIManager.getSystemLookAndFeelClassName()).equals("defjava"));
        jRadioButton7.setText("Use default Java theme"); // NOI18N
        jRadioButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton7ActionPerformed(evt);
            }
        });

        jLabel1.setText("Main Lobby GUI theme (restart for changes to take effect)"); // NOI18N

        jCheckBox1.setSelected(Boolean.valueOf(CUserSettings.GetValue("angledtabs", "true")));
        jCheckBox1.setText("Use substance sideways tabs on tabs to the left and right"); // NOI18N
        jCheckBox1.setEnabled(CUserSettings.GetValue("looknfeel", "org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel").contains("substance"));
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        BattlePositionCheckBox.setSelected(CUserSettings.GetValue("ui.channelview.battlelisttopposition", "true").equals("false"));
        BattlePositionCheckBox.setText("Show Battle List underneath the main tab control (TASClient style)"); // NOI18N
        BattlePositionCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BattlePositionCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout InterfacePaneLayout = new javax.swing.GroupLayout(InterfacePane);
        InterfacePane.setLayout(InterfacePaneLayout);
        InterfacePaneLayout.setHorizontalGroup(
            InterfacePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InterfacePaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(InterfacePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addGroup(InterfacePaneLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(InterfacePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jRadioButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jRadioButton1)
                            .addComponent(jRadioButton3)
                            .addComponent(jRadioButton4)))
                    .addComponent(jLabel1)
                    .addGroup(InterfacePaneLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(InterfacePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(InterfacePaneLayout.createSequentialGroup()
                                .addComponent(jRadioButton5)
                                .addGap(18, 18, 18)
                                .addComponent(jCheckBox1))
                            .addComponent(jRadioButton7)
                            .addComponent(jRadioButton6)))
                    .addComponent(BattlePositionCheckBox))
                .addContainerGap(146, Short.MAX_VALUE))
        );
        InterfacePaneLayout.setVerticalGroup(
            InterfacePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InterfacePaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(InterfacePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton5)
                    .addComponent(jCheckBox1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton6)
                .addGap(18, 18, 18)
                .addComponent(BattlePositionCheckBox)
                .addContainerGap(125, Short.MAX_VALUE))
        );

        settingsTabPane.addTab("UI Settings", new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/apps/preferences-system-windows.png")), InterfacePane); // NOI18N

        jScrollPane3.setMinimumSize(new java.awt.Dimension(19, 60));
        jScrollPane3.setPreferredSize(new java.awt.Dimension(2, 60));

        Highlights.setModel(new CVectorListModel());
        Highlights.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane3.setViewportView(Highlights);

        jLabel14.setFont(jLabel14.getFont().deriveFont(jLabel14.getFont().getSize()+3f));
        jLabel14.setText("Highlighted text"); // NOI18N

        jButton1.setText("Add"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton4.setText("Remove Selected"); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Remove All"); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel15.setText("Highlights words and text in colour. note:: accepts regex highlighting"); // NOI18N

        javax.swing.GroupLayout HighlightPaneLayout = new javax.swing.GroupLayout(HighlightPane);
        HighlightPane.setLayout(HighlightPaneLayout);
        HighlightPaneLayout.setHorizontalGroup(
            HighlightPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HighlightPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(HighlightPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HighlightPaneLayout.createSequentialGroup()
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HighlightPaneLayout.createSequentialGroup()
                        .addComponent(NewHighlightEntry, javax.swing.GroupLayout.DEFAULT_SIZE, 535, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1))
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        HighlightPaneLayout.setVerticalGroup(
            HighlightPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HighlightPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addGap(12, 12, 12)
                .addGroup(HighlightPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(NewHighlightEntry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(HighlightPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addContainerGap())
        );

        settingsTabPane.addTab("Highlighting", new javax.swing.ImageIcon(getClass().getResource("/images/UI/colourpickerwheel16.png")), HighlightPane); // NOI18N

        jCheckBox3.setSelected(Boolean.valueOf(CUserSettings.GetValue("ui.channelview.userjoinranks", "false")));
        jCheckBox3.setText("Show ranks when User joins"); // NOI18N
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        jEditorPane1.setEditable(false);
        jEditorPane1.setEnabled(false);
        jScrollPane2.setViewportView(jEditorPane1);

        jLabel18.setText("Preview:"); // NOI18N

        jLabel19.setText("<html>At the moment only a few of the text types are customizable but in the next release the full spectrum should be available.<br>\n<br>\nThe preview pane will also contain something useful in the next release."); // NOI18N

        jCheckBox2.setSelected(Boolean.valueOf(CUserSettings.GetValue("ui.channelview.userjoinflags", "true")));
        jCheckBox2.setText("Show flags when User joins"); // NOI18N
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jCheckBox4.setSelected(CUserSettings.GetValue("ui.chat.says", "true").equals("true"));
        jCheckBox4.setText("Show Says: in chat text");
        jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });

        chatTextLabel.setForeground(GetTextColour(CUserSettings.GetValue("textcolours.chat-text",""),Color.black));
        chatTextLabel.setText("Chat Text");

        chatTextColourButton.setBackground(GetTextColour(CUserSettings.GetValue("textcolours.chat-text",""),Color.black));
        chatTextColourButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/UI/colourpickerwheel16.png"))); // NOI18N
        chatTextColourButton.setMargin(new java.awt.Insets(2, 8, 2, 8));
        chatTextColourButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chatTextColourButtonActionPerformed(evt);
            }
        });

        actionChatTextColourButton.setBackground(GetTextColour(CUserSettings.GetValue("textcolours.action-chat-text",""),Color.BLUE));
        actionChatTextColourButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/UI/colourpickerwheel16.png"))); // NOI18N
        actionChatTextColourButton.setMargin(new java.awt.Insets(2, 8, 2, 8));
        actionChatTextColourButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionChatTextColourButtonActionPerformed(evt);
            }
        });

        actionChatTextLabel.setForeground(GetTextColour(CUserSettings.GetValue("textcolours.action-chat-text",""),Color.black));
        actionChatTextLabel.setText("Action Chat ( /me )");

        usernamesColourButton.setBackground(GetTextColour(CUserSettings.GetValue("textcolours.usernames",""),Color.black));
        usernamesColourButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/UI/colourpickerwheel16.png"))); // NOI18N
        usernamesColourButton.setMargin(new java.awt.Insets(2, 8, 2, 8));
        usernamesColourButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernamesColourButtonActionPerformed(evt);
            }
        });

        usernamesLabel.setForeground(GetTextColour(CUserSettings.GetValue("textcolours.usernames",""),Color.black));
        usernamesLabel.setText("Usernames");

        timestampsColourButton.setBackground(GetTextColour(CUserSettings.GetValue("textcolours.timestamps",""),Color.black));
        timestampsColourButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/UI/colourpickerwheel16.png"))); // NOI18N
        timestampsColourButton.setMargin(new java.awt.Insets(2, 8, 2, 8));
        timestampsColourButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timestampsColourButtonActionPerformed(evt);
            }
        });

        timestampsLabel.setForeground(GetTextColour(CUserSettings.GetValue("textcolours.timestamps",""),Color.black));
        timestampsLabel.setText("Timestamps");

        jButton27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/UI/colourpickerwheel16.png"))); // NOI18N
        jButton27.setEnabled(false);
        jButton27.setMargin(new java.awt.Insets(2, 8, 2, 8));

        jLabel32.setText("Channel Joins");

        jButton28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/UI/colourpickerwheel16.png"))); // NOI18N
        jButton28.setEnabled(false);
        jButton28.setMargin(new java.awt.Insets(2, 8, 2, 8));

        jLabel33.setText("Channel Exits");

        jButton29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/UI/colourpickerwheel16.png"))); // NOI18N
        jButton29.setEnabled(false);
        jButton29.setMargin(new java.awt.Insets(2, 8, 2, 8));

        jLabel34.setText("Channel Topic Messages");

        jButton30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/UI/colourpickerwheel16.png"))); // NOI18N
        jButton30.setEnabled(false);
        jButton30.setMargin(new java.awt.Insets(2, 8, 2, 8));

        jLabel35.setText("Chat Background");

        jButton31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/UI/colourpickerwheel16.png"))); // NOI18N
        jButton31.setEnabled(false);
        jButton31.setMargin(new java.awt.Insets(2, 8, 2, 8));

        jLabel36.setText("Channel Messages");

        jButton32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/UI/colourpickerwheel16.png"))); // NOI18N
        jButton32.setEnabled(false);
        jButton32.setMargin(new java.awt.Insets(2, 8, 2, 8));

        jLabel37.setText("Says:");

        jButton33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/UI/colourpickerwheel16.png"))); // NOI18N
        jButton33.setEnabled(false);
        jButton33.setMargin(new java.awt.Insets(2, 8, 2, 8));

        jLabel38.setText("Admin Chat Text");

        jButton34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/UI/colourpickerwheel16.png"))); // NOI18N
        jButton34.setEnabled(false);
        jButton34.setMargin(new java.awt.Insets(2, 8, 2, 8));

        jLabel39.setText("Your Chat Text");

        javax.swing.GroupLayout chatPanelLayout = new javax.swing.GroupLayout(chatPanel);
        chatPanel.setLayout(chatPanelLayout);
        chatPanelLayout.setHorizontalGroup(
            chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chatPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
                    .addComponent(jCheckBox2)
                    .addComponent(jCheckBox4)
                    .addGroup(chatPanelLayout.createSequentialGroup()
                        .addGroup(chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(chatPanelLayout.createSequentialGroup()
                                .addComponent(chatTextColourButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chatTextLabel))
                            .addGroup(chatPanelLayout.createSequentialGroup()
                                .addComponent(actionChatTextColourButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(actionChatTextLabel))
                            .addGroup(chatPanelLayout.createSequentialGroup()
                                .addComponent(usernamesColourButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(usernamesLabel))
                            .addGroup(chatPanelLayout.createSequentialGroup()
                                .addComponent(timestampsColourButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(timestampsLabel)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(chatPanelLayout.createSequentialGroup()
                                .addComponent(jButton30)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel35))
                            .addGroup(chatPanelLayout.createSequentialGroup()
                                .addComponent(jButton29)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel34))
                            .addGroup(chatPanelLayout.createSequentialGroup()
                                .addComponent(jButton28)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel33))
                            .addGroup(chatPanelLayout.createSequentialGroup()
                                .addComponent(jButton27)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCheckBox3)
                                    .addGroup(chatPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel32)
                                        .addGap(69, 69, 69)
                                        .addGroup(chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(chatPanelLayout.createSequentialGroup()
                                                .addComponent(jButton34)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel39))
                                            .addGroup(chatPanelLayout.createSequentialGroup()
                                                .addComponent(jButton32)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel37))
                                            .addGroup(chatPanelLayout.createSequentialGroup()
                                                .addComponent(jButton33)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel38))
                                            .addGroup(chatPanelLayout.createSequentialGroup()
                                                .addComponent(jButton31)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel36)))))))))
                .addContainerGap())
        );
        chatPanelLayout.setVerticalGroup(
            chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chatPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox4)
                    .addComponent(jCheckBox3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chatTextColourButton)
                    .addComponent(chatTextLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                    .addComponent(jButton27, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                    .addComponent(jButton34)
                    .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(actionChatTextColourButton)
                    .addComponent(actionChatTextLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                    .addComponent(jButton28)
                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                    .addComponent(jButton33)
                    .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(usernamesColourButton)
                    .addComponent(usernamesLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                    .addComponent(jButton29)
                    .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                    .addComponent(jButton31)
                    .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(timestampsColourButton)
                    .addComponent(timestampsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                    .addComponent(jButton30)
                    .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                    .addComponent(jButton32)
                    .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE))
                .addGap(77, 77, 77))
        );

        chatPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {actionChatTextColourButton, actionChatTextLabel, chatTextColourButton, chatTextLabel, jButton27, jButton28, jButton29, jButton30, jButton31, jButton32, jLabel32, jLabel33, jLabel34, jLabel35, jLabel36, jLabel37, timestampsColourButton, timestampsLabel, usernamesColourButton, usernamesLabel});

        chatPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton33, jLabel38});

        chatPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton34, jLabel39});

        settingsTabPane.addTab("Chat", new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/apps/preferences-desktop-font.png")), chatPanel); // NOI18N

        jLabel5.setText("Ladder password"); // NOI18N

        jButton10.setText("Change Password"); // NOI18N
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setText("Save Password"); // NOI18N
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jLabel26.setFont(jLabel26.getFont().deriveFont(jLabel26.getFont().getSize()+2f));
        jLabel26.setText("Ladder Properties"); // NOI18N

        jLabel27.setText("Note: Currently these only affect the development ladder not the official ladder for testing purposes"); // NOI18N

        javax.swing.GroupLayout LadderPaneLayout = new javax.swing.GroupLayout(LadderPane);
        LadderPane.setLayout(LadderPaneLayout);
        LadderPaneLayout.setHorizontalGroup(
            LadderPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LadderPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(LadderPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(LadderPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(LadderPaneLayout.createSequentialGroup()
                            .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton10))
                        .addGroup(LadderPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ladderPassBox, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel26)
                    .addComponent(jLabel27))
                .addContainerGap(124, Short.MAX_VALUE))
        );
        LadderPaneLayout.setVerticalGroup(
            LadderPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LadderPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel26)
                .addGap(20, 20, 20)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ladderPassBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(LadderPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton10)
                    .addComponent(jButton11))
                .addGap(18, 18, 18)
                .addComponent(jLabel27)
                .addContainerGap(226, Short.MAX_VALUE))
        );

        settingsTabPane.addTab("Ladders", new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/categories/applications-games.png")), LadderPane); // NOI18N

        backButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/go-previous.png"))); // NOI18N
        backButton.setText("Back to Main Menu"); // NOI18N
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        jLabel6.setFont(jLabel6.getFont().deriveFont(jLabel6.getFont().getStyle() | java.awt.Font.BOLD, jLabel6.getFont().getSize()+4));
        jLabel6.setText("Settings"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 350, Short.MAX_VALUE)
                .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(settingsTabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(backButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(settingsTabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void CLogonScriptTextComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_CLogonScriptTextComponentHidden
        //CLogonScriptText.setText ("reload to see script");
    }//GEN-LAST:event_CLogonScriptTextComponentHidden

    private void CLogonScriptTextComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_CLogonScriptTextComponentShown
    }//GEN-LAST:event_CLogonScriptTextComponentShown

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        org.darkstars.battlehub.CUserSettings.SaveStartScript(CLogonScriptText.getText());
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        final String s = org.darkstars.battlehub.CUserSettings.GetStartScript();
        CLogonScriptText.setText(s);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void autologincheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autologincheckboxActionPerformed
        CUserSettings.PutValue("autologin", String.valueOf(autologincheckbox.isSelected()));
}//GEN-LAST:event_autologincheckboxActionPerformed

    private void OnLoginPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_OnLoginPanelComponentShown
}//GEN-LAST:event_OnLoginPanelComponentShown

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        if (jRadioButton1.isSelected()) {
            CUserSettings.PutValue("UI.toptabpane.placement", "" + javax.swing.JTabbedPane.TOP);
        }
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        if (jRadioButton2.isSelected()) {
            CUserSettings.PutValue("UI.toptabpane.placement", "" + javax.swing.JTabbedPane.BOTTOM);
//            tabpane.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
//            tabpane.validate();
        }
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        if (jRadioButton3.isSelected()) {
            CUserSettings.PutValue("UI.toptabpane.placement", "" + javax.swing.JTabbedPane.LEFT);
//            tabpane.setTabPlacement(javax.swing.JTabbedPane.LEFT);
//            tabpane.validate();
        }
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        if (jRadioButton4.isSelected()) {
            CUserSettings.PutValue("UI.toptabpane.placement", String.valueOf(javax.swing.JTabbedPane.RIGHT));
//            tabpane.setTabPlacement(javax.swing.JTabbedPane.RIGHT);
//            tabpane.validate();
        }
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String h = NewHighlightEntry.getText();
        if (h == null) {
            return;
        }
        if (h.trim().equals("")) {
            return;
        }
        CVectorListModel v = (CVectorListModel) Highlights.getModel();
        v.addElement(h);
        SetHighlights();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        CVectorListModel m = (CVectorListModel) Highlights.getModel();
        m.removeElement(Highlights.getSelectedValue());

        SetHighlights();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (Highlights.getModel().getSize() > 0) {
            CVectorListModel m = (CVectorListModel) Highlights.getModel();
            m.removeAllElements();
            SetHighlights();
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        LM.SetFocus("Splash");
        LM.RemoveView(this);
        LM = null;
    }//GEN-LAST:event_backButtonActionPerformed

    private void jRadioButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton7ActionPerformed
        if (jRadioButton7.isSelected()) {
            jCheckBox1.setEnabled(false);
            CUserSettings.PutValue("looknfeel", "defjava");
            //CLooknfeelHelper.SetLooknfeel(UIManager.getCrossPlatformLookAndFeelClassName());
            //SwingUtilities.updateComponentTreeUI(LM);
            //LM.pack();
        }
    }//GEN-LAST:event_jRadioButton7ActionPerformed

    private void jRadioButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton5ActionPerformed
        if (jRadioButton5.isSelected()) {
            jCheckBox1.setEnabled(true);
            CUserSettings.PutValue("looknfeel", "org.jvnet.substance.skin.SubstanceRavenGraphiteLookAndFeel");
//            CLooknfeelHelper.SetLooknfeel("org.jvnet.substance.skin.SubstanceRavenGraphiteLookAndFeel");
//            SwingUtilities.updateComponentTreeUI(LM);
//            LM.pack();
        }
    }//GEN-LAST:event_jRadioButton5ActionPerformed

    private void jRadioButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton6ActionPerformed
        if (jRadioButton6.isSelected()) {
            jCheckBox1.setEnabled(false);
            CUserSettings.PutValue("looknfeel", UIManager.getSystemLookAndFeelClassName());
//            CLooknfeelHelper.SetLooknfeel(UIManager.getSystemLookAndFeelClassName());
//            SwingUtilities.updateComponentTreeUI(LM);
//            LM.pack();
        }
    }//GEN-LAST:event_jRadioButton6ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        CUserSettings.PutValue("angledtabs", String.valueOf(jCheckBox1.isSelected()));
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void BattlePositionCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BattlePositionCheckBoxActionPerformed
        CUserSettings.PutValue("ui.channelview.battlelisttopposition", String.valueOf(!BattlePositionCheckBox.isSelected()));
    }//GEN-LAST:event_BattlePositionCheckBoxActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        CUserSettings.PutValue("ladderpassword." + LMain.protocol.GetUsername(), ladderPassBox.getText());
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        String npass = ladderPassBox.getText();
        String pass = CUserSettings.GetValue("ladderpassword." + LMain.protocol.GetUsername(), LMain.protocol.Password());
        String user = LMain.protocol.GetUsername();

        boolean r = CMeltraxLadder.ChangePassword(user, pass, npass);
        if (!r) {
            LMain.Toasts.AddMessage("error, could nto change password");
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        String c = CUserSettings.GetValue("settings.command", "");

        if (c.equals("")) {
            return;
        } else {
            try {
                Runtime.getRuntime().exec(c);
            } catch (IOException ex) {
                Logger.getLogger(CSplashScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        settingsTabPane.setSelectedComponent(FilePathsPanel);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        settingsTabPane.setSelectedComponent(OnLoginPanel);
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        settingsTabPane.setSelectedComponent(InterfacePane);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        settingsTabPane.setSelectedComponent(HighlightPane);
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        settingsTabPane.setSelectedComponent(LadderPane);
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        CUserSettings.PutValue("ui.channelview.userjoinflags", String.valueOf(jCheckBox2.isSelected()));
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        CUserSettings.PutValue("ui.channelview.userjoinranks", String.valueOf(jCheckBox3.isSelected()));
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new CStartupWizard();
            }
        });
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed
        //CUserSettings.GetValue("ui.chat.says", "true").equals("false");
        CUserSettings.PutValue("ui.chat.says", "" + jCheckBox4.isSelected());
    }//GEN-LAST:event_jCheckBox4ActionPerformed

private void chatTextColourButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chatTextColourButtonActionPerformed
    Color c = LM.ShowColorDialog(this, "Pick a Colour");

    if (c == null) {
        return;
    }
    String sc = org.darkstars.battlehub.helpers.ColourHelper.ColourToHex(c);
    CUserSettings.PutValue("textcolours.chat-text", sc);
    CTextColours.load();

    chatTextColourButton.setBackground(c);
    chatTextLabel.setForeground(c);

    RedrawTextPreview();
}//GEN-LAST:event_chatTextColourButtonActionPerformed

private void actionChatTextColourButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionChatTextColourButtonActionPerformed
    Color c = LM.ShowColorDialog(this, "Pick a Colour");

    if (c == null) {
        return;
    }

    String sc = org.darkstars.battlehub.helpers.ColourHelper.ColourToHex(c);
    CUserSettings.PutValue("textcolours.action-chat-text", sc);
    CTextColours.load();

    actionChatTextColourButton.setBackground(c);
    actionChatTextLabel.setForeground(c);

    RedrawTextPreview();
    
}//GEN-LAST:event_actionChatTextColourButtonActionPerformed

private void usernamesColourButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernamesColourButtonActionPerformed
    Color c = LM.ShowColorDialog(this, "Pick a Colour");

    if (c == null) {
        return;
    }

    String sc = org.darkstars.battlehub.helpers.ColourHelper.ColourToHex(c);
    CUserSettings.PutValue("textcolours.usernames", sc);
    CTextColours.load();

    usernamesColourButton.setBackground(c);
    usernamesLabel.setForeground(c);

    RedrawTextPreview();
}//GEN-LAST:event_usernamesColourButtonActionPerformed

private void timestampsColourButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timestampsColourButtonActionPerformed
    Color c = LM.ShowColorDialog(this, "Pick a Colour");

    if (c == null) {
        return;
    }

    String sc = org.darkstars.battlehub.helpers.ColourHelper.ColourToHex(c);
    CUserSettings.PutValue("textcolours.timestamps", sc);
    CTextColours.load();

    timestampsColourButton.setBackground(c);
    timestampsLabel.setForeground(c);

    RedrawTextPreview();
}//GEN-LAST:event_timestampsColourButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox BattlePositionCheckBox;
    private javax.swing.JTextPane CLogonScriptText;
    private javax.swing.JPanel FilePathsPanel;
    private javax.swing.JPanel HighlightPane;
    private javax.swing.JList Highlights;
    private javax.swing.JPanel InitialPanel;
    private javax.swing.JPanel InterfacePane;
    private javax.swing.JPanel LadderPane;
    private javax.swing.JTextField NewHighlightEntry;
    private javax.swing.JPanel OnLoginPanel;
    private javax.swing.JButton actionChatTextColourButton;
    private javax.swing.JLabel actionChatTextLabel;
    private javax.swing.JCheckBox autologincheckbox;
    private javax.swing.JButton backButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JPanel chatPanel;
    private javax.swing.JButton chatTextColourButton;
    private javax.swing.JLabel chatTextLabel;
    private javax.swing.JPanel internalFilePanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTextField ladderPassBox;
    private javax.swing.JTabbedPane settingsTabPane;
    private javax.swing.JButton timestampsColourButton;
    private javax.swing.JLabel timestampsLabel;
    private javax.swing.JButton usernamesColourButton;
    private javax.swing.JLabel usernamesLabel;
    // End of variables declaration//GEN-END:variables
}