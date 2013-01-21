/*
 * To change this template, choose Tools | Templates | Licenses | Default License
 * and open the template in the editor.
 */

package org.darkstars.battlehub;

import org.darkstars.battlehub.framework.CStringHelper;
import org.darkstars.battlehub.framework.Misc;

/**
 *
 * @author tarendai
 */
public class CTextColours {
    private static boolean loaded=false;
//    private static HashMap<String,Color> textcolours = new HashMap<String,Color>();
    
//    public static final String[] names = {
//        "action chat text","chat text", "usernames", "user joined", "user left", "channel message",
//        "system message", "channel topic", "timestamps", "background"
//    };

    /*
     * Use this method to initialize the values and load the text values from
     * the settings.tdf file
     */
    public static void load(){
//        textcolours.clear();
//        for(int i = 0; i < names.length; i++){
//            String k = names[i];
//            String value = CUserSettings.GetValue("textcolours."+k);
//            if(value == null){
//                continue;
//            }
//            if(value.equals("")){
//                continue;
//            }
//            Color c = Color.decode(value);
//            textcolours.put(k, c);
//        }
//        loaded=true;
    }
    
    /* 
     * Call this method to restore the saved settings to those stored in here
     */
    public static void restore(){
        //
    }
    
    public static String GetColor(String value, String defaultvalue){
        if(!loaded){
            load();
        }
        String s = CUserSettings.GetValue("textcolours."+value, defaultvalue);
        if(s == null){
            s = defaultvalue;
        }else if (s.trim().equals("")){
            //
            s = defaultvalue;
        }
        return s;
        //try{
            //Color c = Color.decode(s);
            //return c;
        //}catch(Exception e){
            //
        //    return Color.black;
        //}
    }
    
    public static String getHTMLTimestamp(){
        return "<font face=\"Arial, Helvetica, sans-serif\" size=\"3\" color=\"" + CTextColours.GetColor("timestamps", "#aaaaaa") + "\">" + CStringHelper.getTimestamp() + "</font>";
    }
    
    public static String getUserChat(String user){
        if(CUserSettings.GetValue("ui.chat.says","true").equalsIgnoreCase("true")){
            return user +" "+java.util.ResourceBundle.getBundle("org.darkstars.battlehub.languages").getString("CPrivateMessageWindow._says:");
        }else{
            return user+" ";
            
        }
        //
    }
    
    public static String getUserChatHTML(String user){
        String c = CTextColours.GetColor("usernames", "#000000");
        return "<font face=\"Arial, Helvetica, sans-serif\" size=\"3\" color=\""+c +"\"><b><i>"+getUserChat(user)+"</i></b></font>";
    }
    
    public static String getChatHTML(String message, boolean action){
        String c = " color=\"";
        if(action){
            c += CTextColours.GetColor("action-chat-text", "#0000ff");
        } else {
            c += CTextColours.GetColor("chat-text", "#000000");
        }
        c += "\"";
        String m = "<font face=\"Arial, Helvetica, sans-serif\" size=\"3\" "+c+" >" +Misc.toHTML(message)+"</font>";
        if(action){
            m = "<b>"+m + "</b>";
        }
        return m;
    }
}
