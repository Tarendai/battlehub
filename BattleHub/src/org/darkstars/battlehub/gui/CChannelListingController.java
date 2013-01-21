/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.darkstars.battlehub.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.SwingUtilities;
import org.darkstars.battlehub.CChannelInfo;
import org.darkstars.battlehub.framework.CEvent;
import org.darkstars.battlehub.framework.ICentralClass;
import org.darkstars.battlehub.framework.IModule;
import org.darkstars.battlehub.framework.Misc;
import org.jvnet.flamingo.common.JCommandButton;
import org.jvnet.flamingo.common.JCommandButton.CommandButtonKind;
import org.jvnet.flamingo.common.icon.ImageWrapperResizableIcon;
import org.jvnet.flamingo.common.icon.ResizableIcon;

/**
 *
 * @author AF-Standard
 */
public class CChannelListingController implements IModule {

    private CChannelListing list = null;
    private ICentralClass L;
    private ArrayList<CChannelInfo> channels = new ArrayList<CChannelInfo>();
    private ArrayList<CChannelInfo> smallChannels = new ArrayList<CChannelInfo>();

    @Override
    public void Init(ICentralClass L) {
        this.L = L;
        setList(new CChannelListing());
    }

    @Override
    public void Update() {
        //
    }

    @Override
    public void NewEvent(CEvent e) {
        //
        if (e.IsEvent("CHANNEL")) {
            int ucount = Integer.parseInt(e.data[2]);
            CChannelInfo c = new CChannelInfo(e.data[1],ucount,Misc.makeSentence(e.data, 3));
            if(ucount > 2){
                channels.add(c);
            }else{
                smallChannels.add(c);
            }

        } else if (e.IsEvent("ENDOFCHANNELS")) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    URL url = this.getClass().getResource("/images/tango/32x32/actions/view-refresh.png");
                    ResizableIcon ri = ImageWrapperResizableIcon.getIcon(url, new Dimension(64, 64));

                    JCommandButton b = new JCommandButton("Refresh List", ri);
                    b.setCommandButtonKind(CommandButtonKind.ACTION_ONLY);

                    b.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            L.GetProtocol().SendTraffic("CHANNELS");
                            list.RemoveAllButtons();
                        }
                    });
                    list.AddActionButton(b);
                }
            });
            {
                Object[] chans = channels.toArray();
                Arrays.sort(chans);
                for (final Object s : chans) {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            list.AddChannel((CChannelInfo) s, L.GetProtocol());
                        }
                    });
                }
                channels.clear();
            }
            
            {
                Object[] chans = smallChannels.toArray();
                Arrays.sort(chans);
                for (final Object s : chans) {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            list.AddChannel((CChannelInfo) s, L.GetProtocol());
                        }
                    });
                }
                smallChannels.clear();
            }
        } else if (e.IsEvent("LOGININFOEND")) {
            L.GetProtocol().SendTraffic("CHANNELS");
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    list.RemoveAllButtons();
                }
            });
        }
    }

    @Override
    public void NewGUIEvent(CEvent e) {
        //
    }

    @Override
    public void OnRemove() {
        //
    }

    public CChannelListing getList() {
        return list;
    }

    public void setList(CChannelListing list) {
        this.list = list;
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
