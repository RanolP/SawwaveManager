package io.github.goldbigdragon.sawwavemanager;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.swing.table.DefaultTableModel;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ObjectTruck {
    private String type = null;
    private String txPower = null;
    private String bandwidth = null;
    private String channel = null;
    private String rssi = null;
    private String txRate = null;
    private String rxRate = null;
    private String ccq = null;
    private boolean readyOverview = false;
    private boolean readyNetwork = false;

    private DefaultTableModel model = (DefaultTableModel) Main.table.getModel();

    public String getType() {
        return type;
    }

    public String getTxPower() {
        return txPower;
    }

    public String getBandwidth() {
        return bandwidth;
    }

    public String getChannel() {
        return channel;
    }

    public String getRssi() {
        return rssi;
    }

    public String getTxRate() {
        return txRate;
    }

    public String getRxRate() {
        return rxRate;
    }

    public String getCcq() {
        return ccq;
    }

    public boolean isReadyOverview() {
        return readyOverview;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTxPower(String txPower) {
        this.txPower = txPower;
    }

    public void setBandwidth(String bandwidth) {
        this.bandwidth = bandwidth;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setRssi(String rSSI) {
        rssi = rSSI;
    }

    public void setTxRate(String txRate) {
        this.txRate = txRate;
    }

    public void setRxRate(String rxRate) {
        this.rxRate = rxRate;
    }

    public void setCcq(String cCQ) {
        ccq = cCQ;
    }

    public void setReadyOverview(boolean readyOverview) {
        this.readyOverview = readyOverview;
        isAllReady();
    }

    public boolean isReadyNetwork() {
        return readyNetwork;
    }

    public void setReadyNetwork(boolean readyNetwork) {
        this.readyNetwork = readyNetwork;
        isAllReady();
    }

    public synchronized void isAllReady() {
        if (readyOverview && readyNetwork) {
            if (Main.sound.isSelected()) {
                try {
                    Player pl = new Player(new FileInputStream("sound\\" + ccq.split("%")[0].trim() + ".mp3"));
                    pl.play();
                    pl.close();
                } catch (FileNotFoundException | JavaLayerException exception) {
                }
            }
            notifyAll();
            readyOverview = false;
            readyNetwork = false;
            Main.BufferStore.add(new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss").format(new Date()) + "★" + type + "★" + Main.distance.getText() + "★" + txPower + "★" + bandwidth + "★" + channel + "★" + rssi + "★" + txRate + "★" + rxRate + "★" + ccq);
            model.addRow(new Object[]{Function.count, rssi, txRate, rxRate, ccq});
            if (Main.BufferStore.size() >= 20)
                new MySql().dataInsert();

            Function.count++;
        }
    }
}
