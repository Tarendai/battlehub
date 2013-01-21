/*
 * CChatPanel.java
 *
 * Created on 02 July 2007, 04:30
 */
package org.darkstars.battlehub.gui;

import org.darkstars.battlehub.gui.CSmileyManager;
import org.darkstars.battlehub.CUserSettings;
import java.awt.Color;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.html.HTMLDocument;
import org.darkstars.battlehub.LMain;
import org.darkstars.battlehub.framework.Misc;
import org.darkstars.battlehub.framework.BrowserLauncher;

/**
 *
 * @author  Tom
 */
public class CChatPanel extends javax.swing.JPanel {

    private boolean autoscroll = true;
    private String contents = "";
    /**
     * An instance of the private subclass of the default highlight painter
     */
    public static Highlighter.HighlightPainter myHighlightPainter = null;

    /**
     * Creates new form CChatPanel
     */
    public CChatPanel() {
        Runnable doWorkRunnable = new Runnable() {

            @Override
            public void run() {
                if (myHighlightPainter == null) {
                    myHighlightPainter = new DefaultHighlighter.DefaultHighlightPainter(new Color(190, 200, 255));
                }
                initComponents();
            }
        };
        SwingUtilities.invokeLater(doWorkRunnable);

    }
    
    public void AddMessage(String s) {
        AddMessage(s,true);
    }

    /**
     * 
     * @param s
     */
    public void AddMessage(String s, boolean urls) {

        String q = ParseChatText(s,urls);
        //q = Misc.toHTML(q);

        //q = q.replaceAll ("((:flag=)[a-zA-Z](:))","<img src=\""+Misc.GetAbsoluteLobbyFolderPath ()+"images/flags/$0.png\"></img>");

        AddRawHTML(q + "<br>");
    }

    /**
     * 
     * @param u
     */
    public void AddIcon(URL u) {
        chatpane.insertIcon(new ImageIcon(u));
    }

    /**
     * 
     * @param i
     */
    public void AddIcon(Icon i) {
        chatpane.insertIcon(i);
    }

    /**
     * 
     * @param s
     */
    public void AddRawHTML(final String s) {
        Runnable doWorkRunnable = new Runnable() {

            @Override
            public void run() {
                HTMLDocument doc = (HTMLDocument) chatpane.getDocument();
                //Document blankdoc = new HTMLDocument();
                //chatpane.setDocument(blankdoc);
                int start_highlighting = doc.getLength();
                //System.out.println ("CChannel::AddMessageB");

                try {
                    doc.setOuterHTML(doc.getCharacterElement(doc.getLength()), s);
                    contents += s;
                    doc.setAsynchronousLoadPriority(500000);
                    chatpane.setDocument(doc);
                    if (GetAutoScroll()) {
                        //scrollbotom=true;
                        chatpane.setCaretPosition(doc.getLength());
                    }
                    Highlight(start_highlighting);
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        };
        SwingUtilities.invokeLater(doWorkRunnable);
    }

    public void Highlight(int start_at) {
        try {
            Highlighter hilite = chatpane.getHighlighter();
            HTMLDocument doc = (HTMLDocument) chatpane.getDocument();
            String text = doc.getText(0, doc.getLength());
            int pos = 0;

            //String u = LM.protocol.GetUsername();
            String h = CUserSettings.GetValue("ui.texthighlights", "");
            String[] entries = h.split(",");
            if (entries.length > 0) {
                for (String pattern : entries) {
                    // Search for pattern
                    if (pattern.trim().equals("")) {
                        continue;
                    }
                    while ((pos = text.indexOf(pattern, pos)) >= 0) {
                        // Create highlighter using private painter and apply around pattern
                        hilite.addHighlight(pos, pos + pattern.length(), myHighlightPainter);
                        pos += pattern.length();
                    }
                }
            }
        } catch (Exception e) {//(BadLocationException e) {

            e.printStackTrace();
        }
    }

    public String GetContents() {
        return contents;
    }

    public void ClearContents() {
        contents = "";
        Runnable doWorkRunnable = new Runnable() {

            @Override
            public void run() {
                chatpane.setText("");
            }
        };
        SwingUtilities.invokeLater(doWorkRunnable);

    }

    public void SetContents(final String s) {
        ClearContents();
        contents = s;
        AddRawHTML(contents);
    }

    public boolean GetAutoScroll() {
        //
        return autoscroll;
    }

    public void SetAutoScroll(boolean b) {
        autoscroll = b;
    /*if(autoscroll==false){
    scroll=false;
    }*/
    }

    public void ScrollToTop() {
        Runnable doWorkRunnable = new Runnable() {

            @Override
            public void run() {
                chatpane.setCaretPosition(0);
            }
        };
        SwingUtilities.invokeLater(doWorkRunnable);
    }

    public static String ParseChatText(String msg){
        return ParseChatText(msg,true);
    }
    
    public static String ParseChatText(String msg, boolean urls) {
        if (CSmileyManager.loaded == false) {
            CSmileyManager.Init();
        }
        String q = msg;
        if(urls){
            q = Misc.chat_string_create_urls(msg);
        }
        //
        for (int i = 0; i < CSmileyManager.smileys.length; i++) {//Misc.GetLobbyFolderPath ()+

            try {
                //Misc.GetLobbyFolderPath ()+
                String us = "file:/" + LMain.GetAbsoluteLobbyFolderPathStatic() + "images/smileys/" + CSmileyManager.smileys[i] + ".gif";
                URL u = new URL(us);
                q = q.replaceAll(":" + CSmileyManager.smileys[i] + ":", "<img src=\"" + u.toExternalForm() + "\"></img>");
            } catch (MalformedURLException ex) {
                Logger.getLogger(CChatPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        q = q.replaceAll("  ", "&#160;&#160;");
        return q;
    }

    public void SetForeColour(final Color c) {
        SwingUtilities.invokeLater( new Runnable() {

            @Override
            public void run() {
                chatpane.setForeground(c);
            }
        });
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        CopyMenuItem = new javax.swing.JMenuItem();
        jScrollPane2 = new javax.swing.JScrollPane();
        chatpane = new javax.swing.JTextPane();

        CopyMenuItem.setText("Item");
        CopyMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CopyMenuItemActionPerformed(evt);
            }
        });
        jPopupMenu1.add(CopyMenuItem);

        chatpane.setContentType("text/html");
        chatpane.setEditable(false);
        chatpane.setFont(chatpane.getFont());
        chatpane.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {
            public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {
                chatpaneHyperlinkUpdate(evt);
            }
        });
        jScrollPane2.setViewportView(chatpane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void CopyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CopyMenuItemActionPerformed
        chatpane.copy();
    }//GEN-LAST:event_CopyMenuItemActionPerformed

private void chatpaneHyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {//GEN-FIRST:event_chatpaneHyperlinkUpdate
    if(evt.getEventType() == EventType.ACTIVATED){
        //
        URL u = evt.getURL();
        BrowserLauncher.openURL(u.toExternalForm());
    }
}//GEN-LAST:event_chatpaneHyperlinkUpdate

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem CopyMenuItem;
    private javax.swing.JTextPane chatpane;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
