/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.darkstars.battlehub;

import org.darkstars.battlehub.CUserSettings;
import java.util.ArrayList;

/**
 *
 * @author tarendai
 */
public class CContentManager {
    //static String[] supported_engines = {"spring","glest"};
    public static ArrayList<String> installed_engines = new ArrayList<String>();
    
    public static boolean SupportsEngine(String engine){
        //
        String value = CUserSettings.GetValue(engine+".command", "");
        if(value.equals("")){
            return false;
        }/*
        for( String e : supported_engines){
            if(e.equals(engine)){
                return installed_engines.contains(engine);
            }
        }*/
        return true;
        
    }
    
    //public static Object[] GetInstalledEngines(){
    //    return installed_engines.toArray();
    //}
}
