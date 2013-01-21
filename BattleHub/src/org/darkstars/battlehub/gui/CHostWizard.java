/*
 * CHostWizard.java
 *
 * Created on 13 April 2008, 14:50
 */
package org.darkstars.battlehub.gui;

import org.darkstars.battlehub.CUserSettings;
import aflobby.CUnitSyncJNIBindings;
import org.darkstars.battlehub.CContentManager;
import org.darkstars.battlehub.CLadderProperties;
import org.darkstars.battlehub.integration.CMeltraxLadder;
import org.darkstars.battlehub.CSync;
import org.darkstars.battlehub.LMain;
import org.darkstars.battlehub.framework.CEvent;
import org.darkstars.battlehub.helpers.AlphanumComparator;
import org.darkstars.battlehub.framework.BrowserLauncher;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.darkstars.battlehub.framework.CEventTypes;

/**
 *
 * @author  tarendai-std
 */
public class CHostWizard extends CView {

    List<JPanel> pages = new ArrayList<JPanel>();
    String game = "";
    int currentIndex = 0;

    /** Creates new form CHostWizard
     * @param LM 
     */
    public CHostWizard( LMain LM ) {
        super(LM);
        currentIndex = 0;
        initComponents();
        SwingWorker worker = new SwingWorker<Void, Void>() {

            @Override
            public Void doInBackground() {
                CMeltraxLadder.Initialize();
                return null;
            }

            @Override
            public void done() {
                containerPanel.SetGUIObject(InitialPanel);
                if ( CMeltraxLadder.GetLadderCount() > 0 ) {
                    for ( String ln : CMeltraxLadder.ladders.values() ) {
                        if ( ln != null ) {
                            laddercomboBox.addItem(ln);
                        }
                    }
                }
            }

        };
        worker.execute();
        RefreshContent();
    }

    @Override
    public void NewGUIEvent( final CEvent e ) {
        if ( e.IsEvent(CEventTypes.EXITEDBATTLE) ) {
            if ( !CUnitSyncJNIBindings.IsLoaded() ) {
                return;
            }
            if ( currentIndex != 0 ) {
                Runnable doWorkRunnable = new Runnable() {

                    @Override
                    public void run() {
                        hostButton.setEnabled(true);
                    }

                };
                SwingUtilities.invokeLater(doWorkRunnable);
            }
        } else if ( e.IsEvent(CEventTypes.CONTENTREFRESH) ) {
            //
            RefreshContent();
        }
    }

    @Override
    public void NewEvent( final CEvent e ) {
        if ( CUnitSyncJNIBindings.IsLoaded() ) {
            if ( e.IsEvent("OPENBATTLE") || e.IsEvent("JOINBATTLE") ) {
                SwingUtilities.invokeLater( new Runnable() {

                    @Override
                    public void run() {
                        hostButton.setEnabled(false);
                    }

                });
            }
        }
    }

    public void RefreshContent() {
        Runnable doWorkRunnable = new Runnable() {

            @Override
            public void run() {
                HostMod.removeAllItems();
                String lname = (String) laddercomboBox.getSelectedItem();
                if ( lname == null ) {
                    lname = "None";
                }
                if ( lname.equals("None") == false ) {
                    CLadderProperties p = CMeltraxLadder.GetLadderData(CMeltraxLadder.GetLadderID(lname));
                    if ( p != null ) {
                        lname = p.mod;
                    }
                }
                lname = lname.toLowerCase();
                ArrayList<String> mods = new ArrayList<String>();
                for ( int i = 0; i < CSync.modcount; i++ ) {
                    String s = CSync.mod_names.get(i);
                    if ( s == null ) {
                        continue;
                    }
                    if ( s.equals("") ) {
                        continue;
                    }
                    if ( lname.equals("none") == false ) {
                        if ( !s.toLowerCase().contains(lname) ) {
                            continue;
                        }
                    }
                    mods.add(CSync.mod_names.get(i));
                }
                if ( !mods.isEmpty() ) {
                    Object[] o = mods.toArray().clone();
                    Arrays.sort(o, new AlphanumComparator());
                    for ( Object s : o ) {
                        if ( s == null ) {
                            continue;
                        }
                        if ( ((String) s).equals("") ) {
                            continue;
                        }
                        HostMod.addItem(s);
                    }
                    HostMod.setSelectedItem(CUserSettings.GetValue("UI.lasthosted.mod", "XTA v9"));
                } else {
                    HostMod.setSelectedItem(CUserSettings.GetValue("UI.lasthosted.mod", "XTA v9"));
                }

            }

        };
        SwingUtilities.invokeLater(doWorkRunnable);

    }

