/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.darkstars.battlehub.protocol.tasserver;

import org.darkstars.battlehub.gui.CChannel;
import org.darkstars.battlehub.CChannelView;
import org.darkstars.battlehub.LMain;
import org.darkstars.battlehub.framework.CBasicTASChannelModel;
import org.darkstars.battlehub.framework.ICentralClass;

/**
 *
 * @author AF-Standard
 */
public class CTASChannelModel extends CBasicTASChannelModel  {
    
    public CChannel ui = null;
    
    public CChannelView c= null;
    
    public CTASChannelModel(){
        //
        super();
    }
    
    @Override
    public void Init(final ICentralClass LM){
        //
        super.Init(LM);
        
        /*Runnable doWorkRunnable = new Runnable() {

            @Override
            public void run() {*/
        ui = new CChannel(this);
        ui.Init((LMain) central,channelName);
        central.GetCore().AddModule(ui);
 
            /*}
        };
        SwingUtilities.invokeLater(doWorkRunnable);
        */
    }
    
    /**
     * Evil method that shouldnt really be needed, but well keep this until we
     * figure out some sort of refactor
     * 
     * @param c
     */
    public void DoChannelViewStuff(CChannelView c){
        //
        synchronized (c.channels) {
            c.channels.put(channelName, ui);
        }
        this.c = c;
    }
    
    @Override
    public void OnRemove(){
        //
        c = null;
        super.OnRemove();
    }
    
    @Override
    public void Exit() {
        c.RemoveChannel(channelName,ui);
        central.GetCore().RemoveModule(ui);
        
        super.Exit();
    }
    
    @Override
    public void Exit(String reason) {
        c.RemoveChannel(channelName,ui);
        central.GetCore().RemoveModule(ui);
        
        super.Exit(reason);
    }

}
