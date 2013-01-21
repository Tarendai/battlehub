/*
 * CBaseModule.java
 * 
 * Created on 22-Aug-2007, 22:27:55
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.darkstars.battlehub;

import org.darkstars.battlehub.framework.CEvent;
import org.darkstars.battlehub.framework.IModule;
import org.darkstars.battlehub.framework.ICentralClass;


/**
 *
 * @author Tom
 */
public class CBaseModule implements IModule{
    public LMain LM;
    
    @Override
    public void Init(ICentralClass L) {
        LM = (LMain) L;
    }

    @Override
    public void Update() {
        
    }

    @Override
    public void NewEvent(final CEvent e) {
        
    }

    @Override
    public void NewGUIEvent(final CEvent e) {
        
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
