/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.darkstars.battlehub.scripting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.darkstars.battlehub.framework.CErrorWindow;
import org.darkstars.battlehub.framework.ICentralClass;
import org.darkstars.battlehub.framework.IModule;

/**
 *
 * @author AF-Standard
 */
public class CScriptManager {

    public static void LoadExtensions(ICentralClass c) {
        //
        String extensionPath = c.GetAbsoluteLobbyFolderPath() + "extensions/";
        File dir = new File(extensionPath);
        File[] dirContents = dir.listFiles();
        if (dirContents != null) {
            //
            if (dirContents.length > 0) {
                for (int i = 0; i < dirContents.length; i++) {
                    //
                    File f = dirContents[i];
                    if (f.isDirectory()) {
                        continue;
                    } else {
                        String filename = f.getName();
                        if (filename.endsWith(".py")) {
                            //
                            //
                            IModule module = GetModuleFromScript(f);
                            if (module != null) {
                                //
                                module.Init(c);
                                c.GetCore().AddModule(module);
                            }
                        }
                    }
                }
            }
        }
    }

    public static IModule GetModuleFromScript(File scriptFile) {

        ScriptEngineManager s = new ScriptEngineManager();
        ScriptEngine engine = s.getEngineByExtension("py");
        //
        if (engine != null) {
            Reader r = null;
            try {
                r = new FileReader(scriptFile);
                engine.eval(r);
                Invocable invocableEngine = (Invocable) engine;
                // i = invocableEngine.getInterface(IModule.class);
                Object o = invocableEngine.invokeFunction("getObject");
                IModule i = invocableEngine.getInterface(o, IModule.class);
                return i;
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(CScriptManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                CErrorWindow ew = new CErrorWindow(ex);
                Logger.getLogger(CScriptManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ScriptException ex) {
                CErrorWindow ew = new CErrorWindow(ex);
                Logger.getLogger(CScriptManager.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    r.close();
                } catch (IOException ex) {
                    Logger.getLogger(CScriptManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }
    
    public static IModule GetCallbacks(ICentralClass c) {

        String extensionPath = c.GetAbsoluteLobbyFolderPath() + "scripting/";
        ScriptEngineManager s = new ScriptEngineManager();
        ScriptEngine engine = s.getEngineByExtension("py");
        //
        if (engine != null) {
            Reader r = null;
            Reader r2 = null;
            try {
                ScriptContext sc = engine.getContext();
                r = new FileReader(extensionPath+"modules/battlehubconduit.py");
                engine.eval(r,sc);
                r2 = new FileReader(extensionPath+"engine/handlers.py");
                engine.eval(r2,sc);
                
                Invocable invocableEngine = (Invocable) engine;
                // i = invocableEngine.getInterface(IModule.class);
                Object o = invocableEngine.invokeFunction("getBattleHubReturnObject");
                IModule i = invocableEngine.getInterface(o, IModule.class);
                return i;
            } catch (IOException ex) {
                Logger.getLogger(CScriptManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(CScriptManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ScriptException ex) {
                CErrorWindow ew = new CErrorWindow(ex);
                Logger.getLogger(CScriptManager.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if(r != null){
                        r.close();
                    }
                    if(r2 != null){
                        r2.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(CScriptManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }
    
}
