package io.github.goldbigdragon.sawwavemanager;

import javazoom.jl.player.Player;

import javax.swing.table.DefaultTableModel;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Object_Truck {
    private String Type = null;
    private String Tx_Power = null;
    private String Bandwidth = null;
    private String Channel = null;
    private String RSSI = null;
    private String Tx_Rate = null;
    private String Rx_Rate = null;
    private String CCQ = null;
    private boolean Ready_OverView = false;
    private boolean Ready_Netwrok = false;

    DefaultTableModel model = (DefaultTableModel) Main.table.getModel();

    public String getType() {
        return Type;
    }

    public String getTx_Power() {
        return Tx_Power;
    }

    public String getBandwidth() {
        return Bandwidth;
    }

    public String getChannel() {
        return Channel;
    }

    public String getRSSI() {
        return RSSI;
    }

    public String getTx_Rate() {
        return Tx_Rate;
    }

    public String getRx_Rate() {
        return Rx_Rate;
    }

    public String getCCQ() {
        return CCQ;
    }

    public boolean isReady_OverView() {
        return Ready_OverView;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setTx_Power(String tx_Power) {
        Tx_Power = tx_Power;
    }

    public void setBandwidth(String bandwidth) {
        Bandwidth = bandwidth;
    }

    public void setChannel(String channel) {
        Channel = channel;
    }

    public void setRSSI(String rSSI) {
        RSSI = rSSI;
    }

    public void setTx_Rate(String tx_Rate) {
        Tx_Rate = tx_Rate;
    }

    public void setRx_Rate(String rx_Rate) {
        Rx_Rate = rx_Rate;
    }

    public void setCCQ(String cCQ) {
        CCQ = cCQ;
    }

    public void setReady_OverView(boolean ready_OverView) {
        Ready_OverView = ready_OverView;
        isAllReady();
    }

    public boolean isReady_Netwrok() {
        return Ready_Netwrok;
    }

    public void setReady_Netwrok(boolean ready_Netwrok) {
        Ready_Netwrok = ready_Netwrok;
        isAllReady();
    }

    public synchronized void isAllReady() {
        if (Ready_OverView && Ready_Netwrok) {
            if (Main.Sound.isSelected()) {
                try {
                    Player pl = new Player(new FileInputStream("sound\\" + CCQ.split("%")[0].trim() + ".mp3"));
                    pl.play();
                    pl.close();
                } catch (Exception ef) {
                }
            }
            notifyAll();
            Ready_OverView = false;
            Ready_Netwrok = false;
            Main.BufferStore.add(new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss").format(new Date()).toString() + "★" + Type + "★" + Main.Distance.getText() + "★" + Tx_Power + "★" + Bandwidth + "★" + Channel + "★" + RSSI + "★" + Tx_Rate + "★" + Rx_Rate + "★" + CCQ);
            model.addRow(new Object[]{Function.count, RSSI, Tx_Rate, Rx_Rate, CCQ});
            if (Main.BufferStore.size() >= 20)
                new MySQL().dataInsert();

            Function.count++;
        }
    }
}
