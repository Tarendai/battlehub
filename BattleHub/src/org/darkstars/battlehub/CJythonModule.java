/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.darkstars.battlehub;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.darkstars.battlehub.framework.CEvent;
import org.darkstars.battlehub.framework.ICentralClass;
import org.darkstars.battlehub.framework.IModule;
import org.darkstars.battlehub.helpers.CFSHelper;

/**
 *
 * @author AF-Standard
 */
public class CJythonModule implements IModule{

    IModule i = null;
    public CJythonModule(String scriptFile){
        //
        String script = CFSHelper.GetFileContents(scriptFile);
        ScriptEngineManager s = new ScriptEngineManager();
        ScriptEngine engine = s.getEngineByExtension("py");
        //
        if ( engine != null ) {
            try {
                engine.eval(script);
                Invocable invocableEngine = (Invocable) engine;
                i = invocableEngine.getInterface(IModule.class);
            } catch ( ScriptException ex ) {
                Logger.getLogger(CJythonModule.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public void Init(ICentralClass L) {
        i.Init(L);
    }

    @Override
    public void Update() {
        i.Update();
    }

    @Override
    public void NewEvent(CEvent e) {
        i.NewEvent(e);
    }

    @Override
    public void NewGUIEvent(CEvent e) {
        i.NewGUIEvent(e);
    }

    @Override
    public void OnRemove() {
        i.OnRemove();
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
