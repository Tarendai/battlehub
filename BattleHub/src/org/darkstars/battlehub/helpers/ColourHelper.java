/*
 * ColourHelper.java
 *
 * Created on 27 December 2006, 14:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.darkstars.battlehub.helpers;

import java.awt.Color;

/**
 *
 * @author AF
 */
public class ColourHelper {
    // BGR

    /**
     * Creates a new instance of ColourHelper
     */
    public ColourHelper() {
    }

    /**
     * 
     * @param x
     * @return
     */
    public static Color IntegerToColor(int x) {
        int[] bytes = new int[4];
        
        bytes[0] = ((x & 0xff000000) >> 24);
        bytes[1] = ((x & 0xff0000) >> 16);
        
        if (bytes[1] < 0) {
            bytes[1] *= -1;
        }
        
        bytes[2] = ((x & 0xff00) >> 8);
        if (bytes[2] < 0) {
            bytes[2] *= -1;
        }

        bytes[3] = ((x & 0xff));
        
        if (bytes[3] < 0) {
            bytes[3] *= -1;
        }
        
        return new Color(bytes[3], bytes[2], bytes[1]);
    }

    /**
     * 
     * @param c
     * @return
     */
    public static Integer ColorToInteger(Color c) {
        int i = 0;
        int r = ((c.getRed()) & 0xff);
        i |= r;
        
        i |= (c.getGreen() << 8) | (c.getBlue() << 16);
        return i;
    }

    /**
     * 
     * @param c
     * @return
     */
    public static String ColourToHex(Color c) {
        String s = "#";
        String t = Integer.toHexString(c.getRGB());
        return s + t.substring(2);
    }
    
}
