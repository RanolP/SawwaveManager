package io.github.goldbigdragon.sawwavemanager;

import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Function extends Thread {
    public static long count = 1;
    public static String StartTime = null;

    public void ResearchStart() {
        //검색 도중에 사용자가 옵션을 변경하지 못하도록 비활성화 시킴.
        Main.mySqlID.setEnabled(false);
        Main.mySqlPassword.setEnabled(false);
        Main.mySqlAddress.setEnabled(false);
        Main.mySqlPort.setEnabled(false);
        Main.PC_IP_Client.setEnabled(false);
        Main.PC_IP_Server.setEnabled(false);
        Main.Web_IP_Client.setEnabled(false);
        Main.Web_IP_Server.setEnabled(false);
        Main.Web_ID_Client.setEnabled(false);
        Main.Web_ID_Server.setEnabled(false);
        Main.Web_PW_Client.setEnabled(false);
        Main.Web_PW_Server.setEnabled(false);
        Main.Distance.setEnabled(false);

        //검색 도중에 사용자가 검색을 중단 시킬 수 있도록 [검색]을 [중지]로 변경
        Main.StartButton.setText("                                         중지                                         ");
        Main.Information.setText("　　　　　　　　　　사이트 접속 여부 확인 중...　　　　　　　　　　　");

        //검색 시작
        Main.started = true;

        Main.Information.setText("　　　　　　　　　　자료 수집 중...　　　　　　　　　　　");

        StartTime = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss").format(new Date()).toString();
        //쓰레드를 사용하여 검색합니다.
        Thread thread1 = new Thread_OverView();
        Thread thread2 = new Thread_Network();
        thread1.start();
        thread2.start();
    }

    public boolean isPingWell() {
        try {
            return InetAddress.getByName(Main.PC_IP_Server.getText()).isReachable(2000);
        } catch (IOException e) {
            return false;
        }
    }


    public void exit(String Message) {

        //[검색 중] 라벨에 검색이 완료되었다고 알림
        if (Message == null)
            Main.Information.setText("　　　　　　　　　　　설정 이후, 시작 버튼을 클릭 해 주세요.　　　　　　　　　　　");
        else
            Main.Information.setText(Message);

        //검색 완료 이후, 다시 사용자가 설정을 할 수 있도록 옵션 활성화
        Main.mySqlID.setEnabled(true);
        Main.mySqlPassword.setEnabled(true);
        Main.mySqlAddress.setEnabled(true);
        Main.mySqlPort.setEnabled(true);
        Main.PC_IP_Client.setEnabled(true);
        Main.PC_IP_Server.setEnabled(true);
        Main.Web_IP_Client.setEnabled(true);
        Main.Web_IP_Server.setEnabled(true);
        Main.Web_ID_Client.setEnabled(true);
        Main.Web_ID_Server.setEnabled(true);
        Main.Web_PW_Client.setEnabled(true);
        Main.Web_PW_Server.setEnabled(true);
        Main.Distance.setEnabled(true);
        //검색 완료 이후, [중지]버튼을 다시 [검색]으로 변경
        Main.StartButton.setText("                                         시작                                         ");
    }
}