    public void NextPage() {
        //
        int nextIndex = currentIndex + 1;
        assert (nextIndex < pages.size());

        ProcessButtonEnables(nextIndex);

        JPanel nextPanel = pages.get(nextIndex);
        containerPanel.SetGUIObject(nextPanel);
        currentIndex++;
    }

    public void PreviousPage() {
        //
        int previousIndex = currentIndex - 1;
        assert (previousIndex >= 0);

        ProcessButtonEnables(previousIndex);


        JPanel nextPanel = pages.get(previousIndex);
        containerPanel.SetGUIObject(nextPanel);
        currentIndex--;
    }

    public void ProcessButtonEnables( int newIndex ) {
        //
        boolean back = true;
        boolean next = true;
        boolean host = true;
        if ( newIndex == 0 ) {
            //
            back = next = host = false;
        } else {
            back = host = true;
            next = false;

            if ( newIndex < pages.size() - 1 ) {
                //
                next = true;

            } else {
                next = false;
            }
        }
        final boolean b,  n,  h;
        b = back;
        n = next;
        h = host;
        Runnable doWorkRunnable4 = new Runnable() {

            @Override
            public void run() {
                backButton.setEnabled(b);
                nextButton.setEnabled(n);
                hostButton.setEnabled(h);
            }

        };
        SwingUtilities.invokeLater(doWorkRunnable4);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        InitialPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        springHostWizardButton = new javax.swing.JButton();
        glestHostWizardButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        glestHostWizardButton1 = new javax.swing.JButton();
        springHostWizardButton1 = new javax.swing.JButton();
        glestHostWizardButton2 = new javax.swing.JButton();
        SpringPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        laddercomboBox = new javax.swing.JComboBox();
        HostMod = new javax.swing.JComboBox();
        jButton4 = new javax.swing.JButton();
        hostReloadmodlist = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        HostUDPPort = new javax.swing.JTextField();
        HostNATCombo = new javax.swing.JComboBox();
        BasicPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        HostBattleName = new javax.swing.JTextField();
        HostMaxPlayers = new javax.swing.JSpinner();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        HostPassword = new javax.swing.JTextField();
        HostRankLimitCombo = new javax.swing.JComboBox();
        hostButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        nextButton = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        containerPanel = new org.darkstars.battlehub.gui.CContainerPanel();

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel4.setFont(jLabel4.getFont().deriveFont(jLabel4.getFont().getSize()+6f));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/darkstars/battlehub/languages"); // NOI18N
        jLabel4.setText(bundle.getString("CHostWizard.jLabel4.text_1")); // NOI18N

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/document-save.png"))); // NOI18N
        jButton6.setText(bundle.getString("CHostWizard.jButton6.text")); // NOI18N
        jButton6.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/document-save.png"))); // NOI18N
        jButton7.setText(bundle.getString("CHostWizard.jButton7.text")); // NOI18N
        jButton7.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/document-save.png"))); // NOI18N
        jButton1.setText("Get TA3D");
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/document-save.png"))); // NOI18N
        jButton2.setText("Get WarZone 2100");
        jButton2.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/apps/help-browser.png"))); // NOI18N
        jButton3.setText("Total Annihilation?");
        jButton3.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jButton7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE))
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        springHostWizardButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/go-next.png"))); // NOI18N
        springHostWizardButton.setText("Host a Game Using The Spring Engine");
        springHostWizardButton.setEnabled(CContentManager.SupportsEngine("spring"));
        springHostWizardButton.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        springHostWizardButton.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        springHostWizardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                springHostWizardButtonActionPerformed(evt);
            }
        });

        glestHostWizardButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/go-next.png"))); // NOI18N
        glestHostWizardButton.setText("Host a Glest Game");
        glestHostWizardButton.setEnabled(CContentManager.SupportsEngine("glest"));
        glestHostWizardButton.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        glestHostWizardButton.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        glestHostWizardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                glestHostWizardButtonActionPerformed(evt);
            }
        });

        jLabel3.setFont(jLabel3.getFont().deriveFont(jLabel3.getFont().getSize()+6f));
        jLabel3.setText("Which would you like to host?");

        glestHostWizardButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/go-next.png"))); // NOI18N
        glestHostWizardButton1.setText("Host a Warzone 2100 Game");
        glestHostWizardButton1.setEnabled(CContentManager.SupportsEngine("wz2100"));
        glestHostWizardButton1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        glestHostWizardButton1.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        glestHostWizardButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                glestHostWizardButton1ActionPerformed(evt);
            }
        });

        springHostWizardButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/go-next.png"))); // NOI18N
        springHostWizardButton1.setText("Host a TA3D Game");
        springHostWizardButton1.setEnabled(CContentManager.SupportsEngine("ta3d"));
        springHostWizardButton1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        springHostWizardButton1.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        springHostWizardButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                springHostWizardButton1ActionPerformed(evt);
            }
        });

        glestHostWizardButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/go-next.png"))); // NOI18N
        glestHostWizardButton2.setText("Host a Total Annihilation Game");
        glestHostWizardButton2.setEnabled(CContentManager.SupportsEngine("ta"));
        glestHostWizardButton2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        glestHostWizardButton2.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        glestHostWizardButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                glestHostWizardButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout InitialPanelLayout = new javax.swing.GroupLayout(InitialPanel);
        InitialPanel.setLayout(InitialPanelLayout);
        InitialPanelLayout.setHorizontalGroup(
            InitialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InitialPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(InitialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(InitialPanelLayout.createSequentialGroup()
                        .addGroup(InitialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(InitialPanelLayout.createSequentialGroup()
                                .addGroup(InitialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(InitialPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE))
                                    .addGroup(InitialPanelLayout.createSequentialGroup()
                                        .addComponent(glestHostWizardButton, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(InitialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(glestHostWizardButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(glestHostWizardButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 133, Short.MAX_VALUE)))
                        .addGap(320, 320, 320))
                    .addGroup(InitialPanelLayout.createSequentialGroup()
                        .addComponent(springHostWizardButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(springHostWizardButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        InitialPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {glestHostWizardButton, glestHostWizardButton1, glestHostWizardButton2, springHostWizardButton, springHostWizardButton1});

        InitialPanelLayout.setVerticalGroup(
            InitialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InitialPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(InitialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(InitialPanelLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(glestHostWizardButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(springHostWizardButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(springHostWizardButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(glestHostWizardButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(glestHostWizardButton2)))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jLabel7.setText(bundle.getString("CHostWizard.jLabel7.text")); // NOI18N

        jLabel12.setText(bundle.getString("CHostWizard.jLabel12.text")); // NOI18N

        laddercomboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None" }));
        laddercomboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                laddercomboBoxActionPerformed(evt);
            }
        });

        HostMod.setMaximumRowCount(16);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/view-refresh.png"))); // NOI18N
        jButton4.setText(bundle.getString("CHostWizard.jButton1.text")); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        hostReloadmodlist.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/view-refresh.png"))); // NOI18N
        hostReloadmodlist.setText(bundle.getString("CHostWizard.hostReloadmodlist.text")); // NOI18N
        hostReloadmodlist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hostReloadmodlistActionPerformed(evt);
            }
        });

        jButton5.setText(bundle.getString("CHostWizard.jButton2.text_1")); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel2.setFont(jLabel2.getFont().deriveFont(jLabel2.getFont().getSize()+6f));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/UI/springIcon.png"))); // NOI18N
        jLabel2.setText(bundle.getString("CHostWizard.jLabel2.text_1")); // NOI18N

        jLabel10.setText(bundle.getString("CHostWizard.jLabel10.text")); // NOI18N

        jLabel9.setText(bundle.getString("CHostWizard.jLabel9.text")); // NOI18N

        HostUDPPort.setText("8200");
        HostUDPPort.setEnabled(false);

        HostNATCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "none", "hole punching", "fixed ports" }));
        HostNATCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HostNATComboActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout SpringPanelLayout = new javax.swing.GroupLayout(SpringPanel);
        SpringPanel.setLayout(SpringPanelLayout);
        SpringPanelLayout.setHorizontalGroup(
            SpringPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SpringPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SpringPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(SpringPanelLayout.createSequentialGroup()
                        .addGroup(SpringPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(SpringPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(laddercomboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(HostMod, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(SpringPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(hostReloadmodlist))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5)
                        .addGap(26, 26, 26))
                    .addGroup(SpringPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addContainerGap(572, Short.MAX_VALUE))
                    .addGroup(SpringPanelLayout.createSequentialGroup()
                        .addGroup(SpringPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(SpringPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(HostNATCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(HostUDPPort, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(554, Short.MAX_VALUE))))
        );

        SpringPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel10, jLabel12, jLabel7, jLabel9});

        SpringPanelLayout.setVerticalGroup(
            SpringPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SpringPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SpringPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(HostMod, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hostReloadmodlist)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SpringPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(laddercomboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SpringPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(HostUDPPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SpringPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(HostNATCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        SpringPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton4, jButton5});

        jLabel6.setText(bundle.getString("CHostWizard.jLabel6.text")); // NOI18N

        jLabel5.setText(bundle.getString("CHostWizard.jLabel5.text")); // NOI18N

        HostBattleName.setText(CUserSettings.GetValue("org.darkstars.battlehub.UI.lasthosted.name",""));

        HostMaxPlayers.setModel(new SpinnerNumberModel (4,1,16,1));
        HostMaxPlayers.setValue(2);

        jLabel8.setText(bundle.getString("CHostWizard.jLabel8.text")); // NOI18N

        jLabel11.setText(bundle.getString("CHostWizard.jLabel11.text")); // NOI18N

        HostPassword.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 0), 2, true));

        HostRankLimitCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No Limit", "Beginner", "Average", "Experienced", "Highly Experienced" }));

        javax.swing.GroupLayout BasicPanelLayout = new javax.swing.GroupLayout(BasicPanel);
        BasicPanel.setLayout(BasicPanelLayout);
        BasicPanelLayout.setHorizontalGroup(
            BasicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BasicPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BasicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, BasicPanelLayout.createSequentialGroup()
                        .addGroup(BasicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(BasicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(HostBattleName, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                            .addComponent(HostMaxPlayers, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, BasicPanelLayout.createSequentialGroup()
                        .addGroup(BasicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(BasicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(HostPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(HostRankLimitCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        BasicPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel11, jLabel5, jLabel6, jLabel8});

        BasicPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {HostBattleName, HostPassword, HostRankLimitCombo});

        BasicPanelLayout.setVerticalGroup(
            BasicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BasicPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BasicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(HostBattleName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(BasicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(HostMaxPlayers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(BasicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(HostPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(BasicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(HostRankLimitCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addContainerGap(82, Short.MAX_VALUE))
        );

        hostButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/go-last.png"))); // NOI18N
        hostButton.setText("Host Game");
        hostButton.setEnabled(false);
        hostButton.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        hostButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hostButtonActionPerformed(evt);
            }
        });

        backButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/go-previous.png"))); // NOI18N
        backButton.setText("Back");
        backButton.setEnabled(false);
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        nextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tango/16x16/actions/go-next.png"))); // NOI18N
        nextButton.setText("Next");
        nextButton.setEnabled(false);
        nextButton.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getSize()+13f));
        jLabel1.setText(bundle.getString("CHostWizard.jLabel1.text")); // NOI18N

        javax.swing.GroupLayout containerPanelLayout = new javax.swing.GroupLayout(containerPanel);
        containerPanel.setLayout(containerPanelLayout);
        containerPanelLayout.setHorizontalGroup(
            containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 560, Short.MAX_VALUE)
        );
        containerPanelLayout.setVerticalGroup(
            containerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 125, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(backButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nextButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hostButton)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                        .addGap(413, 413, 413))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(containerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(containerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hostButton)
                    .addComponent(backButton)
                    .addComponent(nextButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        BrowserLauncher.openURL("http://www.glest.org/en/downloads.html");
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        BrowserLauncher.openURL("http://spring.clan-sy.com/download.php");
    }//GEN-LAST:event_jButton7ActionPerformed

    private void HostNATComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HostNATComboActionPerformed
        HostUDPPort.setEnabled(HostNATCombo.getSelectedIndex() == 2);
    }//GEN-LAST:event_HostNATComboActionPerformed

    private void laddercomboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_laddercomboBoxActionPerformed
        String s = (String) laddercomboBox.getSelectedItem();
        if ( s.equals("None") == false ) {
            HostPassword.setText("ladderlock");
            HostPassword.setEnabled(false);
            HostRankLimitCombo.setEnabled(false);
            HostBattleName.setEnabled(false);
            HostBattleName.setText("(ladder " + CMeltraxLadder.GetLadderID(s) + ") " + s);
        } else {
            HostPassword.setEnabled(true);
            HostRankLimitCombo.setEnabled(true);
            HostBattleName.setEnabled(true);
            String t = HostBattleName.getText();
            if ( t.startsWith("(ladder") ) {
                HostBattleName.setText("");
            }
        }
        RefreshContent();
    }//GEN-LAST:event_laddercomboBoxActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        CMeltraxLadder.Initialize();
        laddercomboBox.removeAllItems();
        laddercomboBox.addItem("None");
        if ( CMeltraxLadder.GetLadderCount() > 0 ) {
            laddercomboBox.setEnabled(true);
            for ( String ln : CMeltraxLadder.ladders.values() ) {
                if ( ln != null ) {
                    laddercomboBox.addItem(ln);
                }
            }
        } else {
            laddercomboBox.setEnabled(false);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void hostReloadmodlistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hostReloadmodlistActionPerformed
        CSync.RefreshUnitSync();
    }//GEN-LAST:event_hostReloadmodlistActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        BrowserLauncher.openURL("http://www.spring-league.com/ladder/index.php?ULadder/URules");
    }//GEN-LAST:event_jButton5ActionPerformed

    private void springHostWizardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_springHostWizardButtonActionPerformed
        game = "spring";

        pages.clear();

        pages.add(InitialPanel);
        pages.add(BasicPanel);
        pages.add(SpringPanel);

        NextPage();
}//GEN-LAST:event_springHostWizardButtonActionPerformed

    private void glestHostWizardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_glestHostWizardButtonActionPerformed
        game = "glest";        //

        pages.clear();

        pages.add(InitialPanel);
        pages.add(BasicPanel);

        NextPage();
}//GEN-LAST:event_glestHostWizardButtonActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        PreviousPage();
    }//GEN-LAST:event_backButtonActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        NextPage();
    }//GEN-LAST:event_nextButtonActionPerformed

    private void hostButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hostButtonActionPerformed
        //OPENBATTLE type natType password port maxplayers hashcode rank maphash {map} {title} {modname}
        String s = "OPENBATTLE ";
        s += "0 "; //type
        s += this.HostNATCombo.getSelectedIndex() + " "; // natType
        if ( HostPassword.getText().equals("") == false ) {
            s += this.HostPassword.getText() + " ";
        } else {
            s += "* ";
        } // password
        s += HostUDPPort.getText() + " "; // port
        s += HostMaxPlayers.getValue() + " "; // maxplayers
        if ( game.equalsIgnoreCase("spring") ) {


            String selected_mod = (String) HostMod.getSelectedItem();
            s += CSync.GetModHashbyName(selected_mod) + " "; // hashcode
            CUserSettings.PutValue("UI.lasthosted.mod", selected_mod);

            s += this.HostRankLimitCombo.getSelectedIndex() + " "; // rank

            String m = CUserSettings.GetValue("UI.lasthosted.map", CSync.map_names.get(0));
            s += CSync.GetMapHash(m) + " "; // maphash
            s += m;
            //        }else{
            //            s += "0 n/a ";
            //        }

            s += "\t";

            //        if (engine.equals("glest")){
            //            s += "glest: ";
            //        }
            s += HostBattleName.getText();

            //        if(engine.equals("spring")){
            s += "\t" + HostMod.getSelectedItem();
            //        }else if (engine.equals("glest")){
            //            s += "\tGlest";
            //        }
            LMain.protocol.SendTraffic(s);
        } else if ( game.equalsIgnoreCase("glest") ) {
            s += "0 ";
            s += this.HostRankLimitCombo.getSelectedIndex() + " "; // rank

            s += "0 n/a ";

            s += "\t";

            s += "glest: ";

            s += HostBattleName.getText();

            s += "\tglest";

            LMain.protocol.SendTraffic(s);

        } else if ( game.equalsIgnoreCase("wz2100") ) {
            s += "0 ";
            s += this.HostRankLimitCombo.getSelectedIndex() + " "; // rank

            s += "0 n/a ";

            s += "\t";

            s += "wz2100: ";

            s += HostBattleName.getText();

            s += "\twz2100";

            LMain.protocol.SendTraffic(s);

        }
        CUserSettings.PutValue("UI.lasthosted.maxplayers", "" + HostMaxPlayers.getValue());
        CUserSettings.PutValue("UI.lasthosted.name", HostBattleName.getText());
}//GEN-LAST:event_hostButtonActionPerformed

private void glestHostWizardButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_glestHostWizardButton1ActionPerformed
        game = "wz2100";        //
        
        pages.clear();
        
        pages.add(InitialPanel);
        pages.add(BasicPanel);
        
        NextPage();
}//GEN-LAST:event_glestHostWizardButton1ActionPerformed

private void springHostWizardButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_springHostWizardButton1ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_springHostWizardButton1ActionPerformed

private void glestHostWizardButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_glestHostWizardButton2ActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_glestHostWizardButton2ActionPerformed

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    BrowserLauncher.openURL("http://ta3d.darkstars.co.uk");
}//GEN-LAST:event_jButton1ActionPerformed

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    BrowserLauncher.openURL("http://www.wz2100.net");
}//GEN-LAST:event_jButton2ActionPerformed

private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jButton3ActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BasicPanel;
    private javax.swing.JTextField HostBattleName;
    private javax.swing.JSpinner HostMaxPlayers;
    private javax.swing.JComboBox HostMod;
    private javax.swing.JComboBox HostNATCombo;
    private javax.swing.JTextField HostPassword;
    private javax.swing.JComboBox HostRankLimitCombo;
    private javax.swing.JTextField HostUDPPort;
    private javax.swing.JPanel InitialPanel;
    private javax.swing.JPanel SpringPanel;
    private javax.swing.JButton backButton;
    private org.darkstars.battlehub.gui.CContainerPanel containerPanel;
    private javax.swing.JButton glestHostWizardButton;
    private javax.swing.JButton glestHostWizardButton1;
    private javax.swing.JButton glestHostWizardButton2;
    private javax.swing.JButton hostButton;
    private javax.swing.JButton hostReloadmodlist;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JComboBox laddercomboBox;
    private javax.swing.JButton nextButton;
    private javax.swing.JButton springHostWizardButton;
    private javax.swing.JButton springHostWizardButton1;
    // End of variables declaration//GEN-END:variables
    
}
