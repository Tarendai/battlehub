/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.darkstars.battlehub;

import org.darkstars.battlehub.battlemodels.IBattleModel;

/**
 *
 * @author tarendai-std
 */
public interface IGameModel {
    
    /**
     * 
     * @param i
     * @return
     */
    public abstract IBattleModel CreateBattleModel(CBattleInfo i);
    
    /**
     * 
     * @return
     */
    public abstract String GetName();
    
    /**
     * 
     * @return
     */
    public abstract String GetDescription();
    
    /**
     * 
     * @return
     */
    public abstract String GetEngine();
}
