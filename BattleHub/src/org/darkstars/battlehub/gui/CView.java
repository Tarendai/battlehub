/*
 * CView.java
 *
 * Created on 27 May 2006, 21:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.darkstars.battlehub.gui;

import org.darkstars.battlehub.framework.CEvent;
import org.darkstars.battlehub.LMain;
import org.darkstars.battlehub.framework.IModule;
import javax.swing.SwingUtilities;
import org.darkstars.battlehub.framework.ICentralClass;

/**
 *
 * @author AF
 */
public class CView extends javax.swing.JPanel implements IModule{
    //public int index;
    private String Title;
    public String UserTitle;
    public LMain LM;
    //public String Tooltip = new String();
    /**
     * Creates a new instance of CView
     */
    public CView(){
        //
    }
    
    public CView(LMain L) {
        LM = L;
        Runnable doWorkRunnable = new Runnable () {
            public void run () { Initialize (); }
        };
        SwingUtilities.invokeLater (doWorkRunnable);
        //ValidatePanel();
    }
    
    public void Update(){
        //
    }
    
    public void NewEvent(CEvent e){
        // do stuff
    }
    
    public void NewGUIEvent(CEvent e){
        // do GUI stuff
    }
    
    public void Initialize(){
        
    }
    
    public void ValidatePanel(){
    }

    public String getTitle() {
        if(Title == null){
            int a = 4;
        }
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }
    
    @Override
    public void OnRemove(){
        //
    }

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
    public void OnEvent(CEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void OnRemove(int channel) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
